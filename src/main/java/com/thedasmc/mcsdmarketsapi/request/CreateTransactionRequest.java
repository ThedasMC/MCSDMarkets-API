package com.thedasmc.mcsdmarketsapi.request;

import java.util.UUID;

public class CreateTransactionRequest extends TransactionRequest {

    private UUID playerId;

    public UUID getPlayerId() {
        return playerId;
    }

    public void setPlayerId(UUID playerId) {
        this.playerId = playerId;
    }

    @Override
    public String toString() {
        return "CreateTransactionRequest{" +
            "playerId=" + playerId +
            ", transactionType=" + transactionType +
            ", material='" + material + '\'' +
            ", quantity=" + quantity +
            "}";
    }
}
