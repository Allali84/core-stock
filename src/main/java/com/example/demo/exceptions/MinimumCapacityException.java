package com.example.demo.exceptions;

public class MinimumCapacityException extends Exception{
    public MinimumCapacityException() {
        super("Shoes quantity cannot be negative");
    }
}
