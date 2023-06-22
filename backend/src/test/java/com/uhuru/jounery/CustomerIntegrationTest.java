package com.uhuru.jounery;


import com.uhuru.customer.Customer;
import com.uhuru.customer.CustomerRegistrationRequest;
import com.uhuru.customer.Gender;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;
import com.github.javafaker.Faker;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Random;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@SpringBootTest(webEnvironment = RANDOM_PORT)
public class CustomerIntegrationTest {

    @Autowired
    private WebTestClient webTestClient;

    private static final Random RANDOM = new Random();
    private static final String CUSTOMER_URI = "/api/v1/customers";

    @Test
    void canRegisterACustomer(){

        // create registration request
        Faker faker = new Faker();
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String email = firstName + "." + lastName + "-" + UUID.randomUUID() + "@foobarhello1.com";
        int age = RANDOM.nextInt(1, 10);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(firstName + " " + lastName, email, "password", age, Gender.MALE);

        // send a post request
        webTestClient.post()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // get all customers
        List<Customer> allCustomers = webTestClient.get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {})
                .returnResult()
                .getResponseBody();

        int id = allCustomers
                    .stream()
                    .filter(c -> c.getEmail().equals(email))
                    .map(Customer::getId)
                    .findFirst()
                    .orElseThrow();


        // make sure that customer is present
        Customer expectedCustomer = new Customer(
                id,
                firstName + " " + lastName,
                email,
                "password",
                age,
                Gender.MALE);

        assertThat(allCustomers)
                //.usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expectedCustomer);

        // get customer by id
        webTestClient.get()
                .uri(CUSTOMER_URI + "/{id}", id )
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<Customer>() {})
                .isEqualTo(expectedCustomer);


    }

    @Test
    void canDeleteACustomer(){

        // create registration request
        Faker faker = new Faker();
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String email = firstName + "." + lastName + "-" + UUID.randomUUID() + "@foobarhello1.com";
        int age = RANDOM.nextInt(1, 10);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(firstName + " " + lastName, email, "password", age,Gender.MALE);

        // send a post request
        webTestClient.post()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // get all customers
        List<Customer> allCustomers = webTestClient.get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {})
                .returnResult()
                .getResponseBody();

        int id = allCustomers
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();


        // make sure that customer is present
        Customer expectedCustomer = new Customer(
                id,
                firstName + " " + lastName,
                email,
                "password",
                age,
                Gender.MALE);

        assertThat(allCustomers)
                //.usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expectedCustomer);

        // delete customer by id
        webTestClient.delete()
                .uri(CUSTOMER_URI + "/{id}", id )
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk();


        // make sure customer is deleted
        webTestClient.get()
                .uri(CUSTOMER_URI + "/{id}", id )
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isNotFound();

    }

    @Test
    void canUpdateACustomer(){

        // create registration request
        Faker faker = new Faker();
        String firstName = faker.name().firstName();
        String lastName = faker.name().lastName();
        String email = firstName + "." + lastName + "-" + UUID.randomUUID() + "@foobarhello1.com";
        int age = RANDOM.nextInt(1, 10);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest(firstName + " " + lastName, email, "password", age, Gender.MALE);

        // send a post request
        webTestClient.post()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // get all customers
        List<Customer> allCustomers = webTestClient.get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<Customer>() {})
                .returnResult()
                .getResponseBody();

        int id = allCustomers
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();


        // make sure that customer is present
        Customer expectedCustomer = new Customer(
                id,
                firstName + " " + lastName,
                email,
                "password",
                age,
                Gender.MALE);

        assertThat(allCustomers)
                //.usingRecursiveFieldByFieldElementComparatorIgnoringFields("id")
                .contains(expectedCustomer);

        // update customer by id
        String updateFirstName = faker.name().firstName();
        String updateLastName = faker.name().lastName();
        int updateAge = RANDOM.nextInt(1, 10);

        CustomerRegistrationRequest updateRequest = new CustomerRegistrationRequest(
                updateFirstName + " " + updateLastName,
                expectedCustomer.getEmail(),
                "password", updateAge,
                expectedCustomer.getGender()
                );

        webTestClient.put()
                .uri(CUSTOMER_URI + "/{id}", id )
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(updateRequest), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // make sure customer is updated
        Customer updatedCustomer = new Customer(
                id,
                updateFirstName + " " + updateLastName,
                email,
                "password",
                updateAge,
                Gender.MALE);

        webTestClient.get()
                .uri(CUSTOMER_URI + "/{id}", id )
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<Customer>() {})
                .isEqualTo(updatedCustomer);

    }

}
