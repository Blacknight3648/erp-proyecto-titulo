package backend.com.shared.validations.run;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = RunValidator.class)
public @interface ValidRun {
    String message() default "RUN invÃ¡lido";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
