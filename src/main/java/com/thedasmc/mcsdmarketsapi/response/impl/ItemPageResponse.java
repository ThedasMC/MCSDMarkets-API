package com.thedasmc.mcsdmarketsapi.response.impl;

import java.util.List;

public class ItemPageResponse {

    private PageResponse pageInfo;
    private List<ItemResponse> items;

    public PageResponse getPageInfo() {
        return pageInfo;
    }

    public void setPageInfo(PageResponse pageInfo) {
        this.pageInfo = pageInfo;
    }

    public List<ItemResponse> getItems() {
        return items;
    }

    public void setItems(List<ItemResponse> itemResponses) {
        this.items = itemResponses;
    }

    @Override
    public String toString() {
        return "ItemPageResponse{" +
            "pageInfo=" + pageInfo +
            ", itemResponses=" + items +
            '}';
    }
}
