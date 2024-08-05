package enums;

//Server


/*
 * this class has various commands for communication between client and server.
 */


public enum Commands {

	
	ClientConnect,      //command to indicates client connection.
	ClientDisconnect,   //command to indicate client disconnection
	terminate,		    //command that calls for client disconnection
	getRestaurantOrders, //command that request all Restaurant Orders data From DB by restaurant ID through Server, for Employee screen
	setRestaurantOrders, // command that tells Client to update Restaurant Orders data From DB by restaurant ID to the Employee screen
	updateOrderStatus //command that request to update order status in DB
}