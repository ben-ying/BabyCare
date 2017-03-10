package com.ben.yjh.babycare.main.event;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.ben.yjh.babycare.R;
import com.ben.yjh.babycare.base.BaseActivity;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class AddEventActivity extends BaseActivity {

    private static final int DESCRIPTION_MAX_LENGTH = 10;
    private static final int COUNT_TEXT_ANIMATION_MILLISECONDS = 300;

    private TextView mCountTextView;
    private TextView mDateTextView;
    private EditText mTitleEditText;
    private EditText mDescriptionEditText;
    private String mTitle;
    private String mDescription;
    private Calendar mCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

//        setTitle(R.string.add_event);
        mCountTextView = (TextView) findViewById(R.id.tv_count);
        mDateTextView = (TextView) findViewById(R.id.tv_date);
        mTitleEditText = (EditText) findViewById(R.id.et_title);
        mDescriptionEditText = (EditText) findViewById(R.id.et_description);

        mCalendar = Calendar.getInstance();
        mDateTextView.setOnClickListener(this);
        findViewById(R.id.btn_add).setOnClickListener(this);

        mDescriptionEditText.addTextChangedListener(new CountTextWatcher());
        mDescriptionEditText.setFilters(
                new InputFilter[] {new InputFilter.LengthFilter(
                        getResources().getInteger(R.integer.description_length))});

        setDate();
    }

    private void setDate() {
        mDateTextView.setText(new SimpleDateFormat(
                "yyyy-MM-dd", Locale.getDefault()).format(mCalendar.getTime()));
    }

    class CountTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.length() == 0) {
                if (mCountTextView.getVisibility() == View.VISIBLE) {
                    mCountTextView.animate()
                            .translationY(mCountTextView.getHeight())
                            .alpha(0.0f)
                            .setDuration(COUNT_TEXT_ANIMATION_MILLISECONDS)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    mCountTextView.setVisibility(View.GONE);
                                }
                            });
                }
            } else {
                if (mCountTextView.getVisibility() == View.GONE) {
                    mCountTextView.animate()
                            .translationY(mCountTextView.getHeight())
                            .alpha(1.0f)
                            .setDuration(COUNT_TEXT_ANIMATION_MILLISECONDS)
                            .setListener(new AnimatorListenerAdapter() {
                                @Override
                                public void onAnimationEnd(Animator animation) {
                                    super.onAnimationEnd(animation);
                                    mCountTextView.setVisibility(View.VISIBLE);
                                }
                            });
                }
                mCountTextView.setText(DESCRIPTION_MAX_LENGTH -
                        s.toString().length() + "/" + DESCRIPTION_MAX_LENGTH);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_date:
                new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        mCalendar.set(Calendar.YEAR, year);
                        mCalendar.set(Calendar.MONTH, month);
                        mCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        setDate();
                    }
                }, mCalendar.get(Calendar.YEAR), mCalendar.get(Calendar.MONTH),
                        mCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.btn_add:
                // todo
                break;
        }
    }
}
