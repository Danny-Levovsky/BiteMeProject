package enums;

//Server


/*
 * this class has various commands for communication between client and server.
 */


public enum Commands {

	
	ClientConnect,      //command to indicates client connection.
	ClientDisconnect,   //command to indicate client disconnection
	terminate,		    //command that calls for client disconnection
	getRestaurantPendingOrders, //command that request all Restaurant Pending Orders data From DB by restaurant ID through Server, for Employee screen
	setRestaurantPendingOrders, // command that tells Client to update Restaurant Pending Orders data From DB by restaurant ID to the Employee screen
	CheckUsername //for Login Process 
}