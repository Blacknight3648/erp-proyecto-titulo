-- V24__hoja_compra_item_validacion_modificacion.sql
-- Añadir stock actual a la tabla de artículos para la consulta en validación de compra
ALTER TABLE articulo
ADD COLUMN stock_actual DECIMAL(12, 4) NOT NULL DEFAULT 0.0000 COMMENT 'Stock disponible en bodega';

-- Añadir campos para validación de stock y seguimiento de modificaciones justificadas en ítems de hoja de compra
ALTER TABLE produccion_hoja_compra_items
ADD COLUMN cantidad_stock DECIMAL(12, 4) NOT NULL DEFAULT 0.0000 COMMENT 'Stock en bodega al momento de la consulta',
ADD COLUMN cantidad_a_comprar DECIMAL(12, 4) NULL COMMENT 'Cantidad real a comprar (por defecto igual a cantidad_requerida)',
ADD COLUMN modificado BOOLEAN NOT NULL DEFAULT FALSE COMMENT 'Indica si el ítem fue modificado respecto al costeo original',
ADD COLUMN justificacion_modificacion VARCHAR(500) NULL COMMENT 'Justificación obligatoria en caso de modificación';
