package itmo.zavar.isbdcyberpunk.repository;

import itmo.zavar.isbdcyberpunk.models.cyberware.CyberwareEntity;
import itmo.zavar.isbdcyberpunk.models.shop.storage.StorageElementEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StorageElementEntityRepository extends JpaRepository<StorageElementEntity, Long> {
    Optional<StorageElementEntity> findByCyberwareId(CyberwareEntity entity);
}