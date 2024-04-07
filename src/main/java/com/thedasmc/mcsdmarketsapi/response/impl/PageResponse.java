package com.thedasmc.mcsdmarketsapi.response.impl;

public class PageResponse {

    private Integer page;
    private Integer pages;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    @Override
    public String toString() {
        return "PageResponse{" +
            "page=" + page +
            ", pages=" + pages +
            '}';
    }
}
