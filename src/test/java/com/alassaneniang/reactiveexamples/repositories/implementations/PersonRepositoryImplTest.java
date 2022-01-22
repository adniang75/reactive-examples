package com.alassaneniang.reactiveexamples.repositories.implementations;

import com.alassaneniang.reactiveexamples.domain.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName( "Person Repository Implementation Test" )
class PersonRepositoryImplTest {

    PersonRepositoryImpl personRepository;
    private Mono<Person> personMono;
    private Flux<Person> personFlux;

    @BeforeEach
    void setUp () {
        personRepository = new PersonRepositoryImpl();
        personFlux = personRepository.findAll();
    }

    // Testing Mono operation

    @Test
    @DisplayName( "Get a person by ID - Mono block" )
    void getByIdBlock () {
        personMono = personRepository.getById( 1 );
        StepVerifier.create( personMono )
                .expectNext( new Person( 1, "Alassane", "Niang" ) )
                .verifyComplete();
        System.out.println( personMono.block() );
    }

    @Test
    @DisplayName( "Get a person by ID - Mono subscribe" )
    void getByIdSubscribe () {
        personMono = personRepository.getById( 1 );
        StepVerifier.create( personMono )
                .expectNextCount( 1 )
                .verifyComplete();
        personMono.subscribe( System.out::println );
    }

    @Test
    @DisplayName( "Get a non-existent person by ID - Mono subscribe" )
    void getByIdSubscribeNotFound () {
        personMono = personRepository.getById( 100 );
        StepVerifier.create( personMono )
                .expectNextCount( 0 )
                .verifyComplete();
    }

    @Test
    @DisplayName( "Get a person by ID - Mono map function" )
    void getByIdMapFunction () {
        personMono = personRepository.getById( 1 );
        personMono.map( Person::getFirstName )
                .subscribe( firstName -> System.out.println( "From map: " + firstName ) );
        StepVerifier.create( personMono )
                .expectNext( new Person( 1, "Alassane", "Niang" ) )
                .expectNextCount( 0 )
                .verifyComplete();
    }

    // Testing Flux operation

    @Test
    @DisplayName( "Get all persons - Flux block first" )
    void fluxTestBlockFirst () {
        Person person = personFlux.blockFirst();
        System.out.println( person );
        StepVerifier.create( personFlux )
                .expectNext( new Person( 1, "Alassane", "Niang" ) )
                .expectNextCount( 3 )
                .verifyComplete();
    }

    @Test
    @DisplayName( "Get all persons - Flux subscribe" )
    void fluxTestSubscribe () {
        StepVerifier.create( personFlux )
                .expectNextCount( 4 )
                .verifyComplete();

        personFlux.subscribe( System.out::println );
    }

    @Test
    @DisplayName( "Get all persons - Flux collectList" )
    void testFluxToListMono () {
        Mono<List<Person>> personListMono = personFlux.collectList();
        personListMono.subscribe( list -> list.forEach( System.out::println ) );
        StepVerifier.create( personFlux )
                .expectNext( new Person( 1, "Alassane", "Niang" ) )
                .expectNext( new Person( 2, "Elon", "Musk" ) )
                .expectNext( new Person( 3, "Jeff", "Bezos" ) )
                .expectNext( new Person( 4, "Barack", "Obama" ) )
                .expectNextCount( 0 )
                .verifyComplete();
    }

    // Filtering Flux objects

    @Test
    @DisplayName( "Get a person by ID with Flux filtering" )
    void testFindPersonById () {
        final Integer id = 4;
        Mono<Person> personFound = personFlux.filter( person -> Objects.equals( person.getId(), id ) ).next();
        personFound.subscribe( System.out::println );
        StepVerifier.create( personFlux.filter( person -> Objects.equals( person.getId(), id ) ) )
                .expectNext( new Person( 4, "Barack", "Obama" ) )
                .expectNextCount( 0 )
                .verifyComplete();
    }

    @Test
    @DisplayName( "Get a person by ID with Flux filtering - Not found" )
    void testFindPersonByIdNotFound () {
        final Integer id = 100;
        Mono<Person> personFound = personFlux.filter( person -> Objects.equals( person.getId(), id ) ).next();
        personFound.subscribe( System.out::println );
        StepVerifier.create( personFlux.filter( person -> Objects.equals( person.getId(), id ) ) )
                .expectNextCount( 0 )
                .verifyComplete();

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

    // Assignment 1
    @Test
    @DisplayName( "Assignment - Should return person with matching ID" )
    void shouldReturnPersonWithMatchingID () {
        final int expectedId = 4;
        Mono<Person> personFound = personRepository.getById( expectedId );
        // Non-blocking
        personFound.subscribe( person -> {
            assertThat( person.getId() ).isEqualTo( expectedId );
            assertThat( person.getFirstName() ).isEqualTo( "Barack" );
            assertThat( person.getLastName() ).isEqualTo( "Obama" );
        } );
    }

    @Test
    @DisplayName( "Assignment - Should return person with matching ID - StepVerifier" )
    void shouldReturnPersonWithMatchingIDWithStepVerifier () {
        final int expectedId = 4;
        Mono<Person> personFound = personRepository.getById( expectedId );
        // StepVerifier
        StepVerifier.create( personFound.map( Person::getId ).single() )
                .expectNext( expectedId ).verifyComplete();
    }

    @Test
    @DisplayName( "Assignment - Should return empty Mono when not found" )
    void shouldReturnEmptyMonoWhenNotFound () {
        final int expectedId = 100;
        Mono<Person> personFound = personRepository.getById( expectedId );
        // Non-blocking
        personFound.subscribe( person -> assertThat( person ).isNull() );
    }

    @Test
    @DisplayName( "Assignment - Should return empty Mono when not found - StepVerifier" )
    void shouldReturnEmptyMonoWhenNotFoundWithStepVerifier () {
        final int expectedId = 100;
        Mono<Person> personFound = personRepository.getById( expectedId );
        // StepVerifier
        StepVerifier.create( personFound.map( Person::getId ).single() )
                .expectError().verify();
    }
}