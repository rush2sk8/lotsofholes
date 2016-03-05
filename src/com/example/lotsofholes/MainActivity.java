package com.example.lotsofholes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements SensorEventListener, LocationListener{

    private SensorManager sensorManager;
    private LocationManager locationManager;
    private Sensor linAcc, acc;
    private TextView linAccTv , accTv;
    private int lon , lat;
    private String provider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	setContentView(R.layout.activity_main);

	linAccTv = (TextView)findViewById(R.id.linacc);
	accTv = (TextView)findViewById(R.id.acc);

	sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
	acc = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	linAcc = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);

	locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
	boolean enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

	if (!enabled) 
	    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
	
	Criteria criteria = new Criteria();
	provider = locationManager.getBestProvider(criteria, false);
	Location location = locationManager.getLastKnownLocation(provider);

	// Initialize the location fields
	if (location != null) {
	    System.out.println("Provider " + provider + " has been selected.");
	    onLocationChanged(location);
	} else
	    Toast.makeText(getApplicationContext(), "RIP NO PROVIDER", Toast.LENGTH_LONG).show();

    }



    @Override
    public void onSensorChanged(SensorEvent event) {

	if(event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) 
	    filterAccValues(event);

	else if(event.sensor.getType() == Sensor.TYPE_LINEAR_ACCELERATION ) 
	    filterLinAccValues(event);


    }



    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {


    }

    private void filterAccValues(SensorEvent event) {
	float x = event.values[0];
	float y = event.values[1];
	float z = event.values[2];
	accTv.setText("X: "+ x+" Y: "+ y+ " Z: "+ z);

    }

    private void filterLinAccValues(SensorEvent event) {
	float x = event.values[0];
	float y = event.values[1];
	float z = event.values[2];

	linAccTv.setText("X: "+ x+" Y: "+ y+ " Z: "+ z);

    }



    @Override
    public void onLocationChanged(Location location) {
	lat = (int) (location.getLatitude());
	lon = (int) (location.getLongitude());

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {


    }

    @Override
    public void onProviderEnabled(String provider) {
	Toast.makeText(this, "Enabled new provider " + provider,
		Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onProviderDisabled(String provider) {
	Toast.makeText(this, "Disabled provider " + provider,
		Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onPause() {
	super.onPause();
	sensorManager.unregisterListener(this);
	locationManager.removeUpdates(this);

    }

    @Override
    protected void onResume() {
	super.onResume();
	sensorManager.registerListener(this, acc, SensorManager.SENSOR_DELAY_NORMAL);
	sensorManager.registerListener(this, linAcc, SensorManager.SENSOR_DELAY_NORMAL);
	locationManager.requestLocationUpdates(provider, 400, 1, this);
    }
}
