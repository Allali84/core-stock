package com.example.demo.repositories;

import com.example.demo.config.JpaConfig;
import com.example.demo.entities.Stock;
import com.example.demo.entities.StockShoe;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.support.AnnotationConfigContextLoader;

import javax.transaction.Transactional;
import java.math.BigInteger;
import java.util.List;


@ExtendWith(SpringExtension.class)
@ContextConfiguration(
        classes = { JpaConfig.class },
        loader = AnnotationConfigContextLoader.class)
@Transactional
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class StockRepositoryTest {

    @Autowired
    private StockRepository stockRepository;

    @Test
    @DisplayName("Save & retrieve a stock Test case")
    void save_and_retrieve_stock() {
        // GIVEN
        Stock stock = new Stock();
        stock.setShoes(List.of(new StockShoe(BigInteger.valueOf(41), StockShoe.Color.BLACK, BigInteger.valueOf(20), stock)));

        // WHEN
        stockRepository.save(stock);
        Stock stock2 = stockRepository.findAll().iterator().next();

        // THEN
        Assertions.assertEquals(stock.getShoes().size(), stock2.getShoes().size());
        StockShoe stockShoe = stock2.getShoes().iterator().next();
        Assertions.assertEquals(BigInteger.valueOf(41), stockShoe.getSize());
        Assertions.assertEquals(StockShoe.Color.BLACK, stockShoe.getColor());
        Assertions.assertEquals(BigInteger.valueOf(20), stockShoe.getQuantity());
    }

    @Test
    @DisplayName("Update a stock Test case")
    void update_stock() {
        // GIVEN
        Stock stock = new Stock();
        stock.setShoes(List.of(new StockShoe(BigInteger.valueOf(41), StockShoe.Color.BLACK, BigInteger.valueOf(20), stock)));
        stockRepository.save(stock);

        // WHEN
        Stock stock2 = stockRepository.findAll().iterator().next();
        StockShoe stockShoe2 = stock2.getShoes().iterator().next();
        stockShoe2.setQuantity(BigInteger.valueOf(30));
        Stock stock3 = stockRepository.findAll().iterator().next();

        // THEN
        Assertions.assertEquals(stock.getShoes().size(), stock3.getShoes().size());
        StockShoe stockShoe = stock3.getShoes().iterator().next();
        Assertions.assertEquals(BigInteger.valueOf(41), stockShoe.getSize());
        Assertions.assertEquals(StockShoe.Color.BLACK, stockShoe.getColor());
        Assertions.assertEquals(BigInteger.valueOf(30), stockShoe.getQuantity());
    }

}
