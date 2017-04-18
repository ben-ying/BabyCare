package com.ben.yjh.babycare.widget.recyclerview;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;

public class LoadMoreRecyclerView extends RecyclerView {

    private Context mContext;
    private LoadMoreListener mLoadMoreListener;
    private boolean mCanloadMore = true;
    private Adapter mAdapter;
    private Adapter mFooterAdapter;
    private boolean mIsLoadingData = false;
    private LoadingMoreFooter mLoadingMoreFooter;

    public LoadMoreRecyclerView(Context context) {
        this(context, null);
    }

    public LoadMoreRecyclerView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadMoreRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        LoadingMoreFooter footView = new LoadingMoreFooter(mContext);
        addFootView(footView);
        footView.setGone();
    }

    public void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        if (mFooterAdapter != null && mFooterAdapter instanceof FooterAdapter) {
            ((FooterAdapter) mFooterAdapter).setOnItemClickListener(onItemClickListener);
        }
    }

    public void setOnItemLongClickListener(AdapterView.OnItemLongClickListener listener) {
        if (mFooterAdapter != null && mFooterAdapter instanceof FooterAdapter) {
            ((FooterAdapter) mFooterAdapter).setOnItemLongClickListener(listener);
        }
    }

    public void addFootView(LoadingMoreFooter view) {
        mLoadingMoreFooter = view;
    }

    public void setFootLoadingView(View view) {
        if (mLoadingMoreFooter != null) {
            mLoadingMoreFooter.addFootLoadingView(view);
        }
    }

    public void setFootEndView(View view) {
        if (mLoadingMoreFooter != null) {
            mLoadingMoreFooter.addFootEndView(view);
        }
    }

    public void refreshComplete() {
        if (mLoadingMoreFooter != null) {
            mLoadingMoreFooter.setGone();
        }
        mIsLoadingData = false;
    }

    public void loadMoreComplete() {
        if (mLoadingMoreFooter != null) {
            mLoadingMoreFooter.setGone();
        }
        mIsLoadingData = false;
    }

    public void loadMoreEnd() {
        if (mLoadingMoreFooter != null) {
            mLoadingMoreFooter.setEnd();
        }
    }

    public void setCanloadMore(boolean flag) {
        mCanloadMore = flag;
    }

    public void setLoadMoreListener(LoadMoreListener listener) {
        mLoadMoreListener = listener;
    }

    @Override
    public void setAdapter(Adapter adapter) {
        mAdapter = adapter;
        mFooterAdapter = new FooterAdapter(this, mLoadingMoreFooter, adapter);
        super.setAdapter(mFooterAdapter);
        mAdapter.registerAdapterDataObserver(mDataObserver);
    }

    @Override
    public void onScrollStateChanged(int state) {
        super.onScrollStateChanged(state);

        if (state == RecyclerView.SCROLL_STATE_IDLE && mLoadMoreListener != null && !mIsLoadingData && mCanloadMore) {
            LayoutManager layoutManager = getLayoutManager();
            int lastVisibleItemPosition;
            if (layoutManager instanceof GridLayoutManager) {
                lastVisibleItemPosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
            } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                int[] into = new int[((StaggeredGridLayoutManager) layoutManager).getSpanCount()];
                ((StaggeredGridLayoutManager) layoutManager).findLastVisibleItemPositions(into);
                lastVisibleItemPosition = last(into);
            } else {
                lastVisibleItemPosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
            }

            if (layoutManager.getChildCount() > 1
                    && lastVisibleItemPosition >= layoutManager.getItemCount() - 1 && layoutManager.getItemCount() > layoutManager.getChildCount()) {
                if (mLoadingMoreFooter != null) {
                    mLoadingMoreFooter.setVisible();
                }
                mIsLoadingData = true;
                mLoadMoreListener.onLoadMore();
            }
        }
    }

    private int last(int[] lastPositions) {
        int last = lastPositions[0];
        for (int value : lastPositions) {
            if (value > last) {
                last = value;
            }
        }
        return last;
    }

    private AdapterDataObserver mDataObserver = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            mFooterAdapter.notifyDataSetChanged();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            mFooterAdapter.notifyItemRangeInserted(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount) {
            mFooterAdapter.notifyItemRangeChanged(positionStart, itemCount);
        }

        @Override
        public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
            mFooterAdapter.notifyItemRangeChanged(positionStart, itemCount, payload);
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            mFooterAdapter.notifyItemRangeRemoved(positionStart, itemCount);
        }

        @Override
        public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
            mFooterAdapter.notifyItemMoved(fromPosition, toPosition);
        }
    };
}
