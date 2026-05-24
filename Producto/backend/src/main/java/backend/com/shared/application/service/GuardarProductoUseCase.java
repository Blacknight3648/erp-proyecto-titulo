package backend.com.shared.application.service;

import backend.com.shared.domain.model.Producto;
import backend.com.shared.infrastructure.persistence.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class GuardarProductoUseCase {

    private final ProductoRepository productoRepository;

    @Transactional
    public Producto ejecutar(Producto producto) {
        if (producto == null) {
            throw new IllegalArgumentException("El producto no puede ser nulo");
        }
        return productoRepository.save(producto);
    }
}
