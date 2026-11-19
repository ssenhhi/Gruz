package com.example.gruz.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gruz.models.CityResponse;
import com.example.gruz.R;

import java.util.List;

public class CityAdapter extends RecyclerView.Adapter<CityAdapter.ViewHolder> {

    private List<CityResponse> cities;

    public CityAdapter(List<CityResponse> cities) {
        this.cities = cities;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_city, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CityResponse city = cities.get(position);
        holder.tvName.setText(city.getName());
        holder.tvBranch.setText(city.getRegion());
    }
    @Override
    public int getItemCount() {
        return cities.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvBranch;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvCityName);
            tvBranch = itemView.findViewById(R.id.tvCityBranch);
        }
    }
}
