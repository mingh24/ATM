/**
 * @author: Yi
 * @className: ServerGUI.java
 * @packageName: banking.server
 * @date: 2018-11-17    11:20
 * @description: This class builds up the server GUI, and responds to various events made by components.
 */

package banking.server;

import banking.data.Fonts;
import banking.data.Instruction;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.PlainDocument;
import java.awt.*;
import java.awt.event.*;
import java.util.ListIterator;
import java.util.logging.*;

class ServerGUI implements Instruction, Fonts {
    private JFrame frame;

    private JPanel cardPanel, p0, p1, p2, p3, p4, operatingPanel, addAndDelete;

    private CardLayout cardLayout;

    private JMenuBar menuBar;
    private JMenu file;
    private JMenu help;
    private JMenuItem quit;
    private JMenuItem tip;
    private JMenuItem about;

    private JTextField cardNum;
    private JTextField password;

    private JButton login;

    private JButton add;
    private JButton delete;

    private BankServer bankServer;

    private static Logger serverGUILogger;
    private static FileHandler serverGUIHandler;


    /**
     * This method builds up the server GUI.
     */
    void buildLoginGUI() {
        // sets the style to nimbus
        try {
            serverGUILogger = Logger.getLogger("Server GUI Logger");
            serverGUIHandler = new FileHandler("../data/ServerGUI");
            serverGUILogger.addHandler(serverGUIHandler);
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ex) {
            // logs the exceptions
            StackTraceElement[] elements = ex.getStackTrace();
            for (StackTraceElement element : elements) {
                serverGUILogger.warning("Server GUI Exception: " + element);
            }
        }

        frame = new JFrame("BJTU ATM Server");

        cardPanel = new JPanel();
        cardLayout = new CardLayout();
        cardPanel.setLayout(cardLayout);

        // p0 is the basic panel
        p0 = new JPanel();
        p0.setLayout(new BoxLayout(p0, BoxLayout.Y_AXIS));

        menuBar = new JMenuBar();
        file = new JMenu("file");
        file.setFont(menuFont);
        help = new JMenu("help");
        help.setFont(menuFont);
        quit = new JMenuItem("quit");
        quit.setFont(menuFont);
        tip = new JMenuItem("tip");
        tip.setFont(menuFont);
        about = new JMenuItem("about");
        about.setFont(menuFont);

        quit.addActionListener(new Quit());
        tip.addActionListener(new Tip());
        about.addActionListener(new About());

        file.add(quit);
        help.add(tip);
        help.addSeparator();
        help.add(about);

        menuBar.add(file);
        menuBar.add(help);

        frame.getContentPane().add(menuBar, BorderLayout.NORTH);

        // p1 contains the welcome label
        p1 = new JPanel();
        JLabel welcome = new JLabel("Welcome, administrator", JLabel.CENTER);
        welcome.setFont(labelFont1);
        p1.add(welcome);
        p0.add(p1);

        // p2 contains the card number label
        p2 = new JPanel();
        JLabel cardNumLabel = new JLabel("card number:");
        cardNumLabel.setFont(labelFont2);
        p2.add(cardNumLabel);
        cardNum = new JTextField(18);
        cardNum.setFont(labelFont2);
        p2.add(cardNum);
        p0.add(p2);

        // p3 contains the password label
        p3 = new JPanel();
        JLabel passwordLabel = new JLabel("password: ");
        passwordLabel.setFont(labelFont2);
        p3.add(passwordLabel);
        password = new JTextField(20);
        password.setFont(labelFont2);
        // only number is allowed to be input
        password.setDocument(new NumberOnlyField());
        p3.add(password);
        p0.add(p3);

        // p4 contains the two buttons
        p4 = new JPanel();
        login = new JButton("     Login    ");
        login.setFont(buttonFont);
        login.addActionListener(new Login());
        p4.add(login);
        p0.add(p4);


        addAndDelete = new JPanel();
        add = new JButton("   Add   ");
        add.setFont(buttonFont);
        add.addActionListener(new Add());
        delete = new JButton("   Delete   ");
        delete.setFont(buttonFont);
        delete.addActionListener(new Delete());
        addAndDelete.add(add);
        addAndDelete.add(delete);

        operatingPanel = new JPanel();
        operatingPanel.setLayout(new BoxLayout(operatingPanel, BoxLayout.Y_AXIS));
        operatingPanel.add(Box.createGlue());
        operatingPanel.add(addAndDelete);

        cardPanel.add(p0, "login card");
        cardPanel.add(operatingPanel, "operating card");

        frame.getContentPane().add(cardPanel);
        frame.setUndecorated(true);
        frame.pack();
        frame.setSize(450, 300);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        bankServer = BankServer.getBankServer();
    }


