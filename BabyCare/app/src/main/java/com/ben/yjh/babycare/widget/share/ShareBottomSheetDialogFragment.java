package com.ben.yjh.babycare.widget.share;

import android.app.Dialog;
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
        recyclerView.setAdapter(new ShareAdapter(getActivity(), event));
    }
}
