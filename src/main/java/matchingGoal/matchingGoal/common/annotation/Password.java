package matchingGoal.matchingGoal.common.annotation;

import jakarta.validation.Constraint;
import matchingGoal.matchingGoal.common.annotation.valid.PasswordValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = PasswordValidator.class)
public @interface Password {
    String message() default "비밀번호는 10자 이상, 최소 하나의 문자열과 특수문자가 포함되어야 합니다";
}
