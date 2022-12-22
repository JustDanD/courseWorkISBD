package itmo.zavar.isbdcyberpunk.controllers;

import itmo.zavar.isbdcyberpunk.models.user.RoleEntity;
import itmo.zavar.isbdcyberpunk.models.user.UserEntity;
import itmo.zavar.isbdcyberpunk.models.user.UserRole;
import itmo.zavar.isbdcyberpunk.models.user.info.BillingEntity;
import itmo.zavar.isbdcyberpunk.models.user.list.ListCustomersEntity;
import itmo.zavar.isbdcyberpunk.models.user.list.ListSellersEntity;
import itmo.zavar.isbdcyberpunk.payload.request.RegisterRequest;
import itmo.zavar.isbdcyberpunk.payload.request.SignInRequest;
import itmo.zavar.isbdcyberpunk.payload.response.JwtResponse;
import itmo.zavar.isbdcyberpunk.payload.response.MessageResponse;
import itmo.zavar.isbdcyberpunk.repository.*;
import itmo.zavar.isbdcyberpunk.security.jwt.JwtUtils;
import itmo.zavar.isbdcyberpunk.security.services.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ListCustomersEntityRepository customersEntityRepository;

    @Autowired
    private ListSellersEntityRepository listSellersEntityRepository;

    @Autowired
    private BillingEntityRepository billingEntityRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder encoder;

    @Autowired
    private JwtUtils jwtUtils;

    @PostMapping("/signIn")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody SignInRequest signInRequest) {

        Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(signInRequest.getUsername(), signInRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        //ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);
        String jwt = jwtUtils.generateTokenFromUsername(userDetails.id());

        return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, jwt).body(new JwtResponse(jwt));
    }

    @PostMapping("/register")
    @Transactional
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {

        if (userRepository.existsByUsername(registerRequest.getUsername()))
            return ResponseEntity.status(HttpStatusCode.valueOf(400)).body(new MessageResponse("Имя пользователя уже занято"));

        UserEntity userEntity = new UserEntity(registerRequest.getUsername(), encoder.encode(registerRequest.getPassword()));

        RoleEntity roleEntity;

        try {
            UserRole userRole = UserRole.valueOf(registerRequest.getRole());
            roleEntity = roleRepository.findByName(userRole)
                    .orElseThrow(() -> new RuntimeException("Роль не найдена"));
            userEntity.setRole(roleEntity);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Роль не найдена");
        } catch (NullPointerException e) {
            roleEntity = roleRepository.findByName(UserRole.ROLE_CUSTOMER)
                    .orElseThrow(() -> new RuntimeException("Роль не найдена"));
            userEntity.setRole(roleEntity);
        }

        userRepository.save(userEntity);

        if(roleEntity.getName() == UserRole.ROLE_CUSTOMER) {
            BillingEntity billing = new BillingEntity();
            billing.setSum(4000000L);
            billingEntityRepository.save(billing);
            ListCustomersEntity listCustomersEntity = new ListCustomersEntity(userEntity, billing);
            customersEntityRepository.save(listCustomersEntity);
        } else if(roleEntity.getName() == UserRole.ROLE_SELLER) {
            BillingEntity billing = new BillingEntity();
            billing.setSum(10000000L);
            billingEntityRepository.save(billing);
            ListSellersEntity listSellersEntity = new ListSellersEntity(userEntity, billing);
            listSellersEntityRepository.save(listSellersEntity);
        }

        return ResponseEntity.ok().build();
    }

    @GetMapping("/isAuthenticated")
    public ResponseEntity<?> isAuthenticated() {
        if (SecurityContextHolder.getContext().getAuthentication() != null &&
                SecurityContextHolder.getContext().getAuthentication().isAuthenticated() &&
                !(SecurityContextHolder.getContext().getAuthentication()
                        instanceof AnonymousAuthenticationToken)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatusCode.valueOf(401)).build();
        }
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<?> handleException(Exception e) {
        return ResponseEntity.status(HttpStatusCode.valueOf(400)).body(new MessageResponse(e.getMessage()));
    }
}
