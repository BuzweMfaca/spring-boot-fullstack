package com.uhuru.customer;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository("list")
public class CustomerListDataAccessService implements CustomerDao{

    private static List<Customer> customers;

    static {

        customers = new ArrayList<>();

        Customer alex = new Customer(
                1,
                "Alex",
                "alex@gmail.com",
                21,
                Gender.MALE
        );

        Customer jamila = new Customer(
                2,
                "jamila",
                "jamila@gmail.com",
                19,
                Gender.MALE
        );

        customers.add(alex);
        customers.add(jamila);
    }

    @Override
    public List<Customer> selectAllCustomers() {
        return customers;
    }

    @Override
    public Optional<Customer> selectCustomerById(Integer customerId) {
        return customers.stream().filter(customer -> customer.getId().equals(customerId))
                .findFirst();
    }

    @Override
    public void insertCustomer(Customer customer) {
        customers.add(customer);
    }

    @Override
    public boolean existPersonWithEmail(String email) {
        return customers.stream()
                .anyMatch(customer -> customer.getEmail().equals(email));
    }

    @Override
    public boolean existPersonWithId(Integer id) {
        return customers.stream()
                .anyMatch(customer -> customer.getId().equals(id));
    }

    @Override
    public void deleteCustomerById(Integer id) {
        customers.stream().filter(c -> c.getId().equals(id))
                        .findFirst()
                        .ifPresent(customers::remove);
    }

    @Override
    public void updateCustomer(Customer customer) {
        customers.add(customer);
    }


}
