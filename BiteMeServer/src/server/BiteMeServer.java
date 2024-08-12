// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 
package server;

import java.io.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
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

		System.out.println("Received message from client: " + msg);
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
			Object RestaurantOrdersData = dbController.getRestaurantOrders((int) m.getObj());
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
			dbController.updateRestaurantOrderStatus(orderId, status1);
			break;

		case updateCoustomerToContactByCoustomerId:
			int customerNumber = (int) m.getObj();
			User customer = dbController.getCustomerDetailsByNumber(customerNumber);
			try {
				client.sendToClient(new Message(customer, Commands.updateCoustomerToContactByCoustomerId));
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case OrderReport:

			Object[] data2 = (Object[]) m.getObj();
			String district = (String) data2[0];
			int restaurantNumber = (int) data2[1];
			String monthYear = (String) data2[2];

			int[] orderReportDetails = dbController.getOrderReport(district, restaurantNumber, monthYear);	
			
			try {
			client.sendToClient(new Message(orderReportDetails, Commands.OrderReport));
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
		case getIncomeReport:
			Object[] incomeReportData = (Object[]) m.getObj();
			int restaurantId = (int) incomeReportData[0];
			String monthYear1 = (String) incomeReportData[1];
			String district1 = (String) incomeReportData[2];
			int[] incomeReportResultData = dbController.IncomeReport(restaurantId, monthYear1, district1);
			try {
				client.sendToClient(new Message(incomeReportResultData, Commands.setIncomeReport));
			} catch (IOException e) {
				e.printStackTrace();
			}

			break;
		case getPerformanceReport:
			Object[] data3 = (Object[]) m.getObj();
			String district2 = (String) data3[0];
			String monthYear2 = (String) data3[1];
			int[] PerformanceReportDetails = dbController.performanceReport(monthYear2, district2);
			try {
				client.sendToClient(new Message(PerformanceReportDetails, Commands.getPerformanceReport));
			} catch (IOException e) {
				e.printStackTrace();
			}
			break;
			
		 case GetRestaurantDishes:
	          User employeeUser = (User) m.getObj();
	          List<Object[]> dishes = dbController.getDishesByCertifiedEmployee(employeeUser);
	          try {
	              client.sendToClient(new Message(dishes, Commands.GetRestaurantDishes));
	          } catch (IOException e) {
	              e.printStackTrace();
	          }
	          break;

	      case AddDish:
	          Object[] dishData = (Object[]) m.getObj();
	          DishUpdate newDish = (DishUpdate) dishData[0];
	          Price newPrice = (Price) dishData[1];
	          DishOption newOption = dishData.length > 2 ? (DishOption) dishData[2] : null;
	          boolean added;
	          
	          // Check if a dish with the same name and different size exists
	          DishUpdate existingDish = dbController.findDishByNameAndSize(newDish.getDishName(), newPrice.getSize());
	          if (existingDish != null) {
	        	  added = dbController.insertPriceAndOption(existingDish, newPrice, newOption);
	          }
	          else {
	        	  added = dbController.addDish(newDish, newPrice, newOption);
	          }
	 
	          try {
	              client.sendToClient(new Message(added ? "Dish added successfully" : "Failed to add dish", Commands.AddDish));
	          } catch (IOException e) {
	              e.printStackTrace();
	          }
	          break;

	      case DeleteDish:
	          DishUpdate dishToDelete = (DishUpdate) m.getObj();
	          boolean deleted = dbController.deleteDish(dishToDelete);
	          try {
	              client.sendToClient(new Message(deleted ? "Dish deleted successfully" : "Failed to delete dish", Commands.DeleteDish));
	          } catch (IOException e) {
	              e.printStackTrace();
	          }
	          break;

	      case UpdateDishPrice:
	          Price priceToUpdate = (Price) m.getObj();
	          boolean updated = dbController.updateDishPrice(priceToUpdate);
	          try {
	              client.sendToClient(new Message(updated ? "Dish price updated successfully" : "Failed to update dish price", Commands.UpdateDishPrice));
	          } catch (IOException e) {
	              e.printStackTrace();
	          }
	          break;
	      case GetRestaurantNum:
	    	  User employeeRestuarant = (User) m.getObj();
	          int restaurantNumber1 = dbController.getRestaurantNum(employeeRestuarant);
	          try {
	              client.sendToClient(new Message(restaurantNumber1, Commands.GetRestaurantNum));
	          } catch (IOException e) {
	              e.printStackTrace();
	          }
	          break;
	      case GetRestaurantName:
	    	  User employee_Restuarant = (User) m.getObj();
	          String restaurantName = dbController.getRestaurantName(employee_Restuarant);
	          try {
	              client.sendToClient(new Message(restaurantName, Commands.GetRestaurantName));
	          } catch (IOException e) {
	              e.printStackTrace();
	          }
	          break;
	      case CheckDishExists:
	          Object[] dish_Data = (Object[]) m.getObj();
	          String dishName = (String) dish_Data[0];
	          String size = (String) dish_Data[1];
	          boolean dishExists = dbController.isDishExists(dishName, size);
	          try {
	              client.sendToClient(new Message(dishExists, Commands.CheckDishExists));
	          } catch (IOException e) {
	              e.printStackTrace();
	          }
	          break;
	      case updateBegin:
	          Object[] update_begin = (Object[]) m.getObj();
	          int restaurantNum2 = (int) update_begin[0];
	          String localTimeStr = (String) update_begin[1];
	          
	          // Parse the datetime string to a LocalDateTime object
	          DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	          LocalDateTime localDateTime = LocalDateTime.parse(localTimeStr, formatter);
	          
	          // Convert LocalDateTime to Timestamp
	          Timestamp localTime = Timestamp.valueOf(localDateTime);
	          
	          dbController.updateEntryTime(restaurantNum2, localTime);
	          break;
	          
	      case EndUpdate:
	          Object[] update_end = (Object[]) m.getObj();
	          int restaurantNum3 = (int) update_end[0];
	          String localTimeStr2 = (String) update_end[1];
	          
	          // Parse the datetime string to a LocalDateTime object
	          DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	          LocalDateTime localDateTime2 = LocalDateTime.parse(localTimeStr2, formatter2);
	          
	          // Convert LocalDateTime to Timestamp
	          Timestamp localTime2 = Timestamp.valueOf(localDateTime2);
	          
	          dbController.updateExitTime(restaurantNum3, localTime2);
	          break;
	          
	      case getRestaurantList: //NEW ORDER - GET REST NAMES
	    	  ArrayList<String> restaurantNames = new ArrayList<>();
	    	  restaurantNames = dbController.getRestaurantNamesFromDB();
	    	  try {
	    	        client.sendToClient(new Message(restaurantNames, Commands.gotMyRestaurantList));
	    	    } catch (IOException e) {
	    	        e.printStackTrace();
	    	    }
	    	    break;
	    	    
	      case getRestaurantMenu: //NEW ORDER - GET REST MENU
	    	  	System.out.println(m.getObj());
	    	  	String restaurantName2 = (String)m.getObj();
	    	  	ArrayList<Map<String, Object>> menu = new ArrayList<>();
	    	  	menu = dbController.getRestaurantMenuFromDB(restaurantName2);
	    	  	System.out.println(menu);
	      	  try {
	      	        client.sendToClient(new Message(menu, Commands.gotMyRestaurantMenu));
	      	    } catch (IOException e) {
	      	        e.printStackTrace();
	      	    }
	      	    
	    	    break;
	    	    
	      case getCustomerDetails:
	    	  int userID = (int)m.getObj();
	    	  Customer customerDetails = dbController.getCustomerFromDB(userID);
	    	  System.out.println(customerDetails);
	    	  try {
	    	        client.sendToClient(new Message(customerDetails, Commands.gotMyCustomerDetails));
	    	    } catch (IOException e) {
	    	        e.printStackTrace();
	    	    }
	    	  break;
	    	  
	      case RestaurantQuarterReport1:
	    	  Object[] requestData1 = (Object[]) m.getObj();
	    	    int restaurantId1 = (int) requestData1[0];
	    	    String quarter = (String) requestData1[1];

	    	    Object[] reportData = dbController.getQuarterReportData(restaurantId1, quarter);
	    	    

	    	    try {
	    	        client.sendToClient(new Message(reportData, Commands.RestaurantQuarterReport1));
	    	    } catch (IOException e) {
	    	        e.printStackTrace();
	    	    }

	    	  break;
	      case RestaurantQuarterIncomeReport:
	    	    Object[] requestData2 = (Object[]) m.getObj();
	    	    int restaurantNumber4 = (int) requestData2[0];
	    	    String quarter1 = (String) requestData2[1];

	    	    Object[] incomeReportData3 = dbController.getQuarterIncomeReport(restaurantNumber4, quarter1);

	    	    try {
	    	        client.sendToClient(new Message(incomeReportData3, Commands.RestaurantQuarterIncomeReport));
	    	    } catch (IOException e) {
	    	        e.printStackTrace();
	    	    }
	    	    break;
	      case RestaurantQuarterReport2:
	    	  Object[] requestData3 = (Object[]) m.getObj();
	    	    int restaurantId2 = (int) requestData3[0];
	    	    String quarter2 = (String) requestData3[1];

	    	    Object[] reportData1 = dbController.getQuarterReportData(restaurantId2, quarter2);
	    	    

	    	    try {
	    	        client.sendToClient(new Message(reportData1, Commands.RestaurantQuarterReport2));
	    	    } catch (IOException e) {
	    	        e.printStackTrace();
	    	    }

	    	  break;
	      case RestaurantQuarterIncomeReport1:
	    	    Object[] requestData4 = (Object[]) m.getObj();
	    	    int restaurantNumber5 = (int) requestData4[0];
	    	    String quarter4 = (String) requestData4[1];

	    	    Object[] incomeReportData4 = dbController.getQuarterIncomeReport(restaurantNumber5, quarter4);

	    	    try {
	    	        client.sendToClient(new Message(incomeReportData4, Commands.RestaurantQuarterIncomeReport1));
	    	    } catch (IOException e) {
	    	        e.printStackTrace();
	    	    }
	    	    break;

	      case RestaurantQuarterReport3:
	    	  Object[] requestDat = (Object[]) m.getObj();
	    	    int restaurantID = (int) requestDat[0];
	    	    String quarter5 = (String) requestDat[1];

	    	    Object[] reportData5 = dbController.getQuarterReportData(restaurantID, quarter5);
	    	    

	    	    try {
	    	        client.sendToClient(new Message(reportData5, Commands.RestaurantQuarterReport3));
	    	    } catch (IOException e) {
	    	        e.printStackTrace();
	    	    }

	    	  break;
	      case RestaurantQuarterIncomeReport2:
	    	    Object[] requestDat1 = (Object[]) m.getObj();
	    	    int restaurantNumber6 = (int) requestDat1[0];
	    	    String quarter6 = (String) requestDat1[1];

	    	    Object[] incomeReportData5 = dbController.getQuarterIncomeReport(restaurantNumber6, quarter6);

	    	    try {
	    	        client.sendToClient(new Message(incomeReportData5, Commands.RestaurantQuarterIncomeReport2));
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
