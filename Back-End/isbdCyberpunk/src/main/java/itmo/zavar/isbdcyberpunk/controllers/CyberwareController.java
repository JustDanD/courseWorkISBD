package itmo.zavar.isbdcyberpunk.controllers;

import itmo.zavar.isbdcyberpunk.models.cyberware.CyberwareEntity;
import itmo.zavar.isbdcyberpunk.models.shop.storage.ReviewEntity;
import itmo.zavar.isbdcyberpunk.models.shop.storage.StorageElementEntity;
import itmo.zavar.isbdcyberpunk.models.user.list.ListCustomersEntity;
import itmo.zavar.isbdcyberpunk.payload.request.AddReviewRequest;
import itmo.zavar.isbdcyberpunk.payload.request.GetCyberwareDetailsRequest;
import itmo.zavar.isbdcyberpunk.payload.request.RemoveFromCartRequest;
import itmo.zavar.isbdcyberpunk.payload.response.GetCyberwareDetailsResponse;
import itmo.zavar.isbdcyberpunk.payload.response.GetReviewResponse;
import itmo.zavar.isbdcyberpunk.payload.response.MessageResponse;
import itmo.zavar.isbdcyberpunk.repository.CyberwareEntityRepository;
import itmo.zavar.isbdcyberpunk.repository.ListCustomersEntityRepository;
import itmo.zavar.isbdcyberpunk.repository.ReviewEntityRepository;
import itmo.zavar.isbdcyberpunk.repository.StorageElementEntityRepository;
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
@RequestMapping("/api/cyberware")
public class CyberwareController {

    @Autowired
    private ReviewEntityRepository reviewEntityRepository;

    @Autowired
    private ListCustomersEntityRepository customersEntityRepository;

    @Autowired
    private StorageElementEntityRepository storageElementEntityRepository;

    @Autowired
    private CyberwareEntityRepository cyberwareEntityRepository;

    @PostMapping("/getReviews")
    @PreAuthorize("isAuthenticated()")
    @Transactional
    public ResponseEntity<List<GetReviewResponse>> getReviews(@Valid @RequestBody RemoveFromCartRequest getReviewRequest) {
        UserDetailsImpl principal = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        List<ReviewEntity> list = reviewEntityRepository.findByStorageElement_Id(getReviewRequest.getStorageElementId());
        List<GetReviewResponse> output = new ArrayList<>();
        list.forEach(reviewEntity -> {
            output.add(new GetReviewResponse(reviewEntity.getListCustomersEntity().getId(), reviewEntity.getListCustomersEntity().getUserId().getUsername(),
                    reviewEntity.getReview(), reviewEntity.getRating()));
        });
        return ResponseEntity.ok(output);
    }

    @PostMapping("/postReview")
    @PreAuthorize("isAuthenticated()")
    @Transactional
    public ResponseEntity<?> postReview(@Valid @RequestBody AddReviewRequest addReviewRequest) {
        UserDetailsImpl principal = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<ListCustomersEntity> customer = customersEntityRepository.findByUserId_Id(principal.id());
        Optional<StorageElementEntity> element = storageElementEntityRepository.findById(addReviewRequest.getId());
        if(customer.isPresent() && element.isPresent()) {
            ReviewEntity reviewEntity = new ReviewEntity(element.get(), customer.get(), addReviewRequest.getReviewText(), addReviewRequest.getRating());
            reviewEntityRepository.save(reviewEntity);
        } else {
            return ResponseEntity.status(400).build();
        }

        return ResponseEntity.ok().build();
    }

    @PostMapping("/getCyberwareDetails")
    @PreAuthorize("isAuthenticated()")
    @Transactional
    public ResponseEntity<?> getCyberwareDetails(@Valid @RequestBody GetCyberwareDetailsRequest getCyberwareDetailsRequest) {
        Optional<StorageElementEntity> storageElementOp = storageElementEntityRepository.findById(getCyberwareDetailsRequest.getStorageElementId());

        if(storageElementOp.isPresent()) {
            StorageElementEntity storageElementEntity = storageElementOp.get();
            CyberwareEntity cyberware = storageElementEntity.getCyberwareId();
            return ResponseEntity.ok(new GetCyberwareDetailsResponse(cyberware, storageElementEntity.getRating(), storageElementEntity.getId()));
        } else {
            return ResponseEntity.status(400).body(new MessageResponse("Позиция не найдена"));
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        return ResponseEntity.status(HttpStatusCode.valueOf(400)).body(new MessageResponse(e.getMessage()));
    }

}
