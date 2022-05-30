package com.vip.marrakech.admin.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;


import androidx.annotation.NonNull;

import com.vip.marrakech.R;
import com.vip.marrakech.admin.models.UserModel;

import java.util.ArrayList;
import java.util.List;

public class AutoCompleteAdapter extends ArrayAdapter<UserModel> {

    private final OnAutoSelectListener listener;
    Context context;
    int textViewResourceId;
    List<UserModel> items, tempItems, suggestions;

    public interface OnAutoSelectListener{
        void onSelectListener(UserModel data, int pos);
    }

    public AutoCompleteAdapter(Context context, List<UserModel> items, OnAutoSelectListener listener) {
        super(context, -1, items);
        this.context = context;
        this.items = items;
        this.listener = listener;
        tempItems = new ArrayList<UserModel>(items); // this makes the difference.
        suggestions = new ArrayList<UserModel>();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.item_auto_text, parent, false);
        }
        UserModel people = items.get(position);
        if (people != null) {
            TextView lblName = (TextView) view.findViewById(R.id.tv_string);
            if (lblName != null)
                lblName.setText(Html.fromHtml(people.getName()));
            lblName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!getItem(position).getName().equalsIgnoreCase("No results found")) {
                        listener.onSelectListener(getItem(position), position);
                    }
                }
            });
        }
        return view;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return nameFilter;
    }

    /**
     * Custom Filter implementation for custom suggestions we provide.
     */
    private Filter nameFilter = new Filter() {
        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((UserModel) resultValue).getName();
        }

        @Override
        protected FilterResults performFiltering(final CharSequence constraint) {
            final FilterResults filterResults = new FilterResults();
            if (constraint != null) {
                suggestions.clear();
                for (UserModel people : tempItems) {
                    if (people.getName().toLowerCase().contains(constraint.toString().toLowerCase())) {
                        suggestions.add(people);
                    }
                }
                filterResults.values = suggestions;
                filterResults.count = suggestions.size();
            }
            return filterResults;
        }

        @Override
        protected void publishResults(final CharSequence constraint, FilterResults results) {
            clear();
            final List<UserModel> filterList = (ArrayList<UserModel>) results.values;
            if (results.count > 0) {
                addAll(filterList);
                notifyDataSetChanged();

            }else {
                UserModel dataItem = new UserModel();
                dataItem.setName("No results found");
                add(dataItem);
                notifyDataSetChanged();
            }
        }
    };
}