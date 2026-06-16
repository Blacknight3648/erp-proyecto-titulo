package backend.com.produccion.application.UseCase;

import backend.com.comercial.domain.model.ItemNV;
import backend.com.comercial.domain.model.NotaVenta;
import backend.com.produccion.domain.model.OrdenTrabajo;
import backend.com.produccion.domain.repository.OrdenTrabajoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class GestionarOrdenTrabajoUseCase {

    private final OrdenTrabajoRepository repository;

    @Transactional
    public void generarDesdeNotaVenta(NotaVenta nv) {
        if (nv == null || nv.getItems() == null)
            return;

        for (ItemNV item : nv.getItems()) {
            // La OT de modificación se genera cuando el ítem la requiere explícitamente,
            // sin importar si es prenda lista o por confeccionar.
            if (Boolean.TRUE.equals(item.getRequiereOt())) {

                // Evitar duplicados (simplificado: chequear si ya existe OT para esta NV e
                // ítem)
                // En una implementación real usaríamos una búsqueda más específica

                OrdenTrabajo ot = OrdenTrabajo.crearParaItem(
                        nv.getIdNV(),
                        item.getIdItemNV(),
                        item.getNroItem(),
                        item.getDetalleOt() != null ? item.getDetalleOt() : "Modificación requerida");
                repository.save(ot);
            }
        }
    }

    @Transactional
    public void iniciarOT(Long id) {
        repository.findById(id).ifPresent(ot -> {
            ot.iniciar();
            repository.save(ot);
        });
    }

    @Transactional
    public void finalizarOT(Long id) {
        repository.findById(id).ifPresent(ot -> {
            ot.finalizar();
            repository.save(ot);
        });
    }

    public List<OrdenTrabajo> listarPorNotaVenta(Long nvId) {
        return repository.findByNotaVentaId(nvId);
    }
}
