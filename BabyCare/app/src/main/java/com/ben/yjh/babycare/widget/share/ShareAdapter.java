package com.ben.yjh.babycare.widget.share;


import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ResolveInfo;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ben.yjh.babycare.model.Event;

import java.util.List;

public class ShareAdapter extends RecyclerView.Adapter<ShareAdapter.ShareViewHolder> {

    private Context mContext;
    private Event mEvent;
    private List<ResolveInfo> mInfoList;

    public ShareAdapter(Context context, Event event, List<ResolveInfo> infoList) {
        this.mContext = context;
        this.mEvent = event;
        this.mInfoList = infoList;
    }

    @Override
    public ShareViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ShareViewHolder(LayoutInflater.from(
                mContext).inflate(android.R.layout.select_dialog_item, parent, false));
    }

    @Override
    public void onBindViewHolder(ShareViewHolder holder, int position) {
        ActivityInfo info = mInfoList.get(position).activityInfo;

        holder.textView.setText(
                info.applicationInfo.loadLabel(mContext.getPackageManager()).toString());
        holder.textView.setCompoundDrawablesWithIntrinsicBounds(null,
                info.applicationInfo.loadIcon(mContext.getPackageManager()), null, null);
    }

    @Override
    public int getItemCount() {
        return mInfoList.size();
    }

    class ShareViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        ShareViewHolder(View itemView) {
            super(itemView);
            this.textView = (TextView) itemView.findViewById(android.R.id.text1);
            this.textView.setGravity(Gravity.CENTER);
            this.textView.setTextSize(12);
            int dp5 = (int) (5 * mContext.getResources().getDisplayMetrics().density + 0.5f);
            this.textView.setCompoundDrawablePadding(dp5);
        }
    }
}
