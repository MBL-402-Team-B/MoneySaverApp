package edu.phoenix.mbl402.moneysaverapp;

import java.net.URL;

public class DebtItem {

// Instance data
    private String sourceInstitution;   // i.e. "Bank of America"
    private String accountName;         // i.e. "Auto Loan - 2015 Honda Accord"
    private double accountBalance;
    private double minPaymentPct;       // i.e. 0.02 means 2.0% of account balance
    private double minPaymentAmt;       // i.e. 20.00 if minPaymentPct is less than this amount
    private int dueDateInMonth;         // 5 if due on the 5th of each month
    private URL website;                // For online banking

    public DebtItem (String source, String acctName, double actBal, double minPct, double minAmt, int dueInMonth, URL webSiteLink){
        setSourceInstitution(source);
        setAccountName(acctName);
        setAccountBalance(actBal);
        setMinPaymentPct(minPct);
        setMinPaymentAmt(minAmt);
        setDueDateInMonth(dueInMonth);
        setWebsite(webSiteLink);
    }

    public String getSourceInstitution() {
        return sourceInstitution;
    }

    public void setSourceInstitution(String sourceInstitution) {
        this.sourceInstitution = sourceInstitution;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(double accountBalance) {
        this.accountBalance = accountBalance;
    }

    public double getMinPaymentPct() {
        return minPaymentPct;
    }

    public void setMinPaymentPct(double minPaymentPct) {
        this.minPaymentPct = minPaymentPct;
    }

    public double getMinPaymentAmt() {
        return minPaymentAmt;
    }

    public void setMinPaymentAmt(double minPaymentAmt) {
        this.minPaymentAmt = minPaymentAmt;
    }

    public int getDueDateInMonth() {
        return dueDateInMonth;
    }

    public void setDueDateInMonth(int dueDateInMonth) {
        this.dueDateInMonth = dueDateInMonth;
    }

    public URL getWebsite() {
        return website;
    }

    public void setWebsite(URL website) {
        this.website = website;
    }
}
