package ru.mstoyan.shiko.androidlogin.security;

import android.content.Context;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.GeneralSecurityException;
import java.text.ParseException;

/**
 * Saves and loads encrypted info.
 */

public class AppPasswordStorage extends PasswordStorage {
    private static final String mPasswordFileName = "x995fn4mkf90";
    private Context mContext;
    private Encryptor mEncryptor;

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

        }catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean decrypt(String name, String password){
        File file = new File(mContext.getFilesDir(),mPasswordFileName);
        String result = "";
        try{
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String data = reader.readLine();
            result = mEncryptor.decrypt(data, password);


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (GeneralSecurityException e) {
            e.printStackTrace();
        } catch (ParseException e) {
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

}
