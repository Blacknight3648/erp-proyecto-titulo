# Endpoints del Sistema ERP

## Resumen

| Módulo | Controladores | Endpoints |
|---|---|---|
| Comercial | 5 | 28 |
| Gestión Usuarios | 7 | 68 |
| Producción | 8 | 46 |
| Compartido (Shared) | 15 | 78 |
| Maestros | 12 | 75 |
| **Total** | **47** | **295** |

> **Nota Postman:** la columna *Ejemplo (Postman)* muestra el request listo para copiar.
> Para GET/DELETE/PATCH sin cuerpo se muestra la URL con valores de muestra; para POST/PUT se
> muestra el cuerpo JSON. Anteponer el host base, p. ej. `http://localhost:8080`.

---

## Módulo Comercial

### Evaluaciones de Negocio

| Método HTTP | Ruta | Descripción | Datos de entrada | Respuestas | Ejemplo (Postman) |
|---|---|---|---|---|---|
| GET | `/api/v1/comercial/evaluaciones-negocio` | Lista todas las evaluaciones de negocio. | N/A | 200 OK | `GET /api/v1/comercial/evaluaciones-negocio` |
| GET | `/api/v1/comercial/evaluaciones-negocio/next-number` | Obtiene siguiente número tentativo de EVN. | N/A | 200 OK (Long) | `GET /api/v1/comercial/evaluaciones-negocio/next-number` |
| POST | `/api/v1/comercial/evaluaciones-negocio` | Crea una evaluación de negocio. | `CrearEVNCommand { campos }` | 200 OK | `{ "clienteId": 1, "vendedorId": 1, "costeoId": 1, "clienteNombre": "Comercial ABC", "referencia": "Poleras corporativas", "estado": "BORRADOR", "porcentajeComision": 5, "flete": 20000, "items": [] }` |
| GET | `/api/v1/comercial/evaluaciones-negocio/{id}` | Obtiene una EVN por ID. | N/A | 200 OK, 404 No encontrada | `GET /api/v1/comercial/evaluaciones-negocio/1` |
| PATCH | `/api/v1/comercial/evaluaciones-negocio/{id}/adjudicar` | Adjudica una evaluación de negocio. | `FirmaAprobacionRequest { ... }` (opcional) | 200 OK | `PATCH /api/v1/comercial/evaluaciones-negocio/1/adjudicar` → `{ "aprobador": "jperez", "observacion": "Adjudicada" }` |
| PATCH | `/api/v1/comercial/evaluaciones-negocio/{id}/aprobar` | Aprueba una evaluación de negocio. | `FirmaAprobacionRequest { "aprobador", "observacion" }` | 200 OK | `PATCH /api/v1/comercial/evaluaciones-negocio/1/aprobar` → `{ "aprobador": "jperez", "observacion": "OK" }` |
| PATCH | `/api/v1/comercial/evaluaciones-negocio/{id}/rechazar` | Rechaza una evaluación de negocio. | `FirmaAprobacionRequest { "aprobador", "motivo" }` | 200 OK | `PATCH /api/v1/comercial/evaluaciones-negocio/1/rechazar` → `{ "aprobador": "jperez", "motivo": "Fuera de presupuesto" }` |
| GET | `/api/v1/comercial/evaluaciones-negocio/{id}/historial` | Historial de cambios de estado. | N/A | 200 OK | `GET /api/v1/comercial/evaluaciones-negocio/1/historial` |

### Notas de Venta

| Método HTTP | Ruta | Descripción | Datos de entrada | Respuestas | Ejemplo (Postman) |
|---|---|---|---|---|---|
| GET | `/api/v1/comercial/notas-venta` | Lista todas las notas de venta. | N/A | 200 OK | `GET /api/v1/comercial/notas-venta` |
| GET | `/api/v1/comercial/notas-venta/next-number` | Obtiene siguiente número tentativo de NV. | N/A | 200 OK (Long) | `GET /api/v1/comercial/notas-venta/next-number` |
| POST | `/api/v1/comercial/notas-venta` | Crea una nota de venta. | `CrearNVCommand { campos de NV }` | 200 OK | `{ "clienteId": 1, "vendedorId": 1, "esKit": false, "fechaEntregaEstimada": "2026-07-01", "items": [] }` |
| GET | `/api/v1/comercial/notas-venta/{id}` | Obtiene una NV por ID. | N/A | 200 OK, 404 No encontrada | `GET /api/v1/comercial/notas-venta/1` |
| GET | `/api/v1/comercial/notas-venta/{id}/trazabilidad` | Consulta trazabilidad de la NV. | N/A | 200 OK | `GET /api/v1/comercial/notas-venta/1/trazabilidad` |
| PATCH | `/api/v1/comercial/notas-venta/{id}/aprobar` | Aprueba una nota de venta. | `FirmaAprobacionRequest { "aprobador", "observacion" }` | 200 OK | `PATCH /api/v1/comercial/notas-venta/1/aprobar` → `{ "aprobador": "jperez", "observacion": "OK" }` |
| PATCH | `/api/v1/comercial/notas-venta/{id}/cancelar` | Cancela una nota de venta. | `FirmaAprobacionRequest { "aprobador", "motivo" }` | 200 OK | `PATCH /api/v1/comercial/notas-venta/1/cancelar` → `{ "aprobador": "jperez", "motivo": "Cliente desiste" }` |
| GET | `/api/v1/comercial/notas-venta/{id}/historial` | Historial de cambios de estado. | N/A | 200 OK | `GET /api/v1/comercial/notas-venta/1/historial` |

### Solicitud de Costos

| Método HTTP | Ruta | Descripción | Datos de entrada | Respuestas | Ejemplo (Postman) |
|---|---|---|---|---|---|
| POST | `/api/v1/solicitudes-costos` | Crea una solicitud de costos. | `SolicitudCostosCreateDTO { campos }` | 201 Creado | `{ "clienteId": 1, "vendedorId": 1, "articuloDescripcion": "Polera piqué", "nombrePrenda": "Polera", "cantidad": 100, "genero": "UNISEX", "tallaje": "S-M-L-XL", "tipo": "PRENDA", "estado": "BORRADOR", "esMuestra": false, "hasLogo": true }` |
| PUT | `/api/v1/solicitudes-costos/{id}` | Actualiza una solicitud de costos. | `SolicitudCostosCreateDTO { campos }` | 200 OK | `PUT /api/v1/solicitudes-costos/1` → `{ "clienteId": 1, "vendedorId": 1, "articuloDescripcion": "Polera piqué", "cantidad": 120, "genero": "UNISEX", "estado": "BORRADOR" }` |
| GET | `/api/v1/solicitudes-costos/{id}` | Obtiene una solicitud por ID. | N/A | 200 OK, 404 No encontrada | `GET /api/v1/solicitudes-costos/1` |
| GET | `/api/v1/solicitudes-costos` | Lista todas las solicitudes de costos. | N/A | 200 OK | `GET /api/v1/solicitudes-costos` |
| DELETE | `/api/v1/solicitudes-costos/{id}` | Elimina una solicitud de costos. | N/A | 204 Sin contenido | `DELETE /api/v1/solicitudes-costos/1` |

### Solicitud de Cotizaciones

| Método HTTP | Ruta | Descripción | Datos de entrada | Respuestas | Ejemplo (Postman) |
|---|---|---|---|---|---|
| POST | `/api/v1/solicitudes-cotizaciones` | Crea una solicitud de cotización. | `SolicitudCotizacionesCreateDTO { campos }` | 201 Creado | `{ "clienteId": 1, "vendedorId": 1, "articuloDescripcion": "Gorro lana", "cantidad": 50, "tipo": "COTIZACION", "estado": "BORRADOR", "esMuestra": false, "hasLogo": false }` |
| PUT | `/api/v1/solicitudes-cotizaciones/{id}` | Actualiza una solicitud de cotización. | `SolicitudCotizacionesCreateDTO { campos }` | 200 OK | `PUT /api/v1/solicitudes-cotizaciones/1` → `{ "clienteId": 1, "vendedorId": 1, "articuloDescripcion": "Gorro lana", "cantidad": 80, "estado": "BORRADOR" }` |
| GET | `/api/v1/solicitudes-cotizaciones/{id}` | Obtiene una cotización por ID. | N/A | 200 OK, 404 No encontrada | `GET /api/v1/solicitudes-cotizaciones/1` |
| GET | `/api/v1/solicitudes-cotizaciones` | Lista todas las solicitudes de cotización. | N/A | 200 OK | `GET /api/v1/solicitudes-cotizaciones` |
| DELETE | `/api/v1/solicitudes-cotizaciones/{id}` | Elimina una solicitud de cotización. | N/A | 204 Sin contenido | `DELETE /api/v1/solicitudes-cotizaciones/1` |

### Configuración de Plantillas

