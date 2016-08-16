package com.example.apple.sample_app.data.View_Data;

import android.graphics.drawable.Drawable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 2016. 7. 30..
 */
public class Friend {
    public String friend_name;
    public String email_address;
    public String home_phone;
    public String mobile_phone;
    public Drawable friend_image;
    public boolean is_follow_check;

    //해쉬맵 구조로 배열선언.//
    public List<Friend> friend_list = new ArrayList<>();

    public String get_friend_name() {
        return this.friend_name;
    }

    public String get_email_address() {
        return this.email_address;
    }

    public String get_home_phone() {
        return this.home_phone;
    }

    public String get_mobild_phone() {
        return this.mobile_phone;
    }

    public Drawable get_people_image() {
        return this.friend_image;
    }

    public boolean get_follow_check() {
        return this.is_follow_check;
    }
}
