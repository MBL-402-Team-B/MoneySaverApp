package edu.phoenix.mbl402.moneysaverapp;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;

import java.net.MalformedURLException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.ExpandableListView;
import android.widget.TextView;

public class SummaryActivity extends AppCompatActivity {
    private ExpandableListAdapter listAdapter;
    private ExpandableListView expListView;
    private List<String> listDataHeader;
    private HashMap<String, List<String>> listDataChild;
    private double totalProjected;
    private NumberFormat formatter = NumberFormat.getCurrencyInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);
        Toolbar toolbar = (Toolbar) findViewById(R.id.summary_toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }


        DataIO.loadUserPreferences(this);
        toolbar.setBackgroundColor(UserPreferences.getBackgroundColor());

        // code borrowed from http://www.androidhive.info/2013/07/android-expandable-list-view-tutorial/
        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.lvExp);
        prepareListData();
        listAdapter = new ExpandableListAdapter(this, listDataHeader, listDataChild);
        expListView.setAdapter(listAdapter);
        // End of borrowed code

        TextView tvGoal = (TextView) findViewById(R.id.txtGoal);
        TextView tvProjected = (TextView) findViewById(R.id.txtProjected);


        double goal = FinancialPlanning.getMonthlySavingsGoal();
        String formattedText = formatter.format(goal);
        formattedText = "Goal: " + formattedText;
        tvGoal.setText(formattedText);
        formattedText = formatter.format(totalProjected);
        formattedText = "Projected: " + formattedText;
        tvProjected.setText(formattedText);
        if (totalProjected >= goal ){
            tvProjected.setTextColor(Color.GREEN);
        } else {
            tvProjected.setTextColor(Color.RED);
        }

    }

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding parent data
        ArrayList<BankAccount> accounts = new ArrayList<>();
        List<String> listAccountItem = new ArrayList<String>();
        ArrayList<DebtItem> debts = new ArrayList<>();
        List<String> debtItems = new ArrayList<String>();
        ArrayList<IncomeItem> incomeItems = new ArrayList<>();
        List<String> incomeItem = new ArrayList<String>();
        ArrayList<ReoccurringExpense> expenses = new ArrayList<>();
        List<String> expenseItems = new ArrayList<String>();
        DataIO.loadBankAccount(this, accounts);
        DataIO.loadIncome(this, incomeItems);
        DataIO.loadDebt(this, debts);
        DataIO.loadFinancialPlan(this);
        DataIO.loadReoccurringExpense(this, expenses);
        double totalAccounts = 0.00;
        double totalDebts = 0.00;
        double totalIncomePerMonth = 0.00;
        double totalDebtPaymentsPerMonth = 0.00;
        double totalExpensesPerMonth = 0.00;


        for (int i = 0; i < accounts.size(); i++) {
            totalAccounts += accounts.get(i).getAccountBalance();
            listAccountItem.add(accounts.get(i).getBankName() + " Acct " + accounts.get(i).getAccountName() + ": " + formatter.format(accounts.get(i).getAccountBalance()));
        }

        for (int i = 0; i < debts.size(); i++) {
            totalDebts += debts.get(i).getAccountBalance();
            totalDebtPaymentsPerMonth += debts.get(i).getMinPaymentAmt();
            double paymentDue = debts.get(i).getAccountBalance() * debts.get(i).getMinPaymentPct();
            if (paymentDue < debts.get(i).getMinPaymentAmt())
                paymentDue = debts.get(i).getMinPaymentAmt();
            if (paymentDue > debts.get(i).getAccountBalance())
                paymentDue = debts.get(i).getAccountBalance();
            debtItems.add(debts.get(i).getSourceInstitution() + " Acct " + debts.get(i).getAccountName() + ": " + formatter.format(debts.get(i).getAccountBalance()) + ", Due: " + formatter.format(paymentDue) + " / Month");

        }

        for (int i = 0; i < incomeItems.size(); i++) {
            totalIncomePerMonth += (incomeItems.get(i).getNetAmount() / incomeItems.get(i).getPeriodicity() * IncomeItem.MONTHLY);
            if (incomeItems.get(i).getPeriodicity() == IncomeItem.ONE_TIME) {
                incomeItem.add(incomeItems.get(i).getIncomeSource() + ": " + formatter.format(incomeItems.get(i).getNetAmount()) + " on " + incomeItems.get(i).getReceivedDate());
            } else {
                incomeItem.add(incomeItems.get(i).getIncomeSource() + ": " + formatter.format(incomeItems.get(i).getNetAmount() / incomeItems.get(i).getPeriodicity() * IncomeItem.MONTHLY) + " / Month");
            }

        }

        for (int i = 0; i < expenses.size(); i++) {
            totalExpensesPerMonth += (expenses.get(i).getAmount() / expenses.get(i).getPeriodicity() * ReoccurringExpense.MONTHLY);
            expenseItems.add(expenses.get(i).getExpenseName() + ": " + formatter.format(expenses.get(i).getAmount()) + " / Month");
        }

        totalProjected = totalIncomePerMonth - totalDebtPaymentsPerMonth - totalExpensesPerMonth;

        listDataHeader.add("Accounts: " + formatter.format(totalAccounts));
        listDataHeader.add("Income: " + formatter.format(totalIncomePerMonth));
        listDataHeader.add("Debt: " + formatter.format(totalDebts));
        listDataHeader.add("Expenses: " + formatter.format(totalExpensesPerMonth));


        listDataChild.put(listDataHeader.get(0), listAccountItem); // Header, Child data
        listDataChild.put(listDataHeader.get(1), incomeItem);
        listDataChild.put(listDataHeader.get(2), debtItems);
        listDataChild.put(listDataHeader.get(3), expenseItems);
    }
}
