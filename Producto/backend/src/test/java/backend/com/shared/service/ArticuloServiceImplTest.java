package backend.com.shared.service;

import backend.com.shared.application.dto.ArticuloAccesorioDTO;
import backend.com.shared.application.dto.ArticuloDTO;
import backend.com.shared.application.dto.ArticuloPrendaDTO;
import backend.com.shared.application.dto.ArticuloTelaDTO;
import backend.com.shared.application.service.CodigoGeneratorService;
import backend.com.shared.application.service.impl.ArticuloServiceImpl;
import backend.com.shared.domain.enums.TipoArticulo;
import backend.com.shared.domain.model.Articulo;
import backend.com.shared.domain.model.ArticuloAccesorio;
import backend.com.shared.domain.model.ArticuloPrenda;
import backend.com.shared.domain.model.ArticuloTela;
import backend.com.shared.domain.model.CategoriaTela;
import backend.com.shared.domain.model.SubCategoriaTela;
import backend.com.shared.exception.EntityNotFoundException;
import backend.com.shared.infrastructure.mapper.ArticuloAccesorioMapper;
import backend.com.shared.infrastructure.mapper.ArticuloMapper;
import backend.com.shared.infrastructure.mapper.ArticuloPrendaMapper;
import backend.com.shared.infrastructure.mapper.ArticuloTelaMapper;
import backend.com.shared.infrastructure.persistence.repository.ArticuloRepository;
import backend.com.shared.infrastructure.persistence.repository.CategoriaTelaRepository;
import backend.com.shared.infrastructure.persistence.repository.FamiliaTelaRepository;
import backend.com.shared.infrastructure.persistence.repository.SubCategoriaTelaRepository;
import backend.com.shared.infrastructure.persistence.repository.TipoAccesorioRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("ArticuloServiceImpl")
class ArticuloServiceImplTest {

    @Mock
    private ArticuloRepository articuloRepository;

    @Mock
    private CategoriaTelaRepository categoriaTelaRepository;

    @Mock
    private SubCategoriaTelaRepository subCategoriaTelaRepository;

    @Mock
    private FamiliaTelaRepository familiaTelaRepository;

    @Mock
    private TipoAccesorioRepository tipoAccesorioRepository;

    @Mock
    private ArticuloMapper articuloMapper;

    @Mock
    private ArticuloTelaMapper articuloTelaMapper;

    @Mock
    private ArticuloPrendaMapper articuloPrendaMapper;

    @Mock
    private ArticuloAccesorioMapper articuloAccesorioMapper;

    @Mock
    private CodigoGeneratorService codigoGeneratorService;

    @InjectMocks
    private ArticuloServiceImpl articuloServiceImpl;

    // ---------------- HELPERS ----------------

    private CategoriaTela categoriaTela(Integer id, String nombre) {
        return CategoriaTela.builder()
                .idCategoriaTela(id)
                .nombreCategoriaTela(nombre)
                .build();
    }

    private SubCategoriaTela subCategoriaTela(Integer id, String nombre) {
        return SubCategoriaTela.builder()
                .idSubCategoriaTela(id)
                .nombreSubCategoriaTela(nombre)
                .build();
    }

    private Articulo articulo(Integer id, String codigo, String nombre, TipoArticulo tipo, Boolean activo, CategoriaTela categoriaTela, SubCategoriaTela subCategoriaTela) {
        return Articulo.builder()
                .idArticulo(id)
                .codigoArticulo(codigo)
                .nombreArticulo(nombre)
                .tipoArticulo(tipo)
                .activo(activo)
                .categoriaTela(categoriaTela)
                .subCategoriaTela(subCategoriaTela)
                .build();
    }

    // ---------------- crearArticulo ----------------

