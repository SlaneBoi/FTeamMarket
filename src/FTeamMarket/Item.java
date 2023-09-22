package FTeamMarket;

public class Item {

	protected String id;
	protected String name;
	protected String desc;
	protected Integer price;
	protected Integer quantity;
	public static int count;
	public Item(String id, String name, String desc, Integer price, Integer quantity) {
		super();
		this.id = id;
		this.name = name;
		this.desc = desc;
		this.price = price;
		this.quantity = quantity;
		count++;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDesc() {
		return desc;
	}
	public void setDesc(String desc) {
		this.desc = desc;
	}
	public Integer getPrice() {
		return price;
	}
	public void setPrice(Integer price) {
		this.price = price;
	}
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	public static int getCount() {
		return count;
	}
	public static void setCount(int count) {
		Item.count = count;
	}
	
	

}