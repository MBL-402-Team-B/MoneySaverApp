package edu.phoenix.mbl402.moneysaverapp;

import java.util.Date;

public class IncomeItem {

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
    private String incomeSource;
    private double netAmount;
    private int periodicity;
    private int dayOfMonth;
    private Date receivedDate;


    public IncomeItem(String source, double amount, int period, int dateInMonth) {
        // Reoccurring income item constructor
        setIncomeSource(source);
        setNetAmount(amount);
        setDayOfMonth(dateInMonth);
        setPeriodicity(period);
    }

    public IncomeItem(String source, double amount, Date received) {
        // One-time income item constructor
        incomeSource = source;
        netAmount = amount;
        setPeriodicity(ONE_TIME);
        setReceivedDate(received);
    }

    public String getIncomeSource() {
        return incomeSource;
    }

    public void setIncomeSource(String incomeSource) {
        this.incomeSource = incomeSource;
    }

    public double getNetAmount() {
        return netAmount;
    }

    public void setNetAmount(double netAmount) {
        this.netAmount = netAmount;
    }

    public int getPeriodicity() {
        return periodicity;
    }

    public void setPeriodicity(int period) {
        if (period != ONE_TIME &&
                period != WEEKLY &&
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

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public Date getReceivedDate() {
        return receivedDate;
    }

    public void setReceivedDate(Date receivedDate) {
        this.receivedDate = receivedDate;
    }
}
