package com.ben.yjh.babycare.main.event;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ben.yjh.babycare.R;
import com.ben.yjh.babycare.base.BaseFragment;
import com.ben.yjh.babycare.http.EventTaskHandler;
import com.ben.yjh.babycare.http.HttpResponseInterface;
import com.ben.yjh.babycare.main.MainActivity;
import com.ben.yjh.babycare.main.user.ImagePagerActivity;
import com.ben.yjh.babycare.model.Event;
import com.ben.yjh.babycare.model.EventComment;
import com.ben.yjh.babycare.model.EventLike;
import com.ben.yjh.babycare.model.EventsResult;
import com.ben.yjh.babycare.model.HttpBaseResult;
import com.ben.yjh.babycare.model.User;
import com.ben.yjh.babycare.util.Constants;
import com.ben.yjh.babycare.widget.recyclerview.LoadMoreListener;
import com.ben.yjh.babycare.widget.recyclerview.LoadMoreRecyclerView;
import com.ben.yjh.babycare.widget.recyclerview.ProgressView;
import com.ben.yjh.babycare.widget.share.ShareBottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

public class EventListFragment extends BaseFragment
        implements EventAdapter.EventRecyclerViewInterface {

    private LoadMoreRecyclerView mRecyclerView;
    private EventAdapter mAdapter;
    private FloatingActionButton mFab;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private List<Event> mEvents;
    private String mNextUrl;
    private int mUserId;
    private boolean mIsHomeEvents;

    public static EventListFragment newInstance(int userId) {
        Bundle args = new Bundle();
        args.putInt(Constants.USER_ID, userId);
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
        mRecyclerView = (LoadMoreRecyclerView) view.findViewById(R.id.recyclerView);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);

        return view;
    }

    @Override
    public void init() {
        mIsHomeEvents = activity instanceof MainActivity;
        mUserId = getArguments().getInt(Constants.USER_ID, Constants.INVALID_VALUE);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setNestedScrollingEnabled(false);
        mSwipeRefreshLayout.setEnabled(mIsHomeEvents);
        mEvents = getEvents();
        mAdapter = new EventAdapter(activity, user, mEvents, mIsHomeEvents, this);
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

        getEventsTask();
    }

    private List<Event> getEvents() {
        if (mIsHomeEvents) {
            return Event.listAll(Event.class, "-event_id");
        } else {
            return Event.find(Event.class, "user_id=?",
                    new String[]{String.valueOf(mUserId)}, null, "-event_id", null);
        }
    }

    private void getEventsTask() {
        int userId = mIsHomeEvents ? Constants.INVALID_VALUE : mUserId;
        new EventTaskHandler(activity, user.getToken()).getEvents(userId,
                new HttpResponseInterface<EventsResult>() {
                    @Override
                    public void onStart() {
                        mSwipeRefreshLayout.setRefreshing(true);
                    }

                    @Override
                    public void onSuccess(EventsResult classOfT) {
                        mRecyclerView.refreshComplete();
                        mSwipeRefreshLayout.setRefreshing(false);
                        if (mIsHomeEvents) {
                            Event.deleteAll(Event.class);
                        }
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
        Event.deleteAll(Event.class,
                "event_id = ?", String.valueOf(event.getEventId()));
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
    public void intent2CommentList(int eventId) {
        Intent intent = new Intent(activity, CommentActivity.class);
        intent.putExtra(Constants.EVENT_ID, eventId);
        startActivityForResult(intent, Constants.COMMENT_REQUEST_CODE);
    }

    @Override
    public void showShareSheet(Event event) {
        BottomSheetDialogFragment bottomSheetDialogFragment =
                ShareBottomSheetDialogFragment.newInstance(event);
        bottomSheetDialogFragment.show(
                activity.getSupportFragmentManager(), activity.getString(R.string.share_to));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if (resultCode == Activity.RESULT_OK) {
//            switch (requestCode) {
//                case Constants.SHOW_EVENT_IMAGE_DETAIL_REQUEST_CODE:
//                    break;
//                case Constants.COMMENT_REQUEST_CODE:
//                    break;
//                case Constants.ADD_EVENT_REQUEST_CODE:
//                    if (data != null && data.getSerializableExtra(Constants.EVENT) != null) {
//                        mEvents.add((Event) data.getSerializableExtra(Constants.EVENT));
//                        mAdapter.setData(mEvents);
//                        mRecyclerView.scrollToPosition(0);
//                    }
//                    break;
//            }
//
//            getEventsTask();
//        }
        mEvents = getEvents();
        user = User.getUser();
        mAdapter.setData(mEvents);
    }

    @Override
    public String getTitle(Context context) {
        return context.getResources().getString(R.string.event);
    }

    @Override
    public void onClick(View v) {

    }
}
