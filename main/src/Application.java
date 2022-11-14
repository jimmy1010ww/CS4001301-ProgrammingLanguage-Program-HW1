
/*****************************************************************
    CS4001301 Programming Languages                   
    
    Programming Assignment #1
    
    Java programming using subtype, subclass, and exception handling
    
    To compile: %> javac Application.java
    
    To execute: %> java Application

******************************************************************/

import java.util.*;

public class Application {

    public static void main(String args[]) {
        Account a;
        Date d;
        double ret;
    
        a = new CheckingAccount("John Smith", 1500.0);
    
        try {
            ret = a.withdraw(100.00);
            System.out.println ("Account <" + a.name() + "> now has $" + ret + " balance\n");
        } catch (Exception e) {
            stdExceptionPrinting(e, a.balance());	
        }
    
        a = new CheckingAccount("John Smith", 1500.0);
    
        try {
            ret = a.withdraw(600.00);
            System.out.println ("Account <" + a.name() + "> now has $" + ret + " balance\n");
        } catch (Exception e) {
            stdExceptionPrinting(e, a.balance());	
        }

        /* put your own tests here ....... */

        Account[] accountList;
        accountList = new Account[4];
         
        // buid 4 different accounts in the same array
        accountList[0] = new CheckingAccount("John Smith", 1500.0);
        accountList[1] = new SavingAccount("William Hurt", 1200.0);
        accountList[2] = new CDAccount("Woody Allison", 1000.0);
        accountList[3] = new LoanAccount("Judi Foster", -1500.0);

        
        // test each account functionality
        System.out.println("Own Test");
        System.out.println("========================================");
        
        for (Account account : accountList) {
            accountBalancePrinting(account);
            Calendar calendar = Calendar.getInstance();
            System.out.println("Current's date is " + calendar.getTime());
            d = calendar.getTime();
            

            //simple withdraw test
            System.out.println("Test Withdraw 3 times, each time => $200\n");
            try {
                ret = account.withdraw(200.00);
                accountBalancePrinting(account);
                ret = account.withdraw(200.00);
                accountBalancePrinting(account);
                ret = account.withdraw(200.00);
                accountBalancePrinting(account);
            } catch (Exception e) {
                stdExceptionPrinting(e, account.balance());
            }
            System.out.println("");
    
            calendar.add(Calendar.MONDAY, 1);
            System.out.println("After modify's date is " + calendar.getTime());
            d = calendar.getTime();

            //test must can't withdraw
            System.out.println("Test Must Can't Withdraw, amount is => $10000\n");
            try {
                ret = account.withdraw(10000.00);
                accountBalancePrinting(account);
            } catch (Exception e) {
                stdExceptionPrinting(e, account.balance());
            }
            System.out.println("");

            calendar.add(Calendar.MONDAY, 1);
            System.out.println("After modify's date is " + calendar.getTime());
            d = calendar.getTime();

            //test deposit
            System.out.println("Test Deposit 3 times, each time => $600\n");
            try {
                ret = account.deposit(600.00);
                accountBalancePrinting(account);
                ret = account.deposit(600.00);
                accountBalancePrinting(account);
                ret = account.deposit(600.00);
                accountBalancePrinting(account);
            } catch (Exception e) {
                stdExceptionPrinting(e, account.balance());
            }
            System.out.println("");

            calendar.add(Calendar.MONDAY, 1);
            System.out.println("After modify's date is " + calendar.getTime());
            d = calendar.getTime();
            
            try {
                ret = account.computeInterest(d);
                accountBalancePrinting(account);
            } catch (Exception e) {
                stdExceptionPrinting(e, account.balance());
            }

            System.out.println("========================================");
        }

        /* 
        //if your implementaion is correct, you can do the following with polymorphicarray accountList
        Account[] accountList;
        
 
        accountList = new Account[4];
         
        // buid 4 different accounts in the same array
        accountList[0] = new CheckingAccount("John Smith", 1500.0);
        accountList[1] = new SavingAccount("William Hurt", 1200.0);
        accountList[2] = new CDAccount("Woody Allison", 1000.0);
        accountList[3] = new LoanAccount("Judi Foster", -1500.0);
        
        // compute interest for all accounts
        for (int count = 0; count < accountList.length; count++) {
            try {
                double newBalance = accountList[count].computeInterest();
                System.out.println ("Account <" + accountList[count].name() + "> now has $" + newBalance + " balance\n");
            } catch (Exception e) {
                stdExceptionPrinting(e, accountList[count].balance());
            }
            
        }
        */
        
    }

    static void accountBalancePrinting(Account a) {
        System.out.println("Account <" + a.name() + "> now has $" + a.balance() + " balance\n");
    }
    
    static void stdExceptionPrinting(Exception e, double balance) {
        System.out.println("EXCEPTION: Banking system throws a " + e.getClass() +
                " with message: \n\t" +
                "MESSAGE: " + e.getMessage());
        System.out.println("\tAccount balance remains $" + balance + "\n");
    }
    
}