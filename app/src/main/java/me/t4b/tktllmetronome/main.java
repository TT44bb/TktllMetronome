package me.t4b.tktllmetronome;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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


    /**
     * Updates the UI with new values
     */
    private void updateUI(){
        TextView field = (TextView)findViewById(R.id.bpmValue);
        field.setText(String.valueOf((int)(Tktll.getPatternsPerSecond()*60+0.5)));
        field = (TextView) findViewById(R.id.tempoName);
        field.setText(classicalTempos.getName(this, (int) (Tktll.getPatternsPerSecond() * 60 + 0.5)));
    }

    /**
     * Handles speed changes
     * @param diff
     */
    public void incr(int diff){
        Tktll.incr(diff);
        updateUI();
    }


    //UI callbacks

    public void toggleTktll(View v){
        Tktll.toggle();
    }

    public void incr(View v){
        incr(1);
    }

    public void incrL(View v){
        incr(10);
    }

    public void decr(View v){
        incr(-1);
    }

    public void decrL(View v){
        incr(-10);
    }

    public void tapTempo(View v){
        Tktll.tap();
    }

}


/**
 * Class containing the tempo names, separated because maybe I could support other (or no) names in the future
 */
class tempoName {
    int[][] tempoNames;
    public tempoName(){
        tempoNames = new int[8][2];
        tempoNames[0] = new int[]{0, R.string.largo};
        tempoNames[1] = new int[]{60, R.string.larghetto};
        tempoNames[2] = new int[]{66, R.string.adagio};
        tempoNames[3] = new int[]{76, R.string.andante};
        tempoNames[4] = new int[]{108, R.string.moderato};
        tempoNames[5] = new int[]{120, R.string.allegro};
        tempoNames[6] = new int[]{168, R.string.presto};
        tempoNames[7] = new int[]{200, R.string.prestissimo};
    }

    /**
     * Returns the matching tempo name
     * @param c Context
     * @param bpm beats per minute
     * @return tempo name, empty string when none matches (would be strange)
     */
    public String getName(Context c, int bpm){
        for (int i = tempoNames.length-1; i>=0; i--) {
            if (bpm >= tempoNames[i][0]) {
                return (c.getString(tempoNames[i][1]));
            }
        }
        return("");
    }
}