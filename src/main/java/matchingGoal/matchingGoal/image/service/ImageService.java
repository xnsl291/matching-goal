package matchingGoal.matchingGoal.image.service;

import lombok.AllArgsConstructor;
import matchingGoal.matchingGoal.common.exception.CustomException;
import matchingGoal.matchingGoal.common.type.ErrorCode;
import matchingGoal.matchingGoal.image.model.entity.Image;
import matchingGoal.matchingGoal.image.repository.ImageRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.UUID;

@Service
@AllArgsConstructor
public class ImageService {
    private final ImageRepository imageRepository;

    /**
     * 이미지 폴더 위치 반환
     * @param date - 이미지 업로드일
     * @return 폴더 위치 (ex. [BASE_PATH]/2023/01/01/ "
     */
    public String getFolderPath(LocalDate date) {
        String path = System.getProperty("user.dir") + "/src/main/resources/static/images/"
                + date.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))
                + "/";
        return path.replace("/", File.separator);
    }

    /**
     * 이미지 업로드
     * @return 이미지 url
     */
    @Transactional
    public String uploadImage(MultipartFile file) {
        UUID uuid = UUID.randomUUID();
        String path = getFolderPath(LocalDate.now());
        String saveName = uuid + "_" + file.getOriginalFilename();
        String pathName = path + saveName;

        if (!Objects.requireNonNull(file.getContentType()).startsWith("image"))
            throw new CustomException(ErrorCode.INVALID_FILE_FORMAT);

        Path directory = Paths.get(path);

        try {
            if (!Files.exists(directory)) {
                Files.createDirectories(directory);
            }

            // 파일 저장
            file.transferTo(directory.resolve(saveName).toFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Image image = Image.builder()
                .size(file.getSize())
                .savedName(saveName)
                .filePathName(pathName)
                .build();
        imageRepository.save(image);

        // 저장된 파일 url 반환
        return pathName;
    }

    /**
     * 이미지 삭제
     * @return 완료여부
     */
    @Transactional
    public boolean removeImage(String imageUrl) {

        try {
            Path filePath = Paths.get(imageUrl);
            return Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
