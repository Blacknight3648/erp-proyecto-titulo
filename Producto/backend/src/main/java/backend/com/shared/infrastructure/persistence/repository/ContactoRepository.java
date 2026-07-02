package backend.com.shared.infrastructure.persistence.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import backend.com.shared.infrastructure.persistence.entity.ContactoJpaEntity;

public interface ContactoRepository extends JpaRepository<ContactoJpaEntity, Long> {

}
