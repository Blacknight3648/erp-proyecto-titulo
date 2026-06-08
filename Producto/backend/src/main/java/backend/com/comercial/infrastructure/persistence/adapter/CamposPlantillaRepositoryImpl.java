package backend.com.comercial.infrastructure.persistence.adapter;

import backend.com.comercial.domain.model.CamposPlantilla;
import backend.com.comercial.domain.repository.CamposPlantillaRepository;
import backend.com.comercial.infrastructure.mapper.CamposPlantillaMapper;
import backend.com.comercial.infrastructure.persistence.repository.CamposPlantillaJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class CamposPlantillaRepositoryImpl implements CamposPlantillaRepository {

    private final CamposPlantillaJpaRepository jpaRepository;
    private final CamposPlantillaMapper mapper;

    @Override
    public List<CamposPlantilla> findAll() {
        return jpaRepository.findAll().stream().map(mapper::toDomain).toList();
    }

    @Override
    public Optional<CamposPlantilla> findById(Long id) {
        if (id == null) return Optional.empty();
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public Optional<CamposPlantilla> findByNombreCampo(String nombreCampo) {
        return jpaRepository.findByNombreCampoIgnoreCase(nombreCampo).map(mapper::toDomain);
    }

    @Override
    public boolean existsByNombreCampo(String nombreCampo) {
        return jpaRepository.existsByNombreCampoIgnoreCase(nombreCampo);
    }

    @Override
    public boolean existsById(Long id) {
        return id != null && jpaRepository.existsById(id);
    }

    @Override
    public CamposPlantilla save(CamposPlantilla plantilla) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(plantilla)));
    }

    @Override
    public void deleteById(Long id) {
        if (id != null) jpaRepository.deleteById(id);
    }
}
