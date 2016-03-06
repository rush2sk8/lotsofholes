package com.example.lotsofholes;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;

public class MainActivity extends Activity implements SensorEventListener, LocationListener{

    private SensorManager sensorManager;
    private LocationManager locationManager;
    private Sensor linAcc, acc;
    private TextView linAccTv , accTv;
    private double lon , lat;
    private Button submitToServer;
    private Firebase fbref; 

    @Override
    protected void onCreate(Bundle savedInstanceState) {
	super.onCreate(savedInstanceState);
	setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	setContentView(R.layout.activity_main);
	Firebase.setAndroidContext(this);
	fbref = new Firebase("https://lots-of-holes.firebaseio.com/");

	linAccTv = (TextView)findViewById(R.id.linacc);
	accTv = (TextView)findViewById(R.id.acc);

	sensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
	acc = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
	linAcc = sensorManager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);

	locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
	boolean enabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

	if (!enabled) 
	    startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));

	refreshlocation();

	submitToServer = (Button)findViewById(R.id.write);

	submitToServer.setOnClickListener(new OnClickListener() {

	    @Override
	    public void onClick(View v) {

		Firebase subRef = fbref.child("locs");
		subRef.push().setValue(new FirebaseLocationUnit(""+lon, ""+lat));
		refreshlocation();
	    }
	});


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
	lat = (location.getLatitude());
	lon = (location.getLongitude());
	Toast.makeText(getApplicationContext(), lat+" "+lon, Toast.LENGTH_LONG).show();
	System.out.println(lat+" "+lon);

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

	refreshlocation();
    }
  
    private void refreshlocation() {
	if(locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER))
	    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,50,1.0f, this);

	else if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
	    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,50,1.0f, this);

	else if(locationManager.isProviderEnabled(LocationManager.PASSIVE_PROVIDER))
	    locationManager.requestLocationUpdates(LocationManager.PASSIVE_PROVIDER,50,1.0f, this);

	else
	    System.out.println("Could not acquire location at all. Turn on yer damn GPS");

    }

}
