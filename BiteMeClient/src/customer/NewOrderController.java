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
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import client.Client;
import client.ClientController;
import entites.Customer;
import entites.Dish;
import entites.Message;
import entites.User;
import enums.Commands;

import entites.OrderItem;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;   
import java.util.Optional;
public class NewOrderController {

	@FXML private TableView<Dish> dishTableViewSalad;
    @FXML private TableColumn<Dish, String> dishNameColumnSalad;
    @FXML private TableColumn<Dish, Integer> dishPriceColumnSalad;
    @FXML private TableColumn<Dish, String> specificationsColumnSalad;
    @FXML private TableColumn<Dish, Integer> quantityColumnSalad;
    
    @FXML private TableView<Dish> dishTableViewMainCourse;
    @FXML private TableColumn<Dish, String> dishNameColumnMain;
    @FXML private TableColumn<Dish, Integer> dishPriceColumnMain;
    @FXML private TableColumn<Dish, String> specificationsColumnMain;
    @FXML private TableColumn<Dish, Integer> quantityColumnMain;
    
    @FXML private TableView<Dish> dishTableViewDrink;
    @FXML private TableColumn<Dish, String> dishNameColumnDrink;
    @FXML private TableColumn<Dish, Integer> dishPriceColumnDrink;
    @FXML private TableColumn<Dish, String> specificationsColumnDrink;
    @FXML private TableColumn<Dish, Integer> quantityColumnDrink;
    
    @FXML private TableView<Dish> dishTableViewDesert;
    @FXML private TableColumn<Dish, String> dishNameColumnDesert;
    @FXML private TableColumn<Dish, Integer> dishPriceColumnDesert;
    @FXML private TableColumn<Dish, String> specificationsColumnDesert;
    @FXML private TableColumn<Dish, Integer> quantityColumnDesert;
    
    @FXML
	private Label SaladLbl;
    @FXML
	private Label MainCourseLbl;
    @FXML
	private Label DesertLbl;
    @FXML
	private Label DrinkLbl;

    @FXML private TableView<OrderItem> orderTableView;
    @FXML private TableColumn<Dish, String> orderDishTypeColumn;
    @FXML private TableColumn<Dish, String> orderDishNameColumn;
    @FXML private TableColumn<Dish, Integer> orderDishPriceColumn;
    @FXML private TableColumn<Dish, String> orderSpecificationsColumn;
    @FXML private TableColumn<Dish, Integer> orderQuantityColumn;
    @FXML private Text orderPriceText;
    @FXML private Text deliveryFeeText;
    @FXML private Text removeItemText;

    @FXML private ComboBox<String> restaurantComboBox;
    @FXML private Button btnBack;
    @FXML private Button btnAddOrder;
    @FXML private Button btnFinish;
    @FXML private Button btnRemoveItem;
    @FXML private Text confirmDeliveryText;
    @FXML private Text totalPriceText;
    
    @FXML private Text addressErrorText;
    @FXML private Text phoneErrorText;
    
    @FXML private ComboBox<String> deliveryTypeComboBox;
    @FXML private ComboBox<String> deliveryTimePicker;
    @FXML private TextField addressField;
    @FXML private TextField companyNameField;
    @FXML private TextField userNameField;
    @FXML private TextField phoneNumberField;
    @FXML private DatePicker deliveryDatePicker;
    @FXML private Button confirmDeliveryButton;
    @FXML private ComboBox<String> deliveryHourPicker;
    @FXML private ComboBox<String> deliveryMinutePicker;
    @FXML private TextField deliveryParticipantsField;
    
    @FXML private Text deliveryParticipantsErrorText;
    @FXML private Text errorText;
    @FXML private Text dateErrorText;
    @FXML private Text timeErrorText;
    @FXML private Text companyNameErrorText;
    @FXML private Text userNameErrorText;
    @FXML private Text finishErrorText;
    
    private double deliveryCharge = 0;
    private double discountPercentage = 0;
    
