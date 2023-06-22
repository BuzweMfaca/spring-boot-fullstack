package com.uhuru.customer;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Objects;
import java.util.Optional;


@Repository("jdbc")
public class CustomerJDBCDataAccessService implements CustomerDao{

    private final JdbcTemplate jdbcTemplate;
    private final CustomerRowMapper customerRowMapper;

    public CustomerJDBCDataAccessService(JdbcTemplate jdbcTemplate, CustomerRowMapper customerRowMapper){
        this.jdbcTemplate = jdbcTemplate;
        this.customerRowMapper = customerRowMapper;
    }

    @Override
    public List<Customer> selectAllCustomers() {

        String sql = """
                        SELECT id, name, email, password, age, gender 
                        FROM customer
                    """;

        List<Customer> customers = jdbcTemplate.query(sql, customerRowMapper);

        return customers;
    }

    @Override
    public Optional<Customer> selectCustomerById(Integer id) {

        String sql = """
                        SELECT id, name, email, password, age , gender
                        FROM customer
                        Where id = ?
                    """;

        return jdbcTemplate.query(sql, customerRowMapper, id)
                .stream()
                .findFirst();

    }

    @Override
    public void insertCustomer(Customer customer) {

        String sql = """
                        INSERT INTO customer(name, email, password, age, gender)
                        VALUES(?,?,?,?, ?)
                     """;

        int result = jdbcTemplate.update(
                sql,
                customer.getName(),
                customer.getEmail(),
                customer.getPassword(),
                customer.getAge(),
                customer.getGender().name()
        );

    }

    @Override
    public boolean existPersonWithEmail(String email) {

        String sql = """
                        SELECT count(id)
                        FROM customer
                        Where email = ? 
                     """;

        Integer result =  jdbcTemplate.queryForObject(sql, Integer.class, email);
        return result != null && result > 0 ;

    }

    @Override
    public boolean existPersonWithId(Integer id) {
        String sql = """
                        SELECT count(id)
                        FROM customer
                        Where id = ? 
                     """;
        Integer result =  jdbcTemplate.queryForObject(sql, Integer.class, id);
        return result != null && result > 0 ;
    }

    @Override
    public void deleteCustomerById(Integer id) {
        String sql = """
                        DELETE 
                        FROM customer
                        Where id = ? 
                     """;

        jdbcTemplate.update(sql, id);

    }

    @Override
    public void updateCustomer(Customer customer) {

        if(Objects.nonNull(customer.getName())){
            String sql = """
                            UPDATE customer SET name = ?
                            WHERE id = ?
                         """;
            jdbcTemplate.update(sql, customer.getName(),customer.getId());
        }

        if(Objects.nonNull(customer.getEmail())){
            String sql = """
                            UPDATE customer SET email = ?
                            WHERE id = ?
                         """;
            jdbcTemplate.update(sql, customer.getEmail(),customer.getId());
        }

        if(Objects.nonNull(customer.getEmail())){
            String sql = """
                            UPDATE customer SET password = ?
                            WHERE id = ?
                         """;
            jdbcTemplate.update(sql, customer.getPassword(),customer.getId());

        }

        if(Objects.nonNull(customer.getAge())){
            String sql = """
                            UPDATE customer SET age = ?
                            WHERE id = ?
                         """;
            jdbcTemplate.update(sql, customer.getAge(),customer.getId());
        }
    }

    @Override
    public Optional<Customer> selectUserByEmail(String email) {
        String sql = """
                        SELECT id, name, email, password, age , gender
                        FROM customer
                        Where email = ?
                    """;

        return jdbcTemplate.query(sql, customerRowMapper, email)
                .stream()
                .findFirst();
    }
}
