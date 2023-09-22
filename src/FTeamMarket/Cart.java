package FTeamMarket;

public class Cart {
	private String itemID;
	private String userID;
	private int quantity;
	private String itemName;
	private String itemDescription;
	private int price;
	private int totalprice = price * quantity;

	public Cart(String itemID, String userID, String itemName, String itemDescription, int price, int quantity, int totalprice) {
		super();
		this.itemID = itemID;
		this.userID = userID;
		this.itemName = itemName;
		this.itemDescription = itemDescription;
		this.price = price;
		this.quantity = quantity;
		this.totalprice = totalprice;
	}

	public String getItemID() {
		return itemID;
	}

	public void setItemID(String itemID) {
		this.itemID = itemID;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public String getItemName() {
		return itemName;
	}

	public void setItemName(String itemName) {
		this.itemName = itemName;
	}

	public String getItemDescription() {
		return itemDescription;
	}

	public void setItemDescription(String itemDescription) {
		this.itemDescription = itemDescription;
	}

	public int getPrice() {
		return price;
	}

	public void setPrice(int price) {
		this.price = price;
	}

	public int getTotalprice() {
		return totalprice;
	}

	public void setTotalprice(int totalprice) {
		this.totalprice = totalprice;
	}

	
	
}
