-- =============================================================
-- SCRIPT DE POBLADO INICIAL - ANTUAN SA
-- Módulo: maestros
-- Fecha: 2026-05-19
-- Tablas JPA: unidad_medida, moneda, familia_tela, clasificacion_tecnica,
--             composicion, gramaje_tela, color_tela, atributo_tecnico,
--             categoria, subcategoria,
--             articulo, articulo_tela, articulo_prenda, articulo_accesorio,
--             tela_color, tela_atributo_tecnico, precio
-- =============================================================

-- -------------------------------------------------------------
-- MONEDA
-- -------------------------------------------------------------
INSERT INTO moneda (codigo_moneda, nombre_moneda, simbolo) VALUES
('CLP', 'Peso Chileno', '$'),
('USD', 'Dólar Estadounidense', 'US$'),
('EUR', 'Euro', '€');

-- -------------------------------------------------------------
-- UNIDAD DE MEDIDA
-- -------------------------------------------------------------
INSERT INTO unidad_medida (nombre_unidad, abreviatura) VALUES
('Metro',          'M'),
('Metro Cuadrado', 'M2'),
('Kilogramo',      'KG'),
('Unidad',         'UN'),
('Par',            'PAR'),
('Caja',           'CJA'),
('Rollo',          'ROL');

-- -------------------------------------------------------------
-- FAMILIA TELA
-- -------------------------------------------------------------
INSERT INTO familia_tela (codigo_familia, nombre_familia) VALUES
('FT-01', 'Jersey'),
('FT-02', 'Rib'),
('FT-03', 'Interlock'),
('FT-04', 'Fleece / French Terry'),
('FT-05', 'Polar / Sherpa'),
('FT-06', 'Scuba / Neoprene'),
('FT-07', 'Piqué / Lacoste'),
('FT-08', 'Tafetán / Popelín'),
('FT-09', 'Sarga / Drill'),
('FT-10', 'Denim'),
('FT-11', 'Gabardina'),
('FT-12', 'Terciopelo / Velvet'),
('FT-13', 'Punto Roma / Bengalina'),
('FT-14', 'Licra / Spandex plano'),
('FT-15', 'Malla deportiva / Mesh'),
('FT-16', 'Oxford / Ripstop'),
('FT-17', 'Lana / Paño'),
('FT-18', 'Lino / Ramio'),
('FT-19', 'Seda / Satén'),
('FT-20', 'Microfibra / Softshell');

-- -------------------------------------------------------------
-- CLASIFICACION TECNICA
-- -------------------------------------------------------------
INSERT INTO clasificacion_tecnica (nombre_clasificacion) VALUES
('Tejido de Punto'),
('Tejido Plano'),
('No Tejido / TNT'),
('Técnico / Funcional'),
('Alta Prestación');

-- -------------------------------------------------------------
-- COMPOSICION
-- -------------------------------------------------------------
INSERT INTO composicion (codigo_composicion, descripcion_composicion, clasificacion, uso_tipico) VALUES
('CO-01',  '100% Algodón',                      'Natural',       'Poleras, básicos, ropa interior'),
('CO-02',  '100% Poliéster',                    'Sintético',     'Deportivo, sublimación, forro'),
('CO-03',  '50% Algodón / 50% Poliéster',       'Mixto',         'Poleras mix, uso general'),
('CO-04',  '65% Poliéster / 35% Algodón',       'Mixto',         'Uniformes corporativos'),
('CO-05',  '95% Algodón / 5% Elastano',         'Natural-Elást', 'Poleras ajustadas, casualwear'),
('CO-06',  '95% Poliéster / 5% Elastano',       'Sint-Elást',    'Deportivo técnico, leggings'),
('CO-07',  '60% Algodón / 40% Poliéster',       'Mixto',         'Prendas escolares, uniformes'),
('CO-08',  '80% Algodón / 20% Poliéster',       'Mixto',         'Camisas, poleras premium mix'),
('CO-09',  '100% Viscosa / Rayón',              'Celulósico',    'Blusas, vestidos, forro liviano'),
('CO-10',  '100% Nylon / Poliamida',            'Sintético',     'Chaquetas, cortavientos'),
('CO-11',  '88% Poliéster / 12% Elastano',      'Sint-Elást',    'Ropa deportiva compresión'),
('CO-12',  '70% Algodón / 30% Poliéster',       'Mixto',         'Poleras escolares'),
('CO-13',  '100% Lana Merino',                  'Natural',       'Paños, abrigos premium'),
('CO-14',  '100% Lino',                          'Natural',       'Verano, guayaberas'),
('CO-15',  '55% Lino / 45% Algodón',            'Natural',       'Ropa verano premium'),
('CO-16',  '50% Viscosa / 50% Poliéster',       'Mixto',         'Vestidos, blusas'),
('CO-17',  '90% Poliéster / 10% Elastano',      'Sint-Elást',    'Mallas, cycling'),
('CO-18',  '100% Algodón Orgánico',             'Natural',       'Línea eco, bebé');

