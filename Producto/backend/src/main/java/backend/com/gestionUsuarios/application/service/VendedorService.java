package backend.com.gestionUsuarios.application.service;

import backend.com.gestionUsuarios.application.dto.VendedorCreateDTO;
import backend.com.gestionUsuarios.application.dto.VendedorDTO;

import java.util.List;

public interface VendedorService {
    VendedorDTO create(VendedorCreateDTO dto);
    VendedorDTO update(Long id, VendedorCreateDTO dto);
    VendedorDTO findById(Long id);
    List<VendedorDTO> findAll();
    void delete(Long id);
    VendedorDTO findByUsuarioId(Long usuarioId);
}
