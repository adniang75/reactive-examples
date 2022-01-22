package com.alassaneniang.reactiveexamples.repositories.implementations;

import com.alassaneniang.reactiveexamples.domain.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Objects;

@DisplayName( "Person Repository Implementation Test" )
class PersonRepositoryImplTest {

    PersonRepositoryImpl personRepository;
    private Mono<Person> personMono;
    private Flux<Person> personFlux;

    @BeforeEach
    void setUp () {
        personRepository = new PersonRepositoryImpl();
        personMono = personRepository.getById( 1 );
        personFlux = personRepository.findAll();
    }

    // Testing Mono operation

    @Test
    @DisplayName( "Get a person by ID - Mono block" )
    void getByIdBlock () {
        Person person = personMono.block();
        System.out.println( person );
    }

    @Test
    @DisplayName( "Get a person by ID - Mono subscribe" )
    void getByIdSubscribe () {
        personMono.subscribe( System.out::println );
    }

    @Test
    @DisplayName( "Get a person by ID - Mono map function" )
    void getByIdMapFunction () {
        personMono.map( Person::getFirstName )
                .subscribe( firstName -> System.out.println( "From map: " + firstName ) );
    }

    // Testing Flux operation

    @Test
    @DisplayName( "Get all persons - Flux block first" )
    void fluxTestBlockFirst () {
        Person person = personFlux.blockFirst();
        System.out.println( person );
    }

    @Test
    @DisplayName( "Get all persons - Flux subscribe" )
    void fluxTestSubscribe () {
        personFlux.subscribe( System.out::println );
    }

    @Test
    @DisplayName( "Get all persons - Flux collectList" )
    void testFluxToListMono () {
        Mono<List<Person>> personListMono = personFlux.collectList();
        personListMono.subscribe( list -> list.forEach( System.out::println ) );
    }

    // Filtering Flux objects

    @Test
    @DisplayName( "Get a person by ID with Flux filtering" )
    void testFindPersonById () {
        final Integer id = 4;
        Mono<Person> personFound = personFlux.filter( person -> Objects.equals( person.getId(), id ) ).next();
        personFound.subscribe( System.out::println );
    }

    @Test
    @DisplayName( "Get a person by ID with Flux filtering - Not found" )
    void testFindPersonByIdNotFound () {
        final Integer id = 100;
        Mono<Person> personFound = personFlux.filter( person -> Objects.equals( person.getId(), id ) ).next();
        personFound.subscribe( System.out::println );
    }

    @Test
    @DisplayName( "Get a person by ID with Flux filtering" )
    void testFindPersonByIdNotFoundWithException () {
        final Integer id = 100;
        Mono<Person> personFound = personFlux.filter( person -> Objects.equals( person.getId(), id ) ).single();
        personFound.doOnError( error -> System.out.println( "Person not found" ) )
                .onErrorReturn( Person.builder().id( id ).build() )
                .subscribe( System.out::println );
    }
}