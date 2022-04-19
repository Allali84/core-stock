package com.example.demo.entities;

import lombok.Data;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.math.BigInteger;

@Entity
@Table(name = "STOCK_SHOES")
@Data
public class StockShoe {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  long id;
  BigInteger size;
  @Enumerated(EnumType.STRING)
  Color      color;
  BigInteger quantity;
  @ManyToOne
  @Cascade(value={org.hibernate.annotations.CascadeType.ALL})
  @JoinColumn(name="STOCK_ID")
  protected Stock stock;

  public StockShoe(BigInteger size, Color color, BigInteger quantity) {
    this.size = size;
    this.color = color;
    this.quantity = quantity;
  }

  public enum Color{

    BLACK,
    BLUE,
    ;

  }


}
