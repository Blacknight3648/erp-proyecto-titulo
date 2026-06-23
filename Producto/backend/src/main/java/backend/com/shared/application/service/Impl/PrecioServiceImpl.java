package backend.com.shared.application.service.impl;

import backend.com.shared.application.dto.PrecioDTO;
import backend.com.shared.application.service.PrecioService;
import backend.com.shared.domain.model.Articulo;
import backend.com.shared.domain.model.Moneda;
import backend.com.shared.domain.model.Precio;
import backend.com.shared.exception.EntityNotFoundException;
import backend.com.shared.infrastructure.mapper.PrecioMapper;
import backend.com.shared.infrastructure.persistence.repository.ArticuloRepository;
import backend.com.shared.infrastructure.persistence.repository.MonedaRepository;
import backend.com.shared.infrastructure.persistence.repository.PrecioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class PrecioServiceImpl implements PrecioService {

    private final PrecioRepository precioRepository;
    private final ArticuloRepository articuloRepository;
    private final MonedaRepository monedaRepository;
    private final PrecioMapper precioMapper;

    @Override
    public PrecioDTO crear(PrecioDTO dto) {
        Articulo articulo = articuloRepository.findById(dto.getIdArticulo())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Artículo con id " + dto.getIdArticulo() + " no encontrado"));
        Moneda moneda = monedaRepository.findById(dto.getIdMoneda())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Moneda con id " + dto.getIdMoneda() + " no encontrada"));

        Precio nuevo = Precio.builder()
                .articulo(articulo)
                .moneda(moneda)
                .tipoPrecio(dto.getTipoPrecio())
                .valor(dto.getValor())
                .build();
        return precioMapper.toDTO(precioRepository.save(nuevo));
    }

    @Override
    public PrecioDTO actualizar(Integer idPrecio, PrecioDTO dto) {
        Precio existente = findOrThrow(idPrecio);
        Moneda moneda = monedaRepository.findById(dto.getIdMoneda())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Moneda con id " + dto.getIdMoneda() + " no encontrada"));
        existente.setMoneda(moneda);
        existente.setTipoPrecio(dto.getTipoPrecio());
        existente.setValor(dto.getValor());
        return precioMapper.toDTO(precioRepository.save(existente));
    }

    @Override
    @Transactional(readOnly = true)
    public PrecioDTO obtenerPorId(Integer idPrecio) {
        return precioMapper.toDTO(findOrThrow(idPrecio));
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrecioDTO> listarPorArticulo(Integer idArticulo) {
        return precioRepository.findByArticuloId(idArticulo)
                .stream().map(precioMapper::toDTO).toList();
    }

    @Override
    public void eliminar(Integer idPrecio) {
        if (!precioRepository.existsById(idPrecio)) {
            throw new EntityNotFoundException("Precio con id " + idPrecio + " no encontrado");
        }
        precioRepository.deleteById(idPrecio);
    }

    @Override
    public void eliminarPorArticulo(Integer idArticulo) {
        precioRepository.deleteByArticuloId(idArticulo);
    }

    private Precio findOrThrow(Integer id) {
        return precioRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Precio con id " + id + " no encontrado"));
    }
}