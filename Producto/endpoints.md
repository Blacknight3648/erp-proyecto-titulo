# Endpoints del Sistema ERP

## Resumen

| Módulo | Controladores | Endpoints |
|---|---|---|
| Comercial | 5 | 28 |
| Gestión Usuarios | 7 | 68 |
| Producción | 8 | 45 |
| Compartido (Shared) | 15 | 78 |
| **Total** | **35** | **219** |

---

## Módulo Comercial

### Evaluaciones de Negocio

| Método HTTP | Ruta | Descripción | Datos de entrada | Respuestas |
|---|---|---|---|---|
| GET | `/api/v1/comercial/evaluaciones-negocio` | Lista todas las evaluaciones de negocio. | N/A | 200 OK |
| GET | `/api/v1/comercial/evaluaciones-negocio/next-number` | Obtiene siguiente número tentativo de EVN. | N/A | 200 OK (Long) |
| POST | `/api/v1/comercial/evaluaciones-negocio` | Crea una evaluación de negocio. | `CrearEVNCommand { campos }` | 200 OK |
| GET | `/api/v1/comercial/evaluaciones-negocio/{id}` | Obtiene una EVN por ID. | N/A | 200 OK, 404 No encontrada |
| PATCH | `/api/v1/comercial/evaluaciones-negocio/{id}/adjudicar` | Adjudica una evaluación de negocio. | `FirmaAprobacionRequest { ... }` (opcional) | 200 OK |
| PATCH | `/api/v1/comercial/evaluaciones-negocio/{id}/aprobar` | Aprueba una evaluación de negocio. | `FirmaAprobacionRequest { "aprobador", "observacion" }` | 200 OK |
| PATCH | `/api/v1/comercial/evaluaciones-negocio/{id}/rechazar` | Rechaza una evaluación de negocio. | `FirmaAprobacionRequest { "aprobador", "motivo" }` | 200 OK |
| GET | `/api/v1/comercial/evaluaciones-negocio/{id}/historial` | Historial de cambios de estado. | N/A | 200 OK |

### Notas de Venta

| Método HTTP | Ruta | Descripción | Datos de entrada | Respuestas |
|---|---|---|---|---|
| GET | `/api/v1/comercial/notas-venta` | Lista todas las notas de venta. | N/A | 200 OK |
| GET | `/api/v1/comercial/notas-venta/next-number` | Obtiene siguiente número tentativo de NV. | N/A | 200 OK (Long) |
| POST | `/api/v1/comercial/notas-venta` | Crea una nota de venta. | `CrearNVCommand { campos de NV }` | 200 OK |
| GET | `/api/v1/comercial/notas-venta/{id}` | Obtiene una NV por ID. | N/A | 200 OK, 404 No encontrada |
| GET | `/api/v1/comercial/notas-venta/{id}/trazabilidad` | Consulta trazabilidad de la NV. | N/A | 200 OK |
| PATCH | `/api/v1/comercial/notas-venta/{id}/aprobar` | Aprueba una nota de venta. | `FirmaAprobacionRequest { "aprobador", "observacion" }` | 200 OK |
| PATCH | `/api/v1/comercial/notas-venta/{id}/cancelar` | Cancela una nota de venta. | `FirmaAprobacionRequest { "aprobador", "motivo" }` | 200 OK |
| GET | `/api/v1/comercial/notas-venta/{id}/historial` | Historial de cambios de estado. | N/A | 200 OK |

### Solicitud de Costos

| Método HTTP | Ruta | Descripción | Datos de entrada | Respuestas |
|---|---|---|---|---|
| POST | `/api/v1/solicitudes-costos` | Crea una solicitud de costos. | `SolicitudCostosCreateDTO { campos }` | 201 Creado |
| PUT | `/api/v1/solicitudes-costos/{id}` | Actualiza una solicitud de costos. | `SolicitudCostosCreateDTO { campos }` | 200 OK |
| GET | `/api/v1/solicitudes-costos/{id}` | Obtiene una solicitud por ID. | N/A | 200 OK, 404 No encontrada |
| GET | `/api/v1/solicitudes-costos` | Lista todas las solicitudes de costos. | N/A | 200 OK |
| DELETE | `/api/v1/solicitudes-costos/{id}` | Elimina una solicitud de costos. | N/A | 204 Sin contenido |

