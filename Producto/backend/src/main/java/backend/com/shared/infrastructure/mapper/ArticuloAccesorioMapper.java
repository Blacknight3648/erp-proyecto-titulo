package backend.com.shared.infrastructure.mapper;

import backend.com.shared.application.dto.ArticuloAccesorioDTO;
import backend.com.shared.application.dto.AtributoAccesorioValorDTO;
import backend.com.shared.domain.model.ArticuloAccesorio;
import backend.com.shared.domain.model.AtributoAccesorioDefinicion;
import backend.com.shared.domain.model.AtributoAccesorioValor;
import backend.com.shared.domain.model.ColorTela;
import backend.com.shared.infrastructure.persistence.entity.ArticuloAccesorioAtributoValorJpaEntity;
import backend.com.shared.infrastructure.persistence.entity.ArticuloAccesorioJpaEntity;
import backend.com.shared.infrastructure.persistence.entity.AtributoAccesorioDefinicionJpaEntity;
import backend.com.shared.infrastructure.persistence.entity.ColorTelaJpaEntity;
import backend.com.shared.infrastructure.persistence.entity.TipoAccesorioJpaEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ArticuloAccesorioMapper {

    private final TipoAccesorioMapper tipoAccesorioMapper;
    private final ColorTelaMapper colorTelaMapper;

    public ArticuloAccesorio toDomain(ArticuloAccesorioJpaEntity entity) {
        if (entity == null) return null;
        return ArticuloAccesorio.builder()
                .id(entity.getId())
                .tipoAccesorio(tipoAccesorioMapper.toDomain(entity.getTipoAccesorio()))
                .atributos(entity.getAtributos() == null ? null
                        : entity.getAtributos().stream().map(this::valorToDomain).toList())
                .proveedor(entity.getProveedor())
                .codigoProveedor(entity.getCodigoProveedor())
                .requiereLogoCliente(entity.getRequiereLogoCliente())
                .build();
    }

    public ArticuloAccesorioJpaEntity toEntity(ArticuloAccesorio domain) {
        if (domain == null) return null;
        ArticuloAccesorioJpaEntity entity = ArticuloAccesorioJpaEntity.builder()
                .id(domain.getId())
                .tipoAccesorio(domain.getTipoAccesorio() != null && domain.getTipoAccesorio().getIdTipoAccesorio() != null
                        ? TipoAccesorioJpaEntity.builder().idTipoAccesorio(domain.getTipoAccesorio().getIdTipoAccesorio()).build()
                        : null)
                .proveedor(domain.getProveedor())
                .codigoProveedor(domain.getCodigoProveedor())
                .requiereLogoCliente(domain.getRequiereLogoCliente() != null ? domain.getRequiereLogoCliente() : false)
                .build();

        List<ArticuloAccesorioAtributoValorJpaEntity> valores = domain.getAtributos() == null ? List.of()
                : domain.getAtributos().stream().map(v -> valorToEntity(v, entity)).toList();
        entity.setAtributos(valores);
        return entity;
    }

    public ArticuloAccesorioDTO toDTO(ArticuloAccesorio domain) {
        if (domain == null) return null;
        return ArticuloAccesorioDTO.builder()
                .id(domain.getId())
                .tipoAccesorio(tipoAccesorioMapper.toDTO(domain.getTipoAccesorio()))
                .atributos(domain.getAtributos() == null ? null
                        : domain.getAtributos().stream().map(this::valorToDTO).toList())
                .proveedor(domain.getProveedor())
                .codigoProveedor(domain.getCodigoProveedor())
                .requiereLogoCliente(domain.getRequiereLogoCliente())
                .build();
    }

    public ArticuloAccesorio toDomain(ArticuloAccesorioDTO dto) {
        if (dto == null) return null;
        return ArticuloAccesorio.builder()
                .id(dto.getId())
                .tipoAccesorio(dto.getTipoAccesorio() != null && dto.getTipoAccesorio().getIdTipoAccesorio() != null
                        ? backend.com.shared.domain.model.TipoAccesorio.builder()
                                .idTipoAccesorio(dto.getTipoAccesorio().getIdTipoAccesorio()).build()
                        : null)
                .atributos(dto.getAtributos() == null ? null
                        : dto.getAtributos().stream().map(this::valorToDomainFromDTO).toList())
                .proveedor(dto.getProveedor())
                .codigoProveedor(dto.getCodigoProveedor())
                .requiereLogoCliente(dto.getRequiereLogoCliente())
                .build();
    }

    // ── valores de atributo ──────────────────────────────────────────────

    private AtributoAccesorioValor valorToDomain(ArticuloAccesorioAtributoValorJpaEntity entity) {
        AtributoAccesorioDefinicion definicion = entity.getDefinicion() == null ? null
                : AtributoAccesorioDefinicion.builder()
                        .idDefinicion(entity.getDefinicion().getIdDefinicion())
                        .nombreCampo(entity.getDefinicion().getNombreCampo())
                        .tipoDato(entity.getDefinicion().getTipoDato())
                        .build();
        ColorTela colorReferencia = entity.getColorReferencia() == null ? null
                : colorTelaMapper.toDomain(entity.getColorReferencia());
        return AtributoAccesorioValor.builder()
                .idValor(entity.getIdValor())
                .definicion(definicion)
                .valorTexto(entity.getValorTexto())
                .colorReferencia(colorReferencia)
                .build();
    }

    private ArticuloAccesorioAtributoValorJpaEntity valorToEntity(
            AtributoAccesorioValor domain, ArticuloAccesorioJpaEntity padre) {
        return ArticuloAccesorioAtributoValorJpaEntity.builder()
                .idValor(domain.getIdValor())
                .articuloAccesorio(padre)
                .definicion(domain.getDefinicion() != null && domain.getDefinicion().getIdDefinicion() != null
                        ? AtributoAccesorioDefinicionJpaEntity.builder()
                                .idDefinicion(domain.getDefinicion().getIdDefinicion()).build()
                        : null)
                .valorTexto(domain.getValorTexto())
                .colorReferencia(domain.getColorReferencia() != null && domain.getColorReferencia().getIdColor() != null
                        ? ColorTelaJpaEntity.builder().idColor(domain.getColorReferencia().getIdColor()).build()
                        : null)
                .build();
    }

    private AtributoAccesorioValorDTO valorToDTO(AtributoAccesorioValor domain) {
        return AtributoAccesorioValorDTO.builder()
                .idValor(domain.getIdValor())
                .idDefinicion(domain.getDefinicion() != null ? domain.getDefinicion().getIdDefinicion() : null)
                .nombreCampo(domain.getDefinicion() != null ? domain.getDefinicion().getNombreCampo() : null)
                .valorTexto(domain.getValorTexto())
                .idColorReferencia(domain.getColorReferencia() != null ? domain.getColorReferencia().getIdColor() : null)
                .colorDescripcion(domain.getColorReferencia() != null ? domain.getColorReferencia().getDescripcionColor() : null)
                .build();
    }

    private AtributoAccesorioValor valorToDomainFromDTO(AtributoAccesorioValorDTO dto) {
        AtributoAccesorioDefinicion definicion = dto.getIdDefinicion() == null ? null
                : AtributoAccesorioDefinicion.builder().idDefinicion(dto.getIdDefinicion()).build();
        ColorTela colorReferencia = dto.getIdColorReferencia() == null ? null
                : ColorTela.builder().idColor(dto.getIdColorReferencia()).build();
        return AtributoAccesorioValor.builder()
                .idValor(dto.getIdValor())
                .definicion(definicion)
                .valorTexto(dto.getValorTexto())
                .colorReferencia(colorReferencia)
                .build();
    }
}