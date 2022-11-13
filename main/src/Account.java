
/*****************************************************************
    CS4001301 Programming Languages                   
    
    Programming Assignment #1
    
    Java programming using subtype, subclass, and exception handling
    
    To compile: %> javac Application.java
    
    To execute: %> java Application

******************************************************************/

import java.util.*;

class BankingException extends Exception {
    BankingException() {
        super();
    }

    BankingException(String s) {
        super(s);
    }
}

interface BasicAccount {
    // Account name
    String name();

    // Account balance
    double balance();
}

interface WithdrawableAccount extends BasicAccount {
    double withdraw(double amount) throws BankingException;
}

interface DepositableAccount extends BasicAccount {
    double deposit(double amount) throws BankingException;
}

interface InterestableAccount extends BasicAccount {
    double computeInterest() throws BankingException;
}

interface FullFunctionalAccount extends WithdrawableAccount,
        DepositableAccount,
        InterestableAccount {
}

public abstract class Account {

    // protected variables to store commom attributes for every bank accounts
    protected String accountName;               // name of the account
    protected double accountBalance;            // money in the account
    protected double accountInterestRate;       // the interest rate of this account
    protected Date openDate;                    // the account open date
    protected Date lastInterestDate;            // last cal. interest data
    protected double defaultInterstRate = 0.12; //interest rate   

    Account(String s, double firstDeposit) {
        accountName = s;
        accountBalance = firstDeposit;
        accountInterestRate = defaultInterstRate;
        Calendar calendar = Calendar.getInstance();
        openDate = calendar.getTime();
        lastInterestDate = openDate;
    }

    Account(String s, double firstDeposit, Date firstDate) {
        accountName = s;
        accountBalance = firstDeposit;
        accountInterestRate = defaultInterstRate;
        openDate = firstDate;
        lastInterestDate = openDate;
    }

    // public methods for every bank accounts

    // return the account name
    public String name() {
        return (accountName);
    }

    // return the balance in the account
    public double balance() {
        return (accountBalance);
    }

    protected Date getCourrentTime() throws BankingException {
        Calendar calendar = Calendar.getInstance();
        return (calendar.getTime());
    }

    // deposit with the amount
    public double deposit(double amount) throws BankingException {
        accountBalance += amount;
        return (accountBalance);
    }

    public double withdraw(double amount) throws BankingException {
        return (withdraw(amount, getCourrentTime()));
    }

    // deposit with amount and the withdrawDate
    abstract double withdraw(double amount, Date withdrawDate) throws BankingException;

    abstract double computeInterest(Date interestDate) throws BankingException;

    public double computeInterest() throws BankingException {
        Calendar calendar = Calendar.getInstance();
        Date interestDate = calendar.getTime();
        return (computeInterest(interestDate));
    }

    protected int computeNumberOfDays(Date current, Date past) throws BankingException {
        int numberOfDays = (int) ((current.getTime()
                - past.getTime())
                / 86400000.0);

        return (numberOfDays);
    }

    protected int computeNumberOfMonths(Date current, Date past) throws BankingException {
        Calendar calendarCurrent = Calendar.getInstance();
        Calendar calendarPast = Calendar.getInstance();

        calendarCurrent.setTime(current);
        calendarPast.setTime(past);

        int yearCurrent = calendarCurrent.get(Calendar.YEAR);
        int monthCurrent = calendarCurrent.get(Calendar.MONTH);
        int yearPast = calendarCurrent.get(Calendar.YEAR);
        int monthPast = calendarCurrent.get(Calendar.MONTH);

        int numberOfMonths = ((yearCurrent - yearPast) * 12) + (monthCurrent - monthPast);

        return (numberOfMonths);
    }
}

/*
 * Derived class: CheckingAccount
 *
 * Description:
 * Interest is computed daily; there's no fee for
 * withdraw; there is a minimum balance of $1000.
 */

class CheckingAccount extends Account implements FullFunctionalAccount {

