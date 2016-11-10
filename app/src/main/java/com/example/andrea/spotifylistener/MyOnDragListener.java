package com.example.andrea.spotifylistener;

import android.content.Context;
import android.support.v4.util.Pair;
import android.view.DragEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Andrea on 21/06/2016.
 */
public class MyOnDragListener implements View.OnDragListener {
    
    private static final String TAG = "SPOTIFY_LISTENER";
    
    private ArrayList<EditText> editTexts;
    private ArrayList<CheckBox> checkBoxes;
    private SettingsManager sm;
    
    public MyOnDragListener(Context ctx, ArrayList<EditText> editTexts, ArrayList<CheckBox> checkBoxes) {
        sm = new SettingsManager(ctx);
        this.editTexts = editTexts;
        this.checkBoxes = checkBoxes;
    }
    
    @Override
    public boolean onDrag(View v, DragEvent event) {
        int pos1 = (int) event.getLocalState();
        LinearLayout rootLayout = (LinearLayout) v;
        
        switch (event.getAction()) {
            // Reset background when entering valid drop area
            case DragEvent.ACTION_DRAG_ENTERED: {
                for (int i = 0; i < rootLayout.getChildCount(); i++) {
                    rootLayout.getChildAt(i).setBackgroundResource(R.drawable.target_drop_idle);
                }
                rootLayout.getChildAt(pos1).setBackgroundResource(0);
                Utils.enableEditTexts(editTexts, false, false, false);
            }
            break;
            // Moving into the drop area
            case DragEvent.ACTION_DRAG_LOCATION: {
                int hoveredPos = Utils.calculateLayoutPosition(v.getWidth(), 3, event.getX());
                for (int i = 0; i < rootLayout.getChildCount(); i++) {
                    rootLayout.getChildAt(i).setBackgroundResource(R.drawable.target_drop_idle);
                }
                rootLayout.getChildAt(hoveredPos).setBackgroundResource(R.drawable.target_drop_hovered);
                rootLayout.getChildAt(pos1).setBackgroundResource(0);
            }
            break;
            // Drop occurred
            case DragEvent.ACTION_DROP: {
                int pos2 = Utils.calculateLayoutPosition(v.getWidth(), 3, event.getX());
                
                resetBackground(rootLayout);
                
                Utils.swapLayouts(rootLayout, pos1, pos2);
                
                ArrayList<Pair<Integer, Integer>> layoutsPositions = new ArrayList<>();
                for (int i = 0; i < rootLayout.getChildCount(); i++) {
                    LinearLayout l = (LinearLayout) rootLayout.getChildAt(i);
                    switch (l.getId()) {
                        case R.id.layoutTrack:
                            layoutsPositions.add(new Pair<>(Utils.Params.PARAM_LAYOUT_TRACK, i));
                            break;
                        case R.id.layoutAlbum:
                            layoutsPositions.add(new Pair<>(Utils.Params.PARAM_LAYOUT_ALBUM, i));
                            break;
                        case R.id.layoutArtist:
                            layoutsPositions.add(new Pair<>(Utils.Params.PARAM_LAYOUT_ARTIST, i));
                            break;
                    }
                }
                Collections.sort(layoutsPositions, new Comparator<Pair<Integer, Integer>>() {
                    @Override
                    public int compare(Pair<Integer, Integer> p1, Pair<Integer, Integer> p2) {
                        return p1.first - p2.first;
                    }
                });
                
                sm.setSettingsLayoutOrder(
                        layoutsPositions.get(0).second,
                        layoutsPositions.get(1).second,
                        layoutsPositions.get(2).second
                );
                
                Utils.enableEditTexts(editTexts, checkBoxes);
            }
            break;
            case DragEvent.ACTION_DRAG_ENDED:
            case DragEvent.ACTION_DRAG_EXITED:
                resetBackground(rootLayout);
                Utils.enableEditTexts(editTexts, checkBoxes);
                break;
        }
        return true;
    }
    
    private void resetBackground(LinearLayout rootLayout) {
        for (int i = 0; i < rootLayout.getChildCount(); i++) {
            LinearLayout l = (LinearLayout) rootLayout.getChildAt(i);
            l.setBackgroundResource(0);
        }
    }
}
