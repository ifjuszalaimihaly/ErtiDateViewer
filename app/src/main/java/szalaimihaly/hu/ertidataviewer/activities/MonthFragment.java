package szalaimihaly.hu.ertidataviewer.activities;

import android.support.v4.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import szalaimihaly.hu.ertidataviewer.R;

/**
 * Created by szala on 2016. 04. 09..
 */
public class MonthFragment extends Fragment {
    static final String TAG = "NORMAL_MONTH_FRAGMEMT";

    SelectTrapPlaceMonthActivity activity;

    protected RadioGroup radioGroup;
    protected Button chartButton;

    protected EditText editTextBeginYear;
    protected EditText editTextEndYear;

    protected EditText editTextBeginMonth;
    protected EditText editTextEndMonth;

    private DatePicker datePickerBegin;
    private DatePicker datePickerEnd;

    private int beginYear=1995;
    private int beginMonth=3;

    private int endYear=1995;
    private int endMonth=4;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) { // hibakezelésen még dolgozni kell
        View view = inflater.inflate(R.layout.month_fragment, null);
        boolean phoneView=true;
        if(view.findViewById(R.id.editTextBeginYear)!=null){
            editTextBeginYear = (EditText) view.findViewById(R.id.editTextBeginYear);
            editTextBeginYear.setText(((Integer) activity.getBeginyear()).toString());
        }
        if(view.findViewById(R.id.editTextBeginMonth)!=null){
            editTextBeginMonth = (EditText) view.findViewById(R.id.editTextBeginMonth);
            editTextBeginMonth.setText(((Integer)activity.getBeginmonth()).toString());
        }
        if(view.findViewById(R.id.editTextEndYear)!=null){
            editTextEndYear = (EditText) view.findViewById(R.id.editTextEndYear);
            editTextEndYear.setText(((Integer) activity.getEndyear()).toString());
        }
        if(view.findViewById(R.id.editTextEndMonth)!=null){
            editTextEndMonth = (EditText) view.findViewById(R.id.editTextEndMonth);
            editTextEndMonth.setText(((Integer) activity.getEndmonth()).toString());
        }
        if(view.findViewById(R.id.datePickerBegin)!=null){
            phoneView = false;
            datePickerBegin = (DatePicker) view.findViewById(R.id.datePickerBegin);
            datePickerBegin.setCalendarViewShown(false);
            LinearLayout pickerParentLayout = (LinearLayout) datePickerBegin.getChildAt(0);
            LinearLayout pickerSpinnersHolder = (LinearLayout) pickerParentLayout.getChildAt(0);
            NumberPicker picker = (NumberPicker) pickerSpinnersHolder.getChildAt(2);
            picker.setVisibility(View.GONE);
            datePickerBegin.init(activity.getBeginyear(), activity.getBeginmonth(), 0, new DatePicker.OnDateChangedListener() {
                        @Override
                        public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            activity.setBeginyear(year);
                            activity.setBeginmonth(monthOfYear + 1);
                        }
                    }

            );
        }
        if(view.findViewById(R.id.datePickerEnd)!=null){
            datePickerEnd = (DatePicker) view.findViewById(R.id.datePickerEnd);
            datePickerEnd.setCalendarViewShown(false);
            LinearLayout pickerParentLayout1 = (LinearLayout) datePickerEnd.getChildAt(0);
            LinearLayout pickerSpinnersHolder1 = (LinearLayout) pickerParentLayout1.getChildAt(0);
            NumberPicker picker1 = (NumberPicker) pickerSpinnersHolder1.getChildAt(2);
            picker1.setVisibility(View.GONE);
            datePickerEnd.init(activity.getEndyear(), activity.getEndmonth(), 0, new DatePicker.OnDateChangedListener() {
                @Override
                public void onDateChanged(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                    activity.setEndyear(year);
                    activity.setEndmonth(monthOfYear + 1);
                }
            });
        }
        radioGroup = (RadioGroup)view.findViewById(R.id.radioGroup);
        final RadioButton radioButton = (RadioButton)view.findViewById(R.id.radioButton);
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
        if(phoneView) {
            chartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean readyDate = true;
                    try {
                        beginYear = new Integer(editTextBeginYear.getText().toString());
                        beginMonth = new Integer(editTextBeginMonth.getText().toString());
                        endYear = new Integer(editTextEndYear.getText().toString());
                        endMonth = new Integer(editTextEndMonth.getText().toString());
                    } catch (NumberFormatException e) {
                        readyDate = false;
                    }
                    if (beginMonth > 12) {
                        readyDate = false;
                    }
                    if (endMonth > 12) {
                        readyDate = false;
                    }
                    if (readyDate) {
                        activity.setBeginyear(beginYear);
                        activity.setEndyear(endYear);
                        activity.setBeginmonth(beginMonth);
                        activity.setEndmonth(endMonth);
                        activity.makeDiagram("bar", SelectTrapPlaceAbstractActivity.MONTH);
                    } else {
                        Toast.makeText(getContext(), R.string.monthnumberfail, Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            chartButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    activity.makeDiagram("bar",SelectTrapPlaceAbstractActivity.MONTH);
                }
            });
        }
        return view;
    }

    @Override
    public void onAttach(Context context){
        this.activity=(SelectTrapPlaceMonthActivity) context;
        super.onAttach(context);;
    }

}
