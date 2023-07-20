package com.fetch_hiring.fetch;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.DataViewHolder> {
    private List<DataItem> dataItems; // data items to be displayed in the RecyclerView.

    public DataAdapter(List<DataItem> dataItems) {
        this.dataItems = dataItems;
    }

    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflate the 'list_item' layout to create the view for each item in the list
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        // create and return a new DataViewHolder with the inflated view
        return new DataViewHolder(itemView);
    }

    // bind data to a ViewHolder at a given position in the RecyclerView
    @Override
    public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {
        DataItem dataItem = dataItems.get(position);
        holder.bind(dataItem);
    }

    // returns the total number of items in the dataItems list
    @Override
    public int getItemCount() {
        return dataItems.size();
    }

    // represents each item's view in the RecyclerView.
    static class DataViewHolder extends RecyclerView.ViewHolder {

        private TextView listIdTextView;
        private TextView nameTextView;

        // Constructor
        DataViewHolder(@NonNull View itemView) {
            super(itemView);
            listIdTextView = itemView.findViewById(R.id.listIdTextView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
        }

        // to bind data from the DataItem to the views in the DataViewHolder
        void bind(DataItem dataItem) {
            // set the text for TextViews
            listIdTextView.setText("List ID: " + dataItem.getListId());
            nameTextView.setText("Name: " + dataItem.getName());
        }
    }
}
