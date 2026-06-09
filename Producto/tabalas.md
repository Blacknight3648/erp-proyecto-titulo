# Esquema de Base de Datos — ERP ANTUAN SA

> Documento generado para construcción del MER. Incluye tablas, atributos, relaciones y cardinalidad.

---

## MÓDULO 1: USUARIOS Y ACCESO

### usuarios
| Columna | Tipo | Restricción |
|---|---|---|
| id_usuario | BIGINT | PK, AUTO |
| run | VARCHAR(12) | NOT NULL, UNIQUE |
| nombre | VARCHAR(50) | NOT NULL |
| apellidos | VARCHAR(50) | NOT NULL |
| email | VARCHAR(80) | NOT NULL, UNIQUE |
| password | VARCHAR(200) | NOT NULL |
| telefono | VARCHAR(50) | NOT NULL |
| enabled | BOOLEAN | DEFAULT true |
| creado_en | DATETIME | NOT NULL |
| actualizado_en | DATETIME | NOT NULL |
| activo | BOOLEAN | NOT NULL, DEFAULT true |

### areas
| Columna | Tipo | Restricción |
|---|---|---|
| id_area | BIGINT | PK, AUTO |
| nombre_area | VARCHAR(100) | NOT NULL, UNIQUE |
| descripcion | VARCHAR(255) | |

### roles
| Columna | Tipo | Restricción |
|---|---|---|
| id_role | BIGINT | PK, AUTO |
| nombre | VARCHAR(50) | NOT NULL, UNIQUE |
| descripcion | VARCHAR(255) | |
| area_id | BIGINT | NOT NULL, FK → areas(id_area) |

### permisos
| Columna | Tipo | Restricción |
|---|---|---|
| id | BIGINT | PK, AUTO |
| nombre | VARCHAR(255) | NOT NULL, UNIQUE |
| descripcion | VARCHAR(255) | |
| modulo | VARCHAR(255) | NOT NULL |

### usuarios_roles *(tabla junction)*
| Columna | Tipo | Restricción |
|---|---|---|
| user_id | BIGINT | FK → usuarios(id_usuario) ON DELETE CASCADE |
| role_id | BIGINT | FK → roles(id_role) ON DELETE CASCADE |
| — | — | UNIQUE(user_id, role_id) |

### usuarios_areas *(tabla junction)*
| Columna | Tipo | Restricción |
|---|---|---|
| user_id | BIGINT | FK → usuarios(id_usuario) ON DELETE CASCADE |
| area_id | BIGINT | FK → areas(id_area) ON DELETE CASCADE |
| — | — | UNIQUE(user_id, area_id) |

### rol_permisos *(tabla junction)*
| Columna | Tipo | Restricción |
|---|---|---|
| rol_id | BIGINT | FK → roles(id_role) ON DELETE CASCADE |
| permiso_id | BIGINT | FK → permisos(id) ON DELETE CASCADE |

### vendedores
| Columna | Tipo | Restricción |
|---|---|---|
| id_vendedor | BIGINT | PK, AUTO |
| id_usuario | BIGINT | NOT NULL, UNIQUE, FK → usuarios(id_usuario) ON DELETE CASCADE |
| codigo_vendedor | VARCHAR(20) | NOT NULL |
| creado_en | DATETIME | NOT NULL |
| actualizado_en | DATETIME | NOT NULL |
| activo | BOOLEAN | NOT NULL |

---

## MÓDULO 2: GEOGRAFÍA

### pais
| Columna | Tipo | Restricción |
|---|---|---|
| pais_id | BIGINT | PK, AUTO |
| nombre_pais | VARCHAR(100) | NOT NULL, UNIQUE |

### region
| Columna | Tipo | Restricción |
|---|---|---|
| region_id | BIGINT | PK, AUTO |
| nombre_region | VARCHAR(100) | NOT NULL, UNIQUE |
| pais_id | BIGINT | NOT NULL, FK → pais(pais_id) |
| creado_en | DATETIME | NOT NULL |
| actualizado_en | DATETIME | NOT NULL |
| activo | BOOLEAN | NOT NULL |

### comuna
| Columna | Tipo | Restricción |
|---|---|---|
| comuna_id | BIGINT | PK, AUTO |
| nombre_comuna | VARCHAR(100) | NOT NULL |
| region_id | BIGINT | NOT NULL, FK → region(region_id) |
| creado_en | DATETIME | NOT NULL |
| actualizado_en | DATETIME | NOT NULL |
| activo | BOOLEAN | NOT NULL |

---

## MÓDULO 3: CLIENTES Y PROVEEDORES

### rubros
| Columna | Tipo | Restricción |
|---|---|---|
| rubro_id | BIGINT | PK, AUTO |
| nombre_rubro | VARCHAR(255) | |
| descripcion_rubro | VARCHAR(255) | |

### giros
| Columna | Tipo | Restricción |
|---|---|---|
| giro_id | BIGINT | PK, AUTO |
| codigo_sii | VARCHAR(255) | |
| nombre_giro | VARCHAR(255) | |
| descripcion_giro | VARCHAR(255) | |
| rubro_id | BIGINT | FK → rubros(rubro_id) |

### clientes
| Columna | Tipo | Restricción |
|---|---|---|
| cliente_id | BIGINT | PK, AUTO |
| razon_social | VARCHAR(255) | NOT NULL |
| run_cliente | VARCHAR(255) | NOT NULL, UNIQUE |
| sigla | VARCHAR(255) | |
| activo | BOOLEAN | NOT NULL |
| fk_giro | BIGINT | FK → giros(giro_id) |

### proveedores
| Columna | Tipo | Restricción |
|---|---|---|
| proveedor_id | BIGINT | PK, AUTO |
| razon_social_proveedor | VARCHAR(255) | NOT NULL |
| run_proveedor | VARCHAR(12) | NOT NULL, UNIQUE |
| sigla | VARCHAR(100) | |
| horario_atencion | VARCHAR(100) | |
| tipo_proveedor | VARCHAR(30) | |
| fk_provee_giro | BIGINT | FK → giros(giro_id) |
| creado_en | DATETIME | NOT NULL |
| actualizado_en | DATETIME | NOT NULL |
| activo | BOOLEAN | NOT NULL |

---

## MÓDULO 4: DIRECCIONES Y CONTACTOS

### tipo_direccion
| Columna | Tipo | Restricción |
|---|---|---|
| tipo_direccion_id | INTEGER | PK, AUTO |
| descripcion | VARCHAR(200) | NOT NULL |

