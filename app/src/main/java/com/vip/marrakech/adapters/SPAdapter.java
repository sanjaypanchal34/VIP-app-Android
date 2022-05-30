package com.vip.marrakech.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;


import com.vip.marrakech.R;

import java.util.List;

public abstract class SPAdapter<T> extends BaseAdapter {

    private Context mContext;
    private List<T> list;

    protected abstract String setSpinnerText(T item);

    protected int setView() {
        return R.layout.item_text_spinner;
    }


    protected int setTextView() {
        return R.id.tv_string;
    }

    protected int setCount() {
        return list.size();
    }

    protected int setTextViewColor() {
        return Color.BLACK;
    }

    protected Typeface setTextViewTypeface() {
        return Typeface.DEFAULT;
    }

    public onSpinnerSelectListener<T> listener;

    public SPAdapter(Context aContext, List<T> aList) {
        mContext = aContext;
        list = aList;
    }

    public SPAdapter(Context aContext, List<T> aList, onSpinnerSelectListener<T> listener) {
        mContext = aContext;
        list = aList;
        this.listener = listener;
    }

    public void setListener(onSpinnerSelectListener<T> listener) {
        this.listener = listener;
    }

    @Override
    public int getCount() {
        return setCount();
    }

    @Override
    public T getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }


    @Override
    public View getView(final int position, View aView, ViewGroup aViewGroup) {
        @SuppressLint("CompaniesViewHolder") View lView = LayoutInflater.from(mContext).inflate(setView(), aViewGroup, false);
        TextView tv = lView.findViewById(setTextView());
        tv.setText(setSpinnerText(list.get(position)));
        tv.setTextColor(setTextViewColor());
        tv.setLineSpacing(0, 1.5f);
        // rl_text_view.setBackgroundColor(mContext.getResources().getColor(R.color.color_lightGray_separator));
        tv.setTypeface(setTextViewTypeface());
        if (listener != null)
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null)
                        listener.onSpinnerItemClick(list.get(position), position);
                }
            });
        return lView;
    }

    public interface onSpinnerSelectListener<T> {
        void onSpinnerItemClick(T item, int pos);
    }
}