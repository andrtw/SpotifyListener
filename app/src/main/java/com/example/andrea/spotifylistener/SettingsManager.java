package com.example.andrea.spotifylistener;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

/**
 * Created by Andrea on 18/06/2016.
 */
public class SettingsManager {

    // Weather to say the track, album and artist name or not
    public static final String PREF_KEY_PLAY_TRACK = "pref_key_play_track";
    public static final String PREF_KEY_PLAY_ALBUM = "pref_key_play_album";
    public static final String PREF_KEY_PLAY_ARTIST = "pref_key_play_artist";
    // What to say between track, album and artist name
    public static final String PREF_KEY_TEXT_TRACK = "pref_key_text_track";
    public static final String PREF_KEY_TEXT_ALBUM = "pref_key_text_album";
    public static final String PREF_KEY_TEXT_ARTIST = "pref_key_text_artist";
    // SettingsLayout children order
    public static final String PREF_KEY_LAYOUT_TRACK = "layout_track";
    public static final String PREF_KEY_LAYOUT_ALBUM = "layout_album";
    public static final String PREF_KEY_LAYOUT_ARTIST = "layout_artist";
    private static final String PREF_NAME = "pref_settings";
    // It the app is active
    private static final String PREF_KEY_ACTIVE = "pref_key_active";
    // Defaults
    private static final boolean PREF_KEY_PLAY_TRACK_DEFAULT = true;
    private static final boolean PREF_KEY_PLAY_ALBUM_DEFAULT = true;
    private static final boolean PREF_KEY_PLAY_ARTIST_DEFAULT = true;
    // Defaults
    private static final String PREF_KEY_TEXT_TRACK_DEFAULT = "";
    private static final String PREF_KEY_TEXT_ALBUM_DEFAULT = "from";
    private static final String PREF_KEY_TEXT_ARTIST_DEFAULT = "by";
    // Defaults
    private static final int PREF_KEY_LAYOUT_TRACK_DEFAULT = 0;
    private static final int PREF_KEY_LAYOUT_ALBUM_DEFAULT = 1;
    private static final int PREF_KEY_LAYOUT_ARTIST_DEFAULT = 2;
    private static SettingsManager instance;
    private SharedPreferences pref;

    private SettingsManager(Context ctx) {
        pref = ctx.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    }

    /**
     * SettingsManager handles all the settings for the application.
     *
     * @param ctx Context
     */
    public static SettingsManager getInstance(Context ctx) {
        if (instance == null) {
            instance = new SettingsManager(ctx);
        }
        return instance;
    }
    
    /**
     * Indicates if the app has to tell track, album, artist
     *
     * @return true if it should tell current track's info, false otherwise
     */
    public boolean isActive() {
        return pref.getBoolean(PREF_KEY_ACTIVE, true);
    }

    /**
     * Sets of the app has to say track, album, artist
     *
     * @param active boolean
     */
    public void setActive(boolean active) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(PREF_KEY_ACTIVE, active);
        editor.apply();
    }
    
    /**
     * Returns a set of values that indicates what has to be spoken
     *
     * @return Booleans into an HashMap
     */
    public HashMap<String, Boolean> getWhatToSay() {
        HashMap<String, Boolean> map = new HashMap<>();
        map.put(PREF_KEY_PLAY_TRACK, pref.getBoolean(PREF_KEY_PLAY_TRACK, PREF_KEY_PLAY_TRACK_DEFAULT));
        map.put(PREF_KEY_PLAY_ALBUM, pref.getBoolean(PREF_KEY_PLAY_ALBUM, PREF_KEY_PLAY_ALBUM_DEFAULT));
        map.put(PREF_KEY_PLAY_ARTIST, pref.getBoolean(PREF_KEY_PLAY_ARTIST, PREF_KEY_PLAY_ARTIST_DEFAULT));
        return map;
    }
    
    /**
     * Returns a set of values of what has to be spoken between track, album and artist
     *
     * @return Strings into an HashMap
     */
    public HashMap<String, String> getIntermediateWords() {
        HashMap<String, String> map = new HashMap<>();
        map.put(PREF_KEY_TEXT_TRACK, pref.getString(PREF_KEY_TEXT_TRACK, PREF_KEY_TEXT_TRACK_DEFAULT));
        map.put(PREF_KEY_TEXT_ALBUM, pref.getString(PREF_KEY_TEXT_ALBUM, PREF_KEY_TEXT_ALBUM_DEFAULT));
        map.put(PREF_KEY_TEXT_ARTIST, pref.getString(PREF_KEY_TEXT_ARTIST, PREF_KEY_TEXT_ARTIST_DEFAULT));
        return map;
    }
    
    /**
     * Returns a set of values of the position for the track, album and artist layout
     *
     * @return Integer into a HashMap
     */
    public HashMap<String, Integer> getSettingsLayoutOrder() {
        HashMap<String, Integer> map = new HashMap<>();
        map.put(PREF_KEY_LAYOUT_TRACK, pref.getInt(PREF_KEY_LAYOUT_TRACK, PREF_KEY_LAYOUT_TRACK_DEFAULT));
        map.put(PREF_KEY_LAYOUT_ALBUM, pref.getInt(PREF_KEY_LAYOUT_ALBUM, PREF_KEY_LAYOUT_ALBUM_DEFAULT));
        map.put(PREF_KEY_LAYOUT_ARTIST, pref.getInt(PREF_KEY_LAYOUT_ARTIST, PREF_KEY_LAYOUT_ARTIST_DEFAULT));
        return map;
    }
    
    /**
     * Sets is track, album and artis should be spoken
     *
     * @param track  boolean
     * @param album  boolean
     * @param artist boolean
     */
    public void setWhatToSay(boolean track, boolean album, boolean artist) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(PREF_KEY_PLAY_TRACK, track);
        editor.putBoolean(PREF_KEY_PLAY_ALBUM, album);
        editor.putBoolean(PREF_KEY_PLAY_ARTIST, artist);
        editor.apply();
    }
    
    /**
     * Sets what to say between track, album and artist
     *
     * @param track  String
     * @param album  String
     * @param artist String
     */
    public void setIntermediateWords(String track, String album, String artist) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(PREF_KEY_TEXT_TRACK, track.trim());
        editor.putString(PREF_KEY_TEXT_ALBUM, album.trim());
        editor.putString(PREF_KEY_TEXT_ARTIST, artist.trim());
        editor.apply();
    }
    
    /**
     * Sets the order of track, album and artist layout
     *
     * @param layoutTrack  Track layout position
     * @param layoutAlbum  Album layout position
     * @param layoutArtist Artist layout position
     */
    public void setSettingsLayoutOrder(int layoutTrack, int layoutAlbum, int layoutArtist) {
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(PREF_KEY_LAYOUT_TRACK, layoutTrack);
        editor.putInt(PREF_KEY_LAYOUT_ALBUM, layoutAlbum);
        editor.putInt(PREF_KEY_LAYOUT_ARTIST, layoutArtist);
        editor.apply();
    }
    
}
