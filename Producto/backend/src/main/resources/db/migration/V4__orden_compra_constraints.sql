-- =============================================================================
-- FASE 3: Orden de Compra (OC) - Constraints e índices complementarios
-- =============================================================================
-- Tablas creadas por Hibernate:
--   * produccion_ordenes_compra
--   * produccion_orden_compra_items
--   * produccion_hc_item_oc_item (tabla puente N:M)
--
-- TODO (deuda para producción / Postgres):
--   * FK oc.proveedor_id              ON DELETE RESTRICT (proteger trazabilidad)
--   * FK oci.oc_id                    ON DELETE CASCADE  (items mueren con la OC)
--   * FK hc_item_oc_item.hc_item_id   ON DELETE CASCADE
--   * FK hc_item_oc_item.oc_item_id   ON DELETE CASCADE
-- =============================================================================

CREATE INDEX IF NOT EXISTS idx_oc_estado
    ON produccion_ordenes_compra (estado);

CREATE INDEX IF NOT EXISTS idx_oc_proveedor
    ON produccion_ordenes_compra (proveedor_id);

CREATE INDEX IF NOT EXISTS idx_oc_fecha_emision
    ON produccion_ordenes_compra (fecha_emision);

CREATE INDEX IF NOT EXISTS idx_oci_oc
    ON produccion_orden_compra_items (oc_id);

CREATE INDEX IF NOT EXISTS idx_oci_insumo
    ON produccion_orden_compra_items (insumo_id);

CREATE INDEX IF NOT EXISTS idx_link_hci
    ON produccion_hc_item_oc_item (hc_item_id);

CREATE INDEX IF NOT EXISTS idx_link_oci
    ON produccion_hc_item_oc_item (oc_item_id);
