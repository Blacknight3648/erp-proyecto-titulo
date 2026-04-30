export type CategoriaProveedor =
    | 'Telas y Géneros'
    | 'Avíos y Accesorios'
    | 'Servicios de Bordado'
    | 'Servicios de Estampado'
    | 'Talleres Externos'
    | 'Insumos de Oficina'
    | 'Otros';

export interface PrecioProveedor {
    garment: string;
    price: number;
    lastUpdate?: string;
}

export interface Proveedor {
    proveedorId: number;
    nombreProveedor: string;
    rutProveedor: string;
    direccionProveedor?: string;
    telefonoProveedor?: string;
    correoProveedor?: string;
    contactoProveedor?: string;
    categoria: CategoriaProveedor | string;
    activo: boolean;
    precios?: PrecioProveedor[];
}
