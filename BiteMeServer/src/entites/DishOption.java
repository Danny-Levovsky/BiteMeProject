package entites;

import enums.OptionType;

//Class based on DB's Table

public class DishOption {
	private int dishOptionID;
	private int dishID;
	private OptionType optionType;
	private String optionValue;

	public DishOption(int dishOptionID, int dishID, OptionType optionType, String optionValue) {
		this.dishOptionID = dishOptionID;
		this.dishID = dishID;
		this.optionType = optionType;
		this.optionValue = optionValue;
	}

	public int getDishOptionID() {
		return dishOptionID;
	}

	public int getDishID() {
		return dishID;
	}

	public OptionType getOptionType() {
		return optionType;
	}

	public String getOptionValue() {
		return optionValue;
	}
}