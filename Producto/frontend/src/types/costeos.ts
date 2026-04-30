export type TipoSolicitud = 'costo' | 'cotizacion';

export type UnidadMedida = 'un' | 'mts' | 'kg' | 'gr';

export interface LineItem {
  id: string;
  descripcion: string;
  nombreProveedor: string;
  consumo: number;
  costoUnitario?: number;
  subtotal?: number;
  unidadMedida: UnidadMedida;
}

export interface CostosFijos {
  hilos: number;
  manoObra: number;
  etiquetas: number;
  embalaje: number;
  flete: number;
  porcentajeCostoFijo: number;
}

export interface SolicitudForm {
  idSolicitud: string | number;
  numero: string;
  fecha?: string;
  clienteId: string | number;
  vendedorId: string | number;
  nroReferencia: string;
  articuloId: string;
  articuloDescripcion: string;
  esMuestra: boolean;
  cantidad: number;

  tipo: TipoSolicitud;

  telas: LineItem[];
  accesorios: LineItem[];
  logotipo: LineItem[];
  cintas: LineItem[];
  plantillas: LineItem[];

  hasLogo: boolean;
  hasCintas: boolean;

  linkReferencia: string;

  costosFijos: CostosFijos;
}

export interface SolicitudRecord {
  idSolicitud: string | number;
  numero: string;
  fecha?: string;
  clienteNombre?: string;
  articuloDescripcion?: string;
  cantidad?: number;
  tipo?: TipoSolicitud;
  costosFijos?: CostosFijos;

  telas?: LineItem[];
  accesorios?: LineItem[];
  logotipo?: LineItem[];
  cintas?: LineItem[];
}