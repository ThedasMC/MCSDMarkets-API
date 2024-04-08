package com.thedasmc.mcsdmarketsapi.response.impl;

import java.math.BigDecimal;

public class ItemResponse {

    private String material;
    private BigDecimal basePrice;
    private BigDecimal currentPrice;
    private Integer inventory;
    private String versionAdded;

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public BigDecimal getBasePrice() {
        return basePrice;
    }

    public void setBasePrice(BigDecimal basePrice) {
        this.basePrice = basePrice;
    }

    public BigDecimal getCurrentPrice() {
        return currentPrice;
    }

    public void setCurrentPrice(BigDecimal currentPrice) {
        this.currentPrice = currentPrice;
    }

    public Integer getInventory() {
        return inventory;
    }

    public void setInventory(Integer inventory) {
        this.inventory = inventory;
    }

    public String getVersionAdded() {
        return versionAdded;
    }

    public void setVersionAdded(String versionAdded) {
        this.versionAdded = versionAdded;
    }

    @Override
    public String toString() {
        return "ItemResponse{" +
            "material='" + material + '\'' +
            ", basePrice=" + basePrice +
            ", currentPrice=" + currentPrice +
            ", inventory=" + inventory +
            ", versionAdded='" + versionAdded + '\'' +
            '}';
    }
}
