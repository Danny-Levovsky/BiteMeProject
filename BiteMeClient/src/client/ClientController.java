package client;

import java.io.IOException;
import java.net.ConnectException;

import entites.Message;
import enums.Commands;

/**
 * The ClientController class is responsible for managing the connection between
 * the client and the server. It initializes the client and handles
 * communication with the server.
 * 
 * @author yosra
 */
public class ClientController {

	// Instance variables **********************************************

	/**
	 * The instance of the client that handles the connection to the server.
	 */
	static public Client client;

	/**
	 * Constructs an instance of the ClientController, establishing a connection to
	 * the specified host and port.
	 * 
	 * @param host The host to connect to.
	 * @param port The port to connect on.
	 * @throws ConnectException if unable to connect to the server at the specified
	 *                          IP address.
	 */
	public ClientController(String host, int port) throws ConnectException {
		try {
			client = new Client(host, port, this);
			Message msg = new Message(null, Commands.ClientConnect);
			client.sendToServer(msg);
		} catch (IOException exception) {
			throw new ConnectException("Unable to connect to the IP address");
			// System.exit(1);
		}
	}

	/**
	 * Displays a message in the client console.
	 * 
	 * @param message The message to be displayed.
	 */
	public void display(String message) {
		System.out.println("> " + message);
	}
}