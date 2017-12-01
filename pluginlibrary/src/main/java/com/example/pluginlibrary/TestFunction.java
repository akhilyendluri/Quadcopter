package com.example.pluginlibrary;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.Wearable;
import com.unity3d.player.UnityPlayer;
import com.unity3d.player.UnityPlayerActivity;

import java.util.List;


/**
 * Created by sanch on 11/27/2017.
 */

public class TestFunction extends UnityPlayerActivity implements SensorEventListener {
    private static SensorManager manager = null;
    private static Sensor sensor;
    public float[] sensorData = null;
    public Context context;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
    }

    public void StartColl(Context context){
        /*boolean updated = false;
        manager = (SensorManager) context.getSystemService(Context.SENSOR_SERVICE);
        Sensor sensor = manager.getDefaultSensor(Sensor.TYPE_LINEAR_ACCELERATION);
        if (sensor != null) {
            updated = manager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
        }*/
        ConnectionManager.getInstance(context).sendMessage(new ConnectionManager.ConnectionManagerRunnable(context) {
            @Override
            public void send(GoogleApiClient googleApiClient) {
                List<Node> nodes = Wearable.NodeApi.getConnectedNodes(googleApiClient).await().getNodes();
                for(Node node : nodes) {
                    Wearable.MessageApi.sendMessage(googleApiClient,node.getId(),ClientPaths.START_MEASUREMENT,null);
                }
            }
        });
    }

    public static String startMeasurement(Context context) {
        TestFunction function = new TestFunction();
        function.StartColl(context);
        function.fun();
        return "data";
    }

    private void stopMeasurement() {
        if (manager != null) {
            manager.unregisterListener(this);
        }
    }

    public String fun() {
        int d1 = (int) sensorData[2];
        String val = sensorData[0]+","+sensorData[1]+","+sensorData[2];
        //Log.d("Call Activity", "Combined Data = "+val);
        UnityPlayer.UnitySendMessage("TestCube","Receiver_Message2", val);
        //Log.d("Call Activity", "Data Sent = "+d1);
        return String.valueOf(d1);
    }

    /*@Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }*/

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {
        //Log.d("Call Activity", "Sensor Data .............");
        sensorData = sensorEvent.values;
        fun();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}
