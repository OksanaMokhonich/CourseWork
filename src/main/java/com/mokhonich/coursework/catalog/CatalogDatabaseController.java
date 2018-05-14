package com.mokhonich.coursework.catalog;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CatalogDatabaseController {

	// JDBC URL, username and password of MySQL server
	private static final String url = "jdbc:mysql://localhost:3306/catalogdatabase?useUnicode=true&characterEncoding=utf-8";
	private static final String user = "root";
	private static final String password = "root";

	// JDBC variables for opening and managing connection
	private static Connection con;
	private static Statement stmt;
	private static ResultSet rs;

	public static void main(String args[]) {
		openDatabaseConnection();
		String query = "INSERT INTO `categories` (`Category_Id`, `Category_Name`, `Category_Href`) VALUES (NULL, \"Щлсана1\", \"црукоцукроуркцоук\");";
		//put(query);
		addCategories("catName", "catHref");
		//addPoducts("qwqw", "productHref", "productPrice", "okok");
		System.out.println(getCategoryIdBNyName("Каблучки"));
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
				+ categoryTitle + "\", \"" + categoryHref + "\");";
		String query1 = "INSERT INTO `categories` (`Category_Id`, `Category_Name`, `Category_Href`) VALUES (NULL, \"Каблучки\", \"https://sribniyvik.ua/ua/kolca\");";
		System.out.println(query);

		try {
			stmt.executeUpdate(query);
			System.out.println("success adding category");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void addSubCategories(String subCategoryTitle, String subCategoryHref, String catHame) {
		int id = getSubCategoryIdBNyName(catHame);
		String query = "INSERT INTO `subcategories` (`Subcategory_Id`, `Subcategory_Name`, `Subategory_Href`, `Category_Id`) VALUES (NULL, \""
				+ subCategoryTitle + "\", \"" + subCategoryHref + "\", \""+id+"\");";
		System.out.println(query);

		//INSERT INTO `subcategories` (`Subcategory_Id`, `Subcategory_Name`, `Subategory_Href`, `Category_Id`) VALUES (NULL, 'ertyui', 'oiuytr', '8');
		
		try {
			stmt.executeUpdate(query);
			System.out.println("success adding subcategory");
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public static int getCategoryIdBNyName(String categoryName) {
		String selectQuery = "SELECT Category_Id FROM Categories WHERE Category_Name = \"" + categoryName + "\"";
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

	public static int getSubCategoryIdBNyName(String subCategoryName) {
		String selectQuery = "SELECT Subcategory_Id FROM Subcategories WHERE Subcategory_Name = \"" + subCategoryName
				+ "\"";
		System.out.println(selectQuery);
		
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

	public static void addPoducts(String productTitle,String productHref, String productPrice, String productImage, String subCategoryName) {
		int id = getSubCategoryIdBNyName(subCategoryName);
		String insertQuery = "INSERT INTO `products` (`Product_Id`, `Product_Name`, `Product_Href`,`Product_Image` ,`Product_Price`, `Subcategory_Id`) VALUES (NULL, \""
				+ productTitle + "\", \""+productHref+"\", \""+productImage+"\" ,\""+productPrice+"\", \""+id+"\");";
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
