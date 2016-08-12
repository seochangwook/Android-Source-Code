package com.example.apple.sample_app.JSON_Data.RequestSuccess;

public class UserListRequest {
    private UserListRequestResult[] result;
    private int code;

    public UserListRequestResult[] getResult() {
        return this.result;
    }

    public void setResult(UserListRequestResult[] result) {
        this.result = result;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