### Solicitud de Cotizaciones

| Método HTTP | Ruta | Descripción | Datos de entrada | Respuestas |
|---|---|---|---|---|
| POST | `/api/v1/solicitudes-cotizaciones` | Crea una solicitud de cotización. | `SolicitudCotizacionesCreateDTO { campos }` | 201 Creado |
| PUT | `/api/v1/solicitudes-cotizaciones/{id}` | Actualiza una solicitud de cotización. | `SolicitudCotizacionesCreateDTO { campos }` | 200 OK |
| GET | `/api/v1/solicitudes-cotizaciones/{id}` | Obtiene una cotización por ID. | N/A | 200 OK, 404 No encontrada |
| GET | `/api/v1/solicitudes-cotizaciones` | Lista todas las solicitudes de cotización. | N/A | 200 OK |
| DELETE | `/api/v1/solicitudes-cotizaciones/{id}` | Elimina una solicitud de cotización. | N/A | 204 Sin contenido |

### Configuración de Plantillas

| Método HTTP | Ruta | Descripción | Datos de entrada | Respuestas |
|---|---|---|---|---|
| GET | `/api/v1/configuracion-plantillas` | Lista todas las configuraciones de plantilla. | N/A | 200 OK |
| GET | `/api/v1/configuracion-plantillas/by-nombre` | Busca configuración por nombre de prenda. | `?nombre=` (query param) | 200 OK |
| POST | `/api/v1/configuracion-plantillas` | Crea una configuración de plantilla. | `ConfiguracionPlantillaDTO { campos }` | 200 OK |
| DELETE | `/api/v1/configuracion-plantillas/{id}` | Elimina una configuración de plantilla. | N/A | 200 OK |

---

## Módulo de Producción

### Órdenes de Producción

| Método HTTP | Ruta | Descripción | Datos de entrada | Respuestas |
|---|---|---|---|---|
| GET | `/api/v1/produccion/ordenes-produccion` | Lista todas las órdenes de producción. | N/A | 200 OK |
| GET | `/api/v1/produccion/ordenes-produccion/{id}` | Obtiene una orden de producción por ID. | N/A | 200 OK, 404 No encontrada |
| POST | `/api/v1/produccion/ordenes-produccion/recepcionar/{id}` | Recepciona una orden de producción. | N/A | 200 OK, 404 No encontrada |
| GET | `/api/v1/produccion/ordenes-produccion/{id}/avance` | Consulta el avance de una OP. | N/A | 200 OK |

### Órdenes de Trabajo

| Método HTTP | Ruta | Descripción | Datos de entrada | Respuestas |
|---|---|---|---|---|
| GET | `/api/v1/produccion/ordenes-trabajo/nota-venta/{nvId}` | Lista OTs por Nota de Venta. | N/A | 200 OK |
| GET | `/api/v1/produccion/ordenes-trabajo/orden-produccion/{opId}` | Lista OTs de una Orden de Producción. | N/A | 200 OK |
| POST | `/api/v1/produccion/ordenes-trabajo/{id}/iniciar` | Inicia una OT. | N/A | 200 OK |
| POST | `/api/v1/produccion/ordenes-trabajo/{id}/finalizar` | Finaliza una OT manualmente. | N/A | 200 OK |
| POST | `/api/v1/produccion/ordenes-trabajo/{id}/avance` | Registra avance/merma del día en una OT. | `RegistrarAvanceCommand { "cantidadBuena", "cantidadMerma", ... }` | 200 OK |
| GET | `/api/v1/produccion/ordenes-trabajo/{id}/avances` | Lista avances registrados de una OT. | N/A | 200 OK |

### Costeos

| Método HTTP | Ruta | Descripción | Datos de entrada | Respuestas |
|---|---|---|---|---|
| GET | `/api/v1/produccion/costeos/scos/{scosId}` | Obtiene costeo por Solicitud de Costos. | N/A | 200 OK, 404 No encontrado |
| GET | `/api/v1/produccion/costeos/scos/{scosId}/all` | Lista todos los costeos por SCOS. | N/A | 200 OK |
| POST | `/api/v1/produccion/costeos` | Registra un nuevo costeo. | `CosteoDTO { campos de costeo }` | 200 OK |

