package itmo.zavar.isbdcyberpunk.controllers;

import itmo.zavar.isbdcyberpunk.models.cyberware.CyberwareEntity;
import itmo.zavar.isbdcyberpunk.models.shop.order.OrderEntity;
import itmo.zavar.isbdcyberpunk.models.shop.storage.SellingPointEntity;
import itmo.zavar.isbdcyberpunk.models.shop.storage.StorageElementEntity;
import itmo.zavar.isbdcyberpunk.models.user.info.ListOwnedCyberwaresEntity;
import itmo.zavar.isbdcyberpunk.models.user.list.ListCustomersEntity;
import itmo.zavar.isbdcyberpunk.payload.response.*;
import itmo.zavar.isbdcyberpunk.repository.*;
import itmo.zavar.isbdcyberpunk.security.services.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/profile")
public class UserProfileController {

    @Autowired
    private ListOwnedCyberwaresEntityRepository listOwnedCyberwaresEntityRepository;

    @Autowired
    private OrderEntityRepository orderEntityRepository;

    @Autowired
    private ListCustomersEntityRepository customersEntityRepository;

    @Autowired
    private SellingPointEntityRepository sellingPointEntityRepository;

    @Autowired
    private StorageElementEntityRepository storageElementEntityRepository;

    @GetMapping("/getOrdersHistory")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<OrderHistoryResponse>> getOrdersHistory() {
        UserDetailsImpl principal = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<ListCustomersEntity> listById = customersEntityRepository.findByUserId_Id(principal.id());
        List<OrderEntity> history = orderEntityRepository.findAllByCustomerId_Id(listById.get().getId());
        List<OrderHistoryResponse> orderHistoryResponses = history.stream().map(orderEntity -> new OrderHistoryResponse(orderEntity.getId(), orderEntity.getCreationTime(), orderEntity.getPrice(), orderEntity.getStatus(), orderEntity.getCustomerId().getId(), orderEntity.getCustomerId().getUserId().getUsername())).toList();
        return ResponseEntity.ok(orderHistoryResponses);
    }

    @PostMapping("/setCyberware")
    @PreAuthorize("isAuthenticated()")
    @Transactional
    public ResponseEntity<?> setCyberware(@Valid @RequestBody SetCyberwareResponse setCyberwareResponse) {
        UserDetailsImpl principal = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<ListCustomersEntity> listById = customersEntityRepository.findByUserId_Id(principal.id());
        Optional<ListOwnedCyberwaresEntity> cyberware = listOwnedCyberwaresEntityRepository.findByCustomerId_IdAndCyberwareId_Id(listById.get().getId(), setCyberwareResponse.getCyberwareId());
        if (cyberware.isPresent()) {
            ListOwnedCyberwaresEntity listOwnedCyberwaresEntity = cyberware.get();
            listOwnedCyberwaresEntity.setInstalled(setCyberwareResponse.isInstalled());
            listOwnedCyberwaresEntityRepository.save(listOwnedCyberwaresEntity);
        } else {
            return ResponseEntity.status(400).build();
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getOwnedCyberwares")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getOwnedCyberwares() {
        UserDetailsImpl principal = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<ListCustomersEntity> listById = customersEntityRepository.findByUserId_Id(principal.id());
        List<ListOwnedCyberwaresEntity> ownedCyberwaresEntityList = listOwnedCyberwaresEntityRepository.findAllByCustomerId_Id(listById.get().getId());
        List<CyberwareEntity> cyberwareEntities = ownedCyberwaresEntityList.stream().map(ListOwnedCyberwaresEntity::getCyberwareId).toList();
        List<GetOwnedCyberwaresResponse> output = new ArrayList<>();

        for (CyberwareEntity entity : cyberwareEntities) {
            Optional<StorageElementEntity> optionalStorageElement = storageElementEntityRepository.findByCyberwareId(entity);
            if (optionalStorageElement.isPresent()) {
                StorageElementEntity storageElementEntity = optionalStorageElement.get();
                Optional<SellingPointEntity> optionalSellingPointEntity = sellingPointEntityRepository.findByStorageEntity_Id(storageElementEntity.getStorageId().getId());
                if (optionalSellingPointEntity.isPresent()) {
                    SellingPointEntity sellingPointEntity = optionalSellingPointEntity.get();
                    Optional<ListOwnedCyberwaresEntity> listOwnedCyberwaresEntity = listOwnedCyberwaresEntityRepository.findByCustomerId_IdAndCyberwareId_Id(listById.get().getId(), entity.getId());
                    if(listOwnedCyberwaresEntity.isEmpty())
                        return ResponseEntity.status(400).body(new MessageResponse("Имплант не найден в списке инвентаря"));
                    output.add(new GetOwnedCyberwaresResponse(sellingPointEntity.getId(), sellingPointEntity.getName(), entity, storageElementEntity.getRating(),
                            storageElementEntity.getCount(), storageElementEntity.getPrice(), storageElementEntity.getId(), listOwnedCyberwaresEntity.get().getInstalled()));
                } else {
                    return ResponseEntity.status(400).body(new MessageResponse("Точка продажи не найдена"));
                }
            } else {
                return ResponseEntity.status(400).body(new MessageResponse("Позиция на складе не найдена"));
            }
        }

        return ResponseEntity.ok(output);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        return ResponseEntity.status(HttpStatusCode.valueOf(400)).body(new MessageResponse(e.getMessage()));
    }
}
