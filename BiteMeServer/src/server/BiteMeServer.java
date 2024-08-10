// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 
package server;

import java.io.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
//import JDBC.DbController;

import JDBC.DbController;
import enums.Commands;
import javafx.scene.control.Alert.AlertType;
import ocsf.server.*;
import controller.ServerScreenController;
import entites.*;

public class BiteMeServer extends AbstractServer {
	static public ArrayList<ConnectionToClient> ClientList;

	public BiteMeServer(int port) {
		super(port);
		System.out.println(port);
		ClientList = new ArrayList<ConnectionToClient>();

		// SalertThread = new NotifyThread(); no idea if it's supposed to be here

	}

	// TODO: CONNECTION TO DB CONTROLLER
	// TODO DB CONTROLLER
	// static public DbController dbController;
	static public DbController dbController;
	private ServerScreenController serverScreenController;

	// TO DO ; boolean flag for connection to database

	// TODO: CONNECTION TO DB CONTROLLER
	// TODO DB CONTROLLER
	public void setDbController(DbController dbController) {
		this.dbController = dbController;
	}

	/**
	 * This method handles any messages received from the client.
	 *
	 * @param msg    The message received from the client.
	 * @param client The connection from which the message originated.
	 * @param
	 */

	// TODO :Create Switch case for msgs from user (SHOW,UPDATE,ect...)

