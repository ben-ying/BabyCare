package com.ben.yjh.babycare.widget.share;


import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ben.yjh.babycare.R;
import com.ben.yjh.babycare.model.Event;
import com.ben.yjh.babycare.util.ImageUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ShareAdapter extends RecyclerView.Adapter<ShareAdapter.ShareViewHolder> {

    private Context mContext;
    private Event mEvent;
    private List<ResolveInfo> mInfoList;
    private boolean mHasWeChat;

    public ShareAdapter(Context context, Event event) {
        this.mContext = context;
        this.mEvent = event;
        Intent sendIntent = new Intent(Intent.ACTION_SEND);
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, event.getContent());
        sendIntent.setType("text/plain");
        this.mInfoList = context.getPackageManager().queryIntentActivities(sendIntent, 0);
        Collections.sort(mInfoList, new Comparator<ResolveInfo>() {
            @Override
            public int compare(ResolveInfo lhs, ResolveInfo rhs) {
                // High frequency app used in the front
                if (lhs.activityInfo.packageName.contains("com.tencent")
                        && !rhs.activityInfo.packageName.contains("com.tencent")) {
                    mHasWeChat = true;
                    return -1;
                } else if (!lhs.activityInfo.packageName.contains("com.tencent")
                        && rhs.activityInfo.packageName.contains("com.tencent")) {
                    mHasWeChat = true;
                    return 1;
                }
                return Integer.compare(rhs.activityInfo.configChanges, lhs.activityInfo.configChanges);
            }
        });
//        if (mHasWeChat) {
//            this.mInfoList.add(1, new ResolveInfo());
//        }
    }

    @Override
    public ShareViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ShareViewHolder(LayoutInflater.from(
                mContext).inflate(R.layout.item_share, parent, false));
    }

    @Override
    public void onBindViewHolder(ShareViewHolder holder, int position) {
        ActivityInfo info = mInfoList.get(position).activityInfo;
        PackageManager packageManager = mContext.getPackageManager();
        final Intent intent = new Intent();
        intent.setComponent(new ComponentName(info.packageName, info.name));
        final ResolveInfo resolveInfo = packageManager.resolveActivity(intent, 0);
        String label;

//        if (mHasWeChat && position)
        if (mInfoList.get(position).labelRes != 0) {
            try {
                label = packageManager.getResourcesForActivity(
                        intent.getComponent()).getString(mInfoList.get(position).labelRes);
            } catch (PackageManager.NameNotFoundException e) {
                label = resolveInfo.activityInfo.applicationInfo
                        .loadLabel(mContext.getPackageManager()).toString();
            }
        } else {
            label = resolveInfo.activityInfo.applicationInfo
                    .loadLabel(mContext.getPackageManager()).toString();
        }
        holder.textView.setText(label);
        holder.textView.setPadding(0, 0, 0, 0);
        Drawable drawable = ImageUtils.scale2IconSize(mContext,
                resolveInfo.activityInfo.loadIcon(mContext.getPackageManager()));
        holder.textView.setCompoundDrawablesWithIntrinsicBounds(null, drawable, null, null);
        holder.textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int stringId = mContext.getApplicationInfo().labelRes;
                String appName = stringId == 0 ? mContext.getApplicationInfo()
                        .nonLocalizedLabel.toString() : mContext.getString(stringId);
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_SUBJECT, appName);
                shareIntent.setComponent(intent.getComponent());
                String text = mEvent.getContent();
                text += "\nhttp://www.baidu.com";
                shareIntent.putExtra(Intent.EXTRA_TEXT, text);
                mContext.startActivity(shareIntent);
            }
        });
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
        }
    }
}
