package com.ben.yjh.babycare.main.event;


import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;

import com.ben.yjh.babycare.R;
import com.viewpagerindicator.CirclePageIndicator;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder>
        implements EventViewpagerAdapter.EventAdapterInterface,
        CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private Context mContext;
    private EventRecyclerViewInterface mInterface;

    public void showImageDetail() {
        mInterface.showImageDetail();
    }

    public void intent2CommentList() {
        mInterface.intent2CommentList();
    }

    interface EventRecyclerViewInterface {
        void showImageDetail();
        void intent2CommentList();
    }


    public EventAdapter(Context context, EventRecyclerViewInterface recyclerViewInterface) {
        this.mContext = context;
        this.mInterface = recyclerViewInterface;
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_card, parent, false);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        viewPager.setAdapter(new EventViewpagerAdapter(mContext, this));
        CirclePageIndicator pageIndicator = (CirclePageIndicator) view.findViewById(R.id.indicator);
        pageIndicator.setViewPager(viewPager);
        pageIndicator.setSnap(true);
        pageIndicator.setFillColor(mContext.getResources().getColor(R.color.colorPrimary));
        pageIndicator.setPageColor(mContext.getResources().getColor(R.color.white));
        pageIndicator.setStrokeColor(mContext.getResources().getColor(R.color.hint_color));

        RadioButton likeCheckBox = (RadioButton) view.findViewById(R.id.rb_like);
        RadioButton commentCheckBox = (RadioButton) view.findViewById(R.id.rb_comment);
        RadioButton shareCheckBox = (RadioButton) view.findViewById(R.id.rb_share);

        likeCheckBox.setOnCheckedChangeListener(this);
        commentCheckBox.setOnClickListener(this);
        shareCheckBox.setOnClickListener(this);

        return new EventViewHolder(view);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        RadioButton radioButton = (RadioButton) buttonView;
        switch (radioButton.getId()) {
            case R.id.rb_like:
                int count = radioButton.getText() == null || radioButton.getText().toString().isEmpty()
                        ? 0 : Integer.valueOf(radioButton.getText().toString());
                if (isChecked) {
                    radioButton.setText(String.valueOf(++count));
                }
                break;
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.rb_comment:
                intent2CommentList();
                break;
            case R.id.rb_share:
                break;
        }
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 20;
    }

    public class EventViewHolder extends RecyclerView.ViewHolder {

        public EventViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Intent intent = new Intent(mContext, EventDetailActivity.class);
//                    mContext.startActivity(intent);
                }
            });
        }
    }
}
