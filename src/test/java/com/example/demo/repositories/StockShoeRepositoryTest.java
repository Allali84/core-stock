package com.example.demo.repositories;

import com.example.demo.config.JpaConfig;
import com.example.demo.entities.Stock;
import com.example.demo.entities.StockShoe;
import com.example.demo.entities.StockShoeId;
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
public class StockShoeRepositoryTest {

    @Autowired
    private StockShoeRepository stockShoeRepository;

    @Test
    @DisplayName("Save & retrieve a stock Test case")
    void save_and_retrieve_stock() {
        // GIVEN
        Stock stock = new Stock();
        stock.setShoes(List.of(new StockShoe(new StockShoeId(BigInteger.valueOf(41), StockShoeId.Color.BLACK), BigInteger.valueOf(20))));

        // WHEN
        stockShoeRepository.save(stock);
        Stock stock2 = stockShoeRepository.findAll().iterator().next();

        // THEN
        Assertions.assertEquals(stock.getShoes().size(), stock2.getShoes().size());
        StockShoe stockShoe = stock2.getShoes().iterator().next();
        Assertions.assertEquals(BigInteger.valueOf(41), stockShoe.getId().getSize());
        Assertions.assertEquals(StockShoeId.Color.BLACK, stockShoe.getId().getColor());
        Assertions.assertEquals(BigInteger.valueOf(20), stockShoe.getQuantity());
    }

    @Test
    @DisplayName("Update a stock Test case")
    void update_stock() {
        // GIVEN
        Stock stock = new Stock();
        stock.setShoes(List.of(new StockShoe(new StockShoeId(BigInteger.valueOf(41), StockShoeId.Color.BLACK), BigInteger.valueOf(20))));
        stockShoeRepository.save(stock);

        // WHEN
        Stock stock2 = stockShoeRepository.findAll().iterator().next();
        StockShoe stockShoe2 = stock2.getShoes().iterator().next();
        stockShoe2.setQuantity(BigInteger.valueOf(30));
        Stock stock3 = stockShoeRepository.findAll().iterator().next();

        // THEN
        Assertions.assertEquals(stock.getShoes().size(), stock3.getShoes().size());
        StockShoe stockShoe = stock3.getShoes().iterator().next();
        Assertions.assertEquals(BigInteger.valueOf(41), stockShoe.getId().getSize());
        Assertions.assertEquals(StockShoeId.Color.BLACK, stockShoe.getId().getColor());
        Assertions.assertEquals(BigInteger.valueOf(30), stockShoe.getQuantity());
    }

}
