package ru.mstoyan.shiko.androidlogin.Utility.Rules;

import android.content.Context;

/**
 * Rules class to set requirements for password.
 */

public class PasswordRule extends CompoundRule {
    private static final int MIN_PASSWORD_LENGTH = 6;

    public PasswordRule(Context context) {
        super(context);
    }

    @Override
    public SimpleRule[] generateRules() {
        return new SimpleRule[]{
                new LengthRule(getContext(),MIN_PASSWORD_LENGTH),
                new NoSameConsecutiveSymbolsRule(getContext()),
                new LettersNumbersCountRules(getContext())
        };
    }
}
