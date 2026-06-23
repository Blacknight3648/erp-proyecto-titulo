Endpoint: {{baseUrl}}/api/v1/produccion/costeos/disponibles-evn?monto=100



Searched for "disponibles-evn"
Viewed CosteoController.java:1-58
Searched for "El monto no puede ser null"
Viewed Money.java:1-60
Viewed CosteoJpaEntity.java:1-73
Viewed data.sql:1-384
Searched for "CosteoMapper"
Viewed CosteoMapper.java:1-196

El error que estás viendo **no se debe al parámetro `?monto=100`** que enviaste en la URL (de hecho, este endpoint no recibe parámetros por URL y simplemente los ignora). El error proviene de la base de datos y la forma en que se están mapeando los datos hacia los objetos de dominio.

Aquí tienes la documentación detallada del error y su causa:

### 📄 Origen del Error
El error se dispara específicamente en la clase `Money` (ubicada en `backend/com/shared/valueobjects/Money.java` en la línea 20):
```java
public Money(@JsonProperty("amount") BigDecimal amount, @JsonProperty("currency") String currency) {
    if (amount == null) {
        throw new IllegalArgumentException("El monto no puede ser null");
    }
    // ...
}
```
Como se trata de un `IllegalArgumentException` arrojado desde la lógica de dominio (Value Object), tu manejador global de excepciones (Global Exception Handler) lo captura y lo transforma correctamente en un `400 Bad Request`.

### 🔍 ¿A qué se debe exactamente?
1. **Datos en la Base de Datos con valores `NULL`**: 
   En tu archivo `data.sql` (línea 301) estás insertando un registro en la tabla `produccion_costeos` así:
   ```sql
   MERGE INTO produccion_costeos (id_costeo, solicitud_costos_id, numero_costeo)
       KEY (id_costeo)
       VALUES
       (1, 2, 'COST-2024-001');
   ```
   Al hacer esto, todos los demás campos monetarios (`costo_hilos`, `costo_mano_obra`, `costo_total_materia_prima`, etc.) quedan guardados en la base de datos como `NULL`.

2. **Falla en el Mapeo (`CosteoMapper.java`)**: 
   Cuando llamas al endpoint `/disponibles-evn`, este busca los costeos en la base de datos y usa el `CosteoMapper.toDomain(CosteoJpaEntity entity)` para convertir la entidad JPA al modelo de dominio. En la línea 36 del Mapper tienes:
   ```java
   new Money(entity.getCostoHilos(), "CLP"),
   new Money(entity.getCostoManoObra(), "CLP"),
   // ... otros campos
   ```
   Como `entity.getCostoHilos()` viene como `null` desde la base de datos, el mapper intenta ejecutar `new Money(null, "CLP")`, lo cual activa de inmediato la validación de la clase `Money` lanzando la excepción `"El monto no puede ser null"`.

---

### 💡 ¿Cómo solucionarlo?

Tienes dos alternativas para corregir esto:

**Alternativa 1: Proteger el Mapper (Recomendada)**
Modificar el método `toDomain()` en `CosteoMapper.java` para que asigne un `BigDecimal.ZERO` si el campo viene `null` desde la BD (igual a como ya lo estás haciendo inteligentemente en el método `toDomainFromDto`):
```java
new Money(entity.getCostoHilos() != null ? entity.getCostoHilos() : BigDecimal.ZERO, "CLP"),
// Aplicar lo mismo a todos los demás campos monetarios...
```

**Alternativa 2: Llenar la data de prueba en `data.sql`**
Completar el `INSERT/MERGE` en tu base de datos para asegurarte de que todos los campos de costo inicien con `0.00` u otro valor válido en lugar de quedar en nulo:
```sql
MERGE INTO produccion_costeos (id_costeo, solicitud_costos_id, numero_costeo, costo_hilos, costo_mano_obra, ...)
    VALUES (1, 2, 'COST-2024-001', 0.00, 0.00, ...);
```

## 🔴 Error Actual (400 Bad Request)
El error exacto es: `IllegalArgumentException: El monto no puede ser null`.

### 🔍 **¿A qué se debe exactamente?**
1. **Datos en la Base de Datos con valores `NULL`**: 
   En tu archivo `data.sql` (línea 301) estás insertando un registro en la tabla `produccion_costeos` así:
   ```sql
   MERGE INTO produccion_costeos (id_costeo, solicitud_costos_id, numero_costeo)
       KEY (id_costeo)
       VALUES
       (1, 2, 'COST-2024-001');
   ```
   Al hacer esto, todos los demás campos monetarios (`costo_hilos`, `costo_mano_obra`, `costo_total_materia_prima`, etc.) quedan guardados en la base de datos como `NULL`.

2. **Falla en el Mapeo (`CosteoMapper.java`)**: 
   Cuando llamas al endpoint `/disponibles-evn`, este busca los costeos en la base de datos y usa el `CosteoMapper.toDomain(CosteoJpaEntity entity)` para convertir la entidad JPA al modelo de dominio. En la línea 36 del Mapper tienes:
   ```java
   new Money(entity.getCostoHilos(), "CLP"),
   new Money(entity.getCostoManoObra(), "CLP"),
   // ... otros campos
   ```
   Como `entity.getCostoHilos()` viene como `null` desde la base de datos, el mapper intenta ejecutar `new Money(null, "CLP")`, lo cual activa de inmediato la validación de la clase `Money` lanzando la excepción `"El monto no puede ser null"`.


