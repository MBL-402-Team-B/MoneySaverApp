package edu.phoenix.mbl402.moneysaverapp;

import java.net.URL;
import java.util.Date;

// For storage of Financial institution accounts that may contain money. Not for loans or debts.
public class BankAccount {

    private String bankName;
    private String accountName;
    private String accountType;
    private double accountBalance;
    private Date asOfDate;
    private URL bankWebSite;

    public BankAccount(String bank, String acctName, String acctType, double balance, Date asOf, URL webSite){
        setBankName( bank);
        setAccountName(acctName);
        setAccountType(acctType);
        setAccountBalance(balance);   // We'll assume a negative balance is possible
        setAsOfDate(asOf);
        setBankWebSite(webSite);

    }
    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public double getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(double accountBalance) {
        this.accountBalance = accountBalance;
    }

    public Date getAsOfDate() {
        return asOfDate;
    }

    public void setAsOfDate(Date asOfDate) {
        this.asOfDate = asOfDate;
    }

    public URL getBankWebSite() {
        return bankWebSite;
    }

    public void setBankWebSite(URL bankWebSite) {
        this.bankWebSite = bankWebSite;
    }

}
