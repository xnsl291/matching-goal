package matchingGoal.matchingGoal.matching.dto;

import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateBoardDto {

  @NotBlank(message = "제목을 입력해주세요")
  private String title;
  
  @NotBlank(message = "내용을 입력해주세요")
  private String content;

  private List<Long> imgList;

}
