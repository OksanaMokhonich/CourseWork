package com.mokhonich.coursework.catalog;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CatalogDatabaseController {
	private static final String DB = "jdbc:mysql://localhost:3306/catalogdatabase?useUnicode=true&characterEncoding=utf-8";
	private static final String LOGIN = "root";
	private static final String PASSWORD = "root";

	private static Connection connection;
	private static Statement statement;
	private static ResultSet rezult;

	public static void openDatabaseConnection() {
		try {
			connection = DriverManager.getConnection(DB, LOGIN, PASSWORD);
			statement = connection.createStatement();
		} catch (SQLException sqlEx) {
			sqlEx.printStackTrace();
		}
	}

	public static void addCategories(String categoryTitle, String categoryHref) {
		String query = "INSERT INTO `categories` (`Category_Id`, `Category_Name`, `Category_Href`) VALUES (NULL, \""
				+ categoryTitle + "\", \"" + categoryHref + "\");";

		try {
			statement.executeUpdate(query);
			// System.out.println("success adding category");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void addSubCategories(String subCategoryTitle, String subCategoryHref, String catHame) {
		int id = getSubCategoryIdBNyName(catHame);
		String query = "INSERT INTO `subcategories` (`Subcategory_Id`, `Subcategory_Name`, `Subategory_Href`, `Category_Id`) VALUES (NULL, \""
				+ subCategoryTitle + "\", \"" + subCategoryHref + "\", \"" + id + "\");";
		try {
			statement.executeUpdate(query);
			// System.out.println("success adding subcategory");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static int getCategoryIdBNyName(String categoryName) {
		String selectQuery = "SELECT Category_Id FROM Categories WHERE Category_Name = \"" + categoryName + "\"";
		try {
			rezult = statement.executeQuery(selectQuery);
			while (rezult.next()) {
				int count = rezult.getInt(1);
				return count;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return 0;
	}

	public static int getSubCategoryIdBNyName(String subCategoryName) {
		String selectQuery = "SELECT Subcategory_Id FROM Subcategories WHERE Subcategory_Name = \"" + subCategoryName
				+ "\"";
		// System.out.println(selectQuery);

		try {
			rezult = statement.executeQuery(selectQuery);

			while (rezult.next()) {
				int count = rezult.getInt(1);
				return count;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return 0;
	}

	public static void addPoducts(String productTitle, String productHref, String productPrice, String productImage,
			String subCategoryName) {
		int id = getSubCategoryIdBNyName(subCategoryName);
		String insertQuery = "INSERT INTO `products` (`Product_Id`, `Product_Name`, `Product_Href`,`Product_Image` ,`Product_Price`, `Subcategory_Id`) VALUES (NULL, \""
				+ productTitle + "\", \"" + productHref + "\", \"" + productImage + "\" ,\"" + productPrice + "\", \""
				+ id + "\");";
		//System.out.println(insertQuery);

		try {
			statement.executeUpdate(insertQuery);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void put(String q) {
		try {
			statement.executeUpdate(q);
			System.out.println("success");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void closeConnection() {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			statement.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		try {
			rezult.close();
		} catch (SQLException e) {
			e.printStackTrace();

		}

	}
}
