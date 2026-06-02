package backend.com.produccion.application.UseCase;

import backend.com.produccion.domain.model.Costeo;
import backend.com.produccion.domain.model.CosteoItem;
import backend.com.produccion.domain.model.CosteoItemVersion;
import backend.com.produccion.domain.model.CosteoVersion;
import backend.com.produccion.domain.repository.CosteoRepository;
import backend.com.produccion.domain.repository.CosteoVersionRepository;
import backend.com.shared.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CrearVersionCosteoUseCase {

        private final CosteoRepository costeoRepository;
        private final CosteoVersionRepository costeoVersionRepository;

        public CosteoVersion ejecutar(Long costeoId, String motivoCambio, String usuarioCreador) {
                if (costeoId == null) {
                        throw new ValidationException("El costeoId es obligatorio para crear una versión");
                }

                Costeo costeo = costeoRepository.findById(costeoId)
                                .orElseThrow(() -> new ValidationException("No existe el Costeo con id: " + costeoId));

                Integer numeroVersion = costeoVersionRepository.siguienteNumeroVersion(costeoId);

                List<CosteoItemVersion> snapshotItems = costeo.getItems() != null
                                ? costeo.getItems().stream()
                                                .map(this::snapshotItem)
                                                .collect(Collectors.toList())
                                : List.of();

                CosteoVersion version = new CosteoVersion(
                                null,
                                costeoId,
                                numeroVersion,
                                LocalDateTime.now(),
                                motivoCambio,
                                usuarioCreador,
                                snapshotItems,
                                costeo.getCostoManoObra(),
                                costeo.getCostoHilos(),
                                costeo.getCostoFlete(),
                                costeo.getCostoEmbalaje(),
                                costeo.getCostoEtiquetas(),
                                costeo.getPorcentajeCostoFijo(),
                                costeo.getCostoTotalMateriaPrima(),
                                costeo.getMargenBrutoSugerido(),
                                costeo.getPrecioVentaSugerido());

                return costeoVersionRepository.save(version);
        }

        private CosteoItemVersion snapshotItem(CosteoItem origen) {
                return new CosteoItemVersion(
                                null,
                                null,
                                origen.getTipoInsumo(),
                                origen.getInsumoId(),
                                origen.getNombreInsumo(),
                                origen.getConsumo(),
                                origen.getPrecioUnitario(),
                                origen.getCostoTotal());
        }
}
