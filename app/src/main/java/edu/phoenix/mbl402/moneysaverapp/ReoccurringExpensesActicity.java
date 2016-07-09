package edu.phoenix.mbl402.moneysaverapp;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class ReoccurringExpensesActicity extends AppCompatActivity {

    ArrayList<ReoccurringExpense> expenseList = new ArrayList<ReoccurringExpense>();

    String[] periods = {"1", "7", "14", "15", "30", "61", "91", "182", "365"};

    EditText editExpenseName;
    EditText editExpenseDescription;
    EditText editExpenseAmount;
    TextView message;
    Button recordButton;
    Button nextButton;
    Button deleteButton;
    Button cancelButton;
    int Index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reoccurring_expenses_acticity);

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
        deleteButton = (Button) findViewById(R.id.Delete_btn);
        cancelButton = (Button) findViewById(R.id.Cancel_bth);
        message = (TextView) findViewById(R.id.textView_message);

        DataIO.loadReoccurringExpense(this, expenseList);

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                cancelButton.setVisibility(View.VISIBLE);
                int itemNumber = Index+1;
                DataIO.loadReoccurringExpense(getApplicationContext(), expenseList);

                String Amount = Double.toString(expenseList.get(Index).getAmount());
                String Period = Integer.toString(expenseList.get(Index).getPeriodicity());

                editExpenseName.setText(expenseList.get(Index).getExpenseName());
                editExpenseDescription.setText(expenseList.get(Index).getDescriptionOfExpense());
                editExpenseAmount.setText(Amount);
                acTextView.setText(Period);

                Toast.makeText(getApplicationContext(), "item number "+itemNumber, Toast.LENGTH_SHORT).show();

                if (Index == expenseList.size()-1)
                {
                    Index = 0;
                }
                else
                {
                    Index++;
                }
            }
        });

        //
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Index > 0) {

                   // int itemNumber = Index;
                    expenseList.remove(Index-1);
                    DataIO.recordReoccurringExpense(getApplicationContext(), expenseList);

                    editExpenseName.setText(null);
                    editExpenseDescription.setText(null);
                    editExpenseAmount.setText(null);
                    acTextView.setText(null);

                    Toast.makeText(getApplicationContext(), "Item " + Index + " was deleted", Toast.LENGTH_SHORT).show();
                    Index = 0;



                }

                else
                {
                    Toast.makeText(getApplicationContext(), "nothing to delete!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Index = 0;
                editExpenseName.setText(null);
                editExpenseDescription.setText(null);
                editExpenseAmount.setText(null);
                acTextView.setText(null);

                cancelButton.setVisibility(View.INVISIBLE);
               // editExpensePeriod.setText(null);
            }
        });

        recordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int Period = Integer.parseInt(acTextView.getText().toString());
                if (Period != 1  && Period != 7 && Period != 14 && Period != 15 && Period != 30 && Period != 61 && Period != 91 && Period != 182 && Period != 365) {
                    Toast.makeText(getApplicationContext(), "INCORRECT PERIOD ENTRY", Toast.LENGTH_SHORT).show();
                    Toast.makeText(getApplicationContext(), "Enter 1, 7, 14, 15, 30, 61, 91, 182, or 365 for the period!!!", Toast.LENGTH_LONG).show();
                }

                else
                {

                    double Amount = Double.parseDouble(editExpenseAmount.getText().toString());
                    DataIO.recordReoccurringExpense(getApplicationContext(), expenseList);
                    ReoccurringExpense reoccurringExpense = new ReoccurringExpense(editExpenseName.getText().toString(),
                            editExpenseDescription.getText().toString(), Period, Amount);

                    Context context = getApplicationContext();

                    expenseList.add(reoccurringExpense);

                   // two.setText(expenseList.get(1).getExpenseName());
                    DataIO.recordReoccurringExpense(context, expenseList);

                    editExpenseName.setText(null);
                    editExpenseDescription.setText(null);
                    editExpenseAmount.setText(null);
                    acTextView.setText(null);

                    Toast.makeText(getApplicationContext(), "data recorded!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }


}
