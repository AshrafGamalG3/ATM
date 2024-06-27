package service;

import database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class MoneyTransferService {

    public void transferMoney(String fromCustomerNumber, String toCustomerNumber, double amount) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            AccountOperations accountOperations = new AccountOperations();
            double fromBalance = accountOperations.getBalance(fromCustomerNumber, connection);
            if (fromBalance < amount) {
                System.out.println("Insufficient balance. Transfer failed.");
                return;
            }
            double toBalance = accountOperations.getBalance(toCustomerNumber, connection);
            accountOperations.updateBalance(fromCustomerNumber, fromBalance - amount, connection);
            accountOperations.updateBalance(toCustomerNumber, toBalance + amount, connection);
            System.out.println("Transfer successful.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public boolean isValidCustomer(String customerNumber) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String selectSQL = "SELECT customerNumber FROM customer WHERE customerNumber = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
                preparedStatement.setString(1, customerNumber);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    return resultSet.next();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
