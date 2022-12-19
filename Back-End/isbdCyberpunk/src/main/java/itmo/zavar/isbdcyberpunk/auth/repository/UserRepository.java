package itmo.zavar.isbdcyberpunk.auth.repository;

import itmo.zavar.isbdcyberpunk.auth.models.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
    Boolean existsByUsername(String username);
}