| Método HTTP | Ruta | Descripción | Datos de entrada | Respuestas | Ejemplo (Postman) |
|---|---|---|---|---|---|
| GET | `/api/v1/configuracion-plantillas` | Lista todas las configuraciones de plantilla. | N/A | 200 OK | `GET /api/v1/configuracion-plantillas` |
| GET | `/api/v1/configuracion-plantillas/by-nombre` | Busca configuración por nombre de prenda. | `?nombre=` (query param) | 200 OK | `GET /api/v1/configuracion-plantillas/by-nombre?nombre=Polera` |
| POST | `/api/v1/configuracion-plantillas` | Crea una configuración de plantilla. | `ConfiguracionPlantillaDTO { campos }` | 200 OK | `{ "nombrePrenda": "Polera", "campos": [] }` |
| DELETE | `/api/v1/configuracion-plantillas/{id}` | Elimina una configuración de plantilla. | N/A | 200 OK | `DELETE /api/v1/configuracion-plantillas/1` |

---

## Módulo de Producción

### Órdenes de Producción

| Método HTTP | Ruta | Descripción | Datos de entrada | Respuestas | Ejemplo (Postman) |
|---|---|---|---|---|---|
| GET | `/api/v1/produccion/ordenes-produccion` | Lista todas las órdenes de producción. | N/A | 200 OK | `GET /api/v1/produccion/ordenes-produccion` |
| GET | `/api/v1/produccion/ordenes-produccion/{id}` | Obtiene una orden de producción por ID. | N/A | 200 OK, 404 No encontrada | `GET /api/v1/produccion/ordenes-produccion/1` |
| POST | `/api/v1/produccion/ordenes-produccion/recepcionar/{id}` | Recepciona una orden de producción. | N/A | 200 OK, 404 No encontrada | `POST /api/v1/produccion/ordenes-produccion/recepcionar/1` |
| GET | `/api/v1/produccion/ordenes-produccion/{id}/avance` | Consulta el avance de una OP. | N/A | 200 OK | `GET /api/v1/produccion/ordenes-produccion/1/avance` |

### Órdenes de Trabajo

| Método HTTP | Ruta | Descripción | Datos de entrada | Respuestas | Ejemplo (Postman) |
|---|---|---|---|---|---|
| GET | `/api/v1/produccion/ordenes-trabajo/nota-venta/{nvId}` | Lista OTs por Nota de Venta. | N/A | 200 OK | `GET /api/v1/produccion/ordenes-trabajo/nota-venta/1` |
| GET | `/api/v1/produccion/ordenes-trabajo/orden-produccion/{opId}` | Lista OTs de una Orden de Producción. | N/A | 200 OK | `GET /api/v1/produccion/ordenes-trabajo/orden-produccion/1` |
| POST | `/api/v1/produccion/ordenes-trabajo/{id}/iniciar` | Inicia una OT. | N/A | 200 OK | `POST /api/v1/produccion/ordenes-trabajo/1/iniciar` |
| POST | `/api/v1/produccion/ordenes-trabajo/{id}/finalizar` | Finaliza una OT manualmente. | N/A | 200 OK | `POST /api/v1/produccion/ordenes-trabajo/1/finalizar` |
| POST | `/api/v1/produccion/ordenes-trabajo/{id}/avance` | Registra avance/merma del día en una OT. | `RegistrarAvanceCommand { "cantidadBuena", "cantidadMerma", ... }` | 200 OK | `POST /api/v1/produccion/ordenes-trabajo/1/avance` → `{ "cantidadProducida": 50, "cantidadMerma": 2, "motivoMerma": "Falla de costura", "usuario": "operario1", "observacion": "Turno mañana" }` |
| GET | `/api/v1/produccion/ordenes-trabajo/{id}/avances` | Lista avances registrados de una OT. | N/A | 200 OK | `GET /api/v1/produccion/ordenes-trabajo/1/avances` |

### Costeos

> Las respuestas de los GET incluyen, por cada ítem, el objeto `articulo` con los datos del
> artículo asociado a su `insumoId` (cargado en lectura, solo informativo).

| Método HTTP | Ruta | Descripción | Datos de entrada | Respuestas | Ejemplo (Postman) |
|---|---|---|---|---|---|
| GET | `/api/v1/produccion/costeos/scos/{scosId}` | Obtiene costeo por Solicitud de Costos (cada ítem incluye `articulo`). | N/A | 200 OK, 404 No encontrado | `GET /api/v1/produccion/costeos/scos/1` |
| GET | `/api/v1/produccion/costeos/scos/{scosId}/all` | Lista todos los costeos por SCOS (cada ítem incluye `articulo`). | N/A | 200 OK | `GET /api/v1/produccion/costeos/scos/1/all` |
| POST | `/api/v1/produccion/costeos` | Registra un nuevo costeo. | `CosteoDTO { campos de costeo }` | 200 OK | `{ "solicitudCostosId": 1, "costoHilos": 800, "costoManoObra": 2500, "costoEtiquetas": 300, "costoEmbalaje": 200, "costoFlete": 500, "porcentajeCostoFijo": 15, "items": [ { "tipoInsumo": "TELA", "insumoId": 1, "nombreInsumo": "Tela polar", "consumo": 1.5, "precioUnitario": 3200, "costoTotal": 4800 } ] }` |
| PUT | `/api/v1/produccion/costeos/{idCosteo}` | Actualiza/modifica un costeo existente. | `CosteoDTO { campos de costeo }` | 200 OK | `PUT /api/v1/produccion/costeos/12` → `{ "idCosteo": 12, "solicitudCostosId": 1, "costoHilos": 800, "costoManoObra": 2500, "costoEtiquetas": 300, "costoEmbalaje": 200, "costoFlete": 500, "porcentajeCostoFijo": 15, "items": [ { "tipoInsumo": "TELA", "insumoId": 1, "nombreInsumo": "Tela polar", "consumo": 1.5, "precioUnitario": 3200, "costoTotal": 4800 } ] }` |

### Hojas de Compra

| Método HTTP | Ruta | Descripción | Datos de entrada | Respuestas | Ejemplo (Postman) |
|---|---|---|---|---|---|
| POST | `/api/v1/hojas-compra/generar/{opId}` | Genera una Hoja de Compra desde una OP. | N/A | 201 Creado | `POST /api/v1/hojas-compra/generar/1` |
| GET | `/api/v1/hojas-compra` | Lista hojas de compra. | `?estado=` (query param opcional: EstadoHC) | 200 OK | `GET /api/v1/hojas-compra?estado=ABIERTA` |
| GET | `/api/v1/hojas-compra/{idHC}` | Obtiene una Hoja de Compra por ID. | N/A | 200 OK, 404 No encontrada | `GET /api/v1/hojas-compra/1` |
| GET | `/api/v1/hojas-compra/op/{opId}` | Obtiene Hoja de Compra por Orden de Producción. | N/A | 200 OK, 404 No encontrada | `GET /api/v1/hojas-compra/op/1` |
| PATCH | `/api/v1/hojas-compra/{idHC}/aprobar` | Aprueba una Hoja de Compra. | N/A | 200 OK | `PATCH /api/v1/hojas-compra/1/aprobar` |
| PATCH | `/api/v1/hojas-compra/{idHC}/cerrar` | Cierra una Hoja de Compra. | N/A | 200 OK | `PATCH /api/v1/hojas-compra/1/cerrar` |

### Órdenes de Compra

| Método HTTP | Ruta | Descripción | Datos de entrada | Respuestas | Ejemplo (Postman) |
|---|---|---|---|---|---|
| POST | `/api/v1/ordenes-compra/consolidar` | Genera una OC consolidada. | `GenerarOCConsolidadaRequest { campos }` | 201 Creado | `{ "proveedorId": 1, "hcItemIds": [1, 2, 3], "fechaEntregaEstimada": "2026-07-10", "observaciones": "Urgente" }` |
| GET | `/api/v1/ordenes-compra` | Lista órdenes de compra. | `?estado=`, `?proveedorId=`, `?hcItemId=` (query params opcionales) | 200 OK | `GET /api/v1/ordenes-compra?estado=ABIERTA&proveedorId=1` |
| GET | `/api/v1/ordenes-compra/{idOC}` | Obtiene una OC por ID. | N/A | 200 OK, 404 No encontrada | `GET /api/v1/ordenes-compra/1` |
| PATCH | `/api/v1/ordenes-compra/{idOC}/enviar` | Marca OC como enviada al proveedor. | N/A | 200 OK | `PATCH /api/v1/ordenes-compra/1/enviar` |
| PATCH | `/api/v1/ordenes-compra/{idOC}/recepcionar` | Marca OC como recepcionada. | N/A | 200 OK | `PATCH /api/v1/ordenes-compra/1/recepcionar` |
| PATCH | `/api/v1/ordenes-compra/{idOC}/cerrar` | Cierra una OC. | N/A | 200 OK | `PATCH /api/v1/ordenes-compra/1/cerrar` |
| PATCH | `/api/v1/ordenes-compra/{idOC}/items/{idOCItem}/precio` | Actualiza precio de un ítem de OC. | `?precio=` (query param, BigDecimal) | 200 OK | `PATCH /api/v1/ordenes-compra/1/items/5/precio?precio=3500` |

