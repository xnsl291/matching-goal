package matchingGoal.matchingGoal.matching.repository;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import matchingGoal.matchingGoal.matching.domain.entity.MatchingBoard;
import matchingGoal.matchingGoal.matching.exception.IllegalSearchTypeException;
import matchingGoal.matchingGoal.member.model.entity.Member;
import org.springframework.data.jpa.domain.Specification;

public class MatchingBoardSpecification {

  public static Specification<MatchingBoard> search(String keyword, String type, LocalDate parsedDate, LocalTime parsedTime) {
    return ((root, query, criteriaBuilder) -> {
      List<Predicate> predicates = new ArrayList<>();

      if (parsedDate != null) {
        predicates.add(criteriaBuilder.equal(root.get("game").get("date"), parsedDate));
      }
      if (parsedTime != null) {
        predicates.add(criteriaBuilder.equal(root.get("game").get("time"), parsedTime));
      }

      if (keyword != null && !keyword.isEmpty()) {
        switch (type) {
          case "title":
            predicates.add(criteriaBuilder.like(root.get("title"), "%" + keyword + "%"));
            break;
          case "writer":
            Join<MatchingBoard, Member> memberJoin = root.join("member");
            predicates.add(memberJoin.get("nickname").in(keyword));
            break;
          case "region":
            predicates.add(criteriaBuilder.like(root.get("region"), "%" + keyword + "%"));
            break;
          default:
            throw new IllegalSearchTypeException();
        }
      }

      predicates.add(criteriaBuilder.isFalse(root.get("isDeleted")));

      return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
    });
  }


}
