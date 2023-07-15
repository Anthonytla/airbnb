package com.quest.etna.repositories;

import com.quest.etna.model.Address;
import com.quest.etna.model.Review;
import com.quest.etna.model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface ReviewRepository extends CrudRepository<Review, Integer> {
    Optional<List<Review>> findByAddressId(int address_id);
}