    @Test
    @DisplayName("crearArticulo guarda correctamente cuando el código no está duplicado")
    void crear_ok() {
        CategoriaTela categoria = categoriaTela(1, "Prendas");
        ArticuloPrendaDTO detallePrendaDTO = ArticuloPrendaDTO.builder().build();
        ArticuloPrenda detallePrenda = ArticuloPrenda.builder().build();
        ArticuloDTO dto = ArticuloDTO.builder()
                .codigoArticulo("ART-001")
                .nombreArticulo("Polera")
                .tipoArticulo(TipoArticulo.PRENDA_LISTA)
                .idCategoriaTela(1)
                .detallePrenda(detallePrendaDTO)
                .build();

        Articulo guardado = articulo(1, "ART-001", "Polera", TipoArticulo.PRENDA_LISTA, true, categoria, null);
        ArticuloDTO esperado = ArticuloDTO.builder().idArticulo(1).codigoArticulo("ART-001").build();

        when(categoriaTelaRepository.findById(1)).thenReturn(Optional.of(categoria));
        when(articuloPrendaMapper.toDomain(detallePrendaDTO)).thenReturn(detallePrenda);
        when(codigoGeneratorService.siguienteCorrelativo(eq("PRD"), any())).thenReturn("ART-001");
        when(articuloRepository.save(any(Articulo.class))).thenReturn(guardado);
        when(articuloMapper.toDTO(guardado)).thenReturn(esperado);

        ArticuloDTO resultado = articuloServiceImpl.crearArticulo(dto);

        assertThat(resultado).isEqualTo(esperado);
        verify(articuloRepository).save(argThat(a ->
                a.getCodigoArticulo().equals("ART-001")
                        && Boolean.TRUE.equals(a.getActivo())
                        && a.getCategoriaTela() == categoria
                        && a.getSubCategoriaTela() == null
                        && a.getDetallePrenda() == detallePrenda));
        verify(subCategoriaTelaRepository, never()).findById(any());
    }

    @Test
    @DisplayName("crearArticulo resuelve la subcategoría y el detalle de tela cuando se especifican")
    void crear_conSubCategoriaYDetalleTela() {
        CategoriaTela categoria = categoriaTela(1, "Telas");
        SubCategoriaTela subCategoria = subCategoriaTela(2, "Algodón");
        ArticuloTelaDTO detalleTelaDTO = ArticuloTelaDTO.builder().build();
        ArticuloTela detalleTela = ArticuloTela.builder().build();
        ArticuloDTO dto = ArticuloDTO.builder()
                .codigoArticulo("ART-002")
                .nombreArticulo("Tela algodón")
                .tipoArticulo(TipoArticulo.TELA)
                .idCategoriaTela(1)
                .idSubCategoriaTela(2)
                .detalleTela(detalleTelaDTO)
                .build();

        Articulo guardado = articulo(2, "ART-002", "Tela algodón", TipoArticulo.TELA, true, categoria, subCategoria);
        ArticuloDTO esperado = ArticuloDTO.builder().idArticulo(2).codigoArticulo("ART-002").build();

        when(categoriaTelaRepository.findById(1)).thenReturn(Optional.of(categoria));
        when(subCategoriaTelaRepository.findById(2)).thenReturn(Optional.of(subCategoria));
        when(articuloTelaMapper.toDomain(detalleTelaDTO)).thenReturn(detalleTela);
        when(codigoGeneratorService.siguienteCorrelativo(eq("TEL"), any())).thenReturn("ART-002");
        when(articuloRepository.save(any(Articulo.class))).thenReturn(guardado);
        when(articuloMapper.toDTO(guardado)).thenReturn(esperado);

        ArticuloDTO resultado = articuloServiceImpl.crearArticulo(dto);

        assertThat(resultado).isEqualTo(esperado);
        verify(articuloRepository).save(argThat(a ->
                a.getSubCategoriaTela() == subCategoria && a.getDetalleTela() == detalleTela));
    }

    @Test
    @DisplayName("crearArticulo lanza excepción si la categoría de tela no existe")
    void crear_categoriaNoExiste() {
        ArticuloDTO dto = ArticuloDTO.builder()
                .codigoArticulo("ART-001")
                .nombreArticulo("Polera")
                .tipoArticulo(TipoArticulo.PRENDA_LISTA)
                .idCategoriaTela(99)
                .build();

        when(categoriaTelaRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> articuloServiceImpl.crearArticulo(dto))
                .isInstanceOf(EntityNotFoundException.class);

        verify(articuloRepository, never()).save(any());
    }

    @Test
    @DisplayName("crearArticulo lanza excepción si la subcategoría de tela no existe")
    void crear_subCategoriaNoExiste() {
        CategoriaTela categoria = categoriaTela(1, "Telas");
        ArticuloDTO dto = ArticuloDTO.builder()
                .codigoArticulo("ART-001")
                .nombreArticulo("Polera")
                .tipoArticulo(TipoArticulo.PRENDA_LISTA)
                .idCategoriaTela(1)
                .idSubCategoriaTela(99)
                .build();

        when(categoriaTelaRepository.findById(1)).thenReturn(Optional.of(categoria));
        when(subCategoriaTelaRepository.findById(99)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> articuloServiceImpl.crearArticulo(dto))
                .isInstanceOf(EntityNotFoundException.class);

        verify(articuloRepository, never()).save(any());
    }