### Hojas de Compra

| Método HTTP | Ruta | Descripción | Datos de entrada | Respuestas |
|---|---|---|---|---|
| POST | `/api/v1/hojas-compra/generar/{opId}` | Genera una Hoja de Compra desde una OP. | N/A | 201 Creado |
| GET | `/api/v1/hojas-compra` | Lista hojas de compra. | `?estado=` (query param opcional: EstadoHC) | 200 OK |
| GET | `/api/v1/hojas-compra/{idHC}` | Obtiene una Hoja de Compra por ID. | N/A | 200 OK, 404 No encontrada |
| GET | `/api/v1/hojas-compra/op/{opId}` | Obtiene Hoja de Compra por Orden de Producción. | N/A | 200 OK, 404 No encontrada |
| PATCH | `/api/v1/hojas-compra/{idHC}/aprobar` | Aprueba una Hoja de Compra. | N/A | 200 OK |
| PATCH | `/api/v1/hojas-compra/{idHC}/cerrar` | Cierra una Hoja de Compra. | N/A | 200 OK |

### Órdenes de Compra

| Método HTTP | Ruta | Descripción | Datos de entrada | Respuestas |
|---|---|---|---|---|
| POST | `/api/v1/ordenes-compra/consolidar` | Genera una OC consolidada. | `GenerarOCConsolidadaRequest { campos }` | 201 Creado |
| GET | `/api/v1/ordenes-compra` | Lista órdenes de compra. | `?estado=`, `?proveedorId=`, `?hcItemId=` (query params opcionales) | 200 OK |
| GET | `/api/v1/ordenes-compra/{idOC}` | Obtiene una OC por ID. | N/A | 200 OK, 404 No encontrada |
| PATCH | `/api/v1/ordenes-compra/{idOC}/enviar` | Marca OC como enviada al proveedor. | N/A | 200 OK |
| PATCH | `/api/v1/ordenes-compra/{idOC}/recepcionar` | Marca OC como recepcionada. | N/A | 200 OK |
| PATCH | `/api/v1/ordenes-compra/{idOC}/cerrar` | Cierra una OC. | N/A | 200 OK |
| PATCH | `/api/v1/ordenes-compra/{idOC}/items/{idOCItem}/precio` | Actualiza precio de un ítem de OC. | `?precio=` (query param, BigDecimal) | 200 OK |

### Órdenes de Servicio

| Método HTTP | Ruta | Descripción | Datos de entrada | Respuestas |
|---|---|---|---|---|
| POST | `/api/v1/ordenes-servicio` | Crea una orden de servicio. | `CrearOSRequest { campos }` | 201 Creado |
| GET | `/api/v1/ordenes-servicio` | Lista órdenes de servicio. | `?estado=`, `?opId=`, `?proveedorId=` (query params opcionales) | 200 OK |
| GET | `/api/v1/ordenes-servicio/{idOS}` | Obtiene una OS por ID. | N/A | 200 OK, 404 No encontrada |
| POST | `/api/v1/ordenes-servicio/{idOS}/despachos` | Registra un despacho de OS. | `DespachoOSDTO { campos }` | 200 OK |
| POST | `/api/v1/ordenes-servicio/{idOS}/recepciones` | Registra una recepción de OS. | `RecepcionOSDTO { campos }` | 200 OK |
| PATCH | `/api/v1/ordenes-servicio/{idOS}/cerrar` | Cierra una OS. | N/A | 200 OK |

### Recepciones de OC

| Método HTTP | Ruta | Descripción | Datos de entrada | Respuestas |
|---|---|---|---|---|
| POST | `/api/v1/recepciones-oc/oc/{ocId}` | Registra una recepción de OC. | `RecepcionOCDTO { campos }` | 200 OK |
| GET | `/api/v1/recepciones-oc/{idRecepcion}` | Obtiene una recepción por ID. | N/A | 200 OK, 404 No encontrada |
| GET | `/api/v1/recepciones-oc/oc/{ocId}` | Lista recepciones por OC. | N/A | 200 OK |

