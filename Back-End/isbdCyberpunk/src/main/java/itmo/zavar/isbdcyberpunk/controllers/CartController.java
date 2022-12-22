package itmo.zavar.isbdcyberpunk.controllers;

import itmo.zavar.isbdcyberpunk.models.cyberware.CyberwareEntity;
import itmo.zavar.isbdcyberpunk.models.shop.ShoppingCartEntity;
import itmo.zavar.isbdcyberpunk.models.shop.order.OrderEntity;
import itmo.zavar.isbdcyberpunk.models.shop.order.OrderStatusEntity;
import itmo.zavar.isbdcyberpunk.models.shop.order.OrderStructureEntity;
import itmo.zavar.isbdcyberpunk.models.shop.storage.StorageElementEntity;
import itmo.zavar.isbdcyberpunk.models.user.info.BillingEntity;
import itmo.zavar.isbdcyberpunk.models.user.list.ListCustomersEntity;
import itmo.zavar.isbdcyberpunk.payload.request.RemoveFromCartRequest;
import itmo.zavar.isbdcyberpunk.payload.response.CartContentResponse;
import itmo.zavar.isbdcyberpunk.payload.response.MessageResponse;
import itmo.zavar.isbdcyberpunk.repository.*;
import itmo.zavar.isbdcyberpunk.security.services.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/cart")
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

    @Autowired
    private StorageElementEntityRepository storageElementEntityRepository;

    @Autowired
    private BillingEntityRepository billingEntityRepository;

    @PostMapping("/removeFromCart")
    @PreAuthorize("isAuthenticated()")
    @Transactional
    public ResponseEntity<?> removeFromCart(@Valid @RequestBody RemoveFromCartRequest removeFromCartRequest) {
        UserDetailsImpl principal = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        cartEntityRepository.deleteByStorageElementEntity_Id(removeFromCartRequest.getStorageElementId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/getCartContent")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getCartContent() {
        UserDetailsImpl principal = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<ListCustomersEntity> customerById = customersEntityRepository.findByUserId_Id(principal.id());
        if(customerById.isEmpty())
            return ResponseEntity.status(400).body(new MessageResponse("Пользователь не найден"));
        List<ShoppingCartEntity> cart = cartEntityRepository.findAllByCustomerId_Id(customerById.get().getId());
        if(cart.isEmpty()) {
            return ResponseEntity.status(400).body(new MessageResponse("Корзина пуста"));
        }
        Long sum = 0L;
        List<CyberwareEntity> cyberwareEntities = cart.stream().map(shoppingCartEntity -> shoppingCartEntity.getStorageElementEntity().getCyberwareId()).toList();
        for(ShoppingCartEntity shoppingCartEntity : cart)  {
            sum += shoppingCartEntity.getStorageElementEntity().getPrice();
        }

        return ResponseEntity.ok().body(new CartContentResponse(cyberwareEntities, sum));
    }

    @PostMapping("/confirmOrder")
    @PreAuthorize("isAuthenticated()")
    @Transactional
    public ResponseEntity<?> confirmOrder() {
        UserDetailsImpl principal = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<ListCustomersEntity> customerById = customersEntityRepository.findByUserId_Id(principal.id());
        if(customerById.isEmpty())
            return ResponseEntity.status(400).body(new MessageResponse("Пользователь не найден"));
        List<ShoppingCartEntity> list = cartEntityRepository.findAllByCustomerId_Id(customerById.get().getId());
        Optional<ListCustomersEntity> customer = customersEntityRepository.findByUserId_Id(principal.id());
        List<StorageElementEntity> ids = new ArrayList<>();
        Long sum = 0L;

        if(list.isEmpty()) {
            return ResponseEntity.status(400).body(new MessageResponse("Корзина пуста"));
        }

        for(ShoppingCartEntity shoppingCartEntity : list)  {
            ids.add(shoppingCartEntity.getStorageElementEntity());
            sum += shoppingCartEntity.getStorageElementEntity().getPrice();
        }
        Optional<OrderStatusEntity> byId = statusEntityRepository.findById(2L);//get "выполнен"
        if(byId.isPresent() && customer.isPresent()) {
            OrderEntity orderEntity = new OrderEntity(Date.from(Instant.now()), sum, byId.get(), customer.get());
            OrderEntity save = orderEntityRepository.save(orderEntity);
            for (StorageElementEntity entity : ids) {
                if (entity.getCount() > 0) {
                    entity.setCount(entity.getCount() - 1);
                    StorageElementEntity save1 = storageElementEntityRepository.save(entity);
                    orderStructureEntityRepository.save(new OrderStructureEntity(save1, save));
                } else {
                    TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                    return ResponseEntity.badRequest().body(new MessageResponse("На складе недостаточно '" + entity.getCyberwareId().getName() + "'"));
                }
            }
        } else {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResponseEntity.status(400).build();
        }
        BillingEntity billing = customerById.get().getBillingId();

        if(billing.getSum() < sum) {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResponseEntity.status(412).body(new MessageResponse("Недостаточно средств"));
        }

        billing.setSum(billing.getSum() - sum);
        cartEntityRepository.deleteAllByCustomerId_Id(customerById.get().getId());

        billingEntityRepository.save(billing);

        return ResponseEntity.ok().build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        return ResponseEntity.status(HttpStatusCode.valueOf(400)).body(new MessageResponse(e.getMessage()));
    }
}
