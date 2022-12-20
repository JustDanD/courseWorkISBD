package itmo.zavar.isbdcyberpunk.controllers;

import itmo.zavar.isbdcyberpunk.models.cyberware.CyberwareEntity;
import itmo.zavar.isbdcyberpunk.models.shop.ShoppingCartEntity;
import itmo.zavar.isbdcyberpunk.models.shop.storage.StorageElementEntity;
import itmo.zavar.isbdcyberpunk.models.user.UserEntity;
import itmo.zavar.isbdcyberpunk.models.user.info.BillingEntity;
import itmo.zavar.isbdcyberpunk.models.user.list.ListCustomersEntity;
import itmo.zavar.isbdcyberpunk.payload.request.AddToCartRequest;
import itmo.zavar.isbdcyberpunk.payload.request.GetCyberwaresRequest;
import itmo.zavar.isbdcyberpunk.payload.response.CartSizeResponse;
import itmo.zavar.isbdcyberpunk.payload.response.UserFullDetailsResponse;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/main")
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

    @GetMapping("/getUserDetails")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getUserFullDetails() {
        UserDetailsImpl principal = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<UserEntity> byId = userRepository.findById(principal.id());
        UserFullDetailsResponse userFullDetails;
        if (byId.isPresent()) {
            UserEntity userEntity = byId.get();
            Optional<ListCustomersEntity> listById = customersEntityRepository.findByUserId_Id(userEntity.getId());
            if (listById.isPresent()) {
                BillingEntity billing = listById.get().getBillingId();
                userFullDetails = new UserFullDetailsResponse(userEntity.getUsername(), userEntity.getRole().getName().ordinal(), billing.getSum());
                return ResponseEntity.ok(userFullDetails);
            } else {
                return ResponseEntity.status(HttpStatusCode.valueOf(400)).build();
            }
        } else {
            return ResponseEntity.status(HttpStatusCode.valueOf(400)).build();
        }
    }

    @GetMapping("/getCartSize")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<CartSizeResponse> getCartSize() {
        UserDetailsImpl principal = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<ListCustomersEntity> listById = customersEntityRepository.findByUserId_Id(principal.id());
        Long count = cartEntityRepository.countByCustomerId_Id(listById.get().getId());
        return ResponseEntity.ok(new CartSizeResponse(count));
    }

    @GetMapping("/getCyberwares")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<CyberwareEntity>> getCyberwares(@Valid @RequestBody GetCyberwaresRequest getCyberwaresRequest) {
        UserDetailsImpl principal = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<CyberwareEntity> all = cyberwareEntityRepository.findAll().stream().skip(getCyberwaresRequest.getStartPosition()).toList();

        List<CyberwareEntity> filtered = all.stream().filter(entity -> {
            Optional<StorageElementEntity> byCyberware = storageElementEntityRepository.findByCyberwareId(entity);
            if (byCyberware.isPresent()) {
                StorageElementEntity storageElementEntity = byCyberware.get();
                if (storageElementEntity.getPrice() >= getCyberwaresRequest.getStartPrice() &&
                        storageElementEntity.getPrice() <= getCyberwaresRequest.getEndPrice()) {
                    return getCyberwaresRequest.getRarity().contains(entity.getRarity().getName()) &&
                            getCyberwaresRequest.getType().contains(entity.getType().getTypeName());
                } else {
                    return false;
                }
            } else {
                return false;
            }
        }).limit(getCyberwaresRequest.getSize()).toList();

        return ResponseEntity.ok(filtered);
    }

    @PostMapping("/addToCart")
    @PreAuthorize("isAuthenticated()")
    @Transactional
    public ResponseEntity<?> addToCart(@Valid @RequestBody AddToCartRequest addToCartRequest) {
        UserDetailsImpl principal = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Optional<ListCustomersEntity> listById = customersEntityRepository.findByUserId_Id(principal.id());
        Optional<StorageElementEntity> storageElementById = storageElementEntityRepository.findById(addToCartRequest.getStorageElementId());
        if (listById.isPresent() && storageElementById.isPresent()) {
            ShoppingCartEntity cartEntity = new ShoppingCartEntity(listById.get(), storageElementById.get());
            cartEntityRepository.save(cartEntity);
        } else {
            return ResponseEntity.status(404).build();
        }

        Long count = cartEntityRepository.countByCustomerId_Id(listById.get().getId());
        return ResponseEntity.ok(new CartSizeResponse(count));
    }
}
