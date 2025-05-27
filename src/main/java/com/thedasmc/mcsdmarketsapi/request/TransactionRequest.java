package com.thedasmc.mcsdmarketsapi.request;

import com.thedasmc.mcsdmarketsapi.enums.TransactionType;

public abstract class TransactionRequest {

    protected TransactionType transactionType;
    protected String material;
    protected Integer quantity;

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
        return "TransactionRequest{" +
            "transactionType=" + transactionType +
            ", material='" + material + '\'' +
            ", quantity=" + quantity +
            '}';
    }
}
