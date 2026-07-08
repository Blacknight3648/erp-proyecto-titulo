-- ============================================================
-- 0. TIPO ARTÍCULO
-- ============================================================
MERGE INTO tipo_articulo (id_tipo_articulo, codigo, nombre)
    KEY (id_tipo_articulo)
    VALUES
    (1, 'TELA',                'Tela'),
    (2, 'PRENDA_LISTA',        'Prenda Lista'),
    (3, 'ACCESORIO',           'Accesorio'),
    (4, 'PRENDA_CONFECCIONAR', 'Prenda a Confeccionar');

-- ============================================================
-- 1. ÁREAS
-- ============================================================
MERGE INTO areas (id_area, nombre_area, descripcion)
    KEY (id_area)
    VALUES
    (1, 'GERENCIA COMERCIAL', 'Planificación estratégica de ventas y marketing'),
    (2, 'ADMINISTRACIÓN', 'Gestión de recursos y procesos internos'),
    (3, 'COMERCIAL', 'Ejecución de fuerza de venta y captación'),
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
MERGE INTO rubros (rubro_id, nombre_rubro, descripcion_rubro, sigla_rubro)
    KEY (rubro_id)
    VALUES
    (1, 'COMERCIO', 'Empresas dedicadas a la compra y venta de bienes y servicios', 'COM'),
    (2, 'SALUD', 'Empresas del sector salud, farmacéutico y laboratorio', 'SAL'),
    (3, 'LOGÍSTICA', 'Empresas de transporte, almacenamiento y distribución', 'LOG'),
    (4, 'CONSTRUCCIÓN', 'Empresas del rubro inmobiliario, infraestructura y ferretería', 'CONS'),
    (5, 'MINERÍA', 'Extracción y procesamiento de cobre, litio y otros minerales y servicios afines', 'MIN'),
    (6, 'AGRICULTURA Y FRUTICULTURA', 'Cultivo, producción y exportación de frutas, hortalizas y productos agrícolas', 'AGRI'),
    (7, 'ACUICULTURA Y PESCA', 'Crianza y captura de recursos del mar, producción de salmón y procesamiento de mariscos', 'PESCA'),
    (8, 'FORESTAL', 'Silvicultura, explotación de maderas, producción de celulosa y derivados', 'FOR'),
    (9, 'VITIVINÍCOLA', 'Cultivo de la vid, producción, embotellado y exportación de vinos', 'VITI'),
    (10, 'TECNOLOGÍA Y SOFTWARE', 'Desarrollo de software, consultoría TI, servicios en la nube y ciberseguridad', 'TECH'),
    (11, 'EDUCACIÓN', 'Instituciones de educación básica, media, técnica, universitaria y capacitación', 'EDUC'),
    (12, 'TURISMO Y HOTELERÍA', 'Servicios de alojamiento, agencias de viaje, guías turísticos y gastronomía', 'TUR'),
    (13, 'ENERGÍA Y SERVICIOS BÁSICOS', 'Generación, transmisión y distribución de energía eléctrica, gas, agua y energías renovables', 'ENER'),
    (14, 'FINANCIERO Y SEGUROS', 'Bancos, instituciones financieras, corredoras de seguros y administradoras de fondos', 'FIN'),
    (15, 'ALIMENTOS Y BEBIDAS', 'Fabricación, procesamiento y envasado de productos alimenticios y bebidas no alcohólicas', 'ALIM'),
    (16, 'MANUFACTURA E INDUSTRIA', 'Fabricación de productos metálicos, plásticos, químicos y bienes de consumo industrial', 'MANU'),
    (17, 'TELECOMUNICACIONES', 'Proveedores de telefonía e internet, infraestructura de redes y servicios de conectividad', 'TELE'),
    (18, 'SERVICIOS PROFESIONALES', 'Consultorías legales, contables, auditorías, arquitectura, ingeniería y recursos humanos', 'SERV'),
    (19, 'GASTRONOMÍA Y RESTAURANTES', 'Establecimientos de preparación y venta de alimentos y bebidas para consumo inmediato', 'GASTR'),
    (20, 'ENTRETENIMIENTO Y CULTURA', 'Cines, teatros, productoras de eventos, museos, gimnasios y centros recreativos', 'ENTR'),
    (21, 'MEDIOS DE COMUNICACIÓN Y PUBLICIDAD', 'Prensa escrita, televisión, radio, agencias de marketing digital y publicidad', 'MCOM'),
    (22, 'SEGURIDAD', 'Servicios de vigilancia privada, transporte de valores y sistemas de seguridad electrónica', 'SEG'),
    (23, 'AUTOMOTRIZ', 'Venta de vehículos, repuestos, talleres mecánicos y servicios de mantención automotriz', 'AUTO'),
    (24, 'GANADERÍA', 'Crianza y producción de ganado bovino, porcino, ovino, avícola y derivados lácteos', 'GAN'),
    (25, 'MEDIO AMBIENTE Y RECICLAJE', 'Gestión de residuos, reciclaje industrial, plantas de tratamiento y consultoría ambiental', 'MEDA');

