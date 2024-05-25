package ru.wiktuar.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.wiktuar.PersonRepo;
import ru.wiktuar.entites.Person;

import java.util.List;
import java.util.Random;

@RestController
public class MyController {

    @Value("${spring.datasource.url}")
    private String address;

    private PersonRepo personRepo;

    @Autowired
    public void setPersonRepo(PersonRepo personRepo) {
        this.personRepo = personRepo;
    }

    @GetMapping("/hello")
    public ResponseEntity<List<Person>> greeting(){
        List<Person> personList = personRepo.getAllPersons();
        return new ResponseEntity<>(personList, null, HttpStatus.OK);
    }

    @GetMapping("/address")
    public String getAddress(){
        return address;
    }

    @GetMapping("/add")
    public String addPerson(){
        Random random = new Random();
        String name = "Viktor" + random.nextInt(11);
        Person person = new Person(name, 28, "/home/user");
        personRepo.save(person);
        return "add successfully";
    }
}
