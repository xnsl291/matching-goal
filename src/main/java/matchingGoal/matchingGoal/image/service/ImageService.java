package matchingGoal.matchingGoal.image.service;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import matchingGoal.matchingGoal.common.exception.CustomException;
import matchingGoal.matchingGoal.common.type.ErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class ImageService {
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    /**
     * 파일명 생성
     * @return 년/월/일/UUID_파일명
     */
    private String generateFileName(String name) {
        //년/월/일/
        String path = LocalDate.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd")) + "/";

        // S3에 저장된 파일 이름
        return path + UUID.randomUUID() + "_" + name;
    }

    /**
     * 이미지 업로드
     * @return 빈 파일: "" , 이미지 파일 : url
     */
    public String uploadImage(MultipartFile uploadFile) {
        if (uploadFile.isEmpty()) // 파일이 비었을 경우
            return "";
        if (!Objects.requireNonNull(uploadFile.getContentType()).startsWith("image"))
            throw new CustomException(ErrorCode.INVALID_FILE_FORMAT);

        // S3에 저장할 파일 이름
        String fileName = generateFileName(uploadFile.getOriginalFilename());

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(uploadFile.getSize());
        metadata.setContentType(uploadFile.getContentType());

        try (InputStream inputStream = uploadFile.getInputStream()) {
            // s3로 업로드
            amazonS3Client.putObject(
                    new PutObjectRequest(bucket, fileName, inputStream, metadata)  // PublicRead 권한으로 upload
							.withCannedAcl(CannedAccessControlList.PublicRead));

        } catch (IOException | AmazonS3Exception e) {
            throw new RuntimeException("파일 업로드 중 오류 발생: " + e.getMessage());
        }
        return amazonS3Client.getUrl(bucket, fileName).toString(); // File의 URL return

    }

    // 이미지 삭제
    public void removeImage(String fileUrl) {
        String splitStr = ".com/";
        String fileName = fileUrl.substring(
                fileUrl.lastIndexOf(splitStr) + splitStr.length());
        amazonS3Client.deleteObject(new DeleteObjectRequest(bucket, fileName));
    }
}
