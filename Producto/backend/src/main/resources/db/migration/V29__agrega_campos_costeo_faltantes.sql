-- =============================================================================
-- V29: Agregar campos faltantes en produccion_costeos
-- =============================================================================
-- Estos campos fueron agregados a la entidad JPA en un commit anterior
-- pero faltaba la migración. Se usa IF NOT EXISTS para evitar errores si
-- Hibernate (ddl-auto=update) ya los creó.
-- =============================================================================

DROP PROCEDURE IF EXISTS add_costeo_columns_if_not_exist;

DELIMITER $$
CREATE PROCEDURE add_costeo_columns_if_not_exist()
BEGIN
    IF NOT EXISTS (
        SELECT * FROM INFORMATION_SCHEMA.COLUMNS
        WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'produccion_costeos' AND COLUMN_NAME = 'costo_flete'
    ) THEN
        ALTER TABLE produccion_costeos ADD COLUMN costo_flete DECIMAL(12, 2) NULL;
    END IF;

    IF NOT EXISTS (
        SELECT * FROM INFORMATION_SCHEMA.COLUMNS
        WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'produccion_costeos' AND COLUMN_NAME = 'observaciones_mano_obra'
    ) THEN
        ALTER TABLE produccion_costeos ADD COLUMN observaciones_mano_obra VARCHAR(1000) NULL;
    END IF;
END $$
DELIMITER ;

CALL add_costeo_columns_if_not_exist();
DROP PROCEDURE add_costeo_columns_if_not_exist;
