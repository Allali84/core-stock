package com.example.demo.entities;

import com.example.demo.dto.in.ShoeFilter.Color;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.math.BigInteger;

@Entity
@Table(name = "STOCK_SHOES")
@Data
@Builder
public class StockShoe {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  long id;
  BigInteger size;
  @Enumerated(EnumType.STRING)
  Color      color;
  BigInteger quantity;

  public static class StockShoeEntityBuilder {

  }


}
