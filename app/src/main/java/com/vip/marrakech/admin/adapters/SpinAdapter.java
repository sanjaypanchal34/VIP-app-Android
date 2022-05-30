package com.vip.marrakech.admin.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.vip.marrakech.R;

public abstract class SpinAdapter<T> extends ArrayAdapter<T> {

    // Your sent context
    private Context context;

    protected abstract String setValue(T model);
    // Your custom values for the spinner (User)
    private T[] values;

    public SpinAdapter(Context context, int textViewResourceId,
                       T[] values) {
        super(context, textViewResourceId, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public int getCount(){
       return values.length;
    }

    @Override
    public T getItem(int position){
       return values[position];
    }

    @Override
    public long getItemId(int position){
       return position;
    }


    // And the "magic" goes here
    // This is for the "passive" state of the spinner
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        @SuppressLint("ViewHolder") TextView view = (TextView) LayoutInflater.from(context).inflate(R.layout.item_spinner,parent,false);
        // I created a dynamic TextView here, but you can reference your own  custom layout for each spinner item
        // Then you can get the current item using the values array (Users array) and the current position
        // You can NOW reference each method you has created in your bean object (User class)
        view.setText(setValue(getItem(position)));

        // And finally return your dynamic (or custom) view for each spinner item
        return view;
    }

    // And here is when the "chooser" is popped up
    // Normally is the same view, but you can customize it if you want
    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        TextView label = (TextView) super.getDropDownView(position, convertView, parent);
        label.setText(setValue(getItem(position)));

        return label;
    }
}