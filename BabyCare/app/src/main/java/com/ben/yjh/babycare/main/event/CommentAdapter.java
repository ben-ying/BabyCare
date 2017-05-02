package com.ben.yjh.babycare.main.event;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ben.yjh.babycare.R;
import com.ben.yjh.babycare.application.MyApplication;
import com.ben.yjh.babycare.main.left.UserInfoActivity;
import com.ben.yjh.babycare.main.user.UserDetailActivity;
import com.ben.yjh.babycare.model.EventComment;
import com.ben.yjh.babycare.model.User;
import com.ben.yjh.babycare.util.Constants;
import com.ben.yjh.babycare.util.ImageUtils;
import com.ben.yjh.babycare.util.Utils;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<
        CommentAdapter.CommentViewHolder> implements View.OnClickListener {

    private Context mContext;
    private List<EventComment> mComments;
    private User mUser;
    private CommentInterface mInterface;

    interface CommentInterface {
        void onItemSelected(EventComment comment);
    }

    CommentAdapter(Context context, List<EventComment> comments,
                          User user, CommentInterface commentInterface) {
        this.mContext = context;
        this.mComments = comments;
        this.mUser = user;
        this.mInterface = commentInterface;
    }

    public void setData(List<EventComment> comments) {
        this.mComments = comments;
        notifyDataSetChanged();
    }

    void addData(EventComment comment) {
        this.mComments.add(comment);
        notifyDataSetChanged();
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CommentViewHolder(
                LayoutInflater.from(mContext).inflate(R.layout.item_comment, parent, false));
    }


    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {
        final EventComment comment = mComments.get(position);
        MyApplication.getInstance(mContext).displayImage(comment.getUserProfile(),
                holder.imageView, ImageUtils.getProfileImageOptions(mContext), false);
        holder.nameTextView.setText(comment.getUsername());
        holder.dateTextView.setText(Utils.getFormatDate(mContext, comment.getDatetime()));
        if (comment.getIndirectUserName() != null) {
            holder.commentTextView.setText(String.format(mContext.getString(
                    R.string.reply_user), comment.getIndirectUserName(), comment.getText()));
        } else {
            holder.commentTextView.setText(comment.getText());
        }

        holder.imageView.setTag(comment.getCommentUserId());
        holder.imageView.setOnClickListener(this);
        holder.rootView.setTag(comment);
        holder.rootView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_profile:
                Intent intent;
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
                break;
            case R.id.content_layout:
                mInterface.onItemSelected((EventComment) v.getTag());
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mComments.size();
    }

    class CommentViewHolder extends RecyclerView.ViewHolder {
        View rootView;
        ImageView imageView;
        TextView nameTextView;
        TextView commentTextView;
        TextView dateTextView;

        CommentViewHolder(View itemView) {
            super(itemView);
            this.rootView = itemView.findViewById(R.id.content_layout);
            this.imageView = (ImageView) itemView.findViewById(R.id.img_profile);
            this.nameTextView = (TextView) itemView.findViewById(R.id.tv_name);
            this.commentTextView = (TextView) itemView.findViewById(R.id.tv_comment);
            this.dateTextView = (TextView) itemView.findViewById(R.id.tv_datetime);
        }
    }
}
