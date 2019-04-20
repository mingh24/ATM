/**
 * @author: Yi
 * @className: ConnectWithClient.java
 * @packageName: banking.server
 * @date: 2018-11-20    14:30
 * @description: This class is responsible for receiving and sending message to clients.
 */

package banking.server;

import banking.data.Instruction;

import java.net.*;
import java.io.*;
import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

import static banking.data.Instruction.*;

class ConnectWithClient extends Thread {
    private final Socket clientSocket;

    private BufferedReader bufferedReader;
    private PrintWriter printWriter;

    private BankServer bankInstance;

    private ListIterator<Account> accountIterator;

    private Account account;

    private String accountType;

    private String receivedAccount;
    private String receivedPassword;

    private final int tryTimes = 5;
    private int mismatching = 0;

    private String receivedMessage;

    private String ownerName;

    private String targetAccount;
    private double transferAmount;

    private double withdrawAmount;

    private double depositAmount;

    private String newPassword;

    private static Logger serverConnectorLogger;
    private static FileHandler connectWithClientHandler;


    /**
     * This constructor initializes the clientSocket and IO streams.
     *
     * @param clientSocket: the client socket established
     */
    ConnectWithClient(Socket clientSocket) {
        this.clientSocket = clientSocket;
        try {
            serverConnectorLogger = Logger.getLogger("Connect With Client Logger");
            connectWithClientHandler = new FileHandler("../data/ConnectWithClient");
            serverConnectorLogger.addHandler(connectWithClientHandler);
            bufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            printWriter = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()), true);
        } catch (Exception ex) {
            // logs the exceptions
            StackTraceElement[] elements = ex.getStackTrace();
            for (StackTraceElement element : elements) {
                serverConnectorLogger.warning("Connect With Client Exception: " + element);
            }
        }
    }


    /**
     * This method is used to match the account number.
     *
     * @param acc: the account number input
     * @return account with such a account number; or null if there is not such a account
     */
    private Account matchAccount(String acc) {
        try {
            bankInstance = BankServer.getBankServer();
            accountIterator = bankInstance.getIterator();

            while (accountIterator.hasNext()) {
                account = accountIterator.next();
                if (account.getCardNum().equals(acc)) {
                    return account;
                }
            }
        } catch (Exception ex) {
            // logs the exceptions
            StackTraceElement[] elements = ex.getStackTrace();
            for (StackTraceElement element : elements) {
                serverConnectorLogger.warning("Connect With Client Exception: " + element);
            }
        }
        return null;
    }


    /**
     * This method is used to match the password.
     *
     * @param acc:      the account to be detected
     * @param password: received password
     * @return true if the account is not null and password is correctly matched
     */
    private boolean matchPassword(Account acc, String password) {
        return !Objects.isNull(acc) && acc.comparePsw(password);
    }


    /**
     * This method deals with the connection with clients by sending and receiving instructions and messages.
     */
    @Override
    public void run() {
        Account localAccount;
        while (true) {
            try {
                receivedMessage = bufferedReader.readLine();
            } catch (Exception ex) {
                // logs the exceptions
                StackTraceElement[] elements = ex.getStackTrace();
                for (StackTraceElement element : elements) {
                    serverConnectorLogger.warning("Connect With Client Exception: " + element);
                }
            }

            switch (receivedMessage) {
                case LOGIN:
                    try {
                        receivedAccount = bufferedReader.readLine();
                        receivedPassword = bufferedReader.readLine();
                    } catch (Exception ex) {
                        // logs the exceptions
                        StackTraceElement[] elements = ex.getStackTrace();
                        for (StackTraceElement element : elements) {
                            serverConnectorLogger.warning("Connect With Client Exception: " + element);
                        }
                    }
                    localAccount = matchAccount(receivedAccount);
                    if (matchPassword(localAccount, receivedPassword)) {
                        accountType = localAccount.getAccountType();
                        try {
                            printWriter.println(SUCCESS);
                            ownerName = localAccount.nameToString();
                            printWriter.println(ownerName);
                            while (true) {
                                receivedMessage = bufferedReader.readLine();
                                switch (receivedMessage) {
                                    case TRANSFER:
                                        try {
                                            targetAccount = bufferedReader.readLine();
                                            transferAmount = Double.parseDouble(bufferedReader.readLine());

                                            Account targetAcc;

                                            if (!Objects.isNull((targetAcc = matchAccount(targetAccount)))) {
                                                printWriter.println(SUCCESS);
                                                printWriter.println("<html><body><font size=\"5\"><center>Payee is <font color=\"red\">" + targetAcc.nameToString() + "</font>.<br>Are you sure you still want to transfer?</center></font></body></html>");
                                                String decision = bufferedReader.readLine();

                                                // user pressed "yes"
                                                if (YES.equals(decision)) {
                                                    if ("saving".equals(accountType)) {
                                                        ((SavingAccount) localAccount).withdraw(transferAmount);
                                                    }

                                                    if ("checking".equals(accountType)) {
                                                        ((CheckingAccount) localAccount).withdraw(transferAmount);
                                                    }
                                                    // user pressed "no"
                                                } else {
                                                    break;
                                                }
                                                targetAcc.deposit(transferAmount);

                                                printWriter.println(SUCCESS);
                                                printWriter.println("<html><body><font size=\"5\"><center>Successful operation</center></font></body></html>");
                                            } else {
                                                printWriter.println(ERROR);
                                                printWriter.println("<html><body><font size=\"5\"><center>Target account does not exit!</center></font></body></html>");
                                            }
                                            bankInstance.serializedWriting();
                                            break;
                                        } catch (OverdraftException oe) {
                                            printWriter.println(ERROR);
                                            printWriter.println("<html><body><font size=\"5\"><center>Your balance is not enough!</center></font></body></html>");
                                            break;
                                        } catch (Exception ex) {
                                            // logs the exceptions
                                            StackTraceElement[] elements = ex.getStackTrace();
                                            for (StackTraceElement element : elements) {
                                                serverConnectorLogger.warning("Connect With Client Exception: " + element);
                                            }
                                        }

                                    case REFUND:
                                        printWriter.close();
                                        bufferedReader.close();
                                        clientSocket.close();
                                        bankInstance.serializedWriting();
                                        return;

                                    case QUERY:
                                        printWriter.println(SUCCESS);
                                        if ("saving".equals(accountType)) {
                                            printWriter.println("<html><body><font size=\"8\"><center>Balance: " + localAccount.getBalance()
                                                    + " ¥" + ((SavingAccount) localAccount).getScheme() + "</center></font></body></html>");
                                        }
                                        if ("checking".equals(accountType)) {
                                            printWriter.println("<html><body><font size=\"8\"><center>Balance: " + localAccount.getBalance()
                                                    + " ¥" + "<br><br>Overdraft: " + ((CheckingAccount) localAccount).getOverdraftProtection() + " ¥</center></font></body></html>");
                                        }
                                        break;

                                    case WITHDRAW:
                                        try {
                                            withdrawAmount = Double.parseDouble(bufferedReader.readLine());
                                            if ("saving".equals(accountType)) {
                                                ((SavingAccount) localAccount).withdraw(withdrawAmount);
                                            }
                                            if ("checking".equals(accountType)) {
                                                ((CheckingAccount) localAccount).withdraw(withdrawAmount);
                                            }

                                            printWriter.println(SUCCESS);
                                            printWriter.println("<html><body><font size=\"5\"><center>Successful operation</center></font></body></html>");
                                            bankInstance.serializedWriting();
                                            break;
                                        } catch (OverdraftException oe) {
                                            // logs the exceptions
                                            StackTraceElement[] elements = oe.getStackTrace();
                                            for (StackTraceElement element : elements) {
                                                serverConnectorLogger.warning("Connect With Client Exception: " + element);
                                            }
                                            printWriter.println(ERROR);
                                            printWriter.println("<html><body><font size=\"5\"><center>" + oe.getMessage() + "<br>Deficit: <font color=\"red\">" + oe.getDeficit() + "</font><br>Failure of operation</center></font></body></html>");
                                            break;
                                        } catch (Exception ex) {
                                            // logs the exceptions
                                            StackTraceElement[] elements = ex.getStackTrace();
                                            for (StackTraceElement element : elements) {
                                                serverConnectorLogger.warning("Connect With Client Exception: " + element);
                                            }
                                        }

                                    case DEPOSIT:
                                        try {
                                            depositAmount = Double.parseDouble(bufferedReader.readLine());
                                            if ("saving".equals(accountType)) {
                                                ((SavingAccount) localAccount).deposit(depositAmount);
                                            }
                                            if ("checking".equals(accountType)) {
                                                ((CheckingAccount) localAccount).deposit(depositAmount);
                                            }
                                            printWriter.println(SUCCESS);
                                            printWriter.println("<html><body><font size=\"5\"><center>Successful operation</center></font></body></html>");
                                            bankInstance.serializedWriting();
                                            break;
                                        } catch (Exception ex) {
                                            // logs the exceptions
                                            StackTraceElement[] elements = ex.getStackTrace();
                                            for (StackTraceElement element : elements) {
                                                serverConnectorLogger.warning("Connect With Client Exception: " + element);
                                            }
                                        }

                                    case MODIFY_PSW:
                                        newPassword = bufferedReader.readLine();
                                        if (localAccount.comparePsw(newPassword)) {
                                            printWriter.println(ERROR);
                                            printWriter.println("<html><body><font size=\"5\"><center>New password and old password can not be the same!</center></font></body></html>");
                                            break;
                                        } else {
                                            localAccount.modifyPsw(newPassword);
                                            printWriter.println(SUCCESS);
                                            printWriter.println("<html><body><font size=\"5\"><center>Successful operation</center></font></body></html>");
                                            bankInstance.serializedWriting();
                                            break;
                                        }
                                }
                            }

                        } catch (Exception ex) {
                            // logs the exceptions
                            StackTraceElement[] elements = ex.getStackTrace();
                            for (StackTraceElement element : elements) {
                                serverConnectorLogger.warning("Connect With Client Exception: " + element);
                            }
                        }

                    } else {
                        mismatching++;
                        printWriter.println(ERROR);
                        if (mismatching == 5) {
                            printWriter.println(Instruction.FORCE_EXIT);
                            return;
                        }
                        printWriter.println("<html><body><font size=\"5\"><center>The password does not match the account!"
                                + "<br>You have only <font color = \"red\">" + (tryTimes - mismatching) + "</font> chance(s)!</center></font></body></html>");
                        break;
                    }

                case FORCE_EXIT:
                    return;
            }
        }

    }
}
