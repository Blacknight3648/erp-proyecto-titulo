-- =============================================================================
-- FASE 2: Integridad referencial declarativa (Hard Foreign Keys a nivel BD)
-- =============================================================================
-- Estos constraints garantizan que no existan registros huerfanos entre
-- agregados, sin acoplar el dominio Java via @ManyToOne. El codigo sigue
-- usando referencias por ID (Long notaVentaId, Long ordenProduccionId, etc.),
-- manteniendo el principio DDD de que los agregados no se referencian por
-- objeto. La integridad la garantiza la base de datos, no el ORM.
--
-- Notas:
--   * Spring Boot 3.x convierte campos JPA `idNV`, `idOP`, `idEVN`, `idOT`,
--     `idSCOS` a snake_case via SpringPhysicalNamingStrategy, separando cada
--     letra mayuscula consecutiva: `id_n_v`, `id_o_p`, etc. Si en tu instancia
--     los nombres son distintos (p.ej. `id_nv`), ajustar las REFERENCES.
--   * No agregamos FK a `clientes` por ahora; queda como migration futura.
-- =============================================================================

ALTER TABLE produccion_ordenes
    ADD CONSTRAINT fk_op_nota_venta
    FOREIGN KEY (nota_venta_id) REFERENCES notas_venta (idnv);

ALTER TABLE produccion_orden_trabajo
    ADD CONSTRAINT fk_ot_nota_venta
    FOREIGN KEY (nota_venta_id) REFERENCES notas_venta (idnv);

ALTER TABLE produccion_orden_trabajo
    ADD CONSTRAINT fk_ot_orden_produccion
    FOREIGN KEY (orden_produccion_id) REFERENCES produccion_ordenes (idop);

ALTER TABLE produccion_costeos
    ADD CONSTRAINT fk_costeo_solicitud_costos
    FOREIGN KEY (solicitud_costos_id) REFERENCES solicitudes_costos (idscos);

ALTER TABLE evaluaciones_negocio
    ADD CONSTRAINT fk_evn_costeo
    FOREIGN KEY (costeo_id) REFERENCES produccion_costeos (id_costeo);

ALTER TABLE notas_venta
    ADD CONSTRAINT fk_nv_evaluacion_negocio
    FOREIGN KEY (evaluacion_negocio_id) REFERENCES evaluaciones_negocio (idevn);
