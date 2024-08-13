package server;

import javafx.application.Application;
import javafx.stage.Stage;
import ocsf.server.ConnectionToClient;
import controller.ServerScreenController;
import java.io.IOException;
import entites.Message;
import enums.Commands;

/**
 * The ServerUI class is responsible for launching the server application. It
 * provides methods to start and stop the server, manage client connections, and
 * interact with the server's graphical user interface (GUI).
 * 
 * @author yosra
 */
public class ServerUI extends Application {

	// private static Connection conn = null;
	/*
	 * private static final int DEFAULT_PORT = 5555; private static final String
	 * DB_URL = "jdbc:mysql://localhost/gonature?serverTimezone=IST"; private static
	 * final String DB_USER = "root"; private static final String DB_PASSWORD =
	 * "Aa123456";
	 */

	/**
	 * The server instance responsible for handling client connections.
	 */
	public static BiteMeServer sv = null;

	/**
	 * A flag to indicate whether a response has been received from the client.
	 */
	public static boolean gotResponse = false;

	/**
	 * The main method that launches the JavaFX application.
	 *
	 * @param args the command line arguments
	 * @throws Exception if an error occurs during the application launch
	 */
	public static void main(String args[]) throws Exception {
		launch(args);
	} // end main

	/**
	 * Starts the JavaFX application by initializing the server's GUI.
	 *
	 * @param primaryStage the primary stage for this application
	 * @throws Exception if an error occurs during the loading of the GUI
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {

		ServerScreenController aFrame = new ServerScreenController();
		aFrame.start(primaryStage);

	}

	/**
	 * Starts the JavaFX application by initializing the server's GUI.
	 *
	 * @param primaryStage the primary stage for this application
	 * @throws Exception if an error occurs during the loading of the GUI
	 */
	public static void disconnect() {
		if (isServerRunning() == true) {
			for (ConnectionToClient client : BiteMeServer.ClientList) {
				gotResponse = false;
				try {
					Message msg = new Message(null, Commands.terminate);
					client.sendToClient(msg);
					while (!gotResponse) {
						Thread.sleep(100);
					}
					client.close();
				} catch (Exception e) {
					e.printStackTrace(); // Handle the exception as needed
				}
			}
			try {
				sv.stopListening();
				sv.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			BiteMeServer.ClientList.clear();
			System.out.println("Server Disconnected");
		}

	}

	/*
	 * public static void runServer(String p) { boolean flag = false; int port = 0;
	 * //Port to listen on try { port = Integer.parseInt(p); //Set port to 5555
	 * 
	 * } catch(Throwable t) { System.out.println("ERROR - Could not connect!"); }
	 * //opening server EchoServer sv = new EchoServer(port);
	 * 
	 * try { flag = true; sv.listen(); // Start listening for connections
	 * 
	 * } catch (Exception ex) { ex.printStackTrace();
	 * System.out.println("ERROR - Could not listen for clients!"); flag = false; }
	 * }
	 */

	/**
	 * Runs the server on the specified port. If the server starts successfully, it
	 * begins listening for client connections.
	 *
	 * @param p the port number as a String
	 * @return true if the server starts successfully, false otherwise
	 */
	public static boolean runServer(String p) {
		boolean flag = false;
		int port = 0; // Port to listen on

		try {
			port = Integer.parseInt(p); // Set port to the specified value

			// Opening server
			sv = new BiteMeServer(port);

			try {
				sv.listen(); // Start listening for connections
				System.out.println("Server is running on port " + port);
				flag = true;
			} catch (Exception ex) {
				ex.printStackTrace();
				System.out.println("ERROR - Could not listen for clients!");
			}
		} catch (Throwable t) {
			System.out.println("ERROR - Could not connect!");
		}

		return flag;
	}

	/**
	 * Checks if the server is currently running and listening for client
	 * connections.
	 *
	 * @return true if the server is running and listening, false otherwise
	 */
	public static boolean isServerRunning() {
		return (sv != null && sv.isListening());
	}

}
