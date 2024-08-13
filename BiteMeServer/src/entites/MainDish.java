package entites;

import enums.MainCourse;
import enums.GrillOption;

public class MainDish extends Dish {
	private MainCourse meat;
	private GrillOption doneness;

	// Constructor for burger with doneness field
	public MainDish(int dishID, int restaurantNumber, int categoryId, String dishName, MainCourse burger,
			GrillOption doneness) {
		super(dishID, restaurantNumber, categoryId, dishName);
		this.doneness = doneness;
		this.meat = burger;
	}

	// Constructor for Chicken and Tuna dishes, doneness set to NONE
	public MainDish(int dishID, int restaurantNumber, int categoryId, String dishName, MainCourse meat) {
		super(dishID, restaurantNumber, categoryId, dishName);
		this.meat = meat;
		this.doneness = GrillOption.NONE;
	}

	public MainCourse getMeatType() {
		return meat;
	}

	public GrillOption getDoneness() {
		return doneness;
	}

}
