package com.example.demo;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class AuthorData {
    public static ObservableList<Author> getAllAuthors() {
    	ObservableList<Author> authors = FXCollections.observableArrayList();
        String query = "SELECT author_id,CONCAT(first_name,' ',last_name) AS full_name,country,bio FROM author";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                Author author = new Author(
                        rs.getInt("author_id"),
                        rs.getString("full_name"),
                        rs.getString("country"),
                        rs.getString("bio"));
                authors.add(author);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return authors;
    }
}