-- ============================================================
-- 4.2. GIROS (referenciando el rubro correspondiente)
-- ============================================================
MERGE INTO giros (giro_id, codigo_sii, nombre_giro, descripcion_giro, rubro_id)
    KEY (giro_id)
    VALUES
    (1, '521000', 'RETAIL', 'RETAIL Y VENTAS POR MENOR', 1),
    (2, '861000', 'SALUD', 'SERVICIOS MÉDICOS Y LABORATORIO', 2),
    (3, '492200', 'LOGÍSTICA', 'LOGÍSTICA Y TRANSPORTE', 3),
    (4, '410000', 'CONSTRUCCIÓN', 'CONSTRUCCIÓN Y FERRETERÍA', 4),
    -- MINERÍA (Rubro 5)
    (5, '071000', 'EXTRACCIÓN DE MINERÁLES DE HIERRO', 'Extracción de minerales de hierro y concentrados', 5),
    (6, '040000', 'EXTRACCIÓN DE COBRE', 'Extracción de minerales de cobre y sus concentrados', 5),
    (7, '099002', 'SERVICIOS DE MINERÍA', 'Actividades de apoyo para la explotación de otras minas y canteras', 5),
    -- AGRICULTURA Y FRUTICULTURA (Rubro 6)
    (8, '011301', 'CULTIVO DE FRUTALES', 'Cultivo de frutas pomáceas y de carozo (manzanas, uvas, etc.)', 6),
    (9, '016100', 'SERVICIOS AGRÍCOLAS', 'Actividades de apoyo a la agricultura y postcosecha', 6),
    -- ACUICULTURA Y PESCA (Rubro 7)
    (10, '032101', 'ACUICULTURA DE SALMÓNIDOS', 'Cultivo y crianza de salmones y truchas', 7),
    (11, '031110', 'PESCA INDUSTRIAL', 'Pesca marítima de altura y costera a gran escala', 7),
    -- FORESTAL (Rubro 8)
    (12, '021001', 'SILVICULTURA Y EXPLOTACIÓN', 'Plantación, manejo de bosques y explotación de madera', 8),
    -- VITIVINÍCOLA (Rubro 9)
    (13, '110200', 'ELABORACIÓN DE VINOS', 'Producción de mostos, vinos y chicha a partir de uvas', 9),
    -- TECNOLOGÍA Y SOFTWARE (Rubro 10)
    (14, '620100', 'DESARROLLO DE SOFTWARE', 'Actividades de programación informática y desarrollo de apps', 10),
    (15, '620200', 'CONSULTORÍA TI', 'Consultoría en informática y gestión de instalaciones informáticas', 10),
    -- EDUCACIÓN (Rubro 11)
    (16, '854200', 'EDUCACIÓN UNIVERSITARIA', 'Enseñanza superior universitaria y técnica de pre y postgrado', 11),
    -- TURISMO Y HOTELERÍA (Rubro 12)
    (17, '551001', 'HOTELES Y ALOJAMIENTO', 'Servicios de hospedaje en hoteles, moteles y cabañas', 12),
    -- ENERGÍA (Rubro 13)
    (18, '351011', 'GENERACIÓN ELÉCTRICA', 'Generación de energía eléctrica en centrales hidráulicas, solares o eólicas', 13),
    -- FINANCIERO (Rubro 14)
    (19, '641900', 'BANCA E INTERMEDIACIÓN', 'Otros tipos de intermediación monetaria y actividades bancarias', 14),
    -- ALIMENTOS Y BEBIDAS (Rubro 15)
    (20, '107100', 'PANADERÍA Y PASTELERÍA', 'Fabricación de productos de panadería, pasteles y masas', 15),
    -- TELECOMUNICACIONES (Rubro 17)
    (21, '611000', 'TELECOMUNICACIONES ALÁMBRICAS', 'Proveedores de internet fibra óptica y telefonía fija', 17),
    (22, '612000', 'TELECOMUNICACIONES INALÁMBRICAS', 'Operadores de telefonía móvil y redes de datos inalámbricas', 17),
    -- SERVICIOS PROFESIONALES (Rubro 18)
    (23, '692000', 'CONTABILIDAD Y AUDITORÍA', 'Actividades de contabilidad, teneduría de libros y auditoría fiscal', 18),
    (24, '711001', 'SERVICIOS DE ARQUITECTURA', 'Diseño de edificios, planificación urbana y dibujo de planos', 18),
    -- GASTRONOMÍA (Rubro 19)
    (25, '561000', 'RESTAURANTES Y SANGUCHERÍAS', 'Actividades de restaurantes y de servicio móvil de comidas', 19);

-- ============================================================
-- 4.3. PRODUCTOS
-- ============================================================
MERGE INTO producto (producto_id, codigo_producto, nombre, descripcion, genero, color, creado_en, actualizado_en, activo)
    KEY (producto_id)
    VALUES
    (1, 'PROD-POL-001', 'Polerón Corporativo Premium', 'Polerón corporativo con gorro y bolsillos canguro', 'UNISEX', 'Azul Marino', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, true),
    (2, 'PROD-CHA-002', 'Chaqueta Impermeable Térmica', 'Chaqueta impermeable con forro micropolar interior', 'UNISEX', 'Gris Plata', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, true);

-- ============================================================
-- 5. CLIENTES (Modelo normalizado sin campos planos de contacto)
-- ============================================================
MERGE INTO clientes (cliente_id, activo, razon_social, run_cliente, sigla, fk_giro)
    KEY (cliente_id)
    VALUES
    (1, true, 'HITES S.A.', '96947020-9', 'S.A.', 1),
    (2, true, 'LABORATORIO MEDCELL', '96706320-7', 'LTDA.', 2),
    (3, true, 'GEODIS WILSON', '79699520-3', 'S.A.', 3);

-- ============================================================
-- 5.1. TIPOS DE CONTACTO Y CONTACTOS
-- ============================================================
MERGE INTO tipos_contacto (tipo_contacto_id, descripcion_tipo_contacto)
    KEY (tipo_contacto_id)
    VALUES
    (1, 'GENERAL'),
    (2, 'COMERCIAL'),
    (3, 'FINANZAS');

MERGE INTO contactos (contacto_id, nombre_contacto, telefono_contacto, email_contacto, tipo_contacto_id, fk_cliente_contacto)
    KEY (contacto_id)
    VALUES
    (1, 'CONTACTO HITES', '+56227275000', 'contacto.hites@hites.cl', 1, 1),
    (2, 'CONTACTO MEDCELL', '+56224396000', 'compras@medcell.cl', 1, 2),
    (3, 'CONTACTO GEODIS WILSON', '+56223816500', 'info.chile@geodis.com', 1, 3);

-- ============================================================
-- 5.2. GEOGRAFÍA Y DIRECCIONES
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

MERGE INTO direccion (direccion_id, calle, numero, depto, tipo_direccion_id, comuna_id, fk_cliente_direccion)
    KEY (direccion_id)
    VALUES
    (1, 'AV. KENNEDY', '5413', 'OF. 201', 1, 1, 1),
    (2, 'HOLANDA', '64', NULL, 1, 1, 2),
    (3, 'LO BOZA', '110', NULL, 1, 1, 3);

