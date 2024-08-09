// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 
package server;
import java.io.*;
import entites.Message;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
//import JDBC.DbController;

import JDBC.DbController;
import enums.Commands;
import javafx.scene.control.Alert.AlertType;
import ocsf.server.*;
import controller.ServerScreenController;
import entites.*;


public class BiteMeServer extends AbstractServer 
  {
	static public ArrayList<ConnectionToClient> ClientList;

  public BiteMeServer(int port) 
  {
    super(port);
    System.out.println(port);
    ClientList = new ArrayList<ConnectionToClient>();

    //SalertThread = new NotifyThread(); no idea if it's supposed to be here 


  }
  //TODO: CONNECTION TO DB CONTROLLER
  //TODO DB CONTROLLER
  //static public DbController dbController; 
  static public DbController dbController;
  private ServerScreenController serverScreenController;
  
  //TO DO ; boolean flag for connection to database 

  
  //TODO: CONNECTION TO DB CONTROLLER
  //TODO DB CONTROLLER
  public void setDbController(DbController dbController) {
  this.dbController = dbController;
	}


/**
   * This method handles any messages received from the client.
   *
   * @param msg The message received from the client.
   * @param client The connection from which the message originated.
   * @param 
   */
  
  
  //TODO :Create Switch case for msgs from user (SHOW,UPDATE,ect...)
  
  public void handleMessageFromClient (Object msg, ConnectionToClient client)
  {
	  System.out.println(msg);

	  Message m = (Message)msg ;
	  
	  switch(m.getCmd()) {
	  
	  case ClientConnect:
		  ClientDetails newClient = new ClientDetails(client.getInetAddress().getCanonicalHostName(),client.getInetAddress().getHostAddress(),true);
		  this.serverScreenController.loadTable(newClient);
		  ClientList.add(client);
		  break;
		  
	  case ClientDisconnect:
		  ClientDetails removedClient = new ClientDetails(client.getInetAddress().getCanonicalHostName(),client.getInetAddress().getHostAddress(),true);
		  this.serverScreenController.updateTable(removedClient);
		  ServerUI.gotResponse = true;
		  break;
		  
	 /* case getRestaurantPendingOrders:
		  Object RestaurantPendingOrdersData = dbController.getRestaurantPendingOrders(m.getObj());
		  // setRestaurantPendingOrders
		  try {
			client.sendToClient(new Message(RestaurantPendingOrdersData, Commands.setRestaurantPendingOrders));
		} catch (IOException e) {
			e.printStackTrace();
		}
		  break;*/
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
          Dish newDish = (Dish) dishData[0];
          Price newPrice = (Price) dishData[1];
          DishOption newOption = dishData.length > 2 ? (DishOption) dishData[2] : null;
          boolean added;
          
          // Check if a dish with the same name and different size exists
          Dish existingDish = dbController.findDishByNameAndSize(newDish.getDishName(), newPrice.getSize());
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
          Dish dishToDelete = (Dish) m.getObj();
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
          int restaurantNumber = dbController.getRestaurantNum(employeeRestuarant);
          try {
              client.sendToClient(new Message(restaurantNumber, Commands.GetRestaurantNum));
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
  

	  	default:
	  		break;	  			  	
	  }  	  
  }
  
  
  //TODO: CONNECTION TO DB CONTROLLER
  //TODO DB CONTROLLER
  public DbController getDbController() {
	return dbController;
  }


/**
   * This method overrides the one in the superclass.  Called
   * when the server starts listening for connections.
 * @return 
 * @return 
 * @return 
   */

  protected void serverStarted() {
		System.out.println("Server listening for connections on port " + getPort());
		// Establish a connection to the database when the server starts
		//alertThread.run();

	}


public void setServerScreenController(ServerScreenController serverScreenController) {
	this.serverScreenController = serverScreenController;

}

//TODO: REMOVE THESE NOTES


  /**
   * This method overrides the one in the superclass.  Called
   * when the server stops listening for connections.
   */

//TODO: Fix ServerStopped()   
/*
    protected void serverStopped()  {
	  System.out.println("Server has stopped listening for connections.");
		try {
			conn.close();
		} // close connection to data base
		catch (SQLException e) {
			e.printStackTrace();
		}  
  }
 */


	  
   
}