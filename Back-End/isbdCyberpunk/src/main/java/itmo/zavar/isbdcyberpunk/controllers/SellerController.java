package itmo.zavar.isbdcyberpunk.controllers;

import itmo.zavar.isbdcyberpunk.models.cyberware.CyberwareEntity;
import itmo.zavar.isbdcyberpunk.models.shop.storage.SellingPointEntity;
import itmo.zavar.isbdcyberpunk.models.shop.storage.StorageElementEntity;
import itmo.zavar.isbdcyberpunk.models.shop.storage.StorageEntity;
import itmo.zavar.isbdcyberpunk.models.user.UserEntity;
import itmo.zavar.isbdcyberpunk.models.user.info.BillingEntity;
import itmo.zavar.isbdcyberpunk.models.user.list.ListSellersEntity;
import itmo.zavar.isbdcyberpunk.payload.request.AddStorageElementRequest;
import itmo.zavar.isbdcyberpunk.payload.request.DeleteStorageElementRequest;
import itmo.zavar.isbdcyberpunk.payload.request.UpdateStorageElementRequest;
import itmo.zavar.isbdcyberpunk.payload.response.*;
import itmo.zavar.isbdcyberpunk.repository.*;
import itmo.zavar.isbdcyberpunk.security.services.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
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

    @Autowired
    private CyberwareEntityRepository cyberwareEntityRepository;

    @Autowired
    private StorageElementEntityRepository storageElementEntityRepository;

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

    @GetMapping("/getAllCyberwares")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getAllCyberwares() {
        List<CyberwareEntity> all = cyberwareEntityRepository.findAll();
        return ResponseEntity.ok(all);
    }

    @GetMapping("/getStorageElements")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> getStorageElements() {
        UserDetailsImpl principal = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<UserEntity> optionalUserEntity = userRepository.findById(principal.id());
        if (optionalUserEntity.isPresent()) {
            UserEntity userEntity = optionalUserEntity.get();
            Optional<ListSellersEntity> listById = listSellersEntityRepository.findByUserId_Id(userEntity.getId());
            if(listById.isPresent()) {
                Optional<StorageEntity> storageEntityOptional = storageEntityRepository.findBySellerId_Id(listById.get().getId());
                if(storageEntityOptional.isPresent()) {
                    List<GetStorageElementsResponse> output = storageElementEntityRepository.findAllByStorageId_Id(storageEntityOptional.get().getId()).stream().map(storageElementEntity -> new GetStorageElementsResponse(storageElementEntity.getId(), storageElementEntity.getCyberwareId(), storageElementEntity.getCount(), storageElementEntity.getRating(), storageElementEntity.getPrice())).toList();
                    return ResponseEntity.ok(output);
                } else {
                    return ResponseEntity.status(400).body(new MessageResponse("Хранилище не найдено"));
                }
            } else {
                return ResponseEntity.status(400).body(new MessageResponse("Пользователь не найден в таблице продавцов"));
            }
        } else {
            return ResponseEntity.status(400).body(new MessageResponse("Пользователь не найден"));
        }
    }

    @PostMapping("/addStorageElement")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> addStorageElement(@Valid @RequestBody AddStorageElementRequest addStorageElementRequest) {
        UserDetailsImpl principal = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<UserEntity> optionalUserEntity = userRepository.findById(principal.id());
        if (optionalUserEntity.isPresent()) {
            UserEntity userEntity = optionalUserEntity.get();
            Optional<ListSellersEntity> listById = listSellersEntityRepository.findByUserId_Id(userEntity.getId());
            if(listById.isPresent()) {
                Optional<StorageEntity> storageEntityOptional = storageEntityRepository.findBySellerId_Id(listById.get().getId());
                if(storageEntityOptional.isPresent()) {
                    Optional<CyberwareEntity> cyberwareEntityOptional = cyberwareEntityRepository.findById(addStorageElementRequest.getCyberwareId());
                    if(cyberwareEntityOptional.isPresent()) {
                        if(!storageElementEntityRepository.existsByCyberwareId_IdAndStorageId_Id(cyberwareEntityOptional.get().getId(), storageEntityOptional.get().getId())) {
                            storageElementEntityRepository.save(new StorageElementEntity(storageEntityOptional.get(), cyberwareEntityOptional.get(), addStorageElementRequest.getCount(), 0.0, addStorageElementRequest.getPrice()));
                            return ResponseEntity.ok().build();
                        } else {
                            return ResponseEntity.status(400).body(new MessageResponse("Такая позиция существует, обновите её"));
                        }
                    } else {
                        return ResponseEntity.status(400).body(new MessageResponse("Имплант не найден"));
                    }
                } else {
                    return ResponseEntity.status(400).body(new MessageResponse("Хранилище не найдено"));
                }
            } else {
                return ResponseEntity.status(400).body(new MessageResponse("Пользователь не найден в таблице продавцов"));
            }
        } else {
            return ResponseEntity.status(400).body(new MessageResponse("Пользователь не найден"));
        }
    }

    @PostMapping("/deleteStorageElement")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> deleteStorageElement(@Valid @RequestBody DeleteStorageElementRequest deleteStorageElementRequest) {
        UserDetailsImpl principal = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<UserEntity> optionalUserEntity = userRepository.findById(principal.id());
        if (optionalUserEntity.isPresent()) {
            UserEntity userEntity = optionalUserEntity.get();
            Optional<ListSellersEntity> listById = listSellersEntityRepository.findByUserId_Id(userEntity.getId());
            if(listById.isPresent()) {
                Optional<StorageEntity> storageEntityOptional = storageEntityRepository.findBySellerId_Id(listById.get().getId());
                Optional<StorageElementEntity> optionalStorageElement = storageElementEntityRepository.findById(deleteStorageElementRequest.getStorageElementId());
                if(storageEntityOptional.isPresent() && optionalStorageElement.isPresent()) {
                    StorageElementEntity storageElementEntity = optionalStorageElement.get();
                    if(Objects.equals(storageElementEntity.getStorageId().getId(), storageEntityOptional.get().getId())) {
                        storageElementEntityRepository.delete(storageElementEntity);
                        return ResponseEntity.ok().build();
                    } else {
                        return ResponseEntity.status(400).body(new MessageResponse("Эта позиция вам не принадлежит"));
                    }
                } else {
                    return ResponseEntity.status(400).body(new MessageResponse("Хранилище или позция на складе не найдены"));
                }
            } else {
                return ResponseEntity.status(400).body(new MessageResponse("Пользователь не найден в таблице продавцов"));
            }
        } else {
            return ResponseEntity.status(400).body(new MessageResponse("Пользователь не найден"));
        }
    }

    @PostMapping("/updateStorageElement")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<?> updateStorageElement(@Valid @RequestBody UpdateStorageElementRequest updateStorageElementRequest) {
        UserDetailsImpl principal = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<UserEntity> optionalUserEntity = userRepository.findById(principal.id());
        if (optionalUserEntity.isPresent()) {
            UserEntity userEntity = optionalUserEntity.get();
            Optional<ListSellersEntity> listById = listSellersEntityRepository.findByUserId_Id(userEntity.getId());
            if(listById.isPresent()) {
                Optional<StorageEntity> storageEntityOptional = storageEntityRepository.findBySellerId_Id(listById.get().getId());
                Optional<StorageElementEntity> optionalStorageElement = storageElementEntityRepository.findById(updateStorageElementRequest.getStorageElementId());
                if(storageEntityOptional.isPresent() && optionalStorageElement.isPresent()) {
                    StorageElementEntity storageElementEntity = optionalStorageElement.get();
                    if(Objects.equals(storageElementEntity.getStorageId().getId(), storageEntityOptional.get().getId())) {
                        if(updateStorageElementRequest.getCount() != -1) {
                            storageElementEntity.setCount(updateStorageElementRequest.getCount());
                        }
                        if(updateStorageElementRequest.getPrice() != -1) {
                            storageElementEntity.setPrice(updateStorageElementRequest.getPrice());
                        }
                        storageElementEntityRepository.save(storageElementEntity);
                        return ResponseEntity.ok().build();
                    } else {
                        return ResponseEntity.status(400).body(new MessageResponse("Эта позиция вам не принадлежит"));
                    }
                } else {
                    return ResponseEntity.status(400).body(new MessageResponse("Хранилище или позция на складе не найдены"));
                }
            } else {
                return ResponseEntity.status(400).body(new MessageResponse("Пользователь не найден в таблице продавцов"));
            }
        } else {
            return ResponseEntity.status(400).body(new MessageResponse("Пользователь не найден"));
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        return ResponseEntity.status(HttpStatusCode.valueOf(400)).body(new MessageResponse(e.getMessage()));
    }
}
