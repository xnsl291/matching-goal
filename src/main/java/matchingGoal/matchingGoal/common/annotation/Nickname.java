package matchingGoal.matchingGoal.common.annotation;

import jakarta.validation.Constraint;
import matchingGoal.matchingGoal.common.annotation.valid.NicknameValidator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = NicknameValidator.class)
public @interface Nickname {
    String message() default "닉네임은 2-20자 이내로 특수문자를 포함하지 않아야 합니다";
}
