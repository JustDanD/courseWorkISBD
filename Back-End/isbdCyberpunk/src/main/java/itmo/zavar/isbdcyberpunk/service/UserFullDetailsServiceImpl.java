package itmo.zavar.isbdcyberpunk.service;

import itmo.zavar.isbdcyberpunk.models.user.UserEntity;
import itmo.zavar.isbdcyberpunk.models.user.info.BillingEntity;
import itmo.zavar.isbdcyberpunk.models.user.list.ListCustomersEntity;
import itmo.zavar.isbdcyberpunk.repository.ListCustomersEntityRepository;
import itmo.zavar.isbdcyberpunk.repository.UserRepository;
import itmo.zavar.isbdcyberpunk.security.services.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class UserFullDetailsServiceImpl {

    @Autowired
    private ListCustomersEntityRepository customersEntityRepository;

    @Autowired
    private UserRepository userRepository;

    @Transactional
    public void loadUserDetails(UserDetailsImpl principal) {
        //UserDetailsImpl principal = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Optional<UserEntity> byId = userRepository.findById(principal.id());
        byId.ifPresent(userEntity -> {
            Optional<ListCustomersEntity> listById = customersEntityRepository.findByUserId_Id(userEntity.getId());
            listById.ifPresent(listCustomersEntity -> {
                BillingEntity billing = listCustomersEntity.getBillingId();
                System.out.println(billing.getSum());
            });
        });

    }

}
