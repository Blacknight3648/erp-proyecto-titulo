import { api } from './api';
import { ProveedorDTO } from '../DTO/ProveedorDTO';

export const ProveedorService = {

    getAll: async () => {
        const response = await api.get('/proveedores');
        return ProveedorDTO.listFromResponse(response);
    },

    getById: async (id) => {
        const response = await api.get(`/proveedores/${id}`);
        return ProveedorDTO.fromResponse(response);
    },

    create: async (proveedor) => {
        const response = await api.post('/proveedores', proveedor);
        return ProveedorDTO.fromResponse(response);
    },

    update: async (id, proveedorData) => {
        const response = await api.put(`/proveedores/${id}`, proveedorData);
        return ProveedorDTO.fromResponse(response);
    },

    delete: async (id) => {
        await api.delete(`/proveedores/${id}`);
    }

};