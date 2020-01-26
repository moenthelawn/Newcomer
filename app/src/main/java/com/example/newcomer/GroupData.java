package com.example.newcomer;

import android.graphics.drawable.BitmapDrawable;
import android.widget.Button;

import java.util.ArrayList;

public class GroupData {
    ArrayList<String> mData;
    ArrayList<Pair> mButton;
    ArrayList<BitmapDrawable>mBitmap;

    public GroupData(int MAXSIZE){
        //UserData userData = getApplicationContext();
        this.mData = new ArrayList<String>();
        this.mButton = new ArrayList<Pair>();
        this.mBitmap = new ArrayList<BitmapDrawable>();

    }
    public ArrayList<BitmapDrawable> getmBitmap(){
        return this.mBitmap;
    }
    public void setArrayList_Button(int position, Button button){
        //Essentially we need the pair to get the position of an element
        Pair pair = new Pair(position,button);
        this.mButton.add(pair);

    }
    public Button getButton_Position(int position){
        for (int i = 0; i < mButton.size();i++){
            Pair pair = mButton.get(i);
            int index = (int) pair.getFirst();
            if (index == position){
                return (Button) pair.getSecond();
            }
        }
        return null; //Otherwise we return that there is nothing to be done

    }
    public void removeDataPoint(int position){
        //Remove the datapoint fromt eh positon
        if (this.mData.get(position) != null || this.mBitmap.get(position) != null) {
            this.mData.remove(position);

        }
    }

    public ArrayList<String> getArrayList_Data(){
        return this.mData;
    }
    public void setArrayList_Data(ArrayList<String> mData){
        this.mData = mData;
    }
    public void appendArrayList_Data(String element){
        this.mData.add(element);
    }
    public String getElement_Data(int index){
        return this.mData.get(index);
    }
}
