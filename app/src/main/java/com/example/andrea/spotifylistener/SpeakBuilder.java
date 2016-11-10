package com.example.andrea.spotifylistener;

import android.content.Context;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Andrea on 18/06/2016.
 */
public class SpeakBuilder {
    
    private static final String TAG = "SPOTIFY_LISTENER";
    private static final Character SPACE = ' ';
    
    public static String build(Context ctx, String track, String album, String artist) {
        SettingsManager sm = new SettingsManager(ctx);
        /*
        * Structure of toBeSpoken:
        * intermediateTrack + track + intermediateAlbum + album + intermediateArtist + artist
        * if track, album or artist is empty, then this and its intermediate won't be played
        * */
        String toBeSpoken = "";
        
        
        HashMap<String, Boolean> whatToSay = sm.getWhatToSay();
        HashMap<String, String> intermediate = sm.getIntermediateWords();
        
        boolean playTrack = whatToSay.get(SettingsManager.PREF_KEY_PLAY_TRACK);
        boolean playAlbum = whatToSay.get(SettingsManager.PREF_KEY_PLAY_ALBUM);
        boolean playArtist = whatToSay.get(SettingsManager.PREF_KEY_PLAY_ARTIST);
        String intermediateTrack = intermediate.get(SettingsManager.PREF_KEY_TEXT_TRACK);
        String intermediateAlbum = intermediate.get(SettingsManager.PREF_KEY_TEXT_ALBUM);
        String intermediateArtist = intermediate.get(SettingsManager.PREF_KEY_TEXT_ARTIST);
        
        HashMap<String, Integer> layoutOrder = sm.getSettingsLayoutOrder();
        List<Map.Entry<String, Integer>> layoutList = new ArrayList<>();
        for (Map.Entry<String, Integer> e : layoutOrder.entrySet()) {
            layoutList.add(e);
        }
        Collections.sort(layoutList, new Comparator<Map.Entry<String, Integer>>() {
            @Override
            public int compare(Map.Entry<String, Integer> e1, Map.Entry<String, Integer> e2) {
                return e1.getValue() - e2.getValue();
            }
        });
        
        for (Map.Entry<String, Integer> e : layoutList) {
            switch (e.getKey()) {
                case SettingsManager.PREF_KEY_LAYOUT_TRACK:
                    toBeSpoken += shouldBeSpoken(track, intermediateTrack, playTrack);
                    break;
                case SettingsManager.PREF_KEY_LAYOUT_ALBUM:
                    toBeSpoken += shouldBeSpoken(album, intermediateAlbum, playAlbum);
                    break;
                case SettingsManager.PREF_KEY_LAYOUT_ARTIST:
                    toBeSpoken += shouldBeSpoken(artist, intermediateArtist, playArtist);
                    break;
            }
        }
        
        Log.i(TAG, "-" + toBeSpoken.trim() + "-");
        return toBeSpoken.trim();
    }
    
    private static String shouldBeSpoken(String what, String intermediate, boolean speak) {
        String result = "";
        if (speak) {
            if (!intermediate.equals("")) {
                result += intermediate + SPACE;
            }
            result += what + SPACE;
        }
        return result;
    }
    
}
