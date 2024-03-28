package matchingGoal.matchingGoal.image.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import matchingGoal.matchingGoal.image.service.ImageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/images")
public class ImageController {
    private final ImageService imageService;

    /**
     * 이미지 업로드
     * @return imageUrl
     */
    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage( @Valid @RequestParam("file") MultipartFile file){
        return ResponseEntity.ok().body(imageService.uploadImage(file));
    }

    /**
     * 이미지 삭제
     * @return "삭제완료"
     */
    @DeleteMapping("/remove")
    public ResponseEntity<String> removeImage(@Valid @RequestParam("imageUrl") String imageUrl){
        imageService.removeImage(imageUrl);
        return ResponseEntity.ok().body("삭제완료");
    }
}
