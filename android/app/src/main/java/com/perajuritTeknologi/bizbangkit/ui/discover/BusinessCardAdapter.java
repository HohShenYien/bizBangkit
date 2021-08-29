package com.perajuritTeknologi.bizbangkit.ui.discover;

import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.card.MaterialCardView;
import com.perajuritTeknologi.bizbangkit.APICaller;
import com.perajuritTeknologi.bizbangkit.DataStructure;
import com.perajuritTeknologi.bizbangkit.R;
import com.perajuritTeknologi.bizbangkit.event.GetBusinessListEvent;
import com.perajuritTeknologi.bizbangkit.event.ProfileScrolled;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

public class BusinessCardAdapter extends RecyclerView.Adapter<BusinessCardAdapter.ViewHolder> {
    public ArrayList<DataStructure.SimpleBusiness> businesses;

    public BusinessCardAdapter() {
        businesses = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.discover_card_layout, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        DataStructure.SimpleBusiness business = businesses.get(position);
        TextView title = holder.card.findViewById(R.id.discover_business_card_title);
        ImageView logo = holder.card.findViewById(R.id.discover_business_card_img);
        TextView type = holder.card.findViewById(R.id.discover_business_card_description);
        TextView phase = holder.card.findViewById(R.id.discover_business_card_phase);
        TextView percent = holder.card.findViewById(R.id.discover_business_card_percent);
        ProgressBar progressBar = holder.card.findViewById(R.id.discover_business_card_progress);

        int totalNeededInThisPhase = (int) (business.phase == 1 ? 0.08 * business.valuation :
                0.4 * business.valuation);
        int gathered = (int) (business.purchasedPercent / 100f * business.valuation);
        int progress = gathered * 100 / totalNeededInThisPhase;

        title.setText(business.businessName);
        if (business.logo == null) {
            logo.setImageResource(R.drawable.default_business_logo);
        } else {
            logo.setImageBitmap(business.logo);
        }
        type.setText(business.type);
        if (business.phase == 1) {
            holder.card.setBackgroundColor(Color.rgb(176,224,230));
        } else {
            holder.card.setBackgroundColor(Color.rgb(152,251,152));
        }
        phase.setText("Phase " + business.phase + ": RM" + gathered + " / RM" + totalNeededInThisPhase);
        progressBar.setProgress(progress);
        percent.setText(progress + "%");


    }

    @Override
    public int getItemCount() {
        return businesses.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public MaterialCardView card;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            card = (MaterialCardView) itemView;
        }
    }

    public void setItems(ArrayList<DataStructure.SimpleBusiness> newBusinesses) {
        businesses = newBusinesses;
    }

}
