package com.thedasmc.mcsdmarketsapi.response.impl;

import java.math.BigDecimal;
import java.util.List;

public class GetItemsResponse {

    private PageResponse pageInfo;
    private List<ItemResponse> itemResponses;

    public PageResponse getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageResponse pageInfo) {
        this.pageInfo = pageInfo;
    }

    public List<ItemResponse> getItems() {
        return itemResponses;
    }

    public void setItems(List<ItemResponse> itemResponses) {
        this.itemResponses = itemResponses;
    }

    @Override
    public String toString() {
        return "GetItemsResponse{" +
            "pageInfo=" + pageInfo +
            ", items=" + itemResponses +
            '}';
    }

    public static class ItemResponse {

        private String material;
        private BigDecimal basePrice;
        private BigDecimal currentPrice;
        private Integer inventory;
        private String versionAdded;

        public String getMaterial() {
            return material;
        }

        public void setMaterial(String material) {
            this.material = material;
        }

        public BigDecimal getBasePrice() {
            return basePrice;
        }

        public void setBasePrice(BigDecimal basePrice) {
            this.basePrice = basePrice;
        }

        public BigDecimal getCurrentPrice() {
            return currentPrice;
        }

        public void setCurrentPrice(BigDecimal currentPrice) {
            this.currentPrice = currentPrice;
        }

        public Integer getInventory() {
            return inventory;
        }

        public void setInventory(Integer inventory) {
            this.inventory = inventory;
        }

        public String getVersionAdded() {
            return versionAdded;
        }

        public void setVersionAdded(String versionAdded) {
            this.versionAdded = versionAdded;
        }

        @Override
        public String toString() {
            return "ItemResponse{" +
                "material='" + material + '\'' +
                ", basePrice=" + basePrice +
                ", currentPrice=" + currentPrice +
                ", inventory=" + inventory +
                ", versionAdded='" + versionAdded + '\'' +
                '}';
        }
    }

}
