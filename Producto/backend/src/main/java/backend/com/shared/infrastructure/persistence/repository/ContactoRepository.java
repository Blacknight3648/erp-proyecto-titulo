package backend.com.shared.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import backend.com.shared.infrastructure.persistence.entity.ContactoJpaEntity;

@Repository
public interface ContactoRepository extends JpaRepository<ContactoJpaEntity, Long> {

}
