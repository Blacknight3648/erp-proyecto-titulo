import { HojaCompraDTO } from './HojaCompraDTO';
import { OrdenCompraDTO } from './OrdenCompraDTO';
import { OrdenServicioDTO } from './OrdenServicioDTO';
import { RecepcionOCDTO } from './RecepcionOCDTO';

/**
 * Vista consolidada de toda la cadena de trazabilidad de una OP.
 * Espejo de TrazabilidadOPDTO.java del backend.
 */
export class TrazabilidadOPDTO {
    constructor(data = {}) {
        this.opId = data.opId ?? null;
        this.numeroOP = data.numeroOP ?? '';
        this.notaVentaId = data.notaVentaId ?? null;
        this.estadoOP = data.estadoOP ?? null;
        this.fechaEntregaProgramada = data.fechaEntregaProgramada ?? null;

        this.costeoVersion = data.costeoVersion ? {
            idCosteoVersion: data.costeoVersion.idCosteoVersion ?? null,
            costeoId: data.costeoVersion.costeoId ?? null,
            numeroVersion: data.costeoVersion.numeroVersion ?? null,
            fechaCreacion: data.costeoVersion.fechaCreacion ?? null,
            motivoCambio: data.costeoVersion.motivoCambio ?? '',
            usuarioCreador: data.costeoVersion.usuarioCreador ?? '',
        } : null;

        this.hojaCompra = data.hojaCompra ? new HojaCompraDTO(data.hojaCompra) : null;
        this.ordenesCompra = Array.isArray(data.ordenesCompra)
            ? data.ordenesCompra.map(oc => new OrdenCompraDTO(oc))
            : [];
        this.recepcionesOC = Array.isArray(data.recepcionesOC)
            ? data.recepcionesOC.map(r => new RecepcionOCDTO(r))
            : [];
        this.ordenesServicio = Array.isArray(data.ordenesServicio)
            ? data.ordenesServicio.map(os => new OrdenServicioDTO(os))
            : [];
    }

    static fromResponse(response) {
        if (!response || !response.data) return null;
        return new TrazabilidadOPDTO(response.data);
    }
}
