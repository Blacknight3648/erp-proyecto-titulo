-- ============================================================
-- 0. TIPO ARTÍCULO
-- ============================================================
MERGE INTO tipo_articulo (id_tipo_articulo, codigo, nombre)
    KEY (id_tipo_articulo)
    VALUES
    (1, 'TELA',               'Tela'),
    (2, 'PRENDA_LISTA',       'Prenda Lista'),
    (3, 'PRENDA_CONFECCIONAR','Prenda Confeccionar'),
    (4, 'ACCESORIO',          'Accesorio');

-- ============================================================
-- 1. ÁREAS
-- ============================================================
MERGE INTO areas (id_area, nombre_area, descripcion)
    KEY (id_area)
    VALUES
    (1, 'GERENCIA COMERCIAL', 'Planificación estratégica de ventas y marketing'),
    (2, 'ADMINISTRACIÓN', 'Gestión de recursos y procesos internos'),
    (3, 'VENTAS', 'Ejecución de fuerza de venta y captación'),
    (4, 'PRODUCCIÓN', 'Operaciones de manufactura y transformación'),
    (5, 'LOGÍSTICA Y BODEGA', 'Control de existencias y despacho'),
    (6, 'ADQUISICIONES', 'Gestión de compras y cadena de suministro'),
    (7, 'FINANZAS', 'Tesorería, contabilidad y cumplimiento tributario'),
    (8, 'TECNOLOGÍA Y SISTEMAS', 'Soporte, infraestructura y desarrollo');

-- ============================================================
-- 2. ROLES
-- ============================================================
MERGE INTO roles (id_role, nombre, descripcion, area_id)
    KEY (id_role)
    VALUES
    (1, 'JEFE_COMERCIAL', 'Líder de estrategia comercial', 1),
    (2, 'ASISTENTE_COMERCIAL', 'Apoyo administrativo comercial', 1),
    (3, 'JEFE_ADMIN', 'Encargado de administración general', 2),
    (4, 'ASISTENTE_ADMIN', 'Apoyo operativo administrativo', 2),
    (5, 'VENDEDOR', 'Ejecutivo de cuentas en terreno', 3),
    (6, 'JEFE_PRODUCCION', 'Supervisor de planta y procesos', 4),
    (7, 'OPERARIO_PRODUCCION', 'Personal de línea de producción', 4),
    (8, 'JEFE_BODEGA', 'Responsable de inventario y WMS', 5),
    (9, 'ASISTENTE_BODEGA', 'Recepción y despacho de mercadería', 5),
    (10, 'JEFE_COMPRAS', 'Responsable de negociación con proveedores', 6),
    (11, 'ASISTENTE_COMPRAS', 'Gestor de órdenes de compra', 6),
    (12, 'CONTADOR_GENERAL', 'Responsable de estados financieros', 7),
    (13, 'ANALISTA_TESORERIA', 'Gestión de pagos y flujo de caja', 7),
    (14, 'DEVOPS_ENGINEER', 'Gestión de infraestructura en nube', 8),
    (15, 'FULLSTACK_DEVELOPER', 'Desarrollo de aplicaciones internas', 8),
    (16, 'SOPORTE_TI', 'Atención técnica a usuarios', 8);

-- ============================================================
-- 3. USUARIOS (RUTs y Teléfonos validados)
-- ============================================================
MERGE INTO usuarios (id_usuario, run, nombre, apellidos, email, password, telefono, enabled)
    KEY (id_usuario)
    VALUES
    (1, '15342981-2', 'Carlos', 'Iturrieta Méndez', 'c.iturrieta@empresa.cl', '$2a$10$xyz1234567890123456789', '+56988223344', true),
    (2, '17589432-K', 'Valentina', 'Lagos Espinoza', 'v.lagos@empresa.cl', '$2a$10$abc1234567890123456789', '+56977445566', true);

