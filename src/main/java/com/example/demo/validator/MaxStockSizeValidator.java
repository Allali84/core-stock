package com.example.demo.validator;

import com.example.demo.entities.StockShoe;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.math.BigInteger;
import java.util.List;

public class MaxStockSizeValidator implements ConstraintValidator<MaxStockSize, List<StockShoe>> {

    private int value;

    @Override
    public void initialize(MaxStockSize constraintAnnotation) {
        value = constraintAnnotation.value();
    }

    @Override
    public boolean isValid(List<StockShoe> values, ConstraintValidatorContext constraintValidatorContext) {
        if (values != null) {
            return values.stream().map(StockShoe::getQuantity).reduce(BigInteger.ZERO, BigInteger::add).intValue() <= this.value;
        } else {
            return true;
        }
    }
}