### Reportes

| Método HTTP | Ruta | Descripción | Datos de entrada | Respuestas |
|---|---|---|---|---|
| GET | `/api/v1/reportes/hcs-pendientes-aprobacion` | Lista HCs pendientes de aprobación. | N/A | 200 OK |
| GET | `/api/v1/reportes/ocs-pendientes-recepcion` | Lista OCs pendientes de recepción. | N/A | 200 OK |
| GET | `/api/v1/reportes/oss-en-taller` | Lista OSs activas en taller. | N/A | 200 OK |

### Trazabilidad

| Método HTTP | Ruta | Descripción | Datos de entrada | Respuestas |
|---|---|---|---|---|
| GET | `/api/v1/trazabilidad/op/{opId}` | Obtiene trazabilidad por Orden de Producción. | N/A | 200 OK |

---

## Módulo de Gestión de Usuarios

### Usuarios

| Método HTTP | Ruta | Descripción | Datos de entrada | Respuestas |
|---|---|---|---|---|
| GET | `/api/v1/usuarios` | Lista todos los usuarios. | N/A | 200 OK |
| GET | `/api/v1/usuarios/{id}` | Obtiene un usuario por ID. | N/A | 200 OK, 404 No encontrado |
| POST | `/api/v1/usuarios` | Crea un usuario. | `CreateUserDTO { campos }` | 201 Creado |
| PUT | `/api/v1/usuarios/{id}` | Actualiza un usuario. | `CreateUserDTO { campos }` | 200 OK |
| DELETE | `/api/v1/usuarios/{id}` | Elimina un usuario. | N/A | 204 Sin contenido |
| PATCH | `/api/v1/usuarios/{id}/toggle-enabled` | Activa o desactiva un usuario. | N/A | 200 OK |
| PUT | `/api/v1/usuarios/{id}/roles` | Asigna roles a un usuario. | `Set<String> (nombres de roles)` | 200 OK |
| PUT | `/api/v1/usuarios/{id}/areas` | Asigna áreas a un usuario. | `Set<String> (nombres de áreas)` | 200 OK |

### Roles

| Método HTTP | Ruta | Descripción | Datos de entrada | Respuestas |
|---|---|---|---|---|
| GET | `/api/v1/roles` | Lista todos los roles. | N/A | 200 OK |
| GET | `/api/v1/roles/{id}` | Obtiene un rol por ID. | N/A | 200 OK, 404 No encontrado |
| POST | `/api/v1/roles` | Crea un rol. | `RoleDTO { campos }` | 201 Creado |
| PUT | `/api/v1/roles/{id}` | Actualiza un rol. | `RoleDTO { campos }` | 200 OK |
| PATCH | `/api/v1/roles/{id}` | Actualiza parcialmente un rol. | `RoleDTO { campos parciales }` | 200 OK |
| DELETE | `/api/v1/roles/{id}` | Elimina un rol. | N/A | 204 Sin contenido |

### Áreas

| Método HTTP | Ruta | Descripción | Datos de entrada | Respuestas |
|---|---|---|---|---|
| GET | `/api/v1/areas` | Lista todas las áreas. | N/A | 200 OK |
| GET | `/api/v1/areas/{id}` | Obtiene un área por ID. | N/A | 200 OK, 404 No encontrada |
| POST | `/api/v1/areas` | Crea un área. | `AreaDTO { campos }` | 201 Creado |
| PUT | `/api/v1/areas/{id}` | Actualiza un área. | `AreaDTO { campos }` | 200 OK |
| PATCH | `/api/v1/areas/{id}` | Actualiza parcialmente un área. | `AreaDTO { campos parciales }` | 200 OK |
| DELETE | `/api/v1/areas/{id}` | Elimina un área. | N/A | 204 Sin contenido |

### Clientes

