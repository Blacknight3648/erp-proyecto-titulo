-- =============================================================================
-- FASE 3: Restricción Not Null para evaluacion_negocio_id en notas_venta
-- =============================================================================
ALTER TABLE notas_venta
    MODIFY COLUMN evaluacion_negocio_id BIGINT NOT NULL;
