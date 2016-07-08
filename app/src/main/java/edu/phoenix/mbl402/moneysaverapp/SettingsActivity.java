package edu.phoenix.mbl402.moneysaverapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.SeekBar;
import android.widget.TextView;

import java.text.NumberFormat;


public class SettingsActivity extends AppCompatActivity {

    private int titleBarColorRed, titleBarColorGreen, titleBarColorBlue;
    private boolean saveBattery;
    private int toolBarBackgroundColor;
    private Toolbar toolbar;
    private static final String DEBUG_KEY = "DEBUG_Settings_Activity";
    private NumberFormat formatter = NumberFormat.getIntegerInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        toolbar = (Toolbar) findViewById(R.id.settings_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        DataIO.loadUserPreferences(this);
        toolBarBackgroundColor=UserPreferences.getBackgroundColor();
        toolbar.setBackgroundColor(toolBarBackgroundColor);
//        Log.d(DEBUG_KEY, "Stage 1: toolBarBackgroundColor = " + String.format("%06X", toolBarBackgroundColor));

        saveBattery = UserPreferences.isSaveBattery();
        CheckBox batterySaverOn = (CheckBox) findViewById(R.id.chkLowBattery);
        batterySaverOn.setChecked(saveBattery);
        batterySaverOn.setOnClickListener(new CheckBox.OnClickListener(){

            @Override
            public void onClick(View view) {
                saveBattery= (((CheckBox) view).isChecked());
            }
        });

        SeekBar redSlider = (SeekBar) findViewById(R.id.redSlider);
        SeekBar greenSlider = (SeekBar) findViewById(R.id.greenSlider);
        SeekBar blueSlider = (SeekBar) findViewById(R.id.blueSlider);
        TextView redSliderValue = (TextView) findViewById(R.id.redSliderValue);
        TextView greenSliderValue = (TextView) findViewById(R.id.greenSliderValue);
        TextView blueSliderValue = (TextView) findViewById(R.id.blueSliderValue);

        //TODO: BUG - SeekBar that does not move gets value reset to 0
        redSlider.setProgress(Color.red(toolBarBackgroundColor));
        redSliderValue.setText(formatter.format(Color.red(toolBarBackgroundColor)));
//        Log.d(DEBUG_KEY, "Stage 2: Set redSliderProgress = " + String.format("%02X", Color.red(toolBarBackgroundColor)));

        greenSlider.setProgress(Color.green(toolBarBackgroundColor));
        greenSliderValue.setText(formatter.format(Color.green(toolBarBackgroundColor)));
//        Log.d(DEBUG_KEY, "Stage 2: Set greenSliderProgress = " + String.format("%02X", Color.green(toolBarBackgroundColor)));

        blueSlider.setProgress(Color.blue(toolBarBackgroundColor));
        blueSliderValue.setText(formatter.format(Color.blue(toolBarBackgroundColor)));
//        Log.d(DEBUG_KEY, "Stage 2: Set blueSliderProgress = " + String.format("%02X", Color.blue(toolBarBackgroundColor)));



        redSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            TextView redSliderValue = (TextView) findViewById(R.id.redSliderValue);

            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                titleBarColorRed = progressValue;

                toolBarBackgroundColor = Color.rgb(titleBarColorRed, titleBarColorGreen, titleBarColorBlue);
                toolbar.setBackgroundColor(toolBarBackgroundColor);
                redSliderValue.setText(formatter.format(titleBarColorRed));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
//                Log.d(DEBUG_KEY, "Stage 2: Started redSlider at " + String.format("%02X", Integer.parseInt(redSliderValue.getText().toString())));

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
//                Log.d(DEBUG_KEY, "Stage 2: Stopped redSlider at " + String.format("%02X", Integer.parseInt(redSliderValue.getText().toString())));
            }
        });


        greenSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            TextView greenSliderValue = (TextView) findViewById(R.id.greenSliderValue);

            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                titleBarColorGreen = progressValue;
                toolBarBackgroundColor = Color.rgb(titleBarColorRed, titleBarColorGreen, titleBarColorBlue);
                toolbar.setBackgroundColor(toolBarBackgroundColor);
                greenSliderValue.setText(formatter.format(titleBarColorGreen));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
//                Log.d(DEBUG_KEY, "Stage 2: Started greenSlider at " + String.format("%02X", Integer.parseInt(greenSliderValue.getText().toString())));
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
//                Log.d(DEBUG_KEY, "Stage 2: Stopped greenSlider at " + String.format("%02X", Integer.parseInt(greenSliderValue.getText().toString())));
            }
        });

        blueSlider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            TextView blueSliderValue = (TextView) findViewById(R.id.blueSliderValue);

            @Override
            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                titleBarColorBlue = progressValue;
                toolBarBackgroundColor = Color.rgb(titleBarColorRed, titleBarColorGreen, titleBarColorBlue);
                toolbar.setBackgroundColor(toolBarBackgroundColor);
                blueSliderValue.setText(formatter.format(titleBarColorBlue));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
//                Log.d(DEBUG_KEY, "Stage 2: Started blueSlider at " + String.format("%02X", Integer.parseInt(blueSliderValue.getText().toString())));
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
//                Log.d(DEBUG_KEY, "Stage 2: Stopped blueSlider at " + String.format("%02X", Integer.parseInt(blueSliderValue.getText().toString())));
            }
        });


    }

    public void closeActivity() {
        Intent intent = new Intent();
        intent.putExtra("TitleBarColor", toolBarBackgroundColor);
        intent.putExtra("SaveBattery",saveBattery);
        setResult(RESULT_OK, intent);
//        Log.d(DEBUG_KEY, "Stage 3: toolBarBackgroundColor = " + String.format("%06X", toolBarBackgroundColor));
        finish();
    }

    public void onBackPressed() {
        closeActivity();

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                closeActivity();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
