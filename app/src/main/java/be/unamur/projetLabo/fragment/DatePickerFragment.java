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
    private OnDatePickerSetListener mCallback;
    private String title;
    private Calendar cal;
    private int month = -1;
    private int day = -1;

    public DatePickerFragment(String title) {
        this.title = title;
    }
    public DatePickerFragment() {
        this.title = null;
    }
    public DatePickerFragment(Calendar cal) {
        this.cal = cal;
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

        if(cal == null){
            final Calendar cal = Calendar.getInstance();
        }
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

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