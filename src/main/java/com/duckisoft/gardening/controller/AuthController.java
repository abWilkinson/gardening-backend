package com.duckisoft.gardening.controller;

import com.duckisoft.gardening.dto.auth.JwtRequest;
import com.duckisoft.gardening.dto.auth.JwtResponse;
import com.duckisoft.gardening.dto.auth.RegisterRequest;
import com.duckisoft.gardening.exception.InvalidCredentialsException;
import com.duckisoft.gardening.exception.UserDisabledException;
import com.duckisoft.gardening.service.UserDetailsServiceImpl;
import com.duckisoft.gardening.util.JwtTokenUtil;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@AllArgsConstructor
public class AuthController {
    private AuthenticationManager authenticationManager;

    private JwtTokenUtil jwtTokenUtil;

    private UserDetailsServiceImpl userDetailsService;

    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @PostMapping(value = "/jwt")
    public ResponseEntity<JwtResponse> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {

        authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

        final String token = jwtTokenUtil.generateToken(authenticationRequest.getUsername());

        return ResponseEntity.ok(new JwtResponse(token));
    }

    @PostMapping(value = "/register")
    public ResponseEntity<JwtResponse> register(@RequestBody RegisterRequest registerRequest) {
        registerRequest.setPassword(bCryptPasswordEncoder.encode(registerRequest.getPassword()));
        final UserDetails userDetails = userDetailsService.register(registerRequest);

        final String token = jwtTokenUtil.generateToken(userDetails.getUsername());

        return ResponseEntity.ok(new JwtResponse(token));
    }


    private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new UserDisabledException();
        } catch (BadCredentialsException e) {
            throw new InvalidCredentialsException();
        }
    }
}
