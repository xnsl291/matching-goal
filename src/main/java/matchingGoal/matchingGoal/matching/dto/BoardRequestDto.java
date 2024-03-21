package matchingGoal.matchingGoal.matching.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BoardRequestDto {

  private Long memberId;

  private List<String> imageUrls;
  
  @NotBlank(message = "지역을 입력해주세요")
  private String region;
  
  @NotBlank(message = "경기장을 입력해주세요")
  private String stadium;
  
  @Pattern(regexp = "^\\d{4}-(0[1-9]|1[012])-(0[1-9]|[12][0-9]|3[01])$")
  private String date;

  @Pattern(regexp = "^(0[0-9]|1[0-9]|2[0-3]):(0[1-9]|[0-5][0-9])$")
  private String time;
  
  @NotBlank(message = "제목을 입력해주세요")
  private String title;
  
  @NotBlank(message = "내용을 입력해주세요")
  private String content;

}