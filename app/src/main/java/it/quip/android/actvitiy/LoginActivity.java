package it.quip.android.actvitiy;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import it.quip.android.R;
import it.quip.android.network.FacebookClient;

public class LoginActivity extends AppCompatActivity {

    private CallbackManager mCallbackManager;
    private LoginButton mBtnLogin;

    private void setupFacebookAuth() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        mCallbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_login);
        mBtnLogin = (LoginButton) findViewById(R.id.btn_login);
        mBtnLogin.setReadPermissions("user_friends");
        mBtnLogin.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                FacebookClient.newInstance(loginResult);
                startMainActivity();
            }

            @Override
            public void onCancel() {
                Toast.makeText(LoginActivity.this, "Auth Cancelled", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(FacebookException exception) {
                Toast.makeText(LoginActivity.this, "Auth Error", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupFacebookAuth();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void startMainActivity() {
        Intent intent = new Intent(this, QuipitHomeActivity.class);
        startActivity(intent);
    }

}
