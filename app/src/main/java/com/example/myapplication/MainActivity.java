package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity {

    private SensorManager sysmanager;
    private Sensor sensor;
    private ImageView img;

    private TextView txt;
    private TextView txt1;
    private TextView txt2;
    private SensorEventListener sv;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Ссылки на объект
        txt = findViewById(R.id.txt);
        txt1 = findViewById(R.id.txt1);
        txt2 = findViewById(R.id.txt2);
        img = findViewById(R.id.img);

        //Обращение к системному вызову с проверкой
        sysmanager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if(sysmanager != null)
            sensor = sysmanager.getDefaultSensor(Sensor.TYPE_ROTATION_VECTOR);
        sv = new SensorEventListener() {

            @Override
            public void onSensorChanged(SensorEvent event) {
                float[] rotationMatrix = new float[16];
                SensorManager.getRotationMatrixFromVector(
                        rotationMatrix, event.values);
                float[] remappedRotationMatrix = new float[16];
                SensorManager.remapCoordinateSystem(rotationMatrix,
                        SensorManager.AXIS_X,
                        SensorManager.AXIS_Z,
                        remappedRotationMatrix);
                float[] orientations = new float[3];

                SensorManager.getOrientation(remappedRotationMatrix, orientations);

                for(int i = 0; i < 3; i++) {
                    orientations[i] = (float) (Math.toDegrees(orientations[i]));

                }
                int y = (int)orientations[2] * -1;
                int r = (int)orientations[1] * -1;
                int g = (int)orientations[0] * -1;

                //Передаем данные координат
                txt.setText("Z-Rot:  " + String.valueOf(y));
                txt1.setText("X-Rot:  " +String.valueOf(r));
                txt2.setText("Y-Rot:  " +String.valueOf(g));
                //Меняем положение полосы
                img.setRotation(-orientations[2]);
                //  img1.setRotation(-orientations[1]);
                //Передаем данные координат

            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {}


        };

    }


    @Override
    protected void onResume(){
        super.onResume();
        sysmanager.registerListener(sv,sensor,SensorManager.SENSOR_DELAY_FASTEST);

    }
    @Override
    protected void onPause() {
        super.onPause();
        sysmanager.unregisterListener(sv);

    }
}