### direccion
| Columna | Tipo | Restricción |
|---|---|---|
| direccion_id | BIGINT | PK, AUTO |
| calle | VARCHAR(150) | NOT NULL |
| numero | VARCHAR(20) | NOT NULL |
| depto | VARCHAR(20) | |
| tipo_direccion_id | INTEGER | NOT NULL, FK → tipo_direccion(tipo_direccion_id) |
| comuna_id | BIGINT | NOT NULL, FK → comuna(comuna_id) |
| fk_direccion | BIGINT | FK → clientes(cliente_id) ON DELETE CASCADE |
| fk_provee_direccion | BIGINT | FK → proveedores(proveedor_id) ON DELETE CASCADE |

### tipos_contacto
| Columna | Tipo | Restricción |
|---|---|---|
| tipo_contacto_id | BIGINT | PK, AUTO |
| descripcion_tipo_contacto | VARCHAR(100) | NOT NULL |

### contactos
| Columna | Tipo | Restricción |
|---|---|---|
| contacto_id | BIGINT | PK, AUTO |
| nombre_contacto | VARCHAR(100) | NOT NULL |
| telefono_contacto | VARCHAR(15) | |
| email_contacto | VARCHAR(100) | |
| tipo_contacto_id | BIGINT | NOT NULL, FK → tipos_contacto(tipo_contacto_id) |
| fk_contacto | BIGINT | FK → clientes(cliente_id) ON DELETE CASCADE |
| fk_provee_contacto | BIGINT | FK → proveedores(proveedor_id) ON DELETE CASCADE |

---

## MÓDULO 5: DATOS BANCARIOS

### banco
| Columna | Tipo | Restricción |
|---|---|---|
| banco_id | INTEGER | PK, AUTO |
| nombre_banco | VARCHAR(150) | NOT NULL |
| codigo_banco | VARCHAR(20) | NOT NULL, UNIQUE |

### tipo_cuenta_bancaria
| Columna | Tipo | Restricción |
|---|---|---|
| tipo_cuenta_id | INTEGER | PK, AUTO |
| denominacion_cuenta | VARCHAR(100) | NOT NULL, UNIQUE |

### dato_bancario
| Columna | Tipo | Restricción |
|---|---|---|
| dato_bancario_id | INTEGER | PK, AUTO |
| numero_cuenta | VARCHAR(50) | NOT NULL |
| banco_id | INTEGER | NOT NULL, FK → banco(banco_id) |
| tipo_cuenta_id | INTEGER | NOT NULL, FK → tipo_cuenta_bancaria(tipo_cuenta_id) |
| fk_provee_dato_bancario | BIGINT | FK → proveedores(proveedor_id) ON DELETE CASCADE |

---

## MÓDULO 6: ARTÍCULOS Y PRODUCTOS

### tipo_articulo
| Columna | Tipo | Restricción |
|---|---|---|
| id_tipo_articulo | INTEGER | PK |
| codigo | VARCHAR(20) | NOT NULL, UNIQUE |
| nombre | VARCHAR(60) | NOT NULL |

> Valores del enum: TELA, PRENDA_LISTA, ACCESORIO, LOGOTIPO, etc.

### articulo
| Columna | Tipo | Restricción |
|---|---|---|
| id_articulo | INTEGER | PK, AUTO |
| codigo_articulo | VARCHAR(20) | NOT NULL, UNIQUE |
| nombre_articulo | VARCHAR(100) | NOT NULL |
| descripcion_articulo | VARCHAR(200) | |
| codigo_barra | VARCHAR(50) | |
| id_tipo_articulo | INTEGER | NOT NULL, FK → tipo_articulo(id_tipo_articulo) |
| activo | BOOLEAN | NOT NULL, DEFAULT true |
| id_categoria_tela | INTEGER | FK → categoria_tela(id_categoria_tela) |
| id_subcategoria_tela | INTEGER | FK → subcategoria_tela(id_subcategoria_tela) |

### articulo_tela *(subtipo 1:1)*
| Columna | Tipo | Restricción |
|---|---|---|
| id_articulo | INTEGER | PK, FK → articulo(id_articulo) @MapsId |
| id_familia_tela | INTEGER | FK → familia_tela(id_familia_tela) |
| id_clasificacion_tecnica | INTEGER | FK → clasificacion_tecnica(id_clasificacion_tecnica) |
| id_composicion | INTEGER | FK → composicion(id_composicion) |
| id_gramaje | INTEGER | FK → gramaje_tela(id_gramaje) |
| abreviaturas_historicas | VARCHAR(60) | |
| uso_tipico | VARCHAR(60) | |
| observacion_proveedor | VARCHAR(200) | |

### articulo_prenda *(subtipo 1:1)*
| Columna | Tipo | Restricción |
|---|---|---|
| id_articulo | INTEGER | PK, FK → articulo(id_articulo) @MapsId |
| marca | VARCHAR(60) | |
| tallas_disponibles | VARCHAR(100) | |
| proveedor | VARCHAR(100) | |
| codigo_proveedor | VARCHAR(30) | |
| requiere_logo_cliente | BOOLEAN | NOT NULL, DEFAULT false |
| tiene_estampado | BOOLEAN | NOT NULL, DEFAULT false |
| ubicacion_logo | VARCHAR(60) | |

### articulo_accesorio *(subtipo 1:1)*
| Columna | Tipo | Restricción |
|---|---|---|
| id_articulo | INTEGER | PK, FK → articulo(id_articulo) @MapsId |
| subtipo_accesorio | VARCHAR(20) | |
| tallas_disponibles | VARCHAR(100) | |
| proveedor | VARCHAR(100) | |
| codigo_proveedor | VARCHAR(30) | |
| requiere_logo_cliente | BOOLEAN | NOT NULL, DEFAULT false |

### producto
| Columna | Tipo | Restricción |
|---|---|---|
| producto_id | BIGINT | PK, AUTO |
| codigo_producto | VARCHAR(50) | NOT NULL, UNIQUE |
| nombre | VARCHAR(150) | NOT NULL |
| descripcion | VARCHAR(300) | |
| genero | VARCHAR(30) | |
| color | VARCHAR(50) | |
| creado_en | DATETIME | NOT NULL |
| actualizado_en | DATETIME | NOT NULL |
| activo | BOOLEAN | NOT NULL |

---

## MÓDULO 7: CATÁLOGOS TEXTILES

