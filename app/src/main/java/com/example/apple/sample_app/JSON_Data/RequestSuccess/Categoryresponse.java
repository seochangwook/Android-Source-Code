package com.example.apple.sample_app.JSON_Data.RequestSuccess;

/**
 * Created by apple on 2016. 8. 7..
 */
public class Categoryresponse {
    //객체는 Request, RootCategory, Childern이 있다.//
    public TStore_Request Request;
    public TStore_RootCategory RootCategory;
    public TStore_Children Children;

    public TStore_Request get_tstore_request() {
        return this.Request;
    }

    public TStore_RootCategory get_tstore_rootcategory() {
        return this.RootCategory;
    }

    public TStore_Children get_tstore_children() {
        return this.Children;
    }

    public void set_TStore_Request(TStore_Request tstore_request) {
        this.Request = tstore_request;
    }

    public void set_TStore_RootCategory(TStore_RootCategory tStore_rootCategory) {
        this.RootCategory = tStore_rootCategory;
    }

    public void set_TStore_Children(TStore_Children tstore_children) {
        this.Children = tstore_children;
    }
}
