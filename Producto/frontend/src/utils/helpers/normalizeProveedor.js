import { cleanRut, cleanPhone, cleanEmail } from "../validations";

export const normalizeProveedor = (proveedor = {}) => {
  const normalizeName = (name) => 
    name ? name.trim().split(" ").filter(w => w.length > 0).map(w => w[0].toUpperCase() + w.slice(1).toLowerCase()).join(" ") : "";

  return {
    ...proveedor,
    nombreProveedor: normalizeName(proveedor.nombreProveedor),
    rutProveedor: cleanRut(proveedor.rutProveedor),
    telefonoProveedor: cleanPhone(proveedor.telefonoProveedor),
    emailProveedor: cleanEmail(proveedor.emailProveedor),
    categoria: normalizeName(proveedor.categoria) || "VARIOS",
    direccionProveedor: proveedor.direccionProveedor?.trim() || "SIN DIRECCION"
  };
};