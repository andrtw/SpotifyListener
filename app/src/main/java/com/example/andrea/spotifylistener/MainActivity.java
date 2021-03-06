package com.example.andrea.spotifylistener;

import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.support.design.widget.TextInputEditText;
import android.support.v4.util.Pair;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements TextToSpeech.OnInitListener, CompoundButton.OnCheckedChangeListener {

    private LinearLayout layoutSettings;
    private LinearLayout layoutTrack, layoutAlbum, layoutArtist;

    private Switch switchActive;
    private CheckBox checkBoxTrackName, checkBoxAlbumName, checkBoxArtistName;
    private TextInputEditText editTextTrack, editTextAlbum, editTextArtist;
    private ArrayList<EditText> editTextsList = new ArrayList<>();
    private ArrayList<CheckBox> checkBoxesList = new ArrayList<>();

    private SettingsManager sm;

    private Button buttonTest;
    private Toast toastTest;
    private TextToSpeech tts;

    private TextView textViewDisabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sm = SettingsManager.getInstance(this);

        init();
        initStatus();

        tts = new TextToSpeech(this, this);
    }

    private void init() {
        // Switch
        switchActive = findViewById(R.id.switchActive);
        switchActive.setOnCheckedChangeListener(this);

        // CheckBoxes
        checkBoxTrackName = findViewById(R.id.checkBoxTrackName);
        checkBoxAlbumName = findViewById(R.id.checkBoxAlbumName);
        checkBoxArtistName = findViewById(R.id.checkBoxArtistName);
        checkBoxTrackName.setOnCheckedChangeListener(this);
        checkBoxAlbumName.setOnCheckedChangeListener(this);
        checkBoxArtistName.setOnCheckedChangeListener(this);
        checkBoxesList.add(checkBoxTrackName);
        checkBoxesList.add(checkBoxAlbumName);
        checkBoxesList.add(checkBoxArtistName);

        // Layouts + drag listener
        layoutSettings = findViewById(R.id.layoutSettings);
        layoutSettings.setOnDragListener(new SpotifyOnDragListener(this, editTextsList, checkBoxesList));
        layoutTrack = findViewById(R.id.layoutTrack);
        layoutAlbum = findViewById(R.id.layoutAlbum);
        layoutArtist = findViewById(R.id.layoutArtist);

        // EditTexts + watch listeners
        editTextTrack = findViewById(R.id.editTextTrack);
        editTextAlbum = findViewById(R.id.editTextAlbum);
        editTextArtist = findViewById(R.id.editTextArtist);
        editTextsList.add(editTextTrack);
        editTextsList.add(editTextAlbum);
        editTextsList.add(editTextArtist);
        editTextTrack.addTextChangedListener(new SpotifyTextWatcher(this, editTextsList));
        editTextAlbum.addTextChangedListener(new SpotifyTextWatcher(this, editTextsList));
        editTextArtist.addTextChangedListener(new SpotifyTextWatcher(this, editTextsList));

        // ImageViews + touch listeners
        ImageView imageViewDragTrack = findViewById(R.id.imageViewDragTrack);
        ImageView imageViewDragAlbum = findViewById(R.id.imageViewDragAlbum);
        ImageView imageViewDragArtist = findViewById(R.id.imageViewDragArtist);
        imageViewDragTrack.setOnTouchListener(new SpotifyOnTouchListener(editTextsList));
        imageViewDragAlbum.setOnTouchListener(new SpotifyOnTouchListener(editTextsList));
        imageViewDragArtist.setOnTouchListener(new SpotifyOnTouchListener(editTextsList));

        buttonTest = findViewById(R.id.buttonTest);
        textViewDisabled = findViewById(R.id.textViewDisabled);
    }

    private void initStatus() {
        // Switch active and error TextView
        switchActive.setChecked(sm.isActive());
        animateDisabledTextView(!sm.isActive());

        // CheckBoxes
        HashMap<String, Boolean> checkBoxMap = sm.getWhatToSay();
        checkBoxTrackName.setChecked(checkBoxMap.get(SettingsManager.PREF_KEY_PLAY_TRACK));
        checkBoxAlbumName.setChecked(checkBoxMap.get(SettingsManager.PREF_KEY_PLAY_ALBUM));
        checkBoxArtistName.setChecked(checkBoxMap.get(SettingsManager.PREF_KEY_PLAY_ARTIST));

        // EditTexts
        Utils.enableEditTexts(
                editTextsList,
                checkBoxTrackName.isChecked(),
                checkBoxAlbumName.isChecked(),
                checkBoxArtistName.isChecked()
        );

        // EditTexts text
        HashMap<String, String> editTextMap = sm.getIntermediateWords();
        editTextTrack.setText(editTextMap.get(SettingsManager.PREF_KEY_TEXT_TRACK));
        editTextAlbum.setText(editTextMap.get(SettingsManager.PREF_KEY_TEXT_ALBUM));
        editTextArtist.setText(editTextMap.get(SettingsManager.PREF_KEY_TEXT_ARTIST));

        // Layouts order
        HashMap<String, Integer> layoutsOrderMap = sm.getSettingsLayoutOrder();
        Pair<LinearLayout, Integer> layoutTrackPos = new Pair<>(
                layoutTrack,
                layoutsOrderMap.get(SettingsManager.PREF_KEY_LAYOUT_TRACK)
        );
        Pair<LinearLayout, Integer> layoutAlbumPos = new Pair<>(
                layoutAlbum,
                layoutsOrderMap.get(SettingsManager.PREF_KEY_LAYOUT_ALBUM)
        );
        Pair<LinearLayout, Integer> layoutArtistPos = new Pair<>(
                layoutArtist,
                layoutsOrderMap.get(SettingsManager.PREF_KEY_LAYOUT_ARTIST)
        );

        ArrayList<Pair<LinearLayout, Integer>> layoutsPositions = new ArrayList<>();
        layoutsPositions.add(layoutTrackPos);
        layoutsPositions.add(layoutAlbumPos);
        layoutsPositions.add(layoutArtistPos);

        Collections.sort(layoutsPositions, new Comparator<Pair<LinearLayout, Integer>>() {
            @Override
            public int compare(Pair<LinearLayout, Integer> p1, Pair<LinearLayout, Integer> p2) {
                return p1.second - p2.second;
            }
        });

        layoutSettings.removeAllViews();
        layoutSettings.addView(layoutsPositions.get(0).first, layoutsPositions.get(0).second);
        layoutSettings.addView(layoutsPositions.get(1).first, layoutsPositions.get(1).second);
        layoutSettings.addView(layoutsPositions.get(2).first, layoutsPositions.get(2).second);
    }

    public void buttonTestClick(View view) {
        String testString = SpeakBuilder.build(this,
                Utils.Params.SAMPLE_TRACK,
                Utils.Params.SAMPLE_ALBUM,
                Utils.Params.SAMPLE_ARTIST);
        speakOut(testString);

        // Custom Toast
        if (toastTest != null) {
            toastTest.cancel();
        }
        RelativeLayout layoutToast = (RelativeLayout) getLayoutInflater().inflate(R.layout.toast_layout, (RelativeLayout) findViewById(R.id.layoutToast));
        TextView textViewToast = (TextView) layoutToast.findViewById(R.id.textViewToast);
        textViewToast.setText(testString);
        toastTest = new Toast(this);
        toastTest.setGravity(Gravity.CENTER, 0, 0);
        toastTest.setView(layoutToast);
        toastTest.setDuration(Toast.LENGTH_SHORT);
        toastTest.show();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (toastTest != null) {
            toastTest.cancel();
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Toast.makeText(this, getString(R.string.tts_error_language), Toast.LENGTH_SHORT).show();
            } else {
                buttonTest.setEnabled(true);
            }
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()) {
            case R.id.switchActive:
                sm.setActive(isChecked);
                animateDisabledTextView(!isChecked);
                break;
            case R.id.checkBoxTrackName:
            case R.id.checkBoxAlbumName:
            case R.id.checkBoxArtistName:
                sm.setWhatToSay(
                        checkBoxTrackName.isChecked(),
                        checkBoxAlbumName.isChecked(),
                        checkBoxArtistName.isChecked()
                );
                Utils.enableEditTexts(
                        editTextsList,
                        checkBoxTrackName.isChecked(),
                        checkBoxAlbumName.isChecked(),
                        checkBoxArtistName.isChecked()
                );
                break;
        }
    }

    private void speakOut(String str) {
        tts.speak(str, TextToSpeech.QUEUE_FLUSH, null);
    }

    @Override
    protected void onDestroy() {
        if (tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    private void animateDisabledTextView(boolean enter) {
        if (enter) {
            textViewDisabled.startAnimation(AnimationUtils.loadAnimation(this, R.anim.disabled_slide_in));
            textViewDisabled.setVisibility(View.VISIBLE);
        } else {
            textViewDisabled.startAnimation(AnimationUtils.loadAnimation(this, R.anim.disabled_slide_out));
            textViewDisabled.setVisibility(View.GONE);
        }
    }

}
