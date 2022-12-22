package itmo.zavar.isbdcyberpunk.repository;

import itmo.zavar.isbdcyberpunk.models.cyberware.CyberwareEntity;
import itmo.zavar.isbdcyberpunk.models.user.info.ListOwnedCyberwaresEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ListOwnedCyberwaresEntityRepository extends JpaRepository<ListOwnedCyberwaresEntity, Long> {
    Optional<ListOwnedCyberwaresEntity> findByCustomerId_IdAndCyberwareId_Id(Long customerId, Long cyberwareId);
    Boolean existsByCustomerId_IdAndCyberwareId_Id(Long customerId, Long cyberwareId);
    List<ListOwnedCyberwaresEntity> findAllByCustomerId_Id(Long id);
}