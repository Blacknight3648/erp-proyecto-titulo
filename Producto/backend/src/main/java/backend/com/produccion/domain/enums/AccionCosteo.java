package backend.com.produccion.domain.enums;

import backend.com.shared.exception.ForbiddenException;

import java.util.Set;

/**
 * Acción de decisión sobre un costeo y los roles autorizados a ejecutarla.
 *
 * Chequeo de rol "blando": el proyecto aún no tiene Spring Security, así que el
 * rol llega en el request (firma del actor). Cuando exista RBAC real, esta
 * política se reemplaza por anotaciones {@code @PreAuthorize}.
 *
 * Los nombres de rol corresponden a la tabla {@code roles} (ver data.sql).
 */
public enum AccionCosteo {
    APROBAR(Set.of("JEFE_PRODUCCION", "JEFE_COMERCIAL", "JEFE_ADMIN")),
    RECHAZAR(Set.of("JEFE_PRODUCCION", "JEFE_COMERCIAL", "JEFE_ADMIN"));

    private final Set<String> rolesAutorizados;

    AccionCosteo(Set<String> rolesAutorizados) {
        this.rolesAutorizados = rolesAutorizados;
    }

    public boolean permite(String rol) {
        return rol != null && rolesAutorizados.contains(rol.trim().toUpperCase());
    }

    /**
     * Valida que el rol pueda ejecutar esta acción; lanza {@link ForbiddenException}
     * (HTTP 403) en caso contrario.
     */
    public void validarRol(String rol) {
        if (!permite(rol)) {
            throw new ForbiddenException(
                    "El rol '" + rol + "' no está autorizado para " + name().toLowerCase() + " un costeo");
        }
    }
}
