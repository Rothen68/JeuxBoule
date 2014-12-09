package com.stephane.rothen.jeuxboule;

import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import android.hardware.SensorEventListener;

import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends ActionBarActivity implements SensorEventListener {

    private SensorManager sensorManager;
    private com.stephane.rothen.jeuxboule.GameView gv;
    private Timer timer;
    private TimerTask timerTask;
    private double vecteurAcc[]={0.0,0.0};
    private boolean dialogGameOver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager=(SensorManager) this.getSystemService(SENSOR_SERVICE);
        gv = (com.stephane.rothen.jeuxboule.GameView) findViewById(R.id.gameView1);
        gv.ajouterActeurAnime(500,250,R.drawable.bille,1,1,0.0,0.04);
        setTimer();

        dialogGameOver=false;


    }

    private void setTimer()
    {
        timer = new Timer();
        timerTask = new TimerTask()
        {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        gv.onTimeOut();
                    }
                });
            }
        };
        timer.schedule(timerTask,500,10);
    }
    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
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

    @Override
    public void onSensorChanged(SensorEvent event) {
        vecteurAcc[0]= event.values[0];
        vecteurAcc[1]=event.values[1];
        gv.setVecteurAcc(vecteurAcc);
        Log.d("test", "dans onSensorChanged gv.getGameOver :" + String.valueOf(gv.getGameOver()) + " dialogGameOver :" + String.valueOf(dialogGameOver) );
        if (gv.getGameOver()&&!dialogGameOver)
        {
            sensorManager.unregisterListener(this);
            timer.cancel();
            afficheGameOver();

        }
    }

    private void afficheGameOver()
    {
        Intent i = new Intent(this,com.stephane.rothen.jeuxboule.GameOverActivity.class);
        startActivityForResult(i, 1);
        dialogGameOver=true;

        Log.d("test", "dans afficheGameOver");
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onStop() {
        super.onStop();

        gv.setQuitter(true);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==1)
        {

            if (resultCode==RESULT_OK)
            {
                String r = data.getStringExtra(GameOverActivity.EXTRA_CHOIX);
                if(r.equals("oui"))
                {
                    // si l'utilisateur veux rejouer
                    gv.resetJeux();
                    dialogGameOver=false;
                    setTimer();
                }
                else if (r.equals("non"))
                {
                    gv.setQuitter(true);
                    finish();
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data);

    }
}
