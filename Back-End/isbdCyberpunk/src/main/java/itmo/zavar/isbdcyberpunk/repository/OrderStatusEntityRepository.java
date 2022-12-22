package itmo.zavar.isbdcyberpunk.repository;

import itmo.zavar.isbdcyberpunk.models.shop.order.OrderStatusEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderStatusEntityRepository extends JpaRepository<OrderStatusEntity, Long> {
}