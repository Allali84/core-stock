package com.example.demo.exceptions;

public class CapacityExceededException extends Exception{
    public CapacityExceededException(Integer capacityMax) {
        super("You can not add more than " + capacityMax);
    }
}
