package itmo.zavar.isbdcyberpunk.controllers;

import itmo.zavar.isbdcyberpunk.models.cyberware.CyberwareEntity;
import itmo.zavar.isbdcyberpunk.models.shop.order.OrderEntity;
import itmo.zavar.isbdcyberpunk.models.user.info.ListOwnedCyberwaresEntity;
import itmo.zavar.isbdcyberpunk.models.user.list.ListCustomersEntity;
import itmo.zavar.isbdcyberpunk.payload.response.OrderHistoryResponse;
import itmo.zavar.isbdcyberpunk.payload.response.SetCyberwareResponse;
import itmo.zavar.isbdcyberpunk.repository.ListCustomersEntityRepository;
import itmo.zavar.isbdcyberpunk.repository.ListOwnedCyberwaresEntityRepository;
import itmo.zavar.isbdcyberpunk.repository.OrderEntityRepository;
import itmo.zavar.isbdcyberpunk.security.services.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/profile")
public class UserProfileController {

    @Autowired
    private ListOwnedCyberwaresEntityRepository listOwnedCyberwaresEntityRepository;

    @Autowired
    private OrderEntityRepository orderEntityRepository;

    @Autowired
    private ListCustomersEntityRepository customersEntityRepository;

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
        Optional<ListOwnedCyberwaresEntity> oldCyberware = listOwnedCyberwaresEntityRepository.findByCustomerId_IdAndCyberwareId_Id(listById.get().getId(), setCyberwareResponse.getOldId());
        Optional<ListOwnedCyberwaresEntity> newCyberware = listOwnedCyberwaresEntityRepository.findByCustomerId_IdAndCyberwareId_Id(listById.get().getId(), setCyberwareResponse.getNewId());
        if (oldCyberware.isPresent() && newCyberware.isPresent()) {
            ListOwnedCyberwaresEntity listOwnedCyberwaresEntity = oldCyberware.get();
            listOwnedCyberwaresEntity.setInstalled(false);
            listOwnedCyberwaresEntityRepository.save(listOwnedCyberwaresEntity);

            listOwnedCyberwaresEntity = newCyberware.get();
            listOwnedCyberwaresEntity.setInstalled(true);
            listOwnedCyberwaresEntityRepository.save(listOwnedCyberwaresEntity);
        } else {
            return ResponseEntity.status(400).build();
        }
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getOwnedCyberwares")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<CyberwareEntity>> getOwnedCyberwares() {
        UserDetailsImpl principal = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<ListCustomersEntity> listById = customersEntityRepository.findByUserId_Id(principal.id());
        List<ListOwnedCyberwaresEntity> ownedCyberwaresEntityList = listOwnedCyberwaresEntityRepository.findAllByCustomerId_Id(listById.get().getId());
        List<CyberwareEntity> cyberwareEntities = ownedCyberwaresEntityList.stream().map(ListOwnedCyberwaresEntity::getCyberwareId).toList();
        return ResponseEntity.ok(cyberwareEntities);
    }
}
