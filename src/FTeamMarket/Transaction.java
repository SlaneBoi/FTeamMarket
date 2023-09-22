package FTeamMarket;

import java.util.Date;

public class Transaction {

	protected String transactionID;
	protected String buyerID;
	protected String buyerName;
	protected String itemName;
	protected String itemDesc;
	protected Integer itemPrice;
	protected Integer itemQty;
	protected Integer itemTotalPrice;
	protected Date transactionDate;
	
	public Transaction(String transactionID, String itemName, String itemDesc, Integer itemPrice, Integer itemQty,
			Integer itemTotalPrice, Date transactionDate) {
		super();
		this.transactionID = transactionID;
		this.itemName = itemName;
		this.itemDesc = itemDesc;
		this.itemPrice = itemPrice;
		this.itemQty = itemQty;
		this.itemTotalPrice = itemTotalPrice;
		this.transactionDate = transactionDate;
	}
	
	public Transaction(String transactionID, String buyerName, String itemName, String itemDesc, Integer itemPrice,
			Integer itemQty, Integer itemTotalPrice, Date transactionDate) {
		super();
		this.transactionID = transactionID;
		this.buyerName = buyerName;
		this.itemName = itemName;
		this.itemDesc = itemDesc;
		this.itemPrice = itemPrice;
		this.itemQty = itemQty;
		this.itemTotalPrice = itemTotalPrice;
		this.transactionDate = transactionDate;
	}

	public Transaction(String transactionID, String buyerID, Date transactionDate) {
		super();
		this.transactionID = transactionID;
		this.buyerID = buyerID;
		this.transactionDate = transactionDate;
	}

	public String getTransactionID() {
		return transactionID;
	}
	public void setTransactionID(String transactionID) {
		this.transactionID = transactionID;
	}
	public String getBuyerName() {
		return buyerName;
	}
	public void setBuyerName(String buyerName) {
		this.buyerName = buyerName;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public String getItemDesc() {
		return itemDesc;
	}
	public void setItemDesc(String itemDesc) {
		this.itemDesc = itemDesc;
	}
	public Integer getItemPrice() {
		return itemPrice;
	}
	public void setItemPrice(Integer itemPrice) {
		this.itemPrice = itemPrice;
	}
	public Integer getItemQty() {
		return itemQty;
	}
	public void setItemQty(Integer itemQty) {
		this.itemQty = itemQty;
	}
	public Integer getItemTotalPrice() {
		return itemTotalPrice;
	}
	public void setItemTotalPrice(Integer itemTotalPrice) {
		this.itemTotalPrice = itemTotalPrice;
	}
	public Date getTransactionDate() {
		return transactionDate;
	}
	public void setTransactionDate(Date transactionDate) {
		this.transactionDate = transactionDate;
	}

	public String getBuyerID() {
		return buyerID;
	}

	public void setBuyerID(String buyerID) {
		this.buyerID = buyerID;
	}
	
	
}