-- -------------------------------------------------------------
-- GRAMAJE TELA
-- -------------------------------------------------------------
INSERT INTO gramaje_tela (codigo_gramaje, valor_gramos_m2, categoria_vestuario) VALUES
('GR-01',  120.00, 'Verano / Ropa interior liviana'),
('GR-02',  140.00, 'Camisetas básicas verano'),
('GR-03',  160.00, 'Poleras estándar'),
('GR-04',  180.00, 'Poleras premium / escolares'),
('GR-05',  200.00, 'Poleras gruesas / licra deportiva'),
('GR-06',  220.00, 'Rib / uniformes corporativos'),
('GR-07',  240.00, 'Interlock / prendas doble cara'),
('GR-08',  260.00, 'Fleece liviano / jogger'),
('GR-09',  280.00, 'Buzo / French Terry'),
('GR-10',  300.00, 'Fleece grueso / parka interior'),
('GR-11',  320.00, 'Polar ligero'),
('GR-12',  350.00, 'Polar medio / Sherpa'),
('GR-13',  380.00, 'Polar grueso / abrigo'),
('GR-14',  400.00, 'Paño / gabardina gruesa'),
('GR-15',  450.00, 'Lana / abrigo invernal'),
('GR-16',  500.00, 'Tapicería / lona técnica');

-- -------------------------------------------------------------
-- COLOR TELA
-- -------------------------------------------------------------
INSERT INTO color_tela (codigo_color, descripcion_color, es_pantone) VALUES
('COL-01',  'Blanco',           FALSE),
('COL-02',  'Negro',            FALSE),
('COL-03',  'Gris Claro',       FALSE),
('COL-04',  'Gris Oscuro',      FALSE),
('COL-05',  'Azul Navy',        FALSE),
('COL-06',  'Azul Royal',       FALSE),
('COL-07',  'Azul Petróleo',    FALSE),
('COL-08',  'Rojo',             FALSE),
('COL-09',  'Burdeo',           FALSE),
('COL-10',  'Verde Botella',    FALSE),
('COL-11',  'Verde Menta',      FALSE),
('COL-12',  'Amarillo',         FALSE),
('COL-13',  'Naranjo',          FALSE),
('COL-14',  'Café',             FALSE),
('COL-15',  'Beige / Arena',    FALSE),
('COL-16',  'Celeste',          FALSE),
('COL-17',  'Lila / Malva',     FALSE),
('COL-18',  'Rosado',           FALSE),
('COL-19',  'Fucsia',           FALSE),
('COL-20',  'Caqui / Olive',    FALSE),
('COL-21',  'Melange Gris',     FALSE),
('COL-22',  'Melange Azul',     FALSE),
('COL-23',  'Marino Melange',   FALSE);

-- -------------------------------------------------------------
-- ATRIBUTO TECNICO
-- -------------------------------------------------------------
INSERT INTO atributo_tecnico (codigo_atributo, clasificacion, descripcion_tecnica, impacto_erp) VALUES
('AT-01', 'Funcional',    'Antimicrobiano',                    'Certificado requerido para exportación'),
('AT-02', 'Funcional',    'Transpirable / Moisture Wicking',   'Indicar en ficha técnica deportiva'),
('AT-03', 'Funcional',    'UPF 50+ Protección Solar',          'Aplica a prendas outdoor/verano'),
('AT-04', 'Funcional',    'Ignifugo / Retardante de Llama',    'Homologación obligatoria para EPP'),
('AT-05', 'Funcional',    'Antiestático',                      'Requerido en ambientes industriales'),
('AT-06', 'Funcional',    'Impermeable / DWR',                 'Aplica a softshell y cortavientos'),
('AT-07', 'Funcional',    'Termorregulador / PCM',             'Indicar rango temperatura en ficha'),
('AT-08', 'Acabado',      'Suavizado Enzimático',              'Proceso post-confección, afecta gramaje final'),
('AT-09', 'Acabado',      'Sanforizado / Pre-Lavado',          'Control de encogimiento en orden de compra'),
('AT-10', 'Acabado',      'Pilling Reducido',                  'Norma Martindale mínima 5000 ciclos'),
('AT-11', 'Acabado',      'Easy Care / Anti-Arrugas',          'Indicar instrucción lavado en etiqueta'),
('AT-12', 'Sustentable',  'GOTS Certified (Orgánico)',         'Código certificación en PO de compra'),
('AT-13', 'Sustentable',  'Reciclado (GRS Certified)',         'Trazabilidad requerida desde proveedor');

