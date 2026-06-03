package backend.com.maestros.service.impl;

import backend.com.maestros.domain.entity.Articulo;
import backend.com.maestros.domain.entity.Moneda;
import backend.com.maestros.domain.entity.Precio;
import backend.com.maestros.dto.request.PrecioRequestDTO;
import backend.com.maestros.dto.response.PrecioResponseDTO;
import backend.com.maestros.exception.RecursoNoEncontradoException;
import backend.com.maestros.repository.ArticuloRepository;
import backend.com.maestros.repository.MonedaRepository;
import backend.com.maestros.repository.PrecioRepository;
import backend.com.maestros.service.PrecioService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PrecioServiceImpl implements PrecioService {

    private final PrecioRepository    precioRepository;
    private final ArticuloRepository  articuloRepository;
    private final MonedaRepository    monedaRepository;

    @Override
    public PrecioResponseDTO crear(PrecioRequestDTO request) {
        Articulo articulo = articuloRepository.findById(request.getIdArticulo())
                .orElseThrow(() -> new RecursoNoEncontradoException("Artículo", request.getIdArticulo()));
        Moneda moneda = monedaRepository.findById(request.getIdMoneda())
                .orElseThrow(() -> new RecursoNoEncontradoException("Moneda", request.getIdMoneda()));

        Precio saved = precioRepository.save(
                Precio.builder()
                        .articulo(articulo)
                        .moneda(moneda)
                        .tipoPrecio(request.getTipoPrecio())
                        .valor(request.getValor())
                        .build());
        return toResponse(saved);
    }

    @Override
    public PrecioResponseDTO actualizar(Integer idPrecio, PrecioRequestDTO request) {
        Precio precio = findOrThrow(idPrecio);
        Moneda moneda = monedaRepository.findById(request.getIdMoneda())
                .orElseThrow(() -> new RecursoNoEncontradoException("Moneda", request.getIdMoneda()));
        precio.setMoneda(moneda);
        precio.setTipoPrecio(request.getTipoPrecio());
        precio.setValor(request.getValor());
        return toResponse(precioRepository.save(precio));
    }

    @Override
    @Transactional(readOnly = true)
    public PrecioResponseDTO obtenerPorId(Integer idPrecio) {
        return toResponse(findOrThrow(idPrecio));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrecioResponseDTO> listarPorArticulo(Integer idArticulo) {
        return precioRepository.findByArticulo_IdArticulo(idArticulo)
                .stream().map(this::toResponse).toList();
    }

    @Override
    public void eliminar(Integer idPrecio) {
        findOrThrow(idPrecio);
        precioRepository.deleteById(idPrecio);
    }

    @Override
    public void eliminarPorArticulo(Integer idArticulo) {
        precioRepository.deleteByArticulo_IdArticulo(idArticulo);
    }

    // ── helpers ──────────────────────────────────────────────

    private Precio findOrThrow(Integer id) {
        return precioRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Precio", id));
    }

    private PrecioResponseDTO toResponse(Precio p) {
        return PrecioResponseDTO.builder()
                .idPrecio(p.getIdPrecio())
                .idArticulo(p.getArticulo().getIdArticulo())
                .codigoArticulo(p.getArticulo().getCodigoArticulo())
                .idMoneda(p.getMoneda().getIdMoneda())
                .codigoMoneda(p.getMoneda().getCodigoMoneda())
                .simboloMoneda(p.getMoneda().getSimbolo())
                .tipoPrecio(p.getTipoPrecio())
                .valor(p.getValor())
                .build();
    }
}
