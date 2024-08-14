package customer;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import client.Client;
import client.ClientController;
import entites.Customer;
import entites.Dish;
import entites.Message;
import entites.User;
import enums.Commands;

import entites.OrderItem;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * The `NewOrderController` class handles the functionality of the new order
 * creation screen in the customer module. It manages the selection of dishes,
 * customization options, and the ordering process, including validation and
 * submission of the order. The class interacts with various UI elements, such
 * as tables, buttons, and text fields, and communicates with the server to
 * fetch and update data.
 * 
 * This class also handles validation of delivery details, updating of the order
 * total, and confirming delivery information. It supports multiple delivery
 * types, including pickup, regular delivery, shared delivery, and robot
 * delivery, each with its own set of fields and validation logic.
 * 
 * The class extends the JavaFX application framework and uses several helper
 * methods to initialize and manage the UI components and business logic.
 * 
 * @author yosra
 */
public class NewOrderController {

	@FXML
	private TableView<Dish> dishTableViewSalad;
	@FXML
	private TableColumn<Dish, String> dishNameColumnSalad;
	@FXML
	private TableColumn<Dish, Integer> dishPriceColumnSalad;
	@FXML
	private TableColumn<Dish, String> specificationsColumnSalad;
	@FXML
	private TableColumn<Dish, Integer> quantityColumnSalad;

	@FXML
	private TableView<Dish> dishTableViewMainCourse;
	@FXML
	private TableColumn<Dish, String> dishNameColumnMain;
	@FXML
	private TableColumn<Dish, Integer> dishPriceColumnMain;
	@FXML
	private TableColumn<Dish, String> specificationsColumnMain;
	@FXML
	private TableColumn<Dish, Integer> quantityColumnMain;

	@FXML
	private TableView<Dish> dishTableViewDrink;
	@FXML
	private TableColumn<Dish, String> dishNameColumnDrink;
	@FXML
	private TableColumn<Dish, Integer> dishPriceColumnDrink;
	@FXML
	private TableColumn<Dish, String> specificationsColumnDrink;
	@FXML
	private TableColumn<Dish, Integer> quantityColumnDrink;

	@FXML
	private TableView<Dish> dishTableViewDesert;
	@FXML
	private TableColumn<Dish, String> dishNameColumnDesert;
	@FXML
	private TableColumn<Dish, Integer> dishPriceColumnDesert;
	@FXML
	private TableColumn<Dish, String> specificationsColumnDesert;
	@FXML
	private TableColumn<Dish, Integer> quantityColumnDesert;

	@FXML
	private Label SaladLbl;
	@FXML
	private Label MainCourseLbl;
	@FXML
	private Label DesertLbl;
	@FXML
	private Label DrinkLbl;

	@FXML
	private TableView<OrderItem> orderTableView;
	@FXML
	private TableColumn<Dish, String> orderDishTypeColumn;
	@FXML
	private TableColumn<Dish, String> orderDishNameColumn;
	@FXML
	private TableColumn<Dish, Integer> orderDishPriceColumn;
	@FXML
	private TableColumn<Dish, String> orderSpecificationsColumn;
	@FXML
	private TableColumn<Dish, Integer> orderQuantityColumn;
	@FXML
	private Text orderPriceText;
	@FXML
	private Text deliveryFeeText;
	@FXML
	private Text removeItemText;
	@FXML
	private ComboBox<String> restaurantComboBox;
	@FXML
	private Button btnBack;
	@FXML
	private Button btnAddOrder;
	@FXML
	private Button btnFinish;
	@FXML
	private Button btnRemoveItem;
	@FXML
	private Text confirmDeliveryText;
	@FXML
	private Text totalPriceText;

	@FXML
	private Text addressErrorText;
	@FXML
	private Text phoneErrorText;

	@FXML
	private ComboBox<String> deliveryTypeComboBox;
	@FXML
	private ComboBox<String> deliveryTimePicker;
	@FXML
	private TextField addressField;
	@FXML
	private TextField companyNameField;
	@FXML
	private TextField userNameField;
	@FXML
	private TextField phoneNumberField;
	@FXML
	private DatePicker deliveryDatePicker;
	@FXML
	private Button confirmDeliveryButton;
	@FXML
	private ComboBox<String> deliveryHourPicker;
	@FXML
	private ComboBox<String> deliveryMinutePicker;
	@FXML
	private TextField deliveryParticipantsField;

	@FXML
	private Text deliveryParticipantsErrorText;
	@FXML
	private Text errorText;
	@FXML
	private Text dateErrorText;
	@FXML
	private Text timeErrorText;
	@FXML
	private Text companyNameErrorText;
	@FXML
	private Text userNameErrorText;
	@FXML
	private Text finishErrorText;

	/**
	 * Flag to indicate whether to call the inBetween method during order
	 * processing.
	 */
	private boolean callBetween = false;

	/**
	 * The delivery charge applied to the order based on the delivery type.
	 */
	private double deliveryCharge = 0;

	/**
	 * The discount percentage applied to the order, particularly for early orders.
	 */
	private double discountPercentage = 0;

	/**
	 * The timestamp indicating when the restaurant's menu was last updated.
	 */
	private java.sql.Timestamp beginUpdate;

	/**
	 * The timestamp indicating when the restaurant's menu update ended.
	 */
	private java.sql.Timestamp endUpdate;

	/**
	 * Flag indicating whether the beginUpdate or endUpdate timestamps are null.
	 */
	private boolean timeIsNull = false;

	/**
	 * Flag to check the time before confirming the order to ensure that the menu
	 * hasn't changed.
	 */
	private boolean checkTimeBeforeConfirm = false;

	/**
	 * Flag to indicate if the company name provided by the user is valid.
	 */
	private boolean isCompanyNameValid = false;

	/**
	 * Flag to indicate if the user name provided by the user is valid.
	 */
	private boolean isUserNameValid = false;

	/**
	 * Flag to indicate if the number of participants for shared delivery is valid.
	 */
	private boolean isDeliveryParticipantsValid = false;

	/**
	 * Flag to indicate if the delivery address provided by the user is valid.
	 */
	private boolean isAddressValid = false;

	/**
	 * Flag to indicate if the phone number provided by the user is valid.
	 */
	private boolean isPhoneValid = false;

	/**
	 * Flag to track whether the order has been changed.
	 */
	private boolean orderChanged = false;

	/**
	 * The currently selected restaurant for the order.
	 */
	private String currentRestaurant = null;

	/**
	 * Flag to indicate whether to display error messages for invalid fields.
	 */
	private boolean showErrorMessages = false;

	/**
	 * Flag to indicate whether the delivery details have been confirmed by the
	 * user.
	 */
	private boolean isDeliveryConfirmed = false;

	/**
	 * The unique number identifying the selected restaurant.
	 */
	private int restaurantNumber;

	/**
	 * ObservableList containing the list of restaurants available for selection.
	 */
	private ObservableList<String> restaurantList = FXCollections.observableArrayList();

	/**
	 * ObservableList containing the list of dishes categorized as "salad."
	 */
	private ObservableList<Dish> dishes1 = FXCollections.observableArrayList();

	/**
	 * ObservableList containing the list of dishes categorized as "main course."
	 */
	private ObservableList<Dish> dishes2 = FXCollections.observableArrayList();

	/**
	 * ObservableList containing the list of dishes categorized as "dessert."
	 */
	private ObservableList<Dish> dishes3 = FXCollections.observableArrayList();

	/**
	 * ObservableList containing the list of dishes categorized as "drink."
	 */
	private ObservableList<Dish> dishes4 = FXCollections.observableArrayList();

	/**
	 * ObservableList containing the list of dishes added to the current order.
	 */
	private ObservableList<Dish> orderDishes = FXCollections.observableArrayList();

	/**
	 * Map to track the quantities of salad dishes in the current order.
	 */
	private Map<String, Integer> orderQuantitiesSalad = new HashMap<>();

	/**
	 * Map to track the quantities of main course dishes in the current order.
	 */
	private Map<String, Integer> orderQuantitiesMain = new HashMap<>();

	/**
	 * Map to track the quantities of dessert dishes in the current order.
	 */
	private Map<String, Integer> orderQuantitiesDesert = new HashMap<>();

