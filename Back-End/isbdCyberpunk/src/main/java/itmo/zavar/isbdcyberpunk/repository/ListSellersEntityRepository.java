package itmo.zavar.isbdcyberpunk.repository;

import itmo.zavar.isbdcyberpunk.models.user.list.ListSellersEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ListSellersEntityRepository extends JpaRepository<ListSellersEntity, Long> {
}