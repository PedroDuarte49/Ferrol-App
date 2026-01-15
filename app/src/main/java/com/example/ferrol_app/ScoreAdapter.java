package com.example.ferrol_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ScoreAdapter extends RecyclerView.Adapter<ScoreAdapter.ScoreViewHolder> {
    private List<Score> scoreList;

    public ScoreAdapter(List<Score> scoreList) {
        this.scoreList = scoreList;
    }

    @NonNull
    @Override
    public ScoreViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_score, parent, false);
        return new ScoreViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ScoreViewHolder holder, int position) {
        Score score = scoreList.get(position);
        holder.tvUserName.setText(score.getUserName());
        holder.tvPoints.setText(score.getPoints() + " points");
    }

    @Override
    public int getItemCount() { return scoreList.size(); }


    public void updateData(List<Score> newList) {
        this.scoreList = newList;
        notifyDataSetChanged(); // Esto hace que la lista se refresque visualmente
    }
    // -----------------------------------

    public static class ScoreViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserName, tvPoints;
        public ScoreViewHolder(@NonNull View itemView) {
            super(itemView);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvPoints = itemView.findViewById(R.id.tvPoints);
        }
    }
}