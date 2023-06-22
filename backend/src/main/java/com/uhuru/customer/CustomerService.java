package com.uhuru.customer;

import com.uhuru.exception.DuplicateResourceException;
import com.uhuru.exception.RequestValidationException;
import com.uhuru.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerDao customerDao;
    private final PasswordEncoder passwordEncoder;

    public CustomerService(@Qualifier("jdbc") CustomerDao customerDao, PasswordEncoder passwordEncoder) {
        this.customerDao = customerDao;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Customer> getAllCustomers(){
        return customerDao.selectAllCustomers();
    }


    public Customer getCustomer(Integer id){
        return customerDao.selectCustomerById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "customer with id [%s] not found".formatted(id)
                ));
    }

    public void addCustomer(CustomerRegistrationRequest request){

        if(customerDao.existPersonWithEmail(request.email())){
            throw new DuplicateResourceException("email already taken");
        }

        Customer customer = new Customer(
                request.name(),
                request.email(),
                passwordEncoder.encode(request.password()),
                request.age(),
                request.gender());

        customerDao.insertCustomer(customer);


    }

    public void deleteCustomerById(Integer id) {

        if(!customerDao.existPersonWithId(id)){
              throw new ResourceNotFoundException("customer with id [%s] not found".formatted(id));
        }

        customerDao.deleteCustomerById(id);
    }


    public void updateCustomer(Integer id, CustomerRegistrationRequest request){

        Customer customer = getCustomer(id);

        boolean changes = false;

        if(request.name() !=null && !request.name().equals(customer.getName())){
            customer.setName(request.name());
            changes = true;
        }

        if(request.age() !=null && !request.age().equals(customer.getAge())){
            customer.setAge(request.age());
            changes = true;
        }

        if(customerDao.existPersonWithEmail(request.email())){
            throw new DuplicateResourceException(
                    "email already taken"
            );
        }

        if(request.email() !=null && !request.email().equals(customer.getEmail())){
            customer.setEmail(request.email());
            changes = true;
        }

        if(!changes){
            throw new RequestValidationException("no data changes found");
        }


        customerDao.updateCustomer(customer);


    }


}
