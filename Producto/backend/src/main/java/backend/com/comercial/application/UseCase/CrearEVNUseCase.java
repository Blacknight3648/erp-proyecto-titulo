package backend.com.comercial.application.UseCase;

import backend.com.comercial.application.dto.CrearEVNCommand;
import backend.com.comercial.application.dto.EVNResponse;
import backend.com.comercial.application.dto.ItemEVNDTO;
import backend.com.comercial.domain.model.EvaluacionNegocio;
import backend.com.comercial.domain.model.GastoAdicional;
import backend.com.comercial.domain.model.ItemEVN;
import backend.com.comercial.domain.model.TomaTallaje;
import backend.com.comercial.domain.repository.EvaluacionNegocioRepository;
import backend.com.shared.application.service.NumeroDocumentoService;
import backend.com.shared.valueobjects.DocumentNumber;
import backend.com.shared.valueobjects.Money;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CrearEVNUseCase {

    private final EvaluacionNegocioRepository evnRepository;
    private final NumeroDocumentoService numeroDocumentoService;

    @Transactional
    public EVNResponse ejecutar(CrearEVNCommand command) {
        DocumentNumber numero = numeroDocumentoService.siguienteFormateado("EVN");

        EvaluacionNegocio evn = EvaluacionNegocio.crear(
                numero,
                command.getClienteId(),
                command.getVendedorId(),
                command.getPorcentajeComision(),
                command.getClienteNombre(),
                command.getReferencia(),
                null);

        if (command.getItems() != null) {
            command.getItems().forEach(dto -> evn.addItem(toItemEVN(dto)));
        }

        // Gastos adicionales
        addGastoIfPositive(evn, GastoAdicional.TipoGastoAdicional.FLETE, command.getFlete());
        addGastoIfPositive(evn, GastoAdicional.TipoGastoAdicional.GARANTIA_SERIEDAD, command.getGarantiaSeriedad());
        addGastoIfPositive(evn, GastoAdicional.TipoGastoAdicional.GARANTIA_CUMPLIMIENTO, command.getGarantiaFielCumplimiento());
        addGastoIfPositive(evn, GastoAdicional.TipoGastoAdicional.CERTIFICACION, command.getCertificacion());
        addGastoIfPositive(evn, GastoAdicional.TipoGastoAdicional.MUESTRAS_FISICAS, command.getMuestras());
        addGastoIfPositive(evn, GastoAdicional.TipoGastoAdicional.ENTREGA_PERSONALIZADA, command.getEntregaPersonalizada());

        if (command.getPegadoCinta() != null && command.getPegadoCinta().compareTo(BigDecimal.ZERO) > 0) {
            evn.addGastoAdicional(new GastoAdicional(
                    GastoAdicional.TipoGastoAdicional.PEGADO_CINTA,
                    new Money(command.getPegadoCinta(), "CLP"),
                    command.getPegadoCintaDetalles()));
        }

        // Toma de tallaje
        boolean hasTomaTallaje = command.getTomaTallajeDiasXRecinto() != null
                || command.getTomaTallajePersonasXRecinto() != null;
        if (hasTomaTallaje) {
            evn.setTomaTallaje(new TomaTallaje(
                    command.getTomaTallajeObservaciones(),
                    LocalDate.now(),
                    command.getTomaTallajeDiasXRecinto(),
                    command.getTomaTallajePersonasXRecinto(),
                    command.getTomaTallajeColaccionXPers(),
                    command.getTomaTallajeAsignacionXPers(),
                    command.getTomaTallajePeajes(),
                    command.getTomaTallajeBencinaXLt(),
                    command.getTomaTallajeKmTotal(),
                    command.getTomaTallajeRendKmLt(),
                    command.getTomaTallajeRecintos(),
                    command.getTomaTallajeDetalles()));
        }

        EvaluacionNegocio guardada = evnRepository.save(evn);
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

    private void addGastoIfPositive(EvaluacionNegocio evn, GastoAdicional.TipoGastoAdicional tipo, BigDecimal monto) {
        if (monto != null && monto.compareTo(BigDecimal.ZERO) > 0) {
            evn.addGastoAdicional(new GastoAdicional(tipo, new Money(monto, "CLP")));
        }
    }
}
