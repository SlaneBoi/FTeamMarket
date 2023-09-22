package FTeamMarket;

import java.util.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import javax.security.auth.login.FailedLoginException;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class CartItem extends Application{

	Scene scene;
	Alert alert = new Alert(AlertType.NONE);
	int qty = 0;
	Connect conn = Connect.getInstance();
	BorderPane Mainpane;
	TableView cartView;
	ArrayList<Cart> carts = new ArrayList<>();
	ArrayList<Transaction> transactions = new ArrayList<>();
	Button removeBtn, checkoutBtn;
	Menu menu;
	FlowPane btnFlow;
	MenuBar menubar;
	MenuItem Menuitems, Menuitems1, Menuitems2, Menuitems3;
	String itemID = "", itemName, itemDescription, generatedId, buyerid;
	int itemPrice, itemQty, totalPrice;
	String tempID;
	java.sql.Date tdate;
	
	
	public void initialize() {
		Mainpane = new BorderPane();
		cartView = new TableView<>();
		
		removeBtn = new Button("Remove From Cart");
		removeBtn.setDisable(true);
		checkoutBtn = new Button("Checkout");
		
		btnFlow = new FlowPane();
		
		Menuitems = new MenuItem("Item Market");
		Menuitems1 = new MenuItem("Cart Item");
		Menuitems2 = new MenuItem("Transaction History");
		Menuitems3 = new MenuItem("Logout");
		
		menu = new Menu("Menu");
		menubar = new MenuBar();
		
		scene = new Scene(Mainpane, 1050, 600);
	}
	
	public void setUpTable() {
	double width = cartView.getMinWidth() / 6;
		
		TableColumn<Cart, String> idColumn = new TableColumn<>("ItemID");
		idColumn.setCellValueFactory(new PropertyValueFactory<Cart, String>("ItemID"));
		idColumn.setMinWidth(width);
		
		TableColumn<Cart, String> nameColumn = new TableColumn<>("Item Name");
		nameColumn.setCellValueFactory(new PropertyValueFactory<Cart, String>("ItemName"));
		nameColumn.setMinWidth(width);
		
		TableColumn<Cart, String> descColumn = new TableColumn<>("Item Description");
		descColumn.setCellValueFactory(new PropertyValueFactory<Cart, String>("ItemDescription"));
		descColumn.setMinWidth(width);
		
		TableColumn<Cart, Integer> priceColumn = new TableColumn<>("Price");
		priceColumn.setCellValueFactory(new PropertyValueFactory<Cart, Integer>("price"));
		priceColumn.setMinWidth(width);
		
		TableColumn<Cart, Integer> qtyColumn = new TableColumn<>("Quantity");
		qtyColumn.setCellValueFactory(new PropertyValueFactory<Cart, Integer>("quantity"));
		qtyColumn.setMinWidth(width);
		
		TableColumn<Cart, Integer> totalPriceColumn = new TableColumn<>("Total Price");
		totalPriceColumn.setCellValueFactory(new PropertyValueFactory<Cart, Integer>("totalprice"));
		totalPriceColumn.setMinWidth(width);
		
		cartView.getColumns().addAll(idColumn, nameColumn, descColumn, priceColumn, qtyColumn, totalPriceColumn);
		cartView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		
		refreshData();
	}
	
	public void getData() {
		carts.clear();
		
		String query = "SELECT c.itemid, i.itemname, i.itemdescription, i.price, c.quantity, c.quantity * i.price AS totalprice \r\n"
				+ "FROM cart c \r\n"
				+ "JOIN user u ON c.userid = u.userid JOIN item i ON c.itemid = i.itemid;";
		conn.rs = conn.execQuery(query);
		
		try {
			while (conn.rs.next()) {
				String id = conn.rs.getString("itemid");
				String name = conn.rs.getString("itemname");
				String desc = conn.rs.getString("itemdescription");
				int price = conn.rs.getInt("price");
				int qty = conn.rs.getInt("quantity");
				int totalprice = conn.rs.getInt("totalprice");
				
				carts.add(new Cart(id, LoginPage.currUser.getUserId() ,name, desc, price, qty, totalprice));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void refreshData() {
		getData();
		ObservableList<Cart> cartobs = FXCollections.observableArrayList(carts);
		cartView.setItems(cartobs);
	}
	
	public void addComponents() {
		btnFlow.getChildren().add(removeBtn);
		btnFlow.getChildren().add(checkoutBtn);
		
		menu.getItems().add(Menuitems);
		menu.getItems().add(Menuitems1);
		menu.getItems().add(Menuitems2);
		menu.getItems().add(Menuitems3);
		menubar.getMenus().add(menu);
		
		
		Mainpane.setTop(menubar);
		Mainpane.setCenter(cartView);
		Mainpane.setBottom(btnFlow);
		
	}
	
	public void arrageComponents() {
		btnFlow.setHgap(10);
		btnFlow.setMinWidth(500);
		btnFlow.setAlignment(Pos.CENTER);
		
		checkoutBtn.setMinWidth(110);
		removeBtn.setMinWidth(110);
		
		Mainpane.setMargin(btnFlow, new Insets(70, 0 , 20, 0));
		
		if (carts.isEmpty()) {
			checkoutBtn.setDisable(true);
			removeBtn.setDisable(true);
		}
	}
	
	public void action() {
		cartView.setOnMouseClicked(e -> {
			getTableItems();
			removeBtn.setDisable(false);
			checkoutBtn.setDisable(false);
			tempID = itemID;
		});
		
		removeBtn.setOnAction(e -> {
			getTableItems();
			String query = String.format("DELETE FROM `cart` WHERE itemid = '%s' AND userid = '%s'", itemID, LoginPage.currUser.getUserId());
			conn.execUpdate(query);
			refreshData();
			cartView.getSelectionModel().getSelectedItems().clear();
			carts.clear();
			
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setHeaderText("Success");
			alert.setContentText("Successfully remove items, click OK to continue");
			alert.showAndWait();
		});
		
		checkoutBtn.setOnAction(e -> {
			System.out.println("Checking out");
			
			String query = String.format("SELECT td.quantity FROM transactiondetail td "
										+ "JOIN transaction t ON td.transactionID = t.transactionID JOIN user u ON u.userID = t.userID "
										+ "WHERE t.userID = '%s'", LoginPage.currUser.getUserId());
			conn.execQuery(query);
			
			try {
				while(conn.rs.next()) {
					qty = conn.rs.getInt("quantity");
				}
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			if (itemQty > qty) {
				alert.setAlertType(AlertType.ERROR);
				alert.setHeaderText("error");
				Label lb = new Label("the item you ordered " + itemName + " with the itemID " + itemID + " is out of stock");
				lb.setWrapText(true);
				alert.getDialogPane().setContent(lb);
				alert.showAndWait();
			} else {
				LocalDate dateObj = LocalDate.now();
				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
				String tdate = dateObj.format(formatter);
				
				String sql = String.format("INSERT INTO transaction VALUES ('%s', '%s', '%s')", generatedId, buyerid, tdate);
				conn.execUpdate(sql);
				
				String query2 = String.format("SELECT quantity FROM item WHERE itemID = '%s'", tempID);
				conn.execQuery(query2);
				try {
								while(conn.rs.next()) {
									itemQty = conn.rs.getInt("quantity");
								}
							} catch (SQLException e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
				
				String query3 = String.format("UPDATE item SET quantity = %d WHERE itemID = '%s'", itemQty - qty, tempID);
				conn.execUpdate(query3);

				
				String query1 = String.format("DELETE FROM `cart` WHERE itemid = '%s' AND userid = '%s'", tempID, LoginPage.currUser.getUserId());
				conn.execUpdate(query1);
				
				refreshData();
				
				alert.setAlertType(AlertType.INFORMATION);
				alert.setHeaderText("SUCCESS");
				Label lb1 = new Label("Successfully checked out with the items with the item name " + itemName + " and itemID " + itemID + ". click OK to continue");
				lb1.setWrapText(true);
				alert.getDialogPane().setContent(lb1);
				alert.showAndWait();
			}
			
		});
		
		Menuitems.setOnAction(e -> {
			Stage curr = (Stage) Mainpane.getScene().getWindow();
			curr.close();
			Stage next = new Stage();
			try {
				new ItemMarket().start(next);
			}catch(Exception e1) {
				e1.printStackTrace();
			}
		});
	}
	
	
	public void getTableItems() {
		itemID = ((Cart) cartView.getSelectionModel().getSelectedItem()).getItemID();
		itemName = ((Cart) cartView.getSelectionModel().getSelectedItem()).getItemName();;
		itemDescription = ((Cart) cartView.getSelectionModel().getSelectedItem()).getItemDescription();
		itemPrice = ((Cart) cartView.getSelectionModel().getSelectedItem()).getPrice();
		itemQty = ((Cart) cartView.getSelectionModel().getSelectedItem()).getQuantity();
		totalPrice = ((Cart) cartView.getSelectionModel().getSelectedItem()).getTotalprice();
	}
	
	public String generateId() {
		String lastId = "", nextId = "";

		String query = "SELECT * FROM transaction";
		conn.rs = conn.execQuery(query);

		try {
			while(conn.rs.next()) {
				String id = conn.rs.getString("transactionID");
				buyerid = conn.rs.getString("userID");
				tdate = conn.rs.getDate("transactionDate");
				transactions.add(new Transaction(id, buyerid, tdate));
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		if(!transactions.isEmpty()) {
			lastId = transactions.get(transactions.size()-1).getBuyerID();
			nextId = lastId.substring(2,5);
			System.out.println(nextId);
			User.setCount(Integer.valueOf(nextId)+1);
		}
		
		String id="TR";
		int x = Integer.parseInt(nextId)+1;

		if(x < 10) {
			id += "00";
		}else if(x >= 10 && x < 100) {
			id += "0";
		}
		id+=Integer.toString(x);

		generatedId = id;
		
		return id;
	}

	@Override
	public void start(Stage stage) throws Exception {
		// TODO Auto-generated method stub
		initialize();
		setUpTable();
		addComponents();
		arrageComponents();
		action();
		stage.setTitle("user page");
		stage.setScene(scene);
		stage.show();
	}

}