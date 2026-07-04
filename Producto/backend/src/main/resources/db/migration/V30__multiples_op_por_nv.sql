-- =============================================================================
-- V27: Múltiples OP por NV — cada ítem tipo OP genera su propia Orden de
--      Producción (con su propio Costeo), en vez de agrupar todos los ítems
--      OP de una NV en una única OP compartida.
-- =============================================================================
-- Se retiran las restricciones UNIQUE(nota_venta_id) que reforzaban la regla
-- anterior "una NV solo puede tener una OP" / "un Costeo auto-creado por NV".
-- Esa regla ahora se relaja intencionalmente: una NV puede tener varias OP,
-- cada una con su propio Costeo (heredado de la EVN, elegido manualmente, o
-- un Costeo vacío de respaldo).
-- =============================================================================
CREATE INDEX idx_op_nota_venta ON orden_produccion(nota_venta_id);
ALTER TABLE orden_produccion
    DROP INDEX uq_op_nota_venta;

CREATE INDEX idx_costeo_nota_venta ON produccion_costeos(nota_venta_id);
ALTER TABLE produccion_costeos
    DROP INDEX uq_costeo_nota_venta;
