package backend.com.comercial.application.UseCase;

import backend.com.comercial.application.dto.CrearNVCommand;
import backend.com.comercial.application.dto.NVResponse;
import backend.com.comercial.application.dto.ItemNVDTO;
import backend.com.comercial.domain.enums.EstadoEVN;
import backend.com.comercial.domain.enums.TipoItem;
import backend.com.comercial.domain.model.EvaluacionNegocio;
import backend.com.comercial.domain.model.ItemNV;
import backend.com.comercial.domain.model.ItemNVTalla;
import backend.com.comercial.domain.model.NotaVenta;
import backend.com.comercial.domain.repository.EvaluacionNegocioRepository;
import backend.com.comercial.domain.repository.NotaVentaRepository;
import backend.com.produccion.application.UseCase.CrearOrdenProduccionUseCase;
import backend.com.shared.application.service.NumeroDocumentoService;
import backend.com.shared.exception.EVNBusinessException;
import backend.com.shared.valueobjects.DocumentNumber;
import backend.com.shared.valueobjects.Money;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CrearNVUseCase {

    private final NotaVentaRepository nvRepository;
    private final CrearOrdenProduccionUseCase crearOPUseCase;
    private final NumeroDocumentoService numeroDocumentoService;
    private final EvaluacionNegocioRepository evnRepository;

    @Transactional
    public NVResponse ejecutar(CrearNVCommand command) {
        // Gate de consistencia: la NV se genera a partir de una EVN, que debe estar
        // ADJUDICADA. Una EVN CERRADA (o en cualquier otro estado) bloquea la
        // generación de nuevas Notas de Venta. La guarda != null es defensiva: el
        // dominio NotaVenta ya exige un evaluacionNegocioId obligatorio.
        if (command.getEvaluacionNegocioId() != null) {
            EvaluacionNegocio evn = evnRepository.findById(command.getEvaluacionNegocioId())
                    .orElseThrow(() -> new EVNBusinessException(
                            "Evaluación de Negocio no encontrada: " + command.getEvaluacionNegocioId()));
            if (evn.getEstado() != EstadoEVN.ADJUDICADA) {
                throw new EVNBusinessException(
                        "No se puede generar una Nota de Venta: la EVN debe estar ADJUDICADA (estado actual: "
                                + evn.getEstado() + ")");
            }
        }

        // El número se asigna siempre desde el contador atómico,
        // ignorando lo que envíe el cliente (que puede traer un valor obsoleto
        // si dos usuarios consultaron /next-number al mismo tiempo).
        DocumentNumber numero = numeroDocumentoService.siguienteFormateado("NV");

        NotaVenta nv = NotaVenta.crear(
                numero,
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
                        null, // idItemNV: lo asigna la BD al persistir
                        i + 1,
                        dto.getArticuloId(),
                        dto.getModelo(),
                        dto.getTela(),
                        dto.getComposicion(),
                        dto.getColor(),
                        dto.getTalla(),
                        dto.getGenero(),
                        null, // codigo (adding it as null for now if not in DTO)
                        dto.getProveedorId(),
                        dto.getLlevaLogo(),
                        dto.getItemType() != null ? dto.getItemType() : TipoItem.OP,
                        dto.getRequiereOt(),
                        dto.getDetalleOt(),
                        dto.getLogoDetalle(),
                        dto.getCantidad(),
                        new Money(dto.getPrecioUnitario(), "CLP"),
                        tallas);
                nv.addItem(item);

            }
        }

        NotaVenta guardada = nvRepository.save(nv);

        // Crear OP y obtener el id asignado para vincularlo a los ítems de la NV
        boolean tieneItemsOP = guardada.getItems() != null &&
                guardada.getItems().stream().anyMatch(i -> TipoItem.OP == i.getTipoItem());

        if (tieneItemsOP) {
            backend.com.produccion.domain.model.OrdenProduccion op = crearOPUseCase.execute(guardada);
            // Trazabilidad: registrar qué OP fue generada para cada ítem OP de esta NV
            nvRepository.vincularOpAItems(guardada.getIdNV(), op.getIdOP());
        }

        return NVResponse.fromDomain(guardada);
    }
}
