# Mapa de Agregados del Dominio

Este documento es la **fuente única de verdad** sobre qué entidades son agregados raíz, qué viven adentro de cada uno, y cómo se relacionan entre sí. Antes de modificar un modelo de dominio o un mapper JPA, consultá este archivo. Si la realidad del código diverge de este documento, actualizá el documento en el mismo PR.

## Reglas que aplican a todo el dominio

1. **Un agregado se referencia a otro solo por ID** (`Long xxxId`), nunca por objeto. Esto desacopla agregados y permite cargarlos independientemente.
2. **Dentro de un agregado, las entidades hijas pueden referenciarse por objeto**. Su ciclo de vida está atado al raíz (se crean, persisten y eliminan con él).
3. **Los Value Objects son inmutables y no tienen ID propio**. Son intercambiables si tienen los mismos valores.
4. **El estado del agregado solo cambia a través de métodos de comportamiento del raíz** (`aprobar()`, `recepcionar()`, etc.), nunca mediante setters genéricos.
5. **`@OneToMany` vs `@ElementCollection`** en JPA debe reflejar este criterio: entidades hijas → `@OneToMany`, value objects en lista → `@ElementCollection`.

---

## Módulo `comercial`

### Agregado: `SolicitudCotizacion` (SCOT) — **FUERA DE ALCANCE ACTUAL**
- El modelo `SolicitudCotizacion` existe en código pero **no está integrado al flujo de negocio en esta fase**. No se debe tratar como agregado activo todavía.
- Cuando se incorpore, completar esta sección y actualizar el "Mapa de flujo" más abajo.

### Agregado: `SolicitudCostos` (SCOS)
- **Raíz:** `SolicitudCostos` — id: `idSCOS`
- **Referencias por ID:** `clienteId`, `vendedorId`, `especificacionTecnicaId`
- **Entidades hijas internas:** `SCOSPlantilla` (con sus `PlantillaTela`, `PlantillaAccesorio`, `SCOSLogotipo`, `SCOSPlantillaMaterialVinculo`), `SCOSCostoFijo`, `SCOSTela`, `SCOSAccesorio`
- **Estados:** `estado` (string libre — *deuda técnica: debería ser enum*)
- **Notas:** la frontera entre `SCOS*` y `Plantilla*` no está clara — ver "Decisiones pendientes" al final.
- **Flujo:** punto de entrada actual del proceso comercial. El área de costos define materiales y mano de obra; alimenta el Costeo, que a su vez alimenta la EvaluacionNegocio.

### Agregado: `EvaluacionNegocio` (EVN)
- **Raíz:** `EvaluacionNegocio` — id: `evaluacionNegocioId`
- **Referencias por ID:** `clienteId`, `vendedorId`, `costeoId`, `solicitudCotizacionId`
- **Entidades hijas internas:** `ItemEVN` (lista), `GastoAdicional` (lista), `TomaTallaje` (1:1 opcional)
- **Value Objects:** `DocumentNumber numeroEvn`, `Money` (en items y gastos)
- **Estados:** `EstadoEVN` → `BORRADOR`, `EVALUACION`, `APROBADA`, `RECHAZADA`, `ADJUDICADA`, `CANCELADA`, `CERRADA`
- **Métodos de comportamiento:** `aprobar()`, `rechazar()`, `adjudicar()`, `cerrar()`, `addItem()`, `addGastoAdicional()`, `setTomaTallaje()`
- **Cálculos derivados:** `getMontoTotal()`, `getCostoTotal()`, `getMontoComision()`, `getMargenGanancia()`, `getRentabilidadEsperada()`
- **Datos desnormalizados (snapshots):** `clienteNombre`, `vendedorNombre`, `referencia` — congelados al momento de creación.
- **Flujo:** evalúa rentabilidad del negocio antes de emitir Nota de Venta. Una EVN **ADJUDICADA** habilita la generación de NV (se usa como plantilla).
- **Regla de negocio — cierre:** `cerrar()` solo es válido desde `ADJUDICADA` → `CERRADA`. Una EVN `CERRADA` es terminal: deja de poder usarse como plantilla y **bloquea la creación de nuevas Notas de Venta**. El cierre se expone vía `PATCH /api/v1/comercial/evaluaciones-negocio/{id}/cerrar` (firma + historial) y lo aplica `CerrarEVNUseCase`.

### Agregado: `NotaVenta` (NV)
- **Raíz:** `NotaVenta` — id: `idNV`
- **Referencias por ID:** `evaluacionNegocioId`, `clienteId`, `vendedorId`
- **Entidades hijas internas:** `ItemNV` (con sus `ItemNVTalla`)
- **Value Objects:** `DocumentNumber numeroNV`, `Money montoSubtotal/montoIva/montoTotal`
- **Estados:** `EstadoNV` → `BORRADOR`, `APROBADA`, `COMPLETADA`, `ENTREGADA`, `CANCELADA`
- **Métodos de comportamiento:** `crear()` (factory), `addItem()`, `calcularTotales()`, `aprobar()`, `cancelar()`
- **Eventos de dominio:** sí (campo `domainEvents`, infraestructura lista)
- **Flujo:** se emite desde una EVN **ADJUDICADA**; dispara la creación de OP en producción. La NV **siempre requiere** un `evaluacionNegocioId` (el dominio lo exige). `CrearNVUseCase` valida el gate: la EVN referenciada debe estar `ADJUDICADA`; una EVN `CERRADA` u otro estado se rechaza con `EVNBusinessException` → HTTP 422.

