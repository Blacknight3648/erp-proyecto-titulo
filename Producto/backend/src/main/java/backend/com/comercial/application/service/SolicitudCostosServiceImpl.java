package backend.com.comercial.application.service;

import backend.com.comercial.domain.model.*;
import backend.com.comercial.domain.repository.SolicitudCostosRepository;
import backend.com.comercial.application.dto.*;
import backend.com.shared.valueobjects.DocumentNumber;
import backend.com.shared.valueobjects.Money;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import backend.com.produccion.application.dto.CosteoDTO;
import backend.com.produccion.application.service.CosteoService;

@Service
@RequiredArgsConstructor
public class SolicitudCostosServiceImpl implements SolicitudCostosService {

        private final SolicitudCostosRepository repository;
        private final CosteoService costeoService;


        @Override
        @Transactional
        public SolicitudCostosDTO create(SolicitudCostosCreateDTO dto) {
                long prefijo = repository.countByTipo("SCOS") + 1;
                String randomNum = String.format("SCOS-" + String.format("%04d", prefijo) );

                SolicitudCostos domain = SolicitudCostos.crear(
                                new DocumentNumber(randomNum),
                                dto.getTipo(),
                                dto.getClienteId(),
                                dto.getVendedorId(),
                                null,
                                dto.getArticuloDescripcion(),
                                dto.getNombrePrenda(),
                                dto.getEsMuestra() != null ? dto.getEsMuestra() : false,
                                dto.getHasLogo() != null ? dto.getHasLogo() : false,
                                dto.getCantidad(),
                                dto.getGenero(),
                                dto.getTallaje());

                mapDetailsToDomain(domain, dto);

                SolicitudCostos saved = repository.save(domain);
                
                // Trigger Pre-costeo
                generatePreCosteo(saved);

                return mapToDTO(saved);
        }

        @Override
        @Transactional
        public SolicitudCostosDTO update(Long id, SolicitudCostosCreateDTO dto) {
                SolicitudCostos existing = repository.findById(id)
                                .orElseThrow(() -> new RuntimeException(
                                                "Solicitud de costos no encontrada con ID: " + id));

                SolicitudCostos updated = new SolicitudCostos(
                                existing.getIdSCOS(),
                                existing.getNumeroSCOS(),
                                dto.getEstado() != null ? dto.getEstado() : existing.getEstado(),
                                dto.getTipo() != null ? dto.getTipo() : existing.getTipo(),
                                dto.getClienteId() != null ? dto.getClienteId() : existing.getClienteId(),
                                null, // clienteNombre (transient for persistence)
                                dto.getVendedorId() != null ? dto.getVendedorId() : existing.getVendedorId(),
                                null, // vendedorNombre (transient for persistence)
                                existing.getEspecificacionTecnicaId(),
                                dto.getArticuloDescripcion() != null ? dto.getArticuloDescripcion() : existing.getArticuloDescripcion(),
                                dto.getNombrePrenda() != null ? dto.getNombrePrenda() : existing.getNombrePrenda(),
                                dto.getEsMuestra() != null ? dto.getEsMuestra() : existing.getEsMuestra(),
                                dto.getHasLogo() != null ? dto.getHasLogo() : existing.getHasLogo(),
                                dto.getCantidad() != null ? dto.getCantidad() : existing.getCantidad(),
                                dto.getGenero() != null ? dto.getGenero() : existing.getGenero(),
                                dto.getTallaje() != null ? dto.getTallaje() : existing.getTallaje(),
                                existing.getFecha(),
                                new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
                                new ArrayList<>(), new ArrayList<>(),
                                dto.getCostoTotal() != null ? dto.getCostoTotal() : existing.getCostoTotal()
                );

                mapDetailsToDomain(updated, dto);

                SolicitudCostos saved = repository.save(updated);
                
                // Trigger Pre-costeo (update)
                generatePreCosteo(saved);

                return mapToDTO(saved);
        }

        @Override
        public Optional<SolicitudCostosDTO> findById(Long id) {
                return repository.findById(id).map(this::mapToDTO);
        }

        @Override
        public List<SolicitudCostosDTO> findAll() {
                return repository.findAll().stream()
                                .map(this::mapToDTO)
                                .collect(Collectors.toList());
        }

