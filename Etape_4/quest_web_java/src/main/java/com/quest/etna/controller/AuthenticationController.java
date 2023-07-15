package com.quest.etna.controller;

import com.quest.etna.config.JwtTokenUtil;
import com.quest.etna.config.JwtUserDetailsService;
import com.quest.etna.model.*;
import com.quest.etna.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;
import java.security.Principal;
import java.util.Optional;

@RestController
public class AuthenticationController {


    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @PostMapping ("/register")
    public ResponseEntity<UserDetails> register(@RequestBody RegisterUserDto dto) {

        if (dto.getUsername() == null || dto.getPassword() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request");
        }
        Optional<User> toFind = userRepository.findByUsername(dto.getUsername());
        if (toFind.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Duplicata");
        }
        User user = new User();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        userRepository.save(user);
        UserDetails userDetails = new UserDetails(user.getUsername());
            return new ResponseEntity<UserDetails>(userDetails, HttpStatus.CREATED);

    }

    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody RegisterUserDto dto) {
        //Authentication aut = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword()));
        JwtUserDetails jwtUserDetails = jwtUserDetailsService.loadUserByUsername(dto.getUsername());
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getUsername(),dto.getPassword()));
        } catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Bad credentials");
        }
        AuthResponse response = new AuthResponse();
        response.setToken(jwtTokenUtil.generateToken(jwtUserDetails));
        return ResponseEntity.ok(response);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDetails> getUser(HttpServletRequest request) {
        /*Principal principal = request.getUserPrincipal();
        UserDetails userDetails = new UserDetails(principal.getName());*/
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Bad credentials");
        }
        String currentPrincipalName = authentication.getName();
        Optional<User> user = userRepository.findByUsername(currentPrincipalName);
        if (!user.isPresent()){
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authenticated");
        }
        UserDetails userDetails = new UserDetails(currentPrincipalName, user.get().getRole());
        return ResponseEntity.ok(userDetails);
    }
}
