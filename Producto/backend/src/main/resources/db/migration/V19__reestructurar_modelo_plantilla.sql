-- =============================================================================
-- V19: modelo_plantilla → una fila por artículo (campos en CSV)
-- =============================================================================
-- Reestructura `modelo_plantilla`: de una fila por (artículo, campo) con FK a
-- `plantilla`, a UNA fila por artículo con los nombres de campo en una columna
-- `campos` TEXT (CSV). Se acepta perder la FK a `plantilla` (schema declarativo).
--
-- En dev/test (H2, ddl-auto=update sobre BD recreada) Hibernate crea la tabla ya
-- con `campos` y sin `id_plantilla`. En PROD (MySQL) puede estar en cualquiera de
-- los dos estados según el historial de despliegues: este bloque guardado hace
-- el paso 1 siempre (si falta la columna), y los pasos 2-4 (backfill,
-- deduplicación, drop de la FK/columna vieja) SOLO si la estructura vieja
-- (`id_plantilla`) todavía existe — si Hibernate ya recreó la tabla desde cero
-- con la forma nueva, esos pasos son no-op seguro.
-- =============================================================================

DROP PROCEDURE IF EXISTS v19_reestructurar_plantilla;

DELIMITER $$
CREATE PROCEDURE v19_reestructurar_plantilla()
BEGIN
    -- 1) Nueva columna, si Hibernate aún no la creó.
    IF NOT EXISTS (
        SELECT * FROM INFORMATION_SCHEMA.COLUMNS
        WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'modelo_plantilla' AND COLUMN_NAME = 'campos'
    ) THEN
        ALTER TABLE modelo_plantilla ADD COLUMN campos TEXT;
    END IF;

    -- 2-4) Solo si la estructura vieja (id_plantilla) todavía existe.
    IF EXISTS (
        SELECT * FROM INFORMATION_SCHEMA.COLUMNS
        WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'modelo_plantilla' AND COLUMN_NAME = 'id_plantilla'
    ) THEN
        -- 2) Backfill: agrupar los nombres de campo por artículo (tabla
        --    `plantilla`, columna `nombre_campo`) en la fila de menor id de
        --    cada artículo.
        UPDATE modelo_plantilla mp
        JOIN (
            SELECT MIN(mp2.id_modelo_plantilla) AS keep_id,
                   GROUP_CONCAT(p.nombre_campo ORDER BY p.id_plantilla SEPARATOR ',') AS campos
            FROM modelo_plantilla mp2
            JOIN plantilla p ON p.id_plantilla = mp2.id_plantilla
            GROUP BY mp2.id_articulo
        ) agg ON agg.keep_id = mp.id_modelo_plantilla
        SET mp.campos = agg.campos;

        -- 3) Borrar duplicados (dejar solo la fila de menor id por artículo).
        DELETE mp FROM modelo_plantilla mp
        JOIN (
            SELECT id_articulo, MIN(id_modelo_plantilla) AS keep_id
            FROM modelo_plantilla GROUP BY id_articulo
        ) k ON k.id_articulo = mp.id_articulo
        WHERE mp.id_modelo_plantilla <> k.keep_id;

        -- 4) Soltar la FK (creada en V7) ANTES de eliminar la columna, y rematar.
        IF EXISTS (
            SELECT * FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS
            WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'modelo_plantilla' AND CONSTRAINT_NAME = 'fk_modelo_plantilla'
        ) THEN
            ALTER TABLE modelo_plantilla DROP FOREIGN KEY fk_modelo_plantilla;
        END IF;

        ALTER TABLE modelo_plantilla DROP COLUMN id_plantilla;

        IF NOT EXISTS (
            SELECT * FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS
            WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'modelo_plantilla' AND CONSTRAINT_NAME = 'uk_modelo_articulo'
        ) THEN
            ALTER TABLE modelo_plantilla ADD CONSTRAINT uk_modelo_articulo UNIQUE (id_articulo);
        END IF;
    END IF;

    -- Red de seguridad: artículos sin ninguna fila de `plantilla` asociada no
    -- reciben backfill en el paso 2 y quedarían con `campos` NULL, lo que
    -- rompería el MODIFY NOT NULL de más abajo.
    UPDATE modelo_plantilla SET campos = '' WHERE campos IS NULL;
END $$
DELIMITER ;

CALL v19_reestructurar_plantilla();
DROP PROCEDURE v19_reestructurar_plantilla;

ALTER TABLE modelo_plantilla MODIFY COLUMN campos TEXT NOT NULL;
