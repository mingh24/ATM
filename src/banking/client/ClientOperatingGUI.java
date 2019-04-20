/**
 * @author: Yi
 * @className: ClientOperatingGUI.java
 * @packageName: banking.client
 * @date: 2018-11-24    22:00
 * @description: This class builds up the client operating GUI, and responds to various events made by components.
 */

package banking.client;

import banking.data.Fonts;
import banking.data.Instruction;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.*;
import java.util.logging.FileHandler;
import java.util.logging.Logger;

class ClientOperatingGUI implements Instruction, Fonts {
    private JFrame operatingFrame;
    private JPanel basic;

    private CardLayout card;

    private JButton transfer;
    private JButton refund;
    private JButton query;
    private JButton withdraw;
    private JButton deposit;
    private JButton modifyPsw;

    private JPanel menuCard;
    private JPanel transferCard;
    private JPanel queryCard;
    private JPanel withdrawCard;
    private JPanel depositCard;
    private JPanel modifyPswCard;

    private ConnectWithServer connectWithServer;

    private JTextField transferAccField;
    private String targetAccount;
    private JTextField transferAmtField;
    private String transferAmount;

    private JLabel infoLabel;

    private JTextField withdrawAmtField;
    private String withdrawAmount;

    private JTextField depositAmtField;
    private String depositAmount;

    private JTextField newPswField;
    private String newPsw;
    private JTextField cfmNewPswField;
    private String cfmNewPsw;

    private String ownerName;

    private Logger clientOperatingLogger;
    private FileHandler clientOperatingGUIHandler;


    ClientOperatingGUI(ConnectWithServer connectWithServer) {
        try {
            clientOperatingLogger = Logger.getLogger("Client Operating GUI Logger");
            clientOperatingGUIHandler = new FileHandler("../data/ClientOperatingGUI");
            clientOperatingLogger.addHandler(clientOperatingGUIHandler);
            this.connectWithServer = connectWithServer;
        } catch (Exception ex) {
            // logs the exceptions
            StackTraceElement[] elements = ex.getStackTrace();
            for (StackTraceElement element : elements) {
                clientOperatingLogger.warning("Client Operating GUI Exception: " + element);
            }
        }
    }

