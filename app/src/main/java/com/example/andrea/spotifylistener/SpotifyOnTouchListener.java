package com.example.andrea.spotifylistener;

import android.content.ClipData;
import android.graphics.Point;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import java.util.ArrayList;

/**
 * Created by Andrea on 19/06/2016.
 */
public class SpotifyOnTouchListener implements View.OnTouchListener {
    private static final String TAG = "SPOTIFY_LISTENER";
    
    private ArrayList<EditText> editTexts;

    public SpotifyOnTouchListener(ArrayList<EditText> editTexts) {
        this.editTexts = editTexts;
    }
    
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            // Disable EditTexts to avoid layouts to be dropped into them
            Utils.enableEditTexts(editTexts, false, false, false);
            
            LinearLayout layout = (LinearLayout) v.getParent();
            LinearLayout rootLayout = (LinearLayout) layout.getParent();
            float lx = event.getRawX();
            int pos1 = Utils.calculateLayoutPosition(rootLayout.getWidth(), 3, lx);
            
            for (int i = 0; i < rootLayout.getChildCount(); i++) {
                rootLayout.getChildAt(i).setBackgroundResource(R.drawable.target_drop_idle);
            }
            rootLayout.getChildAt(pos1).setBackgroundResource(0);
            
            ClipData clipData = ClipData.newPlainText("", "");
            View.DragShadowBuilder shadow = new CustomDragShadowBuilder(
                    layout,
                    new Point(layout.getWidth() / 2, layout.getHeight() - 40)
            );
            layout.startDrag(clipData, shadow, pos1, 0);
        }
        return true;
    }
    
    private class CustomDragShadowBuilder extends View.DragShadowBuilder {
        private Point offset;
        
        public CustomDragShadowBuilder(View view, Point offset) {
            super(view);
            this.offset = offset;
        }
        
        @Override
        public void onProvideShadowMetrics(Point shadowSize, Point shadowTouchPoint) {
            shadowSize.set(getView().getWidth(), getView().getHeight());
            shadowTouchPoint.set(offset.x, offset.y);
        }
    }
}