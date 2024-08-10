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

    @FXML private ComboBox<String> restaurantComboBox;
    @FXML private Button btnBack;
    @FXML private Button btnAddOrder;
    @FXML private Button btnFinish;
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
    
    private boolean isCompanyNameValid = false;
    private boolean isUserNameValid = false;
    private boolean isDeliveryParticipantsValid = false;
    private boolean isAddressValid = false;
    private boolean isPhoneValid = false;
    private boolean orderChanged = false;
    private String currentRestaurant = null;
    private boolean showErrorMessages = false;
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
        orderQuantitiesSalad = new HashMap<>(); // Move this line here
        orderQuantitiesMain = new HashMap<>();
        orderQuantitiesDesert = new HashMap<>();
        orderQuantitiesDrink = new HashMap<>();
        restaurantList = FXCollections.observableArrayList(); // set list of restaurant names from method
        confirmDeliveryButton.setDisable(false);
        
        errorText = new Text();
        errorText.setFill(Color.RED);
        errorText.setStyle("-fx-font-size: 12px;");
        
        //request restaurant names
        requestRestaurantNames();
        //setCustomer(customer);
        
        
        
        //setupComboBoxes();
        setupDishTableView();
        addListeners();
        //addSampleData();
        updateButtonStates();
        setupOrderTableView();
        
        restaurantComboBox.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && !newValue.equals("Choose a restaurant")) {
                requestRestaurantMenu(newValue);
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
            // This is a new customer, create and set it
            this.currentCustomer = new Customer(
                customerDetails.getCustomerNumber(),
                customerDetails.getId(),
                customerDetails.getCredit(),
                customerDetails.isBusiness(),
                customerDetails.getStatus()
            );
    	}
    	System.out.println(currentCustomer);
    	Platform.runLater(this::setupUI);
    	
    }
    private void setupUI() {
        setupComboBoxes();
        // Set up other UI elements as needed
    }
    
    //implement request for rest names
    public void requestRestaurantNames() {
    	Commands command = Commands.getRestaurantList;
    	Message message = new Message(null,command);
    	ClientController.client.handleMessageFromClientControllers(message);
    	//System.out.println(customer.getId());
    	
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
    
    public void setRestaurantMenu(ArrayList<Map<String, Object>> menu) {
        dishes1.clear();
        orderQuantitiesSalad.clear();
        
        dishes2.clear();
        orderQuantitiesMain.clear();
        
        dishes3.clear();
        orderQuantitiesDesert.clear();
        
        dishes4.clear();
        orderQuantitiesDrink.clear();
        
        Map<String, Dish> dishMap = new HashMap<>();

        for (Map<String, Object> dish : menu) {
            String dishID = (String) dish.get("dishID");
            String dishType = (String) dish.get("dishType");
            String dishName = (String) dish.get("dishName");
            int dishPrice = ((Number) dish.get("dishPrice")).intValue();
            Map<String, List<String>> dishOptions = (Map<String, List<String>>) dish.get("dishOptions");

            // Get or create the Dish object for this dishID
            Dish newDish;
            if (dishMap.containsKey(dishID)) {
                newDish = dishMap.get(dishID);
            } else {
                ObservableList<String> specifications = FXCollections.observableArrayList();
                newDish = new Dish(dishID, dishName, dishType, dishPrice, specifications);
                dishMap.put(dishID, newDish);
            }

         // Add specifications to the existing Dish object
            for (Map.Entry<String, List<String>> entry : dishOptions.entrySet()) {
                String optionType = entry.getKey();
                List<String> optionValues = entry.getValue();
                
                if (optionType.equals("Doneness")) {
                    for (String value : optionValues) {
                        String specificationText = "Doneness: " + value;
                        if (!newDish.getSpecifications().contains(specificationText)) {
                            newDish.getSpecifications().add(specificationText);
                        }
                    }
                } else if (optionType.equals("Remove")) {
                    for (String value : optionValues) {
                        String specificationText = "Remove " + value;
                        if (!newDish.getSpecifications().contains(specificationText)) {
                            newDish.getSpecifications().add(specificationText);
                        }
                    }
                } else {
                    for (String value : optionValues) {
                        String specificationText = optionType + ": " + value;
                        if (!newDish.getSpecifications().contains(specificationText)) {
                            newDish.getSpecifications().add(specificationText);
                        }
                    }
                }
            }

            newDish.setSelectedSpecification("None");
        }

        // Categorize dishes into the appropriate lists
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

        // Set items and refresh table views
        dishTableViewSalad.setItems(dishes1);
        dishTableViewSalad.refresh();
        
        dishTableViewMainCourse.setItems(dishes2);
        dishTableViewMainCourse.refresh();
        
        dishTableViewDesert.setItems(dishes3);
        dishTableViewDesert.refresh();
        
        dishTableViewDrink.setItems(dishes4);
        dishTableViewDrink.refresh();
        
        orderChanged = true;
        updateButtonStates();
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

    private void setupDishTableView() {
        //dishTypeColumn.setCellValueFactory(new PropertyValueFactory<>("categoryName"));
        dishNameColumnSalad.setCellValueFactory(new PropertyValueFactory<>("dishName"));
        dishPriceColumnSalad.setCellValueFactory(new PropertyValueFactory<>("dishPrice"));
        
        dishNameColumnMain.setCellValueFactory(new PropertyValueFactory<>("dishName"));
        dishPriceColumnMain.setCellValueFactory(new PropertyValueFactory<>("dishPrice"));
        
        dishNameColumnDesert.setCellValueFactory(new PropertyValueFactory<>("dishName"));
        dishPriceColumnDesert.setCellValueFactory(new PropertyValueFactory<>("dishPrice"));
        
        dishNameColumnDrink.setCellValueFactory(new PropertyValueFactory<>("dishName"));
        dishPriceColumnDrink.setCellValueFactory(new PropertyValueFactory<>("dishPrice"));

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
                        ObservableList<String> specs = FXCollections.observableArrayList("None");
                        specs.addAll(dish.getSpecifications());
                        comboBox.setItems(specs);
                        comboBox.setValue(dish.getSelectedSpecification() != null ? dish.getSelectedSpecification() : "None");
                        comboBox.setOnAction(event -> dish.setSelectedSpecification(comboBox.getValue()));
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
                        ObservableList<String> specs = FXCollections.observableArrayList("None");
                        specs.addAll(dish.getSpecifications());
                        comboBox.setItems(specs);
                        comboBox.setValue(dish.getSelectedSpecification() != null ? dish.getSelectedSpecification() : "None");
                        comboBox.setOnAction(event -> dish.setSelectedSpecification(comboBox.getValue()));
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
                        ObservableList<String> specs = FXCollections.observableArrayList("None");
                        specs.addAll(dish.getSpecifications());
                        comboBox.setItems(specs);
                        comboBox.setValue(dish.getSelectedSpecification() != null ? dish.getSelectedSpecification() : "None");
                        comboBox.setOnAction(event -> dish.setSelectedSpecification(comboBox.getValue()));
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
                        ObservableList<String> specs = FXCollections.observableArrayList("None");
                        specs.addAll(dish.getSpecifications());
                        comboBox.setItems(specs);
                        comboBox.setValue(dish.getSelectedSpecification() != null ? dish.getSelectedSpecification() : "None");
                        comboBox.setOnAction(event -> dish.setSelectedSpecification(comboBox.getValue()));
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

    private void setupOrderTableView() {
//        orderDishTypeColumn.setCellValueFactory(new PropertyValueFactory<>("categoryName"));
//        orderDishNameColumn.setCellValueFactory(new PropertyValueFactory<>("dishName"));
//        orderDishPriceColumn.setCellValueFactory(new PropertyValueFactory<>("dishPrice"));
//        orderSpecificationsColumn.setCellValueFactory(new PropertyValueFactory<>("selectedSpecification"));
//        orderQuantityColumn.setCellValueFactory(cellData -> {
//            Dish dish = cellData.getValue();
//            return javafx.beans.binding.Bindings.createIntegerBinding(() -> {
//                switch (dish.getCategoryName()) {
//                    case "salad":
//                        return orderQuantitiesSalad.getOrDefault(dish.getDishID(), 0);
//                    case "main course":
//                        return orderQuantitiesMain.getOrDefault(dish.getDishID(), 0);
//                    case "dessert":
//                        return orderQuantitiesDesert.getOrDefault(dish.getDishID(), 0);
//                    case "drink":
//                        return orderQuantitiesDrink.getOrDefault(dish.getDishID(), 0);
//                    default:
//                        return 0;
//                }
//            }).asObject();
//        });
//
//        orderTableView.setItems(orderDishes);
    	orderDishTypeColumn.setCellValueFactory(new PropertyValueFactory<>("categoryName"));
        orderDishNameColumn.setCellValueFactory(new PropertyValueFactory<>("dishName"));
        orderDishPriceColumn.setCellValueFactory(new PropertyValueFactory<>("dishPrice"));
        orderSpecificationsColumn.setCellValueFactory(new PropertyValueFactory<>("selectedSpecification"));
        orderQuantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        orderTableView.setItems(orderItems);
    }

    private void addListeners() {
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

//        restaurantComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
//            if (newVal != null && !newVal.equals(currentRestaurant)) {
//                if (currentRestaurant != null && !orderDishes.isEmpty()) {
//                    showRestaurantChangeConfirmation(newVal);
//                } else {
//                    currentRestaurant = newVal;
//                    updateButtonStates();
//                }
//            }
//        });
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

    }
    
    private void updateButtonStates() {
        boolean restaurantSelected = currentRestaurant != null;
        btnAddOrder.setDisable(!restaurantSelected || !orderChanged);
        btnFinish.setDisable(!restaurantSelected || orderDishes.isEmpty());
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
        	errorText.setText("");
            dateErrorText.setText("");
            timeErrorText.setText("");
            confirmDeliveryText.setText("");
            finishErrorText.setText("");
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
    
    private void resetOrder() {
        orderDishes.clear();
        orderItems.clear();
        orderQuantitiesSalad.clear();
        orderChanged = false;
        deliveryCharge = 0;
        discountPercentage = 0;
        resetOrderFields();
        updateOrderTotal();
        updateButtonStates();
        
        // Reset spinners and specifications
        for (Dish dish : dishes1) {
            orderQuantitiesSalad.put(dish.getDishID(), 0);
            dish.setSelectedSpecification("None");
        }
        dishTableViewSalad.refresh();
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
            confirmDeliveryText.setText("Delivery details confirmed successfully");
            showErrorMessages = false;
        } else {
        	showErrorMessages = true;
        	finishErrorText.setText("Please correct the errors in the delivery fields");
        }
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
    }
    
//    private void updateOrderTotal() {
//    	double orderPrice = 0;
//        orderPrice += orderDishes.stream()
//            .filter(dish -> dish.getCategoryName().equals("salad"))
//            .mapToDouble(dish -> dish.getDishPrice() * orderQuantitiesSalad.get(dish.getDishID()))
//            .sum();
//        orderPrice += orderDishes.stream()
//            .filter(dish -> dish.getCategoryName().equals("main course"))
//            .mapToDouble(dish -> dish.getDishPrice() * orderQuantitiesMain.get(dish.getDishID()))
//            .sum();
//        orderPrice += orderDishes.stream()
//            .filter(dish -> dish.getCategoryName().equals("dessert"))
//            .mapToDouble(dish -> dish.getDishPrice() * orderQuantitiesDesert.get(dish.getDishID()))
//            .sum();
//        orderPrice += orderDishes.stream()
//            .filter(dish -> dish.getCategoryName().equals("drink"))
//            .mapToDouble(dish -> dish.getDishPrice() * orderQuantitiesDrink.get(dish.getDishID()))
//            .sum();
//        
//        String deliveryType = deliveryTypeComboBox.getValue();
//        double deliveryFee = deliveryCharge;
//        if (discountPercentage > 0) {
//            deliveryFee = -orderPrice * discountPercentage;
//        }
//
//        double totalPrice = orderPrice + deliveryFee;
//
//        // Update the text in the View Order tab
//        orderPriceText.setText(String.format("Order Price: ₪%.2f", orderPrice));
//        deliveryFeeText.setText(String.format("Delivery Fee: ₪%.2f (%s)", deliveryFee, deliveryType != null ? deliveryType : "Not selected"));
//        totalPriceText.setText(String.format("Total Price: ₪%.2f", totalPrice));
//    }

    @FXML
    void getBtnAddOrder(ActionEvent event) {
    	List<OrderItem> newItems = new ArrayList<>();
        orderDishes.clear();
        boolean hasItems = false;
        double totalPrice = 0.0;
        for (Dish dish : dishes1) {
//            int quantity = orderQuantitiesSalad.getOrDefault(dish.getDishID(), 0);
//            if (quantity > 0) {
//                orderDishes.add(dish);
//                hasItems = true;
//                totalPrice += dish.getDishPrice() * quantity;
//            }
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
//            if (quantity > 0) {
//                orderDishes.add(dish);
//                hasItems = true;
//                totalPrice += dish.getDishPrice() * quantity;
//            }
        	if (quantity > 0) {
        		OrderItem item = new OrderItem(dish, dish.getSelectedSpecification(), quantity);
                newItems.add(item);
                hasItems = true;
                totalPrice += item.getDishPrice() * item.getQuantity();
            }
        }
        for (Dish dish : dishes3) {
            int quantity = orderQuantitiesDesert.getOrDefault(dish.getDishID(), 0);
//            if (quantity > 0) {
//                orderDishes.add(dish);
//                hasItems = true;
//                totalPrice += dish.getDishPrice() * quantity;
//            }
            if (quantity > 0) {
            	OrderItem item = new OrderItem(dish, dish.getSelectedSpecification(), quantity);
                newItems.add(item);
                hasItems = true;
                totalPrice += item.getDishPrice() * item.getQuantity();
            }
        }
        for (Dish dish : dishes4) {
            int quantity = orderQuantitiesDrink.getOrDefault(dish.getDishID(), 0);
//            if (quantity > 0) {
//                orderDishes.add(dish);
//                hasItems = true;
//                totalPrice += dish.getDishPrice() * quantity;
//            }
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
        if (orderDishes.isEmpty()) {
            finishErrorText.setText("Please add items to order before finishing");
        } else if (!validateDeliveryFields()) {
            finishErrorText.setText("Please correct the errors in the delivery fields");
        } else {
            double totalPrice = 0.0;
            for (Dish dish : orderDishes) {
                int quantity = orderQuantitiesSalad.get(dish.getDishID());
                System.out.println("Ordered " + quantity + " of " + dish.getDishName());
                totalPrice += dish.getDishPrice() * quantity;
            }
            System.out.println(String.format("Total Price: ₪%.2f", totalPrice));
            // Proceed with order submission
            finishErrorText.setText("");
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
        
        // Show/hide delivery participants field
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
    
//    private void resetTableFields(TableView<Dish> tableView, Map<String, Integer> quantityMap) {
//        for (Dish dish : tableView.getItems()) {
//            quantityMap.put(dish.getDishID(), 0);
//            dish.setSelectedSpecification("None");
//        }
//        tableView.refresh();
//    }
    private void resetTableFields(TableView<Dish> tableView, Map<String, Integer> quantityMap) {
        for (Dish dish : tableView.getItems()) {
            quantityMap.put(dish.getDishID(), 0);
            dish.setSelectedSpecification("None");
        }
        tableView.refresh();
    }
    
    private void resetEntireOrder() {
        orderItems.clear();
        resetOrderFields();
        updateOrderTotal();
        orderChanged = false;
    }
}