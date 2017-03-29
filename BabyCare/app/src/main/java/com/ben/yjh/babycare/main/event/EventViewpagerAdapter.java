package com.ben.yjh.babycare.main.event;

import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ben.yjh.babycare.R;


public class EventViewpagerAdapter extends PagerAdapter {

    private Context mContext;
    private EventAdapterInterface mInterface;

    interface EventAdapterInterface {
        void showImageDetail();
    }

    EventViewpagerAdapter(Context context, EventAdapterInterface adapterInterface) {
        this.mContext = context;
        this.mInterface = adapterInterface;
        BitmapFactory.Options resizeOptions = new BitmapFactory.Options();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_event_image, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.img_event);
//        TextView textView = (TextView) view.findViewById(R.id.title);
//        textView.setText("Title" + position);
        switch (position) {
//            case 0:
//                imageView.setImageResource(R.drawable.test);
//                break;
//            case 1:
//                imageView.setImageResource(R.drawable.test2);
//                break;
//            case 2:
//                imageView.setImageResource(R.drawable.test);
//                break;
        }

        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mInterface.showImageDetail();
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
        return 3;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((View) object);
    }
}
