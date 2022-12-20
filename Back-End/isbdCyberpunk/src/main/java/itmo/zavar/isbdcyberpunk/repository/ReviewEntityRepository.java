package itmo.zavar.isbdcyberpunk.repository;

import itmo.zavar.isbdcyberpunk.models.shop.storage.ReviewEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewEntityRepository extends JpaRepository<ReviewEntity, Long> {
    List<ReviewEntity> findByStorageElement_Id(Long id);
}