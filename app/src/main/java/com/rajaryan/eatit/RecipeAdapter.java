package com.rajaryan.eatit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public  class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.ViewHolder> {
    LayoutInflater inflater;
    List<RecipeData> recipeDataList;

    public RecipeAdapter(Context ctx, List<RecipeData> recipeDataList) {
        this.inflater = LayoutInflater.from(ctx);
        this.recipeDataList = recipeDataList;
    }

    @NonNull
    @Override
    public RecipeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.food_item, parent, false);
        RecipeAdapter.ViewHolder viewHolder = new RecipeAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeAdapter.ViewHolder holder, int position) {
        // bind the data
        holder.tittle.setText(recipeDataList.get(position).getTitle());
        holder.time.setText(recipeDataList.get(position).getId());


    }

    @Override
    public int getItemCount() {
        return 4;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tittle, time;
        ImageView image;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tittle = itemView.findViewById(R.id.tittle);
            time = itemView.findViewById(R.id.time);
            image = itemView.findViewById(R.id.image);

            // handle onClick

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "Do Something With this Click", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
