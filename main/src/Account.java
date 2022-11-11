
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
    //Account name
    String name();
    //Account balance
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
    protected String accountName;
    protected double accountBalance;
    protected double accountInterestRate;
    protected Date openDate;
    protected Date lastInterestDate;

    Account(String s, double firstDeposit) {
        accountName = s;
        accountBalance = firstDeposit;
        accountInterestRate = 0.12;
        Calendar calendar = Calendar.getInstance();
        openDate = calendar.getTime();
        lastInterestDate = openDate;
    }

    Account(String s, double firstDeposit, Date firstDate) {
        accountName = s;
        accountBalance = firstDeposit;
        accountInterestRate = 0.12;
        openDate = firstDate;
        lastInterestDate = openDate;
    }

    // public methods for every bank accounts
    public String name() {
        return (accountName);
    }

    public double balance() {
        return (accountBalance);
    }

    public double deposit(double amount) throws BankingException {
        accountBalance += amount;
        return (accountBalance);
    }

    abstract double withdraw(double amount, Date withdrawDate) throws BankingException;

    public double withdraw(double amount) throws BankingException {
        Calendar calendar = Calendar.getInstance();
        Date withdrawDate = calendar.getTime();
        return (withdraw(amount, withdrawDate));
    }

    abstract double computeInterest(Date interestDate) throws BankingException;

    public double computeInterest() throws BankingException {
        Calendar calendar = Calendar.getInstance();
        Date interestDate = calendar.getTime();
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

    CheckingAccount(String s, double firstDeposit) {
        super(s, firstDeposit);
    }

    CheckingAccount(String s, double firstDeposit, Date firstDate) {
        super(s, firstDeposit, firstDate);
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

        int numberOfDays = (int) ((interestDate.getTime()
                - lastInterestDate.getTime())
                / 86400000.0);
        System.out.println("Number of days since last interest is " + numberOfDays);
        double interestEarned = (double) numberOfDays / 365.0 *
                accountInterestRate * accountBalance;
        System.out.println("Interest earned is " + interestEarned);
        lastInterestDate = interestDate;
        accountBalance += interestEarned;
        return (accountBalance);
    }
}

class SavingAccount extends Account implements FullFunctionalAccount{

    SavingAccount(String s, double firstDeposit) {
        super(s, firstDeposit);
    }

    SavingAccount(String s, double firstDeposit, Date firstDate) {
        super(s, firstDeposit, firstDate);
    }

    public double withdraw(double amount, Date withdrawDate) throws BankingException {
        System.out.println(withdrawDate);
        // minimum balance is 0, raise exception if violated
        if ((accountBalance - (amount + 1)) < 0) {
            throw new BankingException("Underfraft from checking account name:" +
                    accountName);
        } else {
            accountBalance -= amount;
            int numberOfDays = (int) ((withdrawDate.getTime()
                - lastInterestDate.getTime())
                / 86400000.0);

            if (numberOfDays > 90)
            {
                accountBalance -= 1.0;
            }
            return (accountBalance);
        }


    }

    public double deposit (double amount, Date depositDate) throws BankingException {
        if (depositDate.before(lastInterestDate)) {
            throw new BankingException("Invalid date to compute interest for account name" +
                    accountName);
        }

        int numberOfDays = (int) ((depositDate.getTime()
                - lastInterestDate.getTime())
                / 86400000.0);


        accountBalance -= amount;

        if (numberOfDays > 90)
        {
            accountBalance -= 1.0;
        }
        
        return (accountBalance);
    }


    public double computeInterest(Date interestDate) throws BankingException {
        if (interestDate.before(lastInterestDate)) {
            throw new BankingException("Invalid date to compute interest for account name" +
                    accountName);
        }

        int numberOfDays = (int) ((interestDate.getTime()
                - lastInterestDate.getTime())
                / 86400000.0);
        System.out.println("Number of days since last interest is " + numberOfDays);
        double interestEarned = (double) numberOfDays / 365.0 *
                accountInterestRate * accountBalance;
        System.out.println("Interest earned is " + interestEarned);
        lastInterestDate = interestDate;
        accountBalance += interestEarned;
        return (accountBalance);
    }


}