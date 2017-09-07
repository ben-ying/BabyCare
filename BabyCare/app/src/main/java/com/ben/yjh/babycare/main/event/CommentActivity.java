package com.ben.yjh.babycare.main.event;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ben.yjh.babycare.R;
import com.ben.yjh.babycare.base.BaseActivity;
import com.ben.yjh.babycare.http.EventTaskHandler;
import com.ben.yjh.babycare.http.HttpResponseInterface;
import com.ben.yjh.babycare.model.CommentsResult;
import com.ben.yjh.babycare.model.EventComment;
import com.ben.yjh.babycare.model.HttpBaseResult;
import com.ben.yjh.babycare.util.Constants;
import com.ben.yjh.babycare.util.Utils;
import com.ben.yjh.babycare.widget.recyclerview.LoadMoreListener;
import com.ben.yjh.babycare.widget.recyclerview.LoadMoreRecyclerView;
import com.ben.yjh.babycare.widget.recyclerview.ProgressView;

import java.util.ArrayList;
import java.util.List;

public class CommentActivity extends BaseActivity implements CommentAdapter.CommentInterface {

    private View mRootView;
    private EditText mCommentEditText;
    private CommentAdapter mCommentAdapter;
    private List<EventComment> mComments;
    private int mEventId;
    private EventComment mReplyComment;
    private LoadMoreRecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private String mNextUrl;
    private ImageButton mSendButton;
    private boolean mIsShowKeyboard;
    private FloatingActionButton mFab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        initToolbar(R.string.comment);
        mFab = (FloatingActionButton) findViewById(R.id.fab);
        mFab.setOnClickListener(this);
        mSendButton = (ImageButton) findViewById(R.id.ib_send);
        mSendButton.setOnClickListener(this);
        mRootView = findViewById(R.id.content_layout);
//        mRootView.setFitsSystemWindows(true);
        mRecyclerView = (LoadMoreRecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setNestedScrollingEnabled(false);
        ProgressView progressView = new ProgressView(this);
        progressView.setIndicatorId(ProgressView.BallPulse);
        progressView.setIndicatorColor(0xff69b3e0);
        mRecyclerView.setFootLoadingView(progressView);

        TextView textView = new TextView(this);
        textView.setText(R.string.load_finish);
        mRecyclerView.setFootEndView(textView);

        mCommentEditText = (EditText) findViewById(R.id.et_comment);
        mCommentEditText.setHint(R.string.add_comment);
        if (mCommentEditText.getText() == null ||
                mCommentEditText.getText().toString().trim().isEmpty()) {
            mSendButton.setEnabled(false);
        }

       showKeyboard();

        mCommentEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s == null || s.toString().trim().isEmpty()) {
                    mSendButton.setEnabled(false);
                } else {
                    mSendButton.setEnabled(true);
                }
            }
        });

        mEventId = getIntent().getIntExtra(Constants.EVENT_ID, -1);
        mComments = EventComment.find(EventComment.class, "event_id = ?", String.valueOf(mEventId));
        mCommentAdapter = new CommentAdapter(this, mComments, user, this);
        mRecyclerView.setAdapter(mCommentAdapter);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe_refresh_layout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.google_blue,
                R.color.google_green, R.color.google_red, R.color.google_yellow);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getCommentsTask(mEventId);
            }
        });
        ((NestedScrollView) findViewById(R.id.scrollView))
                .setOnScrollChangeListener(new NestedScrollView.OnScrollChangeListener() {
                    @Override
                    public void onScrollChange(NestedScrollView v, int scrollX,
                                               int scrollY, int oldScrollX, int oldScrollY) {
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

        mRecyclerView.setLoadMoreListener(new LoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (mNextUrl == null) {
                    mRecyclerView.loadMoreEnd();
                } else {
                    loadMoreCommentsTask();
                }
            }
        });

        mCommentEditText.getViewTreeObserver().addOnGlobalLayoutListener(
                new ViewTreeObserver.OnGlobalLayoutListener() {
                    @Override
                    public void onGlobalLayout() {
                        Rect r = new Rect();
                        mCommentEditText.getWindowVisibleDisplayFrame(r);
                        int[] location = new int[2];
                        mCommentEditText.getLocationOnScreen(location);
                        // (root height) - statusBar - mCommentEditText.YPosition
                        // == height of mCommentEditText.YPosition from bottom
                        int navigationBarHeight;
                        int resourceId = getResources().getIdentifier(
                                "navigation_bar_height", "dimen", "android");
                        if (resourceId > 0) {
                            navigationBarHeight = getResources().getDimensionPixelSize(resourceId);
                        } else {
                            navigationBarHeight = mCommentEditText.getRootView().getHeight() / 4;
                        }
                        if (mCommentEditText.getRootView().getHeight() -
                                (r.bottom - r.top) - navigationBarHeight
                                > Utils.dpToPixels(CommentActivity.this, 50)) {
                            Log.d("", "");
                            if (!mIsShowKeyboard) {
                                mIsShowKeyboard = true;
                                mFab.setVisibility(View.GONE);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        findViewById(R.id.rl_comment).setVisibility(View.VISIBLE);
                                    }
                                }, 10);
                                mCommentEditText.requestFocus();
                                invalidateOptionsMenu();
                            }
                        } else {
                            if (mIsShowKeyboard) {
                                mIsShowKeyboard = false;
                                mFab.show();
                                findViewById(R.id.rl_comment).setVisibility(View.GONE);
                                invalidateOptionsMenu();
                            }
                            Log.d("", "");
                        }
                    }
                });

        getCommentsTask(mEventId);
    }

    private void loadMoreCommentsTask() {
        new EventTaskHandler(this, user.getToken()).loadMoreComments(mNextUrl,
                new HttpResponseInterface<CommentsResult>() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(CommentsResult classOfT) {
                        mNextUrl = classOfT.getNext();
                        for (EventComment comment : classOfT.getComments()) {
                            saveCommentData(comment);
                        }
                        mCommentAdapter.setData(mComments);
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

    private void getCommentsTask(final int eventId) {
        new EventTaskHandler(this, user.getToken()).getComments(eventId,
                new HttpResponseInterface<CommentsResult>() {
            @Override
            public void onStart() {
                mSwipeRefreshLayout.setRefreshing(true);
            }

            @Override
            public void onSuccess(CommentsResult classOfT) {
                mNextUrl = classOfT.getNext();
                mSwipeRefreshLayout.setRefreshing(false);
                EventComment.deleteAll(EventComment.class, "event_id = ?", String.valueOf(eventId));
                mComments = new ArrayList<>();
                for (EventComment eventComment : classOfT.getComments()) {
                    saveCommentData(eventComment);
                }

                mCommentAdapter.setData(mComments);
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

    private void saveCommentData(EventComment comment) {
        EventComment.deleteAll(EventComment.class,
                "comment_id = ?", String.valueOf(comment.getCommentId()));
        comment.save();
        mComments.add(comment);
    }

    private void addCommentTask(String text) {
        new EventTaskHandler(this, user.getToken()).addComment(text, mEventId, user.getUserId(),
                mReplyComment == null ? -1 : mReplyComment.getCommentId(),
                new HttpResponseInterface<EventComment>() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(EventComment classOfT) {
                        classOfT.save();
                        mCommentAdapter.addData(classOfT);
                        mCommentEditText.setText(R.string.empty);
                        Utils.hideSoftKeyboard(CommentActivity.this);
                    }

                    @Override
                    public void onFailure(HttpBaseResult result) {

                    }

                    @Override
                    public void onHttpError(String error) {

                    }
                });
    }

    private void deleteCommentTask(final EventComment comment) {
        new EventTaskHandler(this, user.getToken()).deleteComment(comment.getCommentId(),
                new HttpResponseInterface<EventComment>() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(EventComment classOfT) {
                        if (classOfT != null && classOfT.getCommentId() == comment.getCommentId()) {
                            mComments.remove(comment);
                            mCommentAdapter.notifyDataSetChanged();
                            EventComment.deleteAll(EventComment.class, "comment_id = ?",
                                    String.valueOf(comment.getCommentId()));
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_comment, menu);
        MenuItem itemReply = menu.findItem(R.id.title_cancel_reply);
        MenuItem itemComment = menu.findItem(R.id.title_add_comment);
        if (mReplyComment == null) {
            itemReply.setVisible(false);
        } else {
            itemReply.setVisible(true);
        }
        if (mIsShowKeyboard) {
            itemComment.setVisible(false);
        } else {
            if (findViewById(R.id.rl_comment).getVisibility() == View.GONE) {
                itemReply.setVisible(false);
                itemComment.setVisible(true);
            }
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.title_cancel_reply:
                mReplyComment = null;
                item.setVisible(false);
                mCommentEditText.setText(R.string.empty);
                mCommentEditText.setHint(R.string.add_comment);
                return true;
            case R.id.title_add_comment:
                showKeyboard();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        getCommentsTask(mEventId);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ib_send:
                String text = mCommentEditText.getText().toString().trim();
                if (!text.isEmpty()) {
                    addCommentTask(text);
                }
                break;
            case R.id.fab:
                showKeyboard();
                break;
        }
    }

    private void showKeyboard() {
        mCommentEditText.requestFocus();
        Utils.showKeyboard(this, mCommentEditText);
    }

    @Override
    public void onItemSelected(final EventComment comment) {
        if (mReplyComment == null) {
            invalidateOptionsMenu();
        }
        if (comment.getCommentUserId() == user.getUserId()) {
            String[] array = getResources().getStringArray(R.array.self_comment_choices);
            new AlertDialog.Builder(this)
                    .setItems(array, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            switch (which) {
                                case 0:
                                    deleteCommentTask(comment);
                                    break;
                                default:
                                    break;
                            }
                        }
                    })
//                    .setNegativeButton(R.string.cancel, null)
                    .show();
        } else {
            mReplyComment = comment;
            mCommentEditText.setHint(String.format(
                    getString(R.string.reply_user), comment.getUsername(), ""));
            mCommentEditText.setText(R.string.empty);
            showKeyboard();
        }
    }
}
