package com.thedasmc.mcsdmarketsapi.request;

import com.thedasmc.mcsdmarketsapi.enums.TransactionType;

import java.util.UUID;

public class CreateTransactionRequest {

    private UUID playerId;
    private TransactionType transactionType;
    private String material;
    private Integer quantity;

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

    public String getMaterial() {
        return material;
    }

    public void setMaterial(String material) {
        this.material = material;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "CreateTransactionRequest{" +
            "playerId=" + playerId +
            ", transactionType=" + transactionType +
            ", material='" + material + '\'' +
            ", quantity=" + quantity +
            '}';
    }
}
