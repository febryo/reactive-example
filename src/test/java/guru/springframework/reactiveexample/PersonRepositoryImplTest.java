package guru.springframework.reactiveexample;

import guru.springframework.reactiveexample.domain.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

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

        StepVerifier.create(personMono).expectNextCount(1).verifyComplete();

        personMono.subscribe(person -> {
            System.out.println(Instant.now().toString() + " Subscribe " +person.toString());
        });
    }

    @Test
    void getByIdSubscribeNotFound() {
        Mono<Person> personMono = this.personRepository.getById(15);

        StepVerifier.create(personMono).expectNextCount(0).verifyComplete();

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

        StepVerifier.create(personFlux).expectNextCount(4).verifyComplete();

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

    @Test
    void testFIndPersonById() {
        Flux<Person> personFlux = personRepository.findAll();

        final int id = 3;

        Mono<Person> personMono = personFlux.filter(person -> person.getId() == id).next();

        personMono.subscribe(person -> {
            System.out.println(person.toString());
        });

    }

    @Test
    void testFIndPersonByIdNotFound() {
        Flux<Person> personFlux = personRepository.findAll();

        final int id = 5;

        Mono<Person> personMono = personFlux.filter(person -> person.getId() == id).next();

        personMono.subscribe(person -> {
            System.out.println(person.toString());
        });

    }

    @Test
    void testFIndPersonByIdNotFoundWithException() {
        Flux<Person> personFlux = personRepository.findAll();

        final int id = 5;

        Mono<Person> personMono = personFlux.filter(person -> person.getId() == id).single();

        /**
         *  doOnError will throw your expected exception
         *  onErrorReturn instead throw an exception, you can modify your return.
         */
        personMono.doOnError(throwable -> {
            System.out.println("Error catch "+throwable.getMessage());
        }).onErrorReturn(Person.builder().build()).subscribe(person -> {
            System.out.println(person.toString());
        });

    }
}