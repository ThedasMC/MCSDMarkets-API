package com.thedasmc.mcsdmarketsapi.response.impl;

import com.thedasmc.mcsdmarketsapi.enums.TransactionType;

import java.time.Instant;
import java.util.UUID;

public class CreateTransactionResponse {

    private Long transactionId;
    private UUID playerId;
    private TransactionType transactionType;
    private String material;
    private Integer quantity;
    private Instant executed;

    public Long getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(Long transactionId) {
        this.transactionId = transactionId;
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

    public Instant getExecuted() {
        return executed;
    }

    public void setExecuted(Instant executed) {
        this.executed = executed;
    }

    @Override
    public String toString() {
        return "CreateTransactionResponse{" +
            "transactionId=" + transactionId +
            ", playerId=" + playerId +
            ", transactionType=" + transactionType +
            ", material='" + material + '\'' +
            ", quantity=" + quantity +
            ", executed=" + executed +
            '}';
    }
}
