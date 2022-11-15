
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
 **/
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
 * Member variables:
 * # accountName : String           - name of the account
 * # accountBalance : double        - balance of the account
 * # accountInterest : double       - interest of the account
 * # openDate : Date                - open date of the account
 * # lastInterestDate : Date        - last interest date of the account
 * # defaultInterstRate : double    - default interest of the account
 * 
 * Method
 * + name() : String                                - get the name of the account
 * + balance() : double                             - get the balance of the account
 * + deposit(double amount) : double                - deposit the amount to the account
 * + withdraw(double amount) : double               - withdraw the amount from the account
 * + withdraw(double amount, Date date) : double    - withdraw the amount from the account with date
 * + computeInterest() : double                     - compute the interest of the account use cuurent date as interest date
 * + computerInterest(Date date) : double           - compute the interest of the account with specific date
 **/

public abstract class Account {

    protected String accountName; 
    protected double accountBalance; 
    protected double accountInterestRate;
    protected Date openDate;
    protected Date lastInterestDate;
    protected double defaultInterstRate = 0.12; 

    public String name() { return (accountName); }
    public double balance() { return (accountBalance); }

    abstract double deposit(double amount, Date withdrawDate) throws BankingException;

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

/**
 * Derived class: CheckingAccount
 *
 * Description:
 * interest is computed daily; there's no fee for withdrawals; 
 * there is a minimum balance of $1000.
 * 
 * Member variables::
 * - defaultMinimumBalance, : double      - default minimum balance of the account
 * - minimumBalance : double              - minimum balance of the account
 * 
 * Constructor:
 * + CheckingAccount(String s, double firstDeposit)
 * + CheckingAccount(String s, double firstDeposit, Date firstDate)
 * + CheckingAccount(String s, double firstDeposit, Date firstDate, double _minimumBalance)
 * 
 * Method:
 * + deposit(double amount, Date deposiDate) : double       - deposit the amount to the account with specific date
 * + withdraw(double amount, Date withdrawDate) : double    - withdraw the amount from the account with specific date
 * # computeNumberOfDays(Date current, Date past) : int     - compute the number of days between two dates
 * + computeInterest(Date interestDate) : double            - compute the daily of the account with specific date
 **/

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
        if (withdrawDate.before(openDate)){
            throw new BankingException("Invalid date to withdraw from Checking account" + '\n' + '\t' + 
                "Account name: " + accountName);
        }
        else if ((accountBalance - amount) < minimumBalance) {
            throw new BankingException("Underfraft from Checking account" + '\n' + '\t' + 
                "Account name: " + accountName + '\n' + '\t' + 
                "Withdraw amount: " + amount + '\n' + '\t' +
                "Minimum balance: " + minimumBalance);
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
            throw new BankingException("Invalid date to compute interest from Checking account" + '\n' + '\t' +
            "Account name: " + accountName + '\n' + '\t' +
            "Last interest date: " + lastInterestDate + '\n' + '\t' +
            "Interest date: " + interestDate);
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

/**
 * Derived class: SavingsAccount
 * 
 * Description:
 * monthly interest; fee of $1 for every transaction, 
 * except the first three per month are free; no minimum balance.
 * 
 * Member variables::
 * - defaultFreeTranscationTimes : int      - default free transaction times of the account
 * - defaultTransactionFee : double         - default transaction fee of the account
 * - freeTransactionTimes : int             - free transaction times of the account
 * - transactionFee : double                - transaction fee of the account
 * - lastTransactionDate : Date             - last transaction date of the account
 * - calander1 : Calendar                   - calander1 of the account
 * - calander2 : Calendar                   - calander2 of the account
 * 
 * Constructor:
 * + SavingsAccount(String s, double firstDeposit)
 * + SavingsAccount(String s, double firstDeposit, int freeTimes, int fee)
 * + SavingsAccount(String s, double firstDeposit, Date firstDate)
 * + SavingsAccount(String s, double firstDeposit, Date firstDate, int freeTimes, int fee)
 * 
 * Method:
 * - initCalendar() : void                                  - initialize the calander1 and calander2
 * + setFreeTransactionTimes(int freeTimes) : void          - set free transaction times
 * + getFreeTransactionTimes() : int                        - get free transaction times
 * + setTransactionFee(double fee) : void                   - set transaction fee
 * + getTransactionFee() : double                           - get transaction fee
 * - checkTransactionTimes(Date transactionDate) : void     - check transaction times and according the transaction date
 * + withdraw(double amount, Date withdrawDate) : double    - withdraw money with specified date, but need check the freeTransactionTimes
 * + deposit(double amount) : double                        - deposit money with cuurent date, but need check the freeTransactionTimes
 * + deposit(double amount, Date depositDate) : double      - deposit money with specified date, but need check the freeTransactionTimes
 * # computeNumberOfMonths(Date current, Date past) : int   - compute the number of months between two dates
 * + computeInterest(Date interestDate) : double            - compute the monthly interest with specified date 
 */

class SavingAccount extends Account implements FullFunctionalAccount {
    // define variable
    private final int defaultFreeTranscationTimes = 3;
    private final double defaultTransactionFee = 1.0;

    private int freeTransactionTimes;
    private double transactionFee;
    private int freeTransactionTimesCounter;
    private Date lastTransactionDate;
    private Calendar calendar1, calendar2;


    SavingAccount(String s, double firstDeposit) {
        accountName = s;
        accountBalance = firstDeposit;
        accountInterestRate = defaultInterstRate;
        Calendar calendar = Calendar.getInstance();
        openDate = calendar.getTime();
        lastInterestDate = openDate;
        freeTransactionTimes = defaultFreeTranscationTimes;
        transactionFee = defaultTransactionFee;
        freeTransactionTimesCounter = freeTransactionTimes;
        initCalendar();
        lastTransactionDate = calendar1.getTime();

    }

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

    SavingAccount(String s, double firstDeposit, Date firstDate) {
        accountName = s;
        accountBalance = firstDeposit;
        accountInterestRate = defaultInterstRate;
        openDate = firstDate;
        lastInterestDate = openDate;
        freeTransactionTimes = defaultFreeTranscationTimes;
        transactionFee = defaultTransactionFee;
        freeTransactionTimesCounter = freeTransactionTimes;
        initCalendar();
        lastTransactionDate = firstDate;
    }

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

    public void setFreeTransationTimes(int freeTimes) { freeTransactionTimes = freeTimes; }

    public int getFreeTransationTimes() { return (freeTransactionTimes); }

    public void setTransationFee(int fee) { transactionFee = fee; }

    public double getTransationFee() { return (transactionFee); }

    private void checkFreeTransactionTimes(Date transactionDate) throws BankingException {
        // exception => invalid date
        if (transactionDate.before(lastTransactionDate)) {
            throw new BankingException("Invalid date to withdraw from Saving account" + '\n' + '\t' + 
                "Account name: " + accountName);
        }

        calendar1.setTime(transactionDate);
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

        if (withdrawDate.before(lastTransactionDate)) {
            throw new BankingException("Invalid date to withdraw from Saving account" + '\n' + '\t' + 
                "Account name: " + accountName);
        }
        else if (amount < 0) {
            throw new BankingException("Invalid amount to deposit from Saving account" + '\n' + '\t' + 
                "Account name: " + accountName + '\n' + '\t' + 
                "Withdraw amount : " + amount);
        } else if (accountBalance - amount < 0) {
            throw new BankingException("Underfraft from Saving account" + '\n' + '\t' + 
                "Account name: " + accountName + '\n' + '\t' + 
                "Withdraw amount: " + amount);
        }

        // update free transaction times
        checkFreeTransactionTimes(withdrawDate);

        // minimum balance is less than 0(include don't have free transaction
        // times),raise exception if violated
        if (accountBalance - amount <= 0) {
            if (freeTransactionTimesCounter <= 0) {
                throw new BankingException("Underfraft from Saving account" + '\n' + '\t' + 
                    "Account name: " + accountName + '\n' + '\t' + 
                    "Withdraw amount: " + amount);
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
            throw new BankingException("Invalid amount to deposit from Saving account" + '\n' + '\t' + 
                "Account name: " + accountName + '\n' + '\t' + 
                "Withdraw amount : " + amount);
        }

        // update free transaction times
        checkFreeTransactionTimes(new Date());

        // have free transaction times to deposit
        if (freeTransactionTimesCounter > 0) {
            freeTransactionTimesCounter--;
            accountBalance += (amount);
        } else {
            System.out.println("You don't have enough free Transaction fee to withdraw, so we will cost $"
                    + transactionFee + " from your account");
            accountBalance += (amount - transactionFee);

        }

        freeTransactionTimesCounter--;
        lastTransactionDate = new Date();
        return (accountBalance);
    }

    public double deposit(double amount, Date depositDate) throws BankingException {
        // exception => invalid date
        if (depositDate.before(lastTransactionDate)) {
            throw new BankingException("Invalid date to withdraw from Saving account" + '\n' + '\t' + 
                "Account name: " + accountName);
        }
        // exception => invalid amount
        else if (amount < 0) {
            throw new BankingException("Invalid amount to deposit from Saving account" + '\n' + '\t' + 
                "Account name: " + accountName + '\n' + '\t' + 
                "Withdraw amount : " + amount);
        }

        // update free transaction times
        checkFreeTransactionTimes(depositDate);
        System.out.println("freeTransactionTimesCounter: " + freeTransactionTimesCounter);

        // have free transaction times to deposit
        if (freeTransactionTimesCounter > 0) {
            accountBalance += amount;
        } else {
            System.out.println("You don't have enough free Transaction fee to withdraw, so we will take "
                    + transactionFee + " from your account");
            accountBalance += (amount - transactionFee);
        }

        freeTransactionTimesCounter--;
        lastTransactionDate = depositDate;
        return (accountBalance);
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
            throw new BankingException("Invalid date to compute interest from Saving account" + '\n' + '\t' +
                "Account name: " + accountName + '\n' + '\t' +
                "Last interest date: " + lastInterestDate + '\n' + '\t' +
                "Interest date: " + interestDate);
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

/**
 * Derived class : CD Account
 * 
 * Description:
 * monthly interest; fixed amount and duration (e.g., you can open1 12-month CD for $5000;
 * for the next 12 months you can't deposit anything and withdrawals cost a  $250 fee); 
 * at the end of the duration the interest payments stop and you can withdraw w/o fee.
 * 
 * Member variables::
 * - defaultWithdrawFee : double        - default withdraw fee is 250
 * - defaultDurationMonths : int        - default duration months is 12
 * - withdrawFee : double               - withdraw fee
 * - durationMonths : int               - duration months
 * - computeInterestFlag : boolean      - the status of first time calculate interest after the expired date
 * - lastWithdrawDate : Date            - last withdraw date
 * 
 * Constructor:
 * + CDAccount(String s, double firstDeposit)
 * + CDAccount(String s, double firstDeposit, Date firstDate) 
 * + CDAccount(String s, double firstDeposit, int fee, int duration) 
 * + CDAccount(String s, double firstDeposit, Date firstDate, int fee, int duration)
 * 
 * Mehtods:
 * # computeExpiredDate() : void                            - compute the expired date with current date
 * + reActivate() : void                                    - re-activate the CD duration
 * + reActivate(Date reActivateDate) : void                 - re-activate the CD duration with specific date
 * + getExpiredDate() : Date                                - get expired date
 * # isExpired() : boolean                                  - check if expired with current date
 * # isExpired(Date date) : boolean                         - check if expired with specific date
 * + deposit(double amount) : double                        - override deposit method to throw exception 
 * + deposit(double amount, Date depositDate) : double      - override deposit method to throw exception 
 * + withdraw(double amount, Date withdrawDate) : double    - withdraw money with specific date, check if is not expired pay the withdraw fee 
 * + computeNumberOfMonths(Date current, Date past) : int   - Computer the months between two dates
 */

class CDAccount extends Account implements WithdrawableAccount, InterestableAccount {
    private final double defaultWithdrawFee = 250;
    private final int defaultDurationMonth = 12;
    private double withdrawFee;
    private int durationMonths;
    private boolean computeInterestFlag;
    private Date expiredDate;

    CDAccount(String s, double firstDeposit) {
        accountName = s;
        accountBalance = firstDeposit;
        accountInterestRate = defaultInterstRate;
        Calendar calendar = Calendar.getInstance();
        openDate = calendar.getTime();
        lastInterestDate = openDate;
        withdrawFee = defaultWithdrawFee;
        durationMonths = defaultDurationMonth;
        computeInterestFlag = false;
        computeExpiredDate();
    }

    CDAccount(String s, double firstDeposit, Date firstDate) {
        accountName = s;
        accountBalance = firstDeposit;
        accountInterestRate = defaultInterstRate;
        openDate = firstDate;
        lastInterestDate = openDate;
        withdrawFee = defaultWithdrawFee;
        durationMonths = defaultDurationMonth;
        computeInterestFlag = false;
        computeExpiredDate();
    }

    CDAccount(String s, double firstDeposit, int fee, int duration) {
        accountName = s;
        accountBalance = firstDeposit;
        accountInterestRate = defaultInterstRate;
        Calendar calendar = Calendar.getInstance();
        openDate = calendar.getTime();
        lastInterestDate = openDate;
        withdrawFee = fee;
        durationMonths = duration;
        computeInterestFlag = false;
        computeExpiredDate();
    }

    CDAccount(String s, double firstDeposit, Date firstDate, int fee, int duration) {
        accountName = s;
        accountBalance = firstDeposit;
        accountInterestRate = defaultInterstRate;
        openDate = firstDate;
        lastInterestDate = openDate;
        withdrawFee = fee;
        durationMonths = duration;
        computeInterestFlag = false;
        computeExpiredDate();
    }

    protected void computeExpiredDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(openDate);
        calendar.add(Calendar.MONTH, durationMonths);
        expiredDate = calendar.getTime();
    }

    public void reActivate() {
        reActivate(new Date());
    }

    public void reActivate(Date reActivateDate) {
        computeInterestFlag = false;

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(reActivateDate);
        calendar.add(Calendar.MONTH, durationMonths);
        expiredDate = calendar.getTime();
    }

    public Date getExpiredDate() {
        return (expiredDate);
    }

    protected boolean isExpired() throws BankingException {
        return (isExpired(new Date()));
    }

    protected boolean isExpired(Date checkDate) throws BankingException {
        return (expiredDate.before(checkDate));
    }

    @Override
    public double deposit(double amount) throws BankingException {
        throw new BankingException("You can't deposit in CD account !" + '\n'  + '\t' + 
            "Account name: " + accountName);
    }

    double deposit(double amount, Date depositDate) throws BankingException {
        throw new BankingException("You can't deposit in CD account !" + '\n'  + '\t' + 
            "Account name: " + accountName);
    }

    public double withdraw(double amount, Date withdrDate) throws BankingException {
        if (withdrDate.before(openDate)) {
            throw new BankingException("Invalid date to withdraw from CD account" + '\n' + '\t' + 
                "Account name: " + accountName);
        } else if (amount < 0) {
            throw new BankingException("Invalid amount to deposit from CD account" + '\n' + '\t' + 
                "Account name: " + accountName + '\n' + '\t' + 
                "Withdraw amount : " + amount);
        } else if (accountBalance - amount < 0) {
            throw new BankingException("Underfraft from CD account" + '\n' + '\t' + 
                "Account name: " + accountName + '\n' + '\t' + 
                "Withdraw amount: " + amount);
        }

        if (isExpired(withdrDate)) {
            accountBalance -= amount;
        } else {
            if (accountBalance - (amount + withdrawFee) < 0) {
                throw new BankingException("Underfraft from CD account" + '\n' + '\t' + 
                    "Account name: " + accountName + '\n' + '\t' + 
                    "Withdraw fee: " + withdrawFee);
            } else {
                accountBalance -= (amount + withdrawFee);
                System.out.println("Withdraw fee: " + withdrawFee);
            }
        }

        return (accountBalance);

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
        if( interestDate.before(lastInterestDate)) {
            throw new BankingException("Invalid date to compute interest from CD account" + '\n' + '\t' +
            "Account name: " + accountName + '\n' + '\t' +
            "Last interest date: " + lastInterestDate + '\n' + '\t' +
            "Interest date: " + interestDate);
        }
        else if (isExpired(interestDate)) {
            if (computeInterestFlag == true) {
                throw new BankingException("Invalid date to compute interest from CD account" + '\n' + '\t' +
                "Account name: " + accountName + '\n' + '\t' +
                "Last interest date: " + lastInterestDate + '\n' + '\t' +
                "Interest date: " + interestDate + '\n' + '\t' +
                "Expired date: " + expiredDate);
            }
            else {
                int numberOfMonths = durationMonths;
                System.out.println("Number of months since last interest is " + numberOfMonths);
                double interestEarned = (double) numberOfMonths / 12 *
                        accountInterestRate * accountBalance;
                System.out.println("Interest earned is " + interestEarned);
                lastInterestDate = interestDate;
                accountBalance += interestEarned;

                if (isExpired(interestDate))
                {
                    computeInterestFlag = true;
                }

                return (accountBalance);
            }
        }

        int numberOfMonths = computeNumberOfMonths(interestDate, lastInterestDate);
        System.out.println("Number of months since last interest is " + numberOfMonths);
        double interestEarned = (double) numberOfMonths / 12 *
                accountInterestRate * accountBalance;
        System.out.println("Interest earned is " + interestEarned);
        lastInterestDate = interestDate;
        accountBalance += interestEarned;

        if (isExpired(interestDate))
        {
            computeInterestFlag = true;
        }

        return (accountBalance);
    }
}

/**
 * Derived class : LoanAccount
 * 
 * Description:
 * like a saving account, but the balance is "negative" 
 * (you owe the bank money, so a deposit will reduce the amount of the loan);
 * you can't withdraw (i.e., loan more money) but of course you can deposit 
 * (i.e., pay off part of the loan).
 * 
 * Constructor:
 * LoanAccount(String s, double firstDeposit)
 * LoanAccount(String s, double firstDeposit, Date firstDate)
 * 
 * Methods:
 * withdraw(double amount, Date withdrDate)         - throws BankingException, beacuse you can't withdraw from loan account
 * deposit(double amount, Date depositDate)         - deposit money to loan account
 * computerNumberOfMonths(Date current, Date past)  - compute number of months between two dates
 * computeInterest(Date interestDate)               - compute monthly interest for loan account
 */
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
        throw new BankingException("You can't loan more money from Loan account !" + '\n' + '\t' +
        "Account name: " + accountName);
    }

    public double deposit(double amount, Date deposiDate) throws BankingException {
        if (accountBalance + (amount) > 0) {
            throw new BankingException("You can't turn more money back to Loan account" + '\n' + '\t' + 
                "Account name: " + accountName + '\n' + '\t' + 
                "Deposit amount: " + amount);
        }
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
            throw new BankingException("Invalid date to compute interest from Load account" + '\n' + '\t' +
                    "Account name: " + accountName + '\n' + '\t' +
                    "Last interest date: " + lastInterestDate + '\n' + '\t' +
                    "Interest date: " + interestDate);
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
