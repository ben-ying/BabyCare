package com.ben.yjh.babycare.main.event.video.holder;


import android.content.Context;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.ben.yjh.babycare.R;
import com.ben.yjh.babycare.main.event.EventAdapter;
import com.ben.yjh.babycare.main.event.EventViewpagerAdapter;
import com.ben.yjh.babycare.model.Event;
import com.viewpagerindicator.CirclePageIndicator;

import java.util.ArrayList;
import java.util.List;

public class ImageViewHolder extends TextViewHolder {
    ViewPager viewPager;
    CirclePageIndicator pageIndicator;
    private EventAdapter.EventRecyclerViewInterface mInterface;

    public ImageViewHolder(Context context, View itemView, EventAdapter adapter) {
        super(context, itemView, adapter);
        this.viewPager = (ViewPager) itemView.findViewById(R.id.view_pager);
        this.pageIndicator = (CirclePageIndicator) itemView.findViewById(R.id.indicator);
    }

    @Override
    public void onBind(int position, Event event) {
        super.onBind(position, event);
        List<String> images = new ArrayList<>();
        images.add(event.getImage1());
        viewPager.setAdapter(new EventViewpagerAdapter(
                context, images, position, adapter));
        if (viewPager.getAdapter().getCount() < 2) {
            pageIndicator.setVisibility(View.GONE);
        } else {
            pageIndicator.setVisibility(View.VISIBLE);
            pageIndicator.setViewPager(viewPager);
            pageIndicator.setSnap(true);
            pageIndicator.setFillColor(
                    context.getResources().getColor(R.color.colorPrimary));
            pageIndicator.setPageColor(
                    context.getResources().getColor(R.color.white));
            pageIndicator.setStrokeColor(
                    context.getResources().getColor(R.color.hint_color));
        }
    }
}

