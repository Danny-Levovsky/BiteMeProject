package entites;
import java.io.Serializable;

import enums.OptionType;


/**
 * Represents a DishOption entity based on the corresponding database table.
 * This class implements Serializable for object serialization.
 * 
 * @author yosra
 */
public class DishOption implements Serializable{
	
	 /**
     * A unique identifier for the serialization runtime to associate with the DishOption class.
     * This value ensures that a deserialized object is compatible with the current class definition.
     */
	private static final long serialVersionUID = 1L;
	
	/**
     * The unique identifier of the dish this option belongs to.
     */
    private int dishID;
    
    /**
     * The type of the option.
     */
    private OptionType optionType;
    
    /**
     * The value of the option.
     */
    private String optionValue;
    
  
    /**
     * Constructs a new DishOption with the specified details.
     *
     * @param dishID      The unique identifier of the dish this option belongs to
     * @param optionType  The type of the option
     * @param optionValue The value of the option
     */
    public DishOption(int dishID, OptionType optionType, String optionValue) {
        this.dishID = dishID;
        this.optionType = optionType;
        this.optionValue = optionValue;
    }


    /**
     * Returns the dish ID.
     *
     * @return The dish ID
     */
    public int getDishID() {
        return dishID;
    }

    
    /**
     * Returns the option type.
     *
     * @return The option type
     */
    public OptionType getOptionType() {
        return optionType;
    }

    
    /**
     * Returns the option value.
     *
     * @return The option value
     */ 
    public String getOptionValue() {
        return optionValue;
    }
}