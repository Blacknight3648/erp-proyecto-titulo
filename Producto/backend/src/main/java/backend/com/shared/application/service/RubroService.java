package backend.com.shared.application.service;

import backend.com.shared.application.dto.RubroDTO;

import java.util.List;
import java.util.Optional;

public interface RubroService {

    List<RubroDTO> getAllRubros();

    Optional<RubroDTO> getRubroById(Long id);

    Optional<RubroDTO> getRubroByCodigoSii(String codigoSii);

    Optional<RubroDTO> getRubroByNombreRubro(String nombreRubro);

    RubroDTO createRubro(RubroDTO rubroDTO);

    Optional<RubroDTO> updateRubro(Long id, RubroDTO rubroDTO);

    void deleteRubro(Long id);

    List<RubroDTO> searchRubros(String query, int limit);

}
