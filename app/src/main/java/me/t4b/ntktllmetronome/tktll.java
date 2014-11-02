package me.t4b.ntktllmetronome;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

import java.io.IOException;

/**
 * Created by t4b on 01.11.14.
 */
public class tktll {
    //sound files: 16 bit signed, linear pcm, 48kHz, mono, headerless
    private final int SAMPLE_RATE = 48000; //in Hz
    private final int MAX_PATTERN_LENGTH = 2; //in s
    private final int MAX_PATTERN_FRAMES = MAX_PATTERN_LENGTH*SAMPLE_RATE;

    private final int MAX_PATTERN_SIZE = MAX_PATTERN_FRAMES *2; //bitsPerSample=2
    private int BEEP_SIZE;
    private int BEEP_PATTERNS;
    final double MIN_PATTERN_PER_SECOND = 1d / MAX_PATTERN_LENGTH; // don't use it for internal calculations
    private int PATTERN_FRAMES=SAMPLE_RATE; //60bpm

    //set up audio output
    AudioTrack beeper = new AudioTrack(AudioManager.STREAM_MUSIC, SAMPLE_RATE, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, MAX_PATTERN_SIZE, AudioTrack.MODE_STATIC);
    byte[] beep = new byte[MAX_PATTERN_SIZE];

    public void init(Context c){
        try {
            BEEP_SIZE = c.getResources().openRawResource(R.raw.sound1).read(beep, 0 , MAX_PATTERN_SIZE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BEEP_PATTERNS = BEEP_SIZE/2;
        beeper.write(beep, 0 , BEEP_SIZE);
        setPATTERN_FRAMES(PATTERN_FRAMES);
    }

    public void toggle(){
        if(beeper.getPlayState() != AudioTrack.PLAYSTATE_PLAYING){
            beeper.play();
        } else {
            beeper.pause();
        }
    }

    private void setPATTERN_FRAMES(int newValue){
        boolean reenable = false;
        if(newValue <= MAX_PATTERN_FRAMES){
            PATTERN_FRAMES = newValue;
        }

        if(beeper.getPlayState() == AudioTrack.PLAYSTATE_PLAYING) {
            beeper.pause();
            //beeper.setPlaybackHeadPosition(BEEP_PATTERNS);
            //TODO: Doesn't seem to work with loop
            reenable=true;
        }

        beeper.setLoopPoints(0, PATTERN_FRAMES, -1);

        if(reenable) {
            beeper.play();
        }
    }

    public void setPatternsPerSecond(double patternsPerSecond){
        int newPatternFrames = (int) ((SAMPLE_RATE / patternsPerSecond) + 0.5);
        setPATTERN_FRAMES(newPatternFrames);
    }

    public double getPatternsPerSecond(){
        return(SAMPLE_RATE/(double)PATTERN_FRAMES);
    }

    /**
     * Changes patterns per second
     * @param diff difference in patterns per second
     */
    public void incr(double diff){
        setPatternsPerSecond(getPatternsPerSecond() + diff);
    }

    /**
     * Changes patterns per second
     * @param diff difference in patterns per minute
     */
    public void incr(int diff){
        setPatternsPerSecond(getPatternsPerSecond() + (diff/60d) );
    }

}
