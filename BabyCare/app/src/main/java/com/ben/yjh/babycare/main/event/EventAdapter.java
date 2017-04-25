package com.ben.yjh.babycare.main.event;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListAdapter;
import android.widget.RadioButton;
import android.widget.TextView;

import com.ben.yjh.babycare.R;
import com.ben.yjh.babycare.application.MyApplication;
import com.ben.yjh.babycare.http.EventTaskHandler;
import com.ben.yjh.babycare.http.HttpResponseInterface;
import com.ben.yjh.babycare.main.MainActivity;
import com.ben.yjh.babycare.main.left.UserInfoActivity;
import com.ben.yjh.babycare.main.user.UserDetailActivity;
import com.ben.yjh.babycare.model.EventLike;
import com.ben.yjh.babycare.model.User;
import com.ben.yjh.babycare.model.Event;
import com.ben.yjh.babycare.model.HttpBaseResult;
import com.ben.yjh.babycare.util.AlertUtils;
import com.ben.yjh.babycare.util.Constants;
import com.ben.yjh.babycare.util.ImageUtils;
import com.ben.yjh.babycare.util.Utils;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder>
        implements EventViewpagerAdapter.EventAdapterInterface, View.OnClickListener {

    private Context mContext;
    private User mUser;
    private EventRecyclerViewInterface mInterface;
    private List<Event> mEvents;
    private boolean mIsHomeEvent;

    public void showImageDetail(int position) {
        mInterface.showImageDetail(position);
    }

    public void intent2CommentList(int eventId) {
        mInterface.intent2CommentList(eventId);
    }

    public interface EventRecyclerViewInterface {
        void showImageDetail(int position);
        void intent2CommentList(int eventId);
        void showShareSheet(Event event);
    }

    public EventAdapter(Context context, User user, List<Event> events, boolean isHomeEvent,
                        EventRecyclerViewInterface recyclerViewInterface) {
        this.mContext = context;
        this.mUser = user;
        this.mEvents = events;
        this.mIsHomeEvent = isHomeEvent;
        this.mInterface = recyclerViewInterface;
    }

    public void setData(List<Event> events) {
        this.mEvents = events;
        notifyDataSetChanged();
    }

    public List<Event> getEvents() {
        return mEvents;
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new EventViewHolder(LayoutInflater.from(
                mContext).inflate(R.layout.item_card, parent, false));
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        Event event = mEvents.get(position);

        holder.commonRadioButton.setOnClickListener(this);
        holder.commonRadioButton.setTag(event);
        holder.commentRadioButton.setOnClickListener(this);
        holder.shareRadioButton.setOnClickListener(this);
        holder.shareRadioButton.setTag(event);
        holder.profileButton.setOnClickListener(this);
        holder.nameTextView.setOnClickListener(this);
        holder.profileButton.setTag(event.getUserId());
        holder.nameTextView.setTag(event.getUserId());
        holder.commentRadioButton.setTag(event.getEventId());

        if (event.getUserId() == mUser.getUserId()) {
            holder.commonRadioButton.setCompoundDrawablesWithIntrinsicBounds(
                    mContext.getResources().getDrawable(R.drawable.btn_delete), null, null, null);
            holder.commonRadioButton.setText("");
        } else {
            holder.commonRadioButton.setCompoundDrawablesWithIntrinsicBounds(
                    mContext.getResources().getDrawable(R.drawable.btn_like), null, null, null);
            setLikeCount(holder.commonRadioButton, event);
        }

        if (event.getImage1().isEmpty()) {
            holder.viewPager.setVisibility(View.GONE);
            holder.pageIndicator.setVisibility(View.GONE);
        } else {
            holder.viewPager.setVisibility(View.VISIBLE);
            holder.pageIndicator.setVisibility(View.GONE);
            List<String> images = new ArrayList<>();
            images.add(event.getImage1());
            holder.viewPager.setAdapter(new EventViewpagerAdapter(mContext, images, position, this));
            holder.pageIndicator.setViewPager(holder.viewPager);
            holder.pageIndicator.setSnap(true);
            holder.pageIndicator.setFillColor(mContext.getResources().getColor(R.color.colorPrimary));
            holder.pageIndicator.setPageColor(mContext.getResources().getColor(R.color.white));
            holder.pageIndicator.setStrokeColor(mContext.getResources().getColor(R.color.hint_color));
        }

        if (event.getTitle().isEmpty()) {
            holder.titleTextView.setVisibility(View.GONE);
        } else {
            holder.titleTextView.setVisibility(View.VISIBLE);
            holder.titleTextView.setText(event.getTitle());
        }

        if (event.getContent().isEmpty()) {
            holder.contentTextView.setVisibility(View.GONE);
        } else {
            holder.contentTextView.setVisibility(View.VISIBLE);
            holder.contentTextView.setText(event.getContent());
        }

        MyApplication.displayTinyImage(event.getUserProfile(),
                holder.profileButton, ImageUtils.getTinyProfileImageOptions(mContext));
        holder.nameTextView.setText(event.getUsername());
        holder.dateTextView.setText(Utils.getFormatDate(mContext, event.getCreated()));
    }

    @Override
    public int getItemCount() {
        return mEvents.size();
    }

    class EventViewHolder extends RecyclerView.ViewHolder {
        ViewPager viewPager;
        CirclePageIndicator pageIndicator;
        RadioButton commonRadioButton;
        RadioButton commentRadioButton;
        RadioButton shareRadioButton;
        ImageButton profileButton;
        TextView nameTextView;
        TextView dateTextView;
        TextView titleTextView;
        TextView contentTextView;

        EventViewHolder(View itemView) {
            super(itemView);
            this.viewPager = (ViewPager) itemView.findViewById(R.id.view_pager);
            this.pageIndicator = (CirclePageIndicator) itemView.findViewById(R.id.indicator);
            this.commonRadioButton = (RadioButton) itemView.findViewById(R.id.rb_common);
            this.commentRadioButton = (RadioButton) itemView.findViewById(R.id.rb_comment);
            this.shareRadioButton = (RadioButton) itemView.findViewById(R.id.rb_share);
            this.profileButton = (ImageButton) itemView.findViewById(R.id.ib_profile);
            this.nameTextView = (TextView) itemView.findViewById(R.id.tv_name);
            this.dateTextView = (TextView) itemView.findViewById(R.id.tv_datetime);
            this.titleTextView = (TextView) itemView.findViewById(R.id.tv_title);
            this.contentTextView = (TextView) itemView.findViewById(R.id.tv_content);
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        Event event;
        switch (v.getId()) {
            case R.id.rb_comment:
                intent2CommentList((int) v.getTag());
                break;
            case R.id.rb_share:
                mInterface.showShareSheet((Event) v.getTag());
//                showShareDialog((Event) v.getTag());
//                event = (Event) v.getTag();
//                int stringId = mContext.getApplicationInfo().labelRes;
//                String appName = stringId == 0 ? mContext.getApplicationInfo()
//                        .nonLocalizedLabel.toString() : mContext.getString(stringId);
//                intent = new Intent(Intent.ACTION_SEND);
//                intent.setType("text/plain");
////                if (event.getImage1().isEmpty()) {
////                    intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("android.resource://"
////                            + mContext.getPackageName() + "/mipmap/boy"));
////                } else {
////                    intent.putExtra(Intent.EXTRA_STREAM, Uri.parse(event.getImage1()));
////                }
//                intent.putExtra(Intent.EXTRA_SUBJECT, appName);
//                String text = event.getContent();
//                text += "\nhttp://www.baidu.com";
//                intent.putExtra(Intent.EXTRA_TEXT, text);
//                mContext.startActivity(Intent.createChooser(
//                        intent, mContext.getString(R.string.share_to)));
                break;
            case R.id.rb_common:
                event = (Event) v.getTag();
                if (event.getUserId() == mUser.getUserId()) {
                    showDeleteAlert(event);
                } else {
                    if (((RadioButton) v).isChecked()) {
                        likeTask(((RadioButton) v), event);
                    }
                }
                break;
            case R.id.ib_profile:
            case R.id.tv_name:
                if (mIsHomeEvent) {
                    int userId = (int) v.getTag();
                    if (userId == mUser.getUserId()) {
                        intent = new Intent(mContext, UserInfoActivity.class);
                        ((Activity) mContext).startActivityForResult(
                                intent, Constants.USER_INFO_REQUEST_CODE);
                    } else {
                        intent = new Intent(mContext, UserDetailActivity.class);
                        intent.putExtra(Constants.USER_ID, userId);
                        mContext.startActivity(intent);
                    }
                }
                break;
        }
    }

    private void showShareDialog(Event event) {
        Intent sendIntent = new Intent(android.content.Intent.ACTION_SEND);
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, event.getContent());
        sendIntent.setType("text/plain");
        List<ResolveInfo> infoList =
                mContext.getPackageManager().queryIntentActivities(sendIntent, 0);
        List<String> apps = new ArrayList<>();
        final List<Drawable> mIcons = new ArrayList<>();
        PackageManager pm = mContext.getPackageManager();

        for (ResolveInfo info : infoList) {
            String packageName = info.activityInfo.packageName.toLowerCase();
            if (packageName.contains("com.tencent.mm") || packageName.contains("com.tencent.qq")) {
                apps.add(info.activityInfo.applicationInfo
                        .loadLabel(mContext.getPackageManager()).toString());
                mIcons.add(ImageUtils.scaleImage(mContext,
                        info.activityInfo.applicationInfo.loadIcon(mContext.getPackageManager()), 0.6f));
            }
        }

        ListAdapter adapter = new ArrayAdapter<String>(
                mContext,
                android.R.layout.select_dialog_item,
                android.R.id.text1,
                apps) {
            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                TextView tv = (TextView) v.findViewById(android.R.id.text1);
                tv.setTextSize(16);
                tv.setCompoundDrawablesWithIntrinsicBounds(mIcons.get(position), null, null, null);
                int dp5 = (int) (10 * mContext.getResources().getDisplayMetrics().density + 0.5f);
                tv.setCompoundDrawablePadding(dp5);

                return v;
            }
        };

        new AlertDialog.Builder(mContext)
                .setTitle(R.string.share_to)
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                Log.d("", "");
                                break;
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .show();
    }

    private void setLikeCount(RadioButton radioButton, Event event) {
        List<EventLike> likes = EventLike.find(EventLike.class,
                "event_id = ?", String.valueOf(event.getEventId()));
        radioButton.setChecked(false);
        radioButton.setEnabled(true);
        for (EventLike like : likes) {
            if (User.find(User.class, "user_id = ?",
                    String.valueOf(like.getLikeUserId())).size() > 0) {
                radioButton.setChecked(true);
                radioButton.setEnabled(false);
                break;
            }
        }
        if (likes.size() > 0) {
            radioButton.setText(String.valueOf(likes.size()));
        } else {
            radioButton.setText("");
        }
    }

    private void showDeleteAlert(final Event event) {
        AlertUtils.showConfirmDialog(mContext, R.string.delete_event_alert,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        deleteTask(event);
                    }
                });
    }

    private void deleteTask(final Event event) {
        new EventTaskHandler(mContext, mUser.getToken()).deleteEvent(event.getEventId(),
                new HttpResponseInterface<Event>() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(Event classOfT) {
                        if (classOfT != null && event.getEventId() == classOfT.getEventId()) {
                            mEvents.remove(event);
                            notifyDataSetChanged();
                            Event.deleteAll(Event.class, "event_id = ?",
                                    String.valueOf(event.getEventId()));
                        }
                    }

                    @Override
                    public void onFailure(HttpBaseResult result) {
                    }

                    @Override
                    public void onHttpError(String error) {
                    }
                });
    }

    private void likeTask(final RadioButton radioButton, final Event event) {
        new EventTaskHandler(mContext, mUser.getToken()).addLike(event, mUser.getUserId(),
                new HttpResponseInterface<EventLike>() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(EventLike classOfT) {
                        classOfT.save();
                        setLikeCount(radioButton, event);
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
