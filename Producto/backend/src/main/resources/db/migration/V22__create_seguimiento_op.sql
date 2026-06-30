-- ─────────────────────────────────────────────────────────────────────────
-- V8: Crear tabla de seguimiento_op
--
-- Usa IF NOT EXISTS para ser idempotente.
-- ─────────────────────────────────────────────────────────────────────────

CREATE TABLE IF NOT EXISTS produccion_seguimiento_op (
    id_seguimiento BIGINT AUTO_INCREMENT PRIMARY KEY,
    orden_produccion_id BIGINT NOT NULL UNIQUE,
    fecha_recepcion_op DATE,
    fin_tizado DATE,
    fecha_estado_oc_mp DATE,
    recepcion_compras DATE,
    inicio_corte DATE,
    fin_corte DATE,
    inicio_logo DATE,
    estado_ida_logo VARCHAR(50),
    regreso_logo DATE,
    estado_rec_logo VARCHAR(50),
    inicio_taller_externo DATE,
    fin_taller_externo DATE,
    calidad_taller VARCHAR(50),
    obs_taller TEXT,
    fin_terminacion DATE,
    fin_personalizado DATE,
    CONSTRAINT fk_seguimiento_op FOREIGN KEY (orden_produccion_id)
        REFERENCES orden_produccion(id_op) ON DELETE CASCADE
);
