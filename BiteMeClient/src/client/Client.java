package client;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import branch_manager.ReportViewController;
import branch_manager.UpdateClientController;
import ceo.QuarterReportView1;
import ceo.QuarterReportView2;
import customer.CustomerController;
import customer.NewOrderController;
import customer.ViewOrderController;
import entites.Customer;
import entites.Message;
import entites.Order;
import entites.RestaurantOrder;
import entites.User;
import enums.Commands;
import javafx.application.Platform;
import javafx.stage.Stage;
import login.LoginScreenController; 
import ocsf.client.AbstractClient;
import resturant.EmployeeController;
import resturant.UpdateMenuController;

/**
 * The Client class manages the communication between the client and the server.
 * It handles the sending and receiving of messages, and the routing of messages
 * to the appropriate controllers in the client-side application.
 * 
 * @author yosra
 */
public class Client extends AbstractClient {
	// Instance variables **********************************************

	/**
	 * The interface type variable. It allows the implementation of the display
	 * method in the client.
	 */
	ClientController clientUI;

	// Constructors ****************************************************

	// TODO: STATIC IMPORT OF DIFFERENT CONTROLLERS
	// IMPORT CLIENT CONTROLLERS HERE
	
	/**
	 * The controller for managing employee-related operations.
	 */
	static public EmployeeController employeeController;
	
	/**
	 * The controller for managing the login screen and handling user authentication.
	 */
	static public LoginScreenController loginController;
	
	/**
	 * The controller for managing the update client screen and operations.
	 */
	static public UpdateClientController updateClientController;
	
	/**
	 * The controller for managing customer-related operations.
	 */
	static public CustomerController customerController;
	
	/**
	 * The controller for managing the view order screen and handling order-related operations.
	 */
	static public ViewOrderController viewOrderController;
	
	/**
	 * The controller for managing the report view screen and displaying reports.
	 */
	static public ReportViewController reportViewController;
	
	/**
	 * The controller for managing the update menu screen and handling menu updates.
	 */
	static public UpdateMenuController updateMenuController;
	
	/**
	 * The controller for managing the new order screen and handling new order creation.
	 */
	static public NewOrderController newOrderController;
	
	/**
	 * The controller for managing the first quarterly report view for the CEO.
	 */
	static public QuarterReportView1 quarterReportView1;
	
	/**
	 * The controller for managing the second quarterly report view for the CEO.
	 */
	static public QuarterReportView2 quarterReportView2;

	/**
	 * Constructs an instance of the Client.
	 *
	 * @param host     The server to connect to.
	 * @param port     The port number to connect on.
	 * @param clientUI The interface type variable.
	 * @throws IOException if an error occurs during connection.
	 */
	public Client(String host, int port, ClientController clientUI) throws IOException {
		super(host, port); // Call the superclass constructor
		this.clientUI = clientUI;
		System.out.println("Connecting...");
		openConnection();

		updateMenuController = new UpdateMenuController();

		// Initilazing The Contorllers
		// IMPORT CLIENT CONTROLLERS HERE
		// employeeController = new EmployeeController();

		// bookingController = new BookingController();
		// mainScreenController = new MainScreenController();
		// workerController = new WorkerController();
		// reportController = new ReportController();
		// to be continued if needed
	}

	// Instance methods ************************************************

