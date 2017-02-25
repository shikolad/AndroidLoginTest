package ru.mstoyan.shiko.androidlogin.Utility.Input;

import android.content.Context;
import android.text.Editable;
import android.widget.Toast;

import ru.mstoyan.shiko.androidlogin.R;

/**
 * Created by shiko on 21.02.2017.
 */

public class LoginTextWatcher extends AbstractTextWatcher {
    private final static String mBlockCharacterSet = " .";

    public LoginTextWatcher(Context context){
        super(context);
    }

    @Override
    void checkData(Editable s) {
        final int start = getStart();
        final int end = getEnd();
        boolean wrongChar = false;
        for (int i = end; i >= start;i--){
            char character = s.charAt(i);
            if (Character.isSpaceChar(character) || mBlockCharacterSet.contains("" + character)) {
                s.delete(i, i + 1);
                wrongChar = true;
            }
            if (wrongChar){
                Toast.makeText(getContext(), R.string.wrong_login_input,Toast.LENGTH_LONG).show();
            }
        }
    }
}
