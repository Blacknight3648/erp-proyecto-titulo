import { describe, it, expect } from 'vitest';
import {
    cleanRut,
    validateRUN,
    validateRut,
    formatRUN,
    validateEmail,
    validatePhone,
    validateNumber,
    validatePrice,
    validateStock,
    validateNumericInput,
    validateAge,
    formatPrice,
} from './validations';

describe('validateRUN / validateRut', () => {
    it('acepta un RUN válido limpio y formateado', () => {
        expect(validateRUN('123456785')).toBe(true);
        expect(validateRUN('12.345.678-5')).toBe(true);
    });

    it('acepta dígito verificador K', () => {
        expect(validateRUN('6.000.000-K')).toBe(true);
        expect(validateRUN('6000000k')).toBe(true); // case-insensitive
    });

    it('rechaza DV incorrecto, demasiado corto o no-string', () => {
        expect(validateRUN('12.345.678-9')).toBe(false);
        expect(validateRUN('123')).toBe(false);
        expect(validateRUN(123456785)).toBe(false); // no es string
        expect(validateRUN(null)).toBe(false);
        expect(validateRUN('')).toBe(false);
    });

    it('validateRut es alias de validateRUN', () => {
        expect(validateRut).toBe(validateRUN);
    });
});

describe('cleanRut', () => {
    it('deja solo dígitos y K en mayúscula', () => {
        expect(cleanRut('12.345.678-k')).toBe('12345678K');
        expect(cleanRut('  9.876.543-2 ')).toBe('98765432');
        expect(cleanRut('')).toBe('');
        expect(cleanRut(null)).toBe('');
    });
});

describe('formatRUN', () => {
    it('formatea con puntos y guion', () => {
        expect(formatRUN('123456785')).toBe('12.345.678-5');
        expect(formatRUN('6000000K')).toBe('6.000.000-K');
    });

    it('no formatea entradas demasiado cortas', () => {
        expect(formatRUN('5')).toBe('5');
    });
});

describe('validateEmail', () => {
    it('acepta correos válidos', () => {
        expect(validateEmail('user@dominio.cl')).toBe(true);
        expect(validateEmail('a.b+c@sub.dominio.com')).toBe(true);
    });

    it('rechaza correos inválidos', () => {
        expect(validateEmail('sin-arroba.cl')).toBe(false);
        expect(validateEmail('user@')).toBe(false);
        expect(validateEmail('')).toBe(false);
        expect(validateEmail(null)).toBe(false);
    });
});

describe('validatePhone', () => {
    it('acepta 8 o 9 dígitos y campo vacío (opcional)', () => {
        expect(validatePhone('12345678')).toBeNull();
        expect(validatePhone('123456789')).toBeNull();
        expect(validatePhone('')).toBeNull();
        expect(validatePhone(null)).toBeNull();
    });

    it('rechaza longitudes fuera de rango', () => {
        expect(validatePhone('1234567')).toMatch(/8 o 9 dígitos/);
        expect(validatePhone('1234567890')).toMatch(/8 o 9 dígitos/);
    });
});

describe('validateNumber / validatePrice / validateStock', () => {
    it('valida números no negativos', () => {
        expect(validateNumber(5)).toBe(true);
        expect(validateNumber('3.5')).toBe(true);
        expect(validateNumber(-1)).toBe(false);
        expect(validateNumber('abc')).toBe(false);
        expect(validatePrice(0)).toBe(true);
    });

    it('validateStock exige enteros no negativos', () => {
        expect(validateStock(3)).toBe(true);
        expect(validateStock(2.5)).toBe(false);
        expect(validateStock(-2)).toBe(false);
    });
});

describe('validateNumericInput', () => {
    it('devuelve mensaje con el campo ante negativos', () => {
        const msg = validateNumericInput('-5', 'Precio');
        expect(msg).toContain('Precio');
    });

    it('devuelve null para valores válidos o no numéricos', () => {
        expect(validateNumericInput('5', 'Cantidad')).toBeNull();
        expect(validateNumericInput('abc', 'Cantidad')).toBeNull();
    });
});

describe('validateAge', () => {
    const hace = (anios) => {
        const d = new Date();
        d.setFullYear(d.getFullYear() - anios);
        return d.toISOString().slice(0, 10);
    };

    it('acepta mayores de edad y rechaza menores/ inválidos', () => {
        expect(validateAge(hace(20))).toBe(true);
        expect(validateAge(hace(10))).toBe(false);
        expect(validateAge('fecha-invalida')).toBe(false);
        expect(validateAge(null)).toBe(false);
    });
});

describe('formatPrice', () => {
    it('formatea CLP, FREE y entradas no numéricas', () => {
        expect(formatPrice(0)).toBe('FREE');
        expect(formatPrice(1000)).toBe('$1.000');
        expect(formatPrice('x')).toBe('');
    });
});
