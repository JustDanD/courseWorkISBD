package itmo.zavar.isbdcyberpunk.repository;

import itmo.zavar.isbdcyberpunk.models.cyberware.CyberwareEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CyberwareEntityRepository extends JpaRepository<CyberwareEntity, Long> {
    Boolean existsCyberwareEntityById(Long id);
}