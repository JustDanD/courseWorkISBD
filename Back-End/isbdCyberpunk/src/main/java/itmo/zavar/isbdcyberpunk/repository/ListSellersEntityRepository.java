package itmo.zavar.isbdcyberpunk.repository;

import itmo.zavar.isbdcyberpunk.models.user.list.ListCustomersEntity;
import itmo.zavar.isbdcyberpunk.models.user.list.ListSellersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ListSellersEntityRepository extends JpaRepository<ListSellersEntity, Long> {
    Optional<ListSellersEntity> findByUserId_Id(Long id);
}