    @Test
    @DisplayName("crearArticulo asigna el detalle de accesorio cuando el tipo es ACCESORIO")
    void crear_tipoAccesorio() {
        CategoriaTela categoria = categoriaTela(1, "Accesorios");
        ArticuloAccesorioDTO detalleAccesorioDTO = ArticuloAccesorioDTO.builder().build();
        ArticuloAccesorio detalleAccesorio = ArticuloAccesorio.builder().build();
        ArticuloDTO dto = ArticuloDTO.builder()
                .codigoArticulo("ART-003")
                .nombreArticulo("Botón")
                .tipoArticulo(TipoArticulo.ACCESORIO)
                .idCategoriaTela(1)
                .detalleAccesorio(detalleAccesorioDTO)
                .build();

        Articulo guardado = articulo(3, "ART-003", "Botón", TipoArticulo.ACCESORIO, true, categoria, null);
        ArticuloDTO esperado = ArticuloDTO.builder().idArticulo(3).codigoArticulo("ART-003").build();

        when(categoriaTelaRepository.findById(1)).thenReturn(Optional.of(categoria));
        when(articuloAccesorioMapper.toDomain(detalleAccesorioDTO)).thenReturn(detalleAccesorio);
        when(codigoGeneratorService.siguienteCorrelativo(eq("ACC"), any())).thenReturn("ART-003");
        when(articuloRepository.save(any(Articulo.class))).thenReturn(guardado);
        when(articuloMapper.toDTO(guardado)).thenReturn(esperado);

        ArticuloDTO resultado = articuloServiceImpl.crearArticulo(dto);

        assertThat(resultado).isEqualTo(esperado);
        verify(articuloRepository).save(argThat(a -> a.getDetalleAccesorio() == detalleAccesorio));
    }

    // ---------------- actualizarArticulo ----------------

    @Test
    @DisplayName("actualizarArticulo modifica los datos y limpia los detalles del tipo anterior")
    void actualizar_ok() {
        CategoriaTela categoriaAntigua = categoriaTela(1, "Telas");
        CategoriaTela categoriaNueva = categoriaTela(2, "Accesorios");
        Articulo existente = articulo(1, "ART-001", "Polera", TipoArticulo.TELA, true, categoriaAntigua, null);
        existente.setDetalleTela(ArticuloTela.builder().build());

        ArticuloAccesorioDTO detalleAccesorioDTO = ArticuloAccesorioDTO.builder().build();
        ArticuloAccesorio detalleAccesorio = ArticuloAccesorio.builder().build();
        ArticuloDTO dto = ArticuloDTO.builder()
                .nombreArticulo("Botón metálico")
                .descripcionArticulo("Botón de metal")
                .codigoBarra("123456789")
                .tipoArticulo(TipoArticulo.ACCESORIO)
                .activo(false)
                .idCategoriaTela(2)
                .detalleAccesorio(detalleAccesorioDTO)
                .build();
        ArticuloDTO esperado = ArticuloDTO.builder().idArticulo(1).nombreArticulo("Botón metálico").build();

        when(articuloRepository.findById(1)).thenReturn(Optional.of(existente));
        when(categoriaTelaRepository.findById(2)).thenReturn(Optional.of(categoriaNueva));
        when(articuloAccesorioMapper.toDomain(detalleAccesorioDTO)).thenReturn(detalleAccesorio);
        when(articuloRepository.save(existente)).thenReturn(existente);
        when(articuloMapper.toDTO(existente)).thenReturn(esperado);

        ArticuloDTO resultado = articuloServiceImpl.actualizarArticulo(1, dto);

        assertThat(resultado).isEqualTo(esperado);
        assertThat(existente.getNombreArticulo()).isEqualTo("Botón metálico");
        assertThat(existente.getDescripcionArticulo()).isEqualTo("Botón de metal");
        assertThat(existente.getCodigoBarra()).isEqualTo("123456789");
        assertThat(existente.getTipoArticulo()).isEqualTo(TipoArticulo.ACCESORIO);
        assertThat(existente.getActivo()).isFalse();
        assertThat(existente.getCategoriaTela()).isEqualTo(categoriaNueva);
        assertThat(existente.getSubCategoriaTela()).isNull();
        assertThat(existente.getDetalleTela()).isNull();
        assertThat(existente.getDetalleAccesorio()).isEqualTo(detalleAccesorio);
    }

