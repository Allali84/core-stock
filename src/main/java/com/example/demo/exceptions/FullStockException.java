package com.example.demo.exceptions;

public class FullStockException extends Exception {
    public FullStockException() {
        super("Stock is already full");
    }
}
