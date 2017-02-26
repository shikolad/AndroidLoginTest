package ru.mstoyan.shiko.androidlogin.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import ru.mstoyan.shiko.androidlogin.R;
import ru.mstoyan.shiko.androidlogin.security.AES_CBC_PKC_Encryptor;
import ru.mstoyan.shiko.androidlogin.security.AppPasswordStorage;

public class PostLoginActivity extends AppCompatActivity {

    public static final String USERNAME_KEY = "username_key";
    public static final String DATA_DELETED_FLAG = "data deleted";
    public static final String BROADCAST_ACTION = "ru.mstoyan.shiko.delete.broadcast";
    BroadcastReceiver broadcastReceiver;
    IntentFilter filter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                boolean dataDeleted = intent.getBooleanExtra(DATA_DELETED_FLAG,false);
                if (dataDeleted){
                    startLoginActivity();
                }
            }
        };
        filter = new IntentFilter(BROADCAST_ACTION);

        if (getIntent() != null){
            String username = getIntent().getStringExtra(USERNAME_KEY);
            if (username != null){
                TextView view = (TextView) findViewById(R.id.greetingsText);
                view.setText(String.format(getString(R.string.login_greetings),username));
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        registerReceiver(broadcastReceiver,filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    public void onExitClicked(View v){
        if (Build.VERSION.SDK_INT>16){
            finishAffinity();
        } else {
            System.exit(0);
        }
    }

    public void onForgetClicked(View v){
        AppPasswordStorage dataStorage = new AppPasswordStorage(this, new AES_CBC_PKC_Encryptor());
        dataStorage.removeData();
        startLoginActivity();
    }

    private void startLoginActivity(){
        Intent loginIntent = new Intent(this,LoginActivity.class);
        startActivity(loginIntent);
        finish();
    }

}
