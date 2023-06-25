package com.uhuru.jounery;


import com.uhuru.customer.CustomerDTO;
import com.uhuru.customer.CustomerRegistrationRequest;
import com.uhuru.customer.Gender;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
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
public class CustomerIT {

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
        String jwtToken = webTestClient.post()
                            .uri(CUSTOMER_URI)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(Mono.just(request), CustomerRegistrationRequest.class)
                            .exchange()
                            .expectStatus()
                            .isOk()
                            .returnResult(Void.class)
                            .getResponseHeaders()
                            .get(HttpHeaders.AUTHORIZATION)
                            .get(0);

        // get all customers
        List<CustomerDTO> allCustomers = webTestClient.get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<CustomerDTO>() {})
                .returnResult()
                .getResponseBody();

        int customerId = allCustomers
                    .stream()
                    .filter(c -> c.email().equals(email))
                    .map(CustomerDTO::id)
                    .findFirst()
                    .orElseThrow();


        // make sure that customer is present
        CustomerDTO expectedCustomer = new CustomerDTO(
                customerId,
                firstName + " " + lastName,
                email,
                Gender.MALE,
                age,
                List.of("ROLE_USER"),
                email
        );

        assertThat(allCustomers).contains(expectedCustomer);

        // get customer by id
        webTestClient.get()
                .uri(CUSTOMER_URI + "/{id}", customerId )
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<CustomerDTO>() {})
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
        String jwtToken = webTestClient.post()
                            .uri(CUSTOMER_URI)
                            .accept(MediaType.APPLICATION_JSON)
                            .contentType(MediaType.APPLICATION_JSON)
                            .body(Mono.just(request), CustomerRegistrationRequest.class)
                            .exchange()
                            .expectStatus()
                            .isOk()
                            .returnResult(Void.class)
                            .getResponseHeaders()
                            .get(HttpHeaders.AUTHORIZATION)
                            .get(0);


        // get all customers
        List<CustomerDTO> allCustomers = webTestClient.get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<CustomerDTO>() {})
                .returnResult()
                .getResponseBody();

        int customerId = allCustomers
                .stream()
                .filter(c -> c.email().equals(email))
                .map(CustomerDTO::id)
                .findFirst()
                .orElseThrow();


        // make sure that customer is present
        CustomerDTO expectedCustomer = new CustomerDTO(
                customerId,
                firstName + " " + lastName,
                email,
                Gender.MALE,
                age,
                List.of("ROLE_USER"),
                email
        );


        assertThat(allCustomers).contains(expectedCustomer);

        // delete customer by id
        webTestClient.delete()
                .uri(CUSTOMER_URI + "/{id}", customerId )
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isOk();


        // make sure customer is deleted
        webTestClient.get()
                .uri(CUSTOMER_URI + "/{id}", customerId )
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus()
                .isForbidden();

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
        String jwtToken = webTestClient.post()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .body(Mono.just(request), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk()
                .returnResult(Void.class)
                .getResponseHeaders()
                .get(HttpHeaders.AUTHORIZATION)
                .get(0);


        // get all customers
        List<CustomerDTO> allCustomers = webTestClient.get()
                .uri(CUSTOMER_URI)
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBodyList(new ParameterizedTypeReference<CustomerDTO>() {})
                .returnResult()
                .getResponseBody();

        int customerId = allCustomers
                .stream()
                .filter(c -> c.email().equals(email))
                .map(CustomerDTO::id)
                .findFirst()
                .orElseThrow();


        // make sure that customer is present
        CustomerDTO expectedCustomer = new CustomerDTO(
                customerId,
                firstName + " " + lastName,
                email,
                Gender.MALE,
                age,
                List.of("ROLE_USER"),
                email
        );

        assertThat(allCustomers).contains(expectedCustomer);

        // update customer by id
        String updateFirstName = faker.name().firstName();
        String updateLastName = faker.name().lastName();
        int updateAge = RANDOM.nextInt(1, 10);

        CustomerRegistrationRequest updateRequest = new CustomerRegistrationRequest(
                updateFirstName + " " + updateLastName,
                expectedCustomer.email(),
                "password",
                updateAge,
                expectedCustomer.gender()
        );

        webTestClient.put()
                .uri(CUSTOMER_URI + "/{id}", customerId )
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .body(Mono.just(updateRequest), CustomerRegistrationRequest.class)
                .exchange()
                .expectStatus()
                .isOk();

        // make sure customer is updated
        CustomerDTO updatedCustomer = new CustomerDTO(
                customerId,
                updateFirstName + " " + updateLastName,
                email,
                Gender.MALE,
                updateAge,
                List.of("ROLE_USER"),
                email
        );

        webTestClient.get()
                .uri(CUSTOMER_URI + "/{id}", customerId )
                .accept(MediaType.APPLICATION_JSON)
                .header(HttpHeaders.AUTHORIZATION, String.format("Bearer %s", jwtToken))
                .exchange()
                .expectStatus()
                .isOk()
                .expectBody(new ParameterizedTypeReference<CustomerDTO>() {})
                .isEqualTo(updatedCustomer);

    }

}
