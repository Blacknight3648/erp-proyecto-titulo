package backend.com.produccion.application.UseCase;

import backend.com.gestionUsuarios.domain.model.Proveedor;
import backend.com.produccion.domain.model.CosteoVersion;
import backend.com.produccion.domain.model.HojaCompra;
import backend.com.produccion.domain.model.HojaCompraItem;
import backend.com.produccion.domain.model.OrdenProduccion;
import backend.com.produccion.domain.model.OrdenProduccionItem;
import backend.com.produccion.domain.repository.CosteoVersionRepository;
import backend.com.produccion.domain.repository.HojaCompraRepository;
import backend.com.produccion.domain.repository.OrdenProduccionRepository;
import backend.com.produccion.domain.repository.CosteoRepository;
import backend.com.comercial.domain.repository.SolicitudCostosRepository;
import backend.com.shared.infrastructure.persistence.repository.ArticuloRepository;
import backend.com.gestionUsuarios.infrastructure.persistence.repository.ProveedorRepository;
import backend.com.shared.exception.BusinessRuleException;
import backend.com.shared.exception.EntityNotFoundException;
import backend.com.shared.exception.ValidationException;
import backend.com.shared.application.service.NumeroDocumentoService;
import backend.com.shared.valueobjects.DocumentNumber;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class GenerarHCDesdeOPUseCase {

    private final OrdenProduccionRepository ordenProduccionRepository;
    private final CosteoVersionRepository costeoVersionRepository;
    private final HojaCompraRepository hojaCompraRepository;
    private final CosteoRepository costeoRepository;
    private final SolicitudCostosRepository solicitudCostosRepository;
    private final ArticuloRepository articuloRepository;
    private final ProveedorRepository proveedorRepository;
    private final NumeroDocumentoService numeroDocumentoService;

    @Transactional
    public HojaCompra ejecutar(Long opId) {
        if (opId == null) {
            throw new ValidationException("El opId es obligatorio para generar la HC");
        }

        OrdenProduccion op = ordenProduccionRepository.findById(opId)
                .orElseThrow(() -> new EntityNotFoundException("Orden de Producción no encontrada: " + opId));

        if (op.getCosteoVersionId() == null) {
            throw new BusinessRuleException(
                    "La OP " + opId + " no tiene una versión de Costeo vinculada; no se puede generar la HC");
        }

        if (hojaCompraRepository.existsByOpId(opId)) {
            throw new BusinessRuleException(
                    "Ya existe una Hoja de Compra para la OP " + opId);
        }

        CosteoVersion costeoVersion = costeoVersionRepository.findById(op.getCosteoVersionId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Versión de Costeo no encontrada: " + op.getCosteoVersionId()));

        Integer cantidadTotalOP = calcularCantidadTotal(op);

        // La HC genera su propio correlativo independiente, sin heredar el número de la OP.
        DocumentNumber numeroHC = numeroDocumentoService.siguienteFormateado("HC");

        HojaCompra hc = HojaCompra.crearBorrador(numeroHC, opId, op.getCosteoVersionId());

        // Cargar Costeo y SolicitudCostos para resolver proveedores
        java.util.Optional<backend.com.produccion.domain.model.Costeo> costeoOpt = costeoRepository.findById(costeoVersion.getCosteoId());
        java.util.Optional<backend.com.comercial.domain.model.SolicitudCostos> scosOpt = costeoOpt.flatMap(c -> {
            if (c.getSolicitudCostosId() != null) {
                return solicitudCostosRepository.findById(c.getSolicitudCostosId());
            }
            return java.util.Optional.empty();
        });

        if (costeoVersion.getItems() != null) {
            costeoVersion.getItems().forEach(itemVersion -> {
                HojaCompraItem hcItem = HojaCompraItem.desdeCosteoVersionItem(itemVersion, cantidadTotalOP);
                
                Long proveedorId = null;
                String proveedorNombre = null;

                // 1. Intentar resolver por SolicitudCostos (SCOS)
                if (scosOpt.isPresent() && itemVersion.getArticuloId() != null) {
                    backend.com.comercial.domain.model.SolicitudCostos scos = scosOpt.get();
                    if (scos.getTelas() != null) {
                        for (backend.com.comercial.domain.model.SCOSTela t : scos.getTelas()) {
                            if (itemVersion.getArticuloId().equals(t.getIdArticulo())) {
                                proveedorId = t.getProveedorId();
                                break;
                            }
                        }
                    }
                    if (proveedorId == null && scos.getAccesorios() != null) {
                        for (backend.com.comercial.domain.model.SCOSAccesorio a : scos.getAccesorios()) {
                            if (itemVersion.getArticuloId().equals(a.getIdArticulo())) {
                                proveedorId = a.getProveedorId();
                                break;
                            }
                        }
                    }
                }

                // 2. Intentar resolver por Articulo (si SCOS no lo tiene)
                if (proveedorId == null && itemVersion.getArticuloId() != null) {
                    java.util.Optional<backend.com.shared.domain.model.Articulo> artOpt = articuloRepository.findById(itemVersion.getArticuloId());
                    if (artOpt.isPresent()) {
                        backend.com.shared.domain.model.Articulo art = artOpt.get();
                        String providerTextName = null;
                        if (art.getDetallePrenda() != null) {
                            providerTextName = art.getDetallePrenda().getProveedor();
                        } else if (art.getDetalleAccesorio() != null) {
                            providerTextName = art.getDetalleAccesorio().getProveedor();
                        }
                        
                        if (providerTextName != null && !providerTextName.trim().isEmpty()) {
                            List<Proveedor> matchedProvs = proveedorRepository.buscarPorRazonSocial(providerTextName);
                            if (matchedProvs != null && !matchedProvs.isEmpty()) {
                                proveedorId = matchedProvs.get(0).getProveedorId();
                            }
                        }
                    }
                }

                // 3. Obtener el nombre de la razón social del proveedor encontrado
                if (proveedorId != null) {
                    java.util.Optional<Proveedor> provOpt = proveedorRepository.findById(proveedorId);
                    if (provOpt.isPresent()) {
                        proveedorNombre = provOpt.get().getRazonSocialProveedor();
                    }
                }

                hcItem.asignarProveedor(proveedorId, proveedorNombre);
                hc.addItem(hcItem);
            });
        }

        return hojaCompraRepository.save(hc);
    }

    private Integer calcularCantidadTotal(OrdenProduccion op) {
        if (op.getItems() == null)
            return 0;
        return op.getItems().stream()
                .map(OrdenProduccionItem::getCantidad)
                .filter(c -> c != null)
                .mapToInt(Integer::intValue)
                .sum();
    }
}
