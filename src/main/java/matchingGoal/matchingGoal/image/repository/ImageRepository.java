package matchingGoal.matchingGoal.image.repository;

import matchingGoal.matchingGoal.image.model.entity.Image;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ImageRepository extends JpaRepository<Image, Long> {

}
