package com.example.demo;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.stream.Stream;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class operation<E> {
	public void insert(String table, Class<E>clazz, List<String> list) {
			String insert = "INSERT INTO "+table+" (";
		  Constructor<?> constructor = clazz.getConstructors()[0];
		    Class<?>[] paramTypes = constructor.getParameterTypes();
		    for (int i = 0; i <clazz.getDeclaredFields().length; i++) {
			    Field[] f=clazz.getDeclaredFields();
			    if(i<clazz.getDeclaredFields().length-1)
		    insert+=f[i].getName()+",";
			    else insert+=f[i].getName();
		    }
		    insert+=") VALUES (";
		    for(int i=0; i<list.size(); i++) {
		    	if(paramTypes[i]==int.class)
		    	insert+=Integer.parseInt(list.get(i));
		    	else if(paramTypes[i]==double.class)
		    	insert+=Double.parseDouble(list.get(i));
		    	else
		    	insert+="\""+list.get(i)+"\"";
		    	if(i<list.size()-1) insert+=",";
		    }
		    insert+=")";
		    System.out.println(insert);
		    makeQuery(insert);
	}
	public String[] getField (Class<E>clazz) {
		 Constructor<?> constructor = clazz.getConstructors()[0];
		    Class<?>[] paramTypes = constructor.getParameterTypes();
		    String[] str=new String[clazz.getDeclaredFields().length];
		    Field[] f=clazz.getDeclaredFields();
		    for (int i = 0; i <clazz.getDeclaredFields().length; i++) {
		    str[i]=f[i].getName();
	}
		    return str;
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
	
	
	
	public void makeQuery(String str) {

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();){
    
             int rs = stmt.executeUpdate(str); 

        } catch (SQLException e) {
            e.printStackTrace();
        }
	}
}