-- -------------------------------------------------------------
-- CATEGORIA
-- -------------------------------------------------------------
INSERT INTO categoria (codigo_categoria, nombre_categoria) VALUES
('CAT-TEL', 'Telas'),
('CAT-ACC', 'Accesorios'),
('CAT-PRE', 'Prendas Listas');

-- -------------------------------------------------------------
-- SUBCATEGORIA
-- -------------------------------------------------------------
INSERT INTO subcategoria (codigo_subcategoria, nombre_subcategoria, id_categoria) VALUES
('SUB-TEL-PTO',  'Telas de Punto',        (SELECT id_categoria FROM categoria WHERE codigo_categoria = 'CAT-TEL')),
('SUB-TEL-PLA',  'Telas Planas',          (SELECT id_categoria FROM categoria WHERE codigo_categoria = 'CAT-TEL')),
('SUB-TEL-TEC',  'Telas Técnicas',        (SELECT id_categoria FROM categoria WHERE codigo_categoria = 'CAT-TEL')),
('SUB-ACC-CAL',  'Calzado',               (SELECT id_categoria FROM categoria WHERE codigo_categoria = 'CAT-ACC')),
('SUB-ACC-MED',  'Calcetines / Medias',   (SELECT id_categoria FROM categoria WHERE codigo_categoria = 'CAT-ACC')),
('SUB-ACC-PLA',  'Plantillas',            (SELECT id_categoria FROM categoria WHERE codigo_categoria = 'CAT-ACC')),
('SUB-ACC-OTR',  'Otros Accesorios',      (SELECT id_categoria FROM categoria WHERE codigo_categoria = 'CAT-ACC')),
('SUB-PRE-POL',  'Poleras',               (SELECT id_categoria FROM categoria WHERE codigo_categoria = 'CAT-PRE')),
('SUB-PRE-BUZ',  'Buzos / Sudaderas',     (SELECT id_categoria FROM categoria WHERE codigo_categoria = 'CAT-PRE')),
('SUB-PRE-PAN',  'Pantalones',            (SELECT id_categoria FROM categoria WHERE codigo_categoria = 'CAT-PRE')),
('SUB-PRE-JKT',  'Chaquetas / Parkas',    (SELECT id_categoria FROM categoria WHERE codigo_categoria = 'CAT-PRE')),
('SUB-PRE-CAM',  'Camisas',               (SELECT id_categoria FROM categoria WHERE codigo_categoria = 'CAT-PRE')),
('SUB-PRE-OTR',  'Otras Prendas',         (SELECT id_categoria FROM categoria WHERE codigo_categoria = 'CAT-PRE'));

-- =============================================================
-- ARTÍCULOS – tabla principal + satélites
-- =============================================================

-- -------------------------------------------------------------
-- ARTICULO (tipo TELA)
-- -------------------------------------------------------------
INSERT INTO articulo (codigo_articulo, nombre_articulo, descripcion_articulo, tipo_articulo,
                      procedencia, lotes, seriales, compras, activo,
                      id_categoria, id_subcategoria, id_unidad_medida)
VALUES
('TEL-JER-001', 'Jersey 160g Blanco',          'Jersey 100% algodón 160 g/m² color blanco',          'TELA', 'N', FALSE, FALSE, TRUE, TRUE,
 (SELECT id_categoria    FROM categoria    WHERE codigo_categoria    = 'CAT-TEL'),
 (SELECT id_subcategoria FROM subcategoria WHERE codigo_subcategoria = 'SUB-TEL-PTO'),
 (SELECT id_unidad_medida FROM unidad_medida WHERE abreviatura = 'M')),

('TEL-RIB-001', 'Rib 220g Negro',              'Rib 95% algodón 5% elastano 220 g/m² negro',         'TELA', 'N', FALSE, FALSE, TRUE, TRUE,
 (SELECT id_categoria    FROM categoria    WHERE codigo_categoria    = 'CAT-TEL'),
 (SELECT id_subcategoria FROM subcategoria WHERE codigo_subcategoria = 'SUB-TEL-PTO'),
 (SELECT id_unidad_medida FROM unidad_medida WHERE abreviatura = 'M')),

