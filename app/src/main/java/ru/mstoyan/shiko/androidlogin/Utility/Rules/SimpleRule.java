package ru.mstoyan.shiko.androidlogin.Utility.Rules;

import android.content.Context;

/**
 * Abstract rule. Used to check whether input string meets requirements.
 */

abstract class SimpleRule {
    private Context mContext;

    SimpleRule(Context context){
        mContext = context;
    }

    protected Context getContext(){
        return mContext;
    }

    abstract public boolean check(String str);
    abstract public String getErrorMessage();
}
