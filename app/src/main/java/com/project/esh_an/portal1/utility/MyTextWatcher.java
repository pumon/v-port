package com.project.esh_an.portal1.utility;

import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;

public class MyTextWatcher implements TextWatcher {

    TextInputLayout mTextInputLayout;

    public MyTextWatcher(TextInputLayout textInputLayout){
        mTextInputLayout = textInputLayout;
    }
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        mTextInputLayout.setError(null);
    }

    @Override
    public void afterTextChanged(Editable s) {

    }
}
