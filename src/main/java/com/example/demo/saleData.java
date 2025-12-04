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
        String query ="SELECT sale_id,b.title,CONCAT(br.first_name,' ',br.last_name) AS borrower_name ,sale_date,sale_price FROM sale LEFT JOIN book as b ON b.book_id=sale.book_id LEFT JOIN borrower as br ON br.borrower_id=sale.book_id";
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
            	sale sale = new sale(
                        rs.getInt("sale_id"),
                        rs.getString("title"),
                        rs.getString("borrower_name"),
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
