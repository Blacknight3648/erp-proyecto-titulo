-- =============================================================================
-- V31: Ensancha evaluaciones_negocio.porcentaje_comision
-- =============================================================================
-- La columna quedó con una precisión más angosta que la declarada en la entidad
-- JPA (DECIMAL(5,2)), probablemente por una versión anterior del modelo — y
-- ddl-auto=update nunca ensancha columnas ya existentes, solo agrega las que
-- faltan. Esto provocaba "Out of range value for column 'porcentaje_comision'"
-- al guardar una EVN con un valor de comisión válido.
-- =============================================================================
ALTER TABLE evaluaciones_negocio
    MODIFY COLUMN porcentaje_comision DECIMAL(5,2);
