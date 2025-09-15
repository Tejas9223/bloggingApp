 package com.codewithdurgesh.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
 
import com.codewithdurgesh.entities.User;
import com.codewithdurgesh.exceptions.ResourceNotFoundException;
import com.codewithdurgesh.repositories.UserRepo;


@Service
public class CustomerUserDetailService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Fetch user by username 
    	
    	System.out.println("Loading user with email: " + email);
    	
        User user = userRepo.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User", "email "+ email, null));

        return user;
    }
}