-- ============================================================
-- 6. PROVEEDORES (Modelo normalizado con giros asociados)
-- ============================================================
MERGE INTO proveedores (proveedor_id, activo, creado_en, actualizado_en, horario_atencion, razon_social_proveedor, run_proveedor, sigla, tipo_proveedor, fk_provee_giro)
    KEY (proveedor_id)
    VALUES
    (1, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '09:00 - 18:00', 'PARQUE ARAUCO S.A.', '99581960-0', 'S.A.', 'NACIONAL', 1),
    (2, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '09:00 - 18:00', 'C.C. LOS HEROES', '70016330-K', 'LTDA.', 'NACIONAL', 1),
    (3, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '09:00 - 18:00', 'MEDIPHARM LTDA.', '96599510-2', 'LTDA.', 'NACIONAL', 2),
    (4, true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '09:00 - 18:00', 'SODIMAC S.A.', '96792430-K', 'S.A.', 'NACIONAL', 4);

-- ============================================================
-- 6.1. BANCOS Y ENTIDADES FINANCIERAS
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

MERGE INTO tipo_cuenta_bancaria (tipo_cuenta_id, denominacion_cuenta)
    KEY (tipo_cuenta_id)
    VALUES
    (1, 'CUENTA CORRIENTE'),
    (2, 'CUENTA VISTA'),
    (3, 'CUENTA DE AHORRO'),
    (4, 'CUENTA RUT');

-- ============================================================
-- 6.2. DATOS BANCARIOS PROVEEDORES
-- ============================================================
MERGE INTO dato_bancario (dato_bancario_id, numero_cuenta, banco_id, tipo_cuenta_id, fk_provee_dato_bancario)
    KEY (dato_bancario_id)
    VALUES
    (1, '00-123-45678-09', 1, 1, 1),
    (2, '00-234-56789-01', 2, 2, 2),
    (3, '00-345-67890-12', 3, 1, 3),
    (4, '00-456-78901-23', 4, 1, 4);

-- ============================================================
-- 7. CATEGORÍAS Y SUBCATEGORÍAS DE TEXTIL
-- ============================================================

MERGE INTO categoria_tela (id_categoria_tela, codigo_categoria_tela, nombre_categoria_tela)
    KEY (id_categoria_tela)
    VALUES
    (1, 'TEJ-PLN', 'Tejido Plano'),
    (2, 'TEJ-PNT', 'Tejido de Punto'),
    (3, 'NO-TEJ',  'No Tejido / Técnico');

MERGE INTO subcategoria_tela (id_subcategoria_tela, codigo_subcategoria_tela, nombre_subcategoria_tela, id_categoria_tela)
    KEY (id_subcategoria_tela)
    VALUES
    (1, 'PLN-COTT', 'Algodón Plano',     1),
    (2, 'PLN-SYNT', 'Sintético Plano',   1),
    (3, 'PNT-FLEE', 'Fleece / Polar',    2),
    (4, 'PNT-JRSY', 'Jersey',            2),
    (5, 'TEC-IMPR', 'Impermeable Tech',  3);

-- ============================================================
-- 7.0.1 PARAMETRÍA TEXTIL (Familias, Clasificaciones, Composiciones,
-- Gramajes, Colores, Atributos Técnicos) — catálogo de referencia de
-- negocio para telas. Los códigos aquí son los oficiales de la empresa;
-- desde la UI, cualquier registro NUEVO se codifica solo (CodigoGeneratorService).
-- ============================================================

MERGE INTO familia_tela (id_familia_tela, codigo_familia, nombre_familia)
    KEY (id_familia_tela)
    VALUES
    (1,  'T-GAB', 'Gabardina'),
    (2,  'T-POP', 'Popelina'),
    (3,  'T-OXF', 'Oxford'),
    (4,  'T-MEZ', 'Mezclilla / Denim'),
    (5,  'T-TAF', 'Tafeta'),
    (6,  'T-TSL', 'Taslan'),
    (7,  'T-NYL', 'Nylon'),
    (8,  'T-HIP', 'Hipora'),
    (9,  'T-MAL', 'Malla'),
    (10, 'T-SOF', 'Softshell'),
    (11, 'T-POL', 'Polar'),
    (12, 'T-MIC', 'Micropolar'),
    (13, 'T-CHI', 'Chiporro'),
    (14, 'T-FRA', 'Franela'),
    (15, 'T-PIQ', 'Piqué'),
    (16, 'T-RIB', 'Rib / Puño'),
    (17, 'T-BIO', 'Bio Stretch / Bistrech'),
    (18, 'T-CUE', 'Cuello (Tejido)'),
    (19, 'T-AIS', 'Aislante Termico'),
    (20, 'T-ENT', 'Entretela Fusible'),
    (21, 'T-COR', 'Cordura'),
    (22, 'T-QDR', 'DryFit'),
    (23, 'T-JEY', 'Jersey'),
    (24, 'T-TRV', 'Trevira');

MERGE INTO clasificacion_tecnica (id_clasificacion_tecnica, nombre_clasificacion)
    KEY (id_clasificacion_tecnica)
    VALUES
    (1,  'Tejido Plano Estándar'),
    (2,  'Tejido Plano Liviano'),
    (3,  'Tejido Plano Camisería'),
    (4,  'Tejido Plano Pesado'),
    (5,  'Forrería / Tejido Plano'),
    (6,  'Sintético / Cortavientos'),
    (7,  'Sintético Liviano'),
    (8,  'Técnico Impermeable'),
    (9,  'Sintético Perforado / Forro'),
    (10, 'Técnico Multicapa'),
    (11, 'Térmico Sintético'),
    (12, 'Térmico Sintético Liviano'),
    (13, 'Térmico Aislante'),
    (14, 'Térmico Base / Punto'),
    (15, 'Tejido de Punto'),
    (16, 'Tejido de Punto Elástico'),
    (17, 'Componente Tejido'),
    (18, 'Aislante / Relleno'),
    (19, 'Insumo Estructural'),
    (20, 'Mochilas'),
    (21, 'Tela Deportiva'),
    (22, 'Poleras');

