-- ============================================================
-- 0. SCHEMA FIXES (idempotent column-type corrections)
-- ============================================================
-- tamano was historically DOUBLE; ensure it is VARCHAR(50) to match the entity.
ALTER TABLE scos_logotipos MODIFY COLUMN tamano VARCHAR(50);

-- ============================================================
-- 0. TIPO ARTÍCULO
-- ============================================================
INSERT IGNORE INTO tipo_articulo (id_tipo_articulo, codigo, nombre) VALUES
    (1, 'TELA',                 'TELA'),
    (2, 'PRENDA_LISTA',         'PRENDA LISTA'),
    (3, 'ACCESORIO',            'ACCESORIO'),
    (4, 'PRENDA_CONFECCIONAR',  'PRENDA A CONFECCIONAR');

-- ============================================================
-- 1. ÁREAS
-- ============================================================
INSERT IGNORE INTO areas (id_area, nombre_area, descripcion) VALUES
    (1, 'GERENCIA COMERCIAL',    'PLANIFICACIÓN ESTRATÉGICA DE VENTAS Y MARKETING'),
    (2, 'ADMINISTRACIÓN',        'GESTIÓN DE RECURSOS Y PROCESOS INTERNOS'),
    (3, 'VENTAS',                'EJECUCIÓN DE FUERZA DE VENTA Y CAPTACIÓN'),
    (4, 'PRODUCCIÓN',            'OPERACIONES DE MANUFACTURA Y TRANSFORMACIÓN'),
    (5, 'LOGÍSTICA Y BODEGA',    'CONTROL DE EXISTENCIAS Y DESPACHO'),
    (6, 'ADQUISICIONES',         'GESTIÓN DE COMPRAS Y CADENA DE SUMINISTRO'),
    (7, 'FINANZAS',              'TESORERÍA, CONTABILIDAD Y CUMPLIMIENTO TRIBUTARIO'),
    (8, 'TECNOLOGÍA Y SISTEMAS', 'SOPORTE, INFRAESTRUCTURA Y DESARROLLO');

-- ============================================================
-- 2. ROLES
-- ============================================================
INSERT IGNORE INTO roles (id_role, nombre, descripcion, area_id) VALUES
    (1,  'JEFE_COMERCIAL',       'LÍDER DE ESTRATEGIA COMERCIAL',             1),
    (2,  'ASISTENTE_COMERCIAL',  'APOYO ADMINISTRATIVO COMERCIAL',            1),
    (3,  'JEFE_ADMIN',           'ENCARGADO DE ADMINISTRACIÓN GENERAL',       2),
    (4,  'ASISTENTE_ADMIN',      'APOYO OPERATIVO ADMINISTRATIVO',            2),
    (5,  'VENDEDOR',             'EJECUTIVO DE CUENTAS EN TERRENO',           3),
    (6,  'JEFE_PRODUCCION',      'SUPERVISOR DE PLANTA Y PROCESOS',           4),
    (7,  'OPERARIO_PRODUCCION',  'PERSONAL DE LÍNEA DE PRODUCCIÓN',           4),
    (8,  'JEFE_BODEGA',          'RESPONSABLE DE INVENTARIO Y WMS',           5),
    (9,  'ASISTENTE_BODEGA',     'RECEPCIÓN Y DESPACHO DE MERCADERÍA',        5),
    (10, 'JEFE_COMPRAS',         'RESPONSABLE DE NEGOCIACIÓN CON PROVEEDORES',6),
    (11, 'ASISTENTE_COMPRAS',    'GESTOR DE ÓRDENES DE COMPRA',               6),
    (12, 'CONTADOR_GENERAL',     'RESPONSABLE DE ESTADOS FINANCIEROS',        7),
    (13, 'ANALISTA_TESORERIA',   'GESTIÓN DE PAGOS Y FLUJO DE CAJA',          7),
    (14, 'DEVOPS_ENGINEER',      'GESTIÓN DE INFRAESTRUCTURA EN NUBE',        8),
    (15, 'FULLSTACK_DEVELOPER',  'DESARROLLO DE APLICACIONES INTERNAS',       8),
    (16, 'SOPORTE_TI',           'ATENCIÓN TÉCNICA A USUARIOS',               8);

-- ============================================================
-- 3. USUARIOS (RUTs y Teléfonos validados)
-- ============================================================
INSERT IGNORE INTO usuarios (id_usuario, run, nombre, apellidos, email, password, telefono, fecha_nacimiento, direccion, region, comuna, enabled) VALUES
    (1, '15342981-2', 'CARLOS',    'ITURRIETA MÉNDEZ',  'C.ITURRIETA@EMPRESA.CL', '$2a$10$xyz1234567890123456789', '+56988223344', '1985-03-12', 'Av. Providencia 1234', 'Metropolitana', 'Providencia', true),
    (2, '17589432-K', 'VALENTINA', 'LAGOS ESPINOZA',    'V.LAGOS@EMPRESA.CL',     '$2a$10$abc1234567890123456789', '+56977445566', '1992-07-22', 'Los Álamos 567',      'Valparaíso',    'Viña del Mar', true);

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
    (1, 'COMERCIO',      'EMPRESAS DEDICADAS A LA COMPRA Y VENTA DE BIENES Y SERVICIOS'),
    (2, 'SALUD',         'EMPRESAS DEL SECTOR SALUD, FARMACÉUTICO Y LABORATORIO'),
    (3, 'LOGÍSTICA',     'EMPRESAS DE TRANSPORTE, ALMACENAMIENTO Y DISTRIBUCIÓN'),
    (4, 'CONSTRUCCIÓN',  'EMPRESAS DEL RUBRO INMOBILIARIO, INFRAESTRUCTURA Y FERRETERÍA');

-- ============================================================
-- 4.2. GIROS (referenciando el rubro correspondiente)
-- ============================================================
INSERT IGNORE INTO giros (giro_id, codigo_sii, nombre_giro, descripcion_giro, rubro_id) VALUES
    (1, '521000', 'RETAIL',       'RETAIL Y VENTAS POR MENOR',          1),
    (2, '861000', 'SALUD',        'SERVICIOS MÉDICOS Y LABORATORIO',    2),
    (3, '492200', 'LOGÍSTICA',    'LOGÍSTICA Y TRANSPORTE',             3),
    (4, '410000', 'CONSTRUCCIÓN', 'CONSTRUCCIÓN Y FERRETERÍA',          4);

-- ============================================================
-- 4.3. PRODUCTOS
-- ============================================================
INSERT IGNORE INTO producto (producto_id, codigo_producto, nombre, descripcion, genero, color, creado_en, actualizado_en, activo) VALUES
    (1, 'PROD-POL-001', 'POLERA PIQUÉ CORPORATIVA',      'POLERA PIQUÉ 100% ALGODÓN PEINADO 180G CON BORDADO PECHO',         'UNISEX',     'AZUL NAVY',    CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, true),
    (2, 'PROD-PAN-002', 'PANTALÓN CARGO OPERARIO',        'PANTALÓN CARGO RIPSTOP IMPERMEABLE CON REFUERZO RODILLA Y BOLSILLOS','MASCULINO',  'VERDE OLIVA',  CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, true);

-- ============================================================
-- 5. CLIENTES (Modelo normalizado sin campos planos de contacto)
-- ============================================================
INSERT IGNORE INTO clientes (cliente_id, activo, razon_social, run_cliente, sigla, fk_giro) VALUES
    (1, true, 'HITES S.A.',              '96947020-9', 'S.A.',   1),
    (2, true, 'LABORATORIO MEDCELL',     '96706320-7', 'LTDA.',  2),
    (3, true, 'GEODIS WILSON',           '79699520-3', 'S.A.',   3);

-- ============================================================
-- 5.1. TIPOS DE CONTACTO Y CONTACTOS
-- ============================================================
INSERT IGNORE INTO tipos_contacto (tipo_contacto_id, descripcion_tipo_contacto) VALUES
    (1, 'GENERAL'),
    (2, 'COMERCIAL'),
    (3, 'FINANZAS');

INSERT IGNORE INTO contactos (contacto_id, nombre_contacto, telefono_contacto, email_contacto, tipo_contacto_id, fk_contacto) VALUES
    (1, 'CONTACTO HITES',          '+56227275000', 'CONTACTO.HITES@HITES.CL',       1, 1),
    (2, 'CONTACTO MEDCELL',        '+56224396000', 'COMPRAS@MEDCELL.CL',             1, 2),
    (3, 'CONTACTO GEODIS WILSON',  '+56223816500', 'INFO.CHILE@GEODIS.COM',          1, 3);

-- ============================================================
-- 5.2. GEOGRAFÍA Y DIRECCIONES
-- ============================================================
INSERT IGNORE INTO pais (pais_id, nombre_pais) VALUES
    (1, 'CHILE');

INSERT IGNORE INTO region (region_id, nombre_region, pais_id, creado_en, actualizado_en, activo) VALUES
    (1, 'METROPOLITANA', 1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, true);

INSERT IGNORE INTO comuna (comuna_id, nombre_comuna, region_id, creado_en, actualizado_en, activo) VALUES
    (1, 'SANTIAGO',   1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, true),
    (2, 'PROVIDENCIA',1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, true),
    (3, 'PUDAHUEL',   1, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, true);

INSERT IGNORE INTO tipo_direccion (tipo_direccion_id, descripcion) VALUES
    (1, 'PRINCIPAL'),
    (2, 'SUCURSAL');

INSERT IGNORE INTO direccion (direccion_id, calle, numero, depto, tipo_direccion_id, comuna_id, fk_direccion) VALUES
    (1, 'AV. KENNEDY',   '5413', 'OF. 201', 1, 2, 1),
    (2, 'HOLANDA',       '64',   NULL,       1, 2, 2),
    (3, 'LO BOZA',       '110',  NULL,       1, 3, 3);

-- ============================================================
-- 6. PROVEEDORES TEXTILES (Realistas para industria confección Chile)
-- ============================================================
INSERT IGNORE INTO proveedores (proveedor_id, activo, creado_en, actualizado_en, horario_atencion, razon_social_proveedor, run_proveedor, sigla, tipo_proveedor, fk_provee_giro) VALUES
    -- Proveedor de telas técnicas (Ripstop, Fleece, etc.)
    (1, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '08:30 - 18:00', 'TEXTIL CENTRAL LTDA.',                   '76123450-1', 'LTDA.',  'NACIONAL', 1),
    -- Proveedor de avíos, cierres y accesorios confección
    (2, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '09:00 - 17:30', 'AVÍOS Y ACCESORIOS DEL SUR LTDA.',       '76234561-2', 'LTDA.',  'NACIONAL', 1),
    -- Proveedor de telas deportivas e impermeables (importador)
    (3, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '09:00 - 18:00', 'INDUSTRIAS TEXTILES DEL PACÍFICO S.A.',  '76345672-3', 'S.A.',   'NACIONAL', 1),
    -- Proveedor de hilos industriales y etiquetas
    (4, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '08:00 - 17:00', 'HILOS Y BORDADOS INDUSTRIALES S.A.',     '76456783-4', 'S.A.',   'NACIONAL', 1);

