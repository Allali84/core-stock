package com.example.demo.exceptions;

public class CapacityExceededException extends GeneralException{
    public CapacityExceededException(Integer capacityMax) {
        super("ERR-0001", "You can not add more than " + capacityMax);
    }
}
