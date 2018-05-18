package com.mokhonich.coursework.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class NewsDatabaseController {
	
	private static final String DB = "jdbc:mysql://localhost:3306/newsdatabase?useUnicode=true&characterEncoding=utf-8";
	private static final String LOGIN = "root";
	private static final String PASSWORD = "root";

	private static Connection connnection;
	private static Statement statement;
	private static ResultSet result;

	public static void openDatabaseConnection() {
		try {

			connnection = DriverManager.getConnection(DB, LOGIN, PASSWORD);
			statement = connnection.createStatement();
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
		}
		//System.out.println("success");
	}

	public static void addCategories(String categoryTitle, String categoryHref) {
		String query = "INSERT INTO `categories` (`Category_Id`, `Category_Name`, `Category_Href`) VALUES (NULL, \""
				+ categoryTitle + "\", \""	 + categoryHref + "\");";
		//System.out.println(query);

		try {
			statement.executeUpdate(query);
			System.out.println("success adding category");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static int getCategoryIdBNyName(String categoryName) {
		String selectQuery = "SELECT Category_Id FROM Categories WHERE Category_Name = \""+categoryName+"\"";
		try {
			result = statement.executeQuery(selectQuery);
			while (result.next()) {
				int count = result.getInt(1);
				return count;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return 0;
	}

	public static void addNews(String newTitle, String newDateTime, String newHref, String categoryName) {
		int catId = getCategoryIdBNyName(categoryName);
		String insertQuery = "INSERT INTO `newstable` (`New_Id`, `New_Date_Time`, `New_Href`, `New_Title`, `Category_Id`) VALUES (NULL, \""
				+ newDateTime + "\", \""+newHref+"\", \"" + newTitle+ "\", \""+catId+"\");";
		//System.out.println(insertQuery);
		try {
			statement.executeUpdate(insertQuery);
			//System.out.println("success adding news");
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
			connnection.close();
		} catch (SQLException se) {
			se.printStackTrace(); }
		try {
			statement.close();
		} catch (SQLException se) {
			se.printStackTrace(); }
		try {
			if(result!=null)result.close();
		} catch (SQLException se) {
			se.printStackTrace(); }

	}

}
