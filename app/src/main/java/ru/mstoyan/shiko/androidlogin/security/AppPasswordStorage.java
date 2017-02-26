package ru.mstoyan.shiko.androidlogin.security;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.text.ParseException;

import ru.mstoyan.shiko.androidlogin.R;
import ru.mstoyan.shiko.androidlogin.Service.DataRemoverService;

/**
 * Saves and loads encrypted info.
 */

public class AppPasswordStorage extends PasswordStorage {
    private static final String mPasswordFileName = "x995fn4mkf90";
    private Context mContext;
    private Encryptor mEncryptor;
    private final static long DELTA_TIME = 5 * 60 * 1000;

    private AppPasswordStorage(){}

    public AppPasswordStorage(Context context, Encryptor encryptor){
        mContext = context;
        mEncryptor = encryptor;
    }

    @Override
    public void saveLoginData(String name, String password) {
        FileOutputStream passwordOutputStream;

        try{
            passwordOutputStream = mContext.openFileOutput(mPasswordFileName,Context.MODE_PRIVATE);
            String encrypted = mEncryptor.encrypt(name + " " + password, password);
            Log.w("encrypted",encrypted);
            passwordOutputStream.write(encrypted.getBytes());
            passwordOutputStream.close();

        }catch ( GeneralSecurityException | IOException e){
            e.printStackTrace();
            Toast.makeText(mContext, R.string.error_saving_data,Toast.LENGTH_LONG).show();
        }
    }

    private boolean decrypt(String name, String password){
        File file = new File(mContext.getFilesDir(),mPasswordFileName);
        String result = "";
        try{
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String data = reader.readLine();
            result = mEncryptor.decrypt(data, password);


        } catch (GeneralSecurityException | IOException | ParseException e) {
            e.printStackTrace();
        }
        return result.equals(name + " " + password);
    }

    @Override
    public boolean checkLoginData(String name, String password) {
        if (isLoginDataSaved()){
            return decrypt(name,password);
        }else {
            saveLoginData(name, password);
            scheduleDataDeleting();
            return true;
        }
    }

    @Override
    public boolean isLoginDataSaved() {
        File file = new File(mContext.getFilesDir(), mPasswordFileName);
        return file.exists();
    }

    @Override
    public void removeData() {
        File file = new File(mContext.getFilesDir(), mPasswordFileName);
        if (file.exists())
            file.delete();
    }

    private void scheduleDataDeleting(){
        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        Intent removeFileIntent = new Intent(mContext, DataRemoverService.class);
        PendingIntent pendingIntent = PendingIntent.getService(mContext,0,removeFileIntent,0);
        if (Build.VERSION.SDK_INT > 19){
            alarmManager.setExact(AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis() + DELTA_TIME,
                    pendingIntent);
        }else {
            alarmManager.set(AlarmManager.RTC_WAKEUP,
                    System.currentTimeMillis() + DELTA_TIME,
                    pendingIntent);
        }
    }

}
