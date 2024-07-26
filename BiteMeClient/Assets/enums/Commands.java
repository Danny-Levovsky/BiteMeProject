package enums;

//Server


/*
 * this class has various commands for communication between client and server.
 */


public enum Commands {

	
	ClientConnect,      //command to indicates client connection.
	ClientDisconnect,   //command to indicate client disconnection
	terminate		    //command that calls for client disconnection
	
}