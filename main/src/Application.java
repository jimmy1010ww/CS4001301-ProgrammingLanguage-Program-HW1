
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
        Account a, b, c;
        Calendar calendar = Calendar.getInstance();
        Date d = calendar.getTime();
        double ret, ret1;

        a = new CheckingAccount("John Smith", 1500.0);

        try {
            ret = a.withdraw(100.00);
            System.out.println("Account <" + a.name() + "> now has $" + ret + " balance\n");
        } catch (Exception e) {
            stdExceptionPrinting(e, a.balance());
        }

        a = new CheckingAccount("John Smith", 1500.0);

        try {
            ret = a.withdraw(600.00);
            System.out.println("Account <" + a.name() + "> now has $" + ret + " balance\n");
        } catch (Exception e) {
            stdExceptionPrinting(e, a.balance());
        }

        /* put your own tests here ....... */
       
        a = new SavingAccount("John Cena", 1500.00);
        b = new CheckingAccount("John Smith", 1500.00);
        
        
        System.out.println("Before => " + calendar.getTime());
        calendar.add(Calendar.DATE, 30);
        d = calendar.getTime();
        System.out.println("After => " + calendar.getTime());

        try {
            ret = a.withdraw(1500, d);
            ret1 = b.computeInterest(d);
            System.out.println("Account <" + a.name() + "> now has $" + ret + " balance\n");
            System.out.println("Account <" + b.name() + "> now has $" + ret1 + " balance\n");
        } catch (Exception e) {
            stdExceptionPrinting(e, a.balance());
            stdExceptionPrinting(e, b.balance());

        }

        c = new CDAccount("Jimmy Hua", 5000, 12);
        try {
            ret = c.deposit(1500);
            System.out.println("Account <" + c.name() + "> now has $" + ret + " balance\n");
        } catch (Exception e) {
            stdExceptionPrinting(e, c.balance());
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
            double newBalance = accountList[count].computeInterest();
            System.out.println ("Account <" + a.name() + "> now has $" + newBalance + " balance\n");
        }
        */
    }
    
    static void stdExceptionPrinting(Exception e, double balance) {
        System.out.println("EXCEPTION: Banking system throws a " + e.getClass() +
                " with message: \n\t" +
                "MESSAGE: " + e.getMessage());
        System.out.println("\tAccount balance remains $" + balance + "\n");
    }
    
}