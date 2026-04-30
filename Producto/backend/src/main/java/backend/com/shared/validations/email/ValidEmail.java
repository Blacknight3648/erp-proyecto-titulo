package backend.com.shared.validations.email;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
// Local EmailValidator in the same package will be used by default

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = EmailValidator.class)
public @interface ValidEmail {

    String message() default "Correo electrÃ³nico invÃ¡lido";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
