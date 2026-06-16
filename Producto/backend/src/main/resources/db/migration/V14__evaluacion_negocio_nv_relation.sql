-- =============================================================================
-- FASE 3: Restricción Not Null para evaluacion_negocio_id en notas_venta
-- =============================================================================
ALTER TABLE notas_venta
    ALTER COLUMN evaluacion_negocio_id SET NOT NULL;
