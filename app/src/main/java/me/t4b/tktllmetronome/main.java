package me.t4b.tktllmetronome;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class main extends Activity {
    tktll Tktll;
    tempoName classicalTempos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Tktll = new tktll();
        Tktll.init(this);
        classicalTempos = new tempoName();
        updateUI();
    }

/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
*/

    /**
     * Updates the UI with new values
     */
    private void updateUI() {
        TextView field = (TextView) findViewById(R.id.bpmValue);
        field.setText(String.valueOf((int) (Tktll.getPatternsPerSecond() * 60 + 0.5)));
        field = (TextView) findViewById(R.id.tempoName);
        field.setText(classicalTempos.getName(this, (int) (Tktll.getPatternsPerSecond() * 60 + 0.5)));
        field = (TextView) findViewById(R.id.toggleTktll);
        int resID;
        if(Tktll.running) resID=R.string.stopTktll;
        else resID=R.string.startTktll;
        field.setText(resID);
    }

    /**
     * Handles bpm changes
     *
     * @param diff bpm change in beats per minute
     */
    public void incr(int diff) {
        Tktll.incr(diff);
        updateUI();
    }


    //UI callbacks

    /**
     * Toggle button
     * @param v
     */
    public void toggleTktll(View v) {
        Tktll.toggle();
        updateUI();
    }

    /**
     * Increase bpm by 1
     * @param v
     */
    public void incr(View v) {
        incr(1);
    }

    /**
     * Increase bpm by 10
     * @param v
     */
    public void incrL(View v) {
        incr(10);
    }

    /**
     * Increase bpm by 30
     * @param v
     */
    public void incrXL(View v) {
        incr(30);
    }

    /**
     * Decrease bpm by 1
     * @param v
     */
    public void decr(View v) {
        incr(-1);
    }

    /**
     * Decrease bpm by 10
     * @param v
     */
    public void decrL(View v) {
        incr(-10);
    }

    /**
     * Decrease bpm by 30
     * @param v
     */
    public void decrXL(View v) {
        incr(-30);
    }

    /**
     * Tempo tap button
     * @param v
     */
    public void tapTempo(View v) {
        Tktll.tap();
        updateUI();
    }

}


