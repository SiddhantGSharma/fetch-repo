package com.fetch_hiring.fetch;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<DataAdapter.DataViewHolder> {
    private List<DataItem> dataItems;

    public DataAdapter(List<DataItem> dataItems) {
        this.dataItems = dataItems;
    }

    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item, parent, false);
        return new DataViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DataViewHolder holder, int position) {
        DataItem dataItem = dataItems.get(position);
        holder.bind(dataItem);
    }

    @Override
    public int getItemCount() {
        return dataItems.size();
    }

    static class DataViewHolder extends RecyclerView.ViewHolder {

        private TextView listIdTextView;
        private TextView nameTextView;

        DataViewHolder(@NonNull View itemView) {
            super(itemView);
            listIdTextView = itemView.findViewById(R.id.listIdTextView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
        }

        void bind(DataItem dataItem) {
            listIdTextView.setText("List ID: " + dataItem.getListId());
            nameTextView.setText("Name: " + dataItem.getName());
        }
    }
}
