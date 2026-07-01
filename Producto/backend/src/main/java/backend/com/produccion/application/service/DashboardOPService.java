package backend.com.produccion.application.service;

import backend.com.produccion.application.dto.DashboardOPResponse;
import backend.com.produccion.domain.enums.EstadoOP;
import backend.com.produccion.infrastructure.persistence.entity.OrdenProduccionJpaEntity;
import backend.com.produccion.infrastructure.persistence.entity.SeguimientoOPJpaEntity;
import backend.com.produccion.infrastructure.persistence.repository.OrdenProduccionJpaRepository;
import backend.com.produccion.infrastructure.persistence.repository.SeguimientoOPJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class DashboardOPService {

    private static final Set<EstadoOP> ESTADOS_ACTIVOS = Set.of(
            EstadoOP.PENDIENTE, EstadoOP.EN_PROCESO, EstadoOP.DETENIDA);

    private final OrdenProduccionJpaRepository opRepo;
    private final SeguimientoOPJpaRepository seguimientoRepo;

    public DashboardOPResponse calcular() {
        List<OrdenProduccionJpaEntity> ops = opRepo.findAll();
        LocalDate hoy = LocalDate.now();

        long opAtrasada = 0;
        long corteAtrasado = 0;
        long recepcionLogoAtrasado = 0;
        long envioAtrasado = 0;
        long devolucionTallerAtrasada = 0;
        long entregas7d = 0;

        for (OrdenProduccionJpaEntity op : ops) {
            if (!ESTADOS_ACTIVOS.contains(op.getEstado())) continue;

            // Entregas próximas 7 días
            if (op.getFechaEntregaProgramada() != null) {
                long diasEntrega = ChronoUnit.DAYS.between(hoy, op.getFechaEntregaProgramada());
                if (diasEntrega >= 0 && diasEntrega <= 7) entregas7d++;
            }

            Optional<SeguimientoOPJpaEntity> segOpt = seguimientoRepo.findByOrdenProduccion_IdOP(op.getIdOP());
            if (segOpt.isEmpty()) continue;
            SeguimientoOPJpaEntity s = segOpt.get();

            // OP Atrasada: recibida hace > 3 días sin confirmación de OC de MP
            if (s.getFechaRecepcionOp() != null && s.getEstadoOcMp() == null) {
                if (ChronoUnit.DAYS.between(s.getFechaRecepcionOp(), hoy) > 3) opAtrasada++;
            }

            // Corte Atrasado: corte iniciado hace > 10 días sin terminar
            if (s.getInicioCorte() != null && s.getFinCorte() == null) {
                if (ChronoUnit.DAYS.between(s.getInicioCorte(), hoy) > 10) corteAtrasado++;
            }

            // Recepción Logo Atrasado: logo enviado hace > 3 días sin retorno
            if (s.getInicioLogo() != null && s.getRegresoLogo() == null) {
                if (ChronoUnit.DAYS.between(s.getInicioLogo(), hoy) > 3) recepcionLogoAtrasado++;
            }

            // Envío Atrasado: logo regresó hace > 2 días y no inició taller
            if (s.getRegresoLogo() != null && s.getInicioTallerExterno() == null) {
                if (ChronoUnit.DAYS.between(s.getRegresoLogo(), hoy) > 2) envioAtrasado++;
            }

            // Devolución Taller Atrasada: en taller hace > 7 días sin devolución
            if (s.getInicioTallerExterno() != null && s.getFinTallerExterno() == null) {
                if (ChronoUnit.DAYS.between(s.getInicioTallerExterno(), hoy) > 7) devolucionTallerAtrasada++;
            }
        }

        return DashboardOPResponse.builder()
                .opAtrasada(opAtrasada)
                .corteAtrasado(corteAtrasado)
                .recepcionLogoAtrasado(recepcionLogoAtrasado)
                .envioAtrasado(envioAtrasado)
                .devolucionTallerAtrasada(devolucionTallerAtrasada)
                .entregas7d(entregas7d)
                .build();
    }
}
