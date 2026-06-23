/// <reference types="vite/client" />
import axios from "axios";


export const BACKEND_URL = import.meta.env.VITE_API_URL || "http://127.0.0.1:8050";

export const api = axios.create({
    baseURL: `${BACKEND_URL}/api/v1`,
    headers: {
        "Content-Type": "application/json",
    },
});