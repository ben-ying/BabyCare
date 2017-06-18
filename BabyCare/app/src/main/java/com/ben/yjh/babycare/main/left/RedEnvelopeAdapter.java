package com.ben.yjh.babycare.main.left;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ben.yjh.babycare.R;
import com.ben.yjh.babycare.http.EventTaskHandler;
import com.ben.yjh.babycare.http.HttpResponseInterface;
import com.ben.yjh.babycare.http.NavigationTaskHandler;
import com.ben.yjh.babycare.model.Event;
import com.ben.yjh.babycare.model.HttpBaseResult;
import com.ben.yjh.babycare.model.RedEnvelope;
import com.ben.yjh.babycare.model.User;
import com.ben.yjh.babycare.util.AlertUtils;
import com.ben.yjh.babycare.util.Constants;

import java.util.List;

public class RedEnvelopeAdapter extends RecyclerView.Adapter<
        RedEnvelopeAdapter.RedEnvelopeViewHolder> implements View.OnClickListener {

    private Context mContext;
    private List<RedEnvelope> mRedEnvelopes;
    private User mUser;

    RedEnvelopeAdapter(Context context, User user, List<RedEnvelope> redEnvelopes) {
        this.mContext = context;
        this.mUser = user;
        this.mRedEnvelopes = redEnvelopes;
    }

    public void setData(List<RedEnvelope> redEnvelopes) {
        this.mRedEnvelopes = redEnvelopes;
        notifyDataSetChanged();
    }

    @Override
    public RedEnvelopeViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RedEnvelopeViewHolder(
                LayoutInflater.from(mContext).inflate(R.layout.item_red_envelope, parent, false));
    }


    @Override
    public void onBindViewHolder(RedEnvelopeViewHolder holder, int position) {
        final RedEnvelope redEnvelope = mRedEnvelopes.get(position);
        holder.fromTextView.setText(redEnvelope.getMoneyFrom());
        holder.dateTextView.setText(redEnvelope.getCreatedDate());
        holder.moneyTextView.setText(redEnvelope.getMoney() + ", " + redEnvelope.getRemark());
        holder.rootView.setTag(redEnvelope);
        holder.rootView.setOnClickListener(this);
        holder.rootView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                AlertUtils.showConfirmDialog(mContext, R.string.delete_event_alert,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                deleteTask(redEnvelope);
                            }
                        });
                return true;
            }
        });
    }

    private void deleteTask(final RedEnvelope redEnvelope) {
        new NavigationTaskHandler(mContext, mUser.getToken())
                .deleteEvent(redEnvelope.getRedEnvelopeId(), new HttpResponseInterface<RedEnvelope>() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onSuccess(RedEnvelope classOfT) {
                        if (classOfT != null && redEnvelope.getRedEnvelopeId() == classOfT.getRedEnvelopeId()) {
                            mRedEnvelopes.remove(redEnvelope);
                            notifyDataSetChanged();
                            RedEnvelope.deleteAll(RedEnvelope.class, "red_envelope_id = ?",
                                    String.valueOf(redEnvelope.getRedEnvelopeId()));
                        }
                    }

                    @Override
                    public void onFailure(HttpBaseResult result) {
                    }

                    @Override
                    public void onHttpError(String error) {
                    }
                });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.content_layout:
                Intent intent = new Intent(mContext, RedEnvelopeActivity.class);
                ((Activity) mContext).startActivityForResult(
                        intent, Constants.RED_ENVELOPE_EDIT_REQUEST);
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mRedEnvelopes.size();
    }

    class RedEnvelopeViewHolder extends RecyclerView.ViewHolder {
        View rootView;
        TextView fromTextView;
        TextView dateTextView;
        TextView moneyTextView;

        RedEnvelopeViewHolder(View itemView) {
            super(itemView);
            this.rootView = itemView.findViewById(R.id.content_layout);
            this.fromTextView = (TextView) itemView.findViewById(R.id.tv_from);
            this.dateTextView = (TextView) itemView.findViewById(R.id.tv_datetime);
            this.moneyTextView = (TextView) itemView.findViewById(R.id.tv_money);
        }
    }
}
