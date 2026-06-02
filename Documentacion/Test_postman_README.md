# Datos dinámicos para Postman Collection Runner

## Por qué los tests fallaban con 409

El archivo `Test_postman.json` es un **dataset estático** (no una colección). Postman **no interpreta placeholders dentro del dataset** por sí mismo. Por eso, al correr el dataset dos veces, los mismos RUTs/emails se intentaban insertar y la BD respondía con HTTP 409 (Conflict).

## Solución: pre-request script a nivel de colección

Los placeholders del dataset (`{{uniqueSuffix}}`, `{{rutUsuario1}}`, etc.) **se reemplazan en runtime** por valores que genera un script JS que tú pegas en tu colección Postman.

## Cómo configurarlo

### Paso 1 — Abrir tu Collection en Postman

1. En el panel izquierdo, click derecho en tu **Collection** (no en un request individual).
2. Selecciona **Edit**.
3. Ve a la pestaña **Pre-request Script** (a nivel de Collection, NO de Folder ni de Request).

### Paso 2 — Pegar el siguiente script

```javascript
// =====================================================================
// PRE-REQUEST SCRIPT GLOBAL — genera datos dinámicos por iteración
// =====================================================================
// Este script se ejecuta ANTES de cada request de la colección.
// Genera RUTs únicos y un sufijo aleatorio que se inyectan en variables
// disponibles para los requests y el dataset.

// Sufijo único basado en timestamp + random (asegura unicidad incluso
// si se corre múltiples veces en el mismo segundo)
const uniqueSuffix = Date.now().toString().slice(-6) + Math.floor(Math.random() * 1000);
pm.variables.set('uniqueSuffix', uniqueSuffix);

// =====================================================================
// Generador de RUT chileno válido con dígito verificador
// =====================================================================
function calcularDV(rutSinDV) {
    let suma = 0;
    let multiplicador = 2;
    for (let i = rutSinDV.length - 1; i >= 0; i--) {
        suma += parseInt(rutSinDV.charAt(i), 10) * multiplicador;
        multiplicador = multiplicador === 7 ? 2 : multiplicador + 1;
    }
    const resto = 11 - (suma % 11);
    if (resto === 11) return '0';
    if (resto === 10) return 'K';
    return String(resto);
}

function generarRutAleatorio() {
    // Genera un cuerpo entre 10.000.000 y 25.000.000 (rangos realistas)
    const cuerpo = String(10000000 + Math.floor(Math.random() * 15000000));
    const dv = calcularDV(cuerpo);
    return `${cuerpo}-${dv}`;
}

// Pre-genera RUTs únicos para cada entidad de cada iteración
pm.variables.set('rutUsuario1',   generarRutAleatorio());
pm.variables.set('rutUsuario2',   generarRutAleatorio());
pm.variables.set('rutVendedor1',  generarRutAleatorio());
pm.variables.set('rutVendedor2',  generarRutAleatorio());
pm.variables.set('rutProveedor1', generarRutAleatorio());
pm.variables.set('rutProveedor2', generarRutAleatorio());
pm.variables.set('rutCliente1',   generarRutAleatorio());
pm.variables.set('rutCliente2',   generarRutAleatorio());
```

### Paso 3 — Guardar

Click en **Save** o `Ctrl+S`.

## Cómo funciona en runtime

1. Antes de cada request, Postman ejecuta el script y guarda las variables (`uniqueSuffix`, `rutUsuario1`, etc.) en el scope de **variables locales**.
2. Cuando Postman lee el dataset, encuentra `{{rutUsuario1}}` y lo reemplaza por el valor que está en las variables.
3. Si en el body de tu request usas `{{usuario_rut}}` (que viene del dataset y trae `{{rutUsuario1}}` literalmente), Postman lo resuelve en **dos saltos** hasta el valor real.

## Verificación rápida

Después de configurarlo, abre la consola de Postman (`View → Show Postman Console`) y ejecuta una request. Deberías ver algo como:

```
POST /usuarios
Body: {
  "usuarioRut": "23456789-K",
  "usuarioEmail": "juan.perez+123456789@empresa.cl",
  ...
}
```

Si ves `{{rutUsuario1}}` literal en el body → el script no se está ejecutando. Revisa que esté pegado a nivel de Collection y guardado.

## Variables disponibles

| Variable | Para qué |
|----------|----------|
| `uniqueSuffix` | Sufijo único de 6-9 dígitos. Se usa en emails, códigos, descripciones |
| `rutUsuario1`, `rutUsuario2` | RUTs chilenos válidos para los 2 usuarios del dataset |
| `rutVendedor1`, `rutVendedor2` | Idem vendedores |
| `rutProveedor1`, `rutProveedor2` | Idem proveedores |
| `rutCliente1`, `rutCliente2` | Idem clientes |

## Alternativa: limpiar BD entre corridas

Si prefieres no usar dinámicos:

1. Detén el backend.
2. Vuelve a arrancar (H2 es in-memory, se borra al reiniciar).
3. Corre los tests.

Funciona pero es manual y lento.
