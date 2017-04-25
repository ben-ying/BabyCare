package com.ben.yjh.babycare.widget.share;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.ben.yjh.babycare.R;
import com.ben.yjh.babycare.model.Event;
import com.ben.yjh.babycare.util.Constants;

import java.util.ArrayList;
import java.util.List;

public class ShareBottomSheetDialogFragment extends BottomSheetDialogFragment {

    public static ShareBottomSheetDialogFragment newInstance(Event event) {
        Bundle args = new Bundle();
        args.putSerializable(Constants.EVENT, event);
        ShareBottomSheetDialogFragment fragment = new ShareBottomSheetDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private BottomSheetBehavior.BottomSheetCallback
            mBottomSheetBehaviorCallback = new BottomSheetBehavior.BottomSheetCallback() {

        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            Log.d("", "");
        }
    };

    @Override
    public void setupDialog(Dialog dialog, int style) {
        dialog.setTitle(R.string.share_to);
        Event event = (Event) getArguments().getSerializable(Constants.EVENT);
        View contentView = View.inflate(getContext(), R.layout.dialog_share_bottom_sheet, null);
        dialog.setContentView(contentView);
        CoordinatorLayout.LayoutParams layoutParams =
                (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = layoutParams.getBehavior();
        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(mBottomSheetBehaviorCallback);
        }

        RecyclerView recyclerView = (RecyclerView) contentView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 4));
        recyclerView.setHasFixedSize(false);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.addItemDecoration(new ShareDecoration
                ((int) (20 * getActivity().getResources().getDisplayMetrics().density + 0.5f)));
        showShareDialog(event, recyclerView);
    }

    private void showShareDialog(Event event, RecyclerView recyclerView) {
        Intent sendIntent = new Intent(android.content.Intent.ACTION_SEND);
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, event.getContent());
        sendIntent.setType("text/plain");
        List<ResolveInfo> infoList =
                getActivity().getPackageManager().queryIntentActivities(sendIntent, 0);
        List<String> apps = new ArrayList<>();
        final List<Drawable> mIcons = new ArrayList<>();
        PackageManager pm = getActivity().getPackageManager();

        for (ResolveInfo info : infoList) {
            String packageName = info.activityInfo.packageName.toLowerCase();
            apps.add(info.activityInfo.applicationInfo
                    .loadLabel(getActivity().getPackageManager()).toString());
            mIcons.add(info.activityInfo.applicationInfo.loadIcon(getActivity().getPackageManager()));
        }

//        ListAdapter adapter = new ArrayAdapter<String>(
//                getActivity(),
//                android.R.layout.select_dialog_item,
//                android.R.id.text1,
//                apps) {
//            @NonNull
//            @Override
//            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//                View v = super.getView(position, convertView, parent);
//                TextView tv = (TextView) v.findViewById(android.R.id.text1);
//                tv.setGravity(Gravity.CENTER);
//                tv.setTextSize(14);
//                tv.setCompoundDrawablesWithIntrinsicBounds(null, mIcons.get(position), null, null);
//                int dp5 = (int) (10 * getActivity().getResources().getDisplayMetrics().density + 0.5f);
//                tv.setCompoundDrawablePadding(dp5);
//
//                return v;
//            }
//        };

        recyclerView.setAdapter(new ShareAdapter(getActivity(), event, infoList));
    }
}
