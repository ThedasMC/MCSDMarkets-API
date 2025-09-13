package com.thedasmc.mcsdmarketsapi.request;

import com.thedasmc.mcsdmarketsapi.enums.TransactionType;

import java.math.BigDecimal;
import java.util.UUID;

public class SubmitLimitOrderRequest {

    private String material;
    private UUID playerId;
    private TransactionType transactionType;
    private BigDecimal limitPrice;
    private Integer quantity;

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public void setPlayerId(UUID playerId) {
        this.playerId = playerId;
    }

    public TransactionType getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(TransactionType transactionType) {
        this.transactionType = transactionType;
    }

    public BigDecimal getLimitPrice() {
        return limitPrice;
    }

    public void setLimitPrice(BigDecimal limitPrice) {
        this.limitPrice = limitPrice;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "SubmitLimitOrderRequest{" +
            "material='" + material + '\'' +
            ", playerId=" + playerId +
            ", transactionType=" + transactionType +
            ", limitPrice=" + limitPrice +
            ", quantity=" + quantity +
            '}';
    }
}
