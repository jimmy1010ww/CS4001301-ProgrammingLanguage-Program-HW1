
/*****************************************************************
    CS4001301 Programming Languages                   
    
    Programming Assignment #1
    
    Java programming using subtype, subclass, and exception handling
    
    To compile: %> javac Application.java
    
    To execute: %> java Application

******************************************************************/

import java.util.*;

/**
 * Banking Excepotion Class
 *  - This class is used to handle the exception of the banking system
 **/
class BankingException extends Exception {
    BankingException() {
        super();
    }

    BankingException(String s) {
        super(s);
    }
}


/**
 * Basic Account Interface  
 * - This interface is used to define the basic account
 * - Contains two getter methods of name and account balance
 **/
interface BasicAccount {
    String name();
    double balance();
}

/**
 * Withdrawable Interface
 * - This interface is used to define the method withdrawable account
 * 
 * Interface:
 * withraw(double amount) : double
 **/
interface WithdrawableAccount extends BasicAccount {
    double withdraw(double amount) throws BankingException;
}

/**
 * Depositable Interface
 * - This interface is used to define the method of depositable account
 * 
 * Interface:
 * deposit(double amount) : double
 **/
interface DepositableAccount extends BasicAccount {
    double deposit(double amount) throws BankingException;
}

/**
 * Interestable Interface
 * - This interface is used to define the method of interestable account
 * 
 * Interface:
 * computerInterest() : double
 **/
interface InterestableAccount extends BasicAccount {
    double computeInterest() throws BankingException;
}

/**
 * Full Function Account Interface
 * - This interface is used to define the method of full function account
 * - Contains withdraw, deposit, and interest method
 */
interface FullFunctionalAccount extends WithdrawableAccount,
        DepositableAccount,
        InterestableAccount {
}


/**
 * Abstract Class : Account
 * 
 * Description:
 * Abstract class of account
 * 
 * Parameter Name
 * # accountName : String           - name of the account
 * # accountBalance : double        - balance of the account
 * # accountInterest : double       - interest of the account
 * # openDate : Date                - open date of the account
 * # lastInterestDate : Date        - last interest date of the account
 * # defaultInterest : double       - default interest of the account
 * 
 * Method
 * + name() : String
 * + balance() : double
 * + deposit(double amount) : double
 * + withdraw(double amount) : double
 * + withdraw(double amount, Date date) : double
 * + computeInterest() : double
 * + computerInterest(Date date) : double
 */
public abstract class Account {

    protected String accountName; 
    protected double accountBalance; 
    protected double accountInterestRate;
    protected Date openDate;
    protected Date lastInterestDate;
    protected double defaultInterstRate = 0.12; 

    public String name() { return (accountName); }
    public double balance() { return (accountBalance); }

    public double deposit(double amount) throws BankingException {
        accountBalance += amount;
        return (accountBalance);
    }

    abstract double withdraw(double amount, Date withdrawDate) throws BankingException;

    public double withdraw(double amount) throws BankingException {
        Date withdrawDate = new Date();
        return (withdraw(amount, withdrawDate));
    }

    abstract double computeInterest(Date interestDate) throws BankingException;

    public double computeInterest() throws BankingException {
        Date interestDate = new Date();
        return (computeInterest(interestDate));
    }
}

/*
 * Derived class: CheckingAccount
 *
 * Description:
 * interest is computed daily; there's no fee for withdrawals; 
 * there is a minimum balance of $1000.
 * 
 * Parameter Name:
 * - defaultMinimumBalance, : double      - default minimum balance of the account
 * - minimumBalance : double              - minimum balance of the account
 * 
 * Constructor:
 * + CheckingAccount(String s, double firstDeposit) : CheckingAccount
 * + CheckingAccount(String name, double balance, double interestRate, Date openDate)
 */

class CheckingAccount extends Account implements FullFunctionalAccount {
    private final double defaultMinimumBalance = 1000.0;
    private double minimumBalance;

    CheckingAccount(String s, double firstDeposit) {
        accountName = s;
        accountBalance = firstDeposit;
        accountInterestRate = defaultInterstRate;
        openDate = new Date();
        lastInterestDate = openDate;
        minimumBalance = defaultMinimumBalance;
    }

    CheckingAccount(String s, double firstDeposit, Date firstDate) {
        accountName = s;
        accountBalance = firstDeposit;
        accountInterestRate = defaultInterstRate;
        openDate = firstDate;
        lastInterestDate = openDate;
        minimumBalance = defaultMinimumBalance;
    }

    CheckingAccount(String s, double firstDeposit, Date firstDate, double _minimumBalance) {
        accountName = s;
        accountBalance = firstDeposit;
        accountInterestRate = defaultInterstRate;
        openDate = firstDate;
        lastInterestDate = openDate;
        minimumBalance = _minimumBalance;
    }

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