    private java.sql.Timestamp beginUpdate;
    private java.sql.Timestamp endUpdate;
    private boolean timeIsNull = false;
    private boolean checkTimeBeforeConfirm = false;
    private boolean isCompanyNameValid = false;
    private boolean isUserNameValid = false;
    private boolean isDeliveryParticipantsValid = false;
    private boolean isAddressValid = false;
    private boolean isPhoneValid = false;
    private boolean orderChanged = false;
    private String currentRestaurant = null;
    private boolean showErrorMessages = false;
    private boolean isDeliveryConfirmed = false;
    private int restaurantNumber;
    private ObservableList<String> restaurantList = FXCollections.observableArrayList();
    private ObservableList<Dish> dishes1 = FXCollections.observableArrayList();
    private ObservableList<Dish> dishes2 = FXCollections.observableArrayList();
    private ObservableList<Dish> dishes3 = FXCollections.observableArrayList();
    private ObservableList<Dish> dishes4 = FXCollections.observableArrayList();
    private ObservableList<Dish> orderDishes = FXCollections.observableArrayList();
    private Map<String, Integer> orderQuantitiesSalad = new HashMap<>();
    private Map<String, Integer> orderQuantitiesMain = new HashMap<>();
    private Map<String, Integer> orderQuantitiesDesert = new HashMap<>();
    private Map<String, Integer> orderQuantitiesDrink = new HashMap<>();
    private ObservableList<OrderItem> orderItems = FXCollections.observableArrayList();
    private static User customer;
    private Customer currentCustomer;

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
        
        // We'll delay these setups until we have customer details
        // setupDishTableView();
        // addListeners();
        // updateButtonStates();
        // setupOrderTableView();
        
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
    
    
    public static void setCustomer(User user) {
    	
    	customer = user;
    	int userID = customer.getId();
    	Commands command = Commands.getCustomerDetails;
    	Message message = new Message(userID,command);
    	ClientController.client.handleMessageFromClientControllers(message);
    	
    }
    
    public void setCustomerDetails(Customer customerDetails) {
        if (this.currentCustomer == null) {
            this.currentCustomer = new Customer(
                customerDetails.getCustomerNumber(),
                customerDetails.getId(),
                customerDetails.getCredit(),
                customerDetails.isBusiness(),
                customerDetails.getStatus()
            );
        }
        System.out.println("Current Customer: " + currentCustomer);
        Platform.runLater(this::completeSetup);
    }

    private void completeSetup() {
        setupUI();
        setupDishTableView();
        addListeners();
        updateButtonStates();
        setupOrderTableView();
    }

    private void setupUI() {
        setupComboBoxes();
        // Any other UI setup that depends on customer details
    }
    
    //implement request for Restaurant names
    public void requestRestaurantNames() {
    	Commands command = Commands.getRestaurantList;
    	Message message = new Message(null,command);
    	ClientController.client.handleMessageFromClientControllers(message);
    	
    }
    //implement setting combo box
    public void setRestaurantNames(ArrayList<String> restaurantNames) {
        Platform.runLater(() -> {
            restaurantComboBox.setValue("Choose a restaurant");
            this.restaurantList = FXCollections.observableArrayList(restaurantNames);
            restaurantComboBox.setItems(this.restaurantList);
        });
    }
    
    private void requestRestaurantMenu(String restaurantName) {
        Message message = new Message(restaurantName, Commands.getRestaurantMenu);
        ClientController.client.handleMessageFromClientControllers(message);
    }
    
