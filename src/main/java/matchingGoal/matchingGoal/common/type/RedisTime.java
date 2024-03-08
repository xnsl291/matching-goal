package matchingGoal.matchingGoal.common.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RedisTime{

    MAIL_EXPIRE_IN_MINUTES(3),


    ;


    private final int time ;

}
