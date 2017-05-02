package com.ben.yjh.babycare.main.left;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ben.yjh.babycare.R;
import com.ben.yjh.babycare.application.MyApplication;
import com.ben.yjh.babycare.util.ImageUtils;

import java.util.ArrayList;

public class FeedbackImageAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<String> mImageUrls;
    private int mImageSize;

    FeedbackImageAdapter(Context context, ArrayList<String> imageUrls) {
        this.mContext = context;
        this.mImageUrls = imageUrls;
        this.mInflater = LayoutInflater.from(mContext);
        this.mImageSize = context.getResources().getDisplayMetrics().widthPixels / 4;
    }

    void setImageUrls(ArrayList<String> urls) {
        this.mImageUrls = urls;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mImageUrls.size();
    }

    @Override
    public Object getItem(int position) {
        return mImageUrls.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_feedback_image, null);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.image);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.imageView.setLayoutParams(new LinearLayout.LayoutParams(mImageSize, mImageSize));
        if (mImageUrls.get(position).equals(FeedbackActivity.DEFAULT_ADD_IMAGE)) {
            viewHolder.imageView.setImageResource(R.drawable.ic_add_off);
        }
        MyApplication.getInstance(mContext).displayThumbnailImage(mImageUrls.get(position),
                viewHolder.imageView, ImageUtils.getEventImageOptions(mContext));

        return convertView;
    }

    private class ViewHolder {
        ImageView imageView;
    }
}
