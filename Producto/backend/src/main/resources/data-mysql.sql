-- ============================================================
-- 0. TIPO ARTÍCULO
-- ============================================================
INSERT IGNORE INTO tipo_articulo (id_tipo_articulo, codigo, nombre) VALUES
    (1, 'TELA',         'Tela'),
    (2, 'PRENDA_LISTA', 'Prenda Lista'),
    (3, 'ACCESORIO',    'Accesorio');

-- ============================================================
-- 1. ÁREAS
-- ============================================================
INSERT IGNORE INTO areas (id_area, nombre_area, descripcion) VALUES
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
INSERT IGNORE INTO roles (id_role, nombre, descripcion, area_id) VALUES
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
INSERT IGNORE INTO usuarios (id_usuario, run, nombre, apellidos, email, password, telefono, enabled) VALUES
    (1, '15342981-2', 'Carlos', 'Iturrieta Méndez', 'c.iturrieta@empresa.cl', '$2a$10$xyz1234567890123456789', '+56988223344', true),
    (2, '17589432-K', 'Valentina', 'Lagos Espinoza', 'v.lagos@empresa.cl', '$2a$10$abc1234567890123456789', '+56977445566', true);

-- ============================================================
-- 4. VENDEDORES
-- ============================================================
INSERT IGNORE INTO vendedores (id_vendedor, id_usuario, codigo_vendedor, activo, creado_en, actualizado_en) VALUES
    (1, 1, 'V-2024-001', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (2, 2, 'V-2024-002', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
    
-- ============================================================
-- 4.1. RUBROS
-- ============================================================
INSERT IGNORE INTO rubros (rubro_id, nombre_rubro, descripcion_rubro) VALUES
    (1, 'COMERCIO', 'Empresas dedicadas a la compra y venta de bienes y servicios'),
    (2, 'SALUD', 'Empresas del sector salud, farmacéutico y laboratorio'),
    (3, 'LOGÍSTICA', 'Empresas de transporte, almacenamiento y distribución'),
    (4, 'CONSTRUCCIÓN', 'Empresas del rubro inmobiliario, infraestructura y ferretería');

-- ============================================================
-- 4.2. GIROS (referenciando el rubro correspondiente)
-- ============================================================
INSERT IGNORE INTO giros (giro_id, codigo_sii, nombre_giro, descripcion_giro, rubro_id) VALUES
    (1, '521000', 'RETAIL',          'RETAIL Y VENTAS POR MENOR',         1),
    (2, '861000', 'SALUD',           'SERVICIOS MÉDICOS Y LABORATORIO',   2),
    (3, '492200', 'LOGÍSTICA',       'LOGÍSTICA Y TRANSPORTE',            3),
    (4, '410000', 'CONSTRUCCIÓN',    'CONSTRUCCIÓN Y FERRETERÍA',         4);

-- ============================================================
-- 4.3. PRODUCTOS
-- ============================================================
INSERT IGNORE INTO producto (producto_id, codigo_producto, nombre, descripcion, genero, color, creado_en, actualizado_en, activo) VALUES
    (1, 'PROD-POL-001', 'Polerón Corporativo Premium', 'Polerón corporativo con gorro y bolsillos canguro', 'UNISEX', 'Azul Marino', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, true),
    (2, 'PROD-CHA-002', 'Chaqueta Impermeable Térmica', 'Chaqueta impermeable con forro micropolar interior', 'UNISEX', 'Gris Plata', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, true);

-- ============================================================
-- 5. CLIENTES (Modelo normalizado sin campos planos de contacto)
-- ============================================================
INSERT IGNORE INTO clientes (cliente_id, activo, razon_social, run_cliente, sigla, fk_giro) VALUES
    (1, true, 'HITES S.A.', '96947020-9', 'S.A.', 1),
    (2, true, 'LABORATORIO MEDCELL', '96706320-7', 'LTDA.', 2),
    (3, true, 'GEODIS WILSON', '79699520-3', 'S.A.', 3);

-- ============================================================
-- 5.1. TIPOS DE CONTACTO Y CONTACTOS
-- ============================================================
INSERT IGNORE INTO tipos_contacto (tipo_contacto_id, descripcion_tipo_contacto) VALUES
    (1, 'GENERAL'),
    (2, 'COMERCIAL'),
    (3, 'FINANZAS');

INSERT IGNORE INTO contactos (contacto_id, nombre_contacto, telefono_contacto, email_contacto, tipo_contacto_id, fk_contacto) VALUES
    (1, 'CONTACTO HITES', '+56227275000', 'contacto.hites@hites.cl', 1, 1),
    (2, 'CONTACTO MEDCELL', '+56224396000', 'compras@medcell.cl', 1, 2),
    (3, 'CONTACTO GEODIS WILSON', '+56223816500', 'info.chile@geodis.com', 1, 3);

-- ============================================================
-- 5.2. GEOGRAFÍA Y DIRECCIONES
-- ============================================================
INSERT IGNORE INTO pais (pais_id, nombre_pais) VALUES
    (1, 'CHILE');

INSERT IGNORE INTO region (region_id, nombre_region, pais_id, creado_en, actualizado_en, activo) VALUES
    (1, 'METROPOLITANA', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, true);

INSERT IGNORE INTO comuna (comuna_id, nombre_comuna, region_id, creado_en, actualizado_en, activo) VALUES
    (1, 'SANTIAGO', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, true);

INSERT IGNORE INTO tipo_direccion (tipo_direccion_id, descripcion) VALUES
    (1, 'PRINCIPAL'),
    (2, 'SUCURSAL');

INSERT IGNORE INTO direccion (direccion_id, calle, numero, depto, tipo_direccion_id, comuna_id, fk_direccion) VALUES
    (1, 'AV. KENNEDY', '5413', 'OF. 201', 1, 1, 1),
    (2, 'HOLANDA', '64', NULL, 1, 1, 2),
    (3, 'LO BOZA', '110', NULL, 1, 1, 3);

-- ============================================================
-- 6. PROVEEDORES (Modelo normalizado con giros asociados)
-- ============================================================
INSERT IGNORE INTO proveedores (proveedor_id, activo, creado_en, actualizado_en, horario_atencion, razon_social_proveedor, run_proveedor, sigla, tipo_proveedor, fk_provee_giro) VALUES
    (1, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '09:00 - 18:00', 'PARQUE ARAUCO S.A.', '99581960-0', 'S.A.', 'NACIONAL', 1),
    (2, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '09:00 - 18:00', 'C.C. LOS HEROES', '70016330-K', 'LTDA.', 'NACIONAL', 1),
    (3, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '09:00 - 18:00', 'MEDIPHARM LTDA.', '96599510-2', 'LTDA.', 'NACIONAL', 2),
    (4, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '09:00 - 18:00', 'SODIMAC S.A.', '96792430-K', 'S.A.', 'NACIONAL', 4);

-- ============================================================
-- 6.1. BANCOS Y ENTIDADES FINANCIERAS
-- ============================================================
INSERT IGNORE INTO banco (banco_id, nombre_banco, codigo_banco) VALUES
    (1, 'BANCO DE CHILE',         'BCH'),
    (2, 'BANCO ESTADO',           'BEST'),
    (3, 'SANTANDER CHILE',        'SAN'),
    (4, 'BCI',                    'BCI'),
    (5, 'SCOTIABANK CHILE',       'SCOT'),
    (6, 'BANCO SECURITY',         'SEC'),
    (7, 'ITAÚ CORPBANCA',         'ITA');

INSERT IGNORE INTO tipo_cuenta_bancaria (tipo_cuenta_id, denominacion_cuenta) VALUES
    (1, 'CUENTA CORRIENTE'),
    (2, 'CUENTA VISTA'),
    (3, 'CUENTA DE AHORRO'),
    (4, 'CUENTA RUT');

-- ============================================================
-- 6.2. DATOS BANCARIOS PROVEEDORES
-- ============================================================
INSERT IGNORE INTO dato_bancario (dato_bancario_id, numero_cuenta, banco_id, tipo_cuenta_id, fk_provee_dato_bancario) VALUES
    (1, '00-123-45678-09', 1, 1, 1),
    (2, '00-234-56789-01', 2, 2, 2),
    (3, '00-345-67890-12', 3, 1, 3),
    (4, '00-456-78901-23', 4, 1, 4);

-- ============================================================
-- 7. CATEGORÍAS Y SUBCATEGORÍAS DE TEXTIL
-- ============================================================

INSERT IGNORE INTO categoria_tela (id_categoria_tela, codigo_categoria_tela, nombre_categoria_tela) VALUES
    (1, 'TEJ-PLN', 'Tejido Plano'),
    (2, 'TEJ-PNT', 'Tejido de Punto'),
    (3, 'NO-TEJ',  'No Tejido / Técnico');

INSERT IGNORE INTO subcategoria_tela (id_subcategoria_tela, codigo_subcategoria_tela, nombre_subcategoria_tela, id_categoria_tela) VALUES
    (1, 'PLN-COTT', 'Algodón Plano',     1),
    (2, 'PLN-SYNT', 'Sintético Plano',   1),
    (3, 'PNT-FLEE', 'Fleece / Polar',    2),
    (4, 'PNT-JRSY', 'Jersey',            2),
    (5, 'TEC-IMPR', 'Impermeable Tech',  3);

-- ============================================================
-- 7.1. ARTÍCULOS (Catálogo Base)
-- ============================================================
INSERT IGNORE INTO articulo (id_articulo, codigo_articulo, nombre_articulo, descripcion_articulo, codigo_barra, id_tipo_articulo, activo, id_categoria_tela, id_subcategoria_tela) VALUES
    (1, 'ART-FLEE-001', 'Polar Fleece 280 GSM',      'Tela polar fleece gramaje 280 g/m²',    NULL, 1, true, 2, 3),
    (2, 'ART-IMPR-001', 'Ripstop Impermeable',        'Tejido técnico ripstop impermeabilizado', NULL, 1, true, 3, 5),
    (3, 'ART-JRSY-001', 'Jersey Piqué Algodón',       'Jersey piqué 100% algodón peinado',      NULL, 1, true, 2, 4),
    (4, 'ART-ACC-001',  'Cierre YKK 60cm Metálico',   'Cierre metálico YKK 60 cm',             NULL, 3, true, NULL, NULL),
    (5, 'ART-ACC-002',  'Botón Snap 15mm Nácar',       'Botón tipo snap nacarado 15 mm',        NULL, 3, true, NULL, NULL);

-- ============================================================
-- 7.2. CATÁLOGO DE CAMPOS DE PLANTILLA
-- ============================================================
INSERT IGNORE INTO plantilla (id_plantilla, nombre_campo) VALUES
    (1,  'forro'),
    (2,  'relleno'),
    (3,  'colorForro'),
    (4,  'gorro'),
    (5,  'cuello'),
    (6,  'abotonaduraCierre'),
    (7,  'cortesAplicaciones'),
    (8,  'fuelles'),
    (9,  'mangas'),
    (10, 'pretinasRuedo'),
    (11, 'bolsillos'),
    (12, 'cintaDetalle'),
    (13, 'logoDetalle'),
    (14, 'accesoriosDetalle'),
    (15, 'obsModelo');

-- ============================================================
-- 7.3. MODELO PLANTILLA (Mapeo Artículo e Id Plantilla)
-- ============================================================
-- Una sola fila por artículo: los campos de su plantilla en CSV (nombres de `plantilla`).
INSERT IGNORE INTO modelo_plantilla (id_modelo_plantilla, id_articulo, campos) VALUES
    (1, 1, 'gorro,bolsillos,mangas,forro'),                  -- Polar Fleece
    (2, 2, 'cuello,relleno,bolsillos,abotonaduraCierre'),    -- Ripstop
    (3, 3, 'mangas,pretinasRuedo');                          -- Jersey

-- ============================================================
-- 7.4. SOLICITUDES DE COSTOS (SCOS)
-- ============================================================
INSERT IGNORE INTO solicitudes_costos (idscos, numero, estado, tipo, cliente_id, vendedor_id, articulo_descripcion, nombre_prenda, genero, tallaje, es_muestra, has_logo, cantidad, fecha, costo_total) VALUES
    (1, 'SCOS-000001', 'PENDIENTE', 'SCOS', 1, 1, 'POLERA', 'Polera Piqué Corporativa', 'UNISEX', 'Antuan SA', false, true,  100, CURRENT_DATE, 150000.00),
    (2, 'SCOS-000002', 'APROBADA',  'SCOS', 2, 2, 'PANTALON', 'Pantalón Cargo Operario', 'MASCULINO', 'Cliente', false, false, 50, CURRENT_DATE, 250000.00);

-- ============================================================
-- 7.5. EVALUACIONES DE NEGOCIO (EVN)
-- ============================================================
INSERT IGNORE INTO evaluaciones_negocio (idevn, numero, referencia, cliente_nombre, cliente_id, vendedor_id, estado, fecha_evaluacion, porcentaje_comision, created_at, updated_at) VALUES
    (1, 'EVN-000001', 'Cotización Poleras Hites', 'HITES S.A.', 1, 1, 'EVALUACION', CURRENT_DATE, 5.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (2, 'EVN-000002', 'Licitación Pantalones', 'LABORATORIO MEDCELL', 2, 2, 'APROBADA', CURRENT_DATE, 3.50, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- ============================================================
-- 7.6. NOTAS DE VENTA (NV)
-- ============================================================
INSERT IGNORE INTO notas_venta (idnv, numeronv, evaluacion_negocio_id, cliente_id, vendedor_id, estado, es_kit, fecha_emision, fecha_entrega_estimada, monto_subtotal, moneda_subtotal, monto_iva, moneda_iva, monto_total, moneda_total, created_at, updated_at) VALUES
    (1, 'NV-00001', 2, 2, 2, 'BORRADOR', false, CURRENT_DATE, DATE_ADD(CURRENT_DATE, INTERVAL 30 DAY), 300000.00, 'CLP', 57000.00, 'CLP', 357000.00, 'CLP', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (2, 'NV-00002', 1, 1, 1, 'EN_PRODUCCION', false, CURRENT_DATE, DATE_ADD(CURRENT_DATE, INTERVAL 45 DAY), 850000.00, 'CLP', 161500.00, 'CLP', 1011500.00, 'CLP', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- ============================================================
-- 7.7. PLANIFICACIÓN DE PRODUCCIÓN Y HOJAS DE COMPRA
-- ============================================================
INSERT IGNORE INTO produccion_costeos (id_costeo, solicitud_costos_id, numero_costeo, estado, version) VALUES
    (1, 2, 'COST-000001', 'APROBADO', 1);

INSERT IGNORE INTO produccion_costeo_versiones (id_costeo_version, costeo_id, numero_version, fecha_creacion, usuario_creador) VALUES
    (1, 1, 1, CURRENT_TIMESTAMP, 'SISTEMA');

INSERT IGNORE INTO orden_produccion (idop, costeo_version_id, numeroop, nota_venta_id, estado, fecha_inicio, fecha_entrega_programada, observaciones, created_at, updated_at) VALUES
    (1, 1, 'OP- 00001', 2, 'EN_PROCESO', CURRENT_DATE, DATE_ADD(CURRENT_DATE, INTERVAL 30 DAY), 'Producción Pantalón Cargo Operario - Laboratorio Medcell', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

INSERT IGNORE INTO produccion_orden_items (idopitem, orden_produccion_id, articulo_id, nro_item, modelo, tela, color, talla, genero, codigo, lleva_logo, cantidad) VALUES
    (1, 1, 2, 1, 'Pantalón Cargo', 'Ripstop Impermeable', 'Verde', 'M', 'MASCULINO', 'PANT-CARGO-M', 'SI', 25),
    (2, 1, 2, 2, 'Pantalón Cargo', 'Ripstop Impermeable', 'Verde', 'L', 'MASCULINO', 'PANT-CARGO-L', 'SI', 25);

INSERT IGNORE INTO produccion_hojas_compra (id_hc, numero_hc, op_id, costeo_version_id, estado, fecha_generacion, observaciones) VALUES
    (1, 'HC-000001', 1, 1, 'APROBADA', CURRENT_DATE, 'HC generada para OP-000001');

INSERT IGNORE INTO produccion_hoja_compra_items (id_hc_item, hc_id, tipo_insumo, articulo_id, proveedor_id, nombre_insumo, consumo_unitario, cantidad_op, cantidad_requerida, precio_unitario_ref) VALUES
    (1, 1, 'TELA',      2, 1, 'Ripstop Impermeable',         1.8000, 50, 90.0000,  4500.00),
    (2, 1, 'ACCESORIO', 4, 2, 'Cierre YKK 60cm Metálico',    1.0000, 50, 50.0000,  1200.00),
    (3, 1, 'ACCESORIO', 5, 2, 'Botón Snap 15mm Nácar',       4.0000, 50, 200.0000, 150.00);

-- ============================================================
-- 7.8. CONTADORES DE DOCUMENTOS (document_counter)
-- ============================================================
-- Cada documento genera su propio correlativo vía NumeroDocumentoService.siguiente(tipo).
-- El contador parte en 0 (se crea on-demand), por lo que el primer documento sería
-- el número 1 y CHOCARÍA con los registros ya sembrados arriba. Por eso inicializamos
-- 'ultimo_numero' por ENCIMA del máximo existente por tipo: el próximo correlativo
-- continúa la secuencia (NV-0000003, OP-0000002, ...) sin colisionar ni reiniciar.
-- 'ultimo_numero' = cantidad de documentos ya existentes de ese tipo en este seed.
INSERT IGNORE INTO document_counter (tipo, ultimo_numero) VALUES
    ('NV',   2),
    ('EVN',  2),
    ('SCOS', 2),
    ('SCOT', 0),
    ('C',    1),
    ('OP',   1),
    ('HC',   1);

-- ============================================================
-- 7.8. MAESTROS GLOBALES (Moneda, Unidad de Medida)
-- ============================================================
INSERT IGNORE INTO moneda (id_moneda, codigo_moneda, nombre_moneda, simbolo) VALUES
    (1, 'CLP', 'Peso Chileno', '$'),
    (2, 'USD', 'Dólar Estadounidense', 'US$'),
    (3, 'EUR', 'Euro', '€');

INSERT IGNORE INTO unidad_medida (id_unidad_medida, nombre_unidad, abreviatura) VALUES
    (1, 'Metro',          'M'),
    (2, 'Metro Cuadrado', 'M2'),
    (3, 'Kilogramo',      'KG'),
    (4, 'Unidad',         'UN'),
    (5, 'Par',            'PAR'),
    (6, 'Caja',           'CJA'),
    (7, 'Rollo',          'ROL');

-- ============================================================
-- 7.9. MAESTROS DE TELA (Familia, Clasificación, etc)
-- ============================================================
INSERT IGNORE INTO familia_tela (id_familia_tela, codigo_familia, nombre_familia) VALUES
    (1, 'FT-01', 'Jersey'),
    (2, 'FT-02', 'Rib'),
    (3, 'FT-03', 'Interlock'),
    (4, 'FT-04', 'Fleece / French Terry'),
    (5, 'FT-05', 'Polar / Sherpa'),
    (6, 'FT-06', 'Scuba / Neoprene'),
    (7, 'FT-07', 'Piqué / Lacoste'),
    (8, 'FT-08', 'Tafetán / Popelín'),
    (9, 'FT-09', 'Sarga / Drill'),
    (10, 'FT-10', 'Denim'),
    (11, 'FT-11', 'Gabardina'),
    (12, 'FT-12', 'Terciopelo / Velvet'),
    (13, 'FT-13', 'Punto Roma / Bengalina'),
    (14, 'FT-14', 'Licra / Spandex plano'),
    (15, 'FT-15', 'Malla deportiva / Mesh'),
    (16, 'FT-16', 'Oxford / Ripstop'),
    (17, 'FT-17', 'Lana / Paño'),
    (18, 'FT-18', 'Lino / Ramio'),
    (19, 'FT-19', 'Seda / Satén'),
    (20, 'FT-20', 'Microfibra / Softshell');

INSERT IGNORE INTO clasificacion_tecnica (id_clasificacion_tecnica, nombre_clasificacion) VALUES
    (1, 'Tejido de Punto'),
    (2, 'Tejido Plano'),
    (3, 'No Tejido / TNT'),
    (4, 'Técnico / Funcional'),
    (5, 'Alta Prestación');

INSERT IGNORE INTO composicion (id_composicion, codigo_composicion, descripcion_composicion, clasificacion, uso_tipico) VALUES
    (1, 'CO-01',  '100% Algodón',                      'Natural',       'Poleras, básicos, ropa interior'),
    (2, 'CO-02',  '100% Poliéster',                    'Sintético',     'Deportivo, sublimación, forro'),
    (3, 'CO-03',  '50% Algodón / 50% Poliéster',       'Mixto',         'Poleras mix, uso general'),
    (4, 'CO-04',  '65% Poliéster / 35% Algodón',       'Mixto',         'Uniformes corporativos'),
    (5, 'CO-05',  '95% Algodón / 5% Elastano',         'Natural-Elást', 'Poleras ajustadas, casualwear'),
    (6, 'CO-06',  '95% Poliéster / 5% Elastano',       'Sint-Elást',    'Deportivo técnico, leggings'),
    (7, 'CO-07',  '60% Algodón / 40% Poliéster',       'Mixto',         'Prendas escolares, uniformes'),
    (8, 'CO-08',  '80% Algodón / 20% Poliéster',       'Mixto',         'Camisas, poleras premium mix'),
    (9, 'CO-09',  '100% Viscosa / Rayón',              'Celulósico',    'Blusas, vestidos, forro liviano'),
    (10, 'CO-10',  '100% Nylon / Poliamida',            'Sintético',     'Chaquetas, cortavientos'),
    (11, 'CO-11',  '88% Poliéster / 12% Elastano',      'Sint-Elást',    'Ropa deportiva compresión'),
    (12, 'CO-12',  '70% Algodón / 30% Poliéster',       'Mixto',         'Poleras escolares'),
    (13, 'CO-13',  '100% Lana Merino',                  'Natural',       'Paños, abrigos premium'),
    (14, 'CO-14',  '100% Lino',                         'Natural',       'Verano, guayaberas'),
    (15, 'CO-15',  '55% Lino / 45% Algodón',            'Natural',       'Ropa verano premium'),
    (16, 'CO-16',  '50% Viscosa / 50% Poliéster',       'Mixto',         'Vestidos, blusas'),
    (17, 'CO-17',  '90% Poliéster / 10% Elastano',      'Sint-Elást',    'Mallas, cycling'),
    (18, 'CO-18',  '100% Algodón Orgánico',             'Natural',       'Línea eco, bebé');

INSERT IGNORE INTO gramaje_tela (id_gramaje, codigo_gramaje, valor_gramos_m2, categoria_vestuario) VALUES
    (1, 'GR-01',  120.00, 'Verano / Ropa interior liviana'),
    (2, 'GR-02',  140.00, 'Camisetas básicas verano'),
    (3, 'GR-03',  160.00, 'Poleras estándar'),
    (4, 'GR-04',  180.00, 'Poleras premium / escolares'),
    (5, 'GR-05',  200.00, 'Poleras gruesas / licra deportiva'),
    (6, 'GR-06',  220.00, 'Rib / uniformes corporativos'),
    (7, 'GR-07',  240.00, 'Interlock / prendas doble cara'),
    (8, 'GR-08',  260.00, 'Fleece liviano / jogger'),
    (9, 'GR-09',  280.00, 'Buzo / French Terry'),
    (10, 'GR-10',  300.00, 'Fleece grueso / parka interior'),
    (11, 'GR-11',  320.00, 'Polar ligero'),
    (12, 'GR-12',  350.00, 'Polar medio / Sherpa'),
    (13, 'GR-13',  380.00, 'Polar grueso / abrigo'),
    (14, 'GR-14',  400.00, 'Paño / gabardina gruesa'),
    (15, 'GR-15',  450.00, 'Lana / abrigo invernal'),
    (16, 'GR-16',  500.00, 'Tapicería / lona técnica');

INSERT IGNORE INTO color_tela (id_color, codigo_color, descripcion_color, es_pantone) VALUES
    (1, 'COL-01',  'Blanco',           FALSE),
    (2, 'COL-02',  'Negro',            FALSE),
    (3, 'COL-03',  'Gris Claro',       FALSE),
    (4, 'COL-04',  'Gris Oscuro',      FALSE),
    (5, 'COL-05',  'Azul Navy',        FALSE),
    (6, 'COL-06',  'Azul Royal',       FALSE),
    (7, 'COL-07',  'Azul Petróleo',    FALSE),
    (8, 'COL-08',  'Rojo',             FALSE),
    (9, 'COL-09',  'Burdeo',           FALSE),
    (10, 'COL-10',  'Verde Botella',    FALSE),
    (11, 'COL-11',  'Verde Menta',      FALSE),
    (12, 'COL-12',  'Amarillo',         FALSE),
    (13, 'COL-13',  'Naranjo',          FALSE),
    (14, 'COL-14',  'Café',             FALSE),
    (15, 'COL-15',  'Beige / Arena',    FALSE),
    (16, 'COL-16',  'Celeste',          FALSE),
    (17, 'COL-17',  'Lila / Malva',     FALSE),
    (18, 'COL-18',  'Rosado',           FALSE),
    (19, 'COL-19',  'Fucsia',           FALSE),
    (20, 'COL-20',  'Caqui / Olive',    FALSE),
    (21, 'COL-21',  'Melange Gris',     FALSE),
    (22, 'COL-22',  'Melange Azul',     FALSE),
    (23, 'COL-23',  'Marino Melange',   FALSE);

INSERT IGNORE INTO atributo_tecnico (id_atributo, codigo_atributo, clasificacion, descripcion_tecnica, impacto_erp) VALUES
    (1, 'AT-01', 'Funcional',    'Antimicrobiano',                    'Certificado requerido para exportación'),
    (2, 'AT-02', 'Funcional',    'Transpirable / Moisture Wicking',   'Indicar en ficha técnica deportiva'),
    (3, 'AT-03', 'Funcional',    'UPF 50+ Protección Solar',          'Aplica a prendas outdoor/verano'),
    (4, 'AT-04', 'Funcional',    'Ignifugo / Retardante de Llama',    'Homologación obligatoria para EPP'),
    (5, 'AT-05', 'Funcional',    'Antiestático',                      'Requerido en ambientes industriales'),
    (6, 'AT-06', 'Funcional',    'Impermeable / DWR',                 'Aplica a softshell y cortavientos'),
    (7, 'AT-07', 'Funcional',    'Termorregulador / PCM',             'Indicar rango temperatura en ficha'),
    (8, 'AT-08', 'Acabado',      'Suavizado Enzimático',              'Proceso post-confección, afecta gramaje final'),
    (9, 'AT-09', 'Acabado',      'Sanforizado / Pre-Lavado',          'Control de encogimiento en orden de compra'),
    (10, 'AT-10', 'Acabado',      'Pilling Reducido',                  'Norma Martindale mínima 5000 ciclos'),
    (11, 'AT-11', 'Acabado',      'Easy Care / Anti-Arrugas',          'Indicar instrucción lavado en etiqueta'),
    (12, 'AT-12', 'Sustentable',  'GOTS Certified (Orgánico)',         'Código certificación en PO de compra'),
    (13, 'AT-13', 'Sustentable',  'Reciclado (GRS Certified)',         'Trazabilidad requerida desde proveedor');

-- ============================================================
-- 8. REINICIO DE SECUENCIAS (Consolidado)
-- ============================================================
ALTER TABLE areas AUTO_INCREMENT = 50;
ALTER TABLE roles AUTO_INCREMENT = 100;
ALTER TABLE usuarios AUTO_INCREMENT = 100;
ALTER TABLE clientes AUTO_INCREMENT = 100;
ALTER TABLE vendedores AUTO_INCREMENT = 200;
ALTER TABLE proveedores AUTO_INCREMENT = 100;
ALTER TABLE giros AUTO_INCREMENT = 100;
ALTER TABLE producto AUTO_INCREMENT = 100;

-- Artículos y catálogo
ALTER TABLE articulo AUTO_INCREMENT = 1000;
ALTER TABLE categoria_tela AUTO_INCREMENT = 100;
ALTER TABLE subcategoria_tela AUTO_INCREMENT = 100;

-- Plantillas
ALTER TABLE plantilla AUTO_INCREMENT = 100;
ALTER TABLE modelo_plantilla AUTO_INCREMENT = 1000;
ALTER TABLE descripcion_plantilla AUTO_INCREMENT = 5000;

-- SCOS / Costeos
ALTER TABLE solicitudes_costos AUTO_INCREMENT = 2000;
ALTER TABLE scos_telas AUTO_INCREMENT = 2000;
ALTER TABLE scos_logotipos AUTO_INCREMENT = 2000;
ALTER TABLE scos_plantilla_material_vinculo AUTO_INCREMENT = 5000;
ALTER TABLE produccion_costeos AUTO_INCREMENT = 100;
ALTER TABLE produccion_costeo_versiones AUTO_INCREMENT = 100;
ALTER TABLE produccion_costeo_items AUTO_INCREMENT = 5000;

-- EVN / NV / OP
ALTER TABLE evaluaciones_negocio AUTO_INCREMENT = 1000;
ALTER TABLE notas_venta AUTO_INCREMENT = 1000;
ALTER TABLE orden_produccion AUTO_INCREMENT = 100;
ALTER TABLE produccion_orden_items AUTO_INCREMENT = 1000;
ALTER TABLE produccion_hojas_compra AUTO_INCREMENT = 100;
ALTER TABLE produccion_hoja_compra_items AUTO_INCREMENT = 1000;

-- Contactos, Direcciones y Financieros
ALTER TABLE tipos_contacto AUTO_INCREMENT = 10;
ALTER TABLE contactos AUTO_INCREMENT = 10;
ALTER TABLE pais AUTO_INCREMENT = 10;
ALTER TABLE region AUTO_INCREMENT = 10;
ALTER TABLE comuna AUTO_INCREMENT = 10;
ALTER TABLE tipo_direccion AUTO_INCREMENT = 10;
ALTER TABLE direccion AUTO_INCREMENT = 10;
ALTER TABLE rubros AUTO_INCREMENT = 10;
ALTER TABLE banco AUTO_INCREMENT = 20;
ALTER TABLE tipo_cuenta_bancaria AUTO_INCREMENT = 10;
ALTER TABLE dato_bancario AUTO_INCREMENT = 10;

-- Maestros adicionales
ALTER TABLE moneda AUTO_INCREMENT = 10;
ALTER TABLE unidad_medida AUTO_INCREMENT = 10;
ALTER TABLE familia_tela AUTO_INCREMENT = 50;
ALTER TABLE clasificacion_tecnica AUTO_INCREMENT = 20;
ALTER TABLE composicion AUTO_INCREMENT = 50;
ALTER TABLE gramaje_tela AUTO_INCREMENT = 50;
ALTER TABLE color_tela AUTO_INCREMENT = 50;
ALTER TABLE atributo_tecnico AUTO_INCREMENT = 50;
