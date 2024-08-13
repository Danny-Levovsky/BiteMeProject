package entites;

//Class based on DB's Table

public class Option {
	private int optionID;
	private OptionType optionType;

	public enum OptionType {
		INGREDIENT, COOKING_LEVEL
	}

	public Option(int optionID, OptionType optionType) {
		this.optionID = optionID;
		this.optionType = optionType;
	}

	public int getOptionID() {
		return optionID;
	}

	public OptionType getType() {
		return optionType;
	}
}
