package com.example.demo.repositories;

import com.example.demo.entities.Stock;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface StockRepository extends CrudRepository<Stock, Long> {
    Optional<Stock> findByName(String name);
}
