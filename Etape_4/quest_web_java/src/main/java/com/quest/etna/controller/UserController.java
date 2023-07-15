package com.quest.etna.controller;

import com.quest.etna.enums.UserRole;
import com.quest.etna.model.*;
import com.quest.etna.repositories.UserRepository;
import org.apache.catalina.UserDatabase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/user")
public class UserController implements Serializable {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("")
    public ResponseEntity<Iterable<User>> list() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> me = userRepository.findByUsername(authentication.getName());
        if (!me.isPresent()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authenticated");
        }
        Iterable<User> users = userRepository.findAll();
        /*List<UserResponse> userDetails = new ArrayList<>();
        for (User user : users) {
            UserResponse userResponse = new UserResponse(user.getUsername(), user.getRole());
            userResponse.setId(user.getId());
            userResponse.setCreationDate(user.getCreatedAt());
            userResponse.setUpdatedDate(user.getUpdatedAt());
            userDetails.add(userResponse);
        }*/
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> get(@PathVariable int id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> me = userRepository.findByUsername(authentication.getName());
        if (!me.isPresent()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authenticated");
        }
        Optional<User> user = userRepository.findById(id);
        if (!user.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        /*UserResponse userResponse = new UserResponse(user.get().getUsername(), user.get().getRole());
        userResponse.setId(user.get().getId());
        userResponse.setCreationDate(user.get().getCreatedAt());
        userResponse.setUpdatedDate(user.get().getUpdatedAt());*/
        return ResponseEntity.ok(user.get() );
    }

    @PutMapping("/{id}")
    public ResponseEntity<User> put(@PathVariable int id, @RequestBody UserDetails newUser) {
        Optional<User> user = userRepository.findById(id);
        if (!user.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> me = userRepository.findByUsername(authentication.getName());
        if (!me.isPresent()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authenticated");
        }
        if (me.get().getId() != id && me.get().getRole() != UserRole.ROLE_ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You can't modify other user's information");
        }
        // UserDetails toSend = new UserDetails();
        if (newUser.getUsername() != null && !Objects.equals(user.get().getUsername(), newUser.getUsername())) {
            if (userRepository.findByUsername(newUser.getUsername()).isPresent()) {
                throw new ResponseStatusException(HttpStatus.CONFLICT, "This username already exist");
            }
            // toSend.setUsername(newUser.getUsername());
            user.get().setUsername(newUser.getUsername());
        }
        if (newUser.getRole() != null && !Objects.equals(user.get().getRole(), newUser.getRole())) {
            // toSend.setRole(newUser.getRole());
            user.get().setRole(newUser.getRole());
        }
        userRepository.save(user.get());
        return ResponseEntity.ok(user.get());


    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteResponse> delete(@PathVariable int id) {
        Optional<User> toDelete = userRepository.findById(id);
        DeleteResponse response = new DeleteResponse();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> me = userRepository.findByUsername(authentication.getName());
        if (!me.isPresent()) {
            response.setSuccess(false);
            return new ResponseEntity(response, HttpStatus.UNAUTHORIZED);
        }
        if (!toDelete.isPresent()) {
            response.setSuccess(false);
            return new ResponseEntity(response, HttpStatus.NOT_FOUND);
        }
        if (me.get().getRole() != UserRole.ROLE_ADMIN) {
            response.setSuccess(false);
            return new ResponseEntity(response, HttpStatus.FORBIDDEN);
        }

        response.setSuccess(true);
        userRepository.delete(toDelete.get());
        return ResponseEntity.ok(response);
    }
}
