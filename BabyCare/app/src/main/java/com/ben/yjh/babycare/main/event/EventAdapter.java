package com.ben.yjh.babycare.main.event;


import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;

import com.ben.yjh.babycare.R;
import com.ben.yjh.babycare.application.MyApplication;
import com.ben.yjh.babycare.http.EventTaskHandler;
import com.ben.yjh.babycare.http.HttpResponseInterface;
import com.ben.yjh.babycare.model.User;
import com.ben.yjh.babycare.model.Event;
import com.ben.yjh.babycare.model.HttpBaseResult;
import com.ben.yjh.babycare.util.ImageUtils;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder>
        implements EventViewpagerAdapter.EventAdapterInterface,
        CompoundButton.OnCheckedChangeListener, View.OnClickListener {

    private Context mContext;
    private User mUser;
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

    EventAdapter(Context context, User user, List<Event> events,
                 EventRecyclerViewInterface recyclerViewInterface) {
        this.mContext = context;
        this.mUser = user;
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

        holder.likeCheckBox.setOnCheckedChangeListener(this);
        holder.likeCheckBox.setTag(event);
        holder.commentCheckBox.setOnClickListener(this);
        holder.shareCheckBox.setOnClickListener(this);

        if (event.getImage1().isEmpty()) {
            holder.viewPager.setVisibility(View.GONE);
            holder.pageIndicator.setVisibility(View.GONE);
        } else {
            holder.viewPager.setVisibility(View.VISIBLE);
            holder.pageIndicator.setVisibility(View.GONE);
            List<String> images = new ArrayList<>();
            images.add(event.getImage1());
            holder.viewPager.setAdapter(new EventViewpagerAdapter(mContext, images, this));
            holder.pageIndicator.setViewPager(holder.viewPager);
            holder.pageIndicator.setSnap(true);
            holder.pageIndicator.setFillColor(mContext.getResources().getColor(R.color.colorPrimary));
            holder.pageIndicator.setPageColor(mContext.getResources().getColor(R.color.white));
            holder.pageIndicator.setStrokeColor(mContext.getResources().getColor(R.color.hint_color));
        }

        if (event.getTitle().isEmpty()) {
            holder.titleTextView.setVisibility(View.GONE);
        } else {
            holder.titleTextView.setText(event.getTitle());
        }

        if (event.getContent().isEmpty()) {
            holder.contentTextView.setVisibility(View.GONE);
        } else {
            holder.contentTextView.setText(event.getContent());
        }

        List<User> users = User.find(User.class, "user_id = ?", String.valueOf(event.getUserId()));
        if (users.size() > 0) {
            MyApplication.displayTinyImage(users.get(0).getProfile(),
                    holder.profileButton, ImageUtils.getTinyProfileImageOptions());
            holder.nameTextView.setText(users.get(0).getBabyName());
        } else {
            MyApplication.displayTinyImage("drawable://" + R.drawable.ic_profile,
                    holder.profileButton, ImageUtils.getTinyProfileImageOptions());
            holder.nameTextView.setText("");
        }

        holder.dateTextView.setText(event.getCreatedDate(mContext));
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
        ImageButton profileButton;
        TextView nameTextView;
        TextView dateTextView;
        TextView titleTextView;
        TextView contentTextView;

        EventViewHolder(View itemView) {
            super(itemView);
            this.viewPager = (ViewPager) itemView.findViewById(R.id.view_pager);
            this.pageIndicator = (CirclePageIndicator) itemView.findViewById(R.id.indicator);
            this.likeCheckBox = (RadioButton) itemView.findViewById(R.id.rb_like);
            this.commentCheckBox = (RadioButton) itemView.findViewById(R.id.rb_comment);
            this.shareCheckBox = (RadioButton) itemView.findViewById(R.id.rb_share);
            this.profileButton = (ImageButton) itemView.findViewById(R.id.ib_profile);
            this.nameTextView = (TextView) itemView.findViewById(R.id.tv_name);
            this.dateTextView = (TextView) itemView.findViewById(R.id.tv_datetime);
            this.titleTextView = (TextView) itemView.findViewById(R.id.tv_title);
            this.contentTextView = (TextView) itemView.findViewById(R.id.tv_content);
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
        new EventTaskHandler(mContext, mUser.getToken()).addLike(mUser.getUserId(), event,
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
