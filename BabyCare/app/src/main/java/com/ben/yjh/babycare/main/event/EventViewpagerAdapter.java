package com.ben.yjh.babycare.main.event;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ben.yjh.babycare.R;
import com.ben.yjh.babycare.application.MyApplication;
import com.ben.yjh.babycare.util.ImageUtils;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;

import java.util.List;


public class EventViewpagerAdapter extends PagerAdapter {

    private Context mContext;
    private List<String> mImages;
    private EventAdapterInterface mInterface;
    private int mEventPosition;

    interface EventAdapterInterface {
        void showImageDetail(int position);
    }

    public EventViewpagerAdapter(Context context, List<String> images, int position,
                          EventAdapterInterface adapterInterface) {
        this.mContext = context;
        this.mImages = images;
        this.mEventPosition = position;
        this.mInterface = adapterInterface;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_event_image, null);
        final ImageView imageView = (ImageView) view.findViewById(R.id.img_event);
        MyApplication.getInstance(mContext).displayImage(mImages.get(position),
                imageView, ImageUtils.getEventImageOptions(mContext), false, new ImageLoadingListener() {
                    @Override
                    public void onLoadingStarted(String s, View view) {

                    }

                    @Override
                    public void onLoadingFailed(String s, View view, FailReason failReason) {

                    }

                    @Override
                    public void onLoadingComplete(String s, View view, Bitmap bitmap) {
                        imageView.setImageBitmap(bitmap);
                    }

                    @Override
                    public void onLoadingCancelled(String s, View view) {

                    }
                });
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInterface.showImageDetail(mEventPosition);
            }
        });

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {
        return mImages.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View) object);
    }
}
