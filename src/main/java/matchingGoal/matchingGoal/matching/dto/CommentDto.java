package matchingGoal.matchingGoal.matching.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDto {

  @NotNull(message = "평점을 입력해주세요")
  @Max(value = 10, message = "10 이하의 숫자를 입력해주세요")
  @Min(value = 0, message = "0 이상의 숫자를 입력해주세요")
  private Integer score;

  @NotBlank(message = "한줄평을 입력해주세요")
  private String content;

}