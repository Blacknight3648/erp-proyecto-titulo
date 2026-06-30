-- ============================================================
-- SCRIPT COMPLETO DE CREACIÓN DE TABLAS - ERP ANTUAN SA
-- Base de datos: BD_ERP (MariaDB/MySQL)
-- Generado desde entidades JPA (Spring Boot 3.x / Hibernate)
-- Nombrado de columnas: SpringPhysicalNamingStrategy (camelCase → snake_case)
--
-- NOTA: Para las columnas PK sin @Column(name=...) Spring aplica:
--   idNV → id_nv  |  idOP → id_op  |  idEVN → id_evn
--   idSCOS → id_scos  |  idSCOT → id_scot  |  idOT → id_ot
--
-- ORDEN DE EJECUCIÓN: respetar el orden del archivo (dependencias FK)
-- ============================================================

SET FOREIGN_KEY_CHECKS = 0;

-- ============================================================
-- CAPA 0: TABLAS SIN DEPENDENCIAS (CATÁLOGOS Y DATOS MAESTROS)
-- ============================================================

CREATE TABLE IF NOT EXISTS pais (
    pais_id     BIGINT       NOT NULL AUTO_INCREMENT,
    nombre_pais VARCHAR(100) NOT NULL,
    PRIMARY KEY (pais_id),
    UNIQUE KEY uk_pais_nombre (nombre_pais)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS banco (
    banco_id     BIGINT       NOT NULL AUTO_INCREMENT,
    nombre_banco VARCHAR(100) NOT NULL,
    codigo_banco VARCHAR(20)  NOT NULL,
    PRIMARY KEY (banco_id),
    UNIQUE KEY uk_banco_codigo (codigo_banco)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS tipo_cuenta_bancaria (
    tipo_cuenta_id      BIGINT       NOT NULL AUTO_INCREMENT,
    denominacion_cuenta VARCHAR(100) NOT NULL,
    PRIMARY KEY (tipo_cuenta_id),
    UNIQUE KEY uk_tipo_cuenta_denominacion (denominacion_cuenta)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS tipo_direccion (
    tipo_direccion_id BIGINT      NOT NULL AUTO_INCREMENT,
    descripcion       VARCHAR(100),
    PRIMARY KEY (tipo_direccion_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS tipos_contacto (
    tipo_contacto_id          BIGINT      NOT NULL AUTO_INCREMENT,
    descripcion_tipo_contacto VARCHAR(100),
    PRIMARY KEY (tipo_contacto_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS rubros (
    rubro_id          BIGINT       NOT NULL AUTO_INCREMENT,
    nombre_rubro      VARCHAR(255),
    descripcion_rubro VARCHAR(255),
    PRIMARY KEY (rubro_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Materias primas (extienden BaseEntity: creado_en, actualizado_en, activo)
CREATE TABLE IF NOT EXISTS tela (
    id_tela        BIGINT       NOT NULL AUTO_INCREMENT,
    nombre_tela    VARCHAR(255),
    composicion    VARCHAR(255),
    gramaje        DOUBLE,
    creado_en      DATETIME     NOT NULL,
    actualizado_en DATETIME     NOT NULL,
    activo         TINYINT(1)   NOT NULL DEFAULT 1,
    PRIMARY KEY (id_tela)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS accesorio (
    accesorio_id          BIGINT       NOT NULL AUTO_INCREMENT,
    nombre_accesorio      VARCHAR(255),
    descripcion_accesorio VARCHAR(255),
    creado_en             DATETIME     NOT NULL,
    actualizado_en        DATETIME     NOT NULL,
    activo                TINYINT(1)   NOT NULL DEFAULT 1,
    PRIMARY KEY (accesorio_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS cinta_reflectante (
    cinta_reflectante_id BIGINT     NOT NULL AUTO_INCREMENT,
    tipo_cinta           VARCHAR(100),
    ancho_cinta          DOUBLE,
    creado_en            DATETIME   NOT NULL,
    actualizado_en       DATETIME   NOT NULL,
    activo               TINYINT(1) NOT NULL DEFAULT 1,
    PRIMARY KEY (cinta_reflectante_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS costo_fijo (
    costo_fijo_id     BIGINT       NOT NULL AUTO_INCREMENT,
    nombre_costo_fijo VARCHAR(255),
    monto             DECIMAL(12,2),
    periodo           VARCHAR(50),
    creado_en         DATETIME     NOT NULL,
    actualizado_en    DATETIME     NOT NULL,
    activo            TINYINT(1)   NOT NULL DEFAULT 1,
    PRIMARY KEY (costo_fijo_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS prenda_lista (
    prenda_lista_id BIGINT       NOT NULL AUTO_INCREMENT,
    nombre_prenda   VARCHAR(255),
    descripcion     VARCHAR(500),
    genero          VARCHAR(50),
    color           VARCHAR(100),
    modelo          VARCHAR(100),
    tela            VARCHAR(255),
    composicion     VARCHAR(255),
    gramaje         DOUBLE,
    creado_en       DATETIME     NOT NULL,
    actualizado_en  DATETIME     NOT NULL,
    activo          TINYINT(1)   NOT NULL DEFAULT 1,
    PRIMARY KEY (prenda_lista_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS producto (
    producto_id     BIGINT       NOT NULL AUTO_INCREMENT,
    codigo_producto VARCHAR(50)  NOT NULL,
    nombre          VARCHAR(255),
    descripcion     VARCHAR(500),
    genero          VARCHAR(50),
    color           VARCHAR(100),
    creado_en       DATETIME     NOT NULL,
    actualizado_en  DATETIME     NOT NULL,
    activo          TINYINT(1)   NOT NULL DEFAULT 1,
    PRIMARY KEY (producto_id),
    UNIQUE KEY uk_producto_codigo (codigo_producto)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Seguridad / gestión de usuarios
CREATE TABLE IF NOT EXISTS areas (
    id_area     BIGINT       NOT NULL AUTO_INCREMENT,
    nombre_area VARCHAR(100) NOT NULL,
    descripcion VARCHAR(255),
    PRIMARY KEY (id_area),
    UNIQUE KEY uk_area_nombre (nombre_area)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS permisos (
    id          BIGINT       NOT NULL AUTO_INCREMENT,
    nombre      VARCHAR(255) NOT NULL,
    descripcion VARCHAR(255),
    modulo      VARCHAR(255) NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_permiso_nombre (nombre)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- Utilitarios del sistema
CREATE TABLE IF NOT EXISTS document_counter (
    tipo          VARCHAR(20) NOT NULL,
    ultimo_numero BIGINT      NOT NULL DEFAULT 0,
    PRIMARY KEY (tipo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS idempotency_token (
    token         VARCHAR(255) NOT NULL,
    metodo        VARCHAR(10),
    path          VARCHAR(500),
    status_code   INT,
    response_body TEXT,
    created_at    DATETIME,
    PRIMARY KEY (token)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS historial_estado (
    id              BIGINT       NOT NULL AUTO_INCREMENT,
    tipo_entidad    VARCHAR(100),
    entidad_id      BIGINT,
    estado_anterior VARCHAR(50),
    estado_nuevo    VARCHAR(50),
    usuario         VARCHAR(100),
    observacion     TEXT,
    fecha           DATETIME,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS auditoria (
    auditoria_id BIGINT       NOT NULL AUTO_INCREMENT,
    usuario_id   BIGINT,
    entidad      VARCHAR(100),
    entidad_id   BIGINT,
    accion       VARCHAR(50),
    fecha        DATETIME,
    cambios      TEXT,
    PRIMARY KEY (auditoria_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- CAPA 1: DEPENDEN DE CAPA 0
-- ============================================================

CREATE TABLE IF NOT EXISTS region (
    region_id      BIGINT       NOT NULL AUTO_INCREMENT,
    nombre_region  VARCHAR(255),
    pais_id        BIGINT,
    creado_en      DATETIME     NOT NULL,
    actualizado_en DATETIME     NOT NULL,
    activo         TINYINT(1)   NOT NULL DEFAULT 1,
    PRIMARY KEY (region_id),
    CONSTRAINT fk_region_pais FOREIGN KEY (pais_id) REFERENCES pais (pais_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS giros (
    giro_id          BIGINT       NOT NULL AUTO_INCREMENT,
    codigo_sii       VARCHAR(255),
    nombre_giro      VARCHAR(255),
    descripcion_giro VARCHAR(255),
    rubro_id         BIGINT,
    PRIMARY KEY (giro_id),
    CONSTRAINT fk_giro_rubro FOREIGN KEY (rubro_id) REFERENCES rubros (rubro_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS roles (
    id_role     BIGINT       NOT NULL AUTO_INCREMENT,
    nombre      VARCHAR(50)  NOT NULL,
    descripcion VARCHAR(255),
    area_id     BIGINT       NOT NULL,
    PRIMARY KEY (id_role),
    UNIQUE KEY uk_role_nombre (nombre),
    CONSTRAINT fk_role_area FOREIGN KEY (area_id) REFERENCES areas (id_area)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- CAPA 2
-- ============================================================

CREATE TABLE IF NOT EXISTS comuna (
    comuna_id      BIGINT       NOT NULL AUTO_INCREMENT,
    nombre_comuna  VARCHAR(255),
    region_id      BIGINT,
    creado_en      DATETIME     NOT NULL,
    actualizado_en DATETIME     NOT NULL,
    activo         TINYINT(1)   NOT NULL DEFAULT 1,
    PRIMARY KEY (comuna_id),
    CONSTRAINT fk_comuna_region FOREIGN KEY (region_id) REFERENCES region (region_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS rol_permisos (
    rol_id     BIGINT NOT NULL,
    permiso_id BIGINT NOT NULL,
    PRIMARY KEY (rol_id, permiso_id),
    CONSTRAINT fk_rolperm_rol     FOREIGN KEY (rol_id)     REFERENCES roles   (id_role),
    CONSTRAINT fk_rolperm_permiso FOREIGN KEY (permiso_id) REFERENCES permisos (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- CAPA 3: DIRECCIÓN, CONTACTO, DATO BANCARIO
-- Las FK hacia proveedores se agregan vía ALTER TABLE (ver Capa 4)
-- ============================================================

CREATE TABLE IF NOT EXISTS direccion (
    direccion_id        BIGINT       NOT NULL AUTO_INCREMENT,
    calle               VARCHAR(255),
    numero              VARCHAR(20),
    depto               VARCHAR(20),
    tipo_direccion_id   BIGINT,
    comuna_id           BIGINT,
    fk_provee_direccion BIGINT,
    PRIMARY KEY (direccion_id),
    CONSTRAINT fk_dir_tipo   FOREIGN KEY (tipo_direccion_id) REFERENCES tipo_direccion (tipo_direccion_id),
    CONSTRAINT fk_dir_comuna FOREIGN KEY (comuna_id)         REFERENCES comuna          (comuna_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS dato_bancario (
    dato_bancario_id        INT          NOT NULL AUTO_INCREMENT,
    numero_cuenta           VARCHAR(50)  NOT NULL,
    banco_id                BIGINT       NOT NULL,
    tipo_cuenta_id          BIGINT       NOT NULL,
    fk_provee_dato_bancario BIGINT,
    PRIMARY KEY (dato_bancario_id),
    CONSTRAINT fk_dato_banco       FOREIGN KEY (banco_id)       REFERENCES banco               (banco_id),
    CONSTRAINT fk_dato_tipo_cuenta FOREIGN KEY (tipo_cuenta_id) REFERENCES tipo_cuenta_bancaria (tipo_cuenta_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS contactos (
    contacto_id        BIGINT       NOT NULL AUTO_INCREMENT,
    nombre_contacto    VARCHAR(255),
    telefono_contacto  VARCHAR(50),
    email_contacto     VARCHAR(100),
    tipo_contacto_id   BIGINT,
    fk_provee_contacto BIGINT,
    PRIMARY KEY (contacto_id),
    CONSTRAINT fk_contacto_tipo FOREIGN KEY (tipo_contacto_id) REFERENCES tipos_contacto (tipo_contacto_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- CAPA 4: USUARIOS Y PROVEEDORES
-- ============================================================

CREATE TABLE IF NOT EXISTS usuarios (
    id_usuario BIGINT       NOT NULL AUTO_INCREMENT,
    run        VARCHAR(12)  NOT NULL,
    nombre     VARCHAR(50)  NOT NULL,
    apellidos  VARCHAR(50)  NOT NULL,
    email      VARCHAR(80)  NOT NULL,
    password   VARCHAR(200) NOT NULL,
    telefono   VARCHAR(50)  NOT NULL,
    enabled    TINYINT(1)   NOT NULL DEFAULT 1,
    PRIMARY KEY (id_usuario),
    UNIQUE KEY uk_usuario_run   (run),
    UNIQUE KEY uk_usuario_email (email)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS proveedores (
    proveedor_id           BIGINT       NOT NULL AUTO_INCREMENT,
    razon_social_proveedor VARCHAR(255) NOT NULL,
    run_proveedor          VARCHAR(12)  NOT NULL,
    sigla                  VARCHAR(100),
    horario_atencion       VARCHAR(100),
    tipo_proveedor         VARCHAR(30),
    fk_provee_giro         BIGINT,
    creado_en              DATETIME     NOT NULL,
    actualizado_en         DATETIME     NOT NULL,
    activo                 TINYINT(1)   NOT NULL DEFAULT 1,
    PRIMARY KEY (proveedor_id),
    UNIQUE KEY uk_prov_run (run_proveedor),
    INDEX idx_proveedor_run (run_proveedor),
    CONSTRAINT fk_proveedor_giro FOREIGN KEY (fk_provee_giro) REFERENCES giros (giro_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- FK diferidas de Capa 3 (ahora proveedores existe)
ALTER TABLE contactos
    ADD CONSTRAINT fk_contacto_proveedor
    FOREIGN KEY (fk_provee_contacto) REFERENCES proveedores (proveedor_id);

ALTER TABLE direccion
    ADD CONSTRAINT fk_dir_proveedor
    FOREIGN KEY (fk_provee_direccion) REFERENCES proveedores (proveedor_id);

ALTER TABLE dato_bancario
    ADD CONSTRAINT fk_dato_proveedor
    FOREIGN KEY (fk_provee_dato_bancario) REFERENCES proveedores (proveedor_id);

-- ============================================================
-- CAPA 5: CLIENTES, VENDEDORES, TABLAS PIVOT DE SEGURIDAD
-- ============================================================

CREATE TABLE IF NOT EXISTS clientes (
    cliente_id       BIGINT       NOT NULL AUTO_INCREMENT,
    razon_social     VARCHAR(255) NOT NULL,
    run_cliente      VARCHAR(20)  NOT NULL,
    correo_cliente   VARCHAR(255),
    telefono_cliente VARCHAR(50),
    contacto_cliente VARCHAR(255),
    sigla            VARCHAR(100),
    activo           TINYINT(1)   NOT NULL DEFAULT 1,
    fk_giro          BIGINT,
    fk_direccion     BIGINT,
    fk_dato_bancario BIGINT,
    PRIMARY KEY (cliente_id),
    UNIQUE KEY uk_cliente_run (run_cliente),
    CONSTRAINT fk_cliente_giro     FOREIGN KEY (fk_giro)          REFERENCES giros        (giro_id),
    CONSTRAINT fk_cliente_dir      FOREIGN KEY (fk_direccion)     REFERENCES direccion     (direccion_id),
    CONSTRAINT fk_cliente_dato     FOREIGN KEY (fk_dato_bancario) REFERENCES dato_bancario (dato_bancario_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS vendedores (
    id_vendedor     BIGINT       NOT NULL AUTO_INCREMENT,
    id_usuario      BIGINT       NOT NULL,
    codigo_vendedor VARCHAR(20)  NOT NULL,
    creado_en       DATETIME     NOT NULL,
    actualizado_en  DATETIME     NOT NULL,
    activo          TINYINT(1)   NOT NULL DEFAULT 1,
    PRIMARY KEY (id_vendedor),
    UNIQUE KEY uk_vendedor_codigo  (codigo_vendedor),
    UNIQUE KEY uk_vendedor_usuario (id_usuario),
    CONSTRAINT fk_vendedor_usuario FOREIGN KEY (id_usuario) REFERENCES usuarios (id_usuario)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS usuarios_roles (
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    UNIQUE KEY uk_ur (user_id, role_id),
    CONSTRAINT fk_ur_usuario FOREIGN KEY (user_id) REFERENCES usuarios (id_usuario),
    CONSTRAINT fk_ur_role    FOREIGN KEY (role_id)  REFERENCES roles    (id_role)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS usuarios_areas (
    user_id BIGINT NOT NULL,
    area_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, area_id),
    UNIQUE KEY uk_ua (user_id, area_id),
    CONSTRAINT fk_ua_usuario FOREIGN KEY (user_id) REFERENCES usuarios (id_usuario),
    CONSTRAINT fk_ua_area    FOREIGN KEY (area_id)  REFERENCES areas    (id_area)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- CAPA 6: ESPECIFICACIÓN TÉCNICA Y CATÁLOGO PROVEEDOR
-- ============================================================

CREATE TABLE IF NOT EXISTS especificacion_tecnica (
    especificacion_tecnica_id        BIGINT       NOT NULL AUTO_INCREMENT,
    producto_id                      BIGINT,
    detalles_especificacion_tecnica  TEXT,
    url_documento                    VARCHAR(300),
    creado_en                        DATETIME     NOT NULL,
    actualizado_en                   DATETIME     NOT NULL,
    activo                           TINYINT(1)   NOT NULL DEFAULT 1,
    PRIMARY KEY (especificacion_tecnica_id),
    CONSTRAINT fk_et_producto FOREIGN KEY (producto_id) REFERENCES producto (producto_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS catalogo_proveedor (
    catalogo_proveedor_id BIGINT        NOT NULL AUTO_INCREMENT,
    proveedor_id          BIGINT,
    codigo_proveedor      VARCHAR(100),
    producto_id           BIGINT,
    precio                DECIMAL(12,2),
    creado_en             DATETIME      NOT NULL,
    actualizado_en        DATETIME      NOT NULL,
    activo                TINYINT(1)    NOT NULL DEFAULT 1,
    PRIMARY KEY (catalogo_proveedor_id),
    CONSTRAINT fk_cat_proveedor FOREIGN KEY (proveedor_id) REFERENCES proveedores (proveedor_id),
    CONSTRAINT fk_cat_producto  FOREIGN KEY (producto_id)  REFERENCES producto     (producto_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- CAPA 7: SOLICITUDES COMERCIALES (COSTOS Y COTIZACIÓN)
-- ============================================================

CREATE TABLE IF NOT EXISTS solicitudes_costos (
    id_scos                   BIGINT        NOT NULL AUTO_INCREMENT,
    numero                    VARCHAR(20)   NOT NULL,
    estado                    VARCHAR(20),
    tipo                      VARCHAR(20),
    cliente_id                BIGINT        NOT NULL,
    vendedor_id               BIGINT,
    especificacion_tecnica_id BIGINT,
    articulo_descripcion      VARCHAR(255)  NOT NULL,
    nombre_prenda             VARCHAR(255),
    genero                    VARCHAR(50),
    tallaje                   VARCHAR(50),
    es_muestra                TINYINT(1),
    has_logo                  TINYINT(1),
    cantidad                  INT,
    fecha                     DATE,
    costo_total               DECIMAL(12,2),
    PRIMARY KEY (id_scos),
    UNIQUE KEY uk_scos_numero (numero),
    CONSTRAINT fk_scos_cliente  FOREIGN KEY (cliente_id)               REFERENCES clientes              (cliente_id),
    CONSTRAINT fk_scos_vendedor FOREIGN KEY (vendedor_id)              REFERENCES vendedores             (id_vendedor),
    CONSTRAINT fk_scos_et       FOREIGN KEY (especificacion_tecnica_id) REFERENCES especificacion_tecnica (especificacion_tecnica_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS solicitudes_cotizacion (
    id_scot                   BIGINT        NOT NULL AUTO_INCREMENT,
    numero                    VARCHAR(20)   NOT NULL,
    estado                    VARCHAR(20),
    tipo                      VARCHAR(20),
    cliente_id                BIGINT        NOT NULL,
    vendedor_id               BIGINT,
    especificacion_tecnica_id BIGINT,
    articulo_descripcion      VARCHAR(255)  NOT NULL,
    es_muestra                TINYINT(1),
    has_logo                  TINYINT(1),
    cantidad                  INT,
    fecha                     DATE,
    costo_total_calculado     DECIMAL(14,2),
    moneda_costo_total        VARCHAR(3),
    venta_asociada_id         BIGINT,
    PRIMARY KEY (id_scot),
    UNIQUE KEY uk_scot_numero (numero),
    CONSTRAINT fk_scot_cliente  FOREIGN KEY (cliente_id)               REFERENCES clientes              (cliente_id),
    CONSTRAINT fk_scot_vendedor FOREIGN KEY (vendedor_id)              REFERENCES vendedores             (id_vendedor),
    CONSTRAINT fk_scot_et       FOREIGN KEY (especificacion_tecnica_id) REFERENCES especificacion_tecnica (especificacion_tecnica_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- CAPA 8: NOTAS DE VENTA
-- (evaluacion_negocio_id FK es circular → se agrega en Capa 10)
-- ============================================================

CREATE TABLE IF NOT EXISTS notas_venta (
    id_nv                  BIGINT        NOT NULL AUTO_INCREMENT,
    numero_nv              VARCHAR(20)   NOT NULL,
    evaluacion_negocio_id  BIGINT,
    cliente_id             BIGINT        NOT NULL,
    vendedor_id            BIGINT,
    estado                 VARCHAR(20)   NOT NULL,
    es_kit                 TINYINT(1),
    detalle_kit            VARCHAR(500),
    fecha_emision          DATE          NOT NULL,
    fecha_entrega_estimada DATE,
    monto_subtotal         DECIMAL(12,2),
    moneda_subtotal        VARCHAR(3),
    monto_iva              DECIMAL(12,2),
    moneda_iva             VARCHAR(3),
    monto_total            DECIMAL(12,2),
    moneda_total           VARCHAR(3),
    created_at             DATETIME,
    updated_at             DATETIME,
    PRIMARY KEY (id_nv),
    UNIQUE KEY uk_nv_numero (numero_nv),
    CONSTRAINT fk_nv_cliente  FOREIGN KEY (cliente_id)  REFERENCES clientes  (cliente_id),
    CONSTRAINT fk_nv_vendedor FOREIGN KEY (vendedor_id) REFERENCES vendedores (id_vendedor)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS notas_venta_items (
    id_item_nv             BIGINT        NOT NULL AUTO_INCREMENT,
    nota_venta_id          BIGINT        NOT NULL,
    nro_item               INT,
    modelo                 VARCHAR(100),
    tela                   VARCHAR(100),
    composicion            VARCHAR(255),
    color                  VARCHAR(50),
    talla                  VARCHAR(20),
    genero                 VARCHAR(20),
    codigo                 VARCHAR(50),
    proveedor_id           BIGINT,
    lleva_logo             VARCHAR(50),
    tipo_item              VARCHAR(30),
    is_personalized        TINYINT(1),
    detalle_ot             TEXT,
    logo_detalle           TEXT,
    cantidad               INT           NOT NULL,
    precio_unitario        DECIMAL(12,2),
    moneda_precio_unitario VARCHAR(3),
    total                  DECIMAL(12,2),
    moneda_total           VARCHAR(3),
    producto_id            BIGINT,
    PRIMARY KEY (id_item_nv),
    CONSTRAINT fk_nvitem_nv        FOREIGN KEY (nota_venta_id) REFERENCES notas_venta (id_nv),
    CONSTRAINT fk_nvitem_producto  FOREIGN KEY (producto_id)   REFERENCES producto     (producto_id),
    CONSTRAINT fk_nvitem_proveedor FOREIGN KEY (proveedor_id)  REFERENCES proveedores  (proveedor_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS notas_venta_item_tallas (
    id_item_talla BIGINT     NOT NULL AUTO_INCREMENT,
    item_id       BIGINT     NOT NULL,
    talla         VARCHAR(20) NOT NULL,
    cantidad      INT         NOT NULL,
    PRIMARY KEY (id_item_talla),
    CONSTRAINT fk_talla_item FOREIGN KEY (item_id) REFERENCES notas_venta_items (id_item_nv)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- CAPA 8B: DETALLES DE SOLICITUDES DE COSTOS
-- ============================================================

CREATE TABLE IF NOT EXISTS scos_telas (
    id_scos_tela           BIGINT        NOT NULL AUTO_INCREMENT,
    solicitud_costos_id    BIGINT,
    tela_id                BIGINT,
    proveedor_id           BIGINT,
    aplicacion             VARCHAR(100),
    descripcion            VARCHAR(255),
    composicion            VARCHAR(255),
    color                  VARCHAR(100),
    peso                   DOUBLE,
    consumo                DECIMAL(10,4),
    unidad_medida          VARCHAR(20),
    precio_unitario        DECIMAL(12,2),
    moneda_precio_unitario VARCHAR(3),
    costo_total            DECIMAL(12,2),
    moneda_costo_total     VARCHAR(3),
    PRIMARY KEY (id_scos_tela),
    CONSTRAINT fk_scostela_scos FOREIGN KEY (solicitud_costos_id) REFERENCES solicitudes_costos (id_scos),
    CONSTRAINT fk_scostela_tela FOREIGN KEY (tela_id)             REFERENCES tela               (id_tela),
    CONSTRAINT fk_scostela_prov FOREIGN KEY (proveedor_id)        REFERENCES proveedores         (proveedor_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS scos_accesorios (
    id_scos_accesorio      BIGINT        NOT NULL AUTO_INCREMENT,
    solicitud_costos_id    BIGINT,
    accesorio_id           BIGINT,
    proveedor_id           BIGINT,
    tipo                   VARCHAR(100),
    descripcion            VARCHAR(255),
    cantidad               INT,
    consumo                DECIMAL(10,4),
    unidad_medida          VARCHAR(20),
    precio_unitario        DECIMAL(12,2),
    moneda_precio_unitario VARCHAR(3),
    costo_total            DECIMAL(12,2),
    moneda_costo_total     VARCHAR(3),
    PRIMARY KEY (id_scos_accesorio),
    CONSTRAINT fk_scosacc_scos FOREIGN KEY (solicitud_costos_id) REFERENCES solicitudes_costos (id_scos),
    CONSTRAINT fk_scosacc_acc  FOREIGN KEY (accesorio_id)        REFERENCES accesorio           (accesorio_id),
    CONSTRAINT fk_scosacc_prov FOREIGN KEY (proveedor_id)        REFERENCES proveedores          (proveedor_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS scos_logotipos (
    id                      BIGINT        NOT NULL AUTO_INCREMENT,
    solicitud_costos_id     BIGINT,
    solicitud_cotizacion_id BIGINT,
    tipo                    VARCHAR(50),
    nombre                  VARCHAR(255),
    ubicacion               VARCHAR(100),
    color                   VARCHAR(100),
    tamano                  VARCHAR(50),
    cantidad                INT,
    precio                  DECIMAL(12,2),
    PRIMARY KEY (id),
    CONSTRAINT fk_scologo_scos FOREIGN KEY (solicitud_costos_id)     REFERENCES solicitudes_costos    (id_scos),
    CONSTRAINT fk_scologo_scot FOREIGN KEY (solicitud_cotizacion_id) REFERENCES solicitudes_cotizacion (id_scot)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS scot_prendas (
    id_scot_prenda_lista    BIGINT        NOT NULL AUTO_INCREMENT,
    solicitud_cotizacion_id BIGINT,
    solicitud_costos_id     BIGINT,
    producto_id             BIGINT,
    nombre                  VARCHAR(255),
    cantidad                INT,
    talla                   VARCHAR(20),
    color                   VARCHAR(100),
    proveedor_referencia    VARCHAR(255),
    link_referencia         VARCHAR(500),
    composicion             VARCHAR(255),
    peso                    DOUBLE,
    observaciones           VARCHAR(500),
    precio_unitario         DECIMAL(12,2),
    moneda_precio_unitario  VARCHAR(3),
    PRIMARY KEY (id_scot_prenda_lista),
    CONSTRAINT fk_scotp_scot FOREIGN KEY (solicitud_cotizacion_id) REFERENCES solicitudes_cotizacion (id_scot),
    CONSTRAINT fk_scotp_scos FOREIGN KEY (solicitud_costos_id)     REFERENCES solicitudes_costos      (id_scos),
    CONSTRAINT fk_scotp_prod FOREIGN KEY (producto_id)             REFERENCES producto                (producto_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- CAPA 8C: PLANTILLAS DINÁMICAS
-- ============================================================

CREATE TABLE IF NOT EXISTS scos_plantilla (
    id                    BIGINT        NOT NULL AUTO_INCREMENT,
    solicitud_costos_id   BIGINT,
    nombre                VARCHAR(255)  NOT NULL,
    descripcion           TEXT,
    nombre_prenda         VARCHAR(255),
    genero                VARCHAR(50),
    detalles_prenda       JSON,
    campos_personalizados JSON,
    mo_prenda             DECIMAL(12,2),
    mo_costura_sellada    DECIMAL(12,2),
    mo_acolchado          DECIMAL(12,2),
    PRIMARY KEY (id),
    CONSTRAINT fk_plantilla_scos FOREIGN KEY (solicitud_costos_id) REFERENCES solicitudes_costos (id_scos)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS scos_plantilla_campos_activos (
    plantilla_id BIGINT       NOT NULL,
    campo_key    VARCHAR(100),
    CONSTRAINT fk_pcampos_plantilla FOREIGN KEY (plantilla_id) REFERENCES scos_plantilla (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS scos_plantilla_telas (
    plantilla_id  BIGINT       NOT NULL,
    aplicacion    VARCHAR(100),
    nombre        VARCHAR(255),
    composicion   VARCHAR(255),
    color         VARCHAR(100),
    peso          DOUBLE,
    unidad_medida VARCHAR(20),
    CONSTRAINT fk_ptela_plantilla FOREIGN KEY (plantilla_id) REFERENCES scos_plantilla (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS scos_plantilla_accesorios (
    plantilla_id     BIGINT       NOT NULL,
    tipo             VARCHAR(100),
    nombre_accesorio VARCHAR(255),
    cantidad         INT,
    CONSTRAINT fk_pacc_plantilla FOREIGN KEY (plantilla_id) REFERENCES scos_plantilla (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS scos_plantilla_logotipos (
    plantilla_id BIGINT        NOT NULL,
    tipo         VARCHAR(50),
    nombre       VARCHAR(255),
    ubicacion    VARCHAR(100),
    color        VARCHAR(100),
    tamano       VARCHAR(50),
    cantidad     INT,
    precio       DECIMAL(12,2),
    CONSTRAINT fk_plogo_plantilla FOREIGN KEY (plantilla_id) REFERENCES scos_plantilla (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS scos_plantilla_material_vinculos (
    id            BIGINT       NOT NULL AUTO_INCREMENT,
    plantilla_id  BIGINT       NOT NULL,
    field_name    VARCHAR(100),
    material_type VARCHAR(20),
    material_id   BIGINT,
    cantidad      DECIMAL(10,4),
    PRIMARY KEY (id),
    CONSTRAINT fk_pvinculo_plantilla FOREIGN KEY (plantilla_id) REFERENCES scos_plantilla (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS configuracion_plantillas (
    id            BIGINT        NOT NULL AUTO_INCREMENT,
    nombre_prenda VARCHAR(255)  NOT NULL,
    custom_fields TEXT,
    PRIMARY KEY (id),
    UNIQUE KEY uk_config_plantilla (nombre_prenda)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS configuracion_plantilla_campos_activos (
    configuracion_id BIGINT       NOT NULL,
    campo_activo     VARCHAR(100),
    CONSTRAINT fk_cpca_config FOREIGN KEY (configuracion_id) REFERENCES configuracion_plantillas (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS configuracion_plantilla_telas (
    configuracion_id BIGINT        NOT NULL,
    aplicacion       VARCHAR(100),
    nombre           VARCHAR(255),
    composicion      VARCHAR(255),
    color            VARCHAR(100),
    peso             DOUBLE,
    unidad_medida    VARCHAR(20),
    CONSTRAINT fk_cpt_config FOREIGN KEY (configuracion_id) REFERENCES configuracion_plantillas (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS configuracion_plantilla_accesorios (
    configuracion_id BIGINT        NOT NULL,
    tipo             VARCHAR(100),
    nombre_accesorio VARCHAR(255),
    cantidad         INT,
    CONSTRAINT fk_cpa_config FOREIGN KEY (configuracion_id) REFERENCES configuracion_plantillas (id) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- CAPA 9: COSTEO (MÓDULO PRODUCCIÓN)
-- ============================================================

CREATE TABLE IF NOT EXISTS produccion_costeos (
    id_costeo                 BIGINT        NOT NULL AUTO_INCREMENT,
    solicitud_costos_id       BIGINT        NOT NULL,
    numero_costeo             VARCHAR(20)   NOT NULL,
    costo_hilos               DECIMAL(12,2),
    costo_mano_obra           DECIMAL(12,2),
    costo_etiquetas           DECIMAL(12,2),
    costo_embalaje            DECIMAL(12,2),
    costo_flete               DECIMAL(12,2),
    porcentaje_costo_fijo     DECIMAL(5,2),
    precio_cinta_1            DECIMAL(12,2),
    cantidad_cinta_1          DECIMAL(10,4),
    precio_cinta_2            DECIMAL(12,2),
    cantidad_cinta_2          DECIMAL(10,4),
    vivo_reflectivo           DECIMAL(12,2),
    cantidad_vivo             DECIMAL(10,4),
    costo_total_materia_prima DECIMAL(12,2),
    margen_bruto_sugerido     DECIMAL(5,2),
    precio_venta_sugerido     DECIMAL(12,2),
    PRIMARY KEY (id_costeo),
    CONSTRAINT fk_costeo_scos FOREIGN KEY (solicitud_costos_id) REFERENCES solicitudes_costos (id_scos)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS produccion_costeo_items (
    id_costeo_item  BIGINT        NOT NULL AUTO_INCREMENT,
    costeo_id       BIGINT        NOT NULL,
    tipo_insumo     VARCHAR(30),
    insumo_id       BIGINT,
    nombre_insumo   VARCHAR(255),
    consumo         DECIMAL(10,4),
    precio_unitario DECIMAL(12,2),
    costo_total     DECIMAL(12,2),
    PRIMARY KEY (id_costeo_item),
    CONSTRAINT fk_citem_costeo FOREIGN KEY (costeo_id) REFERENCES produccion_costeos (id_costeo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS produccion_costeo_versiones (
    id_costeo_version         BIGINT        NOT NULL AUTO_INCREMENT,
    costeo_id                 BIGINT        NOT NULL,
    numero_version            INT           NOT NULL,
    fecha_creacion            DATETIME      NOT NULL,
    motivo_cambio             VARCHAR(500),
    usuario_creador           VARCHAR(100)  NOT NULL,
    total_mano_obra           DECIMAL(12,2),
    total_hilo                DECIMAL(12,2),
    total_flete               DECIMAL(12,2),
    total_embalaje            DECIMAL(12,2),
    total_etiquetas           DECIMAL(12,2),
    porcentaje_costo_fijo     DECIMAL(12,2),
    costo_total_materia_prima DECIMAL(12,2),
    margen_bruto_sugerido     DECIMAL(12,2),
    precio_venta_sugerido     DECIMAL(12,2),
    PRIMARY KEY (id_costeo_version),
    UNIQUE KEY uk_costeo_version (costeo_id, numero_version),
    CONSTRAINT fk_cver_costeo FOREIGN KEY (costeo_id) REFERENCES produccion_costeos (id_costeo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS produccion_costeo_item_versiones (
    id_costeo_item_version        BIGINT        NOT NULL AUTO_INCREMENT,
    costeo_version_id             BIGINT        NOT NULL,
    costeo_item_id                BIGINT,
    tipo_insumo                   VARCHAR(30),
    insumo_id                     BIGINT,
    nombre_insumo                 VARCHAR(255),
    consumo                       DECIMAL(10,4),
    precio_unitario               DECIMAL(12,2),
    costo_total                   DECIMAL(12,2),
    costeo_item_version_id_padre  BIGINT,
    activo                        TINYINT(1),
    PRIMARY KEY (id_costeo_item_version),
    CONSTRAINT fk_civs_cver FOREIGN KEY (costeo_version_id) REFERENCES produccion_costeo_versiones (id_costeo_version)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- CAPA 10: EVALUACIÓN DE NEGOCIO
-- ============================================================

CREATE TABLE IF NOT EXISTS evaluaciones_negocio (
    id_evn                  BIGINT        NOT NULL AUTO_INCREMENT,
    numero                  VARCHAR(20)   NOT NULL,
    referencia              VARCHAR(300),
    cliente_nombre          VARCHAR(200),
    cliente_id              BIGINT        NOT NULL,
    vendedor_id             BIGINT,
    estado                  VARCHAR(20)   NOT NULL,
    fecha_evaluacion        DATE          NOT NULL,
    costeo_id               BIGINT,
    solicitud_cotizacion_id BIGINT,
    porcentaje_comision     DECIMAL(5,2),
    created_at              DATETIME,
    updated_at              DATETIME,
    PRIMARY KEY (id_evn),
    UNIQUE KEY uk_evn_numero (numero),
    CONSTRAINT fk_evn_cliente  FOREIGN KEY (cliente_id)  REFERENCES clientes           (cliente_id),
    CONSTRAINT fk_evn_vendedor FOREIGN KEY (vendedor_id) REFERENCES vendedores          (id_vendedor),
    CONSTRAINT fk_evn_costeo   FOREIGN KEY (costeo_id)   REFERENCES produccion_costeos (id_costeo)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS evaluacion_negocio_items (
    id_evni                 BIGINT        NOT NULL AUTO_INCREMENT,
    evaluacion_negocio_id   BIGINT        NOT NULL,
    proveedor_id            BIGINT,
    producto_id             BIGINT,
    nro_item                INT,
    descripcion             VARCHAR(500),
    modelo                  VARCHAR(255),
    tela                    VARCHAR(255),
    composicion             VARCHAR(255),
    genero                  VARCHAR(20),
    codigo_interno          VARCHAR(50),
    codigo_proveedor        VARCHAR(50),
    proveedor_nombre        VARCHAR(255),
    cantidad                INT,
    precio_unitario         DECIMAL(12,2),
    moneda_precio_unitario  VARCHAR(3),
    costo_unitario          DECIMAL(12,2),
    moneda_costo_unitario   VARCHAR(3),
    costo_producto          DECIMAL(12,2),
    costo_logo              DECIMAL(12,2),
    costo_orden_trabajo     DECIMAL(12,2),
    tipo_item               VARCHAR(50),
    technical_specs_json    TEXT,
    PRIMARY KEY (id_evni),
    CONSTRAINT fk_evni_evn  FOREIGN KEY (evaluacion_negocio_id) REFERENCES evaluaciones_negocio (id_evn),
    CONSTRAINT fk_evni_prov FOREIGN KEY (proveedor_id)          REFERENCES proveedores           (proveedor_id),
    CONSTRAINT fk_evni_prod FOREIGN KEY (producto_id)           REFERENCES producto              (producto_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS gastos_adicionales (
    id_ga                 BIGINT        NOT NULL AUTO_INCREMENT,
    evaluacion_negocio_id BIGINT        NOT NULL,
    tipo_gasto            VARCHAR(100),
    monto                 DECIMAL(12,2),
    moneda                VARCHAR(3),
    metadata_json         TEXT,
    PRIMARY KEY (id_ga),
    CONSTRAINT fk_gasto_evn FOREIGN KEY (evaluacion_negocio_id) REFERENCES evaluaciones_negocio (id_evn)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS toma_tallaje (
    id_toma_tallaje       BIGINT        NOT NULL AUTO_INCREMENT,
    evaluacion_negocio_id BIGINT        NOT NULL,
    costo_total           DECIMAL(12,2),
    moneda                VARCHAR(3),
    observaciones         TEXT,
    fecha_programada      DATE,
    metadata_json         TEXT,
    PRIMARY KEY (id_toma_tallaje),
    UNIQUE KEY uk_toma_evn (evaluacion_negocio_id),
    CONSTRAINT fk_toma_evn FOREIGN KEY (evaluacion_negocio_id) REFERENCES evaluaciones_negocio (id_evn)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- FK circular resuelta: notas_venta → evaluaciones_negocio
ALTER TABLE notas_venta
    ADD CONSTRAINT fk_nv_evn
    FOREIGN KEY (evaluacion_negocio_id) REFERENCES evaluaciones_negocio (id_evn);

-- ============================================================
-- CAPA 11: ORDEN DE PRODUCCIÓN
-- ============================================================

CREATE TABLE IF NOT EXISTS orden_produccion (
    id_op                    BIGINT       NOT NULL AUTO_INCREMENT,
    costeo_version_id        BIGINT,
    numero_op                VARCHAR(20)  NOT NULL,
    nota_venta_id            BIGINT       NOT NULL,
    estado                   VARCHAR(20)  NOT NULL,
    fecha_inicio             DATE,
    fecha_entrega_programada DATE,
    observaciones            TEXT,
    created_at               DATETIME,
    updated_at               DATETIME,
    PRIMARY KEY (id_op),
    UNIQUE KEY uk_op_numero (numero_op),
    CONSTRAINT fk_op_costeo_version FOREIGN KEY (costeo_version_id) REFERENCES produccion_costeo_versiones (id_costeo_version),
    CONSTRAINT fk_op_nota_venta     FOREIGN KEY (nota_venta_id)     REFERENCES notas_venta                  (id_nv)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS produccion_orden_items (
    id_op_item          BIGINT       NOT NULL AUTO_INCREMENT,
    orden_produccion_id BIGINT       NOT NULL,
    nro_item            INT,
    producto_id         BIGINT,
    modelo              VARCHAR(255),
    tela                VARCHAR(255),
    composicion         VARCHAR(255),
    color               VARCHAR(100),
    talla               VARCHAR(20),
    genero              VARCHAR(20),
    codigo              VARCHAR(50),
    lleva_logo          TINYINT(1),
    cantidad            INT,
    PRIMARY KEY (id_op_item),
    CONSTRAINT fk_opitem_op FOREIGN KEY (orden_produccion_id) REFERENCES orden_produccion (id_op)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS produccion_seguimiento_op (
    id_seguimiento BIGINT AUTO_INCREMENT PRIMARY KEY,
    orden_produccion_id BIGINT NOT NULL UNIQUE,
    fecha_recepcion_op DATE,
    fin_tizado DATE,
    fecha_estado_oc_mp DATE,
    recepcion_compras DATE,
    inicio_corte DATE,
    fin_corte DATE,
    inicio_logo DATE,
    estado_ida_logo VARCHAR(50),
    regreso_logo DATE,
    estado_rec_logo VARCHAR(50),
    inicio_taller_externo DATE,
    fin_taller_externo DATE,
    calidad_taller VARCHAR(50),
    obs_taller TEXT,
    fin_terminacion DATE,
    fin_personalizado DATE,
    CONSTRAINT fk_seguimiento_op FOREIGN KEY (orden_produccion_id)
        REFERENCES orden_produccion(id_op) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- CAPA 12: ORDEN DE TRABAJO Y REGISTRO DE AVANCE
-- ============================================================

CREATE TABLE IF NOT EXISTS produccion_orden_trabajo (
    id_ot               BIGINT       NOT NULL AUTO_INCREMENT,
    numero_ot           VARCHAR(30)  NOT NULL,
    nota_venta_id       BIGINT       NOT NULL,
    orden_produccion_id BIGINT,
    nro_item            INT,
    tipo_ot             VARCHAR(20)  NOT NULL,
    fase                VARCHAR(20),
    estado_ot           VARCHAR(20)  NOT NULL,
    cantidad_total      INT,
    cantidad_producida  INT,
    cantidad_merma      INT,
    observaciones       TEXT,
    created_at          DATETIME,
    updated_at          DATETIME,
    PRIMARY KEY (id_ot),
    CONSTRAINT fk_ot_nota_venta       FOREIGN KEY (nota_venta_id)      REFERENCES notas_venta    (id_nv),
    CONSTRAINT fk_ot_orden_produccion FOREIGN KEY (orden_produccion_id) REFERENCES orden_produccion (id_op)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS produccion_registro_avance (
    id                 BIGINT       NOT NULL AUTO_INCREMENT,
    orden_trabajo_id   BIGINT       NOT NULL,
    cantidad_producida INT,
    cantidad_merma     INT,
    motivo_merma       VARCHAR(255),
    usuario            VARCHAR(100),
    observacion        TEXT,
    fecha              DATETIME,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- CAPA 13: HOJA DE COMPRA
-- ============================================================

CREATE TABLE IF NOT EXISTS produccion_hojas_compra (
    id_hc              BIGINT       NOT NULL AUTO_INCREMENT,
    numero_hc          VARCHAR(20)  NOT NULL,
    op_id              BIGINT       NOT NULL,
    costeo_version_id  BIGINT       NOT NULL,
    estado             VARCHAR(20)  NOT NULL,
    fecha_generacion   DATE         NOT NULL,
    observaciones      TEXT,
    PRIMARY KEY (id_hc),
    UNIQUE KEY uk_hc_numero (numero_hc),
    UNIQUE KEY uk_hc_op (op_id),
    CONSTRAINT fk_hc_op             FOREIGN KEY (op_id)            REFERENCES orden_produccion           (id_op),
    CONSTRAINT fk_hc_costeo_version FOREIGN KEY (costeo_version_id) REFERENCES produccion_costeo_versiones (id_costeo_version)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS produccion_hoja_compra_items (
    id_hc_item          BIGINT        NOT NULL AUTO_INCREMENT,
    hc_id               BIGINT        NOT NULL,
    tipo_insumo         VARCHAR(30),
    insumo_id           BIGINT,
    nombre_insumo       VARCHAR(255),
    consumo_unitario    DECIMAL(10,4),
    cantidad_op         INT,
    cantidad_requerida  DECIMAL(10,4),
    precio_unitario_ref DECIMAL(12,2),
    PRIMARY KEY (id_hc_item),
    CONSTRAINT fk_hcitem_hc FOREIGN KEY (hc_id) REFERENCES produccion_hojas_compra (id_hc)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- CAPA 14: ORDEN DE COMPRA, RECEPCIÓN OC Y TABLA PUENTE HC↔OC
-- ============================================================

CREATE TABLE IF NOT EXISTS produccion_ordenes_compra (
    id_oc                  BIGINT        NOT NULL AUTO_INCREMENT,
    numero_oc              VARCHAR(30)   NOT NULL,
    proveedor_id           BIGINT        NOT NULL,
    estado                 VARCHAR(25)   NOT NULL,
    fecha_emision          DATE          NOT NULL,
    fecha_entrega_estimada DATE,
    observaciones          TEXT,
    total_neto             DECIMAL(14,2),
    PRIMARY KEY (id_oc),
    UNIQUE KEY uk_oc_numero (numero_oc),
    CONSTRAINT fk_oc_proveedor FOREIGN KEY (proveedor_id) REFERENCES proveedores (proveedor_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS produccion_orden_compra_items (
    id_oc_item         BIGINT        NOT NULL AUTO_INCREMENT,
    oc_id              BIGINT        NOT NULL,
    tipo_insumo        VARCHAR(30),
    insumo_id          BIGINT,
    nombre_insumo      VARCHAR(255),
    cantidad_requerida DECIMAL(10,4),
    cantidad_stock     DECIMAL(10,4),
    cantidad_comprada  DECIMAL(10,4),
    precio_unitario    DECIMAL(12,2),
    subtotal           DECIMAL(12,2),
    PRIMARY KEY (id_oc_item),
    CONSTRAINT fk_ocitem_oc FOREIGN KEY (oc_id) REFERENCES produccion_ordenes_compra (id_oc)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS produccion_hc_item_oc_item (
    id_link           BIGINT        NOT NULL AUTO_INCREMENT,
    hc_item_id        BIGINT        NOT NULL,
    oc_item_id        BIGINT        NOT NULL,
    cantidad_asignada DECIMAL(10,4),
    PRIMARY KEY (id_link),
    CONSTRAINT fk_link_hci FOREIGN KEY (hc_item_id) REFERENCES produccion_hoja_compra_items  (id_hc_item),
    CONSTRAINT fk_link_oci FOREIGN KEY (oc_item_id) REFERENCES produccion_orden_compra_items (id_oc_item)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS produccion_recepciones_oc (
    id_recepcion    BIGINT       NOT NULL AUTO_INCREMENT,
    oc_id           BIGINT       NOT NULL,
    fecha_recepcion DATE         NOT NULL,
    numero_guia     VARCHAR(50),
    responsable     VARCHAR(100),
    observaciones   TEXT,
    PRIMARY KEY (id_recepcion),
    CONSTRAINT fk_recoc_oc FOREIGN KEY (oc_id) REFERENCES produccion_ordenes_compra (id_oc)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS produccion_recepcion_oc_items (
    id_recepcion_item  BIGINT       NOT NULL AUTO_INCREMENT,
    recepcion_id       BIGINT       NOT NULL,
    oc_item_id         BIGINT       NOT NULL,
    cantidad_recibida  DECIMAL(10,4),
    cantidad_conforme  DECIMAL(10,4),
    cantidad_rechazada DECIMAL(10,4),
    motivo_rechazo     TEXT,
    PRIMARY KEY (id_recepcion_item),
    CONSTRAINT fk_rocitem_rec FOREIGN KEY (recepcion_id) REFERENCES produccion_recepciones_oc       (id_recepcion),
    CONSTRAINT fk_rocitem_oci FOREIGN KEY (oc_item_id)   REFERENCES produccion_orden_compra_items   (id_oc_item)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- CAPA 15: ORDEN DE SERVICIO (CONFECCIÓN / BORDADO / ETC.)
-- ============================================================

CREATE TABLE IF NOT EXISTS produccion_ordenes_servicio (
    id_os                  BIGINT        NOT NULL AUTO_INCREMENT,
    numero_os              VARCHAR(30)   NOT NULL,
    op_id                  BIGINT        NOT NULL,
    proveedor_id           BIGINT        NOT NULL,
    tipo_servicio          VARCHAR(20)   NOT NULL,
    estado                 VARCHAR(20)   NOT NULL,
    fecha_emision          DATE          NOT NULL,
    fecha_entrega_estimada DATE,
    descripcion_trabajo    TEXT,
    cantidad_pactada       INT           NOT NULL,
    precio_unitario        DECIMAL(12,2),
    total_neto             DECIMAL(14,2),
    observaciones          TEXT,
    PRIMARY KEY (id_os),
    UNIQUE KEY uk_os_numero (numero_os),
    CONSTRAINT fk_os_op       FOREIGN KEY (op_id)       REFERENCES orden_produccion (id_op),
    CONSTRAINT fk_os_proveedor FOREIGN KEY (proveedor_id) REFERENCES proveedores      (proveedor_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS produccion_despachos_os (
    id_despacho         BIGINT       NOT NULL AUTO_INCREMENT,
    os_id               BIGINT       NOT NULL,
    fecha_despacho      DATE,
    cantidad_despachada INT,
    descripcion         VARCHAR(500),
    responsable         VARCHAR(100),
    observaciones       TEXT,
    PRIMARY KEY (id_despacho),
    CONSTRAINT fk_desp_os FOREIGN KEY (os_id) REFERENCES produccion_ordenes_servicio (id_os)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

CREATE TABLE IF NOT EXISTS produccion_recepciones_os (
    id_recepcion        BIGINT       NOT NULL AUTO_INCREMENT,
    os_id               BIGINT       NOT NULL,
    fecha_recepcion     DATE,
    cantidad_recibida   INT,
    cantidad_conforme   INT,
    cantidad_defectuosa INT,
    responsable         VARCHAR(100),
    observaciones       TEXT,
    PRIMARY KEY (id_recepcion),
    CONSTRAINT fk_recos_os FOREIGN KEY (os_id) REFERENCES produccion_ordenes_servicio (id_os)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;

-- ============================================================
-- ÍNDICES ADICIONALES (complementan las migrations V2–V6)
-- ============================================================

CREATE INDEX IF NOT EXISTS idx_oc_estado     ON produccion_ordenes_compra     (estado);
CREATE INDEX IF NOT EXISTS idx_oc_proveedor  ON produccion_ordenes_compra     (proveedor_id);
CREATE INDEX IF NOT EXISTS idx_oc_fecha      ON produccion_ordenes_compra     (fecha_emision);
CREATE INDEX IF NOT EXISTS idx_oci_oc        ON produccion_orden_compra_items (oc_id);
CREATE INDEX IF NOT EXISTS idx_oci_insumo    ON produccion_orden_compra_items (insumo_id);
CREATE INDEX IF NOT EXISTS idx_link_hci      ON produccion_hc_item_oc_item    (hc_item_id);
CREATE INDEX IF NOT EXISTS idx_link_oci      ON produccion_hc_item_oc_item    (oc_item_id);
CREATE INDEX IF NOT EXISTS idx_os_estado     ON produccion_ordenes_servicio   (estado);
CREATE INDEX IF NOT EXISTS idx_os_op         ON produccion_ordenes_servicio   (op_id);
CREATE INDEX IF NOT EXISTS idx_os_proveedor  ON produccion_ordenes_servicio   (proveedor_id);
CREATE INDEX IF NOT EXISTS idx_os_tipo       ON produccion_ordenes_servicio   (tipo_servicio);
CREATE INDEX IF NOT EXISTS idx_desp_os       ON produccion_despachos_os       (os_id);
CREATE INDEX IF NOT EXISTS idx_desp_fecha    ON produccion_despachos_os       (fecha_despacho);
CREATE INDEX IF NOT EXISTS idx_rec_os        ON produccion_recepciones_os     (os_id);
CREATE INDEX IF NOT EXISTS idx_rec_fecha     ON produccion_recepciones_os     (fecha_recepcion);

SET FOREIGN_KEY_CHECKS = 1;

-- ============================================================
-- DATOS MÍNIMOS PARA ARRANCAR (document_counter)
-- Genera números correlativos por tipo de documento
-- ============================================================

INSERT IGNORE INTO document_counter (tipo, ultimo_numero) VALUES
    ('NV',   0),
    ('EVN',  0),
    ('SCOS', 0),
    ('SCOT', 0),
    ('OP',   0),
    ('OT',   0),
    ('HC',   0),
    ('OC',   0),
    ('OS',   0);