MERGE INTO composicion (id_composicion, codigo_composicion, descripcion_composicion, clasificacion, uso_tipico)
    KEY (id_composicion)
    VALUES
    (1,  'C-100AL',  '100% Algodón',                                'Pura',                'Ignífugo base, Franela pura'),
    (2,  'C-100DE',  '100% Algodón (14oz)',                         'Denim',               'Mezclilla Industrial Pesada'),
    (3,  'C-100NY',  '100% Nylon',                                  'Pura',                'Taslan, Cortavientos'),
    (4,  'C-100PA',  '100% Poliamida',                              'Refuerzo Técnico',    'Cordura / Refuerzo de rodillas y codos'),
    (5,  'C-100PO',  '100% Poliéster',                              'Pura',                'Polar, Oxford Económico, Malla'),
    (6,  'C-NPA',    '100% Poliéster ("Napa")',                     'Aislante Base',       '"Napa" Estándar. Relleno económico.'),
    (7,  'C-5050',   '50% Algodón / 50% Poliéster',                 'Tejido de Punto',     'Piqué de alta durabilidad'),
    (8,  'C-544303', '54% Poliéster / 43% Algodón / 3% Elastano',   'Mezcla Elástica',     'Popelina / Gabardina Stretch'),
    (9,  'C-593506', '59% Poliéster / 35% Viscosa / 6% Elastano',   'Mezcla Elástica',     'Popelina / Gabardina Stretch'),
    (10, 'C-6040',   '60% Algodón / 40% Poliéster',                 'Mezcla Confort',      'Ropa de trabajo premium'),
    (11, 'C-653302', '65% Poliéster / 33% Algodón / 2% Elastano',   'Mezcla Elástica',     'Popelina / Gabardina Stretch'),
    (12, 'C-6535',   '65% Poliéster / 35% Algodón',                 'Mezcla Estándar',     'Popelina, Gabardina (Workwear)'),
    (13, 'C-702802', '70% Algodón / 28% Poliéster / 2% Elastano',   'Denim Elástico',      'Mezclilla Flex'),
    (14, 'C-8020AL', '80% Algodón / 20% Poliéster',                 'Alta Respirabilidad', 'Franelas de alto gramaje o Canvas'),
    (15, 'C-8020PO', '80% Poliéster / 20% Algodón',                 'Mezcla Económica',    'Popelina promocional'),
    (16, 'C-9010',   '90% Poliéster / 10% Elastano',                'Elástica',            'Softshell, Ropa técnica'),
    (17, 'C-9208',   '92% Poliéster / 8% Elastano',                 'Mezcla Elástica',     'Popelina / Gabardina Stretch'),
    (18, 'C-9505',   '95% Poliéster / 5% Elastano',                 'Mezcla Elástica',     'Popelina / Gabardina Stretch'),
    (19, 'C-9703',   '97% Poliéster / 3% Elastano',                 'Mezcla Elástica',     'Popelina / Gabardina Stretch'),
    (20, 'C-9802',   '98% Algodón / 2% Elastano',                   'Elástica',            'Bio Stretch, Gabardina Flex'),
    (21, 'C-FLP',    'Fibra Hueca Siliconada ("Flopy")',            'Aislante Volumen',    'Alto volumen (loft), tacto pluma, parkas gruesas.'),
    (22, 'C-TRM',    'Microfibra Térmica ("Termaloft")',            'Aislante Técnico',    'Retención de calor premium, bajo volumen, alta densidad.'),
    (23, 'C-ARAM',   'Modacrílico / Algodón / Aramida',             'Antiarco Eléctrico',  'Ropa ignífuga certificada');

MERGE INTO gramaje_tela (id_gramaje, codigo_gramaje, valor_gramos_m2, categoria_vestuario)
    KEY (id_gramaje)
    VALUES
    (1,  'G-100',  100.00, NULL),
    (2,  'G-115',  115.00, 'Popelina / Microfibra (Liviana)'),
    (3,  'G-120',  120.00, 'Relleno Termico'),
    (4,  'G-150',  150.00, 'Oxford / Popelina (Estándar) / Relleno termico'),
    (5,  'G-165',  165.00, 'Hipora / Oxford / Popelina (pesadas)'),
    (6,  'G-175',  175.00, 'Relleno Termico'),
    (7,  'G-190',  190.00, 'Gabardina Liviana (Verano / Salud)'),
    (8,  'G-210',  210.00, 'Piqué / Poleras Base'),
    (9,  'G-300',  300.00, 'Softshell / Polar'),
    (10, 'G-7OZ',  237.00, 'Gabardina / Twill Estándar'),
    (11, 'G-8OZ',  271.00, 'Gabardina Pesada / Pantalón Cargo'),
    (12, 'G-9OZ',  305.00, 'Gabardina Alta Resistencia'),
    (13, 'G-10OZ', 339.00, 'Mezclilla Liviana / Gabardina Extrema'),
    (14, 'G-14OZ', 475.00, 'Mezclilla Industrial Pesada (Minería)');

MERGE INTO color_tela (id_color, codigo_color, descripcion_color, es_pantone)
    KEY (id_color)
    VALUES
    (1,  '0',    'Crudo / Base / Sin Color',    false),
    (2,  'BCO',  'Blanco',                      false),
    (3,  'NEG',  'Negro',                       false),
    (4,  'AZMA', 'Azul Marino',                 false),
    (5,  'AZFR', 'Azul Francia',                false),
    (6,  'AZRE', 'Azul Rey / Azulino',          false),
    (7,  'AZVI', 'Azul Vigilante / Guardia',    false),
    (8,  'AZCL', 'Celeste / Azul Claro',        false),
    (9,  'ROJ',  'Rojo',                        false),
    (10, 'BEO',  'Burdeo',                      false),
    (11, 'GRCL', 'Gris Claro',                  false),
    (12, 'GRME', 'Gris Medio',                  false),
    (13, 'GROS', 'Gris Oscuro',                 false),
    (14, 'MELG', 'Melange (Gris/Mixto)',        false),
    (15, 'VEBO', 'Verde Botella / Oscuro',      false),
    (16, 'VEES', 'Verde Esmeralda',             false),
    (17, 'CALP', 'Calipso',                     false),
    (18, 'TURQ', 'Turquesa',                    false),
    (19, 'BGE',  'Beige / Caqui',               false),
    (20, 'CAF',  'Café / Marrón',               false),
    (21, 'AMA',  'Amarillo',                    false),
    (22, 'NAR',  'Naranja',                     false),
    (23, 'PET',  'Petróleo',                    false),
    (24, 'AMFL', 'Amarillo Flúor',              false),
    (25, 'NAFL', 'Naranja Flúor',               false),
    (26, 'ROFL', 'Rojo Flúor',                  false),
    (27, 'EST',  'Estampado (Varios)',          false);
