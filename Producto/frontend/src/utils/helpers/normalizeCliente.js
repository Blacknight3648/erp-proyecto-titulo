import { cleanRut, cleanPhone, cleanEmail } from "../validations";

export const normalizeCliente = (cliente = {}) => {

  const normalizeName = (name) =>
    name
      ? name
          .trim()
          .split(" ")
          .filter((w) => w.length > 0)
          .map(
            (w) => w[0].toUpperCase() + w.slice(1).toLowerCase()
          )
          .join(" ")
      : "";

  return {
    ...cliente,

    nombreCliente: normalizeName(cliente.nombreCliente),
    apellidoCliente: normalizeName(cliente.apellidoCliente),

    runCliente: cleanRut(cliente.runCliente),

    telefonoCliente: cleanPhone(cliente.telefonoCliente),

    correoCliente: cleanEmail(
      cliente.correoCliente || cliente.emailCliente
    ),
  };
};