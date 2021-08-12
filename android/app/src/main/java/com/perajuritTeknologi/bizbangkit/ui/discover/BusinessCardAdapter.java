package com.perajuritTeknologi.bizbangkit.ui.discover;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.perajuritTeknologi.bizbangkit.DataStructure;
import com.perajuritTeknologi.bizbangkit.R;

import java.util.ArrayList;

public class BusinessCardAdapter extends RecyclerView.Adapter<BusinessCardAdapter.ViewHolder> {
    private ArrayList<DataStructure.SimpleBusiness> businesses;

    public BusinessCardAdapter(ArrayList<DataStructure.SimpleBusiness> inputList) {
        businesses = inputList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.discover_card_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("ShenYien",position + " bound");
    }

    @Override
    public int getItemCount() {
        return businesses.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