| Método HTTP | Ruta | Descripción | Datos de entrada | Respuestas |
|---|---|---|---|---|
| GET | `/api/v1/clientes` | Lista todos los clientes. | N/A | 200 OK |
| GET | `/api/v1/clientes/{clienteId}` | Obtiene un cliente por ID. | N/A | 200 OK, 404 No encontrado |
| GET | `/api/v1/clientes/run/{runCliente}` | Obtiene un cliente por RUN. | N/A | 200 OK, 404 No encontrado |
| GET | `/api/v1/clientes/razon-social/{razonSocial}` | Busca clientes por razón social. | N/A | 200 OK |
| GET | `/api/v1/clientes/activos` | Lista clientes activos. | N/A | 200 OK |
| GET | `/api/v1/clientes/inactivos` | Lista clientes inactivos. | N/A | 200 OK |
| GET | `/api/v1/clientes/giro/{giroId}` | Lista clientes por giro. | N/A | 200 OK |
| GET | `/api/v1/clientes/sigla/{sigla}` | Obtiene clientes por sigla. | N/A | 200 OK |
| GET | `/api/v1/clientes/giro/descripcion/{descripcionGiro}` | Obtiene clientes por descripción de giro. | N/A | 200 OK |
| GET | `/api/v1/clientes/activos/sigla/{sigla}` | Lista clientes activos por sigla. | N/A | 200 OK |
| GET | `/api/v1/clientes/activos/giro/{giroId}` | Lista clientes activos por giro. | N/A | 200 OK |
| POST | `/api/v1/clientes` | Crea un cliente. | `ClienteDTO { campos }` | 201 Creado |
| PUT | `/api/v1/clientes/{clienteId}` | Actualiza un cliente. | `ClienteDTO { campos }` | 200 OK |
| DELETE | `/api/v1/clientes/{clienteId}` | Elimina un cliente. | N/A | 204 Sin contenido |

### Proveedores

| Método HTTP | Ruta | Descripción | Datos de entrada | Respuestas |
|---|---|---|---|---|
| GET | `/api/v1/proveedores` | Lista todos los proveedores. | N/A | 200 OK |
| GET | `/api/v1/proveedores/{proveedorId}` | Obtiene un proveedor por ID. | N/A | 200 OK, 404 No encontrado |
| GET | `/api/v1/proveedores/run/{runProveedor}` | Obtiene un proveedor por RUN. | N/A | 200 OK, 404 No encontrado |
| GET | `/api/v1/proveedores/razon-social/{razonSocial}` | Busca proveedores por razón social. | N/A | 200 OK |
| GET | `/api/v1/proveedores/activos` | Lista proveedores activos. | N/A | 200 OK |
| GET | `/api/v1/proveedores/inactivos` | Lista proveedores inactivos. | N/A | 200 OK |
| GET | `/api/v1/proveedores/giro/{giroId}` | Lista proveedores por giro. | N/A | 200 OK |
| GET | `/api/v1/proveedores/sigla/{sigla}` | Obtiene proveedores por sigla. | N/A | 200 OK |
| GET | `/api/v1/proveedores/giro/descripcion/{descripcionGiro}` | Obtiene proveedores por descripción de giro. | N/A | 200 OK |
| GET | `/api/v1/proveedores/activos/sigla/{sigla}` | Lista proveedores activos por sigla. | N/A | 200 OK |
| GET | `/api/v1/proveedores/activos/giro/{giroId}` | Lista proveedores activos por giro. | N/A | 200 OK |
| POST | `/api/v1/proveedores` | Crea un proveedor. | `ProveedorDTO { campos }` | 201 Creado |
| PUT | `/api/v1/proveedores/{proveedorId}` | Actualiza un proveedor. | `ProveedorDTO { campos }` | 200 OK |
| DELETE | `/api/v1/proveedores/{proveedorId}` | Elimina un proveedor. | N/A | 204 Sin contenido |

### Vendedores