    /**
     * This inner class responds to the "Login" button pressed event.
     */
    class Login implements ActionListener {
        /**
         * Account and password will be sent to server if the "Login" button is pressed.
         * And it will check whether account or password is empty.
         *
         * @param e: event of pressing "Login" button
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            if (cardNum.getText().length() == 0 || password.getText().length() == 0) {
                JOptionPane.showMessageDialog(null,
                        "<html><body><font size=\"5\"><center>Account and password can't be empty!</center></font></body></html>",
                        "error input", JOptionPane.ERROR_MESSAGE);
            } else {
                if ("admin".equals(cardNum.getText()) && "123456".equals(password.getText())) {
                    cardLayout.show(cardPanel, "operating card");
                } else {
                    JOptionPane.showMessageDialog(null,
                            "<html><body><font size=\"5\"><center>Account and password are not matched!</center></font></body></html>",
                            "error input", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }


    /**
     * This inner class responds to the "Add" button pressed event.
     */
    class Add implements ActionListener {
        private String accountType;
        private int savingTime;
        private double savingRate;
        private double overdraftProtection;
        private String lastName;
        private String firstName;

        JFrame register;

        JPanel chooseTypeCard;
        JComboBox<String> typeBox;
        JPanel buttons1;

        JPanel savingTypeCard;
        JComboBox<String> monthAndRate;
        JPanel buttons2;

        JPanel checkingTypeCard;
        JTextField overdraftField;
        JPanel buttons3;

        JPanel nameCard;
        JTextField lastNameField;
        JTextField firstNameField;
        JPanel buttons4;

        CardLayout card;
        JPanel cardPanel;


