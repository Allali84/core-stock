package com.example.demo.entities;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.math.BigInteger;
import java.util.List;
import java.util.Set;

public class StockShoeTest {

    @Test
    @DisplayName("Max capacity exceeded with one shoe Test case")
    void max_capacity_exceeded_one_shoe() {
        // GIVEN
        Stock stock = new Stock();
        stock.setShoes(List.of(new StockShoe(new StockShoeId(BigInteger.valueOf(41), StockShoeId.Color.BLACK), BigInteger.valueOf(50))));

        // WHEN
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Stock>> violations = validator.validate(stock);

        // THEN
        Assertions.assertEquals(1, violations.size());
        Assertions.assertEquals("Shoes stock must not exceed 30", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Max capacity exceeded with two shoes Test case")
    void max_capacity_exceeded_two_shoes() {
        // GIVEN
        Stock stock = new Stock();
        stock.setShoes(List.of(
                new StockShoe(new StockShoeId(BigInteger.valueOf(41), StockShoeId.Color.BLACK), BigInteger.valueOf(20)),
                new StockShoe(new StockShoeId(BigInteger.valueOf(42), StockShoeId.Color.BLACK), BigInteger.valueOf(15))
                ));

        // WHEN
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Stock>> violations = validator.validate(stock);

        // THEN
        Assertions.assertEquals(1, violations.size());
        Assertions.assertEquals("Shoes stock must not exceed 30", violations.iterator().next().getMessage());
    }

    @Test
    @DisplayName("Max capacity not exceeded Test case")
    void max_capacity_not_exceeded() {
        // GIVEN
        Stock stock = new Stock();
        stock.setShoes(List.of(
                new StockShoe(new StockShoeId(BigInteger.valueOf(41), StockShoeId.Color.BLACK), BigInteger.valueOf(20)),
                new StockShoe(new StockShoeId(BigInteger.valueOf(42), StockShoeId.Color.BLACK), BigInteger.valueOf(10))
        ));

        // WHEN
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();
        Set<ConstraintViolation<Stock>> violations = validator.validate(stock);

        // THEN
        Assertions.assertEquals(0, violations.size());
    }

}