    CheckingAccount(String s, double firstDeposit) {
        super(s, firstDeposit);
    }

    CheckingAccount(String s, double firstDeposit, Date firstDate) {
        super(s, firstDeposit, firstDate);
    }

    public double deposit(double amount, Date deposiDate) throws BankingException{
        return super.deposit(amount);
    }

    public double withdraw(double amount, Date withdrawDate) throws BankingException {
        // minimum balance is 1000, raise exception if violated
        if ((accountBalance - amount) < 1000) {
            throw new BankingException("Underfraft from checking account name:" +
                    accountName);
        } else {
            accountBalance -= amount;
            return (accountBalance);
        }
    }

    public double computeInterest(Date interestDate) throws BankingException {
        if (interestDate.before(lastInterestDate)) {
            throw new BankingException("Invalid date to compute interest for account name" +
                    accountName);
        }

        int numberOfDays = computeNumberOfDays(interestDate, lastInterestDate);
        System.out.println("Number of days since last interest is " + numberOfDays);
        double interestEarned = (double) numberOfDays / 365.0 *
                accountInterestRate * accountBalance;
        System.out.println("Interest earned is " + interestEarned);
        lastInterestDate = interestDate;
        accountBalance += interestEarned;
        return (accountBalance);
    }
}

class SavingAccount extends Account implements FullFunctionalAccount {
    // define variable
    private int defaultFreeTransationTimes = 3;
    private int defaultTransationFee = 1;

    private int freeTransactionTimes;
    private Date lastTransactionDate;
    private Calendar calendar1, calendar2;

    SavingAccount(String s, double firstDeposit) {
        super(s, firstDeposit);
        freeTransactionTimes = defaultFreeTransationTimes;
        calendar1 = Calendar.getInstance();
        calendar2 = Calendar.getInstance();
        lastTransactionDate = calendar1.getTime();
    }

    SavingAccount(String s, double firstDeposit, Date firstDate) {
        super(s, firstDeposit, firstDate);
        freeTransactionTimes = defaultFreeTransationTimes;
        lastTransactionDate = firstDate;

    }

    // free transation time getter & setter
    public void setDefaultFreeTransationTimes(int times) {
        defaultFreeTransationTimes = times;
    }

    public int getDefaultFreeTransationTimes() {
        return (defaultFreeTransationTimes);
    }

    // transation fee getter & setter
    public void setDefaultTransationFee(int times) {
        defaultTransationFee = times;
    }

    public int getDefaultTransationFee() {
        return (defaultTransationFee);
    }

    private void checkFreeTransactionTimes(Date TransactionDate) throws BankingException {

        // debug meesage
        System.out.println("Free transation time => " + freeTransactionTimes);
        System.out.println("TransactionDate => " + TransactionDate);

        calendar1.setTime(TransactionDate);
        calendar2.setTime(lastTransactionDate);
        // not the same year
        if (calendar1.get(Calendar.YEAR) != calendar2.get(Calendar.YEAR)) {
            freeTransactionTimes = defaultFreeTransationTimes;
        }
        // the same year
        else {
            if (calendar1.get(Calendar.MONTH) != calendar2.get(Calendar.MONTH)) {
                freeTransactionTimes = defaultFreeTransationTimes;
            }
        }
    }

    public double withdraw(double amount, Date withdrawDate) throws BankingException {
        if (withdrawDate.before(lastTransactionDate)) {
            throw new BankingException("Invalid date to withdraw for account name" +
                    accountName);
        }

        // update free transaction times
        checkFreeTransactionTimes(withdrawDate);

        // minimum balance is less than 0(include don't have free transaction
        // times),raise exception if violated
        if (accountBalance - amount <= 0) {
            if (freeTransactionTimes <= 0) {
                throw new BankingException("Underfraft from checking account name:" +
                        accountName);
            }
        }

        if (freeTransactionTimes > 0) {
            accountBalance -= amount;
        } else {
            accountBalance -= (amount + 1);
        }

        freeTransactionTimes--;
        lastTransactionDate = withdrawDate;
        System.out.println("Free Transation times => " + freeTransactionTimes);
        return (accountBalance);
    }

