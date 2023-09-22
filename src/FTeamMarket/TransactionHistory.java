package FTeamMarket;
import java.sql.PreparedStatement;
import java.util.Date;
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

public class TransactionHistory extends Application implements EventHandler<ActionEvent>{

	Scene scene;
	BorderPane mainPane;
	TableView transactionTable;
	Vector<Transaction> transactionHistories;
	
	Menu menu;
	MenuBar menubar;
	MenuItem Menuitems, Menuitems1, Menuitems2, Menuitems3;
	
	Connect connect = Connect.getInstance();
	
	public void Initialize() {	
		
		transactionHistories = new Vector<Transaction>();
		mainPane = new BorderPane();
		scene = new Scene(mainPane,1050,600);
		transactionTable = new TableView<>();	
		
		Menuitems = new MenuItem("Item Market");
		Menuitems1 = new MenuItem("Cart Item");
		Menuitems2 = new MenuItem("Transaction History");
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
		transactionIdColumn.setMinWidth(mainPane.getWidth()/7.02);		
		
		TableColumn<Transaction, String> itemNameColumn = new TableColumn<>("Item Name");
		itemNameColumn.setCellValueFactory(new PropertyValueFactory<Transaction, String>("itemName"));
		itemNameColumn.setResizable(false);
		itemNameColumn.setMinWidth(mainPane.getWidth()/7);
			
		TableColumn<Transaction, String> itemDescColumn = new TableColumn<>("Item Description");
		itemDescColumn.setCellValueFactory(new PropertyValueFactory<Transaction, String>("itemDesc"));
		itemDescColumn.setResizable(false);
		itemDescColumn.setMinWidth(mainPane.getWidth()/7);
		
		TableColumn<Transaction, Integer> itemPriceColumn = new TableColumn<>("Price");
		itemPriceColumn.setCellValueFactory(new PropertyValueFactory<Transaction, Integer>("itemPrice"));
		itemPriceColumn.setResizable(false);
		itemPriceColumn.setMinWidth(mainPane.getWidth()/7.02);	
		
		TableColumn<Transaction, Integer> itemQtyColumn = new TableColumn<>("Quantity");
		itemQtyColumn.setCellValueFactory(new PropertyValueFactory<Transaction, Integer>("itemQty"));
		itemQtyColumn.setResizable(false);	
		itemQtyColumn.setMinWidth(mainPane.getWidth()/7.02);
		
		TableColumn<Transaction, Integer> itemTotalPriceColumn = new TableColumn<>("Total Price");
		itemTotalPriceColumn.setCellValueFactory(new PropertyValueFactory<Transaction, Integer>("itemTotalPrice"));
		itemTotalPriceColumn.setResizable(false);
		itemTotalPriceColumn.setMinWidth(mainPane.getWidth()/7);
				
		TableColumn<Transaction, String> transactionDateColumn = new TableColumn<>("transaction date");
		transactionDateColumn.setCellValueFactory(new PropertyValueFactory<Transaction, String>("transactionDate"));
		transactionDateColumn.setResizable(false);		
		transactionDateColumn.setMinWidth(mainPane.getWidth()/7);
		
		transactionTable.getColumns().addAll(transactionIdColumn, itemNameColumn, itemDescColumn, itemPriceColumn, itemQtyColumn, itemTotalPriceColumn, transactionDateColumn);
		transactionTable.setMaxHeight(600);
		
		refreshTable();
	}
	
	
	private void getData() {
		
		transactionHistories.removeAllElements();
		String LoggedID = "";	
		LoggedID = LoginPage.currUser.getUserId(); 
		System.out.println(LoggedID);

		String query = "SELECT t.transactionID, i.itemName, i.itemDescription, i.price, i.price * td.quantity AS 'totalPrice', td.quantity, t.transactionDate "
				+ "FROM transaction t JOIN transactiondetail td ON t.transactionID = td.transactionID "
				+ "JOIN user u ON t.userID = u.userID "
				+ "JOIN item i ON td.itemID = i.itemID \n"
				+ "WHERE t.userID = '"+ LoggedID+"'";

		connect.rs = connect.execQuery(query);

		try {
			while(connect.rs.next()) {
				String transactionID = connect.rs.getString("transactionID");
				String itemName = connect.rs.getString("itemName");
				String itemDescription = connect.rs.getString("itemDescription");
				Integer itemPrice = connect.rs.getInt("price");
				Integer itemQty = connect.rs.getInt("quantity");
				Integer itemTotalPrice = connect.rs.getInt("totalPrice");
				Date transactionDate = connect.rs.getDate("transactionDate");
				transactionHistories.add(new Transaction(transactionID, itemName, itemDescription, itemPrice, itemQty, itemTotalPrice, transactionDate));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	private void refreshTable() {	
		getData();
		ObservableList<Transaction> transactionobs = FXCollections.observableArrayList(transactionHistories);
		transactionTable.setItems(transactionobs);
		}	

	@Override
	public void start(Stage stg) throws Exception {
		Initialize();
		Positioning();
		setUpTable();
		stg.setTitle("user page");
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
				new ItemMarket().start(next);
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
				new CartItem().start(next);
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
				new TransactionHistory().start(next);
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