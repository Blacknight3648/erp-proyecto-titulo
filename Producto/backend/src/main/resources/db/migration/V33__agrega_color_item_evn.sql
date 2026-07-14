-- V33: Agrega columna color a evaluacion_negocio_items.
-- Idempotente (IF NOT EXISTS via information_schema.columns), mismo patrón
-- que V27/V28/V29/V35 — necesario porque esta migración corre en cada
-- arranque (ver FlywayConfig.java) y Hibernate (ddl-auto=update) ya pudo
-- haber creado esta columna antes de que Flyway llegue a correr.

DELIMITER $$

CREATE PROCEDURE tmp_v33_add_color_evn_item()
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_schema = DATABASE() AND table_name = 'evaluacion_negocio_items' AND column_name = 'color'
    ) THEN
        ALTER TABLE evaluacion_negocio_items ADD COLUMN color VARCHAR(100);
    END IF;
END$$

DELIMITER ;

CALL tmp_v33_add_color_evn_item();

DROP PROCEDURE tmp_v33_add_color_evn_item;
