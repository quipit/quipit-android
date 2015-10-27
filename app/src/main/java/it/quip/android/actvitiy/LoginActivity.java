package it.quip.android.actvitiy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import it.quip.android.R;
import it.quip.android.model.Notification;
import it.quip.android.model.User;
import it.quip.android.network.FacebookClient;

public class LoginActivity extends AppCompatActivity {

    private CallbackManager mCallbackManager;
    private LoginButton mBtnLogin;

    private void setupLoginButton() {
        mCallbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_login);
        mBtnLogin = (LoginButton) findViewById(R.id.btn_login);
        mBtnLogin.setReadPermissions("user_friends");
        mBtnLogin.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                AccessToken.setCurrentAccessToken(loginResult.getAccessToken());
                startMainActivity();
            }

            @Override
            public void onCancel() {
                Log.d("LogIn", "Auth Cancelled");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.d("LogIn", "Auth Error");
            }
        });
    }

    private void startMainActivity() {
        User.setUserForSession();
        Intent intent = new Intent(this, QuipitHomeActivity.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (FacebookClient.getInstance() == null) {
            setupLoginButton();

        } else {
            startMainActivity();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

}
