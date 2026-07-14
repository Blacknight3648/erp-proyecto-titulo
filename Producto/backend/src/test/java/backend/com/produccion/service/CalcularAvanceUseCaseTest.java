package backend.com.produccion.service;

import backend.com.produccion.application.UseCase.CalcularAvanceUseCase;
import backend.com.produccion.application.dto.AvanceOPResponse;
import backend.com.produccion.domain.model.SeguimientoOP;
import backend.com.produccion.domain.repository.SeguimientoOPRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("CalcularAvanceUseCase")
class CalcularAvanceUseCaseTest {

    @Mock
    private SeguimientoOPRepository seguimientoOPRepository;

    @InjectMocks
    private CalcularAvanceUseCase useCase;

    @Test
    @DisplayName("sin seguimiento asociado retorna ceros y semáforo ROJO")
    void calcular_sinSeguimiento_retornaCerosYSemaforoRojo() {
        when(seguimientoOPRepository.findByOrdenProduccionId(10L)).thenReturn(Optional.empty());

        AvanceOPResponse resp = useCase.calcular(10L);

        assertThat(resp.getOrdenProduccionId()).isEqualTo(10L);
        assertThat(resp.getPorcentajeGlobal()).isEqualByComparingTo(BigDecimal.ZERO);
        assertThat(resp.getSemaforo()).isEqualTo("ROJO");
        verify(seguimientoOPRepository).findByOrdenProduccionId(10L);
    }

    @Test
    @DisplayName("avance completo (100%) retorna semáforo VERDE")
    void calcular_conAvanceCompleto_retornaSemaforoVerde() {
        SeguimientoOP seg = new SeguimientoOP(10L);
        seg.setFechaRecepcionOp(LocalDate.now());
        seg.setFinTizado(LocalDate.now());
        seg.setEstadoOcMp(backend.com.produccion.domain.enums.EstadoOcMp.OC_EMITIDA);
        seg.setRecepcionCompras(LocalDate.now());
        seg.setInicioCorte(LocalDate.now());
        seg.setFinCorte(LocalDate.now());
        seg.setInicioLogo(LocalDate.now());
        // set 15 fields
        seg.setEstadoIdaLogo(backend.com.produccion.domain.enums.EstadoIdaLogo.IDA_COMPLETA);
        seg.setRegresoLogo(LocalDate.now());
        seg.setEstadoRecLogo(backend.com.produccion.domain.enums.EstadoRecLogo.RECEPCION_COMPLETA);
        seg.setInicioTallerExterno(LocalDate.now());
        seg.setFinTallerExterno(LocalDate.now());
        seg.setCalidadTaller(backend.com.produccion.domain.enums.CalidadTaller.APROBADO);
        seg.setFinTerminacion(LocalDate.now());
        seg.setFinPersonalizado(LocalDate.now());

        when(seguimientoOPRepository.findByOrdenProduccionId(10L)).thenReturn(Optional.of(seg));

        AvanceOPResponse resp = useCase.calcular(10L);

        assertThat(resp.getPorcentajeGlobal()).isEqualByComparingTo(new BigDecimal("100.00"));
        assertThat(resp.getSemaforo()).isEqualTo("VERDE");
    }
}
