package com.example.demo.core;

import com.example.demo.dto.out.StockDto;
import com.example.demo.dto.out.StockShoeDto;
import com.example.demo.entities.Stock;
import com.example.demo.entities.StockShoe;
import com.example.demo.exceptions.CapacityExceededException;
import com.example.demo.exceptions.FullStockException;
import com.example.demo.exceptions.MinimumCapacityException;
import com.example.demo.repositories.StockRepository;
import lombok.RequiredArgsConstructor;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

@Implementation(version = 2)
@RequiredArgsConstructor
public class StockCoreImpl extends AbstractStockCore {

  private final StockRepository stockRepository;

  @Override
  public StockDto getAll(String name) {
    Optional<Stock> existingStock = stockRepository.findByName(name);
    if (existingStock.isEmpty()) {
      return StockDto.builder()
              .state(StockDto.State.EMPTY)
              .shoes(new ArrayList<>())
              .build();
    }
    List<StockShoe> shoesEntities = existingStock.get().getShoes();
    List<StockShoeDto> shoes = shoesEntities.stream()
            .map(it -> StockShoeDto.builder().size(it.getSize()).quantity(it.getQuantity()).color(it.getColor().toDto()).build())
            .collect(Collectors.toList());

    return StockDto.builder()
                .shoes(shoes)
                .build().calculateState();
  }

  @Override
  public StockDto updateStock(String name, List<StockShoeDto> shoes) throws Exception {
    Optional<Stock> existingStock = stockRepository.findByName(name);
    Stock stock = existingStock.orElseGet(Stock::new);
    List<StockShoe> existingShoes = stock.getShoes();

    int existingTotalQuantity = existingShoes.stream().map(StockShoe::getQuantity).reduce(BigInteger.ZERO, BigInteger::add).intValue();
    int totalQuantityToBeAdded = shoes.stream().map(StockShoeDto::getQuantity).reduce(BigInteger.ZERO, BigInteger::add).intValue();
    checkStore(existingTotalQuantity, totalQuantityToBeAdded, shoes);

    // Check if we already have the shoe :
    // if yes -> accumulate the quantity
    // if not -> add the shoe as new one
    shoes.forEach(it -> {
      Optional<StockShoe> shoeDtoOptional = existingShoes.stream().filter(it2 -> it.getColor() == it2.getColor().toDto() && Objects.equals(it.getSize(), it2.getSize())).findFirst();
      StockShoe shoeDto;
      if (shoeDtoOptional.isPresent()) {
        shoeDto = shoeDtoOptional.get();
        existingShoes.remove(shoeDto);
        shoeDto.setQuantity(shoeDto.getQuantity().add(it.getQuantity()));
        existingShoes.add(shoeDto);
      } else {
        existingShoes.add(new StockShoe(it.getSize(), it.getColor(), it.getQuantity(), stock));
      }
    });

    // Save the entity
    stock.setShoes(existingShoes);
    if (existingStock.isEmpty()) {
      stock.setName(name);
    }
    stockRepository.save(stock);

    // Map the entity back to the dto
    List<StockShoeDto> newShoes = existingShoes.stream().map(it ->
            StockShoeDto.builder().size(it.getSize())
                    .color(it.getColor().toDto())
                    .quantity(it.getQuantity())
                    .build()).collect(Collectors.toList());

    return StockDto.builder()
            .shoes(newShoes)
            .build().calculateState();
  }

  /**
   * Check if there is a problem with the upcoming shoes list
   *
   * @param existingTotalQuantity  the total of the capacity of our current stock
   * @param totalQuantityToBeAdded the total of the capacity of the upcoming shoes list
   * @param shoes we need this list to check if there is one that has a negative value
   * @throws FullStockException        this exception is thrown when our store is already full
   * @throws CapacityExceededException this exception is thrown if what we want to add will exceed the allowed capacity
   * @throws MinimumCapacityException  this exception is thrown if what we want to add a negative value as quantity
   */
  private void checkStore(int existingTotalQuantity, int totalQuantityToBeAdded, List<StockShoeDto> shoes)
          throws FullStockException, CapacityExceededException, MinimumCapacityException {
    if (existingTotalQuantity == 30) {
      throw new FullStockException();
    }
    if (existingTotalQuantity + totalQuantityToBeAdded > 30) {
      throw new CapacityExceededException(30 - existingTotalQuantity);
    }
    if (shoes.stream().anyMatch(it -> it.getQuantity().intValue() < 0)) {
      throw new MinimumCapacityException();
    }
  }
}
