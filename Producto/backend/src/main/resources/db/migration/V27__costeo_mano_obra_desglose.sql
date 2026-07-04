DELIMITER $$

CREATE PROCEDURE tmp_add_costeo_mo_columns()
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_schema = DATABASE() AND table_name = 'produccion_costeos' AND column_name = 'mo_prenda'
    ) THEN
        ALTER TABLE produccion_costeos ADD COLUMN mo_prenda NUMERIC(12,2);
    END IF;

    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_schema = DATABASE() AND table_name = 'produccion_costeos' AND column_name = 'mo_cinta'
    ) THEN
        ALTER TABLE produccion_costeos ADD COLUMN mo_cinta NUMERIC(12,2);
    END IF;

    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_schema = DATABASE() AND table_name = 'produccion_costeos' AND column_name = 'mo_costura_sellada'
    ) THEN
        ALTER TABLE produccion_costeos ADD COLUMN mo_costura_sellada NUMERIC(12,2);
    END IF;

    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_schema = DATABASE() AND table_name = 'produccion_costeos' AND column_name = 'mo_acolchado'
    ) THEN
        ALTER TABLE produccion_costeos ADD COLUMN mo_acolchado NUMERIC(12,2);
    END IF;

    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_schema = DATABASE() AND table_name = 'produccion_costeos' AND column_name = 'costo_mo_propia'
    ) THEN
        ALTER TABLE produccion_costeos ADD COLUMN costo_mo_propia NUMERIC(12,2);
    END IF;

    IF NOT EXISTS (
        SELECT 1 FROM information_schema.columns
        WHERE table_schema = DATABASE() AND table_name = 'produccion_costeos' AND column_name = 'costo_gratificacion'
    ) THEN
        ALTER TABLE produccion_costeos ADD COLUMN costo_gratificacion NUMERIC(12,2);
    END IF;
END$$

DELIMITER ;

CALL tmp_add_costeo_mo_columns();

DROP PROCEDURE tmp_add_costeo_mo_columns;