('TEL-FLE-001', 'Fleece 300g Gris Melange',    'Fleece 60% algodón 40% poliéster 300 g/m²',          'TELA', 'N', FALSE, FALSE, TRUE, TRUE,
 (SELECT id_categoria    FROM categoria    WHERE codigo_categoria    = 'CAT-TEL'),
 (SELECT id_subcategoria FROM subcategoria WHERE codigo_subcategoria = 'SUB-TEL-PTO'),
 (SELECT id_unidad_medida FROM unidad_medida WHERE abreviatura = 'M')),

('TEL-DEN-001', 'Denim Sarga 380g Indigo',     'Denim 100% algodón 380 g/m² indigo',                 'TELA', 'I', FALSE, FALSE, TRUE, TRUE,
 (SELECT id_categoria    FROM categoria    WHERE codigo_categoria    = 'CAT-TEL'),
 (SELECT id_subcategoria FROM subcategoria WHERE codigo_subcategoria = 'SUB-TEL-PLA'),
 (SELECT id_unidad_medida FROM unidad_medida WHERE abreviatura = 'M')),

('TEL-MES-001', 'Malla Deportiva 160g Royal',  'Malla 95% poliéster 5% elastano 160 g/m² azul royal','TELA', 'I', FALSE, FALSE, TRUE, TRUE,
 (SELECT id_categoria    FROM categoria    WHERE codigo_categoria    = 'CAT-TEL'),
 (SELECT id_subcategoria FROM subcategoria WHERE codigo_subcategoria = 'SUB-TEL-TEC'),
 (SELECT id_unidad_medida FROM unidad_medida WHERE abreviatura = 'M'));

-- -------------------------------------------------------------
-- ARTICULO_TELA (satélite – PK compartida con articulo)
-- -------------------------------------------------------------
INSERT INTO articulo_tela (id_articulo, id_familia_tela, id_clasificacion_tecnica, id_composicion,
                            id_gramaje, abreviaturas_historicas, uso_tipico, observacion_proveedor)
VALUES
((SELECT id_articulo FROM articulo WHERE codigo_articulo = 'TEL-JER-001'),
 (SELECT id_familia_tela FROM familia_tela WHERE codigo_familia = 'FT-01'),
 (SELECT id_clasificacion_tecnica FROM clasificacion_tecnica WHERE nombre_clasificacion = 'Tejido de Punto'),
 (SELECT id_composicion FROM composicion WHERE codigo_composicion = 'CO-01'),
 (SELECT id_gramaje FROM gramaje_tela WHERE codigo_gramaje = 'GR-03'),
 'JER160-B', 'Poleras básicas y escolares', NULL),

((SELECT id_articulo FROM articulo WHERE codigo_articulo = 'TEL-RIB-001'),
 (SELECT id_familia_tela FROM familia_tela WHERE codigo_familia = 'FT-02'),
 (SELECT id_clasificacion_tecnica FROM clasificacion_tecnica WHERE nombre_clasificacion = 'Tejido de Punto'),
 (SELECT id_composicion FROM composicion WHERE codigo_composicion = 'CO-05'),
 (SELECT id_gramaje FROM gramaje_tela WHERE codigo_gramaje = 'GR-06'),
 'RIB220-N', 'Cuellos, puños y uniformes', NULL),

((SELECT id_articulo FROM articulo WHERE codigo_articulo = 'TEL-FLE-001'),
 (SELECT id_familia_tela FROM familia_tela WHERE codigo_familia = 'FT-04'),
 (SELECT id_clasificacion_tecnica FROM clasificacion_tecnica WHERE nombre_clasificacion = 'Tejido de Punto'),
 (SELECT id_composicion FROM composicion WHERE codigo_composicion = 'CO-07'),
 (SELECT id_gramaje FROM gramaje_tela WHERE codigo_gramaje = 'GR-10'),
 'FLE300-GM', 'Buzos y chaquetas escolares', 'Controlar encogimiento primer lavado'),

