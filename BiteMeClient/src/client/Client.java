package client;

import java.io.IOException;

import branch_manager.UpdateClientController;
import entites.Message;
import enums.Commands;
import javafx.application.Platform;
import javafx.stage.Stage;
import login.LoginScreenController; //**
import ocsf.client.AbstractClient;
import resturant.EmployeeController;


public class Client extends AbstractClient
{
	  //Instance variables **********************************************
	  
	  /**
	   * The interface type variable.  It allows the implementation of 
	   * the display method in the client.
	   */
	  ClientController clientUI; 

	  
	  //Constructors ****************************************************
	  
	  /**
	   * Constructs an instance of the chat client.
	   *
	   * @param host The server to connect to.
	   * @param port The port number to connect on.
	   * @param clientUI The interface type variable.
	   */
	  
	  //TODO: STATIC IMPORT OF DIFFERENT CONTROLLERS 
	  //IMPORT CLIENT CONTROLLERS HERE
	  static public EmployeeController employeeController;
	  static public LoginScreenController loginController; 
	  static public UpdateClientController updateClientController;


	  //static public WorkerController workerController;
	  //static public MainScreenController mainScreenController;
	  //static public BookingController bookingController;
	  //static public ReportController reportController;
	  
	  public Client(String host, int port, ClientController clientUI) 
	    throws IOException 
	  {
	    super(host, port); //Call the superclass constructor
	    this.clientUI = clientUI;
	    System.out.println("Connecting...");
	    openConnection();
	    
	    //Initilazing The Contorllers
	    //IMPORT CLIENT CONTROLLERS HERE
	    employeeController = new EmployeeController();

	    //bookingController = new BookingController();
	    //mainScreenController = new MainScreenController();
	    //workerController = new WorkerController();
	    //reportController = new ReportController();
	    //to be continued if needed
	  }

	  
	  //Instance methods ************************************************
	    
	  /**
	   * This method handles all data that comes in from the server.
	   *
	   * @param msg The message from the server.
	   */
	  public void handleMessageFromServer(Object msg) 
	  {
		  //get the message a message object from the server (getcmd,getobj) while obj is the data from the server  
	      Message m = (Message)msg;
	      //TEMPORARY TODO: SWTICH CASE
	      switch(m.getCmd()) {
    		
	  	  case terminate:
	  	    // Exit the application
    		Message newMsg = new Message(null, Commands.ClientDisconnect);
    		try {
				sendToServer(newMsg);
				this.closeConnection();
			} catch (IOException e) {
				e.printStackTrace();
			}
	         System.exit(0);
	  		 break;
	  		 
	  	 /* case setRestaurantPendingOrders:
	  		employeeController.updateTable(m.getObj());*/
	  		
	  	   case CheckUsername:
               Platform.runLater(() -> {
                   if (loginController != null) {
                       Stage currentStage = (Stage) loginController.txtUserName.getScene().getWindow();
                       try {
						loginController.handleServerResponse(m, currentStage);
					} catch (Exception e) {
						e.printStackTrace();
					}
                   } else {
                       System.err.println("LoginController is not set.");
                   }
               });
               break;         
	  	 case  UpdateStatus:
             Platform.runLater(() -> {
                 if (updateClientController != null) {
                     updateClientController.handleServerResponse(m);
                 }
             });
             break;

         default:
             break;
  
	    }
	    
	  }

	  
	  /**
	   * This method handles all data coming from the UI            
	   *
	   * @param message The message from the UI.    
	   */
	  public void handleMessageFromClientUI(String[] message)  
	  {

	    try
	    {
	    	System.out.println("sendtoserver");
	    	sendToServer(message);
	    	
	    }
	    catch(IOException e)
	    {
	      clientUI.display
	        ("Could not send message to server.  Terminating client.");
	      quit();
	    }
	  }

	  
	  /**
	   * This method terminates the client.
	   */
	  public void quit()
	  {
	    try
	    {
//	      Message msg = new Message(null,Commands.ClientConnect);
//	      ClientController.client.sendToServer(msg);

	      closeConnection();
	    }
	    catch(IOException e) {}
	    System.exit(0);
	  }


	public void handleMessageFromClientControllers(Object message) {
		try
	    {
	    	System.out.println("sendtoserver");
	    	sendToServer(message);
	    	
	    }
	    catch(IOException e)
	    {
	      clientUI.display
	        ("Could not send message to server.  Terminating client.");
	      quit();
	    }
		
	}

}
