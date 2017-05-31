package szalaimihaly.hu.ertidataviewer.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import szalaimihaly.hu.ertidataviewer.R;
import szalaimihaly.hu.ertidataviewer.asynctasks.SpeciesListTask;
import szalaimihaly.hu.ertidataviewer.dataloader.DataLoader;
import szalaimihaly.hu.ertidataviewer.entities.MeteorologicalPlace;
import szalaimihaly.hu.ertidataviewer.entities.SensorPlace;
import szalaimihaly.hu.ertidataviewer.entities.TrapPlace;

public class SensorPlaceMapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private GoogleMap mMap;
    private ArrayList<SensorPlace> sensorPlaces = null;
    private int type;
    private Context context = this;
    private String[] speciesArray = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_palces_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.trapplacemap);
        mapFragment.getMapAsync(this);
        Intent intent = getIntent();
        sensorPlaces = (ArrayList<SensorPlace>) intent.getSerializableExtra("sensorplaces");
        type = intent.getIntExtra("type",0);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setOnMarkerClickListener(this);
        for (SensorPlace sensorPlace : sensorPlaces) {
            LatLng latLng = new LatLng(sensorPlace.getLatitude(), sensorPlace.getLongitude());
            MarkerOptions markerOptions = null;
            if (sensorPlace instanceof TrapPlace) {
                markerOptions = new MarkerOptions().position(latLng).title(((TrapPlace) sensorPlace).getCity());
            }
            if (sensorPlace instanceof MeteorologicalPlace) {
                markerOptions = new MarkerOptions().position(latLng);
            }
            mMap.addMarker(markerOptions);
        }
        if ((getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) ==
                Configuration.SCREENLAYOUT_SIZE_SMALL) {
            Log.e("debug", "normal");
            if ((getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)) {
                LatLng latLng = new LatLng(47.0, 19.4);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5f));
            }
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                LatLng latLng = new LatLng(47.0, 19.4);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 4f));
            }
        }
        if ((getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) ==
                Configuration.SCREENLAYOUT_SIZE_NORMAL) {
            Log.e("debug", "normal");
            if ((getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)) {
                LatLng latLng = new LatLng(47.0, 19.4);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 6f));
            }
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                LatLng latLng = new LatLng(47.0, 19.4);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 5f));
            }
        }
        if ((getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK) >=
                Configuration.SCREENLAYOUT_SIZE_LARGE) {
            Log.e("debug", "large");
            if ((getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)) {
                LatLng latLng = new LatLng(47.0, 19.4);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 7f));
            }
            if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                LatLng latLng = new LatLng(47.0, 19.4);
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 6f));
            }

        }
    }


    @Override
    public boolean onMarkerClick(final Marker marker) {
        if (type == DataLoader.TRAPPLACE) {
            Toast.makeText(getApplicationContext(), marker.getTitle(), Toast.LENGTH_LONG).show();
            String trapplacecity = marker.getTitle();
            SpeciesListTask task = new SpeciesListTask();
            try {
                speciesArray = task.execute(trapplacecity).get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
            String daytrap = getResources().getString(R.string.daytrap);
            String monthtrap = getResources().getString(R.string.monthtrap);
            String[] options = {daytrap, monthtrap};
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle(R.string.chooseaggregationtype);
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (which == 0) {
                        if (speciesArray == null) {
                            Toast.makeText(getApplicationContext(), R.string.internetfail, Toast.LENGTH_LONG).show();
                            return;
                        }
                        if (speciesArray.length == 0) {
                            Toast.makeText(getApplicationContext(), R.string.trapfail, Toast.LENGTH_LONG).show();
                            return;
                        }
                        if (speciesArray != null && speciesArray.length != 0) {
                            Intent intent = new Intent();
                            intent.putExtra("trapplacecity", marker.getTitle());
                            intent.putExtra("speciesarray", speciesArray);
                            intent.setClass(context, SelectTrapPlaceDayActivity.class);
                            startActivity(intent);
                        }
                    }
                    if (which == 1) {
                        if (speciesArray == null) {
                            Toast.makeText(getApplicationContext(), R.string.internetfail, Toast.LENGTH_LONG).show();
                            return;
                        }
                        if (speciesArray.length == 0) {
                            Toast.makeText(getApplicationContext(), R.string.trapfail, Toast.LENGTH_LONG).show();
                            return;
                        }
                        if (speciesArray != null && speciesArray.length != 0) {
                            Intent intent = new Intent();
                            intent.putExtra("trapplacecity", marker.getTitle());
                            intent.putExtra("speciesarray", speciesArray);
                            intent.setClass(context, SelectTrapPlaceMonthActivity.class);
                            startActivity(intent);
                        }
                    }
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }

        return false;
    }


}