((SELECT id_articulo FROM articulo WHERE codigo_articulo = 'TEL-DEN-001'),
 (SELECT id_familia_tela FROM familia_tela WHERE codigo_familia = 'FT-10'),
 (SELECT id_clasificacion_tecnica FROM clasificacion_tecnica WHERE nombre_clasificacion = 'Tejido Plano'),
 (SELECT id_composicion FROM composicion WHERE codigo_composicion = 'CO-01'),
 (SELECT id_gramaje FROM gramaje_tela WHERE codigo_gramaje = 'GR-13'),
 'DEN380-IND', 'Pantalones y faldas', 'Sanforizado obligatorio'),

((SELECT id_articulo FROM articulo WHERE codigo_articulo = 'TEL-MES-001'),
 (SELECT id_familia_tela FROM familia_tela WHERE codigo_familia = 'FT-15'),
 (SELECT id_clasificacion_tecnica FROM clasificacion_tecnica WHERE nombre_clasificacion = 'Técnico / Funcional'),
 (SELECT id_composicion FROM composicion WHERE codigo_composicion = 'CO-06'),
 (SELECT id_gramaje FROM gramaje_tela WHERE codigo_gramaje = 'GR-03'),
 'MES160-ROY', 'Camisetas deportivas y uniformes', 'Verificar solidez del color en sublimación');

-- -------------------------------------------------------------
-- TELA_COLOR  (N:M articulo_tela <-> color_tela)
-- -------------------------------------------------------------
INSERT INTO tela_color (id_articulo, id_color) VALUES
((SELECT id_articulo FROM articulo WHERE codigo_articulo = 'TEL-JER-001'), (SELECT id_color FROM color_tela WHERE codigo_color = 'COL-01')),
((SELECT id_articulo FROM articulo WHERE codigo_articulo = 'TEL-JER-001'), (SELECT id_color FROM color_tela WHERE codigo_color = 'COL-02')),
((SELECT id_articulo FROM articulo WHERE codigo_articulo = 'TEL-JER-001'), (SELECT id_color FROM color_tela WHERE codigo_color = 'COL-16')),
((SELECT id_articulo FROM articulo WHERE codigo_articulo = 'TEL-RIB-001'), (SELECT id_color FROM color_tela WHERE codigo_color = 'COL-02')),
((SELECT id_articulo FROM articulo WHERE codigo_articulo = 'TEL-RIB-001'), (SELECT id_color FROM color_tela WHERE codigo_color = 'COL-05')),
((SELECT id_articulo FROM articulo WHERE codigo_articulo = 'TEL-FLE-001'), (SELECT id_color FROM color_tela WHERE codigo_color = 'COL-03')),
((SELECT id_articulo FROM articulo WHERE codigo_articulo = 'TEL-FLE-001'), (SELECT id_color FROM color_tela WHERE codigo_color = 'COL-21')),
((SELECT id_articulo FROM articulo WHERE codigo_articulo = 'TEL-DEN-001'), (SELECT id_color FROM color_tela WHERE codigo_color = 'COL-05')),
((SELECT id_articulo FROM articulo WHERE codigo_articulo = 'TEL-MES-001'), (SELECT id_color FROM color_tela WHERE codigo_color = 'COL-06')),
((SELECT id_articulo FROM articulo WHERE codigo_articulo = 'TEL-MES-001'), (SELECT id_color FROM color_tela WHERE codigo_color = 'COL-01'));

-- -------------------------------------------------------------
-- TELA_ATRIBUTO_TECNICO  (N:M articulo_tela <-> atributo_tecnico)
-- -------------------------------------------------------------
INSERT INTO tela_atributo_tecnico (id_articulo, id_atributo) VALUES
((SELECT id_articulo FROM articulo WHERE codigo_articulo = 'TEL-JER-001'), (SELECT id_atributo FROM atributo_tecnico WHERE codigo_atributo = 'AT-09')),
((SELECT id_articulo FROM articulo WHERE codigo_articulo = 'TEL-RIB-001'), (SELECT id_atributo FROM atributo_tecnico WHERE codigo_atributo = 'AT-09')),
((SELECT id_articulo FROM articulo WHERE codigo_articulo = 'TEL-FLE-001'), (SELECT id_atributo FROM atributo_tecnico WHERE codigo_atributo = 'AT-10')),
((SELECT id_articulo FROM articulo WHERE codigo_articulo = 'TEL-MES-001'), (SELECT id_atributo FROM atributo_tecnico WHERE codigo_atributo = 'AT-02')),
((SELECT id_articulo FROM articulo WHERE codigo_articulo = 'TEL-MES-001'), (SELECT id_atributo FROM atributo_tecnico WHERE codigo_atributo = 'AT-03'));

