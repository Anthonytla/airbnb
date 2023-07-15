package com.quest.etna.repositories;

import com.quest.etna.model.Address;
import com.quest.etna.model.Reservation;
import com.quest.etna.model.Review;
import com.quest.etna.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ReservationRepository extends CrudRepository<Reservation, Integer> {

    //@Query("SELECT r FROM Reservation r WHERE r.user=?1 AND r.address=?2")
    Optional<List<Reservation>> findByUserIdAndAddressId(int userId, int addressId);
    Optional<List<Reservation>> findByAddressId(int addressId);
    Optional<List<Reservation>> findByUserId(int userId);
}
