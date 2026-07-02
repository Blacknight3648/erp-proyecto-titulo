-- Estandariza numero_nv al formato de 7 dígitos (NV-0000001) para los
-- registros que aún usan el formato antiguo de 5 dígitos (NV-00001).
-- El servicio NumeroDocumentoService genera %s-%07d desde siempre;
-- los registros con 5 dígitos provienen del seed o de una versión anterior.

UPDATE notas_venta
SET numeronv = CONCAT('NV-', LPAD(CAST(SUBSTRING(numeronv, 4) AS UNSIGNED), 7, '0'))
WHERE numeronv REGEXP '^NV-[0-9]{5}$';
