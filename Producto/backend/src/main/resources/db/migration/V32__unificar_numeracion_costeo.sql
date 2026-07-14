-- =============================================================================
-- V32: Unificar numeración de Costeo a COST-XXXXXXX
-- =============================================================================
-- Hasta ahora convivían varios formatos para el mismo tipo de documento:
--   - "PRE-<numero SCOS>": placeholder asignado al auto-crear el pre-costeo de
--     una Solicitud de Costos (nunca se reemplazaba por un número real).
--   - "C-XXXXXXX": correlativo legacy usado por CosteoServiceImpl.
--   - "COST-XXXXXXX": correlativo usado por CrearOrdenProduccionUseCase al
--     crear el costeo vacío de respaldo de una OP.
-- Esta migración renumera los que no siguen "COST-XXXXXXX" y sincroniza el
-- contador atómico. Debe ejecutarse manualmente (Flyway está deshabilitado
-- en este proyecto — ver spring.flyway.enabled=false en application*.properties),
-- igual que V15/V16/V30.
--
-- Nota de implementación: se usa una tabla temporal con columnas de tipo
-- explícito (BIGINT) en vez de una subconsulta derivada con variable de
-- sesión directamente en el UPDATE ... JOIN. Probado en MySQL 8: al usar
-- `@variable := @variable + 1` dentro de una subconsulta derivada de un
-- UPDATE, el tipo de la expresión se infiere como DECIMAL de altísima
-- precisión (ej. "2.00000000000000000000000000000000000000") y el UPDATE
-- no aplica el cambio (Changed: 0). Materializar el plan de renumeración en
-- una tabla temporal con tipos fijos evita el problema.
-- =============================================================================

DROP TEMPORARY TABLE IF EXISTS tmp_renumeracion_costeo;

SET @siguiente = (
    SELECT COALESCE(MAX(CAST(SUBSTRING(numero_costeo, 6) AS UNSIGNED)), 0)
    FROM produccion_costeos
    WHERE numero_costeo REGEXP '^COST-[0-9]+$'
);

CREATE TEMPORARY TABLE tmp_renumeracion_costeo (id_costeo BIGINT, nuevo_numero BIGINT);

INSERT INTO tmp_renumeracion_costeo (id_costeo, nuevo_numero)
SELECT id_costeo, CAST((@siguiente := @siguiente + 1) AS UNSIGNED)
FROM produccion_costeos
WHERE numero_costeo NOT REGEXP '^COST-[0-9]+$'
ORDER BY id_costeo;

UPDATE produccion_costeos c
JOIN tmp_renumeracion_costeo t ON t.id_costeo = c.id_costeo
SET c.numero_costeo = CONCAT('COST-', LPAD(t.nuevo_numero, 7, '0'));

-- Sincroniza el contador atómico para que las próximas numeraciones "COST"
-- continúen justo después del último número recién asignado.
INSERT INTO document_counter (tipo, ultimo_numero)
VALUES ('COST', @siguiente)
ON DUPLICATE KEY UPDATE ultimo_numero = GREATEST(ultimo_numero, @siguiente);

DROP TEMPORARY TABLE tmp_renumeracion_costeo;
