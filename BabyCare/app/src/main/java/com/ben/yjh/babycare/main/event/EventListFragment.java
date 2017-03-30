package com.ben.yjh.babycare.main.event;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ben.yjh.babycare.R;
import com.ben.yjh.babycare.base.BaseFragment;
import com.ben.yjh.babycare.http.EventTaskHandler;
import com.ben.yjh.babycare.http.HttpResponseInterface;
import com.ben.yjh.babycare.main.ImagePagerActivity;
import com.ben.yjh.babycare.model.Event;
import com.ben.yjh.babycare.model.HttpBaseResult;
import com.ben.yjh.babycare.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class EventListFragment extends BaseFragment
        implements EventAdapter.EventRecyclerViewInterface {

    private RecyclerView mRecyclerView;
    private EventAdapter mAdapter;
    private FloatingActionButton mFab;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public static EventListFragment newInstance() {
        Bundle args = new Bundle();
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
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);

        return view;
    }

    @Override
    public void init() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setNestedScrollingEnabled(false);
        mAdapter = new EventAdapter(activity, activity.user, Event.find(Event.class, "user_id = ?",
                String.valueOf(activity.user.getUserId())), this);
        mRecyclerView.setAdapter(mAdapter);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.google_blue,
                R.color.google_green, R.color.google_red, R.color.google_yellow);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getEventsTask();
            }
        });
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (mFab != null) {
                    if (newState > 0) {
                        mFab.hide();
                    } else {
                        mFab.show();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
//                if(dy>0){
//                    mFab.hide();
//                }else{
//                    mFab.show();
//                }
            }
        });

        getEventsTask();
    }

    private void getEventsTask() {
        new EventTaskHandler(activity, activity.user.getToken()).getEvents(
                new HttpResponseInterface<Event[]>() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(Event[] classOfT) {
                        mSwipeRefreshLayout.setRefreshing(false);
                        Event.deleteAll(Event.class, "user_id = ?",
                                String.valueOf(activity.user.getUserId()));
                        ArrayList<Event> events = new ArrayList<>();
                        for (Event event : classOfT) {
                            event.save();
                            events.add(event);
                        }
                        mAdapter.setData(events);
                    }

                    @Override
                    public void onFailure(HttpBaseResult result) {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onHttpError(String error) {
                        mSwipeRefreshLayout.setRefreshing(false);
                    }
                });
    }

    @Override
    public void showImageDetail() {
        Intent intent = new Intent(activity, ImagePagerActivity.class);
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
                    List<Event> events = Event.find(Event.class, "user_id = ?",
                            String.valueOf(activity.user.getUserId()));
                    mAdapter.setData(events);
                    getEventsTask();
                    break;
            }
        }
    }

    @Override
    public void onClick(View v) {

    }
}
