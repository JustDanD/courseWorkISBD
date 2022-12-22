package itmo.zavar.isbdcyberpunk.repository;

import itmo.zavar.isbdcyberpunk.models.shop.storage.StorageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StorageEntityRepository extends JpaRepository<StorageEntity, Long> {
    Optional<StorageEntity> findBySellerId_Id(Long id);
}