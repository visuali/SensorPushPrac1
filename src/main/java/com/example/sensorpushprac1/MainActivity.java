package com.example.sensorpushprac1;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.annotation.SuppressLint;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.webkit.WebView;
import android.webkit.JavascriptInterface;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager mgr;
    private Sensor light;
    private WebView wv;
    private JSInterface jsInterface=new JSInterface();

    @SuppressLint({"AddJavascriptInterface", "SetJavaScriptEnabled"})

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mgr=(SensorManager)getSystemService(Context.SENSOR_SERVICE);
        light=mgr.getDefaultSensor(Sensor.TYPE_LIGHT);

        wv=(WebView)findViewById(R.id.webkit);

        wv.getSettings().setJavaScriptEnabled(true);
        wv.addJavascriptInterface(jsInterface, "LIGHT_SENSOR");
        wv.loadUrl("file:///android_asset/index.html");
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        jsInterface.updateLux(event.values[0]);// “private”
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
    private class JSInterface {
        float lux=0.0f;
        private void updateLux(float lux) {
            this.lux=lux;
        }
        @JavascriptInterface
        public String getLux() {
            return(String.format(Locale.US, "{\"lux\": %f}", lux));
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        mgr.registerListener(this, light, SensorManager.SENSOR_DELAY_UI);
    }
    @Override
    protected void onStop() {
        mgr.unregisterListener(this);
        super.onStop();
    }
}