    /**
     * This method builds up the client operating GUI.
     */
    void buildOperatingGUI() {

        try {
            ownerName = connectWithServer.receiveMessage();
        } catch (Exception ex) {
            // logs the exceptions
            StackTraceElement[] elements = ex.getStackTrace();
            for (StackTraceElement element : elements) {
                clientOperatingLogger.warning("Client Operating GUI Exception: " + element);
            }
        }

        operatingFrame = new JFrame("BJTU ATM Client");

        basic = new JPanel();
        card = new CardLayout();
        basic.setLayout(card);

        transfer = new JButton("   Transfer   ");
        transfer.setFont(buttonFont);
        transfer.setHorizontalAlignment(JButton.LEFT);
        transfer.addActionListener(new Transfer());

        refund = new JButton("    Refund    ");
        refund.setFont(buttonFont);
        refund.setHorizontalAlignment(JButton.LEFT);
        refund.addActionListener(new Refund());

        query = new JButton("     Query     ");
        query.setFont(buttonFont);
        query.setHorizontalAlignment(JButton.RIGHT);
        query.addActionListener(new Query());

        withdraw = new JButton("   Withdraw   ");
        withdraw.setFont(buttonFont);
        withdraw.setHorizontalAlignment(JButton.RIGHT);
        withdraw.addActionListener(new Withdraw());

        deposit = new JButton("   Deposit   ");
        deposit.setFont(buttonFont);
        deposit.setHorizontalAlignment(JButton.RIGHT);
        deposit.addActionListener(new Deposit());

        modifyPsw = new JButton("Modify Password");
        modifyPsw.setFont(buttonFont);
        modifyPsw.setHorizontalAlignment(JButton.RIGHT);
        modifyPsw.addActionListener(new ModifyPsw());


        // menu card
        menuCard = new JPanel();
        menuCard.setLayout(new BoxLayout(menuCard, BoxLayout.Y_AXIS));
        JPanel titlePanel = new JPanel();
        JLabel titleLabel = new JLabel("<html><body>Welcome to BJTU ATM<br><br>" + "<center>" + ownerName + "</center>" + "</body></html>");
        titleLabel.setFont(labelFont1);
        titlePanel.add(titleLabel);
        Box row1 = Box.createHorizontalBox();
        row1.add(Box.createGlue());
        row1.add(query);
        row1.add(Box.createHorizontalStrut(20));
        Box row2 = Box.createHorizontalBox();
        row2.add(Box.createGlue());
        row2.add(withdraw);
        row2.add(Box.createHorizontalStrut(20));
        Box row3 = Box.createHorizontalBox();
        row3.add(Box.createHorizontalStrut(20));
        row3.add(transfer);
        row3.add(Box.createGlue());
        row3.add(deposit);
        row3.add(Box.createHorizontalStrut(20));
        Box row4 = Box.createHorizontalBox();
        row4.add(Box.createHorizontalStrut(20));
        row4.add(refund);
        row4.add(Box.createGlue());
        row4.add(modifyPsw);
        row4.add(Box.createHorizontalStrut(20));
        menuCard.add(Box.createVerticalStrut(130));
        menuCard.add(titlePanel);
        menuCard.add(row1);
        menuCard.add(Box.createVerticalStrut(20));
        menuCard.add(row2);
        menuCard.add(Box.createVerticalStrut(20));
        menuCard.add(row3);
        menuCard.add(Box.createVerticalStrut(20));
        menuCard.add(row4);
        menuCard.add(Box.createVerticalStrut(70));


        // transfer card
        transferCard = new JPanel();
        transferCard.setLayout(new BoxLayout(transferCard, BoxLayout.Y_AXIS));
        JPanel tLabelPanel = new JPanel();
        JLabel transferLabel = new JLabel(" Transfer ");
        transferLabel.setFont(labelFont1);
        tLabelPanel.add(transferLabel);
        JPanel tAccPanel = new JPanel();
        JLabel tAccLabel = new JLabel("target account: ");
        tAccLabel.setFont(labelFont2);
        transferAccField = new JTextField(12);
        transferAccField.setFont(labelFont2);
        // only number is allowed to be input
        transferAccField.setDocument(new NumberOnlyField());
        tAccPanel.add(tAccLabel);
        tAccPanel.add(transferAccField);
        JPanel tAmtPanel = new JPanel();
        JLabel tAmtLabel = new JLabel("transfer amount: ");
        tAmtLabel.setFont(labelFont2);
        transferAmtField = new JTextField(12);
        transferAmtField.setFont(labelFont2);
        transferAmtField.setHorizontalAlignment(JTextField.RIGHT);
        transferAmtField.setText("0");
        // only number is allowed to be input
        transferAmtField.setDocument(new NumberOnlyField());
        JLabel transferYuanLabel = new JLabel("¥");
        transferYuanLabel.setFont(labelFont2);
        tAmtPanel.add(tAmtLabel);
        tAmtPanel.add(transferAmtField);
        tAmtPanel.add(transferYuanLabel);
        JPanel tButtonPanel = new JPanel();
        JButton transferBack = new JButton("     Back     ");
        transferBack.setFont(buttonFont);
        transferBack.addActionListener(new Back());
        JButton transferFinish = new JButton("   Confirm   ");
        transferFinish.setFont(buttonFont);
        transferFinish.addActionListener(new TransferFinish());
        tButtonPanel.add(transferBack);
        tButtonPanel.add(transferFinish);
        transferCard.add(Box.createVerticalStrut(150));
        transferCard.add(tLabelPanel);
        transferCard.add(tAccPanel);
        transferCard.add(tAmtPanel);
        transferCard.add(tButtonPanel);


        // query card
        queryCard = new JPanel();
        queryCard.setLayout(new BoxLayout(queryCard, BoxLayout.Y_AXIS));
        JPanel textPanel = new JPanel();
        JPanel qButtonPanel = new JPanel();
        infoLabel = new JLabel();
        textPanel.add(infoLabel);
        JButton qBack = new JButton("     Back     ");
        qBack.setFont(buttonFont);
        qBack.addActionListener(new Back());
        qButtonPanel.add(qBack);
        queryCard.add(Box.createVerticalStrut(150));
        queryCard.add(textPanel);
        queryCard.add(qButtonPanel);


        // withdraw card
        withdrawCard = new JPanel();
        withdrawCard.setLayout(new BoxLayout(withdrawCard, BoxLayout.Y_AXIS));
        JPanel wLabelPanel = new JPanel();
        JLabel withdrawLabel = new JLabel(" Withdraw ");
        withdrawLabel.setFont(labelFont1);
        wLabelPanel.add(withdrawLabel);
        JPanel wAmtPanel = new JPanel();
        JLabel wAmtLabel = new JLabel("withdraw amount: ");
        wAmtLabel.setFont(labelFont2);
        withdrawAmtField = new JTextField(12);
        withdrawAmtField.setFont(labelFont2);
        withdrawAmtField.setHorizontalAlignment(JTextField.RIGHT);
        withdrawAmtField.setText("0");
        // only number is allowed to be input
        withdrawAmtField.setDocument(new NumberOnlyField());
        JLabel wYuanLabel = new JLabel("¥");
        wYuanLabel.setFont(labelFont2);
        wAmtPanel.add(wAmtLabel);
        wAmtPanel.add(withdrawAmtField);
        wAmtPanel.add(wYuanLabel);
        JPanel wButtonPanel = new JPanel();
        JButton withdrawBack = new JButton("     Back     ");
        withdrawBack.setFont(buttonFont);
        withdrawBack.addActionListener(new Back());
        JButton withdrawFinish = new JButton("   Confirm   ");
        withdrawFinish.setFont(buttonFont);
        withdrawFinish.addActionListener(new WithdrawFinish());
        wButtonPanel.add(withdrawBack);
        wButtonPanel.add(withdrawFinish);
        withdrawCard.add(Box.createVerticalStrut(150));
        withdrawCard.add(wLabelPanel);
        withdrawCard.add(wAmtPanel);
        withdrawCard.add(wButtonPanel);


        // deposit card
        depositCard = new JPanel();
        depositCard.setLayout(new BoxLayout(depositCard, BoxLayout.Y_AXIS));
        JPanel dLabelPanel = new JPanel();
        JLabel depositLabel = new JLabel(" Deposit ");
        depositLabel.setFont(labelFont1);
        dLabelPanel.add(depositLabel);
        JPanel dAmtLabelPanel = new JPanel();
        JLabel dAmtLabel = new JLabel("deposit amount:");
        dAmtLabel.setFont(labelFont2);
        depositAmtField = new JTextField(12);
        depositAmtField.setFont(labelFont2);
        depositAmtField.setHorizontalAlignment(JTextField.RIGHT);
        depositAmtField.setText("0");
        // only number is allowed to be input
        depositAmtField.setDocument(new NumberOnlyField());
        JLabel dYuanLabel = new JLabel("¥");
        dYuanLabel.setFont(labelFont2);
        dAmtLabelPanel.add(dAmtLabel);
        dAmtLabelPanel.add(depositAmtField);
        dAmtLabelPanel.add(dYuanLabel);
        JPanel dButtonPanel = new JPanel();
        JButton depositBack = new JButton("     Back     ");
        depositBack.setFont(buttonFont);
        depositBack.addActionListener(new Back());
        JButton depositFinish = new JButton("   Confirm   ");
        depositFinish.setFont(buttonFont);
        depositFinish.addActionListener(new DepositFinish());
        dButtonPanel.add(depositBack);
        dButtonPanel.add(depositFinish);
        depositCard.add(Box.createVerticalStrut(150));
        depositCard.add(dLabelPanel);
        depositCard.add(dAmtLabelPanel);
        depositCard.add(dButtonPanel);


        // modify password card
        modifyPswCard = new JPanel();
        modifyPswCard.setLayout(new BoxLayout(modifyPswCard, BoxLayout.Y_AXIS));
        JPanel mpLabelPanel = new JPanel();
        JLabel modifyPswLabel = new JLabel(" Modify Password ");
        modifyPswLabel.setFont(labelFont1);
        mpLabelPanel.add(modifyPswLabel);
        JPanel newPswPanel = new JPanel();
        JLabel newPswLabel = new JLabel("new password: ");
        newPswLabel.setFont(labelFont2);
        newPswField = new JTextField(12);
        newPswField.setFont(labelFont2);
        // only number is allowed to be input
        newPswField.setDocument(new NumberOnlyField());
        newPswPanel.add(newPswLabel);
        newPswPanel.add(newPswField);
        JPanel cfmNewPswPanel = new JPanel();
        JLabel cfmNewPswLabel = new JLabel("confirm new password: ");
        cfmNewPswLabel.setFont(labelFont2);
        cfmNewPswField = new JTextField(12);
        cfmNewPswField.setFont(labelFont2);
        // only number is allowed to be input
        cfmNewPswField.setDocument(new NumberOnlyField());
        cfmNewPswPanel.add(cfmNewPswLabel);
        cfmNewPswPanel.add(cfmNewPswField);
        JPanel mpButtonPanel = new JPanel();
        JButton modifyPswBack = new JButton("     Back     ");
        modifyPswBack.setFont(buttonFont);
        modifyPswBack.addActionListener(new Back());
        JButton modifyPswFinish = new JButton("   Confirm   ");
        modifyPswFinish.setFont(buttonFont);
        modifyPswFinish.addActionListener(new ModifyPswFinish());
        mpButtonPanel.add(modifyPswBack);
        mpButtonPanel.add(modifyPswFinish);
        modifyPswCard.add(Box.createVerticalStrut(150));
        modifyPswCard.add(mpLabelPanel);
        modifyPswCard.add(newPswPanel);
        modifyPswCard.add(cfmNewPswPanel);
        modifyPswCard.add(mpButtonPanel);


        basic.add(menuCard, "menu card");
        basic.add(transferCard, "transfer card");
        basic.add(queryCard, "query card");
        basic.add(withdrawCard, "withdraw card");
        basic.add(depositCard, "deposit card");
        basic.add(modifyPswCard, "modify password card");

        operatingFrame.add(basic);
        operatingFrame.setUndecorated(true);
        operatingFrame.pack();
        operatingFrame.setSize(700, 600);
        operatingFrame.setLocationRelativeTo(null);
        operatingFrame.setVisible(true);
    }


