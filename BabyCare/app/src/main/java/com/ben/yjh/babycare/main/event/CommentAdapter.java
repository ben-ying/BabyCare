package com.ben.yjh.babycare.main.event;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ben.yjh.babycare.R;
import com.ben.yjh.babycare.application.MyApplication;
import com.ben.yjh.babycare.util.ImageUtils;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private Context mContext;

    public CommentAdapter(Context context) {
        this.mContext = context;
    }

    @Override
    public CommentViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_comment, parent, false);
        ImageView imageView = (ImageView) view.findViewById(R.id.img_profile);
        TextView nameTextView = (TextView) view.findViewById(R.id.tv_name);
        TextView commentTextView = (TextView) view.findViewById(R.id.tv_comment);
        MyApplication.displayImage("", imageView, ImageUtils.getProfileImageOptions(mContext), false);

        return new CommentViewHolder(view);
    }


    @Override
    public void onBindViewHolder(CommentViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 20;
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {
        public CommentViewHolder(View itemView) {
            super(itemView);
        }
    }
}
