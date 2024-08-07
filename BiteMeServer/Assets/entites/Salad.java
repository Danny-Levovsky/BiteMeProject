/**package entites;
import enums.SaladType;
import enums.ItemSize;

public class Salad extends Dish{
	private SaladType saladType;
	private ItemSize itemSize;

	public Salad(int dishID, int restaurantNumber, int categoryId, String dishName,
			SaladType saladType, ItemSize itemSize) {
		
		super(dishID, restaurantNumber, categoryId, dishName);
		this.saladType = saladType;
		this.itemSize = itemSize;
	}
	
	public SaladType getSaladType() {
		return saladType;
	}
	
	public ItemSize getSize() {
		return itemSize;
	}
}
**/