### Órdenes de Servicio

| Método HTTP | Ruta | Descripción | Datos de entrada | Respuestas | Ejemplo (Postman) |
|---|---|---|---|---|---|
| POST | `/api/v1/ordenes-servicio` | Crea una orden de servicio. | `CrearOSRequest { campos }` | 201 Creado | `{ "opId": 1, "proveedorId": 1, "tipoServicio": "BORDADO", "fechaEntregaEstimada": "2026-07-05", "descripcionTrabajo": "Bordado de logo", "cantidadPactada": 100, "precioUnitario": 500, "observaciones": "" }` |
| GET | `/api/v1/ordenes-servicio` | Lista órdenes de servicio. | `?estado=`, `?opId=`, `?proveedorId=` (query params opcionales) | 200 OK | `GET /api/v1/ordenes-servicio?estado=ABIERTA&opId=1` |
| GET | `/api/v1/ordenes-servicio/{idOS}` | Obtiene una OS por ID. | N/A | 200 OK, 404 No encontrada | `GET /api/v1/ordenes-servicio/1` |
| POST | `/api/v1/ordenes-servicio/{idOS}/despachos` | Registra un despacho de OS. | `DespachoOSDTO { campos }` | 200 OK | `POST /api/v1/ordenes-servicio/1/despachos` → `{ "osId": 1, "fechaDespacho": "2026-07-02", "cantidadDespachada": 100, "descripcion": "Envío a taller", "responsable": "bodega1" }` |
| POST | `/api/v1/ordenes-servicio/{idOS}/recepciones` | Registra una recepción de OS. | `RecepcionOSDTO { campos }` | 200 OK | `POST /api/v1/ordenes-servicio/1/recepciones` → `{ "osId": 1, "fechaRecepcion": "2026-07-06", "cantidadRecibida": 100, "cantidadConforme": 98, "cantidadDefectuosa": 2, "responsable": "bodega1" }` |
| PATCH | `/api/v1/ordenes-servicio/{idOS}/cerrar` | Cierra una OS. | N/A | 200 OK | `PATCH /api/v1/ordenes-servicio/1/cerrar` |

### Recepciones de OC

| Método HTTP | Ruta | Descripción | Datos de entrada | Respuestas | Ejemplo (Postman) |
|---|---|---|---|---|---|
| POST | `/api/v1/recepciones-oc/oc/{ocId}` | Registra una recepción de OC. | `RecepcionOCDTO { campos }` | 200 OK | `POST /api/v1/recepciones-oc/oc/1` → `{ "ocId": 1, "fechaRecepcion": "2026-07-10", "numeroGuia": "G-12345", "responsable": "bodega1", "items": [ { "ocItemId": 1, "cantidadRecibida": 100, "cantidadConforme": 100, "cantidadRechazada": 0 } ] }` |
| GET | `/api/v1/recepciones-oc/{idRecepcion}` | Obtiene una recepción por ID. | N/A | 200 OK, 404 No encontrada | `GET /api/v1/recepciones-oc/1` |
| GET | `/api/v1/recepciones-oc/oc/{ocId}` | Lista recepciones por OC. | N/A | 200 OK | `GET /api/v1/recepciones-oc/oc/1` |

### Reportes

| Método HTTP | Ruta | Descripción | Datos de entrada | Respuestas | Ejemplo (Postman) |
|---|---|---|---|---|---|
| GET | `/api/v1/reportes/hcs-pendientes-aprobacion` | Lista HCs pendientes de aprobación. | N/A | 200 OK | `GET /api/v1/reportes/hcs-pendientes-aprobacion` |
| GET | `/api/v1/reportes/ocs-pendientes-recepcion` | Lista OCs pendientes de recepción. | N/A | 200 OK | `GET /api/v1/reportes/ocs-pendientes-recepcion` |
| GET | `/api/v1/reportes/oss-en-taller` | Lista OSs activas en taller. | N/A | 200 OK | `GET /api/v1/reportes/oss-en-taller` |

### Trazabilidad

| Método HTTP | Ruta | Descripción | Datos de entrada | Respuestas | Ejemplo (Postman) |
|---|---|---|---|---|---|
| GET | `/api/v1/trazabilidad/op/{opId}` | Obtiene trazabilidad por Orden de Producción. | N/A | 200 OK | `GET /api/v1/trazabilidad/op/1` |

---

## Módulo de Gestión de Usuarios

### Usuarios

| Método HTTP | Ruta | Descripción | Datos de entrada | Respuestas | Ejemplo (Postman) |
|---|---|---|---|---|---|
| GET | `/api/v1/usuarios` | Lista todos los usuarios. | N/A | 200 OK | `GET /api/v1/usuarios` |
| GET | `/api/v1/usuarios/{id}` | Obtiene un usuario por ID. | N/A | 200 OK, 404 No encontrado | `GET /api/v1/usuarios/1` |
| POST | `/api/v1/usuarios` | Crea un usuario. | `CreateUserDTO { campos }` | 201 Creado | `{ "usuarioRun": "12345678-9", "usuarioNombre": "Juan", "usuarioApellidos": "Pérez", "usuarioEmail": "jperez@empresa.cl", "usuarioPassword": "Secret123", "telefono": "+56912345678", "roles": ["ADMIN"], "areas": ["PRODUCCION"], "enabled": true }` |
| PUT | `/api/v1/usuarios/{id}` | Actualiza un usuario. | `CreateUserDTO { campos }` | 200 OK | `PUT /api/v1/usuarios/1` → `{ "usuarioNombre": "Juan", "usuarioApellidos": "Pérez", "usuarioEmail": "jperez@empresa.cl", "telefono": "+56912345678", "roles": ["ADMIN"], "areas": ["PRODUCCION"], "enabled": true }` |
| DELETE | `/api/v1/usuarios/{id}` | Elimina un usuario. | N/A | 204 Sin contenido | `DELETE /api/v1/usuarios/1` |
| PATCH | `/api/v1/usuarios/{id}/toggle-enabled` | Activa o desactiva un usuario. | N/A | 200 OK | `PATCH /api/v1/usuarios/1/toggle-enabled` |
| PUT | `/api/v1/usuarios/{id}/roles` | Asigna roles a un usuario. | `Set<String> (nombres de roles)` | 200 OK | `PUT /api/v1/usuarios/1/roles` → `["ADMIN", "VENDEDOR"]` |
| PUT | `/api/v1/usuarios/{id}/areas` | Asigna áreas a un usuario. | `Set<String> (nombres de áreas)` | 200 OK | `PUT /api/v1/usuarios/1/areas` → `["PRODUCCION", "COMERCIAL"]` |

### Roles

| Método HTTP | Ruta | Descripción | Datos de entrada | Respuestas | Ejemplo (Postman) |
|---|---|---|---|---|---|
| GET | `/api/v1/roles` | Lista todos los roles. | N/A | 200 OK | `GET /api/v1/roles` |
| GET | `/api/v1/roles/{id}` | Obtiene un rol por ID. | N/A | 200 OK, 404 No encontrado | `GET /api/v1/roles/1` |
| POST | `/api/v1/roles` | Crea un rol. | `RoleDTO { campos }` | 201 Creado | `{ "nombre": "SUPERVISOR", "descripcion": "Supervisa producción", "areaId": 1, "permisosIds": [1, 2, 3] }` |
| PUT | `/api/v1/roles/{id}` | Actualiza un rol. | `RoleDTO { campos }` | 200 OK | `PUT /api/v1/roles/1` → `{ "nombre": "SUPERVISOR", "descripcion": "Supervisa producción", "areaId": 1, "permisosIds": [1, 2] }` |
| PATCH | `/api/v1/roles/{id}` | Actualiza parcialmente un rol. | `RoleDTO { campos parciales }` | 200 OK | `PATCH /api/v1/roles/1` → `{ "descripcion": "Nueva descripción" }` |
| DELETE | `/api/v1/roles/{id}` | Elimina un rol. | N/A | 204 Sin contenido | `DELETE /api/v1/roles/1` |

### Áreas

| Método HTTP | Ruta | Descripción | Datos de entrada | Respuestas | Ejemplo (Postman) |
|---|---|---|---|---|---|
| GET | `/api/v1/areas` | Lista todas las áreas. | N/A | 200 OK | `GET /api/v1/areas` |
| GET | `/api/v1/areas/{id}` | Obtiene un área por ID. | N/A | 200 OK, 404 No encontrada | `GET /api/v1/areas/1` |
| POST | `/api/v1/areas` | Crea un área. | `AreaDTO { campos }` | 201 Creado | `{ "nombre": "PRODUCCION", "descripcion": "Área de producción" }` |
| PUT | `/api/v1/areas/{id}` | Actualiza un área. | `AreaDTO { campos }` | 200 OK | `PUT /api/v1/areas/1` → `{ "nombre": "PRODUCCION", "descripcion": "Área de producción y armado" }` |
| PATCH | `/api/v1/areas/{id}` | Actualiza parcialmente un área. | `AreaDTO { campos parciales }` | 200 OK | `PATCH /api/v1/areas/1` → `{ "descripcion": "Actualizada" }` |
| DELETE | `/api/v1/areas/{id}` | Elimina un área. | N/A | 204 Sin contenido | `DELETE /api/v1/areas/1` |