-- Nota: "GRME" se repetía en la tabla de origen para "Gris Medio" y "Melange";
-- se desambiguó a "MELG" para Melange (misma regla que aplica el generador de códigos ante colisiones).

MERGE INTO atributo_tecnico (id_atributo, codigo_atributo, clasificacion, descripcion_tecnica, impacto_erp)
    KEY (id_atributo)
    VALUES
    (1,  'A-000',   'Estándar', 'Sin tratamientos especiales',         'Tela base por defecto.'),
    (2,  'A-RIP',   'Construcción', 'Ripstop (Antidesgarro)',          'Modifica la cuadrícula del tejido plano.'),
    (3,  'A-UV',    'Químico',  'Protección UV (UPF 50+)',             'Baño protector corporativo estándar.'),
    (4,  'A-WR',    'Físico',   'Repelencia al Agua',                  'Acabado superficial (resistencia al rocío).'),
    (5,  'A-BRT',   'Fisico',   'Respirabilidad',                      'Membrana interna respirable'),
    (6,  'A-WP',    'Físico',   'Impermeabilidad',                     'Membrana interna o recubrimiento (Ej. PU).'),
    (7,  'A-FR',    'Químico',  'Fire Retardant (Ignífugo)',           'Certificación de seguridad. Multiplica el costo.'),
    (8,  'A-100FR', 'Quimico',  'Tratamiento FR',                      'Ropa ignífuga básica'),
    (9,  'A-ACI',   'Químico',  'Antiácido',                           'Requisito minero o de laboratorio.'),
    (10, 'A-AFLUD', 'Químico',  'Antifluido',                          'Acabado de fácil lavado (ropa clínica/gastronómica).'),
    (11, 'A-ANT',   'Físico',   'Antiestático',                        'Inserción de hilo de carbono para disipación.'),
    (12, 'A-ALU',   'Fisico',   'Reflectivo Aluminizado (Forro)',      'Para termicidad interna'),
    (13, 'A-ACOL',  'Fisico',   'Acolchado especial',                  'Para rellenos termicos + Forro'),
    (14, 'A-ABACT', 'Fisico',   'Antibacteriano',                      'Ropa clínica');

-- ============================================================
-- 7.0.2 INSUMOS Y ACCESORIOS — Tipos y sus atributos dinámicos
-- (reglas de negocio: Cierre, Broche, Botón, Tanka, Ojetillos, Tirador,
-- Velcro, Chaveta, Tip-Top, Escalerilla, Elástico, Cordón, Vivo, Sesgo,
-- Cintas). El formulario de "Nuevo Insumo" dibuja estos campos según el
-- Tipo de Accesorio elegido; el color "REFERENCIA_COLOR" reutiliza el
-- catálogo de Colores de arriba en vez de duplicarlo.
-- ============================================================

MERGE INTO tipo_accesorio (id_tipo_accesorio, codigo, nombre)
    KEY (id_tipo_accesorio)
    VALUES
    (1,  'CIE', 'Cierre'),
    (2,  'BRO', 'Broche'),
    (3,  'BOT', 'Botón'),
    (4,  'TAN', 'Tanka'),
    (5,  'OJE', 'Ojetillos'),
    (6,  'TIR', 'Tirador'),
    (7,  'VEL', 'Velcro'),
    (8,  'CHV', 'Chaveta de Ajuste'),
    (9,  'TTP', 'Tip-Top'),
    (10, 'ESC', 'Escalerilla Reguladora'),
    (11, 'ELA', 'Elástico'),
    (12, 'COR', 'Cordón'),
    (13, 'VIV', 'Vivo'),
    (14, 'SES', 'Sesgo'),
    (15, 'CNE', 'Cinta Espiga'),
    (16, 'CNF', 'Cinta Falla (Mochila)'),
    (17, 'CNR', 'Cinta Reflectante'),
    (18, 'CSC', 'Cinta Sellado de Costura');

