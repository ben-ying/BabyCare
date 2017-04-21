package com.ben.yjh.babycare.main.event;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;

import com.ben.yjh.babycare.R;
import com.ben.yjh.babycare.base.BaseActivity;
import com.ben.yjh.babycare.http.EventTaskHandler;
import com.ben.yjh.babycare.http.HttpResponseInterface;
import com.ben.yjh.babycare.model.CommentsResult;
import com.ben.yjh.babycare.model.EventComment;
import com.ben.yjh.babycare.model.HttpBaseResult;
import com.ben.yjh.babycare.util.Constants;
import com.ben.yjh.babycare.util.Utils;

import java.util.ArrayList;
import java.util.List;

public class CommentActivity extends BaseActivity implements CommentAdapter.CommentInterface {

    private View mRootView;
    private EditText mCommentEditText;
    private CommentAdapter mCommentAdapter;
    private List<EventComment> mComments;
    private int mEventId;
    private EventComment mReplyComment;
    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.comment);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        setStatusBarMargin(R.id.content_layout);
        mRootView = findViewById(R.id.content_layout);
        mRootView.setFitsSystemWindows(true);
        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mRecyclerView.setHasFixedSize(false);
        mRecyclerView.setNestedScrollingEnabled(false);
        mCommentEditText = (EditText) findViewById(R.id.et_comment);
        mCommentEditText.setHint(R.string.add_comment);
//        mCommentEditText.requestFocus();

        mEventId = getIntent().getIntExtra(Constants.EVENT_ID, -1);
        mComments = EventComment.find(EventComment.class, "event_id = ?", String.valueOf(mEventId));
        mCommentAdapter = new CommentAdapter(this, mComments, user, this);
        mRecyclerView.setAdapter(mCommentAdapter);

        findViewById(R.id.ib_send).setOnClickListener(this);

        getCommentsTask(mEventId);
    }

    private void getCommentsTask(final int eventId) {
        new EventTaskHandler(this, user.getToken()).getComments(eventId,
                new HttpResponseInterface<CommentsResult>() {
            @Override
            public void onStart() {

            }

            @Override
            public void onSuccess(CommentsResult classOfT) {
                EventComment.deleteAll(EventComment.class, "event_id = ?", String.valueOf(eventId));
                mComments = new ArrayList<>();
                for (EventComment eventComment : classOfT.getComments()) {
                    eventComment.save();
                    mComments.add(eventComment);
                }

                mCommentAdapter.setData(mComments);
            }

            @Override
            public void onFailure(HttpBaseResult result) {

            }

            @Override
            public void onHttpError(String error) {

            }
        });
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
                        mRecyclerView.smoothScrollToPosition(mCommentAdapter.getItemCount() - 1);
                        mCommentEditText.setText("");
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
                        mComments.remove(comment);
                        mCommentAdapter.notifyDataSetChanged();
                        if (classOfT != null) {
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
        MenuItem item = menu.findItem(R.id.title_cancel_reply);
        if (mReplyComment == null) {
            item.setVisible(false);
        } else {
            item.setVisible(true);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.title_cancel_reply) {
            mReplyComment = null;
            item.setVisible(false);
            mCommentEditText.setHint(R.string.add_comment);
            return true;
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
        }
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
                    .setNegativeButton(R.string.cancel, null)
                    .show();
        } else {
            mReplyComment = comment;
            mCommentEditText.setHint(String.format(
                    getString(R.string.reply_user), comment.getUsername(), ""));
        }
    }
}
