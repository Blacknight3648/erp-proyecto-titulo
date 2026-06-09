package backend.com.comercial.application.UseCase;

import backend.com.comercial.application.dto.CrearEVNCommand;
import backend.com.comercial.application.dto.EVNResponse;
import backend.com.comercial.application.dto.ItemEVNDTO;
import backend.com.comercial.domain.model.EvaluacionNegocio;
import backend.com.comercial.domain.model.ItemEVN;
import backend.com.comercial.domain.model.GastoAdicional;
import backend.com.comercial.domain.model.TomaTallaje;
import backend.com.comercial.domain.repository.EvaluacionNegocioRepository;
import backend.com.shared.application.service.NumeroDocumentoService;
import backend.com.shared.valueobjects.DocumentNumber;
import backend.com.shared.valueobjects.Money;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class CrearEVNUseCase {

    private final EvaluacionNegocioRepository evnRepository;
    private final NumeroDocumentoService numeroDocumentoService;

    @Transactional
    public EVNResponse ejecutar(CrearEVNCommand command) {
        // Número correlativo atómico desde el contador (ignora el valor del cliente).
        Long numeroLong = numeroDocumentoService.siguiente("EVN");

        String numeroFormateado = String.format("EVN-%d-%07d",
                LocalDate.now().getYear(), numeroLong);

        EvaluacionNegocio evn = EvaluacionNegocio.crear(
                new DocumentNumber(numeroFormateado),
                command.getClienteId(),
                command.getVendedorId(),
                command.getPorcentajeComision(),
                command.getClienteNombre(),
                command.getReferencia(),
                null); // vendedorNombre will be loaded from DB

        if (command.getItems() != null) {
            command.getItems().forEach(dto -> {
                // La cantidad nunca puede ser 0 o null — usamos al menos 1
                int cantidad = (dto.getCantidad() != null && dto.getCantidad() > 0) ? dto.getCantidad() : 1;

                // Precio y costo: usamos 0 como valor mínimo aceptable
                Money precio = new Money(
                        dto.getPrecioUnitario() != null ? dto.getPrecioUnitario() : BigDecimal.ZERO,
                        "CLP");
                Money costo = new Money(
                        dto.getCostoUnitario() != null ? dto.getCostoUnitario()
                                : (dto.getCostoProducto() != null ? dto.getCostoProducto() : BigDecimal.ZERO),
                        "CLP");

                // Utilizar el mapa de especificaciones técnicas enviado desde el frontend
                // o construir uno básico si viene vacío pero hay datos descriptivos
                java.util.Map<String, String> specs = dto.getTechnicalSpecs();
                if (specs == null || specs.isEmpty()) {
                    specs = buildSpecsFromDto(dto);
                }

                ItemEVN item = new ItemEVN(
                        dto.getArticuloId(),
                        dto.getProveedorId(),
                        cantidad,
                        precio,
                        costo,
                        dto.getCostoProducto() != null ? dto.getCostoProducto() : java.math.BigDecimal.ZERO,
                        dto.getCostoLogo() != null ? dto.getCostoLogo() : java.math.BigDecimal.ZERO,
                        dto.getCostoOrdenTrabajo() != null ? dto.getCostoOrdenTrabajo() : java.math.BigDecimal.ZERO,
                        dto.getTipoItem() != null ? dto.getTipoItem() : "SC",
                        specs,
                        dto.getCosteoId(),
                        dto.getSolicitudCostosId());

                evn.addItem(item);
            });
        }

        // Procesar Gastos Adicionales si existen en el command
        if (command.getFlete() != null && command.getFlete().compareTo(BigDecimal.ZERO) > 0) {
            evn.addGastoAdicional(
                    new GastoAdicional(GastoAdicional.TipoGastoAdicional.FLETE, new Money(command.getFlete(), "CLP")));
        }
        if (command.getGarantiaSeriedad() != null && command.getGarantiaSeriedad().compareTo(BigDecimal.ZERO) > 0) {
            evn.addGastoAdicional(new GastoAdicional(GastoAdicional.TipoGastoAdicional.GARANTIA_SERIEDAD,
                    new Money(command.getGarantiaSeriedad(), "CLP")));
        }
        if (command.getGarantiaFielCumplimiento() != null
                && command.getGarantiaFielCumplimiento().compareTo(BigDecimal.ZERO) > 0) {
            evn.addGastoAdicional(new GastoAdicional(GastoAdicional.TipoGastoAdicional.GARANTIA_CUMPLIMIENTO,
                    new Money(command.getGarantiaFielCumplimiento(), "CLP")));
        }
        if (command.getCertificacion() != null && command.getCertificacion().compareTo(BigDecimal.ZERO) > 0) {
            evn.addGastoAdicional(new GastoAdicional(GastoAdicional.TipoGastoAdicional.CERTIFICACION,
                    new Money(command.getCertificacion(), "CLP")));
        }
        if (command.getMuestras() != null && command.getMuestras().compareTo(BigDecimal.ZERO) > 0) {
            evn.addGastoAdicional(new GastoAdicional(GastoAdicional.TipoGastoAdicional.MUESTRAS_FISICAS,
                    new Money(command.getMuestras(), "CLP")));
        }
        if (command.getEntregaPersonalizada() != null
                && command.getEntregaPersonalizada().compareTo(BigDecimal.ZERO) > 0) {
            evn.addGastoAdicional(new GastoAdicional(GastoAdicional.TipoGastoAdicional.ENTREGA_PERSONALIZADA,
                    new Money(command.getEntregaPersonalizada(), "CLP")));
        }
        if (command.getPegadoCinta() != null && command.getPegadoCinta().compareTo(BigDecimal.ZERO) > 0) {
            evn.addGastoAdicional(new GastoAdicional(
                    GastoAdicional.TipoGastoAdicional.PEGADO_CINTA,
                    new Money(command.getPegadoCinta(), "CLP"),
                    command.getPegadoCintaMetadata()));
        }

        // Procesar Toma de Tallaje
        if (command.getTomaTallajeMonto() != null && command.getTomaTallajeMonto().compareTo(BigDecimal.ZERO) > 0) {
            evn.setTomaTallaje(new TomaTallaje(
                    new Money(command.getTomaTallajeMonto(), "CLP"),
                    command.getTomaTallajeObservaciones(),
                    LocalDate.now(),
                    command.getTomaTallajeMetadata()));
        }

        EvaluacionNegocio guardada = evnRepository.save(evn);

        // Enriquecer el response con los datos adicionales del command
        EVNResponse response = EVNResponse.fromDomain(guardada);
        response.setReferencia(command.getReferencia());
        response.setClienteNombre(command.getClienteNombre());

        return response;
    }

    /**
     * Construye un mapa de especificaciones técnicas básico a partir de los campos
     * individuales si no se proveyó un mapa estructurado.
     */
    private java.util.Map<String, String> buildSpecsFromDto(ItemEVNDTO dto) {
        java.util.Map<String, String> specs = new java.util.HashMap<>();
        if (dto.getDescripcion() != null && !dto.getDescripcion().isBlank())
            specs.put("descripcion", dto.getDescripcion());
        if (dto.getModelo() != null && !dto.getModelo().isBlank())
            specs.put("modelo", dto.getModelo());
        if (dto.getGenero() != null && !dto.getGenero().isBlank())
            specs.put("genero", dto.getGenero());
        if (dto.getTela() != null && !dto.getTela().isBlank())
            specs.put("tela", dto.getTela());
        if (dto.getComposicion() != null && !dto.getComposicion().isBlank())
            specs.put("composicion", dto.getComposicion());
        if (dto.getProveedorNombre() != null && !dto.getProveedorNombre().isBlank())
            specs.put("proveedor", dto.getProveedorNombre());
        if (dto.getCodigoInterno() != null && !dto.getCodigoInterno().isBlank())
            specs.put("codigoInterno", dto.getCodigoInterno());
        if (dto.getCodigoProveedor() != null && !dto.getCodigoProveedor().isBlank())
            specs.put("codigoProveedor", dto.getCodigoProveedor());
        return specs;
    }
}
