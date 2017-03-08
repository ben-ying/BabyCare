package com.ben.yjh.babycare.main;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ben.yjh.babycare.R;
import com.ben.yjh.babycare.widget.ScaleImageView;

import java.util.ArrayList;
import java.util.List;


public class ImageViewpagerAdapter extends PagerAdapter {

    private Context mContext;
    private List<String> mImages;

    public ImageViewpagerAdapter(Context context, String images) {
        this.mContext = context;
        this.mImages = new ArrayList<>();
        if (images != null && images.contains(",")) {
            for (String image : images.split(",")) {
                mImages.add(image);
            }
        }
        BitmapFactory.Options resizeOptions = new BitmapFactory.Options();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_image, null);
        ScaleImageView imageView = (ScaleImageView) view.findViewById(R.id.img_event);
        switch (position) {
            case 0:
                imageView.setImageResource(R.drawable.test);
                break;
            case 1:
                imageView.setImageResource(R.drawable.test2);
                break;
            case 2:
                imageView.setImageResource(R.drawable.test);
                break;
        }

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
