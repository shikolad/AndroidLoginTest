package ru.mstoyan.shiko.androidlogin.utility.rules;

import android.content.Context;

/**
 * Rules class to set requirements for login.
 */

public class LoginRule extends CompoundRule {

    private static final int MIN_LOGIN_LENGTH = 4;

    public LoginRule(Context context){
        super(context);
    }

    @Override
    public SimpleRule[] generateRules() {
        return new SimpleRule[]{
                new LengthRule(getContext(),MIN_LOGIN_LENGTH),
                new ExcludeSymbolsRule(getContext())
        };
    }

}
