-- =============================================================================
-- V13: Eliminar el número de documento de la Orden de Trabajo (OT)
-- =============================================================================
-- La OT pasó a ser un REGISTRO de seguimiento de producción, no un documento, por
-- lo que ya no lleva número correlativo propio (`numeroOT`). El dominio, la entidad
-- JPA, los DTOs y la API dejaron de exponer ese campo; la OT se identifica por su
-- id y se referencia por su OP, ítem y fase.
--
-- En dev/test (H2 en memoria, ddl-auto=update sobre BD recreada) la columna ya no
-- se crea, porque la entidad dejó de mapearla. En PROD (MySQL, ddl-auto=update)
-- Hibernate NO elimina columnas existentes: la antigua `numero_ot` puede haber
-- quedado huérfana y, al ser NOT NULL sin default, ROMPE los INSERT de nuevas OT.
--
-- Bloque guardado (MySQL no soporta DROP COLUMN IF EXISTS directo, así que se
-- verifica primero contra INFORMATION_SCHEMA, mismo patrón que
-- V28__reserva_numero_op.sql) para no fallar si la columna ya no existe.
-- =============================================================================

DROP PROCEDURE IF EXISTS v13_drop_numero_ot;

DELIMITER $$
CREATE PROCEDURE v13_drop_numero_ot()
BEGIN
    IF EXISTS (
        SELECT * FROM INFORMATION_SCHEMA.COLUMNS
        WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'produccion_orden_trabajo' AND COLUMN_NAME = 'numero_ot'
    ) THEN
        ALTER TABLE produccion_orden_trabajo DROP COLUMN numero_ot;
    END IF;
END $$
DELIMITER ;

CALL v13_drop_numero_ot();
DROP PROCEDURE v13_drop_numero_ot;
