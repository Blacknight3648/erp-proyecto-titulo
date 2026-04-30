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
MERGE INTO usuarios (id_usuario, run, nombre, apellidos, email, password, telefono, fecha_nacimiento, direccion, region, comuna, enabled)
    KEY (id_usuario)
    VALUES
    (1, '15342981-2', 'Carlos', 'Iturrieta Méndez', 'c.iturrieta@empresa.cl', '$2a$10$xyz1234567890123456789', '+56988223344', '1985-05-20', 'Huérfanos 1160', 'Metropolitana', 'Santiago', true),
    (2, '17589432-K', 'Valentina', 'Lagos Espinoza', 'v.lagos@empresa.cl', '$2a$10$abc1234567890123456789', '+56977445566', '1991-11-08', 'Av. Libertad 120', 'Valparaíso', 'Viña del Mar', true);

-- ============================================================
-- 4. VENDEDORES
-- ============================================================
MERGE INTO vendedores (id_vendedor, id_usuario, codigo_vendedor, activo, creado_en, actualizado_en)
    KEY (id_vendedor)
    VALUES
    (1, 1, 'V-2024-001', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (2, 2, 'V-2024-002', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

-- ============================================================
-- 5. CLIENTES (RUTs de empresas reales y teléfonos fijos +562...)
-- ============================================================
MERGE INTO clientes (cliente_id, nombre_cliente, apellido_cliente, correo_cliente, telefono_cliente, run_cliente, activo)
    KEY (cliente_id)
    VALUES
    (1, 'HITES S.A.', 'RETAIL', 'contacto.hites@hites.cl', '+56227275000', '96947020-9', true),
    (2, 'LABORATORIO MEDCELL', 'LTDA', 'compras@medcell.cl', '+56224396000', '96706320-7', true),
    (3, 'GEODIS WILSON', 'CHILE S.A.', 'info.chile@geodis.com', '+56223816500', '79699520-3', true);

-- ============================================================
-- 6. PROVEEDORES (RUTs de empresas reales y teléfonos fijos +562...)
-- ============================================================
MERGE INTO proveedores (proveedor_id, nombre_proveedor, contacto_proveedor, telefono_proveedor, rut_proveedor, direccion_proveedor, activo, creado_en, actualizado_en)
    KEY (proveedor_id)
    VALUES
    (1, 'PARQUE ARAUCO S.A.', 'Ricardo Muñoz', '+56222990503', '99581960-0', 'Av. Kennedy 5413, Las Condes', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (2, 'C.C. LOS HEROES', 'Carolina Díaz', '+56223927000', '70016330-K', 'Holanda 64, Providencia', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (3, 'MEDIPHARM LTDA.', 'Felipe Torres', '+56223700000', '96599510-2', 'Lo Boza 110, Quilicura', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
    (4, 'SODIMAC S.A.', 'Patricio Leiva', '+56227381000', '96792430-K', 'Av. Presidente Eduardo Frei M. 3092', true, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);

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
-- 8. REINICIO DE SECUENCIAS (Unificado al final)
-- ============================================================
ALTER TABLE areas ALTER COLUMN id_area RESTART WITH 50;
ALTER TABLE roles ALTER COLUMN id_role RESTART WITH 100;
ALTER TABLE usuarios ALTER COLUMN id_usuario RESTART WITH 100;
ALTER TABLE clientes ALTER COLUMN cliente_id RESTART WITH 100;
ALTER TABLE vendedores ALTER COLUMN id_vendedor RESTART WITH 200;
ALTER TABLE proveedores ALTER COLUMN proveedor_id RESTART WITH 100;
ALTER TABLE solicitudes_costos ALTER COLUMN idscos RESTART WITH 2000;
ALTER TABLE produccion_costeos ALTER COLUMN id_costeo RESTART WITH 2000;
ALTER TABLE produccion_costeo_items ALTER COLUMN id_costeo_item RESTART WITH 5000;
ALTER TABLE scos_telas ALTER COLUMN idscostela RESTART WITH 2000;
ALTER TABLE scos_logotipos ALTER COLUMN id RESTART WITH 2000;
ALTER TABLE evaluaciones_negocio ALTER COLUMN idevn RESTART WITH 1000;