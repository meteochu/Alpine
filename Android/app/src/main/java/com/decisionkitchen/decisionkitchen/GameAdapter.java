package com.decisionkitchen.decisionkitchen;

import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Alex on 2017-07-30.
 */

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.ViewHolder> {

    public List<Integer> responses;
    private List<String> options;
    private static RecyclerView mRecyclerView;
    private static GameActivity mGameActivity;
    private String question;

    public GameAdapter(List<String> options, List<Integer> responses, RecyclerView recyclerView, GameActivity gameActivity, String question) {
        this.options = options;
        this.responses = responses;
        this.mRecyclerView = recyclerView;
        this.mGameActivity = gameActivity;
        this.question = question;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LinearLayout view = (LinearLayout) LayoutInflater.from(parent.getContext()).inflate(R.layout.options_view, parent, false);
        return new GameAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GameAdapter.ViewHolder holder, final int position) {
        if (position == 0) {
            TextView option = (TextView) holder.itemView.findViewById(R.id.option);
            option.setText(question);
            option.setGravity(Gravity.CENTER_HORIZONTAL);
            option.setTextColor(Color.WHITE);

            CardView cv = (CardView) holder.itemView.findViewById(R.id.hackydooda);
            cv.setCardBackgroundColor(Color.TRANSPARENT);

            Log.e("etag", "" + position);
            Switch s = (Switch) holder.itemView.findViewById(R.id.toggle);
            s.setVisibility(View.INVISIBLE);
        } else {
            TextView option = (TextView) holder.itemView.findViewById(R.id.option);
            option.setText(options.get(position-1));

            final Switch s = (Switch) holder.itemView.findViewById(R.id.toggle);
            s.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    responses.set(position-1, isChecked ? 1 : 0);
                }
            });

            CardView cv = (CardView) holder.itemView.findViewById(R.id.hackydooda);
            cv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    s.toggle();
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return options.size() + 1;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout itemView;
        public ViewHolder(LinearLayout v) {
            super(v);
            itemView = v;
        }
    }
}
