package com.quest.etna.repositories;

import com.quest.etna.model.Address;
import com.quest.etna.model.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.RepositoryDefinition;
import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

@Repository
public interface AddressRepository extends CrudRepository<Address, Integer> {
    Optional<Address> findById(int id);
    Optional<List<Address>> findByUser(User user);
}