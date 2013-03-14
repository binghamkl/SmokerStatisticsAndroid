package com.bingham.smokerstatistics.Utils;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class NumericWatcher implements TextWatcher {

	private EditText mTextBox;
	private String mCurrentWord;
	
	public NumericWatcher(EditText textValue){
		mTextBox = textValue;
	}
	
	private boolean IsValid (CharSequence s) {
		for ( int i = 0; i < s.length(); i ++ )
		{
			if ( !Character.isDigit( s.charAt(i)))
				return false;
		}
		return true;
	}
		
	@Override
	public void afterTextChanged(Editable s) {
		
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		mCurrentWord = s.toString();		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		if (!IsValid(s)) {
			mTextBox.setText(mCurrentWord);
			mTextBox.setSelection(start);
		}
	}

}
