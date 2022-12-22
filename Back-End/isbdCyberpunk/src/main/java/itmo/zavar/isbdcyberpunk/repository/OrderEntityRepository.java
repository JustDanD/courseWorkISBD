package itmo.zavar.isbdcyberpunk.repository;

import itmo.zavar.isbdcyberpunk.models.shop.order.OrderEntity;
import org.aspectj.weaver.ast.Or;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface OrderEntityRepository extends JpaRepository<OrderEntity, Long> {
    List<OrderEntity> findAllByCustomerId_Id(Long id);
}