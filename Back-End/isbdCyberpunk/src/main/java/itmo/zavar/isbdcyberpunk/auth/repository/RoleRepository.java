package itmo.zavar.isbdcyberpunk.auth.repository;

import itmo.zavar.isbdcyberpunk.auth.models.user.RoleEntity;
import itmo.zavar.isbdcyberpunk.auth.models.user.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    Optional<RoleEntity> findByName(UserRole name);
}