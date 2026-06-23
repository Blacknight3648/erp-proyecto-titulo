package backend.com.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Endpoints del módulo produccion: smoke de lectura. Las colecciones y las
 * consultas por NV/OP responden 200 aunque no haya datos (lista vacía), probando
 * el ruteo, la serialización y el stack completo (incluye el OrdenTrabajoController
 * del flujo OT-NV que construimos).
 */
@DisplayName("Endpoints · produccion")
class ProduccionEndpointsTest extends AbstractIntegrationTest {

    @ParameterizedTest(name = "GET {0} → 200")
    @ValueSource(strings = {
            "/api/v1/produccion/ordenes-produccion",
            "/api/v1/hojas-compra",
            "/api/v1/ordenes-compra",
            "/api/v1/ordenes-servicio",
            "/api/v1/produccion/ordenes-trabajo/nota-venta/999999",
            "/api/v1/produccion/ordenes-trabajo/orden-produccion/999999",
            "/api/v1/produccion/ordenes-trabajo/999999/avances",
            "/api/v1/reportes/hcs-pendientes-aprobacion",
            "/api/v1/reportes/ocs-pendientes-recepcion",
            "/api/v1/reportes/oss-en-taller"
    })
    @DisplayName("colecciones y consultas de lectura responden 200")
    void getCollections_returnOk(String url) throws Exception {
        mockMvc.perform(get(url)).andExpect(status().isOk());
    }
}
