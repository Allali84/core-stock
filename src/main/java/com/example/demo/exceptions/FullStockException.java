package com.example.demo.exceptions;

public class FullStockException extends GeneralException {
    public FullStockException() {
        super("ERR-002", "Stock is already full");
    }
}
