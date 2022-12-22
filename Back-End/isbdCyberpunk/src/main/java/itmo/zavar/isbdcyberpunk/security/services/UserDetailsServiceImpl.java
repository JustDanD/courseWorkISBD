package itmo.zavar.isbdcyberpunk.security.services;

import itmo.zavar.isbdcyberpunk.models.user.UserEntity;
import itmo.zavar.isbdcyberpunk.payload.response.MessageResponse;
import itmo.zavar.isbdcyberpunk.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Service
public class UserDetailsServiceImpl implements CyberpunkUserDetailsService {
    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

        return UserDetailsImpl.build(userEntity);
    }

    @Override
    @Transactional
    public UserDetails loadUserById(Long id) throws IllegalArgumentException {
        UserEntity userEntity = userRepository.findById(id)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found with id: " + id));

        return UserDetailsImpl.build(userEntity);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<?> handleException(Exception e) {
        return ResponseEntity.status(HttpStatusCode.valueOf(400)).body(new MessageResponse(e.getMessage()));
    }
}