    @Test
    @DisplayName("actualizarArticulo no modifica el estado activo si no se especifica")
    void actualizar_activoNulo() {
        CategoriaTela categoria = categoriaTela(1, "Prendas");
        Articulo existente = articulo(1, "ART-001", "Polera", TipoArticulo.PRENDA_LISTA, true, categoria, null);
        ArticuloDTO dto = ArticuloDTO.builder()
                .nombreArticulo("Polera actualizada")
                .tipoArticulo(TipoArticulo.PRENDA_LISTA)
                .idCategoriaTela(1)
                .build();
        ArticuloDTO esperado = ArticuloDTO.builder().idArticulo(1).nombreArticulo("Polera actualizada").build();

        when(articuloRepository.findById(1)).thenReturn(Optional.of(existente));
        when(categoriaTelaRepository.findById(1)).thenReturn(Optional.of(categoria));
        when(articuloRepository.save(existente)).thenReturn(existente);
        when(articuloMapper.toDTO(existente)).thenReturn(esperado);

        ArticuloDTO resultado = articuloServiceImpl.actualizarArticulo(1, dto);

        assertThat(resultado).isEqualTo(esperado);
        assertThat(existente.getActivo()).isTrue();
    }

    @Test
    @DisplayName("actualizarArticulo lanza excepción si el artículo no existe")
    void actualizar_noExiste() {
        ArticuloDTO dto = ArticuloDTO.builder()
                .nombreArticulo("Polera")
                .tipoArticulo(TipoArticulo.PRENDA_LISTA)
                .idCategoriaTela(1)
                .build();

        when(articuloRepository.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> articuloServiceImpl.actualizarArticulo(1, dto))
                .isInstanceOf(EntityNotFoundException.class);

        verify(articuloRepository, never()).save(any());
    }

    // ---------------- obtenerPorId ----------------

    @Test
    @DisplayName("obtenerPorId retorna el DTO cuando el artículo existe")
    void obtenerPorId_ok() {
        CategoriaTela categoria = categoriaTela(1, "Prendas");
        Articulo existente = articulo(1, "ART-001", "Polera", TipoArticulo.PRENDA_LISTA, true, categoria, null);
        ArticuloDTO esperado = ArticuloDTO.builder().idArticulo(1).codigoArticulo("ART-001").build();

        when(articuloRepository.findById(1)).thenReturn(Optional.of(existente));
        when(articuloMapper.toDTO(existente)).thenReturn(esperado);

        ArticuloDTO resultado = articuloServiceImpl.obtenerPorId(1);

        assertThat(resultado).isEqualTo(esperado);
    }

    @Test
    @DisplayName("obtenerPorId lanza excepción si el artículo no existe")
    void obtenerPorId_noExiste() {
        when(articuloRepository.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> articuloServiceImpl.obtenerPorId(1))
                .isInstanceOf(EntityNotFoundException.class);
    }

    // ---------------- obtenerPorCodigo ----------------

    @Test
    @DisplayName("obtenerPorCodigo retorna el DTO cuando el artículo existe")
    void obtenerPorCodigo_ok() {
        CategoriaTela categoria = categoriaTela(1, "Prendas");
        Articulo existente = articulo(1, "ART-001", "Polera", TipoArticulo.PRENDA_LISTA, true, categoria, null);
        ArticuloDTO esperado = ArticuloDTO.builder().idArticulo(1).codigoArticulo("ART-001").build();

        when(articuloRepository.findByCodigoArticulo("ART-001")).thenReturn(Optional.of(existente));
        when(articuloMapper.toDTO(existente)).thenReturn(esperado);

        ArticuloDTO resultado = articuloServiceImpl.obtenerPorCodigo("ART-001");

        assertThat(resultado).isEqualTo(esperado);
    }

    @Test
    @DisplayName("obtenerPorCodigo lanza excepción si el artículo no existe")
    void obtenerPorCodigo_noExiste() {
        when(articuloRepository.findByCodigoArticulo("ART-999")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> articuloServiceImpl.obtenerPorCodigo("ART-999"))
                .isInstanceOf(EntityNotFoundException.class);
    }

