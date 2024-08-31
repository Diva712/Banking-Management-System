package com.bankingsystem;

import java.sql.*;
import java.sql.Connection;
import java.util.Scanner;

public class User {
	private Connection connection;
	private Scanner scanner;
	
	public User(Connection con, Scanner scanner) {
		// TODO Auto-generated constructor stub
		this.connection = con;
		this.scanner = scanner;
	}
	
	public void register() {
		scanner.nextLine();
		System.out.println("Full Name: ");
		String full_name = scanner.nextLine();
		System.out.println("Email: ");
		String email = scanner.nextLine();
		System.out.println("Password: ");
		String password = scanner.nextLine();
		
		
		if(user_exist(email)) {
			System.out.println("User is Already Exist for this Email Address !");
			return;
		}
		
		String register_query = "INSERT INTO user(full_name , email , password) VALUES(?,?,?)";
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(register_query);
			preparedStatement.setString(1, full_name);
			preparedStatement.setString(2, email);
			preparedStatement.setString(3, password);
			
			int affectedRows = preparedStatement.executeUpdate();
			if(affectedRows > 0) {
				System.out.println("Registration Successfull !");
			}
			else {
				System.out.println("Registration Failed");
			}
			
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	public String login() {
		scanner.nextLine();
		System.out.println("Email: ");
		String email = scanner.nextLine();
		System.out.println("Password: ");
		String password = scanner.nextLine();
		
		String login_query = "SELECT * FROM user WHERE email = ? AND password = ?";
		try {
			
			PreparedStatement preparedStatement = connection.prepareStatement(login_query);
			preparedStatement.setString(1, email);
			preparedStatement.setString(2, password);
			ResultSet resultSet = preparedStatement.executeQuery();
			if(resultSet.next()) {
				return email;
			}
			else {
				return null;
			}
			
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		
		return null;
	}
	
	
	
	public boolean user_exist(String email) {
		
		String user_exist_query = "SELECT * FROM user WHERE email = ?";
		
		
		try {
			
			PreparedStatement preparedStatement = connection.prepareStatement(user_exist_query);
			preparedStatement.setString(1, email);
			ResultSet resultset = preparedStatement.executeQuery();
			
			if(resultset.next()) {
				return true;
			}
			else {
				return false;
			}
			
			
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		
		return false;
	}

}
