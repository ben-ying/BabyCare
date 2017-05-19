package com.ben.yjh.babycare.main.event.video.holder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.ben.yjh.babycare.R;
import com.ben.yjh.babycare.application.MyApplication;
import com.ben.yjh.babycare.main.event.EventAdapter;
import com.ben.yjh.babycare.model.Event;
import com.ben.yjh.babycare.util.ImageUtils;
import com.ben.yjh.babycare.util.Utils;

public class TextViewHolder extends RecyclerView.ViewHolder  {

    private RadioButton mCommonRadioButton;
    private RadioButton mCommentRadioButton;
    private RadioButton mShareRadioButton;
    private ImageButton mProfileButton;
    private TextView mNameTextView;
    private TextView mDateTextView;
    private TextView mTitleTextView;
    private TextView mContentTextView;

    public Context context;
    public EventAdapter adapter;

    public TextViewHolder(Context context, View itemView, EventAdapter adapter) {
        super(itemView);
        this.adapter = adapter;
        this.context = context;
        this.mCommonRadioButton = (RadioButton) itemView.findViewById(R.id.rb_common);
        this.mCommentRadioButton = (RadioButton) itemView.findViewById(R.id.rb_comment);
        this.mShareRadioButton = (RadioButton) itemView.findViewById(R.id.rb_share);
        this.mProfileButton = (ImageButton) itemView.findViewById(R.id.ib_profile);
        this.mNameTextView = (TextView) itemView.findViewById(R.id.tv_name);
        this.mDateTextView = (TextView) itemView.findViewById(R.id.tv_datetime);
        this.mTitleTextView = (TextView) itemView.findViewById(R.id.tv_title);
        this.mContentTextView = (TextView) itemView.findViewById(R.id.tv_content);
    }

    public void onBind(int position, Event event) {
        mCommonRadioButton.setOnClickListener(adapter);
        mCommonRadioButton.setTag(event);
        mCommentRadioButton.setOnClickListener(adapter);
        mShareRadioButton.setOnClickListener(adapter);
        mShareRadioButton.setTag(event);
        mProfileButton.setOnClickListener(adapter);
        mNameTextView.setOnClickListener(adapter);
        mProfileButton.setTag(event.getUserId());
        mNameTextView.setTag(event.getUserId());
        mCommentRadioButton.setTag(event.getEventId());
        if (event.getUserId() == adapter.getUser().getUserId()) {
            mCommonRadioButton.setCompoundDrawablesWithIntrinsicBounds(
                    context.getResources().getDrawable(R.drawable.btn_delete), null, null, null);
            mCommonRadioButton.setText(R.string.empty);
        } else {
            mCommonRadioButton.setCompoundDrawablesWithIntrinsicBounds(
                    context.getResources().getDrawable(R.drawable.btn_like), null, null, null);
            adapter.setLikeCount(mCommonRadioButton, event);
        }

        if (event.getTitle().isEmpty()) {
            mTitleTextView.setVisibility(View.GONE);
        } else {
            mTitleTextView.setVisibility(View.VISIBLE);
            mTitleTextView.setText(event.getTitle());
        }

        if (event.getContent().isEmpty()) {
            mContentTextView.setVisibility(View.GONE);
        } else {
            mContentTextView.setVisibility(View.VISIBLE);
            mContentTextView.setText(event.getContent());
        }

        MyApplication.getInstance(context).displayTinyImage(event.getUserProfile(),
                mProfileButton, ImageUtils.getTinyProfileImageOptions(context));
        mNameTextView.setText(event.getUsername());
        mDateTextView.setText(Utils.getFormatDate(context, event.getCreated()));
    }
}