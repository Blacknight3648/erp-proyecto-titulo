package backend.com.shared.validations.telefono;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PhoneValidator implements ConstraintValidator<ValidPhone, String> {

    private static final String PHONE_REGEX =
            "^(\\+56)?\\s?0?(9\\d{8}|[2-7]\\d{7,8})$";

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {

        if (value == null || value.isBlank())
            return false;

        // limpiar espacios y guiones
        String clean = value.replaceAll("[\\s-]", "");

        return clean.matches(PHONE_REGEX);
    }
}