        /**
         * Add window will be shown when the "Add" button in the login window is pressed.
         *
         * @param e: event of pressing "Add" button
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            register = new JFrame("add");

            JButton next1 = new JButton("     Next     ");
            next1.setFont(buttonFont);
            next1.addActionListener(new Next1());

            JButton next2 = new JButton("     Next     ");
            next2.setFont(buttonFont);
            next2.addActionListener(new Next2());

            JButton next3 = new JButton("     Next     ");
            next3.setFont(buttonFont);
            next3.addActionListener(new Next2());

            JButton back1 = new JButton("     Back     ");
            back1.setFont(buttonFont);
            back1.addActionListener(new Back1());

            JButton back2 = new JButton("     Back     ");
            back2.setFont(buttonFont);
            back2.addActionListener(new Back1());

            JButton back3 = new JButton("     Back     ");
            back3.setFont(buttonFont);
            back3.addActionListener(new Back2());

            JButton cancel1 = new JButton("   Cancel   ");
            cancel1.setFont(buttonFont);
            cancel1.addActionListener(new Cancel());

            JButton cancel2 = new JButton("   Cancel   ");
            cancel2.setFont(buttonFont);
            cancel2.addActionListener(new Cancel());

            JButton cancel3 = new JButton("   Cancel   ");
            cancel3.setFont(buttonFont);
            cancel3.addActionListener(new Cancel());

            JButton cancel4 = new JButton("   Cancel   ");
            cancel4.setFont(buttonFont);
            cancel4.addActionListener(new Cancel());

            JButton finish = new JButton("    Finish    ");
            finish.setFont(buttonFont);
            finish.addActionListener(new AddFinish());

            buttons1 = new JPanel();
            buttons1.add(cancel1);
            buttons1.add(next1);

            buttons2 = new JPanel();
            buttons2.add(cancel2);
            buttons2.add(back1);
            buttons2.add(next2);

            buttons3 = new JPanel();
            buttons3.add(cancel3);
            buttons3.add(back2);
            buttons3.add(next3);

            buttons4 = new JPanel();
            buttons4.add(cancel4);
            buttons4.add(back3);
            buttons4.add(finish);

            card = new CardLayout();

            cardPanel = new JPanel();
            cardPanel.setLayout(card);


            // account type choosing card
            chooseTypeCard = new JPanel();
            chooseTypeCard.setLayout(new BoxLayout(chooseTypeCard, BoxLayout.Y_AXIS));
            JPanel typeLabelPanel = new JPanel();
            JLabel typeLabel = new JLabel(" choose the account type ");
            typeLabel.setFont(labelFont2);
            typeLabelPanel.add(typeLabel);
            typeBox = new JComboBox<>();
            typeBox.setFont(labelFont3);
            typeBox.addItem(" saving account ");
            typeBox.addItem(" checking account ");
            chooseTypeCard.add(Box.createGlue());
            chooseTypeCard.add(typeLabelPanel);
            chooseTypeCard.add(typeBox);
            chooseTypeCard.add(Box.createGlue());
            chooseTypeCard.add(buttons1);


            // saving scheme choosing card
            savingTypeCard = new JPanel();
            savingTypeCard.setLayout(new BoxLayout(savingTypeCard, BoxLayout.Y_AXIS));
            JPanel schemeLabelPanel = new JPanel();
            JLabel schemeLabel = new JLabel(" deposit scheme ");
            schemeLabel.setFont(labelFont2);
            schemeLabelPanel.add(schemeLabel);
            monthAndRate = new JComboBox<>();
            monthAndRate.setFont(labelFont3);
            monthAndRate.addItem(" 3 months, interest rate: 2.35% ");
            monthAndRate.addItem(" 6 months, interest rate: 2.55% ");
            monthAndRate.addItem(" 1 year, interest rate: 2.75% ");
            monthAndRate.addItem(" 2 years, interest rate: 3.35% ");
            monthAndRate.addItem(" 3 years, interest rate: 4.00% ");
            savingTypeCard.add(Box.createGlue());
            savingTypeCard.add(schemeLabelPanel);
            savingTypeCard.add(Box.createGlue());
            savingTypeCard.add(monthAndRate);
            savingTypeCard.add(Box.createGlue());
            savingTypeCard.add(buttons2);


            // checking overdraft writing card
            checkingTypeCard = new JPanel();
            checkingTypeCard.setLayout(new BoxLayout(checkingTypeCard, BoxLayout.Y_AXIS));
            JPanel overdraftLabelPanel = new JPanel();
            JLabel overdraftLabel = new JLabel("overdraft: ");
            overdraftLabel.setFont(labelFont2);
            overdraftLabelPanel.add(overdraftLabel);
            overdraftField = new JTextField(8);
            overdraftField.setFont(labelFont2);
            overdraftField.setHorizontalAlignment(JTextField.RIGHT);
            overdraftField.setText("0");
            // only number is allowed to be input
            overdraftField.setDocument(new NumberOnlyField());
            overdraftLabelPanel.add(overdraftField);
            JLabel yuanLabel = new JLabel(" ¥");
            yuanLabel.setFont(labelFont2);
            checkingTypeCard.add(Box.createGlue());
            overdraftLabelPanel.add(yuanLabel);
            checkingTypeCard.add(Box.createGlue());
            checkingTypeCard.add(overdraftLabelPanel);
            checkingTypeCard.add(Box.createGlue());
            checkingTypeCard.add(buttons3);


            // name wrting card
            nameCard = new JPanel();
            nameCard.setLayout(new BoxLayout(nameCard, BoxLayout.Y_AXIS));
            JPanel lastNamePanel = new JPanel();
            JLabel lastNameLabel = new JLabel("last name: ");
            lastNameLabel.setFont(labelFont2);
            lastNamePanel.add(lastNameLabel);
            lastNameField = new JTextField(10);
            lastNameField.setFont(labelFont2);
            lastNamePanel.add(lastNameField);
            JPanel firstNamePanel = new JPanel();
            JLabel firstNameLabel = new JLabel("first name: ");
            firstNameLabel.setFont(labelFont2);
            firstNamePanel.add(firstNameLabel);
            firstNameField = new JTextField(10);
            firstNameField.setFont(labelFont2);
            firstNamePanel.add(firstNameField);
            nameCard.add(Box.createGlue());
            nameCard.add(lastNamePanel);
            nameCard.add(firstNamePanel);
            nameCard.add(Box.createGlue());
            nameCard.add(buttons4);


            cardPanel.add(chooseTypeCard, "choosing card");
            cardPanel.add(savingTypeCard, "saving card");
            cardPanel.add(checkingTypeCard, "checking card");
            cardPanel.add(nameCard, "name card");

            register.getContentPane().add(cardPanel);

            register.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            register.pack();
            register.setSize(550, 350);
            register.setResizable(false);
            register.setLocationRelativeTo(null);
            register.setVisible(true);
        }


        /**
         * "Saving card" or "checking card" will be shown when the "Next" button in the "choosing card" is pressed .
         */
        class Next1 implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                accountType = (String) typeBox.getSelectedItem();
                if (" saving account ".equals(accountType)) {
                    card.show(cardPanel, "saving card");
                } else {
                    card.show(cardPanel, "checking card");
                }
            }
        }


