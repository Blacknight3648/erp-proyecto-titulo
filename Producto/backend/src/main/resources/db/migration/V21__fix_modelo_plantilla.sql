-- ─────────────────────────────────────────────────────────────────────────
-- V21: Alinear la tabla modelo_plantilla con la entidad JPA
-- Eliminar id_plantilla y agregar la columna campos (CSV) con restricción UNIQUE por artículo.
-- Safe para instalación fresca: Hibernate ya crea el schema actualizado,
-- por lo que FK y columnas pueden no existir. El CONTINUE HANDLER ignora esos errores.
-- ─────────────────────────────────────────────────────────────────────────

DROP PROCEDURE IF EXISTS v21_fix_modelo_plantilla;

CREATE PROCEDURE v21_fix_modelo_plantilla()
BEGIN
    DECLARE CONTINUE HANDLER FOR SQLEXCEPTION BEGIN END;

    -- 1) Eliminar FK y restricción única obsoletas (no-op si no existen)
    ALTER TABLE modelo_plantilla DROP FOREIGN KEY fk_modelo_plantilla;
    ALTER TABLE modelo_plantilla DROP CONSTRAINT uk_modelo_articulo_plantilla;

    -- 2) Eliminar columna obsoleta (no-op si no existe)
    ALTER TABLE modelo_plantilla DROP COLUMN id_plantilla;

    -- 3) Agregar nueva columna campos (no-op si ya existe)
    ALTER TABLE modelo_plantilla ADD COLUMN campos TEXT NOT NULL DEFAULT '';

    -- 4) Agregar nueva restricción UNIQUE (no-op si ya existe)
    ALTER TABLE modelo_plantilla ADD CONSTRAINT uk_modelo_articulo UNIQUE (id_articulo);
END;

CALL v21_fix_modelo_plantilla();

DROP PROCEDURE IF EXISTS v21_fix_modelo_plantilla;
