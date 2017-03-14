package com.ben.yjh.babycare.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.ben.yjh.babycare.R;

public class ItemInfo extends RelativeLayout {

    private TextView mTitleTextView;
    private TextView mValueTextView;
    private Switch mSwitch;
    private int mType;

    public ItemInfo(Context context) {
        super(context);
        init(context, null, 0, 0);
    }

    public ItemInfo(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs, 0, 0);
    }

    public ItemInfo(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr, 0);
    }

    public ItemInfo(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        if (isInEditMode()) return;

        LayoutInflater.from(context).inflate(R.layout.item_info, this);
        mTitleTextView = (TextView) findViewById(R.id.tv_title);
        mValueTextView = (TextView) findViewById(R.id.tv_value);
        mSwitch = (Switch) findViewById(R.id.switchView);

        TypedArray typedArray = context.getTheme().
                obtainStyledAttributes(attrs, R.styleable.Item, defStyleAttr, 0);
        try {
            mType = typedArray.getInt(R.styleable.Item_item_type, 0);

            switch (mType) {
                case 0:
                    mValueTextView.setVisibility(VISIBLE);
                    mSwitch.setVisibility(GONE);
                    break;
                case 1:
                    mValueTextView.setVisibility(GONE);
                    mSwitch.setVisibility(VISIBLE);
                    break;
            }
        } finally {
            typedArray.recycle();
        }
    }

    public void setValue(int titleRes, String value, int defaultRes) {
        mTitleTextView.setText(titleRes);

        switch (mType) {
            case 0:
                if (value == null || value.trim().isEmpty()) {
                    mValueTextView.setHint(defaultRes);
                } else {
                    mValueTextView.setText(value);
                }
                break;
            case 1:
                mSwitch.setChecked(value.equals("1"));
                break;
        }
    }

    public String getTitle() {
        return mTitleTextView.getText().toString();
    }

    public String getValue() {
        return mValueTextView.getText().toString();
    }

}
