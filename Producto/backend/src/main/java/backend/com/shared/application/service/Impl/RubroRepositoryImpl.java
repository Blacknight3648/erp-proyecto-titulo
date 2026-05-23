package backend.com.shared.application.service.Impl;

import org.springframework.stereotype.Component;

import backend.com.shared.domain.repository.RubroRepository;
import backend.com.shared.domain.model.Rubro;
import backend.com.shared.infrastructure.mapper.RubroMapper;
import backend.com.shared.infrastructure.persistence.repository.RubroJpaRepository;

import lombok.RequiredArgsConstructor;
import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RubroRepositoryImpl implements RubroRepository {

    private final RubroJpaRepository jpaRepository;
    private final RubroMapper mapper;

    @Override
    public List<Rubro> findAllByOrderByNombreRubroAsc() {
        return null;
    }

    @Override
    public void deleteById(Long id) {
    }


    @

}
