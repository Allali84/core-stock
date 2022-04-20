package com.example.demo.entities;

import com.example.demo.dto.in.ShoeFilter;
import lombok.Data;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.io.Serializable;
import java.math.BigInteger;

@Embeddable
@Data
public class StockShoeId implements Serializable {

    BigInteger size;
    @Enumerated(EnumType.STRING)
    Color color;

    public StockShoeId() {}

    public StockShoeId(BigInteger size, Color color) {
        this.size = size;
        this.color = color;
    }

    public StockShoeId(BigInteger size, ShoeFilter.Color color) {
        this.size = size;
        this.color = ShoeFilter.Color.BLACK == color ? Color.BLACK : Color.BLUE;
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
