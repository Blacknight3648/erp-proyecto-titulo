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
--   * Cada ADD CONSTRAINT esta envuelto en un guard IF NOT EXISTS contra
--     information_schema.TABLE_CONSTRAINTS: esta migracion corre en cada
--     arranque (ver FlywayConfig.java, que la ejecuta manualmente sin
--     depender de spring.flyway.enabled) y la base local ya pudo haber
--     recibido estos constraints por otra via (p.ej. una corrida manual de
--     schema_completo_mysql.sql), asi que debe ser segura de re-ejecutar
--     tantas veces como sea necesario, igual que V27/V28/V29.
-- =============================================================================

DELIMITER $$

CREATE PROCEDURE tmp_add_v1_foreign_keys()
BEGIN
    -- =========================================================================
    -- MÓDULO: PRODUCCIÓN (FLUJOS PRINCIPALES Y COSTEO)
    -- =========================================================================
    IF NOT EXISTS (SELECT 1 FROM information_schema.TABLE_CONSTRAINTS WHERE CONSTRAINT_SCHEMA = DATABASE() AND TABLE_NAME = 'orden_produccion' AND CONSTRAINT_NAME = 'fk_op_nota_venta') THEN
        ALTER TABLE orden_produccion
            ADD CONSTRAINT fk_op_nota_venta
            FOREIGN KEY (nota_venta_id) REFERENCES notas_venta (id_nv);
    END IF;

    IF NOT EXISTS (SELECT 1 FROM information_schema.TABLE_CONSTRAINTS WHERE CONSTRAINT_SCHEMA = DATABASE() AND TABLE_NAME = 'produccion_orden_trabajo' AND CONSTRAINT_NAME = 'fk_ot_nota_venta') THEN
        ALTER TABLE produccion_orden_trabajo
            ADD CONSTRAINT fk_ot_nota_venta
            FOREIGN KEY (nota_venta_id) REFERENCES notas_venta (id_nv);
    END IF;

    IF NOT EXISTS (SELECT 1 FROM information_schema.TABLE_CONSTRAINTS WHERE CONSTRAINT_SCHEMA = DATABASE() AND TABLE_NAME = 'produccion_orden_trabajo' AND CONSTRAINT_NAME = 'fk_ot_orden_produccion') THEN
        ALTER TABLE produccion_orden_trabajo
            ADD CONSTRAINT fk_ot_orden_produccion
            FOREIGN KEY (orden_produccion_id) REFERENCES orden_produccion (id_op);
    END IF;

    IF NOT EXISTS (SELECT 1 FROM information_schema.TABLE_CONSTRAINTS WHERE CONSTRAINT_SCHEMA = DATABASE() AND TABLE_NAME = 'produccion_costeos' AND CONSTRAINT_NAME = 'fk_costeo_solicitud_costos') THEN
        ALTER TABLE produccion_costeos
            ADD CONSTRAINT fk_costeo_solicitud_costos
            FOREIGN KEY (solicitud_costos_id) REFERENCES solicitudes_costos (id_scos);
    END IF;

    -- fk_evn_costeo eliminada: costeo_id es ahora BIGINT ARRAY (List<Long>)
    -- Un ARRAY no puede referenciar una columna escalar con FOREIGN KEY.
    -- La integridad EVN ↔ Costeo se gestiona a nivel de aplicación.

    IF NOT EXISTS (SELECT 1 FROM information_schema.TABLE_CONSTRAINTS WHERE CONSTRAINT_SCHEMA = DATABASE() AND TABLE_NAME = 'notas_venta' AND CONSTRAINT_NAME = 'fk_nv_evaluacion_negocio') THEN
        ALTER TABLE notas_venta
            ADD CONSTRAINT fk_nv_evaluacion_negocio
            FOREIGN KEY (evaluacion_negocio_id) REFERENCES evaluaciones_negocio (id_evn);
    END IF;

    IF NOT EXISTS (SELECT 1 FROM information_schema.TABLE_CONSTRAINTS WHERE CONSTRAINT_SCHEMA = DATABASE() AND TABLE_NAME = 'produccion_costeo_versiones' AND CONSTRAINT_NAME = 'fk_costeo_version_costeo') THEN
        ALTER TABLE produccion_costeo_versiones
            ADD CONSTRAINT fk_costeo_version_costeo
            FOREIGN KEY (costeo_id) REFERENCES produccion_costeos (id_costeo);
    END IF;

    -- =========================================================================
    -- MÓDULO: GESTIÓN DE USUARIOS
    -- =========================================================================
    IF NOT EXISTS (SELECT 1 FROM information_schema.TABLE_CONSTRAINTS WHERE CONSTRAINT_SCHEMA = DATABASE() AND TABLE_NAME = 'vendedores' AND CONSTRAINT_NAME = 'fk_vendedor_usuario') THEN
        ALTER TABLE vendedores
            ADD CONSTRAINT fk_vendedor_usuario
            FOREIGN KEY (id_usuario) REFERENCES usuarios (id_usuario) ON DELETE CASCADE;
    END IF;

    IF NOT EXISTS (SELECT 1 FROM information_schema.TABLE_CONSTRAINTS WHERE CONSTRAINT_SCHEMA = DATABASE() AND TABLE_NAME = 'roles' AND CONSTRAINT_NAME = 'fk_role_area') THEN
        ALTER TABLE roles
            ADD CONSTRAINT fk_role_area
            FOREIGN KEY (area_id) REFERENCES areas (id_area);
    END IF;

    IF NOT EXISTS (SELECT 1 FROM information_schema.TABLE_CONSTRAINTS WHERE CONSTRAINT_SCHEMA = DATABASE() AND TABLE_NAME = 'usuarios_roles' AND CONSTRAINT_NAME = 'fk_ur_usuario') THEN
        ALTER TABLE usuarios_roles
            ADD CONSTRAINT fk_ur_usuario
            FOREIGN KEY (user_id) REFERENCES usuarios (id_usuario) ON DELETE CASCADE;
    END IF;

    IF NOT EXISTS (SELECT 1 FROM information_schema.TABLE_CONSTRAINTS WHERE CONSTRAINT_SCHEMA = DATABASE() AND TABLE_NAME = 'usuarios_roles' AND CONSTRAINT_NAME = 'fk_ur_role') THEN
        ALTER TABLE usuarios_roles
            ADD CONSTRAINT fk_ur_role
            FOREIGN KEY (role_id) REFERENCES roles (id_role) ON DELETE CASCADE;
    END IF;

    IF NOT EXISTS (SELECT 1 FROM information_schema.TABLE_CONSTRAINTS WHERE CONSTRAINT_SCHEMA = DATABASE() AND TABLE_NAME = 'usuarios_areas' AND CONSTRAINT_NAME = 'fk_ua_usuario') THEN
        ALTER TABLE usuarios_areas
            ADD CONSTRAINT fk_ua_usuario
            FOREIGN KEY (user_id) REFERENCES usuarios (id_usuario) ON DELETE CASCADE;
    END IF;

    IF NOT EXISTS (SELECT 1 FROM information_schema.TABLE_CONSTRAINTS WHERE CONSTRAINT_SCHEMA = DATABASE() AND TABLE_NAME = 'usuarios_areas' AND CONSTRAINT_NAME = 'fk_ua_area') THEN
        ALTER TABLE usuarios_areas
            ADD CONSTRAINT fk_ua_area
            FOREIGN KEY (area_id) REFERENCES areas (id_area) ON DELETE CASCADE;
    END IF;

    IF NOT EXISTS (SELECT 1 FROM information_schema.TABLE_CONSTRAINTS WHERE CONSTRAINT_SCHEMA = DATABASE() AND TABLE_NAME = 'rol_permisos' AND CONSTRAINT_NAME = 'fk_rp_role') THEN
        ALTER TABLE rol_permisos
            ADD CONSTRAINT fk_rp_role
            FOREIGN KEY (rol_id) REFERENCES roles (id_role) ON DELETE CASCADE;
    END IF;

    IF NOT EXISTS (SELECT 1 FROM information_schema.TABLE_CONSTRAINTS WHERE CONSTRAINT_SCHEMA = DATABASE() AND TABLE_NAME = 'rol_permisos' AND CONSTRAINT_NAME = 'fk_rp_permiso') THEN
        ALTER TABLE rol_permisos
            ADD CONSTRAINT fk_rp_permiso
            FOREIGN KEY (permiso_id) REFERENCES permisos (id) ON DELETE CASCADE;
    END IF;

    -- =========================================================================
    -- MÓDULO: CLIENTES Y PROVEEDORES
    -- =========================================================================
    IF NOT EXISTS (SELECT 1 FROM information_schema.TABLE_CONSTRAINTS WHERE CONSTRAINT_SCHEMA = DATABASE() AND TABLE_NAME = 'giros' AND CONSTRAINT_NAME = 'fk_giro_rubro') THEN
        ALTER TABLE giros
            ADD CONSTRAINT fk_giro_rubro
            FOREIGN KEY (rubro_id) REFERENCES rubros (rubro_id);
    END IF;

    IF NOT EXISTS (SELECT 1 FROM information_schema.TABLE_CONSTRAINTS WHERE CONSTRAINT_SCHEMA = DATABASE() AND TABLE_NAME = 'clientes' AND CONSTRAINT_NAME = 'fk_cliente_giro') THEN
        ALTER TABLE clientes
            ADD CONSTRAINT fk_cliente_giro
            FOREIGN KEY (fk_giro) REFERENCES giros (giro_id);
    END IF;

    IF NOT EXISTS (SELECT 1 FROM information_schema.TABLE_CONSTRAINTS WHERE CONSTRAINT_SCHEMA = DATABASE() AND TABLE_NAME = 'proveedores' AND CONSTRAINT_NAME = 'fk_proveedor_giro') THEN
        ALTER TABLE proveedores
            ADD CONSTRAINT fk_proveedor_giro
            FOREIGN KEY (fk_provee_giro) REFERENCES giros (giro_id);
    END IF;

    -- =========================================================================
    -- MÓDULO: DIRECCIONES Y COMUNAS
    -- =========================================================================
    -- fk_direccion_cliente: la columna real es fk_cliente_direccion (ver
    -- ClienteJpaEntity.java @JoinColumn), no el fk_direccion generico que
    -- tenia esta migracion originalmente.
    IF NOT EXISTS (SELECT 1 FROM information_schema.TABLE_CONSTRAINTS WHERE CONSTRAINT_SCHEMA = DATABASE() AND TABLE_NAME = 'direccion' AND CONSTRAINT_NAME = 'fk_direccion_cliente') THEN
        ALTER TABLE direccion
            ADD CONSTRAINT fk_direccion_cliente
            FOREIGN KEY (fk_cliente_direccion) REFERENCES clientes (cliente_id) ON DELETE CASCADE;
    END IF;

    IF NOT EXISTS (SELECT 1 FROM information_schema.TABLE_CONSTRAINTS WHERE CONSTRAINT_SCHEMA = DATABASE() AND TABLE_NAME = 'direccion' AND CONSTRAINT_NAME = 'fk_direccion_proveedor') THEN
        ALTER TABLE direccion
            ADD CONSTRAINT fk_direccion_proveedor
            FOREIGN KEY (fk_provee_direccion) REFERENCES proveedores (proveedor_id) ON DELETE CASCADE;
    END IF;

    IF NOT EXISTS (SELECT 1 FROM information_schema.TABLE_CONSTRAINTS WHERE CONSTRAINT_SCHEMA = DATABASE() AND TABLE_NAME = 'direccion' AND CONSTRAINT_NAME = 'fk_direccion_tipo') THEN
        ALTER TABLE direccion
            ADD CONSTRAINT fk_direccion_tipo
            FOREIGN KEY (tipo_direccion_id) REFERENCES tipo_direccion (tipo_direccion_id);
    END IF;

    IF NOT EXISTS (SELECT 1 FROM information_schema.TABLE_CONSTRAINTS WHERE CONSTRAINT_SCHEMA = DATABASE() AND TABLE_NAME = 'direccion' AND CONSTRAINT_NAME = 'fk_direccion_comuna') THEN
        ALTER TABLE direccion
            ADD CONSTRAINT fk_direccion_comuna
            FOREIGN KEY (comuna_id) REFERENCES comuna (comuna_id);
    END IF;

    IF NOT EXISTS (SELECT 1 FROM information_schema.TABLE_CONSTRAINTS WHERE CONSTRAINT_SCHEMA = DATABASE() AND TABLE_NAME = 'comuna' AND CONSTRAINT_NAME = 'fk_comuna_region') THEN
        ALTER TABLE comuna
            ADD CONSTRAINT fk_comuna_region
            FOREIGN KEY (region_id) REFERENCES region (region_id);
    END IF;

    IF NOT EXISTS (SELECT 1 FROM information_schema.TABLE_CONSTRAINTS WHERE CONSTRAINT_SCHEMA = DATABASE() AND TABLE_NAME = 'region' AND CONSTRAINT_NAME = 'fk_region_pais') THEN
        ALTER TABLE region
            ADD CONSTRAINT fk_region_pais
            FOREIGN KEY (pais_id) REFERENCES pais (pais_id);
    END IF;

    -- =========================================================================
    -- MÓDULO: CONTACTOS Y DATOS BANCARIOS
    -- =========================================================================
    IF NOT EXISTS (SELECT 1 FROM information_schema.TABLE_CONSTRAINTS WHERE CONSTRAINT_SCHEMA = DATABASE() AND TABLE_NAME = 'contactos' AND CONSTRAINT_NAME = 'fk_contacto_cliente') THEN
        ALTER TABLE contactos
            ADD CONSTRAINT fk_contacto_cliente
            FOREIGN KEY (fk_cliente_contacto) REFERENCES clientes (cliente_id) ON DELETE CASCADE;
    END IF;

    IF NOT EXISTS (SELECT 1 FROM information_schema.TABLE_CONSTRAINTS WHERE CONSTRAINT_SCHEMA = DATABASE() AND TABLE_NAME = 'contactos' AND CONSTRAINT_NAME = 'fk_contacto_proveedor') THEN
        ALTER TABLE contactos
            ADD CONSTRAINT fk_contacto_proveedor
            FOREIGN KEY (fk_provee_contacto) REFERENCES proveedores (proveedor_id) ON DELETE CASCADE;
    END IF;

    IF NOT EXISTS (SELECT 1 FROM information_schema.TABLE_CONSTRAINTS WHERE CONSTRAINT_SCHEMA = DATABASE() AND TABLE_NAME = 'contactos' AND CONSTRAINT_NAME = 'fk_contacto_tipo') THEN
        ALTER TABLE contactos
            ADD CONSTRAINT fk_contacto_tipo
            FOREIGN KEY (tipo_contacto_id) REFERENCES tipos_contacto (tipo_contacto_id);
    END IF;

    IF NOT EXISTS (SELECT 1 FROM information_schema.TABLE_CONSTRAINTS WHERE CONSTRAINT_SCHEMA = DATABASE() AND TABLE_NAME = 'dato_bancario' AND CONSTRAINT_NAME = 'fk_dato_bancario_proveedor') THEN
        ALTER TABLE dato_bancario
            ADD CONSTRAINT fk_dato_bancario_proveedor
            FOREIGN KEY (fk_provee_dato_bancario) REFERENCES proveedores (proveedor_id) ON DELETE CASCADE;
    END IF;

    IF NOT EXISTS (SELECT 1 FROM information_schema.TABLE_CONSTRAINTS WHERE CONSTRAINT_SCHEMA = DATABASE() AND TABLE_NAME = 'dato_bancario' AND CONSTRAINT_NAME = 'fk_dato_bancario_banco') THEN
        ALTER TABLE dato_bancario
            ADD CONSTRAINT fk_dato_bancario_banco
            FOREIGN KEY (banco_id) REFERENCES banco (banco_id);
    END IF;

    IF NOT EXISTS (SELECT 1 FROM information_schema.TABLE_CONSTRAINTS WHERE CONSTRAINT_SCHEMA = DATABASE() AND TABLE_NAME = 'dato_bancario' AND CONSTRAINT_NAME = 'fk_dato_bancario_tipo') THEN
        ALTER TABLE dato_bancario
            ADD CONSTRAINT fk_dato_bancario_tipo
            FOREIGN KEY (tipo_cuenta_id) REFERENCES tipo_cuenta_bancaria (tipo_cuenta_id);
    END IF;

    -- =========================================================================
    -- MÓDULO: COMERCIAL (SOLICITUDES DE COSTOS Y FICHAS TÉCNICAS)
    -- =========================================================================
    IF NOT EXISTS (SELECT 1 FROM information_schema.TABLE_CONSTRAINTS WHERE CONSTRAINT_SCHEMA = DATABASE() AND TABLE_NAME = 'solicitudes_costos' AND CONSTRAINT_NAME = 'fk_scos_cliente') THEN
        ALTER TABLE solicitudes_costos
            ADD CONSTRAINT fk_scos_cliente
            FOREIGN KEY (cliente_id) REFERENCES clientes (cliente_id);
    END IF;

    IF NOT EXISTS (SELECT 1 FROM information_schema.TABLE_CONSTRAINTS WHERE CONSTRAINT_SCHEMA = DATABASE() AND TABLE_NAME = 'solicitudes_costos' AND CONSTRAINT_NAME = 'fk_scos_vendedor') THEN
        ALTER TABLE solicitudes_costos
            ADD CONSTRAINT fk_scos_vendedor
            FOREIGN KEY (vendedor_id) REFERENCES vendedores (id_vendedor);
    END IF;

    IF NOT EXISTS (SELECT 1 FROM information_schema.TABLE_CONSTRAINTS WHERE CONSTRAINT_SCHEMA = DATABASE() AND TABLE_NAME = 'solicitudes_cotizacion' AND CONSTRAINT_NAME = 'fk_scot_especificacion') THEN
        ALTER TABLE solicitudes_cotizacion
            ADD CONSTRAINT fk_scot_especificacion
            FOREIGN KEY (especificacion_tecnica_id) REFERENCES especificacion_tecnica (especificacion_tecnica_id);
    END IF;

    IF NOT EXISTS (SELECT 1 FROM information_schema.TABLE_CONSTRAINTS WHERE CONSTRAINT_SCHEMA = DATABASE() AND TABLE_NAME = 'especificacion_tecnica' AND CONSTRAINT_NAME = 'fk_especificacion_producto') THEN
        ALTER TABLE especificacion_tecnica
            ADD CONSTRAINT fk_especificacion_producto
            FOREIGN KEY (producto_id) REFERENCES producto (producto_id);
    END IF;

    IF NOT EXISTS (SELECT 1 FROM information_schema.TABLE_CONSTRAINTS WHERE CONSTRAINT_SCHEMA = DATABASE() AND TABLE_NAME = 'scos_telas' AND CONSTRAINT_NAME = 'fk_scostelas_solicitud') THEN
        ALTER TABLE scos_telas
            ADD CONSTRAINT fk_scostelas_solicitud
            FOREIGN KEY (solicitud_costos_id) REFERENCES solicitudes_costos (id_scos) ON DELETE CASCADE;
    END IF;

    IF NOT EXISTS (SELECT 1 FROM information_schema.TABLE_CONSTRAINTS WHERE CONSTRAINT_SCHEMA = DATABASE() AND TABLE_NAME = 'scos_telas' AND CONSTRAINT_NAME = 'fk_scostelas_proveedor') THEN
        ALTER TABLE scos_telas
            ADD CONSTRAINT fk_scostelas_proveedor
            FOREIGN KEY (proveedor_id) REFERENCES proveedores (proveedor_id);
    END IF;

    -- =========================================================================
    -- MÓDULO: PRODUCCIÓN (COMPRAS, RECEPCIONES Y AVANCES)
    -- =========================================================================
    IF NOT EXISTS (SELECT 1 FROM information_schema.TABLE_CONSTRAINTS WHERE CONSTRAINT_SCHEMA = DATABASE() AND TABLE_NAME = 'produccion_registro_avance' AND CONSTRAINT_NAME = 'fk_avance_ot') THEN
        ALTER TABLE produccion_registro_avance
            ADD CONSTRAINT fk_avance_ot
            FOREIGN KEY (orden_trabajo_id) REFERENCES produccion_orden_trabajo (id_ot) ON DELETE CASCADE;
    END IF;
END$$

DELIMITER ;

CALL tmp_add_v1_foreign_keys();

DROP PROCEDURE tmp_add_v1_foreign_keys;
