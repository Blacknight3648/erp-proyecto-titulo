export type SegmentoCliente = 'Industrial' | 'Minero' | 'Colegio' | 'Corporativo' | 'Otro';

export interface Cliente {
    clienteId?: number;
    nombreCliente: string;
    apellidoCliente: string;
    runCliente: string;
    correoCliente: string;
    telefonoCliente: string;
    direccion: string;
    segmento: SegmentoCliente | string;
    contacto: string;
    activo: boolean;
}