	/**
	 * This method handles all data that comes in from the server.
	 *
	 * @param msg The message from the server.
	 */
	@SuppressWarnings("unchecked")
	public void handleMessageFromServer(Object msg) {
		// get the message a message object from the server (getcmd,getobj) while obj is
		// the data from the server
		Message m = (Message) msg;
		// TEMPORARY TODO: SWTICH CASE
		switch (m.getCmd()) {

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
		case UpdateStatus:
			Platform.runLater(() -> {
				if (updateClientController != null) {
					updateClientController.handleServerResponse(m);
				}
			});
			break;
		case CheckStatus:
			Platform.runLater(() -> {
				if (customerController != null) {
					customerController.handleServerResponse(m);
				}
			});
			break;
		case getPendingOrders:
			Platform.runLater(() -> {
				List<Order> orders = (List<Order>) m.getObj();
				if (viewOrderController != null) {
					viewOrderController.updateOrderTable(orders);
				}
			});
			break;
		case UpdateCustomerOrdersStatus:
			Platform.runLater(() -> {
				Object[] orderDetails = (Object[]) m.getObj();
				int isEarlyOrder = (int) orderDetails[0];
				String dateTime = (String) orderDetails[1];
				int totalPrice = (int) orderDetails[2];
				if (viewOrderController != null) {
					try {
						viewOrderController.handleServerResponse(isEarlyOrder, dateTime, totalPrice);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			break;
		case setRestaurantOrders:
			Platform.runLater(() -> {
				List<RestaurantOrder> orders = (List<RestaurantOrder>) m.getObj();
				if (employeeController != null) {
					employeeController.setTable(orders);
				}
			});

			break;
		case updateCoustomerToContactByCoustomerId:
			Platform.runLater(() -> {
				User customer = (User) m.getObj();
				if (employeeController != null) {
					employeeController.sendTextMassageAndEmailToCustomer(customer);
				}
			});
			break;
		case OrderReport:
			Platform.runLater(() -> {
				int[] orderReportDetails = (int[]) m.getObj();
				if (reportViewController != null) {
					reportViewController.handleServerResponseOrder(orderReportDetails);
				}
			});
			break;
		case setIncomeReport:
			Platform.runLater(() -> {
				int[] incomeReportResultData = (int[]) m.getObj();
				if (reportViewController != null) {
					reportViewController.handleServerResponseIncome(incomeReportResultData);
				}
			});
			break;
		case getPerformanceReport:
			Platform.runLater(() -> {
				int[] performanceReportResultData = (int[]) m.getObj();
				if (reportViewController != null) {
					reportViewController.handleServerResponsePerformance(performanceReportResultData);
				}
			});
			break;
		case GetRestaurantDishes:
		case AddDish:
		case DeleteDish:
		case UpdateDishPrice:
		case GetRestaurantNum:
		case GetRestaurantName:
		case CheckDishExists:
			Platform.runLater(() -> {
				if (UpdateMenuController.getInstance() != null) {
					UpdateMenuController.getInstance().handleServerResponse(m);
				} else {
					System.err.println("UpdateMenuController instance is null.");
				}
			});
			break;

		case gotMyRestaurantList: // NEWORDER - GET REST NAMES
			Platform.runLater(() -> {
				ArrayList<String> restaurantNames = (ArrayList<String>) m.getObj();
				if (newOrderController != null) {
					newOrderController.setRestaurantNames(restaurantNames);
					System.out.println(restaurantNames); // checking debugging
				}
			});
			break;

		case gotMyRestaurantMenu: // NEWORDER - GET REST NAMES
			Platform.runLater(() -> {
				ArrayList<Map<String, Object>> menu = (ArrayList<Map<String, Object>>) m.getObj();
				if (newOrderController != null) {
					newOrderController.setRestaurantMenu(menu);
					System.out.println(menu); // checking debugging
				}
			});
			break;

		case gotMyCustomerDetails:
			Platform.runLater(() -> {
				Customer customerDetails = (Customer) m.getObj();
				if (newOrderController != null) {
					newOrderController.setCustomerDetails(customerDetails);
					System.out.println(customerDetails); // checking debugging
				}
			});
			break;

		case RestaurantQuarterReport1:
			Platform.runLater(() -> {
				Object[] response = (Object[]) m.getObj();
				int maxOrders = (int) response[0];
				String[] intervals = (String[]) response[1];
				int[] values = (int[]) response[2];
				if (quarterReportView1 != null) {
					quarterReportView1.handleServerResponseQuarter(maxOrders, intervals, values);
				}
			});

			break;

		case RestaurantQuarterIncomeReport:
			Platform.runLater(() -> {
				Object[] response = (Object[]) m.getObj();
				int totalIncome = (int) response[0];
				int[] values = (int[]) response[1];
				if (quarterReportView1 != null) {
					quarterReportView1.handleServerResponseQuarterIncome(totalIncome, values);
				}
			});
			break;

		case RestaurantQuarterReport2:
			Platform.runLater(() -> {
				Object[] response = (Object[]) m.getObj();
				int maxOrders = (int) response[0];
				String[] intervals = (String[]) response[1];
				int[] values = (int[]) response[2];
				if (quarterReportView2 != null) {
					quarterReportView2.handleServerResponseQuarter(maxOrders, intervals, values);
				}
			});

			break;

		case RestaurantQuarterIncomeReport1:
			Platform.runLater(() -> {
				Object[] response = (Object[]) m.getObj();
				int totalIncome = (int) response[0];
				int[] values = (int[]) response[1];
				if (quarterReportView2 != null) {
					quarterReportView2.handleServerResponseQuarterIncome(totalIncome, values);
				}
			});
			break;

		case RestaurantQuarterReport3:
			Platform.runLater(() -> {
				Object[] response = (Object[]) m.getObj();
				int maxOrders = (int) response[0];
				String[] intervals = (String[]) response[1];
				int[] values = (int[]) response[2];
				if (quarterReportView2 != null) {
					quarterReportView2.handleServerResponseQuarter1(maxOrders, intervals, values);
				}
			});

			break;

		case RestaurantQuarterIncomeReport2:
			Platform.runLater(() -> {
				Object[] response = (Object[]) m.getObj();
				int totalIncome = (int) response[0];
				int[] values = (int[]) response[1];
				if (quarterReportView2 != null) {
					quarterReportView2.handleServerResponseQuarterIncome1(totalIncome, values);
				}
			});
			break;
		default:
			break;

		}

	}

	/**
	 * This method handles all data coming from the UI and sends it to the server.
	 *
	 * @param message The message from the UI.
	 */
	public void handleMessageFromClientUI(String[] message) {

		try {
			System.out.println("sendtoserver");
			sendToServer(message);

		} catch (IOException e) {
			clientUI.display("Could not send message to server.  Terminating client.");
			quit();
		}
	}

	/**
	 * Handles the termination command from the server.
	 */
	public void quit() {
		try {
			closeConnection();
		} catch (IOException e) {
		}
		System.exit(0);
	}

	/**
	 * This method handles all data coming from the client controllers and sends it
	 * to the server.
	 *
	 * @param message The message from the client controllers.
	 */
	public void handleMessageFromClientControllers(Object message) {
		try {
			System.out.println("sendtoserver");
			sendToServer(message);

		} catch (IOException e) {
			clientUI.display("Could not send message to server.  Terminating client.");
			quit();
		}
	}

}
