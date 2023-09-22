package FTeamMarket;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Properties;
import java.util.Vector;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class ItemMarket extends Application {

	Connect conn = Connect.getInstance();
	int addedQuantity = 0;
	Stage stage;
	String generatedId;
	Scene scene;
	BorderPane Mainpane;
	GridPane addCart, content;
	TableView<Item> itemView;
	Label idLbl, nameLbl, descLbl, priceLbl, qtyLbl;
	TextField idTxt, nameTxt, descTxt;
	Menu menu;
	MenuBar menubar;
	MenuItem Menuitems, Menuitems1, Menuitems2, Menuitems3;
	Spinner<Integer> priceSpinner;
	Spinner<Integer> qtySpinner;
	Button clearFormBtn, addCartBtn;
	FlowPane btnFlow;
	ArrayList<Item> items = new ArrayList<>();
	ArrayList<Item> cart = new ArrayList<>();
	
	public void initialize() {
		Mainpane = new BorderPane();
		addCart = new GridPane();
		content = new GridPane();
		
		idLbl = new Label("Item ID");
		nameLbl = new Label("Item Name");
		descLbl = new Label("Item Description");
		priceLbl = new Label("Price");
		qtyLbl = new Label("Quantity");
		
		
		idTxt = new TextField();
		nameTxt = new TextField();
		descTxt = new TextField();
		
		Menuitems = new MenuItem("Item Market");
		Menuitems1 = new MenuItem("Cart Item");
		Menuitems2 = new MenuItem("Transaction History");
		Menuitems3 = new MenuItem("Logout");
		
		menu = new Menu("Menu");
		menubar = new MenuBar();
		
		priceSpinner = new Spinner<>(0, 1000000, 0);
		qtySpinner = new Spinner<>(0, 100, 0);
		
		itemView = new TableView<>();
		
		clearFormBtn = new Button("Clear Form");
		addCartBtn = new Button("Add to Cart");
		
		btnFlow = new FlowPane();
		
		scene = new Scene(Mainpane, 1050, 600);
		
	}
	
	public void addItems() {
		btnFlow.getChildren().add(clearFormBtn);
		btnFlow.getChildren().add(addCartBtn);
		
		menu.getItems().add(Menuitems);
		menu.getItems().add(Menuitems1);
		menu.getItems().add(Menuitems2);
		menu.getItems().add(Menuitems3);
		menubar.getMenus().add(menu);
		
		addCart.add(idLbl, 0, 0);
		addCart.add(idTxt, 1, 0);
		addCart.add(nameLbl, 0, 1);
		addCart.add(nameTxt, 1, 1);
		addCart.add(descLbl, 0, 2);
		addCart.add(descTxt, 1, 2);
		addCart.add(priceLbl, 0, 3);
		addCart.add(priceSpinner, 1, 3);
		addCart.add(qtyLbl, 0, 4);
		addCart.add(qtySpinner, 1, 4);
		addCart.add(btnFlow, 0, 5, 3, 1);
		
		Mainpane.setTop(menubar);
		Mainpane.setCenter(content);
	}
	
	public void arrangeItems() {
		addCart.setPadding(new Insets(50));
		addCart.setVgap(10);
		addCart.setHgap(10);
		btnFlow.setHgap(10);
		
		idTxt.setMaxWidth(150);
		nameTxt.setMaxWidth(150);
		descTxt.setMaxWidth(150);
		
		idTxt.setPromptText(generateID());
		
		idTxt.setDisable(true);
		nameTxt.setDisable(true);
		descTxt.setDisable(true);
		priceSpinner.setDisable(true);
		
		if (nameTxt.getText().isEmpty()) {
			clearFormBtn.isDisabled();
			addCartBtn.isDisabled();
		}
		
		idTxt.setPromptText(generatedId);
		
	}
	
	public void setUpTable() {
		itemView.setMinWidth(550);
		double width = itemView.getMinWidth() / 5;
		
		TableColumn<Item, String> idColumn = new TableColumn("Item ID");
		idColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("ItemID"));
		idColumn.setMinWidth(width);
		
		TableColumn<Item, String> nameColumn = new TableColumn("Item Name");
		nameColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("ItemName"));
		nameColumn.setMinWidth(width);
		
		TableColumn<Item, String> descColumn = new TableColumn("Item Description");
		descColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("ItemDescription"));
		descColumn.setMinWidth(width);
		
		TableColumn<Item, Integer> priceColumn = new TableColumn("Price");
		priceColumn.setCellValueFactory(new PropertyValueFactory<Item, Integer>("price"));
		priceColumn.setMinWidth(width);
		
		TableColumn<Item, Integer> qtyColumn = new TableColumn("Quantity");
		qtyColumn.setCellValueFactory(new PropertyValueFactory<Item, Integer>("quantity"));
		qtyColumn.setMinWidth(width);
		
		itemView.getColumns().addAll(idColumn, nameColumn, descColumn, priceColumn, qtyColumn);
		itemView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		
		refreshTable();
		
	}
	
	public void getData() {
		items.clear();
		
		String query = "SELECT * FROM item WHERE quantity > 0";
		conn.rs = conn.execQuery(query);
		
		try {
			while(conn.rs.next()) {
				String itemID = conn.rs.getString("itemid");
				String itemName = conn.rs.getString("itemname");
				String itemDesc = conn.rs.getString("itemdescription");
				int itemPrice = conn.rs.getInt("price");
				int itemQty = conn.rs.getInt("quantity");
				
				items.add(new Item(itemID,itemName,itemDesc,itemPrice,itemQty));
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void refreshTable() {
		getData();
		ObservableList<Item> itemobs = FXCollections.observableArrayList(items);
		itemView.setItems(itemobs);

	}
	
	public void action() {
		
		itemView.setOnMouseClicked(e ->{
			String id = itemView.getSelectionModel().getSelectedItem().getId();
			String name = itemView.getSelectionModel().getSelectedItem().getName();
			String desc = itemView.getSelectionModel().getSelectedItem().getDesc();
			int price = itemView.getSelectionModel().getSelectedItem().getPrice();
			int qty = itemView.getSelectionModel().getSelectedItem().getQuantity();
			
			idTxt.setText(id);
			nameTxt.setText(name);
			descTxt.setText(desc);
			priceSpinner.getValueFactory().setValue(price);
			priceSpinner.setEditable(false);
			
		});
		
		
		addCartBtn.setOnAction(e -> {
			int cartQuantity = qtySpinner.getValue();
			String userID = LoginPage.currUser.getUserId();
			
			if (itemView.getSelectionModel().getSelectedItem() != null){
				String id = idTxt.getText();
				String name = nameTxt.getText();
				String desc = descTxt.getText();
				int price = priceSpinner.getValue();
				int quantity = itemView.getSelectionModel().getSelectedItem().getQuantity();
				boolean duplicateItem = false;
				
				String query = String.format("SELECT * FROM cart WHERE itemid = '%s' AND userid = '%s'", id, userID);
				String query2 = String.format("SELECT quantity FROM cart WHERE itemid = '%s' AND userid = '%s'", id, userID);
				conn.execQuery(query);
				try {
					if (conn.rs.next()) {
						addedQuantity = conn.rs.getInt("quantity");
						duplicateItem = true;
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				
				if (cartQuantity < 1) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setHeaderText("ERROR");
					alert.setContentText("Quantity must be greater than 0!");
					alert.showAndWait();
				} else if (cartQuantity > quantity) {
					Alert alert = new Alert(AlertType.ERROR);
					alert.setHeaderText("ERROR");
					alert.setContentText("The quantity you input cannot exeed the existing quantity!");
					alert.showAndWait();
				} else if (duplicateItem == true) {
							int finalQty = cartQuantity + addedQuantity;
							int totalPrice = finalQty * price;
							String sql = String.format("UPDATE cart SET quantity = '%s' WHERE itemid = '%s' AND userid = '%s'", finalQty , id, userID);
							
							conn.execUpdate(sql);
							
							Alert alert = new Alert(AlertType.INFORMATION);
							alert.setHeaderText("SUCCESS");
							alert.setContentText("Successfully updated same item in cart, click OK to continue");
							alert.showAndWait();
							
						} else {
							int finalQty = cartQuantity + addedQuantity;
							String sql = String.format("INSERT INTO cart VALUES ('%s', '%s', '%d')", id, userID, finalQty);
							conn.execUpdate(sql);
							
							Alert alert = new Alert(AlertType.INFORMATION);
							alert.setHeaderText("SUCCESS");
							alert.setContentText("Successfully added item to cart, click OK to continue");
							alert.showAndWait();
						}
				
			}
		});
		
		clearFormBtn.setOnAction(e ->{
			idTxt.setText(generatedId);
			nameTxt.clear();
			descTxt.clear();
			priceSpinner.getValueFactory().setValue(0);
			qtySpinner.getValueFactory().setValue(0);
		});
		
		Menuitems1.setOnAction(e -> {
			Stage curr = (Stage) Mainpane.getScene().getWindow();
			curr.close();
			Stage next = new Stage();
			try {
				new CartItem().start(next);
			}catch(Exception e1) {
				e1.printStackTrace();
			}
		});
	}
	
	
	public String generateID() {
		String lastId = "", nextId = "";
		
		String query = "SELECT * FROM Item";
		conn.rs = conn.execQuery(query);
		
		try {
			while(conn.rs.next()) {
				String id = conn.rs.getString("itemID");
				String itemName = conn.rs.getString("itemname");
				String itemDescription = conn.rs.getString("itemdescription");
				Integer itemPrice = conn.rs.getInt("price");
				Integer itemQuantity = conn.rs.getInt("quantity");
				items.add(new Item(id, itemName, itemDescription, itemPrice, itemQuantity));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if(!items.isEmpty()) {
			lastId = items.get(items.size()-1).getId();
			nextId = lastId.substring(2,5);
			System.out.println(nextId);
			User.setCount(Integer.valueOf(nextId)+1);
		}
		
		String id="IT";
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
		this.stage = stage;
		initialize();
		addItems();
		arrangeItems();
		setUpTable();
		action();
		this.stage.setTitle("user page");
		this.stage.setScene(scene);
		this.stage.show();
	}			
}