package com.ben.yjh.babycare.widget.recyclerview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.ben.yjh.babycare.R;


public class LoadingMoreFooter extends LinearLayout {

    private Context mContext;
    private LinearLayout mLoadingViewLayout;
    private LinearLayout mEndLayout;

    public LoadingMoreFooter(Context context) {
        super(context);
        this.mContext = context;
        initView(context);
    }

    public LoadingMoreFooter(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public void initView(Context context) {
        setGravity(Gravity.CENTER);
        setLayoutParams(new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.footer_layout, null);
        mLoadingViewLayout = (LinearLayout) view.findViewById(R.id.loading_view_layout);
        mEndLayout = (LinearLayout) view.findViewById(R.id.end_layout);
        addFootLoadingView(new ProgressBar(context, null, android.R.attr.progressBarStyle));

        TextView textView = new TextView(context);
        textView.setText(R.string.load_finish);
        addFootEndView(textView);

        addView(view);
    }

    public void addFootLoadingView(View view) {
        mLoadingViewLayout.removeAllViews();
        mLoadingViewLayout.addView(view);
    }

    public void addFootEndView(View view) {
        mEndLayout.removeAllViews();
        mEndLayout.addView(view);
    }

    public void setEnd() {
        setVisibility(VISIBLE);
        mLoadingViewLayout.setVisibility(GONE);
        mEndLayout.setVisibility(VISIBLE);
    }

    public void setVisible(){
        setVisibility(VISIBLE);
        mLoadingViewLayout.setVisibility(VISIBLE);
        mEndLayout.setVisibility(GONE);
    }

    public void setGone(){
        setVisibility(GONE);
    }
}
