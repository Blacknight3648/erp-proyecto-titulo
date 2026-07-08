package backend.com.comercial.domain.model;

import lombok.Getter;

@Getter
public class SCOSAccesorio {
    private Integer idArticulo;
    private String tempId;
    private String tipo;
    private String descripcion;
    private String proveedorReferencia;
    private Integer cantidad;

    public SCOSAccesorio(Integer idArticulo, String tipo, String descripcion, String proveedorReferencia, Integer cantidad, String tempId) {
        this.idArticulo = idArticulo;
        this.tipo = tipo;
        this.descripcion = descripcion;
        this.proveedorReferencia = proveedorReferencia;
        this.cantidad = cantidad != null ? cantidad : 0;
        this.tempId = tempId;
    }
}
