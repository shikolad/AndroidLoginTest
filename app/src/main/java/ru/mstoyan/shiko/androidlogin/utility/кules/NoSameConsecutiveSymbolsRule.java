package ru.mstoyan.shiko.androidlogin.utility.кules;

import android.content.Context;

import ru.mstoyan.shiko.androidlogin.R;

/**
 * Checks if input string has no same consecutive symbols.
 */

class NoSameConsecutiveSymbolsRule extends SimpleRule {
    NoSameConsecutiveSymbolsRule(Context context) {
        super(context);
    }

    @Override
    public boolean check(String str) {
        final int length = str.length();
        for (int i = 1; i < length; i++){
            if (str.charAt(i) == str.charAt(i-1))
                return false;
        }
        return true;
    }

    @Override
    public String getErrorMessage() {
        return getContext().getString(R.string.password_consecutive_error);
    }
}
