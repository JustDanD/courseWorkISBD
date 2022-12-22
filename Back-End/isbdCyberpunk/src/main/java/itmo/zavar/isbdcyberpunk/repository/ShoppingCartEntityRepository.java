package itmo.zavar.isbdcyberpunk.repository;

import itmo.zavar.isbdcyberpunk.models.cyberware.CyberwareEntity;
import itmo.zavar.isbdcyberpunk.models.shop.ShoppingCartEntity;
import itmo.zavar.isbdcyberpunk.models.user.RoleEntity;
import itmo.zavar.isbdcyberpunk.models.user.list.ListCustomersEntity;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ShoppingCartEntityRepository extends JpaRepository<ShoppingCartEntity, Long> {
    List<ShoppingCartEntity> findAllByCustomerId_Id(Long id);

    void deleteAllByCustomerId_Id(Long id);
    Long countByCustomerId_Id(@NotNull Long id);
    void deleteByStorageElementEntity_Id(Long id);
}