        @Override
        @Transactional
        public void delete(Long id) {
                repository.deleteById(id);
        }

        private void mapDetailsToDomain(SolicitudCostos domain, SolicitudCostosCreateDTO dto) {
                if (dto.getTelas() != null) {
                        dto.getTelas().forEach(t -> domain.addTela(new SCOSTela(null, 
                                        t.getAplicacion(),
                                        t.getNombre(), 
                                        null,
                                        t.getProveedorReferencia(),
                                        t.getComposicion(), t.getColor(), t.getPeso(),
                                        t.getConsumo() != null ? t.getConsumo() : java.math.BigDecimal.ZERO,
                                        t.getUnidadMedida() != null ? t.getUnidadMedida() : "mts",
                                        new Money(t.getPrecioUnitario() != null ? t.getPrecioUnitario()
                                                        : java.math.BigDecimal.ZERO, "CLP"),
                                        t.getTempId())));
                }

                if (dto.getAccesorios() != null) {
                        dto.getAccesorios()
                                        .forEach(a -> domain.addAccesorio(new SCOSAccesorio(null, 
                                                        a.getTipo(),
                                                        a.getNombreAccesorio(),
                                                        a.getCantidad(),
                                                        null,
                                                        a.getProveedorReferencia(),
                                                        a.getConsumo() != null ? a.getConsumo()
                                                                        : java.math.BigDecimal.ZERO,
                                                        a.getUnidadMedida() != null ? a.getUnidadMedida() : "un",
                                                        new Money(a.getPrecioUnitario() != null ? a.getPrecioUnitario()
                                                                        : java.math.BigDecimal.ZERO, "CLP"),
                                                        a.getTempId())));
                }

                if (dto.getPlantillas() != null) {
                        dto.getPlantillas().forEach(p -> {
                                List<PlantillaTela> pTelas = (p.getTelas() != null) ? p.getTelas().stream()
                                        .map(t -> new PlantillaTela(t.getAplicacion(), t.getNombre(), t.getComposicion(), t.getColor(), t.getPeso(), t.getUnidadMedida() != null ? t.getUnidadMedida() : "mts"))
                                        .collect(Collectors.toList()) : new ArrayList<>();
                                
                                List<PlantillaAccesorio> pAccesorios = (p.getAccesorios() != null) ? p.getAccesorios().stream()
                                        .map(a -> new PlantillaAccesorio(a.getTipo(), a.getNombreAccesorio(), a.getCantidad()))
                                        .collect(Collectors.toList()) : new ArrayList<>();

                                List<SCOSLogotipo> pLogos = (p.getLogotipos() != null) ? p.getLogotipos().stream()
                                        .map(l -> new SCOSLogotipo(null, l.getTipo(), l.getNombre(), l.getUbicacion(), l.getColor(), l.getTamanio(), l.getCantidad(), l.getPrecio()))
                                        .collect(Collectors.toList()) : new ArrayList<>();



                                List<SCOSPlantillaMaterialVinculo> pVinculos = (p.getVinculos() != null) ? p.getVinculos().stream()
                                        .map(v -> new SCOSPlantillaMaterialVinculo(v.getId(), v.getTempId(), v.getFieldName(), v.getMaterialType(), v.getMaterialId(), v.getTempMaterialId(), v.getCantidad()))
                                        .collect(Collectors.toList()) : new ArrayList<>();

                                domain.addPlantilla(new SCOSPlantilla(
                                        p.getId(), p.getNombre(), p.getDescripcion(), p.getNombrePrenda(),
                                        p.getForro(), p.getRelleno(), p.getGorro(), p.getCuello(),
                                        p.getAbotonaduraCierre(), p.getCortesAplicaciones(), p.getFuelles(), p.getMangas(),
                                        p.getPretinasRuedo(), p.getBolsillos(), p.getCintaDetalle(), p.getLogoDetalle(),
                                        p.getColorForro(), p.getAccesoriosDetalle(), p.getObsModelo(), p.getGenero(),
                                        p.getCustomFields(),
                                        p.getCamposActivos() != null ? new ArrayList<String>(p.getCamposActivos()) : new ArrayList<String>(),
                                        pTelas, pAccesorios, pLogos, pVinculos,
                                        p.getMoPrenda(), p.getMoCosturaSellada(), p.getMoAcolchado()
                                ));
                        });
                }

                if (dto.getPrendas() != null) {
                        dto.getPrendas().forEach(p -> domain.addProducto(
                                        new SCOTPrendaLista(null, p.getNombre(), p.getCantidad(), p.getTalla(),
                                                        p.getColor(),
                                                        p.getProveedorReferencia(), p.getLinkReferencia(),
                                                        p.getComposicion(), p.getPeso(),
                                                        p.getObservaciones(),
                                                        new Money(p.getPrecioUnitario() != null
                                                                        ? new java.math.BigDecimal(
                                                                                        p.getPrecioUnitario())
                                                                        : java.math.BigDecimal.ZERO, "CLP"))));
                }

                if (dto.getLogotipos() != null) {
                        dto.getLogotipos().forEach(
                                        l -> domain.addLogotipo(new SCOSLogotipo(null, l.getTipo(), l.getNombre(),
                                                        l.getUbicacion(), l.getColor(), l.getTamanio(), l.getCantidad(),
                                                        l.getPrecio())));
                }


        }

