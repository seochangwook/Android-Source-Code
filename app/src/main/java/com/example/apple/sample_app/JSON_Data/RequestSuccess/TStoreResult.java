package com.example.apple.sample_app.JSON_Data.RequestSuccess;

/**
 * Created by apple on 2016. 8. 6..
 */
public class TStoreResult {
    //변수들에 작명은 데이터와 동일하게 한다.//
    //넘어올 json의 구조를 파악하고 작성.//
    public TStore tstore; //이것의 이름은 json의 데이터 이름과 동일해야 한다.//

    public TStore get_TStore() {
        return this.tstore;
    }

    public void set_TStore(TStore tstore) {
        this.tstore = tstore;
    }
}
