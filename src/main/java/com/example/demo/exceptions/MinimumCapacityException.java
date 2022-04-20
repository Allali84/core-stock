package com.example.demo.exceptions;

public class MinimumCapacityException extends Exception{
    public MinimumCapacityException() {
        super("Stock capacity cannot be negative");
    }
}
