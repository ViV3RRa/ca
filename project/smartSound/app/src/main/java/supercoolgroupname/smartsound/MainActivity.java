package supercoolgroupname.smartsound;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.hardware.SensorEventListener;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;

import android.view.View;

import android.widget.CompoundButton;
import android.widget.TextView;

import android.widget.TextView;
import android.widget.ToggleButton;

import android.widget.CompoundButton;


public class MainActivity extends Activity implements Classifier.ClassifierListener, SensorEventListener {


    private SensorManager senSensorManager;
    private Sensor senAccelerometer;

    private SmartSoundMeasurement sm = new SmartSoundMeasurement();


    boolean on = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void clickHandler(View v){
        on = ((ToggleButton) v).isChecked();

        if(on){

            Classifier.registerListener(this);
            senSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
            senAccelerometer = senSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
            senSensorManager.registerListener(this, senAccelerometer , SensorManager.SENSOR_DELAY_NORMAL);
            sm.start();
        } else{
            senSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
            senSensorManager.unregisterListener(this);
            sm.stop();
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor mySensor = event.sensor;

        if(mySensor.getType() == Sensor.TYPE_ACCELEROMETER){
            double x = event.values[0];
            double y = event.values[1];
            double z = event.values[2];
            Classifier.appendReading(x,y,z);
        }

        /*TextView activity = (TextView) findViewById(R.id.activity);
        TextView action = (TextView) findViewById(R.id.action);

        activity.setText("X: " + x + "\nY: " + y + "\nZ: " + z);

        DerivedContext dCon = Classifier.getContext();
        action.setText(dCon.toString());
        ContextMapper.handleContext(dCon, this);*/
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }


    @Override
    public void onContextChange(final DerivedContext new_context) {
        ContextMapper.handleContext(new_context, this);
        final TextView action = (TextView) findViewById(R.id.action);
        action.post(new Runnable() {
            @Override
            public void run() {
                // The bar has an input range of [0.0 ; 1.0] and 10 segments.
                // Each LED corresponds to 6 dB.
                action.setText(new_context.toString());
                Log.i("Status: ", new_context.toString());
            }
        });

    }
}
