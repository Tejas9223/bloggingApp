package com.codewithdurgesh.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

import com.codewithdurgesh.exceptions.ApiException;
import com.codewithdurgesh.payloads.JwtAuthRequest;
import com.codewithdurgesh.payloads.UserDto;
import com.codewithdurgesh.security.JwtAuthResponse;
import com.codewithdurgesh.security.JwtTokenHelper;
import com.codewithdurgesh.services.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Authentication APIs", description = "APIs for user login and registration")
@RestController
@RequestMapping("/api/v1/auth/")
public class AuthController {

    @Autowired
    private JwtTokenHelper jwtTokenHelper;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    @Operation(summary = "Login user and return JWT token")
    @PostMapping("/login")
    public ResponseEntity<JwtAuthResponse> createToken(@RequestBody JwtAuthRequest request) {
        this.authenticate(request.getUsername(), request.getPassword());

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        String token = jwtTokenHelper.generateToken(userDetails);

        JwtAuthResponse response = new JwtAuthResponse();
        response.setToken(token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    private void authenticate(String username, String password) {
        UsernamePasswordAuthenticationToken authenticationToken =
                new UsernamePasswordAuthenticationToken(username, password);

        try {
            this.authenticationManager.authenticate(authenticationToken);
        } catch (BadCredentialsException e) {
            throw new ApiException("Invalid Username or Password");
        }
    }

    // ✅ Register New Normal User (For everyone)
    @PostMapping("/register")
    public ResponseEntity<UserDto> register(@RequestBody UserDto userDto) {
        // Forcefully assign ROLE_USER to all public registrations
        userDto.setRoleName("ROLE_USER");

        UserDto registeredUser = userService.registerNewUser(userDto);
        return new ResponseEntity<>(registeredUser, HttpStatus.CREATED);
    }

    // ✅ Admin Creation Endpoint (Postman only, one-time setup)
    @PostMapping("/create-admin")
    public ResponseEntity<?> createAdmin(@RequestBody UserDto userDto) {
        if (userService.adminExists()) {
            return new ResponseEntity<>("Admin already exists!", HttpStatus.BAD_REQUEST);
        }

        // Forcefully assign ROLE_ADMIN
        userDto.setRoleName("ROLE_ADMIN");

        UserDto adminUser = userService.registerNewUser(userDto);
        return new ResponseEntity<>(adminUser, HttpStatus.CREATED);
    }
}