### Clientes

| Método HTTP | Ruta | Descripción | Datos de entrada | Respuestas | Ejemplo (Postman) |
|---|---|---|---|---|---|
| GET | `/api/v1/clientes` | Lista todos los clientes. | N/A | 200 OK | `GET /api/v1/clientes` |
| GET | `/api/v1/clientes/{clienteId}` | Obtiene un cliente por ID. | N/A | 200 OK, 404 No encontrado | `GET /api/v1/clientes/1` |
| GET | `/api/v1/clientes/run/{runCliente}` | Obtiene un cliente por RUN. | N/A | 200 OK, 404 No encontrado | `GET /api/v1/clientes/run/76123456-7` |
| GET | `/api/v1/clientes/razon-social/{razonSocial}` | Busca clientes por razón social. | N/A | 200 OK | `GET /api/v1/clientes/razon-social/Comercial ABC` |
| GET | `/api/v1/clientes/activos` | Lista clientes activos. | N/A | 200 OK | `GET /api/v1/clientes/activos` |
| GET | `/api/v1/clientes/inactivos` | Lista clientes inactivos. | N/A | 200 OK | `GET /api/v1/clientes/inactivos` |
| GET | `/api/v1/clientes/giro/{giroId}` | Lista clientes por giro. | N/A | 200 OK | `GET /api/v1/clientes/giro/1` |
| GET | `/api/v1/clientes/sigla/{sigla}` | Obtiene clientes por sigla. | N/A | 200 OK | `GET /api/v1/clientes/sigla/ABC` |
| GET | `/api/v1/clientes/giro/descripcion/{descripcionGiro}` | Obtiene clientes por descripción de giro. | N/A | 200 OK | `GET /api/v1/clientes/giro/descripcion/Venta al por mayor` |
| GET | `/api/v1/clientes/activos/sigla/{sigla}` | Lista clientes activos por sigla. | N/A | 200 OK | `GET /api/v1/clientes/activos/sigla/ABC` |
| GET | `/api/v1/clientes/activos/giro/{giroId}` | Lista clientes activos por giro. | N/A | 200 OK | `GET /api/v1/clientes/activos/giro/1` |
| POST | `/api/v1/clientes` | Crea un cliente. | `ClienteDTO { campos }` | 201 Creado | `{ "razonSocial": "Comercial ABC Ltda", "runCliente": "76123456-7", "sigla": "ABC", "activo": true, "giro": { "giroId": 1 }, "contactos": [], "direcciones": [] }` |
| PUT | `/api/v1/clientes/{clienteId}` | Actualiza un cliente. | `ClienteDTO { campos }` | 200 OK | `PUT /api/v1/clientes/1` → `{ "razonSocial": "Comercial ABC Ltda", "runCliente": "76123456-7", "sigla": "ABC", "activo": true, "giro": { "giroId": 1 } }` |
| DELETE | `/api/v1/clientes/{clienteId}` | Elimina un cliente. | N/A | 204 Sin contenido | `DELETE /api/v1/clientes/1` |

### Proveedores

| Método HTTP | Ruta | Descripción | Datos de entrada | Respuestas | Ejemplo (Postman) |
|---|---|---|---|---|---|
| GET | `/api/v1/proveedores` | Lista todos los proveedores. | N/A | 200 OK | `GET /api/v1/proveedores` |
| GET | `/api/v1/proveedores/{proveedorId}` | Obtiene un proveedor por ID. | N/A | 200 OK, 404 No encontrado | `GET /api/v1/proveedores/1` |
| GET | `/api/v1/proveedores/run/{runProveedor}` | Obtiene un proveedor por RUN. | N/A | 200 OK, 404 No encontrado | `GET /api/v1/proveedores/run/77654321-0` |
| GET | `/api/v1/proveedores/razon-social/{razonSocial}` | Busca proveedores por razón social. | N/A | 200 OK | `GET /api/v1/proveedores/razon-social/Telas del Sur` |
| GET | `/api/v1/proveedores/activos` | Lista proveedores activos. | N/A | 200 OK | `GET /api/v1/proveedores/activos` |
| GET | `/api/v1/proveedores/inactivos` | Lista proveedores inactivos. | N/A | 200 OK | `GET /api/v1/proveedores/inactivos` |
| GET | `/api/v1/proveedores/giro/{giroId}` | Lista proveedores por giro. | N/A | 200 OK | `GET /api/v1/proveedores/giro/1` |
| GET | `/api/v1/proveedores/sigla/{sigla}` | Obtiene proveedores por sigla. | N/A | 200 OK | `GET /api/v1/proveedores/sigla/TDS` |
| GET | `/api/v1/proveedores/giro/descripcion/{descripcionGiro}` | Obtiene proveedores por descripción de giro. | N/A | 200 OK | `GET /api/v1/proveedores/giro/descripcion/Venta de insumos` |
| GET | `/api/v1/proveedores/activos/sigla/{sigla}` | Lista proveedores activos por sigla. | N/A | 200 OK | `GET /api/v1/proveedores/activos/sigla/TDS` |
| GET | `/api/v1/proveedores/activos/giro/{giroId}` | Lista proveedores activos por giro. | N/A | 200 OK | `GET /api/v1/proveedores/activos/giro/1` |
| POST | `/api/v1/proveedores` | Crea un proveedor. | `ProveedorDTO { campos }` | 201 Creado | `{ "runProveedor": "77654321-0", "razonSocialProveedor": "Telas del Sur SpA", "tipoProveedor": "INSUMOS", "horarioAtencion": "9-18h", "sigla": "TDS", "activo": true, "giro": { "giroId": 1 } }` |
| PUT | `/api/v1/proveedores/{proveedorId}` | Actualiza un proveedor. | `ProveedorDTO { campos }` | 200 OK | `PUT /api/v1/proveedores/1` → `{ "runProveedor": "77654321-0", "razonSocialProveedor": "Telas del Sur SpA", "tipoProveedor": "INSUMOS", "sigla": "TDS", "activo": true, "giro": { "giroId": 1 } }` |
| DELETE | `/api/v1/proveedores/{proveedorId}` | Elimina un proveedor. | N/A | 204 Sin contenido | `DELETE /api/v1/proveedores/1` |

### Vendedores

| Método HTTP | Ruta | Descripción | Datos de entrada | Respuestas | Ejemplo (Postman) |
|---|---|---|---|---|---|
| GET | `/api/v1/vendedores` | Lista todos los vendedores. | N/A | 200 OK | `GET /api/v1/vendedores` |
| GET | `/api/v1/vendedores/{id}` | Obtiene un vendedor por ID. | N/A | 200 OK, 404 No encontrado | `GET /api/v1/vendedores/1` |
| GET | `/api/v1/vendedores/usuario/{usuarioId}` | Obtiene vendedor por ID de usuario. | N/A | 200 OK, 404 No encontrado | `GET /api/v1/vendedores/usuario/1` |
| POST | `/api/v1/vendedores` | Crea un vendedor. | `VendedorCreateDTO { campos }` | 201 Creado | `{ "usuarioId": 1, "codigoVendedor": "V001" }` |
| PUT | `/api/v1/vendedores/{id}` | Actualiza un vendedor. | `VendedorCreateDTO { campos }` | 200 OK | `PUT /api/v1/vendedores/1` → `{ "usuarioId": 1, "codigoVendedor": "V002" }` |
| DELETE | `/api/v1/vendedores/{id}` | Elimina un vendedor. | N/A | 204 Sin contenido | `DELETE /api/v1/vendedores/1` |

---

## Módulo Compartido (Shared)

### Productos

| Método HTTP | Ruta | Descripción | Datos de entrada | Respuestas | Ejemplo (Postman) |
|---|---|---|---|---|---|
| GET | `/api/v1/shared/productos` | Lista todos los productos. | N/A | 200 OK | `GET /api/v1/shared/productos` |
| GET | `/api/v1/shared/productos/{id}` | Obtiene un producto por ID. | N/A | 200 OK, 404 No encontrado | `GET /api/v1/shared/productos/1` |
| POST | `/api/v1/shared/productos` | Crea un nuevo producto. | `Producto { campos del producto }` | 201 Creado | `{ "codigoProducto": "P-001", "nombreProducto": "Polera básica", "descripcionProducto": "Algodón", "generoProducto": "UNISEX", "colorProducto": "Negro", "modeloProducto": "Clásico", "telaProducto": "Algodón", "composicionProducto": "100% algodón", "gramajeProducto": "180" }` |
| DELETE | `/api/v1/shared/productos/{id}` | Elimina un producto. | N/A | 204 Sin contenido, 404 No encontrado | `DELETE /api/v1/shared/productos/1` |

