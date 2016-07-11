package edu.phoenix.mbl402.moneysaverapp;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class ReoccurringExpensesActivity extends AppCompatActivity {

    ArrayList<ReoccurringExpense> expenseList = new ArrayList<>();  // arrayList used to record new data and load old data from SharedPreferences in DataIO class

    String[] periods = {"1", "7", "14", "15", "30", "61", "91", "182", "365"};  // an array of all optional periods the user can type in

    // set up edit texts, text views, buttons, and ints
    EditText editExpenseName;
    EditText editExpenseDescription;
    EditText editExpenseAmount;
    TextView message;
    TextView itemNumbertxt;
    Button recordButton;
    Button nextButton;
    Button backButton;
    Button deleteButton;
    Button cancelButton;
    int Index = 0;
    int nextClick = 0;
    int backClick = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reoccurring_expenses);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.select_dialog_singlechoice, periods);

        //Find TextView control
        final AutoCompleteTextView acTextView = (AutoCompleteTextView) findViewById(R.id.period);

        //Set the number of characters the user must type before the drop down list is shown
        acTextView.setThreshold(1);

        //Set the adapter
        acTextView.setAdapter(adapter);

        editExpenseName = (EditText) findViewById(R.id.edit_Expense_Name);
        editExpenseDescription = (EditText) findViewById(R.id.edit_Expense_Description);
        editExpenseAmount = (EditText) findViewById(R.id.edit_Expense_Amount);
        recordButton = (Button) findViewById(R.id.Record_btn);
        nextButton = (Button) findViewById(R.id.Next_btn);
        backButton = (Button) findViewById(R.id.Back_btn);
        deleteButton = (Button) findViewById(R.id.Delete_btn);
        cancelButton = (Button) findViewById(R.id.Cancel_bth);
        message = (TextView) findViewById(R.id.textView_message);
        itemNumbertxt = (TextView) findViewById(R.id.ItemNumber_txt);

        DataIO.loadReoccurringExpense(this, expenseList);   // load the current data in SharedPreferences and put in expensList

        // click the NEXT button to view all information for the next item in the list
        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                nextClick = 1;  // set to 1 so we know that the NEXT button was clicked and that we are making modifications

                DataIO.loadReoccurringExpense(getApplicationContext(), expenseList);    // load data

                // if there is nothing in the list, then a message will indicate there is nothing in expensList to modify. Continue as if NEXT was never clicked
                if (expenseList.size() == 0)
                {
                    Toast.makeText(getApplicationContext(), "error, there is no information availabale to edit!", Toast.LENGTH_SHORT).show();
                    nextClick = 0;
                }

                else    // there are items in the arrayList
                {

                    if (backClick == 1) // set BACK button to 0, pretending it wasn't clicked, and go forward one index
                    {
                        backClick = 0;
                        Index++;
                    }

                    if (Index == expenseList.size())   // if Index has reached it's top spot, start back at beginning of list
                    {
                        Index = 0; // reset index
                    }

                    cancelButton.setVisibility(View.VISIBLE);
                    deleteButton.setVisibility(View.VISIBLE);

                    DataIO.loadReoccurringExpense(getApplicationContext(), expenseList);    // load data
                    int itemNumber = Index+1;

                    // display data in the list pertaining to current index
                    String Amount = Double.toString(expenseList.get(Index).getAmount());
                    String Period = Integer.toString(expenseList.get(Index).getPeriodicity());
                    editExpenseName.setText(expenseList.get(Index).getExpenseName());
                    editExpenseDescription.setText(expenseList.get(Index).getDescriptionOfExpense());
                    editExpenseAmount.setText(Amount);
                    acTextView.setText(Period);

                    itemNumbertxt.setText("Expense item: " + itemNumber);
                    Index++;
                }
            }
        });

        // click the BACK button to view all information for the previous item in the list
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                backClick = 1;   // set to 1 so we know that the BACK button was clicked and that we are making modifications

                DataIO.loadReoccurringExpense(getApplicationContext(), expenseList);    // load data

                // if there is nothing in the list, then a message will indicate there is nothing in expensList to modify. Continue as if BACK was never clicked
                if (expenseList.size() == 0)
                {
                    Toast.makeText(getApplicationContext(), "error, there is no information availabale to edit!", Toast.LENGTH_SHORT).show();
                    backClick = 0;
                }

                else    // there are items in the arrayList
                {
                    if (nextClick == 1)   // set NEXT button to 0, pretending it wasn't clicked, and go back one index
                    {
                        nextClick = 0;
                        Index--;
                    }

                    Index--;
                    if (Index < 0)   // back at last item
                    {
                        Index = expenseList.size()-1;   // swing around to last item in list
                    }

                    cancelButton.setVisibility(View.VISIBLE);
                    deleteButton.setVisibility(View.VISIBLE);

                    DataIO.loadReoccurringExpense(getApplicationContext(), expenseList);    // load data
                    int itemNumber = Index+1;

                    // display data in the list pertaining to current index
                    String Amount = Double.toString(expenseList.get(Index).getAmount());
                    String Period = Integer.toString(expenseList.get(Index).getPeriodicity());
                    editExpenseName.setText(expenseList.get(Index).getExpenseName());
                    editExpenseDescription.setText(expenseList.get(Index).getDescriptionOfExpense());
                    editExpenseAmount.setText(Amount);
                    acTextView.setText(Period);

                    itemNumbertxt.setText("Expense item: " + itemNumber);
                }
            }
        });

        // delete current item in list that is displayed in the fields
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // if the NEXT button was last clicked, decrease index by one and remove in order to delete current item shown in fields
                if (nextClick == 1)
                {
                    expenseList.remove(Index-1);
                    DataIO.recordReoccurringExpense(getApplicationContext(), expenseList);

                    editExpenseName.setText(null);
                    editExpenseDescription.setText(null);
                    editExpenseAmount.setText(null);
                    acTextView.setText(null);

                    Toast.makeText(getApplicationContext(), "Item " + Index + " was deleted", Toast.LENGTH_SHORT).show();
                    Index = 0;
                    nextClick = 0;

                    cancelButton.setVisibility(View.INVISIBLE);
                    deleteButton.setVisibility(View.INVISIBLE);

                    itemNumbertxt.setText("");
                }

                // other wise the BACK button was last clicked, so we will remove the current item matching the current index
                else {

                    int itemNumber = Index + 1;
                    expenseList.remove(Index);
                    DataIO.recordReoccurringExpense(getApplicationContext(), expenseList);

                    editExpenseName.setText(null);
                    editExpenseDescription.setText(null);
                    editExpenseAmount.setText(null);
                    acTextView.setText(null);

                    Toast.makeText(getApplicationContext(), "Item " + itemNumber + " was deleted", Toast.LENGTH_SHORT).show();
                    Index = 0;


                    cancelButton.setVisibility(View.INVISIBLE);
                    deleteButton.setVisibility(View.INVISIBLE);

                    itemNumbertxt.setText("");
                }
            }
        });

        // click the CANCEL button if you decide not to modify or delete any current items
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                nextClick = 0;  // set to zero to pretend it was never clicked
                backClick = 0;  // set to zero to pretend it was never clicked
                Index = 0;

                // all fields will now appear blank
                editExpenseName.setText(null);
                editExpenseDescription.setText(null);
                editExpenseAmount.setText(null);
                acTextView.setText(null);

                cancelButton.setVisibility(View.INVISIBLE);
                deleteButton.setVisibility(View.INVISIBLE);
                itemNumbertxt.setText("");
            }
        });

        // record or update the data in SharedPreferences in the DataIO class
        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // if any one field is empty, display a message that no fields can be left empty
                if ((editExpenseName.getText().toString().isEmpty()) || (editExpenseDescription.getText().toString().isEmpty())
                        || (editExpenseAmount.getText().toString().isEmpty()) || (acTextView.getText().toString().isEmpty()))
                {
                    Toast.makeText(getApplicationContext(), "error, no fields can be left empty!", Toast.LENGTH_LONG).show();
                }

                // if all fields are filled in, continue
                else {
                    int Period = Integer.parseInt(acTextView.getText().toString());
                    double Amount = Double.parseDouble(editExpenseAmount.getText().toString());

                    if (nextClick == 1) // if NEXT was the last button clicked, decrease index and add information from fields into expenseList
                    {
                        expenseList.get(Index-1).setExpenseName(editExpenseName.getText().toString());
                        expenseList.get(Index-1).setDescriptionOfExpense(editExpenseDescription.getText().toString());
                        expenseList.get(Index-1).setAmount(Amount);
                        expenseList.get(Index-1).setPeriodicity(Period);

                        DataIO.recordReoccurringExpense(getApplicationContext(), expenseList);

                        Toast.makeText(getApplicationContext(), "data was changed and recorded!", Toast.LENGTH_LONG).show();
                    }

                    else if (backClick == 1)    // if BACK was the last button clicked, decrease index and add information from fields into expenseList
                    {
                        expenseList.get(Index).setExpenseName(editExpenseName.getText().toString());
                        expenseList.get(Index).setDescriptionOfExpense(editExpenseDescription.getText().toString());
                        expenseList.get(Index).setAmount(Amount);
                        expenseList.get(Index).setPeriodicity(Period);

                        DataIO.recordReoccurringExpense(getApplicationContext(), expenseList);

                        Toast.makeText(getApplicationContext(), "data was changed and recorded!", Toast.LENGTH_LONG).show();
                    }

                    else {

                        if (Period != 1 && Period != 7 && Period != 14 && Period != 15 && Period != 30 && Period != 61 && Period != 91 && Period != 182 && Period != 365) {
                            Toast.makeText(getApplicationContext(), "INCORRECT PERIOD ENTRY", Toast.LENGTH_SHORT).show();
                            Toast.makeText(getApplicationContext(), "Enter 1, 7, 14, 15, 30, 61, 91, 182, or 365 for the period!!!", Toast.LENGTH_LONG).show();
                        } else {


                            ReoccurringExpense reoccurringExpense = new ReoccurringExpense(editExpenseName.getText().toString(),
                                    editExpenseDescription.getText().toString(), Period, Amount);

                            Context context = getApplicationContext();

                            expenseList.add(reoccurringExpense);

                            DataIO.recordReoccurringExpense(context, expenseList);

                            // set fields back to empty
                            editExpenseName.setText(null);
                            editExpenseDescription.setText(null);
                            editExpenseAmount.setText(null);
                            acTextView.setText(null);

                            Toast.makeText(getApplicationContext(), "data recorded!", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });
    }


}