-- -------------------------------------------------------------
-- ARTICULO (tipo PRENDA_LISTA)
-- -------------------------------------------------------------
INSERT INTO articulo (codigo_articulo, nombre_articulo, descripcion_articulo, tipo_articulo,
                      procedencia, lotes, seriales, compras, activo,
                      id_categoria, id_subcategoria, id_unidad_medida)
VALUES
('PRE-POL-001', 'Polera Básica Blanca Talla M',   'Polera unisex 100% algodón manga corta blanca talla M',  'PRENDA_LISTA', 'N', FALSE, FALSE, TRUE, TRUE,
 (SELECT id_categoria    FROM categoria    WHERE codigo_categoria    = 'CAT-PRE'),
 (SELECT id_subcategoria FROM subcategoria WHERE codigo_subcategoria = 'SUB-PRE-POL'),
 (SELECT id_unidad_medida FROM unidad_medida WHERE abreviatura = 'UN')),

('PRE-BUZ-001', 'Buzo Canguro Gris Melange L',    'Buzo canguro 60/40 con capucha talla L',                 'PRENDA_LISTA', 'N', FALSE, FALSE, TRUE, TRUE,
 (SELECT id_categoria    FROM categoria    WHERE codigo_categoria    = 'CAT-PRE'),
 (SELECT id_subcategoria FROM subcategoria WHERE codigo_subcategoria = 'SUB-PRE-BUZ'),
 (SELECT id_unidad_medida FROM unidad_medida WHERE abreviatura = 'UN')),

('PRE-JKT-001', 'Chaqueta Softshell Navy XL',     'Chaqueta impermeable DWR 3 capas talla XL',              'PRENDA_LISTA', 'I', FALSE, FALSE, TRUE, TRUE,
 (SELECT id_categoria    FROM categoria    WHERE codigo_categoria    = 'CAT-PRE'),
 (SELECT id_subcategoria FROM subcategoria WHERE codigo_subcategoria = 'SUB-PRE-JKT'),
 (SELECT id_unidad_medida FROM unidad_medida WHERE abreviatura = 'UN')),

('PRE-CAM-001', 'Camisa Oxford Manga Larga Celeste M', 'Camisa corporativa oxford 100% algodón talla M',    'PRENDA_LISTA', 'I', FALSE, FALSE, TRUE, TRUE,
 (SELECT id_categoria    FROM categoria    WHERE codigo_categoria    = 'CAT-PRE'),
 (SELECT id_subcategoria FROM subcategoria WHERE codigo_subcategoria = 'SUB-PRE-CAM'),
 (SELECT id_unidad_medida FROM unidad_medida WHERE abreviatura = 'UN'));

-- -------------------------------------------------------------
-- ARTICULO_PRENDA (satélite)
-- -------------------------------------------------------------
INSERT INTO articulo_prenda (id_articulo, marca, tallas_disponibles, proveedor, codigo_proveedor,
                              requiere_logo_cliente, tiene_estampado, ubicacion_logo)
VALUES
((SELECT id_articulo FROM articulo WHERE codigo_articulo = 'PRE-POL-001'),
 'Antuan Basic', 'XS,S,M,L,XL,XXL', 'Textil Sur Ltda.',  'TS-POL-001', FALSE, FALSE, NULL),

((SELECT id_articulo FROM articulo WHERE codigo_articulo = 'PRE-BUZ-001'),
 'Antuan Sport', 'S,M,L,XL,XXL',    'Textil Sur Ltda.',  'TS-BUZ-001', TRUE,  FALSE, 'Pecho izquierdo'),

((SELECT id_articulo FROM articulo WHERE codigo_articulo = 'PRE-JKT-001'),
 'Antuan Pro',   'M,L,XL,XXL',      'ImportChile S.A.',  'IC-JKT-001', TRUE,  TRUE,  'Pecho izquierdo / Espalda'),

((SELECT id_articulo FROM articulo WHERE codigo_articulo = 'PRE-CAM-001'),
 'Antuan Corp',  'S,M,L,XL',        'ImportChile S.A.',  'IC-CAM-001', TRUE,  FALSE, 'Pecho izquierdo');

-- -------------------------------------------------------------
-- ARTICULO (tipo ACCESORIO)
-- -------------------------------------------------------------
INSERT INTO articulo (codigo_articulo, nombre_articulo, descripcion_articulo, tipo_articulo,
                      procedencia, lotes, seriales, compras, activo,
                      id_categoria, id_subcategoria, id_unidad_medida)
