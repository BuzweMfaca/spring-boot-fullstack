package com.uhuru.customer;

import com.uhuru.AbstractTestcontainersUnitTest;
import com.uhuru.TestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Import;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Import({TestConfig.class})
class CustomerRepositoryTest extends AbstractTestcontainersUnitTest {

    @Autowired
    private CustomerRepository underTest;


    @Autowired
    private ApplicationContext applicationContext;

    @BeforeEach
    void setUp() {
    }

    @Test
    void existsCustomerByEmail() {
        // Given
        String firstName = FAKER.name().firstName();
        String lastName = FAKER.name().lastName();
        String email = firstName + "." + lastName + "-" + UUID.randomUUID() + "@gmail.com";
        Customer customer = new Customer(
                firstName + " " + lastName,
                email,
                "password", 20,
                Gender.MALE
        );

        underTest.save(customer);

        // When
        Boolean exist =  underTest.existsCustomerByEmail(email);

        // Then
        assertThat(exist).isTrue();
    }

    @Test
    void existsCustomerByEmailWillReturnFalseWhenEmailNotPresent() {
        // Given
        String email = FAKER.name().firstName() + "." + FAKER.name().lastName() + "-" + UUID.randomUUID() + "@gmail.com";

        // When
        Boolean exist =  underTest.existsCustomerByEmail(email);

        // Then
        assertThat(exist).isFalse();
    }

    @Test
    void existsCustomerById() {
        // Given
        String firstName = FAKER.name().firstName();
        String lastName = FAKER.name().lastName();

        Customer customer = new Customer(
                firstName + " " + lastName,
                firstName + "." + lastName + "-" + UUID.randomUUID() + "@gmail.com",
                "password", 20,
                Gender.MALE
        );

        underTest.save(customer);

        int id = underTest.findAll()
                .stream()
                .filter(c -> c.getEmail().equals(customer.getEmail()))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // When
        Boolean exist =  underTest.existsCustomerById(id);

        // Then
        assertThat(exist).isTrue();
    }


    @Test
    void existsCustomerByIdWillReturnFalseWhenIdNotPresent() {
        // Given
        int id = -19;

        // When
        Boolean exist =  underTest.existsCustomerById(id);

        // Then
        assertThat(exist).isFalse();
    }
}