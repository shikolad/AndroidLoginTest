package ru.mstoyan.shiko.androidlogin.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import ru.mstoyan.shiko.androidlogin.activities.PostLoginActivity;
import ru.mstoyan.shiko.androidlogin.security.AES_CBC_PKC_Encryptor;
import ru.mstoyan.shiko.androidlogin.security.AppPasswordStorage;

public class DataRemoverService extends Service {
    public DataRemoverService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AppPasswordStorage passwordStorage = new AppPasswordStorage(this, new AES_CBC_PKC_Encryptor());
        //remove data
        passwordStorage.removeData();
        //send intent to postloginactivity
        Intent intent = new Intent(PostLoginActivity.BROADCAST_ACTION);
        intent.putExtra(PostLoginActivity.DATA_DELETED_FLAG,true);
        sendBroadcast(intent);
        //stopping self
        stopSelf();
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
