package com.thedasmc.mcsdmarketsapi.response.impl;

import com.thedasmc.mcsdmarketsapi.enums.TransactionType;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

public class LimitOrderResponse {

    private Long limitOrderId;
    private String material;
    private UUID playerId;
    private Instant submitted;
    private TransactionType transactionType;
    private BigDecimal limitPrice;
    private Integer quantity;
    private Integer quantityFulfilled;
    private BigDecimal fulfilledAmount;
    private BigDecimal fulfillmentPaid;

    public Long getLimitOrderId() {
        return limitOrderId;
    }

    public void setLimitOrderId(Long limitOrderId) {
        this.limitOrderId = limitOrderId;
    }

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

    public Instant getSubmitted() {
        return submitted;
    }

    public void setSubmitted(Instant submitted) {
        this.submitted = submitted;
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

    public Integer getQuantityFulfilled() {
        return quantityFulfilled;
    }

    public void setQuantityFulfilled(Integer quantityFulfilled) {
        this.quantityFulfilled = quantityFulfilled;
    }

    public BigDecimal getFulfilledAmount() {
        return fulfilledAmount;
    }

    public void setFulfilledAmount(BigDecimal fulfilledAmount) {
        this.fulfilledAmount = fulfilledAmount;
    }

    public BigDecimal getFulfillmentPaid() {
        return fulfillmentPaid;
    }

    public void setFulfillmentPaid(BigDecimal fulfillmentPaid) {
        this.fulfillmentPaid = fulfillmentPaid;
    }

    @Override
    public String toString() {
        return "LimitOrderResponse{" +
                "limitOrderId=" + limitOrderId +
                ", material='" + material + '\'' +
                ", playerId=" + playerId +
                ", submitted=" + submitted +
                ", transactionType=" + transactionType +
                ", limitPrice=" + limitPrice +
                ", quantity=" + quantity +
                ", quantityFulfilled=" + quantityFulfilled +
                ", fulfilledAmount=" + fulfilledAmount +
                ", fulfillmentPaid=" + fulfillmentPaid +
                '}';
    }
}
