/**
 * @author: Yi
 * @className: Account.java
 * @packageName: banking.server
 * @date: 2018-11-13    14:20
 * @description: This class has two subclasses: SavingAccount and CheckingAccount.
 * It has several methods to change and get the balance.
 * @see banking.server.SavingAccount
 * @see banking.server.CheckingAccount
 */

package banking.server;

import java.io.Serializable;

class Account implements Serializable, Comparable<Account> {
    private static int accountNum;
    private static final String accountType = "prototype";
    private final String firstName;
    private final String lastName;
    private final String cardNum;
    private String password;
    private double balance;     // it holds the current (or "running") balance of the bank account


    /**
     * It initializes one account.
     *
     * @param firstName: the first name of the owner
     * @param lastName:  the last name of the owner
     */
    Account(String firstName, String lastName) {
        accountNum++;
        this.firstName = firstName;
        this.lastName = lastName;
        cardNum = Integer.toString(1000 + accountNum);
        password = "123456";
        balance = 0;
    }


    /**
     * This method returns the account type.
     *
     * @return the account type
     */
    String getAccountType() {
        return accountType;
    }


    /**
     * This method returns the card number.
     *
     * @return the card number
     */
    String getCardNum() {
        return cardNum;
    }


    /**
     * This method is used to judge whether the user has entered matched password.
     *
     * @param enteredPsw: the password user entered
     * @return true if the user entered right password;
     * false if the user entered wrong one.
     */
    boolean comparePsw(String enteredPsw) {
        return password.equals(enteredPsw);
    }


    /**
     * This method modify the password of account.
     *
     * @param newPassword: the new password user entered.
     */
    void modifyPsw(String newPassword) {
        password = newPassword;
    }


    /**
     * You can invoke this method to get the amount of balance.
     *
     * @return value of balance
     */
    double getBalance() {
        return balance;
    }


    /**
     * You can invoke this method to deposit.
     *
     * @param amount: the amount that will be deposited
     * @return successfully deposited
     */
    boolean deposit(double amount) {
        balance += amount;
        return true;
    }


    /**
     * You can invoke this method to withdraw money.
     * And before the operation comes into effect, it will check whether the balance is sufficient.
     *
     * @param amount: the amount that will be withdrew
     * @throws OverdraftException the balance is insufficient to afford the withdrew amount
     */
    void withdraw(double amount) throws OverdraftException {
        /* do the check */
        if (balance >= amount) {
            balance -= amount;
        } else {
            // the balance is unable to afford the amount
            throw new OverdraftException("Insufficient funds!", amount - balance);
        }
    }


    /**
     * This method generates the name of String type.
     *
     * @return first name and last name separated by a comma.
     */
    String nameToString() {
        return firstName + ", " + lastName;
    }


    /**
     * This method can sort the cards by ascending order.
     *
     * @param o: other Account in the list.
     * @return 1 if current card number is larger than o's;
     * 0 if current card number is equal to o's;
     * -1 if current card number is less than o's.
     */
    @Override
    public int compareTo(Account o) {
        return this.getCardNum().compareTo(o.getCardNum());
    }
}
