package matchingGoal.matchingGoal.common.annotation.valid;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import matchingGoal.matchingGoal.common.annotation.Password;

public class PasswordValidator implements ConstraintValidator<Password,String> {
    @Override
    public boolean isValid(String string, ConstraintValidatorContext constraintValidatorContext) {
        if (string == null) {
            return false;
        }

        return string.matches("^(?=.*[A-Za-z가-힣])(?=.*\\d)[A-Za-z\\d!@#$%^&*]{10,}$");
    }
}
