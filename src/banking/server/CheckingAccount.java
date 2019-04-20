/**
 * @author: Yi
 * @className: CheckingAccount.java
 * @packageName: banking.server
 * @date: 2018-11-13    14:20
 * @description: This is a subclass of Amount.
 * It describes one checking account that may has overdraft protection.
 * @see banking.server.Account
 */

package banking.server;

class CheckingAccount extends Account {
    private static final String accountType = "checking";
    private double overdraftProtection = 0;


    /**
     * This constructor initializes one instance of checking account without overdraft protection.
     *
     * @param firstName: the first name of the owner
     * @param lastName:  the last name of the owner
     */
    CheckingAccount(String firstName, String lastName) {
        super(firstName, lastName);
    }


    /**
     * This constructor initializes one instance of checking account with overdraft protection.
     *
     * @param firstName: the first name of the owner
     * @param lastName:  the last name of the owner
     * @param protect:   overdraft protection
     */
    CheckingAccount(String firstName, String lastName, double protect) {
        super(firstName, lastName);
        this.overdraftProtection = protect;
    }


    /**
     * This method returns the account type.
     *
     * @return "checking"
     */
    @Override
    String getAccountType() {
        return accountType;
    }


    /**
     * This method returns the amount of the overdraft.
     *
     * @return the amount of the overdraft
     */
    double getOverdraftProtection() {
        return overdraftProtection;
    }


    /**
     * This method overrides the Account's.
     * Before the operation comes into effect, it will check whether the balance and overdraft protection is sufficient.
     *
     * @param amount: the amount that will be withdrew
     * @throws OverdraftException when the sum of balance and overdraftProtection is less than amt
     */
    @Override
    void withdraw(double amount) throws OverdraftException {
        if ((getBalance() + overdraftProtection) >= amount) {
            if (getBalance() >= amount) {
                super.withdraw(amount);
            } else {
                overdraftProtection = getBalance() + overdraftProtection - amount;
                super.withdraw(getBalance());
            }
        } else {
            // the overdraftProtection amount is insufficient to cover the deficit
            if (overdraftProtection < (amount - getBalance()) && overdraftProtection != 0) {
                throw new OverdraftException("Insufficient funds for overdraft protection!", amount);
            }
            // there is a deficit with no overdraft protection from the savings account
            if (overdraftProtection == 0) {
                throw new OverdraftException("No overdraft protection!", amount - getBalance());
            }
        }
    }
}
