
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

        Account[] myAccountsList;
        myAccountsList = new Account[4];
         
        // buid 4 different accounts in the same array
        myAccountsList[0] = new CheckingAccount("John Smith", 1500.0);
        myAccountsList[1] = new SavingAccount("William Hurt", 1200.0);
        myAccountsList[2] = new CDAccount("Woody Allison", 1000.0);
        myAccountsList[3] = new LoanAccount("Judi Foster", -1500.0);

        
        // test each account functionality
        System.out.println("B10951003 Test");
        System.out.println("==========================================================");
        
        for (Account account : myAccountsList) {
            accountBalancePrinting(account);
            Calendar calendar = Calendar.getInstance();
            
        
            //simple withdraw test
            System.out.println("\n----------------------------------------------------------\n");
            System.out.println("1. Simple withdraw test!");
            System.out.println("-->Test Withdraw 4 times, each time withdraw $200\n");
            System.out.println("----------------------------------------------------------");
            for (int i = 1; i <= 4; i++) {
                try {
                    System.out.println("Current date => " + calendar.getTime());
                    ret = account.withdraw(200.00, calendar.getTime());
                    accountBalancePrinting(account);
                } catch (Exception e) {
                    stdExceptionPrinting(e, account.balance());	
                }
                calendar.add(Calendar.DATE, i * 10);
            }


            //large amount withdraw test
            System.out.println("\n----------------------------------------------------------\n");
            System.out.println("2.Large amount withdraw test!");
            System.out.println("-->Test Withdraw $10000\n");
            System.out.println("----------------------------------------------------------");

            try {
                System.out.println("Current date => " + calendar.getTime());
                ret = account.withdraw(10000.00, calendar.getTime());
                accountBalancePrinting(account);
            } catch (Exception e) {
                stdExceptionPrinting(e, account.balance());	
            }


            //simple deposit test
            System.out.println("\n----------------------------------------------------------\n");
            System.out.println("3.Simple deposit test!");
            System.out.println("-->Test Deposit 4 times, each time => $500\n");
            System.out.println("----------------------------------------------------------");
            for (int i = 1; i <= 4; i++) {
                try {
                    System.out.println("Current date => " + calendar.getTime());
                    ret = account.deposit(500.00, calendar.getTime());
                    accountBalancePrinting(account);
                } catch (Exception e) {
                    stdExceptionPrinting(e, account.balance());	
                }
            }

            System.out.println("\n----------------------------------------------------------\n");
            System.out.println("4.Interest test!\n");
            System.out.println("----------------------------------------------------------");
            try {
                ret = account.computeInterest(calendar.getTime());
                accountBalancePrinting(account);
            } catch (Exception e) {
                stdExceptionPrinting(e, account.balance());
            }

            System.out.println("==========================================================");
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