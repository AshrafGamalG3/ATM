import service.AccountOperations;
import service.CustomerService;
import service.MoneyTransferService;

import java.sql.*;
import java.util.Scanner;

public class Main {
    static Scanner scanner = new Scanner(System.in);
    static CustomerService customerService = new CustomerService();
    static AccountOperations accountOperations = new AccountOperations();
    static MoneyTransferService moneyTransferService = new MoneyTransferService();

    public static void main(String[] args) {
        System.out.println("Type 1 - Create Account");
        System.out.println("Type 2 - Execute an operation");
        System.out.print("Please choose a number from 1 - 2: ");
        int choice = scanner.nextInt();
        scanner.nextLine();
        chooseOption(choice);
    }
    public static void chooseOption(int choice) {
        switch (choice) {
            case 1:
                createAccount();
                break;
            case 2:
                executeOperation();
                break;
            default:
                System.out.println("Invalid choice. Please choose again.");
                int newChoice = scanner.nextInt();
                scanner.nextLine();
                chooseOption(newChoice);
                break;
        }
    }
    public static void createAccount() {
        System.out.print("Enter your customer name: ");
        String customerName = scanner.nextLine();
        System.out.print("Enter your customer number: ");
        String customerNumber = scanner.nextLine();
        System.out.print("Enter your pin number: ");
        String pinNumber = scanner.nextLine();
        customerService.createAccount(customerName, customerNumber, pinNumber);
    }
    public static void executeOperation() {
        System.out.print("Enter your customer number: ");
        String customerNumber = scanner.nextLine();
        System.out.print("Enter your pin number: ");
        String pinNumber = scanner.nextLine();

        if (customerService.isValidCustomer(customerNumber, pinNumber)) {
            System.out.println("Type 1 - View balance");
            System.out.println("Type 2 - Withdraw funds");
            System.out.println("Type 3 - Deposit funds");
            System.out.println("Type 4 - Transfer funds");
            System.out.print("Please choose a number from 1 - 4: ");
            int operationChoice = scanner.nextInt();
            scanner.nextLine();
            handleOperationChoice(customerNumber, operationChoice);
        } else {
            System.out.println("Invalid customer number or pin number.");
        }
    }
    public static void handleOperationChoice(String customerNumber, int operationChoice) {
        switch (operationChoice) {
            case 1:
                accountOperations.viewBalance(customerNumber);
                break;
            case 2:
                System.out.print("Enter amount to withdraw: ");
                double withdrawAmount = scanner.nextDouble();
                scanner.nextLine();
                accountOperations.withdrawFunds(customerNumber, withdrawAmount);
                break;
            case 3:
                System.out.print("Enter amount to deposit: ");
                double depositAmount = scanner.nextDouble();
                scanner.nextLine();
                accountOperations.depositFunds(customerNumber, depositAmount);
                break;
            case 4:
                System.out.print("Enter the recipient's customer number: ");
                String toCustomerNumber = scanner.nextLine();
                System.out.print("Enter amount to transfer: ");
                double transferAmount = scanner.nextDouble();
                scanner.nextLine();
                if (moneyTransferService.isValidCustomer(toCustomerNumber)) {
                    moneyTransferService.transferMoney(customerNumber, toCustomerNumber, transferAmount);
                } else {
                    System.out.println("Invalid recipient customer number.");
                }
                break;
            default:
                System.out.println("Invalid choice.");
                break;
        }
    }
}


