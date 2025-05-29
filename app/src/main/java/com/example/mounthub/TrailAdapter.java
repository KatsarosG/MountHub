package com.example.mounthub;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

public class TrailAdapter extends RecyclerView.Adapter<TrailAdapter.TrailViewHolder> {

    private Trail[] trails;

    public TrailAdapter(Trail[] trails) {
        this.trails = trails;
    }

    @NonNull
    @Override
    public TrailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_trail, parent, false);
        return new TrailViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TrailViewHolder holder, int position) {
        holder.trailNameTextView.setText(trails[position].getName());
        holder.trailDurationTextView.setText("Duration: " + trails[position].getDuration() + "h");
        holder.trailDifficultyTextView.setText("Difficulty: " + trails[position].getDifficulty().toString().toLowerCase());
        holder.trailDistanceTextView.setText("Distance: " + trails[position].getDistance() + "km");
    }

    @Override
    public int getItemCount() {
        return trails.length;
    }

    static class TrailViewHolder extends RecyclerView.ViewHolder {
        TextView trailNameTextView;
        TextView trailDurationTextView;
        TextView trailDifficultyTextView;
        TextView trailDistanceTextView;

        public TrailViewHolder(@NonNull View itemView) {
            super(itemView);
            trailNameTextView = itemView.findViewById(R.id.trail_name);
            trailDurationTextView = itemView.findViewById(R.id.trail_duration);
            trailDifficultyTextView = itemView.findViewById(R.id.trail_difficulty);
            trailDistanceTextView = itemView.findViewById(R.id.trail_distance);
        }
    }
}
