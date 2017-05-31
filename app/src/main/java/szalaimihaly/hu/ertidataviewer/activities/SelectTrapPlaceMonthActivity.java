package szalaimihaly.hu.ertidataviewer.activities;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.widget.ArrayAdapter;
import android.widget.MultiAutoCompleteTextView;

import szalaimihaly.hu.ertidataviewer.R;

public class SelectTrapPlaceMonthActivity extends SelectTrapPlaceAbstractActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        trapplacecity = intent.getStringExtra("trapplacecity");
        speciesArray = intent.getStringArrayExtra("speciesarray");
        setContentView(R.layout.activity_select_trap_place_month);
        multiAutoCompleteTextView = (MultiAutoCompleteTextView) findViewById(R.id.multiAutoCompleteTextView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.multi_auto_complete_text_view, speciesArray);
        multiAutoCompleteTextView.setAdapter(adapter);
        multiAutoCompleteTextView.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        MonthFragment monthFragment = (MonthFragment) manager.findFragmentByTag(MonthFragment.TAG);
        if(monthFragment == null){
            monthFragment = new MonthFragment();
        }
        transaction.add(R.id.frame_layout, monthFragment, monthFragment.TAG);
        transaction.commit();
    }



}