    /**
     * Sets up the restaurant menu based on the provided menu data.
     * This method processes the menu data, creates Dish objects, and populates the appropriate TableViews.
     * It also sets default specifications for size and doneness options.
     *
     * @param menu An ArrayList of Map objects, each representing a dish with its properties and options.
     */
    public void setRestaurantMenu(ArrayList<Map<String, Object>> menu) {
        dishes1.clear();
        orderQuantitiesSalad.clear();
        dishes2.clear();
        orderQuantitiesMain.clear();
        dishes3.clear();
        orderQuantitiesDesert.clear();
        dishes4.clear();
        orderQuantitiesDrink.clear();
        
        System.out.println("NewOrderController: Received menu size: " + menu.size());
        Map<String, Dish> dishMap = new HashMap<>();
        
        // The last item in the menu list contains the restaurant info
        Map<String, Object> restaurantInfo = menu.remove(menu.size() - 1);
        
        // Debug log
        System.out.println("NewOrderController: Restaurant Info map: " + restaurantInfo);
        
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
            System.out.println("NewOrderController: RestaurantNumber set to: " + this.restaurantNumber);
        } else {
            this.restaurantNumber = -1;
            System.out.println("NewOrderController: RestaurantNumber is null, set to -1");
        }
        
        // Print statements to verify the data
        System.out.println("Restaurant Information:");
        System.out.println("Restaurant Number: " + this.restaurantNumber);
        if (this.timeIsNull) {
            System.out.println("Update times are NULL");
        } else {
            System.out.println("Begin Update: " + this.beginUpdate);
            System.out.println("End Update: " + this.endUpdate);
        }
        System.out.println("TimeIsNull: " + this.timeIsNull);
        
        
        //Instead of creating another method and sql query and etc.
        //Save the current beginUpdate and endUpdate then
        //Call method requestRestaurantMenu() to pull the menu (yes the entire menu)
        //this will update the beginUpdate and endUpdate times so you can compare them 
        //This flag is here so that if you're just calling requestRestaurantMenu to confirm order
        //that it will not do anything other than get update the the timestamps.
        //Don't forget to set the flag to true when calling requestRestaurantMenu
        //and then back to false after comparing.
        
        if(!checkTimeBeforeConfirm) {
        
        	
        	
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
    }

