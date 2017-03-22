package com.ben.yjh.babycare.widget;

import android.content.Context;
import android.support.v7.widget.AppCompatTextView;
import android.util.AttributeSet;
import android.view.View;


public class ExpandableTextView extends AppCompatTextView implements View.OnClickListener {

    private static final int MAX_LINES = 3;
    private int mCurrentMaxLines = Integer.MAX_VALUE;

    public ExpandableTextView(Context context) {
        super(context);
        setOnClickListener(this);
    }

    public ExpandableTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setOnClickListener(this);
    }

    public ExpandableTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setOnClickListener(this);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        post(new Runnable() {
            public void run() {
//                if (getLineCount() > MAX_LINES) {
//                    setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.show_more);
//                } else {
//                    setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, 0);
//                }

                setMaxLines(MAX_LINES);
            }
        });
    }


    @Override
    public void setMaxLines(int maxLines) {
        mCurrentMaxLines = maxLines;
        super.setMaxLines(maxLines);
    }

    public int getMyMaxLines() {
        return mCurrentMaxLines;
    }

    @Override
    public void onClick(View v) {
        if (getMyMaxLines() == Integer.MAX_VALUE) {
            setMaxLines(MAX_LINES);
        } else {
            setMaxLines(Integer.MAX_VALUE);
        }
    }

}
