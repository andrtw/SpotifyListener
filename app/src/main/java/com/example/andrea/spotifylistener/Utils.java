package com.example.andrea.spotifylistener;

import android.content.Context;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;

/**
 * Created by Andrea on 25/06/2016.
 */
public class Utils {
    
    private static final String TAG = "SPOTIFY_LISTENER";

    culates the
    position of
    the layout
     *
     * @param width       Width of the root layout
     * @param subdivision How many areas is the root layout divided in
     * @param lx          X coordinate of layout
     * @return Position
     */
    public static int calculateLayoutPosition(int width, int subdivision, float lx) {
        float oneSubdivisionWidth = width / subdivision;
        float normalizedXR = lx - 2;
        float normalizedXL = lx + 2;
        if (normalizedXL >= 0 && lx <= oneSubdivisionWidth) {
            return Params.LayoutPosition.FIRST;
        } else if (lx >= oneSubdivisionWidth && lx <= oneSubdivisionWidth * 2) {
            return Params.LayoutPosition.SECOND;
        } else if (lx >= oneSubdivisionWidth * 2 && normalizedXR <= oneSubdivisionWidth * 3) {
            return Params.LayoutPosition.THIRD;
        }
        return -1;
    }
    
    /**
     * Swap two layouts
     *
     * @param layoutMain The layout root containing the layouts
     * @param pos1       Position of first layout
     * @param pos2       Position of second layout
     */
    public static void swapLayouts(LinearLayout layoutMain, int pos1, int pos2) {
        if (pos1 != pos2) {
            boolean leftToRight = pos1 < pos2;
            LinearLayout l1 = (LinearLayout) layoutMain.getChildAt(pos1);
            LinearLayout l2 = (LinearLayout) layoutMain.getChildAt(pos2);
            if (leftToRight) {
                layoutMain.removeView(l1);
                layoutMain.removeView(l2);
                layoutMain.addView(l2, pos1);
                layoutMain.addView(l1, pos2);
            } else {
                layoutMain.removeView(l2);
                layoutMain.removeView(l1);
                layoutMain.addView(l1, pos2);
                layoutMain.addView(l2, pos1);
            }
        }
    }
    
    /**
     * Saves the text to say between track, album and artist
     *
     * @param ctx       Context
     * @param editTexts List that must contain track, album and artist EditText
     */
    public static void saveIntermediate(Context ctx, ArrayList<EditText> editTexts) {
        SettingsManager sm = SettingsManager.getInstance(ctx);
        sm.setIntermediateWords(
                editTexts.get(0).getText().toString(),
                editTexts.get(1).getText().toString(),
                editTexts.get(2).getText().toString()
        );
    }
    
    /**
     * Enable or disable EditTexts based on its CheckBox
     *
     * @param editTexts  List of EditTexts
     * @param checkBoxes List of checkBoxes
     */
    public static void enableEditTexts(ArrayList<EditText> editTexts, ArrayList<CheckBox> checkBoxes) {
        if (editTexts.size() != checkBoxes.size()) {
            throw new RuntimeException("Two lists must be of the same size");
        }
        int i = 0;
        for (EditText et : editTexts) {
            et.setEnabled(checkBoxes.get(i).isChecked());
            i++;
        }
    }
    
    /**
     * Enable or
     /**
     * Global parameters
     */
    public static class Params {
        public static final int PARAM_LAYOUT_TRACK = 0;
        public static final int PARAM_LAYOUT_ALBUM = 1;
        public static final int PARAM_LAYOUT_ARTIST = 2;
        public static final String SAMPLE_TRACK = "Basket Case";
        public static final String SAMPLE_ALBUM = "Dookie";
        public static final String SAMPLE_ARTIST = "Green Day";

        /**
         * Positions for layouts to swap
         */
        public static class LayoutPosition {
            public static final int FIRST = 0;
            public static final int SECOND = 1;
            public static final int THIRD = 2;
        }

        /**
         * Types of Broadcast Intents sent by Spotify
         */
        public static final class BroadcastTypes {
            public static final String SPOTIFY_PACKAGE = "com.spotify.music";
            public static final String PLAYBACK_STATE_CHANGED = SPOTIFY_PACKAGE + ".playbackstatechanged";
            public static final String QUEUE_CHANGED = SPOTIFY_PACKAGE + ".queuechanged";
            public static final String METADATA_CHANGED = SPOTIFY_PACKAGE + ".metadatachanged";
        }

        public static final class BroadcastExtras {
            public static final String EXTRA_TYPE = "type";
            public static final String EXTRA_TRACK_ID = "trackId";
            public static final String EXTRA_ARTIST_NAME = "artistName";
            public static final String EXTRA_ALBUM_NAME = "albumName";
            public static final String EXTRA_TRACK_NAME = "trackName";
            public static final String EXTRA_TRACK_LENGTH_IN_SEC = "trackLengthInSec";
            public static final String EXTRA_PLAYING = "playing";
            public static final String EXTRA_POSITION_IN_MS = "positionInMs";
        }

    }

    /**
     * Cal disable the EditText you want
     *
     * @param editTexts List of EditTexts
     * @param track     Boolean
     * @param album     Boolean
     * @param artist    Boolean
     */
    public static void enableEditTexts(ArrayList<EditText> editTexts, boolean track, boolean album, boolean artist) {
        if (editTexts.size() > 3) {
            throw new RuntimeException("EditTexts list must contain three elements");
        }
        editTexts.get(0).setEnabled(track);
        editTexts.get(1).setEnabled(album);
        editTexts.get(2).setEnabled(artist);
    }
    
}
