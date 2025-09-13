package com.thedasmc.mcsdmarketsapi.response.impl;

public class PageResponse {

    protected Integer page;
    protected Integer pages;

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