MERGE INTO atributo_accesorio_definicion (id_definicion, id_tipo_accesorio, nombre_campo, tipo_dato, opciones, orden, requerido)
    KEY (id_definicion)
    VALUES
    -- Cierre
    (1,  1,  'Tipo',             'LISTA',            'DP|NY|NY Invertido|Impermeable|Bronce|Ignífugo', 1, true),
    (2,  1,  'N°',               'LISTA',            '3|5|7', 2, false),
    (3,  1,  'Terminal',         'LISTA',            'Fijo|Separable', 3, true),
    (4,  1,  'Carro',            'LISTA',            'Simple|Doble|Reversible', 4, true),
    (5,  1,  'Medida',           'LISTA',            '12cm|15cm|18cm|20cm|50cm|55cm|60cm|65cm|70cm|75cm|80cm|85cm|90cm|95cm|100cm', 5, true),
    (6,  1,  'Color',            'REFERENCIA_COLOR', NULL, 6, true),
    -- Broche
    (7,  2,  'Material',         'LISTA',            'Plástico|Metálico', 1, true),
    (8,  2,  'Tipo',             'LISTA',            'Corriente|Punta de bola|Envejecido', 2, true),
    (9,  2,  'Color',            'REFERENCIA_COLOR', NULL, 3, true),
    -- Botón
    (10, 3,  'Material',         'LISTA',            'Plástico', 1, true),
    (11, 3,  'Diámetro (Línea)', 'LISTA',            '18|24|28', 2, true),
    (12, 3,  'Color',            'REFERENCIA_COLOR', NULL, 3, true),
    -- Tanka
    (13, 4,  'Material',         'LISTA',            'Plástico|Metálica', 1, true),
    (14, 4,  'Tipo',             'LISTA',            'Simple|Doble|Envejecido', 2, true),
    (15, 4,  'Color',            'REFERENCIA_COLOR', NULL, 3, true),
    -- Ojetillos
    (16, 5,  'Material',         'LISTA',            'Plástico|Metálico', 1, true),
    (17, 5,  'Color',            'LISTA',            'Mismos colores que las telas|Metal|Envejecido', 2, true),
    -- Tirador
    (18, 6,  'Tipo',             'LISTA',            'Corriente|Marca de Cliente|Logo Antuan', 1, true),
    (19, 6,  'Color',            'REFERENCIA_COLOR', NULL, 2, true),
    -- Velcro
    (20, 7,  'Medida (Ancho)',   'LISTA',            '1.0cm|1.5cm|2.0cm|2.5cm|4.0cm|5.0cm', 1, true),
    (21, 7,  'Color',            'REFERENCIA_COLOR', NULL, 2, true),
    -- Chaveta de Ajuste
    (22, 8,  'Material',         'LISTA',            'Plástica', 1, true),
    (23, 8,  'Color',            'LISTA',            'Negro', 2, true),
    -- Tip-Top
    (24, 9,  'Material',         'LISTA',            'Plástico', 1, true),
    (25, 9,  'Color',            'LISTA',            'Negro', 2, true),
    -- Escalerilla Reguladora
    (26, 10, 'Material',         'LISTA',            'Plástica|Metálica', 1, true),
    (27, 10, 'Color',            'LISTA',            'Negro|Metal', 2, true),
    -- Elástico
    (28, 11, 'Medida (Ancho)',   'LISTA',            '1.0cm|1.5cm|2.0cm|2.5cm|3.0cm|4.0cm|5.0cm', 1, true),
    (29, 11, 'Color',            'LISTA',            'Blanco|Negro', 2, true),
    -- Cordón
    (30, 12, 'Material',         'LISTA',            'Elasticado|Rígido', 1, true),
    (31, 12, 'Color',            'REFERENCIA_COLOR', NULL, 2, true),
    -- Vivo
    (32, 13, 'Material',         'LISTA',            'Tela|Reflectante', 1, true),
    (33, 13, 'Color',            'LISTA',            'Gris|Día y Noche', 2, true),
    -- Sesgo
    (34, 14, 'Material',         'LISTA',            'Elasticado|Rígido', 1, true),
    (35, 14, 'Color',            'REFERENCIA_COLOR', NULL, 2, true),
    -- Cinta Espiga
    (36, 15, 'Composición',      'LISTA',            '50% Poliéster - 50% Algodón|100% Poliéster|100% Algodón', 1, true),
    (37, 15, 'Medida (Ancho)',   'LISTA',            '1.0cm|1.5cm|2.0cm', 2, true),
    (38, 15, 'Color',            'REFERENCIA_COLOR', NULL, 3, true),
    -- Cinta Falla (Mochila)
    (39, 16, 'Composición',      'LISTA',            '50% Poliéster - 50% Algodón|100% Poliéster|100% Algodón', 1, true),
    (40, 16, 'Medida (Ancho)',   'LISTA',            '1.0cm|1.5cm|2.0cm|2.5cm|3.0cm|4.0cm', 2, true),
    -- Cinta Reflectante
    (41, 17, 'Material',         'LISTA',            'Tela|PET - Poliéster', 1, true),
    (42, 17, 'Diseño',           'LISTA',            'Lisa|Segmentada', 2, true),
    (43, 17, 'Color',            'LISTA',            'Gris|Día y Noche', 3, true),
    (44, 17, 'Medida (Ancho)',   'LISTA',            '1"|2"', 4, true),
    (45, 17, 'Pegado',           'LISTA',            'Cosida|Termofusionada|Corriente', 5, true),
    (46, 17, 'Observaciones',    'TEXTO',            NULL, 6, false);
    -- Cinta Sellado de Costura (tipo 18) no tiene atributos dinámicos propios.

-- ============================================================
-- 7.1. ARTÍCULOS (Catálogo Base)
-- ============================================================
MERGE INTO articulo (id_articulo, codigo_articulo, nombre_articulo, descripcion_articulo, codigo_barra, id_tipo_articulo, activo, id_categoria_tela, id_subcategoria_tela)
    KEY (id_articulo)
    VALUES
    (1, 'ART-FLEE-001', 'Polar Fleece 280 GSM',      'Tela polar fleece gramaje 280 g/m²',    NULL, 1, true, 2, 3),
    (2, 'ART-IMPR-001', 'Ripstop Impermeable',        'Tejido técnico ripstop impermeabilizado', NULL, 1, true, 3, 5),
    (3, 'ART-JRSY-001', 'Jersey Piqué Algodón',       'Jersey piqué 100% algodón peinado',      NULL, 1, true, 2, 4),
    (4, 'ART-ACC-001',  'Cierre YKK 60cm Metálico',   'Cierre metálico YKK 60 cm',             NULL, 3, true, NULL, NULL),
    (5, 'ART-ACC-002',  'Botón Snap 15mm Nácar',       'Botón tipo snap nacarado 15 mm',        NULL, 3, true, NULL, NULL);

-- ============================================================
-- 7.1.1. DETALLE TELA (articulo_tela) — completa el maestro de
--        Composición/Familia/Gramaje de los 3 artículos tipo TELA,
--        requerido para que el combo de Tela en EVN/NV pueda
--        autocompletar la Composición al seleccionarlos.
--        IDs referencian el bloque de familia_tela/clasificacion_tecnica/
--        composicion/gramaje_tela que gana en H2 (el segundo MERGE, más
--        abajo en este archivo, sobrescribe al primero por compartir KEY).
-- ============================================================
MERGE INTO articulo_tela (id_articulo, id_familia_tela, id_clasificacion_tecnica, id_composicion, id_gramaje)
    KEY (id_articulo)
    VALUES
    (1, 5,  1, 2,  9),
    (2, 16, 4, 10, 3),
    (3, 7,  1, 1,  4);

-- ============================================================
-- 7.2. CATÁLOGO DE CAMPOS DE PLANTILLA
-- ============================================================
MERGE INTO plantilla (id_plantilla, nombre_campo)
    KEY (id_plantilla)
    VALUES
    (1,  'GORRO'),
    (2,  'CUELLO'),
    (3,  'ABOTONADURA / CIERRE'),
    (4,  'CORTES Y APLICACIONES'),
    (5,  'FUELLES'),
    (6,  'MANGAS'),
    (7, 'PUÑOS'),
    (8,  'PRETINAS / RUEDO'),
    (9,  'BOLSILLOS'),
    (10, 'OBS. DEL MODELO');

