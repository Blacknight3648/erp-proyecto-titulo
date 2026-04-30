export type PrioridadOP = 'Baja' | 'Media' | 'Alta' | 'Crítica';

export interface SLAProduccion {
    corte: number;
    logo: number;
    taller: number;
    term: number;
    personalizado?: number;
    entrega: number;
}

export interface OrdenProduccion {
    idOP: string | number;
    numeroOP?: string;
    notaVentaId: string | number;
    cliente: string;
    clienteId?: number;
    producto: string;
    estado: string;
    progreso: number;
    prioridad: PrioridadOP;
    fechaInicio?: string;
    fechaEmision?: string;
    fechaTizado?: string;
    fechaCorte?: string;
    fechaLogo?: string;
    fechaTallerExt?: string;
    fechaTerminaciones?: string;
    fechaEntregaProgramada?: string;
    sla: SLAProduccion;
    tienePersonalizado: boolean;
    opPersonalizadoId?: string;
}

export interface DetalleSeguimientoOP {
    recepcionOP?: string;
    finTizado?: string;
    estadoOcMp?: 'sin tela en mercado' | 'OC emitida' | 'tela en stock' | 'en stock y OC emitida' | 'N/A';
    recepCompra?: string;
    inicioCorte?: string;
    cantidadCortes?: number;
    finCorte?: string;
    inicioLogo?: string;
    regresoLogo?: string;
    estadoRecLogo?: 'Recep. completa' | 'Recep. parcial' | 'N/A';
    inicioTaller?: string;
    finTaller?: string;
    calidadTaller?: 'Aprobado' | 'Rechazado' | 'con Obs.';
    obsTaller?: string;
    finPersonalizado?: string;
    finOP?: string;
    entregaBodega?: string;
}

export interface InsumoProduccion {
    id: string | number;
    producto: string;
    costo: number;
    unidad: string;
    cantidad: number;
    categoryId: 'telas' | 'accesorios' | 'logotipo' | 'cintas' | string;
}

export interface CosteoOP {
    id: string;
    solicitudCostosId: string;
    insumos: InsumoProduccion[];
    totalMP: number;
    estado: 'Borrador' | 'En Proceso' | 'Costeado';
    fecha: string;
}
