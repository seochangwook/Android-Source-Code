package com.example.apple.sample_app.JSON_Data.RequestSuccess;

public class ContentsRequest {
    private ContentsRequestResult[] result;
    private int code;

    public ContentsRequestResult[] getResult() {
        return this.result;
    }

    public void setResult(ContentsRequestResult[] result) {
        this.result = result;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