-- ============================================================
-- 7.3. MODELO PLANTILLA (Mapeo Artículo e Id Plantilla)
-- ============================================================
-- Una sola fila por artículo: los campos de su plantilla en CSV (nombres de `plantilla`).
MERGE INTO modelo_plantilla (id_modelo_plantilla, id_articulo, campos)
    KEY (id_modelo_plantilla)
    VALUES
    (1, 1, 'GORRO,CUELLO,ABOTONADURA / CIERRE,CORTES Y APLICACIONES,FUELLES,MANGAS,PUÑOS,PRETINAS / RUEDO,BOLSILLOS,OBS. DEL MODELO'),                  -- Polar Fleece
    (2, 2, 'CUELLO,ABOTONADURA / CIERRE,MANGAS,BOLSILLOS'),    -- Ripstop
    (3, 3, 'CORTES Y APLICACIONES,MANGAS,PRETINAS / RUEDO');                          -- Jersey

-- ============================================================
-- 7.4. SOLICITUDES DE COSTOS (SCOS)
-- ============================================================
MERGE INTO solicitudes_costos (id_scos, numero, estado, tipo, cliente_id, vendedor_id, articulo_descripcion, nombre_prenda, genero, tallaje, es_muestra, has_logo, cantidad, fecha, costo_total)
    KEY (id_scos)
    VALUES
    (1, 'SCOS-000001', 'PENDIENTE', 'SCOS', 1, 1, 'POLERA', 'Polera Piqué Corporativa', 'UNISEX', 'Antuan SA', false, true,  100, CURRENT_DATE, 150000.00),
    (2, 'SCOS-000002', 'APROBADA',  'SCOS', 2, 2, 'PANTALON', 'Pantalón Cargo Operario', 'MASCULINO', 'Cliente', false, false, 50, CURRENT_DATE, 250000.00);