	/**
	 * Map to track the quantities of drink dishes in the current order.
	 */
	private Map<String, Integer> orderQuantitiesDrink = new HashMap<>();

	/**
	 * ObservableList containing the list of items in the current order.
	 */
	private ObservableList<OrderItem> orderItems = FXCollections.observableArrayList();

	/**
	 * The static User object representing the current customer.
	 */
	private static User customer;

	/**
	 * The Customer object representing the current customer with additional
	 * details.
	 */
	private Customer currentCustomer;

	/**
	 * Temporary timestamp to store the beginUpdate value during order processing.
	 */
	private java.sql.Timestamp tempBeginUpdate;

	/**
	 * Temporary timestamp to store the endUpdate value during order processing.
	 */
	private java.sql.Timestamp tempEndUpdate;

	/**
	 * The ActionEvent associated with the current order submission.
	 */
	private ActionEvent event = null;

	/**
	 * Flag indicating whether the current order is a delivery or a pickup.
	 */
	private boolean isItDelivery = false;

	/**
	 * Flag indicating whether the current order qualifies as an early order.
	 */
	private int isItEarlyOrder;

	/**
	 * Initializes the `NewOrderController` class. This method is automatically
	 * called after the FXML file has been loaded. It sets up the initial state of
	 * the UI components, configures listeners for user interactions, and requests
	 * initial data such as restaurant names.
	 * 
	 * The method performs the following actions:
	 * <ul>
	 * <li>Initializes observable lists for dishes and orders.</li>
	 * <li>Disables certain UI components until they are needed.</li>
	 * <li>Sets up listeners for restaurant selection, item removal, and delivery
	 * type changes.</li>
	 * <li>Requests the list of available restaurants from the server.</li>
	 * </ul>
	 */
	@FXML
	private void initialize() {
		Client.newOrderController = this;
		dishes1 = FXCollections.observableArrayList();
		dishes2 = FXCollections.observableArrayList();
		dishes3 = FXCollections.observableArrayList();
		dishes4 = FXCollections.observableArrayList();
		orderDishes = FXCollections.observableArrayList();
		orderQuantitiesSalad = new HashMap<>();
		orderQuantitiesMain = new HashMap<>();
		orderQuantitiesDesert = new HashMap<>();
		orderQuantitiesDrink = new HashMap<>();
		restaurantList = FXCollections.observableArrayList();
		confirmDeliveryButton.setDisable(true);
		deliveryHourPicker.setDisable(true);
		deliveryMinutePicker.setDisable(true);

		errorText = new Text();
		errorText.setFill(Color.RED);
		errorText.setStyle("-fx-font-size: 12px;");

		requestRestaurantNames();

		restaurantComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null && !newValue.equals("Choose a restaurant")) {
				requestRestaurantMenu(newValue);
			}
		});

		btnRemoveItem.setOnAction(this::handleRemoveItem);
		btnRemoveItem.setDisable(true);
		removeItemText.setVisible(false);

		orderTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
			btnRemoveItem.setDisable(newSelection == null);
			if (newSelection == null) {
				removeItemText.setVisible(false);
			}
		});

		deliveryTypeComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
			if (newVal != null) {
				updateDeliveryFields(newVal);
				confirmDeliveryButton.setDisable(false);
			}
		});

	}

	/**
	 * Sets the customer for the current order session. This method is called to
	 * initialize the customer details and fetches additional customer information
	 * from the server based on the user's ID.
	 *
	 * @param user The User object representing the customer.
	 */
	public static void setCustomer(User user) {

		customer = user;
		int userID = customer.getId();
		Commands command = Commands.getCustomerDetails;
		Message message = new Message(userID, command);
		ClientController.client.handleMessageFromClientControllers(message);

	}

	/**
	 * Sets the detailed information of the customer. This method initializes the
	 * `currentCustomer` object with the provided customer details if it has not
	 * been set already, then proceeds to complete the UI setup.
	 *
	 * @param customerDetails The Customer object containing detailed customer
	 *                        information.
	 */
	public void setCustomerDetails(Customer customerDetails) {
		if (this.currentCustomer == null) {
			this.currentCustomer = new Customer(customerDetails.getCustomerNumber(), customerDetails.getId(),
					customerDetails.getCredit(), customerDetails.isBusiness(), customerDetails.getStatus());
		}
		System.out.println("Current Customer: " + currentCustomer);
		Platform.runLater(this::completeSetup);
	}

	/**
	 * Completes the setup of the user interface after customer details have been
	 * received. This method configures the UI elements such as combo boxes and
	 * table views, adds necessary listeners, and updates the state of buttons based
	 * on the current order status.
	 */
	private void completeSetup() {
		setupUI();
		setupDishTableView();
		addListeners();
		updateButtonStates();
		setupOrderTableView();
	}

	/**
	 * Configures the UI components that depend on the customer details. This
	 * includes setting up combo boxes and other UI elements that require
	 * customer-specific data.
	 */
	private void setupUI() {
		setupComboBoxes();
	}

	/**
	 * Sends a request to retrieve the list of restaurant names. This method sends a
	 * message to the server to fetch the available restaurant names and populates
	 * the restaurant combo box once the data is received.
	 */
	public void requestRestaurantNames() {
		Commands command = Commands.getRestaurantList;
		Message message = new Message(null, command);
		ClientController.client.handleMessageFromClientControllers(message);

	}

	/**
	 * Populates the restaurant combo box with the retrieved restaurant names. This
	 * method updates the combo box with the list of restaurants provided by the
	 * server.
	 *
	 * @param restaurantNames An ArrayList of restaurant names to populate the combo
	 *                        box.
	 */
	public void setRestaurantNames(ArrayList<String> restaurantNames) {
		Platform.runLater(() -> {
			restaurantComboBox.setValue("Choose a restaurant");
			this.restaurantList = FXCollections.observableArrayList(restaurantNames);
			restaurantComboBox.setItems(this.restaurantList);
		});
	}

	/**
	 * Sends a request to retrieve the menu for a selected restaurant. This method
	 * sends a message to the server to fetch the menu of the specified restaurant.
	 *
	 * @param restaurantName The name of the restaurant for which to fetch the menu.
	 */
	private void requestRestaurantMenu(String restaurantName) {
		Message message = new Message(restaurantName, Commands.getRestaurantMenu);
		ClientController.client.handleMessageFromClientControllers(message);
	}

	/**
	 * Populates the restaurant's menu and updates the corresponding dish lists and
	 * TableViews. This method processes the provided menu data, categorizes dishes
	 * by type, and handles specific settings like size options and default
	 * specifications. If the Finish button was pressed, it also checks for updates
	 * and continues processing the order.
	 *
	 * @param menu An ArrayList of Map objects, each representing a dish with its
	 *             properties and options.
	 * @throws Exception If an error occurs while processing the menu data.
	 */
	public void setRestaurantMenu(ArrayList<Map<String, Object>> menu) throws Exception {
		dishes1.clear();
		orderQuantitiesSalad.clear();
		dishes2.clear();
		orderQuantitiesMain.clear();
		dishes3.clear();
		orderQuantitiesDesert.clear();
		dishes4.clear();
		orderQuantitiesDrink.clear();

		Map<String, Dish> dishMap = new HashMap<>();

		// The last item in the menu list contains the restaurant info
		Map<String, Object> restaurantInfo = menu.remove(menu.size() - 1);

		// Handle potential null values for BeginUpdate and EndUpdate
		Object beginUpdateObj = restaurantInfo.get("BeginUpdate");
		Object endUpdateObj = restaurantInfo.get("EndUpdate");

		if (beginUpdateObj == null || endUpdateObj == null) {
			this.timeIsNull = true;
			this.beginUpdate = null;
			this.endUpdate = null;
		} else {
			this.timeIsNull = false;
			this.beginUpdate = (java.sql.Timestamp) beginUpdateObj;
			this.endUpdate = (java.sql.Timestamp) endUpdateObj;
		}

		// Handle RestaurantNumber
		Object restaurantNumberObj = restaurantInfo.get("RestaurantNumber");
		if (restaurantNumberObj != null) {
			this.restaurantNumber = (Integer) restaurantNumberObj;
		} else {
			this.restaurantNumber = -1;
		}

		if (!checkTimeBeforeConfirm) {

			for (Map<String, Object> dishData : menu) {
				String dishID = (String) dishData.get("dishID");
				String dishType = (String) dishData.get("dishType");
				String dishName = (String) dishData.get("dishName");
				int dishPrice = ((Number) dishData.get("dishPrice")).intValue();
				Map<String, List<String>> dishOptions = (Map<String, List<String>>) dishData.get("dishOptions");
				Map<String, Integer> dishPrices = (Map<String, Integer>) dishData.get("dishPrices");

				// Create or get existing Dish object
				Dish dish = dishMap.computeIfAbsent(dishID, k -> {
					ObservableList<String> specifications = FXCollections.observableArrayList();
					return new Dish(dishID, dishName, dishType, dishPrice, specifications);
				});

				// Set size prices after dish is created
				if (dishPrices != null && !dishPrices.isEmpty()) {
					dish.setSizePrices(new HashMap<>(dishPrices));
				} else {
					dish.setSizePrices(new HashMap<>());
				}

				// Process and add specifications
				boolean hasSpecifications = false;
				boolean hasRemoveOption = false;
				for (Map.Entry<String, List<String>> entry : dishOptions.entrySet()) {
					String optionType = entry.getKey();
					List<String> optionValues = entry.getValue();

					for (String value : optionValues) {
						String specificationText = optionType + ": " + value;
						if (!dish.getSpecifications().contains(specificationText)) {
							dish.getSpecifications().add(specificationText);
							hasSpecifications = true;
							if (optionType.equals("Remove")) {
								hasRemoveOption = true;
							}
						}
					}
				}

				// Add "None" if there are no specifications or if there's a "Remove" option
				if (!hasSpecifications || hasRemoveOption) {
					dish.getSpecifications().add(0, "None");
				}

				// Set default specification
				if (hasRemoveOption) {
					dish.setSelectedSpecification("None");
				} else if (!dish.getSpecifications().isEmpty()) {
					dish.setSelectedSpecification(dish.getSpecifications().get(0));
				}

				// Update price if the default specification is a size
				if (dish.getSelectedSpecification().startsWith("Size:")) {
					String selectedSize = dish.getSelectedSpecification().substring(6).trim().toLowerCase();
					Integer newPrice = dish.getSizePrices().get(selectedSize);
					if (newPrice != null) {
						dish.setDishPrice(newPrice);
					}
				}
			}

			// Categorize dishes into appropriate lists
			for (Dish dish : dishMap.values()) {
				switch (dish.getCategoryName()) {
				case "salad":
					dishes1.add(dish);
					orderQuantitiesSalad.put(dish.getDishID(), 0);
					break;
				case "main course":
					dishes2.add(dish);
					orderQuantitiesMain.put(dish.getDishID(), 0);
					break;
				case "dessert":
					dishes3.add(dish);
					orderQuantitiesDesert.put(dish.getDishID(), 0);
					break;
				case "drink":
					dishes4.add(dish);
					orderQuantitiesDrink.put(dish.getDishID(), 0);
					break;
				default:
					break;
				}
			}

			// Update TableViews
			dishTableViewSalad.setItems(dishes1);
			dishTableViewSalad.refresh();
			dishTableViewMainCourse.setItems(dishes2);
			dishTableViewMainCourse.refresh();
			dishTableViewDesert.setItems(dishes3);
			dishTableViewDesert.refresh();
			dishTableViewDrink.setItems(dishes4);
			dishTableViewDrink.refresh();

			orderChanged = false;
			updateButtonStates();
		}

		// Call inBetween if Finish button was pressed
		if (callBetween) {
			inBetween();
		}
	}

	/**
	 * Initializes and sets up the ComboBoxes and pickers in the UI. This method
	 * configures the restaurant selection ComboBox, populates the delivery type
	 * options based on whether the current customer is a business, and sets up the
	 * hour and minute pickers for delivery time selection.
	 */
	private void setupComboBoxes() {
		restaurantComboBox.setValue("Choose a restaurant");
		restaurantComboBox.setItems(restaurantList);

		if (currentCustomer.isBusiness() == true) {
			deliveryTypeComboBox.getItems().addAll("Pickup", "Regular Delivery", "Shared Delivery", "Robot Delivery");
		} else {
			deliveryTypeComboBox.getItems().addAll("Pickup", "Regular Delivery", "Robot Delivery");
		}

		// Setup hour and minute pickers
		for (int i = 0; i < 24; i++) {
			deliveryHourPicker.getItems().add(String.format("%02d", i));
		}
		for (int i = 0; i < 60; i += 5) {
			deliveryMinutePicker.getItems().add(String.format("%02d", i));
		}
	}

	/**
	 * Configures the TableViews that display dish information for different
	 * categories. This method sets up the columns for each TableView, including
	 * dish name and price. It also configures the specifications and quantity
	 * columns, where users can select options and specify the quantity of each
	 * dish.
	 */
	private void setupDishTableView() {
		dishNameColumnSalad.setCellValueFactory(new PropertyValueFactory<>("dishName"));
		dishPriceColumnSalad.setCellValueFactory(new PropertyValueFactory<>("dishPrice"));

		dishNameColumnMain.setCellValueFactory(new PropertyValueFactory<>("dishName"));
		dishPriceColumnMain.setCellValueFactory(new PropertyValueFactory<>("dishPrice"));

		dishNameColumnDesert.setCellValueFactory(new PropertyValueFactory<>("dishName"));
		dishPriceColumnDesert.setCellValueFactory(new PropertyValueFactory<>("dishPrice"));

		dishNameColumnDrink.setCellValueFactory(new PropertyValueFactory<>("dishName"));
		dishPriceColumnDrink.setCellValueFactory(new PropertyValueFactory<>("dishPrice"));

		setupSpecificationColumn(specificationsColumnSalad);
		setupSpecificationColumn(specificationsColumnMain);
		setupSpecificationColumn(specificationsColumnDesert);
		setupSpecificationColumn(specificationsColumnDrink);

		/**
		 * Configures the specifications column for salads. The column uses a ComboBox
		 * for selecting available specifications (e.g., size or customization).
		 */
		specificationsColumnSalad.setCellFactory(column -> {
			return new TableCell<Dish, String>() {
				private final ComboBox<String> comboBox = new ComboBox<>();

				{
					comboBox.setMaxWidth(Double.MAX_VALUE);
					comboBox.setStyle("-fx-font-size: 12px;");
				}

				@Override
				protected void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);
					if (empty) {
						setGraphic(null);
					} else {
						Dish dish = getTableView().getItems().get(getIndex());
						ObservableList<String> specs = FXCollections.observableArrayList(dish.getSpecifications());
						comboBox.setItems(specs);
						comboBox.setValue(dish.getSelectedSpecification() != null ? dish.getSelectedSpecification()
								: specs.get(0));
						comboBox.setOnAction(event -> {
							dish.setSelectedSpecification(comboBox.getValue());
							handleSpecificationChange(dish, comboBox.getValue());
						});
						setGraphic(comboBox);
					}
				}
			};
		});

		specificationsColumnMain.setCellFactory(column -> {
			return new TableCell<Dish, String>() {
				private final ComboBox<String> comboBox = new ComboBox<>();

				{
					comboBox.setMaxWidth(Double.MAX_VALUE);
					comboBox.setStyle("-fx-font-size: 12px;");
				}

				@Override
				protected void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);
					if (empty) {
						setGraphic(null);
					} else {
						Dish dish = getTableView().getItems().get(getIndex());
						ObservableList<String> specs = FXCollections.observableArrayList(dish.getSpecifications());
						comboBox.setItems(specs);
						comboBox.setValue(dish.getSelectedSpecification() != null ? dish.getSelectedSpecification()
								: specs.get(0));
						comboBox.setOnAction(event -> {
							dish.setSelectedSpecification(comboBox.getValue());
							handleSpecificationChange(dish, comboBox.getValue());
						});
						setGraphic(comboBox);
					}
				}
			};
		});

		specificationsColumnDesert.setCellFactory(column -> {
			return new TableCell<Dish, String>() {
				private final ComboBox<String> comboBox = new ComboBox<>();

				{
					comboBox.setMaxWidth(Double.MAX_VALUE);
					comboBox.setStyle("-fx-font-size: 12px;");
				}

				@Override
				protected void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);
					if (empty) {
						setGraphic(null);
					} else {
						Dish dish = getTableView().getItems().get(getIndex());
						ObservableList<String> specs = FXCollections.observableArrayList(dish.getSpecifications());
						comboBox.setItems(specs);
						comboBox.setValue(dish.getSelectedSpecification() != null ? dish.getSelectedSpecification()
								: specs.get(0));
						comboBox.setOnAction(event -> {
							dish.setSelectedSpecification(comboBox.getValue());
							handleSpecificationChange(dish, comboBox.getValue());
						});
						setGraphic(comboBox);
					}
				}
			};
		});

		specificationsColumnDrink.setCellFactory(column -> {
			return new TableCell<Dish, String>() {
				private final ComboBox<String> comboBox = new ComboBox<>();

				{
					comboBox.setMaxWidth(Double.MAX_VALUE);
					comboBox.setStyle("-fx-font-size: 12px;");
				}

				@Override
				protected void updateItem(String item, boolean empty) {
					super.updateItem(item, empty);
					if (empty) {
						setGraphic(null);
					} else {
						Dish dish = getTableView().getItems().get(getIndex());
						ObservableList<String> specs = FXCollections.observableArrayList(dish.getSpecifications());
						comboBox.setItems(specs);
						comboBox.setValue(dish.getSelectedSpecification() != null ? dish.getSelectedSpecification()
								: specs.get(0));
						comboBox.setOnAction(event -> {
							dish.setSelectedSpecification(comboBox.getValue());
							handleSpecificationChange(dish, comboBox.getValue());
						});
						setGraphic(comboBox);
					}
				}
			};
		});

		/**
		 * Configures the quantity column for salads. The column uses a Spinner for
		 * selecting the quantity of each dish.
		 */
		quantityColumnSalad.setCellFactory(column -> new TableCell<Dish, Integer>() {
			private final Spinner<Integer> spinner = new Spinner<>(0, 100, 0);

			{
				spinner.setEditable(true);
				spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
					Dish dish = getTableRow().getItem();
					if (dish != null) {
						if (newValue < 0 || newValue > 100) {
							Platform.runLater(() -> {
								spinner.getValueFactory().setValue(oldValue);
								errorText.setText("Please enter a number between 0 and 100 for Quantity");
							});
						} else {
							orderQuantitiesSalad.put(dish.getDishID(), newValue);
							errorText.setText("");
						}
					}
					orderChanged = true;
					updateButtonStates();
				});

				// Add a TextFormatter to the Spinner's TextField
				spinner.getEditor().setTextFormatter(new TextFormatter<Integer>(change -> {
					if (change.getControlNewText().matches("\\d*")) {
						return change;
					}
					return null;
				}));
			}

			@Override
			protected void updateItem(Integer item, boolean empty) {
				super.updateItem(item, empty);
				if (empty) {
					setGraphic(null);
				} else {
					Dish dish = getTableRow().getItem();
					if (dish != null) {
						spinner.getValueFactory().setValue(orderQuantitiesSalad.getOrDefault(dish.getDishID(), 0));
					}
					setGraphic(spinner);
				}
			}
		});

		quantityColumnMain.setCellFactory(column -> new TableCell<Dish, Integer>() {
			private final Spinner<Integer> spinner = new Spinner<>(0, 100, 0);

			{
				spinner.setEditable(true);
				spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
					Dish dish = getTableRow().getItem();
					if (dish != null) {
						if (newValue < 0 || newValue > 100) {
							Platform.runLater(() -> {
								spinner.getValueFactory().setValue(oldValue);
								errorText.setText("Please enter a number between 0 and 100 for Quantity");
							});
						} else {
							orderQuantitiesMain.put(dish.getDishID(), newValue);
							errorText.setText("");
						}
					}
					orderChanged = true;
					updateButtonStates();
				});

				// Add a TextFormatter to the Spinner's TextField
				spinner.getEditor().setTextFormatter(new TextFormatter<Integer>(change -> {
					if (change.getControlNewText().matches("\\d*")) {
						return change;
					}
					return null;
				}));
			}

			@Override
			protected void updateItem(Integer item, boolean empty) {
				super.updateItem(item, empty);
				if (empty) {
					setGraphic(null);
				} else {
					Dish dish = getTableRow().getItem();
					if (dish != null) {
						spinner.getValueFactory().setValue(orderQuantitiesMain.getOrDefault(dish.getDishID(), 0));
					}
					setGraphic(spinner);
				}
			}
		});

		quantityColumnDesert.setCellFactory(column -> new TableCell<Dish, Integer>() {
			private final Spinner<Integer> spinner = new Spinner<>(0, 100, 0);

			{
				spinner.setEditable(true);
				spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
					Dish dish = getTableRow().getItem();
					if (dish != null) {
						if (newValue < 0 || newValue > 100) {
							Platform.runLater(() -> {
								spinner.getValueFactory().setValue(oldValue);
								errorText.setText("Please enter a number between 0 and 100 for Quantity");
							});
						} else {
							orderQuantitiesDesert.put(dish.getDishID(), newValue);
							errorText.setText("");
						}
					}
					orderChanged = true;
					updateButtonStates();
				});

				// Add a TextFormatter to the Spinner's TextField
				spinner.getEditor().setTextFormatter(new TextFormatter<Integer>(change -> {
					if (change.getControlNewText().matches("\\d*")) {
						return change;
					}
					return null;
				}));
			}

			@Override
			protected void updateItem(Integer item, boolean empty) {
				super.updateItem(item, empty);
				if (empty) {
					setGraphic(null);
				} else {
					Dish dish = getTableRow().getItem();
					if (dish != null) {
						spinner.getValueFactory().setValue(orderQuantitiesDesert.getOrDefault(dish.getDishID(), 0));
					}
					setGraphic(spinner);
				}
			}
		});

		quantityColumnDrink.setCellFactory(column -> new TableCell<Dish, Integer>() {
			private final Spinner<Integer> spinner = new Spinner<>(0, 100, 0);

			{
				spinner.setEditable(true);
				spinner.valueProperty().addListener((obs, oldValue, newValue) -> {
					Dish dish = getTableRow().getItem();
					if (dish != null) {
						if (newValue < 0 || newValue > 100) {
							Platform.runLater(() -> {
								spinner.getValueFactory().setValue(oldValue);
								errorText.setText("Please enter a number between 0 and 100 for Quantity");
							});
						} else {
							orderQuantitiesDrink.put(dish.getDishID(), newValue);
							errorText.setText("");
						}
					}
					orderChanged = true;
					updateButtonStates();
				});

				// Add a TextFormatter to the Spinner's TextField
				spinner.getEditor().setTextFormatter(new TextFormatter<Integer>(change -> {
					if (change.getControlNewText().matches("\\d*")) {
						return change;
					}
					return null;
				}));
			}

			@Override
			protected void updateItem(Integer item, boolean empty) {
				super.updateItem(item, empty);
				if (empty) {
					setGraphic(null);
				} else {
					Dish dish = getTableRow().getItem();
					if (dish != null) {
						spinner.getValueFactory().setValue(orderQuantitiesDrink.getOrDefault(dish.getDishID(), 0));
					}
					setGraphic(spinner);
				}
			}
		});

		dishTableViewSalad.setItems(dishes1);
		dishTableViewMainCourse.setItems(dishes2);
		dishTableViewDesert.setItems(dishes3);
		dishTableViewDrink.setItems(dishes4);
	}

	/// Might need to delete if this da
	private void setupSpecificationColumn(TableColumn<Dish, String> column) {
		column.setCellFactory(col -> new TableCell<Dish, String>() {
			private final ComboBox<String> comboBox = new ComboBox<>();

			{
				comboBox.setMaxWidth(Double.MAX_VALUE);
				comboBox.setStyle("-fx-font-size: 12px;");
			}

			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				if (empty) {
					setGraphic(null);
				} else {
					Dish dish = getTableView().getItems().get(getIndex());
					ObservableList<String> specs = FXCollections.observableArrayList(dish.getSpecifications());
					comboBox.setItems(specs);
					comboBox.setValue(dish.getSelectedSpecification());
					comboBox.setOnAction(event -> {
						dish.setSelectedSpecification(comboBox.getValue());
						handleSpecificationChange(dish, comboBox.getValue());
					});
					setGraphic(comboBox);
				}
			}
		});
	}

	private void handleSpecificationChange(Dish dish, String newValue) {
		if (dish == null || newValue == null) {

			if (dish == null) {

			} else {

			}
			return;
		}

		if (newValue.startsWith("Size: ")) {
			String selectedSize = newValue.substring(6).toLowerCase();
			Map<String, Integer> sizePrices = dish.getSizePrices();
			if (sizePrices == null) {

				return;
			}
			Integer newPrice = sizePrices.get(selectedSize);
			if (newPrice != null) {
				dish.setDishPrice(newPrice);
				updateDishPrice(dish);

			} else {

			}
		}
	}

	private void updateDishPrice(Dish dish) {
		// Refresh the TableView that contains this dish
		if (dishes1.contains(dish)) {
			dishTableViewSalad.refresh();
		} else if (dishes2.contains(dish)) {
			dishTableViewMainCourse.refresh();
		} else if (dishes3.contains(dish)) {
			dishTableViewDesert.refresh();
		} else if (dishes4.contains(dish)) {
			dishTableViewDrink.refresh();
		}
		// Update total price if necessary
		updateOrderTotal();
	}

	private void setupOrderTableView() {
		orderDishTypeColumn.setCellValueFactory(new PropertyValueFactory<>("categoryName"));
		orderDishNameColumn.setCellValueFactory(new PropertyValueFactory<>("dishName"));
		orderDishPriceColumn.setCellValueFactory(new PropertyValueFactory<>("dishPrice"));
		orderSpecificationsColumn.setCellValueFactory(new PropertyValueFactory<>("selectedSpecification"));
		orderQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

		orderTableView.setItems(orderItems);
	}

	private void addListeners() {

		orderItems.addListener((ListChangeListener<OrderItem>) c -> updateButtonStates()); // Listener to update
																							// 'Confirm Order' button

		dishTableViewSalad.getItems().addListener((ListChangeListener<Dish>) c -> {
			orderChanged = true;
			updateButtonStates();
		});

		for (Dish dish : dishes1) {
			dish.selectedSpecificationProperty().addListener((obs, oldVal, newVal) -> {
				orderChanged = true;
				updateButtonStates();
			});
		}

		dishTableViewMainCourse.getItems().addListener((ListChangeListener<Dish>) c -> {
			orderChanged = true;
			updateButtonStates();
		});

		for (Dish dish : dishes2) {
			dish.selectedSpecificationProperty().addListener((obs, oldVal, newVal) -> {
				orderChanged = true;
				updateButtonStates();
			});
		}

		dishTableViewDesert.getItems().addListener((ListChangeListener<Dish>) c -> {
			orderChanged = true;
			updateButtonStates();
		});

		for (Dish dish : dishes3) {
			dish.selectedSpecificationProperty().addListener((obs, oldVal, newVal) -> {
				orderChanged = true;
				updateButtonStates();
			});
		}

		dishTableViewDrink.getItems().addListener((ListChangeListener<Dish>) c -> {
			orderChanged = true;
			updateButtonStates();
		});

		for (Dish dish : dishes4) {
			dish.selectedSpecificationProperty().addListener((obs, oldVal, newVal) -> {
				orderChanged = true;
				updateButtonStates();
			});
		}

		restaurantComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
			if (newVal != null && !newVal.equals(currentRestaurant)) {
				if (currentRestaurant != null && !orderItems.isEmpty()) {
					showRestaurantChangeConfirmation(newVal);
				} else {
					currentRestaurant = newVal;
					requestRestaurantMenu(newVal);
					updateButtonStates();
				}
			}
		});

		addressField.textProperty().addListener((observable, oldValue, newValue) -> validateAddress());
		phoneNumberField.textProperty().addListener((observable, oldValue, newValue) -> validatePhoneNumber());

		deliveryTypeComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
			if (newVal != null) {
				updateDeliveryFields(newVal);

			}
		});

		addressField.textProperty().addListener((obs, oldVal, newVal) -> validateAddress());
		phoneNumberField.textProperty().addListener((obs, oldVal, newVal) -> validatePhoneNumber());
		deliveryDatePicker.valueProperty().addListener((obs, oldVal, newVal) -> {
			if (showErrorMessages)
				updateErrorMessages();
		});
		deliveryHourPicker.valueProperty().addListener((obs, oldVal, newVal) -> {
			if (showErrorMessages)
				updateErrorMessages();
		});
		deliveryMinutePicker.valueProperty().addListener((obs, oldVal, newVal) -> {
			if (showErrorMessages)
				updateErrorMessages();
		});

		companyNameField.textProperty().addListener((obs, oldVal, newVal) -> validateCompanyName());
		userNameField.textProperty().addListener((obs, oldVal, newVal) -> validateUserName());
		deliveryParticipantsField.textProperty().addListener((obs, oldVal, newVal) -> validateDeliveryParticipants());

		deliveryTypeComboBox.getSelectionModel().selectedItemProperty()
				.addListener((obs, oldVal, newVal) -> invalidateDeliveryConfirmation());
		addressField.textProperty().addListener((obs, oldVal, newVal) -> invalidateDeliveryConfirmation());
		companyNameField.textProperty().addListener((obs, oldVal, newVal) -> invalidateDeliveryConfirmation());
		userNameField.textProperty().addListener((obs, oldVal, newVal) -> invalidateDeliveryConfirmation());
		phoneNumberField.textProperty().addListener((obs, oldVal, newVal) -> invalidateDeliveryConfirmation());
		deliveryDatePicker.valueProperty().addListener((obs, oldVal, newVal) -> invalidateDeliveryConfirmation());
		deliveryHourPicker.valueProperty().addListener((obs, oldVal, newVal) -> invalidateDeliveryConfirmation());
		deliveryMinutePicker.valueProperty().addListener((obs, oldVal, newVal) -> invalidateDeliveryConfirmation());
		deliveryParticipantsField.textProperty().addListener((obs, oldVal, newVal) -> invalidateDeliveryConfirmation());

	}

	private void invalidateDeliveryConfirmation() {
		if (isDeliveryConfirmed) {
			isDeliveryConfirmed = false;
			confirmDeliveryText.setText("Delivery details changed. Please confirm again.");
			confirmDeliveryText.setFill(Color.ORANGE);
		}
	}

	private void updateButtonStates() {
		boolean restaurantSelected = currentRestaurant != null;
		boolean hasOrderItems = !orderItems.isEmpty();
		btnAddOrder.setDisable(!orderChanged);
		btnFinish.setDisable(!restaurantSelected || !hasOrderItems);
		btnRemoveItem.setDisable(!restaurantSelected || !hasOrderItems);
		removeItemText.setVisible(false);
		finishErrorText.setText("");

	}

	private boolean validateDeliveryFields() {
		if (deliveryTypeComboBox.getValue() == null) {
			return false;
		}
		boolean isValidPickup = deliveryDatePicker.getValue() != null && deliveryHourPicker.getValue() != null
				&& deliveryMinutePicker.getValue() != null && isDeliveryDateTimeValid();

		if (deliveryTypeComboBox.getValue().equals("Pickup")) {
			return isValidPickup;
		}
		boolean isValid = !addressField.getText().isEmpty() && !companyNameField.getText().isEmpty()
				&& !userNameField.getText().isEmpty() && !phoneNumberField.getText().isEmpty()
				&& deliveryDatePicker.getValue() != null && deliveryHourPicker.getValue() != null
				&& deliveryMinutePicker.getValue() != null && isAddressValid && isPhoneValid && isCompanyNameValid
				&& isUserNameValid && isDeliveryDateTimeValid();

		if (deliveryTypeComboBox.getValue().equals("Shared Delivery")) {
			isValid = isValid && isDeliveryParticipantsValid;
		}

		return isValid;
	}

	private void updateErrorMessages() {

		String deliveryType;
		deliveryType = deliveryTypeComboBox.getValue();
//    	if (deliveryType == "Pickup") {
//    		if (showErrorMessages) {
//                if (deliveryDatePicker.getValue() == null) {
//                    dateErrorText.setText("Please select a date");
//                } else if (deliveryDatePicker.getValue().isBefore(LocalDate.now())) {
//                    dateErrorText.setText("Selected date is in the past");
//                } else {
//                    dateErrorText.setText("");
//                }
//                
//                if (deliveryHourPicker.getValue() == null || deliveryMinutePicker.getValue() == null) {
//                    timeErrorText.setText("Please select a time");
//                } else if (!isDeliveryDateTimeValid()) {
//                    timeErrorText.setText("Selected time is in the past");
//                } else {
//                    timeErrorText.setText("");
//                }
//                if (deliveryDatePicker.getValue() == null) {
//                    dateErrorText.setText("Please select a date");
//                } else if (deliveryDatePicker.getValue().isBefore(LocalDate.now())) {
//                    dateErrorText.setText("Selected date is in the past");
//                } else {
//                    dateErrorText.setText("");
//                }
//                
//                if (deliveryHourPicker.getValue() == null || deliveryMinutePicker.getValue() == null) {
//                    timeErrorText.setText("Please select a time");
//                } else if (!isDeliveryDateTimeValid()) {
//                    timeErrorText.setText("Selected time is in the past");
//                } else {
//                    timeErrorText.setText("");
//                }
//                
//               
//            } else {
//                dateErrorText.setText("");
//                timeErrorText.setText("");
//            }
//    		return;
//    	}

		if (deliveryType != "Pickup") {
			addressErrorText.setText(
					showErrorMessages && !isAddressValid ? "Please use 'City, Street Name (optional number)'" : "");
			phoneErrorText.setText(
					showErrorMessages && !isPhoneValid ? "Please enter valid phone number (e.g. 0501133131)" : "");
			companyNameErrorText
					.setText(showErrorMessages && !isCompanyNameValid ? "Company name cannot be empty" : "");
			userNameErrorText.setText(showErrorMessages && !isUserNameValid ? "User name cannot be empty" : "");
		}

		if (showErrorMessages) {
			if (deliveryDatePicker.getValue() == null) {
				dateErrorText.setText("Please select a date");
			} else if (deliveryDatePicker.getValue().isBefore(LocalDate.now())) {
				dateErrorText.setText("Selected date is in the past");
			} else {
				dateErrorText.setText("");
			}

			if (deliveryHourPicker.getValue() == null || deliveryMinutePicker.getValue() == null) {
				timeErrorText.setText("Please select a time");
			} else if (!isDeliveryDateTimeValid()) {
				timeErrorText.setText("Selected time is in the past");
			} else {
				timeErrorText.setText("");
			}

			if (deliveryTypeComboBox.getValue() != null && deliveryTypeComboBox.getValue().equals("Shared Delivery")) {
				deliveryParticipantsErrorText.setText(
						showErrorMessages && !isDeliveryParticipantsValid ? "number of participants can only be 2-4"
								: "");
			} else {
				deliveryParticipantsErrorText.setText("");
			}
		} else {
			dateErrorText.setText("");
			timeErrorText.setText("");
		}
	}

	private boolean isDeliveryDateTimeValid() {
		if (deliveryDatePicker.getValue() == null) {
			return false;
		}

		LocalDate selectedDate = deliveryDatePicker.getValue();
		LocalDate today = LocalDate.now();

		if (selectedDate.isBefore(today)) {
			return false;
		}

		if (deliveryHourPicker.getValue() == null || deliveryMinutePicker.getValue() == null) {
			return selectedDate.isAfter(today);
		}

		LocalDateTime now = LocalDateTime.now();
		LocalDateTime selectedDateTime = LocalDateTime.of(selectedDate, LocalTime.of(
				Integer.parseInt(deliveryHourPicker.getValue()), Integer.parseInt(deliveryMinutePicker.getValue())));
		return selectedDateTime.isAfter(now);
	}

	private void showRestaurantChangeConfirmation(String newRestaurant) {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Change Restaurant");
		alert.setHeaderText("Changing restaurant will reset your current order.");
		alert.setContentText("Are you sure you want to change the restaurant?");

		Optional<ButtonType> result = alert.showAndWait();
		if (result.isPresent() && result.get() == ButtonType.OK) {
			resetEntireOrder();
			currentRestaurant = newRestaurant;
			requestRestaurantMenu(newRestaurant);
		} else {
			restaurantComboBox.setValue(currentRestaurant);
		}
		updateButtonStates();
	}

	private void validateCompanyName() {
		String companyName = companyNameField.getText().trim();
		isCompanyNameValid = !companyName.isEmpty();
		if (showErrorMessages) {
			updateErrorMessages();
		}
	}

	private void validateUserName() {
		String userName = userNameField.getText().trim();
		isUserNameValid = !userName.isEmpty();
		if (showErrorMessages) {
			updateErrorMessages();
		}
	}

	private void validateDeliveryParticipants() {
		String participants = deliveryParticipantsField.getText().trim();
		isDeliveryParticipantsValid = participants.matches("\\d+") && Integer.parseInt(participants) > 1
				&& Integer.parseInt(participants) <= 4;
		if (showErrorMessages) {
			updateErrorMessages();
		}
	}

	@FXML
	private void handleConfirmDelivery() {
		if (validateDeliveryFields()) {
			String deliveryType = deliveryTypeComboBox.getValue();
			switch (deliveryType) {
			case "Regular Delivery":
				deliveryCharge = 25;
				discountPercentage = 0;
				isItDelivery = true;
				break;
			case "Shared Delivery":
				discountPercentage = 0;
				deliveryCharge = calculateSharedFee();
				isItDelivery = true;
				break;

			case "Robot Delivery":
				deliveryCharge = 0;
				discountPercentage = 0;
				break;
			default: // DEFAULT IS PICKUP
				deliveryCharge = 0;
				discountPercentage = 0;
				isItDelivery = false;
			}
			updateOrderTotal();
			isDeliveryConfirmed = true;
			confirmDeliveryText.setText("Delivery details confirmed successfully");
			confirmDeliveryText.setFill(Color.GREEN);
			showErrorMessages = false;
		} else {
			showErrorMessages = true;
			confirmDeliveryText.setText("Please correct the errors in the delivery fields");
			confirmDeliveryText.setFill(Color.RED);
		}
		updateButtonStates();
		updateErrorMessages();
	}

	private void updateOrderTotal() {
		double orderPrice = orderItems.stream().mapToDouble(item -> item.getDishPrice() * item.getQuantity()).sum();

		String deliveryType = deliveryTypeComboBox.getValue();
		double deliveryFee = deliveryCharge;
		if (discountPercentage > 0) {
			deliveryFee = -orderPrice * discountPercentage;
		}

		double totalPrice = orderPrice + deliveryFee;

		orderPriceText.setText(String.format("Order Price: ₪%.2f", orderPrice));
		deliveryFeeText.setText(String.format("Delivery Fee: ₪%.2f (%s)", deliveryFee,
				deliveryType != null ? deliveryType : "Not selected"));
		totalPriceText.setText(String.format("Total Price: ₪%.2f", totalPrice));

		updateButtonStates();
	}

	@FXML
	void getBtnAddOrder(ActionEvent event) {
		List<OrderItem> newItems = new ArrayList<>();
		orderDishes.clear();
		boolean hasItems = false;
		double totalPrice = 0.0;
		for (Dish dish : dishes1) {
			int quantity = orderQuantitiesSalad.getOrDefault(dish.getDishID(), 0);
			if (quantity > 0) {
				OrderItem item = new OrderItem(dish, dish.getSelectedSpecification(), quantity);
				newItems.add(item);
				hasItems = true;
				totalPrice += item.getDishPrice() * item.getQuantity();
			}
		}
		for (Dish dish : dishes2) {
			int quantity = orderQuantitiesMain.getOrDefault(dish.getDishID(), 0);
			if (quantity > 0) {
				OrderItem item = new OrderItem(dish, dish.getSelectedSpecification(), quantity);
				newItems.add(item);
				hasItems = true;
				totalPrice += item.getDishPrice() * item.getQuantity();
			}
		}
		for (Dish dish : dishes3) {
			int quantity = orderQuantitiesDesert.getOrDefault(dish.getDishID(), 0);
			if (quantity > 0) {
				OrderItem item = new OrderItem(dish, dish.getSelectedSpecification(), quantity);
				newItems.add(item);
				hasItems = true;
				totalPrice += item.getDishPrice() * item.getQuantity();
			}
		}
		for (Dish dish : dishes4) {
			int quantity = orderQuantitiesDrink.getOrDefault(dish.getDishID(), 0);
			if (quantity > 0) {
				OrderItem item = new OrderItem(dish, dish.getSelectedSpecification(), quantity);
				newItems.add(item);
				hasItems = true;
				totalPrice += item.getDishPrice() * item.getQuantity();
			}
		}
		if (!hasItems) {
			errorText.setText("Please add items to order");
		} else {
			errorText.setText("");
			orderItems.addAll(newItems);
			resetOrderFields();
		}
		totalPriceText.setText(String.format("Total Price: ₪%.2f", totalPrice));
		orderChanged = false;
		updateButtonStates();
		updateOrderTotal();
	}

	@FXML
	void getBtnBack(ActionEvent event) throws Exception {
		((Node) event.getSource()).getScene().getWindow().hide();
		CustomerController newScreen = new CustomerController();
		newScreen.start(new Stage());
	}

	@FXML
	void getBtnFinish(ActionEvent event) throws Exception {
		if (orderItems.isEmpty()) {
			finishErrorText.setText("Please add items to order before finishing");
			return;
		}
		if (!validateDeliveryFields()) {
			finishErrorText.setText("Please correct the errors in the delivery fields");
			return;
		}

		// Save current timestamps
		java.sql.Timestamp tempBeginUpdate = this.beginUpdate;
		java.sql.Timestamp tempEndUpdate = this.endUpdate;
		this.tempBeginUpdate = tempBeginUpdate;
		this.tempEndUpdate = tempEndUpdate;
		this.event = event;

		// Set flag to true before calling requestRestaurantMenu
		checkTimeBeforeConfirm = true;
		requestRestaurantMenu(currentRestaurant);

		// Set flag to call inBetween method
		callBetween = true;
	}

	private void inBetween() throws Exception {

		callBetween = false;

		double totalPrice = Double.parseDouble(totalPriceText.getText().split(":")[1].trim().substring(1));

		if (Objects.equals(this.tempBeginUpdate, this.beginUpdate) && Objects.equals(this.tempEndUpdate, this.endUpdate)
				&& (this.beginUpdate == null || this.endUpdate == null
						|| this.beginUpdate.compareTo(this.endUpdate) <= 0)) {
			// Prepare order data
			List<Map<String, Object>> orderData = new ArrayList<>();
			int quantitySalad = 0;
			int quantityMain = 0;
			int quantityDessert = 0;
			int quantityDrink = 0;

			for (OrderItem item : orderItems) {
				Map<String, Object> itemData = new HashMap<>();
				itemData.put("dishID", item.getDishID());
				itemData.put("dishName", item.getDishName());
				itemData.put("quantity", item.getQuantity());
				itemData.put("price", item.getDishPrice());
				itemData.put("specification", item.getSelectedSpecification());
				orderData.add(itemData);

				// Increment the appropriate counter based on dish type
				switch (item.getCategoryName()) {
				case "salad":
					quantitySalad += item.getQuantity();
					break;
				case "main course":
					quantityMain += item.getQuantity();
					break;
				case "dessert":
					quantityDessert += item.getQuantity();
					break;
				case "drink":
					quantityDrink += item.getQuantity();
					break;
				}
			}

			if (currentCustomer.getCredit() != 0) {
				boolean feeResponse = feeAlert();
				if (feeResponse) {
					if (totalPrice > currentCustomer.getCredit()) {
						int newCreditBalance = 0;
						totalPrice = totalPrice - currentCustomer.getCredit();
						updateCustomerCredit(currentCustomer.getCustomerNumber(), newCreditBalance);
					} else {
						int newCreditBalance = currentCustomer.getCredit() - (int) totalPrice;
						totalPrice = 0;
						updateCustomerCredit(currentCustomer.getCustomerNumber(), newCreditBalance);
					}
				}
			}

			// Add customer and order details
			Map<String, Object> orderDetails = new HashMap<>();
			orderDetails.put("customerNumber", currentCustomer.getCustomerNumber());
			orderDetails.put("restaurantNumber", this.restaurantNumber);
			orderDetails.put("totalPrice",
					Double.parseDouble(totalPriceText.getText().split(":")[1].trim().substring(1)));

			// Add IsDelivery
			String deliveryType = deliveryTypeComboBox.getValue();
			int isDelivery = deliveryType.equals("Pickup") ? 0 : 1;
			orderDetails.put("isDelivery", isDelivery);

			// Add IsEarlyOrder
			LocalDateTime now = LocalDateTime.now();
			LocalDate deliveryDate = deliveryDatePicker.getValue();
			LocalTime deliveryTime = LocalTime.of(Integer.parseInt(deliveryHourPicker.getValue()),
					Integer.parseInt(deliveryMinutePicker.getValue()));
			LocalDateTime requestedDateTime = LocalDateTime.of(deliveryDate, deliveryTime);
			long hoursDifference = ChronoUnit.HOURS.between(now, requestedDateTime);
			int isEarlyOrder = (deliveryDate.equals(now.toLocalDate()) && hoursDifference <= 2) ? 1 : 0;
			isItEarlyOrder = isEarlyOrder;
			if (isItEarlyOrder == 1) {
				totalPrice = totalPrice * 0.9;
				totalPriceText.setText(String.format("Total Price: ₪%.2f", totalPrice));
				orderDetails.put("totalPrice", totalPrice);
			}

			orderDetails.put("isEarlyOrder", isEarlyOrder);

			// Add dish type quantities
			orderDetails.put("quantitySalad", quantitySalad);
			orderDetails.put("quantityMain", quantityMain);
			orderDetails.put("quantityDessert", quantityDessert);
			orderDetails.put("quantityDrink", quantityDrink);

			// Add timestamps
			orderDetails.put("orderDateTime", Timestamp.valueOf(now));
			orderDetails.put("requestedDateTime", Timestamp.valueOf(requestedDateTime));

			// Add delivery-specific details only if it's not a pickup order
			if (isDelivery == 1) {
				orderDetails.put("deliveryType", deliveryType);
				orderDetails.put("address", addressField.getText());
				orderDetails.put("phoneNumber", phoneNumberField.getText());
			}

			// Combine order items and details
			List<Object> completeOrderData = new ArrayList<>();
			completeOrderData.add(orderDetails);
			completeOrderData.add(orderData);

			// Send order to server
			Message msg = new Message(completeOrderData, Commands.sendCustomerOrder);
			ClientController.client.handleMessageFromClientControllers(msg);

			finishErrorText.setText("Order submitted successfully!");
			orderSummaryAlert(deliveryTypeComboBox.getValue(), totalPrice, isItEarlyOrder, requestedDateTime);
//        resetEntireOrder();
			((Node) this.event.getSource()).getScene().getWindow().hide();
			CustomerController newScreen = new CustomerController();
			newScreen.start(new Stage());
		} else {
			kickedOutAlert();
			((Node) this.event.getSource()).getScene().getWindow().hide();
			CustomerController newScreen = new CustomerController();
			newScreen.start(new Stage());
		}

		// Reset the flag
		this.checkTimeBeforeConfirm = false;

	}

	private void kickedOutAlert() {
		Alert alert = new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Order Update");
		alert.setHeaderText(null);

		// Create a larger content text with adjusted layout
		String contentText = "Your restaurant's menu was updated during your order,\n" + "please make a new order!\n\n"
				+ "We're sorry for the inconvenience!!!!";

		alert.setContentText(contentText);

		// Create a single "Ok" button
		ButtonType okButton = new ButtonType("Ok", ButtonBar.ButtonData.OK_DONE);
		alert.getButtonTypes().setAll(okButton);

		// Increase the width and height of the alert
		alert.getDialogPane().setPrefWidth(400);
		alert.getDialogPane().setPrefHeight(200);

		// Center the button and text
		alert.getDialogPane().setStyle("-fx-alignment: CENTER;");

		alert.showAndWait();
	}

	private void updateCustomerCredit(int customerNumber, int newCreditBalance) {
		Map<String, Object> creditUpdateData = new HashMap<>();
		creditUpdateData.put("customerNumber", customerNumber);
		creditUpdateData.put("newCreditBalance", newCreditBalance);

		Message msg = new Message(creditUpdateData, Commands.updateCustomerCredit);
		ClientController.client.handleMessageFromClientControllers(msg);
	}

	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/customer/NewOrder.fxml"));
		Parent root = loader.load();
		Scene scene = new Scene(root);
		primaryStage.setTitle("New Order");
		primaryStage.setScene(scene);
		primaryStage.show();
	}

	private void updateDeliveryFields(String deliveryType) {
		boolean isRobotDelivery = "Robot Delivery".equals(deliveryType);
		boolean isPickup = "Pickup".equals(deliveryType);
		boolean isOtherDelivery = !isRobotDelivery && !isPickup && deliveryType != null;

		// Disable everything for Robot Delivery
		if (isRobotDelivery) {
			setAllFieldsDisabled(true);
			confirmDeliveryButton.setDisable(true);
			confirmDeliveryText.setText("Feature not yet available");
			confirmDeliveryText.setFill(Color.RED);
		}
		// For Pickup, disable everything except pickers
		else if (isPickup) {
			setAllFieldsDisabled(true);
			enablePickers();
			confirmDeliveryButton.setDisable(false);
			confirmDeliveryText.setText(null);
		}
		// For other delivery types, enable everything
		else if (isOtherDelivery) {
			setAllFieldsDisabled(false);
			confirmDeliveryButton.setDisable(false);
			confirmDeliveryText.setText(null);
		}

		// Show/hide shared delivery based on User type
		boolean isSharedDelivery = "Shared Delivery".equals(deliveryType);
		deliveryParticipantsField.setVisible(isSharedDelivery);
		deliveryParticipantsField.setManaged(isSharedDelivery);

		// Reset fields when choosing new delivery option
		if (isOtherDelivery || isPickup) {
			resetFields();
		}
	}

	private void setAllFieldsDisabled(boolean disabled) {
		addressField.setDisable(disabled);
		companyNameField.setDisable(disabled);
		userNameField.setDisable(disabled);
		phoneNumberField.setDisable(disabled);
		deliveryDatePicker.setDisable(disabled);
		deliveryHourPicker.setDisable(disabled);
		deliveryMinutePicker.setDisable(disabled);
		deliveryParticipantsField.setDisable(disabled);
	}

	private void enablePickers() {
		deliveryDatePicker.setDisable(false);
		deliveryHourPicker.setDisable(false);
		deliveryMinutePicker.setDisable(false);
	}

	private void resetFields() {
		addressField.clear();
		companyNameField.clear();
		userNameField.clear();
		phoneNumberField.clear();
		deliveryDatePicker.setValue(null);
		deliveryHourPicker.setValue(null);
		deliveryMinutePicker.setValue(null);
		deliveryParticipantsField.clear();
		showErrorMessages = false;
		updateErrorMessages();
	}

	private void validateAddress() {
		String address = addressField.getText().trim();
		String[] parts = address.split(",");
		isAddressValid = parts.length == 2 && parts[0].trim().matches("^[a-zA-Z\\s]+$")
				&& parts[1].trim().matches("^[a-zA-Z\\s]+(\\s?\\d{1,3})?$");
		if (showErrorMessages) {
			updateErrorMessages();
		}
	}

	private void validatePhoneNumber() {
		String phone = phoneNumberField.getText().trim();
		isPhoneValid = phone.matches("\\d{10}");
		if (showErrorMessages) {
			updateErrorMessages();
		}
	}

	void getRestaurantList() {
		Message msg = new Message(null, Commands.getRestaurantList);
		ClientController.client.handleMessageFromClientControllers(msg);
	}

	private void resetOrderFields() {
		resetTableFields(dishTableViewSalad, orderQuantitiesSalad);
		resetTableFields(dishTableViewMainCourse, orderQuantitiesMain);
		resetTableFields(dishTableViewDesert, orderQuantitiesDesert);
		resetTableFields(dishTableViewDrink, orderQuantitiesDrink);
	}

	private void resetTableFields(TableView<Dish> tableView, Map<String, Integer> quantityMap) {
		for (Dish dish : tableView.getItems()) {
			quantityMap.put(dish.getDishID(), 0);
			// Reset to the first specification instead of "None"
			dish.setSelectedSpecification(dish.getSpecifications().get(0));
		}
		tableView.refresh();
	}

	private void resetEntireOrder() {
		orderItems.clear();
		resetOrderFields();
		updateOrderTotal();
		orderChanged = false;
	}

	private void handleRemoveItem(ActionEvent event) {
		OrderItem selectedItem = orderTableView.getSelectionModel().getSelectedItem();
		if (selectedItem != null) {
			orderItems.remove(selectedItem);
			updateOrderTotal();
			removeItemText.setText("Removed: " + selectedItem.getDishName());
			updateButtonStates();
			removeItemText.setVisible(true);
		}
	}

	private double calculateSharedFee() {
		String participantsString = deliveryParticipantsField.getText().trim();
		int participants = Integer.parseInt(participantsString);
		if (participants == 2) {
			deliveryCharge = 30;
			return deliveryCharge;
		} else {
			deliveryCharge = participants * 10;
			return deliveryCharge;
		}
	}

	// alert of credit
	private boolean feeAlert() {
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Before you continue");
		alert.setHeaderText("You have: " + currentCustomer.getCredit());
		alert.setContentText("Do you want to pay with credit?");

		ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
		ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

		alert.getButtonTypes().setAll(yesButton, noButton);

		Optional<ButtonType> result = alert.showAndWait();
		if (result.isPresent() && result.get() == yesButton) {
			return true;
		} else if (result.isPresent() && result.get() == noButton) {
			return false;
		}

		return false; // Default return in case the alert is closed in another way
	}

	private void orderSummaryAlert(String deliveryChoice, double totalPrice, int isItEarlyOrder,
			LocalDateTime requestedDateTime) {
		// Create a StringBuilder to construct the order summary

		StringBuilder orderSummary = new StringBuilder();

		// Append each item in the ObservableList to the order summary
		for (OrderItem item : orderItems) {
			orderSummary.append(" - Dish Type: ").append(item.getCategoryName()).append(" - Dish Name: ")
					.append(item.getDishName()).append(", Price: $").append(item.getDishPrice())
					.append(", specifications: ").append(item.getSelectedSpecification()).append(" - Quantity: ")
					.append(item.getQuantity()).append("\n");

		}

		// Construct the full message
		String message;
		if (isItEarlyOrder == 1) {
			message = "Your Order:\n" + orderSummary.toString() + "\nDelivery Choice: " + deliveryChoice
					+ " with early order" + "\nTotal Price: $" + String.format("%.2f", totalPrice) + "\nArrival at: "
					+ requestedDateTime;
		} else {
			message = "Your Order:\n" + orderSummary.toString() + "\nDelivery Choice: " + deliveryChoice
					+ "\nTotal Price: $" + String.format("%.2f", totalPrice) + "\nArrival at: " + requestedDateTime;
		}

		// Create the alert
		Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
		alert.setTitle("Order Confirmation");
		alert.setHeaderText("Please review your order");
		alert.setContentText(message);

		alert.getDialogPane().setPrefSize(800, 600); // Set width to 600 and height to 400

		// You can also set a minimum size if needed
		alert.getDialogPane().setMinHeight(Region.USE_PREF_SIZE);
		alert.getDialogPane().setMinWidth(Region.USE_PREF_SIZE);

		// Create the Yes and No buttons
		ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);

		// Set the buttons in the alert
		alert.getButtonTypes().setAll(yesButton);

		// Show the alert and wait for a response
		Optional<ButtonType> result = alert.showAndWait();
		if (result.isPresent() && result.get() == yesButton) {
			System.out.println("okay");
		}

	}

}