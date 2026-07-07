package backend.com.produccion.application.service;

import backend.com.produccion.application.dto.AlertaOPDTO;
import backend.com.produccion.application.dto.DashboardOPKpisResponse;
import backend.com.produccion.application.dto.DashboardOPResponse;
import backend.com.produccion.domain.enums.EstadoOP;
import backend.com.produccion.domain.model.SeguimientoOP;
import backend.com.produccion.infrastructure.persistence.entity.OrdenProduccionJpaEntity;
import backend.com.produccion.infrastructure.persistence.entity.SeguimientoOPJpaEntity;
import backend.com.produccion.infrastructure.persistence.repository.OrdenProduccionJpaRepository;
import backend.com.produccion.infrastructure.persistence.repository.SeguimientoOPJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class DashboardOPService {

    private static final Set<EstadoOP> ESTADOS_ACTIVOS = Set.of(
            EstadoOP.PENDIENTE, EstadoOP.EN_PROCESO, EstadoOP.DETENIDA);

    /** Expone la misma noción de "OP activa" que usa {@link #calcular()}, para que otros
     *  puntos (ej. la alerta por-OP de {@code OrdenProduccionController}) apliquen el mismo criterio. */
    public static boolean esEstadoActivo(EstadoOP estado) {
        return ESTADOS_ACTIVOS.contains(estado);
    }

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

    /**
     * Evalúa TODAS las alertas de atraso vigentes para una única OP, usando las mismas
     * reglas SLA (independientes entre sí, no excluyentes) que {@link #calcular()} aplica
     * de forma agregada — una misma OP puede estar, por ejemplo, "OP Atrasada" y
     * "Recepción de Logo Atrasada" al mismo tiempo, así que se devuelven todas las que
     * apliquen en vez de solo la primera.
     */
    public List<AlertaOPDTO> evaluarAlertas(SeguimientoOP seguimiento) {
        List<AlertaOPDTO> alertas = new ArrayList<>();
        if (seguimiento == null) return alertas;
        LocalDate hoy = LocalDate.now();

        if (seguimiento.getFechaRecepcionOp() != null && seguimiento.getEstadoOcMp() == null) {
            long dias = ChronoUnit.DAYS.between(seguimiento.getFechaRecepcionOp(), hoy);
            if (dias > 3) alertas.add(new AlertaOPDTO("OP", dias, "Sin confirmación de OC de Materia Prima (> 3 días)"));
        }
        if (seguimiento.getInicioCorte() != null && seguimiento.getFinCorte() == null) {
            long dias = ChronoUnit.DAYS.between(seguimiento.getInicioCorte(), hoy);
            if (dias > 10) alertas.add(new AlertaOPDTO("CORTE", dias, "Corte sin finalizar (> 10 días)"));
        }
        if (seguimiento.getInicioLogo() != null && seguimiento.getRegresoLogo() == null) {
            long dias = ChronoUnit.DAYS.between(seguimiento.getInicioLogo(), hoy);
            if (dias > 3) alertas.add(new AlertaOPDTO("LOGO", dias, "Logo sin retorno (> 3 días)"));
        }
        if (seguimiento.getRegresoLogo() != null && seguimiento.getInicioTallerExterno() == null) {
            long dias = ChronoUnit.DAYS.between(seguimiento.getRegresoLogo(), hoy);
            if (dias > 2) alertas.add(new AlertaOPDTO("ENVIO", dias, "Envío a taller sin iniciar (> 2 días)"));
        }
        if (seguimiento.getInicioTallerExterno() != null && seguimiento.getFinTallerExterno() == null) {
            long dias = ChronoUnit.DAYS.between(seguimiento.getInicioTallerExterno(), hoy);
            if (dias > 7) alertas.add(new AlertaOPDTO("TALLER", dias, "En taller externo sin devolución (> 7 días)"));
        }
        return alertas;
    }

    private static final List<String> ETIQUETAS_LOTE = List.of("1-49", "50-99", "100-199", "200-399", "+400");

    /**
     * KPIs de tiempos de ciclo por etapa y distribución de OPs por tamaño de lote
     * (cantidad total de prendas), para los gráficos operacionales del dashboard.
     */
    @Transactional(readOnly = true)
    public DashboardOPKpisResponse calcularKpis() {
        List<OrdenProduccionJpaEntity> ops = opRepo.findAll();
        int numBuckets = ETIQUETAS_LOTE.size();

        EtapaAcumulador corteGlobal = new EtapaAcumulador();
        EtapaAcumulador logoGlobal = new EtapaAcumulador();
        EtapaAcumulador tallerGlobal = new EtapaAcumulador();
        EtapaAcumulador termGlobal = new EtapaAcumulador();

        long[] opsPorBucket = new long[numBuckets];
        EtapaAcumulador[] cortePorBucket = inicializarAcumuladores(numBuckets);
        EtapaAcumulador[] logoPorBucket = inicializarAcumuladores(numBuckets);
        EtapaAcumulador[] tallerPorBucket = inicializarAcumuladores(numBuckets);
        EtapaAcumulador[] termPorBucket = inicializarAcumuladores(numBuckets);
        EtapaAcumulador[] totalPorBucket = inicializarAcumuladores(numBuckets);

        for (OrdenProduccionJpaEntity op : ops) {
            int cantidadTotal = op.getItems().stream()
                    .mapToInt(i -> i.getCantidad() != null ? i.getCantidad() : 0)
                    .sum();
            int bucket = bucketDeLote(cantidadTotal);
            opsPorBucket[bucket]++;

            SeguimientoOPJpaEntity s = op.getSeguimiento();
            if (s == null) continue;

            Long dCorte = diasEntre(s.getInicioCorte(), s.getFinCorte());
            Long dLogo = diasEntre(s.getInicioLogo(), s.getRegresoLogo());
            Long dTaller = diasEntre(s.getInicioTallerExterno(), s.getFinTallerExterno());
            Long dTerm = diasEntre(s.getFinTallerExterno(), s.getFinTerminacion());
            LocalDate inicioCiclo = s.getFinTizado() != null ? s.getFinTizado() : op.getFechaInicio();
            Long dTotal = diasEntre(inicioCiclo, s.getFinTerminacion());

            corteGlobal.add(dCorte);
            logoGlobal.add(dLogo);
            tallerGlobal.add(dTaller);
            termGlobal.add(dTerm);

            cortePorBucket[bucket].add(dCorte);
            logoPorBucket[bucket].add(dLogo);
            tallerPorBucket[bucket].add(dTaller);
            termPorBucket[bucket].add(dTerm);
            totalPorBucket[bucket].add(dTotal);
        }

        List<DashboardOPKpisResponse.EtapaPromedio> tiemposPorEtapa = List.of(
                new DashboardOPKpisResponse.EtapaPromedio("Corte", corteGlobal.promedio()),
                new DashboardOPKpisResponse.EtapaPromedio("Logotipo", logoGlobal.promedio()),
                new DashboardOPKpisResponse.EtapaPromedio("Taller Externo", tallerGlobal.promedio()),
                new DashboardOPKpisResponse.EtapaPromedio("Terminaciones", termGlobal.promedio())
        );

        List<DashboardOPKpisResponse.LotePromedio> promedioPorLote = new ArrayList<>();
        List<DashboardOPKpisResponse.LoteDistribucion> distribucionLote = new ArrayList<>();
        for (int i = 0; i < numBuckets; i++) {
            String rango = ETIQUETAS_LOTE.get(i);
            promedioPorLote.add(new DashboardOPKpisResponse.LotePromedio(
                    rango, opsPorBucket[i],
                    cortePorBucket[i].promedio(), logoPorBucket[i].promedio(),
                    tallerPorBucket[i].promedio(), termPorBucket[i].promedio(),
                    totalPorBucket[i].promedio()));
            distribucionLote.add(new DashboardOPKpisResponse.LoteDistribucion(rango, opsPorBucket[i]));
        }

        return DashboardOPKpisResponse.builder()
                .tiemposPorEtapa(tiemposPorEtapa)
                .promedioPorLote(promedioPorLote)
                .distribucionLote(distribucionLote)
                .build();
    }

    private static EtapaAcumulador[] inicializarAcumuladores(int n) {
        EtapaAcumulador[] arr = new EtapaAcumulador[n];
        for (int i = 0; i < n; i++) arr[i] = new EtapaAcumulador();
        return arr;
    }

    private static Long diasEntre(LocalDate desde, LocalDate hasta) {
        if (desde == null || hasta == null) return null;
        return ChronoUnit.DAYS.between(desde, hasta);
    }

    private static int bucketDeLote(int cantidad) {
        if (cantidad <= 49) return 0;
        if (cantidad <= 99) return 1;
        if (cantidad <= 199) return 2;
        if (cantidad <= 399) return 3;
        return 4;
    }

    private static class EtapaAcumulador {
        private double suma = 0;
        private int n = 0;

        void add(Long dias) {
            if (dias != null) {
                suma += dias;
                n++;
            }
        }

        Double promedio() {
            if (n == 0) return null;
            return Math.round((suma / n) * 10.0) / 10.0;
        }
    }
}
