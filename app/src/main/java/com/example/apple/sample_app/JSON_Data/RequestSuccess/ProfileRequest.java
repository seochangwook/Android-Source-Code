package com.example.apple.sample_app.JSON_Data.RequestSuccess;

public class ProfileRequest {
    private ProfileRequestResult result;
    private int code;

    public ProfileRequestResult getResult() {
        return this.result;
    }

    public void setResult(ProfileRequestResult result) {
        this.result = result;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
