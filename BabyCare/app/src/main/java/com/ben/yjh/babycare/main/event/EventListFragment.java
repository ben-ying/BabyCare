package com.ben.yjh.babycare.main.event;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ben.yjh.babycare.R;
import com.ben.yjh.babycare.base.BaseFragment;
import com.ben.yjh.babycare.http.EventTaskHandler;
import com.ben.yjh.babycare.http.HttpResponseInterface;
import com.ben.yjh.babycare.main.user.ImagePagerActivity;
import com.ben.yjh.babycare.model.Event;
import com.ben.yjh.babycare.model.EventComment;
import com.ben.yjh.babycare.model.EventLike;
import com.ben.yjh.babycare.model.EventsResult;
import com.ben.yjh.babycare.model.HttpBaseResult;
import com.ben.yjh.babycare.util.Constants;
import com.ben.yjh.babycare.widget.recyclerview.LoadMoreListener;
import com.ben.yjh.babycare.widget.recyclerview.LoadMoreRecyclerView;
import com.ben.yjh.babycare.widget.recyclerview.ProgressView;

import java.util.ArrayList;
import java.util.List;

public class EventListFragment extends BaseFragment
        implements EventAdapter.EventRecyclerViewInterface {

    private static final String IS_HOME_EVENT = "is_home_event";

    private LoadMoreRecyclerView mRecyclerView;
    private EventAdapter mAdapter;
    private FloatingActionButton mFab;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private boolean mIsHomeEvent;
    private List<Event> mEvents;
    private String mNextUrl;

    public static EventListFragment newInstance(boolean isHomeEvent) {
        Bundle args = new Bundle();
        args.putBoolean(IS_HOME_EVENT, isHomeEvent);
        EventListFragment fragment = new EventListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_event_list, container, false);
        mFab = (FloatingActionButton) activity.findViewById(R.id.fab);
        mRecyclerView = (LoadMoreRecyclerView) view.findViewById(R.id.recycler_view);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);

        return view;
    }

    @Override
    public void init() {
        mIsHomeEvent = getArguments().getBoolean(IS_HOME_EVENT, false);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setNestedScrollingEnabled(false);
        if (mIsHomeEvent) {
            getEventsTask();
            mSwipeRefreshLayout.setEnabled(true);
            mEvents = Event.listAll(Event.class);
        } else {
            user = activity.user;
            mSwipeRefreshLayout.setEnabled(false);
            mEvents = Event.find(Event.class, "user_id = ?", String.valueOf(user.getUserId()));
        }
        mAdapter = new EventAdapter(activity, user, mEvents, mIsHomeEvent, this);
        mRecyclerView.setAdapter(mAdapter);
        ProgressView progressView = new ProgressView(activity);
        progressView.setIndicatorId(ProgressView.BallPulse);
        progressView.setIndicatorColor(0xff69b3e0);
        mRecyclerView.setFootLoadingView(progressView);

        TextView textView = new TextView(activity);
        textView.setText(R.string.load_finish);
        mRecyclerView.setFootEndView(textView);

        mRecyclerView.setLoadMoreListener(new LoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (mNextUrl == null) {
                    mRecyclerView.loadMoreEnd();
                } else {
                    loadMoreEventsTask();
                }
            }
        });

        mSwipeRefreshLayout.setColorSchemeResources(R.color.google_blue,
                R.color.google_green, R.color.google_red, R.color.google_yellow);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getEventsTask();
            }
        });
        ((NestedScrollView) rootView.findViewById(R.id.scrollView))
                .setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                    @Override
                    public void onScrollChange(NestedScrollView v,
                                               int scrollX, int scrollY, int oldScrollX, int oldScrollY) {
                        if (scrollY > oldScrollY) {
                            if (mFab != null) {
                                mFab.hide();
                            }
                        }
                        if (scrollY < oldScrollY) {
                            if (mFab != null) {
                                mFab.show();
                            }
                        }

                        if (mNextUrl != null && scrollY == (
                                v.getChildAt(0).getMeasuredHeight() - v.getMeasuredHeight())) {
                            mRecyclerView.onScrollStateChanged(RecyclerView.SCROLL_STATE_IDLE);
                        }
                    }
                });
    }

    private void getEventsTask() {
        new EventTaskHandler(activity, user.getToken()).getEvents(
                new HttpResponseInterface<EventsResult>() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(EventsResult classOfT) {
                        mRecyclerView.refreshComplete();
                        mSwipeRefreshLayout.setRefreshing(false);
                        Event.deleteAll(Event.class, "user_id = ?", String.valueOf(user.getUserId()));
                        mNextUrl = classOfT.getNext();
                        mEvents = new ArrayList<>();
                        for (Event event : classOfT.getEvents()) {
                            saveEventData(event);
                        }
                        mAdapter.setData(mEvents);
                    }

                    @Override
                    public void onFailure(HttpBaseResult result) {
                        mRecyclerView.refreshComplete();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onHttpError(String error) {
                        mRecyclerView.refreshComplete();
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });
    }

    private void loadMoreEventsTask() {
        new EventTaskHandler(activity, user.getToken()).loadMoreEvents(mNextUrl,
                new HttpResponseInterface<EventsResult>() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(EventsResult classOfT) {
                        mNextUrl = classOfT.getNext();
                        for (Event event : classOfT.getEvents()) {
                            Event.deleteAll(Event.class,
                                    "event_id = ?", String.valueOf(event.getEventId()));
                            saveEventData(event);
                        }
                        mAdapter.setData(mEvents);
                        mRecyclerView.loadMoreComplete();
                    }

                    @Override
                    public void onFailure(HttpBaseResult result) {
                        mRecyclerView.loadMoreComplete();
                    }

                    @Override
                    public void onHttpError(String error) {
                        mRecyclerView.loadMoreComplete();
                    }
                });
    }

    private void saveEventData(Event event) {
        event.save();
        EventLike.deleteAll(EventLike.class,
                "event_id = ?", String.valueOf(event.getEventId()));
        EventComment.deleteAll(EventComment.class, "event_id = ?",
                String.valueOf(event.getEventId()));
        for (EventLike like : event.getEventLikes()) {
            like.save();
        }
        for (EventComment comment : event.getEventComments()) {
            comment.save();
        }
        mEvents.add(event);
    }

    @Override
    public void showImageDetail(int position) {
        List<Event> events = mAdapter.getEvents();
        Intent intent = new Intent(activity, ImagePagerActivity.class);
        intent.putExtra(Constants.IMAGE_URL, events.get(position).getImage1());
        startActivityForResult(intent, Constants.SHOW_EVENT_IMAGE_DETAIL_REQUEST_CODE);
    }

    @Override
    public void intent2CommentList() {
        Intent intent = new Intent(activity, CommentActivity.class);
        startActivityForResult(intent, Constants.COMMENT_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case Constants.SHOW_EVENT_IMAGE_DETAIL_REQUEST_CODE:
                    break;
                case Constants.COMMENT_REQUEST_CODE:
                    break;
                case Constants.ADD_EVENT_REQUEST_CODE:
                    if (data != null && data.getSerializableExtra(Constants.EVENT) != null) {
                        mEvents.add((Event) data.getSerializableExtra(Constants.EVENT));
                        mAdapter.setData(mEvents);
                        mRecyclerView.scrollToPosition(0);
                    }
                    break;
            }

            getEventsTask();
        }
    }

    @Override
    public String getTitle(Context context) {
        return context.getResources().getString(R.string.event);
    }

    @Override
    public void onClick(View v) {

    }
}
