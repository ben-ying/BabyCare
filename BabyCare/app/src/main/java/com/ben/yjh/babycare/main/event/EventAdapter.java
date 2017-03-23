package com.ben.yjh.babycare.main.event;


import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.Toast;

import com.ben.yjh.babycare.R;
import com.ben.yjh.babycare.http.EventTaskHandler;
import com.ben.yjh.babycare.http.HttpResponseInterface;
import com.ben.yjh.babycare.login.LoginActivity;
import com.ben.yjh.babycare.http.UserTaskHandler;
import com.ben.yjh.babycare.main.MainActivity;
import com.ben.yjh.babycare.model.BabyUser;
import com.ben.yjh.babycare.model.Event;
import com.ben.yjh.babycare.model.HttpBaseResult;
import com.ben.yjh.babycare.model.UserHistory;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder>
        implements EventViewpagerAdapter.EventAdapterInterface,
        CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private Context mContext;
    private BabyUser mBabyUser;
    private EventRecyclerViewInterface mInterface;
    private List<Event> mEvents;

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

    EventAdapter(Context context, BabyUser babyUser, List<Event> events,
                 EventRecyclerViewInterface recyclerViewInterface) {
        this.mContext = context;
        this.mBabyUser = babyUser;
        this.mEvents = events;
        this.mInterface = recyclerViewInterface;
    }

    void setData(List<Event> events) {
        this.mEvents = events;
        notifyDataSetChanged();
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new EventViewHolder(LayoutInflater.from(
                mContext).inflate(R.layout.item_card, parent, false));
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        Event event = mEvents.get(position);
        holder.viewPager.setAdapter(new EventViewpagerAdapter(mContext, this));
        holder.pageIndicator.setViewPager(holder.viewPager);
        holder.pageIndicator.setSnap(true);
        holder.pageIndicator.setFillColor(mContext.getResources().getColor(R.color.colorPrimary));
        holder.pageIndicator.setPageColor(mContext.getResources().getColor(R.color.white));
        holder.pageIndicator.setStrokeColor(mContext.getResources().getColor(R.color.hint_color));
        holder.likeCheckBox.setOnCheckedChangeListener(this);
        holder.likeCheckBox.setTag(event);
        holder.commentCheckBox.setOnClickListener(this);
        holder.shareCheckBox.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return mEvents.size();
    }

    class EventViewHolder extends RecyclerView.ViewHolder {
        ViewPager viewPager;
        CirclePageIndicator pageIndicator;
        RadioButton likeCheckBox;
        RadioButton commentCheckBox;
        RadioButton shareCheckBox;

        EventViewHolder(View itemView) {
            super(itemView);
            this.viewPager = (ViewPager) itemView.findViewById(R.id.view_pager);
            this.pageIndicator = (CirclePageIndicator) itemView.findViewById(R.id.indicator);
            this.commentCheckBox = (RadioButton) itemView.findViewById(R.id.rb_like);
            this.commentCheckBox = (RadioButton) itemView.findViewById(R.id.rb_comment);
            this.shareCheckBox = (RadioButton) itemView.findViewById(R.id.rb_share);
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        RadioButton radioButton = (RadioButton) buttonView;
        switch (radioButton.getId()) {
            case R.id.rb_like:
                if (isChecked) {
                    likeTask(radioButton, (Event) buttonView.getTag());
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

    private void likeTask(final RadioButton radioButton, Event event) {
        new EventTaskHandler(mContext, mBabyUser.getToken()).addLike(mBabyUser.getUserId(), event,
                new HttpResponseInterface<HttpBaseResult>() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(HttpBaseResult classOfT) {
                        int count = radioButton.getText() == null ||
                                radioButton.getText().toString().isEmpty()
                                ? 0 : Integer.valueOf(radioButton.getText().toString());
                        radioButton.setText(String.valueOf(++count));
                    }

                    @Override
                    public void onFailure(HttpBaseResult result) {
                    }

                    @Override
                    public void onHttpError(String error) {
                    }
                });
    }


}
