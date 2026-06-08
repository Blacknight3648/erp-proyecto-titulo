package backend.com.comercial.application.service.impl;

import backend.com.comercial.application.dto.ArticuloCamposPlantillaDTO;
import backend.com.comercial.application.service.ArticuloCamposPlantillaService;
import backend.com.comercial.domain.model.ArticuloCamposPlantilla;
import backend.com.comercial.domain.model.CamposPlantilla;
import backend.com.comercial.domain.repository.ArticuloCamposPlantillaRepository;
import backend.com.comercial.domain.repository.CamposPlantillaRepository;
import backend.com.comercial.infrastructure.mapper.ArticuloCamposPlantillaMapper;
import backend.com.shared.domain.model.Articulo;
import backend.com.shared.exception.BusinessRuleException;
import backend.com.shared.exception.EntityNotFoundException;
import backend.com.shared.infrastructure.persistence.repository.ArticuloRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ArticulosCamposPlantillaServiceImpl implements ArticuloCamposPlantillaService {

    private final ArticuloCamposPlantillaRepository modeloPlantillaRepository;
    private final ArticuloRepository articuloRepository;
    private final CamposPlantillaRepository plantillaRepository;
    private final ArticuloCamposPlantillaMapper mapper;

    @Override
    public ArticuloCamposPlantillaDTO crear(ArticuloCamposPlantillaDTO dto) {
        if (modeloPlantillaRepository.existsByArticuloIdAndPlantillaId(dto.getIdArticulo(), dto.getIdPlantilla())) {
            throw new BusinessRuleException(
                    "Ya existe un ModeloPlantilla para el articulo " + dto.getIdArticulo()
                            + " y la plantilla " + dto.getIdPlantilla());
        }
        Articulo articulo = articuloRepository.findById(dto.getIdArticulo())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Articulo con id " + dto.getIdArticulo() + " no encontrado"));
        CamposPlantilla plantilla = plantillaRepository.findById(dto.getIdPlantilla())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Plantilla con id " + dto.getIdPlantilla() + " no encontrada"));

        ArticuloCamposPlantilla nuevo = ArticuloCamposPlantilla.builder()
                .articulo(articulo)
                .plantilla(plantilla)
                .build();
        return mapper.toDTO(modeloPlantillaRepository.save(nuevo));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ArticuloCamposPlantillaDTO> listarPorArticulo(Integer idArticulo) {
        return modeloPlantillaRepository.findByArticuloId(idArticulo)
                .stream().map(mapper::toDTO).toList();
    }

    @Override
    public void eliminar(Long id) {
        if (!modeloPlantillaRepository.existsById(id)) {
            throw new EntityNotFoundException("ModeloPlantilla con id " + id + " no encontrado");
        }
        modeloPlantillaRepository.deleteById(id);
    }
}