### categoria_tela
| Columna | Tipo | Restricción |
|---|---|---|
| id_categoria_tela | INTEGER | PK, AUTO |
| codigo_categoria_tela | VARCHAR(10) | NOT NULL, UNIQUE |
| nombre_categoria_tela | VARCHAR(255) | NOT NULL, UNIQUE |

### subcategoria_tela
| Columna | Tipo | Restricción |
|---|---|---|
| id_subcategoria_tela | INTEGER | PK, AUTO |
| codigo_subcategoria_tela | VARCHAR(15) | NOT NULL, UNIQUE |
| nombre_subcategoria_tela | VARCHAR(255) | NOT NULL |
| id_categoria_tela | INTEGER | FK → categoria_tela(id_categoria_tela) |

### familia_tela
| Columna | Tipo | Restricción |
|---|---|---|
| id_familia_tela | INTEGER | PK, AUTO |
| codigo_familia | VARCHAR(10) | NOT NULL, UNIQUE |
| nombre_familia | VARCHAR(60) | NOT NULL |

### clasificacion_tecnica
| Columna | Tipo | Restricción |
|---|---|---|
| id_clasificacion_tecnica | INTEGER | PK, AUTO |
| nombre_clasificacion | VARCHAR(40) | NOT NULL, UNIQUE |

### composicion
| Columna | Tipo | Restricción |
|---|---|---|
| id_composicion | INTEGER | PK, AUTO |
| codigo_composicion | VARCHAR(10) | NOT NULL, UNIQUE |
| descripcion_composicion | VARCHAR(60) | NOT NULL |
| clasificacion | VARCHAR(20) | |
| uso_tipico | VARCHAR(60) | |

### gramaje_tela
| Columna | Tipo | Restricción |
|---|---|---|
| id_gramaje | INTEGER | PK, AUTO |
| codigo_gramaje | VARCHAR(10) | NOT NULL, UNIQUE |
| valor_gramos_m2 | DECIMAL(8,2) | NOT NULL |
| categoria_vestuario | VARCHAR(60) | |

### color_tela
| Columna | Tipo | Restricción |
|---|---|---|
| id_color | INTEGER | PK, AUTO |
| codigo_color | VARCHAR(10) | NOT NULL, UNIQUE |
| descripcion_color | VARCHAR(40) | NOT NULL |
| es_pantone | BOOLEAN | NOT NULL, DEFAULT false |

### atributo_tecnico
| Columna | Tipo | Restricción |
|---|---|---|
| id_atributo | INTEGER | PK, AUTO |
| codigo_atributo | VARCHAR(10) | NOT NULL, UNIQUE |
| clasificacion | VARCHAR(20) | |
| descripcion_tecnica | VARCHAR(60) | NOT NULL |
| impacto_erp | VARCHAR(100) | |

### tela_color *(tabla junction)*
| Columna | Tipo | Restricción |
|---|---|---|
| id_articulo | INTEGER | FK → articulo_tela(id_articulo) |
| id_color | INTEGER | FK → color_tela(id_color) |

### tela_atributo_tecnico *(tabla junction)*
| Columna | Tipo | Restricción |
|---|---|---|
| id_articulo | INTEGER | FK → articulo_tela(id_articulo) |
| id_atributo | INTEGER | FK → atributo_tecnico(id_atributo) |

---

## MÓDULO 8: PRECIOS Y UNIDADES

### moneda
| Columna | Tipo | Restricción |
|---|---|---|
| id_moneda | INTEGER | PK, AUTO |
| codigo_moneda | VARCHAR(5) | NOT NULL, UNIQUE |
| nombre_moneda | VARCHAR(40) | NOT NULL |
| simbolo | VARCHAR(5) | |

### unidad_medida
| Columna | Tipo | Restricción |
|---|---|---|
| id_unidad_medida | INTEGER | PK, AUTO |
| nombre_unidad | VARCHAR(30) | NOT NULL, UNIQUE |
| abreviatura | VARCHAR(5) | NOT NULL, UNIQUE |

### precio
| Columna | Tipo | Restricción |
|---|---|---|
| id_precio | INTEGER | PK, AUTO |
| id_articulo | INTEGER | NOT NULL, FK → articulo(id_articulo) |
| id_moneda | INTEGER | NOT NULL, FK → moneda(id_moneda) |
| tipo_precio | VARCHAR(10) | NOT NULL |
| valor | DECIMAL(18,2) | NOT NULL |

---

## MÓDULO 9: ESPECIFICACIONES TÉCNICAS

### especificacion_tecnica
| Columna | Tipo | Restricción |
|---|---|---|
| especificacion_tecnica_id | BIGINT | PK, AUTO |
| producto_id | BIGINT | FK → producto(producto_id) |
| detalles_especificacion_tecnica | TEXT | |
| url_documento_especificacion_tecnica | VARCHAR(300) | |
| creado_en | DATETIME | NOT NULL |
| actualizado_en | DATETIME | NOT NULL |
| activo | BOOLEAN | NOT NULL |

---

## MÓDULO 10: COMERCIAL — SOLICITUDES DE COSTOS (SCOS)

### solicitudes_costos
| Columna | Tipo | Restricción |
|---|---|---|
| id_scos | BIGINT | PK, AUTO |
| numero | VARCHAR(20) | NOT NULL, UNIQUE |
| estado | VARCHAR(20) | ENUM(EstadoSCOS) |
| tipo | VARCHAR(20) | |
| cliente_id | BIGINT | NOT NULL, FK → clientes(cliente_id) |
| vendedor_id | BIGINT | FK → vendedores(id_vendedor) |
| articulo_descripcion | VARCHAR(255) | NOT NULL |
| nombre_prenda | VARCHAR(255) | |
| genero | VARCHAR(50) | |
| tallaje | VARCHAR(255) | |
| es_muestra | BOOLEAN | |
| has_logo | BOOLEAN | |
| cantidad | INTEGER | |
| fecha | DATE | |
| costo_total | DECIMAL(12,2) | |

### scos_telas
| Columna | Tipo | Restricción |
|---|---|---|
| id_scos_tela | BIGINT | PK, AUTO |
| solicitud_costos_id | BIGINT | FK → solicitudes_costos(id_scos) ON DELETE CASCADE |
| articulo_id | INTEGER | FK → articulo(id_articulo) |
| aplicacion | VARCHAR(255) | |
| descripcion | VARCHAR(255) | |
| proveedor_id | BIGINT | FK → proveedores(proveedor_id) |
| proveedor_referencia | VARCHAR(255) | |
| composicion | VARCHAR(255) | |
| color | VARCHAR(100) | |
| peso | DECIMAL(10,4) | |
| consumo | DECIMAL(10,4) | |
| unidad_medida | VARCHAR(20) | |
| precio_unitario | DECIMAL(12,2) | |
| moneda_precio_unitario | VARCHAR(3) | |
| costo_total | DECIMAL(12,2) | |
| moneda_costo_total | VARCHAR(3) | |

