package resturant;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import java.io.IOException;
import java.util.List;
import client.ClientController;
import entites.Category;
import entites.DishUpdate;
import entites.DishOption;
import entites.Message;
import entites.Price;
import entites.User;
import enums.Commands;
import enums.OptionType;
import javafx.scene.control.Alert;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Controller class for updating the restaurant menu. This class handles the
 * user interface and logic for adding, deleting, and updating dishes in the
 * menu.
 * 
 * @author yosra
 */
public class UpdateMenuController {
	@FXML
	private TableView<DishDisplay> txtTable;
	@FXML
	private TableColumn<DishDisplay, String> nameColumn;
	@FXML
	private TableColumn<DishDisplay, String> optionColumn;
	@FXML
	private TableColumn<DishDisplay, String> sizeColumn;
	@FXML
	private TableColumn<DishDisplay, Integer> priceColumn;
	@FXML
	private TableColumn<DishDisplay, String> categoryColumn;
	@FXML
	private ChoiceBox<String> categoryBox;
	@FXML
	private ChoiceBox<String> OptionBox;
	@FXML
	private TextField txtDishName;
	@FXML
	private TextField txtDishSize;
	@FXML
	private TextField txtDishPrice;
	@FXML
	private TextField txtDishOption;
	@FXML
	private Button btnAddDish;
	@FXML
	private Button btnDeleteDish;
	@FXML
	private Button btnUpdatePrice;
	@FXML
	private Button btnBack;
	@FXML
	private Label restaurantLabel;

	/**
	 * Observable list for displaying dishes in the table.
	 */
	private ObservableList<DishDisplay> dishList = FXCollections.observableArrayList();

	/**
	 * Observable list for displaying categories in the choice box.
	 */
	private ObservableList<String> categories = FXCollections.observableArrayList("Salad", "Main Course", "Dessert",
			"Drink");

	/**
	 * Observable list for displaying categories in the choice box.
	 */
	private ObservableList<String> options = FXCollections.observableArrayList("None", "INGREDIENT", "COOKING LEVEL");

	/**
	 * The certified employee currently logged in.
	 */
	private static User certifiedEmployee;

	/**
	 * Singleton instance of the UpdateMenuController.
	 */
	private static UpdateMenuController instance;

	/**
	 * The restaurant number associated with the certified employee.
	 */
	private int restaurantNumber;

	/**
	 * The name of the restaurant associated with the certified employee.
	 */
	private String restaurantName;

	/**
	 * Flag to indicate if the dish already exists in the menu.
	 */
	private boolean dishExists;

	/**
	 * The category ID of the selected category.
	 */
	private int categoryId;

	/**
	 * Formatter for date and time.
	 */
	DateTimeFormatter formatter;

	/**
	 * Formatted string representation of the current date and time.
	 */
	String formattedDateTime;

	/**
	 * Inner class representing a dish display object. Combines Dish, DishOption,
	 * Price, and Category information for display purposes.
	 */
	public static class DishDisplay {
		private DishUpdate dish;
		private DishOption option;
		private Price price;
		private Category category;

		public DishDisplay(DishUpdate dish, DishOption option, Price price, Category category) {
			this.dish = dish;
			this.option = option;
			this.price = price;
			this.category = category;
		}

		public String getDishName() {
			return dish != null ? dish.getDishName() : "";
		}

		public String getOptionValue() {
			return option != null ? option.getOptionValue() : "None";
		}

		public String getSize() {
			return price != null ? price.getSize() : "None";
		}

		public int getPrice() {
			return price != null ? price.getPrice() : 0;
		}

		public String getCategoryName() {
			return category != null ? category.getName() : "";
		}

		public DishUpdate getDish() {
			return dish;
		}

		public DishOption getOption() {
			return option;
		}

		public Price getPriceObj() {
			return price;
		}

		public Category getCategory() {
			return category;
		}
	}

	/**
	 * Sets the certified employee for the controller.
	 *
	 * @param employee The certified employee user
	 */
	public static void setCertifiedEmployee(User employee) {
		certifiedEmployee = employee;
	}

	/**
	 * Gets the singleton instance of the UpdateMenuController.
	 *
	 * @return The UpdateMenuController instance
	 */
	public static UpdateMenuController getInstance() {
		if (instance == null) {
			instance = new UpdateMenuController();
		}
		return instance;
	}

