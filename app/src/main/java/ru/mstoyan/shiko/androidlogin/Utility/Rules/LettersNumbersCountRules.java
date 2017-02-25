package ru.mstoyan.shiko.androidlogin.Utility.Rules;

import android.content.Context;

import ru.mstoyan.shiko.androidlogin.R;

/**
 * Checks if input string has at least 3 letters and 3 numbers.
 */

class LettersNumbersCountRules extends SimpleRule {
    private static final int MIN_LETTERS = 3;
    private static final int MIN_NUMBERS = 3;
    LettersNumbersCountRules(Context context) {
        super(context);
    }

    @Override
    public boolean check(String str) {
        int lettersCount = 0;
        int numbersCount = 0;

        final int length = str.length();

        for (int i = 0; i < length; i++){
            char currChar = str.charAt(i);
            if (Character.isDigit(currChar))
                numbersCount++;
            else if (Character.isLetter(currChar))
                lettersCount++;
        }

        return lettersCount >= MIN_LETTERS && numbersCount >= MIN_NUMBERS;
    }

    @Override
    public String getErrorMessage() {
        return getContext().getString(R.string.password_number_letter_count_error);
    }
}
