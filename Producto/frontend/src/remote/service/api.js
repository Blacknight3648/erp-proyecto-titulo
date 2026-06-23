import axios from "axios";

export const BACKEND_URL = import.meta.env.VITE_API_URL || "http://127.0.0.1:8050";

export const api = axios.create({
  baseURL: `${BACKEND_URL}/api/v1`,
});

const IDEMPOTENT_METHODS = new Set(["post", "patch", "put"]);

// Genera un Idempotency-Key automático en POST/PATCH/PUT si el caller no envió uno propio.
// Esto protege contra doble-click, retries automáticos y race conditions de UI.
// Si una llamada concreta quiere reusar un token entre reintentos manuales, debe
// pasarlo explícitamente: api.post(url, body, { headers: { 'Idempotency-Key': miUuid } }).
function generarIdempotencyKey() {
  if (typeof crypto !== "undefined" && typeof crypto.randomUUID === "function") {
    return crypto.randomUUID();
  }
  return "k-" + Date.now().toString(36) + "-" + Math.random().toString(36).slice(2, 14);
}

api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem("token");
    if (token) {
      config.headers.Authorization = `Bearer ${token}`;
    }

    const method = (config.method || "get").toLowerCase();
    if (IDEMPOTENT_METHODS.has(method)) {
      const existing =
        config.headers?.["Idempotency-Key"] || config.headers?.["idempotency-key"];
      if (!existing) {
        config.headers["Idempotency-Key"] = generarIdempotencyKey();
      }
    }

    // Header provisional de identidad mientras no hay Spring Security.
    // El backend lo exige solo en endpoints de historial de estado.
    const usuarioActual = localStorage.getItem("usuarioActual");
    if (usuarioActual) {
      config.headers["X-User"] = usuarioActual;
    }

    return config;
  },
  (error) => Promise.reject(error)
);

api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response && error.response.status === 401) {
      console.warn("Sesión expirada o no autorizada");
    }
    return Promise.reject(error);
  }
);
