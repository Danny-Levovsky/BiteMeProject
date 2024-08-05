
// This file contains material supporting section 3.7 of the textbook:
// "Object Oriented Software Engineering" and is issued under the open-source
// license found at www.lloseng.com 
package server;
import java.io.*;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
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
		  
	  case getRestaurantOrders:
		  Object RestaurantPendingOrdersData = dbController.getRestaurantOrders(m.getObj());
		  // setRestaurantPendingOrders
		  try {
			client.sendToClient(new Message(RestaurantPendingOrdersData, Commands.setRestaurantOrders));
		} catch (IOException e) {
			e.printStackTrace();
		}
		  break;

	  	default:
	  		System.out.println("Shouldn't have gotten here?!?!?!");
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
