package com.example.demo.core;

import com.example.demo.dto.in.ShoeFilter;
import com.example.demo.dto.out.StockDto;
import com.example.demo.dto.out.StockShoeDto;
import com.example.demo.entities.Stock;
import com.example.demo.entities.StockShoe;
import com.example.demo.entities.StockShoeId;
import com.example.demo.exceptions.CapacityExceededException;
import com.example.demo.exceptions.FullStockException;
import com.example.demo.exceptions.MinimumCapacityException;
import com.example.demo.repositories.StockShoeRepository;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;

@ExtendWith(MockitoExtension.class)
public class StockCoreTest {

  @Mock
  private StockShoeRepository stockShoeRepository;

  @Test
  @DisplayName("Retrieve a stock with some state Test case")
  void retrieve_some_stock() {

    // GIVEN
    Stock stock = new Stock();
    stock.setShoes(List.of(new StockShoe(new StockShoeId(BigInteger.valueOf(41), StockShoeId.Color.BLACK), BigInteger.valueOf(20))));
    Mockito.when(stockShoeRepository.findAll()).thenReturn(List.of(stock));
    StockCoreImpl stockCoreImpl = new StockCoreImpl(stockShoeRepository);

    // WHEN
    StockDto stock2 = stockCoreImpl.getAll();

    // THEN
    Mockito.verify(stockShoeRepository).findAll();
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
    stock.setShoes(List.of(new StockShoe(new StockShoeId(BigInteger.valueOf(41), StockShoeId.Color.BLACK), BigInteger.valueOf(30))));
    Mockito.when(stockShoeRepository.findAll()).thenReturn(List.of(stock));
    StockCoreImpl stockCoreImpl = new StockCoreImpl(stockShoeRepository);

    // WHEN
    StockDto stock2 = stockCoreImpl.getAll();

    // THEN
    Mockito.verify(stockShoeRepository).findAll();
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
    Mockito.when(stockShoeRepository.findAll()).thenReturn(new ArrayList<>());
    StockCoreImpl stockCoreImpl = new StockCoreImpl(stockShoeRepository);

    // WHEN
    StockDto stock2 = stockCoreImpl.getAll();

    // THEN
    Mockito.verify(stockShoeRepository).findAll();
    Assertions.assertEquals(0, stock2.getShoes().size());
    Assertions.assertEquals(StockDto.State.EMPTY, stock2.getState());
  }

  @SneakyThrows
  @Test
  @DisplayName("Update an empty stock Test case")
  void update_empty_stock() {

    // GIVEN
    Mockito.when(stockShoeRepository.findAll()).thenReturn(new ArrayList<>());
    Stock stock = new Stock();
    stock.setShoes(List.of(new StockShoe(new StockShoeId(BigInteger.valueOf(41), StockShoeId.Color.BLACK), BigInteger.valueOf(20))));
    Mockito.when(stockShoeRepository.save(stock)).thenReturn(stock);
    StockCoreImpl stockCoreImpl = new StockCoreImpl(stockShoeRepository);

    // WHEN
    StockDto stock2 = stockCoreImpl.updateStock(
            List.of(
                    StockShoeDto.builder()
                            .size(BigInteger.valueOf(41))
                            .color(ShoeFilter.Color.BLACK)
                            .quantity(BigInteger.valueOf(20))
                            .build()
            )
    );

    // THEN
    Mockito.verify(stockShoeRepository).findAll();
    Mockito.verify(stockShoeRepository).save(stock);
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
    stock.setShoes(List.of(new StockShoe(new StockShoeId(BigInteger.valueOf(41), StockShoeId.Color.BLACK), BigInteger.valueOf(20))));
    Mockito.when(stockShoeRepository.findAll()).thenReturn(List.of(stock));
    Stock stock3 = new Stock();
    stock3.setShoes(List.of(
            new StockShoe(new StockShoeId(BigInteger.valueOf(41), StockShoeId.Color.BLACK), BigInteger.valueOf(20)),
            new StockShoe(new StockShoeId(BigInteger.valueOf(42), StockShoeId.Color.BLACK), BigInteger.valueOf(10))
            ));
    Mockito.when(stockShoeRepository.save(stock3)).thenReturn(stock3);
    StockCoreImpl stockCoreImpl = new StockCoreImpl(stockShoeRepository);

    // WHEN
    StockDto stock2 = stockCoreImpl.updateStock(
            List.of(
                    StockShoeDto.builder()
                            .size(BigInteger.valueOf(42))
                            .color(ShoeFilter.Color.BLACK)
                            .quantity(BigInteger.valueOf(10))
                            .build()
            )
    );

    // THEN
    Mockito.verify(stockShoeRepository).findAll();
    Mockito.verify(stockShoeRepository).save(stock3);
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
    stock.setShoes(List.of(new StockShoe(new StockShoeId(BigInteger.valueOf(41), StockShoeId.Color.BLACK), BigInteger.valueOf(20))));
    Mockito.when(stockShoeRepository.findAll()).thenReturn(List.of(stock));
    Stock stock3 = new Stock();
    stock3.setShoes(List.of(
            new StockShoe(new StockShoeId(BigInteger.valueOf(41), StockShoeId.Color.BLACK), BigInteger.valueOf(30))
    ));
    Mockito.when(stockShoeRepository.save(stock3)).thenReturn(stock3);
    StockCoreImpl stockCoreImpl = new StockCoreImpl(stockShoeRepository);

    // WHEN
    StockDto stock2 = stockCoreImpl.updateStock(
            List.of(
                    StockShoeDto.builder()
                            .size(BigInteger.valueOf(41))
                            .color(ShoeFilter.Color.BLACK)
                            .quantity(BigInteger.valueOf(10))
                            .build()
            )
    );

    // THEN
    Mockito.verify(stockShoeRepository).findAll();
    Mockito.verify(stockShoeRepository).save(stock3);
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
    stock.setShoes(List.of(new StockShoe(new StockShoeId(BigInteger.valueOf(41), StockShoeId.Color.BLACK), BigInteger.valueOf(30))));
    Mockito.when(stockShoeRepository.findAll()).thenReturn(List.of(stock));
    StockCoreImpl stockCoreImpl = new StockCoreImpl(stockShoeRepository);

    // WHEN & THEN
    assertThrows(FullStockException.class, () -> {
      stockCoreImpl.updateStock(
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
    stock.setShoes(List.of(new StockShoe(new StockShoeId(BigInteger.valueOf(41), StockShoeId.Color.BLACK), BigInteger.valueOf(20))));
    Mockito.when(stockShoeRepository.findAll()).thenReturn(List.of(stock));
    StockCoreImpl stockCoreImpl = new StockCoreImpl(stockShoeRepository);

    // WHEN & THEN
    assertThrows(CapacityExceededException.class, () -> {
      stockCoreImpl.updateStock(
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
    Mockito.when(stockShoeRepository.findAll()).thenReturn(new ArrayList<>());
    StockCoreImpl stockCoreImpl = new StockCoreImpl(stockShoeRepository);

    // WHEN & THEN
    assertThrows(MinimumCapacityException.class, () -> {
      stockCoreImpl.updateStock(
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
