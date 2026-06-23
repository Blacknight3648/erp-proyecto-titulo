-- ─────────────────────────────────────────────────────────────────────────
-- V21: Alinear la tabla modelo_plantilla con la entidad JPA
-- Safe para instalación fresca (Hibernate ya creó el schema actualizado)
-- y para bases de datos existentes con el schema anterior.
-- ─────────────────────────────────────────────────────────────────────────

-- 1) Drop FK si existe (MySQL no soporta DROP FOREIGN KEY IF EXISTS, usar prepared statement)
SET @fk_exists = (
    SELECT COUNT(*) FROM information_schema.TABLE_CONSTRAINTS
    WHERE CONSTRAINT_SCHEMA = DATABASE()
      AND TABLE_NAME = 'modelo_plantilla'
      AND CONSTRAINT_NAME = 'fk_modelo_plantilla'
      AND CONSTRAINT_TYPE = 'FOREIGN KEY'
);
SET @sql = IF(@fk_exists > 0,
    'ALTER TABLE modelo_plantilla DROP FOREIGN KEY fk_modelo_plantilla',
    'SELECT 1');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 2) Drop unique constraint obsoleta si existe
SET @uc_exists = (
    SELECT COUNT(*) FROM information_schema.TABLE_CONSTRAINTS
    WHERE CONSTRAINT_SCHEMA = DATABASE()
      AND TABLE_NAME = 'modelo_plantilla'
      AND CONSTRAINT_NAME = 'uk_modelo_articulo_plantilla'
);
SET @sql = IF(@uc_exists > 0,
    'ALTER TABLE modelo_plantilla DROP INDEX uk_modelo_articulo_plantilla',
    'SELECT 1');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;

-- 3) Drop columna obsoleta si existe (MySQL 8.0.27+)
ALTER TABLE modelo_plantilla DROP COLUMN IF EXISTS id_plantilla;

-- 4) Agregar columna campos si no existe (TEXT no admite DEFAULT, tabla vacía en fresh install)
ALTER TABLE modelo_plantilla ADD COLUMN IF NOT EXISTS campos TEXT NOT NULL;

-- 5) Agregar restricción UNIQUE si no existe
SET @uk_exists = (
    SELECT COUNT(*) FROM information_schema.TABLE_CONSTRAINTS
    WHERE CONSTRAINT_SCHEMA = DATABASE()
      AND TABLE_NAME = 'modelo_plantilla'
      AND CONSTRAINT_NAME = 'uk_modelo_articulo'
);
SET @sql = IF(@uk_exists = 0,
    'ALTER TABLE modelo_plantilla ADD CONSTRAINT uk_modelo_articulo UNIQUE (id_articulo)',
    'SELECT 1');
PREPARE stmt FROM @sql;
EXECUTE stmt;
DEALLOCATE PREPARE stmt;
