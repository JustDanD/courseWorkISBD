package itmo.zavar.isbdcyberpunk.controllers;

import itmo.zavar.isbdcyberpunk.security.services.UserDetailsImpl;
import itmo.zavar.isbdcyberpunk.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private UserServiceImpl userService;

    @GetMapping("/user")
    @PreAuthorize("isAuthenticated()")
    public String userAccess() {
        return "User Content.";
    }

    @GetMapping("/det")
    @PreAuthorize("isAuthenticated()")
    public String det() {
        UserDetailsImpl principal = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        userService.loadUserDetails(principal);
        return "Det";
    }
}
