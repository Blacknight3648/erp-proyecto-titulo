package backend.com.comercial.application.UseCase;

import backend.com.comercial.application.dto.CrearEVNCommand;
import backend.com.comercial.application.dto.EVNResponse;
import backend.com.comercial.application.dto.ItemEVNDTO;
import backend.com.comercial.domain.model.EvaluacionNegocio;
import backend.com.comercial.domain.model.GastoAdicional;
import backend.com.comercial.domain.model.ItemEVN;
import backend.com.comercial.domain.model.TomaTallaje;
import backend.com.comercial.domain.repository.EvaluacionNegocioRepository;
import backend.com.shared.exception.EntityNotFoundException;
import backend.com.shared.valueobjects.Money;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Caso de uso para actualizar una EVN existente (en estado BORRADOR o EVALUACION).
 * Reutiliza el mismo CrearEVNCommand como payload. No modifica el número ni el estado.
 */
@Service
@RequiredArgsConstructor
public class ActualizarEVNUseCase {

    private final EvaluacionNegocioRepository evnRepository;

    @Transactional
    public EVNResponse ejecutar(Long id, CrearEVNCommand command) {
        EvaluacionNegocio existente = evnRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Evaluación de Negocio no encontrada: " + id));

        // Reconstruir la EVN con los datos actualizados pero manteniendo ID, número y estado
        List<ItemEVN> nuevosItems = new ArrayList<>();
        if (command.getItems() != null) {
            command.getItems().forEach(dto -> nuevosItems.add(toItemEVN(dto)));
        }

        List<GastoAdicional> nuevosGastos = buildGastos(command);

        TomaTallaje nuevaTomaTallaje = buildTomaTallaje(command);

        EvaluacionNegocio actualizada = new EvaluacionNegocio(
                existente.getEvaluacionNegocioId(),
                existente.getNumeroEvn(),
                command.getClienteId() != null ? command.getClienteId() : existente.getClienteId(),
                command.getVendedorId() != null ? command.getVendedorId() : existente.getVendedorId(),
                existente.getEstado(),
                existente.getFechaEvaluacion(),
                nuevaTomaTallaje,
                nuevosGastos,
                nuevosItems,
                command.getPorcentajeComision() != null ? command.getPorcentajeComision() : existente.getPorcentajeComision(),
                command.getClienteNombre() != null ? command.getClienteNombre() : existente.getClienteNombre(),
                command.getReferencia() != null ? command.getReferencia() : existente.getReferencia(),
                existente.getVendedorNombre()
        );

        EvaluacionNegocio guardada = evnRepository.save(actualizada);
        return EVNResponse.fromDomain(guardada);
    }

    private ItemEVN toItemEVN(ItemEVNDTO dto) {
        int cantidad = (dto.getCantidad() != null && dto.getCantidad() > 0) ? dto.getCantidad() : 1;
        Money precio = new Money(dto.getPrecioUnitario() != null ? dto.getPrecioUnitario() : BigDecimal.ZERO, "CLP");
        BigDecimal costoBase = dto.getCostoProducto() != null ? dto.getCostoProducto() : BigDecimal.ZERO;
        Money costo = new Money(dto.getCostoUnitario() != null ? dto.getCostoUnitario() : costoBase, "CLP");

        return new ItemEVN(
                dto.getArticuloId(),
                dto.getProveedorId(),
                dto.getNroItem(),
                dto.getDescripcion(),
                dto.getModelo(),
                dto.getTela(),
                dto.getComposicion(),
                dto.getGenero(),
                dto.getCodigoInterno(),
                dto.getCodigoProveedor(),
                dto.getProveedorNombre(),
                cantidad,
                precio,
                costo,
                dto.getCostoProducto() != null ? dto.getCostoProducto() : BigDecimal.ZERO,
                dto.getCostoLogo() != null ? dto.getCostoLogo() : BigDecimal.ZERO,
                dto.getCostoOrdenTrabajo() != null ? dto.getCostoOrdenTrabajo() : BigDecimal.ZERO,
                dto.getTipoItem() != null ? dto.getTipoItem() : "SC",
                dto.getTechnicalSpecs() != null ? dto.getTechnicalSpecs() : Collections.emptyList(),
                dto.getCosteoId(),
                dto.getSolicitudCostosId());
    }

    private List<GastoAdicional> buildGastos(CrearEVNCommand cmd) {
        List<GastoAdicional> gastos = new ArrayList<>();
        addGastoIfPositive(gastos, GastoAdicional.TipoGastoAdicional.FLETE, cmd.getFlete());
        addGastoIfPositive(gastos, GastoAdicional.TipoGastoAdicional.GARANTIA_SERIEDAD, cmd.getGarantiaSeriedad());
        addGastoIfPositive(gastos, GastoAdicional.TipoGastoAdicional.GARANTIA_CUMPLIMIENTO, cmd.getGarantiaFielCumplimiento());
        addGastoIfPositive(gastos, GastoAdicional.TipoGastoAdicional.CERTIFICACION, cmd.getCertificacion());
        addGastoIfPositive(gastos, GastoAdicional.TipoGastoAdicional.MUESTRAS_FISICAS, cmd.getMuestras());
        addGastoIfPositive(gastos, GastoAdicional.TipoGastoAdicional.ENTREGA_PERSONALIZADA, cmd.getEntregaPersonalizada());
        if (cmd.getPegadoCinta() != null && cmd.getPegadoCinta().compareTo(BigDecimal.ZERO) > 0) {
            gastos.add(new GastoAdicional(
                    GastoAdicional.TipoGastoAdicional.PEGADO_CINTA,
                    new Money(cmd.getPegadoCinta(), "CLP"),
                    cmd.getPegadoCintaDetalles()));
        }
        return gastos;
    }

    private TomaTallaje buildTomaTallaje(CrearEVNCommand cmd) {
        boolean hasTomaTallaje = cmd.getTomaTallajeDiasXRecinto() != null
                || cmd.getTomaTallajePersonasXRecinto() != null;
        if (!hasTomaTallaje) return null;
        return new TomaTallaje(
                cmd.getTomaTallajeObservaciones(),
                LocalDate.now(),
                cmd.getTomaTallajeDiasXRecinto(),
                cmd.getTomaTallajePersonasXRecinto(),
                cmd.getTomaTallajeColaccionXPers(),
                cmd.getTomaTallajeAsignacionXPers(),
                cmd.getTomaTallajePeajes(),
                cmd.getTomaTallajeBencinaXLt(),
                cmd.getTomaTallajeKmTotal(),
                cmd.getTomaTallajeRendKmLt(),
                cmd.getTomaTallajeRecintos(),
                cmd.getTomaTallajeDetalles());
    }

    private void addGastoIfPositive(List<GastoAdicional> gastos, GastoAdicional.TipoGastoAdicional tipo, BigDecimal monto) {
        if (monto != null && monto.compareTo(BigDecimal.ZERO) > 0) {
            gastos.add(new GastoAdicional(tipo, new Money(monto, "CLP")));
        }
    }
}
