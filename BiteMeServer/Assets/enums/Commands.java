package enums;



/**
 * This enum defines various commands for communication between client and server.
 */
public enum Commands {

	ClientConnect,      //command to indicates client connection.
	ClientDisconnect,   //command to indicate client disconnection
	terminate,		    //command that calls for client disconnection
	getRestaurantOrders, //command that request all Restaurant Orders data From DB by restaurant ID through Server, for Employee screen
	setRestaurantOrders, // command that tells Client to update Restaurant Orders data From DB by restaurant ID to the Employee screen
	updateRestaurantOrderToStatus, //command that request to update order status in DB table restaurant orders
	CheckUsername, //command for Login Process 
    UpdateLoginStatus, // command for updating login status
    LogoutUser, //  command for logging out
    UpdateStatus, //command for updating customer status
    getPendingOrders, //command for customer to approve receiving order 
    getUserDetails, // command to get user details by user name
    setUserDetails // command that tells Client to set user details for SMS and Email in Restaurant Orders
}
