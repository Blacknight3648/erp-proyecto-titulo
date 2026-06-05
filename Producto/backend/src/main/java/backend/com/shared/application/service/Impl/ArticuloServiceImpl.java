package backend.com.shared.application.service.impl;

import backend.com.shared.application.dto.ArticuloDTO;
import backend.com.shared.application.service.ArticuloService;
import backend.com.shared.domain.enums.TipoArticulo;
import backend.com.shared.domain.model.Articulo;
import backend.com.shared.domain.model.ArticuloAccesorio;
import backend.com.shared.domain.model.ArticuloPrenda;
import backend.com.shared.domain.model.ArticuloTela;
import backend.com.shared.domain.model.Categoria;
import backend.com.shared.domain.model.SubCategoria;
import backend.com.shared.domain.model.UnidadMedida;
import backend.com.shared.exception.DuplicadoException;
import backend.com.shared.exception.EntityNotFoundException;
import backend.com.shared.infrastructure.mapper.ArticuloAccesorioMapper;
import backend.com.shared.infrastructure.mapper.ArticuloMapper;
import backend.com.shared.infrastructure.mapper.ArticuloPrendaMapper;
import backend.com.shared.infrastructure.mapper.ArticuloTelaMapper;
import backend.com.shared.infrastructure.persistence.repository.ArticuloRepository;
import backend.com.shared.infrastructure.persistence.repository.CategoriaRepository;
import backend.com.shared.infrastructure.persistence.repository.SubCategoriaRepository;
import backend.com.shared.infrastructure.persistence.repository.UnidadMedidaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ArticuloServiceImpl implements ArticuloService {

    private final ArticuloRepository articuloRepository;
    private final CategoriaRepository categoriaRepository;
    private final SubCategoriaRepository subCategoriaRepository;
    private final UnidadMedidaRepository unidadMedidaRepository;
    private final ArticuloMapper articuloMapper;
    private final ArticuloTelaMapper articuloTelaMapper;
    private final ArticuloPrendaMapper articuloPrendaMapper;
    private final ArticuloAccesorioMapper articuloAccesorioMapper;

    @Override
    public ArticuloDTO crearArticulo(ArticuloDTO dto) {
        if (articuloRepository.existsByCodigoArticulo(dto.getCodigoArticulo())) {
            throw new DuplicadoException("código de artículo", dto.getCodigoArticulo());
        }

        Articulo articulo = Articulo.builder()
                .codigoArticulo(dto.getCodigoArticulo())
                .nombreArticulo(dto.getNombreArticulo())
                .descripcionArticulo(dto.getDescripcionArticulo())
                .codigoBarra(dto.getCodigoBarra())
                .tipoArticulo(dto.getTipoArticulo())
                .procedencia(dto.getProcedencia())
                .lotes(dto.getLotes() != null ? dto.getLotes() : false)
                .seriales(dto.getSeriales() != null ? dto.getSeriales() : false)
                .compras(dto.getCompras() != null ? dto.getCompras() : true)
                .comision(dto.getComision())
                .activo(true)
                .categoria(resolverCategoria(dto.getIdCategoria()))
                .subCategoria(resolverSubCategoria(dto.getIdSubCategoria()))
                .unidadMedida(resolverUnidadMedida(dto.getIdUnidadMedida()))
                .build();

        asignarDetalle(articulo, dto);
        return articuloMapper.toDTO(articuloRepository.save(articulo));
    }

    @Override
    public ArticuloDTO actualizarArticulo(Integer idArticulo, ArticuloDTO dto) {
        Articulo articulo = findOrThrow(idArticulo);

        articulo.setNombreArticulo(dto.getNombreArticulo());
        articulo.setDescripcionArticulo(dto.getDescripcionArticulo());
        articulo.setCodigoBarra(dto.getCodigoBarra());
        articulo.setTipoArticulo(dto.getTipoArticulo());
        articulo.setProcedencia(dto.getProcedencia());
        if (dto.getLotes() != null) articulo.setLotes(dto.getLotes());
        if (dto.getSeriales() != null) articulo.setSeriales(dto.getSeriales());
        if (dto.getCompras() != null) articulo.setCompras(dto.getCompras());
        articulo.setComision(dto.getComision());
        if (dto.getActivo() != null) articulo.setActivo(dto.getActivo());
        articulo.setCategoria(resolverCategoria(dto.getIdCategoria()));
        articulo.setSubCategoria(resolverSubCategoria(dto.getIdSubCategoria()));
        articulo.setUnidadMedida(resolverUnidadMedida(dto.getIdUnidadMedida()));

        limpiarDetallesAnteriores(articulo, dto.getTipoArticulo());
        asignarDetalle(articulo, dto);

        return articuloMapper.toDTO(articuloRepository.save(articulo));
    }

    @Override
    @Transactional(readOnly = true)
    public ArticuloDTO obtenerPorId(Integer idArticulo) {
        return articuloMapper.toDTO(findOrThrow(idArticulo));
    }

    @Override
    @Transactional(readOnly = true)
    public ArticuloDTO obtenerPorCodigo(String codigoArticulo) {
        return articuloMapper.toDTO(articuloRepository.findByCodigoArticulo(codigoArticulo)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Articulo con código " + codigoArticulo + " no encontrado")));
    }

    @Override
    @Transactional(readOnly = true)
    public List<ArticuloDTO> listarTodos() {
        return articuloRepository.findAll().stream().map(articuloMapper::toDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ArticuloDTO> listarActivos() {
        return articuloRepository.findByActivoTrue().stream().map(articuloMapper::toDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ArticuloDTO> listarPorTipo(TipoArticulo tipoArticulo) {
        return articuloRepository.findByTipoArticulo(tipoArticulo).stream().map(articuloMapper::toDTO).toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<ArticuloDTO> buscarPorNombre(String nombre) {
        return articuloRepository.findByNombreArticuloContainingIgnoreCase(nombre)
                .stream().map(articuloMapper::toDTO).toList();
    }

    @Override
    public void eliminarArticulo(Integer idArticulo) {
        Articulo articulo = findOrThrow(idArticulo);
        articulo.setActivo(false);
        articuloRepository.save(articulo);
    }

    // ── helpers privados ─────────────────────────────────────────────────────

    private Articulo findOrThrow(Integer id) {
        return articuloRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Articulo con id " + id + " no encontrado"));
    }

    private Categoria resolverCategoria(Integer id) {
        if (id == null) return null;
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Categoría con id " + id + " no encontrada"));
    }

    private SubCategoria resolverSubCategoria(Integer id) {
        if (id == null) return null;
        return subCategoriaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("SubCategoría con id " + id + " no encontrada"));
    }

    private UnidadMedida resolverUnidadMedida(Integer id) {
        if (id == null) return null;
        return unidadMedidaRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Unidad de medida con id " + id + " no encontrada"));
    }

    private void asignarDetalle(Articulo articulo, ArticuloDTO dto) {
        TipoArticulo tipo = dto.getTipoArticulo();
        if (tipo == TipoArticulo.TELA && dto.getDetalleTela() != null) {
            ArticuloTela tela = articuloTelaMapper.toDomain(dto.getDetalleTela());
            articulo.setDetalleTela(tela);
        } else if (tipo == TipoArticulo.PRENDA_LISTA && dto.getDetallePrenda() != null) {
            ArticuloPrenda prenda = articuloPrendaMapper.toDomain(dto.getDetallePrenda());
            articulo.setDetallePrenda(prenda);
        } else if (tipo == TipoArticulo.ACCESORIO && dto.getDetalleAccesorio() != null) {
            ArticuloAccesorio accesorio = articuloAccesorioMapper.toDomain(dto.getDetalleAccesorio());
            articulo.setDetalleAccesorio(accesorio);
        }
    }

    private void limpiarDetallesAnteriores(Articulo articulo, TipoArticulo nuevoTipo) {
        if (nuevoTipo != TipoArticulo.TELA) articulo.setDetalleTela(null);
        if (nuevoTipo != TipoArticulo.PRENDA_LISTA) articulo.setDetallePrenda(null);
        if (nuevoTipo != TipoArticulo.ACCESORIO) articulo.setDetalleAccesorio(null);
    }
}