package com.perajuritTeknologi.bizbangkit.ui.discover;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.perajuritTeknologi.bizbangkit.DataStructure;
import com.perajuritTeknologi.bizbangkit.R;

import java.util.ArrayList;

public class InvestorAdapter extends BaseAdapter {
    Context context;
    private ArrayList<DataStructure.Investor> investors;
    private DataStructure.BusinessProfileDetails business;

    public InvestorAdapter(ArrayList<DataStructure.Investor> investors, Context context,
                           DataStructure.BusinessProfileDetails business) {
        this.investors = investors;
        this.context = context;
        this.business = business;
    }

    public void resetList(ArrayList<DataStructure.Investor> investors) {
        this.investors = investors;
    }

    @Override
    public int getCount() {
        return investors.size();
    }

    @Override
    public DataStructure.Investor getItem(int position) {
        return investors.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        DataStructure.Investor investor = getItem(position);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view;
        view = inflater.inflate(R.layout.investor_item_layout, parent, false);
        ImageView investorImg = view.findViewById(R.id.investor_pic);
        TextView username = view.findViewById(R.id.investor_username);
        TextView contribution = view.findViewById(R.id.investor_contributed);
        TextView percent = view.findViewById(R.id.investor_percent);
        ProgressBar progressBar = view.findViewById(R.id.investor_progress);

        username.setText(investor.username);
        progressBar.setProgress(investor.sharePercent);
        percent.setText(investor.sharePercent + "%");
        if (investor.userPic == null) {
            investorImg.setImageResource(investor.userGender.compareTo("M") == 0 ?
                    R.drawable.male_default : R.drawable.female_default);
        } else {
            investorImg.setImageBitmap(investor.userPic);
        }
        contribution.setText("Invested: RM " + investor.sharePercent * Integer.parseInt(business.valuation) / 100);
        return view;
    }
}
