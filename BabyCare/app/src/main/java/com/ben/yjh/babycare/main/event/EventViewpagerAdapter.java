package com.ben.yjh.babycare.main.event;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ben.yjh.babycare.R;
import com.ben.yjh.babycare.application.MyApplication;
import com.ben.yjh.babycare.util.ImageUtils;

import java.util.List;


public class EventViewpagerAdapter extends PagerAdapter {

    private Context mContext;
    private List<String> mImages;
    private EventAdapterInterface mInterface;
    private int mEventPosition;

    interface EventAdapterInterface {
        void showImageDetail(int position);
    }

    EventViewpagerAdapter(Context context, List<String> images, int position,
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
        ImageView imageView = (ImageView) view.findViewById(R.id.img_event);
        MyApplication.displayImage(mImages.get(position),
                imageView, ImageUtils.getEventImageOptions(), false);
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
