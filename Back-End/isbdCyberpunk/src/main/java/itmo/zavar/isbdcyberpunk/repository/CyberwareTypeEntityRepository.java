package itmo.zavar.isbdcyberpunk.repository;

import itmo.zavar.isbdcyberpunk.models.cyberware.CyberwareTypeEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CyberwareTypeEntityRepository extends JpaRepository<CyberwareTypeEntity, Long> {
}