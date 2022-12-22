package itmo.zavar.isbdcyberpunk.repository;

import itmo.zavar.isbdcyberpunk.models.shop.storage.SellingPointEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SellingPointEntityRepository extends JpaRepository<SellingPointEntity, Long> {
    Optional<SellingPointEntity> findByStorageEntity_Id(Long id);
}