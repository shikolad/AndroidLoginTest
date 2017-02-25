package ru.mstoyan.shiko.androidlogin.activities;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import ru.mstoyan.shiko.androidlogin.R;

public class PostLoginActivity extends AppCompatActivity {

    public static final String USERNAME_KEY = "username_key";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    public void onExitClicked(View v){
        if (Build.VERSION.SDK_INT>16){
            finishAffinity();
        } else {
            System.exit(0);
        }
    }

    public void onForgetClicked(View v){
        Intent loginIntent = new Intent(this,LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }

}
