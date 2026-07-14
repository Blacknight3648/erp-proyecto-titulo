-- ─────────────────────────────────────────────────────────────────────────
-- V21: Alinear la tabla modelo_plantilla con la entidad JPA
-- Safe para instalación fresca (Hibernate ya creó el schema actualizado)
-- y para bases de datos existentes con el schema anterior.
--
-- Reescrito para no depender de `ADD COLUMN IF NOT EXISTS` / `DROP COLUMN
-- IF EXISTS` (requiere MySQL 8.0.29+; ver V10__evn_item_costeo_columns.sql,
-- que documenta que esta sintaxis ya causó problemas en este proyecto). Se
-- usa el mismo patrón guardado con procedimiento + INFORMATION_SCHEMA que
-- V19__reestructurar_modelo_plantilla.sql (que hace el mismo trabajo y corre
-- antes, dejando esta migración como no-op seguro en el caso normal).
-- ─────────────────────────────────────────────────────────────────────────

DROP PROCEDURE IF EXISTS v21_fix_modelo_plantilla;

DELIMITER $$
CREATE PROCEDURE v21_fix_modelo_plantilla()
BEGIN
    -- 1) Drop FK vieja si existe.
    IF EXISTS (
        SELECT * FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS
        WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'modelo_plantilla' AND CONSTRAINT_NAME = 'fk_modelo_plantilla'
    ) THEN
        ALTER TABLE modelo_plantilla DROP FOREIGN KEY fk_modelo_plantilla;
    END IF;

    -- 2) Drop unique constraint obsoleta si existe.
    IF EXISTS (
        SELECT * FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS
        WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'modelo_plantilla' AND CONSTRAINT_NAME = 'uk_modelo_articulo_plantilla'
    ) THEN
        ALTER TABLE modelo_plantilla DROP INDEX uk_modelo_articulo_plantilla;
    END IF;

    -- 3) Drop columna obsoleta si existe.
    IF EXISTS (
        SELECT * FROM INFORMATION_SCHEMA.COLUMNS
        WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'modelo_plantilla' AND COLUMN_NAME = 'id_plantilla'
    ) THEN
        ALTER TABLE modelo_plantilla DROP COLUMN id_plantilla;
    END IF;

    -- 4) Agregar columna campos si no existe (tabla vacía en fresh install;
    --    en un despliegue con datos previos, V19 ya la deja poblada antes de
    --    que esta migración corra).
    IF NOT EXISTS (
        SELECT * FROM INFORMATION_SCHEMA.COLUMNS
        WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'modelo_plantilla' AND COLUMN_NAME = 'campos'
    ) THEN
        ALTER TABLE modelo_plantilla ADD COLUMN campos TEXT;
    END IF;

    -- 5) Agregar restricción UNIQUE si no existe.
    IF NOT EXISTS (
        SELECT * FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS
        WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'modelo_plantilla' AND CONSTRAINT_NAME = 'uk_modelo_articulo'
    ) THEN
        ALTER TABLE modelo_plantilla ADD CONSTRAINT uk_modelo_articulo UNIQUE (id_articulo);
    END IF;
END $$
DELIMITER ;

CALL v21_fix_modelo_plantilla();
DROP PROCEDURE v21_fix_modelo_plantilla;

-- Red de seguridad: filas sin backfill (p. ej. artículos sin plantilla
-- asociada) no deben bloquear el NOT NULL final.
UPDATE modelo_plantilla SET campos = '' WHERE campos IS NULL;

ALTER TABLE modelo_plantilla MODIFY COLUMN campos TEXT NOT NULL;