-- ============================================================
-- 6.1. BANCOS Y ENTIDADES FINANCIERAS
-- ============================================================
INSERT IGNORE INTO banco (banco_id, nombre_banco, codigo_banco) VALUES
    (1, 'BANCO DE CHILE',   'BCH'),
    (2, 'BANCO ESTADO',     'BEST'),
    (3, 'SANTANDER CHILE',  'SAN'),
    (4, 'BCI',              'BCI'),
    (5, 'SCOTIABANK CHILE', 'SCOT'),
    (6, 'BANCO SECURITY',   'SEC'),
    (7, 'ITAÚ CORPBANCA',   'ITA');

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
    (1, 'TEJ-PLN', 'TEJIDO PLANO'),
    (2, 'TEJ-PNT', 'TEJIDO DE PUNTO'),
    (3, 'NO-TEJ',  'NO TEJIDO / TÉCNICO');

INSERT IGNORE INTO subcategoria_tela (id_subcategoria_tela, codigo_subcategoria_tela, nombre_subcategoria_tela, id_categoria_tela) VALUES
    (1, 'PLN-COTT', 'ALGODÓN PLANO',     1),
    (2, 'PLN-SYNT', 'SINTÉTICO PLANO',   1),
    (3, 'PNT-FLEE', 'FLEECE / POLAR',    2),
    (4, 'PNT-JRSY', 'JERSEY',            2),
    (5, 'TEC-IMPR', 'IMPERMEABLE TECH',  3);

-- ============================================================
-- 7.0.1 PARAMETRÍA TEXTIL (Familias, Clasificaciones, Composiciones,
-- Gramajes, Colores, Atributos Técnicos)
-- ============================================================

INSERT IGNORE INTO familia_tela (id_familia_tela, codigo_familia, nombre_familia) VALUES
    (1,  'T-GAB', 'GABARDINA'),
    (2,  'T-POP', 'POPELINA'),
    (3,  'T-OXF', 'OXFORD'),
    (4,  'T-MEZ', 'MEZCLILLA / DENIM'),
    (5,  'T-TAF', 'TAFETA'),
    (6,  'T-TSL', 'TASLAN'),
    (7,  'T-NYL', 'NYLON'),
    (8,  'T-HIP', 'HIPORA'),
    (9,  'T-MAL', 'MALLA'),
    (10, 'T-SOF', 'SOFTSHELL'),
    (11, 'T-POL', 'POLAR'),
    (12, 'T-MIC', 'MICROPOLAR'),
    (13, 'T-CHI', 'CHIPORRO'),
    (14, 'T-FRA', 'FRANELA'),
    (15, 'T-PIQ', 'PIQUÉ'),
    (16, 'T-RIB', 'RIB / PUÑO'),
    (17, 'T-BIO', 'BIO STRETCH / BISTRECH'),
    (18, 'T-CUE', 'CUELLO (TEJIDO)'),
    (19, 'T-AIS', 'AISLANTE TERMICO'),
    (20, 'T-ENT', 'ENTRETELA FUSIBLE'),
    (21, 'T-COR', 'CORDURA'),
    (22, 'T-QDR', 'DRYFIT'),
    (23, 'T-JEY', 'JERSEY'),
    (24, 'T-TRV', 'TREVIRA');

INSERT IGNORE INTO clasificacion_tecnica (id_clasificacion_tecnica, nombre_clasificacion) VALUES
    (1,  'TEJIDO PLANO ESTÁNDAR'),
    (2,  'TEJIDO PLANO LIVIANO'),
    (3,  'TEJIDO PLANO CAMISERÍA'),
    (4,  'TEJIDO PLANO PESADO'),
    (5,  'FORRERÍA / TEJIDO PLANO'),
    (6,  'SINTÉTICO / CORTAVIENTOS'),
    (7,  'SINTÉTICO LIVIANO'),
    (8,  'TÉCNICO IMPERMEABLE'),
    (9,  'SINTÉTICO PERFORADO / FORRO'),
    (10, 'TÉCNICO MULTICAPA'),
    (11, 'TÉRMICO SINTÉTICO'),
    (12, 'TÉRMICO SINTÉTICO LIVIANO'),
    (13, 'TÉRMICO AISLANTE'),
    (14, 'TÉRMICO BASE / PUNTO'),
    (15, 'TEJIDO DE PUNTO'),
    (16, 'TEJIDO DE PUNTO ELÁSTICO'),
    (17, 'COMPONENTE TEJIDO'),
    (18, 'AISLANTE / RELLENO'),
    (19, 'INSUMO ESTRUCTURAL'),
    (20, 'MOCHILAS'),
    (21, 'TELA DEPORTIVA'),
    (22, 'POLERAS');

INSERT IGNORE INTO composicion (id_composicion, codigo_composicion, descripcion_composicion, clasificacion, uso_tipico) VALUES
    (1,  'C-100AL',  '100% ALGODÓN',                                'PURA',                'IGNÍFUGO BASE, FRANELA PURA'),
    (2,  'C-100DE',  '100% ALGODÓN (14OZ)',                         'DENIM',               'MEZCLILLA INDUSTRIAL PESADA'),
    (3,  'C-100NY',  '100% NYLON',                                  'PURA',                'TASLAN, CORTAVIENTOS'),
    (4,  'C-100PA',  '100% POLIAMIDA',                              'REFUERZO TÉCNICO',    'CORDURA / REFUERZO DE RODILLAS Y CODOS'),
    (5,  'C-100PO',  '100% POLIÉSTER',                              'PURA',                'POLAR, OXFORD ECONÓMICO, MALLA'),
    (6,  'C-NPA',    '100% POLIÉSTER ("NAPA")',                     'AISLANTE BASE',       '"NAPA" ESTÁNDAR. RELLENO ECONÓMICO.'),
    (7,  'C-5050',   '50% ALGODÓN / 50% POLIÉSTER',                 'TEJIDO DE PUNTO',     'PIQUÉ DE ALTA DURABILIDAD'),
    (8,  'C-544303', '54% POLIÉSTER / 43% ALGODÓN / 3% ELASTANO',   'MEZCLA ELÁSTICA',     'POPELINA / GABARDINA STRETCH'),
    (9,  'C-593506', '59% POLIÉSTER / 35% VISCOSA / 6% ELASTANO',   'MEZCLA ELÁSTICA',     'POPELINA / GABARDINA STRETCH'),
    (10, 'C-6040',   '60% ALGODÓN / 40% POLIÉSTER',                 'MEZCLA CONFORT',      'ROPA DE TRABAJO PREMIUM'),
    (11, 'C-653302', '65% POLIÉSTER / 33% ALGODÓN / 2% ELASTANO',   'MEZCLA ELÁSTICA',     'POPELINA / GABARDINA STRETCH'),
    (12, 'C-6535',   '65% POLIÉSTER / 35% ALGODÓN',                 'MEZCLA ESTÁNDAR',     'POPELINA, GABARDINA (WORKWEAR)'),
    (13, 'C-702802', '70% ALGODÓN / 28% POLIÉSTER / 2% ELASTANO',   'DENIM ELÁSTICO',      'MEZCLILLA FLEX'),
    (14, 'C-8020AL', '80% ALGODÓN / 20% POLIÉSTER',                 'ALTA RESPIRABILIDAD', 'FRANELAS DE ALTO GRAMAJE O CANVAS'),
    (15, 'C-8020PO', '80% POLIÉSTER / 20% ALGODÓN',                 'MEZCLA ECONÓMICA',    'POPELINA PROMOCIONAL'),
    (16, 'C-9010',   '90% POLIÉSTER / 10% ELASTANO',                'ELÁSTICA',            'SOFTSHELL, ROPA TÉCNICA'),
    (17, 'C-9208',   '92% POLIÉSTER / 8% ELASTANO',                 'MEZCLA ELÁSTICA',     'POPELINA / GABARDINA STRETCH'),
    (18, 'C-9505',   '95% POLIÉSTER / 5% ELASTANO',                 'MEZCLA ELÁSTICA',     'POPELINA / GABARDINA STRETCH'),
    (19, 'C-9703',   '97% POLIÉSTER / 3% ELASTANO',                 'MEZCLA ELÁSTICA',     'POPELINA / GABARDINA STRETCH'),
    (20, 'C-9802',   '98% ALGODÓN / 2% ELASTANO',                   'ELÁSTICA',            'BIO STRETCH, GABARDINA FLEX'),
    (21, 'C-FLP',    'FIBRA HUECA SILICONADA ("FLOPY")',            'AISLANTE VOLUMEN',    'ALTO VOLUMEN (LOFT), TACTO PLUMA, PARKAS GRUESAS.'),
    (22, 'C-TRM',    'MICROFIBRA TÉRMICA ("TERMALOFT")',            'AISLANTE TÉCNICO',    'RETENCIÓN DE CALOR PREMIUM, BAJO VOLUMEN, ALTA DENSIDAD.'),
    (23, 'C-ARAM',   'MODACRÍLICO / ALGODÓN / ARAMIDA',             'ANTIARCO ELÉCTRICO',  'ROPA IGNÍFUGA CERTIFICADA');

INSERT IGNORE INTO gramaje_tela (id_gramaje, codigo_gramaje, valor_gramos_m2, categoria_vestuario) VALUES
    (1,  'G-100',  100.00, NULL),
    (2,  'G-115',  115.00, 'POPELINA / MICROFIBRA (LIVIANA)'),
    (3,  'G-120',  120.00, 'RELLENO TERMICO'),
    (4,  'G-150',  150.00, 'OXFORD / POPELINA (ESTÁNDAR) / RELLENO TERMICO'),
    (5,  'G-165',  165.00, 'HIPORA / OXFORD / POPELINA (PESADAS)'),
    (6,  'G-175',  175.00, 'RELLENO TERMICO'),
    (7,  'G-190',  190.00, 'GABARDINA LIVIANA (VERANO / SALUD)'),
    (8,  'G-210',  210.00, 'PIQUÉ / POLERAS BASE'),
    (9,  'G-300',  300.00, 'SOFTSHELL / POLAR'),
    (10, 'G-7OZ',  237.00, 'GABARDINA / TWILL ESTÁNDAR'),
    (11, 'G-8OZ',  271.00, 'GABARDINA PESADA / PANTALÓN CARGO'),
    (12, 'G-9OZ',  305.00, 'GABARDINA ALTA RESISTENCIA'),
    (13, 'G-10OZ', 339.00, 'MEZCLILLA LIVIANA / GABARDINA EXTREMA'),
    (14, 'G-14OZ', 475.00, 'MEZCLILLA INDUSTRIAL PESADA (MINERÍA)');

