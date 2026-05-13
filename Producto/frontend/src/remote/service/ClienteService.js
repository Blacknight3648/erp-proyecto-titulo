import { api } from './api';
import { ClienteDTO } from '../DTO/ClienteDTO';

/**
 * Servicio para la gestión de Clientes.
 */
export const ClienteService = {

    getAll: async () => {
        try {
            const response = await api.get('/clientes');
            return ClienteDTO.listFromResponse(response);
        } catch (error) {
            console.error("Error fetching clients:", error);
            throw error;
        }
    },

    getById: async (id) => {
        try {
            const response = await api.get(`/clientes/${id}`);
            return ClienteDTO.fromResponse(response);
        } catch (error) {
            console.error(`Error fetching client ${id}:`, error);
            throw error;
        }
    },

    create: async (cliente) => {
        try {
            const response = await api.post('/clientes', cliente);
            return ClienteDTO.fromResponse(response);
        } catch (error) {
            console.error("Error creating client:", error);
            throw error;
        }
    },

    update: async (cliente) => {
        try {
            const response = await api.put(`/clientes/${cliente.clienteId}`, cliente);
            return ClienteDTO.fromResponse(response);
        } catch (error) {
            console.error(`Error updating client ${cliente.clienteId}:`, error);
            throw error;
        }
    },

    delete: async (id) => {
        try {
            await api.delete(`/clientes/${id}`);
        } catch (error) {
            console.error(`Error deleting client ${id}:`, error);
            throw error;
        }
    },

    lookupCompanyData: async (run) => {

        const cleanRun = run.replace(/[^0-9kK]/g, '');

        await new Promise(resolve => setTimeout(resolve, 800));

        const mockCompanies = {
            "77123456K": {
                nombreCliente: "Antuan SA",
                apellidoCliente: "Consultoría",
                segmento: "Industrial",
                correoCliente: "contacto@antuan.cl",
                telefonoCliente: "+56 9 1234 5678",
                direccionCliente: "Av. Principal 1234, Santiago"
            },
            "765432101": {
                nombreCliente: "Logística Nacional",
                apellidoCliente: "SPA",
                segmento: "Retail",
                correoCliente: "info@logistica.cl",
                telefonoCliente: "+56 2 2345 6789",
                direccionCliente: "Calle Central 456, Valparaíso"
            }
        };

        const data = mockCompanies[cleanRun.toUpperCase()];
        if (!data) throw new Error("Empresa no encontrada");

        return data;
    }
};