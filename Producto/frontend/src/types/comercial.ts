export type ItemType = 'OP' | 'OC' | 'OS';

export interface TallaItem {
    talla: string;
    cantidad: number;
}

export interface Sizes {
    XS: number;
    S: number;
    M: number;
    L: number;
    XL: number;
    [key: string]: number;
}

export interface ItemNV {
    nroItem: number | string;
    idItem?: number;
    productoId: string | number | null;
    nombreProducto: string;
    modelo: string;
    tela: string;
    composicion: string;
    color: string;
    genero: string;
    talla: string;
    sizes: Sizes;
    cantidad: number;
    precioUnitario: number;
    tipoItem: ItemType;
    logo: string;
    proveedorId: string | number | null;
    requiereOt: boolean;
    detalleOt: string;
    logoDetalle?: string;
}

export interface NotaVenta {
    idNV?: number | string;
    numeroNV?: string | number;
    clienteId: number | string;
    clienteNombre?: string;
    vendedorId: number | string;
    fechaEntregaEstimada: string;
    fechaEmision?: string;
    esKit: boolean;
    detalleKit: string;
    items: ItemNV[];
    sourceEvaluacionId?: number | null;
    total?: number;
    estado?: string;
}

export interface ItemSC {
    id: number | string;
    garment: string;
    color: string;
    size: string;
    quantity: number;
    supplier: string;
    gender: string;
    requiereOT: boolean;
    otDetalle: string;
    code: string;
    logo: string;
    priceType: string;
    unitPrice: number;
    sizes: Sizes;
    showSizeMenu?: boolean;
}

export interface SolicitudCompra {
    idSC?: string | number;
    numeroSC?: string;
    items: any[]; // List of ItemSC
    notaVentaId?: string | number;
    vendedorId?: string | number;
    clienteId?: string | number;
    fecha?: string;
    fechaEmision?: string;
    estado?: string;
    isKitSC?: boolean;
}

export interface ItemEvaluacion {
    id: number | string;
    cant: number;
    descripcion: string;
    familia: string;
    precioVentaMin: number;
    precioNetoUnit: number;
    costoCompra?: number;
    costoBordado?: number;
    costoPrenda?: number;
    cajas?: number;
    tipo: 'Compra' | 'Produccion';
}

export interface OtrosCostos {
    garantiaSeriedad: number;
    garantiaFiel: number;
    flete: number;
    modificacion: number;
    tomaTallaje: number;
    certificacion: number;
    muestras: number;
}

export interface CondicionesComerciales {
    anticipo: number;
    saldo: number;
    flete: string;
    garantia: string;
    plazoEntrega: string;
}

export interface EvaluacionNegocio {
    evaluacionNegocioId: string | number;
    clienteId?: number;
    cliente: string;
    articuloDescripcion: string;
    fecha?: string;
    fechaEmision?: string;
    estado: 'Activo' | 'Cerrado' | string;
    costoFinalAcordado: number;
    margenGanancia: number;
    items?: ItemEvaluacion[];
    otrosCostos?: OtrosCostos;
    condiciones?: CondicionesComerciales;
    solicitud?: any;
}
