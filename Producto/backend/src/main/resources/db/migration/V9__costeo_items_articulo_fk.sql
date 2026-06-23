-- =============================================================================
-- V9: Normalización de items a Articulo (FK articulo_id)
-- =============================================================================
-- Reemplaza el antiguo atributo local `insumo_id` (un id suelto, sin tabla
-- propia) por una FK normalizada `articulo_id` -> articulo(id_articulo) en toda
-- la cadena Costeo -> CosteoVersion -> HojaCompra -> OrdenCompra.
--
-- La columna `articulo_id` la crea Hibernate (ddl-auto=update) a partir de las
-- entidades; aquí solo agregamos la integridad referencial declarativa a nivel
-- BD, sin acoplar el dominio Java vía @ManyToOne (mismo patrón que V1).
--
-- Criterio de borrado según la naturaleza de cada registro:
--   * CosteoItem        -> FK dura   (RESTRICT): línea viva; protege un artículo en uso.
--   * CosteoItemVersion -> FK blanda (SET NULL): snapshot histórico; conserva nombre_insumo.
--   * HojaCompraItem    -> FK blanda (SET NULL): derivado de compra; conserva nombre_insumo.
--   * OrdenCompraItem   -> FK blanda (SET NULL): derivado de compra; conserva nombre_insumo.
--
-- Nota: `articulo` usa soft-delete (columna `activo`), por lo que las filas no
-- desaparecen físicamente y la FK es segura incluso en los snapshots.
-- =============================================================================

ALTER TABLE produccion_costeo_items
    ADD CONSTRAINT fk_costeo_item_articulo
    FOREIGN KEY (articulo_id) REFERENCES articulo (id_articulo) ON DELETE RESTRICT;

ALTER TABLE produccion_costeo_item_versiones
    ADD CONSTRAINT fk_costeo_item_version_articulo
    FOREIGN KEY (articulo_id) REFERENCES articulo (id_articulo) ON DELETE SET NULL;

ALTER TABLE produccion_hoja_compra_items
    ADD CONSTRAINT fk_hc_item_articulo
    FOREIGN KEY (articulo_id) REFERENCES articulo (id_articulo) ON DELETE SET NULL;

ALTER TABLE produccion_orden_compra_items
    ADD CONSTRAINT fk_oc_item_articulo
    FOREIGN KEY (articulo_id) REFERENCES articulo (id_articulo) ON DELETE SET NULL;
