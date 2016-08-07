package com.example.apple.sample_app.JSON_Data;

/**
 * Created by apple on 2016. 8. 7..
 */
public class TStore_CategoryResult {
    //최초 json객체는 CategoryResponse가 있다.//
    public Categoryresponse CategoryResponse; //이름은 파싱할려는 것과 동일시.//

    public Categoryresponse get_Categoryresponse() {
        return this.CategoryResponse;
    }

    public void set_Categoryresponse(Categoryresponse CategoryResponse) {
        this.CategoryResponse = CategoryResponse;
    }
}
