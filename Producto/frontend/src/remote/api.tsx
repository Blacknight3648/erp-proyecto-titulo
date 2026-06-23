/// <reference types="vite/client" />
import axios from "axios";

export const api = axios.create({
    baseURL: import.meta.env.VITE_API_URL ? `${import.meta.env.VITE_API_URL}/api/v1` : "http://127.0.0.1:8050/api/v1",
    headers: {
        "Content-Type": "application/json",
    },
});