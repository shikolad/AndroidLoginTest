package ru.mstoyan.shiko.androidlogin.Utility.Rules;

import android.content.Context;
import android.text.TextUtils;

import ru.mstoyan.shiko.androidlogin.R;

/**
 * Checks if there is no dots and spacebars in input string.
 */

class ExcludeSymbolsRule extends SimpleRule {
    ExcludeSymbolsRule(Context context) {
        super(context);
    }

    @Override
    public boolean check(String str) {
        return !(str.contains(" ") || str.contains("."));
    }

    @Override
    public String getErrorMessage() {
        return getContext().getString(R.string.wrong_login_input);
    }
}
