package ru.mstoyan.shiko.androidlogin.utility.rules;

import android.content.Context;

/**
 * Abstract rule, consists of couple of other rules. Checks if input string meets the conditions of
 * inner rules.
 */

abstract class CompoundRule extends SimpleRule {

    private SimpleRule[] mRules;
    private boolean[] rulePerforms;

    CompoundRule(Context context) {
        super(context);
        mRules = generateRules();
        rulePerforms = new boolean[mRules.length];
    }

    public abstract SimpleRule[] generateRules();


    @Override
    public boolean check(String str) {
        boolean result = true;
        for (int i = 0; i < mRules.length; i++){
            rulePerforms[i] = mRules[i].check(str);
            result &= rulePerforms[i];
        }
        return result;
    }

    @Override
    public String getErrorMessage() {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < mRules.length; i++){
            if (!rulePerforms[i])
                result.append(mRules[i].getErrorMessage()).append("\n");
        }
        return result.toString();
    }
}
