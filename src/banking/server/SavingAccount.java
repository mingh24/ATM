/**
 * @author: Yi
 * @className: SavingAccount.java
 * @packageName: banking.server
 * @date: 2018-11-13    14:20
 * @description: This is a subclass of Account.
 * It describes one saving account with interest rate.
 * @see banking.server.Account
 */

package banking.server;

class SavingAccount extends Account {
    private static final String accountType = "saving";
    private final double interestRate;
    private final int month;


    /**
     * This constructor initializes one instance of saving account.
     *
     * @param firstName:     the first name of the owner
     * @param lastName:      the last name of the owner
     * @param interest_rate: the interest rate of this type of saving account
     */
    SavingAccount(String firstName, String lastName, int month, double interest_rate) {
        super(firstName, lastName);
        this.month = month;
        this.interestRate = interest_rate;
    }


    /**
     * This method returns the account type.
     *
     * @return "saving"
     */
    String getAccountType() {
        return accountType;
    }


    /**
     * This method returns the saving scheme in the form of HTML.
     *
     * @return saving scheme
     */
    String getScheme() {
        return "<br><br>Saving time: " + month + " month(s)" + "<br><br>Interest rate: " + interestRate;
    }


    /**
     * You can invoke this method to deposit money.
     * And because it is a saving account, every time the user operates, the money will grow according to the interest rate.
     *
     * @param amount: the amount that will be deposited
     * @return result of depositing
     */
    @Override
    boolean deposit(double amount) {
        super.deposit(getBalance() * interestRate + amount);
        return true;
    }


    /**
     * You can invoke this method to withdraw money.
     * And because it is a saving account, every time the user operates, the money will grow according to the interest rate.
     *
     * @param amount: the amount that will be withdrew
     * @throws OverdraftException the balance is insufficient to afford the withdrew amount
     */
    @Override
    void withdraw(double amount) throws OverdraftException {
        super.deposit(getBalance() * interestRate);
        super.withdraw(amount);
    }
}