VALUES
('ACC-MED-001', 'Calcetín Deportivo Blanco Talla 40-43', 'Calcetín tobillero 80% algodón transpirable',       'ACCESORIO', 'N', FALSE, FALSE, TRUE, TRUE,
 (SELECT id_categoria    FROM categoria    WHERE codigo_categoria    = 'CAT-ACC'),
 (SELECT id_subcategoria FROM subcategoria WHERE codigo_subcategoria = 'SUB-ACC-MED'),
 (SELECT id_unidad_medida FROM unidad_medida WHERE abreviatura = 'PAR')),

('ACC-CAL-001', 'Zapatilla Running Azul/Blanca Talla 42', 'Zapatilla técnica EVA sole talla 42',               'ACCESORIO', 'I', FALSE, TRUE,  TRUE, TRUE,
 (SELECT id_categoria    FROM categoria    WHERE codigo_categoria    = 'CAT-ACC'),
 (SELECT id_subcategoria FROM subcategoria WHERE codigo_subcategoria = 'SUB-ACC-CAL'),
 (SELECT id_unidad_medida FROM unidad_medida WHERE abreviatura = 'PAR')),

('ACC-PLA-001', 'Plantilla Anatómica Talla 41-42',        'Plantilla memory foam 5mm talla 41-42',             'ACCESORIO', 'I', FALSE, FALSE, TRUE, TRUE,
 (SELECT id_categoria    FROM categoria    WHERE codigo_categoria    = 'CAT-ACC'),
 (SELECT id_subcategoria FROM subcategoria WHERE codigo_subcategoria = 'SUB-ACC-PLA'),
 (SELECT id_unidad_medida FROM unidad_medida WHERE abreviatura = 'PAR')),

('ACC-OTR-001', 'Gorro Corporativo Navy',                 'Gorro estructurado 6 paneles 100% algodón',         'ACCESORIO', 'N', FALSE, FALSE, TRUE, TRUE,
 (SELECT id_categoria    FROM categoria    WHERE codigo_categoria    = 'CAT-ACC'),
 (SELECT id_subcategoria FROM subcategoria WHERE codigo_subcategoria = 'SUB-ACC-OTR'),
 (SELECT id_unidad_medida FROM unidad_medida WHERE abreviatura = 'UN'));

-- -------------------------------------------------------------
-- ARTICULO_ACCESORIO (satélite)
-- -------------------------------------------------------------
INSERT INTO articulo_accesorio (id_articulo, subtipo_accesorio, tallas_disponibles,
                                 proveedor, codigo_proveedor, requiere_logo_cliente)
VALUES
((SELECT id_articulo FROM articulo WHERE codigo_articulo = 'ACC-MED-001'),
 'MEDIA',    '36-39,40-43,44-46', 'Calcetines SA',    'CS-MED-001', FALSE),

((SELECT id_articulo FROM articulo WHERE codigo_articulo = 'ACC-CAL-001'),
 'CALZADO',  '38,39,40,41,42,43', 'ImportChile S.A.', 'IC-CAL-001', TRUE),

((SELECT id_articulo FROM articulo WHERE codigo_articulo = 'ACC-PLA-001'),
 'PLANTILLA', '39-40,41-42,43-44','ImportChile S.A.', 'IC-PLA-001', FALSE),

((SELECT id_articulo FROM articulo WHERE codigo_articulo = 'ACC-OTR-001'),
 'GORRO',    'UNICO',             'Textil Sur Ltda.', 'TS-GOR-001', TRUE);

