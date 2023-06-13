package com.uhuru.customer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CustomerRowMapperTest {

    @BeforeEach
    void setUp() {
    }


    @Test
    void mapRow() throws SQLException {
        // Given
        CustomerRowMapper customerRowMapper = new CustomerRowMapper();

        ResultSet resultSet = mock(ResultSet.class);
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getInt("age")).thenReturn(19);
        when(resultSet.getString("name")).thenReturn("test");
        when(resultSet.getString("email")).thenReturn("test@gmail.com");
        when(resultSet.getString("gender")).thenReturn(String.valueOf(Gender.MALE));

        // When
        Customer actual = customerRowMapper.mapRow(resultSet, 1);

        // Then
        Customer expected = new Customer(
                1, "test", "test@gmail.com", 19, Gender.MALE
        );

        assertThat(actual).isEqualTo(expected);
    }
}