        private SolicitudCostosDTO mapToDTO(SolicitudCostos domain) {
                return SolicitudCostosDTO.builder()
                                .id(domain.getIdSCOS())
                                .numero(domain.getNumeroSCOS() != null ? domain.getNumeroSCOS().getValue() : null)
                                .estado(domain.getEstado())
                                .tipo(domain.getTipo())
                                .clienteId(domain.getClienteId())
                                .clienteNombre(domain.getClienteNombre())
                                .vendedorId(domain.getVendedorId())
                                .vendedorNombre(domain.getVendedorNombre())
                                .articuloDescripcion(domain.getArticuloDescripcion())
                                .nombrePrenda(domain.getNombrePrenda())
                                .cantidad(domain.getCantidad())
                                .genero(domain.getGenero())
                                .tallaje(domain.getTallaje())
                                .fecha(domain.getFecha())
                                .esMuestra(domain.getEsMuestra())
                                .hasLogo(domain.getHasLogo())
                                .costoTotal(domain.getCostoTotal())
                                .moneda("CLP")
                                .telas(domain.getTelas().stream().map(t -> SCOSTelaDTO.builder()
                                                .aplicacion(t.getAplicacion())
                                                .nombre(t.getDescripcion())
                                                .proveedorReferencia(t.getProveedorReferencia())
                                                .composicion(t.getComposicion())
                                                .color(t.getColor())
                                                .peso(t.getPeso())
                                                .consumo(t.getConsumo())
                                                .unidadMedida(t.getUnidadMedida())
                                                .precioUnitario(t.getPrecioUnitario().getAmount())
                                                .precioTotal(java.math.BigDecimal.ZERO)
                                                .build()).collect(Collectors.toList()))
                                .accesorios(domain.getAccesorios().stream().map(a -> SCOSAccesorioDTO.builder()
                                                .tipo(a.getTipo())
                                                .nombreAccesorio(a.getDescripcion())
                                                .proveedorReferencia(a.getProveedorReferencia())
                                                .cantidad(a.getCantidad())
                                                .consumo(a.getConsumo())
                                                .unidadMedida(a.getUnidadMedida())
                                                .precioUnitario(a.getPrecioUnitario().getAmount())
                                                .precioTotal(java.math.BigDecimal.ZERO)
                                                .build()).collect(Collectors.toList()))
                                .plantillas(domain.getPlantillas().stream().map(p -> SCOSPlantillaDTO.builder()
                                                .id(p.getId())
                                                .nombre(p.getNombre()).descripcion(p.getDescripcion())
                                                .nombrePrenda(p.getNombrePrenda())
                                                .forro(p.getForro()).relleno(p.getRelleno()).gorro(p.getGorro())
                                                .cuello(p.getCuello())
                                                .abotonaduraCierre(p.getAbotonaduraCierre())
                                                .cortesAplicaciones(p.getCortesAplicaciones())
                                                .fuelles(p.getFuelles()).mangas(p.getMangas())
                                                .pretinasRuedo(p.getPretinasRuedo())
                                                .bolsillos(p.getBolsillos()).cintaDetalle(p.getCintaDetalle())
                                                .logoDetalle(p.getLogoDetalle())
                                                .colorForro(p.getColorForro())
                                                .accesoriosDetalle(p.getAccesoriosDetalle())
                                                .obsModelo(p.getObsModelo())
                                                .genero(p.getGenero())
                                                .camposActivos(p.getCamposActivos() != null ? new ArrayList<>(p.getCamposActivos()) : new ArrayList<>())
                                                .telas(p.getPlantillaTelas() != null ? p.getPlantillaTelas().stream()
                                                        .map(t -> PlantillaTelaDTO.builder()
                                                                .aplicacion(t.getAplicacion()).nombre(t.getNombre())
                                                                .composicion(t.getComposicion()).color(t.getColor()).peso(t.getPeso()).build())
                                                        .collect(Collectors.toList()) : new ArrayList<>())
                                                .accesorios(p.getPlantillaAccesorios() != null ? p.getPlantillaAccesorios().stream()
                                                        .map(a -> PlantillaAccesorioDTO.builder()
                                                                .tipo(a.getTipo()).nombreAccesorio(a.getNombreAccesorio()).cantidad(a.getCantidad()).build())
                                                        .collect(Collectors.toList()) : new ArrayList<>())
                                                .logotipos(p.getPlantillaLogotipos() != null ? p.getPlantillaLogotipos().stream()
                                                        .map(l -> new SCOSLogotipoDTO(l.getTipo(), l.getNombre(), l.getUbicacion(), l.getColor(), l.getTamano(), l.getCantidad(), l.getPrecio()))
                                                        .collect(Collectors.toList()) : new ArrayList<>())
                                                .vinculos(p.getVinculos() != null ? p.getVinculos().stream()
                                                        .map(v -> new SCOSPlantillaMaterialVinculoDTO(v.getId(), v.getTempId(), v.getFieldName(), v.getMaterialType(), v.getMaterialId(), v.getTempMaterialId(), v.getCantidad()))
                                                        .collect(Collectors.toList()) : new ArrayList<>())
                                                .moPrenda(p.getMoPrenda())
                                                .moCosturaSellada(p.getMoCosturaSellada())
                                                .moAcolchado(p.getMoAcolchado())
                                                .build()).collect(Collectors.toList()))
                                .prendas(domain.getProductos().stream().map(p -> {
                                        double precioUnitario = p.getPrecioUnitario() != null
                                                        ? p.getPrecioUnitario().getAmount().doubleValue()
                                                        : 0.0;
                                        double cantidad = p.getCantidad() != null ? p.getCantidad().doubleValue() : 0.0;
                                        return new SCOTPrendaListaDTO(
                                                        p.getNombre(), p.getCantidad(), p.getTalla(), p.getColor(),
                                                        p.getProveedorReferencia(),
                                                        p.getLinkReferencia(), p.getComposicion(), p.getPeso(),
                                                        p.getObservaciones(),
                                                        precioUnitario, precioUnitario * cantidad);
                                }).collect(Collectors.toList()))
                                .logotipos(domain.getLogotipos().stream()
                                        .map(l -> new SCOSLogotipoDTO(l.getTipo(), l.getNombre(),
                                                l.getUbicacion(), l.getColor(),
                                                l.getTamano(), l.getCantidad(), l.getPrecio()))
                                        .collect(Collectors.toList()))
                                .costoFijo(null)
                                .build();
        }

        private void generatePreCosteo(SolicitudCostos scos) {
                if (scos.getPlantillas().isEmpty()) return;
                
                // Por ahora tomamos la primera plantilla para el pre-costeo
                SCOSPlantilla p = scos.getPlantillas().get(0);
                
                CosteoDTO costeoDTO = costeoService.findBySolicitudCostosId(scos.getIdSCOS())
                    .orElse(CosteoDTO.builder()
                        .solicitudCostosId(scos.getIdSCOS())
                        .build());
                
                if (costeoDTO.getNumeroCosteo() == null) {
                    costeoDTO.setNumeroCosteo("PRE-" + scos.getNumeroSCOS().getValue());
                }
                
                // Mapear campos de Mano de Obra
                costeoDTO.setCostoManoObra(p.getMoPrenda());
                // (Se pueden expandir otros mapeos aquí según sea necesario)
                
                costeoService.save(costeoDTO);
        }
}
