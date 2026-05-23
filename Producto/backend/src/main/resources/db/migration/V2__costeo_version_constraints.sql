-- =============================================================================
-- FASE 1: Versionado del Costeo - Constraints e índices complementarios
-- =============================================================================
-- Las tablas `produccion_costeo_versiones` y `produccion_costeo_item_versiones`
-- son creadas por Hibernate (ddl-auto=update) a partir de las JpaEntities.
-- Las FKs básicas y el UNIQUE compuesto también las genera Hibernate desde
-- las anotaciones @ManyToOne, @JoinColumn, @UniqueConstraint, @OneToMany.
--
-- Este archivo solo agrega lo que Hibernate NO maneja:
--   * Índices nombrados para joins frecuentes
--
-- TODO (deuda técnica para producción / Postgres):
--   * Migrar a ddl-auto=none y mover los CREATE TABLE de las JpaEntities
--     a Flyway, declarando FKs con ON DELETE explícito:
--       - orden_produccion.costeo_version_id  ON DELETE RESTRICT
--         (protege la trazabilidad: no se puede borrar una versión si
--         alguna OP la referencia)
--       - costeo_version.costeo_id            ON DELETE RESTRICT
--       - costeo_item_version.costeo_version_id  ON DELETE CASCADE
--         (los items snapshot mueren con su versión)
-- =============================================================================

CREATE INDEX IF NOT EXISTS idx_op_costeo_version
    ON orden_produccion (costeo_version_id);

CREATE INDEX IF NOT EXISTS idx_cv_costeo
    ON produccion_costeo_versiones (costeo_id);

CREATE INDEX IF NOT EXISTS idx_civ_version
    ON produccion_costeo_item_versiones (costeo_version_id);
