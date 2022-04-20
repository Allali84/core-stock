package com.example.demo.repositories;

import com.example.demo.entities.Stock;
import org.springframework.data.repository.CrudRepository;

public interface StockShoeRepository extends CrudRepository<Stock, Long> {

}
