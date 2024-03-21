package matchingGoal.matchingGoal.image.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UploadImageResponse {
    private Long imageId;

    private String imageUrl;

}