### scos_accesorios
| Columna | Tipo | Restricción |
|---|---|---|
| id_scos_accesorio | BIGINT | PK, AUTO |
| solicitud_costos_id | BIGINT | FK → solicitudes_costos(id_scos) ON DELETE CASCADE |
| articulo_id | INTEGER | FK → articulo(id_articulo) |
| tipo | VARCHAR(255) | |
| descripcion | VARCHAR(255) | |
| proveedor_id | BIGINT | FK → proveedores(proveedor_id) |
| proveedor_referencia | VARCHAR(255) | |
| cantidad | INTEGER | |
| consumo | DECIMAL(10,4) | |
| unidad_medida | VARCHAR(20) | |
| precio_unitario | DECIMAL(12,2) | |
| moneda_precio_unitario | VARCHAR(3) | |
| costo_total | DECIMAL(12,2) | |
| moneda_costo_total | VARCHAR(3) | |

### scos_logotipos
| Columna | Tipo | Restricción |
|---|---|---|
| id | BIGINT | PK, AUTO |
| solicitud_costos_id | BIGINT | FK → solicitudes_costos(id_scos) |
| solicitud_cotizacion_id | BIGINT | FK → solicitudes_cotizacion(id_scot) |
| tipo | VARCHAR(255) | |
| nombre | VARCHAR(255) | |
| ubicacion | VARCHAR(255) | |
| color | VARCHAR(100) | |
| tamano | DOUBLE | |
| cantidad | INTEGER | |
| precio | DECIMAL(14,2) | |

### plantilla
| Columna | Tipo | Restricción |
|---|---|---|
| id_plantilla | BIGINT | PK, AUTO |
| nombre_campo | VARCHAR(100) | NOT NULL, UNIQUE |

### modelo_plantilla
| Columna | Tipo | Restricción |
|---|---|---|
| id_modelo_plantilla | BIGINT | PK, AUTO |
| id_articulo | INTEGER | NOT NULL, FK → articulo(id_articulo) ON DELETE CASCADE |
| id_plantilla | BIGINT | NOT NULL, FK → plantilla(id_plantilla) ON DELETE CASCADE |
| — | — | UNIQUE(id_articulo, id_plantilla) |

### descripcion_plantilla
| Columna | Tipo | Restricción |
|---|---|---|
| id_descripcion_plantilla | BIGINT | PK, AUTO |
| id_scos | BIGINT | NOT NULL, FK → solicitudes_costos(id_scos) ON DELETE CASCADE |
| id_plantilla | BIGINT | NOT NULL, FK → plantilla(id_plantilla) |
| valor_descripcion | VARCHAR(500) | |
| activo | BOOLEAN | NOT NULL, DEFAULT true |

### scos_plantilla_material_vinculo
| Columna | Tipo | Restricción |
|---|---|---|
| id | BIGINT | PK, AUTO |
| id_descripcion_plantilla | BIGINT | NOT NULL, FK → descripcion_plantilla(id_descripcion_plantilla) ON DELETE CASCADE |
| material_type | VARCHAR(20) | ENUM(TELA, ACCESORIO) |
| material_id | BIGINT | NOT NULL |
| cantidad | INTEGER | |

---

## MÓDULO 11: COMERCIAL — SOLICITUDES DE COTIZACIÓN (SCOT)

### solicitudes_cotizacion
| Columna | Tipo | Restricción |
|---|---|---|
| id_scot | BIGINT | PK, AUTO |
| numero | VARCHAR(20) | NOT NULL, UNIQUE |
| estado | VARCHAR(20) | |
| tipo | VARCHAR(20) | |
| cliente_id | BIGINT | NOT NULL, FK → clientes(cliente_id) |
| vendedor_id | BIGINT | FK → vendedores(id_vendedor) |
| especificacion_tecnica_id | BIGINT | FK → especificacion_tecnica(especificacion_tecnica_id) |
| articulo_descripcion | VARCHAR(255) | NOT NULL |
| es_muestra | BOOLEAN | |
| has_logo | BOOLEAN | |
| cantidad | INTEGER | |
| fecha | DATE | |
| costo_total_calculado | DECIMAL(14,2) | |
| moneda_costo_total | VARCHAR(3) | |
| venta_asociada_id | BIGINT | |

---

## MÓDULO 12: COMERCIAL — EVALUACIONES DE NEGOCIO (EVN)

### evaluaciones_negocio
| Columna | Tipo | Restricción |
|---|---|---|
| id_evn | BIGINT | PK, AUTO |
| numero | VARCHAR(20) | NOT NULL, UNIQUE |
| referencia | VARCHAR(300) | |
| cliente_nombre | VARCHAR(200) | |
| cliente_id | BIGINT | NOT NULL, FK → clientes(cliente_id) |
| vendedor_id | BIGINT | FK → vendedores(id_vendedor) |
| estado | VARCHAR(20) | NOT NULL |
| fecha_evaluacion | DATE | NOT NULL |
| porcentaje_comision | DECIMAL(5,2) | |
| created_at | DATETIME | |
| updated_at | DATETIME | |

### evaluacion_negocio_items
| Columna | Tipo | Restricción |
|---|---|---|
| id_evni | BIGINT | PK, AUTO |
| evaluacion_negocio_id | BIGINT | NOT NULL, FK → evaluaciones_negocio(id_evn) |
| proveedor_id | BIGINT | FK → proveedores(proveedor_id) |
| articulo_id | INTEGER | FK → articulo(id_articulo) |
| nro_item | INTEGER | |
| descripcion | VARCHAR(500) | |
| modelo | VARCHAR(200) | |
| tela | VARCHAR(200) | |
| composicion | VARCHAR(200) | |
| genero | VARCHAR(50) | |
| codigo_interno | VARCHAR(100) | |
| codigo_proveedor | VARCHAR(100) | |
| proveedor_nombre | VARCHAR(200) | |
| cantidad | INTEGER | NOT NULL |
| precio_unitario | DECIMAL(12,2) | NOT NULL |
| moneda_precio_unitario | VARCHAR(3) | |
| costo_unitario | DECIMAL(12,2) | |
| moneda_costo_unitario | VARCHAR(3) | |
| costo_producto | DECIMAL(12,2) | |
| costo_logo | DECIMAL(12,2) | |
| costo_orden_trabajo | DECIMAL(12,2) | |
| tipo_item | VARCHAR(30) | |
| costeo_id | BIGINT | (soft ref, sin FK) |
| solicitud_costos_id | BIGINT | (soft ref, sin FK) |