### Permisos

| Método HTTP | Ruta | Descripción | Datos de entrada | Respuestas | Ejemplo (Postman) |
|---|---|---|---|---|---|
| GET | `/api/v1/permisos` | Lista todos los permisos del sistema. | N/A | 200 OK | `GET /api/v1/permisos` |

### Historial de Estado

| Método HTTP | Ruta | Descripción | Datos de entrada | Respuestas | Ejemplo (Postman) |
|---|---|---|---|---|---|
| GET | `/api/v1/historial-estado/{tipoEntidad}/{entidadId}` | Consulta historial de cambios de estado. | N/A | 200 OK | `GET /api/v1/historial-estado/NOTA_VENTA/1` |

### Bancos

| Método HTTP | Ruta | Descripción | Datos de entrada | Respuestas | Ejemplo (Postman) |
|---|---|---|---|---|---|
| GET | `/api/v1/bancos` | Lista todos los bancos. | N/A | 200 OK | `GET /api/v1/bancos` |
| GET | `/api/v1/bancos/{id}` | Obtiene un banco por ID. | N/A | 200 OK, 404 No encontrado | `GET /api/v1/bancos/1` |
| POST | `/api/v1/bancos` | Crea un banco. | `BancoDTO { campos }` | 201 Creado | `{ "nombreBanco": "Banco Estado", "codigoBanco": "012" }` |
| PUT | `/api/v1/bancos/{id}` | Actualiza un banco. | `BancoDTO { campos }` | 200 OK | `PUT /api/v1/bancos/1` → `{ "nombreBanco": "Banco Estado", "codigoBanco": "012" }` |
| DELETE | `/api/v1/bancos/{id}` | Elimina un banco. | N/A | 204 Sin contenido | `DELETE /api/v1/bancos/1` |

### Comunas

| Método HTTP | Ruta | Descripción | Datos de entrada | Respuestas | Ejemplo (Postman) |
|---|---|---|---|---|---|
| GET | `/api/v1/comunas` | Lista todas las comunas. | N/A | 200 OK | `GET /api/v1/comunas` |
| GET | `/api/v1/comunas/{id}` | Obtiene una comuna por ID. | N/A | 200 OK, 404 No encontrada | `GET /api/v1/comunas/1` |
| GET | `/api/v1/comunas/region/{regionId}` | Lista comunas por región. | N/A | 200 OK | `GET /api/v1/comunas/region/1` |
| POST | `/api/v1/comunas` | Crea una comuna. | `ComunaDTO { campos }` | 201 Creado | `{ "nombreComuna": "Santiago", "region": { "regionId": 1 } }` |
| PUT | `/api/v1/comunas/{id}` | Actualiza una comuna. | `ComunaDTO { campos }` | 200 OK | `PUT /api/v1/comunas/1` → `{ "nombreComuna": "Providencia", "region": { "regionId": 1 } }` |
| DELETE | `/api/v1/comunas/{id}` | Elimina una comuna. | N/A | 204 Sin contenido | `DELETE /api/v1/comunas/1` |

### Contactos

| Método HTTP | Ruta | Descripción | Datos de entrada | Respuestas | Ejemplo (Postman) |
|---|---|---|---|---|---|
| GET | `/api/v1/contacto` | Lista todos los contactos. | N/A | 200 OK | `GET /api/v1/contacto` |
| GET | `/api/v1/contacto/{id}` | Obtiene un contacto por ID. | N/A | 200 OK, 404 No encontrado | `GET /api/v1/contacto/1` |
| POST | `/api/v1/contacto` | Crea un contacto. | `ContactoDTO { campos }` | 201 Creado | `{ "nombreContacto": "María López", "telefonoContacto": "+56987654321", "emailContacto": "maria@empresa.cl", "tipoContacto": { "tipoContactoId": 1 } }` |
| PUT | `/api/v1/contacto/{id}` | Actualiza un contacto. | `ContactoDTO { campos }` | 200 OK | `PUT /api/v1/contacto/1` → `{ "nombreContacto": "María López", "telefonoContacto": "+56987654321", "emailContacto": "maria@empresa.cl", "tipoContacto": { "tipoContactoId": 1 } }` |
| DELETE | `/api/v1/contacto/{id}` | Elimina un contacto. | N/A | 204 Sin contenido | `DELETE /api/v1/contacto/1` |

### Datos Bancarios

| Método HTTP | Ruta | Descripción | Datos de entrada | Respuestas | Ejemplo (Postman) |
|---|---|---|---|---|---|
| GET | `/api/v1/datos-bancarios` | Lista todos los datos bancarios. | N/A | 200 OK | `GET /api/v1/datos-bancarios` |
| GET | `/api/v1/datos-bancarios/{id}` | Obtiene un dato bancario por ID. | N/A | 200 OK, 404 No encontrado | `GET /api/v1/datos-bancarios/1` |
| GET | `/api/v1/datos-bancarios/banco/{bancoId}` | Lista datos bancarios por banco. | N/A | 200 OK | `GET /api/v1/datos-bancarios/banco/1` |
| POST | `/api/v1/datos-bancarios` | Crea un dato bancario. | `DatoBancarioDTO { campos }` | 201 Creado | `{ "numeroCuenta": "00012345678", "banco": { "bancoId": 1 }, "tipoCuentaBancaria": { "tipoCuentaId": 1 } }` |
| PUT | `/api/v1/datos-bancarios/{id}` | Actualiza un dato bancario. | `DatoBancarioDTO { campos }` | 200 OK | `PUT /api/v1/datos-bancarios/1` → `{ "numeroCuenta": "00012345678", "banco": { "bancoId": 1 }, "tipoCuentaBancaria": { "tipoCuentaId": 1 } }` |
| DELETE | `/api/v1/datos-bancarios/{id}` | Elimina un dato bancario. | N/A | 204 Sin contenido | `DELETE /api/v1/datos-bancarios/1` |

### Direcciones

| Método HTTP | Ruta | Descripción | Datos de entrada | Respuestas | Ejemplo (Postman) |
|---|---|---|---|---|---|
| GET | `/api/v1/direcciones` | Lista todas las direcciones. | N/A | 200 OK | `GET /api/v1/direcciones` |
| GET | `/api/v1/direcciones/{id}` | Obtiene una dirección por ID. | N/A | 200 OK, 404 No encontrada | `GET /api/v1/direcciones/1` |
| GET | `/api/v1/direcciones/comuna/{comunaId}` | Lista direcciones por comuna. | N/A | 200 OK | `GET /api/v1/direcciones/comuna/1` |
| POST | `/api/v1/direcciones` | Crea una dirección. | `DireccionDTO { campos }` | 201 Creado | `{ "calle": "Av. Principal", "numero": "1234", "depto": "501", "tipoDireccion": { "tipoDireccionId": 1 }, "comuna": { "comunaId": 1 } }` |
| PUT | `/api/v1/direcciones/{id}` | Actualiza una dirección. | `DireccionDTO { campos }` | 200 OK | `PUT /api/v1/direcciones/1` → `{ "calle": "Av. Principal", "numero": "1234", "depto": "502", "tipoDireccion": { "tipoDireccionId": 1 }, "comuna": { "comunaId": 1 } }` |
| DELETE | `/api/v1/direcciones/{id}` | Elimina una dirección. | N/A | 204 Sin contenido | `DELETE /api/v1/direcciones/1` |

### Giros

| Método HTTP | Ruta | Descripción | Datos de entrada | Respuestas | Ejemplo (Postman) |
|---|---|---|---|---|---|
| GET | `/api/v1/giros` | Lista todos los giros. | N/A | 200 OK | `GET /api/v1/giros` |
| GET | `/api/v1/giros/{giroId}` | Obtiene un giro por ID. | N/A | 200 OK, 404 No encontrado | `GET /api/v1/giros/1` |
| GET | `/api/v1/giros/codigo/{codigoSii}` | Obtiene giro por código SII. | N/A | 200 OK, 404 No encontrado | `GET /api/v1/giros/codigo/620100` |
| GET | `/api/v1/giros/nombre/{nombreGiro}` | Busca giro por nombre. | N/A | 200 OK | `GET /api/v1/giros/nombre/Software` |
| GET | `/api/v1/giros/descripcion/{descripcionGiro}` | Obtiene giro por descripción. | N/A | 200 OK, 404 No encontrado | `GET /api/v1/giros/descripcion/Desarrollo de software` |
| GET | `/api/v1/giros/obtenerocrear/{nombreGiro}` | Obtiene o crea un giro por nombre. | N/A | 200 OK | `GET /api/v1/giros/obtenerocrear/Software` |
| POST | `/api/v1/giros` | Crea un giro. | `GiroDTO { campos }` | 201 Creado | `{ "codigoSii": "620100", "nombreGiro": "Software", "descripcionGiro": "Desarrollo de software", "rubro": { "rubroId": 1 } }` |
| PUT | `/api/v1/giros/{giroId}` | Actualiza un giro. | `GiroDTO { campos }` | 200 OK | `PUT /api/v1/giros/1` → `{ "codigoSii": "620100", "nombreGiro": "Software", "descripcionGiro": "Desarrollo y consultoría", "rubro": { "rubroId": 1 } }` |
| DELETE | `/api/v1/giros/{giroId}` | Elimina un giro. | N/A | 204 Sin contenido | `DELETE /api/v1/giros/1` |

