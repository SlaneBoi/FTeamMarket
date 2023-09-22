package FTeamMarket;

import java.sql.Date;
import java.util.Vector;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class TransactionPage extends Application implements EventHandler<ActionEvent>{

	Scene scene;
	BorderPane mainPane;
	TableView transactionTable;
	Vector<Transaction> transactions;
	
	Menu menu;
	MenuBar menubar;
	MenuItem Menuitems, Menuitems1, Menuitems2, Menuitems3;
	
	Connect connect = Connect.getInstance();
	
	public void Initialize() {	
		mainPane = new BorderPane();
		scene = new Scene(mainPane,1050,600);
		transactionTable = new TableView<>();	
		transactions = new Vector<Transaction>();
		
		Menuitems = new MenuItem("Manage User");
		Menuitems1 = new MenuItem("Manage Item");
		Menuitems2 = new MenuItem("Transaction");
		Menuitems3 = new MenuItem("Logout");
		
		menu = new Menu("Menu");
		menubar = new MenuBar();
		
		menu.getItems().add(Menuitems);
		menu.getItems().add(Menuitems1);
		menu.getItems().add(Menuitems2);
		menu.getItems().add(Menuitems3);
		menubar.getMenus().add(menu);
		
		Menuitems.setOnAction(this);
		Menuitems1.setOnAction(this);
		Menuitems2.setOnAction(this);
		Menuitems3.setOnAction(this);
	}
	
	public void Positioning() {	
		mainPane.setCenter(transactionTable);
		mainPane.setTop(menubar);
	}
	
	public void setUpTable() {
		
		TableColumn<Transaction, String> transactionIdColumn = new TableColumn<>("transactionID");
		transactionIdColumn.setCellValueFactory(new PropertyValueFactory<Transaction, String>("transactionID"));
		transactionIdColumn.setResizable(false);	
		transactionIdColumn.setMinWidth(mainPane.getWidth()/8);		
		
		
		TableColumn<Transaction, String> buyerColumn = new TableColumn<>("buyer");
		buyerColumn.setCellValueFactory(new PropertyValueFactory<Transaction, String>("buyerName"));
		buyerColumn.setResizable(false);	
		buyerColumn.setMinWidth(mainPane.getWidth()/8);

		
		TableColumn<Transaction, String> itemNameColumn = new TableColumn<>("Item Name");
		itemNameColumn.setCellValueFactory(new PropertyValueFactory<Transaction, String>("itemName"));
		itemNameColumn.setResizable(false);
		itemNameColumn.setMinWidth(mainPane.getWidth()/8);
	
		
		TableColumn<Transaction, String> itemDescColumn = new TableColumn<>("Item Description");
		itemDescColumn.setCellValueFactory(new PropertyValueFactory<Transaction, String>("itemDesc"));
		itemDescColumn.setResizable(false);
		itemDescColumn.setMinWidth(mainPane.getWidth()/8);
	
		
		TableColumn<Transaction, Integer> itemPriceColumn = new TableColumn<>("Price");
		itemPriceColumn.setCellValueFactory(new PropertyValueFactory<Transaction, Integer>("itemPrice"));
		itemPriceColumn.setResizable(false);
		itemPriceColumn.setMinWidth(mainPane.getWidth()/8);	
		
		TableColumn<Transaction, Integer> itemQtyColumn = new TableColumn<>("Item Quantity");
		itemQtyColumn.setCellValueFactory(new PropertyValueFactory<Transaction, Integer>("itemQty"));
		itemQtyColumn.setResizable(false);	
		itemQtyColumn.setMinWidth(mainPane.getWidth()/8);
		
		TableColumn<Transaction, Integer> itemTotalPriceColumn = new TableColumn<>("Total Price");
		itemTotalPriceColumn.setCellValueFactory(new PropertyValueFactory<Transaction, Integer>("itemTotalPrice"));
		itemTotalPriceColumn.setResizable(false);
		itemTotalPriceColumn.setMinWidth(mainPane.getWidth()/8);
				
		TableColumn<Transaction, String> transactionDateColumn = new TableColumn<>("transaction date");
		transactionDateColumn.setCellValueFactory(new PropertyValueFactory<Transaction, String>("transactionDate"));
		transactionDateColumn.setResizable(false);		
		transactionDateColumn.setMinWidth(mainPane.getWidth()/8);
		
		transactionTable.getColumns().addAll(transactionIdColumn, buyerColumn, itemNameColumn, itemDescColumn, itemPriceColumn, itemQtyColumn, itemTotalPriceColumn, transactionDateColumn);
		transactionTable.setMaxHeight(600);
		
		refreshTable();
	}
	
	private void getData() {
		transactions.removeAllElements();

		String query = "SELECT t.transactionID, u.username, i.itemName, i.itemDescription, i.price, i.price*td.quantity AS 'totalPrice', td.quantity, t.transactionDate "
				+ "FROM transaction t JOIN transactiondetail td ON t.transactionID = td.transactionID "
				+ "JOIN user u ON t.userID = u.userID "
				+ "JOIN item i ON td.itemID = i.itemID";
		
		connect.rs = connect.execQuery(query);

		try {
			while(connect.rs.next()) {
				String transactionID = connect.rs.getString("transactionID");
				String buyerName = connect.rs.getString("username");
				String itemName = connect.rs.getString("itemName");
				String itemDescription = connect.rs.getString("itemDescription");
				Integer itemPrice = connect.rs.getInt("price");
				Integer itemQty = connect.rs.getInt("quantity");
				Integer itemTotalPrice = connect.rs.getInt("totalPrice");
				Date transactionDate = connect.rs.getDate("transactionDate");
				transactions.add(new Transaction(transactionID, buyerName, itemName, itemDescription, itemPrice, itemQty, itemTotalPrice, transactionDate));
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}		
	}
	
	private void refreshTable() {	
		getData();
		ObservableList<Transaction> transactionobs = FXCollections.observableArrayList(transactions);
		transactionTable.setItems(transactionobs);
		}	

	@Override
	public void start(Stage stg) throws Exception {
		Initialize();
		Positioning();
		setUpTable();
		stg.setTitle("admin page");
		stg.setScene(scene);
		stg.show();
	}

	@Override
	public void handle(ActionEvent e) {
		if (e.getSource() == Menuitems) {
			Stage curr = (Stage)mainPane.getScene().getWindow();
			curr.close();
			Stage next = new Stage();
			try {
				new ManageUser().start(next);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		else if (e.getSource() == Menuitems1) {
			Stage curr = (Stage)mainPane.getScene().getWindow();
			curr.close();
			Stage next = new Stage();
			try {
				new ManageItem().start(next);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		if (e.getSource() == Menuitems2) {
			Stage curr = (Stage)mainPane.getScene().getWindow();
			curr.close();
			Stage next = new Stage();
			try {
				new TransactionPage().start(next);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		else if (e.getSource() == Menuitems3) {
			Stage curr = (Stage)mainPane.getScene().getWindow();
			curr.close();
			Stage next = new Stage();
			try {
				new LoginPage().start(next);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
	}
}