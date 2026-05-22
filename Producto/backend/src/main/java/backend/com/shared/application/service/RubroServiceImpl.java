package backend.com.shared.application.service;

import backend.com.shared.application.dto.RubroDTO;

import backend.com.shared.domain.model.Rubro;
import backend.com.shared.infrastructure.mapper.RubroMapper;
import backend.com.shared.infrastructure.persistence.repository.RubroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RubroServiceImpl implements RubroService {

    @Autowired
    private RubroRepository rubroRepository;

    @Autowired
    private RubroMapper rubroMapper;

    @Override
    @Transactional(readOnly = true)
    public List<RubroDTO> getAllRubros() {
        List<Rubro> rubros = rubroRepository.findAll();

        List<RubroDTO> rubroDTOs = rubros.stream()
                .map(rubroMapper::toDTO)
                .collect(Collectors.toList());

        return rubroDTOs;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<RubroDTO> getRubroById(Long id) {
        return rubroRepository.findById(id)
                .map(rubroMapper::toDTO);
    }

    @Override
    @Transactional
    public RubroDTO createRubro(RubroDTO rubroDTO) {
        Rubro rubro = rubroMapper.toDomain(rubroDTO);
        Rubro savedRubro = rubroRepository.save(rubro);
        return rubroMapper.toDTO(savedRubro);
    }

    @Override
    @Transactional
    public Optional<RubroDTO> updateRubro(Long id, RubroDTO rubroDTO) {
        return rubroRepository.findById(id)
                .map(existingRubro -> {
                    existingRubro.setNombreRubro(rubroDTO.getNombreRubro());
                    existingRubro.setDescripcionRubro(rubroDTO.getDescripcionRubro());
                    Rubro updatedRubro = rubroRepository.save(existingRubro);
                    return rubroMapper.toDTO(updatedRubro);
                });
    }

    @Override
    @Transactional
    public void deleteRubro(Long id) {
        rubroRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RubroDTO> searchRubros(String query, int limit) {
        List<Rubro> rubros = rubroRepository.findAllByOrderByNombreRubroAsc();
        return rubros.stream()
                .map(rubroMapper::toDTO)
                .collect(Collectors.toList());
    }
}
