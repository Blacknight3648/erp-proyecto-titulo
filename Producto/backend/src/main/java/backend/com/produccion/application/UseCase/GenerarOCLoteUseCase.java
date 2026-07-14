package backend.com.produccion.application.UseCase;

import backend.com.produccion.application.dto.GenerarOCConsolidadaRequest;
import backend.com.produccion.application.dto.GenerarOCLoteRequest;
import backend.com.produccion.domain.model.OrdenCompra;
import backend.com.produccion.domain.repository.OrdenCompraRepository;
import backend.com.shared.exception.BusinessRuleException;
import backend.com.shared.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class GenerarOCLoteUseCase {

    private final GenerarOCConsolidadaUseCase generarOCConsolidadaUseCase;
    private final OrdenCompraRepository ordenCompraRepository;

    @Transactional
    public List<OrdenCompra> ejecutar(GenerarOCLoteRequest request) {
        validar(request);

        List<OrdenCompra> ocs = new ArrayList<>();
        for (GenerarOCConsolidadaRequest grupo : request.getGrupos()) {
            OrdenCompra oc = generarOCConsolidadaUseCase.construirOC(grupo);
            oc = ordenCompraRepository.save(oc);
            generarOCConsolidadaUseCase.vincularItemsAOrdenCompra(oc, grupo.getHcItemIds());
            ocs.add(oc);
        }
        return ocs;
    }

    private void validar(GenerarOCLoteRequest request) {
        if (request == null || request.getGrupos() == null || request.getGrupos().isEmpty()) {
            throw new ValidationException("Debe enviar al menos un grupo para consolidar");
        }

        Set<Long> vistos = new HashSet<>();
        for (GenerarOCConsolidadaRequest grupo : request.getGrupos()) {
            if (grupo.getHcItemIds() == null) {
                continue;
            }
            for (Long hcItemId : grupo.getHcItemIds()) {
                if (!vistos.add(hcItemId)) {
                    throw new BusinessRuleException(
                            "El hcItemId " + hcItemId + " está duplicado entre distintos grupos de la misma tanda");
                }
            }
        }
    }
}
