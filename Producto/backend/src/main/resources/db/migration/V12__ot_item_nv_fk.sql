-- =============================================================================
-- V12: FK directa OrdenTrabajo -> ItemNV y renombre del flag de OT
-- =============================================================================
-- Reemplaza la referencia débil de la Orden de Trabajo al ítem de la Nota de
-- Venta (nota_venta_id + nro_item, datos sueltos) por una FK explícita
-- `item_nv_id` -> notas_venta_items(id_item_nv). Mismo patrón declarativo que
-- V1/V9: la columna `item_nv_id` la crea Hibernate (ddl-auto=update) a partir de
-- la entidad; aquí solo agregamos backfill + integridad referencial, sin acoplar
-- el dominio Java vía @ManyToOne.
--
-- COMPATIBILIDAD: el motor productivo es MySQL (InnoDB). Flyway corre el mismo
-- script en dev (H2). Por eso esta sección usa solo SQL soportado por ambos:
--   * Evitamos `CREATE INDEX IF NOT EXISTS` (MySQL no lo soporta). InnoDB/H2
--     autocrean el índice de la FK al declararla, así que no hace falta.
--   * El UPDATE con subconsulta correlacionada es portable a MySQL/H2/Postgres.
--   * El renombre de `is_personalized` -> requiere_ot es destructivo y depende
--     del dato existente en prod (en H2 fresco la columna antigua no existe), por
--     lo que va en un bloque guardado que verifica su existencia primero.
-- =============================================================================

-- 1) Backfill: resolver item_nv_id de las OT existentes a partir de la antigua
--    referencia (nota_venta_id + nro_item). No-op si no hay filas pendientes
--    (idempotente por su propio WHERE item_nv_id IS NULL).
UPDATE produccion_orden_trabajo ot
SET item_nv_id = (
    SELECT i.id_item_nv
    FROM notas_venta_items i
    WHERE i.nota_venta_id = ot.nota_venta_id
      AND i.nro_item = ot.nro_item
)
WHERE ot.item_nv_id IS NULL
  AND EXISTS (
    SELECT 1 FROM notas_venta_items i
    WHERE i.nota_venta_id = ot.nota_venta_id
      AND i.nro_item = ot.nro_item
  );

-- 2) Integridad referencial + limpieza de la columna vieja `is_personalized`,
--    guardadas contra INFORMATION_SCHEMA por si un despliegue previo parcial ya
--    dejó la FK creada, o si la columna vieja ya no existe.
DROP PROCEDURE IF EXISTS v12_fix_ot_item_nv;

DELIMITER $$
CREATE PROCEDURE v12_fix_ot_item_nv()
BEGIN
    -- FK blanda (SET NULL): la OT es un registro vivo de producción; si el ítem
    -- de NV desaparece, conservamos la OT con la referencia desnormalizada
    -- (nota_venta_id + nro_item) como respaldo.
    IF NOT EXISTS (
        SELECT * FROM INFORMATION_SCHEMA.TABLE_CONSTRAINTS
        WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'produccion_orden_trabajo' AND CONSTRAINT_NAME = 'fk_ot_item_nv'
    ) THEN
        ALTER TABLE produccion_orden_trabajo
            ADD CONSTRAINT fk_ot_item_nv
            FOREIGN KEY (item_nv_id) REFERENCES notas_venta_items (id_item_nv) ON DELETE SET NULL;
    END IF;

    -- El dominio renombró `generaOt`/`isPersonalized` -> `requiereOt`. Si la
    -- columna antigua `is_personalized` todavía existe (prod con datos previos),
    -- se migran sus valores a `requiere_ot` (ya creada por Hibernate) y se
    -- elimina la columna huérfana.
    IF EXISTS (
        SELECT * FROM INFORMATION_SCHEMA.COLUMNS
        WHERE TABLE_SCHEMA = DATABASE() AND TABLE_NAME = 'notas_venta_items' AND COLUMN_NAME = 'is_personalized'
    ) THEN
        UPDATE notas_venta_items SET requiere_ot = is_personalized WHERE requiere_ot IS NULL;
        ALTER TABLE notas_venta_items DROP COLUMN is_personalized;
    END IF;
END $$
DELIMITER ;

CALL v12_fix_ot_item_nv();
DROP PROCEDURE v12_fix_ot_item_nv;
