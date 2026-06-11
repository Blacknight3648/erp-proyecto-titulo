# Casos de Uso del Sistema — ERP Antuan SA Gestión

> Documento de especificación de casos de uso para el sistema ERP de gestión comercial y de producción de Antuan SA.

---

## Tabla de Contenidos

1. [Módulo Gestión de Usuarios y Acceso](#1-módulo-gestión-de-usuarios-y-acceso)
2. [Módulo Gestión de Entidades (Clientes, Proveedores, Vendedores)](#2-módulo-gestión-de-entidades)
3. [Módulo Comercial](#3-módulo-comercial)
4. [Módulo Producción](#4-módulo-producción)
5. [Módulo Reportes y Trazabilidad](#5-módulo-reportes-y-trazabilidad)
6. [Product Backlog — Historias de Usuario](#6-product-backlog--historias-de-usuario)
7. [Matriz de Trazabilidad: CU ↔ HU](#7-matriz-de-trazabilidad-casos-de-uso--historias-de-usuario)
8. [Trazabilidad Inversa: HU → CU](#8-trazabilidad-inversa-historias-de-usuario--casos-de-uso)
9. [Resumen de Cobertura Cruzada](#9-resumen-de-cobertura-cruzada)

---

## 1. Módulo Gestión de Usuarios y Acceso

| CU | Caso de uso | Actores | Precondición | Flujo básico | Postcondición |
|----|-------------|---------|--------------|--------------|---------------|
| CU-01 | Gestionar Usuarios | Administrador | El actor ha iniciado sesión y cuenta con permisos de administración del sistema. | 1. El administrador accede al módulo de gestión de usuarios.<br>2. Selecciona la acción deseada: crear, editar, listar o eliminar usuario.<br>3. Para crear: ingresa RUN, nombre, apellidos, email, teléfono y contraseña.<br>4. El sistema valida el formato del RUN chileno (dígito verificador), unicidad de email y unicidad de RUN.<br>5. El sistema registra el usuario con estado habilitado por defecto.<br>6. Para editar: modifica los campos deseados; solo se actualizan los campos no vacíos.<br>7. Para eliminar: el sistema desvincula al vendedor asociado (si existe) de evaluaciones de negocio, notas de venta, solicitudes de costos y solicitudes de cotización antes de eliminar. | El usuario queda registrado/actualizado/eliminado en el sistema. Los datos del vendedor asociado quedan desvinculados de los documentos comerciales si se eliminó el usuario. |
| CU-02 | Habilitar / Deshabilitar Usuario | Administrador | El usuario objetivo existe en el sistema y el actor ha iniciado sesión. | 1. El administrador busca al usuario por ID.<br>2. Selecciona la opción de habilitar/deshabilitar (toggle).<br>3. El sistema invierte el estado actual de habilitación del usuario.<br>4. El sistema guarda el cambio y confirma la acción. | El campo `enabled` del usuario queda invertido (true↔false). Un usuario deshabilitado no puede operar en el sistema. |
| CU-03 | Gestionar Roles | Administrador | El actor ha iniciado sesión y existe al menos un área registrada en el sistema. | 1. El administrador accede al módulo de roles.<br>2. Selecciona crear, editar, eliminar o listar roles.<br>3. Para crear: ingresa nombre, descripción y área a la que pertenece el rol.<br>4. Para editar parcial: puede modificar solo nombre, descripción, área o permisos de forma independiente.<br>5. El sistema persiste los cambios. | El rol queda creado/actualizado/eliminado. Los permisos asociados al rol quedan actualizados. Los usuarios que tengan el rol reflejan los cambios en sus permisos. |
| CU-04 | Gestionar Áreas | Administrador | El actor ha iniciado sesión con permisos de administración. | 1. El administrador accede al módulo de áreas.<br>2. Selecciona crear, editar, eliminar o listar áreas.<br>3. Para crear: ingresa nombre (obligatorio, único) y descripción del área.<br>4. Para editar parcial: puede actualizar solo nombre o solo descripción.<br>5. El sistema valida unicidad del nombre y persiste los cambios. | El área queda creada/actualizada/eliminada. Los roles y usuarios asociados al área se ven afectados en cascada. |
| CU-05 | Asignar Roles a Usuario | Administrador | El usuario y los roles existen en el sistema. | 1. El administrador selecciona un usuario.<br>2. Ingresa el conjunto de nombres de roles a asignar.<br>3. El sistema busca cada rol por nombre en la base de datos.<br>4. Si un rol no se encuentra, el sistema informa el error indicando el nombre del rol faltante.<br>5. El sistema reemplaza los roles actuales del usuario por los nuevos y guarda. | El usuario tiene asignados exactamente los roles indicados. Los roles anteriores que no estén en la nueva lista se desvinculan. |
| CU-06 | Asignar Áreas a Usuario | Administrador | El usuario y las áreas existen en el sistema. | 1. El administrador selecciona un usuario.<br>2. Ingresa el conjunto de nombres de áreas a asignar.<br>3. El sistema busca cada área por nombre.<br>4. Si un área no se encuentra, el sistema informa el error.<br>5. El sistema reemplaza las áreas actuales del usuario y guarda. | El usuario tiene asignadas exactamente las áreas indicadas. |

---

## 2. Módulo Gestión de Entidades

| CU | Caso de uso | Actores | Precondición | Flujo básico | Postcondición |
|----|-------------|---------|--------------|--------------|---------------|
| CU-07 | Gestionar Clientes | Vendedor, Jefe Comercial, Administrador | El actor ha iniciado sesión en el sistema. | 1. El actor accede al módulo de clientes.<br>2. Selecciona la acción: crear, editar, listar o eliminar cliente.<br>3. Para crear: ingresa RUN del cliente, razón social, sigla, hora de atención y giro comercial.<br>4. Opcionalmente agrega contactos, direcciones y datos bancarios.<br>5. El sistema valida el RUN y la unicidad de los datos.<br>6. Para listar: puede filtrar por estado (activos/inactivos), razón social, sigla, giro o descripción de giro.<br>7. Para buscar: puede hacerlo por ID, RUN o combinaciones de filtros (activos por sigla, activos por giro). | El cliente queda registrado/actualizado/eliminado. Los contactos, direcciones y datos bancarios quedan asociados al cliente. |
| CU-08 | Gestionar Proveedores | Asistente de Producción, Jefe de Producción, Administrador | El actor ha iniciado sesión en el sistema. | 1. El actor accede al módulo de proveedores.<br>2. Selecciona crear, editar, listar o eliminar proveedor.<br>3. Para crear: ingresa RUN, razón social, datos de contacto, dirección y datos bancarios del proveedor.<br>4. El sistema valida los datos ingresados.<br>5. Puede filtrar proveedores por estado (activos/inactivos), giro, razón social o sigla. | El proveedor queda registrado/actualizado/eliminado y disponible para asociarse a órdenes de compra y órdenes de servicio. |
| CU-09 | Gestionar Vendedores | Jefe Comercial, Administrador | El usuario al que se asociará el vendedor ya existe en el sistema. | 1. El actor accede al módulo de vendedores.<br>2. Selecciona crear, editar, listar o eliminar vendedor.<br>3. Para crear: asocia un usuario existente al perfil de vendedor con sus datos específicos.<br>4. Puede buscar un vendedor por ID de usuario asociado.<br>5. El sistema persiste la asociación usuario-vendedor. | El vendedor queda creado/actualizado/eliminado. El vendedor puede ser asignado a documentos comerciales (EVN, NV, SCOS, SCOT). |

---

## 3. Módulo Comercial

| CU | Caso de uso | Actores | Precondición | Flujo básico | Postcondición |
|----|-------------|---------|--------------|--------------|---------------|
| CU-10 | Gestionar Solicitud de Costos (SCOS) | Vendedor, Jefe Comercial | El cliente y el vendedor están registrados en el sistema. El actor ha iniciado sesión. | 1. El actor accede al módulo de solicitudes de costos.<br>2. Crea una nueva SCOS indicando: tipo, cliente, vendedor, descripción del artículo, nombre de la prenda, si es muestra, si lleva logo, cantidad, género y tallaje.<br>3. El sistema asigna un número de documento (SCOS) de forma atómica y establece estado PENDIENTE.<br>4. Opcionalmente agrega telas (material, color, proveedor, consumo), accesorios, logotipos y descripciones de plantilla.<br>5. Para editar: modifica los datos de la solicitud existente.<br>6. Puede listar todas las solicitudes o buscar por ID.<br>7. Para eliminar: el sistema borra la solicitud y sus dependencias. | La SCOS queda registrada con estado PENDIENTE, número correlativo único, y disponible para el proceso de costeo y la ficha técnica. |
| CU-11 | Gestionar Ficha Técnica (Descripción Plantilla) | Vendedor, Jefe Comercial, Asistente de Producción | El cliente tiene una solicitud de costos (SCOS) registrada y el actor ha iniciado sesión en el sistema. | 1. El actor crea o edita una ficha técnica (descripción plantilla) asociada a una SCOS.<br>2. Define telas, colores, accesorios y personalizaciones según los campos de la plantilla.<br>3. El sistema asocia la descripción a la SCOS indicada.<br>4. Puede listar todas las descripciones de una SCOS específica.<br>5. Para editar: actualiza los datos de la ficha existente.<br>6. Para eliminar: el sistema borra la ficha técnica. | La ficha técnica queda almacenada y disponible para costeo, producción y futuras consultas. |
| CU-12 | Gestionar Solicitud de Cotización (SCOT) | Vendedor, Jefe Comercial | El cliente y el vendedor están registrados. El actor ha iniciado sesión. | 1. El actor accede al módulo de solicitudes de cotización.<br>2. Crea una nueva SCOT indicando: tipo, cliente, vendedor, descripción del artículo, si es muestra, si tiene logo y cantidad.<br>3. El sistema asigna un número de documento (SCOT) correlativo y estado PENDIENTE.<br>4. Opcionalmente agrega prendas listas y logotipos a la solicitud.<br>5. Puede editar, listar o eliminar solicitudes de cotización. | La SCOT queda registrada con estado PENDIENTE y número correlativo. Disponible para generar una evaluación de negocio posterior. |
| CU-13 | Registrar Costeo | Asistente de Producción, Jefe de Producción | Existe al menos una solicitud de costos (SCOS) registrada. El actor ha iniciado sesión. | 1. El actor accede al módulo de costeos.<br>2. Crea un nuevo costeo vinculado a una SCOS, ingresando costos de materiales, mano de obra y gastos generales por ítem.<br>3. El sistema genera versiones del costeo (versionamiento).<br>4. Puede editar el costeo para ajustar valores y generar nuevas versiones.<br>5. Puede consultar costeos por SCOS, listar todos o filtrar disponibles para EVN. | El costeo queda registrado con sus versiones e ítems. Disponible para ser vinculado a una Evaluación de Negocio. |
| CU-14 | Crear Evaluación de Negocio (EVN) | Vendedor, Jefe Comercial | El cliente y el vendedor están registrados. El actor ha iniciado sesión. | 1. El actor accede al módulo de evaluaciones de negocio.<br>2. Crea una nueva EVN indicando: cliente, vendedor, referencia comercial, porcentaje de comisión.<br>3. El sistema asigna un número de documento (EVN) correlativo y establece estado BORRADOR.<br>4. Agrega ítems a la EVN con precio unitario, costo unitario y cantidad (puede vincular costeos).<br>5. Opcionalmente agrega gastos adicionales (transporte, embalaje, etc.) y datos de toma de tallaje (días, personal, colación, movilización).<br>6. El sistema calcula automáticamente: monto total, costo total, comisión, margen de ganancia y rentabilidad esperada. | La EVN queda registrada en estado BORRADOR con cálculos financieros actualizados. |
| CU-15 | Actualizar Evaluación de Negocio | Vendedor, Jefe Comercial | La EVN existe en el sistema. | 1. El actor selecciona una EVN existente.<br>2. Modifica ítems (precios, cantidades, costos), gastos adicionales o datos de tallaje.<br>3. El sistema recalcula todos los totales financieros (monto, costo, comisión, margen, rentabilidad).<br>4. El sistema guarda los cambios sin modificar el número ni el estado de la EVN. | La EVN queda actualizada con los nuevos valores financieros recalculados. |
| CU-16 | Aprobar Evaluación de Negocio | Jefe Comercial, Gerente | La EVN existe en estado BORRADOR o EVALUACIÓN, tiene al menos un ítem y el monto total es mayor a cero. | 1. El actor selecciona la EVN a aprobar.<br>2. Ingresa su nombre como aprobador y opcionalmente una observación.<br>3. El sistema valida que la EVN esté en estado BORRADOR o EVALUACIÓN.<br>4. Valida que tenga ítems y que el monto total sea mayor a cero.<br>5. Cambia el estado a APROBADA y registra la firma y fecha en el historial de estados. | La EVN queda en estado APROBADA. Se registra en el historial quién aprobó, cuándo y con qué observación. |
| CU-17 | Rechazar Evaluación de Negocio | Jefe Comercial, Gerente | La EVN existe en estado BORRADOR o EVALUACIÓN. | 1. El actor selecciona la EVN a rechazar.<br>2. Ingresa su nombre y el motivo del rechazo (obligatorio).<br>3. El sistema valida que la EVN esté en estado BORRADOR o EVALUACIÓN.<br>4. Cambia el estado a RECHAZADA y registra la firma, motivo y fecha en el historial. | La EVN queda en estado RECHAZADA con el motivo documentado en el historial. |
| CU-18 | Adjudicar Evaluación de Negocio | Jefe Comercial, Gerente | La EVN existe en estado BORRADOR, EVALUACIÓN o APROBADA. | 1. El actor selecciona la EVN a adjudicar.<br>2. Opcionalmente ingresa su nombre y observación.<br>3. El sistema valida que la EVN esté en un estado activo (BORRADOR, EVALUACIÓN o APROBADA).<br>4. Cambia el estado a ADJUDICADA.<br>5. Genera automáticamente una Nota de Venta (NV) asociada a la EVN.<br>6. Registra la firma en el historial de estados. | La EVN queda en estado ADJUDICADA. Se genera automáticamente una Nota de Venta asociada con los datos de la EVN. |
| CU-19 | Consultar Historial de Estados (EVN) | Vendedor, Jefe Comercial, Gerente | La EVN existe en el sistema. | 1. El actor selecciona una EVN.<br>2. Consulta el historial de cambios de estado.<br>3. El sistema retorna la lista cronológica de todos los cambios de estado con: estado anterior, estado nuevo, fecha, usuario que realizó el cambio y observación/motivo. | El actor visualiza la trazabilidad completa de los cambios de estado de la EVN. |
| CU-20 | Crear Nota de Venta (NV) | Vendedor, Jefe Comercial | El cliente y el vendedor están registrados. Opcionalmente existe una EVN adjudicada. El actor ha iniciado sesión. | 1. El actor accede al módulo de notas de venta.<br>2. Crea una NV indicando: EVN de origen (opcional), cliente, vendedor, si es kit, detalle del kit y fecha de entrega estimada.<br>3. El sistema asigna un número de documento (NV) correlativo y establece estado BORRADOR.<br>4. Agrega ítems a la NV con descripción, cantidad, precio unitario y tallas.<br>5. El sistema calcula automáticamente: subtotal, IVA (19%) y monto total.<br>6. Puede previsualizar el próximo número de NV antes de crear. | La NV queda registrada en estado BORRADOR con los cálculos de IVA y totales actualizados. |
| CU-21 | Aprobar Nota de Venta | Jefe Comercial, Gerente | La NV existe en estado BORRADOR. | 1. El actor selecciona la NV a aprobar.<br>2. Ingresa su nombre como aprobador y opcionalmente una observación.<br>3. El sistema valida que la NV esté en estado BORRADOR.<br>4. Cambia el estado a APROBADA y registra la firma en el historial.<br>5. La NV aprobada queda disponible para generar órdenes de producción. | La NV queda en estado APROBADA con la firma de aprobación registrada en el historial. |
| CU-22 | Cancelar Nota de Venta | Jefe Comercial, Gerente | La NV existe y NO está en estado COMPLETADA ni ENTREGADA. | 1. El actor selecciona la NV a cancelar.<br>2. Ingresa su nombre y el motivo de cancelación.<br>3. El sistema valida que la NV no esté en estado COMPLETADA ni ENTREGADA.<br>4. Cambia el estado a CANCELADA y registra el motivo en el historial. | La NV queda en estado CANCELADA. No se puede revertir. El motivo queda documentado en el historial. |
| CU-23 | Consultar Trazabilidad de Nota de Venta | Vendedor, Jefe Comercial, Gerente, Asistente de Producción | La NV existe en el sistema. | 1. El actor selecciona una NV.<br>2. Consulta la trazabilidad del documento.<br>3. El sistema retorna el árbol completo de documentos relacionados: EVN de origen, órdenes de producción, órdenes de trabajo, hojas de compra, órdenes de compra y órdenes de servicio derivadas. | El actor visualiza la cadena completa de documentos generados a partir de la NV. |

---

## 4. Módulo Producción

| CU | Caso de uso | Actores | Precondición | Flujo básico | Postcondición |
|----|-------------|---------|--------------|--------------|---------------|
| CU-24 | Consultar Órdenes de Producción (OP) | Jefe de Producción, Asistente de Producción | Existen OPs generadas en el sistema. El actor ha iniciado sesión. | 1. El actor accede al módulo de órdenes de producción.<br>2. Lista todas las OPs o busca una OP específica por ID.<br>3. El sistema retorna la información de la OP con sus ítems (prendas, cantidades, tallas).<br>4. Puede consultar el porcentaje de avance de la OP. | El actor visualiza el estado y detalle de las órdenes de producción con su avance calculado. |
| CU-25 | Recepcionar Orden de Producción | Jefe de Producción | La OP existe y está en un estado que permite recepción. | 1. El actor selecciona la OP a recepcionar.<br>2. El sistema marca la OP como recepcionada.<br>3. Se actualiza el estado de la OP y se persiste el cambio. | La OP queda marcada como recepcionada, indicando que las prendas finalizadas fueron recibidas en bodega. |
| CU-26 | Calcular Avance de Orden de Producción | Jefe de Producción, Asistente de Producción | La OP existe y tiene órdenes de trabajo (OT) asociadas. | 1. El actor selecciona una OP.<br>2. Solicita el cálculo de avance.<br>3. El sistema recorre todas las OTs de la OP, sumando las cantidades producidas y mermas reportadas.<br>4. Calcula el porcentaje de avance por fase (Corte, Confección, etc.) y el avance global. | El actor obtiene un resumen detallado del avance de producción por fase y global de la OP. |
| CU-27 | Gestionar Órdenes de Trabajo (OT) | Jefe de Producción, Operario de Producción | Existe una OP asociada. El actor ha iniciado sesión. | 1. El actor accede al módulo de órdenes de trabajo.<br>2. Lista las OTs por Nota de Venta o por Orden de Producción.<br>3. Cada OT corresponde a una fase de producción (Corte, Confección, Terminación, etc.).<br>4. Para iniciar una OT: cambia el estado de PENDIENTE a EN_PROCESO.<br>5. Para finalizar una OT: cambia el estado a FINALIZADA manualmente. | La OT queda en el estado correspondiente (PENDIENTE, EN_PROCESO o FINALIZADA). |
| CU-28 | Registrar Avance en Orden de Trabajo | Operario de Producción, Jefe de Producción | La OT existe y está en estado EN_PROCESO. | 1. El operario selecciona la OT en la que trabaja.<br>2. Registra el avance del día: cantidad producida, merma (unidades defectuosas) y observaciones.<br>3. El sistema valida los datos y registra el avance con fecha y hora.<br>4. Puede listar todos los avances registrados para una OT específica. | El avance queda registrado en la OT. El porcentaje de avance de la OP se actualiza automáticamente al ser consultado. |
| CU-29 | Generar Hoja de Compra (HC) | Asistente de Producción, Jefe de Producción | Existe una OP registrada con ítems que requieren materiales. | 1. El actor selecciona una OP.<br>2. Solicita la generación de la hoja de compra.<br>3. El sistema analiza los ítems de la OP y genera una HC consolidada con los materiales necesarios (telas, accesorios, insumos), cantidades, proveedores sugeridos y costos estimados.<br>4. La HC se crea en estado BORRADOR.<br>5. Puede listar HCs por estado o por OP. | La HC queda generada en estado BORRADOR con el detalle de materiales requeridos para la OP. |
| CU-30 | Aprobar Hoja de Compra | Jefe de Producción, Gerente | La HC existe en estado BORRADOR. | 1. El actor selecciona la HC a aprobar.<br>2. El sistema valida que la HC esté en estado BORRADOR.<br>3. Cambia el estado a APROBADA.<br>4. La HC aprobada habilita la generación de órdenes de compra. | La HC queda en estado APROBADA y lista para generar órdenes de compra consolidadas. |
| CU-31 | Cerrar Hoja de Compra | Jefe de Producción | La HC existe en un estado que permite cierre. | 1. El actor selecciona la HC a cerrar.<br>2. El sistema cambia el estado a CERRADA.<br>3. No se pueden generar más OCs desde esta HC. | La HC queda en estado CERRADA. No se pueden realizar más operaciones sobre ella. |
| CU-32 | Generar Orden de Compra Consolidada (OC) | Asistente de Producción, Jefe de Producción | Existe al menos una HC aprobada. Los proveedores están registrados. | 1. El actor solicita generar una OC consolidada indicando los ítems de HC a incluir y el proveedor destino.<br>2. El sistema agrupa los materiales por proveedor y genera la OC con estado EMITIDA.<br>3. Asigna un número de documento (OC) correlativo.<br>4. Calcula el total neto sumando los subtotales de todos los ítems.<br>5. Puede agregar, editar o eliminar ítems de la OC mientras esté en estado EMITIDA. | La OC queda generada en estado EMITIDA con los materiales consolidados, proveedor asignado y total calculado. |
| CU-33 | Gestionar Ciclo de Vida de Orden de Compra | Asistente de Producción, Jefe de Producción | La OC existe en el sistema. | 1. **Enviar**: el actor marca la OC como ENVIADA al proveedor (transición EMITIDA → ENVIADA).<br>2. **Recepcionar**: marca la OC como RECEPCIONADA al recibir los materiales (ENVIADA → RECEPCIONADA o RECEPCIONADA_PARCIAL).<br>3. **Cerrar**: cierra la OC una vez completada la recepción (RECEPCIONADA → CERRADA).<br>4. El sistema valida que cada transición de estado sea válida según la máquina de estados (no se permiten saltos).<br>5. Puede actualizar el precio unitario de un ítem cuando la OC está en estado EMITIDA, recalculando subtotal y total. | La OC avanza en su ciclo de vida según las transiciones válidas. El total se recalcula al modificar precios. |
| CU-34 | Registrar Recepción de Orden de Compra | Asistente de Producción, Bodeguero | La OC existe y ha sido enviada (estado ENVIADA o RECEPCIONADA_PARCIAL). | 1. El actor selecciona la OC a recepcionar.<br>2. Registra los ítems recibidos con: cantidad recibida, cantidad rechazada/defectuosa y observaciones.<br>3. El sistema crea un registro de recepción con fecha y hora.<br>4. Si la recepción es parcial, la OC pasa a RECEPCIONADA_PARCIAL.<br>5. Si la recepción es total, la OC pasa a RECEPCIONADA.<br>6. Puede consultar el historial de recepciones de una OC. | Queda registrada la recepción con cantidades recibidas y rechazadas. El estado de la OC se actualiza según la completitud. |
| CU-35 | Crear Orden de Servicio (OS) | Jefe de Producción, Asistente de Producción | Existe una OP registrada. Los proveedores de servicio (talleres externos) están registrados. | 1. El actor crea una OS indicando: OP de origen, proveedor de servicio (taller), tipo de servicio, fecha de entrega estimada y observaciones.<br>2. El sistema registra la OS y calcula los valores según los ítems.<br>3. Puede listar OSs por estado, por OP o por proveedor. | La OS queda creada y asociada a la OP, disponible para gestionar despachos y recepciones con el taller externo. |
| CU-36 | Registrar Despacho de Orden de Servicio | Asistente de Producción | La OS existe y está en un estado que permite despacho. | 1. El actor selecciona la OS.<br>2. Registra el despacho indicando: cantidad despachada, fecha de despacho y observaciones.<br>3. El sistema registra el despacho y actualiza el estado de la OS a EN_PROCESO. | El despacho queda registrado. La OS refleja las cantidades enviadas al taller externo. |
| CU-37 | Registrar Recepción de Orden de Servicio | Asistente de Producción | La OS existe y tiene despachos registrados. | 1. El actor selecciona la OS.<br>2. Registra la recepción indicando: cantidad recibida del taller, cantidad con defectos y observaciones.<br>3. El sistema registra la recepción y actualiza el estado de la OS según la completitud (RECEPCIONADA parcial o total). | La recepción queda registrada. El estado de la OS se actualiza según las cantidades recibidas vs. despachadas. |
| CU-38 | Cerrar Orden de Servicio | Jefe de Producción | La OS existe en un estado que permite cierre. | 1. El actor selecciona la OS a cerrar.<br>2. El sistema valida la transición de estado y cierra la OS.<br>3. No se permiten más despachos ni recepciones. | La OS queda en estado CERRADA. Las cantidades finales quedan consolidadas. |

---

## 5. Módulo Reportes y Trazabilidad

| CU | Caso de uso | Actores | Precondición | Flujo básico | Postcondición |
|----|-------------|---------|--------------|--------------|---------------|
| CU-39 | Consultar Hojas de Compra Pendientes de Aprobación | Jefe de Producción, Gerente | Existen HCs registradas en el sistema. | 1. El actor accede al reporte de HCs pendientes.<br>2. El sistema consulta todas las Hojas de Compra en estado BORRADOR.<br>3. Retorna la lista con el detalle de cada HC pendiente (OP asociada, materiales, cantidades y costos estimados). | El actor visualiza todas las HCs que requieren aprobación para proceder con las compras. |
| CU-40 | Consultar Órdenes de Compra Pendientes de Recepción | Asistente de Producción, Jefe de Producción, Bodeguero | Existen OCs registradas en el sistema. | 1. El actor accede al reporte de OCs pendientes de recepción.<br>2. El sistema consulta todas las OCs en estado EMITIDA, ENVIADA o RECEPCIONADA_PARCIAL.<br>3. Retorna la lista consolidada con proveedor, materiales pendientes y fechas de entrega estimada. | El actor visualiza todas las OCs que tienen recepciones pendientes, permitiendo planificar la recepción de materiales. |
| CU-41 | Consultar Órdenes de Servicio en Taller | Jefe de Producción, Asistente de Producción | Existen OSs registradas en el sistema. | 1. El actor accede al reporte de OSs en taller.<br>2. El sistema consulta todas las OSs en estado EN_PROCESO o RECEPCIONADA.<br>3. Retorna la lista con detalle de proveedor de servicio, cantidades despachadas y recibidas. | El actor visualiza el trabajo pendiente en talleres externos, facilitando el seguimiento y la coordinación. |
| CU-42 | Consultar Trazabilidad de Orden de Producción | Jefe de Producción, Gerente | La OP existe en el sistema. | 1. El actor selecciona una OP.<br>2. Solicita la trazabilidad completa.<br>3. El sistema construye el árbol de documentos: NV de origen → OP → OTs por fase → HCs → OCs → Recepciones → OSs → Despachos/Recepciones.<br>4. Retorna la estructura jerárquica completa con estados y fechas de cada documento. | El actor visualiza la trazabilidad completa de la cadena productiva desde la nota de venta hasta las operaciones de taller. |
| CU-43 | Consultar Historial de Estados de Nota de Venta | Vendedor, Jefe Comercial, Gerente | La NV existe en el sistema. | 1. El actor selecciona una NV.<br>2. Consulta el historial de cambios de estado.<br>3. El sistema retorna la lista cronológica: estado anterior → estado nuevo, fecha, usuario responsable y observación/motivo en cada cambio. | El actor visualiza la trazabilidad de estados de la NV (BORRADOR → APROBADA → EN_PRODUCCIÓN → COMPLETADA → ENTREGADA o CANCELADA). |
| CU-44 | Consultar Resumen de Costeo para EVN | Vendedor, Jefe Comercial | Existe un costeo registrado en el sistema. | 1. El actor selecciona un costeo existente.<br>2. Solicita el resumen para vincular a una EVN.<br>3. El sistema retorna: descripción del artículo, costo unitario, costo total y detalles del costeo para auto-rellenar un ítem de la EVN. | El actor obtiene los datos resumidos del costeo, listos para vincular como ítem en una Evaluación de Negocio. |

---

## Resumen de Casos de Uso por Módulo

| Módulo | Cantidad | Rango |
|--------|----------|-------|
| Gestión de Usuarios y Acceso | 6 | CU-01 a CU-06 |
| Gestión de Entidades | 3 | CU-07 a CU-09 |
| Comercial | 14 | CU-10 a CU-23 |
| Producción | 15 | CU-24 a CU-38 |
| Reportes y Trazabilidad | 6 | CU-39 a CU-44 |
| **Total** | **44** | |

---

## Diagrama de Actores del Sistema

| Actor | Descripción | Módulos principales |
|-------|-------------|---------------------|
| **Administrador** | Gestiona usuarios, roles, áreas y configuración del sistema. | Gestión de Usuarios, Gestión de Entidades |
| **Vendedor** | Crea y gestiona documentos comerciales: SCOS, SCOT, EVN, NV. Gestiona clientes. | Comercial, Gestión de Entidades |
| **Jefe Comercial** | Aprueba/rechaza/adjudica documentos comerciales. Supervisa el área comercial. | Comercial, Reportes |
| **Gerente** | Aprueba documentos de alto impacto (EVN, NV, HC). Consulta reportes y trazabilidad. | Comercial, Producción, Reportes |
| **Jefe de Producción** | Gestiona OPs, OTs, HCs, OCs y OSs. Aprueba hojas de compra. | Producción, Reportes |
| **Asistente de Producción** | Apoya en la gestión de fichas técnicas, OCs, OSs y recepciones. | Producción, Comercial |
| **Operario de Producción** | Registra avances y mermas diarias en las OTs asignadas. | Producción |
| **Bodeguero** | Registra recepciones de OCs y gestiona inventario entrante. | Producción |

---

## 6. Product Backlog — Historias de Usuario

| ID | Feature | Historia de Usuario | Prioridad | Story Points | Sprint |
|----|---------|---------------------|-----------|:------------:|--------|
| HU-001 | Login de usuarios | Como usuario quiero iniciar sesión para acceder al ERP de forma segura | Must Have | 5 | Sprint 1 |
| HU-002 | JWT Authentication | Como sistema quiero generar tokens JWT para validar sesiones seguras | Must Have | 5 | Sprint 1 |
| HU-003 | Gestión RBAC | Como administrador quiero asignar roles para controlar permisos de acceso | Must Have | 8 | Sprint 1 |
| HU-004 | CRUD usuarios | Como administrador quiero crear usuarios para permitir acceso al sistema | Must Have | 5 | Sprint 1 |
| HU-005 | CRUD usuarios | Como administrador quiero editar usuarios para mantener información actualizada | Must Have | 3 | Sprint 1 |
| HU-006 | CRUD usuarios | Como administrador quiero desactivar usuarios para bloquear accesos no autorizados | Must Have | 3 | Sprint 1 |
| HU-007 | Auditoría de acciones | Como auditor quiero visualizar logs para revisar acciones críticas del sistema | Should Have | 5 | Sprint 1 |
| HU-008 | Gestión de clientes | Como ejecutivo comercial quiero registrar clientes para asociarlos a solicitudes comerciales | Must Have | 5 | Sprint 2 |
| HU-009 | Gestión de clientes | Como ejecutivo comercial quiero editar clientes para mantener información comercial actualizada | Must Have | 3 | Sprint 2 |
| HU-010 | Gestión SCOS | Como ejecutivo comercial quiero registrar solicitudes SCOS para iniciar procesos comerciales | Must Have | 8 | Sprint 2 |
| HU-011 | Workflow comercial | Como ejecutivo comercial quiero actualizar estados comerciales para controlar el avance de solicitudes | Must Have | 5 | Sprint 2 |
| HU-012 | Gestión EVN | Como ejecutivo comercial quiero registrar evaluaciones de negocio para validar viabilidad comercial | Must Have | 8 | Sprint 2 |
| HU-013 | APIs comerciales | Como sistema quiero exponer APIs REST para integrar información comercial | Should Have | 5 | Sprint 2 |
| HU-014 | Gestión notas de venta | Como ejecutivo comercial quiero generar notas de venta para formalizar acuerdos comerciales | Must Have | 8 | Sprint 3 |
| HU-015 | Gestión fichas técnicas | Como diseñador textil quiero crear fichas técnicas para definir especificaciones del producto | Must Have | 13 | Sprint 3 |
| HU-016 | Gestión fichas técnicas | Como diseñador textil quiero editar fichas técnicas para actualizar especificaciones textiles | Must Have | 5 | Sprint 3 |
| HU-017 | Versionado fichas | Como producción quiero visualizar versiones de fichas técnicas para mantener trazabilidad | Should Have | 8 | Sprint 3 |
| HU-018 | Asociación materiales | Como producción quiero asociar materiales a fichas técnicas para calcular requerimientos | Must Have | 8 | Sprint 3 |
| HU-019 | Gestión órdenes producción | Como jefe de producción quiero generar órdenes de producción para iniciar fabricación | Must Have | 8 | Sprint 4 |
| HU-020 | Seguimiento producción | Como jefe de producción quiero visualizar estados de producción para controlar avance operativo | Must Have | 5 | Sprint 4 |
| HU-021 | Consumo automático materiales | Como sistema quiero descontar materiales automáticamente para mantener stock actualizado | Must Have | 13 | Sprint 4 |
| HU-022 | Gestión materias primas | Como bodeguero quiero registrar materias primas para controlar inventario | Must Have | 5 | Sprint 5 |
| HU-023 | Control stock | Como bodeguero quiero visualizar stock disponible para controlar abastecimiento | Must Have | 5 | Sprint 5 |
| HU-024 | Alertas stock crítico | Como bodeguero quiero recibir alertas de stock crítico para evitar quiebres de inventario | Should Have | 5 | Sprint 5 |
| HU-025 | Gestión proveedores | Como encargado de compras quiero registrar proveedores para gestionar abastecimiento | Must Have | 5 | Sprint 5 |
| HU-026 | Gestión órdenes compra | Como encargado de compras quiero generar órdenes de compra para solicitar materiales | Must Have | 8 | Sprint 5 |
| HU-027 | Recepción materiales | Como bodeguero quiero registrar recepción de materiales para actualizar inventario | Must Have | 5 | Sprint 5 |
| HU-028 | Dashboard gerencial | Como gerente quiero visualizar indicadores KPI para apoyar decisiones estratégicas | Should Have | 8 | Sprint 6 |
| HU-029 | Reportes comerciales | Como gerente quiero generar reportes comerciales para analizar ventas | Should Have | 5 | Sprint 6 |
| HU-030 | Reportes producción | Como gerente quiero generar reportes productivos para evaluar rendimiento operacional | Should Have | 5 | Sprint 6 |
| HU-031 | Exportación PDF | Como usuario quiero exportar documentos PDF para compartir información | Could Have | 3 | Sprint 6 |
| HU-032 | APIs integración legacy | Como sistema quiero integrar información legacy para mantener continuidad operacional | Should Have | 13 | Sprint 6 |
| HU-033 | Notificaciones internas | Como usuario quiero recibir notificaciones para visualizar eventos importantes | Could Have | 5 | Sprint 6 |
| HU-034 | Testing unitario | Como equipo de desarrollo queremos validar componentes para asegurar calidad del sistema | Must Have | 8 | Sprint 7 |
| HU-035 | Testing integración | Como QA quiero validar integración entre servicios para detectar errores de comunicación | Must Have | 8 | Sprint 7 |
| HU-036 | Testing E2E | Como QA quiero validar flujos completos para asegurar funcionamiento operacional | Must Have | 13 | Sprint 7 |
| HU-037 | Hardening seguridad | Como DevSecOps quiero mitigar vulnerabilidades para aumentar seguridad del ERP | Must Have | 8 | Sprint 7 |
| HU-038 | Testing performance | Como QA quiero validar rendimiento del sistema para asegurar estabilidad bajo carga | Should Have | 5 | Sprint 7 |
| HU-039 | Deploy producción | Como DevOps quiero desplegar el ERP en producción para disponibilizar la plataforma | Must Have | 8 | Sprint 8 |
| HU-040 | Observabilidad | Como DevOps quiero monitorear métricas para detectar incidentes operacionales | Should Have | 5 | Sprint 8 |
| HU-041 | Gestión logs centralizados | Como DevOps quiero centralizar logs para facilitar diagnóstico de errores | Should Have | 5 | Sprint 8 |
| HU-042 | Monitoreo infraestructura | Como DevOps quiero supervisar servicios para garantizar disponibilidad del sistema | Should Have | 5 | Sprint 8 |

### Resumen del Backlog por Sprint

| Sprint | Story Points | Historias | Foco principal |
|--------|:------------:|:---------:|----------------|
| Sprint 1 | 34 | 7 | Autenticación, gestión de usuarios y RBAC |
| Sprint 2 | 34 | 6 | Clientes, solicitudes comerciales y EVN |
| Sprint 3 | 42 | 5 | Notas de venta y fichas técnicas |
| Sprint 4 | 26 | 3 | Órdenes de producción y consumo de materiales |
| Sprint 5 | 33 | 6 | Inventario, proveedores y órdenes de compra |
| Sprint 6 | 39 | 6 | Reportes, dashboard y exportación |
| Sprint 7 | 42 | 5 | Testing y hardening de seguridad |
| Sprint 8 | 23 | 4 | Deploy, observabilidad y monitoreo |
| **Total** | **273** | **42** | |

---

## 7. Matriz de Trazabilidad: Casos de Uso ↔ Historias de Usuario

La siguiente tabla establece la correspondencia entre cada Caso de Uso (CU) del sistema y las Historias de Usuario (HU) del Product Backlog que lo implementan.

| CU | Caso de uso | HU asociadas | Sprint |
|----|-------------|--------------|--------|
| CU-01 | Gestionar Usuarios | HU-004 (Crear), HU-005 (Editar), HU-001 (Login) | Sprint 1 |
| CU-02 | Habilitar / Deshabilitar Usuario | HU-006 (Desactivar usuarios) | Sprint 1 |
| CU-03 | Gestionar Roles | HU-003 (Gestión RBAC) | Sprint 1 |
| CU-04 | Gestionar Áreas | HU-003 (Gestión RBAC) | Sprint 1 |
| CU-05 | Asignar Roles a Usuario | HU-003 (Gestión RBAC) | Sprint 1 |
| CU-06 | Asignar Áreas a Usuario | HU-003 (Gestión RBAC) | Sprint 1 |
| CU-07 | Gestionar Clientes | HU-008 (Registrar clientes), HU-009 (Editar clientes) | Sprint 2 |
| CU-08 | Gestionar Proveedores | HU-025 (Registrar proveedores) | Sprint 5 |
| CU-09 | Gestionar Vendedores | HU-004 (Crear usuarios), HU-005 (Editar usuarios) | Sprint 1 |
| CU-10 | Gestionar Solicitud de Costos (SCOS) | HU-010 (Registrar SCOS), HU-011 (Workflow comercial) | Sprint 2 |
| CU-11 | Gestionar Ficha Técnica | HU-015 (Crear fichas técnicas), HU-016 (Editar fichas), HU-018 (Asociar materiales) | Sprint 3 |
| CU-12 | Gestionar Solicitud de Cotización (SCOT) | HU-010 (Registrar SCOS), HU-011 (Workflow comercial), HU-013 (APIs comerciales) | Sprint 2 |
| CU-13 | Registrar Costeo | HU-018 (Asociar materiales), HU-017 (Versionado fichas) | Sprint 3 |
| CU-14 | Crear Evaluación de Negocio (EVN) | HU-012 (Registrar EVN) | Sprint 2 |
| CU-15 | Actualizar Evaluación de Negocio | HU-012 (Registrar EVN), HU-011 (Workflow comercial) | Sprint 2 |
| CU-16 | Aprobar Evaluación de Negocio | HU-011 (Workflow comercial), HU-012 (Registrar EVN) | Sprint 2 |
| CU-17 | Rechazar Evaluación de Negocio | HU-011 (Workflow comercial), HU-012 (Registrar EVN) | Sprint 2 |
| CU-18 | Adjudicar Evaluación de Negocio | HU-011 (Workflow comercial), HU-012 (Registrar EVN), HU-014 (Notas de venta) | Sprint 2–3 |
| CU-19 | Consultar Historial de Estados (EVN) | HU-007 (Auditoría de acciones), HU-011 (Workflow comercial) | Sprint 1–2 |
| CU-20 | Crear Nota de Venta (NV) | HU-014 (Gestión notas de venta) | Sprint 3 |
| CU-21 | Aprobar Nota de Venta | HU-014 (Gestión notas de venta), HU-011 (Workflow comercial) | Sprint 3 |
| CU-22 | Cancelar Nota de Venta | HU-014 (Gestión notas de venta), HU-011 (Workflow comercial) | Sprint 3 |
| CU-23 | Consultar Trazabilidad de NV | HU-007 (Auditoría), HU-029 (Reportes comerciales) | Sprint 1, 6 |
| CU-24 | Consultar Órdenes de Producción (OP) | HU-019 (Gestión OP), HU-020 (Seguimiento producción) | Sprint 4 |
| CU-25 | Recepcionar Orden de Producción | HU-019 (Gestión OP), HU-027 (Recepción materiales) | Sprint 4–5 |
| CU-26 | Calcular Avance de Orden de Producción | HU-020 (Seguimiento producción) | Sprint 4 |
| CU-27 | Gestionar Órdenes de Trabajo (OT) | HU-019 (Gestión OP), HU-020 (Seguimiento producción) | Sprint 4 |
| CU-28 | Registrar Avance en Orden de Trabajo | HU-020 (Seguimiento producción) | Sprint 4 |
| CU-29 | Generar Hoja de Compra (HC) | HU-021 (Consumo automático materiales), HU-026 (Gestión OC) | Sprint 4–5 |
| CU-30 | Aprobar Hoja de Compra | HU-026 (Gestión OC) | Sprint 5 |
| CU-31 | Cerrar Hoja de Compra | HU-026 (Gestión OC) | Sprint 5 |
| CU-32 | Generar Orden de Compra Consolidada (OC) | HU-026 (Gestión OC), HU-025 (Gestión proveedores) | Sprint 5 |
| CU-33 | Gestionar Ciclo de Vida de OC | HU-026 (Gestión OC) | Sprint 5 |
| CU-34 | Registrar Recepción de OC | HU-027 (Recepción materiales), HU-022 (Gestión materias primas), HU-023 (Control stock) | Sprint 5 |
| CU-35 | Crear Orden de Servicio (OS) | HU-019 (Gestión OP), HU-025 (Gestión proveedores) | Sprint 4–5 |
| CU-36 | Registrar Despacho de OS | HU-021 (Consumo automático materiales) | Sprint 4 |
| CU-37 | Registrar Recepción de OS | HU-027 (Recepción materiales) | Sprint 5 |
| CU-38 | Cerrar Orden de Servicio | HU-019 (Gestión OP) | Sprint 4 |
| CU-39 | Consultar HC Pendientes de Aprobación | HU-030 (Reportes producción), HU-028 (Dashboard gerencial) | Sprint 6 |
| CU-40 | Consultar OC Pendientes de Recepción | HU-030 (Reportes producción), HU-028 (Dashboard gerencial) | Sprint 6 |
| CU-41 | Consultar OS en Taller | HU-030 (Reportes producción), HU-028 (Dashboard gerencial) | Sprint 6 |
| CU-42 | Consultar Trazabilidad de OP | HU-030 (Reportes producción), HU-020 (Seguimiento producción) | Sprint 4, 6 |
| CU-43 | Consultar Historial de Estados NV | HU-007 (Auditoría), HU-029 (Reportes comerciales) | Sprint 1, 6 |
| CU-44 | Consultar Resumen de Costeo para EVN | HU-012 (Gestión EVN), HU-013 (APIs comerciales) | Sprint 2 |

---

## 8. Trazabilidad Inversa: Historias de Usuario → Casos de Uso

La siguiente tabla muestra, para cada Historia de Usuario, qué Casos de Uso cubre en el sistema.

| HU | Feature | Casos de Uso que implementa |
|----|---------|----------------------------|
| HU-001 | Login de usuarios | CU-01 (Gestionar Usuarios — acceso) |
| HU-002 | JWT Authentication | Transversal — soporte de autenticación para todos los CU |
| HU-003 | Gestión RBAC | CU-03 (Gestionar Roles), CU-04 (Gestionar Áreas), CU-05 (Asignar Roles), CU-06 (Asignar Áreas) |
| HU-004 | CRUD usuarios (Crear) | CU-01 (Gestionar Usuarios), CU-09 (Gestionar Vendedores) |
| HU-005 | CRUD usuarios (Editar) | CU-01 (Gestionar Usuarios), CU-09 (Gestionar Vendedores) |
| HU-006 | CRUD usuarios (Desactivar) | CU-02 (Habilitar/Deshabilitar Usuario) |
| HU-007 | Auditoría de acciones | CU-19 (Historial EVN), CU-23 (Trazabilidad NV), CU-43 (Historial NV) |
| HU-008 | Gestión de clientes (Registrar) | CU-07 (Gestionar Clientes) |
| HU-009 | Gestión de clientes (Editar) | CU-07 (Gestionar Clientes) |
| HU-010 | Gestión SCOS | CU-10 (Gestionar SCOS), CU-12 (Gestionar SCOT) |
| HU-011 | Workflow comercial | CU-10, CU-12, CU-15, CU-16, CU-17, CU-18, CU-19, CU-21, CU-22 |
| HU-012 | Gestión EVN | CU-14 (Crear EVN), CU-15 (Actualizar EVN), CU-16 (Aprobar EVN), CU-17 (Rechazar EVN), CU-18 (Adjudicar EVN), CU-44 (Resumen costeo) |
| HU-013 | APIs comerciales | CU-12 (SCOT), CU-44 (Resumen costeo para EVN) |
| HU-014 | Gestión notas de venta | CU-18 (Adjudicar EVN → genera NV), CU-20 (Crear NV), CU-21 (Aprobar NV), CU-22 (Cancelar NV) |
| HU-015 | Gestión fichas técnicas (Crear) | CU-11 (Gestionar Ficha Técnica) |
| HU-016 | Gestión fichas técnicas (Editar) | CU-11 (Gestionar Ficha Técnica) |
| HU-017 | Versionado fichas | CU-13 (Registrar Costeo — versiones) |
| HU-018 | Asociación materiales | CU-11 (Ficha Técnica), CU-13 (Registrar Costeo) |
| HU-019 | Gestión órdenes producción | CU-24 (Consultar OP), CU-25 (Recepcionar OP), CU-27 (Gestionar OT), CU-35 (Crear OS), CU-38 (Cerrar OS) |
| HU-020 | Seguimiento producción | CU-24 (Consultar OP), CU-26 (Calcular avance), CU-27 (Gestionar OT), CU-28 (Registrar avance), CU-42 (Trazabilidad OP) |
| HU-021 | Consumo automático materiales | CU-29 (Generar HC), CU-36 (Despacho OS) |
| HU-022 | Gestión materias primas | CU-34 (Registrar Recepción OC) |
| HU-023 | Control stock | CU-34 (Registrar Recepción OC) |
| HU-024 | Alertas stock crítico | Transversal — sin CU directo (funcionalidad automática del sistema) |
| HU-025 | Gestión proveedores | CU-08 (Gestionar Proveedores), CU-32 (Generar OC), CU-35 (Crear OS) |
| HU-026 | Gestión órdenes compra | CU-29 (Generar HC), CU-30 (Aprobar HC), CU-31 (Cerrar HC), CU-32 (Generar OC), CU-33 (Ciclo de vida OC) |
| HU-027 | Recepción materiales | CU-25 (Recepcionar OP), CU-34 (Recepción OC), CU-37 (Recepción OS) |
| HU-028 | Dashboard gerencial | CU-39 (HC pendientes), CU-40 (OC pendientes), CU-41 (OS en taller) |
| HU-029 | Reportes comerciales | CU-23 (Trazabilidad NV), CU-43 (Historial NV) |
| HU-030 | Reportes producción | CU-39 (HC pendientes), CU-40 (OC pendientes), CU-41 (OS en taller), CU-42 (Trazabilidad OP) |
| HU-031 | Exportación PDF | Transversal — aplica a CU-20 (NV), CU-32 (OC), CU-14 (EVN) |
| HU-032 | APIs integración legacy | Transversal — soporte de integración para módulos comerciales y producción |
| HU-033 | Notificaciones internas | Transversal — aplica a CU-16 (Aprobar EVN), CU-21 (Aprobar NV), CU-24 (Alertas stock) |
| HU-034 | Testing unitario | Transversal — valida la lógica de todos los CU |
| HU-035 | Testing integración | Transversal — valida comunicación entre módulos de todos los CU |
| HU-036 | Testing E2E | Transversal — valida flujos completos multi-CU |
| HU-037 | Hardening seguridad | Transversal — refuerza CU-01 (Usuarios), CU-02 (JWT), CU-03 (RBAC) |
| HU-038 | Testing performance | Transversal — valida rendimiento de todos los CU |
| HU-039 | Deploy producción | Transversal — habilita todos los CU en entorno productivo |
| HU-040 | Observabilidad | Transversal — monitoreo de todos los CU en operación |
| HU-041 | Gestión logs centralizados | Transversal — CU-07 (Auditoría), CU-19, CU-43 (Historial) |
| HU-042 | Monitoreo infraestructura | Transversal — disponibilidad de todos los CU |

---

## 9. Resumen de Cobertura Cruzada

| Métrica | Valor |
|---------|-------|
| Total de Casos de Uso | 44 |
| Total de Historias de Usuario | 42 |
| CU con al menos 1 HU asociada | 44 / 44 (100%) |
| HU con al menos 1 CU asociado | 35 / 42 (83%) |
| HU transversales (sin CU directo) | 7 (HU-002, HU-024, HU-031–HU-042) |
| Sprints planificados | 8 |
| Story Points totales | 273 |

### Clasificación por Prioridad MoSCoW

| Prioridad | Cantidad HU | Story Points | % del Total |
|-----------|:-----------:|:------------:|:-----------:|
| **Must Have** | 27 | 187 | 68.5% |
| **Should Have** | 12 | 76 | 27.8% |
| **Could Have** | 2 | 8 | 2.9% |
| **Won't Have** | 0 | 0 | 0% |
