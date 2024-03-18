package matchingGoal.matchingGoal.image.service;

import lombok.AllArgsConstructor;
import matchingGoal.matchingGoal.image.exception.NotFoundImageException;
import matchingGoal.matchingGoal.image.model.entity.Image;
import matchingGoal.matchingGoal.image.repository.ImageRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@AllArgsConstructor
public class ImageService {
    private final String BASE_PATH = "src/main/resources/static/images/";
    private final ImageRepository imageRepository;

    /**
     * 이미지 폴더 위치 반환
     * @param date - 이미지 업로드일
     * @return 폴더 위치 (ex. [BASE_PATH]/2023/01/01/ "
     */
    public String getFolderPath(LocalDateTime date){
        return BASE_PATH
                + date.format(DateTimeFormatter.ofPattern("yyyy/MM/dd"))
                + "/";
    }

    /**
     * 이미지 URL 반환
     * @param id - 이미지 ID
     * @return 이미지 URL
     */
    public String getImageUrl(Long id){
        try{
            Image image = imageRepository.findById(id).orElseThrow(NotFoundImageException::new);
            return getFolderPath(image.getCreatedDate()) + image.getSaved_name();
        }catch (RuntimeException e){
            return null;
        }
    }
}
