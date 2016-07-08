package edu.phoenix.mbl402.moneysaverapp;


public class FinancialPlanning {

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

// Static class data
    private static double savingsGoal;
    private static int lengthOfGoal;

    public FinancialPlanning(){
        lengthOfGoal = MONTHLY;
        savingsGoal = 0.00;
    }

    public static void setSavingsGoal(double goalAmount){
        savingsGoal = goalAmount;
    }

    public static double getSavingsGoal(){
        return savingsGoal;
    }

    public static void setLengthOfGoal(int lenOfGoalConstant){
        if (    lenOfGoalConstant != WEEKLY &&
                lenOfGoalConstant != BIWEEKLY &&
                lenOfGoalConstant != SEMIMONTHLY &&
                lenOfGoalConstant != MONTHLY &&
                lenOfGoalConstant != BIMONTHLY &&
                lenOfGoalConstant != QUARTERLY &&
                lenOfGoalConstant != SEMIANNUAL &&
                lenOfGoalConstant != ANNUAL) {
            lengthOfGoal = MONTHLY;
        } else {
            lengthOfGoal = lenOfGoalConstant;
        }
    }

    public static int getLengthOfGoal(){
        return lengthOfGoal;
    }

    public static double getMonthlySavingsGoal(){
        switch (lengthOfGoal){
            case WEEKLY: return (savingsGoal / WEEKLY) * MONTHLY;
            case BIWEEKLY: return (savingsGoal / WEEKLY) * MONTHLY;
            case MONTHLY: return savingsGoal;
            case SEMIMONTHLY: return (savingsGoal / SEMIMONTHLY) * MONTHLY;
            case BIMONTHLY: return (savingsGoal / BIMONTHLY) * MONTHLY;
            case QUARTERLY: return (savingsGoal / QUARTERLY) * MONTHLY;
            case SEMIANNUAL: return (savingsGoal / SEMIANNUAL) * MONTHLY;
            case ANNUAL: return (savingsGoal / ANNUAL) * MONTHLY;
        }

        return 0;

    }

}
