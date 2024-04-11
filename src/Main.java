import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

// Class representing bank account details
class BankDetails {
    private String account;
    private String name;
    private String acc_type;
    private long balance;
    private final ArrayList<String> transactionHistory;

    // Set to keep track of used account numbers
    private static final Set<String> usedAccountNumbers = new HashSet<>();

    // Constructor to initialize the transaction history
    public BankDetails() {
        transactionHistory = new ArrayList<>();
    }

    // Method to open a new bank account
    public void openAccount() {
        boolean validAccountNumber = false;
        boolean validAccountType = false;
        boolean validName = false;

        // Validate Account Number
        while (!validAccountNumber) {
            try {
                account = JOptionPane.showInputDialog("Enter Account No (11 digits):");
                validateAccountNumber(account);

                // Check if the account number is already used
                if (isAccountNumberUsed(account)) {
                    throw new IllegalArgumentException("Account number already exists. Please enter a different account number.");
                }

                validAccountNumber = true;
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        }

        // Validate Account Type
        while (!validAccountType) {
            try {
                acc_type = JOptionPane.showInputDialog("Enter Account type (Current, Savings, Salary, Fixed Deposit, Recurring Deposit, NRI):");
                validateAccountType(acc_type);
                validAccountType = true;
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        }

        // Validate Name
        while (!validName) {
            try {
                name = JOptionPane.showInputDialog("Enter Name (Only capital letters allowed):");
                validateName(name);
                validName = true;
            } catch (IllegalArgumentException e) {
                JOptionPane.showMessageDialog(null, e.getMessage());
            }
        }

        // Validate Balance
        try {
            balance = Long.parseLong(JOptionPane.showInputDialog("Enter Balance:"));
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(null, "Invalid balance! Please enter a valid numeric value.");
        }

        // Add the new account number to the set of used account numbers
        usedAccountNumbers.add(account);
    }

    // Check if the account number is already used
    private boolean isAccountNumberUsed(String accountNumber) {
        return usedAccountNumbers.contains(accountNumber);
    }

    // Validate Account Number
    private void validateAccountNumber(String accountNumber) {
        if (accountNumber != null && accountNumber.matches("\\d{11}")) {
            return;
        }
        throw new IllegalArgumentException("Invalid account number! Please enter an 11-digit account number.");
    }

    // Validate Account Type
    private void validateAccountType(String accountType) {
        String[] validTypes = {"Current", "Savings", "Salary", "Fixed Deposit", "Recurring Deposit", "NRI"};

        for (String validType : validTypes) {
            if (accountType.equalsIgnoreCase(validType)) {
                return;
            }
        }

        throw new IllegalArgumentException("Invalid account type! Please enter a valid account type.");
    }

    // Validate Name
    private void validateName(String inputName) {
        if (inputName != null && inputName.matches("[A-Z]+")) {
            return;
        }
        throw new IllegalArgumentException("Invalid name! Please enter only capital letters.");
    }

    // Method to display account details
    public void showAccount() {
        JOptionPane.showMessageDialog(null,
                "Name of account holder: " + name +
                        "\nAccount no.: " + account +
                        "\nAccount type: " + acc_type +
                        "\nBalance: " + balance);
    }

    // Method for depositing money
    public void deposit() {
        long amt = Long.parseLong(JOptionPane.showInputDialog("Enter the amount you want to deposit:"));
        balance = balance + amt;
        transactionHistory.add("Deposit: +" + amt);
    }

    // Method for withdrawing money
    public void withdrawal() {
        long amt = Long.parseLong(JOptionPane.showInputDialog("Enter the amount you want to withdraw:"));
        if (balance >= amt) {
            balance = balance - amt;
            transactionHistory.add("Withdrawal: -" + amt);
            JOptionPane.showMessageDialog(null, "Balance after withdrawal: " + balance);
        } else {
            JOptionPane.showMessageDialog(null, "Your balance is less than " + amt + "\tTransaction failed...!!");
        }
    }

    // Method to search for an account number
    public boolean search(String ac_no) {
        if (account.equals(ac_no)) {
            showAccount();
            return true;
        }
        return false;
    }

    // Method to display transaction history
    public void displayTransactionHistory() {
        StringBuilder history = new StringBuilder("Transaction History:\n");
        for (String transaction : transactionHistory) {
            history.append(transaction).append("\n");
        }
        JOptionPane.showMessageDialog(null, history.toString());
    }
}

// Main class to execute the banking system application
public class Main {
    public static void main(String[] arg) {
        // Input the number of bank customers
        int n = Integer.parseInt(JOptionPane.showInputDialog("How many number of customers do you want to input?"));
        BankDetails[] C = new BankDetails[n];

        // Create instances of BankDetails for each customer
        for (int i = 0; i < C.length; i++) {
            C[i] = new BankDetails();
            C[i].openAccount();
        }

        // Create the GUI frame for the banking system application
        JFrame frame = new JFrame("Banking System Application");
        frame.setSize(400, 300);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Create a panel with a grid layout
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(7, 1));


        // Create buttons for different operations
        JButton displayButton = new JButton("Display all account details");
        JButton searchButton = new JButton("Search by Account number");
        JButton depositButton = new JButton("Deposit the amount");
        JButton withdrawButton = new JButton("Withdraw the amount");
        JButton historyButton = new JButton("Transaction History");
        JButton exitButton = new JButton("Exit");

        // Add buttons to the panel
        panel.add(displayButton);
        panel.add(searchButton);
        panel.add(depositButton);
        panel.add(withdrawButton);
        panel.add(historyButton);
        panel.add(exitButton);

        // Add the panel to the center of the frame
        frame.getContentPane().add(BorderLayout.CENTER, panel);

        // ActionListener for displaying all account details
        displayButton.addActionListener(e -> {
            for (BankDetails bankDetails : C) {
                bankDetails.showAccount();
            }
        });

        // ActionListener for searching by account number
        searchButton.addActionListener(e -> {
            String ac_no = JOptionPane.showInputDialog("Enter account no. you want to search:");
            boolean found = false;
            for (BankDetails bankDetails : C) {
                found = bankDetails.search(ac_no);
                if (found) {
                    break;
                }
            }
            if (!found) {
                JOptionPane.showMessageDialog(null, "Search failed! Account doesn't exist..!!");
            }
        });

        // ActionListener for depositing money
        depositButton.addActionListener(e -> {
            String ac_no = JOptionPane.showInputDialog("Enter Account no.:");
            boolean found = false;
            for (BankDetails details : C) {
                found = details.search(ac_no);
                if (found) {
                    details.deposit();
                    break;
                }
            }
            if (!found) {
                JOptionPane.showMessageDialog(null, "Search failed! Account doesn't exist..!!");
            }
        });

        // ActionListener for withdrawing money
        withdrawButton.addActionListener(e -> {
            String ac_no = JOptionPane.showInputDialog("Enter Account No:");
            boolean found = false;
            for (BankDetails bankDetails : C) {
                found = bankDetails.search(ac_no);
                if (found) {
                    bankDetails.withdrawal();
                    break;
                }
            }
            if (!found) {
                JOptionPane.showMessageDialog(null, "Search failed! Account doesn't exist..!!");
            }
        });

        // ActionListener for displaying transaction history
        historyButton.addActionListener(e -> {
            String ac_no = JOptionPane.showInputDialog("Enter Account No:");
            boolean found = false;
            for (BankDetails bankDetails : C) {
                found = bankDetails.search(ac_no);
                if (found) {
                    bankDetails.displayTransactionHistory();
                    break;
                }
            }
            if (!found) {
                JOptionPane.showMessageDialog(null, "Search failed! Account doesn't exist..!!");
            }
        });

        // ActionListener for exiting the application
        exitButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(null, "See you soon...");
            System.exit(0);
        });

        // Make the frame visible
        frame.setVisible(true);
    }
}
