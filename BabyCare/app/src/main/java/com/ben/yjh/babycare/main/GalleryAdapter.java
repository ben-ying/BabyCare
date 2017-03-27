package com.ben.yjh.babycare.main;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.ben.yjh.babycare.R;
import com.ben.yjh.babycare.application.MyApplication;
import com.ben.yjh.babycare.util.ImageUtils;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.ArrayList;
import java.util.List;

public class GalleryAdapter extends BaseAdapter {

    private static final int TYPE_CAMERA = 0;
    private static final int TYPE_IMAGE = 1;

    private Context mContext;
    private List<Bitmap> mBitmaps;
    private List<String> mUrls;
    private LayoutInflater mInflater;

    public GalleryAdapter(Context context, List<String> urls) {
        this.mContext = context;
        this.mUrls = urls;
//        this.mBitmaps = bitmaps;
        this.mInflater = LayoutInflater.from(context);
//        for (String url : urls) {
//            mBitmaps.add(null);
//        }
    }

    public void addItem(Bitmap bitmap, int position) {
        mBitmaps.set(position, bitmap);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mUrls.size() + 1;
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_CAMERA;
        } else {
            return TYPE_IMAGE;
        }
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;

        if (convertView == null) {
            if (getItemViewType(position) == TYPE_CAMERA) {
                convertView = mInflater.inflate(R.layout.item_camera, null);
            } else {
                convertView = mInflater.inflate(R.layout.item_gallery, null);
                viewHolder = new ViewHolder();
                viewHolder.imageView = (ImageView) convertView.findViewById(R.id.image);
                convertView.setTag(viewHolder);
            }
        }

        if (getItemViewType(position) == TYPE_IMAGE) {
            viewHolder = (ViewHolder) convertView.getTag();
            final ImageView imageView = viewHolder.imageView;
            MyApplication.displayImage(mUrls.get(position - 1), imageView,
                    ImageUtils.getThumbnailImageOptions(), false, new ImageLoadingListener() {
                        @Override
                        public void onLoadingStarted(String s, View view) {
                            imageView.setImageResource(0);
                        }

                        @Override
                        public void onLoadingFailed(String s, View view, FailReason failReason) {

                        }

                        @Override
                        public void onLoadingComplete(String s, View view, Bitmap bitmap) {

                        }

                        @Override
                        public void onLoadingCancelled(String s, View view) {

                        }
                    });
        }

        return convertView;
    }

    class ViewHolder {
        ImageView imageView;
    }
}