	/**
	 * Starts the UpdateMenu scene.
	 *
	 * @param primaryStage The primary stage for the scene
	 * @throws Exception if there's an error loading the FXML
	 */
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/resturant/UpdateMenu.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root);
		primaryStage.setTitle("UpdateMenuScreen");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	/**
	 * Initializes the controller. Sets up the table columns, choice boxes, and
	 * loads the initial dish data.
	 *
	 * @throws IOException if there's an error loading the dishes
	 */
	public void initialize() throws IOException {
		instance = this;
		if (nameColumn != null)
			nameColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getDishName()));
		if (optionColumn != null)
			optionColumn
					.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getOptionValue()));
		if (sizeColumn != null)
			sizeColumn.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getSize()));
		if (priceColumn != null)
			priceColumn.setCellValueFactory(
					cellData -> new SimpleIntegerProperty(cellData.getValue().getPrice()).asObject());
		if (categoryColumn != null)
			categoryColumn
					.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategoryName()));
		if (txtTable != null)
			txtTable.setItems(dishList);

		categoryBox.setItems(categories);
		OptionBox.setItems(options);
		OptionBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
			handleOptionSelection(newVal);
		});

		try {
			loadDishes();
		} catch (IOException e) {
			System.err.println("Error loading dishes: " + e.getMessage());
			e.printStackTrace();
		}
		requestRestaurantName(certifiedEmployee);
		requestRestaurantNumber(certifiedEmployee);
	}

	/**
	 * Loads the dishes for the current restaurant. Sends a message to the server to
	 * retrieve the dish data.
	 *
	 * @throws IOException if there's an error communicating with the server
	 */
	public void loadDishes() throws IOException {
		if (certifiedEmployee == null) {
			System.err.println("certifiedEmployee is null in loadDishes()");
			return;
		}
		Message getDishesMessage = new Message(certifiedEmployee, Commands.GetRestaurantDishes);
		ClientController.client.handleMessageFromClientControllers(getDishesMessage);
	}

	/**
	 * Updates the database with the entry time of a certified employee. This method
	 * is called when a certified employee begins updating the menu.
	 *
	 * @param localTime A string representation of the local time when the update
	 *                  began, formatted as "yyyy-MM-dd HH:mm:ss".
	 */
	public void updateEntry(String localTime) {
		Message updateBegin = new Message(new Object[] { restaurantNumber, localTime }, Commands.updateBegin);
		ClientController.client.handleMessageFromClientControllers(updateBegin);
	}

	/**
	 * Updates the database with the exit time of a certified employee. This method
	 * is called when a certified employee finishes updating the menu.
	 *
	 * @param localTime A string representation of the local time when the update
	 *                  ended, formatted as "yyyy-MM-dd HH:mm:ss".
	 */
	public void updateExit(String localTime) {
		Message updateEnd = new Message(new Object[] { restaurantNumber, localTime }, Commands.EndUpdate);
		ClientController.client.handleMessageFromClientControllers(updateEnd);
	}

	/**
	 * Sets the restaurant number and name for the current session. This method is
	 * used to initialize or update the restaurant information in the controller.
	 *
	 * @param restaurantNumber The unique identifier of the restaurant
	 * @param restaurantName   The name of the restaurant
	 */
	public void setRestaurantNumberAndName(int restaurantNumber, String restaurantName) {
		this.restaurantNumber = restaurantNumber;
		this.restaurantName = restaurantName;
	}

	/**
	 * Requests the restaurant number associated with the certified employee. Sends
	 * a message to the server to retrieve the restaurant number.
	 *
	 * @param user The certified employee user
	 * @throws IOException if there's an error communicating with the server
	 */
	public void requestRestaurantNumber(User user) throws IOException {
		Message RestaurantNum = new Message(certifiedEmployee, Commands.GetRestaurantNum);
		ClientController.client.handleMessageFromClientControllers(RestaurantNum);
	}

	/**
	 * Requests the restaurant name associated with the certified employee. Sends a
	 * message to the server to retrieve the restaurant name.
	 *
	 * @param user The certified employee user
	 * @throws IOException if there's an error communicating with the server
	 */
	public void requestRestaurantName(User user) throws IOException {
		Message RestaurantNum = new Message(certifiedEmployee, Commands.GetRestaurantName);
		ClientController.client.handleMessageFromClientControllers(RestaurantNum);
	}

	/**
	 * Checks if a dish with the given name and size already exists in the
	 * restaurant's menu. Sends a message to the server to perform the check.
	 *
	 * @param dishName The name of the dish to check
	 * @param size     The size of the dish to check
	 * @throws IOException if there's an error communicating with the server
	 */
	public void isDishAlreadyExists(String dishName, String size) throws IOException {
		// Send a message to the server to check if a dish with the same name and size
		// already exists
		Message checkDishMessage = new Message(new Object[] { dishName, size }, Commands.CheckDishExists);
		ClientController.client.handleMessageFromClientControllers(checkDishMessage);
	}

	/**
	 * Handles the action of adding a new dish to the menu. Validates input, checks
	 * for existing dishes, and sends the new dish data to the server.
	 *
	 * @param event The action event triggered by the add button
	 * @throws IOException if there's an error communicating with the server
	 */
	@FXML
	void addDish(ActionEvent event) throws IOException {
		String dishName = txtDishName.getText();
		String size = txtDishSize.getText();
		String priceStr = txtDishPrice.getText();
		categoryId = getCategoryId(); // Get the category ID based on the user's selection
		OptionType selectedOption = getSelectedOptionType(); // Get the selected option type
		String optionValue = txtDishOption.getText(); // Get the option value

		if (dishName.isEmpty() || size.isEmpty() || priceStr.isEmpty() || categoryId == 0) {
			showAlert("Dish name, size, price, and category are required");
			return;
		}

		if (selectedOption != null && optionValue.isEmpty()) {
			showAlert("Please add dish option");
			return;
		}

		boolean isDishNameValid = dishName.matches(".*[a-zA-Z].*"); // Ensures there is at least one letter
		boolean isSizeValid = size.matches("[a-zA-Z\\s]+");

		if (!isDishNameValid && !isSizeValid) {
			showAlert("Dish Name and Dish Size must be a String");
			return;
		} else if (!isDishNameValid) {
			showAlert("Dish Name must be a String");
			return;
		} else if (!isSizeValid) {
			showAlert("Dish Size must be a String");
			return;
		}

		if (restaurantNumber <= 0) {
			System.err.println("Restaurant number not set. Cannot add dish.");
			return;
		}

		// Check if a dish with the same name and size already exists
		isDishAlreadyExists(dishName, size); // The addDish logic will be triggered from handleServerResponse if
												// dishExists is false.
	}

	/**
	 * Handles the action of deleting a dish from the menu. Sends a delete request
	 * to the server for the selected dish.
	 *
	 * @param event The action event triggered by the delete button
	 * @throws IOException if there's an error communicating with the server
	 */
	@FXML
	void deleteDish(ActionEvent event) throws IOException {
		DishDisplay selectedDish = txtTable.getSelectionModel().getSelectedItem();
		if (selectedDish == null) {
			showAlert("Please select a dish to delete");
			return;
		}

		Message deleteDishMessage = new Message(selectedDish.getDish(), Commands.DeleteDish);
		ClientController.client.handleMessageFromClientControllers(deleteDishMessage);

		// print to verify selected dish
		System.out.println("Selected dish to delete: " + selectedDish.getDishName());
	}

	/**
	 * Handles the action of updating the price of a selected dish. Validates the
	 * selected dish and new price input, then sends an update request to the
	 * server.
	 *
	 * @param event The action event triggered by the update price button
	 * @throws IOException if there's an error communicating with the server
	 */
	@FXML
	void updateDishPrice(ActionEvent event) throws IOException {
		DishDisplay selectedDish = txtTable.getSelectionModel().getSelectedItem();
		if (selectedDish == null) {
			showAlert("Please select a dish to update");
			return;
		}

		String newPriceStr = txtDishPrice.getText();
		if (newPriceStr.isEmpty()) {
			showAlert("Please enter a new price");
			return;
		}

		try {
			int newPrice = Integer.parseInt(newPriceStr);
			Price updatedPrice = new Price(selectedDish.getDish().getDishID(), selectedDish.getSize(), newPrice);
			Message updateDishMessage = new Message(updatedPrice, Commands.UpdateDishPrice);
			ClientController.client.handleMessageFromClientControllers(updateDishMessage);
		} catch (NumberFormatException e) {
			showAlert("Price must be a number");
		}
	}

	/**
	 * Handles the action of clicking the 'Back' button. This method performs the
	 * following actions: 1. Gets the current local time and formats it. 2. Calls
	 * updateExit to record the departure time of the certified employee. 3. Closes
	 * the current window. 4. Opens a new CertifiedEmployeeController screen.
	 *
	 * @param event The ActionEvent triggered by clicking the 'Back' button.
	 * @throws Exception If there's an error while opening the new screen.
	 */
	@FXML
	void getBtnBack(ActionEvent event) throws Exception {
		// Get current local time and format it
		LocalDateTime localTime = LocalDateTime.now();
		formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		formattedDateTime = localTime.format(formatter);
		updateExit(formattedDateTime); // calls updateExit to update the Update the departure time of a certified
										// employee

		((Node) event.getSource()).getScene().getWindow().hide();
		CertifiedEmployeeController newScreen = new CertifiedEmployeeController();
		newScreen.start(new Stage());

	}

	/**
	 * Handles responses received from the server. Processes various types of server
	 * responses including dish data, operation confirmations, restaurant
	 * information, and certified employee enters the update menu screen.
	 *
	 * @param message The message received from the server
	 */
	public void handleServerResponse(Message message) {
		System.out.println("Received server response: " + message.getCmd());
		switch (message.getCmd()) {
		case GetRestaurantDishes:
			List<Object[]> dishData = (List<Object[]>) message.getObj();
			System.out.println("Received " + dishData.size() + " dishes from server");
			dishList.clear();

			for (Object[] data : dishData) {
				DishUpdate dish = (DishUpdate) data[0];
				DishOption option = (DishOption) data[1];
				Price price = (Price) data[2];
				Category category = (Category) data[3];

				dishList.add(new DishDisplay(dish, option, price, category));
			}

			txtTable.setItems(dishList);
			System.out.println("Updated TableView with " + dishList.size() + " dishes");
			break;

		case AddDish:
		case DeleteDish:
		case UpdateDishPrice:
			String response = (String) message.getObj();
			showAlert(response);
			try {
				loadDishes();
			} catch (IOException e) {
				System.err.println("Error reloading dishes: " + e.getMessage());
				e.printStackTrace();
			}
			break;

		case GetRestaurantNum:
			restaurantNumber = (Integer) message.getObj();
			System.out.println("Restaurant Number: " + restaurantNumber);

			// Get current local time and format it
			LocalDateTime localTime = LocalDateTime.now();
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			String formattedDateTime = localTime.format(formatter);
			updateEntry(formattedDateTime); // calls updateEntry to update the entry time of certified employee
			break;

		case GetRestaurantName:
			restaurantName = (String) message.getObj();
			System.out.println("Restaurant Name: " + restaurantName);
			if (restaurantLabel != null) {
				restaurantLabel.setText(restaurantName + " Menu");
			}
			break;

		case CheckDishExists:
			dishExists = (Boolean) message.getObj();
			if (dishExists) {
				showAlert("There's already a Dish with the same name and size.");
			} else { // Proceed to add the dish only if it does not exist
				try {
					categoryId = getCategoryId();
					DishUpdate newDish = new DishUpdate(0, restaurantNumber, categoryId, txtDishName.getText());
					Price newPrice = new Price(0, txtDishSize.getText(), Integer.parseInt(txtDishPrice.getText()));
					DishOption newOption = null;
					OptionType selectedOption = getSelectedOptionType();
					if (selectedOption != null) {
						newOption = new DishOption(0, selectedOption, txtDishOption.getText());
					}
					Message addDishMessage = new Message(new Object[] { newDish, newPrice, newOption },
							Commands.AddDish);
					ClientController.client.handleMessageFromClientControllers(addDishMessage);
				} catch (NumberFormatException e) {
					showAlert("Price must be a number");
				}
			}
			break;
		default:
			System.out.println("Unhandled command: " + message.getCmd());
			break;
		}
	}

	/**
	 * Handles the selection of a dish option type. Controls the visibility of the
	 * dish option input field based on the selected option.
	 *
	 * @param selectedOption The option selected by the user ("None", "INGREDIENT",
	 *                       or "COOKING_LEVEL")
	 */
	private void handleOptionSelection(String selectedOption) {
		if (selectedOption == null) {
			return;
		}
		switch (selectedOption) {
		case "None":
			txtDishOption.setVisible(false);
			break;
		case "INGREDIENT":
		case "COOKING_LEVEL":
			txtDishOption.setVisible(true);
			break;
		default:
			break;
		}
	}

	/**
	 * Retrieves the category ID based on the selected category name. Maps category
	 * names to their corresponding numeric IDs.
	 *
	 * @return The numeric ID of the selected category, or 0 if no category is
	 *         selected
	 */
	private int getCategoryId() {
		String selectedCategory = categoryBox.getSelectionModel().getSelectedItem();
		if (selectedCategory == null) {
			return 0; // Return 0 to indicate no category selected
		}
		switch (selectedCategory) {
		case "Salad":
			return 1;
		case "Main Course":
			return 2;
		case "Dessert":
			return 3;
		case "Drink":
			return 4;
		default:
			return 0; // Return 0 for any unexpected category
		}
	}

	/**
	 * Retrieves the OptionType enum based on the selected option string. Maps
	 * option strings to their corresponding OptionType enum values.
	 *
	 * @return The OptionType enum value corresponding to the selected option, or
	 *         null if no option is selected
	 */
	private OptionType getSelectedOptionType() {
		String selectedOption = OptionBox.getSelectionModel().getSelectedItem();
		if (selectedOption == null) {
			return null; // Return null to indicate no option selected
		}
		switch (selectedOption) {
		case "None":
			return null;
		case "INGREDIENT":
			return OptionType.INGREDIENT;
		case "COOKING LEVEL":
			return OptionType.COOKING_LEVEL;
		default:
			return null; // Return null for any unexpected option
		}
	}

	/**
	 * Displays an alert dialog with the given message. Used for showing information
	 * and error messages to the user.
	 *
	 * @param message The message to be displayed in the alert dialog
	 */
	private void showAlert(String message) {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Information");
		alert.setHeaderText(null);
		alert.setContentText(message);
		alert.showAndWait();
	}

}
