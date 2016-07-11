package edu.phoenix.mbl402.moneysaverapp;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;
public class FinancialPlanningActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_financial_planning);

        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        List<String> list = new ArrayList<String>();
        list.add("WEEKLY");
        list.add("BIWEEKLY");
        list.add("SEMIMONTHLY");
        list.add("MONTHLY");
        list.add("BIMONTHLY");
        list.add("QUARTERLY");
        list.add("SEMIANNUAL");
        list.add("ANNUAL");
        ArrayAdapter<String>adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

//        EditText editTextDollar = (EditText)findViewById(R.id.editText);
//        editTextDollar.setOnEditorActionListener(new TextView.OnEditorActionListener()) {
//            public boolean OnEditorAction (TextView textview,int i, KeyEvent keyevent){
//                boolean handled = false;
//                if (i == EditorInfo.IME_ACTION_NEXT) {
//                    String inputText = textview.getText().toString();
//                    Toast.makeText(MainActivity.this, "Your savings amount is: " + inputText,
//                            Toast.LENGTH_SHORT).show();
//
//                    InputMethodManager inputManager = (InputMethodManager)
//                            getSystemService(Context.INPUT_METHOD_SERVICE);
//                    inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
//                            InputMethodManager.HIDE_NOT_ALWAYS);
//
//                    handled = true;
//                }
//                return handled;
//            }
//        }
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
