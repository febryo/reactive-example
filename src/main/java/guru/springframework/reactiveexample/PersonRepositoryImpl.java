package guru.springframework.reactiveexample;

import guru.springframework.reactiveexample.domain.Person;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class PersonRepositoryImpl implements PersonRepository {

    Person michael = new Person(1, "Michael", "Weston");
    Person fiona = new Person(2, "Fiona", "Glenanne");
    Person sam = new Person(3, "Sam", "Axe");
    Person jesse = new Person(4, "Jesse", "Porter");

    @Override
    public Mono<Person> getById(Integer id) {
        return this.findAll().filter(person -> person.getId().equals(id)).next();
    }

    @Override
    public Flux<Person> findAll() {
        return Flux.just(michael, fiona, sam, jesse);
    }
}
