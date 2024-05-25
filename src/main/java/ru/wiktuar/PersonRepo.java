package ru.wiktuar;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.wiktuar.entites.Person;

import java.util.List;

@Repository
public interface PersonRepo extends CrudRepository<Person, Long> {
    @Query("select p from Person p")
    List<Person> getAllPersons();
}
