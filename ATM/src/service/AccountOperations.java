package service;

import database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AccountOperations {

    public void viewBalance(String customerNumber) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String selectSQL = "SELECT * FROM customer WHERE customerNumber = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
                preparedStatement.setString(1, customerNumber);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        String customerMoney = resultSet.getString("customerMoney");
                        System.out.println("Checking Account Balance: " + customerMoney + "$");
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void withdrawFunds(String customerNumber, double amount) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            double currentBalance = getBalance(customerNumber, connection);
            if (amount > currentBalance) {
                System.out.println("Insufficient balance. Withdrawal failed.");
                return;
            }
            double newBalance = currentBalance - amount;
            updateBalance(customerNumber, newBalance, connection);
            System.out.println("Withdrawal Successful. New Balance: " + newBalance + "$");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void depositFunds(String customerNumber, double amount) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            double currentBalance = getBalance(customerNumber, connection);
            double newBalance = currentBalance + amount;
            updateBalance(customerNumber, newBalance, connection);
            System.out.println("Amount deposited successfully.");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    double getBalance(String customerNumber, Connection connection) throws SQLException {
        String selectSQL = "SELECT customerMoney FROM customer WHERE customerNumber = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
            preparedStatement.setString(1, customerNumber);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getDouble("customerMoney");
                }
            }
        }
        return 0.0;
    }
    void updateBalance(String customerNumber, double newBalance, Connection connection) throws SQLException {
        String updateSQL = "UPDATE customer SET customerMoney = ? WHERE customerNumber = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(updateSQL)) {
            preparedStatement.setDouble(1, newBalance);
            preparedStatement.setString(2, customerNumber);
            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected == 0) {
                System.out.println("Failed to update balance. Customer not found.");
            }
        }
    }
}
