package edu.phoenix.mbl402.moneysaverapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.net.MalformedURLException;


public class MainActivity extends AppCompatActivity {
    private static Toolbar toolbar;
    private int toolBarBackgroundColor;
    private static final String DEBUG_TAG = "DEBUG_Main_Activity";
    int backButtonCount;
    public Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        backButtonCount = 0;

        DataIO.loadUserPreferences(this);
        toolBarBackgroundColor = UserPreferences.getBackgroundColor();
        toolbar.setBackgroundColor(toolBarBackgroundColor);
//        Log.d(DEBUG_TAG, "Stage 1: toolBarBackgroundColor = " + String.format("%06X", toolBarBackgroundColor));

        context = this;
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MapsActivity.class);
                Log.i(DEBUG_TAG, "********************start Maps Activity ***************************");
                startActivity(intent);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        DataIO.loadUserPreferences(this);
        toolBarBackgroundColor = UserPreferences.getBackgroundColor();
        toolbar.setBackgroundColor(toolBarBackgroundColor);
//        Log.d(DEBUG_TAG, "Stage 4: toolBarBackgroundColor = " + String.format("%06X", toolBarBackgroundColor));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();


        //noinspection SimplifiableIfStatement

        switch (id) {
            case R.id.action_settings: {
                Intent intent = new Intent(this, SettingsActivity.class);
                int requestCode = 1;
//                Log.d(DEBUG_TAG, "Stage 2: toolBarBackgroundColor = " + String.format("%06X", toolBarBackgroundColor));
                startActivityForResult(intent, requestCode);
                return true;
            }

            case R.id.action_expenses: {
                Intent intent = new Intent(this, ReoccurringExpensesActicity.class);
                startActivity(intent);
                return true;
            }

            case R.id.action_planning: {
                Intent intent = new Intent(this, FinancialPlanningActivity.class);
                startActivity(intent);
                return true;
            }

            case R.id.action_createTestData: {
                try {
                    TestingTools.createTestData(this);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                return true;
            }

            case R.id.action_summary: {
                Intent intent = new Intent(this, SummaryActivity.class);
                int requestCode = 2;
                startActivityForResult(intent, requestCode);
                return true;
            }

            default:
                return super.onOptionsItemSelected(item);
        }


    }

    @Override
    public void onBackPressed() {
        if (backButtonCount >= 1) {
            Log.i(DEBUG_TAG, "User double-tapped back key");
            finish();
        } else {
            bigToast("Press the back button once again to close the application.");
            backButtonCount++;
        }
    }

    public void bigToast(String toastMessage) {
        // Simplifies toast message to make larger text, default to long length and auto injects context
        Toast toast = Toast.makeText(this, toastMessage, Toast.LENGTH_LONG);
        ViewGroup group = (ViewGroup) toast.getView();
        TextView messageTextView = (TextView) group.getChildAt(0);
        TypedValue outValue = new TypedValue();
        getResources().getValue(R.dimen.BigToastTextSizeInSP, outValue, true);
        float txtSize = outValue.getFloat();
        messageTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, txtSize);
        toast.show();
    }

    @Override
    public void onDestroy() {
        // disconnect any file resources

        // call the super last
        super.onDestroy();
        System.exit(0); // Ensures app completely shuts down on exit
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Collect data from the intent and use it
        switch (requestCode) {
            case 1: {
                // Returned from Settings Activity
//                Log.d(DEBUG_TAG, "Stage 3: UserPreferences.backgroundColor before change= " + String.format("%06X", UserPreferences.getBackgroundColor()));
                int toolBarBackgroundColor = data.getIntExtra("TitleBarColor", UserPreferences.getBackgroundColor());
//                Log.d(DEBUG_TAG, "Stage 3: toolBarBackgroundColor = " + String.format("%06X", toolBarBackgroundColor));
                UserPreferences.setBackgroundColor(toolBarBackgroundColor);
//                Log.d(DEBUG_TAG, "Stage 3: UserPreferences.backgroundColor after change= " + String.format("%06X", UserPreferences.getBackgroundColor()));
                boolean batSav = data.getBooleanExtra("SaveBattery", true);
                UserPreferences.setSaveBattery(batSav);

                DataIO.recordUserPreferences(this);
                toolbar.setBackgroundColor(toolBarBackgroundColor);
            }

            case 2: {
                // Returned from Summary Activity
            }
        }

    }

    //TODO: Need to implement a response to low battery state if UserPreferences.isSaveBattery()

//    private void activateTestLayout() {
//
//        setContentView(R.layout.testing_area);

//        FloatingActionButton fabTest = (FloatingActionButton) findViewById(R.id.fabTest);
//        fabTest.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //Replace with your own action
//
//            }
//        });
//    }
//
//
}