### evaluacion_negocio_item_specs
| Columna | Tipo | Restricción |
|---|---|---|
| id | BIGINT | PK, AUTO |
| evaluacion_negocio_item_id | BIGINT | NOT NULL, FK → evaluacion_negocio_items(id_evni) |
| spec_key | VARCHAR(255) | |
| spec_value | VARCHAR(255) | |

### gasto_adicional
| Columna | Tipo | Restricción |
|---|---|---|
| id_gasto | BIGINT | PK, AUTO |
| evaluacion_negocio_id | BIGINT | NOT NULL, FK → evaluaciones_negocio(id_evn) |
| descripcion | VARCHAR(500) | |
| cantidad | INTEGER | |
| valor_unitario | DECIMAL(12,2) | |

### toma_tallaje
| Columna | Tipo | Restricción |
|---|---|---|
| id_toma_tallaje | BIGINT | PK, AUTO |
| evaluacion_negocio_id | BIGINT | NOT NULL, UNIQUE, FK → evaluaciones_negocio(id_evn) |

---

## MÓDULO 13: COMERCIAL — NOTAS DE VENTA (NV)

### notas_venta
| Columna | Tipo | Restricción |
|---|---|---|
| id_nv | BIGINT | PK, AUTO |
| numero_nv | VARCHAR(20) | NOT NULL, UNIQUE |
| evaluacion_negocio_id | BIGINT | FK → evaluaciones_negocio(id_evn) |
| cliente_id | BIGINT | NOT NULL, FK → clientes(cliente_id) |
| vendedor_id | BIGINT | FK → vendedores(id_vendedor) |
| estado | VARCHAR(20) | NOT NULL |
| es_kit | BOOLEAN | |
| detalle_kit | VARCHAR(500) | |
| fecha_emision | DATE | NOT NULL |
| fecha_entrega_estimada | DATE | |
| monto_subtotal | DECIMAL(12,2) | |
| moneda_subtotal | VARCHAR(3) | |
| monto_iva | DECIMAL(12,2) | |
| moneda_iva | VARCHAR(3) | |
| monto_total | DECIMAL(12,2) | |
| moneda_total | VARCHAR(3) | |
| created_at | DATETIME | |
| updated_at | DATETIME | |

### notas_venta_items
| Columna | Tipo | Restricción |
|---|---|---|
| id_item_nv | BIGINT | PK, AUTO |
| nota_venta_id | BIGINT | NOT NULL, FK → notas_venta(id_nv) |
| articulo_id | INTEGER | FK → articulo(id_articulo) |
| nro_item | INTEGER | |
| modelo | VARCHAR(100) | |
| tela | VARCHAR(100) | |
| composicion | VARCHAR(255) | |
| color | VARCHAR(50) | |
| talla | VARCHAR(20) | |
| genero | VARCHAR(20) | |
| codigo | VARCHAR(50) | |
| proveedor_id | BIGINT | FK → proveedores(proveedor_id) |
| lleva_logo | VARCHAR(50) | |
| tipo_item | VARCHAR(30) | |
| is_personalized | BOOLEAN | |
| detalle_ot | TEXT | |
| logo_detalle | TEXT | |
| cantidad | INTEGER | NOT NULL |
| precio_unitario | DECIMAL(12,2) | |
| moneda_precio_unitario | VARCHAR(3) | |
| total | DECIMAL(12,2) | |
| moneda_total | VARCHAR(3) | |

### notas_venta_item_tallas
| Columna | Tipo | Restricción |
|---|---|---|
| id | BIGINT | PK, AUTO |
| item_id | BIGINT | NOT NULL, FK → notas_venta_items(id_item_nv) |
| talla | VARCHAR(20) | |
| cantidad | INTEGER | |

---

## MÓDULO 14: PRODUCCIÓN — ÓRDENES DE PRODUCCIÓN (OP)

### orden_produccion
| Columna | Tipo | Restricción |
|---|---|---|
| id_op | BIGINT | PK, AUTO |
| numero_op | VARCHAR(20) | NOT NULL, UNIQUE |
| nota_venta_id | BIGINT | NOT NULL, FK → notas_venta(id_nv) |
| costeo_version_id | BIGINT | FK → produccion_costeo_versiones(id_costeo_version) |
| estado | VARCHAR(20) | NOT NULL, ENUM(EstadoOP) |
| fecha_inicio | DATE | |
| fecha_entrega_programada | DATE | |
| observaciones | TEXT | |
| created_at | DATETIME | |
| updated_at | DATETIME | |

### produccion_orden_items
| Columna | Tipo | Restricción |
|---|---|---|
| id_op_item | BIGINT | PK, AUTO |
| orden_produccion_id | BIGINT | NOT NULL, FK → orden_produccion(id_op) |
| articulo_id | INTEGER | FK → articulo(id_articulo) |
| nro_item | INTEGER | |
| modelo | VARCHAR(100) | |
| tela | VARCHAR(100) | |
| composicion | VARCHAR(255) | |
| color | VARCHAR(50) | |
| talla | VARCHAR(20) | |
| genero | VARCHAR(20) | |
| codigo | VARCHAR(50) | |
| lleva_logo | VARCHAR(50) | |
| cantidad | INTEGER | NOT NULL |

---

## MÓDULO 15: PRODUCCIÓN — ÓRDENES DE TRABAJO (OT)

### produccion_orden_trabajo
| Columna | Tipo | Restricción |
|---|---|---|
| id_ot | BIGINT | PK, AUTO |
| numero_ot | VARCHAR(30) | NOT NULL |
| nota_venta_id | BIGINT | NOT NULL, FK → notas_venta(id_nv) |
| orden_produccion_id | BIGINT | FK → orden_produccion(id_op) |
| nro_item | INTEGER | |
| tipo_ot | VARCHAR(20) | NOT NULL, ENUM(TipoOT) |
| fase | VARCHAR(20) | ENUM(FaseProduccion) |
| estado_ot | VARCHAR(20) | NOT NULL, ENUM(EstadoOT) |
| cantidad_total | INTEGER | |
| cantidad_producida | INTEGER | |
| cantidad_merma | INTEGER | |
| observaciones | TEXT | |
| created_at | DATETIME | |
| updated_at | DATETIME | |

