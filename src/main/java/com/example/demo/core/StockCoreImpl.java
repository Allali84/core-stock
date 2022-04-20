package com.example.demo.core;

import com.example.demo.dto.out.StockDto;
import com.example.demo.dto.out.StockShoeDto;
import com.example.demo.entities.Stock;
import com.example.demo.entities.StockShoe;
import com.example.demo.entities.StockShoeId;
import com.example.demo.exceptions.CapacityExceededException;
import com.example.demo.exceptions.FullStockException;
import com.example.demo.exceptions.MinimumCapacityException;
import com.example.demo.repositories.StockShoeRepository;
import lombok.RequiredArgsConstructor;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Implementation(version = 2)
@RequiredArgsConstructor
public class StockCoreImpl extends AbstractStockCore {

  private final StockShoeRepository stockShoeRepository;

  @Override
  public StockDto getAll() {
    Iterable<Stock> stocks = stockShoeRepository.findAll();
    if (!stocks.iterator().hasNext()) {
      return StockDto.builder()
              .state(StockDto.State.EMPTY)
              .shoes(new ArrayList<>())
              .build();
    }
   List<StockShoeDto> shoes = stocks.iterator().next().getShoes().stream()
            .map(it -> StockShoeDto.builder().size(it.getId().getSize()).quantity(it.getQuantity()).color(it.getId().getColor().toDto()).build())
            .collect(Collectors.toList());

    return StockDto.builder()
                .shoes(shoes)
                .build().calculateState();
  }

  @Override
  public StockDto updateStock(List<StockShoeDto> shoes) throws Exception {
    StockDto existingStockDto = getAll();
    List<StockShoeDto> existingShoes = existingStockDto.getShoes();

    int existingTotalQuantity = existingShoes.stream().map(StockShoeDto::getQuantity).reduce(BigInteger.ZERO, BigInteger::add).intValue();
    int totalQuantityToBeAdded = shoes.stream().map(StockShoeDto::getQuantity).reduce(BigInteger.ZERO, BigInteger::add).intValue();
    checkStore(existingStockDto, existingTotalQuantity, totalQuantityToBeAdded);

    // Check if we already have the shoe :
    // if yes -> accumulate the quantity
    // if not -> add the shoe as new one
    shoes.forEach(it -> {
      Optional<StockShoeDto> shoeDtoOptional = existingShoes.stream().filter(it2 -> it.getColor() == it2.getColor() && Objects.equals(it.getSize(), it2.getSize())).findFirst();
      StockShoeDto shoeDto;
      if (shoeDtoOptional.isPresent()) {
        shoeDto = shoeDtoOptional.get();
        existingShoes.remove(shoeDto);
        existingShoes.add(StockShoeDto.builder().size(shoeDto.getSize()).color(shoeDto.getColor()).quantity(shoeDto.getQuantity().add(it.getQuantity())).build());
      } else {
        existingShoes.add(it);
      }
    });

    // Map the dto to entity to be saved
    List<StockShoe> entities = existingShoes.stream()
            .map(it -> new StockShoe(new StockShoeId(it.getSize(), it.getColor()), it.getQuantity()))
            .collect(Collectors.toList());
    Stock stock = new Stock();
    stock.setShoes(entities);
    stockShoeRepository.save(stock);

    return StockDto.builder()
            .shoes(existingShoes)
            .build().calculateState();
  }

  /**
   * Check if there is a problem with the upcoming shoes list
   *
   *
   * @param existingStockDto what we already have in the stock
   * @param existingTotalQuantity the total of the capacity of our current stock
   * @param totalQuantityToBeAdded the total of the capacity of the upcoming shoes list
   * @throws FullStockException this exception is thrown when our store is already full
   * @throws CapacityExceededException this exception is thrown if what we want to add will exceed the allowed capacity
   * @throws MinimumCapacityException this exception is thrown if what we want to add has a negative value as quantity
   *
   */
  private void checkStore(StockDto existingStockDto, int existingTotalQuantity, int totalQuantityToBeAdded)
          throws FullStockException, CapacityExceededException, MinimumCapacityException {
    if (StockDto.State.FULL.equals(existingStockDto.getState())) {
      throw new FullStockException();
    }
    if (existingTotalQuantity + totalQuantityToBeAdded > 30) {
      throw new CapacityExceededException(30 - existingTotalQuantity);
    }
    if (totalQuantityToBeAdded < 0) {
      throw new MinimumCapacityException();
    }
  }
}
