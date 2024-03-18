package matchingGoal.matchingGoal.image.service;

import lombok.AllArgsConstructor;
import matchingGoal.matchingGoal.common.auth.JwtTokenProvider;
import matchingGoal.matchingGoal.image.dto.UploadImageResponse;
import matchingGoal.matchingGoal.image.exception.InvalidFileFormatException;
import matchingGoal.matchingGoal.image.exception.NotFoundImageException;
import matchingGoal.matchingGoal.image.model.entity.Image;
import matchingGoal.matchingGoal.image.repository.ImageRepository;
import matchingGoal.matchingGoal.member.exception.MemberNotFoundException;
import matchingGoal.matchingGoal.member.model.entity.Member;
import matchingGoal.matchingGoal.member.repository.MemberRepository;
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
    private final MemberRepository memberRepository;
    private final JwtTokenProvider jwtTokenProvider;

    /**
     * 이미지 폴더 위치 반환
     *
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
     * 이미지 URL 반환
     *
     * @return 이미지 URL
     */
    public String getImageUrl(Long imageId) {
        try {
            Image image = imageRepository.findById(imageId).orElseThrow(NotFoundImageException::new);
            return getFolderPath(image.getCreatedDate().toLocalDate()) + image.getSavedName();
        } catch (RuntimeException e) {
            return null;
        }
    }

    /**
     * 이미지 업로드
     *
     * @return 이미지 url
     */
    @Transactional
    public UploadImageResponse uploadProfileImage(String token, MultipartFile file) {
        jwtTokenProvider.validateToken(token);
        Member member = memberRepository.findById(jwtTokenProvider.getId(token)).orElseThrow(MemberNotFoundException::new);

        UUID uuid = UUID.randomUUID();
        String path = getFolderPath(LocalDate.now());
        String saveName = uuid + "_" + file.getOriginalFilename();
        String pathName = path + saveName;

        if (!Objects.requireNonNull(file.getContentType()).startsWith("image"))
            throw new InvalidFileFormatException();

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
        member.setImageId(image.getId());
        // 저장된 파일 url 반환
        return UploadImageResponse.builder().imageId(image.getId()).imageUrl(image.getFilePathName()).build();
    }

    @Transactional
    public boolean removeProfileImage(String token) {
        jwtTokenProvider.validateToken(token);
        Member member = memberRepository.findById(jwtTokenProvider.getId(token)).orElseThrow(MemberNotFoundException::new);

        Long imageId = member.getImageId();

        String imagePath = getImageUrl(imageId);

        System.out.println(">>>>" + member.getNickname() + " " + imageId + " " + imagePath);


        try {
            member.setImageId(null);

            // 파일 삭제
            Path filePath = Paths.get(imagePath);
            return Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

//    public String uploadBoardImage(MultipartFile file) {
//        return "";
//    }

//    public boolean removeImage(Long imageId) {
//        return false;
//    }
}
