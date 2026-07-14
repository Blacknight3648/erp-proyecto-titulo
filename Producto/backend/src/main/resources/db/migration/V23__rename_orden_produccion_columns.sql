-- =============================================================================
-- V23: Alinear nombres físicos de columnas con el mapeo JPA explícito
-- =============================================================================
-- Las entidades OrdenProduccionJpaEntity / OrdenProduccionItemJpaEntity
-- declaraban antes idOP/numeroOP/idOPItem sin @Column(name=...), por lo que
-- Hibernate generó columnas sin guion bajo (idop, numeroop, idopitem).
-- Al agregar @Column(name = "id_op" / "numero_op" / "id_op_item") explícito,
-- ddl-auto=update no renombra columnas existentes: solo agrega "numero_op"
-- como columna nueva (vacía, duplicada de "numeroop") y no puede agregar
-- "id_op" porque es la PK. Esta migración corrige el desfase.
--
-- Escrita en forma idempotente (verifica el estado real de cada columna antes
-- de actuar) porque distintos entornos pueden llegar a ella en distintos
-- estados intermedios (por ejemplo, si el rename ya se aplicó manualmente).
-- =============================================================================

-- orden_produccion.idop -> id_op (PK)
SET @stmt = (SELECT IF(
    EXISTS(SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'orden_produccion' AND column_name = 'idop'),
    'ALTER TABLE orden_produccion CHANGE COLUMN idop id_op BIGINT NOT NULL AUTO_INCREMENT',
    'SELECT 1'));
PREPARE stmt FROM @stmt; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- orden_produccion: eliminar la columna "numero_op" duplicada agregada por ddl-auto
-- (solo si todavía coexiste junto con la columna original "numeroop").
SET @stmt = (SELECT IF(
    EXISTS(SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'orden_produccion' AND column_name = 'numeroop')
    AND EXISTS(SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'orden_produccion' AND column_name = 'numero_op'),
    'ALTER TABLE orden_produccion DROP COLUMN numero_op',
    'SELECT 1'));
PREPARE stmt FROM @stmt; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- orden_produccion.numeroop -> numero_op
SET @stmt = (SELECT IF(
    EXISTS(SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'orden_produccion' AND column_name = 'numeroop'),
    'ALTER TABLE orden_produccion CHANGE COLUMN numeroop numero_op VARCHAR(20) NOT NULL',
    'SELECT 1'));
PREPARE stmt FROM @stmt; EXECUTE stmt; DEALLOCATE PREPARE stmt;

-- produccion_orden_items.idopitem -> id_op_item (PK)
SET @stmt = (SELECT IF(
    EXISTS(SELECT 1 FROM information_schema.columns WHERE table_schema = DATABASE() AND table_name = 'produccion_orden_items' AND column_name = 'idopitem'),
    'ALTER TABLE produccion_orden_items CHANGE COLUMN idopitem id_op_item BIGINT NOT NULL AUTO_INCREMENT',
    'SELECT 1'));
PREPARE stmt FROM @stmt; EXECUTE stmt; DEALLOCATE PREPARE stmt;
