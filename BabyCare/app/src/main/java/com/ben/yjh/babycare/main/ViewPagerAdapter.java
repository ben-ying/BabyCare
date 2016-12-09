package com.ben.yjh.babycare.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.ben.yjh.babycare.R;
import com.ben.yjh.babycare.util.RecyclingPagerAdapter;

public class ViewPagerAdapter extends RecyclingPagerAdapter {

    private Context mContext;
    private LayoutInflater mInflater;

    public ViewPagerAdapter(Context context) {
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
    }


    @Override
    public View getView(final int position, View convertView, ViewGroup container) {
        View view;
        if (convertView == null) {
            view  = mInflater.inflate(R.layout.item_card, null);
        } else {
            view = convertView;
        }
        view.setTag(position);
        ImageView imageView = (ImageView) view.findViewById(R.id.image);
        TextView textView = (TextView) view.findViewById(R.id.tv_description);
        imageView.setImageResource(R.mipmap.ic_launcher);
        textView.setText("Page " + (position + 1));

        container.addView(view);

        return view;
    }

    @Override
    public int getCount() {
        return 20;
    }
}
