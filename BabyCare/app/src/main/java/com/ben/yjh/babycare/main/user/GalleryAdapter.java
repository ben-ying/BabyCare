package com.ben.yjh.babycare.main.user;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.ben.yjh.babycare.R;
import com.ben.yjh.babycare.application.MyApplication;
import com.ben.yjh.babycare.util.ImageUtils;

import java.util.List;

public class GalleryAdapter extends BaseAdapter {

    private static final int TYPE_CAMERA = 0;
    private static final int TYPE_IMAGE = 1;

    private Context mContext;
    private List<String> mUrls;
    private LayoutInflater mInflater;
    private GalleryInterface mInterface;

    interface GalleryInterface {
        void intent2Gallery(String url);
        void intent2Camera();
    }

    GalleryAdapter(Context context, List<String> urls, GalleryInterface galleryInterface) {
        this.mContext = context;
        this.mUrls = urls;
        this.mInterface = galleryInterface;
        this.mInflater = LayoutInflater.from(context);
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
    public View getView(final int position, View convertView, ViewGroup parent) {
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
            imageView.setImageResource(0);
            MyApplication.getInstance(mContext).displayThumbnailImage(mUrls.get(position - 1),
                    imageView, ImageUtils.getGalleryOptions());
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mInterface.intent2Gallery(mUrls.get(position - 1));
                }
            });
        } else {
            convertView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mInterface.intent2Camera();
                }
            });
        }

        return convertView;
    }

    private class ViewHolder {
        ImageView imageView;
    }
}
