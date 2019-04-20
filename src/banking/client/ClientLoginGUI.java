/**
 * @author: Yi
 * @className: ClientLoginGUI.java
 * @packageName: banking.client
 * @date: 2018-11-17    11:20
 * @description: This class builds up the client login GUI.
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

class ClientLoginGUI implements Instruction, Fonts {
    private JFrame loginFrame;

    private JPanel p0, p1, p2, p3, p4;

    private JMenuBar menuBar;
    private JMenu file;
    private JMenu help;
    private JMenuItem quit;
    private JMenuItem tip;
    private JMenuItem about;

    private JTextField cardNum;
    private JTextField password;

    private JButton login;

    private ConnectWithServer connectWithServer;

    private String receivedMessage;

    private Logger clientLoginLogger;
    private FileHandler clientLoginGUIHandler;


    /**
     * This method builds up the client login GUI.
     */
    private void buildLoginGUI() {
        connectWithServer = new ConnectWithServer();
        // sets the style to nimbus
        try {
            clientLoginLogger = Logger.getLogger("Client Login GUI Logger");
            clientLoginGUIHandler = new FileHandler("../data/ClientLoginGUI");
            clientLoginLogger.addHandler(clientLoginGUIHandler);
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel");
        } catch (Exception ex) {
            // logs the exceptions
            StackTraceElement[] elements = ex.getStackTrace();
            for (StackTraceElement element : elements) {
                clientLoginLogger.warning("Client Login GUI Exception: " + element);
            }
        }

        loginFrame = new JFrame("BJTU ATM Client");

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

        loginFrame.getContentPane().add(menuBar, BorderLayout.NORTH);

        // p1 contains the welcome label
        p1 = new JPanel();
        JLabel welcome = new JLabel("Welcome to use BJTU ATM", JLabel.CENTER);
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
        // only number is allowed to be input
        cardNum.setDocument(new NumberOnlyField());
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

        loginFrame.getContentPane().add(p0, BorderLayout.CENTER);
        loginFrame.setUndecorated(true);
        loginFrame.pack();
        loginFrame.setSize(450, 300);
        loginFrame.setLocationRelativeTo(null);
        loginFrame.setVisible(true);
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
                        "Error input", JOptionPane.ERROR_MESSAGE);
            } else {
                connectWithServer.sendMessage(LOGIN);
                connectWithServer.sendMessage(cardNum.getText());
                connectWithServer.sendMessage(password.getText());

                receivedMessage = connectWithServer.receiveMessage();

                if (SUCCESS.equals(receivedMessage)) {
                    loginFrame.dispose();
                    new ClientOperatingGUI(connectWithServer).buildOperatingGUI();
                }
                if (ERROR.equals(receivedMessage)) {
                    receivedMessage = connectWithServer.receiveMessage();
                    if (FORCE_EXIT.equals(receivedMessage)) {
                        System.exit(-1);
                    } else {
                        JOptionPane.showMessageDialog(null,
                                receivedMessage,
                                "Login failure", JOptionPane.ERROR_MESSAGE);
                    }
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
                connectWithServer.sendMessage(FORCE_EXIT);
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
                    "<html><body><font size=\"5\"><center>This is an ATM simulation program.<br>" +
                            "The default password is \"123456\".<br>" +
                            "And the balance is 0 yuan.<br>" +
                            "But be careful, when you entered mismatched account and password for 5 times, the terminal will be shut off.</center></font></body></html>",
                    "Tip",
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
                    "About",
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

    public static void main(String[] args) {
        ClientLoginGUI clientGUI = new ClientLoginGUI();
        clientGUI.buildLoginGUI();
    }
}
