package backend.com.comercial.application.service.impl;

import backend.com.comercial.application.dto.DescripcionPlantillaDTO;
import backend.com.comercial.application.dto.SCOSPlantillaMaterialVinculoDTO;
import backend.com.comercial.application.service.DescripcionPlantillaService;
import backend.com.comercial.domain.model.DescripcionPlantilla;
import backend.com.comercial.domain.model.CamposPlantilla;
import backend.com.comercial.domain.model.SCOSPlantillaMaterialVinculo;
import backend.com.comercial.domain.repository.DescripcionPlantillaRepository;
import backend.com.comercial.domain.repository.CamposPlantillaRepository;
import backend.com.comercial.infrastructure.mapper.DescripcionPlantillaMapper;
import backend.com.comercial.infrastructure.mapper.SCOSPlantillaMaterialVinculoMapper;
import backend.com.shared.exception.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DescripcionPlantillaServiceImpl implements DescripcionPlantillaService {

    private final DescripcionPlantillaRepository descripcionRepository;
    private final CamposPlantillaRepository plantillaRepository;
    private final DescripcionPlantillaMapper mapper;
    private final SCOSPlantillaMaterialVinculoMapper vinculoMapper;

    @Override
    public DescripcionPlantillaDTO crear(DescripcionPlantillaDTO dto) {
        CamposPlantilla plantilla = plantillaRepository.findById(dto.getIdPlantilla())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Plantilla con id " + dto.getIdPlantilla() + " no encontrada"));

        DescripcionPlantilla nueva = DescripcionPlantilla.builder()
                .idSCOS(dto.getIdSCOS())
                .plantilla(plantilla)
                .valorDescripcion(dto.getValorDescripcion())
                .activo(dto.getActivo() != null ? dto.getActivo() : true)
                .vinculos(toDomainVinculos(dto.getVinculos()))
                .build();
        return mapper.toDTO(descripcionRepository.save(nueva));
    }

    @Override
    public DescripcionPlantillaDTO actualizar(Long id, DescripcionPlantillaDTO dto) {
        DescripcionPlantilla existente = findOrThrow(id);

        if (dto.getIdPlantilla() != null
                && (existente.getPlantilla() == null
                        || !dto.getIdPlantilla().equals(existente.getPlantilla().getIdPlantilla()))) {
            CamposPlantilla plantilla = plantillaRepository.findById(dto.getIdPlantilla())
                    .orElseThrow(() -> new EntityNotFoundException(
                            "Plantilla con id " + dto.getIdPlantilla() + " no encontrada"));
            existente.setPlantilla(plantilla);
        }

        existente.setValorDescripcion(dto.getValorDescripcion());
        if (dto.getActivo() != null) existente.setActivo(dto.getActivo());

        existente.getVinculos().clear();
        existente.getVinculos().addAll(toDomainVinculos(dto.getVinculos()));

        return mapper.toDTO(descripcionRepository.save(existente));
    }

    @Override
    @Transactional(readOnly = true)
    public DescripcionPlantillaDTO obtenerPorId(Long id) {
        return mapper.toDTO(findOrThrow(id));
    }

    @Override
    @Transactional(readOnly = true)
    public List<DescripcionPlantillaDTO> listarPorSCOS(Long idSCOS) {
        return descripcionRepository.findByIdSCOS(idSCOS)
                .stream().map(mapper::toDTO).toList();
    }

    @Override
    public void eliminar(Long id) {
        if (!descripcionRepository.existsById(id)) {
            throw new EntityNotFoundException("DescripcionPlantilla con id " + id + " no encontrada");
        }
        descripcionRepository.deleteById(id);
    }

    private DescripcionPlantilla findOrThrow(Long id) {
        return descripcionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "DescripcionPlantilla con id " + id + " no encontrada"));
    }

    private List<SCOSPlantillaMaterialVinculo> toDomainVinculos(List<SCOSPlantillaMaterialVinculoDTO> dtos) {
        if (dtos == null) return new ArrayList<>();
        return new ArrayList<>(dtos.stream().map(vinculoMapper::toDomain).toList());
    }
}
