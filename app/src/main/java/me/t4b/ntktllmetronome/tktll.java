package me.t4b.ntktllmetronome;

import android.content.Context;
import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

import java.io.IOException;

/**
 * This class is the actual metronome, everything else is just UI and glue.
 * At the moment it supports basic metronome functionality,
 * but in the future I would like it to be able to play simple patterns,
 * that's why I use "pattern" instead of "beat" all the time.
 * A beat is just the simplest pattern.
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

    public boolean running=false;

    //set up audio output
    AudioTrack beeper = new AudioTrack(AudioManager.STREAM_MUSIC, SAMPLE_RATE, AudioFormat.CHANNEL_OUT_MONO, AudioFormat.ENCODING_PCM_16BIT, MAX_PATTERN_SIZE, AudioTrack.MODE_STATIC);
    byte[] beep = new byte[MAX_PATTERN_SIZE];

    /**
     * Initialize this Tktll with an audio resource. Here around the selection of different sounds could happen in the future
     * @param c Context
     */
    public void init(Context c){
        try {
            BEEP_SIZE = c.getResources().openRawResource(R.raw.sound1).read(beep, 0 , MAX_PATTERN_SIZE);
        } catch (IOException e) {
            e.printStackTrace();
        }
        BEEP_PATTERNS = BEEP_SIZE/2;
        beeper.write(beep, 0 , BEEP_SIZE);
    }

    /**
     * Toggle between running/stopped states
     */
    public void toggle(){
        if(running){
            stop();
        } else {
            start();
        }
    }

    /**
     * Stop this Tktll, trying to reset audio playback to beginning (TODO: The latter doesn't work, I think)
     */
    public void stop(){
        if(beeper.getPlayState() == AudioTrack.PLAYSTATE_PLAYING) {
            beeper.setPlaybackHeadPosition(0);
            beeper.pause();
            running=false;
        }
    }

    /**
     * Start this Tktll if beeper is ready, updating the loop points in the process
     */
    public void start(){
        if(!running && beeper.getState() == AudioTrack.STATE_INITIALIZED){
            updateBeeper();
            beeper.play();
            running=true;
        }
    }

    /**
     * Updates beeper loop, resetting PlaybackHeadPosition to 0
     */
    public void updateBeeper(){
        if(running) {
            beeper.pause();
            //beeper.setPlaybackHeadPosition(BEEP_PATTERNS);
            //TODO: Doesn't seem to work with loop, and needs to go in another method now anyway
        }

        beeper.setLoopPoints(0, PATTERN_FRAMES, -1);

        if(running) {
            beeper.play();
        }
    }

    /**
     * Updates PATTERN_FRAMES with a new value, doing some checks and updating the beeper afterwards
     * @param newValue
     */
    private void setPATTERN_FRAMES(int newValue){
        if(newValue <= MAX_PATTERN_FRAMES){
            PATTERN_FRAMES = newValue;
        }
        updateBeeper();
    }

    /**
     * Calls setPATTERN_FRAMES, doing the necessary unit conversions
     * @param patternsPerSecond
     */
    public void setPatternsPerSecond(double patternsPerSecond){
        int newPatternFrames = (int) ((SAMPLE_RATE / patternsPerSecond) + 0.5);
        setPATTERN_FRAMES(newPatternFrames);
    }

    /**
     * Returns the number of patterns per second, doing the necessary unit conversion
     * @return
     */
    public double getPatternsPerSecond(){
        return(SAMPLE_RATE/(double)PATTERN_FRAMES);
    }

    /**
     * Changes the number patterns per second
     * @param diff difference in patterns per second
     */
    public void incr(double diff){
        setPatternsPerSecond(getPatternsPerSecond() + diff);
    }

    /**
     * Changes the number patterns per second
     * @param diff difference in patterns per minute
     */
    public void incr(int diff){
        setPatternsPerSecond(getPatternsPerSecond() + (diff/60d) );
    }

    /**
     * Sets tempo to lowest possible
     */
    public void setSlowestTempo(){
        setPATTERN_FRAMES(MAX_PATTERN_FRAMES);
    }

    /**
     * TODO, will be used to set tempo by tapping. Called on each tap.
     */
    public void tap() {

    }
}
