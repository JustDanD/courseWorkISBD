package itmo.zavar.isbdcyberpunk.controllers;

import itmo.zavar.isbdcyberpunk.models.shop.ShoppingCartEntity;
import itmo.zavar.isbdcyberpunk.models.shop.order.OrderEntity;
import itmo.zavar.isbdcyberpunk.models.shop.order.OrderStatusEntity;
import itmo.zavar.isbdcyberpunk.models.shop.order.OrderStructureEntity;
import itmo.zavar.isbdcyberpunk.models.shop.storage.StorageElementEntity;
import itmo.zavar.isbdcyberpunk.models.user.list.ListCustomersEntity;
import itmo.zavar.isbdcyberpunk.payload.request.RemoveFromCartRequest;
import itmo.zavar.isbdcyberpunk.repository.*;
import itmo.zavar.isbdcyberpunk.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/cart")
public class CartController {

    @Autowired
    private OrderEntityRepository orderEntityRepository;

    @Autowired
    private ShoppingCartEntityRepository cartEntityRepository;

    @Autowired
    private OrderStructureEntityRepository orderStructureEntityRepository;

    @Autowired
    private OrderStatusEntityRepository statusEntityRepository;

    @Autowired
    private ListCustomersEntityRepository customersEntityRepository;

    @PostMapping("/removeFromCart")
    @PreAuthorize("isAuthenticated()")
    @Transactional
    public ResponseEntity<?> setCyberware(RemoveFromCartRequest removeFromCartRequest) {
        UserDetailsImpl principal = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        cartEntityRepository.deleteByStorageElementEntity_Id(removeFromCartRequest.getStorageElementId());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/confirmOrder")
    @PreAuthorize("isAuthenticated()")
    @Transactional
    public ResponseEntity<?> confirmOrder() {
        UserDetailsImpl principal = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<ListCustomersEntity> listById = customersEntityRepository.findByUserId_Id(principal.id());
        List<ShoppingCartEntity> list = cartEntityRepository.findAllByCustomerId_Id(listById.get().getId());
        Optional<ListCustomersEntity> customer = customersEntityRepository.findByUserId_Id(principal.id());
        List<StorageElementEntity> ids = new ArrayList<>();
        Long sum = 0L;

        if(list.isEmpty())
            return ResponseEntity.status(400).build();

        for(ShoppingCartEntity shoppingCartEntity : list)  {
            ids.add(shoppingCartEntity.getStorageElementEntity());
            sum += shoppingCartEntity.getStorageElementEntity().getPrice();
        }
        Optional<OrderStatusEntity> byId = statusEntityRepository.findById(1L);//get "оформлен"
        if(byId.isPresent() && customer.isPresent()) {
            OrderEntity orderEntity = new OrderEntity(Date.from(Instant.now()), sum, byId.get(), customer.get());
            OrderEntity save = orderEntityRepository.save(orderEntity);
            ids.forEach(entity -> orderStructureEntityRepository.save(new OrderStructureEntity(entity, save)));
        } else {
            return ResponseEntity.status(400).build();
        }

        cartEntityRepository.deleteAllByCustomerId_Id(listById.get().getId());

        return ResponseEntity.ok().build();
    }
}
