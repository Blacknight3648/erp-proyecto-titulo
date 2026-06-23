/**
 * DTO para representar un Item de Evaluación de Negocio.
 * Basado en ItemEVN.java (3FN: campos descriptivos explícitos, no EAV)
 */
export class ItemEVNDTO {
    constructor(data = {}) {
        this.idItemEVN         = data.id || data.idItemEVN || null;
        this.nroItem           = data.nroItem || data.numero || null;
        this.articuloId        = data.articuloId || data.productoId || null;
        this.proveedorId       = data.proveedorId || null;
        this.proveedorNombre   = data.proveedorNombre || '';
        this.descripcion       = data.descripcion || '';
        this.modelo            = data.modelo || '';
        this.tela              = data.tela || '';
        this.composicion       = data.composicion || '';
        this.genero            = data.genero || '';
        this.codigoInterno     = data.codigoInterno || '';
        this.codigoProveedor   = data.codigoProveedor || '';
        this.cantidad          = data.cantidad || 0;
        this.precioUnitario    = data.precioUnitario || 0;
        this.costoUnitario     = data.costoUnitario || 0;
        this.costoProducto     = data.costoProducto || data.costoUnitario || 0;
        this.costoLogo         = data.costoLogo || 0;
        this.costoOrdenTrabajo = data.costoOrdenTrabajo || 0;
        this.costoTotalUnitario = data.costoTotalUnitario || 0;
        this.tipoItem          = data.tipoItem || '';
        // Array de specs verdaderamente dinámicas [{clave, valor}]
        this.technicalSpecs    = Array.isArray(data.technicalSpecs) ? data.technicalSpecs : [];
        this.margenItem        = data.margenItem || 0;
        this.totalItem         = data.totalItem || data.total || 0;
        this.costeoId          = data.costeoId || null;
        this.solicitudCostosId = data.solicitudCostosId || null;
    }
}
