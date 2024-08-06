package customer;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Callback;
import client.ClientController;
import entites.Dish;
import entites.Message;
import enums.Commands;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap; 
import java.util.Map;   
import java.util.Optional;
public class NewOrderController {

	@FXML private TableView<Dish> dishTableView;
    @FXML private TableColumn<Dish, String> dishTypeColumn;
    @FXML private TableColumn<Dish, String> dishNameColumn;
    @FXML private TableColumn<Dish, Integer> dishPriceColumn;
    @FXML private TableColumn<Dish, String> specificationsColumn;
    @FXML private TableColumn<Dish, Integer> quantityColumn;

    @FXML private TableView<Dish> orderTableView;
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
    private ObservableList<String> restaurantList = FXCollections.observableArrayList("Crusty", "McDonalds", "Burgerking");
    private ObservableList<Dish> dishes = FXCollections.observableArrayList();
    private ObservableList<Dish> orderDishes = FXCollections.observableArrayList();
    private Map<Integer, Integer> orderQuantities = new HashMap<>();

    @FXML
    private void initialize() {
        dishes = FXCollections.observableArrayList();
        orderDishes = FXCollections.observableArrayList();
        orderQuantities = new HashMap<>(); // Move this line here
        restaurantList = FXCollections.observableArrayList("Crusty", "McDonalds", "Burgerking");
        confirmDeliveryButton.setDisable(false);

        setupComboBoxes();
        setupDishTableView();
        setupOrderTableView();
        addListeners();
        addSampleData();
        updateButtonStates();
    }

    private void setupComboBoxes() {
        restaurantComboBox.setValue("Choose a restaurant");
        restaurantComboBox.setItems(restaurantList);

        deliveryTypeComboBox.getItems().addAll("Pickup", "Regular Delivery", "Early Delivery", "Shared Delivery", "Robot Delivery");

        // Setup hour and minute pickers
        for (int i = 0; i < 24; i++) {
            deliveryHourPicker.getItems().add(String.format("%02d", i));
        }
        for (int i = 0; i < 60; i += 5) {
            deliveryMinutePicker.getItems().add(String.format("%02d", i));
        }
    }

    private void setupDishTableView() {
        dishTypeColumn.setCellValueFactory(new PropertyValueFactory<>("categoryName"));
        dishNameColumn.setCellValueFactory(new PropertyValueFactory<>("dishName"));
        dishPriceColumn.setCellValueFactory(new PropertyValueFactory<>("dishPrice"));

        specificationsColumn.setCellFactory(column -> {
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

        quantityColumn.setCellFactory(column -> new TableCell<Dish, Integer>() {
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
                            orderQuantities.put(dish.getDishID(), newValue);
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
                        spinner.getValueFactory().setValue(orderQuantities.getOrDefault(dish.getDishID(), 0));
                    }
                    setGraphic(spinner);
                }
            }
        });