### produccion_registro_avance
| Columna | Tipo | Restricción |
|---|---|---|
| id | BIGINT | PK, AUTO |
| orden_trabajo_id | BIGINT | NOT NULL, FK → produccion_orden_trabajo(id_ot) ON DELETE CASCADE |
| cantidad_producida | INTEGER | NOT NULL |
| cantidad_merma | INTEGER | NOT NULL |
| motivo_merma | VARCHAR(255) | |
| usuario | VARCHAR(100) | |
| observacion | TEXT | |
| fecha | DATETIME | NOT NULL |

---

## MÓDULO 16: PRODUCCIÓN — COSTEOS

### produccion_costeos
| Columna | Tipo | Restricción |
|---|---|---|
| id_costeo | BIGINT | PK, AUTO |
| solicitud_costos_id | BIGINT | NOT NULL, FK → solicitudes_costos(id_scos) |
| numero_costeo | VARCHAR(20) | NOT NULL |
| costo_hilos | DECIMAL(12,2) | |
| costo_mano_obra | DECIMAL(12,2) | |
| costo_etiquetas | DECIMAL(12,2) | |
| costo_embalaje | DECIMAL(12,2) | |
| costo_flete | DECIMAL(12,2) | |
| porcentaje_costo_fijo | DECIMAL(5,2) | |
| precio_cinta_1 | DECIMAL(12,2) | |
| cantidad_cinta_1 | DECIMAL(10,4) | |
| precio_cinta_2 | DECIMAL(12,2) | |
| cantidad_cinta_2 | DECIMAL(10,4) | |
| vivo_reflectivo | DECIMAL(12,2) | |
| cantidad_vivo | DECIMAL(10,4) | |
| costo_total_materia_prima | DECIMAL(12,2) | |
| margen_bruto_sugerido | DECIMAL(5,2) | |
| precio_venta_sugerido | DECIMAL(12,2) | |

### produccion_costeo_items
| Columna | Tipo | Restricción |
|---|---|---|
| id_costeo_item | BIGINT | PK, AUTO |
| costeo_id | BIGINT | NOT NULL, FK → produccion_costeos(id_costeo) |
| tipo_insumo | VARCHAR(30) | NOT NULL |
| articulo_id | INTEGER | soft FK → articulo(id_articulo) |
| nombre_insumo | VARCHAR(255) | |
| consumo | DECIMAL(10,4) | |
| precio_unitario | DECIMAL(12,2) | |
| costo_total | DECIMAL(12,2) | |

### produccion_costeo_versiones
| Columna | Tipo | Restricción |
|---|---|---|
| id_costeo_version | BIGINT | PK, AUTO |
| costeo_id | BIGINT | NOT NULL, FK → produccion_costeos(id_costeo) ON DELETE RESTRICT |
| numero_version | INTEGER | NOT NULL |
| fecha_creacion | DATETIME | NOT NULL |
| motivo_cambio | VARCHAR(255) | |
| usuario_creador | VARCHAR(255) | NOT NULL |
| total_mano_obra | DECIMAL(12,2) | |
| total_hilo | DECIMAL(12,2) | |
| total_flete | DECIMAL(12,2) | |
| total_embalaje | DECIMAL(12,2) | |
| total_etiquetas | DECIMAL(12,2) | |
| porcentaje_costo_fijo | DECIMAL(12,2) | |
| costo_total_materia_prima | DECIMAL(12,2) | |
| margen_bruto_sugerido | DECIMAL(12,2) | |
| precio_venta_sugerido | DECIMAL(12,2) | |
| — | — | UNIQUE(costeo_id, numero_version) |

### produccion_costeo_item_versiones
| Columna | Tipo | Restricción |
|---|---|---|
| id_costeo_item_version | BIGINT | PK, AUTO |
| costeo_version_id | BIGINT | NOT NULL, FK → produccion_costeo_versiones(id_costeo_version) |
| tipo_insumo | VARCHAR(30) | |
| articulo_id | INTEGER | soft FK → articulo(id_articulo) ON DELETE SET NULL |
| nombre_insumo | VARCHAR(255) | |
| consumo | DECIMAL(10,4) | |
| precio_unitario | DECIMAL(12,2) | |
| costo_total | DECIMAL(12,2) | |

---

## MÓDULO 17: PRODUCCIÓN — HOJAS DE COMPRA (HC)

### produccion_hojas_compra
| Columna | Tipo | Restricción |
|---|---|---|
| id_hc | BIGINT | PK, AUTO |
| numero_hc | VARCHAR(20) | NOT NULL, UNIQUE |
| op_id | BIGINT | NOT NULL, UNIQUE, FK → orden_produccion(id_op) ON DELETE RESTRICT |
| costeo_version_id | BIGINT | NOT NULL, FK → produccion_costeo_versiones(id_costeo_version) ON DELETE RESTRICT |
| estado | VARCHAR(20) | NOT NULL, ENUM(EstadoHC) |
| fecha_generacion | DATE | NOT NULL |
| observaciones | TEXT | |

### produccion_hoja_compra_items
| Columna | Tipo | Restricción |
|---|---|---|
| id_hc_item | BIGINT | PK, AUTO |
| hc_id | BIGINT | NOT NULL, FK → produccion_hojas_compra(id_hc) ON DELETE CASCADE |
| tipo_insumo | VARCHAR(50) | NOT NULL |
| articulo_id | INTEGER | soft FK → articulo(id_articulo) ON DELETE SET NULL |
| nombre_insumo | VARCHAR(200) | |
| consumo_unitario | DECIMAL(12,4) | |
| cantidad_op | INTEGER | |
| cantidad_requerida | DECIMAL(12,4) | |
| precio_unitario_ref | DECIMAL(12,2) | |

---

## MÓDULO 18: PRODUCCIÓN — ÓRDENES DE COMPRA (OC)

### produccion_ordenes_compra
| Columna | Tipo | Restricción |
|---|---|---|
| id_oc | BIGINT | PK, AUTO |
| numero_oc | VARCHAR(30) | NOT NULL, UNIQUE |
| proveedor_id | BIGINT | NOT NULL, FK → proveedores(proveedor_id) |
| estado | VARCHAR(25) | NOT NULL, ENUM(EstadoOC) |
| fecha_emision | DATE | NOT NULL |
| fecha_entrega_estimada | DATE | |
| observaciones | TEXT | |
| total_neto | DECIMAL(14,2) | |