-- ============================================================
-- 4. VENDEDORES
-- ============================================================
MERGE INTO vendedores (id_vendedor, id_usuario, codigo_vendedor, activo, creado_en, actualizado_en)
    KEY (id_vendedor)
    VALUES
    (1, 1, 'V-2024-001', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (2, 2, 'V-2024-002', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
    
-- ============================================================
-- 4.1. RUBROS
-- ============================================================
MERGE INTO rubros (rubro_id, nombre_rubro, descripcion_rubro)
    KEY (rubro_id)
    VALUES
    (1, 'COMERCIO', 'Empresas dedicadas a la compra y venta de bienes y servicios'),
    (2, 'SALUD', 'Empresas del sector salud, farmacéutico y laboratorio'),
    (3, 'LOGÍSTICA', 'Empresas de transporte, almacenamiento y distribución'),
    (4, 'CONSTRUCCIÓN', 'Empresas del rubro inmobiliario, infraestructura y ferretería');

-- ============================================================
-- 4.2. GIROS (referenciando el rubro correspondiente)
-- ============================================================
MERGE INTO giros (giro_id, codigo_sii, nombre_giro, descripcion_giro, rubro_id)
    KEY (giro_id)
    VALUES
    (1, '521000', 'RETAIL',          'RETAIL Y VENTAS POR MENOR',         1),
    (2, '861000', 'SALUD',           'SERVICIOS MÉDICOS Y LABORATORIO',   2),
    (3, '492200', 'LOGÍSTICA',       'LOGÍSTICA Y TRANSPORTE',            3),
    (4, '410000', 'CONSTRUCCIÓN',    'CONSTRUCCIÓN Y FERRETERÍA',         4);



-- ============================================================
-- 4.3. PRODUCTOS
-- ============================================================
MERGE INTO producto (producto_id, codigo_producto, nombre, descripcion, genero, color, creado_en, actualizado_en, activo)
    KEY (producto_id)
    VALUES
    (1, 'PROD-POL-001', 'Polerón Corporativo Premium', 'Polerón corporativo con gorro y bolsillos canguro', 'UNISEX', 'Azul Marino', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, true),
    (2, 'PROD-CHA-002', 'Chaqueta Impermeable Térmica', 'Chaqueta impermeable con forro micropolar interior', 'UNISEX', 'Gris Plata', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, true);



-- ============================================================
-- 5. CLIENTES (Nuevo modelo sin campos de correo/teléfono y con mappers asociados)
-- ============================================================
MERGE INTO clientes (cliente_id, activo, razon_social, run_cliente, sigla, fk_giro)
    KEY (cliente_id)
    VALUES
    (1, true, 'HITES S.A.', '96947020-9', 'S.A.', 1),
    (2, true, 'LABORATORIO MEDCELL', '96706320-7', 'LTDA.', 2),
    (3, true, 'GEODIS WILSON', '79699520-3', 'S.A.', 3);

-- ============================================================
-- 5.1. TIPOS DE CONTACTO Y CONTACTOS (Establece Fk_ de cliente)
-- ============================================================
MERGE INTO tipos_contacto (tipo_contacto_id, descripcion_tipo_contacto)
    KEY (tipo_contacto_id)
    VALUES
    (1, 'GENERAL'),
    (2, 'COMERCIAL'),
    (3, 'FINANZAS');

MERGE INTO contactos (contacto_id, nombre_contacto, telefono_contacto, email_contacto, tipo_contacto_id, fk_contacto)
    KEY (contacto_id)
    VALUES
    (1, 'CONTACTO HITES', '+56227275000', 'contacto.hites@hites.cl', 1, 1),
    (2, 'CONTACTO MEDCELL', '+56224396000', 'compras@medcell.cl', 1, 2),
    (3, 'CONTACTO GEODIS WILSON', '+56223816500', 'info.chile@geodis.com', 1, 3);

-- ============================================================
-- 5.2. PAÍS, REGION, COMUNA, TIPO DIRECCIÓN Y DIRECCIONES (Establece Fk_ de cliente)
-- ============================================================
MERGE INTO pais (pais_id, nombre_pais)
    KEY (pais_id)
    VALUES
    (1, 'CHILE');

MERGE INTO region (region_id, nombre_region, pais_id, creado_en, actualizado_en, activo)
    KEY (region_id)
    VALUES
    (1, 'METROPOLITANA', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, true);

MERGE INTO comuna (comuna_id, nombre_comuna, region_id, creado_en, actualizado_en, activo)
    KEY (comuna_id)
    VALUES
    (1, 'SANTIAGO', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, true);

MERGE INTO tipo_direccion (tipo_direccion_id, descripcion)
    KEY (tipo_direccion_id)
    VALUES
    (1, 'PRINCIPAL'),
    (2, 'SUCURSAL');

MERGE INTO direccion (direccion_id, calle, numero, depto, tipo_direccion_id, comuna_id, fk_direccion)
    KEY (direccion_id)
    VALUES
    (1, 'AV. KENNEDY', '5413', 'OF. 201', 1, 1, 1),
    (2, 'HOLANDA', '64', NULL, 1, 1, 2),
    (3, 'LO BOZA', '110', NULL, 1, 1, 3);

-- ============================================================
-- 6. PROVEEDORES (RUTs de empresas reales)
-- ============================================================
MERGE INTO proveedores (proveedor_id, activo, creado_en, actualizado_en, horario_atencion, razon_social_proveedor, run_proveedor, sigla, tipo_proveedor, fk_provee_giro)
    KEY (proveedor_id)
    VALUES
    (1, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '09:00 - 18:00', 'PARQUE ARAUCO S.A.', '99581960-0', 'S.A.', 'NACIONAL', 1),
    (2, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '09:00 - 18:00', 'C.C. LOS HEROES', '70016330-K', 'LTDA.', 'NACIONAL', 1),
    (3, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '09:00 - 18:00', 'MEDIPHARM LTDA.', '96599510-2', 'LTDA.', 'NACIONAL', 2),
    (4, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '09:00 - 18:00', 'SODIMAC S.A.', '96792430-K', 'S.A.', 'NACIONAL', 4);

-- ============================================================
-- 6.1. BANCOS
-- ============================================================
MERGE INTO banco (banco_id, nombre_banco, codigo_banco)
    KEY (banco_id)
    VALUES
    (1, 'BANCO DE CHILE',         'BCH'),
    (2, 'BANCO ESTADO',           'BEST'),
    (3, 'SANTANDER CHILE',        'SAN'),
    (4, 'BCI',                    'BCI'),
    (5, 'SCOTIABANK CHILE',       'SCOT'),
    (6, 'BANCO SECURITY',         'SEC'),
    (7, 'ITAÚ CORPBANCA',         'ITA');

-- ============================================================
-- 6.2. TIPOS DE CUENTA BANCARIA
-- ============================================================
MERGE INTO tipo_cuenta_bancaria (tipo_cuenta_id, denominacion_cuenta)
    KEY (tipo_cuenta_id)
    VALUES
    (1, 'CUENTA CORRIENTE'),
    (2, 'CUENTA VISTA'),
    (3, 'CUENTA DE AHORRO'),
    (4, 'CUENTA RUT');

-- ============================================================
-- 6.3. DATOS BANCARIOS (Asociados a proveedores vía fk_provee_dato_bancario)
-- ============================================================
MERGE INTO dato_bancario (dato_bancario_id, numero_cuenta, banco_id, tipo_cuenta_id, fk_provee_dato_bancario)
    KEY (dato_bancario_id)
    VALUES
    (1, '00-123-45678-09', 1, 1, 1),
    (2, '00-234-56789-01', 2, 2, 2),
    (3, '00-345-67890-12', 3, 1, 3),
    (4, '00-456-78901-23', 4, 1, 4);

-- ============================================================
-- 7. CONFIGURACIÓN DE PLANTILLAS (SCOS)
-- ============================================================
MERGE INTO configuracion_plantillas (id, nombre_prenda)
    KEY (id)
    VALUES
    (1, 'POLERÓN'),
    (2, 'CHAQUETA'),
    (3, 'CALZA');

MERGE INTO configuracion_plantilla_campos (configuracion_id, campo)
    KEY (configuracion_id, campo)
    VALUES
    (1, 'gorro'), (1, 'bolsillos'), (1, 'mangas'),
    (2, 'cuello'), (2, 'relleno'), (2, 'bolsillos'),
    (3, 'pretinasRuedo');

-- ============================================================
-- 7.1. SOLICITUDES DE COSTOS (SCOS)
-- ============================================================
MERGE INTO solicitudes_costos (idscos, numero, estado, tipo, cliente_id, vendedor_id, articulo_descripcion, nombre_prenda, genero, tallaje, es_muestra, has_logo, cantidad, fecha, costo_total)
    KEY (idscos)
    VALUES
    (1, 'SCOS-2024-001', 'PENDIENTE', 'NUEVO', 1, 1, 'Polera Pique Corporativa', 'POLERA', 'UNISEX', 'L', false, true, 100, CURRENT_DATE, 150000.00),
    (2, 'SCOS-2024-002', 'APROBADA', 'REPETICION', 2, 2, 'Pantalón Cargo Operario', 'PANTALON', 'MASCULINO', '42', false, false, 50, CURRENT_DATE, 250000.00);

-- ============================================================
-- 7.2. EVALUACIONES DE NEGOCIO (EVN)
-- ============================================================
MERGE INTO evaluaciones_negocio (idevn, numero, referencia, cliente_nombre, cliente_id, vendedor_id, estado, fecha_evaluacion, porcentaje_comision, created_at, updated_at)
    KEY (idevn)
    VALUES
    (1, 'EVN-2024-001', 'Cotización Poleras Hites', 'HITES S.A.', 1, 1, 'EVALUACION', CURRENT_DATE, 5.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (2, 'EVN-2024-002', 'Licitación Pantalones', 'LABORATORIO MEDCELL', 2, 2, 'APROBADA', CURRENT_DATE, 3.50, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- ============================================================
-- 7.3. NOTAS DE VENTA (NV)
-- ============================================================
MERGE INTO notas_venta (idnv, numeronv, evaluacion_negocio_id, cliente_id, vendedor_id, estado, es_kit, fecha_emision, fecha_entrega_estimada, monto_subtotal, moneda_subtotal, monto_iva, moneda_iva, monto_total, moneda_total, created_at, updated_at)
    KEY (idnv)
    VALUES
    (1, 'NV-2024-001', 2, 2, 2, 'BORRADOR', false, CURRENT_DATE, DATEADD('DAY', 30, CURRENT_DATE), 300000.00, 'CLP', 57000.00, 'CLP', 357000.00, 'CLP', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (2, 'NV-2024-002', 1, 1, 1, 'EN_PRODUCCION', false, CURRENT_DATE, DATEADD('DAY', 45, CURRENT_DATE), 850000.00, 'CLP', 161500.00, 'CLP', 1011500.00, 'CLP', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- ============================================================
-- 8. REINICIO DE SECUENCIAS (Unificado al final)
-- ============================================================
ALTER TABLE areas ALTER COLUMN id_area RESTART WITH 50;
ALTER TABLE roles ALTER COLUMN id_role RESTART WITH 100;
ALTER TABLE usuarios ALTER COLUMN id_usuario RESTART WITH 100;
ALTER TABLE clientes ALTER COLUMN cliente_id RESTART WITH 100;
ALTER TABLE vendedores ALTER COLUMN id_vendedor RESTART WITH 200;
ALTER TABLE proveedores ALTER COLUMN proveedor_id RESTART WITH 100;
ALTER TABLE giros ALTER COLUMN giro_id RESTART WITH 100;
ALTER TABLE producto ALTER COLUMN producto_id RESTART WITH 100;
ALTER TABLE solicitudes_costos ALTER COLUMN idscos RESTART WITH 2000;
ALTER TABLE produccion_costeos ALTER COLUMN id_costeo RESTART WITH 2000;
ALTER TABLE produccion_costeo_items ALTER COLUMN id_costeo_item RESTART WITH 5000;
ALTER TABLE scos_telas ALTER COLUMN idscostela RESTART WITH 2000;
ALTER TABLE scos_logotipos ALTER COLUMN id RESTART WITH 2000;
ALTER TABLE evaluaciones_negocio ALTER COLUMN idevn RESTART WITH 1000;
ALTER TABLE notas_venta ALTER COLUMN idnv RESTART WITH 1000;
ALTER TABLE tipos_contacto ALTER COLUMN tipo_contacto_id RESTART WITH 10;
ALTER TABLE contactos ALTER COLUMN contacto_id RESTART WITH 10;
ALTER TABLE pais ALTER COLUMN pais_id RESTART WITH 10;
ALTER TABLE region ALTER COLUMN region_id RESTART WITH 10;
ALTER TABLE comuna ALTER COLUMN comuna_id RESTART WITH 10;
ALTER TABLE tipo_direccion ALTER COLUMN tipo_direccion_id RESTART WITH 10;
ALTER TABLE direccion ALTER COLUMN direccion_id RESTART WITH 10;
ALTER TABLE rubros ALTER COLUMN rubro_id RESTART WITH 10;
ALTER TABLE banco ALTER COLUMN banco_id RESTART WITH 20;
ALTER TABLE tipo_cuenta_bancaria ALTER COLUMN tipo_cuenta_id RESTART WITH 10;
ALTER TABLE dato_bancario ALTER COLUMN dato_bancario_id RESTART WITH 10;