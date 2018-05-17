package com.mokhonich.coursework.olx;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class OlxDatabaseController {

	private static final String DB = "jdbc:mysql://localhost:3306/olxdatabase?useUnicode=true&characterEncoding=utf-8";
	private static final String LOGIN = "root";
	private static final String PASSWORD = "root";

	private static Connection connection;
	private static Statement statement;
	private static ResultSet result;

	public static void openDatabaseConnection() {
		try {
			connection = DriverManager.getConnection(DB, LOGIN, PASSWORD);
			statement = connection.createStatement();
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
		}
	}

	public static void addPoducts(String productName, String productHref, String productPrice, String productImage,
			String productRegion) {
		String insertQuery = "INSERT INTO `products` (`Product_Id`, `Product_Name`, `Product_Href`, `Product_Price`, `Product_Image`, `Product_Region`) VALUES (NULL, \""
				+ productName + "\", \"" + productHref + "\", \"" + productPrice + "\", \"" + productImage + "\", \""
				+ productRegion + "\");";
		try {
			statement.executeUpdate(insertQuery);
			// System.out.println("success adding news");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void put(String q) {
		try {
			statement.executeUpdate(q);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void closeConnection() {
		try {
			connection.close();
		} catch (SQLException se) {
			se.printStackTrace();
		}
		try {
			statement.close();
		} catch (SQLException se) {
			se.printStackTrace();
		}
		try {
			if (result != null)
				result.close();
		} catch (SQLException se) {
			se.printStackTrace();
		}

	}

}
