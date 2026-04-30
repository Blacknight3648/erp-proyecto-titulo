package backend.com.produccion.domain.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TipoOT {
    INTERNA("Trabajo Interno (Taller Propio)"),
    EXTERNA("Servicio Externo (Subcontrato)");

    private final String descripcion;
}
