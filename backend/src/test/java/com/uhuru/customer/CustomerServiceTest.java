package com.uhuru.customer;

import com.uhuru.exception.DuplicateResourceException;
import com.uhuru.exception.RequestValidationException;
import com.uhuru.exception.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {


    private CustomerService underTest;
    //private AutoCloseable autoCloseable;

    @Mock
    private CustomerDao customerDao;

    @BeforeEach
    void setUp() {
        //autoCloseable = MockitoAnnotations.openMocks(this);
        underTest = new CustomerService(customerDao);
    }

//    @AfterEach
//    void tearDown() throws Exception {
//        autoCloseable.close();
//    }

    @Test
    void getAllCustomers() {

        // When
        underTest.getAllCustomers();

        // Then
        verify(customerDao)
                .selectAllCustomers();

    }

    @Test
    void canGetCustomer() {
        // Given
        Customer customer = new Customer(
                1,
                "test",
                "test@gmail.com",
                21,
                Gender.MALE

        );

        when(customerDao.selectCustomerById(customer.getId())).thenReturn(Optional.of(customer));

        // When
        Customer actual = underTest.getCustomer(customer.getId());

        // Then
        assert(actual).equals(customer);
    }

    @Test
    void willThrowErrorWhenGetCustomerReturnEmptyOption() {
        // Given
        int id = 11;

        when(customerDao.selectCustomerById(id)).thenReturn(Optional.empty());

        // When
        // Then
        assertThatThrownBy(() ->underTest.getCustomer(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("customer with id [%s] not found".formatted(id));
    }

    @Test
    void addCustomer() {
        // Given
        String email =  "test@gmial.com";

        when(customerDao.existPersonWithEmail(email)).thenReturn(false);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest();
        request.setName("test");
        request.setEmail(email);
        request.setAge(21);
        request.setGender(String.valueOf(Gender.MALE));

        // When
        underTest.addCustomer(request);

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor = ArgumentCaptor.forClass(
                Customer.class
        );

        verify(customerDao).insertCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getId()).isNull();
        assertThat(capturedCustomer.getName()).isEqualTo(request.getName());
        assertThat(capturedCustomer.getEmail()).isEqualTo(request.getEmail());
        assertThat(capturedCustomer.getAge()).isEqualTo(request.getAge());


        // OR
//        Customer customer = new Customer(
//                request.getName(),
//                request.getEmail(),
//                request.getAge()
//        );
//
//        verify(customerDao)
//                .insertCustomer(customer);
    }


    @Test
    void willThrowWhenEmailExistsWhileAddingACustomer() {
        // Given
        String email =  "test@gmial.com";

        when(customerDao.existPersonWithEmail(email)).thenReturn(true);

        CustomerRegistrationRequest request = new CustomerRegistrationRequest();
        request.setName("test");
        request.setEmail(email);
        request.setAge(21);

        // When
        assertThatThrownBy(() -> underTest.addCustomer(request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("email already taken");

        // Then
        verify(customerDao, never()).insertCustomer(any());

    }

    @Test
    void deleteCustomerById() {
        // Given
        int id = 10;

        when(customerDao.existPersonWithId(id)).thenReturn(true);

        // When
        underTest.deleteCustomerById(id);

        // Then
        verify(customerDao).deleteCustomerById(id);

    }

    @Test
    void willThrowErrorWhenGetDeleteCustomerByIdNotFound() {
        // Given
        int id = 11;

        when(customerDao.existPersonWithId(id)).thenReturn(false);

        // When
        assertThatThrownBy(() ->underTest.deleteCustomerById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("customer with id [%s] not found".formatted(id));

        // Then
        verify(customerDao, never()).deleteCustomerById(any());


    }

    @Test
    void canUpdateAllCustomerValues() {
        // Given
        int id = 11;

        Customer customer = new Customer(
                id,
                "test",
                "test@gmail.com",
                19,
                Gender.MALE
        );

        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        String newEmail = "Alex@gmail.com";
        CustomerRegistrationRequest request = new CustomerRegistrationRequest();
        request.setName("Alex");
        request.setEmail(newEmail);
        request.setAge(21);

        when(customerDao.existPersonWithEmail(newEmail)).thenReturn(false);

        // When
        underTest.updateCustomer(id, request);

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor =
                ArgumentCaptor.forClass(Customer.class);

        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(request.getName());
        assertThat(capturedCustomer.getEmail()).isEqualTo(request.getEmail());
        assertThat(capturedCustomer.getAge()).isEqualTo(request.getAge());

    }

    @Test
    void canUpdateCustomerName() {
        // Given
        int id = 11;

        Customer customer = new Customer(
                id,
                "test",
                "test@gmail.com",
                19,
                Gender.MALE
        );

        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        String newEmail = "Alex@gmail.com";
        CustomerRegistrationRequest request = new CustomerRegistrationRequest();
        request.setName("Alex");


        // When
        underTest.updateCustomer(id, request);

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor =
                ArgumentCaptor.forClass(Customer.class);

        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getName()).isEqualTo(request.getName());

    }

    @Test
    void canUpdateCustomerEmail() {
        // Given
        int id = 11;

        Customer customer = new Customer(
                id,
                "test",
                "test@gmail.com",
                19,
                Gender.MALE
        );

        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        String newEmail = "Alex@gmail.com";
        CustomerRegistrationRequest request = new CustomerRegistrationRequest();
        request.setEmail(newEmail);


        when(customerDao.existPersonWithEmail(newEmail)).thenReturn(false);

        // When
        underTest.updateCustomer(id, request);

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor =
                ArgumentCaptor.forClass(Customer.class);

        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getEmail()).isEqualTo(request.getEmail());

    }


    @Test
    void canUpdateCustomerAge() {
        // Given
        int id = 11;

        Customer customer = new Customer(
                id,
                "test",
                "test@gmail.com",
                19,
                Gender.MALE
        );

        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        CustomerRegistrationRequest request = new CustomerRegistrationRequest();
        request.setAge(21);

        // When
        underTest.updateCustomer(id, request);

        // Then
        ArgumentCaptor<Customer> customerArgumentCaptor =
                ArgumentCaptor.forClass(Customer.class);

        verify(customerDao).updateCustomer(customerArgumentCaptor.capture());

        Customer capturedCustomer = customerArgumentCaptor.getValue();

        assertThat(capturedCustomer.getAge()).isEqualTo(request.getAge());

    }


    @Test
    void willThrowErrorWhenUpdateCustomerNotFound() {
        // Given
        int id = 11;

        Customer customer = new Customer(
                id,
                "test",
                "test@gmail.com",
                19,
                Gender.MALE
        );

        when(customerDao.selectCustomerById(id)).thenReturn(Optional.empty());

        String newEmail = "Alex@gmail.com";
        CustomerRegistrationRequest request = new CustomerRegistrationRequest();
        request.setName("Alex");
        request.setEmail(newEmail);
        request.setAge(21);

        // When
        assertThatThrownBy(() -> underTest.updateCustomer(id, request))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("customer with id [%s] not found".formatted(id));

        // Then
        verify(customerDao, never()).updateCustomer(any());

    }

    @Test
    void willThrowErrorWhenUpdateCustomerEmailExists() {
        // Given
        int id = 11;

        Customer customer = new Customer(
                id,
                "test",
                "test@gmail.com",
                19,
                Gender.MALE
        );

        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        String newEmail = "Alex@gmail.com";
        CustomerRegistrationRequest request = new CustomerRegistrationRequest();
        request.setName("Alex");
        request.setEmail(newEmail);
        request.setAge(21);

        when(customerDao.existPersonWithEmail(newEmail)).thenReturn(true);

        // When
        assertThatThrownBy(() -> underTest.updateCustomer(id, request))
                .isInstanceOf(DuplicateResourceException.class)
                .hasMessage("email already taken");

        // Then
        verify(customerDao, never()).updateCustomer(any());

    }

    @Test
    void willThrowErrorWhenUpdateCustomerHasNoChanges() {
        // Given
        int id = 11;

        Customer customer = new Customer(
                id,
                "test",
                "test@gmail.com",
                19,
                Gender.MALE
        );

        when(customerDao.selectCustomerById(id)).thenReturn(Optional.of(customer));

        CustomerRegistrationRequest request = new CustomerRegistrationRequest();
        request.setName("test");
        request.setEmail("test@gmail.com");
        request.setAge(19);

        // When
        assertThatThrownBy(() -> underTest.updateCustomer(id, request))
                .isInstanceOf(RequestValidationException.class)
                .hasMessage("no data changes found");

        // Then
        verify(customerDao, never()).updateCustomer(any());

    }
}