package com.example.apple.sample_app.JSON_Data.RequestSuccess;

public class SignInRequest {
    private SignInRequestResult result;
    private int code;

    public SignInRequestResult getResult() {
        return this.result;
    }

    public void setResult(SignInRequestResult result) {
        this.result = result;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
