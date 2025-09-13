package com.thedasmc.mcsdmarketsapi.request;

import java.util.UUID;

public class CancelLimitOrderRequest {

    private Long limitOrderId;
    private UUID playerId;

    public Long getLimitOrderId() {
        return limitOrderId;
    }

    public void setLimitOrderId(Long limitOrderId) {
        this.limitOrderId = limitOrderId;
    }

    public UUID getPlayerId() {
        return playerId;
    }

    public void setPlayerId(UUID playerId) {
        this.playerId = playerId;
    }

    @Override
    public String toString() {
        return "CancelLimitOrderRequest{" +
            "limitOrderId=" + limitOrderId +
            ", playerId=" + playerId +
            '}';
    }
}
