package com.example.apple.sample_app.data.Trans_Data;

import java.io.Serializable;

/**
 * Created by apple on 2016. 8. 16..
 */
public class Image_Trans implements Serializable {
    private String download_image_url;
    private String download_image_content;

    public String get_download_image_url() {
        return this.download_image_url;
    }

    public void set_download_image_url(String download_image_url) {
        this.download_image_url = download_image_url;
    }

    public String get_download_image_content() {
        return this.download_image_content;
    }

    public void set_download_image_content(String download_image_content) {
        this.download_image_content = download_image_content;
    }
}
