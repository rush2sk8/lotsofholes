package com.example.lotsofholes;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener{

    private SensorManager sensorManager;
    private Sensor linAcc, acc;
    private TextView linAccTv , accTv;


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


    @Override
    protected void onPause() {
	sensorManager.unregisterListener(this);

	super.onPause();
    }

    @Override
    protected void onResume() {
	sensorManager.registerListener(this, acc, SensorManager.SENSOR_DELAY_NORMAL);
	sensorManager.registerListener(this, linAcc, SensorManager.SENSOR_DELAY_NORMAL);
	super.onResume();
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
    
}
