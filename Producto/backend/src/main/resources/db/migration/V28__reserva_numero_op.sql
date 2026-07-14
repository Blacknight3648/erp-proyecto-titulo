-- =============================================================================
-- V28: Reserva de número de OP y costeo manual en borrador
-- =============================================================================
-- Esta migración usa un bloque procedural para evitar el error de columna duplicada
-- si Hibernate (ddl-auto=update) ya creó las columnas antes que Flyway.
-- =============================================================================

DROP PROCEDURE IF EXISTS add_columns_if_not_exist;

DELIMITER $$
CREATE PROCEDURE add_columns_if_not_exist()
BEGIN
    -- Agregar numero_op_reservado
    IF NOT EXISTS (
        SELECT * FROM INFORMATION_SCHEMA.COLUMNS
        WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'notas_venta_items' AND COLUMN_NAME = 'numero_op_reservado'
    ) THEN
        ALTER TABLE notas_venta_items ADD COLUMN numero_op_reservado VARCHAR(30) NULL;
    END IF;

    -- Agregar costeo_id_manual
    IF NOT EXISTS (
        SELECT * FROM INFORMATION_SCHEMA.COLUMNS
        WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'notas_venta_items' AND COLUMN_NAME = 'costeo_id_manual'
    ) THEN
        ALTER TABLE notas_venta_items ADD COLUMN costeo_id_manual BIGINT NULL;
    END IF;
END $$
DELIMITER ;

CALL add_columns_if_not_exist();
DROP PROCEDURE add_columns_if_not_exist;