        dishTableView.setItems(dishes);
    }

    private void setupOrderTableView() {
        orderDishTypeColumn.setCellValueFactory(new PropertyValueFactory<>("categoryName"));
        orderDishNameColumn.setCellValueFactory(new PropertyValueFactory<>("dishName"));
        orderDishPriceColumn.setCellValueFactory(new PropertyValueFactory<>("dishPrice"));
        orderSpecificationsColumn.setCellValueFactory(new PropertyValueFactory<>("selectedSpecification"));
        orderQuantityColumn.setCellValueFactory(cellData -> {
            Dish dish = cellData.getValue();
            return javafx.beans.binding.Bindings.createIntegerBinding(
                () -> orderQuantities.getOrDefault(dish.getDishID(), 0)).asObject();
        });

        orderTableView.setItems(orderDishes);
    }

    private void addListeners() {
        dishTableView.getItems().addListener((ListChangeListener<Dish>) c -> {
            orderChanged = true;
            updateButtonStates();
        });

        for (Dish dish : dishes) {
            dish.selectedSpecificationProperty().addListener((obs, oldVal, newVal) -> {
                orderChanged = true;
                updateButtonStates();
            });
        }

        restaurantComboBox.getSelectionModel().selectedItemProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal != null && !newVal.equals(currentRestaurant)) {
                if (currentRestaurant != null && !orderDishes.isEmpty()) {
                    showRestaurantChangeConfirmation(newVal);
                } else {
                    currentRestaurant = newVal;
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

    private void addSampleData() {
        dishes.addAll(
            new Dish(1, "Yerushalmi Salad", "Salad", 40, FXCollections.observableArrayList("Regular", "No Onions", "Extra Dressing")),
            new Dish(2, "Tuna Salad", "Salad", 36, FXCollections.observableArrayList("Regular", "Spicy", "Extra Mayo")),
            new Dish(5, "Mac and Cheese", "Main Course", 57, FXCollections.observableArrayList("Regular", "Extra Cheese", "With Bacon")),
            new Dish(6, "Salmon with Herbs", "Main Course", 97, FXCollections.observableArrayList("Regular", "Lemon Butter", "Cajun Style")),
            new Dish(9, "Alfajor", "Dessert", 19, FXCollections.observableArrayList("Regular", "Extra Dulce de Leche", "Chocolate Coated")),
            new Dish(13, "Ice Tea", "Drink", 12, FXCollections.observableArrayList("Regular", "No Sugar", "Extra Ice"))
        );
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
            resetOrder();
            currentRestaurant = newRestaurant;
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
        orderQuantities.clear();
        orderChanged = false;
        deliveryCharge = 0;
        discountPercentage = 0;
        updateOrderTotal();
        
        // Reset spinners and specifications
        for (Dish dish : dishes) {
            orderQuantities.put(dish.getDishID(), 0);
            dish.setSelectedSpecification("None");
        }
        dishTableView.refresh();
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
                case "Early Delivery":
                    deliveryCharge = 0;
                    discountPercentage = 0.1; // 10% discount
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
        double orderPrice = orderDishes.stream()
            .mapToDouble(dish -> dish.getDishPrice() * orderQuantities.get(dish.getDishID()))
            .sum();
        
        String deliveryType = deliveryTypeComboBox.getValue();
        double deliveryFee = deliveryCharge;
        if (discountPercentage > 0) {
            deliveryFee = -orderPrice * discountPercentage;
        }

        double totalPrice = orderPrice + deliveryFee;

        // Update the text in the View Order tab
        orderPriceText.setText(String.format("Order Price: ₪%.2f", orderPrice));
        deliveryFeeText.setText(String.format("Delivery Fee: ₪%.2f (%s)", deliveryFee, deliveryType != null ? deliveryType : "Not selected"));
        totalPriceText.setText(String.format("Total Price: ₪%.2f", totalPrice));
    }

    @FXML
    void getBtnAddOrder(ActionEvent event) {
        orderDishes.clear();
        boolean hasItems = false;
        double totalPrice = 0.0;
        for (Dish dish : dishes) {
            int quantity = orderQuantities.getOrDefault(dish.getDishID(), 0);
            if (quantity > 0) {
                orderDishes.add(dish);
                hasItems = true;
                totalPrice += dish.getDishPrice() * quantity;
            }
        }
        if (!hasItems) {
            errorText.setText("Please add items to order");
        } else {
            errorText.setText("");
        }
        totalPriceText.setText(String.format("Total Price: ₪%.2f", totalPrice));
        orderChanged = false;
        updateButtonStates();
        updateOrderTotal();
    }

    @FXML
    void getBtnBack(ActionEvent event) {
        // Implement back functionality
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
                int quantity = orderQuantities.get(dish.getDishID());
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
    
    
    
//    private boolean isDeliveryTimeValid() {
//        if (deliveryDatePicker.getValue() == null || 
//        		deliveryHourPicker.getValue() == null ||
//        		deliveryMinutePicker.getValue() == null) {
//            return false;
//        }
//
//        LocalDateTime selectedDateTime = LocalDateTime.of(
//            deliveryDatePicker.getValue(),
//            LocalTime.of(Integer.parseInt
//            		(deliveryHourPicker.getValue()),
//            		Integer.parseInt(deliveryMinutePicker.getValue()))
//        );
//
//        return selectedDateTime.isAfter(LocalDateTime.now());
//    }
    
//    private void updateConfirmButtonState() {
//        boolean allFieldsFilled = !addressField.getText().isEmpty() &&
//                                  !companyNameField.getText().isEmpty() &&
//                                  !userNameField.getText().isEmpty() &&
//                                  !phoneNumberField.getText().isEmpty() &&
//                                  deliveryDatePicker.getValue() != null &&
//                                  deliveryHourPicker.getValue() != null &&
//                                  deliveryMinutePicker.getValue() != null;
//
//        boolean isPickup = deliveryTypeComboBox.getValue() != null && deliveryTypeComboBox.getValue().equals("Pickup");
//
//        confirmDeliveryButton.setDisable(!(isPickup || (allFieldsFilled && isAddressValid && isPhoneValid && isDeliveryDateTimeValid())));
//    }

//    void getRestaurantList() {
//        Message msg = new Message(null, Commands.getRestaurantList);
//        ClientController.client.handleMessageFromClientControllers(msg);
//    }
}