INSERT IGNORE INTO color_tela (id_color, codigo_color, descripcion_color, es_pantone) VALUES
    (1,  '0',    'CRUDO / BASE / SIN COLOR',    false),
    (2,  'BCO',  'BLANCO',                      false),
    (3,  'NEG',  'NEGRO',                       false),
    (4,  'AZMA', 'AZUL MARINO',                 false),
    (5,  'AZFR', 'AZUL FRANCIA',                false),
    (6,  'AZRE', 'AZUL REY / AZULINO',          false),
    (7,  'AZVI', 'AZUL VIGILANTE / GUARDIA',    false),
    (8,  'AZCL', 'CELESTE / AZUL CLARO',        false),
    (9,  'ROJ',  'ROJO',                        false),
    (10, 'BEO',  'BURDEO',                      false),
    (11, 'GRCL', 'GRIS CLARO',                  false),
    (12, 'GRME', 'GRIS MEDIO',                  false),
    (13, 'GROS', 'GRIS OSCURO',                 false),
    (14, 'MELG', 'MELANGE (GRIS/MIXTO)',        false),
    (15, 'VEBO', 'VERDE BOTELLA / OSCURO',      false),
    (16, 'VEES', 'VERDE ESMERALDA',             false),
    (17, 'CALP', 'CALIPSO',                     false),
    (18, 'TURQ', 'TURQUESA',                    false),
    (19, 'BGE',  'BEIGE / CAQUI',               false),
    (20, 'CAF',  'CAFÉ / MARRÓN',               false),
    (21, 'AMA',  'AMARILLO',                    false),
    (22, 'NAR',  'NARANJA',                     false),
    (23, 'PET',  'PETRÓLEO',                    false),
    (24, 'AMFL', 'AMARILLO FLÚOR',              false),
    (25, 'NAFL', 'NARANJA FLÚOR',               false),
    (26, 'ROFL', 'ROJO FLÚOR',                  false),
    (27, 'EST',  'ESTAMPADO (VARIOS)',          false);

INSERT IGNORE INTO atributo_tecnico (id_atributo, codigo_atributo, clasificacion, descripcion_tecnica, impacto_erp) VALUES
    (1,  'A-000',   'ESTÁNDAR',     'SIN TRATAMIENTOS ESPECIALES',         'TELA BASE POR DEFECTO.'),
    (2,  'A-RIP',   'CONSTRUCCIÓN', 'RIPSTOP (ANTIDESGARRO)',              'MODIFICA LA CUADRÍCULA DEL TEJIDO PLANO.'),
    (3,  'A-UV',    'QUÍMICO',      'PROTECCIÓN UV (UPF 50+)',             'BAÑO PROTECTOR CORPORATIVO ESTÁNDAR.'),
    (4,  'A-WR',    'FÍSICO',       'REPELENCIA AL AGUA',                  'ACABADO SUPERFICIAL (RESISTENCIA AL ROCÍO).'),
    (5,  'A-BRT',   'FISICO',       'RESPIRABILIDAD',                      'MEMBRANA INTERNA RESPIRABLE'),
    (6,  'A-WP',    'FÍSICO',       'IMPERMEABILIDAD',                     'MEMBRANA INTERNA O RECUBRIMIENTO (EJ. PU).'),
    (7,  'A-FR',    'QUÍMICO',      'FIRE RETARDANT (IGNÍFUGO)',           'CERTIFICACIÓN DE SEGURIDAD. MULTIPLICA EL COSTO.'),
    (8,  'A-100FR', 'QUIMICO',      'TRATAMIENTO FR',                      'ROPA IGNÍFUGA BÁSICA'),
    (9,  'A-ACI',   'QUÍMICO',      'ANTIÁCIDO',                           'REQUISITO MINERO O DE LABORATORIO.'),
    (10, 'A-AFLUD', 'QUÍMICO',      'ANTIFLUIDO',                          'ACABADO DE FÁCIL LAVADO (ROPA CLÍNICA/GASTRONÓMICA).'),
    (11, 'A-ANT',   'FÍSICO',       'ANTIESTÁTICO',                        'INSERCIÓN DE HILO DE CARBONO PARA DISIPACIÓN.'),
    (12, 'A-ALU',   'FISICO',       'REFLECTIVO ALUMINIZADO (FORRO)',      'PARA TERMICIDAD INTERNA'),
    (13, 'A-ACOL',  'FISICO',       'ACOLCHADO ESPECIAL',                  'PARA RELLENOS TERMICOS + FORRO'),
    (14, 'A-ABACT', 'FISICO',       'ANTIBACTERIANO',                      'ROPA CLÍNICA');

-- ============================================================
-- 7.0.2 INSUMOS Y ACCESORIOS — Tipos y sus atributos dinámicos
-- ============================================================

INSERT IGNORE INTO tipo_accesorio (id_tipo_accesorio, codigo, nombre) VALUES
    (1,  'CIE', 'CIERRE'),
    (2,  'BRO', 'BROCHE'),
    (3,  'BOT', 'BOTÓN'),
    (4,  'TAN', 'TANKA'),
    (5,  'OJE', 'OJETILLOS'),
    (6,  'TIR', 'TIRADOR'),
    (7,  'VEL', 'VELCRO'),
    (8,  'CHV', 'CHAVETA DE AJUSTE'),
    (9,  'TTP', 'TIP-TOP'),
    (10, 'ESC', 'ESCALERILLA REGULADORA'),
    (11, 'ELA', 'ELÁSTICO'),
    (12, 'COR', 'CORDÓN'),
    (13, 'VIV', 'VIVO'),
    (14, 'SES', 'SESGO'),
    (15, 'CNE', 'CINTA ESPIGA'),
    (16, 'CNF', 'CINTA FALLA (MOCHILA)'),
    (17, 'CNR', 'CINTA REFLECTANTE'),
    (18, 'CSC', 'CINTA SELLADO DE COSTURA');

INSERT IGNORE INTO atributo_accesorio_definicion (id_definicion, id_tipo_accesorio, nombre_campo, tipo_dato, opciones, orden, requerido) VALUES
    (1,  1,  'TIPO',             'LISTA',            'DP|NY|NY Invertido|Impermeable|Bronce|Ignífugo', 1, true),
    (2,  1,  'N°',               'LISTA',            '3|5|7', 2, false),
    (3,  1,  'TERMINAL',         'LISTA',            'Fijo|Separable', 3, true),
    (4,  1,  'CARRO',            'LISTA',            'Simple|Doble|Reversible', 4, true),
    (5,  1,  'MEDIDA',           'LISTA',            '12cm|15cm|18cm|20cm|50cm|55cm|60cm|65cm|70cm|75cm|80cm|85cm|90cm|95cm|100cm', 5, true),
    (6,  1,  'COLOR',            'REFERENCIA_COLOR', NULL, 6, true),
    (7,  2,  'MATERIAL',         'LISTA',            'Plástico|Metálico', 1, true),
    (8,  2,  'TIPO',             'LISTA',            'Corriente|Punta de bola|Envejecido', 2, true),
    (9,  2,  'COLOR',            'REFERENCIA_COLOR', NULL, 3, true),
    (10, 3,  'MATERIAL',         'LISTA',            'Plástico', 1, true),
    (11, 3,  'DIÁMETRO (LÍNEA)', 'LISTA',            '18|24|28', 2, true),
    (12, 3,  'COLOR',            'REFERENCIA_COLOR', NULL, 3, true),
    (13, 4,  'MATERIAL',         'LISTA',            'Plástico|Metálica', 1, true),
    (14, 4,  'TIPO',             'LISTA',            'Simple|Doble|Envejecido', 2, true),
    (15, 4,  'COLOR',            'REFERENCIA_COLOR', NULL, 3, true),
    (16, 5,  'MATERIAL',         'LISTA',            'Plástico|Metálico', 1, true),
    (17, 5,  'COLOR',            'LISTA',            'Mismos colores que las telas|Metal|Envejecido', 2, true),
    (18, 6,  'TIPO',             'LISTA',            'Corriente|Marca de Cliente|Logo Antuan', 1, true),
    (19, 6,  'COLOR',            'REFERENCIA_COLOR', NULL, 2, true),
    (20, 7,  'MEDIDA (ANCHO)',   'LISTA',            '1.0cm|1.5cm|2.0cm|2.5cm|4.0cm|5.0cm', 1, true),
    (21, 7,  'COLOR',            'REFERENCIA_COLOR', NULL, 2, true),
    (22, 8,  'MATERIAL',         'LISTA',            'Plástica', 1, true),
    (23, 8,  'COLOR',            'LISTA',            'Negro', 2, true),
    (24, 9,  'MATERIAL',         'LISTA',            'Plástico', 1, true),
    (25, 9,  'COLOR',            'LISTA',            'Negro', 2, true),
    (26, 10, 'MATERIAL',         'LISTA',            'Plástica|Metálica', 1, true),
    (27, 10, 'COLOR',            'LISTA',            'Negro|Metal', 2, true),
    (28, 11, 'MEDIDA (ANCHO)',   'LISTA',            '1.0cm|1.5cm|2.0cm|2.5cm|3.0cm|4.0cm|5.0cm', 1, true),
    (29, 11, 'COLOR',            'LISTA',            'Blanco|Negro', 2, true),
    (30, 12, 'MATERIAL',         'LISTA',            'Elasticado|Rígido', 1, true),
    (31, 12, 'COLOR',            'REFERENCIA_COLOR', NULL, 2, true),
    (32, 13, 'MATERIAL',         'LISTA',            'Tela|Reflectante', 1, true),
    (33, 13, 'COLOR',            'LISTA',            'Gris|Día y Noche', 2, true),
    (34, 14, 'MATERIAL',         'LISTA',            'Elasticado|Rígido', 1, true),
    (35, 14, 'COLOR',            'REFERENCIA_COLOR', NULL, 2, true),
    (36, 15, 'COMPOSICIÓN',      'LISTA',            '50% Poliéster - 50% Algodón|100% Poliéster|100% Algodón', 1, true),
    (37, 15, 'MEDIDA (ANCHO)',   'LISTA',            '1.0cm|1.5cm|2.0cm', 2, true),
    (38, 15, 'COLOR',            'REFERENCIA_COLOR', NULL, 3, true),
    (39, 16, 'COMPOSICIÓN',      'LISTA',            '50% Poliéster - 50% Algodón|100% Poliéster|100% Algodón', 1, true),
    (40, 16, 'MEDIDA (ANCHO)',   'LISTA',            '1.0cm|1.5cm|2.0cm|2.5cm|3.0cm|4.0cm', 2, true),
    (41, 17, 'MATERIAL',         'LISTA',            'Tela|PET - Poliéster', 1, true),
    (42, 17, 'DISEÑO',           'LISTA',            'Lisa|Segmentada', 2, true),
    (43, 17, 'COLOR',            'LISTA',            'Gris|Día y Noche', 3, true),
    (44, 17, 'MEDIDA (ANCHO)',   'LISTA',            '1"|2"', 4, true),
    (45, 17, 'PEGADO',           'LISTA',            'Cosida|Termofusionada|Corriente', 5, true),
    (46, 17, 'OBSERVACIONES',    'TEXTO',            NULL, 6, false);

