package com.thedasmc.mcsdmarketsapi.request;

import java.util.List;
import java.util.UUID;

public class BatchSellRequest {

    private UUID playerId;
    private List<BatchTransactionRequest> transactions;

    public UUID getPlayerId() {
        return playerId;
    }

    public void setPlayerId(UUID playerId) {
        this.playerId = playerId;
    }

    public List<BatchTransactionRequest> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<BatchTransactionRequest> transactions) {
        this.transactions = transactions;
    }

    @Override
    public String toString() {
        return "BatchSellRequest{" +
            "playerId=" + playerId +
            ", transactions=" + transactions +
            '}';
    }
}
