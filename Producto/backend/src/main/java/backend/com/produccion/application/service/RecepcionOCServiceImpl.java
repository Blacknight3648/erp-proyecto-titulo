package backend.com.produccion.application.service;

import backend.com.produccion.application.dto.RecepcionOCDTO;
import backend.com.produccion.application.dto.RecepcionOCItemDTO;
import backend.com.produccion.domain.model.RecepcionOC;
import backend.com.produccion.domain.model.RecepcionOCItem;
import backend.com.produccion.domain.repository.RecepcionOCRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RecepcionOCServiceImpl implements RecepcionOCService {

    private final RecepcionOCRepository recepcionOCRepository;
    private final RegistrarRecepcionOCUseCase registrarRecepcionOCUseCase;

    @Override
    public RecepcionOCDTO registrar(Long ocId, RecepcionOCDTO request) {
        return toDTO(registrarRecepcionOCUseCase.ejecutar(ocId, request));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RecepcionOCDTO> obtenerPorId(Long idRecepcion) {
        return recepcionOCRepository.findById(idRecepcion).map(this::toDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecepcionOCDTO> listarPorOC(Long ocId) {
        return recepcionOCRepository.findAllByOcId(ocId).stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    private RecepcionOCDTO toDTO(RecepcionOC rec) {
        if (rec == null) return null;
        return RecepcionOCDTO.builder()
                .idRecepcion(rec.getIdRecepcion())
                .ocId(rec.getOcId())
                .fechaRecepcion(rec.getFechaRecepcion())
                .numeroGuia(rec.getNumeroGuia())
                .responsable(rec.getResponsable())
                .observaciones(rec.getObservaciones())
                .items(rec.getItems() != null
                        ? rec.getItems().stream().map(this::itemToDTO).collect(Collectors.toList())
                        : List.of())
                .build();
    }

    private RecepcionOCItemDTO itemToDTO(RecepcionOCItem item) {
        return RecepcionOCItemDTO.builder()
                .idRecepcionItem(item.getIdRecepcionItem())
                .recepcionId(item.getRecepcionId())
                .ocItemId(item.getOcItemId())
                .cantidadRecibida(item.getCantidadRecibida())
                .cantidadConforme(item.getCantidadConforme())
                .cantidadRechazada(item.getCantidadRechazada())
                .motivoRechazo(item.getMotivoRechazo())
                .build();
    }
}
