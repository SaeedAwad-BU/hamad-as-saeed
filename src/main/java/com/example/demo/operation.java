package com.example.demo;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class operation<E> {
    public boolean insert(String table, Class<E> clazz, List<String> list) {
        try {
            ResultSet rs = DatabaseConnection.getConnection()
                    .createStatement()
                    .executeQuery("SELECT * FROM " + table);

            ResultSetMetaData rsmd = rs.getMetaData();
            int columnCount = rsmd.getColumnCount();

            StringBuilder insert = new StringBuilder("INSERT INTO " + table + " (");

            // أسماء الأعمدة
            for (int i = 1; i <= columnCount; i++) {
                insert.append(rsmd.getColumnName(i));
                if (i < columnCount) insert.append(",");
            }

            insert.append(") VALUES (");

            // القيم
            for (int i = 1; i <= columnCount; i++) {
list.stream().forEach(syso -> System.out.println(syso));
                String val = list.get(i - 1); // لأن list 0-based

                if (rsmd.getColumnType(i) == Types.INTEGER) {

                    insert.append(val.isEmpty() ? "0" : Integer.parseInt(val));

                } else if (rsmd.getColumnType(i) == Types.DOUBLE) {

                    insert.append(val.isEmpty() ? "0.0" : Double.parseDouble(val));

                } else {
                    insert.append("'").append(val).append("'");
                }

                if (i < columnCount) insert.append(",");
            }

            insert.append(")");

            System.out.println(insert);

            return makeQuery(insert.toString());

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public String[] getField(Class<E> clazz) {
        try {
            ResultSet rs = Objects.requireNonNull(DatabaseConnection.getConnection()).createStatement().executeQuery("SELECT * FROM " + clazz.getSimpleName().toLowerCase());

            int columnCount = rs.getMetaData().getColumnCount();
            String[] str = new String[columnCount];

            for (int i = 0; i < columnCount; i++) {
                str[i] = rs.getMetaData().getColumnName(i + 1);
            }

            return str;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


	
	
	
	public String[] setField (Class<E>clazz, String id) {
		 	
		Constructor<?> constructor = clazz.getConstructors()[0];
		    Class<?>[] paramTypes = constructor.getParameterTypes();
		    String[] str=new String[clazz.getDeclaredFields().length];
		    Field[] f=clazz.getDeclaredFields();
		    book book1 = bookData.getAllBooks().stream().filter(e -> e.getBook_id()==Integer.parseInt(id)).findFirst().get();
		    System.out.println(book1);
		    return book1.toString().split(",");
		  }
	
	
	
	public boolean makeQuery(String str) {

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();){
    
             int rs = stmt.executeUpdate(str);
                return rs > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
	}
}