### produccion_orden_compra_items
| Columna | Tipo | Restricción |
|---|---|---|
| id_oc_item | BIGINT | PK, AUTO |
| oc_id | BIGINT | NOT NULL, FK → produccion_ordenes_compra(id_oc) ON DELETE CASCADE |
| tipo_insumo | VARCHAR(50) | NOT NULL |
| articulo_id | INTEGER | soft FK → articulo(id_articulo) ON DELETE SET NULL |
| nombre_insumo | VARCHAR(200) | |
| cantidad_requerida | DECIMAL(12,4) | |
| cantidad_stock | DECIMAL(12,4) | |
| cantidad_comprada | DECIMAL(12,4) | |
| precio_unitario | DECIMAL(12,2) | |
| subtotal | DECIMAL(14,2) | |

### produccion_hc_item_oc_item *(tabla junction)*
| Columna | Tipo | Restricción |
|---|---|---|
| hc_item_id | BIGINT | FK → produccion_hoja_compra_items(id_hc_item) ON DELETE CASCADE |
| oc_item_id | BIGINT | FK → produccion_orden_compra_items(id_oc_item) ON DELETE CASCADE |

---

## MÓDULO 19: PRODUCCIÓN — RECEPCIONES DE OC

### produccion_recepciones_oc
| Columna | Tipo | Restricción |
|---|---|---|
| id_recepcion | BIGINT | PK, AUTO |
| oc_id | BIGINT | NOT NULL, FK → produccion_ordenes_compra(id_oc) ON DELETE CASCADE |
| fecha_recepcion | DATE | NOT NULL |
| numero_guia | VARCHAR(50) | |
| responsable | VARCHAR(100) | |
| observaciones | TEXT | |

### produccion_recepcion_oc_items
| Columna | Tipo | Restricción |
|---|---|---|
| id_recepcion_item | BIGINT | PK, AUTO |
| recepcion_id | BIGINT | NOT NULL, FK → produccion_recepciones_oc(id_recepcion) ON DELETE CASCADE |
| oc_item_id | BIGINT | NOT NULL, FK → produccion_orden_compra_items(id_oc_item) ON DELETE RESTRICT |
| cantidad_recibida | DECIMAL(12,4) | NOT NULL |
| cantidad_conforme | DECIMAL(12,4) | |
| cantidad_rechazada | DECIMAL(12,4) | |
| motivo_rechazo | TEXT | |

---

## MÓDULO 20: PRODUCCIÓN — ÓRDENES DE SERVICIO (OS)

### produccion_ordenes_servicio
| Columna | Tipo | Restricción |
|---|---|---|
| id_os | BIGINT | PK, AUTO |
| numero_os | VARCHAR(30) | NOT NULL, UNIQUE |
| op_id | BIGINT | NOT NULL, FK → orden_produccion(id_op) |
| proveedor_id | BIGINT | NOT NULL, FK → proveedores(proveedor_id) |
| tipo_servicio | VARCHAR(20) | NOT NULL, ENUM(TipoServicioOS) |
| estado | VARCHAR(20) | NOT NULL, ENUM(EstadoOS) |
| fecha_emision | DATE | NOT NULL |
| fecha_entrega_estimada | DATE | |
| descripcion_trabajo | TEXT | |
| cantidad_pactada | INTEGER | NOT NULL |
| precio_unitario | DECIMAL(12,2) | |
| total_neto | DECIMAL(14,2) | |
| observaciones | TEXT | |

### produccion_despachos_os
| Columna | Tipo | Restricción |
|---|---|---|
| id_despacho | BIGINT | PK, AUTO |
| os_id | BIGINT | NOT NULL, FK → produccion_ordenes_servicio(id_os) ON DELETE CASCADE |
| fecha_despacho | DATE | NOT NULL |
| cantidad_despachada | INTEGER | |
| observaciones | TEXT | |

### produccion_recepciones_os
| Columna | Tipo | Restricción |
|---|---|---|
| id_recepcion | BIGINT | PK, AUTO |
| os_id | BIGINT | NOT NULL, FK → produccion_ordenes_servicio(id_os) ON DELETE CASCADE |
| fecha_recepcion | DATE | NOT NULL |
| cantidad_recibida | INTEGER | |
| cantidad_conforme | INTEGER | |
| observaciones | TEXT | |

---

## RESUMEN DE RELACIONES Y CARDINALIDAD

