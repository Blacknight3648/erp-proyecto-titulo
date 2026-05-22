package backend.com.shared.application.service;

import java.util.List;
import java.util.Optional;

import backend.com.shared.application.dto.TipoContactoDTO;

public interface TipoContactoService {

    List<TipoContactoDTO> listarTodos();

    Optional<TipoContactoDTO> obtenerPorId(Long id);

    Optional<TipoContactoDTO> obtenerPorNombreTipoContacto(String nombreTipoContacto);

    TipoContactoDTO crearTipoContacto(TipoContactoDTO tipoContacto);

    TipoContactoDTO actualizarTipoContacto(Long id, TipoContactoDTO tipoContacto);

    void eliminarTipoContacto(Long id);

    Optional<TipoContactoDTO> obtenerOCrearPorNombreTipoContacto(String nombreTipoContacto);

}
