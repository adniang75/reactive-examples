package com.alassaneniang.reactiveexamples.repositories.implementations;

import com.alassaneniang.reactiveexamples.domain.Person;
import com.alassaneniang.reactiveexamples.repositories.PersonRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class PersonRepositoryImpl implements PersonRepository {
    @Override
    public Mono<Person> getById ( Integer id ) {
        return null;
    }

    @Override
    public Flux<Person> findAll () {
        return null;
    }
}