### Países

| Método HTTP | Ruta | Descripción | Datos de entrada | Respuestas | Ejemplo (Postman) |
|---|---|---|---|---|---|
| GET | `/api/v1/paises` | Lista todos los países. | N/A | 200 OK | `GET /api/v1/paises` |
| GET | `/api/v1/paises/{id}` | Obtiene un país por ID. | N/A | 200 OK, 404 No encontrado | `GET /api/v1/paises/1` |
| POST | `/api/v1/paises` | Crea un país. | `PaisDTO { campos }` | 201 Creado | `{ "nombrePais": "Chile" }` |
| PUT | `/api/v1/paises/{id}` | Actualiza un país. | `PaisDTO { campos }` | 200 OK | `PUT /api/v1/paises/1` → `{ "nombrePais": "Chile" }` |
| DELETE | `/api/v1/paises/{id}` | Elimina un país. | N/A | 204 Sin contenido | `DELETE /api/v1/paises/1` |

### Regiones

| Método HTTP | Ruta | Descripción | Datos de entrada | Respuestas | Ejemplo (Postman) |
|---|---|---|---|---|---|
| GET | `/api/v1/regiones` | Lista todas las regiones. | N/A | 200 OK | `GET /api/v1/regiones` |
| GET | `/api/v1/regiones/{id}` | Obtiene una región por ID. | N/A | 200 OK, 404 No encontrada | `GET /api/v1/regiones/1` |
| GET | `/api/v1/regiones/pais/{paisId}` | Lista regiones por país. | N/A | 200 OK | `GET /api/v1/regiones/pais/1` |
| POST | `/api/v1/regiones` | Crea una región. | `RegionDTO { campos }` | 201 Creado | `{ "nombreRegion": "Región Metropolitana", "pais": { "idPais": 1 } }` |
| PUT | `/api/v1/regiones/{id}` | Actualiza una región. | `RegionDTO { campos }` | 200 OK | `PUT /api/v1/regiones/1` → `{ "nombreRegion": "Región Metropolitana", "pais": { "idPais": 1 } }` |
| DELETE | `/api/v1/regiones/{id}` | Elimina una región. | N/A | 204 Sin contenido | `DELETE /api/v1/regiones/1` |

### Rubros

| Método HTTP | Ruta | Descripción | Datos de entrada | Respuestas | Ejemplo (Postman) |
|---|---|---|---|---|---|
| GET | `/api/v1/rubros` | Lista todos los rubros. | N/A | 200 OK | `GET /api/v1/rubros` |
| GET | `/api/v1/rubros/{id}` | Obtiene un rubro por ID. | N/A | 200 OK, 404 No encontrado | `GET /api/v1/rubros/1` |
| GET | `/api/v1/rubros/nombre/{nombreRubro}` | Obtiene rubro por nombre. | N/A | 200 OK, 404 No encontrado | `GET /api/v1/rubros/nombre/Tecnología` |
| POST | `/api/v1/rubros` | Crea un rubro. | `RubroDTO { campos }` | 201 Creado | `{ "nombreRubro": "Tecnología", "descripcionRubro": "Empresas TI" }` |
| PUT | `/api/v1/rubros/{id}` | Actualiza un rubro. | `RubroDTO { campos }` | 200 OK | `PUT /api/v1/rubros/1` → `{ "nombreRubro": "Tecnología", "descripcionRubro": "Empresas de tecnología" }` |
| DELETE | `/api/v1/rubros/{id}` | Elimina un rubro. | N/A | 204 Sin contenido | `DELETE /api/v1/rubros/1` |

### Tipos de Contacto

| Método HTTP | Ruta | Descripción | Datos de entrada | Respuestas | Ejemplo (Postman) |
|---|---|---|---|---|---|
| GET | `/api/v1/tipo-contacto` | Lista todos los tipos de contacto. | N/A | 200 OK | `GET /api/v1/tipo-contacto` |
| GET | `/api/v1/tipo-contacto/{id}` | Obtiene un tipo de contacto por ID. | N/A | 200 OK, 404 No encontrado | `GET /api/v1/tipo-contacto/1` |
| GET | `/api/v1/tipo-contacto/descripcion/{descripcionTipoContacto}` | Obtiene tipo de contacto por descripción. | N/A | 200 OK, 404 No encontrado | `GET /api/v1/tipo-contacto/descripcion/Comercial` |
| POST | `/api/v1/tipo-contacto` | Crea un tipo de contacto. | `TipoContactoDTO { campos }` | 201 Creado | `{ "descripcionTipoContacto": "Comercial" }` |
| PUT | `/api/v1/tipo-contacto/{id}` | Actualiza un tipo de contacto. | `TipoContactoDTO { campos }` | 200 OK | `PUT /api/v1/tipo-contacto/1` → `{ "descripcionTipoContacto": "Comercial" }` |
| DELETE | `/api/v1/tipo-contacto/{id}` | Elimina un tipo de contacto. | N/A | 204 Sin contenido | `DELETE /api/v1/tipo-contacto/1` |

### Tipos de Cuenta Bancaria

| Método HTTP | Ruta | Descripción | Datos de entrada | Respuestas | Ejemplo (Postman) |
|---|---|---|---|---|---|
| GET | `/api/v1/tipos-cuenta-bancaria` | Lista todos los tipos de cuenta bancaria. | N/A | 200 OK | `GET /api/v1/tipos-cuenta-bancaria` |
| GET | `/api/v1/tipos-cuenta-bancaria/{id}` | Obtiene un tipo de cuenta por ID. | N/A | 200 OK, 404 No encontrado | `GET /api/v1/tipos-cuenta-bancaria/1` |
| POST | `/api/v1/tipos-cuenta-bancaria` | Crea un tipo de cuenta bancaria. | `TipoCuentaBancariaDTO { campos }` | 201 Creado | `{ "denominacionCuenta": "Cuenta Corriente" }` |
| PUT | `/api/v1/tipos-cuenta-bancaria/{id}` | Actualiza un tipo de cuenta bancaria. | `TipoCuentaBancariaDTO { campos }` | 200 OK | `PUT /api/v1/tipos-cuenta-bancaria/1` → `{ "denominacionCuenta": "Cuenta Vista" }` |
| DELETE | `/api/v1/tipos-cuenta-bancaria/{id}` | Elimina un tipo de cuenta bancaria. | N/A | 204 Sin contenido | `DELETE /api/v1/tipos-cuenta-bancaria/1` |

### Tipos de Dirección

| Método HTTP | Ruta | Descripción | Datos de entrada | Respuestas | Ejemplo (Postman) |
|---|---|---|---|---|---|
| GET | `/api/v1/tipos-direccion` | Lista todos los tipos de dirección. | N/A | 200 OK | `GET /api/v1/tipos-direccion` |
| GET | `/api/v1/tipos-direccion/{id}` | Obtiene un tipo de dirección por ID. | N/A | 200 OK, 404 No encontrado | `GET /api/v1/tipos-direccion/1` |
| POST | `/api/v1/tipos-direccion` | Crea un tipo de dirección. | `TipoDireccionDTO { campos }` | 201 Creado | `{ "descripcion": "Facturación" }` |
| PUT | `/api/v1/tipos-direccion/{id}` | Actualiza un tipo de dirección. | `TipoDireccionDTO { campos }` | 200 OK | `PUT /api/v1/tipos-direccion/1` → `{ "descripcion": "Despacho" }` |
| DELETE | `/api/v1/tipos-direccion/{id}` | Elimina un tipo de dirección. | N/A | 204 Sin contenido | `DELETE /api/v1/tipos-direccion/1` |

---

## Módulo Maestros

> Base URL: `/api/v3/maestros/`

### Artículos

