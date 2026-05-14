import { cleanRut, cleanPhone, cleanEmail } from "../validations";

export const normalizeProveedor = (proveedor = {}) => {
  const normalizeName = (name) =>
    name
      ? name
          .trim()
          .split(" ")
          .filter((w) => w.length > 0)
          .map((w) => w[0].toUpperCase() + w.slice(1).toLowerCase())
          .join(" ")
      : "";

  return {
    ...proveedor,
    razonSocialProveedor: normalizeName(proveedor.razonSocialProveedor),
    runProveedor: cleanRut(proveedor.runProveedor),
    telefonoProveedor: cleanPhone(proveedor.telefonoProveedor),
    emailProveedor: cleanEmail(proveedor.emailProveedor),
    tipoProveedor: normalizeName(proveedor.tipoProveedor),
    contactoProveedor: proveedor.contactoProveedor?.trim() || "",
    direccionProveedor: proveedor.direccionProveedor?.trim() || "",
  };
};
