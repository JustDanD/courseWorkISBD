package itmo.zavar.isbdcyberpunk.repository;

import itmo.zavar.isbdcyberpunk.models.shop.order.OrderStructureEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderStructureEntityRepository extends JpaRepository<OrderStructureEntity, Long> {
}