        /**
         * "Name card" will be shown when the "Next" button in the "saving card" or "checking card" is pressed.
         */
        class Next2 implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (" saving account ".equals(accountType)) {
                    switch (monthAndRate.getSelectedIndex()) {
                        case 0:
                            savingTime = 3;
                            savingRate = 0.0235;
                            break;
                        case 1:
                            savingTime = 6;
                            savingRate = 0.0255;
                            break;
                        case 2:
                            savingTime = 12;
                            savingRate = 0.0275;
                            break;
                        case 3:
                            savingTime = 24;
                            savingRate = 0.0335;
                            break;
                        case 4:
                            savingTime = 36;
                            savingRate = 0.04;
                            break;
                        default:
                    }
                } else {
                    overdraftProtection = Double.parseDouble(overdraftField.getText());
                }
                card.show(cardPanel, "name card");
            }
        }


        /**
         * "Choosing card" will be shown when the "Back" button in the "saving card" or "checking card" is pressed.
         */
        class Back1 implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                card.show(cardPanel, "choosing card");
            }
        }


        /**
         * "Saving card" or "checking card" will be shown when the "Back" button in the "name card" is pressed.
         */
        class Back2 implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (" saving account ".equals(accountType)) {
                    card.show(cardPanel, "saving card");
                } else {
                    card.show(cardPanel, "checking card");
                }
            }
        }


        /**
         * This inner class will close the add window when the "Cancel" button in any card is pressed.
         */
        class Cancel implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                register.dispose();
            }
        }


        /**
         * This inner class will complete the add operation when the "Finish" button in "name card" is pressed.
         */
        class AddFinish implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (lastNameField.getText().length() == 0 || firstNameField.getText().length() == 0) {
                    JOptionPane.showMessageDialog(null,
                            "<html><body><font size=\"5\"><center>Name can't be empty!</center></font></body></html>",
                            "Error input", JOptionPane.ERROR_MESSAGE);
                } else {
                    lastName = lastNameField.getText();
                    firstName = firstNameField.getText();

                    if (" saving account ".equals(accountType)) {
                        bankServer.addSavingAccount(firstName, lastName, savingTime, savingRate);
                    } else {
                        bankServer.addCheckingAccount(firstName, lastName, overdraftProtection);
                    }
                    JOptionPane.showMessageDialog(null,
                            "<html><body><font size=\"5\"><center>Create account successfully<br>"
                                    + "It is a " + accountType + ".<br>"
                                    + "Its card number is <font color=\"red\">" + bankServer.getLastAccount().getCardNum() + "</font>.<br>"
                                    + "And the default password is <font color=\"red\">'123456'</font>.</center></font></body></html>",
                            "Create account successfully", JOptionPane.INFORMATION_MESSAGE);
                    register.dispose();
                }
            }
        }
    }


    /**
     * This inner class responds to the "Delete" button pressed event.
     */
    class Delete implements ActionListener {
        private Account account;

        private String cardNum;
        private String information;

        private JFrame deleter;

        private JPanel cardPanel;

        private CardLayout cardLayout;

        private JPanel inputCard;
        private JPanel titlePanel;
        private JPanel inputPanel;
        private JTextField cardNumField;
        private JPanel buttons1;

        private JButton cancel1;
        private JButton next;

        private JPanel deletingCard;
        private JPanel textPanel;
        private JPanel buttons2;

        private JButton cancel2;
        private JButton deleteFinish;

        private JLabel infoLabel;


        /**
         * This method responds to the pressing "Delete" button event.
         *
         * @param e: event of pressing "Delete" button
         */
        @Override
        public void actionPerformed(ActionEvent e) {
            deleter = new JFrame("delete");

            cardPanel = new JPanel();
            cardLayout = new CardLayout();
            cardPanel.setLayout(cardLayout);

            // input card
            inputCard = new JPanel();
            inputCard.setLayout(new BoxLayout(inputCard, BoxLayout.Y_AXIS));

            titlePanel = new JPanel();
            JLabel titleLabel = new JLabel("Delete");
            titleLabel.setFont(labelFont1);
            titlePanel.add(titleLabel);

            inputPanel = new JPanel();
            JLabel inputCardNum = new JLabel("card number: ");
            inputCardNum.setFont(labelFont2);
            cardNumField = new JTextField(15);
            cardNumField.setFont(labelFont2);
            inputPanel.add(inputCardNum);
            inputPanel.add(cardNumField);

            buttons1 = new JPanel();
            next = new JButton("     Next     ");
            next.setFont(buttonFont);
            next.addActionListener(new Next());
            cancel1 = new JButton("   Cancel   ");
            cancel1.setFont(buttonFont);
            cancel1.addActionListener(new Cancel());
            buttons1.add(cancel1);
            buttons1.add(next);

            inputCard.add(Box.createGlue());
            inputCard.add(titleLabel);
            inputCard.add(inputPanel);
            inputCard.add(buttons1);

            // deleting card
            deletingCard = new JPanel();
            deletingCard.setLayout(new BoxLayout(deletingCard, BoxLayout.Y_AXIS));

            textPanel = new JPanel();
            infoLabel = new JLabel();
            infoLabel.setFont(labelFont2);
            textPanel.add(infoLabel);

            buttons2 = new JPanel();
            deleteFinish = new JButton("   Delete   ");
            deleteFinish.setFont(buttonFont);
            deleteFinish.addActionListener(new DeleteFinish());
            cancel2 = new JButton("   Cancel   ");
            cancel2.setFont(buttonFont);
            cancel2.addActionListener(new Cancel());
            buttons2.add(cancel2);
            buttons2.add(deleteFinish);

            deletingCard.add(Box.createGlue());
            deletingCard.add(textPanel);
            deletingCard.add(Box.createGlue());
            deletingCard.add(buttons2);

            cardPanel.add(inputCard, "input card");
            cardPanel.add(deletingCard, "deleting card");

            deleter.getContentPane().add(cardPanel);

            deleter.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            deleter.pack();
            deleter.setSize(550, 350);
            deleter.setResizable(false);
            deleter.setLocationRelativeTo(null);
            deleter.setVisible(true);
        }


        /**
         * "DeleteFinish card" may be shown when the "Next" button in the "input card" is pressed .
         */
        class Next implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                cardNum = cardNumField.getText();
                if (cardNum.length() == 0) {
                    JOptionPane.showMessageDialog(null,
                            "<html><body><font size=\"5\"><center>Card number can't be empty!</center></font></body></html>",
                            "error input", JOptionPane.ERROR_MESSAGE);
                } else {
                    BankServer bankServer = BankServer.getBankServer();
                    ListIterator<Account> iterator = bankServer.getIterator();
                    while (iterator.hasNext()) {
                        Account tempAcc = iterator.next();
                        if (cardNum.equals(tempAcc.getCardNum())) {
                            account = tempAcc;
                            information = "<html><body><font size=\"5\"><center>Owner: " + account.nameToString()
                                    + "<br>account type: " + account.getAccountType()
                                    + "<br>balance: " + account.getBalance() + "</center></font></body></html>";
                            infoLabel.setText(information);
                            cardLayout.show(cardPanel, "deleting card");
                            return;
                        }
                    }
                    JOptionPane.showMessageDialog(null,
                            "<html><body><font size=\"5\"><center>There is not such a card!</center></font></body></html>",
                            "error input", JOptionPane.WARNING_MESSAGE);
                }
            }
        }


        /**
         * DeleteFinish window will be shut off when the "Cancel" button is pressed.
         */
        class Cancel implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleter.dispose();
            }
        }


        /**
         * This inner class responds to the pressing "Delete" button event.
         */
        class DeleteFinish implements ActionListener {
            @Override
            public void actionPerformed(ActionEvent e) {
                int decision = JOptionPane.showConfirmDialog(null,
                        "<html><body><font size=\"5\"><center>Are you sure to delete it?</center></font></body></html>",
                        "deleting",
                        JOptionPane.YES_NO_OPTION);
                if (decision == JOptionPane.YES_OPTION) {
                    bankServer.removeAccount(account);
                    JOptionPane.showMessageDialog(null,
                            "<html><body><font size=\"5\"><center>Deleted successfully</center></font></body></html>",
                            "delete result", JOptionPane.INFORMATION_MESSAGE);
                    deleter.dispose();
                }
            }
        }
    }

    /**
     * This inner class will close the login window when "quit" menu item is pressed.
     */
    class Quit implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            int decision = JOptionPane.showConfirmDialog(null,
                    "<html><body><font size=\"5\"><center>Do you want to close the program?</center></font></body></html>",
                    "Whether or not to quit",
                    JOptionPane.YES_NO_OPTION);
            if (decision == JOptionPane.YES_OPTION) {
                bankServer.serializedWriting();
                System.exit(0);
            }
        }
    }


    /**
     * This inner class responds the "tip" menu item and show the corresponding message dialog.
     */
    class Tip implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(null,
                    "<html><body><font size=\"5\"><center>This is a server GUI of an ATM simulation program.<br>" +
                            "When you choose to add a card, the default password is \"123456\".<br>" +
                            "And the balance is 0 yuan.</center></font></body></html>",
                    "tip",
                    JOptionPane.PLAIN_MESSAGE);
        }
    }


    /**
     * This inner class responds the "about" menu item and show the corresponding message dialog.
     */
    class About implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            JOptionPane.showMessageDialog(null,
                    "<html><body><font size=\"5\"><center>The author is Huang Ming, whose student number is 17301004.</center></font></body></html>",
                    "about",
                    JOptionPane.INFORMATION_MESSAGE);
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
