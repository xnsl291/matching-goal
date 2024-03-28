package matchingGoal.matchingGoal.matching.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CommentDto {

  @NotNull(message = "평점을 입력해주세요")
  @Range(min = 0, max = 10, message = "0 이상 10 이하의 숫자를 입력해주세요")
  private int score;

  @NotBlank(message = "한줄평을 입력해주세요")
  @Length(max = 50, message = "50자 이내로 입력해주세요")
  private String content;

}