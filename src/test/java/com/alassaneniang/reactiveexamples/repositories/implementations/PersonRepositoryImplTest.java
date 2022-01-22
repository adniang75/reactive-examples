package com.alassaneniang.reactiveexamples.repositories.implementations;

import com.alassaneniang.reactiveexamples.domain.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

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
    void testFluxToListMono () {
        Mono<List<Person>> personListMono = personFlux.collectList();
        personListMono.subscribe( list -> list.forEach( System.out::println ) );
    }
}