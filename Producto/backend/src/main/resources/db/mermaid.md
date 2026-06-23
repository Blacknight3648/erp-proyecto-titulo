erDiagram
    ACCESORIO {
        NUMBER accesorio_id PK
        NUMBER activo
        TIMESTAMP actualizado_en
        TIMESTAMP creado_en
        VARCHAR2 descripcion_accesorio
        VARCHAR2 nombre_accesorio
    }
    AREAS {
        NUMBER id_area PK
        VARCHAR2 descripcion
        VARCHAR2 nombre_area
    }
    AUDITORIA {
        NUMBER auditoria_id PK
        VARCHAR2 accion
        CLOB cambios
        VARCHAR2 entidad
        NUMBER entidad_id
        TIMESTAMP fecha
        NUMBER usuario_id
    }
    BANCO {
        NUMBER banco_id PK
        VARCHAR2 codigo_banco
        VARCHAR2 nombre_banco
    }
    CATALOGO_PROVEEDOR {
        NUMBER catalogo_proveedor_id PK
        NUMBER activo
        TIMESTAMP actualizado_en
        TIMESTAMP creado_en
        VARCHAR2 codigo_proveedor
        BINARY_DOUBLE precio
        NUMBER producto_id FK
        NUMBER proveedor_id FK
    }
    CINTA_REFLECTANTE {
        NUMBER cinta_reflectante_id PK
        NUMBER activo
        TIMESTAMP actualizado_en
        TIMESTAMP creado_en
        VARCHAR2 ancho_cinta
        VARCHAR2 tipo_cinta
    }
    CLIENTES {
        NUMBER cliente_id PK
        NUMBER activo
        VARCHAR2 contacto_cliente
        VARCHAR2 correo_cliente
        VARCHAR2 razon_social
        VARCHAR2 run_cliente
        VARCHAR2 sigla
        VARCHAR2 telefono_cliente
        NUMBER fk_dato_bancario FK
        NUMBER fk_direccion FK
        NUMBER fk_giro FK
    }
    COMUNA {
        NUMBER comuna_id PK
        NUMBER activo
        TIMESTAMP actualizado_en
        TIMESTAMP creado_en
        VARCHAR2 nombre_comuna
        NUMBER region_id FK
    }
    CONFIGURACION_PLANTILLA_ACCESORIOS {
        NUMBER configuracion_id FK
        NUMBER cantidad
        VARCHAR2 nombre_accesorio
        VARCHAR2 tipo
    }
    CONFIGURACION_PLANTILLA_CAMPOS {
        NUMBER configuracion_id FK
        VARCHAR2 campo
    }
    CONFIGURACION_PLANTILLA_TELAS {
        NUMBER configuracion_id FK
        VARCHAR2 aplicacion
        VARCHAR2 color
        VARCHAR2 composicion
        VARCHAR2 nombre
        NUMBER peso
        VARCHAR2 unidad_medida
    }
    CONFIGURACION_PLANTILLAS {
        NUMBER id PK
        CLOB custom_fields
        VARCHAR2 nombre_prenda
    }
    CONTACTOS {
        NUMBER contacto_id PK
        VARCHAR2 email_contacto
        VARCHAR2 nombre_contacto
        VARCHAR2 telefono_contacto
        NUMBER tipo_contacto_id FK
        NUMBER fk_provee_contacto FK
    }
    COSTO_FIJO {
        NUMBER costo_fijo_id PK
        NUMBER activo
        TIMESTAMP actualizado_en
        TIMESTAMP creado_en
        BINARY_DOUBLE monto
        VARCHAR2 nombre_costo_fijo
        VARCHAR2 periodo
    }
    DATO_BANCARIO {
        NUMBER dato_bancario_id PK
        VARCHAR2 numero_cuenta
        NUMBER banco_id FK
        NUMBER tipo_cuenta_id FK
        NUMBER fk_provee_dato_bancario FK
    }
    DIRECCION {
        NUMBER direccion_id PK
        VARCHAR2 calle
        VARCHAR2 depto
        VARCHAR2 numero
        NUMBER comuna_id FK
        NUMBER tipo_direccion_id FK
        NUMBER fk_provee_direccion FK
    }
    DOCUMENT_COUNTER {
        VARCHAR2 tipo PK
        NUMBER ultimo_numero
    }
    ESPECIFICACION_TECNICA {
        NUMBER especificacion_tecnica_id PK
        NUMBER activo
        TIMESTAMP actualizado_en
        TIMESTAMP creado_en
        CLOB detalles_especificacion_tecnica
        VARCHAR2 url_documento
        NUMBER producto_id FK
    }
    EVALUACION_NEGOCIO_ITEMS {
        NUMBER idevni PK
        NUMBER cantidad
        VARCHAR2 codigo_interno
        VARCHAR2 codigo_proveedor
        VARCHAR2 composicion
        NUMBER costo_logo
        NUMBER costo_orden_trabajo
        NUMBER costo_producto
        NUMBER costo_unitario
        VARCHAR2 descripcion
        VARCHAR2 genero
        VARCHAR2 modelo
        VARCHAR2 moneda_costo_unitario
        VARCHAR2 moneda_precio_unitario
        NUMBER nro_item
        NUMBER precio_unitario
        VARCHAR2 proveedor_nombre
        VARCHAR2 technical_specs_json
        VARCHAR2 tela
        VARCHAR2 tipo_item
        NUMBER evaluacion_negocio_id FK
        NUMBER producto_id FK
        NUMBER proveedor_id FK
    }
    EVALUACIONES_NEGOCIO {
        NUMBER idevn PK
        TIMESTAMP created_at
        TIMESTAMP updated_at
        VARCHAR2 cliente_nombre
        NUMBER costeo_id FK
        VARCHAR2 estado
        DATE fecha_evaluacion
        VARCHAR2 numero
        NUMBER porcentaje_comision
        VARCHAR2 referencia
        NUMBER solicitud_cotizacion_id
        NUMBER cliente_id FK
        NUMBER vendedor_id FK
    }
    FLYWAY_SCHEMA_HISTORY {
        NUMBER installed_rank PK
        VARCHAR2 version
        VARCHAR2 description
        VARCHAR2 type
        VARCHAR2 script
        NUMBER checksum
        VARCHAR2 installed_by
        TIMESTAMP installed_on
        NUMBER execution_time
        NUMBER success
    }
    GASTOS_ADICIONALES {
        NUMBER idga PK
        CLOB metadata_json
        VARCHAR2 moneda
        NUMBER monto
        VARCHAR2 tipo_gasto
        NUMBER evaluacion_negocio_id FK
    }
    GIROS {
        NUMBER giro_id PK
        VARCHAR2 codigo_sii
        VARCHAR2 descripcion_giro
        VARCHAR2 nombre_giro
        NUMBER rubro_id FK
    }
    HISTORIAL_ESTADO {
        NUMBER id PK
        NUMBER entidad_id
        VARCHAR2 estado_anterior
        VARCHAR2 estado_nuevo
        TIMESTAMP fecha
        CLOB observacion
        VARCHAR2 tipo_entidad
        VARCHAR2 usuario
    }
    IDEMPOTENCY_TOKEN {
        VARCHAR2 token PK
        TIMESTAMP created_at
        VARCHAR2 metodo
        VARCHAR2 path
        CLOB response_body
        NUMBER status_code
    }
    NOTAS_VENTA {
        NUMBER idnv PK
        TIMESTAMP created_at
        TIMESTAMP updated_at
        VARCHAR2 detalle_kit
        NUMBER es_kit
        VARCHAR2 estado
        NUMBER evaluacion_negocio_id FK
        DATE fecha_emision
        DATE fecha_entrega_estimada
        VARCHAR2 moneda_iva
        VARCHAR2 moneda_subtotal
        VARCHAR2 moneda_total
        NUMBER monto_iva
        NUMBER monto_subtotal
        NUMBER monto_total
        VARCHAR2 numeronv
        NUMBER cliente_id FK
        NUMBER vendedor_id FK
    }
    NOTAS_VENTA_ITEM_TALLAS {
        NUMBER id_item_talla PK
        NUMBER cantidad
        VARCHAR2 talla
        NUMBER item_id FK
    }
    NOTAS_VENTA_ITEMS {
        NUMBER id_itemnv PK
        NUMBER cantidad
        VARCHAR2 codigo
        VARCHAR2 color
        VARCHAR2 composicion
        CLOB detalle_ot
        VARCHAR2 genero
        NUMBER is_personalized
        VARCHAR2 lleva_logo
        CLOB logo_detalle
        VARCHAR2 modelo
        VARCHAR2 moneda_precio_unitario
        VARCHAR2 moneda_total
        NUMBER nro_item
        NUMBER precio_unitario
        VARCHAR2 talla
        VARCHAR2 tela
        VARCHAR2 tipo_item
        NUMBER total
        NUMBER nota_venta_id FK
        NUMBER producto_id FK
        NUMBER proveedor_id FK
    }
    ORDEN_PRODUCCION {
        NUMBER idop PK
        TIMESTAMP created_at
        TIMESTAMP updated_at
        VARCHAR2 estado
        DATE fecha_entrega_programada
        DATE fecha_inicio
        NUMBER nota_venta_id FK
        VARCHAR2 numeroop
        CLOB observaciones
        NUMBER costeo_version_id FK
    }
    PAIS {
        NUMBER pais_id PK
        VARCHAR2 nombre_pais
    }
    PERMISOS {
        NUMBER id PK
        VARCHAR2 descripcion
        VARCHAR2 modulo
        VARCHAR2 nombre
    }
    PRENDA_LISTA {
        NUMBER prenda_lista_id PK
        NUMBER activo
        TIMESTAMP actualizado_en
        TIMESTAMP creado_en
        VARCHAR2 color
        VARCHAR2 composicion
        VARCHAR2 descripcion
        VARCHAR2 genero
        VARCHAR2 gramaje
        VARCHAR2 modelo
        VARCHAR2 nombre_prenda
        VARCHAR2 tela
    }
    PRODUCCION_COSTEO_ITEM_VERSIONES {
        NUMBER id_costeo_item_version PK
        NUMBER activo
        NUMBER consumo
        NUMBER costeo_item_id
        NUMBER costeo_item_version_id_padre
        NUMBER costo_total
        NUMBER insumo_id
        VARCHAR2 nombre_insumo
        NUMBER precio_unitario
        VARCHAR2 tipo_insumo
        NUMBER costeo_version_id FK
    }
    PRODUCCION_COSTEO_ITEMS {
        NUMBER id_costeo_item PK
        NUMBER consumo
        NUMBER costo_total
        NUMBER insumo_id
        VARCHAR2 nombre_insumo
        NUMBER precio_unitario
        VARCHAR2 tipo_insumo
        NUMBER costeo_id FK
    }
    PRODUCCION_COSTEO_VERSIONES {
        NUMBER id_costeo_version PK
        NUMBER costo_total_materia_prima
        TIMESTAMP fecha_creacion
        NUMBER margen_bruto_sugerido
        VARCHAR2 motivo_cambio
        NUMBER numero_version
        NUMBER porcentaje_costo_fijo
        NUMBER precio_venta_sugerido
        NUMBER total_embalaje
        NUMBER total_etiquetas
        NUMBER total_flete
        NUMBER total_hilo
        NUMBER total_mano_obra
        VARCHAR2 usuario_creador
        NUMBER costeo_id FK
    }
    PRODUCCION_COSTEOS {
        NUMBER id_costeo PK
        NUMBER cantidad_cinta_1
        NUMBER cantidad_cinta_2
        NUMBER cantidad_vivo
        NUMBER costo_embalaje
        NUMBER costo_etiquetas
        NUMBER costo_flete
        NUMBER costo_hilos
        NUMBER costo_mano_obra
        NUMBER costo_total_materia_prima
        NUMBER margen_bruto_sugerido
        VARCHAR2 numero_costeo
        NUMBER porcentaje_costo_fijo
        NUMBER precio_cinta_1
        NUMBER precio_cinta_2
        NUMBER precio_venta_sugerido
        NUMBER solicitud_costos_id FK
    }
    PRODUCCION_DESPACHOS_OS {
        NUMBER id_despacho PK
        NUMBER cantidad_despachada
        VARCHAR2 descripcion
        DATE fecha_despacho
        CLOB observaciones
        VARCHAR2 responsable
        NUMBER os_id FK
    }
    PRODUCCION_HC_ITEM_OC_ITEM {
        NUMBER id_link PK
        NUMBER cantidad_asignada
        NUMBER hc_item_id FK
        NUMBER oc_item_id FK
    }
    PRODUCCION_HOJA_COMPRA_ITEMS {
        NUMBER id_hc_item PK
        NUMBER cantidad_op
        NUMBER cantidad_requerida
        NUMBER consumo_unitario
        NUMBER insumo_id
        VARCHAR2 nombre_insumo
        NUMBER precio_unitario_ref
        VARCHAR2 tipo_insumo
        NUMBER hc_id FK
    }
    PRODUCCION_HOJAS_COMPRA {
        NUMBER id_hc PK
        VARCHAR2 estado
        DATE fecha_generacion
        VARCHAR2 numero_hc
        CLOB observaciones
        NUMBER costeo_version_id FK
        NUMBER op_id FK
    }
    PRODUCCION_ORDEN_COMPRA_ITEMS {
        NUMBER id_oc_item PK
        NUMBER cantidad_comprada
        NUMBER cantidad_requerida
        NUMBER cantidad_stock
        NUMBER insumo_id
        VARCHAR2 nombre_insumo
        NUMBER precio_unitario
        NUMBER subtotal
        VARCHAR2 tipo_insumo
        NUMBER oc_id FK
    }
    PRODUCCION_ORDEN_ITEMS {
        NUMBER idopitem PK
        NUMBER cantidad
        VARCHAR2 codigo
        VARCHAR2 color
        VARCHAR2 composicion
        VARCHAR2 genero
        VARCHAR2 lleva_logo
        VARCHAR2 modelo
        NUMBER nro_item
        NUMBER producto_id
        VARCHAR2 talla
        VARCHAR2 tela
        NUMBER orden_produccion_id FK
    }
    PRODUCCION_ORDEN_TRABAJO {
        NUMBER idot PK
        TIMESTAMP created_at
        TIMESTAMP updated_at
        NUMBER cantidad_merma
        NUMBER cantidad_producida
        NUMBER cantidad_total
        VARCHAR2 estadoot
        VARCHAR2 fase
        NUMBER nota_venta_id FK
        NUMBER nro_item
        VARCHAR2 numeroot
        CLOB observaciones
        NUMBER orden_produccion_id FK
        VARCHAR2 tipoot
    }
    PRODUCCION_ORDENES_COMPRA {
        NUMBER id_oc PK
        VARCHAR2 estado
        DATE fecha_emision
        DATE fecha_entrega_estimada
        VARCHAR2 numero_oc
        CLOB observaciones
        NUMBER total_neto
        NUMBER proveedor_id FK
    }
    PRODUCCION_ORDENES_SERVICIO {
        NUMBER id_os PK
        NUMBER cantidad_pactada
        CLOB descripcion_trabajo
        VARCHAR2 estado
        DATE fecha_emision
        DATE fecha_entrega_estimada
        VARCHAR2 numero_os
        CLOB observaciones
        NUMBER precio_unitario
        VARCHAR2 tipo_servicio
        NUMBER total_neto
        NUMBER op_id FK
        NUMBER proveedor_id FK
    }
    PRODUCCION_RECEPCION_OC_ITEMS {
        NUMBER id_recepcion_item PK
        NUMBER cantidad_conforme
        NUMBER cantidad_rechazada
        NUMBER cantidad_recibida
        CLOB motivo_rechazo
        NUMBER oc_item_id FK
        NUMBER recepcion_id FK
    }
    PRODUCCION_RECEPCIONES_OC {
        NUMBER id_recepcion PK
        DATE fecha_recepcion
        VARCHAR2 numero_guia
        CLOB observaciones
        VARCHAR2 responsable
        NUMBER oc_id FK
    }
    PRODUCCION_RECEPCIONES_OS {
        NUMBER id_recepcion PK
        NUMBER cantidad_conforme
        NUMBER cantidad_defectuosa
        NUMBER cantidad_recibida
        DATE fecha_recepcion
        CLOB observaciones
        VARCHAR2 responsable
        NUMBER os_id FK
    }
    PRODUCCION_REGISTRO_AVANCE {
        NUMBER id PK
        NUMBER cantidad_merma
        NUMBER cantidad_producida
        TIMESTAMP fecha
        VARCHAR2 motivo_merma
        CLOB observacion
        NUMBER orden_trabajo_id
        VARCHAR2 usuario
    }
    PRODUCTO {
        NUMBER producto_id PK
        NUMBER activo
        TIMESTAMP actualizado_en
        TIMESTAMP creado_en
        VARCHAR2 codigo_producto
        VARCHAR2 color
        VARCHAR2 descripcion
        VARCHAR2 genero
        VARCHAR2 nombre
    }
    PROVEEDORES {
        NUMBER proveedor_id PK
        NUMBER activo
        TIMESTAMP actualizado_en
        TIMESTAMP creado_en
        VARCHAR2 horario_atencion
        VARCHAR2 razon_social_proveedor
        VARCHAR2 run_proveedor
        VARCHAR2 sigla
        VARCHAR2 tipo_proveedor
        NUMBER fk_provee_giro FK
    }
    REGION {
        NUMBER region_id PK
        NUMBER activo
        TIMESTAMP actualizado_en
        TIMESTAMP creado_en
        VARCHAR2 nombre_region
        NUMBER pais_id FK
    }
    ROL_PERMISOS {
        NUMBER rol_id PK
        NUMBER permiso_id PK
    }
    ROLES {
        NUMBER id_role PK
        VARCHAR2 descripcion
        VARCHAR2 nombre
        NUMBER area_id FK
    }
    RUBROS {
        NUMBER rubro_id PK
        VARCHAR2 descripcion_rubro
        VARCHAR2 nombre_rubro
    }
    SCOS_ACCESORIOS {
        NUMBER idscosaccesorio PK
        NUMBER cantidad
        NUMBER consumo
        NUMBER costo_total
        VARCHAR2 descripcion
        VARCHAR2 moneda_costo_total
        VARCHAR2 moneda_precio_unitario
        NUMBER precio_unitario
        VARCHAR2 proveedor_referencia
        VARCHAR2 tipo
        VARCHAR2 unidad_medida
        NUMBER accesorio_id FK
        NUMBER proveedor_id FK
        NUMBER solicitud_costos_id FK
    }
    SCOS_COSTO_FIJO {
        NUMBER idscoscosto_fijo PK
        NUMBER embalaje
        NUMBER etiqueta
        NUMBER flete
        NUMBER hilo
        NUMBER mano_obra_gratificacion
        NUMBER mano_obra_simple
        VARCHAR2 moneda_total
        NUMBER total
        NUMBER solicitud_costos_id FK
    }
    SCOS_LOGOTIPOS {
        NUMBER id PK
        NUMBER cantidad
        VARCHAR2 color
        VARCHAR2 nombre
        NUMBER precio
        BINARY_DOUBLE tamano
        VARCHAR2 tipo
        VARCHAR2 ubicacion
        NUMBER solicitud_costos_id FK
        NUMBER solicitud_cotizacion_id FK
    }
    SCOS_PLANTILLA {
        NUMBER id PK
        CLOB campos_personalizados
        CLOB descripcion
        CLOB detalles_prenda
        VARCHAR2 genero
        NUMBER mo_acolchado
        NUMBER mo_costura_sellada
        NUMBER mo_prenda
        VARCHAR2 nombre
        VARCHAR2 nombre_prenda
        NUMBER solicitud_costos_id FK
    }
    SCOS_PLANTILLA_ACCESORIOS {
        NUMBER plantilla_id FK
        NUMBER cantidad
        VARCHAR2 nombre_accesorio
        VARCHAR2 tipo
    }
    SCOS_PLANTILLA_CAMPOS_ACTIVOS {
        NUMBER plantilla_id FK
        VARCHAR2 campo_key
    }
    SCOS_PLANTILLA_LOGOTIPOS {
        NUMBER plantilla_id FK
        NUMBER cantidad
        VARCHAR2 color
        VARCHAR2 nombre
        NUMBER precio
        BINARY_DOUBLE tamano
        VARCHAR2 tipo
        VARCHAR2 ubicacion
    }
    SCOS_PLANTILLA_MATERIAL_VINCULOS {
        NUMBER id PK
        NUMBER cantidad
        VARCHAR2 field_name
        NUMBER material_id
        VARCHAR2 material_type
        NUMBER plantilla_id FK
    }
    SCOS_PLANTILLA_TELAS {
        NUMBER plantilla_id FK
        VARCHAR2 aplicacion
        VARCHAR2 color
        VARCHAR2 composicion
        VARCHAR2 nombre
        NUMBER peso
        VARCHAR2 unidad_medida
    }
    SCOS_TELAS {
        NUMBER idscostela PK
        VARCHAR2 aplicacion
        VARCHAR2 color
        VARCHAR2 composicion
        NUMBER consumo
        NUMBER costo_total
        VARCHAR2 descripcion
        VARCHAR2 moneda_costo_total
        VARCHAR2 moneda_precio_unitario
        NUMBER peso
        NUMBER precio_unitario
        VARCHAR2 proveedor_referencia
        VARCHAR2 unidad_medida
        NUMBER proveedor_id FK
        NUMBER solicitud_costos_id FK
        NUMBER tela_id FK
    }
    SCOT_PRENDAS {
        NUMBER idscotprenda_lista PK
        NUMBER cantidad
        VARCHAR2 color
        VARCHAR2 composicion
        VARCHAR2 link_referencia
        VARCHAR2 moneda_precio_unitario
        VARCHAR2 nombre
        CLOB observaciones
        VARCHAR2 peso
        NUMBER precio_unitario
        VARCHAR2 proveedor_referencia
        VARCHAR2 talla
        NUMBER producto_id FK
        NUMBER solicitud_costos_id FK
        NUMBER solicitud_id FK
        NUMBER solicitud_cotizacion_id FK
    }
    SOLICITUDES_COSTOS {
        NUMBER idscos PK
        VARCHAR2 articulo_descripcion
        NUMBER cantidad
        NUMBER costo_total
        NUMBER es_muestra
        VARCHAR2 estado
        DATE fecha
        VARCHAR2 genero
        NUMBER has_logo
        VARCHAR2 nombre_prenda
        VARCHAR2 numero
        VARCHAR2 tallaje
        VARCHAR2 tipo
        NUMBER cliente_id FK
        NUMBER especificacion_tecnica_id FK
        NUMBER vendedor_id FK
    }
    SOLICITUDES_COTIZACION {
        NUMBER idscot PK
        VARCHAR2 articulo_descripcion
        NUMBER cantidad
        NUMBER costo_total_calculado
        NUMBER es_muestra
        VARCHAR2 estado
        DATE fecha
        NUMBER has_logo
        VARCHAR2 moneda_costo_total
        VARCHAR2 numero
        VARCHAR2 tipo
        NUMBER venta_asociada_id
        NUMBER cliente_id FK
        NUMBER especificacion_tecnica_id FK
        NUMBER vendedor_id FK
    }
    TELA {
        NUMBER id_tela PK
        NUMBER activo
        TIMESTAMP actualizado_en
        TIMESTAMP creado_en
        VARCHAR2 composicion
        VARCHAR2 gramaje
        VARCHAR2 nombre_tela
    }
    TIPO_CUENTA_BANCARIA {
        NUMBER tipo_cuenta_id PK
        VARCHAR2 denominacion_cuenta
    }
    TIPO_DIRECCION {
        NUMBER tipo_direccion_id PK
        VARCHAR2 descripcion
    }
    TIPO_DIRECCION_SEQ {
        NUMBER next_val
    }
    TIPOS_CONTACTO {
        NUMBER tipo_contacto_id PK
        VARCHAR2 descripcion_tipo_contacto
    }
    TOMA_TALLAJE {
        NUMBER id_toma_tallaje PK
        NUMBER costo_total
        DATE fecha_programada
        CLOB metadata_json
        VARCHAR2 moneda
        CLOB observaciones
        NUMBER evaluacion_negocio_id FK
    }
    USUARIOS {
        NUMBER id_usuario PK
        NUMBER enabled
        VARCHAR2 telefono
        VARCHAR2 apellidos
        VARCHAR2 email
        VARCHAR2 nombre
        VARCHAR2 password
        VARCHAR2 run
    }
    USUARIOS_AREAS {
        NUMBER user_id FK
        NUMBER area_id FK
    }
    USUARIOS_ROLES {
        NUMBER user_id FK
        NUMBER role_id FK
    }
    VENDEDORES {
        NUMBER id_vendedor PK
        NUMBER activo
        TIMESTAMP actualizado_en
        TIMESTAMP creado_en
        VARCHAR2 codigo_vendedor
        NUMBER id_usuario FK
    }

    %% Relaciones
    PRODUCTO ||--o{ CATALOGO_PROVEEDOR : "producto_id"
    PROVEEDORES ||--o{ CATALOGO_PROVEEDOR : "proveedor_id"
    GIROS ||--o{ CLIENTES : "fk_giro"
    DATO_BANCARIO ||--o{ CLIENTES : "fk_dato_bancario"
    DIRECCION ||--o{ CLIENTES : "fk_direccion"
    REGION ||--o{ COMUNA : "region_id"
    CONFIGURACION_PLANTILLAS ||--o{ CONFIGURACION_PLANTILLA_ACCESORIOS : "configuracion_id"
    CONFIGURACION_PLANTILLAS ||--o{ CONFIGURACION_PLANTILLA_CAMPOS : "configuracion_id"
    CONFIGURACION_PLANTILLAS ||--o{ CONFIGURACION_PLANTILLA_TELAS : "configuracion_id"
    TIPOS_CONTACTO ||--o{ CONTACTOS : "tipo_contacto_id"
    PROVEEDORES ||--o{ CONTACTOS : "fk_provee_contacto"
    PROVEEDORES ||--o{ DATO_BANCARIO : "fk_provee_dato_bancario"
    TIPO_CUENTA_BANCARIA ||--o{ DATO_BANCARIO : "tipo_cuenta_id"
    BANCO ||--o{ DATO_BANCARIO : "banco_id"
    TIPO_DIRECCION ||--o{ DIRECCION : "tipo_direccion_id"
    PROVEEDORES ||--o{ DIRECCION : "fk_provee_direccion"
    COMUNA ||--o{ DIRECCION : "comuna_id"
    PRODUCTO ||--o{ ESPECIFICACION_TECNICA : "producto_id"
    PROVEEDORES ||--o{ EVALUACION_NEGOCIO_ITEMS : "proveedor_id"
    PRODUCTO ||--o{ EVALUACION_NEGOCIO_ITEMS : "producto_id"
    EVALUACIONES_NEGOCIO ||--o{ EVALUACION_NEGOCIO_ITEMS : "evaluacion_negocio_id"
    CLIENTES ||--o{ EVALUACIONES_NEGOCIO : "cliente_id"
    VENDEDORES ||--o{ EVALUACIONES_NEGOCIO : "vendedor_id"
    PRODUCCION_COSTEOS ||--o{ EVALUACIONES_NEGOCIO : "costeo_id"
    EVALUACIONES_NEGOCIO ||--o{ GASTOS_ADICIONALES : "evaluacion_negocio_id"
    RUBROS ||--o{ GIROS : "rubro_id"
    VENDEDORES ||--o{ NOTAS_VENTA : "vendedor_id"
    CLIENTES ||--o{ NOTAS_VENTA : "cliente_id"
    EVALUACIONES_NEGOCIO ||--o{ NOTAS_VENTA : "evaluacion_negocio_id"
    NOTAS_VENTA_ITEMS ||--o{ NOTAS_VENTA_ITEM_TALLAS : "item_id"
    NOTAS_VENTA ||--o{ NOTAS_VENTA_ITEMS : "nota_venta_id"
    PRODUCTO ||--o{ NOTAS_VENTA_ITEMS : "producto_id"
    PROVEEDORES ||--o{ NOTAS_VENTA_ITEMS : "proveedor_id"
    PRODUCCION_COSTEO_VERSIONES ||--o{ ORDEN_PRODUCCION : "costeo_version_id"
    NOTAS_VENTA ||--o{ ORDEN_PRODUCCION : "nota_venta_id"
    PRODUCCION_COSTEO_VERSIONES ||--o{ PRODUCCION_COSTEO_ITEM_VERSIONES : "costeo_version_id"
    PRODUCCION_COSTEOS ||--o{ PRODUCCION_COSTEO_ITEMS : "costeo_id"
    PRODUCCION_COSTEOS ||--o{ PRODUCCION_COSTEO_VERSIONES : "costeo_id"
    SOLICITUDES_COSTOS ||--o{ PRODUCCION_COSTEOS : "solicitud_costos_id"
    PRODUCCION_ORDENES_SERVICIO ||--o{ PRODUCCION_DESPACHOS_OS : "os_id"
    PRODUCCION_HOJA_COMPRA_ITEMS ||--o{ PRODUCCION_HC_ITEM_OC_ITEM : "hc_item_id"
    PRODUCCION_ORDEN_COMPRA_ITEMS ||--o{ PRODUCCION_HC_ITEM_OC_ITEM : "oc_item_id"
    PRODUCCION_HOJAS_COMPRA ||--o{ PRODUCCION_HOJA_COMPRA_ITEMS : "hc_id"
    PRODUCCION_COSTEO_VERSIONES ||--o{ PRODUCCION_HOJAS_COMPRA : "costeo_version_id"
    ORDEN_PRODUCCION ||--o{ PRODUCCION_HOJAS_COMPRA : "op_id"
    PRODUCCION_ORDENES_COMPRA ||--o{ PRODUCCION_ORDEN_COMPRA_ITEMS : "oc_id"
    ORDEN_PRODUCCION ||--o{ PRODUCCION_ORDEN_ITEMS : "orden_produccion_id"
    NOTAS_VENTA ||--o{ PRODUCCION_ORDEN_TRABAJO : "nota_venta_id"
    ORDEN_PRODUCCION ||--o{ PRODUCCION_ORDEN_TRABAJO : "orden_produccion_id"
    PROVEEDORES ||--o{ PRODUCCION_ORDENES_COMPRA : "proveedor_id"
    ORDEN_PRODUCCION ||--o{ PRODUCCION_ORDENES_SERVICIO : "op_id"
    PROVEEDORES ||--o{ PRODUCCION_ORDENES_SERVICIO : "proveedor_id"
    PRODUCCION_ORDEN_COMPRA_ITEMS ||--o{ PRODUCCION_RECEPCION_OC_ITEMS : "oc_item_id"
    PRODUCCION_RECEPCIONES_OC ||--o{ PRODUCCION_RECEPCION_OC_ITEMS : "recepcion_id"
    PRODUCCION_ORDENES_COMPRA ||--o{ PRODUCCION_RECEPCIONES_OC : "oc_id"
    PRODUCCION_ORDENES_SERVICIO ||--o{ PRODUCCION_RECEPCIONES_OS : "os_id"
    GIROS ||--o{ PROVEEDORES : "fk_provee_giro"
    PAIS ||--o{ REGION : "pais_id"
    ROLES ||--o{ ROL_PERMISOS : "rol_id"
    PERMISOS ||--o{ ROL_PERMISOS : "permiso_id"
    AREAS ||--o{ ROLES : "area_id"
    PROVEEDORES ||--o{ SCOS_ACCESORIOS : "proveedor_id"
    SOLICITUDES_COSTOS ||--o{ SCOS_ACCESORIOS : "solicitud_costos_id"
    ACCESORIO ||--o{ SCOS_ACCESORIOS : "accesorio_id"
    SOLICITUDES_COSTOS ||--o{ SCOS_COSTO_FIJO : "solicitud_costos_id"
    SOLICITUDES_COSTOS ||--o{ SCOS_LOGOTIPOS : "solicitud_costos_id"
    SOLICITUDES_COTIZACION ||--o{ SCOS_LOGOTIPOS : "solicitud_cotizacion_id"
    SOLICITUDES_COSTOS ||--o{ SCOS_PLANTILLA : "solicitud_costos_id"
    SCOS_PLANTILLA ||--o{ SCOS_PLANTILLA_ACCESORIOS : "plantilla_id"
    SCOS_PLANTILLA ||--o{ SCOS_PLANTILLA_CAMPOS_ACTIVOS : "plantilla_id"
    SCOS_PLANTILLA ||--o{ SCOS_PLANTILLA_LOGOTIPOS : "plantilla_id"
    SCOS_PLANTILLA ||--o{ SCOS_PLANTILLA_MATERIAL_VINCULOS : "plantilla_id"
    SCOS_PLANTILLA ||--o{ SCOS_PLANTILLA_TELAS : "plantilla_id"
    TELA ||--o{ SCOS_TELAS : "tela_id"
    PROVEEDORES ||--o{ SCOS_TELAS : "proveedor_id"
    SOLICITUDES_COSTOS ||--o{ SCOS_TELAS : "solicitud_costos_id"
    PRODUCTO ||--o{ SCOT_PRENDAS : "producto_id"
    SOLICITUDES_COTIZACION ||--o{ SCOT_PRENDAS : "solicitud_id"
    SOLICITUDES_COTIZACION ||--o{ SCOT_PRENDAS : "solicitud_cotizacion_id"
    SOLICITUDES_COSTOS ||--o{ SCOT_PRENDAS : "solicitud_costos_id"
    CLIENTES ||--o{ SOLICITUDES_COSTOS : "cliente_id"
    VENDEDORES ||--o{ SOLICITUDES_COSTOS : "vendedor_id"
    ESPECIFICACION_TECNICA ||--o{ SOLICITUDES_COSTOS : "especificacion_tecnica_id"
    ESPECIFICACION_TECNICA ||--o{ SOLICITUDES_COTIZACION : "especificacion_tecnica_id"
    CLIENTES ||--o{ SOLICITUDES_COTIZACION : "cliente_id"
    VENDEDORES ||--o{ SOLICITUDES_COTIZACION : "vendedor_id"
    EVALUACIONES_NEGOCIO ||--o{ TOMA_TALLAJE : "evaluacion_negocio_id"
    USUARIOS ||--o{ USUARIOS_AREAS : "user_id"
    AREAS ||--o{ USUARIOS_AREAS : "area_id"
    ROLES ||--o{ USUARIOS_ROLES : "role_id"
    USUARIOS ||--o{ USUARIOS_ROLES : "user_id"
    USUARIOS ||--o{ VENDEDORES : "id_usuario"