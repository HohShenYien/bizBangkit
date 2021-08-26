package com.perajuritTeknologi.bizbangkit.ui.business;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.perajuritTeknologi.bizbangkit.DataConversion;
import com.perajuritTeknologi.bizbangkit.R;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class BusinessNewBusinessDetailsFragment extends Fragment {
    private View root;
    private ImageView backButton, businessLicenseImage;
    private Button nextButton;
    private MaterialButton pickDate, uploadBusinessLicense;
    public static TextView showDate;
    private TextView businessLicenseFixedText;
    private EditText principlePlace, branchAddress, nric1, nric2, nric3, valuation, businessLicense;
    private FloatingActionButton addBranchBusiness, addPartnerNRIC;
    private static int branchBusinessNum = 1, partnerNRICNum = 1;
    private static ArrayList<Integer> branchBusinessLayoutList = new ArrayList<>();
    private static ArrayList<Integer> branchBusinessTrashCanList = new ArrayList<>();
    private static ArrayList<Integer> partnerNRICLayoutList = new ArrayList<>();
    private static ArrayList<Integer> partnerNRICTrashCanList = new ArrayList<>();
    private ArrayAdapter<CharSequence> adapter;
    private Spinner businessTypeSpinner;
    private TextInputLayout businessLicenseTextLayout;
    private ActivityResultLauncher<String[]> choosePicture;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        root = inflater.inflate(R.layout.fragment_business_new_business_details, container, false);

        setUpComponents();
        setUpBusinessLicense();
        setUpFragmentManager();
        onPickDateClicked();
        onAddBranchBusinessClicked();
        onAddPartnerNRICClicked();
        onBusinessTypeClicked();
        onBackButtonClicked();
        onNextButtonClicked();

        return root;
    }

    private void setUpComponents() {
        backButton = root.findViewById(R.id.businessNewBusinessDetailsBackButton);
        nextButton = root.findViewById(R.id.businessNewBusinessDetailsNextButton);
        pickDate = root.findViewById(R.id.registerCommencementDateButton);
        showDate = root.findViewById(R.id.registerCommencementDateText);
        principlePlace = root.findViewById(R.id.editTextRegisterPrincipalPlace);
        branchAddress = root.findViewById(R.id.editTextRegisterBranchAddress);
        nric1 = root.findViewById(R.id.editTextRegisterPartnerNRIC1);
        nric2 = root.findViewById(R.id.editTextRegisterPartnerNRIC2);
        nric3 = root.findViewById(R.id.editTextRegisterPartnerNRIC3);
        addBranchBusiness = root.findViewById(R.id.registerAddBranchAddress);
        addPartnerNRIC = root.findViewById(R.id.registerAddPartnerNric);
        businessTypeSpinner = root.findViewById(R.id.registerBusinessCarriedOut);
        valuation = root.findViewById(R.id.editTextRegisterBusinessValuation);
        businessLicenseFixedText = root.findViewById(R.id.uselessBusinessLicense);
        businessLicense = root.findViewById(R.id.editTextRegisterBusinessLicense);
        businessLicenseTextLayout = root.findViewById(R.id.registerBusinessLicense);
        uploadBusinessLicense = root.findViewById(R.id.registerBusinessLicenseButton);
        businessLicenseImage = root.findViewById(R.id.registerBusinessLicenseImageView);

        toNextTextField(nric1, nric2, nric3);

        adapter = ArrayAdapter.createFromResource(root.getContext(), R.array.business_type_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        businessTypeSpinner.setAdapter(adapter);
    }

    private void onBackButtonClicked() {
        backButton.setOnClickListener(view -> {
            fragmentManager.popBackStack();
            NewBusinessActivity.progressIndicatorChanges(3,2);
            NewBusinessActivity.currentFragmentPos = 2;
        });
    }

    private void setUpFragmentManager() {
        fragmentManager = getParentFragmentManager();
    }

    private void onPickDateClicked() {
        pickDate.setOnClickListener(view -> {
            DialogFragment newFragment = new DatePickerFragment();
            newFragment.show(fragmentManager, "datePicker");
        });
    }

    public static void showDateAfterClicked(int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String selectedDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        showDate.setText(selectedDate);
    }

    private void appendView(int containerLayoutId, int linearLayoutId, int currentNumId, int currentNum, int currentTrashId, int type, ArrayList<Integer> layoutList, ArrayList<Integer> trashCanList) {
        View view = getLayoutInflater().inflate(linearLayoutId, null);
        LinearLayout containerLayout = root.findViewById(containerLayoutId);

        currentNum++;
        TextView num = view.findViewById(currentNumId);
        num.setText((String.format("%d", currentNum) + ". "));

        view.setId(View.generateViewId());
        layoutList.add(view.getId());

        ImageView trashCan = view.findViewById(currentTrashId);
        trashCan.setId(View.generateViewId());
        trashCanList.add(trashCan.getId());

        int finalCurrentNum = currentNum;
        int layoutListSize = layoutList.size();
        int trashCanListSize = trashCanList.size();

        if (currentNum != 2) {
            LinearLayout tmp = containerLayout.findViewById(layoutList.get(layoutListSize - 2));
            ImageView prevTrashcan = tmp.findViewById(trashCanList.get(trashCanListSize - 2));
            prevTrashcan.setVisibility(View.GONE);
        }

        if (type == 2) {
            EditText nric1 = view.findViewById(R.id.editTextRegisterPartnerNRIC1);
            EditText nric2 = view.findViewById(R.id.editTextRegisterPartnerNRIC2);
            EditText nric3 = view.findViewById(R.id.editTextRegisterPartnerNRIC3);
            toNextTextField(nric1, nric2, nric3);
        }

        trashCan.setOnClickListener(view1 -> {
            containerLayout.removeView(view);
            if (finalCurrentNum != 2) {
                LinearLayout tmp = containerLayout.findViewById(layoutList.get(layoutListSize - 2));
                ImageView prevTrashcan = tmp.findViewById(trashCanList.get(trashCanListSize - 2));
                prevTrashcan.setVisibility(View.VISIBLE);
            }
            layoutList.remove(layoutListSize - 1);
            trashCanList.remove(trashCanListSize - 1);

            if (type == 1)
                branchBusinessNum--;
            else
                partnerNRICNum--;
        });

        containerLayout.addView(view);
    }

    private void onAddBranchBusinessClicked() {
        addBranchBusiness.setOnClickListener(view -> {
            appendView(R.id.businessBranchAddressLinearLayout,
                    R.layout.branch_business_text_field,
                    R.id.registerBranchAddressNum,
                    branchBusinessNum,
                    R.id.registerBranchAddressTrashCan,
                    1,
                    branchBusinessLayoutList,
                    branchBusinessTrashCanList);
            branchBusinessNum++;
        });
    }

    private void onAddPartnerNRICClicked() {
        addPartnerNRIC.setOnClickListener(view -> {
            appendView(R.id.businessPartnerNricLinearLayout,
                    R.layout.partner_nric_text_field,
                    R.id.registerPartnerNRICNum,
                    partnerNRICNum,
                    R.id.registerPartnerNRICTrashCan,
                    2,
                    partnerNRICLayoutList,
                    partnerNRICTrashCanList);
            partnerNRICNum++;
        });
    }

    private void toNextTextField(EditText nric1, EditText nric2, EditText nric3) {
        nric1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (nric1.getText().length() == 6) {
                    nric2.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });

        nric2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (nric2.getText().length() == 2) {
                    nric3.requestFocus();
                }
            }

            @Override
            public void afterTextChanged(Editable editable) { }
        });
    }

    private void onBusinessTypeClicked() {
        businessTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
                NewBusinessActivity.businessProfileDetails.businessType = adapterView.getItemAtPosition(pos).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) { }
        });
    }

    private void setUpBusinessLicense() {
        if (NewBusinessActivity.businessProfileDetails.type.equals("Existing")) {
            choosePicture = registerForActivityResult(new ActivityResultContracts.OpenDocument(), new ActivityResultCallback<Uri>() {
                @Override
                public void onActivityResult(Uri result) {
                    if (result != null) {
                        businessLicenseImage.setImageURI(result);
                    }
                }
            });
            uploadBusinessLicense.setOnClickListener(view -> {
                choosePicture.launch(new String[]{"image/*"});
            });
        }
        else {      //NewBusinessActivity.businessProfileDetails.type.equals("New")
            businessLicenseFixedText.setVisibility(View.GONE);
            businessLicenseTextLayout.setVisibility(View.GONE);
            uploadBusinessLicense.setVisibility(View.GONE);
            businessLicenseImage.setVisibility(View.GONE);
        }
    }

    private void setUpFragment() {
        fragmentTransaction = fragmentManager.beginTransaction();
        Fragment fragment = new BusinessNewBusinessProposalFragment();
        fragmentTransaction.setCustomAnimations(R.anim.slide_in_from_right, R.anim.slide_out_to_left, R.anim.slide_in_from_left, R.anim.slide_out_to_right)
                .replace(R.id.businessNewBusinessFragmentContainer, fragment)
                .setReorderingAllowed(true)
                .addToBackStack(null)
                .commit();
        NewBusinessActivity.progressIndicatorChanges(3, 4);
        NewBusinessActivity.currentFragmentPos = 4;
    }

    private void onNextButtonClicked() {
        nextButton.setOnClickListener(view -> {
            NewBusinessActivity.businessProfileDetails.principalAddress = principlePlace.getText().toString();
            NewBusinessActivity.businessProfileDetails.branchAddress = branchAddress.getText().toString();
            NewBusinessActivity.businessProfileDetails.partnerNric = nric1.getText().toString() + nric2.getText().toString() + nric3.getText().toString();
            NewBusinessActivity.businessProfileDetails.valuation = valuation.getText().toString();
            if (NewBusinessActivity.businessProfileDetails.type.equals("Existing")) {
                NewBusinessActivity.businessProfileDetails.licenseNumber = businessLicense.getText().toString();
            }
            setUpFragment();
        });
    }

}