| Método HTTP | Ruta | Descripción | Datos de entrada | Respuestas |
|---|---|---|---|---|
| GET | `/api/v1/vendedores` | Lista todos los vendedores. | N/A | 200 OK |
| GET | `/api/v1/vendedores/{id}` | Obtiene un vendedor por ID. | N/A | 200 OK, 404 No encontrado |
| GET | `/api/v1/vendedores/usuario/{usuarioId}` | Obtiene vendedor por ID de usuario. | N/A | 200 OK, 404 No encontrado |
| POST | `/api/v1/vendedores` | Crea un vendedor. | `VendedorCreateDTO { campos }` | 201 Creado |
| PUT | `/api/v1/vendedores/{id}` | Actualiza un vendedor. | `VendedorCreateDTO { campos }` | 200 OK |
| DELETE | `/api/v1/vendedores/{id}` | Elimina un vendedor. | N/A | 204 Sin contenido |

---

## Módulo Compartido (Shared)

### Productos

| Método HTTP | Ruta | Descripción | Datos de entrada | Respuestas |
|---|---|---|---|---|
| GET | `/api/v1/shared/productos` | Lista todos los productos. | N/A | 200 OK |
| GET | `/api/v1/shared/productos/{id}` | Obtiene un producto por ID. | N/A | 200 OK, 404 No encontrado |
| POST | `/api/v1/shared/productos` | Crea un nuevo producto. | `Producto { campos del producto }` | 201 Creado |
| DELETE | `/api/v1/shared/productos/{id}` | Elimina un producto. | N/A | 204 Sin contenido, 404 No encontrado |

### Permisos

| Método HTTP | Ruta | Descripción | Datos de entrada | Respuestas |
|---|---|---|---|---|
| GET | `/api/v1/permisos` | Lista todos los permisos del sistema. | N/A | 200 OK |

### Historial de Estado

| Método HTTP | Ruta | Descripción | Datos de entrada | Respuestas |
|---|---|---|---|---|
| GET | `/api/v1/historial-estado/{tipoEntidad}/{entidadId}` | Consulta historial de cambios de estado. | N/A | 200 OK |

### Bancos

| Método HTTP | Ruta | Descripción | Datos de entrada | Respuestas |
|---|---|---|---|---|
| GET | `/api/v1/bancos` | Lista todos los bancos. | N/A | 200 OK |
| GET | `/api/v1/bancos/{id}` | Obtiene un banco por ID. | N/A | 200 OK, 404 No encontrado |
| POST | `/api/v1/bancos` | Crea un banco. | `BancoDTO { campos }` | 201 Creado |
| PUT | `/api/v1/bancos/{id}` | Actualiza un banco. | `BancoDTO { campos }` | 200 OK |
| DELETE | `/api/v1/bancos/{id}` | Elimina un banco. | N/A | 204 Sin contenido |

### Comunas

| Método HTTP | Ruta | Descripción | Datos de entrada | Respuestas |
|---|---|---|---|---|
| GET | `/api/v1/comunas` | Lista todas las comunas. | N/A | 200 OK |
| GET | `/api/v1/comunas/{id}` | Obtiene una comuna por ID. | N/A | 200 OK, 404 No encontrada |
| GET | `/api/v1/comunas/region/{regionId}` | Lista comunas por región. | N/A | 200 OK |
| POST | `/api/v1/comunas` | Crea una comuna. | `ComunaDTO { campos }` | 201 Creado |
| PUT | `/api/v1/comunas/{id}` | Actualiza una comuna. | `ComunaDTO { campos }` | 200 OK |
| DELETE | `/api/v1/comunas/{id}` | Elimina una comuna. | N/A | 204 Sin contenido |

### Contactos

| Método HTTP | Ruta | Descripción | Datos de entrada | Respuestas |
|---|---|---|---|---|
| GET | `/api/v1/contacto` | Lista todos los contactos. | N/A | 200 OK |
| GET | `/api/v1/contacto/{id}` | Obtiene un contacto por ID. | N/A | 200 OK, 404 No encontrado |
| POST | `/api/v1/contacto` | Crea un contacto. | `ContactoDTO { campos }` | 201 Creado |
| PUT | `/api/v1/contacto/{id}` | Actualiza un contacto. | `ContactoDTO { campos }` | 200 OK |
| DELETE | `/api/v1/contacto/{id}` | Elimina un contacto. | N/A | 204 Sin contenido |

### Datos Bancarios

