package com.quest.etna.controller;

import com.quest.etna.enums.UserRole;
import com.quest.etna.model.*;
import com.quest.etna.repositories.AddressRepository;
import com.quest.etna.repositories.ReservationRepository;
import com.quest.etna.repositories.ReviewRepository;
import com.quest.etna.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
public class ReviewController {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ReservationRepository reservationRespository;
    @Autowired
    private AddressRepository addressRepository;

    @CrossOrigin
    @PostMapping("address/{address_id}/review")
    public ResponseEntity<Review> post(@RequestBody Review review, @PathVariable int address_id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> me = userRepository.findByUsername(authentication.getName());
        if (!me.isPresent()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authenticated");
        }
        Optional<Address> address = addressRepository.findById(address_id);
        if (!address.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This address does not exist...");
        }
        Optional<List<Reservation>> reservations = reservationRespository.findByUserIdAndAddressId(me.get().getId(), address_id);
        if (reservations.get().size()==0) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You haven't reserved this address");
        }

        float mean = 0;
        mean = (review.getServices() + review.getQualityPrice() + review.getCleanliness()) / 3f;
        review.setNote(mean);
        review.setAddress(address.get());
        review.setUser(me.get());
        reviewRepository.save(review);
        Address reviewAddress = review.getAddress();
        Optional<List<Review>> reviews = reviewRepository.findByAddressId(reviewAddress.getId());
        float note = 0;
        for (Review m : reviews.get()){
            note += m.getNote();
        }
        reviewAddress.setNote(note/reviews.get().size());
        addressRepository.save(reviewAddress);
        return new ResponseEntity<>(review, HttpStatus.CREATED);
    }

    @CrossOrigin
    @GetMapping("address/visitor/{address_id}/review")
    public ResponseEntity<List<Review>> findAll(@PathVariable int address_id) {
        Optional<Address> address = addressRepository.findById(address_id);
        if (!address.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This address does not exist...");
        }

        Optional<List<Review>> reviews = reviewRepository.findByAddressId(address_id);
        return ResponseEntity.ok(reviews.get());
    }

    @CrossOrigin
    @DeleteMapping("/review/{review_id}")
    public ResponseEntity<DeleteResponse> delete(@PathVariable int review_id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> user = userRepository.findByUsername(authentication.getName());
        Optional<Review> review = reviewRepository.findById(review_id);
        DeleteResponse response = new DeleteResponse();

        if (!user.isPresent()) {
            response.setSuccess(false);
            return new ResponseEntity(response, HttpStatus.UNAUTHORIZED);
        }
        if (!review.isPresent())  {
            response.setSuccess(false);
            return new ResponseEntity(response, HttpStatus.NOT_FOUND);
        }
        if ((review.get().getUser().getId() != user.get().getId() && user.get().getRole() != UserRole.ROLE_ADMIN)) {
            response.setSuccess(false);
            return new ResponseEntity(response, HttpStatus.FORBIDDEN);
        }
        Address reviewAddress = review.get().getAddress();
        reviewRepository.delete(review.get());
        Optional<List<Review>> reviews = reviewRepository.findByAddressId(reviewAddress.getId());
        float note = 0;
        for (Review m : reviews.get()){
            note += m.getNote();
        }
        reviewAddress.setNote(reviews.get().size()>0 ? note/reviews.get().size() : 0);
        addressRepository.save(reviewAddress);
        response.setSuccess(true);
        return ResponseEntity.ok(response);
    }

    @CrossOrigin
    @PutMapping("/review/{review_id}")
    public ResponseEntity<Review> put(@RequestBody Review newReview, @PathVariable int review_id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> me = userRepository.findByUsername(authentication.getName());
        if (!me.isPresent()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authenticated");
        }
        Optional<Review> reviewOptional = reviewRepository.findById(review_id);
        if (!reviewOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This review does not exist...");
        }
        Review review = reviewOptional.get();
        if (review.getUser().getId() != me.get().getId()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not authorized to edit this review !");
        }
        review.setCommentaire(newReview.getCommentaire());
        review.setServices(newReview.getServices());
        review.setQualityPrice(newReview.getQualityPrice());
        review.setCleanliness(newReview.getCleanliness());
        float mean = 0;
        mean = (newReview.getServices() + newReview.getQualityPrice() + newReview.getCleanliness()) / 3f;
        review.setNote(mean);
        Address address = review.getAddress();
        Optional<List<Review>> reviews = reviewRepository.findByAddressId(address.getId());
        float note = 0;
        for (Review m : reviews.get()){
            note += m.getNote();
        }
        address.setNote(note/reviews.get().size());
        addressRepository.save(address);
        reviewRepository.save(review);
        return new ResponseEntity<>(review, HttpStatus.OK);
    }

}
