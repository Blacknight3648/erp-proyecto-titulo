-- V33: Versionado/rechazo de Orden de Compra + insumos no presupuestados en HC.
-- Idempotente (IF NOT EXISTS via information_schema.columns), mismo patrón que
-- V27/V28/V29 — necesario porque esta migración corre en cada arranque
-- (ver FlywayConfig.java) y Hibernate (ddl-auto=update) ya pudo haber creado
-- estas columnas antes de que Flyway llegue a correr.

DELIMITER $$

CREATE PROCEDURE tmp_v33_add_columns()
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_schema = DATABASE() AND table_name = 'produccion_ordenes_compra' AND column_name = 'motivo_rechazo'
    ) THEN
        ALTER TABLE produccion_ordenes_compra ADD COLUMN motivo_rechazo VARCHAR(500);
    END IF;

    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_schema = DATABASE() AND table_name = 'produccion_ordenes_compra' AND column_name = 'version'
    ) THEN
        ALTER TABLE produccion_ordenes_compra ADD COLUMN version INT NOT NULL DEFAULT 1;
    END IF;

    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_schema = DATABASE() AND table_name = 'produccion_hoja_compra_items' AND column_name = 'presupuestado'
    ) THEN
        ALTER TABLE produccion_hoja_compra_items ADD COLUMN presupuestado TINYINT(1) NOT NULL DEFAULT 1;
    END IF;
END$$

DELIMITER ;

CALL tmp_v33_add_columns();

DROP PROCEDURE tmp_v33_add_columns;
