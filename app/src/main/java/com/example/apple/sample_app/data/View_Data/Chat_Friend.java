package com.example.apple.sample_app.data.View_Data;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by apple on 2016. 8. 11..
 */
public class Chat_Friend {
    public String userid;
    public String username;
    public String useremail;

    public List<Chat_Friend> chat_friend_list = new ArrayList<>();

    public String get_userid() {
        return this.userid;
    }

    public void set_userid(String user_id) {
        this.userid = user_id;
    }

    public String get_username() {
        return this.username;
    }

    public void set_username(String username) {
        this.username = username;
    }

    public String get_useremail() {
        return this.useremail;
    }

    public void set_useremail(String useremail) {
        this.useremail = useremail;
    }
}
