-- =============================================================================
-- V20: Estado CERRADA para la Evaluación de Negocio (EVN)
-- =============================================================================
-- Incorpora el estado terminal CERRADA al ciclo de vida de la EVN. Una EVN solo
-- puede cerrarse desde ADJUDICADA; una vez CERRADA deja de poder usarse como
-- plantilla y no admite la generación de nuevas Notas de Venta.
--
-- La columna `estado` (VARCHAR(20) NOT NULL) de `evaluaciones_negocio` ya existe
-- y admite el nuevo valor 'CERRADA', por lo que NO se requiere cambio de esquema.
-- El dominio (EstadoEVN), el mapper, los DTOs y la API ya manejan el valor nuevo.
--
-- Migración de datos defensiva: garantiza un estado inicial válido para EVN
-- existentes que, por datos legacy, pudieran tener el estado vacío. Es
-- idempotente y compatible con H2 (dev/test) y MySQL (prod): si no hay filas
-- afectadas, no realiza cambios.
-- =============================================================================

UPDATE evaluaciones_negocio
SET    estado = 'BORRADOR'
WHERE  estado IS NULL OR TRIM(estado) = '';
