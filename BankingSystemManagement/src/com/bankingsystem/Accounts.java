package com.bankingsystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Scanner;

public class Accounts {
	
	private Connection connection;
	private Scanner scanner;
	

	public Accounts(Connection con, Scanner scanner) {
		// TODO Auto-generated constructor stub
		this.connection = con;
		this.scanner=scanner;
	}
	public long open_account(String email) {
		if(account_exist(email) == false) {
			
			String open_account_query = "INSERT INTO accounts(account_number , full_name , email , balance , security_pin) VALUES(?,?,?,?,?)";
			scanner.nextLine();
			System.out.println("Eneter Full Name: ");
			String full_name = scanner.nextLine();
			System.out.println("Enter Initial Amount: ");
			double balance = scanner.nextDouble();
			scanner.nextLine();
			System.out.println("Enter Sequrity Pin: ");
			String security_pin = scanner.nextLine();
			
			try {
				
				long account_number = generateAccountNumber();
				PreparedStatement preparedStatement = connection.prepareStatement(open_account_query);
				preparedStatement.setLong(1, account_number);
				preparedStatement.setString(2, full_name);
				preparedStatement.setString(3, email);
				preparedStatement.setDouble(4,balance);
				preparedStatement.setString(5, security_pin);
				
				int affectedRows = preparedStatement.executeUpdate();
				if(affectedRows > 0) {
					return account_number;
				}
				else {
					throw new RuntimeException("Account Creation Failed !!");
				}
				
			}
			catch(SQLException e) {
				e.printStackTrace();
			}
		}
		throw new RuntimeException("Already Have an Account !!");
	}
	public boolean account_exist(String email) {
		String account_exist_query = "SELECT * FROM accounts WHERE email = ?";
		
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(account_exist_query);
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

	public long generateAccountNumber() {
		try {
			Statement statement = connection.createStatement();
			String query = "SELECT account_number FROM accounts ORDER BY account_number DESC LIMIT 1";
			ResultSet resultset = statement.executeQuery(query);
			if(resultset.next()) {
				long last_account_number = resultset.getLong("account_number");
				return last_account_number+1;
			}
			else {
				return 10000100;
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		
		return 10000100;
	}
	
	public long getAccountNumber(String email) {
		String query = "SELECT account_number FROM accounts WHERE email = ?";
		
		try {
			PreparedStatement preparedStatement = connection.prepareStatement(query);
			preparedStatement.setString(1, email);
			ResultSet resultSet = preparedStatement.executeQuery();
			if(resultSet.next()) {
				return resultSet.getLong("account_number");
			}
			
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		
		throw new RuntimeException("Account Number Does't Exist !!");
	}
	

}
