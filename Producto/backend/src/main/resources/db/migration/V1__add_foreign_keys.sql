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
-- =============================================================================

-- =============================================================================
-- MÓDULO: PRODUCCIÓN (FLUJOS PRINCIPALES Y COSTEO)
-- =============================================================================
ALTER TABLE orden_produccion
    ADD CONSTRAINT fk_op_nota_venta
    FOREIGN KEY (nota_venta_id) REFERENCES notas_venta (idnv);

ALTER TABLE produccion_orden_trabajo
    ADD CONSTRAINT fk_ot_nota_venta
    FOREIGN KEY (nota_venta_id) REFERENCES notas_venta (idnv);

ALTER TABLE produccion_orden_trabajo
    ADD CONSTRAINT fk_ot_orden_produccion
    FOREIGN KEY (orden_produccion_id) REFERENCES orden_produccion (idop);

ALTER TABLE produccion_costeos
    ADD CONSTRAINT fk_costeo_solicitud_costos
    FOREIGN KEY (solicitud_costos_id) REFERENCES solicitudes_costos (idscos);

-- fk_evn_costeo eliminada: costeo_id es ahora BIGINT ARRAY (List<Long>)
-- Un ARRAY no puede referenciar una columna escalar con FOREIGN KEY.
-- La integridad EVN ↔ Costeo se gestiona a nivel de aplicación.

ALTER TABLE notas_venta
    ADD CONSTRAINT fk_nv_evaluacion_negocio
    FOREIGN KEY (evaluacion_negocio_id) REFERENCES evaluaciones_negocio (idevn);

ALTER TABLE produccion_costeo_versiones
    ADD CONSTRAINT fk_costeo_version_costeo
    FOREIGN KEY (costeo_id) REFERENCES produccion_costeos (id_costeo);

-- =============================================================================
-- MÓDULO: GESTIÓN DE USUARIOS
-- =============================================================================
ALTER TABLE vendedores
    ADD CONSTRAINT fk_vendedor_usuario
    FOREIGN KEY (id_usuario) REFERENCES usuarios (id_usuario) ON DELETE CASCADE;

ALTER TABLE roles
    ADD CONSTRAINT fk_role_area
    FOREIGN KEY (area_id) REFERENCES areas (id_area);

ALTER TABLE usuarios_roles
    ADD CONSTRAINT fk_ur_usuario
    FOREIGN KEY (user_id) REFERENCES usuarios (id_usuario) ON DELETE CASCADE;

ALTER TABLE usuarios_roles
    ADD CONSTRAINT fk_ur_role
    FOREIGN KEY (role_id) REFERENCES roles (id_role) ON DELETE CASCADE;

ALTER TABLE usuarios_areas
    ADD CONSTRAINT fk_ua_usuario
    FOREIGN KEY (user_id) REFERENCES usuarios (id_usuario) ON DELETE CASCADE;

ALTER TABLE usuarios_areas
    ADD CONSTRAINT fk_ua_area
    FOREIGN KEY (area_id) REFERENCES areas (id_area) ON DELETE CASCADE;

ALTER TABLE rol_permisos
    ADD CONSTRAINT fk_rp_role
    FOREIGN KEY (rol_id) REFERENCES roles (id_role) ON DELETE CASCADE;

ALTER TABLE rol_permisos
    ADD CONSTRAINT fk_rp_permiso
    FOREIGN KEY (permiso_id) REFERENCES permisos (id) ON DELETE CASCADE;

-- =============================================================================
-- MÓDULO: CLIENTES Y PROVEEDORES
-- =============================================================================
ALTER TABLE giros
    ADD CONSTRAINT fk_giro_rubro
    FOREIGN KEY (rubro_id) REFERENCES rubros (rubro_id);

ALTER TABLE clientes
    ADD CONSTRAINT fk_cliente_giro
    FOREIGN KEY (fk_giro) REFERENCES giros (giro_id);

ALTER TABLE proveedores
    ADD CONSTRAINT fk_proveedor_giro
    FOREIGN KEY (fk_provee_giro) REFERENCES giros (giro_id);

-- =============================================================================
-- MÓDULO: DIRECCIONES Y COMUNAS
-- =============================================================================
ALTER TABLE direccion
    ADD CONSTRAINT fk_direccion_cliente
    FOREIGN KEY (fk_direccion) REFERENCES clientes (cliente_id) ON DELETE CASCADE;

