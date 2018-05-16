package com.mokhonich.coursework.olx;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class OlxDatabaseController {

	// JDBC URL, username and password of MySQL server
	private static final String url = "jdbc:mysql://localhost:3306/olxdatabase?useUnicode=true&characterEncoding=utf-8";
	private static final String user = "root";
	private static final String password = "root";

	// JDBC variables for opening and managing connection
	private static Connection con;
	private static Statement stmt;
	private static ResultSet rs;

	public static void main(String args[]) {
		openDatabaseConnection();
		addPoducts("productName", "productHref", "productPrice", "productImage", "productRegion");

	}

	public static void openDatabaseConnection() {
		try {

			con = DriverManager.getConnection(url, user, password);

			stmt = con.createStatement();
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
		}
		System.out.println("success");
	}

	public static void addPoducts(String productName, String productHref, String productPrice, String productImage,
			String productRegion) {
		String insertQuery = "INSERT INTO `products` (`Product_Id`, `Product_Name`, `Product_Href`, `Product_Price`, `Product_Image`, `Product_Region`) VALUES (NULL, \""
				+ productName + "\", \""+productHref+"\", \""+productPrice+"\", \""+productImage+"\", \""+productRegion+"\");";
		System.out.println(insertQuery);

		try {
			stmt.executeUpdate(insertQuery);
			System.out.println("success adding news");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void put(String q) {
		try {
			stmt.executeUpdate(q);
			System.out.println("success");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static void closeConnection() {
		try {
			con.close();
		} catch (SQLException se) {
			/* can't do anything */ }
		try {
			stmt.close();
		} catch (SQLException se) {
			/* can't do anything */ }
		try {
			if(rs!=null)rs.close();
		} catch (SQLException se) {
			/* can't do anything */ }

	}

}
