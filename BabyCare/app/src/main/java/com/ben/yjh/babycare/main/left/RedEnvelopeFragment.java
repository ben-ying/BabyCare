package com.ben.yjh.babycare.main.left;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import com.ben.yjh.babycare.R;
import com.ben.yjh.babycare.base.BaseFragment;
import com.ben.yjh.babycare.http.EventTaskHandler;
import com.ben.yjh.babycare.http.HttpResponseInterface;
import com.ben.yjh.babycare.http.NavigationTaskHandler;
import com.ben.yjh.babycare.main.event.EventAdapter;
import com.ben.yjh.babycare.model.Event;
import com.ben.yjh.babycare.model.EventsResult;
import com.ben.yjh.babycare.model.HttpBaseResult;
import com.ben.yjh.babycare.model.RedEnvelope;
import com.ben.yjh.babycare.model.RedEnvelopesResult;
import com.ben.yjh.babycare.util.Constants;
import com.ben.yjh.babycare.widget.recyclerview.LoadMoreRecyclerView;

import java.util.ArrayList;
import java.util.List;


public class RedEnvelopeFragment extends BaseFragment {

    private List<RedEnvelope> mRedEnvelopes;
    private RecyclerView mRecyclerView;
    private FloatingActionButton mFab;
    private RedEnvelopeAdapter mAdapter;
    private boolean mShowFab = true;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public static RedEnvelopeFragment newInstance() {
        Bundle args = new Bundle();
        RedEnvelopeFragment fragment = new RedEnvelopeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_red_envelopes, container, false);
        mFab = (FloatingActionButton) activity.findViewById(R.id.fab);
        mFab.setOnClickListener(this);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setEnabled(true);

        return view;
    }

    @Override
    public void init() {
        setData();
        getRedEnvelopesTask();
    }

    private void setData() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setNestedScrollingEnabled(false);
        user = activity.user;
        mRedEnvelopes = RedEnvelope.find(RedEnvelope.class,
                "user_id = ?", String.valueOf(user.getUserId()));
        mAdapter = new RedEnvelopeAdapter(activity, user, mRedEnvelopes);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE && mAdapter.getItemCount() > 0) {
                    if (mShowFab && mFab != null) {
                        mFab.show();
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (dy > 0) {
                    mShowFab = false;
                    if (mFab != null) {
                        mFab.hide();
                    }
                } else {
                    mShowFab = true;
                }
            }
        });

        mSwipeRefreshLayout.setColorSchemeResources(R.color.google_blue,
                R.color.google_green, R.color.google_red, R.color.google_yellow);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getRedEnvelopesTask();
            }
        });
    }

    private void getRedEnvelopesTask() {
        new NavigationTaskHandler(activity, user.getToken()).getRedEnvelopes(String.valueOf(
                user.getUserId()), new HttpResponseInterface<RedEnvelopesResult>() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onSuccess(RedEnvelopesResult redEnvelopesResult) {
                        mSwipeRefreshLayout.setRefreshing(false);
                        if (redEnvelopesResult != null && redEnvelopesResult.getRedEnvelopes() != null) {
                            RedEnvelope.deleteAll(RedEnvelope.class,
                                    "user_id = ?", String.valueOf(user.getUserId()));
                            mRedEnvelopes = redEnvelopesResult.getRedEnvelopes();
                            for (RedEnvelope redEnvelope : mRedEnvelopes) {
                                redEnvelope.save();
                            }
                            mAdapter.setData(mRedEnvelopes);
                        }
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

    private void addRedEnvelopeTask(String from, String money, String remark) {
        new NavigationTaskHandler(activity, user.getToken()).addRedEnvelope(from, money, remark,
                new HttpResponseInterface<RedEnvelope>() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onSuccess(RedEnvelope redEnvelope) {
                        mSwipeRefreshLayout.setRefreshing(false);
                        mRedEnvelopes.add(redEnvelope);
                        redEnvelope.save();
                        mAdapter.setData(mRedEnvelopes);
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
    public String getTitle(Context context) {
        return context.getResources().getString(R.string.red_envelopes);
    }

    private void addRedEnvelopDialog() {
        final View view = LayoutInflater.from(activity).inflate(R.layout.dialog_add_red_envelope, null);
        final EditText fromEditText = (EditText) view.findViewById(R.id.et_from);
        final EditText moneyEditText = (EditText) view.findViewById(R.id.et_money);
        final AutoCompleteTextView remarkEditText = (AutoCompleteTextView) view.findViewById(R.id.et_remark);
        final AlertDialog dialog = new AlertDialog.Builder(activity, R.style.MyDialogTheme)
                .setTitle(R.string.red_envelopes)
                .setView(view)
                .setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addRedEnvelopeTask(fromEditText.getText().toString(),
                                moneyEditText.getText().toString(),
                                remarkEditText.getText().toString());
                    }
                })
                .setNegativeButton(R.string.cancel, null)
                .create();
        dialog.setCancelable(true);
        dialog.show();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab:
                addRedEnvelopDialog();
                break;
        }
    }
}
