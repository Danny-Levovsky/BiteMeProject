package enums;



/**
 * This enum defines various commands for communication between client and server.
 */
public enum Commands {

	ClientConnect,      //command to indicates client connection.
	ClientDisconnect,   //command to indicate client disconnection
	terminate,		    //command that calls for client disconnection
	getRestaurantPendingOrders, //command that request all Restaurant Pending Orders data From DB by restaurant ID through Server, for Employee screen
	setRestaurantPendingOrders, // command that tells Client to update Restaurant Pending Orders data From DB by restaurant ID to the Employee screen
	updateRestaurantOrderToStatus, //command that request to update order status in DB table restaurant orders
	CheckUsername, //command for Login Process 
    UpdateLoginStatus, // command for updating login status
    LogoutUser, //  command for logging out
    UpdateStatus, //command for updating customer status
    getPendingOrders, //command for customer to approve receiving order 
    getRestaurantList,
    gotMyRestaurantList,
    getRestaurantMenu, //command to get restaurant menu
    gotMyRestaurantMenu,
    getCustomerDetails,
    gotMyCustomerDetails,
    sendCustomerOrder,
    updateClientCredit 
}