-- ============================================================
-- 7.1. ARTÍCULOS (Catálogo Base)
-- ============================================================
INSERT IGNORE INTO articulo (id_articulo, codigo_articulo, nombre_articulo, descripcion_articulo, codigo_barra, id_tipo_articulo, activo, id_categoria_tela, id_subcategoria_tela) VALUES
    (1, 'ART-FLEE-001', 'POLAR FLEECE 280 GSM',        'TELA POLAR FLEECE GRAMAJE 280 G/M², ANTI-PILLING',         NULL, 1, true, 2, 3),
    (2, 'ART-IMPR-001', 'RIPSTOP IMPERMEABLE 150 GSM', 'TEJIDO TÉCNICO RIPSTOP 150G IMPERMEABILIZADO DWR',          NULL, 1, true, 3, 5),
    (3, 'ART-JRSY-001', 'JERSEY PIQUÉ ALGODÓN 180 GSM','JERSEY PIQUÉ 100% ALGODÓN PEINADO COMPACTO 180G',          NULL, 1, true, 2, 4),
    (4, 'ART-ACC-001',  'CIERRE YKK 60CM METÁLICO',    'CIERRE METÁLICO YKK NYLON NO. 5, LONGITUD 60CM',           NULL, 3, true, NULL, NULL),
    (5, 'ART-ACC-002',  'BOTÓN SNAP 15MM NÁCAR',        'BOTÓN TIPO SNAP NACARADO RESISTENTE AL LAVADO, 15MM',      NULL, 3, true, NULL, NULL),
    (6, 'ART-ACC-003',  'HILO INDUSTRIAL 40/2 POLIÉSTER','HILO COSTURA INDUSTRIAL POLIÉSTER 40/2 CONE 5000M',       NULL, 3, true, NULL, NULL),
    (7, 'ART-ACC-004',  'CINTA REFLECTANTE 50MM',       'CINTA REFLECTANTE CERTIFICADA EN ISO 20471, ANCHO 50MM',   NULL, 3, true, NULL, NULL),
    -- Prendas a confeccionar (id_tipo_articulo = 4)
    (8,  'ART-PRC-001', 'POLERÓN',  'Prenda tipo polerón / sudadera con capucha',   NULL, 4, true, NULL, NULL),
    (9,  'ART-PRC-002', 'PARKA',    'Prenda tipo parka con aislación',               NULL, 4, true, NULL, NULL),
    (10, 'ART-PRC-003', 'CHALECO',  'Chaleco sin mangas funcional o corporativo',    NULL, 4, true, NULL, NULL),
    (11, 'ART-PRC-004', 'POLERA',   'Polera manga corta o larga',                    NULL, 4, true, NULL, NULL),
    (12, 'ART-PRC-005', 'PANTALÓN', 'Pantalón de trabajo o corporativo',             NULL, 4, true, NULL, NULL);

-- ============================================================
-- 7.2. CATÁLOGO DE CAMPOS DE PLANTILLA
-- ============================================================
INSERT IGNORE INTO plantilla (id_plantilla, nombre_campo) VALUES
    (1,  'GORRO'),
    (2,  'CUELLO'),
    (3,  'ABOTONADURA / CIERRE'),
    (4,  'CORTES Y APLICACIONES'),
    (5,  'FUELLES'),
    (6,  'MANGAS'),
    (7,  'PUÑOS'),
    (8,  'PRETINAS / RUEDO'),
    (9,  'BOLSILLOS'),
    (10, 'OBS. DEL MODELO');

-- ============================================================
-- 7.3. MODELO PLANTILLA (Mapeo Artículo e Id Plantilla)
-- ============================================================
INSERT IGNORE INTO modelo_plantilla (id_modelo_plantilla, id_articulo, campos) VALUES
    (1, 1, 'GORRO,BOLSILLOS,MANGAS,PUÑOS'),
    (2, 2, 'CUELLO,BOLSILLOS,ABOTONADURA / CIERRE,FUELLES'),
    (3, 3, 'MANGAS,PRETINAS / RUEDO,CUELLO'),
    -- Plantillas prendas a confeccionar (nombres display en mayúsculas)
    (4, 8,  'GORRO,CUELLO,ABOTONADURA / CIERRE,CORTES Y APLICACIONES,MANGAS,PUÑOS,BOLSILLOS,OBS. DEL MODELO'),
    (5, 9,  'GORRO,CUELLO,ABOTONADURA / CIERRE,FUELLES,MANGAS,PUÑOS,BOLSILLOS,OBS. DEL MODELO'),
    (6, 10, 'CUELLO,ABOTONADURA / CIERRE,CORTES Y APLICACIONES,BOLSILLOS,OBS. DEL MODELO'),
    (7, 11, 'CUELLO,MANGAS,PRETINAS / RUEDO,OBS. DEL MODELO'),
    (8, 12, 'ABOTONADURA / CIERRE,FUELLES,PRETINAS / RUEDO,BOLSILLOS,OBS. DEL MODELO');

-- ============================================================
-- 7.4. SOLICITUDES DE COSTOS (SCOS)
-- ============================================================
-- SCOS-000001: 100 Poleras Piqué para HITES — aprobado, con costo real
-- SCOS-000002: 50 Pantalones Cargo para MEDCELL — aprobado, con costo real
INSERT INTO solicitudes_costos (id_scos, numero, estado, tipo, cliente_id, vendedor_id, articulo_descripcion, nombre_prenda, genero, tallaje, es_muestra, has_logo, cantidad, fecha, costo_total) VALUES
    (1, 'SCOS-000001', 'PENDIENTE', 'SCOS', 1, 1, 'POLERA',   'POLERA PIQUÉ CORPORATIVA',  'UNISEX',    'ANTUAN SA', false, true,  100, CURRENT_DATE, 1060000.00),
    (2, 'SCOS-000002', 'APROBADA', 'SCOS', 2, 2, 'PANTALON', 'PANTALÓN CARGO OPERARIO',   'MASCULINO', 'CLIENTE',   false, false,  50, CURRENT_DATE,  858550.00)
ON DUPLICATE KEY UPDATE
    estado = VALUES(estado),
    costo_total = VALUES(costo_total);

-- Telas de la SCOS-000001 (Polera Piqué)
INSERT IGNORE INTO scos_telas (id_scos_tela, solicitud_costos_id, aplicacion, descripcion, composicion, color, peso, unidad_medida) VALUES
    (1, 1, 'CUERPO', 'JERSEY PIQUÉ ALGODÓN 180 GSM', '100% ALGODÓN PEINADO', 'AZUL NAVY', 180, 'MTRS');

-- Telas de la SCOS-000002 (Pantalón Cargo)
INSERT IGNORE INTO scos_telas (id_scos_tela, solicitud_costos_id, aplicacion, descripcion, composicion, color, peso, unidad_medida) VALUES
    (2, 2, 'CUERPO', 'RIPSTOP IMPERMEABLE 150 GSM', '100% POLIÉSTER DWR', 'VERDE OLIVA', 150, 'MTRS');