| Método HTTP | Ruta | Descripción | Datos de entrada | Respuestas |
|---|---|---|---|---|
| GET | `/api/v1/datos-bancarios` | Lista todos los datos bancarios. | N/A | 200 OK |
| GET | `/api/v1/datos-bancarios/{id}` | Obtiene un dato bancario por ID. | N/A | 200 OK, 404 No encontrado |
| GET | `/api/v1/datos-bancarios/banco/{bancoId}` | Lista datos bancarios por banco. | N/A | 200 OK |
| POST | `/api/v1/datos-bancarios` | Crea un dato bancario. | `DatoBancarioDTO { campos }` | 201 Creado |
| PUT | `/api/v1/datos-bancarios/{id}` | Actualiza un dato bancario. | `DatoBancarioDTO { campos }` | 200 OK |
| DELETE | `/api/v1/datos-bancarios/{id}` | Elimina un dato bancario. | N/A | 204 Sin contenido |

### Direcciones

| Método HTTP | Ruta | Descripción | Datos de entrada | Respuestas |
|---|---|---|---|---|
| GET | `/api/v1/direcciones` | Lista todas las direcciones. | N/A | 200 OK |
| GET | `/api/v1/direcciones/{id}` | Obtiene una dirección por ID. | N/A | 200 OK, 404 No encontrada |
| GET | `/api/v1/direcciones/comuna/{comunaId}` | Lista direcciones por comuna. | N/A | 200 OK |
| POST | `/api/v1/direcciones` | Crea una dirección. | `DireccionDTO { campos }` | 201 Creado |
| PUT | `/api/v1/direcciones/{id}` | Actualiza una dirección. | `DireccionDTO { campos }` | 200 OK |
| DELETE | `/api/v1/direcciones/{id}` | Elimina una dirección. | N/A | 204 Sin contenido |

### Giros

| Método HTTP | Ruta | Descripción | Datos de entrada | Respuestas |
|---|---|---|---|---|
| GET | `/api/v1/giros` | Lista todos los giros. | N/A | 200 OK |
| GET | `/api/v1/giros/{giroId}` | Obtiene un giro por ID. | N/A | 200 OK, 404 No encontrado |
| GET | `/api/v1/giros/codigo/{codigoSii}` | Obtiene giro por código SII. | N/A | 200 OK, 404 No encontrado |
| GET | `/api/v1/giros/nombre/{nombreGiro}` | Busca giro por nombre. | N/A | 200 OK |
| GET | `/api/v1/giros/descripcion/{descripcionGiro}` | Obtiene giro por descripción. | N/A | 200 OK, 404 No encontrado |
| GET | `/api/v1/giros/obtenerocrear/{nombreGiro}` | Obtiene o crea un giro por nombre. | N/A | 200 OK |
| POST | `/api/v1/giros` | Crea un giro. | `GiroDTO { campos }` | 201 Creado |
| PUT | `/api/v1/giros/{giroId}` | Actualiza un giro. | `GiroDTO { campos }` | 200 OK |
| DELETE | `/api/v1/giros/{giroId}` | Elimina un giro. | N/A | 204 Sin contenido |

### Países

| Método HTTP | Ruta | Descripción | Datos de entrada | Respuestas |
|---|---|---|---|---|
| GET | `/api/v1/paises` | Lista todos los países. | N/A | 200 OK |
| GET | `/api/v1/paises/{id}` | Obtiene un país por ID. | N/A | 200 OK, 404 No encontrado |
| POST | `/api/v1/paises` | Crea un país. | `PaisDTO { campos }` | 201 Creado |
| PUT | `/api/v1/paises/{id}` | Actualiza un país. | `PaisDTO { campos }` | 200 OK |
| DELETE | `/api/v1/paises/{id}` | Elimina un país. | N/A | 204 Sin contenido |

### Regiones

| Método HTTP | Ruta | Descripción | Datos de entrada | Respuestas |
|---|---|---|---|---|
| GET | `/api/v1/regiones` | Lista todas las regiones. | N/A | 200 OK |
| GET | `/api/v1/regiones/{id}` | Obtiene una región por ID. | N/A | 200 OK, 404 No encontrada |
| GET | `/api/v1/regiones/pais/{paisId}` | Lista regiones por país. | N/A | 200 OK |
| POST | `/api/v1/regiones` | Crea una región. | `RegionDTO { campos }` | 201 Creado |
| PUT | `/api/v1/regiones/{id}` | Actualiza una región. | `RegionDTO { campos }` | 200 OK |
| DELETE | `/api/v1/regiones/{id}` | Elimina una región. | N/A | 204 Sin contenido |

