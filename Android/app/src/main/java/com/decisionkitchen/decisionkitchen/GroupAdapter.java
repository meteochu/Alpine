package com.decisionkitchen.decisionkitchen;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Alex on 2017-07-29.
 */

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder> {
    private static List<Group> mGroups;
    private static RecyclerView mRecyclerView;

    private static final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
          int position = mRecyclerView.getChildLayoutPosition(v);
          String text = mGroups.get(position).getName();
          Log.e("ayylmao", "ayylmao");
        }
    };

    public GroupAdapter(List<Group> groups, RecyclerView recyclerView) {
        this.mGroups = groups;
        this.mRecyclerView = recyclerView;
    }

    public GroupAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        TextView view = (TextView) LayoutInflater.from(parent.getContext()).inflate(R.layout.text_view, parent, false);
        view.setOnClickListener(mOnClickListener);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GroupAdapter.ViewHolder holder, int position) {
        holder.itemView.setText(mGroups.get(position).getName());
    }

    @Override
    public int getItemCount() {
        return mGroups.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView itemView;
        public ViewHolder(TextView itemView) {
            super(itemView);
            this.itemView = itemView;
        }
    }
}
