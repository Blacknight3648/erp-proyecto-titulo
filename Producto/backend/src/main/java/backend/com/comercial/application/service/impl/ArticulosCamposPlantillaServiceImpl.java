package backend.com.comercial.application.service.impl;

import backend.com.comercial.application.dto.ArticuloCamposPlantillaDTO;
import backend.com.comercial.application.service.ArticuloCamposPlantillaService;
import backend.com.comercial.domain.model.ArticuloCamposPlantilla;
import backend.com.comercial.domain.repository.ArticuloCamposPlantillaRepository;
import backend.com.comercial.infrastructure.mapper.ArticuloCamposPlantillaMapper;
import backend.com.shared.domain.model.Articulo;
import backend.com.shared.exception.BusinessRuleException;
import backend.com.shared.exception.EntityNotFoundException;
import backend.com.shared.infrastructure.persistence.repository.ArticuloRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ArticulosCamposPlantillaServiceImpl implements ArticuloCamposPlantillaService {

    private final ArticuloCamposPlantillaRepository modeloPlantillaRepository;
    private final ArticuloRepository articuloRepository;
    private final ArticuloCamposPlantillaMapper mapper;

    @Override
    public ArticuloCamposPlantillaDTO guardar(ArticuloCamposPlantillaDTO dto) {
        Articulo articulo = articuloRepository.findById(dto.getIdArticulo())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Articulo con id " + dto.getIdArticulo() + " no encontrado"));

        List<String> campos = normalizar(dto.getCamposPlantilla());
        if (campos.isEmpty()) {
            throw new BusinessRuleException("Debe indicar al menos un campo de plantilla");
        }

        // Upsert: una sola fila por artículo.
        ArticuloCamposPlantilla modelo = modeloPlantillaRepository.findByArticuloId(dto.getIdArticulo())
                .map(existente -> {
                    existente.setArticulo(articulo);
                    existente.setCampos(campos);
                    return existente;
                })
                .orElseGet(() -> ArticuloCamposPlantilla.builder()
                        .articulo(articulo)
                        .campos(campos)
                        .build());

        return mapper.toDTO(modeloPlantillaRepository.save(modelo));
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<ArticuloCamposPlantillaDTO> obtenerPorArticulo(Integer idArticulo) {
        return modeloPlantillaRepository.findByArticuloId(idArticulo).map(mapper::toDTO);
    }

    @Override
    public void eliminarPorArticulo(Integer idArticulo) {
        if (modeloPlantillaRepository.findByArticuloId(idArticulo).isEmpty()) {
            throw new EntityNotFoundException(
                    "No existe configuración de plantilla para el articulo " + idArticulo);
        }
        modeloPlantillaRepository.deleteByArticuloId(idArticulo);
    }

    /** Limpia nombres: trim, descarta vacíos y duplicados (preservando orden). */
    private List<String> normalizar(List<String> campos) {
        if (campos == null) return List.of();
        return campos.stream()
                .filter(c -> c != null && !c.trim().isEmpty())
                .map(String::trim)
                .distinct()
                .toList();
    }
}
