package com.example.demo.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Constraint(validatedBy = MaxStockSizeValidator.class)
@Target({ ElementType.TYPE, ElementType.FIELD, ElementType.ANNOTATION_TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface MaxStockSize {
    String message() default "{com.example.demo.dto.validator.MaxStockSize.message}";
    Class <?> [] groups() default {};
    Class <? extends Payload> [] payload() default {};

    int value();
}
