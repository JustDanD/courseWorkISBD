package itmo.zavar.isbdcyberpunk.auth.security.services;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public interface CyberpunkUserDetailsService extends UserDetailsService {

    UserDetails loadUserById(Long id) throws IllegalArgumentException;
}
