package com.uhuru.customer;

import com.uhuru.AbstractTestcontainersUnitTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class CustomerJDBCDataAccessServiceTest extends AbstractTestcontainersUnitTest {

    private CustomerJDBCDataAccessService underTest;
    private final CustomerRowMapper customerRowMapper = new CustomerRowMapper();


    @BeforeEach
    void setUp() {
        underTest = new CustomerJDBCDataAccessService(
                getJdbcTemplate(),
                customerRowMapper
        );
    }

    @Test
    void selectAllCustomers() {
        // Given
        String firstName = FAKER.name().firstName();
        String lastName = FAKER.name().lastName();
        Customer customer = new Customer(
                firstName + " " + lastName,
                firstName + "." + lastName + "-" + UUID.randomUUID() + "@gmail.com",
                "password", 20,
                Gender.MALE

        );

        underTest.insertCustomer(customer);

        // When
        List<Customer> customers = underTest.selectAllCustomers();

        // Then
        assertThat(customers).isNotEmpty();
    }

    @Test
    void selectCustomerById() {
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

        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(email))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // When
        Optional<Customer> actual =  underTest.selectCustomerById(id);

        // Then
        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }


    @Test
    void willReturnEmptyWhenSelectCustomerByID() {
        // Given
        int id = -100;

        // When
        Optional<Customer> actual =  underTest.selectCustomerById(id);

        // Then
        assertThat(actual).isEmpty();
    }

    @Test
    void insertCustomer() {
        // Given
        String firstName = FAKER.name().firstName();
        String lastName = FAKER.name().lastName();
        Customer customer = new Customer(
                firstName + " " + lastName,
                firstName + "." + lastName + "-" + UUID.randomUUID() + "@gmail.com",
                "password", 20,
                Gender.MALE
        );

        // When
        underTest.insertCustomer(customer);

        // Then
        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(customer.getEmail()))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        Optional<Customer> actual =  underTest.selectCustomerById(id);

        assertThat(actual).isPresent().hasValueSatisfying(c -> {
            assertThat(c.getId()).isEqualTo(id);
            assertThat(c.getName()).isEqualTo(customer.getName());
            assertThat(c.getEmail()).isEqualTo(customer.getEmail());
            assertThat(c.getAge()).isEqualTo(customer.getAge());
        });
    }

    @Test
    void existPersonWithEmail() {
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

        underTest.insertCustomer(customer);

        // When
        Boolean exist =  underTest.existPersonWithEmail(email);

        // Then
        assertThat(exist).isTrue();

    }

    @Test
    void existPersonWithEmailReturnsFalseWhenEmailDoesNotExist() {
        // Given
        String email = FAKER.name().firstName() + "." + FAKER.name().lastName() + "-" + UUID.randomUUID() + "@gmail.com";

        // When
        Boolean exist =  underTest.existPersonWithEmail(email);

        // Then
        assertThat(exist).isFalse();

    }

    @Test
    void existPersonWithId() {
        // Given
        String firstName = FAKER.name().firstName();
        String lastName = FAKER.name().lastName();

        Customer customer = new Customer(
                firstName + " " + lastName,
                firstName + "." + lastName + "-" + UUID.randomUUID() + "@gmail.com",
                "password", 20,
                Gender.MALE
        );

        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(customer.getEmail()))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // When
        Boolean exist =  underTest.existPersonWithId(id);

        // Then
        assertThat(exist).isTrue();
    }

    @Test
    void existPersonWithIdWillReturnFalseWhenIdNotPresent() {
        // Given
        int id = -19;

        // When
        Boolean exist =  underTest.existPersonWithId(id);

        // Then
        assertThat(exist).isFalse();
    }

    @Test
    void deleteCustomerById() {
        // Given
        String firstName = FAKER.name().firstName();
        String lastName = FAKER.name().lastName();

        Customer customer = new Customer(
                firstName + " " + lastName,
                firstName + "." + lastName + "-" + UUID.randomUUID() + "@gmail.com",
                "password", 20,
                Gender.MALE
        );

        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(customer.getEmail()))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // When
        underTest.deleteCustomerById(id);

        // Then
        Optional<Customer> actual = underTest.selectCustomerById(id);
        assertThat(actual).isNotPresent();

    }

    @Test
    void updateCustomer() {
        // Given
        String firstName = FAKER.name().firstName();
        String lastName = FAKER.name().lastName();

        Customer customer = new Customer(
                firstName + " " + lastName,
                firstName + "." + lastName + "-" + UUID.randomUUID() + "@gmail.com",
                "password", 20,
                Gender.MALE
        );

        underTest.insertCustomer(customer);

        int id = underTest.selectAllCustomers()
                .stream()
                .filter(c -> c.getEmail().equals(customer.getEmail()))
                .map(Customer::getId)
                .findFirst()
                .orElseThrow();

        // When
        String updateFirstName = FAKER.name().firstName();
        String updateLastName = FAKER.name().lastName();
        Customer update = new Customer(
                id,
                updateFirstName + " " + updateLastName,
                updateFirstName + " " + updateLastName + "-" + UUID.randomUUID() + "@gmail.com",
                "password",
                30,
                Gender.MALE);


        underTest.updateCustomer(update);

        // Then
        Optional<Customer> actual = underTest.selectCustomerById(id);
        assertThat(actual).isPresent().hasValue(update);
    }
}