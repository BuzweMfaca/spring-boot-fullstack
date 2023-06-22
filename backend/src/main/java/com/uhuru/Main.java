package com.uhuru;


import com.github.javafaker.Faker;
import com.github.javafaker.Name;
import com.uhuru.customer.Customer;
import com.uhuru.customer.CustomerRepository;
import com.uhuru.customer.Gender;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Random;
import java.util.UUID;


@SpringBootApplication
public class Main {

    public static void main(String[] args) {

        SpringApplication.run(Main.class, args);

    }

    @Bean
    CommandLineRunner runner(
            CustomerRepository repository,
            PasswordEncoder passwordEncoder)
    {

        return args -> {
            var faker = new Faker();
            Random random = new Random();
            Name name = faker.name();
            String firstName = name.firstName();
            String lastName = name.lastName();
            Customer customer = new Customer(
                    firstName + " " + lastName,
                    firstName.toLowerCase() + "." + lastName.toLowerCase() + "@gmail.com",
                    passwordEncoder.encode(UUID.randomUUID().toString()),
                    random.nextInt(16, 99),
                    Gender.MALE
            );



            List<Customer> customers = List.of(customer);
            repository.saveAll(customers);
        };
    }

}