| Método HTTP | Ruta | Descripción | Datos de entrada | Respuestas | Ejemplo (Postman) |
|---|---|---|---|---|---|
| POST | `/api/v3/maestros/articulos` | Crea un nuevo artículo (incluye satélite según tipo). | `ArticuloDTO { codigoArticulo, nombreArticulo, tipoArticulo, idCategoriaTela, idSubCategoriaTela, detalleTela/detallePrenda/detalleAccesorio }` | 201 Creado | `{ "codigoArticulo": "ART-001", "nombreArticulo": "Tela polar", "descripcionArticulo": "Polar antipeeling", "codigoBarra": "7800001234567", "tipoArticulo": "TELA", "activo": true, "idCategoriaTela": 1, "idSubCategoriaTela": 1 }` |
| PUT | `/api/v3/maestros/articulos/{id}` | Actualiza un artículo existente. | `ArticuloDTO { campos }` | 200 OK | `PUT /api/v3/maestros/articulos/1` → `{ "codigoArticulo": "ART-001", "nombreArticulo": "Tela polar reforzada", "tipoArticulo": "TELA", "activo": true, "idCategoriaTela": 1 }` |
| GET | `/api/v3/maestros/articulos/{id}` | Obtiene un artículo por ID. | N/A | 200 OK, 404 No encontrado | `GET /api/v3/maestros/articulos/1` |
| GET | `/api/v3/maestros/articulos/codigo/{codigo}` | Obtiene un artículo por su código único. | N/A | 200 OK, 404 No encontrado | `GET /api/v3/maestros/articulos/codigo/ART-001` |
| GET | `/api/v3/maestros/articulos` | Lista todos los artículos. | N/A | 200 OK | `GET /api/v3/maestros/articulos` |
| GET | `/api/v3/maestros/articulos/activos` | Lista solo artículos activos. | N/A | 200 OK | `GET /api/v3/maestros/articulos/activos` |
| GET | `/api/v3/maestros/articulos/tipo/{tipo}` | Filtra artículos por tipo (`TELA`, `PRENDA_LISTA`, `ACCESORIO`). | N/A | 200 OK | `GET /api/v3/maestros/articulos/tipo/TELA` |
| GET | `/api/v3/maestros/articulos/buscar?nombre=` | Búsqueda parcial de artículos por nombre (case-insensitive). | `nombre` (query param) | 200 OK | `GET /api/v3/maestros/articulos/buscar?nombre=polar` |
| DELETE | `/api/v3/maestros/articulos/{id}` | Desactiva un artículo (eliminación lógica). | N/A | 204 Sin contenido | `DELETE /api/v3/maestros/articulos/1` |

### Categorías

| Método HTTP | Ruta | Descripción | Datos de entrada | Respuestas | Ejemplo (Postman) |
|---|---|---|---|---|---|
| POST | `/api/v3/maestros/categorias` | Crea una nueva categoría. | `CategoriaRequestDTO { codigoCategoria, nombreCategoria }` | 201 Creado | `{ "codigoCategoria": "CAT-01", "nombreCategoria": "Telas" }` |
| PUT | `/api/v3/maestros/categorias/{id}` | Actualiza una categoría. | `CategoriaRequestDTO { campos }` | 200 OK | `PUT /api/v3/maestros/categorias/1` → `{ "codigoCategoria": "CAT-01", "nombreCategoria": "Telas técnicas" }` |
| GET | `/api/v3/maestros/categorias/{id}` | Obtiene una categoría por ID. | N/A | 200 OK, 404 No encontrada | `GET /api/v3/maestros/categorias/1` |
| GET | `/api/v3/maestros/categorias` | Lista todas las categorías. | N/A | 200 OK | `GET /api/v3/maestros/categorias` |
| DELETE | `/api/v3/maestros/categorias/{id}` | Elimina una categoría. | N/A | 204 Sin contenido | `DELETE /api/v3/maestros/categorias/1` |

### SubCategorías

| Método HTTP | Ruta | Descripción | Datos de entrada | Respuestas | Ejemplo (Postman) |
|---|---|---|---|---|---|
| POST | `/api/v3/maestros/subcategorias` | Crea una subcategoría vinculada a una categoría padre. | `SubCategoriaRequestDTO { codigoSubcategoria, nombreSubcategoria, idCategoria }` | 201 Creado | `{ "codigoSubcategoria": "SUB-01", "nombreSubcategoria": "Polar", "idCategoria": 1 }` |
| PUT | `/api/v3/maestros/subcategorias/{id}` | Actualiza una subcategoría. | `SubCategoriaRequestDTO { campos }` | 200 OK | `PUT /api/v3/maestros/subcategorias/1` → `{ "codigoSubcategoria": "SUB-01", "nombreSubcategoria": "Polar antipeeling", "idCategoria": 1 }` |
| GET | `/api/v3/maestros/subcategorias/{id}` | Obtiene una subcategoría por ID. | N/A | 200 OK, 404 No encontrada | `GET /api/v3/maestros/subcategorias/1` |
| GET | `/api/v3/maestros/subcategorias` | Lista todas las subcategorías. | N/A | 200 OK | `GET /api/v3/maestros/subcategorias` |
| GET | `/api/v3/maestros/subcategorias/por-categoria/{idCategoria}` | Lista subcategorías de una categoría padre. | N/A | 200 OK | `GET /api/v3/maestros/subcategorias/por-categoria/1` |
| DELETE | `/api/v3/maestros/subcategorias/{id}` | Elimina una subcategoría. | N/A | 204 Sin contenido | `DELETE /api/v3/maestros/subcategorias/1` |

### Unidades de Medida

| Método HTTP | Ruta | Descripción | Datos de entrada | Respuestas | Ejemplo (Postman) |
|---|---|---|---|---|---|
| POST | `/api/v3/maestros/unidades-medida` | Crea una unidad de medida. | `UnidadMedidaRequestDTO { nombreUnidad, abreviatura }` | 201 Creado | `{ "nombreUnidad": "Metro", "abreviatura": "m" }` |
| PUT | `/api/v3/maestros/unidades-medida/{id}` | Actualiza una unidad de medida. | `UnidadMedidaRequestDTO { campos }` | 200 OK | `PUT /api/v3/maestros/unidades-medida/1` → `{ "nombreUnidad": "Kilogramo", "abreviatura": "kg" }` |
| GET | `/api/v3/maestros/unidades-medida/{id}` | Obtiene una unidad de medida por ID. | N/A | 200 OK, 404 No encontrada | `GET /api/v3/maestros/unidades-medida/1` |
| GET | `/api/v3/maestros/unidades-medida` | Lista todas las unidades de medida. | N/A | 200 OK | `GET /api/v3/maestros/unidades-medida` |
| DELETE | `/api/v3/maestros/unidades-medida/{id}` | Elimina una unidad de medida. | N/A | 204 Sin contenido | `DELETE /api/v3/maestros/unidades-medida/1` |

### Monedas

| Método HTTP | Ruta | Descripción | Datos de entrada | Respuestas | Ejemplo (Postman) |
|---|---|---|---|---|---|
| POST | `/api/v3/maestros/monedas` | Crea una moneda. | `MonedaRequestDTO { codigoMoneda, nombreMoneda, simbolo }` | 201 Creado | `{ "codigoMoneda": "CLP", "nombreMoneda": "Peso chileno", "simbolo": "$" }` |
| PUT | `/api/v3/maestros/monedas/{id}` | Actualiza una moneda. | `MonedaRequestDTO { campos }` | 200 OK | `PUT /api/v3/maestros/monedas/1` → `{ "codigoMoneda": "USD", "nombreMoneda": "Dólar", "simbolo": "US$" }` |
| GET | `/api/v3/maestros/monedas/{id}` | Obtiene una moneda por ID. | N/A | 200 OK, 404 No encontrada | `GET /api/v3/maestros/monedas/1` |
| GET | `/api/v3/maestros/monedas` | Lista todas las monedas. | N/A | 200 OK | `GET /api/v3/maestros/monedas` |
| DELETE | `/api/v3/maestros/monedas/{id}` | Elimina una moneda. | N/A | 204 Sin contenido | `DELETE /api/v3/maestros/monedas/1` |

### Precios

| Método HTTP | Ruta | Descripción | Datos de entrada | Respuestas | Ejemplo (Postman) |
|---|---|---|---|---|---|
| POST | `/api/v3/maestros/precios` | Registra un precio para un artículo. | `PrecioRequestDTO { idArticulo, idMoneda, tipoPrecio, valor }` | 201 Creado | `{ "idArticulo": 1, "idMoneda": 1, "tipoPrecio": "VENTA", "valor": 3500 }` |
| PUT | `/api/v3/maestros/precios/{id}` | Actualiza un precio existente. | `PrecioRequestDTO { campos }` | 200 OK | `PUT /api/v3/maestros/precios/1` → `{ "idArticulo": 1, "idMoneda": 1, "tipoPrecio": "VENTA", "valor": 3800 }` |
| GET | `/api/v3/maestros/precios/{id}` | Obtiene un precio por ID. | N/A | 200 OK, 404 No encontrado | `GET /api/v3/maestros/precios/1` |
| GET | `/api/v3/maestros/precios/articulo/{idArticulo}` | Lista todos los precios de un artículo. | N/A | 200 OK | `GET /api/v3/maestros/precios/articulo/1` |
| DELETE | `/api/v3/maestros/precios/{id}` | Elimina un precio. | N/A | 204 Sin contenido | `DELETE /api/v3/maestros/precios/1` |

