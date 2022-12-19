package itmo.zavar.isbdcyberpunk.repository;

import itmo.zavar.isbdcyberpunk.models.user.info.BillingEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BillingEntityRepository extends JpaRepository<BillingEntity, Long> {

}