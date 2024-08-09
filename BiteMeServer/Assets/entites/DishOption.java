package entites;
import java.io.Serializable;

import enums.OptionType;

//Class based on DB's Table

public class DishOption implements Serializable{
	private static final long serialVersionUID = 1L;
	 
    private int dishID;
    private OptionType optionType;
    private String optionValue;
    
    
    public DishOption(int dishID, OptionType optionType, String optionValue) {
        this.dishID = dishID;
        this.optionType = optionType;
        this.optionValue = optionValue;
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