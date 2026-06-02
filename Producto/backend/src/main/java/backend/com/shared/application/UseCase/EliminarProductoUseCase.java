package backend.com.shared.application.UseCase;

import backend.com.shared.infrastructure.persistence.repository.ProductoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EliminarProductoUseCase {

    private final ProductoRepository productoRepository;

    @Transactional
    public void ejecutar(Long id) {
        if (id != null) {
            productoRepository.deleteById(id);
        }
    }
}
