package backend.com.shared.infrastructure.persistence.repository.Jpa;

import backend.com.shared.infrastructure.persistence.entity.ContactoJpaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
public interface ContactoJpaRepository extends JpaRepository<ContactoJpaEntity, Long> {
}
