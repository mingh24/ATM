/**
 * @author: Yi
 * @className: BankServer.java
 * @packageName: banking.server
 * @date: 2018-11-13    14:20
 * @description: This file contains several attributes and methods to store some information of savingAccounts.
 * And collections are added in this version.
 */

package banking.server;

import java.io.*;
import java.util.*;
import java.net.*;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

class BankServer {
    private static LinkedList<Account> accountList;
    private static ServerSocket serverSocket;

    private static BankServer bankServer;

    private static File file;
    private static ObjectInputStream ois;
    private static ObjectOutputStream oos;

    private static Logger accDataLogger;
    private static FileHandler bankServerHandler;


    /**
     * This constructor initializes the two account lists.
     */
    private BankServer() {
        try {
            accDataLogger = Logger.getLogger("Account Data Logger");
            bankServerHandler = new FileHandler("../data/BankServer");
            accDataLogger.addHandler(bankServerHandler);
            accountList = new LinkedList<>();
            serverSocket = new ServerSocket(2333);
            file = new File("../data/data.ser");
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception ex) {
            // logs the exceptions
            StackTraceElement[] elements = ex.getStackTrace();
            for (StackTraceElement element : elements) {
                accDataLogger.warning("Account Data Exception: " + element);
            }
        }
    }


    /**
     * When this method is invoked, it will check whether there is already a instance of BankServer.
     * If not, the instance will be created.
     * Then this method returns the only instance of BankServer.
     *
     * @return the only instance of bankServer class
     */
    static BankServer getBankServer() {
        // initialize only once
        if (bankServer == null) {
            bankServer = new BankServer();
        }
        return bankServer;
    }


    /**
     * This method is invoked when adding a new saving account to the list.
     *
     * @param firstName:     the first name of the owner
     * @param lastName:      the last name of the owner
     * @param interest_rate: the interest rate
     */
    void addSavingAccount(String firstName, String lastName, int month, double interest_rate) {
        accountList.add(new SavingAccount(firstName, lastName, month, interest_rate));
    }


    /**
     * This method is invoked when adding a new checking account to the list.
     *
     * @param firstName: the first name of the owner
     * @param lastName:  the last name of the owner
     */
    void addCheckingAccount(String firstName, String lastName) {
        accountList.add(new CheckingAccount(firstName, lastName));
    }


    /**
     * This method is invoked when adding a new checking account with overdraft protection to the list.
     *
     * @param firstName: the first name of the owner
     * @param lastName:  the last name of the owner
     * @param protect:   the amount of overdraft protection
     */
    void addCheckingAccount(String firstName, String lastName, double protect) {
        accountList.add(new CheckingAccount(firstName, lastName, protect));
    }


    /**
     * You can get the size of account list by this method.
     *
     * @return elements number of account list
     */
    int getSizeOfAccountList() {
        return accountList.size();
    }


    /**
     * You can get the last account added into the list by this method.
     *
     * @return last element in the account list
     */
    Account getLastAccount() {
        return accountList.getLast();
    }


    /**
     * You can get the List iterator of the account list using this method.
     *
     * @return list iterator of the account list
     */
    ListIterator<Account> getIterator() {
        return accountList.listIterator();
    }


    /**
     * You can invoke this method to remove the latest added element of the account list.
     *
     * @param acc: the element to be removed
     */
    void removeAccount(Account acc) {
        accountList.remove(acc);
    }


    /**
     * This method sorts the savingAccounts list and checkingAccounts list.
     */
    void sortAccounts() {
        if (!accountList.isEmpty()) {
            Collections.sort(accountList);
        }
    }


    /**
     * This method is used to read the serialized data file.
     */
    void serializedReading() {
        try {
            if (file.length() != 0) {
                ois = new ObjectInputStream(new FileInputStream(file));
                accountList = (LinkedList<Account>) ois.readObject();
            }
        } catch (Exception ex) {
            // logs the exceptions
            StackTraceElement[] elements = ex.getStackTrace();
            for (StackTraceElement element : elements) {
                accDataLogger.warning("Account Data Exception: " + element);
            }
        }
    }


    /**
     * This method is used to write the serialized data file.
     */
    void serializedWriting() {
        try {
            oos = new ObjectOutputStream(new FileOutputStream(file));
            oos.writeObject(accountList);
        } catch (Exception ex) {
            // logs the exceptions
            StackTraceElement[] elements = ex.getStackTrace();
            for (StackTraceElement element : elements) {
                accDataLogger.warning("Account Data Exception: " + element);
            }
        }
    }


    public static void main(String[] args) {
        getBankServer().serializedReading();
        new ServerGUI().buildLoginGUI();
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                new ConnectWithClient(clientSocket).start();
            } catch (Exception ex) {
                // logs the exceptions
                StackTraceElement[] elements = ex.getStackTrace();
                for (StackTraceElement element : elements) {
                    accDataLogger.warning("Account Data Exception: " + element);
                }
            }
        }
    }
}
