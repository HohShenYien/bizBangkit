package com.perajuritTeknologi.bizbangkit.ui.business;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.ParcelFileDescriptor;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.perajuritTeknologi.bizbangkit.DataConversion;
import com.perajuritTeknologi.bizbangkit.MainActivity;
import com.perajuritTeknologi.bizbangkit.R;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;

public class BusinessExistingBusinessFinancialDetailsFragment extends Fragment {
    private View root;
    private LinearLayout costTab, revenueTab, incomeTab;
    private ExpandableLayout costLayout, revenueLayout, incomeLayout;
    private ImageView costArrow, revenueArrow, incomeArrow;
    private MaterialButton costUploadButton, revenueUploadButton, incomeUploadButton;
    private ActivityResultLauncher<String[]> chooseCostPicture, chooseRevenuePicture, chooseIncomePicture;
    private GridLayout costGrid, revenueGrid, incomeGrid;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_business_existing_financial_details, container, false);

        setUpComponents();
        onExpandableLayoutClicked();

        setUpCostUploadButton();
        setUpRevenueUploadButton();
        setUpIncomeUploadButton();

        return root;
    }

    private void setUpComponents() {
        costTab = root.findViewById(R.id.businessFinancialCostLayout);
        costLayout = root.findViewById(R.id.businessFinancialCostExpandable);
        costArrow = root.findViewById(R.id.businessFinancialCostArrow);
        costUploadButton = root.findViewById(R.id.businessExistingFinancialCostUploadButton);
        costGrid = root.findViewById(R.id.businessExistingFinancialCostGrid);

        revenueTab = root.findViewById(R.id.businessFinancialRevenueLayout);
        revenueLayout = root.findViewById(R.id.businessFinancialRevenueExpandable);
        revenueArrow = root.findViewById(R.id.businessFinancialRevenueArrow);
        revenueUploadButton = root.findViewById(R.id.businessExistingFinancialRevenueUploadButton);
        revenueGrid = root.findViewById(R.id.businessExistingFinancialRevenueGrid);

        incomeTab = root.findViewById(R.id.businessFinancialIncomeLayout);
        incomeLayout = root.findViewById(R.id.businessFinancialIncomeExpandable);
        incomeArrow = root.findViewById(R.id.businessFinancialIncomeArrow);
        incomeUploadButton = root.findViewById(R.id.businessFinancialIncomeUploadButton);
        incomeGrid = root.findViewById(R.id.businessExistingFinancialIncomeGrid);

    }

    private int convertDipToPix(int dip) {
        return Math.round(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dip, getResources().getDisplayMetrics()));
    }

    private GridLayout.LayoutParams getLayoutParams() {
        GridLayout.LayoutParams param =new GridLayout.LayoutParams();
        param.height = convertDipToPix(120);
        param.width = convertDipToPix(120);
        param.setGravity(Gravity.CENTER);

        return param;
    }

    private void onExpandableLayoutClicked() {
        costTab.setOnClickListener(view -> {
            if (costLayout.isExpanded()) {
                costArrow.setImageResource(R.drawable.arrow_down_ic);
                costLayout.collapse();
            } else {
                costArrow.setImageResource(R.drawable.arrow_up_ic);
                costLayout.expand();
            }
        });

        revenueTab.setOnClickListener(view -> {
            if (revenueLayout.isExpanded()) {
                revenueArrow.setImageResource(R.drawable.arrow_down_ic);
                revenueLayout.collapse();
            } else {
                revenueArrow.setImageResource(R.drawable.arrow_up_ic);
                revenueLayout.expand();
            }
        });

        incomeTab.setOnClickListener(view -> {
            if (incomeLayout.isExpanded()) {
                incomeArrow.setImageResource(R.drawable.arrow_down_ic);
                incomeLayout.collapse();
            } else {
                incomeArrow.setImageResource(R.drawable.arrow_up_ic);
                incomeLayout.expand();
            }
        });
    }

    private void setUpCostUploadButton() {
        chooseCostPicture = registerForActivityResult(new ActivityResultContracts.OpenDocument(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if (result != null) {
                    ImageView imageView = new ImageView(root.getContext());
                    imageView.setImageURI(result);
                    imageView.setLayoutParams(getLayoutParams());
                    costGrid.addView(imageView);
                }
            }
        });
        costUploadButton.setOnClickListener(view -> {
            chooseCostPicture.launch(new String[] {"image/*"});
        });
    }

    private void setUpRevenueUploadButton() {
        chooseRevenuePicture = registerForActivityResult(new ActivityResultContracts.OpenDocument(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if (result != null) {
                    ImageView imageView = new ImageView(root.getContext());
                    imageView.setImageURI(result);
                    imageView.setLayoutParams(getLayoutParams());
                    revenueGrid.addView(imageView);
                }
            }
        });
        revenueUploadButton.setOnClickListener(view -> {
            chooseRevenuePicture.launch(new String[] {"image/*"});
        });
    }

    private void setUpIncomeUploadButton() {
        chooseIncomePicture = registerForActivityResult(new ActivityResultContracts.OpenDocument(), new ActivityResultCallback<Uri>() {
            @Override
            public void onActivityResult(Uri result) {
                if (result != null) {
                    ImageView imageView = new ImageView(root.getContext());
                    imageView.setImageURI(result);
                    imageView.setLayoutParams(getLayoutParams());
                    incomeGrid.addView(imageView);
                }
            }
        });
        incomeUploadButton.setOnClickListener(view -> {
            chooseIncomePicture.launch(new String[] {"image/*"});
        });
    }
}
