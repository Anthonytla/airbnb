package com.quest.etna.controller;

import com.quest.etna.enums.UserRole;
import com.quest.etna.model.*;
import com.quest.etna.repositories.AddressRepository;
import com.quest.etna.repositories.ReservationRepository;
import com.quest.etna.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
public class ReservationController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private ReservationRepository reservationRepository;

    @CrossOrigin
    @PostMapping("/address/{address_id}/reservation")
    public ResponseEntity<Reservation> post(@RequestBody Reservation reservation, @PathVariable int address_id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> me = userRepository.findByUsername(authentication.getName());
        if (!me.isPresent()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authenticated");
        }

        Optional<Address> address = addressRepository.findById(address_id);
        if (!address.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This address does not exist...");
        }
        Date now = new Date();
        if (reservation.getEnding_date().toInstant().isBefore(reservation.getStarting_date().toInstant())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid date range");
        }
        if (reservation.getEnding_date().toInstant().isBefore(now.toInstant())) {
            throw new ResponseStatusException(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS, "This slot is unavailable");
        }
        Optional<List<Reservation>> alreadyReserved = reservationRepository.findByAddressId(address_id);
        for (int i = 0; i < alreadyReserved.get().size(); i++) {

            if (reservation.getEnding_date().toInstant().isAfter(alreadyReserved.get().get(i).getStarting_date().toInstant())
            && reservation.getStarting_date().toInstant().isBefore(alreadyReserved.get().get(i).getEnding_date().toInstant())){
                throw new ResponseStatusException(HttpStatus.CONFLICT, "This slot has already been taken");
            }
        };
        reservation.setUser(me.get());
        reservation.setAddress(address.get());

        reservationRepository.save(reservation);
        return new ResponseEntity<>(reservation, HttpStatus.CREATED);
    }

    @CrossOrigin
    @GetMapping("/address/{address_id}/reservation/user")
    public ResponseEntity<Iterable<Reservation>> getByAddressAndUser(@PathVariable int address_id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> me = userRepository.findByUsername(authentication.getName());
        if (!me.isPresent()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authenticated");
        }
        Optional<Address> address = addressRepository.findById(address_id);
        if (!address.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This address does not exist...");
        }
        Optional<List<Reservation>> reservations = reservationRepository.findByUserIdAndAddressId(me.get().getId(), address_id);
        return ResponseEntity.ok(reservations.get());
    }

    @CrossOrigin
    @GetMapping("/user/reservation")
    public ResponseEntity<Iterable<Reservation>> getByUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> me = userRepository.findByUsername(authentication.getName());
        if (!me.isPresent()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authenticated");
        }
        Optional<List<Reservation>> reservations = reservationRepository.findByUserId(me.get().getId());
        return ResponseEntity.ok(reservations.get());
    }

    @CrossOrigin
    @GetMapping("/address/{address_id}/reservation")
    public ResponseEntity<Iterable<Reservation>> get(@PathVariable int address_id) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> me = userRepository.findByUsername(authentication.getName());
        if (!me.isPresent()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authenticated");
        }
        Optional<Address> address = addressRepository.findById(address_id);
        if (!address.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This address does not exist...");
        }
        Optional<List<Reservation>> reservations = reservationRepository.findByAddressId(address_id);
        return ResponseEntity.ok(reservations.get());
    }

    @CrossOrigin
    @PutMapping("/address/{address_id}/reservation/{reservation_id}")
    public ResponseEntity<Reservation> put(@PathVariable int address_id, @PathVariable int reservation_id, @RequestBody Reservation newReservation) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> me = userRepository.findByUsername(authentication.getName());
        if (!me.isPresent()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Not authenticated");
        }
        Optional<Address> address = addressRepository.findById(address_id);
        if (!address.isPresent()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "This address does not exist...");
        }
        Date now = new Date();
        if (newReservation.getEnding_date().toInstant().isBefore(newReservation.getStarting_date().toInstant())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid date range");
        }
        if (newReservation.getEnding_date().toInstant().isBefore(now.toInstant())) {
            throw new ResponseStatusException(HttpStatus.UNAVAILABLE_FOR_LEGAL_REASONS, "This slot is unavailable");
        }
        Optional<List<Reservation>> alreadyReserved = reservationRepository.findByAddressId(address_id);
        Optional<Reservation> reservation = reservationRepository.findById(reservation_id);

        if (reservation.get().getUser().getUsername() != me.get().getUsername()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You haven't reserved this reservation");
        }
        for (int i = 0; i < alreadyReserved.get().size(); i++) {

            if( reservation.get().getId() == alreadyReserved.get().get(i).getId()) {continue;}
            if (newReservation.getEnding_date().toInstant().isAfter(alreadyReserved.get().get(i).getStarting_date().toInstant())
                    && newReservation.getStarting_date().toInstant().isBefore(alreadyReserved.get().get(i).getEnding_date().toInstant())){
                throw new ResponseStatusException(HttpStatus.CONFLICT, "This slot has already been taken");
            }
        };

        if (newReservation.getStarting_date() != null) {
            reservation.get().setStarting_date(newReservation.getStarting_date());
        }
        if (newReservation.getEnding_date() != null) {
            reservation.get().setEnding_date(newReservation.getEnding_date());
        }
        reservationRepository.save(reservation.get());
        return ResponseEntity.ok(reservation.get());
    }

    @CrossOrigin
    @DeleteMapping("/address/{address_id}/reservation/{reservation_id}")
    public ResponseEntity<DeleteResponse> delete(@PathVariable int address_id, @PathVariable int reservation_id) {
        DeleteResponse response = new DeleteResponse();
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<User> me = userRepository.findByUsername(authentication.getName());
        if (!me.isPresent()) {
            response.setSuccess(false);
            return new ResponseEntity(response, HttpStatus.UNAUTHORIZED);
        }
        Optional<Reservation> toDelete = reservationRepository.findById(reservation_id);
        if (!toDelete.isPresent()) {
            response.setSuccess(false);
            return new ResponseEntity(response, HttpStatus.NOT_FOUND);
        }
        if (me.get().getRole() != UserRole.ROLE_ADMIN && !toDelete.get().getUser().getUsername().equals(authentication.getName())) {
            response.setSuccess(false);
            return new ResponseEntity(response, HttpStatus.FORBIDDEN);
        }
        response.setSuccess(true);
        reservationRepository.delete(toDelete.get());
        return ResponseEntity.ok(response);

    }


}
