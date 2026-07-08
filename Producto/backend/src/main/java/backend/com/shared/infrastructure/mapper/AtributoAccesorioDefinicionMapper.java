package backend.com.shared.infrastructure.mapper;

import backend.com.shared.application.dto.AtributoAccesorioDefinicionDTO;
import backend.com.shared.domain.model.AtributoAccesorioDefinicion;
import backend.com.shared.infrastructure.persistence.entity.AtributoAccesorioDefinicionJpaEntity;
import backend.com.shared.infrastructure.persistence.entity.TipoAccesorioJpaEntity;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class AtributoAccesorioDefinicionMapper {

    private final TipoAccesorioMapper tipoAccesorioMapper;

    public AtributoAccesorioDefinicionMapper(TipoAccesorioMapper tipoAccesorioMapper) {
        this.tipoAccesorioMapper = tipoAccesorioMapper;
    }

    public AtributoAccesorioDefinicion toDomain(AtributoAccesorioDefinicionJpaEntity entity) {
        if (entity == null) return null;
        return AtributoAccesorioDefinicion.builder()
                .idDefinicion(entity.getIdDefinicion())
                .tipoAccesorio(tipoAccesorioMapper.toDomain(entity.getTipoAccesorio()))
                .nombreCampo(entity.getNombreCampo())
                .tipoDato(entity.getTipoDato())
                .opciones(entity.getOpciones())
                .orden(entity.getOrden())
                .requerido(entity.getRequerido())
                .build();
    }

    public AtributoAccesorioDefinicionJpaEntity toEntity(AtributoAccesorioDefinicion domain) {
        if (domain == null) return null;
        return AtributoAccesorioDefinicionJpaEntity.builder()
                .idDefinicion(domain.getIdDefinicion())
                .tipoAccesorio(domain.getTipoAccesorio() != null
                        ? TipoAccesorioJpaEntity.builder().idTipoAccesorio(domain.getTipoAccesorio().getIdTipoAccesorio()).build()
                        : null)
                .nombreCampo(domain.getNombreCampo())
                .tipoDato(domain.getTipoDato())
                .opciones(domain.getOpciones())
                .orden(domain.getOrden() != null ? domain.getOrden() : 0)
                .requerido(domain.getRequerido() != null ? domain.getRequerido() : false)
                .build();
    }

    public AtributoAccesorioDefinicionDTO toDTO(AtributoAccesorioDefinicion domain) {
        if (domain == null) return null;
        return AtributoAccesorioDefinicionDTO.builder()
                .idDefinicion(domain.getIdDefinicion())
                .tipoAccesorio(tipoAccesorioMapper.toDTO(domain.getTipoAccesorio()))
                .nombreCampo(domain.getNombreCampo())
                .tipoDato(domain.getTipoDato())
                .opciones(dividirOpciones(domain.getOpciones()))
                .orden(domain.getOrden())
                .requerido(domain.getRequerido())
                .build();
    }

    public AtributoAccesorioDefinicion toDomain(AtributoAccesorioDefinicionDTO dto) {
        if (dto == null) return null;
        return AtributoAccesorioDefinicion.builder()
                .idDefinicion(dto.getIdDefinicion())
                .tipoAccesorio(dto.getTipoAccesorio() != null
                        ? backend.com.shared.domain.model.TipoAccesorio.builder()
                                .idTipoAccesorio(dto.getTipoAccesorio().getIdTipoAccesorio()).build()
                        : null)
                .nombreCampo(dto.getNombreCampo())
                .tipoDato(dto.getTipoDato())
                .opciones(unirOpciones(dto.getOpciones()))
                .orden(dto.getOrden())
                .requerido(dto.getRequerido())
                .build();
    }

    private List<String> dividirOpciones(String opciones) {
        if (opciones == null || opciones.isBlank()) return List.of();
        return Arrays.asList(opciones.split("\\|"));
    }

    private String unirOpciones(List<String> opciones) {
        if (opciones == null || opciones.isEmpty()) return null;
        return opciones.stream().filter(o -> o != null && !o.isBlank()).collect(Collectors.joining("|"));
    }
}
