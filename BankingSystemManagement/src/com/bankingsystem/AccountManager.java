package com.bankingsystem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Scanner;


public class AccountManager {
	
	private Connection connection;
	private Scanner scanner;

	public AccountManager(Connection con, Scanner scanner) {
		// TODO Auto-generated constructor stub
		this.connection = con;
		this.scanner = scanner;
	}
	
	public void debit_money(long account_number) throws SQLException{
		scanner.nextLine();
		System.out.print("Enter Amount: ");
		double amount = scanner.nextDouble();
		scanner.nextLine();
		System.out.print("Enter Security Pin: ");
		String securty_pin = scanner.nextLine();
		try {
			connection.setAutoCommit(false);
			if(account_number!=0) {
				PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM accounts WHERE account_number = ? and security_pin = ?");
				preparedStatement.setLong(1, account_number);
				preparedStatement.setString(2, securty_pin);
				ResultSet resultSet = preparedStatement.executeQuery();
				
				if(resultSet.next()) {
					double current_balance = resultSet.getDouble("balance");
					
					if(amount<=current_balance) {
						String query = "UPDATE accounts SET balance = balance - ? WHERE account_number = ?";
						PreparedStatement preparedStatement1 = connection.prepareStatement(query);
						preparedStatement1.setDouble(1, amount);
						preparedStatement1.setLong(2, account_number);
						int affected = preparedStatement1.executeUpdate();
						if(affected > 0) {
							System.out.println("Rs. " + amount + " debited Successfully !!");
							connection.commit();
							connection.setAutoCommit(true);
						}
						else {
							System.out.println("Transaction Failed");
							connection.rollback();
							connection.setAutoCommit(true);
							
						}
					}
					else {
						System.out.println("InSufficient Balance !!");
					}
				}
				else {
					System.out.println("Invalid Pin!");
				}
			}
			else {
				System.out.println("Account Number Is not valid !");
			}
			
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void credit_money(long account_number) throws SQLException{
		scanner.nextLine();
		System.out.print("Enter Amount: ");
		double amount = scanner.nextDouble();
		scanner.nextLine();
		System.out.print("Enter Security Pin: ");
		String securty_pin = scanner.nextLine();
		
		try {
			connection.setAutoCommit(false);
			if(account_number!=0) {
				
				PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM accounts WHERE account_number = ? and security_pin = ?");
				preparedStatement.setLong(1, account_number);
				preparedStatement.setString(2, securty_pin);
				ResultSet resultSet = preparedStatement.executeQuery();
				
				if(resultSet.next()) {
					String credit_query = "UPDATE accounts SET balance = balance + ? WHERE account_number = ?";
					PreparedStatement preparedStatement1 = connection.prepareStatement(credit_query);
					preparedStatement1.setDouble(1, amount);
					preparedStatement1.setLong(2, account_number);
					
					int rowAffected = preparedStatement1.executeUpdate();
					if(rowAffected > 0) {
						System.out.println("Rs. "+amount+" credited Successfully !!");
						connection.commit();
						connection.setAutoCommit(true);
					}
					else {
						System.out.println("Transaction Failed !!");
						connection.rollback();
						connection.setAutoCommit(true);
					}
				}
				else {
					System.out.println("Invalid Pin!!");
				}
				
			}
			else {
				System.out.println("Account Number is Invalid !!");
			}
			
			
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
	}
	
	public void getBalance(long account_number){
		scanner.nextLine();
		System.out.print("Enter Security Pin: ");
		String security_pin = scanner.nextLine();
		String query = "SELECT balance FROM accounts WHERE account_number = ? and security_pin = ?";
		
		try {
			PreparedStatement preparedStatement1 = connection.prepareStatement(query);
			preparedStatement1.setLong(1, account_number);
			preparedStatement1.setString(2, security_pin);
			
			ResultSet resultSet = preparedStatement1.executeQuery();
			if(resultSet.next()) {
				double balance = resultSet.getDouble("balance");
				System.out.println("Balance: "+ balance);
			}
			else {
				System.out.println("Invalid Pin!!");
			}
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public void transferMoney(long sender_account_number) throws SQLException{
		scanner.nextLine();
		System.out.print("Enter Receiver Account Number: ");
		long receiver_account_number = scanner.nextLong();
		scanner.nextLine();
		System.out.print("Enter Amount: ");
		double amount = scanner.nextDouble();
		scanner.nextLine();
		System.out.print("Enter Security Pin: ");
		String security_pin = scanner.nextLine();
		
		try {
			connection.setAutoCommit(false);
			if(sender_account_number!=0 && receiver_account_number!=0) {
				PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM accounts WHERE account_number = ? AND security_pin = ?");
				preparedStatement.setLong(1, sender_account_number);
				preparedStatement.setString(2, security_pin);
				ResultSet resultSet = preparedStatement.executeQuery();
				
				if(resultSet.next()) {
					
					double balance = resultSet.getDouble("balance");
					if(amount <= balance) {
						
						String debit_query = "UPDATE accounts SET balance = balance - ? WHERE account_number = ?";
						String credit_query = "UPDATE accounts SET balance = balance + ? WHERE account_number = ? ";
						
						PreparedStatement debitStatement = connection.prepareStatement(debit_query);
						PreparedStatement creditStatement = connection.prepareStatement(credit_query);
						
						creditStatement.setDouble(1, amount);
						creditStatement.setLong(2, receiver_account_number);
						debitStatement.setDouble(1, amount);
						debitStatement.setLong(2, sender_account_number);
						
						int rowaffected1 = debitStatement.executeUpdate();
						int rowaffected2 = creditStatement.executeUpdate();
						if(rowaffected1 > 0 && rowaffected2 > 0) {
							System.out.println("Transaction Successfull !!");
							System.out.println("Rs. "+amount+" Transferred Successfully");
							connection.commit();
							connection.setAutoCommit(true);
							return;
						}
						else {
							System.out.println("Transaction Failed !!");
							connection.rollback();
							connection.setAutoCommit(true);
						}
						
					}
					else {
						System.out.println("Balance is Insufficient !");
					}
				}
				else {
					System.out.println("Invalid Pin!");
				}
			}
			else {
				System.out.println("Account Numbers are Invalid !!");
			}
			
		}
		catch(SQLException e) {
			e.printStackTrace();
		}
		
	}
}