-- ============================================================
-- 7.5. EVALUACIONES DE NEGOCIO (EVN)
-- ============================================================
INSERT IGNORE INTO evaluaciones_negocio (id_evn, numero, referencia, cliente_nombre, cliente_id, vendedor_id, estado, fecha_evaluacion, porcentaje_comision, created_at, updated_at) VALUES
    (1, 'EVN-000001', 'COTIZACIÓN POLERAS CORPORATIVAS TEMPORADA 2024', 'HITES S.A.',          1, 1, 'ADJUDICADA', CURRENT_DATE, 5.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (2, 'EVN-000002', 'LICITACIÓN PANTALONES CARGO PERSONAL OPERATIVO',  'LABORATORIO MEDCELL', 2, 2, 'ADJUDICADA', CURRENT_DATE, 3.50, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Items de EVN-000001 (100 Poleras Piqué para HITES) — articulo_id=3: Jersey Piqué
INSERT IGNORE INTO evaluacion_negocio_items (idevni, evaluacion_negocio_id, proveedor_id, articulo_id, nro_item, descripcion, modelo, tela, genero, cantidad, precio_unitario, moneda_precio_unitario, costo_unitario, moneda_costo_unitario, costo_producto, costo_logo, tipo_item) VALUES
    (1, 1, 1, 3, 1, 'POLERA PIQUÉ CORPORATIVA BORDADA', 'SLIM FIT MANGA CORTA', 'JERSEY PIQUÉ 180G', 'UNISEX', 100, 10600.00, 'CLP', 8400.00, 'CLP', 6200.00, 2200.00, 'OP');

-- Items de EVN-000002 (50 Pantalones Cargo para MEDCELL) — articulo_id=2: Ripstop Impermeable
INSERT IGNORE INTO evaluacion_negocio_items (idevni, evaluacion_negocio_id, proveedor_id, articulo_id, nro_item, descripcion, modelo, tela, genero, cantidad, precio_unitario, moneda_precio_unitario, costo_unitario, moneda_costo_unitario, costo_producto, tipo_item) VALUES
    (2, 2, 1, 2, 1, 'PANTALÓN CARGO OPERARIO REFORZADO', 'CARGO 6 BOLSILLOS', 'RIPSTOP IMPERMEABLE 150G', 'MASCULINO', 50, 23100.00, 'CLP', 17171.00, 'CLP', 17171.00, 'OP');

-- Condiciones comerciales EVN-000001 (HITES)
-- anticipo 50%, flete incluido, garantía 30 días, validez oferta 30 días
-- (Se guarda vía metadatos en la entidad EVN, no hay tabla separada aquí)

-- ============================================================
-- 7.6. NOTAS DE VENTA (NV)
-- ============================================================
-- NV-0000001: MEDCELL, 50 pantalones → EN_PRODUCCION (tiene OP activa)
-- NV-0000002: HITES, 100 poleras    → EMITIDA (sin OP aún, comercial adjudicada)
INSERT IGNORE INTO notas_venta (id_nv, numeronv, evaluacion_negocio_id, cliente_id, vendedor_id, estado, es_kit, fecha_emision, fecha_entrega_estimada, monto_subtotal, moneda_subtotal, monto_iva, moneda_iva, monto_total, moneda_total, created_at, updated_at) VALUES
    (1, 'NV-0000001', 2, 2, 2, 'EN_PRODUCCION', false, CURRENT_DATE, DATE_ADD(CURRENT_DATE, INTERVAL 45 DAY),  858550.00, 'CLP', 163124.50, 'CLP', 1021674.50, 'CLP', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (2, 'NV-0000002', 1, 1, 1, 'EMITIDA',        false, CURRENT_DATE, DATE_ADD(CURRENT_DATE, INTERVAL 30 DAY), 1060000.00, 'CLP', 201400.00, 'CLP', 1261400.00, 'CLP', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Items de NV-0000001 (50 Pantalones Cargo para MEDCELL — 2 tallas) — articulo_id=2: Ripstop
INSERT IGNORE INTO notas_venta_items (id_item_nv, nota_venta_id, nro_item, modelo, tela, composicion, color, talla, genero, codigo, proveedor_id, lleva_logo, tipo_item, requiere_ot, cantidad, precio_unitario, moneda_precio_unitario, total, moneda_total, articulo_id) VALUES
    (1, 1, 1, 'CARGO 6 BOLSILLOS', 'RIPSTOP IMPERMEABLE 150G', '100% POLIÉSTER DWR', 'VERDE OLIVA', 'M', 'MASCULINO', 'PAN-CARGO-M', 1, 'NO', 'OP', false, 25, 17171.00, 'CLP', 429275.00, 'CLP', 2),
    (2, 1, 2, 'CARGO 6 BOLSILLOS', 'RIPSTOP IMPERMEABLE 150G', '100% POLIÉSTER DWR', 'VERDE OLIVA', 'L', 'MASCULINO', 'PAN-CARGO-L', 1, 'NO', 'OP', false, 25, 17171.00, 'CLP', 429275.00, 'CLP', 2);

-- Tallas NV-0000001
INSERT IGNORE INTO notas_venta_item_tallas (id_item_talla, item_id, talla, cantidad) VALUES
    (1, 1, 'M', 25),
    (2, 2, 'L', 25);

-- Items de NV-0000002 (100 Poleras Piqué para HITES — distribución de tallas) — articulo_id=3: Jersey Piqué
INSERT IGNORE INTO notas_venta_items (id_item_nv, nota_venta_id, nro_item, modelo, tela, composicion, color, talla, genero, codigo, proveedor_id, lleva_logo, tipo_item, requiere_ot, cantidad, precio_unitario, moneda_precio_unitario, total, moneda_total, articulo_id) VALUES
    (3, 2, 1, 'SLIM FIT MANGA CORTA', 'JERSEY PIQUÉ 180G', '100% ALGODÓN PEINADO', 'AZUL NAVY', 'S',  'UNISEX', 'POL-PIQUE-S',  1, 'SI', 'OP', false, 20, 10600.00, 'CLP', 212000.00, 'CLP', 3),
    (4, 2, 2, 'SLIM FIT MANGA CORTA', 'JERSEY PIQUÉ 180G', '100% ALGODÓN PEINADO', 'AZUL NAVY', 'M',  'UNISEX', 'POL-PIQUE-M',  1, 'SI', 'OP', false, 40, 10600.00, 'CLP', 424000.00, 'CLP', 3),
    (5, 2, 3, 'SLIM FIT MANGA CORTA', 'JERSEY PIQUÉ 180G', '100% ALGODÓN PEINADO', 'AZUL NAVY', 'L',  'UNISEX', 'POL-PIQUE-L',  1, 'SI', 'OP', false, 30, 10600.00, 'CLP', 318000.00, 'CLP', 3),
    (6, 2, 4, 'SLIM FIT MANGA CORTA', 'JERSEY PIQUÉ 180G', '100% ALGODÓN PEINADO', 'AZUL NAVY', 'XL', 'UNISEX', 'POL-PIQUE-XL', 1, 'SI', 'OP', false, 10, 10600.00, 'CLP', 106000.00, 'CLP', 3);

-- Tallas NV-0000002
INSERT IGNORE INTO notas_venta_item_tallas (id_item_talla, item_id, talla, cantidad) VALUES
    (3, 3, 'S',  20),
    (4, 4, 'M',  40),
    (5, 5, 'L',  30),
    (6, 6, 'XL', 10);

-- ============================================================
-- 7.7. COSTEO: PANTALÓN CARGO OPERARIO (50 unidades — MEDCELL)
-- ============================================================
-- Costeo asociado a SCOS-000002 (Pantalón Cargo), estado APROBADO
INSERT INTO produccion_costeos (id_costeo, solicitud_costos_id, numero_costeo, estado, version,
    costo_hilos, costo_mano_obra, costo_etiquetas, costo_embalaje, costo_flete,
    porcentaje_costo_fijo, costo_total_materia_prima, margen_bruto_sugerido, precio_venta_sugerido) VALUES
    (1, 2, 'COST-0000001', 'APROBADO', 1,
    12500.00, 190000.00, 7500.00, 10800.00, 25000.00,
    10.00, 586250.00, 25.00, 1220340.00)
ON DUPLICATE KEY UPDATE
    estado                    = VALUES(estado),
    version                   = VALUES(version),
    costo_hilos               = VALUES(costo_hilos),
    costo_mano_obra           = VALUES(costo_mano_obra),
    costo_etiquetas           = VALUES(costo_etiquetas),
    costo_embalaje            = VALUES(costo_embalaje),
    costo_flete               = VALUES(costo_flete),
    porcentaje_costo_fijo     = VALUES(porcentaje_costo_fijo),
    costo_total_materia_prima = VALUES(costo_total_materia_prima),
    margen_bruto_sugerido     = VALUES(margen_bruto_sugerido),
    precio_venta_sugerido     = VALUES(precio_venta_sugerido);

-- Items del costeo (consumos y costos POR UNIDAD de prenda)
--   Ripstop Impermeable:  1.80 m/prenda × $4,800/m  = $8,640
--   Cierre YKK 60cm:      1.00 u/prenda × $1,350/u  = $1,350
--   Botón Snap 15mm:      4.00 u/prenda × $180/u    =   $720
--   Hilo Industrial 40/2: 0.15 co/prenda × $3,500/co=   $525  (0.15 conos)
--   Cinta Reflectante:    0.50 m/prenda × $980/m    =   $490
--   ─────────────────────────────────────────────────────────────
--   Total materia prima por prenda: $11,725
INSERT INTO produccion_costeo_items (id_costeo_item, costeo_id, tipo_insumo, articulo_id, nombre_insumo, consumo, precio_unitario, costo_total) VALUES
    (1, 1, 'TELAS',      2, 'RIPSTOP IMPERMEABLE 150 GSM', 1.8000, 4800.00,  8640.00),
    (2, 1, 'ACCESORIOS', 4, 'CIERRE YKK 60CM METÁLICO',   1.0000, 1350.00,  1350.00),
    (3, 1, 'ACCESORIOS', 5, 'BOTÓN SNAP 15MM NÁCAR',       4.0000,  180.00,   720.00),
    (4, 1, 'ACCESORIOS', 6, 'HILO INDUSTRIAL 40/2',        0.1500, 3500.00,   525.00),
    (5, 1, 'ACCESORIOS', 7, 'CINTA REFLECTANTE 50MM',      0.5000,  980.00,   490.00)
ON DUPLICATE KEY UPDATE
    tipo_insumo     = VALUES(tipo_insumo),
    articulo_id     = VALUES(articulo_id),
    nombre_insumo   = VALUES(nombre_insumo),
    consumo         = VALUES(consumo),
    precio_unitario = VALUES(precio_unitario),
    costo_total     = VALUES(costo_total);

-- Versión 1 del costeo con totales consolidados (base: 50 prendas)
--   Materia prima total: $11,725 × 50               = $586,250
--   Mano de obra (corte + confección + acabado):     = $190,000  ($3,800/prenda)
--   Hilo (ya en items, acá MO hilo bordado logo):    =  $12,500  ($250/prenda)
--   Flete a bodega cliente:                          =  $25,000  ($500/prenda)
--   Embalaje (bolsa + caja individual):              =  $10,800  ($216/prenda)
--   Etiquetas (tejida + lavado + talla):             =   $7,500  ($150/prenda)
--   Costo fijo 10% sobre total directo:             ~  $83,205
--   ─────────────────────────────────────────────────
--   Costo total directo: $832,050
--   Con overhead 10%:    $915,255
--   Precio venta (margen 25%): $915,255 / 0.75 = $1,220,340  → ~$24,407/prenda
--   (NV usa $23,100 acordado en negociación comercial, 20% margen efectivo)
INSERT IGNORE INTO produccion_costeo_versiones (id_costeo_version, costeo_id, numero_version, fecha_creacion, usuario_creador,
    total_mano_obra, total_hilo, total_flete, total_embalaje, total_etiquetas,
    porcentaje_costo_fijo, costo_total_materia_prima, margen_bruto_sugerido, precio_venta_sugerido) VALUES
    (1, 1, 1, CURRENT_TIMESTAMP, 'SISTEMA',
    190000.00, 12500.00, 25000.00, 10800.00, 7500.00,
    10.00, 586250.00, 25.00, 1220340.00);

-- Items de la versión 1 del costeo (snapshot por versión)
INSERT INTO produccion_costeo_item_versiones (id_costeo_item_version, costeo_version_id, costeo_item_id, tipo_insumo, articulo_id, nombre_insumo, consumo, precio_unitario, costo_total, activo) VALUES
    (1, 1, 1, 'TELAS',      2, 'RIPSTOP IMPERMEABLE 150 GSM', 1.8000, 4800.00,  8640.00, true),
    (2, 1, 2, 'ACCESORIOS', 4, 'CIERRE YKK 60CM METÁLICO',   1.0000, 1350.00,  1350.00, true),
    (3, 1, 3, 'ACCESORIOS', 5, 'BOTÓN SNAP 15MM NÁCAR',       4.0000,  180.00,   720.00, true),
    (4, 1, 4, 'ACCESORIOS', 6, 'HILO INDUSTRIAL 40/2',        0.1500, 3500.00,   525.00, true),
    (5, 1, 5, 'ACCESORIOS', 7, 'CINTA REFLECTANTE 50MM',      0.5000,  980.00,   490.00, true)
ON DUPLICATE KEY UPDATE
    tipo_insumo     = VALUES(tipo_insumo),
    articulo_id     = VALUES(articulo_id),
    nombre_insumo   = VALUES(nombre_insumo),
    consumo         = VALUES(consumo),
    precio_unitario = VALUES(precio_unitario),
    costo_total     = VALUES(costo_total),
    activo          = VALUES(activo);

-- ============================================================
-- 7.7.1. COSTEO: POLERA PIQUÉ CORPORATIVA (100 unidades — HITES)
-- ============================================================
-- Costeo asociado a SCOS-000001 (Polera Piqué), estado APROBADO.
-- Toda SCOS creada vía la app recibe automáticamente un Costeo
-- (generatePreCosteo) — esta fila evita que SCOS-000001 quede sin el suyo.
INSERT INTO produccion_costeos (id_costeo, solicitud_costos_id, numero_costeo, estado, version,
    costo_hilos, costo_mano_obra, costo_etiquetas, costo_embalaje, costo_flete,
    porcentaje_costo_fijo, costo_total_materia_prima, margen_bruto_sugerido, precio_venta_sugerido) VALUES
    (2, 1, 'COST-0000002', 'APROBADO', 1,
    8000.00, 220000.00, 12000.00, 15000.00, 20000.00,
    10.00, 620000.00, 25.00, 1312667.00)
ON DUPLICATE KEY UPDATE
    estado                    = VALUES(estado),
    version                   = VALUES(version),
    costo_hilos               = VALUES(costo_hilos),
    costo_mano_obra           = VALUES(costo_mano_obra),
    costo_etiquetas           = VALUES(costo_etiquetas),
    costo_embalaje            = VALUES(costo_embalaje),
    costo_flete               = VALUES(costo_flete),
    porcentaje_costo_fijo     = VALUES(porcentaje_costo_fijo),
    costo_total_materia_prima = VALUES(costo_total_materia_prima),
    margen_bruto_sugerido     = VALUES(margen_bruto_sugerido),
    precio_venta_sugerido     = VALUES(precio_venta_sugerido);

INSERT INTO produccion_costeo_items (id_costeo_item, costeo_id, tipo_insumo, articulo_id, nombre_insumo, consumo, precio_unitario, costo_total) VALUES
    (6, 2, 'TELAS',      3, 'JERSEY PIQUÉ ALGODÓN 180 GSM', 1.2000, 4500.00, 540000.00),
    (7, 2, 'ACCESORIOS', 6, 'HILO INDUSTRIAL 40/2',         0.1000, 3500.00,  35000.00),
    (8, 2, 'ACCESORIOS', 7, 'CINTA REFLECTANTE 50MM',       0.4500,  980.00,  44100.00)
ON DUPLICATE KEY UPDATE
    tipo_insumo     = VALUES(tipo_insumo),
    articulo_id     = VALUES(articulo_id),
    nombre_insumo   = VALUES(nombre_insumo),
    consumo         = VALUES(consumo),
    precio_unitario = VALUES(precio_unitario),
    costo_total     = VALUES(costo_total);

-- Versión 1 del costeo de poleras — usada por OP-00002 (ver 8.4)
INSERT IGNORE INTO produccion_costeo_versiones (id_costeo_version, costeo_id, numero_version, fecha_creacion, usuario_creador,
    total_mano_obra, total_hilo, total_flete, total_embalaje, total_etiquetas,
    porcentaje_costo_fijo, costo_total_materia_prima, margen_bruto_sugerido, precio_venta_sugerido) VALUES
    (2, 2, 1, CURRENT_TIMESTAMP, 'SISTEMA',
    220000.00, 8000.00, 20000.00, 15000.00, 12000.00,
    10.00, 620000.00, 25.00, 1312667.00);

INSERT INTO produccion_costeo_item_versiones (id_costeo_item_version, costeo_version_id, costeo_item_id, tipo_insumo, articulo_id, nombre_insumo, consumo, precio_unitario, costo_total, activo) VALUES
    (6, 2, 6, 'TELAS',      3, 'JERSEY PIQUÉ ALGODÓN 180 GSM', 1.2000, 4500.00, 540000.00, true),
    (7, 2, 7, 'ACCESORIOS', 6, 'HILO INDUSTRIAL 40/2',         0.1000, 3500.00,  35000.00, true),
    (8, 2, 8, 'ACCESORIOS', 7, 'CINTA REFLECTANTE 50MM',       0.4500,  980.00,  44100.00, true)
ON DUPLICATE KEY UPDATE
    tipo_insumo     = VALUES(tipo_insumo),
    articulo_id     = VALUES(articulo_id),
    nombre_insumo   = VALUES(nombre_insumo),
    consumo         = VALUES(consumo),
    precio_unitario = VALUES(precio_unitario),
    costo_total     = VALUES(costo_total),
    activo          = VALUES(activo);

-- ============================================================
-- 7.8. ORDEN DE PRODUCCIÓN (OP)
-- ============================================================
-- OP-00001: Pantalón Cargo MEDCELL → referencia NV-0000001 (cliente MEDCELL, EN_PRODUCCION)
INSERT IGNORE INTO orden_produccion (id_op, costeo_version_id, numero_op, nota_venta_id, estado, fecha_inicio, fecha_entrega_programada, observaciones, created_at, updated_at) VALUES
    (1, 1, 'OP-00001', 1, 'EN_PROCESO', CURRENT_DATE, DATE_ADD(CURRENT_DATE, INTERVAL 30 DAY),
     'PANTALÓN CARGO OPERARIO — LABORATORIO MEDCELL — 50 UNIDADES TALLAS M/L', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- Items de la OP (artículos a producir, desglosados por talla)
INSERT IGNORE INTO produccion_orden_items (id_op_item, orden_produccion_id, articulo_id, nro_item, modelo, tela, color, talla, genero, codigo, lleva_logo, cantidad) VALUES
    (1, 1, 2, 1, 'CARGO 6 BOLSILLOS', 'RIPSTOP IMPERMEABLE 150G', 'VERDE OLIVA', 'M', 'MASCULINO', 'PAN-CARGO-M', 'NO', 25),
    (2, 1, 2, 2, 'CARGO 6 BOLSILLOS', 'RIPSTOP IMPERMEABLE 150G', 'VERDE OLIVA', 'L', 'MASCULINO', 'PAN-CARGO-L', 'NO', 25);

-- ============================================================
-- 7.9. HOJA DE COMPRA (HC)
-- ============================================================
-- HC-000001 generada desde OP-00001 con costeo versión 1
-- Cantidades = consumo_unitario × 50 prendas (con 5% holgura redondeado)
INSERT IGNORE INTO produccion_hojas_compra (id_hc, numero_hc, op_id, costeo_version_id, estado, fecha_generacion, observaciones) VALUES
    (1, 'HC-000001', 1, 1, 'APROBADA', CURRENT_DATE, 'HC GENERADA AUTOMÁTICAMENTE DESDE COSTEO VERSIÓN 1 — OP-00001');

INSERT IGNORE INTO produccion_hoja_compra_items (id_hc_item, hc_id, tipo_insumo, articulo_id, proveedor_id, nombre_insumo, consumo_unitario, cantidad_op, cantidad_requerida, precio_unitario_ref) VALUES
    (1, 1, 'TELA',      2, 1, 'RIPSTOP IMPERMEABLE 150 GSM', 1.8000, 50,  90.0000,  4800.00),
    (2, 1, 'ACCESORIO', 4, 2, 'CIERRE YKK 60CM METÁLICO',   1.0000, 50,  50.0000,  1350.00),
    (3, 1, 'ACCESORIO', 5, 2, 'BOTÓN SNAP 15MM NÁCAR',       4.0000, 50, 200.0000,   180.00),
    (4, 1, 'ACCESORIO', 6, 4, 'HILO INDUSTRIAL 40/2',        0.1500, 50,   8.0000,  3500.00),
    (5, 1, 'ACCESORIO', 7, 4, 'CINTA REFLECTANTE 50MM',      0.5000, 50,  26.0000,   980.00);

-- ============================================================
-- 7.10. CONTADORES DE DOCUMENTOS (document_counter)
-- ============================================================
INSERT IGNORE INTO document_counter (tipo, ultimo_numero) VALUES
    ('NV',   2),
    ('EVN',  2),
    ('SCOS', 2),
    ('SCOT', 0),
    ('COST', 4),
    ('OP',   1),
    ('HC',   1);

-- ============================================================
-- 8. DATOS DE PRUEBA DASHBOARD — FLUJO END-TO-END
--    Objetivo: poblar KPIs y gráficos del Welcome con valores
--    representativos que reflejen el flujo real del ERP.
-- ============================================================

-- ── 8.1. SCOS ADICIONALES EN ESTADO PENDIENTE ──────────────
-- Agrega 2 SCOS para que el KPI "SCOS Pendientes" muestre 3
INSERT IGNORE INTO solicitudes_costos
    (id_scos, numero, estado, tipo, cliente_id, vendedor_id,
     articulo_descripcion, nombre_prenda, genero, tallaje,
     es_muestra, has_logo, cantidad, fecha, costo_total)
VALUES
    (3, 'SCOS-000003', 'PENDIENTE', 'SCOS', 3, 1,
     'POLERON', 'POLERÓN CORPORATIVO CON CAPUCHA', 'UNISEX', 'ESTANDAR',
     false, true, 200, CURRENT_DATE, 0.00),
    (4, 'SCOS-000004', 'PENDIENTE', 'SCOS', 1, 2,
     'CHALECO', 'CHALECO CORPORATIVO ACOLCHADO', 'UNISEX', 'TALLA UNICA',
     true, false, 50, CURRENT_DATE, 0.00)
ON DUPLICATE KEY UPDATE estado = VALUES(estado);

-- Costeos en blanco (BORRADOR) para SCOS-000003 y SCOS-000004 — toda SCOS
-- creada vía la app recibe automáticamente un Costeo vacío (generatePreCosteo),
-- aunque siga PENDIENTE de costear.
INSERT IGNORE INTO produccion_costeos (id_costeo, solicitud_costos_id, numero_costeo, estado, version) VALUES
    (3, 3, 'COST-0000003', 'BORRADOR', 1),
    (4, 4, 'COST-0000004', 'BORRADOR', 1);

-- ── 8.2. EVN EN BORRADOR / EVALUACIÓN ──────────────────────
-- Agrega 2 EVN activas para que el KPI "EVN en Evaluación" muestre 2
-- (el código filtra estado IN ['BORRADOR', 'EVALUACION'])
INSERT IGNORE INTO evaluaciones_negocio
    (id_evn, numero, referencia, cliente_nombre, cliente_id, vendedor_id,
     estado, fecha_evaluacion, porcentaje_comision, created_at, updated_at)
VALUES
    (3, 'EVN-000003',
     'COTIZACIÓN POLERÓN CORPORATIVO CON CAPUCHA — GEODIS WILSON',
     'GEODIS WILSON', 3, 1,
     'BORRADOR', CURRENT_DATE, 5.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (4, 'EVN-000004',
     'PROPUESTA CHALECO ACOLCHADO CORPORATIVO — HITES S.A.',
     'HITES S.A.', 1, 2,
     'EVALUACION', CURRENT_DATE, 4.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON DUPLICATE KEY UPDATE estado = VALUES(estado);

-- Items básicos EVN-000003 (Polerón para GEODIS)
INSERT IGNORE INTO evaluacion_negocio_items
    (idevni, evaluacion_negocio_id, proveedor_id, articulo_id, nro_item,
     descripcion, modelo, tela, genero,
     cantidad, precio_unitario, moneda_precio_unitario,
     costo_unitario, moneda_costo_unitario,
     costo_producto, costo_logo, tipo_item)
VALUES
    (3, 3, 1, 8, 1,
     'POLERÓN CORPORATIVO CAPUCHA BORDADO', 'HOODIE REGULAR FIT', 'POLAR FLEECE 280G', 'UNISEX',
     200, 18500.00, 'CLP', 13800.00, 'CLP', 11200.00, 2600.00, 'OP');

-- Items básicos EVN-000004 (Chaleco para HITES)
INSERT IGNORE INTO evaluacion_negocio_items
    (idevni, evaluacion_negocio_id, proveedor_id, articulo_id, nro_item,
     descripcion, modelo, tela, genero,
     cantidad, precio_unitario, moneda_precio_unitario,
     costo_unitario, moneda_costo_unitario,
     costo_producto, tipo_item)
VALUES
    (4, 4, 3, 10, 1,
     'CHALECO CORPORATIVO ACOLCHADO IMPERMEABLE', 'VEST SOFTSHELL', 'RIPSTOP IMPERMEABLE 150G', 'UNISEX',
     50, 32000.00, 'CLP', 23500.00, 'CLP', 23500.00, 'OP');

-- ── 8.3. NOTAS DE VENTA HISTÓRICAS (Ene–May 2026) ──────────
-- Pobla el gráfico "Ventas Mensuales" con datos de los meses anteriores.
-- Estas NV representan negocios ya cerrados (estado ENTREGADA).
-- Se usan los EVN existentes (1 y 2) como referencia comercial.
INSERT IGNORE INTO notas_venta
    (id_nv, numeronv, evaluacion_negocio_id, cliente_id, vendedor_id,
     estado, es_kit,
     fecha_emision, fecha_entrega_estimada,
     monto_subtotal, moneda_subtotal,
     monto_iva,     moneda_iva,
     monto_total,   moneda_total,
     created_at, updated_at)
VALUES
    -- Enero 2026 — GEODIS, 80 poleras basic
    (3, 'NV-0000003', 3, 3, 1,
     'ENTREGADA', false,
     '2026-01-20', '2026-02-10',
     730000.00, 'CLP', 138700.00, 'CLP', 868700.00, 'CLP',
     '2026-01-20 09:00:00', '2026-01-20 09:00:00'),
    -- Febrero 2026 — HITES, 120 poleras
    (4, 'NV-0000004', 1, 1, 1,
     'ENTREGADA', false,
     '2026-02-14', '2026-03-05',
     1272000.00, 'CLP', 241680.00, 'CLP', 1513680.00, 'CLP',
     '2026-02-14 10:00:00', '2026-02-14 10:00:00'),
    -- Marzo 2026 — MEDCELL, 30 pantalones cargo
    (5, 'NV-0000005', 2, 2, 2,
     'ENTREGADA', false,
     '2026-03-08', '2026-04-01',
     515130.00, 'CLP', 97874.70, 'CLP', 613004.70, 'CLP',
     '2026-03-08 11:00:00', '2026-03-08 11:00:00'),
    -- Abril 2026 — GEODIS, 150 poleras corporativas
    (6, 'NV-0000006', 3, 3, 2,
     'ENTREGADA', false,
     '2026-04-22', '2026-05-15',
     1590000.00, 'CLP', 302100.00, 'CLP', 1892100.00, 'CLP',
     '2026-04-22 08:30:00', '2026-04-22 08:30:00'),
    -- Mayo 2026 — HITES, 60 chalecos acolchados
    (7, 'NV-0000007', 4, 1, 1,
     'ENTREGADA', false,
     '2026-05-10', '2026-06-01',
     1920000.00, 'CLP', 364800.00, 'CLP', 2284800.00, 'CLP',
     '2026-05-10 14:00:00', '2026-05-10 14:00:00')
ON DUPLICATE KEY UPDATE estado = VALUES(estado);

-- Items simplificados de las NV históricas (un ítem por NV)
INSERT IGNORE INTO notas_venta_items
    (id_item_nv, nota_venta_id, nro_item, modelo, tela, composicion, color,
     talla, genero, codigo, proveedor_id, lleva_logo, tipo_item,
     requiere_ot, cantidad, precio_unitario, moneda_precio_unitario,
     total, moneda_total, articulo_id)
VALUES
    (7, 3, 1, 'BÁSICA MANGA CORTA',    'JERSEY PIQUÉ 180G', '100% ALGODÓN', 'BLANCO',     'M', 'UNISEX',    'POL-BAS-M',   1, 'NO', 'OP', false,  80, 9125.00, 'CLP',  730000.00, 'CLP', 3),
    (8, 4, 1, 'SLIM FIT MANGA CORTA',  'JERSEY PIQUÉ 180G', '100% ALGODÓN', 'AZUL NAVY',  'M', 'UNISEX',    'POL-PIQUE-M', 1, 'SI', 'OP', false, 120, 10600.00, 'CLP', 1272000.00, 'CLP', 3),
    (9, 5, 1, 'CARGO 6 BOLSILLOS',     'RIPSTOP 150G',      '100% POLIÉSTER','VERDE OLIVA','M', 'MASCULINO', 'PAN-CARGO-M', 1, 'NO', 'OP', false,  30, 17171.00, 'CLP',  515130.00, 'CLP', 2),
    (10, 6, 1,'SLIM FIT MANGA CORTA',  'JERSEY PIQUÉ 180G', '100% ALGODÓN', 'NEGRO',      'L', 'UNISEX',    'POL-PIQUE-L', 1, 'SI', 'OP', false, 150, 10600.00, 'CLP', 1590000.00, 'CLP', 3),
    (11, 7, 1,'VEST SOFTSHELL',        'RIPSTOP 150G',      '100% POLIÉSTER','NEGRO',      'M', 'UNISEX',    'CHL-SOFT-M',  3, 'NO', 'OP', false,  60, 32000.00, 'CLP', 1920000.00, 'CLP', 10)
ON DUPLICATE KEY UPDATE cantidad = VALUES(cantidad);

-- ── 8.4. OP ADICIONAL EN ESTADO PENDIENTE ──────────────────
-- Agrega una segunda OP (poleras HITES) para que el KPI "OPs en Planta" muestre 2.
-- Usa costeo_version_id=2 (costeo de poleras, ver 7.7.1) — antes reutilizaba
-- por error el costeo_version_id=1 del pantalón cargo de otra OP/cliente.
INSERT IGNORE INTO orden_produccion
    (id_op, costeo_version_id, numero_op, nota_venta_id, estado,
     fecha_inicio, fecha_entrega_programada, observaciones,
     created_at, updated_at)
VALUES
    (2, 2, 'OP-00002', 2, 'PENDIENTE',
     DATE_ADD(CURRENT_DATE, INTERVAL 3 DAY),
     DATE_ADD(CURRENT_DATE, INTERVAL 12 DAY),
     'POLERA PIQUÉ CORPORATIVA — HITES S.A. — 100 UNIDADES — EN ESPERA DE INSUMOS',
     CURRENT_TIMESTAMP, CURRENT_TIMESTAMP)
ON DUPLICATE KEY UPDATE estado = VALUES(estado), fecha_entrega_programada = VALUES(fecha_entrega_programada);

-- ── 8.5. SEGUIMIENTO DE OPs (produccion_seguimiento_op) ────
-- Estas fechas generan valores reales en el dashboard operacional:
--   OP-00001 (EN_PROCESO): recibida hace 5d sin OC confirmada → opAtrasada
--                           logo enviado hace 4d sin retorno  → recepcionLogoAtrasado
--   OP-00002 (PENDIENTE):  recibida hace 4d sin OC confirmada → opAtrasada
--   entregas7d: OP-00002 vence en 12d (fuera de ventana 7d por diseño)
--   Resultado esperado: opAtrasada=2, recepcionLogoAtrasado=1, resto=0, entregas7d=1
INSERT IGNORE INTO produccion_seguimiento_op
    (id_seguimiento, orden_produccion_id,
     fecha_recepcion_op, fin_tizado, estado_oc_mp, recepcion_compras,
     inicio_corte, fin_corte,
     inicio_logo, estado_ida_logo, regreso_logo, estado_rec_logo,
     inicio_taller_externo, fin_taller_externo, calidad_taller, obs_taller,
     fin_terminacion, fin_personalizado)
VALUES
    -- Seguimiento OP-00001 (Pantalón Cargo MEDCELL)
    (1, 1,
     DATE_SUB(CURRENT_DATE, INTERVAL 5 DAY),   -- fecha_recepcion_op: 5 días atrás
     DATE_SUB(CURRENT_DATE, INTERVAL 4 DAY),   -- fin_tizado
     NULL,                                      -- sin OC confirmada → ALERTA opAtrasada
     NULL,
     DATE_SUB(CURRENT_DATE, INTERVAL 3 DAY),   -- inicio_corte
     DATE_SUB(CURRENT_DATE, INTERVAL 1 DAY),   -- fin_corte (2 días — dentro del límite 10d)
     DATE_SUB(CURRENT_DATE, INTERVAL 4 DAY),   -- inicio_logo: hace 4d > límite 3d → ALERTA
     'IDA_COMPLETA',
     NULL,                                      -- regreso_logo: aún no retorna
     NULL,
     NULL, NULL, NULL, NULL,
     NULL, NULL),
    -- Seguimiento OP-00002 (Polera Piqué HITES — recién recibida)
    (2, 2,
     DATE_SUB(CURRENT_DATE, INTERVAL 4 DAY),   -- fecha_recepcion_op: 4 días atrás
     NULL,                                      -- sin tizado aún
     NULL,                                      -- sin OC confirmada → ALERTA opAtrasada
     NULL,
     NULL, NULL,
     NULL, NULL, NULL, NULL,
     NULL, NULL, NULL, NULL,
     NULL, NULL)
ON DUPLICATE KEY UPDATE
    fecha_recepcion_op   = VALUES(fecha_recepcion_op),
    estado_oc_mp   = VALUES(estado_oc_mp),
    inicio_logo          = VALUES(inicio_logo),
    estado_ida_logo      = VALUES(estado_ida_logo),
    regreso_logo         = VALUES(regreso_logo);

-- ── 8.5. ACTUALIZAR CONTADORES ─────────────────────────────
INSERT INTO document_counter (tipo, ultimo_numero) VALUES
    ('NV',   7),
    ('EVN',  10),
    ('SCOS', 10),
    ('OP',   10)
ON DUPLICATE KEY UPDATE ultimo_numero = GREATEST(ultimo_numero, VALUES(ultimo_numero));

-- ============================================================
-- 7.11. MAESTROS GLOBALES (Moneda, Unidad de Medida)
-- ============================================================
INSERT IGNORE INTO moneda (id_moneda, codigo_moneda, nombre_moneda, simbolo) VALUES
    (1, 'CLP', 'PESO CHILENO',          '$'),
    (2, 'USD', 'DÓLAR ESTADOUNIDENSE',  'US$'),
    (3, 'EUR', 'EURO',                  '€');

INSERT IGNORE INTO unidad_medida (id_unidad_medida, nombre_unidad, abreviatura) VALUES
    (1, 'METRO',          'M'),
    (2, 'METRO CUADRADO', 'M2'),
    (3, 'KILOGRAMO',      'KG'),
    (4, 'UNIDAD',         'UN'),
    (5, 'PAR',            'PAR'),
    (6, 'CAJA',           'CJA'),
    (7, 'ROLLO',          'ROL');

-- ============================================================
-- 7.12. MAESTROS DE TELA
-- ============================================================
INSERT IGNORE INTO familia_tela (id_familia_tela, codigo_familia, nombre_familia) VALUES
    (1,  'FT-01', 'JERSEY'),
    (2,  'FT-02', 'RIB'),
    (3,  'FT-03', 'INTERLOCK'),
    (4,  'FT-04', 'FLEECE / FRENCH TERRY'),
    (5,  'FT-05', 'POLAR / SHERPA'),
    (6,  'FT-06', 'SCUBA / NEOPRENE'),
    (7,  'FT-07', 'PIQUÉ / LACOSTE'),
    (8,  'FT-08', 'TAFETÁN / POPELÍN'),
    (9,  'FT-09', 'SARGA / DRILL'),
    (10, 'FT-10', 'DENIM'),
    (11, 'FT-11', 'GABARDINA'),
    (12, 'FT-12', 'TERCIOPELO / VELVET'),
    (13, 'FT-13', 'PUNTO ROMA / BENGALINA'),
    (14, 'FT-14', 'LICRA / SPANDEX PLANO'),
    (15, 'FT-15', 'MALLA DEPORTIVA / MESH'),
    (16, 'FT-16', 'OXFORD / RIPSTOP'),
    (17, 'FT-17', 'LANA / PAÑO'),
    (18, 'FT-18', 'LINO / RAMIO'),
    (19, 'FT-19', 'SEDA / SATÉN'),
    (20, 'FT-20', 'MICROFIBRA / SOFTSHELL');

INSERT IGNORE INTO clasificacion_tecnica (id_clasificacion_tecnica, nombre_clasificacion) VALUES
    (1, 'TEJIDO DE PUNTO'),
    (2, 'TEJIDO PLANO'),
    (3, 'NO TEJIDO / TNT'),
    (4, 'TÉCNICO / FUNCIONAL'),
    (5, 'ALTA PRESTACIÓN');

INSERT IGNORE INTO composicion (id_composicion, codigo_composicion, descripcion_composicion, clasificacion, uso_tipico) VALUES
    (1,  'CO-01',  '100% ALGODÓN',                       'NATURAL',       'POLERAS, BÁSICOS, ROPA INTERIOR'),
    (2,  'CO-02',  '100% POLIÉSTER',                     'SINTÉTICO',     'DEPORTIVO, SUBLIMACIÓN, FORRO'),
    (3,  'CO-03',  '50% ALGODÓN / 50% POLIÉSTER',        'MIXTO',         'POLERAS MIX, USO GENERAL'),
    (4,  'CO-04',  '65% POLIÉSTER / 35% ALGODÓN',        'MIXTO',         'UNIFORMES CORPORATIVOS'),
    (5,  'CO-05',  '95% ALGODÓN / 5% ELASTANO',          'NATURAL-ELÁST', 'POLERAS AJUSTADAS, CASUALWEAR'),
    (6,  'CO-06',  '95% POLIÉSTER / 5% ELASTANO',        'SINT-ELÁST',    'DEPORTIVO TÉCNICO, LEGGINGS'),
    (7,  'CO-07',  '60% ALGODÓN / 40% POLIÉSTER',        'MIXTO',         'PRENDAS ESCOLARES, UNIFORMES'),
    (8,  'CO-08',  '80% ALGODÓN / 20% POLIÉSTER',        'MIXTO',         'CAMISAS, POLERAS PREMIUM MIX'),
    (9,  'CO-09',  '100% VISCOSA / RAYÓN',                'CELULÓSICO',    'BLUSAS, VESTIDOS, FORRO LIVIANO'),
    (10, 'CO-10',  '100% NYLON / POLIAMIDA',              'SINTÉTICO',     'CHAQUETAS, CORTAVIENTOS'),
    (11, 'CO-11',  '88% POLIÉSTER / 12% ELASTANO',        'SINT-ELÁST',    'ROPA DEPORTIVA COMPRESIÓN'),
    (12, 'CO-12',  '70% ALGODÓN / 30% POLIÉSTER',         'MIXTO',         'POLERAS ESCOLARES'),
    (13, 'CO-13',  '100% LANA MERINO',                    'NATURAL',       'PAÑOS, ABRIGOS PREMIUM'),
    (14, 'CO-14',  '100% LINO',                           'NATURAL',       'VERANO, GUAYABERAS'),
    (15, 'CO-15',  '55% LINO / 45% ALGODÓN',              'NATURAL',       'ROPA VERANO PREMIUM'),
    (16, 'CO-16',  '50% VISCOSA / 50% POLIÉSTER',         'MIXTO',         'VESTIDOS, BLUSAS'),
    (17, 'CO-17',  '90% POLIÉSTER / 10% ELASTANO',        'SINT-ELÁST',    'MALLAS, CYCLING'),
    (18, 'CO-18',  '100% ALGODÓN ORGÁNICO',               'NATURAL',       'LÍNEA ECO, BEBÉ');

INSERT IGNORE INTO gramaje_tela (id_gramaje, codigo_gramaje, valor_gramos_m2, categoria_vestuario) VALUES
    (1,  'GR-01',  120.00, 'VERANO / ROPA INTERIOR LIVIANA'),
    (2,  'GR-02',  140.00, 'CAMISETAS BÁSICAS VERANO'),
    (3,  'GR-03',  160.00, 'POLERAS ESTÁNDAR'),
    (4,  'GR-04',  180.00, 'POLERAS PREMIUM / ESCOLARES'),
    (5,  'GR-05',  200.00, 'POLERAS GRUESAS / LICRA DEPORTIVA'),
    (6,  'GR-06',  220.00, 'RIB / UNIFORMES CORPORATIVOS'),
    (7,  'GR-07',  240.00, 'INTERLOCK / PRENDAS DOBLE CARA'),
    (8,  'GR-08',  260.00, 'FLEECE LIVIANO / JOGGER'),
    (9,  'GR-09',  280.00, 'BUZO / FRENCH TERRY'),
    (10, 'GR-10',  300.00, 'FLEECE GRUESO / PARKA INTERIOR'),
    (11, 'GR-11',  320.00, 'POLAR LIGERO'),
    (12, 'GR-12',  350.00, 'POLAR MEDIO / SHERPA'),
    (13, 'GR-13',  380.00, 'POLAR GRUESO / ABRIGO'),
    (14, 'GR-14',  400.00, 'PAÑO / GABARDINA GRUESA'),
    (15, 'GR-15',  450.00, 'LANA / ABRIGO INVERNAL'),
    (16, 'GR-16',  500.00, 'TAPICERÍA / LONA TÉCNICA');

INSERT IGNORE INTO color_tela (id_color, codigo_color, descripcion_color, es_pantone) VALUES
    (1,  'COL-01',  'BLANCO',         FALSE),
    (2,  'COL-02',  'NEGRO',          FALSE),
    (3,  'COL-03',  'GRIS CLARO',     FALSE),
    (4,  'COL-04',  'GRIS OSCURO',    FALSE),
    (5,  'COL-05',  'AZUL NAVY',      FALSE),
    (6,  'COL-06',  'AZUL ROYAL',     FALSE),
    (7,  'COL-07',  'AZUL PETRÓLEO',  FALSE),
    (8,  'COL-08',  'ROJO',           FALSE),
    (9,  'COL-09',  'BURDEO',         FALSE),
    (10, 'COL-10',  'VERDE BOTELLA',  FALSE),
    (11, 'COL-11',  'VERDE OLIVA',    FALSE),
    (12, 'COL-12',  'AMARILLO',       FALSE),
    (13, 'COL-13',  'NARANJO',        FALSE),
    (14, 'COL-14',  'CAFÉ',           FALSE),
    (15, 'COL-15',  'BEIGE / ARENA',  FALSE),
    (16, 'COL-16',  'CELESTE',        FALSE),
    (17, 'COL-17',  'LILA / MALVA',   FALSE),
    (18, 'COL-18',  'ROSADO',         FALSE),
    (19, 'COL-19',  'FUCSIA',         FALSE),
    (20, 'COL-20',  'CAQUI / OLIVE',  FALSE),
    (21, 'COL-21',  'MELANGE GRIS',   FALSE),
    (22, 'COL-22',  'MELANGE AZUL',   FALSE),
    (23, 'COL-23',  'MARINO MELANGE', FALSE);

INSERT IGNORE INTO atributo_tecnico (id_atributo, codigo_atributo, clasificacion, descripcion_tecnica, impacto_erp) VALUES
    (1,  'AT-01', 'FUNCIONAL',   'ANTIMICROBIANO',                      'CERTIFICADO REQUERIDO PARA EXPORTACIÓN'),
    (2,  'AT-02', 'FUNCIONAL',   'TRANSPIRABLE / MOISTURE WICKING',     'INDICAR EN FICHA TÉCNICA DEPORTIVA'),
    (3,  'AT-03', 'FUNCIONAL',   'UPF 50+ PROTECCIÓN SOLAR',            'APLICA A PRENDAS OUTDOOR/VERANO'),
    (4,  'AT-04', 'FUNCIONAL',   'IGNÍFUGO / RETARDANTE DE LLAMA',      'HOMOLOGACIÓN OBLIGATORIA PARA EPP'),
    (5,  'AT-05', 'FUNCIONAL',   'ANTIESTÁTICO',                        'REQUERIDO EN AMBIENTES INDUSTRIALES'),
    (6,  'AT-06', 'FUNCIONAL',   'IMPERMEABLE / DWR',                   'APLICA A SOFTSHELL Y CORTAVIENTOS'),
    (7,  'AT-07', 'FUNCIONAL',   'TERMORREGULADOR / PCM',               'INDICAR RANGO TEMPERATURA EN FICHA'),
    (8,  'AT-08', 'ACABADO',     'SUAVIZADO ENZIMÁTICO',                'PROCESO POST-CONFECCIÓN, AFECTA GRAMAJE FINAL'),
    (9,  'AT-09', 'ACABADO',     'SANFORIZADO / PRE-LAVADO',            'CONTROL DE ENCOGIMIENTO EN ORDEN DE COMPRA'),
    (10, 'AT-10', 'ACABADO',     'PILLING REDUCIDO',                    'NORMA MARTINDALE MÍNIMA 5000 CICLOS'),
    (11, 'AT-11', 'ACABADO',     'EASY CARE / ANTI-ARRUGAS',            'INDICAR INSTRUCCIÓN LAVADO EN ETIQUETA'),
    (12, 'AT-12', 'SUSTENTABLE', 'GOTS CERTIFIED (ORGÁNICO)',           'CÓDIGO CERTIFICACIÓN EN PO DE COMPRA'),
    (13, 'AT-13', 'SUSTENTABLE', 'RECICLADO (GRS CERTIFIED)',           'TRAZABILIDAD REQUERIDA DESDE PROVEEDOR');

