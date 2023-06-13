package com.uhuru.customer;

import com.uhuru.exception.DuplicateResourceException;
import com.uhuru.exception.RequestValidationException;
import com.uhuru.exception.ResourceNotFoundException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {

    private final CustomerDao customerDao;

    public CustomerService(@Qualifier("jdbc") CustomerDao customerDao) {
        this.customerDao = customerDao;
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

        if(customerDao.existPersonWithEmail(request.getEmail())){
            throw new DuplicateResourceException("email already taken");
        }

        Customer customer = new Customer(request.getName(), request.getEmail(), request.getAge(), Gender.valueOf(request.getGender()));
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

        if(request.getName() !=null && !request.getName().equals(customer.getName())){
            customer.setName(request.getName());
            changes = true;
        }

        if(request.getAge() !=null && !request.getAge().equals(customer.getAge())){
            customer.setAge(request.getAge());
            changes = true;
        }

        if(request.getEmail() !=null && !request.getEmail().equals(customer.getEmail())){
            if(customerDao.existPersonWithEmail(request.getEmail())){
                throw new DuplicateResourceException(
                  "email already taken"
                );
            }
            customer.setEmail(request.getEmail());
            changes = true;
        }

        if(!changes){
            throw new RequestValidationException("no data changes found");
        }


        customerDao.updateCustomer(customer);


    }


}
