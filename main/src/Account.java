
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
    protected String accountName; // name of the account
    protected double accountBalance; // money in the account
    protected double accountInterestRate; // the interest rate of this account
    protected Date openDate; // the account open date
    protected Date lastInterestDate; // last cal. interest data
    protected double defaultInterstRate = 0.12; // interest rate

    // public methods for every bank accounts

    // return the name
    public String name() {
        return (accountName);
    }

    // return the balance
    public double balance() {
        return (accountBalance);
    }

    // deposit method
    public double deposit(double amount) throws BankingException {
        accountBalance += amount;
        return (accountBalance);
    }

    // withdraw method with custmoized withdraw date
    abstract double withdraw(double amount, Date withdrawDate) throws BankingException;

    // withdraw method with current date
    public double withdraw(double amount) throws BankingException {
        Date withdrawDate = new Date();
        return (withdraw(amount, withdrawDate));
    }

    // compute interest method with customized interest date
    abstract double computeInterest(Date interestDate) throws BankingException;

    // compute interest method with current date
    public double computeInterest() throws BankingException {
        Date interestDate = new Date();
        return (computeInterest(interestDate));
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
    private final double defaultMinimumBalance = 1000.0;
    private double minimumBalance;

    // constructor with default minimum balance and current date
    CheckingAccount(String s, double firstDeposit) {
        accountName = s;
        accountBalance = firstDeposit;
        accountInterestRate = defaultInterstRate;
        openDate = new Date();
        lastInterestDate = openDate;
        minimumBalance = defaultMinimumBalance;
    }

    // constructor with customized date and default minimum balance
    CheckingAccount(String s, double firstDeposit, Date firstDate) {
        accountName = s;
        accountBalance = firstDeposit;
        accountInterestRate = defaultInterstRate;
        openDate = firstDate;
        lastInterestDate = openDate;
        minimumBalance = defaultMinimumBalance;
    }

    // constructor with customized date and minimum balance
    CheckingAccount(String s, double firstDeposit, Date firstDate, double _minimumBalance) {
        accountName = s;
        accountBalance = firstDeposit;
        accountInterestRate = defaultInterstRate;
        openDate = firstDate;
        lastInterestDate = openDate;
        minimumBalance = _minimumBalance;
    }

    // deposit method with customized date
    public double deposit(double amount, Date deposiDate) throws BankingException {
        return super.deposit(amount);
    }

    public double withdraw(double amount, Date withdrawDate) throws BankingException {
        // minimum balance is 1000(default), raise exception if violated
        if ((accountBalance - amount) < minimumBalance) {
            throw new BankingException("Underfraft from checking account name:" + accountName
                    + '\n' + '\t' + "Minimum balance: " + minimumBalance);
        } else {
            accountBalance -= amount;
            return (accountBalance);
        }
    }

    protected int computeNumberOfDays(Date current, Date past) throws BankingException {
        int numberOfDays = (int) ((current.getTime()
                - past.getTime())
                / 86400000.0);

        return (numberOfDays);
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
    private final int defaultFreeTransationTimes = 3;
    private final int defaultTransationFee = 1;

    private int freeTransactionTimes;
    private int transactionFee;
    private int freeTransactionTimesCounter;
    private Date lastTransactionDate;
    private Calendar calendar1, calendar2;

    // default constructor
    SavingAccount(String s, double firstDeposit) {
        accountName = s;
        accountBalance = firstDeposit;
        accountInterestRate = defaultInterstRate;
        Calendar calendar = Calendar.getInstance();
        openDate = calendar.getTime();
        lastInterestDate = openDate;
        freeTransactionTimes = defaultFreeTransationTimes;
        transactionFee = defaultTransationFee;
        freeTransactionTimesCounter = freeTransactionTimes;
        initCalendar();
        lastTransactionDate = calendar1.getTime();

    }

    // constructor with current time and customize free transaction times &
    // transaction fee
    SavingAccount(String s, double firstDeposit, int freeTimes, int fee) {
        accountName = s;
        accountBalance = firstDeposit;
        accountInterestRate = defaultInterstRate;
        Calendar calendar = Calendar.getInstance();
        openDate = calendar.getTime();
        lastInterestDate = openDate;
        freeTransactionTimes = freeTimes;
        transactionFee = fee;
        freeTransactionTimesCounter = freeTransactionTimes;
        initCalendar();
        lastTransactionDate = calendar1.getTime();
    }

    // constructor with customize open date and default free transaction times &
    // transaction fee
    SavingAccount(String s, double firstDeposit, Date firstDate) {
        accountName = s;
        accountBalance = firstDeposit;
        accountInterestRate = defaultInterstRate;
        openDate = firstDate;
        lastInterestDate = openDate;
        freeTransactionTimes = defaultFreeTransationTimes;
        transactionFee = defaultTransationFee;
        freeTransactionTimesCounter = freeTransactionTimes;
        initCalendar();
        lastTransactionDate = firstDate;
    }

    // constructor with open date & free transaction times & transaction fee
    SavingAccount(String s, double firstDeposit, Date firstDate, int freeTimes, int fee) {
        accountName = s;
        accountBalance = firstDeposit;
        accountInterestRate = defaultInterstRate;
        openDate = firstDate;
        lastInterestDate = openDate;
        freeTransactionTimes = freeTimes;
        transactionFee = fee;
        freeTransactionTimesCounter = freeTransactionTimes;
        initCalendar();
        lastTransactionDate = firstDate;
    }

    private void initCalendar() {
        calendar1 = Calendar.getInstance();
        calendar2 = Calendar.getInstance();
    }

    /* free transation time getter & setter */
    public void setFreeTransationTimes(int times) {
        freeTransactionTimes = times;
    }

    public int getFreeTransationTimes() {
        return (freeTransactionTimes);
    }

    /* transation fee getter & setter */
    public void setTransationFee(int times) {
        transactionFee = times;
    }

    public int getTransationFee() {
        return (transactionFee);
    }

    private void checkFreeTransactionTimes(Date TransactionDate) throws BankingException {
        // exception => invalid date
        if (TransactionDate.before(lastTransactionDate)) {
            throw new BankingException("Invalid transaction date for account name" + accountName);
        }

        calendar1.setTime(TransactionDate);
        calendar2.setTime(lastTransactionDate);
        // not the same year
        if (calendar1.get(Calendar.YEAR) != calendar2.get(Calendar.YEAR)) {
            freeTransactionTimesCounter = freeTransactionTimes;
        }
        // the same year
        else {
            if (calendar1.get(Calendar.MONTH) != calendar2.get(Calendar.MONTH)) {
                freeTransactionTimesCounter = freeTransactionTimes;
            }
        }
    }

    public double withdraw(double amount, Date withdrawDate) throws BankingException {

        // exception => invalid date
        if (withdrawDate.before(lastTransactionDate)) {
            throw new BankingException("Invalid date to withdraw for account name" + accountName);
        }
        // exception => invalid amount
        else if (amount < 0) {
            throw new BankingException("Invalid amount to withdraw for account name" + accountName);
        } else if (accountBalance - amount < 0) {
            throw new BankingException("Underfraft from saving account name:" + accountName);
        }

        // update free transaction times
        checkFreeTransactionTimes(withdrawDate);

        // minimum balance is less than 0(include don't have free transaction
        // times),raise exception if violated
        if (accountBalance - amount <= 0) {
            if (freeTransactionTimesCounter <= 0) {
                throw new BankingException("Underfraft from checking account name:" +
                        accountName);
            }
        }

        // have free transaction times to withdraw
        if (freeTransactionTimesCounter > 0) {
            accountBalance -= amount;
        }
        // don't have free transaction times to withdraw
        else {
            System.out.println("You don't have enough free Transaction fee to withdraw, so we will cost $"
                    + transactionFee + " from your account");
            accountBalance -= (amount + transactionFee);
        }

        freeTransactionTimesCounter--;
        lastTransactionDate = withdrawDate;
        return (accountBalance);
    }

    @Override
    public double deposit(double amount) throws BankingException {
        if (amount < 0) {
            throw new BankingException("Invalid amount to deposit for account name" + accountName);
        }

        // update free transaction times
        checkFreeTransactionTimes(new Date());

        System.out.println("Free transaction times counter is " + freeTransactionTimesCounter);

        // have free transaction times to deposit
        if (freeTransactionTimesCounter > 0) {
            freeTransactionTimesCounter--;
            accountBalance += (amount);
            return (accountBalance);
        } else {
            freeTransactionTimesCounter--;
            System.out.println("You don't have enough free Transaction fee to withdraw, so we will cost $"
                    + transactionFee + " from your account");
            accountBalance += (amount - transactionFee);
            return (accountBalance);

        }
    }

    public double deposit(double amount, Date depositDate) throws BankingException {
        // exception => invalid date
        if (depositDate.before(lastTransactionDate)) {
            throw new BankingException("Invalid date to deposit for account name" + accountName);
        }
        // exception => invalid amount
        else if (amount < 0) {
            throw new BankingException("Invalid amount to deposit for account name" + accountName);
        }

        // update free transaction times
        checkFreeTransactionTimes(depositDate);

        System.out.println("Free transaction times counter is " + freeTransactionTimesCounter);

        // have free transaction times to deposit
        if (freeTransactionTimesCounter > 0) {
            freeTransactionTimesCounter--;
            accountBalance += amount;
            return (accountBalance);
        } else {
            freeTransactionTimesCounter--;
            System.out.println("You don't have enough free Transaction fee to withdraw, so we will take "
                    + transactionFee + " from your account");
            accountBalance += (amount - transactionFee);
            return (accountBalance);

        }
    }

    protected int computeNumberOfMonths(Date current, Date past) throws BankingException {
        Calendar calendarCurrent = Calendar.getInstance();
        Calendar calendarPast = Calendar.getInstance();

        calendarCurrent.setTime(current);
        calendarPast.setTime(past);

        int yearCurrent = calendarCurrent.get(Calendar.YEAR);
        int monthCurrent = calendarCurrent.get(Calendar.MONTH);
        int yearPast = calendarPast.get(Calendar.YEAR);
        int monthPast = calendarPast.get(Calendar.MONTH);

        int numberOfMonths = ((yearCurrent - yearPast) * 12) + (monthCurrent - monthPast);

        return (numberOfMonths);
    }

    public double computeInterest(Date interestDate) throws BankingException {
        if (interestDate.before(lastInterestDate)) {
            throw new BankingException("Invalid date to compute interest for account name" + accountName);
        }

        int numberOfMonths = computeNumberOfMonths(interestDate, lastInterestDate);
        System.out.println("Number of months since last interest is " + numberOfMonths);
        double interestEarned = (double) numberOfMonths / 12 * accountInterestRate * accountBalance;
        System.out.println("Interest earned is " + interestEarned);
        lastInterestDate = interestDate;
        accountBalance += interestEarned;
        return (accountBalance);
    }

}

class CDAccount extends Account implements WithdrawableAccount, InterestableAccount {
    private final double defaultWithdrawFee = 250;
    private final int defaultDurationMonth = 12;
    private double withdrawFee;
    private int durationMonth;
    private Date expiredDate;

    // constructor with default withdraw fee & duration month
    CDAccount(String s, double firstDeposit) {
        accountName = s;
        accountBalance = firstDeposit;
        accountInterestRate = defaultInterstRate;
        Calendar calendar = Calendar.getInstance();
        openDate = calendar.getTime();
        lastInterestDate = openDate;
        withdrawFee = defaultWithdrawFee;
        durationMonth = defaultDurationMonth;
        computeExpiredDate();
    }

    // constructor with default withdraw fee & duration month customize open date
    CDAccount(String s, double firstDeposit, Date firstDate) {
        accountName = s;
        accountBalance = firstDeposit;
        accountInterestRate = defaultInterstRate;
        openDate = firstDate;
        lastInterestDate = openDate;
        withdrawFee = defaultWithdrawFee;
        durationMonth = defaultDurationMonth;
        computeExpiredDate();
    }

    CDAccount(String s, double firstDeposit, int _withdrawFee, int _durationMonth) {
        accountName = s;
        accountBalance = firstDeposit;
        accountInterestRate = defaultInterstRate;
        Calendar calendar = Calendar.getInstance();
        openDate = calendar.getTime();
        lastInterestDate = openDate;
        withdrawFee = _withdrawFee;
        durationMonth = _durationMonth;
        computeExpiredDate();
    }

    CDAccount(String s, double firstDeposit, Date firstDate, int _withdrawFee, int _durationMonth) {
        accountName = s;
        accountBalance = firstDeposit;
        accountInterestRate = defaultInterstRate;
        openDate = firstDate;
        lastInterestDate = openDate;
        withdrawFee = _withdrawFee;
        durationMonth = _durationMonth;
        computeExpiredDate();
    }

    protected void computeExpiredDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(openDate);
        calendar.add(Calendar.MONTH, durationMonth);
        expiredDate = calendar.getTime();
    }

    public Date getExpiredDate() {
        return (expiredDate);
    }

    protected boolean isExpired() throws BankingException {
        return (isExpired(new Date()));
    }

    protected boolean isExpired(Date checkDate) throws BankingException {
        computeExpiredDate();
        return (expiredDate.before(checkDate));
    }

    @Override
    public double deposit(double amount) throws BankingException {
        return deposit(amount, new Date());
    }

    double deposit(double amount, Date depositDate) throws BankingException {
        if (depositDate.before(openDate)) {
            throw new BankingException("Invalid date to withdraw for account name" + accountName);
        } else if (amount < 0) {
            throw new BankingException("Invalid amount to deposit for account name" + accountName
                    + '\n' + '\t' + "Try to deposit : " + amount);
        } else if (!isExpired(depositDate)) {
            throw new BankingException("Account wasn't expired, can't deposit for account name" + accountName
                    + '\n' + '\t' + "Expired date is " + expiredDate);
        }

        amount += amount;
        return (amount);
    }

    public double withdraw(double amount, Date withdrDate) throws BankingException {
        if (withdrDate.before(openDate)) {
            throw new BankingException("Invalid date to withdraw for account name" + accountName);
        } else if (amount < 0) {
            throw new BankingException("Invalid amount to deposit for account name " + accountName
                    + '\n' + '\t' + "Try to withdraw : " + amount);
        } else if (accountBalance - amount < 0) {
            throw new BankingException("Underfraft from checking account name:" + accountName);
        }

        if (isExpired(withdrDate)) {
            accountBalance -= amount;
            return (accountBalance);
        } else {
            if (accountBalance - (amount + withdrawFee) < 0) {
                throw new BankingException("Underfraft from checking account name:" + accountName
                        + '\n' + '\t' + "wuthdraw fee is " + withdrawFee);
            } else {
                accountBalance -= (amount + withdrawFee);
                return (accountBalance);
            }
        }

    }

    protected int computeNumberOfMonths(Date current, Date past) throws BankingException {
        Calendar calendarCurrent = Calendar.getInstance();
        Calendar calendarPast = Calendar.getInstance();

        calendarCurrent.setTime(current);
        calendarPast.setTime(past);

        int yearCurrent = calendarCurrent.get(Calendar.YEAR);
        int monthCurrent = calendarCurrent.get(Calendar.MONTH);
        int yearPast = calendarPast.get(Calendar.YEAR);
        int monthPast = calendarPast.get(Calendar.MONTH);

        int numberOfMonths = ((yearCurrent - yearPast) * 12) + (monthCurrent - monthPast);

        return (numberOfMonths);
    }

    public double computeInterest(Date interestDate) throws BankingException {
        if (interestDate.before(lastInterestDate)) {
            throw new BankingException("Invalid date to compute interest for account name" + accountName);
        } else if (interestDate.before(expiredDate)) {
            throw new BankingException("Invalid date to compute interest for account name" + accountName
                    + '\n' + '\t' + "Expired date is " + expiredDate);
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

class LoanAccount extends Account implements DepositableAccount, InterestableAccount {

    LoanAccount(String s, double firstLoan) {
        accountName = s;
        accountBalance = firstLoan;
        accountInterestRate = defaultInterstRate;
        openDate = new Date();
        lastInterestDate = openDate;
    }

    LoanAccount(String s, double firstLoan, Date firstDate) {
        accountName = s;
        accountBalance = firstLoan;
        accountInterestRate = defaultInterstRate;
        openDate = firstDate;
        lastInterestDate = openDate;
    }

    @Override
    public double withdraw(double amount, Date withdrawDate) throws BankingException {
        throw new BankingException("You can't loan more money ");
    }

    public double deposit(double amount, Date deposiDate) throws BankingException {
        return super.deposit(amount);
    }

    protected int computeNumberOfMonths(Date current, Date past) throws BankingException {
        Calendar calendarCurrent = Calendar.getInstance();
        Calendar calendarPast = Calendar.getInstance();

        calendarCurrent.setTime(current);
        calendarPast.setTime(past);

        int yearCurrent = calendarCurrent.get(Calendar.YEAR);
        int monthCurrent = calendarCurrent.get(Calendar.MONTH);
        int yearPast = calendarPast.get(Calendar.YEAR);
        int monthPast = calendarPast.get(Calendar.MONTH);

        int numberOfMonths = ((yearCurrent - yearPast) * 12) + (monthCurrent - monthPast);

        return (numberOfMonths);
    }

    public double computeInterest(Date interestDate) throws BankingException {
        if (interestDate.before(lastInterestDate)) {
            throw new BankingException("Invalid date to compute interest for account name" +
                    accountName);
        }

        int numbnerOfMonths = computeNumberOfMonths(interestDate, lastInterestDate);
        System.out.println("Number of months since last interest is " + numbnerOfMonths);
        double interestEarned = (double) numbnerOfMonths / 365.0 *
                accountInterestRate * accountBalance;
        System.out.println("Interest earned is " + interestEarned);
        lastInterestDate = interestDate;
        accountBalance += interestEarned;
        return (accountBalance);
    }
}