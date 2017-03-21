package com.ben.yjh.babycare.main.event;


import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ben.yjh.babycare.R;
import com.viewpagerindicator.CirclePageIndicator;

public class EventAdapter extends RecyclerView.Adapter<EventAdapter.EventViewHolder>
        implements EventViewpagerAdapter.EventAdapterInterface {

    private Context mContext;
    private EventRecyclerViewInterface mInterface;

    @Override
    public void showImageDetail() {
        mInterface.showImageDetail();
    }

    interface EventRecyclerViewInterface {
        void showImageDetail();
    }


    public EventAdapter(Context context, EventRecyclerViewInterface recyclerViewInterface) {
        this.mContext = context;
        this.mInterface = recyclerViewInterface;
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_card, parent, false);
        ViewPager viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        viewPager.setAdapter(new EventViewpagerAdapter(mContext, this));
        CirclePageIndicator pageIndicator = (CirclePageIndicator) view.findViewById(R.id.indicator);
        pageIndicator.setViewPager(viewPager);
        pageIndicator.setSnap(true);
        pageIndicator.setFillColor(mContext.getResources().getColor(R.color.colorPrimary));
        pageIndicator.setPageColor(mContext.getResources().getColor(R.color.white));
        pageIndicator.setStrokeColor(mContext.getResources().getColor(R.color.hint_color));

        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 20;
    }

    public class EventViewHolder extends RecyclerView.ViewHolder {

        public EventViewHolder(View itemView) {
            super(itemView);
        }
    }
}
