package com.example.calorieteststuff;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Calendar;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

    private ArrayList<Food> mFoodList;
//    public int publicint = 0;
    private Context mContext;
    public FoodAdapter(Context context, ArrayList<Food> foodList) {
        this.mFoodList = foodList;
        this.mContext = context;
    }

      class FoodViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {


        private ImageView mFoodImage;
        private TextView mNameText;
        private TextView mCaloriesText;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            mFoodImage = itemView.findViewById(R.id.imageViewFood);
            mNameText = itemView.findViewById(R.id.foodTitle);
            mCaloriesText = itemView.findViewById(R.id.foodCalories);

            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

        }

        // TO DO: FIX THIS ENTIRE THING, IT IS GHASTLY!!!
        @Override
        public boolean onLongClick(View view) {
            int position = getAdapterPosition();
            Log.d("Viewholder", "Long Click Food item" + position);
//            ArrayList<Integer> foodToRemove = new ArrayList<>();
//            foodToRemove.add(position);
//            view.setBackgroundColor(Color.BLACK);
            Food currFood = mFoodList.get(position);
            double currCalories = currFood.calories;
            Log.d("CurrentCaloriesRemoved", ""+currCalories);
            mFoodList.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, mFoodList.size());
            int start = MainActivity.totalCaloriesText.getLayout().getLineStart(0);
            int end = MainActivity.totalCaloriesText.getLayout().getLineEnd(MainActivity.totalCaloriesText.getLineCount() - 1);
            String displayed = MainActivity.totalCaloriesText.getText().toString().substring(start, end);
//            Log.d("Current calorie text", ""+displayed);
            double newAmount = Double.parseDouble(displayed);
//            Log.d("toRemoveIS!!", ""+(newAmount- currCalories));
            double finalNumber = (newAmount - currCalories);
//            Log.d("finalNumber!!", ""+finalNumber);
            MainActivity.setTotalCalories(finalNumber);
            MainActivity.totalCaloriesText.setText(Double.toString(finalNumber));
            return true;
        }

        @Override
        public void onClick(View view) {
            int position = getAdapterPosition();
            Food currentFood = mFoodList.get(getAdapterPosition());
            Intent intent = new Intent(mContext, DetailActivity.class);
            intent.putExtra("title", currentFood.getName());
            intent.putExtra("info", Double.toString(currentFood.getCalories()));
//            Log.d("calories lol", ""+currentFood.getCalories());
            intent.putExtra("image_resource", currentFood.getImageResource());
            intent.putExtra("time_added", Calendar.getInstance().getTime().toString());
            mContext.startActivity(intent);
        }
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item, parent, false);
        FoodViewHolder fvh = new FoodViewHolder(v);
        return fvh;
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        Food currentFood = mFoodList.get(position);
        holder.mNameText.setText(currentFood.getName());
        holder.mCaloriesText.setText(Double.toString(currentFood.getCalories()));
        holder.mFoodImage.setImageResource(currentFood.getImageResource());
    }

    @Override
    public int getItemCount() { return mFoodList.size(); }
}
