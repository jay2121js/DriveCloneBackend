package com.jay21213.googledrivebd.Services;


import com.jay21213.googledrivebd.Config.CustomUserDetails;
import com.jay21213.googledrivebd.Entity.User;
import com.jay21213.googledrivebd.reprository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserDetailService implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepo.findByEmail(email);

        if (user == null) {
            throw new UsernameNotFoundException("User not found: " + email);
        }

        // Build authorities (assuming role already contains ROLE_)
        List<GrantedAuthority> authorities =
                List.of(new SimpleGrantedAuthority(user.getRole()));

        return new CustomUserDetails(user, authorities);
    }
}
