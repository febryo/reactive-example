package guru.springframework.reactiveexample;

import guru.springframework.reactiveexample.domain.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class PersonRepositoryImplTest {

    PersonRepositoryImpl personRepository;

    @BeforeEach
    void setUp() {
        this.personRepository = new PersonRepositoryImpl();
    }

    @Test
    void getByIdBlock() {
        Mono<Person> personMono = this.personRepository.getById(1);

        Person person = personMono.block();

        System.out.println(Instant.now().toString() + " Blocking " + person.toString());
    }

    @Test
    void getByIdSubscribe() {
        Mono<Person> personMono = this.personRepository.getById(1);

        personMono.subscribe(person -> {
            System.out.println(Instant.now().toString() + " Subscribe " +person.toString());
        });

    }

    @Test
    void getByIdMapFunction() {
        Mono<Person> personMono = this.personRepository.getById(1);

        personMono.map(person -> {
            System.out.println(Instant.now().toString() + " Map " + person.toString());
            return person.getFirstName();
        }).subscribe(firstName -> {
            System.out.println(Instant.now().toString() + " from map: " + firstName);
        });
    }
}