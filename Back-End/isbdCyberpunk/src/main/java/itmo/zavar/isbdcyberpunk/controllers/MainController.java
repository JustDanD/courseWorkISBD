package itmo.zavar.isbdcyberpunk.controllers;

import itmo.zavar.isbdcyberpunk.models.cyberware.CyberwareEntity;
import itmo.zavar.isbdcyberpunk.models.cyberware.CyberwareRarityEntity;
import itmo.zavar.isbdcyberpunk.models.cyberware.CyberwareTypeEntity;
import itmo.zavar.isbdcyberpunk.models.shop.ShoppingCartEntity;
import itmo.zavar.isbdcyberpunk.models.shop.storage.SellingPointEntity;
import itmo.zavar.isbdcyberpunk.models.shop.storage.StorageElementEntity;
import itmo.zavar.isbdcyberpunk.models.user.UserEntity;
import itmo.zavar.isbdcyberpunk.models.user.info.BillingEntity;
import itmo.zavar.isbdcyberpunk.models.user.list.ListCustomersEntity;
import itmo.zavar.isbdcyberpunk.payload.request.AddToCartRequest;
import itmo.zavar.isbdcyberpunk.payload.request.GetCyberwaresRequest;
import itmo.zavar.isbdcyberpunk.payload.response.*;
import itmo.zavar.isbdcyberpunk.repository.*;
import itmo.zavar.isbdcyberpunk.security.services.UserDetailsImpl;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/main")
public class MainController {
    @Autowired
    private ListCustomersEntityRepository customersEntityRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ShoppingCartEntityRepository cartEntityRepository;

    @Autowired
    private StorageElementEntityRepository storageElementEntityRepository;

    @Autowired
    private CyberwareEntityRepository cyberwareEntityRepository;

    @Autowired
    private SellingPointEntityRepository sellingPointEntityRepository;

    @Autowired
    private CyberwareTypeEntityRepository cyberwareTypeEntityRepository;

    @Autowired
    private CyberwareRarityEntityRepository cyberwareRarityEntityRepository;

    @GetMapping("/getUserDetails")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getUserFullDetails() {
        UserDetailsImpl principal = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<UserEntity> optionalUserEntity = userRepository.findById(principal.id());
        UserFullDetailsResponse userFullDetails;
        if (optionalUserEntity.isPresent()) {
            UserEntity userEntity = optionalUserEntity.get();
            Optional<ListCustomersEntity> listById = customersEntityRepository.findByUserId_Id(userEntity.getId());
            if (listById.isPresent()) {
                BillingEntity billing = listById.get().getBillingId();
                userFullDetails = new UserFullDetailsResponse(userEntity.getUsername(), userEntity.getRole().getName().ordinal(), billing.getSum());
                return ResponseEntity.ok(userFullDetails);
            } else {
                return ResponseEntity.status(400).body(new MessageResponse("Пользователь не найден в таблице покупателей"));
            }
        } else {
            return ResponseEntity.status(400).body(new MessageResponse("Пользователь не найден"));
        }
    }

    @GetMapping("/getCartSize")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getCartSize() {
        UserDetailsImpl principal = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<ListCustomersEntity> customersEntity = customersEntityRepository.findByUserId_Id(principal.id());
        if(customersEntity.isEmpty())
            return ResponseEntity.status(400).body(new MessageResponse("Пользователь не найден в таблице покупателей"));
        Long count = cartEntityRepository.countByCustomerId_Id(customersEntity.get().getId());
        return ResponseEntity.ok(new CartSizeResponse(count));
    }

    @PostMapping("/getCyberwares")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getCyberwares(@Valid @RequestBody GetCyberwaresRequest getCyberwaresRequest) {
        UserDetailsImpl principal = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<CyberwareEntity> all = cyberwareEntityRepository.findAll().stream().skip(getCyberwaresRequest.getStartPosition()).toList();

        if(getCyberwaresRequest.getRarity().isEmpty()) {
            getCyberwaresRequest.getRarity().addAll(cyberwareRarityEntityRepository.findAll().stream().map(CyberwareRarityEntity::getName).toList());
        }
        if(getCyberwaresRequest.getType().isEmpty()) {
            getCyberwaresRequest.getType().addAll(cyberwareTypeEntityRepository.findAll().stream().map(CyberwareTypeEntity::getTypeName).toList());
        }

        List<CyberwareEntity> filtered = all.stream().filter(entity -> {
            Optional<StorageElementEntity> byCyberware = storageElementEntityRepository.findByCyberwareId(entity);
            if (byCyberware.isPresent()) {
                StorageElementEntity storageElementEntity = byCyberware.get();
                if ((storageElementEntity.getPrice() >= getCyberwaresRequest.getStartPrice() &&
                        storageElementEntity.getPrice() <= getCyberwaresRequest.getEndPrice() ||
                        (getCyberwaresRequest.getStartPrice() == 0 && getCyberwaresRequest.getEndPrice() == -1))) {
                    return getCyberwaresRequest.getRarity().contains(entity.getRarity().getName()) &&
                            getCyberwaresRequest.getType().contains(entity.getType().getTypeName());
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }).limit(getCyberwaresRequest.getSize()).toList();

        List<GetCyberwaresResponse> output = new ArrayList<>();

        for (CyberwareEntity entity : filtered) {
            Optional<StorageElementEntity> optionalStorageElement = storageElementEntityRepository.findByCyberwareId(entity);
            if (optionalStorageElement.isPresent()) {
                StorageElementEntity storageElementEntity = optionalStorageElement.get();
                Optional<SellingPointEntity> optionalSellingPointEntity = sellingPointEntityRepository.findByStorageEntity_Id(storageElementEntity.getStorageId().getId());
                if (optionalSellingPointEntity.isPresent()) {
                    SellingPointEntity sellingPointEntity = optionalSellingPointEntity.get();
                    output.add(new GetCyberwaresResponse(sellingPointEntity.getId(), sellingPointEntity.getName(), entity, storageElementEntity.getRating(), storageElementEntity.getCount(), storageElementEntity.getPrice(), storageElementEntity.getId()));
                } else {
                    return ResponseEntity.status(400).body(new MessageResponse("Точка продажи не найдена"));
                }
            } else {
                return ResponseEntity.status(400).body(new MessageResponse("Позиция на складе не найдена"));
            }
        }

        return ResponseEntity.ok(output);
    }

    @PostMapping("/addToCart")
    @PreAuthorize("isAuthenticated()")
    @Transactional
    public ResponseEntity<?> addToCart(@Valid @RequestBody AddToCartRequest addToCartRequest) {
        UserDetailsImpl principal = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<ListCustomersEntity> customersEntity = customersEntityRepository.findByUserId_Id(principal.id());
        Optional<StorageElementEntity> storageElementById = storageElementEntityRepository.findById(addToCartRequest.getStorageElementId());
        if (customersEntity.isPresent() && storageElementById.isPresent()) {
            ShoppingCartEntity cartEntity = new ShoppingCartEntity(customersEntity.get(), storageElementById.get());
            cartEntityRepository.save(cartEntity);
        } else {
            TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
            return ResponseEntity.status(400).body(new MessageResponse("Пользователь или позиция на складе не найден в таблице покупателей"));
        }

        Long count = cartEntityRepository.countByCustomerId_Id(customersEntity.get().getId());
        return ResponseEntity.ok(new CartSizeResponse(count));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        return ResponseEntity.status(HttpStatusCode.valueOf(400)).body(new MessageResponse(e.getMessage()));
    }
}
