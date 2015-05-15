package be.unamur.projetLabo.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;

import java.util.Calendar;

/**
 * Created by Quentin on 14-05-15.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    OnDatePickerSetListener mCallback;
    String title;
    public DatePickerFragment(String title) {
        this.title = title;
    }
    public DatePickerFragment() {
        this.title = null;
    }

    // Container Activity must implement this interface
    public interface OnDatePickerSetListener {
        public void onDatePickerSet(int year, int monthOfYear, int dayOfMonth);
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // Vérifier que la fonction callback est implémenté dans l'activité parente
        try {
            mCallback = (OnDatePickerSetListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " you must implement OnDatePickerSetListener");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(getActivity(), this, year, month, day);
        if(title != null) { dialog.setTitle(title + "\n"); }
        return dialog;
    }

    @Override
    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
        //appeler le fonction de callback lorsque la date est choisie
        mCallback.onDatePickerSet(year, monthOfYear, dayOfMonth);
    }

}