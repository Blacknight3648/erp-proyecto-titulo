/**
 * Validations for Chilean business rules.
 */

/** -------------------------
 * RUT / RUN
 * ------------------------- */
export const cleanRut = (rut) => {
    if (!rut) return "";
    return rut.replace(/[^0-9kK]/g, "").toUpperCase();
};

export const validateRUN = (run) => {
    if (!run || typeof run !== 'string') return false;
    const clean = cleanRut(run);
    if (clean.length < 8) return false;

    const body = clean.slice(0, -1);
    const dv = clean.slice(-1);

    let sum = 0, multiplier = 2;
    for (let i = body.length - 1; i >= 0; i--) {
        sum += parseInt(body[i], 10) * multiplier;
        multiplier = multiplier === 7 ? 2 : multiplier + 1;
    }

    const expected = 11 - (sum % 11);
    const expectedDv = expected === 11 ? '0' : expected === 10 ? 'K' : expected.toString();
    return dv === expectedDv;
};

export const validateRut = validateRUN; // alias for compatibility

export const formatRUN = (run) => {
    const clean = cleanRut(run);
    if (clean.length < 2) return clean;
    const body = clean.slice(0, -1);
    const dv = clean.slice(-1);
    let formatted = '';
    for (let i = body.length - 1, j = 1; i >= 0; i--, j++) {
        formatted = body[i] + formatted;
        if (j % 3 === 0 && i !== 0) formatted = '.' + formatted;
    }
    return `${formatted}-${dv}`;
};

/** -------------------------
 * Email
 * ------------------------- */
export const cleanEmail = (email) => {
    if (!email) return null;
    const trimmed = email.trim();
    return trimmed === "" ? null : trimmed.toLowerCase();
};

export const validateEmail = (email) => {
    if (!email || typeof email !== 'string') return false;
    return /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,10}$/.test(email);
};

/** -------------------------
 * Phone
 * ------------------------- */
export const cleanPhone = (phone) => {
    if (!phone) return "";
    let clean = phone.replace(/[^\d+]/g, "");
    if (clean.startsWith("56") && !clean.startsWith("+56")) clean = `+${clean}`;
    return clean;
};

if (name === "telefonoProveedor") {
  // Limitar solo 9 dígitos
  const onlyNumbers = value.replace(/\D/g, "");
  if (onlyNumbers.length > 9) {
    newErrors[name] = "No se pueden ingresar más de 9 dígitos";
  } else {
    const phoneError = validatePhone(onlyNumbers);
    if (phoneError) newErrors[name] = phoneError;
    else delete newErrors[name];
  }
}

// validations.js
export const validatePhone = (phone) => {
  if (!phone) return null; // campo opcional
  const clean = phone.replace(/\D/g, ""); // solo números
  if (clean.length < 8 || clean.length > 9) {
    return "Teléfono debe tener 8 o 9 dígitos";
  }
  return null;
};

/** -------------------------
 * Age
 * ------------------------- */
export const validateAge = (birthDate) => {
    if (!birthDate) return false;
    const today = new Date();
    const birth = new Date(birthDate);
    if (isNaN(birth.getTime())) return false;
    let age = today.getFullYear() - birth.getFullYear();
    const monthDiff = today.getMonth() - birth.getMonth();
    if (monthDiff < 0 || (monthDiff === 0 && today.getDate() < birth.getDate())) age--;
    return age >= 18;
};

/** -------------------------
 * Numbers / Prices / Stock
 * ------------------------- */
export const validateNumber = (value, allowDecimal = true) => {
    if (typeof value === 'number') return allowDecimal ? value >= 0 : Number.isInteger(value) && value >= 0;
    if (typeof value === 'string') {
        const num = parseFloat(value);
        return !isNaN(num) && num >= 0;
    }
    return false;
};

export const validatePrice = (price) => validateNumber(price);
export const validateStock = (stock) => validateNumber(stock, false);

export const validateProductCode = (code) => typeof code === 'string' && code.trim().length >= 3;
export const validateAddress = (address) => typeof address === 'string' && address.trim().length > 5;

export const validateNumericInput = (value, fieldName) => {
    const num = parseFloat(value);
    if (!isNaN(num) && num < 0) return `El campo ${fieldName}, se esta introduciendo un numero negativo favor corregir y volver a intentar`;
    return null;
};

/** -------------------------
 * Price formatting
 * ------------------------- */
export const formatPrice = (price) => {
    if (price === 0) return 'FREE';
    if (typeof price !== 'number') return '';
    return `$${price.toLocaleString('es-CL')}`;
};