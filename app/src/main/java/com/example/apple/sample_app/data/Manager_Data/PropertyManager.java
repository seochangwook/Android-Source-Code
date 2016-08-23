package com.example.apple.sample_app.data.Manager_Data;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by apple on 2016. 8. 10..
 */
public class PropertyManager {
    //저장할 키값들을 정의.//
    private static final String KEY_EMAIL = "email";
    private static final String KEY_PASSWORD = "password";
    private static final String KEY_REGISTERID = "registerid";

    /**
     * SNS로그인 연동 관련 저장소
     **/
    private static final String KEY_FACEBOOK_ID = "facebookid"; //저장은 facebookid로 해준다.//

    /** Facebook로그인 관련 정보 **/

    /**
     * GCM 관련 정보
     **/
    private static final String REG_ID = "regToken";

    //PropertyManager의 객체는 싱글톤 디자인 패턴으로 설계.//
    private static PropertyManager instance; //private로 객체를 선언.//
    //공유 프래퍼런스 생성.//
    SharedPreferences mPrefs;
    SharedPreferences.Editor mEditor;

    private PropertyManager() {
        //현재 어플리케이션의 Context의 자원을 얻어온다.//
        Context context = MyApplication.getContext(); //현재의 자원을 가진다.//

        mPrefs = PreferenceManager.getDefaultSharedPreferences(context); //디폴트로 프래퍼런스 생성.//
        mEditor = mPrefs.edit();
    }

    public static PropertyManager getInstance() {
        if (instance == null) {
            instance = new PropertyManager(); //오직 하나의 객체를 생성.//
        }

        return instance;
    }

    public String getEmail() {
        //프래퍼런스에서 값을 가져온다.(get)//
        return mPrefs.getString(KEY_EMAIL, ""); //만약에 프래퍼런스에 없을 경우 ""로 나온다.//
    }

    public void setEmail(String email) {
        mEditor.putString(KEY_EMAIL, email); //put으로 저장소에 대입.//
        mEditor.commit(); //commit()를 해주어야 한다.//
    }

    public String getPassword() {
        return mPrefs.getString(KEY_PASSWORD, "");
    }

    public void setPassword(String password) {
        mEditor.putString(KEY_PASSWORD, password);
        mEditor.commit();
    }

    public String getRegisterid() {
        return mPrefs.getString(KEY_REGISTERID, "");
    }

    public void setRegisterid(String registerid) {
        mEditor.putString(KEY_REGISTERID, registerid);
        mEditor.commit();
    }

    public String getKeyFacebookId() {
        return mPrefs.getString(KEY_FACEBOOK_ID, "");
    }

    public void setKeyFacebookId(String facebookid) {
        mEditor.putString(KEY_FACEBOOK_ID, facebookid);
        mEditor.commit();
    }

    public String getRegistrationToken() {
        return mPrefs.getString(REG_ID, "");
    }

    /**
     * GCM
     **/
    public void setRegistrationToken(String regId) {
        mEditor.putString(REG_ID, regId);
        mEditor.commit();
    }
}
