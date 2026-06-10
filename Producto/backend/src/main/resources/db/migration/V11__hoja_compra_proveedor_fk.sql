-- =============================================================================
-- V11: FK de proveedor en Hoja de Compra Items
-- =============================================================================
-- La columna `proveedor_id` la crea Hibernate (@Column en HojaCompraItemJpaEntity;
-- el @ManyToOne usa ConstraintMode.NO_CONSTRAINT, así que no genera FK). Aquí solo
-- declaramos la integridad referencial. Se retiró el `ADD COLUMN IF NOT EXISTS`
-- (redundante y no soportado por MySQL).

ALTER TABLE produccion_hoja_compra_items
    ADD CONSTRAINT fk_hc_item_proveedor
    FOREIGN KEY (proveedor_id) REFERENCES proveedores (proveedor_id) ON DELETE SET NULL;

CREATE INDEX idx_hci_proveedor
    ON produccion_hoja_compra_items (proveedor_id);