    private void setupComboBoxes() {
        restaurantComboBox.setValue("Choose a restaurant");
        restaurantComboBox.setItems(restaurantList);
        
        if (currentCustomer.isBusiness()== true) {
        deliveryTypeComboBox.getItems().addAll("Pickup", "Regular Delivery", "Shared Delivery", "Robot Delivery");
        }
        else {
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
     * Sets up the TableViews for displaying dish information.
     * This method configures the columns for each TableView, including the specifications column
     * which uses a ComboBox for selecting dish options.
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
         * Configures the specifications column to use a ComboBox for selecting dish options.
         * The ComboBox is populated with available specifications for each dish and displays
         * the currently selected specification.
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
                        comboBox.setValue(dish.getSelectedSpecification() != null ? dish.getSelectedSpecification() : specs.get(0));
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
                        comboBox.setValue(dish.getSelectedSpecification() != null ? dish.getSelectedSpecification() : specs.get(0));
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
                        comboBox.setValue(dish.getSelectedSpecification() != null ? dish.getSelectedSpecification() : specs.get(0));
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
                        comboBox.setValue(dish.getSelectedSpecification() != null ? dish.getSelectedSpecification() : specs.get(0));
                        comboBox.setOnAction(event -> {
                            dish.setSelectedSpecification(comboBox.getValue());
                            handleSpecificationChange(dish, comboBox.getValue());
                        });
                        setGraphic(comboBox);
                    }
                }
            };
        });
       
        
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
    
    ///Might need to delete if this da
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
            System.out.println("Dish or newValue is null in handleSpecificationChange");
            if (dish == null) {
                System.out.println("Dish is null");
            } else {
                System.out.println("Dish: " + dish.getDishName() + ", but newValue is null");
            }
            return;
        }

        System.out.println("Handling specification change for dish: " + dish.getDishName() + ", new value: " + newValue);

        if (newValue.startsWith("Size: ")) {
            String selectedSize = newValue.substring(6).toLowerCase();
            Map<String, Integer> sizePrices = dish.getSizePrices();
            if (sizePrices == null) {
                System.out.println("SizePrices is null for dish: " + dish.getDishName());
                return;
            }
            Integer newPrice = sizePrices.get(selectedSize);
            if (newPrice != null) {
                dish.setDishPrice(newPrice);
                updateDishPrice(dish);
                System.out.println("Updated price for " + dish.getDishName() + " to " + newPrice + " for size " + selectedSize);
            } else {
                System.out.println("No price found for size: " + selectedSize + " in dish: " + dish.getDishName());
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
    	
    	orderItems.addListener((ListChangeListener<OrderItem>) c -> updateButtonStates()); //Listener to update 'Confirm Order' button
    	
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
        deliveryDatePicker.valueProperty().addListener((obs, oldVal, newVal) -> {if (showErrorMessages) updateErrorMessages();});
        deliveryHourPicker.valueProperty().addListener((obs, oldVal, newVal) -> {if (showErrorMessages) updateErrorMessages();});
        deliveryMinutePicker.valueProperty().addListener((obs, oldVal, newVal) -> {if (showErrorMessages) updateErrorMessages();});
        
        companyNameField.textProperty().addListener((obs, oldVal, newVal) -> validateCompanyName());
        userNameField.textProperty().addListener((obs, oldVal, newVal) -> validateUserName());
        deliveryParticipantsField.textProperty().addListener((obs, oldVal, newVal) -> validateDeliveryParticipants());
        
        deliveryTypeComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> invalidateDeliveryConfirmation());
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
        if (deliveryTypeComboBox.getValue().equals("Pickup")) {
            return true;
        }
        boolean isValid = !addressField.getText().isEmpty() &&
                !companyNameField.getText().isEmpty() &&
                !userNameField.getText().isEmpty() &&
                !phoneNumberField.getText().isEmpty() &&
                deliveryDatePicker.getValue() != null &&
                deliveryHourPicker.getValue() != null &&
                deliveryMinutePicker.getValue() != null &&
                isAddressValid &&
                isPhoneValid &&
                isCompanyNameValid &&
                isUserNameValid &&
                isDeliveryDateTimeValid();

        if (deliveryTypeComboBox.getValue().equals("Shared Delivery")) {
            isValid = isValid && isDeliveryParticipantsValid;
        }

        return isValid;
    }
    
    private void updateErrorMessages() {
        addressErrorText.setText(showErrorMessages && !isAddressValid ? "Please use 'City, Street Name (optional number)'" : "");
        phoneErrorText.setText(showErrorMessages && !isPhoneValid ? "Please enter valid phone number (e.g. 0501133131)" : "");
        companyNameErrorText.setText(showErrorMessages && !isCompanyNameValid ? "Company name cannot be empty" : "");
        userNameErrorText.setText(showErrorMessages && !isUserNameValid ? "User name cannot be empty" : "");
        
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
                deliveryParticipantsErrorText.setText(showErrorMessages && !isDeliveryParticipantsValid ? "Please enter a number between 1 and 100" : "");
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
        if (deliveryTypeComboBox.getValue().equals("Pickup")) {
            return true;
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
        LocalDateTime selectedDateTime = LocalDateTime.of(
            selectedDate,
            LocalTime.of(Integer.parseInt(deliveryHourPicker.getValue()), Integer.parseInt(deliveryMinutePicker.getValue()))
        );
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
        isDeliveryParticipantsValid = participants.matches("\\d+") && 
                                      Integer.parseInt(participants) > 0 && 
                                      Integer.parseInt(participants) <= 100;
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
                    break;
                case "Shared Delivery":
                    deliveryCharge = 15;
                    discountPercentage = 0;
                    break;
                case "Robot Delivery":
                    deliveryCharge = 0;
                    discountPercentage = 0;
                    break;
                default:
                    deliveryCharge = 0;
                    discountPercentage = 0;
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
        double orderPrice = orderItems.stream()
            .mapToDouble(item -> item.getDishPrice() * item.getQuantity())
            .sum();

        String deliveryType = deliveryTypeComboBox.getValue();
        double deliveryFee = deliveryCharge;
        if (discountPercentage > 0) {
            deliveryFee = -orderPrice * discountPercentage;
        }

        double totalPrice = orderPrice + deliveryFee;

        orderPriceText.setText(String.format("Order Price: ₪%.2f", orderPrice));
        deliveryFeeText.setText(String.format("Delivery Fee: ₪%.2f (%s)", deliveryFee, deliveryType != null ? deliveryType : "Not selected"));
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
    void getBtnFinish(ActionEvent event) {
        if (orderItems.isEmpty()) {
            finishErrorText.setText("Please add items to order before finishing");
        } else if (!validateDeliveryFields()) {
            finishErrorText.setText("Please correct the errors in the delivery fields");
        } else {
            // Save current timestamps
            java.sql.Timestamp tempBeginUpdate = this.beginUpdate;
            java.sql.Timestamp tempEndUpdate = this.endUpdate;

            // Set flag to true before calling requestRestaurantMenu
            this.checkTimeBeforeConfirm = true;
            requestRestaurantMenu(currentRestaurant);

            // Compare timestamps
            if (tempBeginUpdate.equals(this.beginUpdate) && tempEndUpdate.equals(this.endUpdate)) {
                // Prepare order data
                List<Map<String, Object>> orderData = new ArrayList<>();
                for (OrderItem item : orderItems) {
                    Map<String, Object> itemData = new HashMap<>();
                    itemData.put("dishID", item.getDishID());
                    itemData.put("dishName", item.getDishName());
                    itemData.put("quantity", item.getQuantity());
                    itemData.put("price", item.getDishPrice());
                    itemData.put("specification", item.getSelectedSpecification());
                    orderData.add(itemData);
                }

                // Add customer and order details
                Map<String, Object> orderDetails = new HashMap<>();
                orderDetails.put("customerNumber", currentCustomer.getCustomerNumber());
                orderDetails.put("restaurantNumber", this.restaurantNumber);
                orderDetails.put("deliveryType", deliveryTypeComboBox.getValue());
                orderDetails.put("address", addressField.getText());
                orderDetails.put("phoneNumber", phoneNumberField.getText());
                orderDetails.put("deliveryDate", deliveryDatePicker.getValue().toString());
                orderDetails.put("deliveryTime", deliveryHourPicker.getValue() + ":" + deliveryMinutePicker.getValue());
                orderDetails.put("totalPrice", Double.parseDouble(totalPriceText.getText().split(":")[1].trim().substring(1)));

                // Combine order items and details
                List<Object> completeOrderData = new ArrayList<>();
                completeOrderData.add(orderDetails);
                completeOrderData.add(orderData);

                // Send order to server
                Message msg = new Message(completeOrderData, Commands.sendCustomerOrder);
                ClientController.client.handleMessageFromClientControllers(msg);

                finishErrorText.setText("Order submitted successfully!");
                resetEntireOrder();
            } else {
                finishErrorText.setText("Menu has been updated. Please review your order and try again.");
            }

            // Reset the flag
            this.checkTimeBeforeConfirm = false;
        }
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
    	boolean fieldsEnabled = !deliveryType.equals("Pickup") && deliveryType != null;
        addressField.setDisable(!fieldsEnabled);
        companyNameField.setDisable(!fieldsEnabled);
        userNameField.setDisable(!fieldsEnabled);
        phoneNumberField.setDisable(!fieldsEnabled);
        deliveryDatePicker.setDisable(!fieldsEnabled);
        deliveryHourPicker.setDisable(!fieldsEnabled);
        deliveryMinutePicker.setDisable(!fieldsEnabled);
        
        // Show/hide shared delivery based on User type 
        deliveryParticipantsField.setVisible(deliveryType.equals("Shared Delivery"));
        deliveryParticipantsField.setManaged(deliveryType.equals("Shared Delivery"));
        
        // Reset fields when choosing new delivery option
        if (fieldsEnabled || deliveryType.equals("Pickup")) {
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
    }
    
    private void validateAddress() {
        String address = addressField.getText().trim();
        String[] parts = address.split(",");
        isAddressValid = parts.length == 2 && 
                         parts[0].trim().matches("^[a-zA-Z\\s]+$") && 
                         parts[1].trim().matches("^[a-zA-Z\\s]+(\\s?\\d{1,3})?$");
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
}