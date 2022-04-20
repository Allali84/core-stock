package com.example.demo.entities;

import lombok.Data;
import org.hibernate.annotations.Cascade;

import javax.persistence.*;
import java.math.BigInteger;

@Entity
@Table(name = "STOCK_SHOES")
@Data
public class StockShoe {

  @EmbeddedId
  StockShoeId id;
  BigInteger quantity;
  @ManyToOne
  @Cascade(value={org.hibernate.annotations.CascadeType.ALL})
  @JoinColumn(name="STOCK_ID")
  protected Stock stock;

  public StockShoe(StockShoeId id, BigInteger quantity) {
    this.id = id;
    this.quantity = quantity;
  }


}
