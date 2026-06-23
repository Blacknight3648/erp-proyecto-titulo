-- =============================================================================
-- V15: Integridad referencial OP ↔ Costeo ↔ ItemNV
-- =============================================================================
-- Objetivo: garantizar que toda Orden de Producción tenga un Costeo asociado,
--           y que cada ItemNV de tipo OP registre la OP que le fue generada.
-- =============================================================================

-- -----------------------------------------------------------------------------
-- 1. Trazabilidad ItemNV → OP
--    op_id ya existe en notas_venta_items (creada por Hibernate DDL anterior).
-- -----------------------------------------------------------------------------

-- -----------------------------------------------------------------------------
-- 2. Costeo auto-creado: soporte para costeos sin SolicitudCostos
--    - solicitud_costos_id pasa a ser nullable (los costeos auto-generados
--      para NVs sin EVN vinculada no tienen solicitud de costos).
--    - nota_venta_id identifica al Costeo auto-creado para una NV específica.
--      La restricción UNIQUE garantiza que solo exista un Costeo auto-creado
--      por NV (idempotencia ACID en CrearOrdenProduccionUseCase).
-- -----------------------------------------------------------------------------
ALTER TABLE produccion_costeos
    MODIFY COLUMN solicitud_costos_id BIGINT NULL;

-- nota_venta_id ya existe en produccion_costeos (creada por Hibernate DDL anterior).
-- Flyway garantiza que esta migración se ejecuta una sola vez (idempotencia por historial),
-- por lo que no se requiere IF NOT EXISTS (no soportado por MySQL en CREATE INDEX).
CREATE UNIQUE INDEX uq_costeo_nota_venta ON produccion_costeos(nota_venta_id);

-- -----------------------------------------------------------------------------
-- 3. Integridad de OP: toda OP debe tener un costeo_version_id válido.
--    PRERREQUISITO: ejecutar este ALTER solo si no existen filas con NULL en
--    costeo_version_id. Si la BD tiene datos previos sin costeo, ejecutar
--    primero el script de limpieza correspondiente.
-- -----------------------------------------------------------------------------
ALTER TABLE orden_produccion
    MODIFY COLUMN costeo_version_id BIGINT NOT NULL;
