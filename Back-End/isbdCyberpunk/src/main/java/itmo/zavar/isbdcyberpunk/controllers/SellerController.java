package itmo.zavar.isbdcyberpunk.controllers;

import itmo.zavar.isbdcyberpunk.models.shop.storage.SellingPointEntity;
import itmo.zavar.isbdcyberpunk.models.shop.storage.StorageEntity;
import itmo.zavar.isbdcyberpunk.models.user.UserEntity;
import itmo.zavar.isbdcyberpunk.models.user.info.BillingEntity;
import itmo.zavar.isbdcyberpunk.models.user.list.ListCustomersEntity;
import itmo.zavar.isbdcyberpunk.models.user.list.ListSellersEntity;
import itmo.zavar.isbdcyberpunk.payload.response.MessageResponse;
import itmo.zavar.isbdcyberpunk.payload.response.SellerDetailsResponse;
import itmo.zavar.isbdcyberpunk.payload.response.UserFullDetailsResponse;
import itmo.zavar.isbdcyberpunk.repository.*;
import itmo.zavar.isbdcyberpunk.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/seller")
public class SellerController {

    @Autowired
    private ListSellersEntityRepository listSellersEntityRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private StorageEntityRepository storageEntityRepository;

    @Autowired
    private SellingPointEntityRepository sellingPointEntityRepository;

    @GetMapping("/getSellerDetails")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getSellerDetails() {
        UserDetailsImpl principal = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<UserEntity> optionalUserEntity = userRepository.findById(principal.id());
        SellerDetailsResponse sellerDetailsResponse;
        if (optionalUserEntity.isPresent()) {
            UserEntity userEntity = optionalUserEntity.get();
            Optional<ListSellersEntity> listById = listSellersEntityRepository.findByUserId_Id(userEntity.getId());
            if (listById.isPresent()) {
                Optional<StorageEntity> storageEntityOptional = storageEntityRepository.findBySellerId_Id(listById.get().getId());
                if(storageEntityOptional.isEmpty())
                    return ResponseEntity.status(400).body(new MessageResponse("Хранилище не найдено"));
                Optional<SellingPointEntity> sellingPointEntityOptional = sellingPointEntityRepository.findByStorageEntity_Id(storageEntityOptional.get().getId());
                if(sellingPointEntityOptional.isEmpty())
                    return ResponseEntity.status(400).body(new MessageResponse("Точка продажи не найдена"));
                BillingEntity billing = listById.get().getBillingId();
                sellerDetailsResponse = new SellerDetailsResponse(userEntity.getUsername(), userEntity.getRole().getName().ordinal(), billing.getSum(),
                        storageEntityOptional.get().getId(), sellingPointEntityOptional.get().getId(), sellingPointEntityOptional.get().getName());
                return ResponseEntity.ok(sellerDetailsResponse);
            } else {
                return ResponseEntity.status(400).body(new MessageResponse("Пользователь не найден в таблице продавцов"));
            }
        } else {
            return ResponseEntity.status(400).body(new MessageResponse("Пользователь не найден"));
        }
    }
}
