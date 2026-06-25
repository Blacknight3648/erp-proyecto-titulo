-- =============================================================================
-- V21: Soft Delete para catálogo de campos de plantilla
-- =============================================================================
-- Agrega columna `activo` a la tabla `plantilla` para permitir desactivar
-- campos sin eliminarlos físicamente. Los campos inactivos no aparecen en
-- las pantallas de configuración de plantillas.
--
-- También incorpora 'puños' al catálogo, ya que el frontend lo usa pero no
-- estaba en el seed original.
-- =============================================================================

ALTER TABLE plantilla
    ADD COLUMN IF NOT EXISTS activo BOOLEAN NOT NULL DEFAULT TRUE;

INSERT IGNORE INTO plantilla (nombre_campo, activo)
    VALUES ('puños', TRUE);
