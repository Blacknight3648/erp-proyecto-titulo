package backend.com.maestros.service.impl;

import backend.com.maestros.domain.entity.*;
import backend.com.maestros.domain.enums.TipoArticulo;
import backend.com.maestros.dto.articuloDto.ArticuloCreateRequestDTO;
import backend.com.maestros.dto.articuloDto.ArticuloUpdateRequestDTO;
import backend.com.maestros.exception.DuplicadoException;
import backend.com.maestros.exception.RecursoNoEncontradoException;
import backend.com.maestros.repository.*;
import backend.com.maestros.service.ArticuloService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ArticuloServiceImpl implements ArticuloService {

    private final ArticuloRepository      articuloRepository;
    private final CategoriaRepository     categoriaRepository;
    private final SubCategoriaRepository  subCategoriaRepository;
    private final UnidadMedidaRepository  unidadMedidaRepository;

    @Override
    public Articulo crearArticulo(ArticuloCreateRequestDTO request) {
        if (articuloRepository.existsByCodigoArticulo(request.getCodigoArticulo())) {
            throw new DuplicadoException("código de artículo", request.getCodigoArticulo());
        }

        Categoria    categoria    = resolverCategoria(request.getIdCategoria());
        SubCategoria subCategoria = resolverSubCategoria(request.getIdSubCategoria());
        UnidadMedida unidadMedida = resolverUnidadMedida(request.getIdUnidadMedida());

        Articulo articulo = Articulo.builder()
                .codigoArticulo(request.getCodigoArticulo())
                .nombreArticulo(request.getNombreArticulo())
                .descripcionArticulo(request.getDescripcionArticulo())
                .codigoBarra(request.getCodigoBarra())
                .tipoArticulo(request.getTipoArticulo())
                .procedencia(request.getProcedencia())
                .lotes(request.getLotes()    != null ? request.getLotes()    : false)
                .seriales(request.getSeriales() != null ? request.getSeriales() : false)
                .compras(request.getCompras()  != null ? request.getCompras()  : true)
                .comision(request.getComision())
                .activo(true)
                .categoria(categoria)
                .subCategoria(subCategoria)
                .unidadMedida(unidadMedida)
                .build();

        asignarDetalle(articulo, request.getTipoArticulo(),
                request.getDetalleTela(), request.getDetallePrenda(), request.getDetalleAccesorio());

        return articuloRepository.save(articulo);
    }

    @Override
    public Articulo actualizarArticulo(Integer idArticulo, ArticuloUpdateRequestDTO request) {
        Articulo     articulo     = obtenerPorId(idArticulo);
        Categoria    categoria    = resolverCategoria(request.getIdCategoria());
        SubCategoria subCategoria = resolverSubCategoria(request.getIdSubCategoria());
        UnidadMedida unidadMedida = resolverUnidadMedida(request.getIdUnidadMedida());

        articulo.setNombreArticulo(request.getNombreArticulo());
        articulo.setDescripcionArticulo(request.getDescripcionArticulo());
        articulo.setCodigoBarra(request.getCodigoBarra());
        articulo.setTipoArticulo(request.getTipoArticulo());
        articulo.setProcedencia(request.getProcedencia());
        if (request.getLotes()    != null) articulo.setLotes(request.getLotes());
        if (request.getSeriales() != null) articulo.setSeriales(request.getSeriales());
        if (request.getCompras()  != null) articulo.setCompras(request.getCompras());
        articulo.setComision(request.getComision());
        if (request.getActivo()   != null) articulo.setActivo(request.getActivo());
        articulo.setCategoria(categoria);
        articulo.setSubCategoria(subCategoria);
        articulo.setUnidadMedida(unidadMedida);

        limpiarDetallesAnteriores(articulo, request.getTipoArticulo());
        asignarDetalle(articulo, request.getTipoArticulo(),
                request.getDetalleTela(), request.getDetallePrenda(), request.getDetalleAccesorio());

        return articuloRepository.save(articulo);
    }

    @Override
    @Transactional(readOnly = true)
    public Articulo obtenerPorId(Integer idArticulo) {
        return articuloRepository.findById(idArticulo)
                .orElseThrow(() -> new RecursoNoEncontradoException("Articulo", idArticulo));
    }

    @Override
    @Transactional(readOnly = true)
    public Articulo obtenerPorCodigo(String codigoArticulo) {
        return articuloRepository.findByCodigoArticulo(codigoArticulo)
                .orElseThrow(() -> new RecursoNoEncontradoException("Articulo con código", codigoArticulo));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Articulo> listarTodos() {
        return articuloRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Articulo> listarActivos() {
        return articuloRepository.findByActivoTrue();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Articulo> listarPorTipo(TipoArticulo tipoArticulo) {
        return articuloRepository.findByTipoArticulo(tipoArticulo);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Articulo> buscarPorNombre(String nombre) {
        return articuloRepository.findByNombreArticuloContainingIgnoreCase(nombre);
    }

    @Override
    public void eliminarArticulo(Integer idArticulo) {
        Articulo articulo = obtenerPorId(idArticulo);
        articulo.setActivo(false);
        articuloRepository.save(articulo);
    }

    // ── helpers privados ─────────────────────────────────────────────────────

    private Categoria resolverCategoria(Integer id) {
        if (id == null) return null;
        return categoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Categoría", id));
    }

    private SubCategoria resolverSubCategoria(Integer id) {
        if (id == null) return null;
        return subCategoriaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("SubCategoría", id));
    }

    private UnidadMedida resolverUnidadMedida(Integer id) {
        if (id == null) return null;
        return unidadMedidaRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Unidad de medida", id));
    }

    private void asignarDetalle(Articulo articulo, TipoArticulo tipo,
                                ArticuloTela tela, ArticuloPrenda prenda, ArticuloAccesorio accesorio) {
        if (tipo == TipoArticulo.TELA && tela != null) {
            tela.setArticulo(articulo);
            articulo.setDetalleTela(tela);
        } else if (tipo == TipoArticulo.PRENDA_LISTA && prenda != null) {
            prenda.setArticulo(articulo);
            articulo.setDetallePrenda(prenda);
        } else if (tipo == TipoArticulo.ACCESORIO && accesorio != null) {
            accesorio.setArticulo(articulo);
            articulo.setDetalleAccesorio(accesorio);
        }
    }

    private void limpiarDetallesAnteriores(Articulo articulo, TipoArticulo nuevoTipo) {
        if (nuevoTipo != TipoArticulo.TELA)          articulo.setDetalleTela(null);
        if (nuevoTipo != TipoArticulo.PRENDA_LISTA)  articulo.setDetallePrenda(null);
        if (nuevoTipo != TipoArticulo.ACCESORIO)     articulo.setDetalleAccesorio(null);
    }
}
