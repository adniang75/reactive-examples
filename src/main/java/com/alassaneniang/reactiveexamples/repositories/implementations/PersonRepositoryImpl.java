package com.alassaneniang.reactiveexamples.repositories.implementations;

import com.alassaneniang.reactiveexamples.domain.Person;
import com.alassaneniang.reactiveexamples.repositories.PersonRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class PersonRepositoryImpl implements PersonRepository {

    // emulating a Person repository
    Person alassane = new Person( 1, "Alassane", "Niang" );
    Person elon = new Person( 2, "Elon", "Musk" );
    Person jeff = new Person( 3, "Jeff", "Bezos" );
    Person barack = new Person( 4, "Barack", "Obama" );

    @Override
    public Mono<Person> getById ( final Integer id ) {
        // publisher of zero or one element
        return findAll()
                .filter( person -> person.getId().equals( id ) )
                .next();
    }

    @Override
    public Flux<Person> findAll () {
        // publisher of zero or more elements
        return Flux.just( alassane, elon, jeff, barack );
    }
}
