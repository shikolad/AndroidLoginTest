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
import ru.mstoyan.shiko.androidlogin.service.DataRemoverService;

/**
 * Saves and loads encrypted info.
 */

public class AppPasswordStorage implements PasswordStorage {
    private static final String mPasswordFileName = "x995fn4mkf90";
    private static final String mKeyFileName = "fkkfiejrkewml2";
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
        FileOutputStream outputStream;

        try{
            KeysPair keys = mEncryptor.getKeys(name + " " + password);

            String encrypted = mEncryptor.encrypt(name + " " + password, keys);

            //save encrypted data
            outputStream = mContext.openFileOutput(mPasswordFileName,Context.MODE_PRIVATE);
            outputStream.write(encrypted.getBytes());
            outputStream.close();

            //save integrity key
            outputStream = mContext.openFileOutput(mKeyFileName, Context.MODE_PRIVATE);
            outputStream.write(keys.getIntegrityKey().getEncoded());

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
            KeysPair keys = mEncryptor.getKeys(name + " " + password);
            if (mEncryptor.checkIntegrity(data, keys.getIntegrityKey()))
                result = mEncryptor.decrypt(data, keys.getConfidentialityKey());
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
        //removing data
        File file = new File(mContext.getFilesDir(), mPasswordFileName);
        if (file.exists())
            file.delete();
        //alarm to delete data is useless for now
        AlarmManager alarmManager = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
        Intent canceledIntent = new Intent(mContext, DataRemoverService.class);
        PendingIntent pendingIntent = PendingIntent.getService(mContext,0,canceledIntent,PendingIntent.FLAG_CANCEL_CURRENT);
        alarmManager.cancel(pendingIntent);
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