    // ---------------- listarTodos ----------------

    @Test
    @DisplayName("listarTodos mapea correctamente la lista completa")
    void listarTodos_ok() {
        CategoriaTela categoria = categoriaTela(1, "Prendas");
        Articulo a1 = articulo(1, "ART-001", "Polera", TipoArticulo.PRENDA_LISTA, true, categoria, null);
        ArticuloDTO dto1 = ArticuloDTO.builder().idArticulo(1).codigoArticulo("ART-001").build();

        when(articuloRepository.findAll()).thenReturn(List.of(a1));
        when(articuloMapper.toDTO(a1)).thenReturn(dto1);

        List<ArticuloDTO> resultado = articuloServiceImpl.listarTodos();

        assertThat(resultado).containsExactly(dto1);
    }

    // ---------------- listarActivos ----------------

    @Test
    @DisplayName("listarActivos mapea correctamente los artículos activos")
    void listarActivos_ok() {
        CategoriaTela categoria = categoriaTela(1, "Prendas");
        Articulo a1 = articulo(1, "ART-001", "Polera", TipoArticulo.PRENDA_LISTA, true, categoria, null);
        ArticuloDTO dto1 = ArticuloDTO.builder().idArticulo(1).codigoArticulo("ART-001").build();

        when(articuloRepository.findByActivoTrue()).thenReturn(List.of(a1));
        when(articuloMapper.toDTO(a1)).thenReturn(dto1);

        List<ArticuloDTO> resultado = articuloServiceImpl.listarActivos();

        assertThat(resultado).containsExactly(dto1);
    }

    // ---------------- listarPorTipo ----------------

    @Test
    @DisplayName("listarPorTipo mapea correctamente los artículos del tipo especificado")
    void listarPorTipo_ok() {
        CategoriaTela categoria = categoriaTela(1, "Prendas");
        Articulo a1 = articulo(1, "ART-001", "Polera", TipoArticulo.PRENDA_LISTA, true, categoria, null);
        ArticuloDTO dto1 = ArticuloDTO.builder().idArticulo(1).codigoArticulo("ART-001").build();

        when(articuloRepository.findByTipoArticulo(TipoArticulo.PRENDA_LISTA)).thenReturn(List.of(a1));
        when(articuloMapper.toDTO(a1)).thenReturn(dto1);

        List<ArticuloDTO> resultado = articuloServiceImpl.listarPorTipo(TipoArticulo.PRENDA_LISTA);

        assertThat(resultado).containsExactly(dto1);
    }

    // ---------------- buscarPorNombre ----------------

    @Test
    @DisplayName("buscarPorNombre mapea correctamente los resultados")
    void buscarPorNombre_ok() {
        CategoriaTela categoria = categoriaTela(1, "Prendas");
        Articulo a1 = articulo(1, "ART-001", "Polera", TipoArticulo.PRENDA_LISTA, true, categoria, null);
        ArticuloDTO dto1 = ArticuloDTO.builder().idArticulo(1).codigoArticulo("ART-001").build();

        when(articuloRepository.findByNombreArticuloContainingIgnoreCase("pole")).thenReturn(List.of(a1));
        when(articuloMapper.toDTO(a1)).thenReturn(dto1);

        List<ArticuloDTO> resultado = articuloServiceImpl.buscarPorNombre("pole");

        assertThat(resultado).containsExactly(dto1);
    }

    // ---------------- eliminarArticulo ----------------

    @Test
    @DisplayName("eliminarArticulo desactiva el artículo cuando existe")
    void eliminarArticulo_ok() {
        CategoriaTela categoria = categoriaTela(1, "Prendas");
        Articulo existente = articulo(1, "ART-001", "Polera", TipoArticulo.PRENDA_LISTA, true, categoria, null);

        when(articuloRepository.findById(1)).thenReturn(Optional.of(existente));
        when(articuloRepository.save(existente)).thenReturn(existente);

        articuloServiceImpl.eliminarArticulo(1);

        assertThat(existente.getActivo()).isFalse();
        verify(articuloRepository).save(existente);
    }

    @Test
    @DisplayName("eliminarArticulo lanza excepción si el artículo no existe")
    void eliminarArticulo_noExiste() {
        when(articuloRepository.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> articuloServiceImpl.eliminarArticulo(1))
                .isInstanceOf(EntityNotFoundException.class);

        verify(articuloRepository, never()).save(any());
    }
}
