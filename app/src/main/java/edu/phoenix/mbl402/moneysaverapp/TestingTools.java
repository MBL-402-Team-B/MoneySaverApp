package edu.phoenix.mbl402.moneysaverapp;


import android.content.Context;
import android.util.TypedValue;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by usrJoseph on 7/2/2016.
 * Used to create data to test with and any other tools we need
 */
public class TestingTools {

    public static void createTestData(Context context) throws MalformedURLException {
        GregorianCalendar gc;

        ArrayList<IncomeItem> incomeItems = new ArrayList<>();
        ArrayList<DebtItem> debtItems = new ArrayList<>();
        ArrayList<ReoccurringExpense> expenses = new ArrayList<>();
        ArrayList<BankAccount> accounts = new ArrayList<>();

        // Empty all existing data
        DataIO.recordIncome(context,incomeItems);
        DataIO.recordDebt(context,debtItems);
        DataIO.recordReoccurringExpense(context,expenses);
        DataIO.recordBankAccount(context,accounts);
        DataIO.recordFinancialPlan(context);

        // Create dummy data


        for (int i = 0; i < 3; i++) {
            String source = "Job No. " + Integer.toString(i);
            double amount = Math.random() * (double) ((i + 1) * 200);
            incomeItems.add( new IncomeItem(source, amount, IncomeItem.MONTHLY, i));
        }
        // Add some manual items not included in the loop above
        incomeItems.add( new IncomeItem("Quarterly Income Item of 3000", 3000.00, IncomeItem.QUARTERLY, 1));
        incomeItems.add( new IncomeItem("Mowed Lawn", 20.00,new GregorianCalendar(2016,1,15).getTime()));

        DataIO.recordIncome(context, incomeItems);

        for (int i = 0; i < 5; i++) {
            String source = "Money Tree";
            String accountName = "Loan No " + Integer.toString(i);
            double balance = Math.random() * (double) ((i + 1) * 500);
            double minPct = Math.random();
            double minAmt = 20.00;
            URL webLink = new URL("http://www.bofa.com/");
            debtItems.add(new DebtItem(source, accountName, balance, minPct, minAmt, i, webLink));
        }
        DataIO.recordDebt(context, debtItems);

        for (int i = 0; i < 10; i++) {
            String name = "Expense No. " + Integer.toString(i);
            String description = "Description of expense No. " + Integer.toString(i);
            double amount = Math.random() * (double) ((i + 1) * 80);
            expenses.add(new ReoccurringExpense(name, description, ReoccurringExpense.MONTHLY, amount));
        }
        DataIO.recordReoccurringExpense(context, expenses);

        for (int i = 0; i < 4; i++) {
            String bank = "Bank of America";
            String accountName = "Account No. "+ Integer.toString(i);
            double balance = Math.random() * (double) ((i + 1) * 500);
            gc = new GregorianCalendar();
            gc.set(GregorianCalendar.YEAR, 2015);
            gc.set(GregorianCalendar.DAY_OF_YEAR, i * 10);
            Date asOf = gc.getTime();
            URL webLink = new URL("http://www.bofa.com/");
            accounts.add(new BankAccount(bank,accountName,"accountType",balance,asOf,webLink));
        }
        DataIO.recordBankAccount(context, accounts);

        FinancialPlanning.setSavingsGoal(1000.00);
        FinancialPlanning.setLengthOfGoal(FinancialPlanning.ANNUAL);
        DataIO.recordFinancialPlan(context);

        Toast toast = Toast.makeText(context,"Created test data.",Toast.LENGTH_SHORT);
        ViewGroup group = (ViewGroup) toast.getView();
        TextView messageTextView = (TextView) group.getChildAt(0);
        messageTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,24);
        toast.show();


    }


}
