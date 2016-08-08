package com.example.apple.sample_app.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 2016. 8. 8..
 */
public class Game_News {
    public String webUrl;
    public String name;
    public String description;
    public String thumbnailUrl;
    public String tinyUrl;

    public List<Game_News> game_news_array = new ArrayList<>();

    public String get_webUrl() {
        return this.webUrl;
    }

    public void set_webUrl(String webUrl) {
        this.webUrl = webUrl;
    }

    public String get_name() {
        return this.name;
    }

    public void set_name(String name) {
        this.name = name;
    }

    public String get_description() {
        return this.description;
    }

    public void set_description(String description) {
        this.description = description;
    }

    public String get_thumbnailUrl() {
        return this.thumbnailUrl;
    }

    public void set_thumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    public String get_tinyUrl() {
        return this.tinyUrl;
    }

    public void set_tinyUrl(String tinyUrl) {
        this.tinyUrl = tinyUrl;
    }
}
