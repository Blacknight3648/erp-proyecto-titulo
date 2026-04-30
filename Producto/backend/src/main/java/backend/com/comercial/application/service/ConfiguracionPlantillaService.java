package backend.com.comercial.application.service;

import backend.com.comercial.application.dto.ConfiguracionPlantillaDTO;
import backend.com.comercial.application.dto.PlantillaAccesorioDTO;
import backend.com.comercial.application.dto.PlantillaTelaDTO;
import backend.com.comercial.infrastructure.persistence.entity.ConfiguracionPlantillaJpaEntity;
import backend.com.comercial.infrastructure.persistence.entity.PlantillaAccesorio;
import backend.com.comercial.infrastructure.persistence.entity.PlantillaTela;
import backend.com.comercial.infrastructure.persistence.repository.ConfiguracionPlantillaJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ConfiguracionPlantillaService {

    private final ConfiguracionPlantillaJpaRepository repository;

    @Transactional(readOnly = true)
    public List<ConfiguracionPlantillaDTO> findAll() {
        return repository.findAll().stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ConfiguracionPlantillaDTO findByNombrePrenda(String nombrePrenda) {
        return repository.findByNombrePrenda(nombrePrenda.toUpperCase())
                .map(this::toDTO)
                .orElse(null);
    }

    @Transactional
    public ConfiguracionPlantillaDTO save(ConfiguracionPlantillaDTO dto) {
        ConfiguracionPlantillaJpaEntity entity = repository.findByNombrePrenda(dto.getNombrePrenda())
                .orElse(new ConfiguracionPlantillaJpaEntity());
        
        entity.setNombrePrenda(dto.getNombrePrenda().toUpperCase());
        entity.setCamposActivos(dto.getCamposActivos());
        entity.setCustomFields(dto.getCustomFields());
        
        // Mapear Telas
        entity.getPlantillaTelas().clear();
        if (dto.getPlantillaTelas() != null) {
            dto.getPlantillaTelas().forEach(t -> entity.getPlantillaTelas().add(new PlantillaTela(
                    t.getAplicacion(), t.getNombre(), t.getComposicion(), t.getColor(), t.getPeso(), 
                    t.getUnidadMedida() != null ? t.getUnidadMedida() : "mts"
            )));
        }

        // Mapear Accesorios
        entity.getPlantillaAccesorios().clear();
        if (dto.getPlantillaAccesorios() != null) {
            dto.getPlantillaAccesorios().forEach(a -> entity.getPlantillaAccesorios().add(new PlantillaAccesorio(
                    a.getTipo(), a.getNombreAccesorio(), a.getCantidad()
            )));
        }
        
        return toDTO(repository.save(entity));
    }

    @Transactional
    public void delete(Long id) {
        if (id != null) {
            repository.deleteById(id);
        }
    }

    private ConfiguracionPlantillaDTO toDTO(ConfiguracionPlantillaJpaEntity entity) {
        ConfiguracionPlantillaDTO dto = new ConfiguracionPlantillaDTO();
        dto.setId(entity.getId());
        dto.setNombrePrenda(entity.getNombrePrenda());
        dto.setCamposActivos(entity.getCamposActivos());
        dto.setCustomFields(entity.getCustomFields());
        
        if (entity.getPlantillaTelas() != null) {
            dto.setPlantillaTelas(entity.getPlantillaTelas().stream()
                .map(t -> new PlantillaTelaDTO(t.getAplicacion(), t.getNombre(), t.getComposicion(), t.getColor(), t.getPeso(), t.getUnidadMedida()))
                .collect(Collectors.toList()));
        }
        
        if (entity.getPlantillaAccesorios() != null) {
            dto.setPlantillaAccesorios(entity.getPlantillaAccesorios().stream()
                .map(a -> new PlantillaAccesorioDTO(a.getTipo(), a.getNombreAccesorio(), a.getCantidad()))
                .collect(Collectors.toList()));
        }
        
        return dto;
    }
}
