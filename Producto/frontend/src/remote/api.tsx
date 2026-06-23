/// <reference types="vite/client" />
import axios from "axios";


export const BACKEND_URL = import.meta.env.VITE_API_URL || "";

export const api = axios.create({
    baseURL: `${BACKEND_URL}/api/v1`,
    headers: {
        "Content-Type": "application/json",
    },
});