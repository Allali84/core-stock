package com.example.demo.entities;

import com.example.demo.dto.in.ShoeFilter;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.Objects;

@Entity
@Table(name = "STOCK_SHOES")
@Data
public class StockShoe {


  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  long id;

  BigInteger size;
  @Enumerated(EnumType.STRING)
  Color color;
  BigInteger quantity;

  @ManyToOne
  @JoinColumn(name = "STOCK_ID", nullable = false)
  @ToString.Exclude
  @EqualsAndHashCode.Exclude
  protected Stock stock;

  public StockShoe() {
  }

  public StockShoe(BigInteger size, Color color, BigInteger quantity, Stock stock) {
    this.size = size;
    this.color = color;
    this.quantity = quantity;
    this.stock = stock;
  }

  public StockShoe(BigInteger size, ShoeFilter.Color color, BigInteger quantity, Stock stock) {
    this.size = size;
    this.color = ShoeFilter.Color.BLACK == color ? Color.BLACK : Color.BLUE;
    this.quantity = quantity;
    this.stock = stock;
  }

  public enum Color{

    BLACK,
    BLUE,
    ;

    public ShoeFilter.Color toDto() {
      if (this == BLACK)
        return ShoeFilter.Color.BLACK;
      else
        return ShoeFilter.Color.BLUE;
    }
  }
}
