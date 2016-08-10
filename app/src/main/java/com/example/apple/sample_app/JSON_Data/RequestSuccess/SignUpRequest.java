package com.example.apple.sample_app.JSON_Data.RequestSuccess;

public class SignUpRequest {
    private SignUpRequestResult result;
    private int code;

    public SignUpRequestResult getResult() {
        return this.result;
    }

    public void setResult(SignUpRequestResult result) {
        this.result = result;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
