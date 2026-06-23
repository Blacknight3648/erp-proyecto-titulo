-- =============================================================================
-- V16: Unicidad OP → NV
-- =============================================================================
-- Una Nota de Venta solo puede generar UNA Orden de Producción.
-- Este constraint refuerza a nivel de base de datos la regla de negocio
-- ya validada en CrearOrdenProduccionUseCase.
-- =============================================================================
ALTER TABLE orden_produccion
    ADD CONSTRAINT uq_op_nota_venta UNIQUE (nota_venta_id);
