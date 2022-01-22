package com.alassaneniang.reactiveexamples.repositories.implementations;

import com.alassaneniang.reactiveexamples.domain.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;

@DisplayName( "Person Repository Implementation Test" )
class PersonRepositoryImplTest {

    PersonRepositoryImpl personRepository;
    private Mono<Person> personMono;

    @BeforeEach
    void setUp () {
        personRepository = new PersonRepositoryImpl();
        personMono = personRepository.getById( 1 );
    }

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
}