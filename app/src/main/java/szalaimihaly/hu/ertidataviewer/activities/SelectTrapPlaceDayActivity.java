package szalaimihaly.hu.ertidataviewer.activities;


import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.MultiAutoCompleteTextView;
import android.widget.NumberPicker;


import szalaimihaly.hu.ertidataviewer.R;

public class SelectTrapPlaceDayActivity extends SelectTrapPlaceAbstractActivity{

    private NumberPicker numberPicker;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_trap_place_day);
        Intent intent = getIntent();
        trapplacecity = intent.getStringExtra("trapplacecity");
        speciesArray = intent.getStringArrayExtra("speciesarray");
        multiAutoCompleteTextView = (MultiAutoCompleteTextView) findViewById(R.id.multiAutoCompleteTextView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.multi_auto_complete_text_view, speciesArray);
        multiAutoCompleteTextView.setAdapter(adapter);
        multiAutoCompleteTextView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        DayFragment dayFragment = (DayFragment) manager.findFragmentByTag(DayFragment.TAG);
        if (dayFragment == null) {
            dayFragment = new DayFragment();
        }
        transaction.add(R.id.frame_layout, dayFragment, dayFragment.TAG);
        transaction.commit();
    }

}




