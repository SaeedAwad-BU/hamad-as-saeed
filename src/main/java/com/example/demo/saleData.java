package com.example.demo;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class saleData {
	public static ObservableList<sale> getAllSales() {
    	ObservableList<sale> sales = FXCollections.observableArrayList();
        String query = "SELECT * FROM sale";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
            	sale sale = new sale(
                        rs.getInt("sale_id"),
                        rs.getInt("book_id"),
                        rs.getInt("borrower_id"),
                        rs.getString("sale_date"),
                        rs.getDouble("sale_price"));
            	sales.add(sale);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sales;
    }
}
