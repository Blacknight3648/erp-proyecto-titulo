-- =============================================================================
-- FASE 4: Orden de Servicio (OS) - Constraints e índices complementarios
-- =============================================================================
-- Tablas creadas por Hibernate:
--   * produccion_ordenes_servicio
--   * produccion_despachos_os
--   * produccion_recepciones_os
--
-- TODO (deuda para producción / Postgres):
--   * FK os.op_id          ON DELETE RESTRICT
--   * FK os.proveedor_id   ON DELETE RESTRICT
--   * FK despacho.os_id    ON DELETE CASCADE
--   * FK recepcion.os_id   ON DELETE CASCADE
-- =============================================================================

CREATE INDEX idx_os_estado
    ON produccion_ordenes_servicio (estado);

CREATE INDEX idx_os_op
    ON produccion_ordenes_servicio (op_id);

CREATE INDEX idx_os_proveedor
    ON produccion_ordenes_servicio (proveedor_id);

CREATE INDEX idx_os_tipo
    ON produccion_ordenes_servicio (tipo_servicio);

CREATE INDEX idx_desp_os
    ON produccion_despachos_os (os_id);

CREATE INDEX idx_desp_fecha
    ON produccion_despachos_os (fecha_despacho);

CREATE INDEX idx_rec_os
    ON produccion_recepciones_os (os_id);

CREATE INDEX idx_rec_fecha
    ON produccion_recepciones_os (fecha_recepcion);
