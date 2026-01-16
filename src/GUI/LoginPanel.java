package GUI;

import Infrastructure.AppManager;

import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {
    JPanel centerPanel;
    private AppManager manager;
    private JTextField nameField;
    private JPasswordField passwordField;
    private Color backgroundColor = Color.darkGray;

    public LoginPanel(AppManager manager){
        this.manager = manager;
        this.centerPanel = new JPanel();
        centerPanel.setBackground(backgroundColor);
        setBackground(backgroundColor);
        showLoginPanel();
    }
    private void showLoginPanel(){
        System.out.println("showLoginPanel is reached");
        JLabel welcomeLabel = new JLabel("Welcome!");
        welcomeLabel.setFont(GUI.Game.GameFont.topHeaderFont());
        welcomeLabel.setForeground(GUI.Game.GameColors.headerText());
        JLabel usernameLabel = new JLabel("Enter user name: ");
        usernameLabel.setFont(GUI.Game.GameFont.headerFont());
        usernameLabel.setForeground(GUI.Game.GameColors.headerText());
        this.nameField = new JTextField();
        nameField.setForeground(GUI.Game.GameColors.headerText());
        JLabel passwordLabel = new JLabel("Enter password:");
        passwordLabel.setFont(GUI.Game.GameFont.headerFont());
        passwordLabel.setForeground(GUI.Game.GameColors.headerText());
        this.passwordField = new JPasswordField(20);
        passwordLabel.setForeground(GUI.Game.GameColors.headerText());
        JPanel inputFields = new JPanel(new GridLayout(2, 2));
        inputFields.setBackground(backgroundColor);
        inputFields.setBorder(
                BorderFactory.createLineBorder(GUI.Game.GameColors.headerText(), 4, true));
        inputFields.setPreferredSize(new Dimension(400, 80));
        inputFields.setMinimumSize(new Dimension(400, 80));
        inputFields.setMaximumSize(new Dimension(400, 80));
        inputFields.add(usernameLabel);
        inputFields.add(nameField);
        inputFields.add(passwordLabel);
        inputFields.add(passwordField);
        JButton loginButton = new JButton("Log in");
        loginButton.setForeground(GUI.Game.GameColors.headerText());
        loginButton.setFont(GUI.Game.GameFont.headerFont());
        loginButton.addActionListener(e -> {
            String usernameInput = nameField.getText();
            String passwordInput = passwordField.getText();
            manager.validateUser(this, false, usernameInput, passwordInput);
        });

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.setBackground(backgroundColor);
        inputPanel.add(inputFields);
        inputPanel.add(loginButton);

        JButton newUserButton = new JButton("Create account");
        newUserButton.setForeground(GUI.Game.GameColors.headerText());
        newUserButton.setFont(GUI.Game.GameFont.headerFont());
        newUserButton.addActionListener(e -> {
            createNewAccount();
        });

        centerPanel.setLayout(new BorderLayout());
        centerPanel.add(welcomeLabel, BorderLayout.NORTH);
        centerPanel.add(inputPanel, BorderLayout.CENTER);
        centerPanel.add(newUserButton, BorderLayout.SOUTH);
        setLayout(new BorderLayout());
        setOpaque(true);
        add(centerPanel, BorderLayout.CENTER);
        repaint();
        revalidate();
    }
    public void promptCompleteLogin(){
        JOptionPane.showMessageDialog(this, "All fields must be filled in, try again.");
    }
    public void promptDifferentName(){
        JOptionPane.showMessageDialog(this, "The username is already taken. Try another name.");
        nameField.setText("");
        passwordField.setText("");
    }
    public void promptNoSuchUser(){
        int choice = JOptionPane.showOptionDialog(null, "The username doesn't exist. Would you like to create a new account?", "", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new Object[]{"Yes", "No"}, "Yes");
        passwordField.setText("");
        manager.assessCreateAccount(choice);
    }
    public void promptWrongPassword(){
        JOptionPane.showMessageDialog(this, "The password is incorrect.");
    }
    public void createNewAccount(){
        System.out.println("createNewAccount in LoginPanel is reached");
        String username = nameField.getText();
        centerPanel.removeAll();

        JLabel prompt = new JLabel("Create a new account: ");
        prompt.setFont(GUI.Game.GameFont.topHeaderFont());
        prompt.setForeground(GUI.Game.GameColors.headerText());
        JLabel usernameLabel = new JLabel("Enter user name: ");
        usernameLabel.setForeground(GUI.Game.GameColors.headerText());
        usernameLabel.setFont(GUI.Game.GameFont.headerFont());
        JLabel passwordLabel = new JLabel("Enter password:");
        passwordLabel.setForeground(GUI.Game.GameColors.headerText());
        passwordLabel.setFont(GUI.Game.GameFont.headerFont());
        JPanel inputFields = new JPanel(new GridLayout(2, 2));
        inputFields.setBackground(backgroundColor);
        inputFields.setPreferredSize(new Dimension(400, 80));
        inputFields.setMinimumSize(new Dimension(400, 80));
        inputFields.setMaximumSize(new Dimension(400, 80));

        inputFields.add(usernameLabel);
        inputFields.add(nameField);
        nameField.setText(username);
        inputFields.add(passwordLabel);
        passwordField.setText("");
        inputFields.add(passwordField);
        JButton createAccount = new JButton("Save");
        createAccount.setFont(GUI.Game.GameFont.headerFont());
        createAccount.setForeground(GUI.Game.GameColors.headerText());
        createAccount.addActionListener(e -> {
            String usernameInput = nameField.getText();
            String passwordInput = passwordField.getText();
            manager.validateUser(this, true, usernameInput,  passwordInput);
        });

        JPanel inputPanel = new JPanel();
        inputPanel.setBackground(backgroundColor);
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        inputPanel.add(inputFields);
        inputPanel.add(createAccount);

        JButton returnButton = new JButton("Cancel");
        returnButton.setForeground(GUI.Game.GameColors.headerText());
        returnButton.setFont(GUI.Game.GameFont.headerFont());
        returnButton.addActionListener(e -> {
            centerPanel.removeAll();
            showLoginPanel();
        });
        centerPanel.add(prompt, BorderLayout.NORTH);
        centerPanel.add(inputPanel, BorderLayout.CENTER);
        centerPanel.add(returnButton, BorderLayout.SOUTH);
        setEnabled(true);
        repaint();
        revalidate();
    }
}
