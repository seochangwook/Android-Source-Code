package com.example.apple.sample_app.data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 2016. 8. 12..
 */
public class ImageData implements java.io.Serializable {
    public String image_Url;
    public String image_content;

    public List<ImageData> imagelist = new ArrayList<>();

    public String get_image_Url() {
        return this.image_Url;
    }

    public void set_image_Url(String image_url) {
        this.image_Url = image_url;
    }

    public String get_image_content() {
        return this.image_content;
    }

    public void set_image_content(String image_content) {
        this.image_content = image_content;
    }
}