	public void handleMessageFromClient(Object msg, ConnectionToClient client) {

		System.out.println(msg);
		Message m = (Message) msg;

		switch (m.getCmd()) {

		case ClientConnect:
			ClientDetails newClient = new ClientDetails(client.getInetAddress().getCanonicalHostName(),
					client.getInetAddress().getHostAddress(), true);
			this.serverScreenController.loadTable(newClient);
			ClientList.add(client);
			break;

		case ClientDisconnect:
			ClientDetails removedClient = new ClientDetails(client.getInetAddress().getCanonicalHostName(),
					client.getInetAddress().getHostAddress(), true);
			this.serverScreenController.updateTable(removedClient);
			ServerUI.gotResponse = true;
			break;
	
		case CheckUsername:
			User user = (User) m.getObj();
			boolean usernameExists = dbController.isUsernameExists(user.getUsername());
			if (!usernameExists) {
				try {
					client.sendToClient(new Message("username not found", Commands.CheckUsername));
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				boolean passwordCorrect = dbController.isPasswordCorrect(user.getUsername(), user.getPassword());
				if (!passwordCorrect) {
					try {
						client.sendToClient(new Message("incorrect password", Commands.CheckUsername));
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					User completeUser = dbController.getUserDetails(user.getUsername());
					try {
						client.sendToClient(new Message(completeUser, Commands.CheckUsername));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
			break;
			
		case UpdateLoginStatus:
			int userId = (int) m.getObj();
			dbController.updateLoginStatus(userId, 1);
			break;
			
		case LogoutUser:
			int logoutUserId = (int) m.getObj();
			dbController.updateLoginStatus(logoutUserId, 0);
			break;
			
		case UpdateStatus:
			Object[] requestData = (Object[]) m.getObj();
			int userId1 = (int) requestData[0];
			String branchManagerDistrict = (String) requestData[1];
			User user1 = dbController.getUserDetailsById(userId1);

			if (user1 == null) {
				try {
					client.sendToClient(new Message("user not found", Commands.UpdateStatus));
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else if (!user1.getDistrict().equals(branchManagerDistrict) || !user1.getType().equals("Customer")) {
				try {
					client.sendToClient(new Message("no permission", Commands.UpdateStatus));
				} catch (IOException e) {
					e.printStackTrace();
				}
			} else {
				String customerStatus = dbController.getCustomerStatus(userId1);
				if (customerStatus.equals("active")) {
					try {
						client.sendToClient(new Message("user already active", Commands.UpdateStatus));
					} catch (IOException e) {
						e.printStackTrace();
					}
				} else {
					dbController.updateCustomerStatus(userId1, "active");
					try {
						client.sendToClient(new Message("status updated successfully", Commands.UpdateStatus));
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

			break;
			
		case getPendingOrders:
			int customerId = (int) m.getObj();
			List<Order> pendingOrders = dbController.getPendingOrders(customerId);
			try {
				client.sendToClient(new Message(pendingOrders, Commands.getPendingOrders));
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
			
		case CheckStatus:
			int id = (int) m.getObj();
			String status = dbController.getCustomerStatus(id);
			try {
				client.sendToClient(new Message(status, Commands.CheckStatus));
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
			
		case UpdateCustomerOrdersStatus:
			try {
				int orderId;
				String receivedDateTime;
				Object[] updateData = (Object[]) m.getObj();
				orderId = (Integer) updateData[0];
				receivedDateTime = (String) updateData[1];
				Object[] orderDetails = dbController.updateOrderStatus(orderId, receivedDateTime);
				client.sendToClient(new Message(orderDetails, Commands.UpdateCustomerOrdersStatus));
			} catch (Exception e) {
				System.err.println("Error processing UpdateCustomerOrdersStatus: " + e.getMessage());
				e.printStackTrace();
			}
			break;
			
			case UpdateCredit:
                try {
                    Object[] data = (Object[]) m.getObj();
                    int id1 = (int) data[0];
                    int orderId = (int) data[1];
                    int credit = (int) data[2];
                    dbController.updateCredit(id1, orderId, credit);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
                
			 case getRestaurantOrders:
				  Object RestaurantOrdersData = dbController.getRestaurantOrders((int)m.getObj());
				  // setRestaurantPendingOrders
				  try {
					client.sendToClient(new Message(RestaurantOrdersData, Commands.setRestaurantOrders));
				} catch (IOException e) {
					e.printStackTrace();
				}
				  break;
				  
			  case updateRestaurantOrderStatus:
				  Object[] data1 = (Object[]) m.getObj();
				  int orderId = (int) data1[0];
				  String status1 = (String) data1[1];
				  dbController.updateRestaurantOrderStatus(orderId,status1);
				  break;
				  
			  case updateCoustomerToContactByCoustomerId: 
				  int customerNumber = (int) m.getObj();
				  User customer = dbController.getCustomerDetailsByNumber(customerNumber);
				  try {
						client.sendToClient(new Message(customer, Commands.updateCoustomerToContactByCoustomerId));
					} catch (IOException e) {
						e.printStackTrace();
					}
				  
			  case getIncomeReport:
				  Object[] incomeReportData = (Object[]) m.getObj();
				  int restaurantId = (int) incomeReportData[0];
				  String monthYear = (String) incomeReportData[1];
				  String district = (String) incomeReportData[2];
				  int[] incomeReportResultData = dbController.IncomeReport(restaurantId, monthYear, district);
				  try {
						client.sendToClient(new Message(incomeReportResultData, Commands.setIncomeReport));
					} catch (IOException e) {
						e.printStackTrace();
					}
				  
				  break;
				  
				default:
					break;
				}
	}

	// TODO: CONNECTION TO DB CONTROLLER
	// TODO DB CONTROLLER
	public DbController getDbController() {
		return dbController;
	}

	/**
	 * This method overrides the one in the superclass. Called when the server
	 * starts listening for connections.
	 * 
	 * @return
	 * @return
	 * @return
	 */

	protected void serverStarted() {
		System.out.println("Server listening for connections on port " + getPort());
		// Establish a connection to the database when the server starts
		// alertThread.run();

	}

	public void setServerScreenController(ServerScreenController serverScreenController) {
		this.serverScreenController = serverScreenController;

	}

//TODO: REMOVE THESE NOTES

	/**
	 * This method overrides the one in the superclass. Called when the server stops
	 * listening for connections.
	 */

//TODO: Fix ServerStopped()   
	/*
	 * protected void serverStopped() {
	 * System.out.println("Server has stopped listening for connections."); try {
	 * conn.close(); } // close connection to data base catch (SQLException e) {
	 * e.printStackTrace(); } }
	 */

}
