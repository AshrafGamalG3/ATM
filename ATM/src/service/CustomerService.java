package service;

import database.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CustomerService {
    public void createAccount(String name, String customerNumber, String pinNumber) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            if (isCustomerNumberExists(customerNumber, connection)) {
                System.out.println("Customer Number already exists. Please try again with a different number.");
                return;
            }
            String insertSQL = "INSERT INTO customer (customerName, customerNumber, pinNumber, customerMoney) VALUES (?, ?, ?, ?)";
            try (PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {
                preparedStatement.setString(1, name);
                preparedStatement.setString(2, customerNumber);
                preparedStatement.setString(3, pinNumber);
                preparedStatement.setString(4, "0");
                preparedStatement.executeUpdate();
                System.out.println("Customer created successfully.");
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean isValidCustomer(String customerNumber, String pinNumber) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String selectSQL = "SELECT * FROM customer WHERE customerNumber = ?";
            try (PreparedStatement preparedStatement = connection.prepareStatement(selectSQL)) {
                preparedStatement.setString(1, customerNumber);
                try (ResultSet resultSet = preparedStatement.executeQuery()) {
                    if (resultSet.next()) {
                        String dbPinNumber = resultSet.getString("pinNumber");
                        return pinNumber.equals(dbPinNumber);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private boolean isCustomerNumberExists(String customerNumber, Connection connection) {
        String query = "SELECT COUNT(*) FROM customer WHERE customerNumber = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, customerNumber);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    int count = resultSet.getInt(1);
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
