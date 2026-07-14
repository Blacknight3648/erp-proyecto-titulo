-- =============================================================================
-- V18: Versionado de costeo (contador de reproceso)
-- =============================================================================
-- Agrega `version` al Costeo: contador de iteraciones que arranca en 1 y se
-- incrementa cada vez que se retoma (reabrir) un costeo previamente RECHAZADO.
-- Es independiente del log técnico de snapshots `produccion_costeo_versiones`.
--
-- En dev/test (H2) Hibernate (ddl-auto=update) crea la columna `version` desde la
-- entidad; en PROD puede o no existir según el historial de despliegues. Bloque
-- guardado con IF NOT EXISTS (mismo patrón que V28__reserva_numero_op.sql) para
-- cubrir ambos casos sin fallar con "columna duplicada".
-- =============================================================================

DROP PROCEDURE IF EXISTS v18_add_costeo_version;

DELIMITER $$
CREATE PROCEDURE v18_add_costeo_version()
BEGIN
    IF NOT EXISTS (
        SELECT * FROM INFORMATION_SCHEMA.COLUMNS
        WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'produccion_costeos' AND COLUMN_NAME = 'version'
    ) THEN
        ALTER TABLE produccion_costeos ADD COLUMN version INT NOT NULL DEFAULT 1;
    END IF;
END $$
DELIMITER ;

CALL v18_add_costeo_version();
DROP PROCEDURE v18_add_costeo_version;
