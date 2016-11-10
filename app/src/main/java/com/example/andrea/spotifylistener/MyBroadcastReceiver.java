package com.example.andrea.spotifylistener;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by Andrea on 17/06/2016.
 */
public class MyBroadcastReceiver extends BroadcastReceiver {
    
    private static final String TAG = "SPOTIFY_LISTENER";
    
    
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        
        if (action.equals(Utils.Params.BroadcastTypes.METADATA_CHANGED)) {
            String trackId = intent.getStringExtra("id");
            String artistName = intent.getStringExtra("artist");
            String albumName = intent.getStringExtra("album");
            String trackName = intent.getStringExtra("track");
            int trackLengthInSec = intent.getIntExtra("length", 0);
            
            Log.i(TAG, "[data] trackId: " + trackId);
            Log.i(TAG, "[data] artistName: " + artistName);
            Log.i(TAG, "[data] albumName: " + albumName);
            Log.i(TAG, "[data] trackName: " + trackName);
            Log.i(TAG, "[data] trackLengthInSec: " + trackLengthInSec);
            
            Log.i(TAG, "Starting TTS Service for type METADATA_CHANGED...");
            Intent i = new Intent(context, TTS.class);
            i.putExtra(Utils.Params.BroadcastExtras.EXTRA_TYPE, action);
            i.putExtra(Utils.Params.BroadcastExtras.EXTRA_TRACK_ID, trackId);
            i.putExtra(Utils.Params.BroadcastExtras.EXTRA_ARTIST_NAME, artistName);
            i.putExtra(Utils.Params.BroadcastExtras.EXTRA_ALBUM_NAME, albumName);
            i.putExtra(Utils.Params.BroadcastExtras.EXTRA_TRACK_NAME, trackName);
            i.putExtra(Utils.Params.BroadcastExtras.EXTRA_TRACK_LENGTH_IN_SEC, trackLengthInSec);
            context.startService(i);
            
        }
        /*else if (action.equals(Utils.Params.BroadcastTypes.PLAYBACK_STATE_CHANGED)) {
            boolean playing = intent.getBooleanExtra("playing", false);
            int positionInMs = intent.getIntExtra("playbackPosition", 0);
            
            Log.i(TAG, "[data] playing: " + playing);
            Log.i(TAG, "[data] positionInMs: " + positionInMs);
            
            Log.i(TAG, "Starting TTS Service for type PLAYBACK_STATE_CHANGED...");
            Intent i = new Intent(context, TTS.class);
            i.putExtra(Utils.Params.BroadcastExtras.EXTRA_TYPE, action);
            i.putExtra(Utils.Params.BroadcastExtras.EXTRA_PLAYING, playing);
            i.putExtra(Utils.Params.BroadcastExtras.EXTRA_POSITION_IN_MS, positionInMs);
            context.startService(i);
        }*/
    }
    
}
