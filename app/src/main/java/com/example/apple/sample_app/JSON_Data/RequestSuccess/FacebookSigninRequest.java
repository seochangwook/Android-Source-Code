package com.example.apple.sample_app.JSON_Data.RequestSuccess;

public class FacebookSigninRequest {
    private FacebookSigninRequestResult result;
    private int code;

    public FacebookSigninRequestResult getResult() {
        return this.result;
    }

    public void setResult(FacebookSigninRequestResult result) {
        this.result = result;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
