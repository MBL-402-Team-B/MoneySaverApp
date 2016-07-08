package edu.phoenix.mbl402.moneysaverapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class DataIO {

    private static final int ONE_TIME = 1;
    private static final int MONTHLY = 30;
    private static final int ANNUALLY = 365;

    private static final String PLAN_SAVINGS_GOAL = "Plan:SavingsGoal";
    private static final String PLAN_LEN_OF_GOAL = "Plan:LenGoal";

    private static final String INCOME_COUNT = "Income:Count";
    private static final String INCOME_SOURCE = "Income:Source";
    private static final String INCOME_NET = "Income:NetAmount";
    private static final String INCOME_PERIOD = "Income:Periodicity";
    private static final String INCOME_DAY_OF_MONTH = "Income:dayOfMonth";
    private static final String INCOME_RCV_DATE = "Income:receivedDate";

    private static final String DEBT_COUNT = "Debt:Count";
    private static final String DEBT_SOURCE = "Debt:Source";
    private static final String DEBT_NAME = "Debt:Name";
    private static final String DEBT_BALANCE = "Debt:Balance";
    private static final String DEBT_MIN_PERCENT = "Debt:MinPercent";
    private static final String DEBT_MIN_AMOUNT = "Debt:MinAmount";
    private static final String DEBT_DUE = "Debt:Due";
    private static final String DEBT_WEB = "Debt:Web";

    private static final String EXPENSE_COUNT = "Expense:Count";
    private static final String EXPENSE_NAME = "Expense:Name";
    private static final String EXPENSE_DESC = "Expense:Description";
    private static final String EXPENSE_PERIOD = "Expense:Periodicity";
    private static final String EXPENSE_AMOUNT = "Expense:Amount";

    private static final String BANK_COUNT = "Bank:Count";
    private static final String BANK_NAME = "Bank:Name";
    private static final String BANK_ACCT_NAME = "Bank:AccountName";
    private static final String BANK_ACCT_TYPE = "Bank:AccountType";
    private static final String BANK_ACCT_BAL = "Bank:AccountBalance";
    private static final String BANK_AS_OF_DATE = "Bank:AsOfDate";
    private static final String BANK_WEB = "Bank:WebSite";

    private static final String SETTINGS_TITLE_BAR_COLOR = "Settings:TitleBarColor";
    private static final String SETTINGS_SAVE_BATTERY = "Settings:SaveBattery";


    public DataIO() {
    }

    public static void recordFinancialPlan(Context context) {
        final SharedPreferences sharedPref = PreferenceManager.
                getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        String saveGoal = Double.toString(FinancialPlanning.getSavingsGoal());
        int saveLenOfGoal = FinancialPlanning.getLengthOfGoal();
        editor.putString(PLAN_SAVINGS_GOAL, saveGoal);
        editor.putInt(PLAN_LEN_OF_GOAL, saveLenOfGoal);
        editor.apply();
    }

    public static void loadFinancialPlan(Context context) {
        // Because the FinancialPlanning object is static, the reference to it as a parameter will
        // allow this method to update its values directly and not have to return anything.
        SharedPreferences sharedPref = PreferenceManager.
                getDefaultSharedPreferences(context);
        int loadedLenOfGoal = sharedPref.getInt(PLAN_LEN_OF_GOAL, MONTHLY);
        String loadedSavingsGoal = sharedPref.getString(PLAN_SAVINGS_GOAL, "0");
        FinancialPlanning.setLengthOfGoal(loadedLenOfGoal);
        FinancialPlanning.setSavingsGoal(Double.parseDouble(loadedSavingsGoal));
    }

    // Because SharedPreferences doesn't handle double or Date, they were converted to Strings
    public static void recordIncome(Context context, ArrayList<IncomeItem> incomeItems) {
        int oldCount, newCount;
        final SharedPreferences sharedPref = PreferenceManager.
                getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();

        // First need to remove any excess items stored in case the array has shrunken
        oldCount = sharedPref.getInt(INCOME_COUNT, 0);
        newCount = incomeItems.size();
        if (oldCount > newCount) {
            for (int i = newCount + 1; i <= oldCount; i++) {
                editor.remove(INCOME_SOURCE + Integer.toString(i));
                editor.remove(INCOME_NET + Integer.toString(i));
                editor.remove(INCOME_PERIOD + Integer.toString(i));
                editor.remove(INCOME_DAY_OF_MONTH + Integer.toString(i));
                editor.remove(INCOME_RCV_DATE + Integer.toString(i));
            }
        }
        // update changed or add new items
        for (int i = 1; i <= newCount; i++) {
            editor.putString(INCOME_SOURCE + Integer.toString(i), incomeItems.get(i - 1).getIncomeSource());
            editor.putString(INCOME_NET + Integer.toString(i), Double.toString(incomeItems.get(i - 1).getNetAmount()));
            editor.putInt(INCOME_PERIOD + Integer.toString(i), incomeItems.get(i - 1).getPeriodicity());
            editor.putInt(INCOME_DAY_OF_MONTH + Integer.toString(i), incomeItems.get(i - 1).getDayOfMonth());

            if (incomeItems.get(i - 1).getReceivedDate() != null) {
                editor.putString(INCOME_RCV_DATE + Integer.toString(i), incomeItems.get(i - 1).getReceivedDate().toString());
            } else {
                editor.putString(INCOME_RCV_DATE + Integer.toString(i), "");
            }
        }
        editor.putInt(INCOME_COUNT, newCount);
        editor.apply(); // Synchronous commit
    }

    public static void loadIncome(Context context, ArrayList<IncomeItem> incomeItems) {

        int newCount;
        String incomeSource = "";
        double netAmount = 0;
        int periodicity = ANNUALLY;
        int dayOfMonth = 1;
        Date receivedDate = new Date();
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy", Locale.US);

        final SharedPreferences sharedPref = PreferenceManager.
                getDefaultSharedPreferences(context);
        newCount = sharedPref.getInt(INCOME_COUNT, 0);
        incomeItems.clear();

        for (int i = 1; i <= newCount; i++) {
            incomeSource = sharedPref.getString(INCOME_SOURCE + Integer.toString(i), incomeSource);
            netAmount = Double.parseDouble(sharedPref.getString(INCOME_NET + Integer.toString(i), Double.toString(netAmount)));
            periodicity = sharedPref.getInt(INCOME_PERIOD + Integer.toString(i), periodicity);
            dayOfMonth = sharedPref.getInt(INCOME_DAY_OF_MONTH + Integer.toString(i), dayOfMonth);
            String newDate =
                    sharedPref.getString(INCOME_RCV_DATE + Integer.toString(i), receivedDate.toString());
            try {
                receivedDate = df.parse(newDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            if (periodicity > ONE_TIME) {
                incomeItems.add(new IncomeItem(incomeSource, netAmount, periodicity, dayOfMonth));
            } else {
                incomeItems.add(new IncomeItem(incomeSource, netAmount, receivedDate));
            }
        }
    }

    public static void recordDebt(Context context, ArrayList<DebtItem> debtItems) {
        int oldCount, newCount;
        final SharedPreferences sharedPref = PreferenceManager.
                getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();

        // First need to remove any excess items stored in case the array has shrunken
        oldCount = sharedPref.getInt(INCOME_COUNT, 0);
        newCount = debtItems.size();
        if (oldCount > newCount) {
            for (int i = newCount + 1; i <= oldCount; i++) {
                editor.remove(DEBT_SOURCE + Integer.toString(i));
                editor.remove(DEBT_NAME + Integer.toString(i));
                editor.remove(DEBT_BALANCE + Integer.toString(i));
                editor.remove(DEBT_MIN_PERCENT + Integer.toString(i));
                editor.remove(DEBT_MIN_AMOUNT + Integer.toString(i));
                editor.remove(DEBT_DUE + Integer.toString(i));
                editor.remove(DEBT_WEB + Integer.toString(i));
            }
        }
        // update changed or add new items
        for (int i = 1; i <= newCount; i++) {
            editor.putString(DEBT_SOURCE + Integer.toString(i), debtItems.get(i - 1).getSourceInstitution());
            editor.putString(DEBT_NAME + Integer.toString(i), debtItems.get(i - 1).getAccountName());
            editor.putString(DEBT_BALANCE + Integer.toString(i), Double.toString(debtItems.get(i - 1).getAccountBalance()));
            editor.putString(DEBT_MIN_PERCENT + Integer.toString(i), Double.toString(debtItems.get(i - 1).getMinPaymentPct()));
            editor.putString(DEBT_MIN_AMOUNT + Integer.toString(i), Double.toString(debtItems.get(i - 1).getMinPaymentAmt()));
            editor.putInt(DEBT_DUE + Integer.toString(i), debtItems.get(i - 1).getDueDateInMonth());
            editor.putString(DEBT_WEB + Integer.toString(i), debtItems.get(i - 1).getWebsite().toString());
        }
        editor.putInt(DEBT_COUNT, newCount);
        editor.apply(); // Synchronous commit
    }

    public static void loadDebt(Context context, ArrayList<DebtItem> debtItems) {
        // removed throws MalformedURLException
        int newCount;
        String debtSource = "";
        String accountName = "";
        double accountBalance = 0;
        double minPaymentPct = 0;
        double minPaymentAmt = 0;
        int dueDateInMonth = 1;
        URL website;

        final SharedPreferences sharedPref = PreferenceManager.
                getDefaultSharedPreferences(context);
        newCount = sharedPref.getInt(DEBT_COUNT, 0);
        debtItems.clear();

        for (int i = 1; i <= newCount; i++) {
            debtSource = sharedPref.getString(DEBT_SOURCE + Integer.toString(i), debtSource);
            accountName = sharedPref.getString(DEBT_NAME + Integer.toString(i), accountName);
            accountBalance = Double.parseDouble(sharedPref.getString(DEBT_BALANCE + Integer.toString(i), Double.toString(accountBalance)));
            minPaymentPct = Double.parseDouble(sharedPref.getString(DEBT_MIN_PERCENT + Integer.toString(i), Double.toString(minPaymentPct)));
            minPaymentAmt = Double.parseDouble(sharedPref.getString(DEBT_MIN_AMOUNT + Integer.toString(i), Double.toString(minPaymentAmt)));
            dueDateInMonth = sharedPref.getInt(DEBT_DUE + Integer.toString(i), dueDateInMonth);
            try {
                website = new URL(sharedPref.getString(DEBT_WEB + Integer.toString(i), "about:blank"));
            } catch (MalformedURLException e) {
                e.printStackTrace();
                website = null;
            }
            debtItems.add(new DebtItem(debtSource, accountName, accountBalance, minPaymentPct, minPaymentAmt, dueDateInMonth, website));
        }

    }

    public static void recordReoccurringExpense(Context context, ArrayList<ReoccurringExpense> expenses) {
        int oldCount, newCount;
        final SharedPreferences sharedPref = PreferenceManager.
                getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();

        // First need to remove any excess items stored in case the array has shrunken
        oldCount = sharedPref.getInt(INCOME_COUNT, 0);
        newCount = expenses.size();
        if (oldCount > newCount) {
            for (int i = newCount + 1; i <= oldCount; i++) {
                editor.remove(EXPENSE_NAME + Integer.toString(i));
                editor.remove(EXPENSE_DESC + Integer.toString(i));
                editor.remove(EXPENSE_PERIOD + Integer.toString(i));
                editor.remove(EXPENSE_AMOUNT + Integer.toString(i));
            }

        }

        // update changed or add new items
        for (int i = 1; i <= newCount; i++) {
            editor.putString(EXPENSE_NAME + Integer.toString(i), expenses.get(i - 1).getExpenseName());
            editor.putString(EXPENSE_DESC + Integer.toString(i), expenses.get(i - 1).getDescriptionOfExpense());
            editor.putInt(EXPENSE_PERIOD + Integer.toString(i), expenses.get(i - 1).getPeriodicity());
            editor.putString(EXPENSE_AMOUNT + Integer.toString(i), Double.toString(expenses.get(i - 1).getAmount()));
        }
        editor.putInt(EXPENSE_COUNT, newCount);
        editor.apply(); // Synchronous commit
    }

    public static void loadReoccurringExpense(Context context, ArrayList<ReoccurringExpense> expenses) {
        int newCount;
        String expenseName = "";
        String descriptionOfExpense = "";
        int periodicity = MONTHLY;
        double amount = 0;

        final SharedPreferences sharedPref = PreferenceManager.
                getDefaultSharedPreferences(context);
        newCount = sharedPref.getInt(EXPENSE_COUNT, 0);
        expenses.clear();
        for (int i = 1; i <= newCount; i++) {
            expenseName = sharedPref.getString(EXPENSE_NAME + Integer.toString(i), expenseName);
            descriptionOfExpense = sharedPref.getString(EXPENSE_DESC + Integer.toString(i), descriptionOfExpense);
            periodicity = sharedPref.getInt(EXPENSE_PERIOD + Integer.toString(i), periodicity);
            amount = Double.parseDouble(sharedPref.getString(EXPENSE_AMOUNT + Integer.toString(i), Double.toString(amount)));
            expenses.add(new ReoccurringExpense(expenseName, descriptionOfExpense, periodicity, amount));
        }
    }

    public static void recordBankAccount(Context context, ArrayList<BankAccount> accounts) {
        int oldCount, newCount;
        final SharedPreferences sharedPref = PreferenceManager.
                getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();

        // First need to remove any excess items stored in case the array has shrunken
        oldCount = sharedPref.getInt(BANK_COUNT, 0);
        newCount = accounts.size();
        if (oldCount > newCount) {
            for (int i = newCount + 1; i <= oldCount; i++) {
                editor.remove(BANK_NAME + Integer.toString(i));
                editor.remove(BANK_ACCT_NAME + Integer.toString(i));
                editor.remove(BANK_ACCT_TYPE + Integer.toString(i));
                editor.remove(BANK_ACCT_BAL + Integer.toString(i));
                editor.remove(BANK_AS_OF_DATE + Integer.toString(i));
                editor.remove(BANK_WEB + Integer.toString(i));
            }

        }

        // update changed or add new items
        for (int i = 1; i <= newCount; i++) {
            editor.putString(BANK_NAME + Integer.toString(i), accounts.get(i - 1).getBankName());
            editor.putString(BANK_ACCT_NAME + Integer.toString(i), accounts.get(i - 1).getAccountName());
            editor.putString(BANK_ACCT_TYPE + Integer.toString(i), accounts.get(i - 1).getAccountType());
            editor.putString(BANK_ACCT_BAL + Integer.toString(i), Double.toString(accounts.get(i - 1).getAccountBalance()));
            editor.putString(BANK_AS_OF_DATE + Integer.toString(i), accounts.get(i - 1).getAsOfDate().toString());
            editor.putString(BANK_WEB + Integer.toString(i), accounts.get(i - 1).getBankWebSite().toString());
        }
        editor.putInt(BANK_COUNT, newCount);
        editor.apply(); // Synchronous commit
    }

    public static void loadBankAccount(Context context, ArrayList<BankAccount> accounts) {
        // Removed throws MalformedURLException
        int newCount;
        String bankName = "";
        String accountName = "";
        String accountType = "";
        double accountBalance = 0;
        Date asOfDate = new Date();
        DateFormat df = new SimpleDateFormat("MM/dd/yyyy", Locale.US);
        URL bankWebSite;
        final SharedPreferences sharedPref = PreferenceManager.
                getDefaultSharedPreferences(context);
        newCount = sharedPref.getInt(BANK_COUNT, 0);
        accounts.clear();
        for (int i = 1; i <= newCount; i++) {
            bankName = sharedPref.getString(BANK_NAME + Integer.toString(i), bankName);
            accountName = sharedPref.getString(BANK_ACCT_NAME + Integer.toString(i), accountName);
            accountType = sharedPref.getString(BANK_ACCT_TYPE + Integer.toString(i), accountType);
            accountBalance = Double.parseDouble(sharedPref.getString(BANK_ACCT_BAL + Integer.toString(i), Double.toString(accountBalance)));
            String newDate = (sharedPref.getString(BANK_AS_OF_DATE + Integer.toString(i), asOfDate.toString()));
            try {
                asOfDate = df.parse(newDate);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                bankWebSite = new URL(sharedPref.getString(BANK_WEB + Integer.toString(i), "about:blank"));
            } catch (MalformedURLException e) {
                e.printStackTrace();
                bankWebSite = null;
            }
            accounts.add(new BankAccount(bankName, accountName, accountType, accountBalance, asOfDate, bankWebSite));
        }
    }

    public static void recordUserPreferences(Context context) {
        final SharedPreferences sharedPref = PreferenceManager.
                getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt(SETTINGS_TITLE_BAR_COLOR, UserPreferences.getBackgroundColor());
        editor.putBoolean(SETTINGS_SAVE_BATTERY, UserPreferences.isSaveBattery());
        editor.apply();

    }

    public static void loadUserPreferences(Context context) {
        SharedPreferences sharedPref = PreferenceManager.
                getDefaultSharedPreferences(context);
        UserPreferences.setBackgroundColor(sharedPref.getInt(SETTINGS_TITLE_BAR_COLOR, R.color.colorPrimary));
        UserPreferences.setSaveBattery(sharedPref.getBoolean(SETTINGS_SAVE_BATTERY, true));

    }


}