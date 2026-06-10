-- ─────────────────────────────────────────────────────────────────────────
-- V8: Eliminar sistema antiguo de plantillas
--
-- Se eliminan las tablas legacy de SCOSPlantilla y ConfiguracionPlantilla,
-- reemplazadas por el modelo normalizado creado en V7.
--
-- IMPORTANTE: el orden importa por FKs. Se eliminan primero las tablas
-- satélite (ElementCollection) y luego las principales.
-- ─────────────────────────────────────────────────────────────────────────

-- ── ConfiguracionPlantilla (sistema viejo del frontend PlantillasPanel) ──
DROP TABLE IF EXISTS configuracion_plantilla_telas;
DROP TABLE IF EXISTS configuracion_plantilla_accesorios;
DROP TABLE IF EXISTS configuracion_plantilla_campos;
DROP TABLE IF EXISTS configuracion_plantillas;

-- ── SCOSPlantilla (embebido viejo en SolicitudCostos) ───────────────────
DROP TABLE IF EXISTS scos_plantilla_material_vinculos;
DROP TABLE IF EXISTS scos_plantilla_campos_activos;
DROP TABLE IF EXISTS scos_plantilla_telas;
DROP TABLE IF EXISTS scos_plantilla_accesorios;
DROP TABLE IF EXISTS scos_plantilla_logotipos;
DROP TABLE IF EXISTS scos_plantilla;

-- ── SCOT Prendas: limpieza de columnas legacy ───────────────────────────
-- Antes se hacía aquí `ALTER TABLE scot_prendas DROP COLUMN IF EXISTS ...`.
-- Se retiró: MySQL no soporta DROP COLUMN IF EXISTS, y en un despliegue fresco
-- (greenfield MySQL / H2 en memoria) esas columnas legacy nunca son creadas por
-- las entidades actuales, por lo que el DROP era un no-op. Si en el futuro se
-- migra una instancia con esas columnas, ejecutar el DROP manualmente.