| Entidad A | Cardinalidad | Entidad B | Vía / Notas |
|---|---|---|---|
| pais | 1 : N | region | region.pais_id |
| region | 1 : N | comuna | comuna.region_id |
| areas | 1 : N | roles | roles.area_id |
| usuarios | N : M | roles | tabla junction usuarios_roles |
| usuarios | N : M | areas | tabla junction usuarios_areas |
| roles | N : M | permisos | tabla junction rol_permisos |
| usuarios | 1 : 1 | vendedores | vendedores.id_usuario (UNIQUE) |
| rubros | 1 : N | giros | giros.rubro_id |
| giros | 1 : N | clientes | clientes.fk_giro |
| giros | 1 : N | proveedores | proveedores.fk_provee_giro |
| clientes | 1 : N | direccion | direccion.fk_direccion |
| proveedores | 1 : N | direccion | direccion.fk_provee_direccion |
| tipo_direccion | 1 : N | direccion | direccion.tipo_direccion_id |
| comuna | 1 : N | direccion | direccion.comuna_id |
| clientes | 1 : N | contactos | contactos.fk_contacto |
| proveedores | 1 : N | contactos | contactos.fk_provee_contacto |
| tipos_contacto | 1 : N | contactos | contactos.tipo_contacto_id |
| proveedores | 1 : N | dato_bancario | dato_bancario.fk_provee_dato_bancario |
| banco | 1 : N | dato_bancario | dato_bancario.banco_id |
| tipo_cuenta_bancaria | 1 : N | dato_bancario | dato_bancario.tipo_cuenta_id |
| tipo_articulo | 1 : N | articulo | articulo.id_tipo_articulo |
| articulo | 1 : 1 | articulo_tela | MapsId (subtipo) |
| articulo | 1 : 1 | articulo_prenda | MapsId (subtipo) |
| articulo | 1 : 1 | articulo_accesorio | MapsId (subtipo) |
| categoria_tela | 1 : N | subcategoria_tela | subcategoria_tela.id_categoria_tela |
| categoria_tela | 1 : N | articulo | articulo.id_categoria_tela |
| subcategoria_tela | 1 : N | articulo | articulo.id_subcategoria_tela |
| familia_tela | 1 : N | articulo_tela | articulo_tela.id_familia_tela |
| clasificacion_tecnica | 1 : N | articulo_tela | articulo_tela.id_clasificacion_tecnica |
| composicion | 1 : N | articulo_tela | articulo_tela.id_composicion |
| gramaje_tela | 1 : N | articulo_tela | articulo_tela.id_gramaje |
| articulo_tela | N : M | color_tela | tabla junction tela_color |
| articulo_tela | N : M | atributo_tecnico | tabla junction tela_atributo_tecnico |
| articulo | 1 : N | precio | precio.id_articulo |
| moneda | 1 : N | precio | precio.id_moneda |
| producto | 1 : N | especificacion_tecnica | especificacion_tecnica.producto_id |
| clientes | 1 : N | solicitudes_costos | solicitudes_costos.cliente_id |
| vendedores | 1 : N | solicitudes_costos | solicitudes_costos.vendedor_id |
| solicitudes_costos | 1 : N | scos_telas | scos_telas.solicitud_costos_id |
| solicitudes_costos | 1 : N | scos_accesorios | scos_accesorios.solicitud_costos_id |
| solicitudes_costos | 1 : N | scos_logotipos | scos_logotipos.solicitud_costos_id |
| articulo | 1 : N | scos_telas | scos_telas.articulo_id |
| articulo | 1 : N | scos_accesorios | scos_accesorios.articulo_id |
| proveedores | 1 : N | scos_telas | scos_telas.proveedor_id |
| proveedores | 1 : N | scos_accesorios | scos_accesorios.proveedor_id |
| articulo | N : M | plantilla | tabla junction modelo_plantilla |
| solicitudes_costos | 1 : N | descripcion_plantilla | descripcion_plantilla.id_scos |
| plantilla | 1 : N | descripcion_plantilla | descripcion_plantilla.id_plantilla |
| descripcion_plantilla | 1 : N | scos_plantilla_material_vinculo | vinculo.id_descripcion_plantilla |
| clientes | 1 : N | solicitudes_cotizacion | solicitudes_cotizacion.cliente_id |
| vendedores | 1 : N | solicitudes_cotizacion | solicitudes_cotizacion.vendedor_id |
| especificacion_tecnica | 1 : N | solicitudes_cotizacion | solicitudes_cotizacion.especificacion_tecnica_id |
| solicitudes_cotizacion | 1 : N | scos_logotipos | scos_logotipos.solicitud_cotizacion_id |
| clientes | 1 : N | evaluaciones_negocio | evaluaciones_negocio.cliente_id |
| vendedores | 1 : N | evaluaciones_negocio | evaluaciones_negocio.vendedor_id |
| evaluaciones_negocio | 1 : N | evaluacion_negocio_items | items.evaluacion_negocio_id |
| evaluaciones_negocio | 1 : N | gasto_adicional | gasto_adicional.evaluacion_negocio_id |
| evaluaciones_negocio | 1 : 1 | toma_tallaje | toma_tallaje.evaluacion_negocio_id (UNIQUE) |
| evaluacion_negocio_items | 1 : N | evaluacion_negocio_item_specs | specs.evaluacion_negocio_item_id |
| proveedores | 1 : N | evaluacion_negocio_items | items.proveedor_id |
| articulo | 1 : N | evaluacion_negocio_items | items.articulo_id |
| evaluaciones_negocio | 1 : N | notas_venta | notas_venta.evaluacion_negocio_id |
| clientes | 1 : N | notas_venta | notas_venta.cliente_id |
| vendedores | 1 : N | notas_venta | notas_venta.vendedor_id |
| notas_venta | 1 : N | notas_venta_items | items.nota_venta_id |
| notas_venta_items | 1 : N | notas_venta_item_tallas | tallas.item_id |
| articulo | 1 : N | notas_venta_items | items.articulo_id |
| proveedores | 1 : N | notas_venta_items | items.proveedor_id |
| notas_venta | 1 : N | orden_produccion | op.nota_venta_id |
| produccion_costeo_versiones | 1 : N | orden_produccion | op.costeo_version_id |
| orden_produccion | 1 : N | produccion_orden_items | items.orden_produccion_id |
| articulo | 1 : N | produccion_orden_items | items.articulo_id |
| notas_venta | 1 : N | produccion_orden_trabajo | ot.nota_venta_id |
| orden_produccion | 1 : N | produccion_orden_trabajo | ot.orden_produccion_id |
| produccion_orden_trabajo | 1 : N | produccion_registro_avance | avance.orden_trabajo_id |
| solicitudes_costos | 1 : N | produccion_costeos | costeos.solicitud_costos_id |
| produccion_costeos | 1 : N | produccion_costeo_items | items.costeo_id |
| produccion_costeos | 1 : N | produccion_costeo_versiones | versiones.costeo_id |
| produccion_costeo_versiones | 1 : N | produccion_costeo_item_versiones | item_ver.costeo_version_id |
| orden_produccion | 1 : 1 | produccion_hojas_compra | hc.op_id (UNIQUE) |
| produccion_costeo_versiones | 1 : N | produccion_hojas_compra | hc.costeo_version_id |
| produccion_hojas_compra | 1 : N | produccion_hoja_compra_items | items.hc_id |
| produccion_hoja_compra_items | N : M | produccion_orden_compra_items | junction produccion_hc_item_oc_item |
| proveedores | 1 : N | produccion_ordenes_compra | oc.proveedor_id |
| produccion_ordenes_compra | 1 : N | produccion_orden_compra_items | items.oc_id |
| produccion_ordenes_compra | 1 : N | produccion_recepciones_oc | recepciones.oc_id |
| produccion_recepciones_oc | 1 : N | produccion_recepcion_oc_items | items.recepcion_id |
| produccion_orden_compra_items | 1 : N | produccion_recepcion_oc_items | items.oc_item_id |
| orden_produccion | 1 : N | produccion_ordenes_servicio | os.op_id |
| proveedores | 1 : N | produccion_ordenes_servicio | os.proveedor_id |
| produccion_ordenes_servicio | 1 : N | produccion_despachos_os | despachos.os_id |
| produccion_ordenes_servicio | 1 : N | produccion_recepciones_os | recepciones.os_id |
