-- =============================================================================
-- V33: Agregar sigla_rubro a rubros (código corto para generación de SKU)
-- =============================================================================
-- Se agrega RubroJpaEntity.siglaRubro (columna sigla_rubro, VARCHAR(10),
-- UNIQUE) para usarla como componente corto en la generación de SKU/códigos
-- de artículos. En dev/prod (MySQL, ddl-auto=update) y en test (H2,
-- create-drop) Hibernate ya crea la columna a partir de la entidad JPA; esta
-- migración queda como respaldo manual si Flyway llegara a habilitarse — ver
-- spring.flyway.enabled=false en application*.properties (debe ejecutarse
-- manualmente, igual que V29/V31/V32).
--
-- Se usa un procedimiento con IF NOT EXISTS vía INFORMATION_SCHEMA porque
-- MySQL no soporta "ADD COLUMN IF NOT EXISTS" (ver V10__evn_item_costeo_columns.sql).
-- =============================================================================

DROP PROCEDURE IF EXISTS add_sigla_rubro_column;

DELIMITER $$
CREATE PROCEDURE add_sigla_rubro_column()
BEGIN
    IF NOT EXISTS (
        SELECT * FROM INFORMATION_SCHEMA.COLUMNS
        WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'rubros' AND COLUMN_NAME = 'sigla_rubro'
    ) THEN
        ALTER TABLE rubros ADD COLUMN sigla_rubro VARCHAR(10) NULL;
    END IF;

    IF NOT EXISTS (
        SELECT * FROM INFORMATION_SCHEMA.STATISTICS
        WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'rubros' AND INDEX_NAME = 'uk_rubros_sigla_rubro'
    ) THEN
        ALTER TABLE rubros ADD UNIQUE KEY uk_rubros_sigla_rubro (sigla_rubro);
    END IF;
END $$
DELIMITER ;

CALL add_sigla_rubro_column();
DROP PROCEDURE add_sigla_rubro_column;
