package com.example.demo.entities;


import com.example.demo.validator.MaxStockSize;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "STOCK")
@Data
public class Stock {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  long id;
  @OneToMany(mappedBy = "stock")
  @MaxStockSize(value = 30, message = "Shoes stock must not exceed 30")
  List<StockShoe> shoes;


}
