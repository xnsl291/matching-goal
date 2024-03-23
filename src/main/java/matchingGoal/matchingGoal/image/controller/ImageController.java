package matchingGoal.matchingGoal.image.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import matchingGoal.matchingGoal.image.dto.UploadImageResponse;
import matchingGoal.matchingGoal.image.service.ImageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/images")
public class ImageController {
    private final ImageService imageService;
    private static final String AUTH_HEADER = "Authorization";

    @PostMapping("/upload")
    public ResponseEntity<String> uploadImage( @Valid @RequestParam("file") MultipartFile file){
        return ResponseEntity.ok().body(imageService.uploadImage(file));
    }

    @DeleteMapping("/remove")
    public ResponseEntity<Boolean> removeImage(@Valid @RequestParam("imageUrl") String imageUrl){
        return ResponseEntity.ok().body(imageService.removeImage(imageUrl));
    }
}
