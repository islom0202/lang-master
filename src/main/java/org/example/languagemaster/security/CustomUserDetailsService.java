package org.example.languagemaster.security;


import lombok.RequiredArgsConstructor;
import org.example.languagemaster.entity.Users;
import org.example.languagemaster.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository auth;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        final Users userAuth = auth.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return new UsersDetails(userAuth);
    }
}