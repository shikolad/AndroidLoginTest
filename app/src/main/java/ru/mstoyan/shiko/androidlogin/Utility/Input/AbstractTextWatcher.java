package ru.mstoyan.shiko.androidlogin.Utility.Input;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Toast;

import ru.mstoyan.shiko.androidlogin.R;

/**
 * Created by shiko on 22.02.2017.
 */

public abstract class AbstractTextWatcher implements TextWatcher {
    private int st = 0;
    private int en = 0;
    private boolean inRecursion = false;
    private Context mContext;

    public AbstractTextWatcher(Context context){
        mContext = context;
    }

    protected int getStart(){
        return st;
    }

    protected int getEnd(){
        return en;
    }

    protected Context getContext(){
        return mContext;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        st = start;
        en = start + count - 1;
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (inRecursion)
            return;
        inRecursion = true;
        checkData(s);
        inRecursion = false;
    }

    abstract void checkData(Editable s);
}
