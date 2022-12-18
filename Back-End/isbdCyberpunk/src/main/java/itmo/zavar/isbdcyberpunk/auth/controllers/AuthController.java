package itmo.zavar.isbdcyberpunk.auth.controllers;

import itmo.zavar.isbdcyberpunk.auth.models.RoleEntity;
import itmo.zavar.isbdcyberpunk.auth.models.User;
import itmo.zavar.isbdcyberpunk.auth.models.UserRole;
import itmo.zavar.isbdcyberpunk.auth.payload.request.RegisterRequest;
import itmo.zavar.isbdcyberpunk.auth.payload.request.SignInRequest;
import itmo.zavar.isbdcyberpunk.auth.payload.response.MessageResponse;
import itmo.zavar.isbdcyberpunk.auth.payload.response.UserInfoResponse;
import itmo.zavar.isbdcyberpunk.auth.repository.RoleRepository;
import itmo.zavar.isbdcyberpunk.auth.repository.UserRepository;
import itmo.zavar.isbdcyberpunk.auth.security.jwt.JwtUtils;
import itmo.zavar.isbdcyberpunk.auth.security.services.UserDetailsImpl;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

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

        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

//        List<String> roles = userDetails.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority).toList();

        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, jwtCookie.toString()).build();
//                .body(new UserInfoResponse(userDetails.getId(),
//                        userDetails.getUsername(),
//                        roles.get(0)));
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        if (userRepository.existsByUsername(registerRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        User user = new User(registerRequest.getUsername(),
                encoder.encode(registerRequest.getPassword()));

        String strRole = registerRequest.getRole();

        if (strRole == null) {
            RoleEntity customerRole = roleRepository.findByName(UserRole.ROLE_CUSTOMER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            user.setRole(customerRole);
        } else {
            switch (strRole) {
                case "ROLE_CUSTOMER" -> {
                    RoleEntity customerRole = roleRepository.findByName(UserRole.ROLE_CUSTOMER)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    user.setRole(customerRole);
                }
                case "ROLE_ADMIN" -> {
                    RoleEntity adminRole = roleRepository.findByName(UserRole.ROLE_ADMIN)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    user.setRole(adminRole);
                }
                default -> {
                    RoleEntity sellerRole = roleRepository.findByName(UserRole.ROLE_SELLER)
                            .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                    user.setRole(sellerRole);
                }
            }
        }
        userRepository.save(user);

        return ResponseEntity.ok().build();
    }

    @PostMapping("/logOut")
    public ResponseEntity<?> logoutUser() {
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).build();
    }

    @PostMapping("/isAuthenticated")
    public ResponseEntity<?> isAuthenticated() {
        if(SecurityContextHolder.getContext().getAuthentication() != null &&
                SecurityContextHolder.getContext().getAuthentication().isAuthenticated() &&
                !(SecurityContextHolder.getContext().getAuthentication()
                        instanceof AnonymousAuthenticationToken)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.status(HttpStatusCode.valueOf(401)).build();
        }
    }
}
