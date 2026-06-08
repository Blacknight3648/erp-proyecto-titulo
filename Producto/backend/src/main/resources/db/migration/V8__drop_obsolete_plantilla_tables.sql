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

-- ── SCOT Prendas: quitar FK obsoleta solicitud_costos_id ────────────────
-- La relación SCOTPrendaLista→SolicitudCostos se eliminó al separar contextos.
-- Si las columnas existían y H2/MySQL las creó, las eliminamos. SCOT prendas
-- conservan solo la FK a solicitudes_cotizacion (su único owner legítimo).
ALTER TABLE scot_prendas DROP COLUMN IF EXISTS solicitud_costos_id;
ALTER TABLE scot_prendas DROP COLUMN IF EXISTS solicitud_cotizacion_id;
