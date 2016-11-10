package com.example.andrea.spotifylistener;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import java.util.ArrayList;

/**
 * Created by Andrea on 25/06/2016.
 */
public class MyTextWatcher implements TextWatcher {
    
    private Context ctx;
    private ArrayList<EditText> editTexts;
    
    public MyTextWatcher(Context ctx, ArrayList<EditText> editTexts) {
        this.ctx = ctx;
        this.editTexts = editTexts;
    }
    
    @Override
    public void afterTextChanged(Editable s) {
        Utils.saveIntermediate(ctx, editTexts);
    }
    
    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }
    
    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }
}
