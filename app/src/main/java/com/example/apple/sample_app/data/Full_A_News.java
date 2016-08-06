package com.example.apple.sample_app.data;

/**
 * Created by apple on 2016. 8. 5..
 */
public class Full_A_News {
    public String item_name; //아이템의 이름.//
    public String item_webUrl; //아이템의 웹 주소.//
    public String item_thumbnaillUrl; //아이템 이미지 주소의 썸네일.//
    public String item_tinyUrl; //아이템 간략 주소//
    public String item_description; //아이템 설명.//

    public String get_item_name() {
        return this.item_name;
    }

    public void set_item_name(String item_name) {
        this.item_name = item_name;
    }

    public String get_item_webUrl() {
        return this.item_webUrl;
    }

    public void set_item_webUrl(String item_webUrl) {
        this.item_webUrl = item_webUrl;
    }

    public String get_item_thumbnailUrl() {
        return this.item_thumbnaillUrl;
    }

    public void set_item_thumbnailUrl(String item_thumbnaillUrl) {
        this.item_thumbnaillUrl = item_thumbnaillUrl;
    }

    public String get_item_tinyUrl() {
        return this.item_tinyUrl;
    }

    public void set_item_tinyUrl(String item_tinyUrl) {
        this.item_tinyUrl = item_tinyUrl;
    }

    public String get_item_description() {
        return this.item_description;
    }

    public void set_item_description(String item_description) {
        this.item_description = item_description;
    }
}
