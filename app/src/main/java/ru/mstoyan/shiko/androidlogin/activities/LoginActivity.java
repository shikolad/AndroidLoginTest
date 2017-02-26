package ru.mstoyan.shiko.androidlogin.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import ru.mstoyan.shiko.androidlogin.R;
import ru.mstoyan.shiko.androidlogin.Utility.Rules.LoginRule;
import ru.mstoyan.shiko.androidlogin.Utility.Rules.PasswordRule;
import ru.mstoyan.shiko.androidlogin.security.AES_CBC_PKC_Encryptor;
import ru.mstoyan.shiko.androidlogin.security.AppPasswordStorage;
import ru.mstoyan.shiko.androidlogin.security.PasswordStorage;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity{

    // UI references.
    private EditText mLoginView;
    private EditText mPasswordView;
    private LoginRule mLoginRule;
    private PasswordRule mPasswordRule;
    private PasswordStorage mPasswordStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLoginRule = new LoginRule(this);
        mPasswordRule = new PasswordRule(this);
        mPasswordStorage = new AppPasswordStorage(this, new AES_CBC_PKC_Encryptor());

        // Set up the login form.
        mLoginView = (EditText) findViewById(R.id.login);
        mLoginView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkUsername();
            }
        });

        mPasswordView = (EditText) findViewById(R.id.password);
        mPasswordView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                checkPassword();
            }
        });
        mPasswordView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mLoginView.setText("");
        mPasswordView.setText("");
    }

    void checkUsername(){
        if (!mLoginRule.check(getLoginString()))
            mLoginView.setError(mLoginRule.getErrorMessage());
    }

    @NonNull
    private String getLoginString() {
        return mLoginView.getText().toString();
    }

    void checkPassword(){
        if (!mPasswordRule.check(getPasswordString()))
            mPasswordView.setError(mPasswordRule.getErrorMessage());
    }

    @NonNull
    private String getPasswordString() {
        return mPasswordView.getText().toString();
    }

    public void attemptLogin(){
        if (mLoginRule.check(getLoginString()) && mPasswordRule.check(getPasswordString())){
            if (mPasswordStorage.checkLoginData(getLoginString(),getPasswordString())){
                Intent postLoginIntent = new Intent(this,PostLoginActivity.class)
                        .putExtra(PostLoginActivity.USERNAME_KEY,getLoginString());
                startActivity(postLoginIntent);
            } else {
                Toast.makeText(this, R.string.login_error,Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this,R.string.error_in_login_or_password,Toast.LENGTH_LONG).show();
        }
    }

}

