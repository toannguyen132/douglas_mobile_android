package com.project.groupproject.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.libraries.places.api.model.AutocompletePrediction;
import com.google.android.libraries.places.api.net.FindAutocompletePredictionsResponse;
import com.project.groupproject.R;

import java.util.ArrayList;

public class LocationAdapter extends RecyclerView.Adapter<LocationAdapter.LocationViewHolder> {

    ArrayList<String> data = new ArrayList<>();
    OnSelectLocation onSelectLocation;

    public LocationAdapter() {
        data = new ArrayList<>();
    }

    public LocationAdapter(ArrayList<String> data) {
        this.data = data;
    }

    public void setOnSelectListener(OnSelectLocation event) {
        onSelectLocation = event;
    }

    @NonNull
    @Override
    public LocationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.location_autocomplete, parent, false);

        LocationViewHolder viewHolder = new LocationViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull LocationViewHolder holder, int position) {
        String item = data.get(position);
        holder.textLocation.setText(item);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void updateData(ArrayList<String> newData){
        this.data = newData;
        this.notifyDataSetChanged();
    }

    public void clearData() {
        this.updateData(new ArrayList<String>());
    }

    static public ArrayList<String> convertToArray(FindAutocompletePredictionsResponse response) {
        ArrayList<String> list = new ArrayList<>();
        for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
            list.add(prediction.getFullText(null).toString());
        }
        return list;
    }

    public class LocationViewHolder extends RecyclerView.ViewHolder {

        TextView textLocation;

        public LocationViewHolder(View itemView) {
            super(itemView);
            textLocation = itemView.findViewById(R.id.txt_address);

            textLocation.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onSelectLocation.onSelect(textLocation.getText().toString());
                }
            });
        }
    }

    public interface OnSelectLocation {
        void onSelect(String item);
    }
}
