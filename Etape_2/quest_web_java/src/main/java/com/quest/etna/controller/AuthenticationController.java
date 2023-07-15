package com.quest.etna.controller;

import com.quest.etna.model.User;
import com.quest.etna.model.UserDetails;
import com.quest.etna.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
public class AuthenticationController {

    @Autowired
    private UserRepository userRepository;
    @PostMapping ("/register")
    public ResponseEntity<UserDetails> register(@RequestBody User user) {

        if (user.getUsername() == null || user.getPassword() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Bad request");
        }
        Optional<User> toFind = userRepository.findByUsername(user.getUsername());
        if (toFind.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Duplicata");
        }
        userRepository.save(user);
        UserDetails userDetails = new UserDetails(user.getUsername());
            return new ResponseEntity<UserDetails>(userDetails, HttpStatus.CREATED);

    }
}
