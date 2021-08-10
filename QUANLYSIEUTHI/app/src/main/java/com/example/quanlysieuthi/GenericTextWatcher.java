package com.example.quanlysieuthi;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

public class GenericTextWatcher implements TextWatcher {
    private final EditText[] editTexts;
    private View view;

    public GenericTextWatcher(EditText[] editTexts, View view) {
        this.editTexts = editTexts;
        this.view = view;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        String text = s.toString();

        switch (view.getId())
        {
            case  R.id.otp_1:
                if(text.length() == 1) {
                    editTexts[1].requestFocus();
                    editTexts[0].setEnabled(false);
                }
                break;
            case  R.id.otp_2:
                if(text.length() == 1) {
                    editTexts[2].requestFocus();
                    editTexts[1].setEnabled(false);
                }
                else  if(text.length() == 0)
                    editTexts[0].requestFocus();
                break;
            case  R.id.otp_3:
                if(text.length() == 1) {
                    editTexts[3].requestFocus();
                    editTexts[2].setEnabled(false);
                }
                else  if(text.length() == 0)
                    editTexts[1].requestFocus();
                break;
            case  R.id.otp_4:
                if(text.length() == 1)
                    editTexts[3].setEnabled(false);
                break;
        }
    }
}
