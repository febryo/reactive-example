package guru.springframework.reactiveexample;

import guru.springframework.reactiveexample.domain.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;
import java.util.List;

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

    @Test
    void fluxTestBlockFirst() {
        Flux<Person> personFlux = this.personRepository.findAll();

        Person person = personFlux.blockFirst();

        System.out.println(person.toString());
    }

    @Test
    void fluxTestSubscribe() {
        Flux<Person> personFlux = this.personRepository.findAll();

        personFlux.subscribe(person -> {
            System.out.println(person.toString());
        });
    }

    @Test
    void fluxTestToListMono() {
        Flux<Person> personFlux = this.personRepository.findAll();

        Mono<List<Person>> personListMono = personFlux.collectList();

        personListMono.subscribe(list -> {
            list.forEach(person ->{
                System.out.println(person.toString());
            });
        });
    }
}