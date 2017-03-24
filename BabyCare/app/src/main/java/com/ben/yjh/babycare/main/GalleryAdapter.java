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

import java.util.ArrayList;
import java.util.List;

public class GalleryAdapter extends BaseAdapter {

    private Context mContext;
    private List<Bitmap> mBitmaps;
    private List<String> mUrls;
    private LayoutInflater mInflater;

    public GalleryAdapter(Context context, List<Bitmap> bitmaps, List<String> urls) {
        this.mContext = context;
        this.mUrls = urls;
        this.mBitmaps = bitmaps;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;

        if (convertView == null || convertView.getTag() == null) {
            if (position == 0) {
                convertView = mInflater.inflate(R.layout.item_camera, null);
            } else {
                convertView = mInflater.inflate(R.layout.item_gallery, null);
                viewHolder = new ViewHolder();
                viewHolder.imageView = (ImageView) convertView.findViewById(R.id.image);
                convertView.setTag(viewHolder);
            }
        }

        if (position != 0) {
            viewHolder = (ViewHolder) convertView.getTag();
            viewHolder.imageView.setImageBitmap(mBitmaps.get(position - 1));
//            MyApplication.displayImage(mUrls.get(position - 1), viewHolder.imageView,
//                    ImageUtils.getThumbnailImageOptions(), false);
        }
        return convertView;
    }

    class ViewHolder {
        ImageView imageView;
    }
}
