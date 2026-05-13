/**
 * DTO para representar un Item de Evaluación de Negocio.
 * Basado en ItemEVN.java
 */
export class ItemEVNDTO {
    constructor(data = {}) {
        const specs = data.technicalSpecs || {};
        this.idItemEVN       = data.id || data.idItemEVN || null;
        this.nroItem         = data.nroItem || data.numero || null;
        this.productoId      = data.productoId || null;
        this.proveedorId     = data.proveedorId || null;
        this.proveedorNombre = data.proveedorNombre || specs.proveedor || '';
        this.cantidad        = data.cantidad || 0;
        this.precioUnitario  = data.precioUnitario || 0;
        this.costoUnitario   = data.costoUnitario || 0;
        this.costoProducto   = data.costoProducto || data.costoUnitario || 0;
        this.costoLogo       = data.costoLogo || 0;
        this.costoOrdenTrabajo = data.costoOrdenTrabajo || 0;
        this.descripcion     = data.descripcion || specs.descripcion || '';
        this.modelo          = data.modelo || specs.modelo || '';
        this.tela            = data.tela || specs.tela || '';
        this.composicion     = data.composicion || specs.composicion || '';
        this.genero          = data.genero || specs.genero || '';
        this.codigoInterno   = data.codigoInterno || specs.codigoInterno || '';
        this.codigoProveedor = data.codigoProveedor || specs.codigoProveedor || '';
        this.tipoItem        = data.tipoItem || '';
        this.technicalSpecs  = data.technicalSpecs || {};
        this.margenItem      = data.margenItem || 0;
        this.totalItem       = data.totalItem || data.total || 0;
    }
}
