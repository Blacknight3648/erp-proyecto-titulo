-- =============================================================================
-- V11: Agregar columna de proveedor y FK a Hoja de Compra Items
-- =============================================================================

ALTER TABLE produccion_hoja_compra_items
    ADD COLUMN IF NOT EXISTS proveedor_id BIGINT;

ALTER TABLE produccion_hoja_compra_items
    ADD CONSTRAINT fk_hc_item_proveedor
    FOREIGN KEY (proveedor_id) REFERENCES proveedores (proveedor_id) ON DELETE SET NULL;

CREATE INDEX IF NOT EXISTS idx_hci_proveedor
    ON produccion_hoja_compra_items (proveedor_id);
