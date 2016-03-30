package com.example.arwinkish.cameramodule;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class LoginActivity extends AppCompatActivity implements GooglePlusLoginUtils.GPlusLoginStatus {

    private String TAG = "LoginActivity";
    private GooglePlusLoginUtils gLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        gLogin = new GooglePlusLoginUtils(this, R.id.activity_login_gplus);
        gLogin.setLoginStatus(this);


    }
    @Override
    protected void onStart() {
        super.onStart();
        gLogin.connect();
    }
    @Override
    protected void onStop() {
        super.onStop();
        gLogin.disconnect();
    }
    @Override
    protected void onActivityResult(int requestCode, int responseCode,
                                    Intent intent) {
        gLogin.onActivityResult(requestCode, responseCode, intent);
    }

    @Override
    public void OnSuccessGPlusLogin(Bundle profile) {
        Log.i(TAG,profile.getString(GooglePlusLoginUtils.NAME));
        Log.i(TAG,profile.getString(GooglePlusLoginUtils.EMAIL));
        Log.i(TAG,profile.getString(GooglePlusLoginUtils.PHOTO));
        Log.i(TAG,profile.getString(GooglePlusLoginUtils.PROFILE));
    }
}