    public double deposit(double amount, Date depositDate) throws BankingException {
        if (depositDate.before(lastTransactionDate)) {
            throw new BankingException("Invalid date to deposit for account name" +
                    accountName);
        }

        // update free transaction times
        checkFreeTransactionTimes(depositDate);

        if (freeTransactionTimes <= 0) {
            freeTransactionTimes--;
            return (super.deposit(amount - 1));
        } else {
            freeTransactionTimes--;
            return (super.deposit(amount));
        }
    }

    public double computeInterest(Date interestDate) throws BankingException {
        if (interestDate.before(lastInterestDate)) {
            throw new BankingException("Invalid date to compute interest for account name" +
                    accountName);
        }

        int numberOfMonths = computeNumberOfMonths(interestDate, lastInterestDate);
        System.out.println("Number of months since last interest is " + numberOfMonths);
        double interestEarned = (double) numberOfMonths / 12 *
                accountInterestRate * accountBalance;
        System.out.println("Interest earned is " + interestEarned);
        lastInterestDate = interestDate;
        accountBalance += interestEarned;
        return (accountBalance);
    }

}

class CDAccount extends Account implements FullFunctionalAccount {
    private final int defaultWithdrawFee = 250;
    private final int defaultDurationMonth = 12;
    private int durationMonth;

    
    CDAccount(String s, double firstDeposit) {
        super(s, firstDeposit);
        durationMonth = defaultDurationMonth;
    }

    CDAccount(String s, double firstDeposit, Date firstDate) {
        super(s, firstDeposit, firstDate);
        durationMonth = defaultDurationMonth;
    }

    CDAccount(String s, double firstDeposit, int _durationMonth) {
        super(s, firstDeposit);
        durationMonth = _durationMonth;
    }

    CDAccount(String s, double firstDeposit, Date firstDate, int _durationMonth) {
        super(s, firstDeposit, firstDate);
        durationMonth = _durationMonth;
    }

    public boolean checkWithdrawable() throws BankingException
    {
        return (checkWithdrawable(new Date()));
    }

    public boolean checkWithdrawable(Date checkDate) throws BankingException
    {
        Calendar expiredDate = Calendar.getInstance();
        expiredDate.setTime(openDate);
        expiredDate.add(Calendar.MONTH, durationMonth);

        return ((expiredDate.getTime()).before(checkDate));        
    }

    @Override
    public double deposit(double amount) throws BankingException {
        return deposit(amount, new Date());
    }

    double deposit(double amount, Date withdrawDate) throws BankingException {
        if (withdrawDate.before(openDate)) {
            throw new BankingException("Invalid date to withdraw for account name" +
                    accountName);
        }

        System.out.println(checkWithdrawable(withdrawDate));
        
        if(checkWithdrawable(withdrawDate))
        {
            amount += amount;
            return (amount);

        }
        else
        {
            throw new BankingException("Invalid date to withdraw, CD duration not reach for account name: " +
                    accountName);
        }
    }


    public double withdraw(double amount, Date withdrDate) throws BankingException {

        return super.withdraw(amount);
    }
    
    public double computeInterest(Date interestDate) throws BankingException {
        if (interestDate.before(lastInterestDate)) {
            throw new BankingException("Invalid date to compute interest for account name" +
                    accountName);
        }

        int numberOfMonths = computeNumberOfMonths(interestDate, lastInterestDate);
        System.out.println("Number of months since last interest is " + numberOfMonths);
        double interestEarned = (double) numberOfMonths / 12 *
                accountInterestRate * accountBalance;
        System.out.println("Interest earned is " + interestEarned);
        lastInterestDate = interestDate;
        accountBalance += interestEarned;
        return (accountBalance);
    }
}