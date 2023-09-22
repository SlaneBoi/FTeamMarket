package FTeamMarket;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Vector;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableSelectionModel;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class ManageItem extends Application implements EventHandler<ActionEvent>{
	String message = null;
	Scene scene;
	BorderPane mainPane;
	GridPane content, content2;
	TableView itemsTable;
	Label titleLbl, itemIdLbl, itemNameLbl, itemDescLbl,itemPriceLbl, itemQtyLbl;
	TextField idText, nameText, descText;
	MenuItem Menuitems, Menuitems1, Menuitems2, Menuitems3;
	Menu menu;
	MenuBar menubar;


	Spinner <Integer> priceSpinner, qtySpinner;
	Button insertbtn, deletebtn, updatebtn, clearbtn;
	FlowPane buttons;

	Vector<Item> items;

	Connect connect = Connect.getInstance();

	private String tempId = null;
	String generatedId = null;


	public void Initialize() {

		mainPane = new BorderPane();
		scene = new Scene(mainPane, 1050, 600);

		items = new Vector<Item>();
		content = new GridPane();
		content2 = new GridPane();

		itemsTable = new TableView<>();
		itemIdLbl = new Label("itemID");
		itemNameLbl = new Label("item name");
		itemDescLbl = new Label("item description");
		itemPriceLbl = new Label("price");
		itemQtyLbl = new Label("quantity");

		idText = new TextField();
		idText.setText(generateID());
		idText.setEditable(false);
		idText.setDisable(true);
		nameText = new TextField();
		descText = new TextField();

		priceSpinner = new Spinner<Integer>(0,Integer.MAX_VALUE,0,1000);
		qtySpinner = new Spinner<Integer>(0,Integer.MAX_VALUE, 0);

		insertbtn = new Button("Insert Item");
		updatebtn = new Button("Update Item");
		deletebtn = new Button("Delete Item");
		deletebtn.setDisable(true);

		insertbtn.setMinWidth(99);
		updatebtn.setMinWidth(99);
		deletebtn.setMinWidth(99);

		buttons = new FlowPane(insertbtn,updatebtn,deletebtn);
		clearbtn = new Button("Clear Form");

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

		insertbtn.setOnAction(this);
		Menuitems.setOnAction(this);
		Menuitems1.setOnAction(this);
		Menuitems2.setOnAction(this);
		Menuitems3.setOnAction(this);

		insertbtn.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				Alert alert = new Alert(AlertType.NONE);

				String id = idText.getText();
				String name = nameText.getText();
				String desc = descText.getText();
				Integer price = Integer.valueOf(priceSpinner.getValue());
				Integer qty = Integer.valueOf(qtySpinner.getValue());

				id = generatedId;


				if (validateItemData()) {	

					String query = "INSERT INTO item " + "VALUES('"+id+"', '"+name+"', '"+desc+"', '"+price+"', '"+qty+"')";
					connect.execUpdate(query);			
					alert.setAlertType(AlertType.INFORMATION);
					alert.setHeaderText("INSERT SUCCESS");
					alert.setContentText("Click OK to Continue...");
					alert.show();

				}
				else {

					alert.setAlertType(AlertType.INFORMATION);
					alert.setHeaderText("INSERT FAILED");
					alert.setContentText(message);
					alert.show();

				}
				refreshTable();
			}});


		updatebtn.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				// TODO Auto-generated method stub
				Alert alert = new Alert(AlertType.NONE);

				String id = idText.getText();
				String name = nameText.getText();
				String desc = descText.getText();
				Integer price = priceSpinner.getValue();
				Integer qty = qtySpinner.getValue();

				if(validateItemData()) {
					String query = "UPDATE item\n"
							+ "SET itemID = ?, itemName = ?, itemDescription = ?, price = ?, quantity = ?\n"
							+ "WHERE itemID = ?";
					PreparedStatement ps = connect.prepareStatement(query);

					try {
						ps.setString(1, id);
						ps.setString(2, name);
						ps.setString(3, desc);
						ps.setInt(4, price);
						ps.setInt(5, qty);
						ps.setString(6, id);
						ps.execute();
					} catch (Exception e1) {
						e1.printStackTrace();
					}

					alert.setAlertType(AlertType.INFORMATION);
					alert.setHeaderText("UPDATE SUCCESS");
					alert.setContentText("Click OK to continue");
					alert.showAndWait();

				}else {

					alert.setAlertType(AlertType.ERROR);
					alert.setHeaderText("UPDATE FAILED");
					alert.setContentText(message);
					alert.showAndWait();
				}	
				refreshTable();
			}		
		});


		deletebtn.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {

				Alert alert = new Alert(AlertType.NONE);

				String id = idText.getText();
				String name = nameText.getText();
				String desc = descText.getText();
				Integer price = priceSpinner.getValue();
				Integer qty = qtySpinner.getValue();

				if (nameText.getText().equals("")) {
					alert.setAlertType(AlertType.INFORMATION);
					alert.setHeaderText("DELETE FAILED");
					alert.setContentText("Make sure an item is selected!!");
					alert.showAndWait();		
				}
				else {
					String query = "DELETE FROM item\n"
							+ "WHERE itemID = ?";
					PreparedStatement ps = connect.prepareStatement(query);

					try {
						ps.setString(1, id);
						ps.execute();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

					alert.setAlertType(AlertType.INFORMATION);
					alert.setHeaderText("DELETE SUCCESS");
					alert.setContentText("Click OK to continue...");
					alert.showAndWait();

				}
				refreshTable();
			}

		});

		clearbtn.setOnMouseClicked(new EventHandler<MouseEvent>(){
			@Override
			public void handle(MouseEvent arg0) {
				
				updatebtn.setDisable(true);
				deletebtn.setDisable(true);
				insertbtn.setDisable(false);
				
				idText.setText(generateID());
				nameText.setText(null);
				descText.setText(null);
				priceSpinner.getValueFactory().setValue(0);
				qtySpinner.getValueFactory().setValue(0);
				refreshTable();
			}

		});


	}

	public void addComponents() {

		content.add(itemIdLbl, 0, 0);
		content.add(idText, 1, 0);

		content.add(itemNameLbl, 0, 1);
		content.add(nameText, 1, 1);

		content.add(itemDescLbl, 0, 2);
		content.add(descText, 1, 2);

		content.add(itemPriceLbl, 0, 3);
		content.add(priceSpinner, 1, 3);

		content.add(itemQtyLbl, 0, 4);
		content.add(qtySpinner, 1, 4);

		content.add(buttons, 0, 5, 2, 1);


		content.add(clearbtn, 0, 6, 2, 1);

		clearbtn.setMinWidth(320);

		content.getColumnConstraints().add(new ColumnConstraints(100));

		content2.add(itemsTable, 0, 0);
		content2.add(content, 1, 0);

		content2.getColumnConstraints().add(new ColumnConstraints(620));
		content2.getColumnConstraints().add(new ColumnConstraints(430));

		itemsTable.setMinHeight(575);

	}

	public void Positioning() {

		content.setPadding(new Insets(30, 50, 50, 50));
		content.setHgap(10);
		content.setVgap(10);

		mainPane.setTop(menubar);
		mainPane.setCenter(content2);
		buttons.setHgap(10);

	}


	public void setUpTable() {

		TableColumn<Item, String> idColumn = new TableColumn<>("itemID");
		idColumn.setResizable(false);
		idColumn.setMinWidth(mainPane.getWidth()/8.5);
		idColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("id"));

		TableColumn<Item, String> nameColumn = new TableColumn<>("item name");
		nameColumn.setResizable(false);
		nameColumn.setMinWidth(mainPane.getWidth()/8.5);
		nameColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("name"));

		TableColumn<Item, String> descColumn = new TableColumn<>("item description");
		descColumn.setResizable(false);
		descColumn.setMinWidth(mainPane.getWidth()/8.5);
		descColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("desc"));

		TableColumn<Item, String> priceColumn = new TableColumn<>("price");
		priceColumn.setResizable(false);
		priceColumn.setMinWidth(mainPane.getWidth()/8.5);
		priceColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("price"));

		TableColumn<Item, String> qtyColumn = new TableColumn<>("quantity");
		qtyColumn.setResizable(false);
		qtyColumn.setMinWidth(mainPane.getWidth()/8.5);
		qtyColumn.setCellValueFactory(new PropertyValueFactory<Item, String>("quantity"));

		itemsTable.getColumns().addAll(idColumn, nameColumn, descColumn, priceColumn, qtyColumn);
		itemsTable.setOnMouseClicked(tableMouseEvent());

		refreshTable();
	}

	private EventHandler <MouseEvent> tableMouseEvent(){
		return new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
				TableSelectionModel<Item> tableSelectionModel = itemsTable.getSelectionModel();
				tableSelectionModel.setSelectionMode(SelectionMode.SINGLE);

				Item items = tableSelectionModel.getSelectedItem();
				insertbtn.setDisable(true);
				deletebtn.setDisable(false);

				idText.setText(items.getId());
				nameText.setText(items.getName());
				descText.setText(items.getDesc());
				priceSpinner.getValueFactory().setValue(items.getPrice());;
				qtySpinner.getValueFactory().setValue(items.getQuantity());

				tempId = items.getId();
			}

		};

	}

	private void getData() {
		items.removeAllElements();

		String query = "SELECT * FROM item";
		connect.rs = connect.execQuery(query);

		try {
			while(connect.rs.next()) {
				String id = connect.rs.getString("itemID");
				String name = connect.rs.getString("itemName");
				String desc = connect.rs.getString("itemDescription");
				Integer price = connect.rs.getInt("price");
				Integer qty = connect.rs.getInt("quantity");
				items.add(new Item(id,name, desc, price, qty));
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void refreshTable() {
		String lastId = "", nextId = "";		
		getData();
		ObservableList<Item> itemObs = FXCollections.observableArrayList(items);
		itemsTable.setItems(itemObs);

		if(!items.isEmpty()) {
			lastId = items.get(items.size()-1).getId();
			nextId = lastId.substring(2,5);

			Item.setCount(Integer.valueOf(nextId)+1);
		}	
	}

	@Override
	public void start(Stage stg) throws Exception {
		Initialize();
		addComponents();
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
	public boolean validateItemData() {

		boolean validateall=false; 
		boolean nameValidation=false; 
		boolean descValidation=false;
		boolean priceValidation=false;
		boolean qtyValidation=false;

		String name = nameText.getText();
		String desc = descText.getText();
		int price = priceSpinner.getValue();
		int qty = qtySpinner.getValue();

		if(name.length()>=5 && name.length()<=100) {
			if(name.contains(":") && name.contains(" ")) {
				nameValidation = true;
			}else {
				message = "item name must consist of at least 2 words containing the game name and item name then separated by (:)";
			}
		}else {
			message = ("item name must be between 5-100 characters!");
		}

		if(desc.length()>=10 && desc.length()<=200) {
			descValidation = true;
		}else {
			message = "Item Description must be between 10-200 characters.";
		}

		if(price>0) {
			priceValidation = true;
		}
		else {
			message = "Price must be greater than 0!";
		}

		if(qty>0) {
			qtyValidation = true;

		}
		else {
			message = "Quantity must be greater than 0!";
		}

		if(nameValidation && descValidation && priceValidation && qtyValidation) {
			validateall = true;
		}

		return validateall;
	}

	public String generateID() {
		String lastId="", nextId="";

		String query = "SELECT * FROM item";
		connect.rs = connect.execQuery(query);

		try {
			while(connect.rs.next()) {
				String itemID = connect.rs.getString("itemID");
				String itemName = connect.rs.getString("itemName");
				String itemDescription = connect.rs.getString("itemDescription");
				Integer price = connect.rs.getInt("price");
				Integer quantity = connect.rs.getInt("quantity");
				items.add(new Item(itemID, itemName, itemDescription, price, quantity));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		if(!items.isEmpty()) {
			lastId = items.get(items.size()-1).getId();
			nextId = lastId.substring(2,5);

			User.setCount(Integer.valueOf(nextId)+1);
		}
		String id="IT";
		int x = Integer.parseInt(nextId)+1;

		if(x < 10) {
			id += "00";
		}else if(x >= 10 && x<100) {
			id += "0";
		}
		id+=Integer.toString(x);

		generatedId = id;

		return id;
	}
}