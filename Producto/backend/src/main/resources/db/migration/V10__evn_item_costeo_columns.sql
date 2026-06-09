-- Mueve el vínculo Costeo/SCOS desde la EVN (columnas ARRAY que rompían Flyway)
-- hacia el ítem de la EVN, donde realmente pertenece (1 costeo por ítem tipo OP).

-- Nuevas columnas en el ítem de la EVN (sin FK: desacople cross-módulo con producción)
ALTER TABLE evaluacion_negocio_items
    ADD COLUMN IF NOT EXISTS costeo_id BIGINT;

ALTER TABLE evaluacion_negocio_items
    ADD COLUMN IF NOT EXISTS solicitud_costos_id BIGINT;

-- Eliminar las columnas ARRAY obsoletas de la cabecera de la EVN
ALTER TABLE evaluaciones_negocio
    DROP COLUMN IF EXISTS costeo_id;

ALTER TABLE evaluaciones_negocio
    DROP COLUMN IF EXISTS solicitud_cotizacion_id;
