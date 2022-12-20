package itmo.zavar.isbdcyberpunk.repository;

import itmo.zavar.isbdcyberpunk.models.user.UserEntity;
import itmo.zavar.isbdcyberpunk.models.user.list.ListCustomersEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ListCustomersEntityRepository extends CrudRepository<ListCustomersEntity, Long> {
    Optional<ListCustomersEntity> findByUserId_Id(Long id);
}