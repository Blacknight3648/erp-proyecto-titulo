package backend.com.shared.service;

import backend.com.shared.application.dto.PrecioDTO;
import backend.com.shared.application.service.impl.PrecioServiceImpl;
import backend.com.shared.domain.model.Articulo;
import backend.com.shared.domain.model.Moneda;
import backend.com.shared.domain.model.Precio;
import backend.com.shared.exception.EntityNotFoundException;
import backend.com.shared.infrastructure.mapper.PrecioMapper;
import backend.com.shared.infrastructure.persistence.repository.ArticuloRepository;
import backend.com.shared.infrastructure.persistence.repository.MonedaRepository;
import backend.com.shared.infrastructure.persistence.repository.PrecioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("PrecioServiceImpl")
class PrecioServiceImplTest {

    @Mock
    private PrecioRepository precioRepository;

    @Mock
    private ArticuloRepository articuloRepository;

    @Mock
    private MonedaRepository monedaRepository;

    @Mock
    private PrecioMapper precioMapper;

    @InjectMocks
    private PrecioServiceImpl precioServiceImpl;

    // ---------------- HELPERS ----------------

    private Articulo articulo(Integer id, String codigo) {
        return Articulo.builder()
                .idArticulo(id)
                .codigoArticulo(codigo)
                .build();
    }

    private Moneda moneda(Integer id, String codigo, String simbolo) {
        return Moneda.builder()
                .idMoneda(id)
                .codigoMoneda(codigo)
                .simbolo(simbolo)
                .build();
    }

    private Precio precio(Integer id, Articulo articulo, Moneda moneda, String tipoPrecio, BigDecimal valor) {
        return Precio.builder()
                .idPrecio(id)
                .articulo(articulo)
                .moneda(moneda)
                .tipoPrecio(tipoPrecio)
                .valor(valor)
                .build();
    }

    // ---------------- crear ----------------

    @Test
    @DisplayName("crear guarda correctamente cuando el artículo y la moneda existen")
    void crear_ok() {
        Articulo articuloEntity = articulo(1, "ART-001");
        Moneda monedaEntity = moneda(1, "CLP", "$");
        PrecioDTO dto = PrecioDTO.builder()
                .idArticulo(1)
                .idMoneda(1)
                .tipoPrecio("VENTA")
                .valor(new BigDecimal("1000.00"))
                .build();

        Precio guardado = precio(1, articuloEntity, monedaEntity, "VENTA", new BigDecimal("1000.00"));
        PrecioDTO esperado = PrecioDTO.builder().idPrecio(1).idArticulo(1).idMoneda(1).tipoPrecio("VENTA").valor(new BigDecimal("1000.00")).build();

        when(articuloRepository.findById(1)).thenReturn(Optional.of(articuloEntity));
        when(monedaRepository.findById(1)).thenReturn(Optional.of(monedaEntity));
        when(precioRepository.save(any(Precio.class))).thenReturn(guardado);
        when(precioMapper.toDTO(guardado)).thenReturn(esperado);

        PrecioDTO resultado = precioServiceImpl.crear(dto);

        assertThat(resultado).isEqualTo(esperado);
        verify(precioRepository).save(argThat(p ->
                p.getArticulo() == articuloEntity && p.getMoneda() == monedaEntity
                        && p.getTipoPrecio().equals("VENTA") && p.getValor().equals(new BigDecimal("1000.00"))));
    }

    @Test
    @DisplayName("crear lanza excepción si el artículo no existe")
    void crear_articuloNoExiste() {
        PrecioDTO dto = PrecioDTO.builder()
                .idArticulo(99)
                .idMoneda(1)
                .tipoPrecio("VENTA")
                .valor(new BigDecimal("1000.00"))
                .build();

        when(articuloRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> precioServiceImpl.crear(dto))
                .isInstanceOf(EntityNotFoundException.class);

        verify(precioRepository, never()).save(any());
    }

    @Test
    @DisplayName("crear lanza excepción si la moneda no existe")
    void crear_monedaNoExiste() {
        Articulo articuloEntity = articulo(1, "ART-001");
        PrecioDTO dto = PrecioDTO.builder()
                .idArticulo(1)
                .idMoneda(99)
                .tipoPrecio("VENTA")
                .valor(new BigDecimal("1000.00"))
                .build();

        when(articuloRepository.findById(1)).thenReturn(Optional.of(articuloEntity));
        when(monedaRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> precioServiceImpl.crear(dto))
                .isInstanceOf(EntityNotFoundException.class);

        verify(precioRepository, never()).save(any());
    }

    // ---------------- actualizar ----------------

