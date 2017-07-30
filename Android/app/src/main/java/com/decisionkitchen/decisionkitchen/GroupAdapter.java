package com.decisionkitchen.decisionkitchen;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;

/**
 * Created by Alex on 2017-07-29.
 */

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.ViewHolder> {
    private static List<Group> mGroups;
    private static RecyclerView mRecyclerView;
    private static MainActivity mMainActivity;

    private static final View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = mRecyclerView.getChildLayoutPosition(v);
            String id = mGroups.get(position).getId();
            mMainActivity.goTest(id);
        }
    };

    public GroupAdapter(List<Group> groups, RecyclerView recyclerView, MainActivity mainActivity) {
        this.mGroups = groups;
        this.mRecyclerView = recyclerView;
        this.mMainActivity = mainActivity;
    }

    public GroupAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout view = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.text_view, parent, false);
        view.setOnClickListener(mOnClickListener);
        return new ViewHolder(view);
    }

    public String getPreviewText(Group group) {
        boolean isGameRunning = false;
        Game currentGame = null;

        if (group.getGames() == null) {
            return mMainActivity.getResources().getString(R.string.no_games_text);
        }

        for (Game game : group.getGames()) {
            if (game.getMeta().getEnd() == null) {
                isGameRunning = true;
                currentGame = game;
                break;
            }
        }

        if (isGameRunning) {
            FirebaseUser user = mMainActivity.getUser();
            if (currentGame.getResponses() == null || currentGame.getRating() == null) {
                return mMainActivity.getResources().getString(R.string.not_voted_text);
            } else if (!currentGame.getResponses().containsKey(user.getUid()) || !currentGame.getRating().containsKey(user.getUid())) {
                return mMainActivity.getResources().getString(R.string.not_voted_text);
            } else {
                return mMainActivity.getResources().getString(R.string.voted_text);
            }
        }

        long recent = 0;
        Game recentGame = null;
        for (Game game : group.getGames()) {
            long gameTime = new DateTime(game.getMeta().getEnd()).getMillis();
            if (gameTime > recent) {
                recent = gameTime;
                recentGame = game;
            }
        }

        if (recentGame == null) {
            return "Live demos amirite";
        }

        return "Last vote: " + group.getRestaurants().get(recentGame.getResult().getRestaurant_id()).getName() + " at " + new DateTime(recentGame.getMeta().getEnd()).toString(DateTimeFormat.shortDateTime());

    }

    @Override
    public void onBindViewHolder(GroupAdapter.ViewHolder holder, int position) {
        TextView titleView = (TextView) holder.itemView.findViewById(R.id.title);
        titleView.setText(mGroups.get(position).getName());

        TextView blurbView = (TextView) holder.itemView.findViewById(R.id.blurb);
        blurbView.setText(getPreviewText(mGroups.get(position)));
    }

    @Override
    public int getItemCount() {
        return mGroups.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        LinearLayout itemView;
        public ViewHolder(LinearLayout itemView) {
            super(itemView);
            this.itemView = itemView;
        }
    }
}
