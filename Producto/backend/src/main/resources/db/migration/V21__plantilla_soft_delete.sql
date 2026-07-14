-- =============================================================================
-- V21: Soft Delete para catálogo de campos de plantilla
-- =============================================================================
-- La columna `activo` ya está mapeada en CamposPlantillaJpaEntity (@Column),
-- por lo que Hibernate (ddl-auto=update) la crea antes de que Flyway corra.
-- El `ADD COLUMN IF NOT EXISTS` era redundante y MySQL no soporta esa
-- cláusula (ver V10__evn_item_costeo_columns.sql) → se retiró.
--
-- También incorpora 'puños' al catálogo, ya que el frontend lo usa pero no
-- estaba en el seed original.
-- =============================================================================

INSERT IGNORE INTO plantilla (nombre_campo, activo)
    VALUES ('puños', TRUE);
