package com.example.demo.entities;


import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "STOCK")
@Data
@Builder
@JsonDeserialize(builder = Stock.StockBuilder.class)
public class Stock {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  long id;
  @OneToMany
  List<StockShoe> shoes;

  @JsonPOJOBuilder(withPrefix = "")
  public static class StockBuilder {

  }


}
