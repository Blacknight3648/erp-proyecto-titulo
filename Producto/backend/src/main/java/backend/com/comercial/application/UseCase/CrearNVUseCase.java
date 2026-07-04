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
                        "Prenda",
                        dto.getModelo(),
                        dto.getTela(),
                        dto.getComposicion(),
                        dto.getColor(),
                        dto.getTalla(),
                        dto.getGenero(),
                        null, // codigo (adding it as null for now if not in DTO)
                        dto.getProveedorId(),
                        "PENDIENTE",
                        dto.getLlevaLogo(),
                        dto.getItemType() != null ? dto.getItemType() : TipoItem.OP,
                        dto.getRequiereOt(),
                        dto.getDetalleOt(),
                        dto.getLogoDetalle(),
                        dto.getCantidad(),
                        new Money(dto.getPrecioUnitario(), "CLP"),
                        tallas);

                // Costeo elegido manualmente para este ítem OP (si no hereda uno de la EVN).
                if (dto.getCosteoId() != null) {
                    item.setCosteoIdManual(dto.getCosteoId());
                }
                nv.addItem(item);

            }
        }

        if (Boolean.TRUE.equals(command.getEmitir())) {
            nv.emitir();
        }

        // Borrador: reservar números de OP para cada ítem tipo OP nuevo (sin número aún).
        // El número se consume del contador atómico para que nadie más pueda usarlo.
        if (!Boolean.TRUE.equals(command.getEmitir())) {
            if (nv.getItems() != null) {
                for (ItemNV item : nv.getItems()) {
                    if (TipoItem.OP == item.getTipoItem()
                            && item.getNumeroOPReservado() == null
                            && item.getOpId() == null) {
                        DocumentNumber reservado = numeroDocumentoService.siguienteFormateado("OP");
                        item.setNumeroOPReservado(reservado.getValue());
                    }
                }
            }
        }

        NotaVenta guardada = nvRepository.save(nv);

        // Solo al emitir se crean las OPs reales (con costeo, fases, etc.).
        if (Boolean.TRUE.equals(command.getEmitir())) {
            crearOPUseCase.execute(guardada);
            // Re-leer desde BD para que el NVResponse incluya los opId asignados por vincularOpAItem.
            guardada = nvRepository.findById(guardada.getIdNV()).orElse(guardada);
        }

        return NVResponse.fromDomain(guardada);
    }
}
