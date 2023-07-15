package com.quest.etna.repositories;


import com.quest.etna.model.Message;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MessageRepository extends CrudRepository<Message, Integer> {
    Optional<List<Message>> findByAddressId(int addressId);
}
