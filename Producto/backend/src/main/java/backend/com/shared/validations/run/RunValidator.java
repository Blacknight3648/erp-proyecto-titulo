package backend.com.shared.validations.run;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RunValidator implements ConstraintValidator<ValidRun, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null || value.isBlank())
            return false;

        // Limpiar para validación interna pero permitir formatos variados:
        // Acepta: 76120450-8, 76.120.450-8, 761204508, etc.
        String clean = value.replace(".", "").replace("-", "").toUpperCase();

        // Formato básico después de limpiar: 8 o 9 caracteres (7 u 8 números + 1 DV)
        if (!clean.matches("^\\d{7,8}[\\dkK]$"))
            return false;

        String cuerpo = clean.substring(0, clean.length() - 1);
        char dv = clean.charAt(clean.length() - 1);

        // Calcular DV
        int m = 0, s = 1;
        for (int i = cuerpo.length() - 1; i >= 0; i--) {
            s = (s + Character.getNumericValue(cuerpo.charAt(i)) * (9 - m++ % 6)) % 11;
        }

        char dvEsperado = (char) (s != 0 ? s + 47 : 75); // 75 = 'K'

        return dv == dvEsperado || dv == (char) (dvEsperado + 32); // acepta k minÃºscula
    }
}
