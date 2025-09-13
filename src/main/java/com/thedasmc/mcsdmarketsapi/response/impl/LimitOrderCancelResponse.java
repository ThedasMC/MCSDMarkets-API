package com.thedasmc.mcsdmarketsapi.response.impl;

import java.math.BigDecimal;

public class LimitOrderCancelResponse {

    private Long limitOrderId;
    private String material;
    private BigDecimal refundAmount;
    private BigDecimal partialPayoutAmount;
    private Integer refundQuantity;

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

    public BigDecimal getRefundAmount() {
        return refundAmount;
    }

    public void setRefundAmount(BigDecimal refundAmount) {
        this.refundAmount = refundAmount;
    }

    public BigDecimal getPartialPayoutAmount() {
        return partialPayoutAmount;
    }

    public void setPartialPayoutAmount(BigDecimal partialPayoutAmount) {
        this.partialPayoutAmount = partialPayoutAmount;
    }

    public Integer getRefundQuantity() {
        return refundQuantity;
    }

    public void setRefundQuantity(Integer refundQuantity) {
        this.refundQuantity = refundQuantity;
    }

    @Override
    public String toString() {
        return "LimitOrderCancelResponse{" +
            "limitOrderId=" + limitOrderId +
            ", material='" + material + '\'' +
            ", refundAmount=" + refundAmount +
            ", partialPayoutAmount=" + partialPayoutAmount +
            ", refundQuantity=" + refundQuantity +
            '}';
    }
}
