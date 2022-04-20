package com.example.demo.exceptions;

public class MinimumCapacityException extends GeneralException{
    public MinimumCapacityException() {
        super("ERR-0003", "Shoes quantity cannot be negative");
    }
}
