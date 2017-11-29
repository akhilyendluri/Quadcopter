package com.example.pluginlibrary;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

import com.unity3d.player.UnityPlayer;

/**
 * Created by sanch on 11/27/2017.
 */

public class TestFunction extends Service implements SensorEventListener {
    private static SensorManager manager;
    private static Sensor sensor;
    private float[] sensorData = null;

    @Override
    public void onCreate() {
        super.onCreate();
        startMeasurement();
    }

    
    public void StartColl(){
        //Adapter adapter = new
        boolean updated = false;
        Log.d("Call Activity", "Start measurement 1 .... !!!");
        manager = (SensorManager) getApplicationContext().getSystemService(SEARCH_SERVICE);
        Log.d("Call Activity", "Start measurement 2.... !!!");
        Sensor sensor = manager.getDefaultSensor(Sensor.TYPE_GRAVITY);
        Log.d("Call Activity", "Start measurement 3.... !!!");
        if (sensor != null) {
            Log.d("Call Activity", "Start measurement 4.... !!!");
            updated = manager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
        Log.d("Call Activity", "Start measurement 5.... !!!");
        if(updated) {
            Log.d("Call Activity", "Start measurement Updated Successfully .... !!!");
        } else {
            Log.d("Call Activity", "Start measurement Updated Failure .... !!!");
        }
    }

    public static String startMeasurement() {
        TestFunction function = new TestFunction();
        function.StartColl();
        return "Started";
    }

    private void stopMeasurement() {
        if (manager != null) {
            manager.unregisterListener(this);
        }
    }

    public void fun() {
        Log.d("Call Activity", "Data Sent = "+sensorData.toString());
        UnityPlayer.UnitySendMessage("GvrControllerMain","Receiver_Message", sensorData.toString());
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        Log.d("Call Activity", "Sensor Data .............");
        sensorData = sensorEvent.values;
        fun();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
