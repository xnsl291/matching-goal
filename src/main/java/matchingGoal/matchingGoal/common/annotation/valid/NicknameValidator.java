package matchingGoal.matchingGoal.common.annotation.valid;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import matchingGoal.matchingGoal.common.annotation.Nickname;

public class NicknameValidator implements ConstraintValidator<Nickname,String> {
    @Override
    public boolean isValid(String string, ConstraintValidatorContext constraintValidatorContext) {
        if (string == null) {
            return false;
        }

        return string.matches("^[a-zA-Z0-9가-힣]{2,20}$");
    }
}
