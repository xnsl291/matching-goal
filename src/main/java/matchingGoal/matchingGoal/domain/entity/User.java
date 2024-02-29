package matchingGoal.matchingGoal.domain.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.web.WebProperties.Resources.Chain.Strategy;

@Entity
@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class User {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private long id;

  private String nickname;

}
