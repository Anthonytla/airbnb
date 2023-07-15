package com.quest.etna.controller;

import com.quest.etna.enums.UserRole;
import com.quest.etna.model.Address;
import com.quest.etna.model.DeleteResponse;
import com.quest.etna.model.Review;
import com.quest.etna.model.User;
import com.quest.etna.repositories.AddressRepository;
import com.quest.etna.repositories.ReviewRepository;
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

    @Autowired
    private ReviewRepository reviewRepository;

    @GetMapping("/visitor")
    @CrossOrigin
    public ResponseEntity<Iterable<Address>> list4Visitor() {
        Iterable<Address> addresses = addressRepository.findAll();


        for (Address address : addresses) {
            Optional<List<Review>> reviews = reviewRepository.findByAddressId(address.getId());
            address.reviewsNb = reviews.get().size();
        }

        return ResponseEntity.ok(addresses);
    }

    @CrossOrigin
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
        Optional<List<Review>> reviews = reviewRepository.findByAddressId(id);
        address.get().setReviews(reviews.get());

        return ResponseEntity.ok(address.get());
    }

    @CrossOrigin
    @GetMapping("/visitor/{id}")
    public ResponseEntity<Address> getPublicAddress(@PathVariable int id) {
        Optional<Address> address = addressRepository.findById(id);
        if (!address.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Address not found");
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

    @CrossOrigin
    @PostMapping("")
    public ResponseEntity<Address> post(@RequestBody Address address) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userRepository.findByUsername(authentication.getName());
        if (user.get().getRole() != UserRole.ROLE_HOTE && user.get().getRole() != UserRole.ROLE_ADMIN) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You must sign in as a host to make propositions");
        }
        address.setUser(user.get());
        addressRepository.save(address);
        return new ResponseEntity<>(address, HttpStatus.CREATED);
    }
    @CrossOrigin
    @PutMapping("/{id}")
    public ResponseEntity<Address> put(@PathVariable int id, @RequestBody Address newAddress) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userRepository.findByUsername(authentication.getName());
        Optional<Address> toUpdate = addressRepository.findById(id);
        if (!toUpdate.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Address not found");
        }
        if (!user.isPresent() || (toUpdate.get().getUser().getId() != user.get().getId() && user.get().getRole() != UserRole.ROLE_ADMIN)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Forbidden");
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
        if (newAddress.getName() != null && !Objects.equals(newAddress.getName(), toUpdate.get().getName())) {
            toUpdate.get().setName(newAddress.getName());
            toSend.setName(newAddress.getName());
        }
        if (newAddress.getDescription() != null && !Objects.equals(newAddress.getDescription(), toUpdate.get().getDescription())) {
            toUpdate.get().setDescription(newAddress.getDescription());
            toSend.setDescription(newAddress.getDescription());
        }
        if (!Objects.equals(newAddress.getPrice(), toUpdate.get().getPrice())) {
            toUpdate.get().setPrice(newAddress.getPrice());
            toSend.setPrice(newAddress.getPrice());
        }
        if (newAddress.getImageData() != null && !Objects.equals(newAddress.getImageData(), toUpdate.get().getImageData())) {
            toUpdate.get().setImageData(newAddress.getImageData());
            toSend.setImageData(newAddress.getImageData());
        }
        //toUpdate.get().setUpdatedDate(new Date());
        addressRepository.save(toUpdate.get());
        return ResponseEntity.ok(toUpdate.get());
    }

    @CrossOrigin
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
