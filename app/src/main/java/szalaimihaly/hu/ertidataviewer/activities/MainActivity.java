package szalaimihaly.hu.ertidataviewer.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.os.Bundle;
import android.content.Intent;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import szalaimihaly.hu.ertidataviewer.asynctasks.SensorPlaceListTask;
import szalaimihaly.hu.ertidataviewer.dataloader.ErtiDataViewer;
import szalaimihaly.hu.ertidataviewer.dataloader.DataLoader;
import szalaimihaly.hu.ertidataviewer.R;
import szalaimihaly.hu.ertidataviewer.entities.SensorPlace;

public class MainActivity extends Activity {

    private Context context = this;
    private ArrayList<SensorPlace> sensorPlaces = null;


    @Override
    protected void onResume() {
        super.onResume();
        ErtiDataViewer.changeDataLoader();
    }

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button trapPlacesButton = (Button) findViewById(R.id.trapPlacesButton);
        Button meteorologicalPlacesButton = (Button) findViewById(R.id.meteorologicalPlasesButton);
        if (!ErtiDataViewer.isOnline()) {
            final AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(R.string.internetconnect);
            builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                }
            });
            builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }
        trapPlacesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                SensorPlaceListTask listTask = new SensorPlaceListTask();
                try {
                    sensorPlaces = (ArrayList) listTask.execute(DataLoader.TRAPPLACE).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                if(sensorPlaces!=null) {
                    intent.putExtra("type",DataLoader.TRAPPLACE);
                    intent.putExtra("sensorplaces", sensorPlaces);
                    intent.setClass(MainActivity.this, SensorPlaceMapsActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(),R.string.internetfail,Toast.LENGTH_LONG).show();
                }
            }
        });
        meteorologicalPlacesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                SensorPlaceListTask listTask = new SensorPlaceListTask();
                try {
                    sensorPlaces = (ArrayList) listTask.execute(DataLoader.METEOROLOGICALPLACE).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }
                if(sensorPlaces!=null) {
                    intent.putExtra("type",DataLoader.METEOROLOGICALPLACE);
                    intent.putExtra("sensorplaces", sensorPlaces);
                    intent.setClass(MainActivity.this, SensorPlaceMapsActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(getApplicationContext(),R.string.internetfail,Toast.LENGTH_LONG).show();
                }
            }
        });


        Button internetButton = (Button) findViewById(R.id.internetButton);
        internetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(R.string.internetconnect);
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
                    }
                });
                builder.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
        Button languageButton = (Button) findViewById(R.id.languageButton);
        languageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(context);
                builder.setTitle(R.string.chooselanguage);
                String options[] = new String[]{"Magyar", "English", "Deutch"};
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        if (which == 0) {
                            Configuration configuration = getResources().getConfiguration();
                            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
                            configuration.locale = new Locale("hu");
                            getResources().updateConfiguration(configuration, displayMetrics);
                            onCreate(getIntent().getExtras());
                        }
                        if (which == 1) {
                            Configuration configuration = getResources().getConfiguration();
                            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
                            configuration.locale = new Locale("en");
                            getResources().updateConfiguration(configuration, displayMetrics);
                            onCreate(getIntent().getExtras());
                        }
                        if (which == 2) {
                            Configuration configuration = getResources().getConfiguration();
                            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
                            configuration.locale = new Locale("de");
                            getResources().updateConfiguration(configuration, displayMetrics);
                            onCreate(getIntent().getExtras());
                        }
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }


}