### Rubros

| Método HTTP | Ruta | Descripción | Datos de entrada | Respuestas |
|---|---|---|---|---|
| GET | `/api/v1/rubros` | Lista todos los rubros. | N/A | 200 OK |
| GET | `/api/v1/rubros/{id}` | Obtiene un rubro por ID. | N/A | 200 OK, 404 No encontrado |
| GET | `/api/v1/rubros/nombre/{nombreRubro}` | Obtiene rubro por nombre. | N/A | 200 OK, 404 No encontrado |
| POST | `/api/v1/rubros` | Crea un rubro. | `RubroDTO { campos }` | 201 Creado |
| PUT | `/api/v1/rubros/{id}` | Actualiza un rubro. | `RubroDTO { campos }` | 200 OK |
| DELETE | `/api/v1/rubros/{id}` | Elimina un rubro. | N/A | 204 Sin contenido |

### Tipos de Contacto

| Método HTTP | Ruta | Descripción | Datos de entrada | Respuestas |
|---|---|---|---|---|
| GET | `/api/v1/tipo-contacto` | Lista todos los tipos de contacto. | N/A | 200 OK |
| GET | `/api/v1/tipo-contacto/{id}` | Obtiene un tipo de contacto por ID. | N/A | 200 OK, 404 No encontrado |
| GET | `/api/v1/tipo-contacto/descripcion/{descripcionTipoContacto}` | Obtiene tipo de contacto por descripción. | N/A | 200 OK, 404 No encontrado |
| POST | `/api/v1/tipo-contacto` | Crea un tipo de contacto. | `TipoContactoDTO { campos }` | 201 Creado |
| PUT | `/api/v1/tipo-contacto/{id}` | Actualiza un tipo de contacto. | `TipoContactoDTO { campos }` | 200 OK |
| DELETE | `/api/v1/tipo-contacto/{id}` | Elimina un tipo de contacto. | N/A | 204 Sin contenido |

### Tipos de Cuenta Bancaria

| Método HTTP | Ruta | Descripción | Datos de entrada | Respuestas |
|---|---|---|---|---|
| GET | `/api/v1/tipos-cuenta-bancaria` | Lista todos los tipos de cuenta bancaria. | N/A | 200 OK |
| GET | `/api/v1/tipos-cuenta-bancaria/{id}` | Obtiene un tipo de cuenta por ID. | N/A | 200 OK, 404 No encontrado |
| POST | `/api/v1/tipos-cuenta-bancaria` | Crea un tipo de cuenta bancaria. | `TipoCuentaBancariaDTO { campos }` | 201 Creado |
| PUT | `/api/v1/tipos-cuenta-bancaria/{id}` | Actualiza un tipo de cuenta bancaria. | `TipoCuentaBancariaDTO { campos }` | 200 OK |
| DELETE | `/api/v1/tipos-cuenta-bancaria/{id}` | Elimina un tipo de cuenta bancaria. | N/A | 204 Sin contenido |

### Tipos de Dirección

| Método HTTP | Ruta | Descripción | Datos de entrada | Respuestas |
|---|---|---|---|---|
| GET | `/api/v1/tipos-direccion` | Lista todos los tipos de dirección. | N/A | 200 OK |
| GET | `/api/v1/tipos-direccion/{id}` | Obtiene un tipo de dirección por ID. | N/A | 200 OK, 404 No encontrado |
| POST | `/api/v1/tipos-direccion` | Crea un tipo de dirección. | `TipoDireccionDTO { campos }` | 201 Creado |
| PUT | `/api/v1/tipos-direccion/{id}` | Actualiza un tipo de dirección. | `TipoDireccionDTO { campos }` | 200 OK |
| DELETE | `/api/v1/tipos-direccion/{id}` | Elimina un tipo de dirección. | N/A | 204 Sin contenido |
