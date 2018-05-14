package com.mokhonich.coursework.news;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class NewsDatabaseController {

	// JDBC URL, username and password of MySQL server
	private static final String url = "jdbc:mysql://localhost:3306/newsdatabase?useUnicode=true&characterEncoding=utf-8";
	private static final String user = "root";
	private static final String password = "root";

	// JDBC variables for opening and managing connection
	private static Connection con;
	private static Statement stmt;
	private static ResultSet rs;

	public static void main(String args[]) {
		openDatabaseConnection();
		String query = "INSERT INTO `categories` (`Category_Id`, `Category_Name`, `Category_Href`) VALUES (NULL, \"Щлсана1\", \"црукоцукроуркцоук\");";
		put(query);
		addCategories("catName", "catHref");
		System.out.println(getCategoryIdBNyName("Головне"));
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

	public static void addCategories(String categoryTitle, String categoryHref) {
		String query = "INSERT INTO `categories` (`Category_Id`, `Category_Name`, `Category_Href`) VALUES (NULL, \""
				+ categoryTitle + "\", \""	 + categoryHref + "\");";
		System.out.println(query);

		try {
			stmt.executeUpdate(query);
			System.out.println("success adding category");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static int getCategoryIdBNyName(String categoryName) {
		String selectQuery = "SELECT Category_Id FROM Categories WHERE Category_Name = \""+categoryName+"\"";
		try {
			rs = stmt.executeQuery(selectQuery);
			while (rs.next()) {
				int count = rs.getInt(1);
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
			rs.close();
		} catch (SQLException se) {
			/* can't do anything */ }

	}

}