### Familias de Tela

| Método HTTP | Ruta | Descripción | Datos de entrada | Respuestas | Ejemplo (Postman) |
|---|---|---|---|---|---|
| POST | `/api/v3/maestros/familias-tela` | Crea una familia de tela. | `FamiliaTelaRequestDTO { codigoFamilia, nombreFamilia }` | 201 Creado | `{ "codigoFamilia": "FAM-01", "nombreFamilia": "Algodones" }` |
| PUT | `/api/v3/maestros/familias-tela/{id}` | Actualiza una familia de tela. | `FamiliaTelaRequestDTO { campos }` | 200 OK | `PUT /api/v3/maestros/familias-tela/1` → `{ "codigoFamilia": "FAM-01", "nombreFamilia": "Algodones premium" }` |
| GET | `/api/v3/maestros/familias-tela/{id}` | Obtiene una familia de tela por ID. | N/A | 200 OK, 404 No encontrada | `GET /api/v3/maestros/familias-tela/1` |
| GET | `/api/v3/maestros/familias-tela` | Lista todas las familias de tela. | N/A | 200 OK | `GET /api/v3/maestros/familias-tela` |
| DELETE | `/api/v3/maestros/familias-tela/{id}` | Elimina una familia de tela. | N/A | 204 Sin contenido | `DELETE /api/v3/maestros/familias-tela/1` |

### Clasificaciones Técnicas

| Método HTTP | Ruta | Descripción | Datos de entrada | Respuestas | Ejemplo (Postman) |
|---|---|---|---|---|---|
| POST | `/api/v3/maestros/clasificaciones-tecnicas` | Crea una clasificación técnica de tela. | `ClasificacionTecnicaRequestDTO { nombreClasificacion }` | 201 Creado | `{ "nombreClasificacion": "Impermeable" }` |
| PUT | `/api/v3/maestros/clasificaciones-tecnicas/{id}` | Actualiza una clasificación técnica. | `ClasificacionTecnicaRequestDTO { campos }` | 200 OK | `PUT /api/v3/maestros/clasificaciones-tecnicas/1` → `{ "nombreClasificacion": "Repelente al agua" }` |
| GET | `/api/v3/maestros/clasificaciones-tecnicas/{id}` | Obtiene una clasificación por ID. | N/A | 200 OK, 404 No encontrada | `GET /api/v3/maestros/clasificaciones-tecnicas/1` |
| GET | `/api/v3/maestros/clasificaciones-tecnicas` | Lista todas las clasificaciones técnicas. | N/A | 200 OK | `GET /api/v3/maestros/clasificaciones-tecnicas` |
| DELETE | `/api/v3/maestros/clasificaciones-tecnicas/{id}` | Elimina una clasificación técnica. | N/A | 204 Sin contenido | `DELETE /api/v3/maestros/clasificaciones-tecnicas/1` |

### Composiciones

| Método HTTP | Ruta | Descripción | Datos de entrada | Respuestas | Ejemplo (Postman) |
|---|---|---|---|---|---|
| POST | `/api/v3/maestros/composiciones` | Crea una composición de tela. | `ComposicionRequestDTO { codigoComposicion, descripcionComposicion, clasificacion, usoTipico }` | 201 Creado | `{ "codigoComposicion": "COMP-01", "descripcionComposicion": "100% algodón", "clasificacion": "Natural", "usoTipico": "Poleras" }` |
| PUT | `/api/v3/maestros/composiciones/{id}` | Actualiza una composición. | `ComposicionRequestDTO { campos }` | 200 OK | `PUT /api/v3/maestros/composiciones/1` → `{ "codigoComposicion": "COMP-01", "descripcionComposicion": "95% algodón 5% elastano", "clasificacion": "Mixta", "usoTipico": "Poleras" }` |
| GET | `/api/v3/maestros/composiciones/{id}` | Obtiene una composición por ID. | N/A | 200 OK, 404 No encontrada | `GET /api/v3/maestros/composiciones/1` |
| GET | `/api/v3/maestros/composiciones` | Lista todas las composiciones. | N/A | 200 OK | `GET /api/v3/maestros/composiciones` |
| DELETE | `/api/v3/maestros/composiciones/{id}` | Elimina una composición. | N/A | 204 Sin contenido | `DELETE /api/v3/maestros/composiciones/1` |

### Gramajes de Tela

| Método HTTP | Ruta | Descripción | Datos de entrada | Respuestas | Ejemplo (Postman) |
|---|---|---|---|---|---|
| POST | `/api/v3/maestros/gramajes-tela` | Crea un gramaje de tela. | `GramajeTelaRequestDTO { codigoGramaje, valorGramosM2, categoriaVestuario }` | 201 Creado | `{ "codigoGramaje": "GR-180", "valorGramosM2": 180, "categoriaVestuario": "Poleras" }` |
| PUT | `/api/v3/maestros/gramajes-tela/{id}` | Actualiza un gramaje. | `GramajeTelaRequestDTO { campos }` | 200 OK | `PUT /api/v3/maestros/gramajes-tela/1` → `{ "codigoGramaje": "GR-220", "valorGramosM2": 220, "categoriaVestuario": "Polerones" }` |
| GET | `/api/v3/maestros/gramajes-tela/{id}` | Obtiene un gramaje por ID. | N/A | 200 OK, 404 No encontrado | `GET /api/v3/maestros/gramajes-tela/1` |
| GET | `/api/v3/maestros/gramajes-tela` | Lista todos los gramajes. | N/A | 200 OK | `GET /api/v3/maestros/gramajes-tela` |
| DELETE | `/api/v3/maestros/gramajes-tela/{id}` | Elimina un gramaje. | N/A | 204 Sin contenido | `DELETE /api/v3/maestros/gramajes-tela/1` |

### Colores de Tela

| Método HTTP | Ruta | Descripción | Datos de entrada | Respuestas | Ejemplo (Postman) |
|---|---|---|---|---|---|
| POST | `/api/v3/maestros/colores-tela` | Crea un color de tela. | `ColorTelaRequestDTO { codigoColor, descripcionColor, esPantone }` | 201 Creado | `{ "codigoColor": "COL-NEG", "descripcionColor": "Negro", "esPantone": false }` |
| PUT | `/api/v3/maestros/colores-tela/{id}` | Actualiza un color de tela. | `ColorTelaRequestDTO { campos }` | 200 OK | `PUT /api/v3/maestros/colores-tela/1` → `{ "codigoColor": "COL-AZU", "descripcionColor": "Azul marino", "esPantone": true }` |
| GET | `/api/v3/maestros/colores-tela/{id}` | Obtiene un color de tela por ID. | N/A | 200 OK, 404 No encontrado | `GET /api/v3/maestros/colores-tela/1` |
| GET | `/api/v3/maestros/colores-tela` | Lista todos los colores de tela. | N/A | 200 OK | `GET /api/v3/maestros/colores-tela` |
| DELETE | `/api/v3/maestros/colores-tela/{id}` | Elimina un color de tela. | N/A | 204 Sin contenido | `DELETE /api/v3/maestros/colores-tela/1` |

### Atributos Técnicos

| Método HTTP | Ruta | Descripción | Datos de entrada | Respuestas | Ejemplo (Postman) |
|---|---|---|---|---|---|
| POST | `/api/v3/maestros/atributos-tecnicos` | Crea un atributo técnico de tela. | `AtributoTecnicoRequestDTO { codigoAtributo, clasificacion, descripcionTecnica, impactoErp }` | 201 Creado | `{ "codigoAtributo": "ATR-01", "clasificacion": "Resistencia", "descripcionTecnica": "Protección anti-UV", "impactoErp": "Ninguno" }` |
| PUT | `/api/v3/maestros/atributos-tecnicos/{id}` | Actualiza un atributo técnico. | `AtributoTecnicoRequestDTO { campos }` | 200 OK | `PUT /api/v3/maestros/atributos-tecnicos/1` → `{ "codigoAtributo": "ATR-01", "clasificacion": "Resistencia", "descripcionTecnica": "Anti-UV 50+", "impactoErp": "Ninguno" }` |
| GET | `/api/v3/maestros/atributos-tecnicos/{id}` | Obtiene un atributo técnico por ID. | N/A | 200 OK, 404 No encontrado | `GET /api/v3/maestros/atributos-tecnicos/1` |
| GET | `/api/v3/maestros/atributos-tecnicos` | Lista todos los atributos técnicos. | N/A | 200 OK | `GET /api/v3/maestros/atributos-tecnicos` |
| DELETE | `/api/v3/maestros/atributos-tecnicos/{id}` | Elimina un atributo técnico. | N/A | 204 Sin contenido | `DELETE /api/v3/maestros/atributos-tecnicos/1` |
