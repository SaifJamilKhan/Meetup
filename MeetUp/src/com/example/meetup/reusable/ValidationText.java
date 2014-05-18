package com.example.meetup.reusable;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.meetup.R;

public class ValidationText extends EditText {

	private TextView mValidationIcon;

	public ValidationText(Context context) {
		super(context);
		View superView = (View) this.getParent();

		mValidationIcon = (TextView) superView.findViewById(R.id.validate_icon);

	}

	public ValidationText(Context context, AttributeSet attrs) {

		super(context, attrs);
	}

	public ValidationText(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public void setValid() {
		mValidationIcon.setText(R.string.check_mark);
	}

	public void setInvalid() {
		mValidationIcon.setText(R.string.x);
	}
}
