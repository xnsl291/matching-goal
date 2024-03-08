package matchingGoal.matchingGoal.member.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Member{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @Column(unique = true)
    private String email;

    private Long imgId;

    private String name;

    private String password;

    private String nickname;

    private String introduction;

    private String region;

    @CreatedDate
    private LocalDateTime createdDate;

    @Builder.Default
    private boolean isDeleted = false;

    private LocalDateTime deletedDate;

}
