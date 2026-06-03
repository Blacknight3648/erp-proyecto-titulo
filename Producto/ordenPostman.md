# Orden Lógico de Ejecución en Postman

Para que las pruebas en Postman no arrojen errores de regla de negocio (`422 Unprocessable Entity`) o de entidad no encontrada (`404 Not Found`), es muy importante seguir el flujo natural del ERP. Las entidades tienen una máquina de estados estricta y dependencias entre sí (por ejemplo, no puedes crear una Orden de Producción sin una Nota de Venta aprobada).

A continuación, te detallo el orden exacto en el que debes ejecutar las peticiones en tu colección de Postman:

## 1. Flujo Comercial (Ventas)
El proceso siempre comienza con la venta.

1. **Crear Evaluación de Negocio (EVN)**: Nace en estado `EVALUACION`.
2. **Aprobar EVN**: Cambia el estado de `EVALUACION` a `APROBADA`.
3. **Adjudicar EVN**: Cambia el estado de `APROBADA` a `ADJUDICADA`.
4. **Crear Nota de Venta (NV)**: Se genera a partir de la EVN adjudicada. Nace en estado `BORRADOR`.
5. **Aprobar NV**: Pasa la NV de `BORRADOR` a `APROBADA`.
   *⚠️ Importante: Si ejecutas la petición "Aprobar NV" por segunda vez (ej. el request duplicado "aprobar 1"), te arrojará error 422, ya que no puedes aprobar algo que ya está aprobado.*

## 2. Flujo de Producción y Servicios
Una vez que la Nota de Venta está aprobada, pasa a producción.

6. **Crear Orden de Producción (OP)**: Se asocia a la NV. Nace en estado `PENDIENTE`.
7. **Recepcionar OP**: Pasa de `PENDIENTE` a `EN_PROCESO`.
8. **Crear Orden de Servicio (OS)**: Se asocia a la OP y a un proveedor. Nace como `EMITIDA`.
9. **Registrar Despacho en OS**: Pasa la OS a estado `EN_PROCESO` (entregas el material al proveedor).
10. **Registrar Recepción en OS**: Pasa la OS a estado `RECEPCIONADA` (el proveedor te devuelve el trabajo listo). *Debes despachar antes de poder recepcionar.*
11. **Cerrar OS**: Cambia de `RECEPCIONADA` a `CERRADA`.

## 3. Flujo de Adquisiciones (Compras)
Las materias primas se gestionan mediante compras vinculadas a la OP.

12. **Crear Hoja de Compra (HC)**: Se asocia a la OP. Nace en estado `BORRADOR`.
13. **Aprobar HC**: Pasa a estado `APROBADA`.
14. **Consolidar/Crear Orden de Compra (OC)**: Nace en estado `EMITIDA`.
15. **Marcar OC como Enviada**: Pasa a estado `ENVIADA`.
16. **Marcar OC como Recepcionada**: Pasa a estado `RECEPCIONADA`.
17. **Cerrar OC**: Pasa a estado `CERRADA`.
18. **Cerrar HC**: Una vez que llegan los insumos, la Hoja de Compra puede marcarse como `CERRADA`.

---

### Consejos Clave para Postman:
1. **Evita ejecutar los request duplicados**: Noté que en tu colección de Postman tienes requests como `cerrar 1` o `aprobar 1`. Esos requests fallan con HTTP 422 porque intentan aplicar una transición de estado a un documento que ya fue procesado por el request original. Si tu intención no es testear el rechazo (idempotencia), deshabilita esos requests duplicados.
2. **Uso de Variables de Entorno**: Asegúrate de que las peticiones POST (Crear EVN, Crear NV, etc.) tengan un pequeño script en la pestaña `Tests` de Postman que guarde el ID generado en las variables de entorno, por ejemplo:
   ```javascript
   if (pm.response.code === 200 || pm.response.code === 201) {
       var jsonData = pm.response.json();
       pm.environment.set("id", jsonData.id || jsonData.idOS || jsonData.idOC || jsonData.idHC);
   }
   ```
   Esto asegura que las peticiones subsecuentes (Aprobar, Cerrar, etc.) ataquen al ID correcto y no a un ID estático que podría devolver un `404 Not Found`.
