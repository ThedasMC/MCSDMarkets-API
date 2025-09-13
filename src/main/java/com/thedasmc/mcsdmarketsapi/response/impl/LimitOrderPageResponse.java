package com.thedasmc.mcsdmarketsapi.response.impl;

import java.util.List;

public class LimitOrderPageResponse extends PageResponse {

    private List<LimitOrderResponse> limitOrders;

    public List<LimitOrderResponse> getLimitOrders() {
        return limitOrders;
    }

    public void setLimitOrders(List<LimitOrderResponse> limitOrders) {
        this.limitOrders = limitOrders;
    }

    @Override
    public String toString() {
        return "LimitOrderPageResponse{" +
            "limitOrders=" + limitOrders +
            ", page=" + page +
            ", pages=" + pages +
            '}';
    }
}