-- -------------------------------------------------------------
-- PRECIO (CLP y USD para cada artículo)
-- -------------------------------------------------------------
INSERT INTO precio (id_articulo, id_moneda, tipo_precio, valor) VALUES
-- Telas (precio por metro)
((SELECT id_articulo FROM articulo WHERE codigo_articulo = 'TEL-JER-001'), (SELECT id_moneda FROM moneda WHERE codigo_moneda = 'CLP'), 'COMPRA',  3200.00),
((SELECT id_articulo FROM articulo WHERE codigo_articulo = 'TEL-JER-001'), (SELECT id_moneda FROM moneda WHERE codigo_moneda = 'USD'), 'COMPRA',     3.50),
((SELECT id_articulo FROM articulo WHERE codigo_articulo = 'TEL-RIB-001'), (SELECT id_moneda FROM moneda WHERE codigo_moneda = 'CLP'), 'COMPRA',  3800.00),
((SELECT id_articulo FROM articulo WHERE codigo_articulo = 'TEL-FLE-001'), (SELECT id_moneda FROM moneda WHERE codigo_moneda = 'CLP'), 'COMPRA',  5200.00),
((SELECT id_articulo FROM articulo WHERE codigo_articulo = 'TEL-DEN-001'), (SELECT id_moneda FROM moneda WHERE codigo_moneda = 'CLP'), 'COMPRA',  6400.00),
((SELECT id_articulo FROM articulo WHERE codigo_articulo = 'TEL-DEN-001'), (SELECT id_moneda FROM moneda WHERE codigo_moneda = 'USD'), 'COMPRA',     7.20),
((SELECT id_articulo FROM articulo WHERE codigo_articulo = 'TEL-MES-001'), (SELECT id_moneda FROM moneda WHERE codigo_moneda = 'CLP'), 'COMPRA',  4100.00),
-- Prendas listas (precio por unidad)
((SELECT id_articulo FROM articulo WHERE codigo_articulo = 'PRE-POL-001'), (SELECT id_moneda FROM moneda WHERE codigo_moneda = 'CLP'), 'COMPRA',  4900.00),
((SELECT id_articulo FROM articulo WHERE codigo_articulo = 'PRE-POL-001'), (SELECT id_moneda FROM moneda WHERE codigo_moneda = 'CLP'), 'VENTA',   7200.00),
((SELECT id_articulo FROM articulo WHERE codigo_articulo = 'PRE-BUZ-001'), (SELECT id_moneda FROM moneda WHERE codigo_moneda = 'CLP'), 'COMPRA', 12500.00),
((SELECT id_articulo FROM articulo WHERE codigo_articulo = 'PRE-BUZ-001'), (SELECT id_moneda FROM moneda WHERE codigo_moneda = 'CLP'), 'VENTA',  18900.00),
((SELECT id_articulo FROM articulo WHERE codigo_articulo = 'PRE-JKT-001'), (SELECT id_moneda FROM moneda WHERE codigo_moneda = 'USD'), 'COMPRA',    24.50),
((SELECT id_articulo FROM articulo WHERE codigo_articulo = 'PRE-JKT-001'), (SELECT id_moneda FROM moneda WHERE codigo_moneda = 'CLP'), 'VENTA',  39900.00),
((SELECT id_articulo FROM articulo WHERE codigo_articulo = 'PRE-CAM-001'), (SELECT id_moneda FROM moneda WHERE codigo_moneda = 'USD'), 'COMPRA',    11.00),
((SELECT id_articulo FROM articulo WHERE codigo_articulo = 'PRE-CAM-001'), (SELECT id_moneda FROM moneda WHERE codigo_moneda = 'CLP'), 'VENTA',  19900.00),
-- Accesorios (precio por par/unidad)
((SELECT id_articulo FROM articulo WHERE codigo_articulo = 'ACC-MED-001'), (SELECT id_moneda FROM moneda WHERE codigo_moneda = 'CLP'), 'COMPRA',  1200.00),
((SELECT id_articulo FROM articulo WHERE codigo_articulo = 'ACC-MED-001'), (SELECT id_moneda FROM moneda WHERE codigo_moneda = 'CLP'), 'VENTA',   2500.00),
((SELECT id_articulo FROM articulo WHERE codigo_articulo = 'ACC-CAL-001'), (SELECT id_moneda FROM moneda WHERE codigo_moneda = 'USD'), 'COMPRA',    18.00),
((SELECT id_articulo FROM articulo WHERE codigo_articulo = 'ACC-CAL-001'), (SELECT id_moneda FROM moneda WHERE codigo_moneda = 'CLP'), 'VENTA',  49900.00),
((SELECT id_articulo FROM articulo WHERE codigo_articulo = 'ACC-PLA-001'), (SELECT id_moneda FROM moneda WHERE codigo_moneda = 'USD'), 'COMPRA',     5.50),
((SELECT id_articulo FROM articulo WHERE codigo_articulo = 'ACC-PLA-001'), (SELECT id_moneda FROM moneda WHERE codigo_moneda = 'CLP'), 'VENTA',   8900.00),
((SELECT id_articulo FROM articulo WHERE codigo_articulo = 'ACC-OTR-001'), (SELECT id_moneda FROM moneda WHERE codigo_moneda = 'CLP'), 'COMPRA',  3200.00),
((SELECT id_articulo FROM articulo WHERE codigo_articulo = 'ACC-OTR-001'), (SELECT id_moneda FROM moneda WHERE codigo_moneda = 'CLP'), 'VENTA',   6500.00);