    /**
     * This inner class responds when "Back" button in any card except "menu card" is pressed.
     * The "menu card" will be shown.
     */
    class Back implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            card.show(basic, "menu card");
        }
    }


    /**
     * This inner class responds when the "Transfer" button in "menu card" is pressed.
     * The "transfer card" will be shown.
     */
    class Transfer implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            transferAccField.setText("");
            transferAmtField.setText("");
            card.show(basic, "transfer card");
        }
    }


    /**
     * This inner class responds when the "Confirm" button in "transfer card' is pressed.
     */
    class TransferFinish implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            targetAccount = transferAccField.getText();
            transferAmount = transferAmtField.getText();
            // target account or amount is empty
            if (targetAccount.length() == 0 || transferAmount.length() == 0) {
                JOptionPane.showMessageDialog(null,
                        "<html><body><font size=\"5\"><center>Target account and amount can't be empty!</center></font></body></html>",
                        "Error input", JOptionPane.ERROR_MESSAGE);
            } else {
                connectWithServer.sendMessage(TRANSFER);
                connectWithServer.sendMessage(targetAccount);
                connectWithServer.sendMessage(transferAmount);

                String receivedMessage = connectWithServer.receiveMessage();

                // target account does exist
                if (SUCCESS.equals(receivedMessage)) {
                    int decision = JOptionPane.showConfirmDialog(null,
                            connectWithServer.receiveMessage(),
                            "Transfer",
                            JOptionPane.YES_NO_OPTION);
                    if (decision == JOptionPane.YES_OPTION) {
                        connectWithServer.sendMessage(YES);
                        receivedMessage = connectWithServer.receiveMessage();
                        // successful transfer
                        if (SUCCESS.equals(receivedMessage)) {
                            JOptionPane.showMessageDialog(null,
                                    connectWithServer.receiveMessage(),
                                    "Transfer result",
                                    JOptionPane.INFORMATION_MESSAGE);
                            card.show(basic, "menu card");
                        }
                        // balance is not enough
                        if (ERROR.equals(receivedMessage)) {
                            JOptionPane.showMessageDialog(null,
                                    connectWithServer.receiveMessage(),
                                    "Transfer result", JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                    }
                    if (decision == JOptionPane.NO_OPTION) {
                        connectWithServer.sendMessage(NO);

                    }
                }
                // target account does not exist
                if (ERROR.equals(receivedMessage)) {
                    JOptionPane.showMessageDialog(null,
                            connectWithServer.receiveMessage(),
                            "Transfer result", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }


    /**
     * This inner class responds when the "Refund" button in "menu card" is pressed.
     * The client terminal will be shut off.
     */
    class Refund implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int decision = JOptionPane.showConfirmDialog(null,
                    "<html><body><font size=\"5\"><center>Are you sure to withdraw your card?</center></font></body></html>",
                    "Refund",
                    JOptionPane.YES_NO_OPTION);
            if (decision == JOptionPane.YES_OPTION) {
                connectWithServer.sendMessage(REFUND);
                System.exit(0);
            }
        }
    }


    class Query implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            connectWithServer.sendMessage(QUERY);
            String receivedMessage = connectWithServer.receiveMessage();
            if (SUCCESS.equals(receivedMessage)) {
                infoLabel.setText(connectWithServer.receiveMessage());
                card.show(basic, "query card");
            }
        }
    }


    /**
     * This inner class responds when the "Withdraw" button in "menu card" is pressed.
     * The "withdraw card" will be shown.
     */
    class Withdraw implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            withdrawAmtField.setText("");
            card.show(basic, "withdraw card");
        }
    }


    /**
     * This inner class responds when the "Confirm" button in "withdraw card" is pressed.
     */
    class WithdrawFinish implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            withdrawAmount = withdrawAmtField.getText();
            // the amount is empty
            if (withdrawAmount.length() == 0) {
                JOptionPane.showMessageDialog(null,
                        "<html><body><font size=\"5\"><center>Amount can't be empty!</center></font></body></html>",
                        "Error input", JOptionPane.ERROR_MESSAGE);
            } else {
                int decision = JOptionPane.showConfirmDialog(null,
                        "<html><body><font size=\"5\"><center>Do you want to withdraw <font color=\"red\">" + withdrawAmount + " ¥</font> ?</center></font></body></html>",
                        "Withdraw",
                        JOptionPane.YES_NO_OPTION);
                if (decision == JOptionPane.YES_OPTION) {
                    connectWithServer.sendMessage(WITHDRAW);
                    connectWithServer.sendMessage(withdrawAmount);

                    String receivedMessage = connectWithServer.receiveMessage();

                    if (SUCCESS.equals(receivedMessage)) {
                        JOptionPane.showMessageDialog(null, connectWithServer.receiveMessage(), "Withdraw result", JOptionPane.INFORMATION_MESSAGE);
                        card.show(basic, "menu card");
                    }
                    if (ERROR.equals(receivedMessage)) {
                        JOptionPane.showMessageDialog(null, connectWithServer.receiveMessage(), "Withdraw result", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        }
    }


    /**
     * This inner class responds when the "Deposit" button in "menu card" is pressed.
     * The "deposit card" will be shown.
     */
    class Deposit implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            depositAmtField.setText("");
            card.show(basic, "deposit card");
        }
    }


    /**
     * This inner class responds when the "Confirm" button in "deposit card" is pressed.
     */
    class DepositFinish implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            depositAmount = depositAmtField.getText();
            // the amount is empty
            if (depositAmount.length() == 0) {
                JOptionPane.showMessageDialog(null,
                        "<html><body><font size=\"5\"><center>Amount can't be empty!</center></font></body></html>",
                        "Error input", JOptionPane.ERROR_MESSAGE);
            } else {
                int decision = JOptionPane.showConfirmDialog(null,
                        "<html><body><font size=\"5\"><center>Do you want to deposit <font color=\"red\">" + depositAmount + " ¥</font> ?</center></font></body></html>",
                        "Deposit",
                        JOptionPane.YES_NO_OPTION);
                if (decision == JOptionPane.YES_OPTION) {
                    connectWithServer.sendMessage(DEPOSIT);
                    connectWithServer.sendMessage(depositAmount);

                    String receivedMessage = connectWithServer.receiveMessage();
                    if (SUCCESS.equals(receivedMessage)) {
                        JOptionPane.showMessageDialog(null, connectWithServer.receiveMessage(), "Deposit result", JOptionPane.INFORMATION_MESSAGE);
                        card.show(basic, "menu card");
                    } else {
                        JOptionPane.showMessageDialog(null, ERROR, "Deposit result", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        }
    }


    /**
     * This inner class responds when the "Modify Password" button in "menu card" is pressed.
     * The "modify password card" will be shown.
     */
    class ModifyPsw implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            newPswField.setText("");
            cfmNewPswField.setText("");
            card.show(basic, "modify password card");
        }
    }


    /**
     * This inner class responds when the "Confirm" button in "modify password card" is pressed.
     */
    class ModifyPswFinish implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            newPsw = newPswField.getText();
            cfmNewPsw = cfmNewPswField.getText();
            // new password or confirmation is empty
            if (newPsw.length() == 0 || cfmNewPsw.length() == 0) {
                JOptionPane.showMessageDialog(null,
                        "<html><body><font size=\"5\"><center>New password and confirmation can't be empty!</center></font></body></html>",
                        "Error input", JOptionPane.ERROR_MESSAGE);
            } else {
                // confirmation is not equal to the new password
                if (!newPsw.equals(cfmNewPsw)) {
                    JOptionPane.showMessageDialog(null,
                            "<html><body><font size=\"5\"><center>New password and its confirmation must be the same!</center></font></body></html>",
                            "Error input", JOptionPane.ERROR_MESSAGE);
                } else {
                    connectWithServer.sendMessage(MODIFY_PSW);
                    connectWithServer.sendMessage(newPsw);

                    String receivedMessage = connectWithServer.receiveMessage();
                    if (SUCCESS.equals(receivedMessage)) {
                        JOptionPane.showMessageDialog(null,
                                connectWithServer.receiveMessage(),
                                "Modify successfully",
                                JOptionPane.INFORMATION_MESSAGE);
                        JOptionPane.showMessageDialog(null,
                                "<html><body><font size=\"5\"><center>Please login again.</center></font></body></html>",
                                "Modify successfully",
                                JOptionPane.INFORMATION_MESSAGE);
                        connectWithServer.sendMessage(REFUND);
                        System.exit(0);
                    }
                    if (ERROR.equals(receivedMessage)) {
                        JOptionPane.showMessageDialog(null,
                                connectWithServer.receiveMessage(),
                                "Modification failed", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }
    }


    /**
     * This inner class provides the method to only read in number but other characters.
     */
    class NumberOnlyField extends PlainDocument {
        NumberOnlyField() {
            super();
        }

        public void insertString(int offset, String str, AttributeSet attr)
                throws javax.swing.text.BadLocationException {
            if (str == null) {
                return;
            }

            char[] s = str.toCharArray();
            int length = 0;
            // filters the non-digital character
            for (int i = 0; i < s.length; i++) {
                if ((s[i] >= '0') && (s[i] <= '9')) {
                    s[length++] = s[i];
                }
                // inserts the content
                super.insertString(offset, new String(s, 0, length), attr);
            }
        }
    }
}
