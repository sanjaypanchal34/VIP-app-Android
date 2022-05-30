package com.vip.marrakech.adapters;

import android.content.Context;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public abstract class GRecyclerAdapter<T, VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> implements Filterable {

    public Context context;
    public List<T> list, filterList;


    public abstract VH setItemRow(ViewGroup viewGroup, int i);

    public abstract void myBindViewHolder(VH viewHolder, int i, List<T> mList);

    public String onFilterWith(T item) {
        return "NULL";
    }

    public String onFilterWith2(T item) {
        return "";
    }

    public int setItemViewType(T item, int pos) {
        return 0;
    }

    public GRecyclerAdapter(Context context, List<T> list) {
        this.context = context;
        this.filterList = list;
        this.list = list;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return setItemRow(viewGroup, i);
    }


    @Override
    public void onBindViewHolder(@NonNull VH viewHolder, int i) {
        myBindViewHolder(viewHolder, i, filterList);
    }

    @Override
    public int getItemViewType(int position) {
        return setItemViewType(list.get(position), position);
    }

    @Override
    public int getItemCount() {
        return filterList.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    filterList = list;
                } else {
                    List<T> filteredList = new ArrayList<>();
                    for (T row : list) {

                        if (onFilterWith(row).toLowerCase().contains(charString.toLowerCase()) || onFilterWith2(row).toLowerCase().contains(charString.toLowerCase())) {
                                filteredList.add(row);
                        }
                    }

                    filterList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = filterList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                filterList = (List<T>) filterResults.values;

                // refresh the list with filtered data
                notifyDataSetChanged();
            }
        };
    }
}