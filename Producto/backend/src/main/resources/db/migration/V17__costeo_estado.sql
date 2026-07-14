-- =============================================================================
-- V17: Ciclo de vida (estado) del Costeo
-- =============================================================================
-- Incorpora el estado del costeo (BORRADOR / COSTEADO / APROBADO / RECHAZADO) y
-- el motivo de rechazo. El dominio, la entidad JPA, los DTOs y la API ya manejan
-- estos campos; en dev/test (H2 en memoria) Hibernate (ddl-auto=update) crea las
-- columnas `estado` y `motivo_rechazo` desde la entidad, por lo que este bloque
-- no hace nada ahí (guardado con IF NOT EXISTS).
--
-- En PROD (MySQL) Hibernate puede o no haber agregado ya estas columnas según el
-- historial de despliegues; el bloque guardado cubre ambos casos sin fallar con
-- "columna duplicada" (mismo patrón que V28__reserva_numero_op.sql).
-- =============================================================================

DROP PROCEDURE IF EXISTS v17_add_costeo_estado;

DELIMITER $$
CREATE PROCEDURE v17_add_costeo_estado()
BEGIN
    IF NOT EXISTS (
        SELECT * FROM INFORMATION_SCHEMA.COLUMNS
        WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'produccion_costeos' AND COLUMN_NAME = 'estado'
    ) THEN
        ALTER TABLE produccion_costeos ADD COLUMN estado VARCHAR(20) NOT NULL DEFAULT 'BORRADOR';
    END IF;

    IF NOT EXISTS (
        SELECT * FROM INFORMATION_SCHEMA.COLUMNS
        WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'produccion_costeos' AND COLUMN_NAME = 'motivo_rechazo'
    ) THEN
        ALTER TABLE produccion_costeos ADD COLUMN motivo_rechazo VARCHAR(300) NULL;
    END IF;
END $$
DELIMITER ;

CALL v17_add_costeo_estado();
DROP PROCEDURE v17_add_costeo_estado;

-- Backfill: costeos ya vinculados a una versión (es decir, ya en uso) se marcan
-- APROBADO en vez de dejarlos en el DEFAULT 'BORRADOR' recién asignado.
UPDATE produccion_costeos c
SET    c.estado = 'APROBADO'
WHERE  EXISTS (SELECT 1 FROM produccion_costeo_versiones v WHERE v.costeo_id = c.id_costeo)
  AND  c.estado = 'BORRADOR';
