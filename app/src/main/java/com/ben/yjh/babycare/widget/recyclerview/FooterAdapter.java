package com.ben.yjh.babycare.widget.recyclerview;

import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;


public class FooterAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
        implements View.OnClickListener, View.OnLongClickListener {

    private static final int DEFAULT = 0;
    private static final int FOOTER = -1;

    private RecyclerView.Adapter mAdapter;
    private LoadMoreRecyclerView mRecyclerView;
    private LoadingMoreFooter mLoadingMoreFooter;
    private AdapterView.OnItemLongClickListener onItemLongClickListener;
    private AdapterView.OnItemClickListener onItemClickListener;

    FooterAdapter(LoadMoreRecyclerView loadMoreRecyclerView,
                         LoadingMoreFooter loadingMoreFooter, RecyclerView.Adapter adapter) {
        this.mRecyclerView = loadMoreRecyclerView;
        this.mAdapter = adapter;
        this.mLoadingMoreFooter = loadingMoreFooter;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
        if (manager instanceof GridLayoutManager) {
            final GridLayoutManager gridManager = ((GridLayoutManager) manager);
            final GridLayoutManager.SpanSizeLookup originLookup = gridManager.getSpanSizeLookup();
            gridManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (getItemViewType(position) == RecyclerView.INVALID_TYPE
                            || getItemViewType(position) == RecyclerView.INVALID_TYPE - 1)
                        return gridManager.getSpanCount();
                    else if (originLookup != null)
                        return originLookup.getSpanSize(position);
                    else
                        return 1;
                }
            });
        }
    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);
        ViewGroup.LayoutParams lp = holder.itemView.getLayoutParams();
        if (lp != null
                && lp instanceof StaggeredGridLayoutManager.LayoutParams
                && isFooter(holder.getLayoutPosition())) {
            StaggeredGridLayoutManager.LayoutParams p =
                    (StaggeredGridLayoutManager.LayoutParams) lp;
            p.setFullSpan(true);
        }
    }

    private boolean isFooter(int position) {
        return position < getItemCount() && position >= getItemCount() - 1;
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == FOOTER) {
            return new SimpleViewHolder(mLoadingMoreFooter);
        }
        return mAdapter.onCreateViewHolder(parent, viewType);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        holder.itemView.setOnClickListener(this);
        holder.itemView.setOnLongClickListener(this);
        if (mAdapter != null) {
            int count = mAdapter.getItemCount();
            if (position < count) {
                mAdapter.onBindViewHolder(holder, position);
            }
        }
    }

    @Override
    public int getItemCount() {
        if (mAdapter != null) {
            return 1 + mAdapter.getItemCount();
        } else {
            return 1;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (isFooter(position)) {
            return FOOTER;
        }
        if (mAdapter != null) {
            int count = mAdapter.getItemCount();
            if (position < count) {
                return mAdapter.getItemViewType(position);
            }
        }
        return DEFAULT;
    }

    @Override
    public long getItemId(int position) {
        if (mAdapter != null && position >= 0) {
            int adapterCount = mAdapter.getItemCount();
            if (position < adapterCount) {
                return mAdapter.getItemId(position);
            }
        }
        return -1;
    }

    @Override
    public void onClick(View view) {
        if (onItemClickListener != null) {
            onItemClickListener.onItemClick(null, view,
                    mRecyclerView.getChildAdapterPosition(view), 0);
        }
    }

    @Override
    public boolean onLongClick(View view) {
        if (onItemLongClickListener != null) {
            onItemLongClickListener.onItemLongClick(null,
                    view, mRecyclerView.getChildAdapterPosition(view), 0);
        }
        return true;
    }

    void setOnItemClickListener(AdapterView.OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }


    void setOnItemLongClickListener(
            AdapterView.OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    private class SimpleViewHolder extends RecyclerView.ViewHolder {
        SimpleViewHolder(View itemView) {
            super(itemView);
        }
    }
}