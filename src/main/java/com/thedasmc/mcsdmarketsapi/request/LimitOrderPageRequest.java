package com.thedasmc.mcsdmarketsapi.request;

import java.util.UUID;

public class LimitOrderPageRequest extends PageRequest {

    private UUID playerId;

    public UUID getPlayerId() {
        return playerId;
    }

    public void setPlayerId(UUID playerId) {
        this.playerId = playerId;
    }

    @Override
    public String toString() {
        return "LimitOrderPageRequest{" +
            "playerId=" + playerId +
            ", page=" + page +
            ", pageSize=" + pageSize +
            '}';
    }
}
