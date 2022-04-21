package com.example.demo.core;

import com.example.demo.dto.in.ShoeFilter;
import com.example.demo.dto.out.StockDto;
import com.example.demo.dto.out.StockShoeDto;
import com.example.demo.entities.Stock;
import com.example.demo.entities.StockShoe;
import com.example.demo.exceptions.CapacityExceededException;
import com.example.demo.exceptions.FullStockException;
import com.example.demo.exceptions.MinimumCapacityException;
import com.example.demo.repositories.StockRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigInteger;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class StockCoreTest {

  @Mock
  private StockRepository stockRepository;

  @Test
  @DisplayName("Retrieve a stock with some state Test case")
  void retrieve_some_stock() {

    // GIVEN
    Stock stock = new Stock();
    stock.setShoes(List.of(new StockShoe(BigInteger.valueOf(41), StockShoe.Color.BLACK, BigInteger.valueOf(20), stock)));
    Mockito.when(stockRepository.findByName("")).thenReturn(Optional.of(stock));
    StockCoreImpl stockCoreImpl = new StockCoreImpl(stockRepository);

    // WHEN
    StockDto stock2 = stockCoreImpl.getAll("");

    // THEN
    Mockito.verify(stockRepository).findByName("");
    Assertions.assertEquals(stock.getShoes().size(), stock2.getShoes().size());
    Assertions.assertEquals(StockDto.State.SOME, stock2.getState());
    StockShoeDto stockShoe = stock2.getShoes().iterator().next();
    Assertions.assertEquals(BigInteger.valueOf(41), stockShoe.getSize());
    Assertions.assertEquals(ShoeFilter.Color.BLACK, stockShoe.getColor());
    Assertions.assertEquals(BigInteger.valueOf(20), stockShoe.getQuantity());
  }

  @Test
  @DisplayName("Retrieve a stock with full state Test case")
  void retrieve_full_stock() {

    // GIVEN
    Stock stock = new Stock();
    stock.setShoes(List.of(new StockShoe(BigInteger.valueOf(41), StockShoe.Color.BLACK, BigInteger.valueOf(30), stock)));
    Mockito.when(stockRepository.findByName("")).thenReturn(Optional.of(stock));
    StockCoreImpl stockCoreImpl = new StockCoreImpl(stockRepository);

    // WHEN
    StockDto stock2 = stockCoreImpl.getAll("");

    // THEN
    Mockito.verify(stockRepository).findByName("");
    Assertions.assertEquals(stock.getShoes().size(), stock2.getShoes().size());
    Assertions.assertEquals(StockDto.State.FULL, stock2.getState());
    StockShoeDto stockShoe = stock2.getShoes().iterator().next();
    Assertions.assertEquals(BigInteger.valueOf(41), stockShoe.getSize());
    Assertions.assertEquals(ShoeFilter.Color.BLACK, stockShoe.getColor());
    Assertions.assertEquals(BigInteger.valueOf(30), stockShoe.getQuantity());
  }

  @Test
  @DisplayName("Retrieve an empty stock Test case")
  void retrieve_empty_stock() {

    // GIVEN
    Mockito.when(stockRepository.findByName("")).thenReturn(Optional.empty());
    StockCoreImpl stockCoreImpl = new StockCoreImpl(stockRepository);

    // WHEN
    StockDto stock2 = stockCoreImpl.getAll("");

    // THEN
    Mockito.verify(stockRepository).findByName("");
    Assertions.assertEquals(0, stock2.getShoes().size());
    Assertions.assertEquals(StockDto.State.EMPTY, stock2.getState());
  }

  @SneakyThrows
  @Test
  @DisplayName("Update an empty stock Test case")
  void update_empty_stock() {

    // GIVEN
    Mockito.when(stockRepository.findByName("")).thenReturn(Optional.empty());
    Stock stock = new Stock();
    stock.setName("");
    stock.setShoes(List.of(new StockShoe(BigInteger.valueOf(41), StockShoe.Color.BLACK, BigInteger.valueOf(20), stock)));
    Mockito.when(stockRepository.save(stock)).thenReturn(stock);
    StockCoreImpl stockCoreImpl = new StockCoreImpl(stockRepository);

    // WHEN
    StockDto stock2 = stockCoreImpl.updateStock("",
            List.of(
                    StockShoeDto.builder()
                            .size(BigInteger.valueOf(41))
                            .color(ShoeFilter.Color.BLACK)
                            .quantity(BigInteger.valueOf(20))
                            .build()
            )
    );

    // THEN
    Mockito.verify(stockRepository).findByName("");
    Mockito.verify(stockRepository).save(stock);
    Assertions.assertEquals(1, stock2.getShoes().size());
    Assertions.assertEquals(StockDto.State.SOME, stock2.getState());
    StockShoeDto stockShoe = stock2.getShoes().iterator().next();
    Assertions.assertEquals(BigInteger.valueOf(41), stockShoe.getSize());
    Assertions.assertEquals(ShoeFilter.Color.BLACK, stockShoe.getColor());
    Assertions.assertEquals(BigInteger.valueOf(20), stockShoe.getQuantity());
  }

  @SneakyThrows
  @Test
  @DisplayName("Update not empty stock Test case")
  void update_not_empty_stock() {

    // GIVEN
    Stock stock = new Stock();
    stock.getShoes().add(new StockShoe(BigInteger.valueOf(41), StockShoe.Color.BLACK, BigInteger.valueOf(20), stock));
    Mockito.when(stockRepository.findByName("")).thenReturn(Optional.of(stock));
    Stock stock3 = new Stock();
    stock3.getShoes().add(new StockShoe(BigInteger.valueOf(41), StockShoe.Color.BLACK, BigInteger.valueOf(20), stock3));
    stock3.getShoes().add(new StockShoe(BigInteger.valueOf(42), StockShoe.Color.BLACK, BigInteger.valueOf(10), stock3));
    Mockito.when(stockRepository.save(stock3)).thenReturn(stock3);
    StockCoreImpl stockCoreImpl = new StockCoreImpl(stockRepository);

    // WHEN
    StockDto stock2 = stockCoreImpl.updateStock("",
            List.of(
                    StockShoeDto.builder()
                            .size(BigInteger.valueOf(42))
                            .color(ShoeFilter.Color.BLACK)
                            .quantity(BigInteger.valueOf(10))
                            .build()
            )
    );

    // THEN
    Mockito.verify(stockRepository).findByName("");
    Mockito.verify(stockRepository).save(stock3);
    Assertions.assertEquals(2, stock2.getShoes().size());
    Assertions.assertEquals(StockDto.State.FULL, stock2.getState());
    Iterator<StockShoeDto> iterator = stock2.getShoes().iterator();
    StockShoeDto stockShoe = iterator.next();
    Assertions.assertEquals(BigInteger.valueOf(41), stockShoe.getSize());
    Assertions.assertEquals(ShoeFilter.Color.BLACK, stockShoe.getColor());
    Assertions.assertEquals(BigInteger.valueOf(20), stockShoe.getQuantity());
    StockShoeDto stockShoe2 = iterator.next();
    Assertions.assertEquals(BigInteger.valueOf(42), stockShoe2.getSize());
    Assertions.assertEquals(ShoeFilter.Color.BLACK, stockShoe2.getColor());
    Assertions.assertEquals(BigInteger.valueOf(10), stockShoe2.getQuantity());
  }

  @SneakyThrows
  @Test
  @DisplayName("Update stock with already exist shoe Test case")
  void update_stock_already_exist_shoe() {

    // GIVEN
    Stock stock = new Stock();
    stock.getShoes().add(new StockShoe(BigInteger.valueOf(41), StockShoe.Color.BLACK, BigInteger.valueOf(20), stock));
    Mockito.when(stockRepository.findByName("")).thenReturn(Optional.of(stock));
    Stock stock3 = new Stock();
    stock3.setShoes(List.of(
            new StockShoe(BigInteger.valueOf(41), StockShoe.Color.BLACK, BigInteger.valueOf(30), stock3)
    ));
    Mockito.when(stockRepository.save(stock3)).thenReturn(stock3);
    StockCoreImpl stockCoreImpl = new StockCoreImpl(stockRepository);

    // WHEN
    StockDto stock2 = stockCoreImpl.updateStock("",
            List.of(
                    StockShoeDto.builder()
                            .size(BigInteger.valueOf(41))
                            .color(ShoeFilter.Color.BLACK)
                            .quantity(BigInteger.valueOf(10))
                            .build()
            )
    );

    // THEN
    Mockito.verify(stockRepository).findByName("");
    Mockito.verify(stockRepository).save(stock3);
    Assertions.assertEquals(1, stock2.getShoes().size());
    Assertions.assertEquals(StockDto.State.FULL, stock2.getState());
    Iterator<StockShoeDto> iterator = stock2.getShoes().iterator();
    StockShoeDto stockShoe = iterator.next();
    Assertions.assertEquals(BigInteger.valueOf(41), stockShoe.getSize());
    Assertions.assertEquals(ShoeFilter.Color.BLACK, stockShoe.getColor());
    Assertions.assertEquals(BigInteger.valueOf(30), stockShoe.getQuantity());
  }

  @Test
  @DisplayName("Update already full stock Test case")
  void update_already_full_stock() {

    // GIVEN
    Stock stock = new Stock();
    stock.setShoes(List.of(new StockShoe(BigInteger.valueOf(41), StockShoe.Color.BLACK, BigInteger.valueOf(30), stock)));
    Mockito.when(stockRepository.findByName("")).thenReturn(Optional.of(stock));
    StockCoreImpl stockCoreImpl = new StockCoreImpl(stockRepository);

    // WHEN & THEN
    assertThrows(FullStockException.class, () -> {
      stockCoreImpl.updateStock("",
              List.of(
                      StockShoeDto.builder()
                              .size(BigInteger.valueOf(42))
                              .color(ShoeFilter.Color.BLACK)
                              .quantity(BigInteger.valueOf(10))
                              .build()
              )
      );
    });
  }

  @Test
  @DisplayName("Update & exceed stock capacity Test case")
  void update_and_exceed_stock_capacity() {

    // GIVEN
    Stock stock = new Stock();
    stock.setShoes(List.of(new StockShoe(BigInteger.valueOf(41), StockShoe.Color.BLACK, BigInteger.valueOf(20), stock)));
    Mockito.when(stockRepository.findByName("")).thenReturn(Optional.of(stock));
    StockCoreImpl stockCoreImpl = new StockCoreImpl(stockRepository);

    // WHEN & THEN
    assertThrows(CapacityExceededException.class, () -> {
      stockCoreImpl.updateStock("",
              List.of(
                      StockShoeDto.builder()
                              .size(BigInteger.valueOf(42))
                              .color(ShoeFilter.Color.BLACK)
                              .quantity(BigInteger.valueOf(20))
                              .build()
              )
      );
    });
  }

  @Test
  @DisplayName("Stock capacity cannot be negative Test case")
  void update_stock_capacity_under_zero() {

    // GIVEN
    Mockito.when(stockRepository.findByName("")).thenReturn(Optional.empty());
    StockCoreImpl stockCoreImpl = new StockCoreImpl(stockRepository);

    // WHEN & THEN
    assertThrows(MinimumCapacityException.class, () -> {
      stockCoreImpl.updateStock("",
              List.of(
                      StockShoeDto.builder()
                              .size(BigInteger.valueOf(42))
                              .color(ShoeFilter.Color.BLACK)
                              .quantity(BigInteger.valueOf(-1))
                              .build()
              )
      );
    });
  }

}
