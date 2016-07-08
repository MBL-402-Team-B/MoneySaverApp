package edu.phoenix.mbl402.moneysaverapp;


import android.widget.EditText;

public class ReoccurringExpense {
    // Class constants
    public static final int ONE_TIME = 1;
    public static final int WEEKLY = 7;
    public static final int BIWEEKLY = 14;
    public static final int SEMIMONTHLY = 15;
    public static final int MONTHLY = 30;
    public static final int BIMONTHLY = 61;
    public static final int QUARTERLY = 91;
    public static final int SEMIANNUAL = 182;
    public static final int ANNUAL = 365;

    // Instance data
    private String expenseName;
    private String descriptionOfExpense;
    private int periodicity;
    private double amount;

    public ReoccurringExpense(String name, String description, int period, double amt) {
        setExpenseName(name);
        setDescriptionOfExpense(description);
        setPeriodicity(period); // jump to setter because there is more than 1 line of code
        setAmount(amt);
    }

    public String getExpenseName() {
        return expenseName;
    }

    public void setExpenseName(String expenseName) {
        this.expenseName = expenseName;
    }

    public void setExpenseName(EditText txtExpenseName) {
        setExpenseName(txtExpenseName.toString());
    }

    public String getDescriptionOfExpense() {
        return descriptionOfExpense;
    }

    public void setDescriptionOfExpense(String descriptionOfExpense) {
        this.descriptionOfExpense = descriptionOfExpense;
    }

    public void setDescriptionOfExpense(EditText txtDescriptionOfExpense) {
        setDescriptionOfExpense( txtDescriptionOfExpense.toString());
    }

    public int getPeriodicity() {
        return periodicity;
    }

    public void setPeriodicity(int period) {
        if (period != WEEKLY &&
                period != BIWEEKLY &&
                period != SEMIMONTHLY &&
                period != MONTHLY &&
                period != BIMONTHLY &&
                period != QUARTERLY &&
                period != SEMIANNUAL &&
                period != ANNUAL) {
            this.periodicity = MONTHLY;
        } else {
            this.periodicity = period;
        }

    }


    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setAmount(int amount) {
        setAmount((double) amount);
    }

    public void setAmount(float amount) {
        setAmount((double) amount);
    }
}
