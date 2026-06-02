package backend.com.comercial.application.UseCase;

import backend.com.comercial.application.dto.CrearNVCommand;
import backend.com.comercial.application.dto.NVResponse;
import backend.com.comercial.application.dto.ItemNVDTO;
import backend.com.comercial.domain.model.ItemNV;
import backend.com.comercial.domain.model.ItemNVTalla;
import backend.com.comercial.domain.model.NotaVenta;
import backend.com.comercial.domain.repository.NotaVentaRepository;
import backend.com.produccion.application.UseCase.CrearOrdenProduccionUseCase;
import backend.com.shared.application.service.NumeroDocumentoService;
import backend.com.shared.valueobjects.DocumentNumber;
import backend.com.shared.valueobjects.Money;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class CrearNVUseCase {

    private final NotaVentaRepository nvRepository;
    private final CrearOrdenProduccionUseCase crearOPUseCase;
    private final NumeroDocumentoService numeroDocumentoService;

    @Transactional
    public NVResponse ejecutar(CrearNVCommand command) {
        // El número se asigna siempre desde el contador atómico,
        // ignorando lo que envíe el cliente (que puede traer un valor obsoleto
        // si dos usuarios consultaron /next-number al mismo tiempo).
        Long numeroVal = numeroDocumentoService.siguiente("NV");

        String numeroFormateado = String.format("NV-%d-%07d",
                LocalDate.now().getYear(), numeroVal);

        NotaVenta nv = NotaVenta.crear(
                new DocumentNumber(numeroFormateado),
                command.getEvaluacionNegocioId(),
                command.getClienteId(),
                command.getVendedorId(),
                command.getEsKit(),
                command.getDetalleKit(),
                command.getFechaEntregaEstimada());

        if (command.getItems() != null) {
            for (int i = 0; i < command.getItems().size(); i++) {
                ItemNVDTO dto = command.getItems().get(i);

                List<ItemNVTalla> tallas = (dto.getTallas() == null) ? null
                        : dto.getTallas().stream()
                                .map(t -> new ItemNVTalla(t.getTalla(), t.getCantidad()))
                                .collect(Collectors.toList());

                ItemNV item = new ItemNV(
                        i + 1,
                        dto.getProductoId(),
                        dto.getModelo(),
                        dto.getTela(),
                        dto.getComposicion(),
                        dto.getColor(),
                        dto.getTalla(),
                        dto.getGenero(),
                        null, // codigo (adding it as null for now if not in DTO)
                        dto.getProveedorId(),
                        dto.getLlevaLogo(),
                        dto.getItemType() != null ? dto.getItemType() : "OP",
                        dto.getGeneraOt(),
                        dto.getDetalleOt(),
                        dto.getLogoDetalle(),
                        dto.getCantidad(),
                        new Money(dto.getPrecioUnitario(), "CLP"),
                        tallas);
                nv.addItem(item);

            }
        }

        NotaVenta guardada = nvRepository.save(nv);

        // Automaticaly generate OrdenProduccion
        crearOPUseCase.execute(guardada);

        return NVResponse.fromDomain(guardada);
    }
}
