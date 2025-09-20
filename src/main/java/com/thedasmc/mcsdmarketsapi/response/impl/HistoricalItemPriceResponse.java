package com.thedasmc.mcsdmarketsapi.response.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class HistoricalItemPriceResponse {

    private String material;
    private LocalDateTime executed;
    private BigDecimal price;

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public LocalDateTime getExecuted() {
        return executed;
    }

    public void setExecuted(LocalDateTime executed) {
        this.executed = executed;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "HistoricalIItemPriceResponse{" +
            "material='" + material + '\'' +
            ", executed=" + executed +
            ", price=" + price +
            '}';
    }
}
