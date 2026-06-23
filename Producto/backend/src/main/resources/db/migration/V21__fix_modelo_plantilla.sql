-- ─────────────────────────────────────────────────────────────────────────
-- V21: Alinear la tabla modelo_plantilla con la entidad JPA
-- Eliminar id_plantilla y agregar la columna campos (CSV) con restricción UNIQUE por artículo.
-- ─────────────────────────────────────────────────────────────────────────

-- 1) Eliminar FK y restricción única obsoletas
ALTER TABLE modelo_plantilla DROP FOREIGN KEY fk_modelo_plantilla;
ALTER TABLE modelo_plantilla DROP CONSTRAINT uk_modelo_articulo_plantilla;

-- 2) Eliminar columna obsoleta
ALTER TABLE modelo_plantilla DROP COLUMN id_plantilla;

-- 3) Agregar nueva columna campos y nueva restricción UNIQUE
ALTER TABLE modelo_plantilla ADD COLUMN campos TEXT NOT NULL;
ALTER TABLE modelo_plantilla ADD CONSTRAINT uk_modelo_articulo UNIQUE (id_articulo);
