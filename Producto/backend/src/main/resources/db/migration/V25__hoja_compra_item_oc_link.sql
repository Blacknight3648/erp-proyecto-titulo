-- V25__hoja_compra_item_oc_link.sql
-- Añadir campos oc_id y numero_oc en la tabla de ítems de hoja de compra para evitar desvinculación
ALTER TABLE produccion_hoja_compra_items
ADD COLUMN oc_id BIGINT NULL COMMENT 'ID de la Orden de Compra asociada',
ADD COLUMN numero_oc VARCHAR(100) NULL COMMENT 'Número de la Orden de Compra asociada';
