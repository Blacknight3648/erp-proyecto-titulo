package backend.com.comercial.infrastructure.mapper;

import backend.com.comercial.domain.enums.EstadoEVN;
import backend.com.comercial.domain.model.EvaluacionNegocio;
import backend.com.comercial.domain.model.GastoAdicional;
import backend.com.comercial.domain.model.ItemEVN;
import backend.com.comercial.domain.model.TomaTallaje;
import backend.com.comercial.infrastructure.persistence.entity.EvaluacionNegocioItemJpaEntity;
import backend.com.comercial.infrastructure.persistence.entity.EvaluacionNegocioJpaEntity;
import backend.com.comercial.infrastructure.persistence.entity.GastoAdicionalJpaEntity;
import backend.com.comercial.infrastructure.persistence.entity.TomaTallajeJpaEntity;
import backend.com.gestionUsuarios.cliente.infrastructure.persistence.entity.ClienteJpaEntity;
import backend.com.gestionUsuarios.vendedor.infrastructure.persistence.entity.VendedorJpaEntity;
import backend.com.shared.infrastructure.persistence.entity.ArticuloJpaEntity;
import backend.com.gestionUsuarios.proveedor.infrastructure.persistence.entity.ProveedorJpaEntity;
import backend.com.shared.valueobjects.DocumentNumber;
import backend.com.shared.valueobjects.Money;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EvaluacionNegocioMapper {

    private final com.fasterxml.jackson.databind.ObjectMapper objectMapper = new com.fasterxml.jackson.databind.ObjectMapper();

    public EvaluacionNegocio toDomain(EvaluacionNegocioJpaEntity entity) {
        if (entity == null)
            return null;

        TomaTallaje tomaTallaje = null;
        if (entity.getTomaTallaje() != null) {
            tomaTallaje = new TomaTallaje(
                    new Money(entity.getTomaTallaje().getCostoTotal(), entity.getTomaTallaje().getMoneda()),
                    entity.getTomaTallaje().getObservaciones(),
                    entity.getTomaTallaje().getFechaProgramada(),
                    entity.getTomaTallaje().getMetadataJson());
        }

        List<GastoAdicional> gastosAdicionales = entity.getGastosAdicionales().stream()
                .map(gasto -> new GastoAdicional(
                        GastoAdicional.TipoGastoAdicional.valueOf(gasto.getTipoGasto()),
                        new Money(gasto.getMonto(), gasto.getMoneda()),
                        gasto.getMetadataJson()))
                .collect(Collectors.toList());

        String vNombre = null;
        if (entity.getVendedor() != null && entity.getVendedor().getUsuario() != null) {
            vNombre = entity.getVendedor().getUsuario().getUsuarioNombre() + " " +
                    entity.getVendedor().getUsuario().getUsuarioApellidos();
        }

        return new EvaluacionNegocio(
                entity.getIdEVN(),
                entity.getNumero() != null ? new DocumentNumber(entity.getNumero()) : null,
                entity.getCliente() != null ? entity.getCliente().getClienteId() : null,
                entity.getVendedor() != null ? entity.getVendedor().getIdVendedor() : null,
                EstadoEVN.valueOf(entity.getEstado()),
                entity.getFechaEvaluacion(),
                tomaTallaje,
                gastosAdicionales,
                entity.getItems().stream().map(this::toItemDomain).collect(Collectors.toList()),
                entity.getPorcentajeComision(),
                entity.getClienteNombre(),
                entity.getReferencia(),
                vNombre);
    }

    private ItemEVN toItemDomain(EvaluacionNegocioItemJpaEntity entity) {
        if (entity == null)
            return null;

        java.util.Map<String, String> specs = new java.util.HashMap<>();

        // Cargar desde columnas explícitas primero para asegurar recuperación
        if (entity.getDescripcion() != null)
            specs.put("descripcion", entity.getDescripcion());
        if (entity.getModelo() != null)
            specs.put("modelo", entity.getModelo());
        if (entity.getTela() != null)
            specs.put("tela", entity.getTela());
        if (entity.getComposicion() != null)
            specs.put("composicion", entity.getComposicion());
        if (entity.getGenero() != null)
            specs.put("genero", entity.getGenero());
        if (entity.getCodigoInterno() != null)
            specs.put("codigoInterno", entity.getCodigoInterno());
        if (entity.getCodigoProveedor() != null)
            specs.put("codigoProveedor", entity.getCodigoProveedor());
        if (entity.getProveedorNombre() != null)
            specs.put("proveedor", entity.getProveedorNombre());

        // Mezclar con el JSON para campos adicionales
        if (entity.getTechnicalSpecsJson() != null && !entity.getTechnicalSpecsJson().isBlank()) {
            try {
                java.util.Map<String, String> jsonSpecs = objectMapper.readValue(entity.getTechnicalSpecsJson(),
                        new com.fasterxml.jackson.core.type.TypeReference<java.util.Map<String, String>>() {
                        });
                specs.putAll(jsonSpecs);
            } catch (Exception e) {
                System.err.println(
                        "Error deserializing technical specs for item " + entity.getIdEVNI() + ": " + e.getMessage());
            }
        }

        return new ItemEVN(
                entity.getArticulo() != null ? entity.getArticulo().getIdArticulo() : null,
                entity.getProveedor() != null ? entity.getProveedor().getProveedorId() : null,
                entity.getCantidad(),
                new Money(entity.getPrecioUnitario(), entity.getMonedaPrecioUnitario()),
                new Money(entity.getCostoUnitario(), entity.getMonedaCostoUnitario()),
                entity.getCostoProducto(),
                entity.getCostoLogo(),
                entity.getCostoOrdenTrabajo(),
                entity.getTipoItem(),
                specs,
                entity.getCosteoId(),
                entity.getSolicitudCostosId());
    }

    public EvaluacionNegocioJpaEntity toEntity(EvaluacionNegocio domain) {
        if (domain == null)
            return null;

        EvaluacionNegocioJpaEntity entity = new EvaluacionNegocioJpaEntity();
        if (domain.getEvaluacionNegocioId() != null) {
            entity.setIdEVN(domain.getEvaluacionNegocioId());
        }
        entity.setNumero(domain.getNumeroEvn().getValue());
        entity.setEstado(domain.getEstado().name());
        entity.setFechaEvaluacion(domain.getFechaEvaluacion());
        entity.setClienteNombre(domain.getClienteNombre());
        entity.setReferencia(domain.getReferencia());

        if (domain.getClienteId() != null) {
            ClienteJpaEntity c = new ClienteJpaEntity();
            c.setClienteId(domain.getClienteId());
            entity.setCliente(c);
        }
        if (domain.getVendedorId() != null) {
            VendedorJpaEntity v = new VendedorJpaEntity();
            v.setIdVendedor(domain.getVendedorId());
            entity.setVendedor(v);
        }

        if (domain.getTomaTallaje() != null) {
            TomaTallajeJpaEntity tt = new TomaTallajeJpaEntity();
            tt.setCostoTotal(domain.getTomaTallaje().getCostoTotal().getAmount());
            tt.setMoneda(domain.getTomaTallaje().getCostoTotal().getCurrency());
            tt.setObservaciones(domain.getTomaTallaje().getObservaciones());
            tt.setFechaProgramada(domain.getTomaTallaje().getFechaProgramada());
            tt.setMetadataJson(domain.getTomaTallaje().getMetadataJson());
            entity.setTomaTallajeEntity(tt);
        }

        domain.getGastosAdicionales().forEach(gasto -> {
            GastoAdicionalJpaEntity g = new GastoAdicionalJpaEntity();
            g.setTipoGasto(gasto.getTipoGasto().name());
            g.setMonto(gasto.getMonto().getAmount());
            g.setMoneda(gasto.getMonto().getCurrency());
            g.setMetadataJson(gasto.getMetadataJson());
            entity.addGastoAdicional(g);
        });

        entity.setPorcentajeComision(domain.getPorcentajeComision());

        domain.getItems().forEach(itemDomain -> {
            EvaluacionNegocioItemJpaEntity itemEntity = new EvaluacionNegocioItemJpaEntity();
            if (itemDomain.getArticuloId() != null) {
                ArticuloJpaEntity a = new ArticuloJpaEntity();
                a.setIdArticulo(itemDomain.getArticuloId());
                itemEntity.setArticulo(a);
            }
            if (itemDomain.getProveedorId() != null) {
                ProveedorJpaEntity prov = new ProveedorJpaEntity();
                prov.setProveedorId(itemDomain.getProveedorId());
                itemEntity.setProveedor(prov);
            }
            itemEntity.setCantidad(itemDomain.getCantidad());
            if (itemDomain.getPrecioUnitario() != null) {
                itemEntity.setPrecioUnitario(itemDomain.getPrecioUnitario().getAmount());
                itemEntity.setMonedaPrecioUnitario(itemDomain.getPrecioUnitario().getCurrency());
            }
            if (itemDomain.getCostoUnitario() != null) {
                itemEntity.setCostoUnitario(itemDomain.getCostoUnitario().getAmount());
                itemEntity.setMonedaCostoUnitario(itemDomain.getCostoUnitario().getCurrency());
            }
            itemEntity.setCostoProducto(itemDomain.getCostoProducto());
            itemEntity.setCostoLogo(itemDomain.getCostoLogo());
            itemEntity.setCostoOrdenTrabajo(itemDomain.getCostoOrdenTrabajo());
            itemEntity.setTipoItem(itemDomain.getTipoItem());
            itemEntity.setCosteoId(itemDomain.getCosteoId());
            itemEntity.setSolicitudCostosId(itemDomain.getSolicitudCostosId());

            // Poblar columnas individuales desde el mapa de especificaciones
            if (itemDomain.getTechnicalSpecs() != null) {
                itemEntity.setDescripcion(itemDomain.getTechnicalSpecs().get("descripcion"));
                itemEntity.setModelo(itemDomain.getTechnicalSpecs().get("modelo"));
                itemEntity.setTela(itemDomain.getTechnicalSpecs().get("tela"));
                itemEntity.setComposicion(itemDomain.getTechnicalSpecs().get("composicion"));
                itemEntity.setGenero(itemDomain.getTechnicalSpecs().get("genero"));
                itemEntity.setCodigoInterno(itemDomain.getTechnicalSpecs().get("codigoInterno"));
                itemEntity.setProveedorNombre(itemDomain.getTechnicalSpecs().get("proveedor"));
            }

            // Serializar el mapa de especificaciones a JSON para compatibilidad
            try {
                itemEntity.setTechnicalSpecsJson(objectMapper.writeValueAsString(itemDomain.getTechnicalSpecs()));
            } catch (Exception e) {
                System.err.println("Error serializing specs: " + e.getMessage());
                itemEntity.setTechnicalSpecsJson("{}");
            }

            entity.addItem(itemEntity);
        });

        return entity;
    }

    

    public void updateEntityFromDomain(EvaluacionNegocio domain, EvaluacionNegocioJpaEntity entity) {
        if (domain == null || entity == null)
            return;

        entity.setEstado(domain.getEstado().name());
        entity.setFechaEvaluacion(domain.getFechaEvaluacion());
        entity.setClienteNombre(domain.getClienteNombre());
        entity.setReferencia(domain.getReferencia());
        entity.setPorcentajeComision(domain.getPorcentajeComision());

        // El resto de colecciones (items, gastos) no se tocan para evitar recreación
        // si el dominio no indica cambios en ellas (como es el caso de adjudicar).
    }
}
