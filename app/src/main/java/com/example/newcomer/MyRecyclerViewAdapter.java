package com.example.newcomer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.ViewHolder> {

    private ArrayList<Pair> mData;

    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    private ViewHolder viewHolder;

    // data is passed into the constructor
    MyRecyclerViewAdapter(Context context, ArrayList<Pair> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }
    // inflates the cell layout from xml when needed
    @Override
    @NonNull
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.interest_button, parent, false);
        this.viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    // binds the data to the TextView in each cell
    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        Pair pair = mData.get(position);
        String g = (String) pair.getFirst();

        holder.myButton.setText(g);

        Pair button_curr = new Pair(holder.myButton,false); //First represents the button while the second index represents whether it was clicked or nah

        //Listen for the button to be clicke so that we can use the callback to send the button information back to the create group in order to properly
        //set the stroke width (i.e we clicked the button)

        holder.myButton.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN: {
                        mClickListener.setStrokeWidth(holder.myButton,true);
                        break;
                    }
                    case MotionEvent.ACTION_BUTTON_PRESS: {
                        mClickListener.setStrokeWidth(holder.myButton,true);
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        mClickListener.setStrokeWidth(holder.myButton,   false);
                        break;
                    }
                    case MotionEvent.ACTION_BUTTON_RELEASE: {
                        mClickListener.setStrokeWidth(holder.myButton,false);
                        break;
                    }
                }
                new CountDownTimer(1500, 1000) {

                    @Override
                    public void onTick(long millisUntilFinished) {

                    }

                    @Override
                    public void onFinish() {
                        mClickListener.setStrokeWidth(holder.myButton,false);
                    }
                }.start();
                return false;
            }
        });
        holder.myButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Then we set the listener
                mClickListener.onItemClick(v,position,holder.myButton);
                mClickListener.setStrokeWidth(holder.myButton,   false);

            }
        });
        holder.myButton.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mClickListener.setStrokeWidth(holder.myButton,   false);
                return false;
            }
        });

    }

    // total number of cells
    @Override
    public int getItemCount() {
        return mData.size();

    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        Button myButton;

        ViewHolder(View itemView) {

            super(itemView);
            myButton = itemView.findViewById(R.id.button3);
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition(),myButton);
        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {

        Pair pair = mData.get(id);
        return (String) pair.getFirst();

    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }
    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position,Button currButton);
        void setStrokeWidth(Button currButton, boolean clicked);
    }

}
