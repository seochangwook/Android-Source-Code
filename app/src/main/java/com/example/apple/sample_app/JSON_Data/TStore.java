package com.example.apple.sample_app.JSON_Data;

/**
 * Created by apple on 2016. 8. 6..
 */
public class TStore {
    public int totalCount; //상품의 총 개수//
    public Products products;

    public int getTotalCount() {
        return this.totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }

    public Products getProducts() {
        return this.products;
    }

    public void setProducts(Products products) {
        this.products = products;
    }
}
