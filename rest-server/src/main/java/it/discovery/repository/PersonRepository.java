package it.discovery.repository;

import it.discovery.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RestResource;

public interface PersonRepository extends JpaRepository<Person, Integer> {

    @Override
    @RestResource(exported = false)
    void deleteById(Integer integer);
}