---

## Módulo `produccion`

### Agregado: `Costeo`
- **Raíz:** `Costeo` — id: `idCosteo`
- **Referencias por ID:** `solicitudCostosId`, `clienteId`, `vendedorId`
- **Entidades hijas internas:** `CosteoItem` (lista)
- **Value Objects:** `DocumentNumber`, `Money` (costoHilos, costoManoObra, costoEtiquetas, costoEmbalaje, costoFlete, costoTotalMateriaPrima, precioVentaSugerido, precios de cintas, vivoReflectivo)
- **Datos desnormalizados:** `clienteNombre`, `vendedorNombre`
- **Notas:** este agregado vive en `produccion` pero se consume desde `comercial` (EVN lo referencia). *Decisión a revisar: ¿pertenece a comercial o a producción?*

### Agregado: `OrdenProduccion` (OP)
- **Raíz:** `OrdenProduccion` — id: `idOP`
- **Referencias por ID:** `notaVentaId`
- **Entidades hijas internas:** `OrdenProduccionItem` (lista)
- **Value Objects:** `DocumentNumber numeroOP`
- **Estados:** `EstadoOP` → `PENDIENTE`, `EN_PROCESO`, ...
- **Métodos de comportamiento:** `crearNueva()` (factory), `recepcionar()`, `addItem()`
- **Flujo:** se crea automáticamente al aprobar una NV. Agrupa los items a fabricar.

### Agregado: `OrdenTrabajo` (OT)
- **Raíz:** `OrdenTrabajo` — id: `idOT`
- **Referencias por ID:** `notaVentaId`, `ordenProduccionId`
- **Entidades hijas internas:** ninguna (raíz hoja). Los avances se modelan como agregado aparte.
- **Value Objects:** `DocumentNumber numeroOT`
- **Estados:** `EstadoOT` → `PENDIENTE`, `EN_PROCESO`, `FINALIZADA`
- **Enums asociados:** `TipoOT` (INTERNA, ...), `FaseProduccion`
- **Métodos de comportamiento:** `crearParaItem()`, `crearParaFase()` (factories), `iniciar()`, `finalizar()`, `registrarAvance()`
- **Cálculos derivados:** `getPorcentajeAvance()`
- **Flujo:** una OP genera múltiples OTs (una por fase/item). El avance de las OTs alimenta el progreso de la OP.

### Agregado: `RegistroAvance`
- **Raíz:** `RegistroAvance`
- **Referencias por ID:** `ordenTrabajoId` (probable — confirmar)
- **Flujo:** bitácora de avances reportados sobre una OT.

---

## Mapa de flujo (de extremo a extremo)

```
SolicitudCostos (SCOS) ──► Plantillas, Telas, Accesorios, Logotipos
        ↓ produce
Costeo  ◄────────────────── (calcula precios y costos)
        ↓ alimenta
EvaluacionNegocio (EVN) ──► TomaTallaje, GastosAdicionales, ItemEVN
        ↓ adjudicada habilita
NotaVenta (NV) ──► ItemNV ──► ItemNVTalla
        ↓ aprobada genera
OrdenProduccion (OP) ──► OrdenProduccionItem
        ↓ se desglosa en
OrdenTrabajo (OT) [una por fase/item]
        ↓ recibe avances en
RegistroAvance
```

---

## Decisiones pendientes (deuda y ambigüedades detectadas)

Estos puntos se identificaron al construir este mapa. **Resolverlos es prerrequisito para refactorizar los mappers JPA con confianza.**

1. **Frontera `SCOS` vs `Plantilla`:** en `infrastructure/persistence/entity` coexisten `PlantillaTela.java` (value object embebido vía `@ElementCollection`) y `SCOSTelaJpaEntity.java` (entidad propia). El dominio tiene ambos. ¿Son lo mismo? ¿Cuándo se usa cada uno?
2. **Estados como `String` libre:** `SolicitudCostos.estado` es `String`. Debe convertirse a enum `EstadoSCOS` para que las transiciones sean validables. (`SolicitudCotizacion` queda fuera de alcance por ahora.)
3. **¿Dónde vive `Costeo`?** Está en `produccion` pero conceptualmente es parte del flujo comercial (lo consume EVN). Mover a `comercial` o crear un módulo `costeo` propio.
4. **Datos desnormalizados (`clienteNombre`, `vendedorNombre`):** aparecen en EVN, SCOS, Costeo. Documentar la política: ¿se congelan al crear? ¿se sincronizan? ¿son solo lectura para UI?
5. **`EvaluacionNegocioJpaEntity` y `EvaluacionNegocioItemJpaEntity`:** verificar que la relación esté como `@OneToMany` con `cascade = ALL` + `orphanRemoval = true` para que los items sigan el ciclo de vida del raíz.
6. **Eventos de dominio:** `NotaVenta` y `EvaluacionNegocio` declaran `domainEvents` pero no se ve un dispatcher configurado. Decidir si se activan o se eliminan.

---

## Cómo usar este documento

- **Al crear un nuevo modelo:** ubicar a qué agregado pertenece. Si es raíz nuevo, agregarlo aquí antes de escribir código.
- **Al modificar un mapper JPA:** confirmar que la relación que estás escribiendo coincide con la jerarquía documentada (raíz vs hijo, ID vs objeto).
- **Al revisar un PR:** si toca el dominio y no actualiza este archivo cuando corresponde, pedir el cambio.
