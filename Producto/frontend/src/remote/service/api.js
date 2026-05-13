import axios from "axios";

// Configuración de la instancia base de Axios
export const api = axios.create({
  baseURL: "http://localhost:8050/api/v1", // El puerto 8050 es el configurado en application.properties
});

// Interceptor para incluir el token de autorización en cada petición
api.interceptors.request.use((config) => {
  const token = localStorage.getItem("token");
  if (token) {
    config.headers.Authorization = `Bearer ${token}`;
  }
  return config;
}, (error) => {
  return Promise.reject(error);
});

// Interceptor para manejar errores globales (ej. 401 si el token expira)
api.interceptors.response.use((response) => {
  return response;
}, (error) => {
  if (error.response && error.response.status === 401) {
    // Aquí se podría redirigir al login o limpiar el localStorage
    console.warn("Sesión expirada o no autorizada");
  }
  return Promise.reject(error);
});