ALTER TABLE direccion
    ADD CONSTRAINT fk_direccion_proveedor
    FOREIGN KEY (fk_provee_direccion) REFERENCES proveedores (proveedor_id) ON DELETE CASCADE;

ALTER TABLE direccion
    ADD CONSTRAINT fk_direccion_tipo
    FOREIGN KEY (tipo_direccion_id) REFERENCES tipo_direccion (tipo_direccion_id);

ALTER TABLE direccion
    ADD CONSTRAINT fk_direccion_comuna
    FOREIGN KEY (comuna_id) REFERENCES comuna (comuna_id);

ALTER TABLE comuna
    ADD CONSTRAINT fk_comuna_region
    FOREIGN KEY (region_id) REFERENCES region (region_id);

ALTER TABLE region
    ADD CONSTRAINT fk_region_pais
    FOREIGN KEY (pais_id) REFERENCES pais (pais_id);

-- =============================================================================
-- MÓDULO: CONTACTOS Y DATOS BANCARIOS
-- =============================================================================
ALTER TABLE contactos
    ADD CONSTRAINT fk_contacto_cliente
    FOREIGN KEY (fk_contacto) REFERENCES clientes (cliente_id) ON DELETE CASCADE;

ALTER TABLE contactos
    ADD CONSTRAINT fk_contacto_proveedor
    FOREIGN KEY (fk_provee_contacto) REFERENCES proveedores (proveedor_id) ON DELETE CASCADE;

ALTER TABLE contactos
    ADD CONSTRAINT fk_contacto_tipo
    FOREIGN KEY (tipo_contacto_id) REFERENCES tipos_contacto (tipo_contacto_id);

ALTER TABLE dato_bancario
    ADD CONSTRAINT fk_dato_bancario_proveedor
    FOREIGN KEY (fk_provee_dato_bancario) REFERENCES proveedores (proveedor_id) ON DELETE CASCADE;

ALTER TABLE dato_bancario
    ADD CONSTRAINT fk_dato_bancario_banco
    FOREIGN KEY (banco_id) REFERENCES banco (banco_id);

ALTER TABLE dato_bancario
    ADD CONSTRAINT fk_dato_bancario_tipo
    FOREIGN KEY (tipo_cuenta_id) REFERENCES tipo_cuenta_bancaria (tipo_cuenta_id);

-- =============================================================================
-- MÓDULO: COMERCIAL (SOLICITUDES DE COSTOS Y FICHAS TÉCNICAS)
-- =============================================================================
ALTER TABLE solicitudes_costos
    ADD CONSTRAINT fk_scos_cliente
    FOREIGN KEY (cliente_id) REFERENCES clientes (cliente_id);

ALTER TABLE solicitudes_costos
    ADD CONSTRAINT fk_scos_vendedor
    FOREIGN KEY (vendedor_id) REFERENCES vendedores (id_vendedor);

ALTER TABLE solicitudes_cotizacion
    ADD CONSTRAINT fk_scot_especificacion
    FOREIGN KEY (especificacion_tecnica_id) REFERENCES especificacion_tecnica (especificacion_tecnica_id);

ALTER TABLE especificacion_tecnica
    ADD CONSTRAINT fk_especificacion_producto
    FOREIGN KEY (producto_id) REFERENCES producto (producto_id);

ALTER TABLE scos_telas
    ADD CONSTRAINT fk_scostelas_solicitud
    FOREIGN KEY (solicitud_costos_id) REFERENCES solicitudes_costos (idscos) ON DELETE CASCADE;

ALTER TABLE scos_telas
    ADD CONSTRAINT fk_scostelas_proveedor
    FOREIGN KEY (proveedor_id) REFERENCES proveedores (proveedor_id);

-- =============================================================================
-- MÓDULO: PRODUCCIÓN (COMPRAS, RECEPCIONES Y AVANCES)
-- =============================================================================
ALTER TABLE produccion_registro_avance
    ADD CONSTRAINT fk_avance_ot
    FOREIGN KEY (orden_trabajo_id) REFERENCES produccion_orden_trabajo (idot) ON DELETE CASCADE;
