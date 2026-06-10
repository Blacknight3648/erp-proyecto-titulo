-- =============================================================================
-- FASE 2: Hoja de Compra (HC) - Constraints e índices complementarios
-- =============================================================================
-- Las tablas `produccion_hojas_compra` y `produccion_hoja_compra_items` las
-- crea Hibernate (ddl-auto=update). El UNIQUE en (numero_hc) y (op_id) ya está
-- declarado en @UniqueConstraint de HojaCompraJpaEntity.
--
-- Aquí solo agregamos índices para joins frecuentes.
--
-- TODO (deuda para producción / Postgres):
--   * FK hoja_compra.op_id              ON DELETE RESTRICT
--   * FK hoja_compra.costeo_version_id  ON DELETE RESTRICT
--   * FK hoja_compra_item.hc_id         ON DELETE CASCADE
-- =============================================================================

CREATE INDEX idx_hc_estado
    ON produccion_hojas_compra (estado);

CREATE INDEX idx_hc_costeo_version
    ON produccion_hojas_compra (costeo_version_id);

CREATE INDEX idx_hci_hc
    ON produccion_hoja_compra_items (hc_id);

CREATE INDEX idx_hci_articulo
    ON produccion_hoja_compra_items (articulo_id);
