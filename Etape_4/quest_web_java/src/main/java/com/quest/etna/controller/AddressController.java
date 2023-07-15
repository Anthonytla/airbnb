package com.quest.etna.controller;

import com.quest.etna.enums.UserRole;
import com.quest.etna.model.Address;
import com.quest.etna.model.DeleteResponse;
import com.quest.etna.model.User;
import com.quest.etna.repositories.AddressRepository;
import com.quest.etna.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.swing.text.html.Option;
import java.util.*;


@RestController
@RequestMapping("/address")
public class AddressController {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/{id}")
    public ResponseEntity<Address> get(@PathVariable int id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userRepository.findByUsername(authentication.getName());
        Optional<Address> address = addressRepository.findById(id);
        if (!address.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Address not found");
        }
        if (!user.isPresent()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authenticated");
        }
        if (address.get().getUser().getId() != user.get().getId() && user.get().getRole() != UserRole.ROLE_ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Unauthorized");
        }
        return ResponseEntity.ok(address.get());

    }

    @GetMapping("")
    public ResponseEntity<Iterable<Address>> list() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userRepository.findByUsername(authentication.getName());
        if (!user.isPresent()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authenticated");
        }
        if (user.get().getRole() == UserRole.ROLE_ADMIN) {
            Iterable<Address> addresses = addressRepository.findAll();
            return ResponseEntity.ok(addresses);
        }
        else {
            Optional<List<Address>> addresses = addressRepository.findByUser(user.get());
            return ResponseEntity.ok(addresses.get());
        }

    }

    @PostMapping("")
    public ResponseEntity<Address> post(@RequestBody Address address) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userRepository.findByUsername(authentication.getName());
        address.setUser(user.get());
        addressRepository.save(address);
        return new ResponseEntity<>(address, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Address> put(@PathVariable int id, @RequestBody Address newAddress) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userRepository.findByUsername(authentication.getName());
        Optional<Address> toUpdate = addressRepository.findById(id);
        if (!toUpdate.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Address not found");
        }
        if (!user.isPresent() || (toUpdate.get().getUser().getId() != user.get().getId() && user.get().getRole() != UserRole.ROLE_ADMIN)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Unauthorized");
        }
        Address toSend = new Address();

        if (newAddress.getStreet() != null && !Objects.equals(newAddress.getStreet(), toUpdate.get().getStreet())) {
            toUpdate.get().setStreet(newAddress.getStreet());
            toSend.setStreet(newAddress.getStreet());
        }
        if (newAddress.getCity() != null && !Objects.equals(newAddress.getCity(), toUpdate.get().getCity())) {
            toUpdate.get().setCity(newAddress.getCity());
            toSend.setCity(newAddress.getCity());
        }
        if (newAddress.getPostalCode() != null && !Objects.equals(newAddress.getPostalCode(), toUpdate.get().getPostalCode())) {
            toUpdate.get().setPostalCode(newAddress.getPostalCode());
            toSend.setPostalCode(newAddress.getPostalCode());
        }
        if (newAddress.getCountry() != null && !Objects.equals(newAddress.getCountry(), toUpdate.get().getCountry())) {
            toUpdate.get().setCountry(newAddress.getCountry());
            toSend.setCountry(newAddress.getCountry());
        }
        toUpdate.get().setUpdatedDate(new Date());
        addressRepository.save(toUpdate.get());
        return ResponseEntity.ok(toSend);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<DeleteResponse> delete(@PathVariable int id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userRepository.findByUsername(authentication.getName());
        Optional<Address> address = addressRepository.findById(id);
        DeleteResponse response = new DeleteResponse();

        if (!user.isPresent()) {
            response.setSuccess(false);
            return new ResponseEntity(response, HttpStatus.UNAUTHORIZED);
        }
        if (!address.isPresent())  {
            response.setSuccess(false);
            return new ResponseEntity(response, HttpStatus.NOT_FOUND);
        }
        if ((address.get().getUser().getId() != user.get().getId() && user.get().getRole() != UserRole.ROLE_ADMIN)) {
            response.setSuccess(false);
            return new ResponseEntity(response, HttpStatus.FORBIDDEN);
        }


        addressRepository.delete(address.get());
        response.setSuccess(true);
        return ResponseEntity.ok(response);
    }

}
