-- =============================================================================
-- FASE 5: Recepción de OC - Constraints e índices complementarios
-- =============================================================================
-- Tablas creadas por Hibernate:
--   * produccion_recepciones_oc
--   * produccion_recepcion_oc_items
--
-- TODO (deuda para producción / Postgres):
--   * FK recepcion.oc_id            ON DELETE CASCADE (las recepciones se borran con la OC)
--   * FK recepcion_item.recepcion_id ON DELETE CASCADE
--   * FK recepcion_item.oc_item_id  ON DELETE RESTRICT
-- =============================================================================

CREATE INDEX IF NOT EXISTS idx_recoc_oc
    ON produccion_recepciones_oc (oc_id);

CREATE INDEX IF NOT EXISTS idx_recoc_fecha
    ON produccion_recepciones_oc (fecha_recepcion);

CREATE INDEX IF NOT EXISTS idx_reci_recepcion
    ON produccion_recepcion_oc_items (recepcion_id);

CREATE INDEX IF NOT EXISTS idx_reci_oci
    ON produccion_recepcion_oc_items (oc_item_id);
