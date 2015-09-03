package com.lull.burnedcaloriescalculator;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.SeekBar;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView.OnEditorActionListener;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import java.text.NumberFormat;


public class BurnedCaloriesCalculatorActivity extends AppCompatActivity {

    // declare variables for widgets
    private TextView caloriesBurnedTextView;
    private EditText weightEditText;
    private EditText nameEditText;
    private TextView BMITextView;
    private TextView milesRanTextView;
    private SeekBar milesRanSeekbar;
    private Spinner heightFeetSpinner;
    private Spinner heightInchesSpinner;

    //declare variables for calculations
    private float caloriesBurned = 0.0f;
    private float weight = 0.00f;
    private int milesRan = 1;
    private int height = 0;
    private float bmi = 0.0f;
    private String weightEditTextString;
    private String nameEditTextString;
    private int heightFeetPosition = 0;
    private int heightInchesPosition = 0;
    NumberFormat format = NumberFormat.getNumberInstance();

    // set up shared preferences variable
    private SharedPreferences savedValues;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_burned_calories_calculator);

        //get references for widgets

        caloriesBurnedTextView = (TextView) findViewById(R.id.caloriesBurnedTextView);
        weightEditText = (EditText) findViewById(R.id.weightEditText);
        nameEditText = (EditText) findViewById(R.id.nameEditText);
        BMITextView = (TextView) findViewById(R.id.BMITextView);
        milesRanTextView = (TextView) findViewById(R.id.milesRanTextView);
        milesRanSeekbar = (SeekBar) findViewById(R.id.milesRanSeekbar);
        heightFeetSpinner = (Spinner) findViewById(R.id.heightFeetSpinner);
        heightInchesSpinner = (Spinner) findViewById(R.id.heightInchesSpinner);

        // set up array adapter for spinners

        ArrayAdapter<CharSequence> feetAdapter = ArrayAdapter.createFromResource(
                this, R.array.height_feet_array, android.R.layout.simple_spinner_item);
        feetAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        heightFeetSpinner.setAdapter(feetAdapter);


        ArrayAdapter<CharSequence> inchesAdapter = ArrayAdapter.createFromResource(
                this, R.array.height_inches_array, android.R.layout.simple_spinner_item);
        inchesAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item);
        heightInchesSpinner.setAdapter(inchesAdapter);

        //wire widgets for events

        weightEditText.setOnEditorActionListener(editorActionListener);
        milesRanSeekbar.setOnSeekBarChangeListener(seekBarChangeListener);

        // set up a savedValues file for OnPause/OnResume event

        savedValues = getSharedPreferences("SavedValues", MODE_PRIVATE);

    }

    @Override
    protected void onPause() {
        // save the instance variables
        nameEditTextString = nameEditText.getText().toString();
        Editor editor = savedValues.edit();
        editor.putString("weightEditTextString", weightEditTextString);
        editor.putString("nameEditTextString", nameEditTextString);
        editor.putInt("milesRan", milesRan);
        editor.putInt("heightFeetPosition", heightFeetPosition);
        editor.putInt("heightInchesPosition", heightInchesPosition);
        editor.commit();

        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();

        weightEditTextString = savedValues.getString("weightEditTextString", "000");
        milesRan = savedValues.getInt("milesRan", 1);
        heightFeetPosition = savedValues.getInt("heightFeetPosition", 0);
        heightInchesPosition = savedValues.getInt("heightInchesPosition", 0);
        nameEditTextString = savedValues.getString("nameEditTextString", "");

        weightEditText.setText(weightEditTextString);
        milesRanTextView.setText(format.format(milesRan));
        heightFeetSpinner.setSelection(heightFeetPosition);
        heightInchesSpinner.setSelection(heightInchesPosition);
        nameEditText.setText(nameEditTextString);
        milesRanSeekbar.setProgress(milesRan);

    }

    public void calculateCaloriesAndBMI(){
        weightEditTextString = weightEditText.getText().toString();
        nameEditTextString = nameEditText.getText().toString();
        //check for null or empty value in weightEditText field
        if (weightEditTextString != "") {

            weight = Integer.parseInt(weightEditText.getText().toString());
            heightFeetPosition = heightFeetSpinner.getSelectedItemPosition();
            heightInchesPosition = heightInchesSpinner.getSelectedItemPosition();
            height = (((heightFeetPosition + 1) * 12) + (heightInchesPosition + 1));
            milesRan = milesRanSeekbar.getProgress();

            caloriesBurned = 0.75f * weight * milesRan;
            bmi = (weight * 703) / ((height) * (height));

            caloriesBurnedTextView.setText(format.format(caloriesBurned));
            BMITextView.setText(format.format(bmi));
        }

    }

    public OnEditorActionListener editorActionListener = new OnEditorActionListener() {
        @Override
        public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
            if (actionId == EditorInfo.IME_ACTION_DONE || actionId == EditorInfo.IME_ACTION_UNSPECIFIED) {

                calculateCaloriesAndBMI();

            }
            return false;

        }
    };

    public OnSeekBarChangeListener seekBarChangeListener = new OnSeekBarChangeListener() {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            milesRanTextView.setText(format.format(milesRanSeekbar.getProgress()));
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {
            calculateCaloriesAndBMI();
        }
    };

}
