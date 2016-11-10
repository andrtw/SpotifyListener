package com.example.andrea.spotifylistener;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.speech.tts.TextToSpeech;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.Locale;

/**
 * Created by Andrea on 18/06/2016.
 */
public class TTS extends Service implements TextToSpeech.OnInitListener {
    
    private static final String TAG = "SPOTIFY_LISTENER";
    
    private SettingsManager sm;
    private TextToSpeech tts;
    
    private String type;
    
    private String trackId;
    private String artistName;
    private String albumName;
    private String trackName;
    private int trackLengthInSec;
    
    private boolean playing;
    private int positionInMs;
    
    
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i(TAG, "onStartCommand()");
        sm = new SettingsManager(this);
        tts = new TextToSpeech(this, this);
        
        if (intent != null) {
            type = intent.getStringExtra(Utils.Params.BroadcastExtras.EXTRA_TYPE);
            Log.i(TAG, "type from onStartCommand(): " + type);
            if (type.equals(Utils.Params.BroadcastTypes.METADATA_CHANGED)) {
                trackId = intent.getStringExtra(Utils.Params.BroadcastExtras.EXTRA_TRACK_ID);
                artistName = intent.getStringExtra(Utils.Params.BroadcastExtras.EXTRA_ARTIST_NAME);
                albumName = intent.getStringExtra(Utils.Params.BroadcastExtras.EXTRA_ALBUM_NAME);
                trackName = intent.getStringExtra(Utils.Params.BroadcastExtras.EXTRA_TRACK_NAME);
                trackLengthInSec = intent.getIntExtra(Utils.Params.BroadcastExtras.EXTRA_TRACK_LENGTH_IN_SEC, 0);
            }
            /*else if (type.equals(Utils.Params.BroadcastTypes.PLAYBACK_STATE_CHANGED)) {
                playing = intent.getBooleanExtra(Utils.Params.BroadcastExtras.EXTRA_PLAYING, false);
                positionInMs = intent.getIntExtra(Utils.Params.BroadcastExtras.EXTRA_POSITION_IN_MS, 0);
            }*/
        }
        
        return super.onStartCommand(intent, flags, startId);
    }
    
    @Override
    public void onInit(int status) {
        Log.i(TAG, "onInit()");
        if (sm.isActive()) {
            if (status == TextToSpeech.SUCCESS) {
                int result = tts.setLanguage(Locale.US);
                if (result != TextToSpeech.LANG_MISSING_DATA && result != TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.i(TAG, "type from onInit(): " + type);
                    
                    if (type != null && type.equals(Utils.Params.BroadcastTypes.METADATA_CHANGED)) {
                        if (trackName != null && albumName != null && artistName != null) {
                            String toBeSpoken = SpeakBuilder.build(this, trackName, albumName, artistName);
                            tts.speak(toBeSpoken, TextToSpeech.QUEUE_FLUSH, null);
                        }
                    }
                    /*else if (type.equals(Utils.Params.BroadcastTypes.PLAYBACK_STATE_CHANGED)) {
                        tts.speak("playback state changed", TextToSpeech.QUEUE_FLUSH, null);
                    }*/
                    
                }
            }
        } else {
            Log.i(TAG, "Deactivated");
        }
    }
    
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    
    @Override
    public void onDestroy() {
        Log.i(TAG, "onDestroy()");
        super.onDestroy();
    }
}
