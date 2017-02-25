package ru.mstoyan.shiko.androidlogin.Utility.Input;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;

/**
 * Created by shiko on 22.02.2017.
 */

public class PasswordTextWatcher extends AbstractTextWatcher {

    public PasswordTextWatcher(Context context){
        super(context);
    }

    @Override
    void checkData(Editable s) {

    }
}
