package com.thedasmc.mcsdmarketsapi.response.impl;

import java.math.BigDecimal;
import java.util.List;

public class LimitOrderCashoutResponse {

    private List<Long> limitOrderIds;
    private BigDecimal cashoutAmount;

    public List<Long> getLimitOrderIds() {
        return limitOrderIds;
    }

    public void setLimitOrderIds(List<Long> limitOrderIds) {
        this.limitOrderIds = limitOrderIds;
    }

    public BigDecimal getCashoutAmount() {
        return cashoutAmount;
    }

    public void setCashoutAmount(BigDecimal cashoutAmount) {
        this.cashoutAmount = cashoutAmount;
    }

    @Override
    public String toString() {
        return "LimitOrderCashoutResponse{" +
                "limitOrderIds=" + limitOrderIds +
                ", cashoutAmount=" + cashoutAmount +
                '}';
    }
}
