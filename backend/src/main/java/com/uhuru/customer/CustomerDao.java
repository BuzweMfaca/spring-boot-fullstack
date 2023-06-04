package com.uhuru.customer;

import jakarta.persistence.criteria.CriteriaBuilder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;


public interface CustomerDao {
    List<Customer> selectAllCustomers();
    Optional<Customer> selectCustomerById(Integer id);
    void insertCustomer(Customer customer);
    boolean existPersonWithEmail(String email);

    boolean existPersonWithId(Integer id);
    void deleteCustomerById(Integer id);

    void updateCustomer(Customer customer);

}
