package com.perajuritTeknologi.bizbangkit;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.perajuritTeknologi.bizbangkit.ui.business.BusinessNewBusinessDetailsFragment;

import java.util.Calendar;

public class SpinnerDOBPicker extends DialogFragment implements DatePickerDialog.OnDateSetListener {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        return new DatePickerDialog(getActivity(), R.style.MySpinnerDatePickerStyle, this, year, month, day);
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
        RegistrationAccountActivity1.showDOBAfterClicked(year, month, dayOfMonth);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("registerUserP1", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("dob", String.format("%d", year) + "-" + String.format("%02d", month + 1) + "-" + String.format("%d", dayOfMonth));
        editor.apply();
    }
}