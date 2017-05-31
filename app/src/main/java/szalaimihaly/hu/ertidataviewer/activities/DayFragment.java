package szalaimihaly.hu.ertidataviewer.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import szalaimihaly.hu.ertidataviewer.R;

/**
 * Created by Mihaly on 2016.03.26..
 */
public class DayFragment extends Fragment {

    static final String TAG = "NORMAL_DAY_FRAGMEMT";

    private RadioGroup radioGroup;
    private Button chartButton;

    private EditText editTextBegin;
    private EditText editTextEnd;

    private DatePicker datePickerBegin;
    private DatePicker datePickerEnd;

    private NumberPicker numberPicker;


    private SelectTrapPlaceDayActivity activity;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.day_fragment, null);
        if (view.findViewById(R.id.editTextBeginYear) != null) {
            editTextBegin = (EditText) view.findViewById(R.id.editTextBeginYear);
            editTextBegin.setText(activity.getBeginyear() + "." + activity.getBeginmonth() + "." + activity.getBeginday());
            final DatePickerDialog.OnDateSetListener datePickerDialogBegin = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    monthOfYear++;
                    activity.setBeginyear(year);
                    activity.setBeginmonth(monthOfYear);
                    activity.setBeginday(dayOfMonth);
                    editTextBegin.setText(activity.getBeginyear() + "." + activity.getBeginmonth() + "." + activity.getBeginday());
                }
            };
            editTextBegin.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    InputMethodManager methodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    methodManager.hideSoftInputFromWindow(editTextBegin.getWindowToken(), 0);
                    new DatePickerDialog(getActivity(), datePickerDialogBegin, 1995, 2, 1).show();
                }
            });
        }
        if(view.findViewById(R.id.editTextBeginYear)!=null) {
            editTextEnd = (EditText) view.findViewById(R.id.editTextEndYear);
            editTextEnd.setText(activity.getEndyear() + "." + activity.getEndmonth() + "." + activity.getEndday());
            final DatePickerDialog.OnDateSetListener datePickerDialogEnd = new DatePickerDialog.OnDateSetListener() {
                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    monthOfYear++;
                    activity.setEndyear(year);
                    activity.setEndmonth(monthOfYear);
                    activity.setEndday(dayOfMonth);
                    editTextEnd.setText(activity.getEndyear() + "." + activity.getEndmonth() + "." + activity.getEndday());
                }
            };
            editTextEnd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    InputMethodManager methodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                    methodManager.hideSoftInputFromWindow(editTextEnd.getWindowToken(), 0);
                    new DatePickerDialog(getActivity(), datePickerDialogEnd, 1995, 3, 1).show();
                }
            });
        }
        if(view.findViewById(R.id.datePickerBegin)!=null){
            datePickerBegin = (DatePicker) view.findViewById(R.id.datePickerBegin);
            datePickerBegin.init(activity.getBeginyear(), activity.getBeginmonth()-1, activity.getBeginday(), new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker datePicker, int year, int month, int day) {
                    activity.setBeginyear(year);
                    activity.setBeginmonth(month+1);
                    activity.setBeginday(day);
                }
            });
        }
        if(view.findViewById(R.id.datePickerEnd)!=null){
            datePickerEnd = (DatePicker) view.findViewById(R.id.datePickerEnd);
            datePickerEnd.init(activity.getEndyear(), activity.getEndmonth()-1, activity.getEndday(), new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker datePicker, int year, int month, int day) {
                    activity.setEndyear(year);
                    activity.setEndmonth(month+1);
                    activity.setEndyear(day);
                }
            });
        }
        numberPicker = (NumberPicker) view.findViewById(R.id.numberPicker);
        numberPicker.setEnabled(true);
        numberPicker.setMinValue(0);
        numberPicker.setMaxValue(100);
        numberPicker.setWrapSelectorWheel(false);
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                activity.setLimit(newVal);
            }
        });
        radioGroup = (RadioGroup) view.findViewById(R.id.radioGroup);
        final RadioButton radioButton = (RadioButton) view.findViewById(R.id.radioButton);
        radioButton.toggle();
        final RadioButton radioButton1 = (RadioButton) view.findViewById(R.id.radioButton1);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == radioButton.getId()) {
                    activity.setGraficontype(true);
                }
                if (checkedId == radioButton1.getId()) {
                    activity.setGraficontype(false);
                }
            }
        });
        chartButton = (Button) view.findViewById(R.id.chartButton);
        chartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.makeDiagram("bar", SelectTrapPlaceAbstractActivity.DAY);
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        this.activity = (SelectTrapPlaceDayActivity) context;
        super.onAttach(context);

    }


}
