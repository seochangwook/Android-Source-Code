package com.example.apple.sample_app.data;

import java.io.Serializable;

/**
 * Created by apple on 2016. 8. 12..
 */
//Intent로 객체를 전달하기 위해서 직렬화 적용. 특수한 경우에는 Parcerable//
public class User implements Serializable {
    private long id;
    private String userName;
    private String email;

    public long getId() {
        return this.id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
