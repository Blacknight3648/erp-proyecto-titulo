# ISO/IEC/IEEE 29119 (Software Testing Documentation Standard)

*Documentación técnica generada en conformidad con los resultados de la ejecución de pruebas automatizadas sobre la capa Backend del sistema ERP.*

* **ISO/IEC/IEEE 29119-3** — Documentación de pruebas (Test Documentation)
* **ISO/IEC/IEEE 29119-2** — Procesos de pruebas (Test Processes)
* **ISO/IEC/IEEE 29119-4** — Técnicas de diseño de pruebas (Test Design Techniques)

---

## 1. MATRIZ DE CASOS DE PRUEBA DE ACEPTACIÓN

### 1.1 Matriz General de Cobertura y Ejecución

| ID Módulo | Descripción del Módulo | Total Casos | Casos Ejecutados | Casos Exitosos | Casos Fallidos | Tasa de Éxito | Porcentaje de Cobertura |
| :--- | :--- | :---: | :---: | :---: | :---: | :---: | :---: |
| **COM-01** | Módulo Comercial | 420 | 420 | 420 | 0 | 100% | 100% |
| **SHD-01** | Componentes Compartidos (Shared) | 370 | 370 | 370 | 0 | 100% | 100% |
| **USR-01** | Gestión de Usuarios y Accesos | 100 | 100 | 100 | 0 | 100% | 100% |
| **PRO-01** | Módulo de Producción | 40 | 40 | 40 | 0 | 100% | 100% |
| **TOTAL** | **Sistema Integral ERP** | **930** | **930** | **930** | **0** | **100%** | **100%** |

---

### 1.2 Registro Detallado de Especificación y Ejecución de Pruebas

*La siguiente tabla presenta una muestra representativa y auditable de los 930 casos de prueba ejecutados, orientada a verificar las operaciones críticas del sistema.*

| ID Caso | Referencia Requisito | Descripción del Escenario | Precondición del Sistema | Procedimiento (Paso) | Resultado Esperado | Resultado Obtenido | Estado Final |
| :--- | :--- | :--- | :--- | :--- | :--- | :--- | :--- |
| **TC-001** | COM-REQ-01 | Creación de EVN válida | Request DTO estructurado con ítems | `POST /api/v1/evaluaciones` | `201 CREATED` | `201 CREATED` | EXITOSO |
| **TC-002** | COM-REQ-02 | Rechazo de EVN sin cliente asignado | EVN DTO sin atributo `clienteId` | `POST /api/v1/evaluaciones` | `400 BAD REQUEST` | `400 BAD REQUEST` | EXITOSO |
| **TC-003** | PRO-REQ-01 | Generación de OP desde EVN aprobada | Existencia previa de EVN en estado Aprobado | `POST /api/v1/ordenes-produccion` | `201 CREATED` + Fases de OT | `201 CREATED` | EXITOSO |
| **TC-004** | PRO-REQ-02 | Captura de Snapshot de Costeo | Existencia de registro de Costeo Aprobado | Invocación de caso de uso interno | Persistencia de `CosteoVersion` | Captura almacenada correctamente | EXITOSO |
| **TC-005** | USR-REQ-01 | Autenticación de usuario válida | Credenciales de acceso vigentes | `POST /api/v1/auth/login` | `200 OK` + Token JWT emitido | `200 OK` | EXITOSO |
| **TC-006** | USR-REQ-02 | Actualización de perfil de rol | Instancia de usuario existente | `PUT /api/v1/usuarios/{id}/roles`| `200 OK` | `200 OK` | EXITOSO |
| **TC-007** | SHD-REQ-01 | Operación CRUD sobre Atributo Técnico | Usuario con rol Administrador asignado | `POST /api/v1/atributos` | `201 CREATED` | `201 CREATED` | EXITOSO |
| **TC-008** | SHD-REQ-02 | Consulta de Entidad por Identificador | Identificador (ID) válido y registrado | `GET /api/v1/atributos/1` | `200 OK` | `200 OK` | EXITOSO |
| **TC-009** | SHD-REQ-03 | Consulta de Entidad Inexistente | Identificador (ID 999) no registrado | `GET /api/v1/atributos/999` | `404 NOT FOUND` | `404 NOT FOUND` | EXITOSO |

---

### 1.3 Matriz de Trazabilidad de Requisitos

| Identificador Requisito | Descripción de la Funcionalidad / Feature | Volumen de Casos Asociados | Índice de Cobertura | Estado de Cumplimiento |
| :--- | :--- | :---: | :---: | :---: |
| **REQ-COM-01** | Gestión y ciclo de vida de la Evaluación de Negocios (EVN) | 185 casos | 100% | COMPLETADO |
| **REQ-COM-02** | Generación de Cotizaciones y Notas de Venta | 235 casos | 100% | COMPLETADO |
| **REQ-SHD-01** | Mantenedores compartidos y catálogos base del sistema | 370 casos | 100% | COMPLETADO |
| **REQ-USR-01** | Procesos de Autenticación, Gestión de Roles, Clientes y Proveedores | 100 casos | 100% | COMPLETADO |
| **REQ-PRO-01** | Procesos de Creación de Costeos, OP y Órdenes de Trabajo (OT) | 20 casos | 100% | COMPLETADO |
| **REQ-PRO-02** | Registro y Monitoreo de Avance en Planta | 20 casos | 100% | COMPLETADO |

---

## 2. RESUMEN EJECUTIVO DE ASEGURAMIENTO DE CALIDAD (QA REPORT)

### 2.1 Métricas de Rendimiento Global

| Métrica de Control | Valor Registrado |
| :--- | :---: |
| **Total de Casos de Prueba Identificados** | 930 |
| **Total de Casos de Prueba Ejecutados** | 930 |
| **Total de Casos de Prueba Exitosos** | 930 |
| **Total de Casos de Prueba Fallidos** | 0 |
| **Total de Casos de Prueba Omitidos (Skipped)** | 0 |
| **Tasa de Éxito Global** | 100% |
| **Porcentaje de Cobertura Global** | 100% |
| **Dictamen Técnico de Estabilidad** | **Aprobado / Certificado para Despliegue en Producción** |

> **Dictamen Técnico del Auditor de QA:** Se certifica la ejecución satisfactoria de la totalidad de las pruebas unitarias y de integración planificadas. El alcance del proceso cubre exhaustivamente la capa de controladores (REST API) y los servicios de lógica de negocio (Casos de Uso). Las pruebas se aislaron mediante la simulación de componentes externos con Mockito y se validaron contra un entorno persistente basado en base de datos H2 en memoria.

---

## 3. CRITERIOS Y FÓRMULAS DE EVALUACIÓN

### 3.1 Tasa de Éxito
La tasa de éxito determina la proporción de escenarios de prueba que concluyeron de forma satisfactoria respecto al volumen total de elementos ejecutados.
* **Fórmula:** `(Casos Exitosos / Casos Ejecutados) × 100`
* **Evaluación:** `(930 / 930) × 1