-- ============================================================
-- 7.5. EVALUACIONES DE NEGOCIO (EVN)
-- ============================================================
MERGE INTO evaluaciones_negocio (id_evn, numero, referencia, cliente_nombre, cliente_id, vendedor_id, estado, fecha_evaluacion, porcentaje_comision, created_at, updated_at)
    KEY (id_evn)
    VALUES
    (1, 'EVN-000001', 'Cotización Poleras Hites', 'HITES S.A.', 1, 1, 'EVALUACION', CURRENT_DATE, 20.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (2, 'EVN-000002', 'Licitación Pantalones', 'LABORATORIO MEDCELL', 2, 2, 'APROBADA', CURRENT_DATE, 20.00, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- ============================================================
-- 7.6. NOTAS DE VENTA (NV)
-- ============================================================
MERGE INTO notas_venta (id_nv, numero_nv, evaluacion_negocio_id, cliente_id, vendedor_id, estado, es_kit, fecha_emision, fecha_entrega_estimada, monto_subtotal, moneda_subtotal, monto_iva, moneda_iva, monto_total, moneda_total, created_at, updated_at)
    KEY (id_nv)
    VALUES
    (1, 'NV-0000001', 2, 2, 2, 'BORRADOR', false, CURRENT_DATE, DATEADD('DAY', 30, CURRENT_DATE), 300000.00, 'CLP', 57000.00, 'CLP', 357000.00, 'CLP', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (2, 'NV-0000002', 1, 1, 1, 'EN_PRODUCCION', false, CURRENT_DATE, DATEADD('DAY', 45, CURRENT_DATE), 850000.00, 'CLP', 161500.00, 'CLP', 1011500.00, 'CLP', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- ============================================================
-- 7.7. PLANIFICACIÓN DE PRODUCCIÓN Y HOJAS DE COMPRA
-- ============================================================
MERGE INTO produccion_costeos (id_costeo, solicitud_costos_id, numero_costeo, estado, version)
    KEY (id_costeo)
    VALUES
    (1, 2, 'COST-0000001', 'APROBADO', 1),
    -- Costeo en blanco de SCOS-000001 (Polera) — toda SCOS creada vía la app
    -- recibe automáticamente un Costeo (generatePreCosteo), aunque no se use
    -- en el flujo de pruebas de OP/HC de este seed.
    (2, 1, 'COST-0000002', 'BORRADOR', 1);

MERGE INTO produccion_costeo_versiones (id_costeo_version, costeo_id, numero_version, fecha_creacion, usuario_creador)
    KEY (id_costeo_version)
    VALUES
    (1, 1, 1, CURRENT_TIMESTAMP, 'SISTEMA');

MERGE INTO orden_produccion (id_op, costeo_version_id, numero_op, nota_venta_id, estado, fecha_inicio, fecha_entrega_programada, observaciones, created_at, updated_at)
    KEY (id_op)
    VALUES
    (1, 1, 'OP- 00001', 2, 'EN_PROCESO', CURRENT_DATE, DATEADD('DAY', 30, CURRENT_DATE), 'Producción Pantalón Cargo Operario - Laboratorio Medcell', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

MERGE INTO produccion_orden_items (id_op_item, orden_produccion_id, articulo_id, nro_item, modelo, tela, color, talla, genero, codigo, lleva_logo, cantidad)
    KEY (id_op_item)
    VALUES
    (1, 1, 2, 1, 'Pantalón Cargo', 'Ripstop Impermeable', 'Verde', 'M', 'MASCULINO', 'PANT-CARGO-M', 'SI', 25),
    (2, 1, 2, 2, 'Pantalón Cargo', 'Ripstop Impermeable', 'Verde', 'L', 'MASCULINO', 'PANT-CARGO-L', 'SI', 25);

MERGE INTO produccion_hojas_compra (id_hc, numero_hc, op_id, costeo_version_id, estado, fecha_generacion, observaciones)
    KEY (id_hc)
    VALUES
    (1, 'HC-000001', 1, 1, 'APROBADA', CURRENT_DATE, 'HC generada para OP-000001');

MERGE INTO produccion_hoja_compra_items (id_hc_item, hc_id, tipo_insumo, articulo_id, proveedor_id, nombre_insumo, consumo_unitario, cantidad_op, cantidad_requerida, precio_unitario_ref)
    KEY (id_hc_item)
    VALUES
    (1, 1, 'TELA',      2, 1, 'Ripstop Impermeable',         1.8000, 50, 90.0000,  4500.00),
    (2, 1, 'ACCESORIO', 4, 2, 'Cierre YKK 60cm Metálico',    1.0000, 50, 50.0000,  1200.00),
    (3, 1, 'ACCESORIO', 5, 2, 'Botón Snap 15mm Nácar',       4.0000, 50, 200.0000, 150.00);

-- ============================================================
-- 7.8. CONTADORES DE DOCUMENTOS (document_counter)
-- ============================================================
MERGE INTO document_counter (tipo, ultimo_numero)
    KEY (tipo)
    VALUES
    ('NV',   2),
    ('EVN',  2),
    ('SCOS', 2),
    ('SCOT', 0),
    ('COST', 2),
    ('OP',   1),
    ('HC',   1);

-- ============================================================
-- 7.8. MAESTROS GLOBALES (Moneda, Unidad de Medida)
-- ============================================================
MERGE INTO moneda (id_moneda, codigo_moneda, nombre_moneda, simbolo)
    KEY (id_moneda)
    VALUES
    (1, 'CLP', 'Peso Chileno', '$'),
    (2, 'USD', 'Dólar Estadounidense', 'US$'),
    (3, 'EUR', 'Euro', '€');

MERGE INTO unidad_medida (id_unidad_medida, nombre_unidad, abreviatura)
    KEY (id_unidad_medida)
    VALUES
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
MERGE INTO familia_tela (id_familia_tela, codigo_familia, nombre_familia)
    KEY (id_familia_tela)
    VALUES
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

MERGE INTO clasificacion_tecnica (id_clasificacion_tecnica, nombre_clasificacion)
    KEY (id_clasificacion_tecnica)
    VALUES
    (1, 'Tejido de Punto'),
    (2, 'Tejido Plano'),
    (3, 'No Tejido / TNT'),
    (4, 'Técnico / Funcional'),
    (5, 'Alta Prestación');

MERGE INTO composicion (id_composicion, codigo_composicion, descripcion_composicion, clasificacion, uso_tipico)
    KEY (id_composicion)
    VALUES
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

MERGE INTO gramaje_tela (id_gramaje, codigo_gramaje, valor_gramos_m2, categoria_vestuario)
    KEY (id_gramaje)
    VALUES
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

MERGE INTO color_tela (id_color, codigo_color, descripcion_color, es_pantone)
    KEY (id_color)
    VALUES
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

MERGE INTO atributo_tecnico (id_atributo, codigo_atributo, clasificacion, descripcion_tecnica, impacto_erp)
    KEY (id_atributo)
    VALUES
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
ALTER TABLE areas ALTER COLUMN id_area RESTART WITH 50;
ALTER TABLE roles ALTER COLUMN id_role RESTART WITH 100;
ALTER TABLE usuarios ALTER COLUMN id_usuario RESTART WITH 100;
ALTER TABLE clientes ALTER COLUMN cliente_id RESTART WITH 100;
ALTER TABLE vendedores ALTER COLUMN id_vendedor RESTART WITH 200;
ALTER TABLE proveedores ALTER COLUMN proveedor_id RESTART WITH 100;
ALTER TABLE giros ALTER COLUMN giro_id RESTART WITH 100;
ALTER TABLE producto ALTER COLUMN producto_id RESTART WITH 100;

-- Artículos y catálogo
ALTER TABLE articulo ALTER COLUMN id_articulo RESTART WITH 1000;
ALTER TABLE categoria_tela ALTER COLUMN id_categoria_tela RESTART WITH 100;
ALTER TABLE subcategoria_tela ALTER COLUMN id_subcategoria_tela RESTART WITH 100;

-- Plantillas
ALTER TABLE plantilla ALTER COLUMN id_plantilla RESTART WITH 100;
ALTER TABLE modelo_plantilla ALTER COLUMN id_modelo_plantilla RESTART WITH 1000;
ALTER TABLE descripcion_plantilla ALTER COLUMN id_descripcion_plantilla RESTART WITH 5000;

-- SCOS / Costeos
ALTER TABLE solicitudes_costos ALTER COLUMN id_scos RESTART WITH 2000;
ALTER TABLE scos_telas ALTER COLUMN id_scos_tela RESTART WITH 2000;
ALTER TABLE scos_logotipos ALTER COLUMN id_scos_logotipo RESTART WITH 2000;
ALTER TABLE scos_plantilla_material_vinculo ALTER COLUMN id_scos_plantilla_material_vinculo RESTART WITH 5000;
ALTER TABLE produccion_costeos ALTER COLUMN id_costeo RESTART WITH 100;
ALTER TABLE produccion_costeo_versiones ALTER COLUMN id_costeo_version RESTART WITH 100;
ALTER TABLE produccion_costeo_items ALTER COLUMN id_costeo_item RESTART WITH 5000;

-- EVN / NV / OP
ALTER TABLE evaluaciones_negocio ALTER COLUMN id_evn RESTART WITH 1000;
ALTER TABLE notas_venta ALTER COLUMN id_nv RESTART WITH 1000;
ALTER TABLE orden_produccion ALTER COLUMN id_op RESTART WITH 100;
ALTER TABLE produccion_orden_items ALTER COLUMN id_op_item RESTART WITH 1000;
ALTER TABLE produccion_hojas_compra ALTER COLUMN id_hc RESTART WITH 100;
ALTER TABLE produccion_hoja_compra_items ALTER COLUMN id_hc_item RESTART WITH 1000;

-- Contactos, Direcciones y Financieros
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

-- Maestros adicionales
ALTER TABLE moneda ALTER COLUMN id_moneda RESTART WITH 10;
ALTER TABLE unidad_medida ALTER COLUMN id_unidad_medida RESTART WITH 10;
ALTER TABLE familia_tela ALTER COLUMN id_familia_tela RESTART WITH 50;
ALTER TABLE clasificacion_tecnica ALTER COLUMN id_clasificacion_tecnica RESTART WITH 20;
ALTER TABLE composicion ALTER COLUMN id_composicion RESTART WITH 50;
ALTER TABLE gramaje_tela ALTER COLUMN id_gramaje RESTART WITH 50;
ALTER TABLE color_tela ALTER COLUMN id_color RESTART WITH 50;
ALTER TABLE atributo_tecnico ALTER COLUMN id_atributo RESTART WITH 50;
ALTER TABLE tipo_accesorio ALTER COLUMN id_tipo_accesorio RESTART WITH 50;
ALTER TABLE atributo_accesorio_definicion ALTER COLUMN id_definicion RESTART WITH 100;
