package ru.mstoyan.shiko.androidlogin.Utility.Rules;

import android.content.Context;

import ru.mstoyan.shiko.androidlogin.R;

/**
 * Rule class that checks if input string has at least length of minLength.
 */

class LengthRule extends SimpleRule {
    private int mMinLength = 1;

    LengthRule(Context context, int minLength){
        super(context);
        mMinLength = minLength;
    }

    @Override
    public boolean check(String str) {
        return str.length() >= mMinLength;
    }

    @Override
    public String getErrorMessage() {
        return String.format(getContext().getString(R.string.short_value_error), mMinLength);
    }
}
