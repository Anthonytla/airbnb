package com.quest.etna.controller;

import com.quest.etna.config.JwtTokenUtil;
import com.quest.etna.config.JwtUserDetailsService;
import com.quest.etna.model.*;
import com.quest.etna.repositories.AddressRepository;
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
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class AuthenticationController {


    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;
    @Autowired
    private JwtUserDetailsService jwtUserDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;


    @CrossOrigin
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
        user.setRole(dto.getRole());
        userRepository.save(user);
        UserDetails userDetails = new UserDetails(user.getUsername(), user.getRole());
            return new ResponseEntity<UserDetails>(userDetails, HttpStatus.CREATED);

    }
    @CrossOrigin
    @PostMapping("/authenticate")
    public ResponseEntity<AuthResponse> authenticate(@RequestBody RegisterUserDto dto) {
        //Authentication aut = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword()));
        long beforeUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
        int a = 12;
        int b = 12;
        int v = 12;
        int c = 12;
        int f = 12;
        int y = 12;
        long afterUsedMem=Runtime.getRuntime().totalMemory()-Runtime.getRuntime().freeMemory();
        JwtUserDetails jwtUserDetails = jwtUserDetailsService.loadUserByUsername(dto.getUsername());
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(dto.getUsername(),dto.getPassword()));
        } catch (BadCredentialsException e) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Bad credentials");
        }
        AuthResponse response = new AuthResponse();
        response.setToken(jwtTokenUtil.generateToken(jwtUserDetails));

        long actualMemUsed=afterUsedMem-beforeUsedMem;
        System.out.println("Used Memory   :  " + (Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory()) + " bytes");
        System.out.println("Free Memory   : " + Runtime.getRuntime().freeMemory() + " bytes");
        System.out.println("Total Memory  : " + Runtime.getRuntime().totalMemory() + " bytes");
        System.out.println("Max Memory    : " + Runtime.getRuntime().maxMemory() + " bytes");
        System.out.println(beforeUsedMem);
        System.out.println(afterUsedMem);
        System.out.println(afterUsedMem-beforeUsedMem);
        return ResponseEntity.ok(response);
    }

    @CrossOrigin
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
/*
        UserDetails userDetails = new UserDetails(currentPrincipalName, user.get().getRole());
*/
        UserResponse userDto = new UserResponse(currentPrincipalName, user.get().getRole());
        userDto.setId(user.get().getId());
        userDto.setCreatedAt(user.get().getCreatedAt());
        userDto.setUpdatedAt(user.get().getUpdatedAt());
        Optional<List<Address>> addresses = addressRepository.findByUser(user.get());
        List<AddressDto> addressesDto = addresses.get().stream().map((address -> new AddressDto(address.getId(), address.getStreet(), address.getPostalCode(), address.getCity(), address.getCountry(),address.getDescription(), address.getName(),address.getPrice(),address.getImageData()))).collect(Collectors.toList());
        userDto.setAddresses(addressesDto);
        /*UserResponse userResponse = new UserResponse(user.get().getUsername(), user.get().getRole());
        userResponse.setId(user.get().getId());
        userResponse.setCreationDate(user.get().getCreatedAt());
        userResponse.setUpdatedDate(user.get().getUpdatedAt());*/
        return ResponseEntity.ok(userDto);
    }
}