    @Test
    @DisplayName("actualizar modifica los datos cuando el precio existe")
    void actualizar_ok() {
        Articulo articuloEntity = articulo(1, "ART-001");
        Moneda clp = moneda(1, "CLP", "$");
        Moneda usd = moneda(2, "USD", "US$");
        Precio existente = precio(1, articuloEntity, clp, "VENTA", new BigDecimal("1000.00"));
        PrecioDTO dto = PrecioDTO.builder()
                .idMoneda(2)
                .tipoPrecio("COSTO")
                .valor(new BigDecimal("2000.00"))
                .build();
        PrecioDTO esperado = PrecioDTO.builder().idPrecio(1).idMoneda(2).tipoPrecio("COSTO").valor(new BigDecimal("2000.00")).build();

        when(precioRepository.findById(1)).thenReturn(Optional.of(existente));
        when(monedaRepository.findById(2)).thenReturn(Optional.of(usd));
        when(precioRepository.save(existente)).thenReturn(existente);
        when(precioMapper.toDTO(existente)).thenReturn(esperado);

        PrecioDTO resultado = precioServiceImpl.actualizar(1, dto);

        assertThat(resultado).isEqualTo(esperado);
        assertThat(existente.getMoneda()).isEqualTo(usd);
        assertThat(existente.getTipoPrecio()).isEqualTo("COSTO");
        assertThat(existente.getValor()).isEqualTo(new BigDecimal("2000.00"));
    }

    @Test
    @DisplayName("actualizar lanza excepción si el precio no existe")
    void actualizar_noExiste() {
        PrecioDTO dto = PrecioDTO.builder()
                .idMoneda(1)
                .tipoPrecio("VENTA")
                .valor(new BigDecimal("1000.00"))
                .build();

        when(precioRepository.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> precioServiceImpl.actualizar(1, dto))
                .isInstanceOf(EntityNotFoundException.class);

        verify(precioRepository, never()).save(any());
    }

    @Test
    @DisplayName("actualizar lanza excepción si la moneda no existe")
    void actualizar_monedaNoExiste() {
        Articulo articuloEntity = articulo(1, "ART-001");
        Moneda clp = moneda(1, "CLP", "$");
        Precio existente = precio(1, articuloEntity, clp, "VENTA", new BigDecimal("1000.00"));
        PrecioDTO dto = PrecioDTO.builder()
                .idMoneda(99)
                .tipoPrecio("COSTO")
                .valor(new BigDecimal("2000.00"))
                .build();

        when(precioRepository.findById(1)).thenReturn(Optional.of(existente));
        when(monedaRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> precioServiceImpl.actualizar(1, dto))
                .isInstanceOf(EntityNotFoundException.class);

        verify(precioRepository, never()).save(any());
    }

    // ---------------- obtenerPorId ----------------

    @Test
    @DisplayName("obtenerPorId retorna el DTO cuando el precio existe")
    void obtenerPorId_ok() {
        Articulo articuloEntity = articulo(1, "ART-001");
        Moneda clp = moneda(1, "CLP", "$");
        Precio existente = precio(1, articuloEntity, clp, "VENTA", new BigDecimal("1000.00"));
        PrecioDTO esperado = PrecioDTO.builder().idPrecio(1).idArticulo(1).idMoneda(1).tipoPrecio("VENTA").valor(new BigDecimal("1000.00")).build();

        when(precioRepository.findById(1)).thenReturn(Optional.of(existente));
        when(precioMapper.toDTO(existente)).thenReturn(esperado);

        PrecioDTO resultado = precioServiceImpl.obtenerPorId(1);

        assertThat(resultado).isEqualTo(esperado);
    }

    @Test
    @DisplayName("obtenerPorId lanza excepción si el precio no existe")
    void obtenerPorId_noExiste() {
        when(precioRepository.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> precioServiceImpl.obtenerPorId(1))
                .isInstanceOf(EntityNotFoundException.class);
    }

    // ---------------- listarPorArticulo ----------------

    @Test
    @DisplayName("listarPorArticulo mapea correctamente los precios del artículo")
    void listarPorArticulo_ok() {
        Articulo articuloEntity = articulo(1, "ART-001");
        Moneda clp = moneda(1, "CLP", "$");
        Precio p1 = precio(1, articuloEntity, clp, "VENTA", new BigDecimal("1000.00"));
        PrecioDTO dto1 = PrecioDTO.builder().idPrecio(1).idArticulo(1).idMoneda(1).tipoPrecio("VENTA").valor(new BigDecimal("1000.00")).build();

        when(precioRepository.findByArticuloId(1)).thenReturn(List.of(p1));
        when(precioMapper.toDTO(p1)).thenReturn(dto1);

        List<PrecioDTO> resultado = precioServiceImpl.listarPorArticulo(1);

        assertThat(resultado).containsExactly(dto1);
    }

    // ---------------- eliminar ----------------

    @Test
    @DisplayName("eliminar elimina correctamente cuando el precio existe")
    void eliminar_ok() {
        when(precioRepository.existsById(1)).thenReturn(true);

        precioServiceImpl.eliminar(1);

        verify(precioRepository).deleteById(1);
    }

    @Test
    @DisplayName("eliminar lanza excepción si el precio no existe")
    void eliminar_noExiste() {
        when(precioRepository.existsById(1)).thenReturn(false);

        assertThatThrownBy(() -> precioServiceImpl.eliminar(1))
                .isInstanceOf(EntityNotFoundException.class);

        verify(precioRepository, never()).deleteById(any());
    }

    // ---------------- eliminarPorArticulo ----------------

    @Test
    @DisplayName("eliminarPorArticulo invoca la eliminación de precios por artículo")
    void eliminarPorArticulo_ok() {
        precioServiceImpl.eliminarPorArticulo(1);

        verify(precioRepository).deleteByArticuloId(1);
    }
}
