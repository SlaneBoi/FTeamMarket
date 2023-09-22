package FTeamMarket;

import java.sql.PreparedStatement;
import java.util.Vector;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Spinner;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableSelectionModel;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ManageUser extends Application implements EventHandler<ActionEvent>{

	Connect connect = Connect.getInstance();
	String updateError = null;

	Scene scene;
	BorderPane Mainpane;

	TableView userTable;
	GridPane content, centerBox;
	FlowPane genderPane, btnPane;

	Text titleLbl, userIdLbl, unameLbl, passLbl, emailLbl, phoneNumberLbl, ageLbl, genderLbl;
	TextField userIdField, unameField, emailField, phoneNumberField;
	PasswordField passField;
	Spinner<Integer> ageSpinner;
	RadioButton maleBtn, femaleBtn;
	ToggleGroup genderToggle;
	Button updateBtn, deleteBtn;
	MenuItem Menuitems, Menuitems1, Menuitems2, Menuitems3;
	Menu menu;
	MenuBar menubar;

	Vector<User> users;
	
	public void Initialize() {	
		
		Mainpane = new BorderPane();
		content = new GridPane();
		centerBox = new GridPane();
		scene = new Scene(Mainpane, 1050, 600);

		users = new Vector<User>();
		userTable = new TableView<>();
		userTable.setMinHeight(700);

		titleLbl = new Text("Register");
		userIdLbl = new Text("User ID");
		unameLbl = new Text("Username");
		passLbl = new Text("Password");
		emailLbl = new Text("Email");
		phoneNumberLbl = new Text("Phone Number");
		ageLbl = new Text("Age");
		genderLbl = new Text("Gender");

		userIdField = new TextField();
		userIdField.setDisable(true);

		unameField = new TextField();
		unameField.setDisable(true);
		
		passField = new PasswordField();
		passField.setDisable(true);
		
		emailField = new TextField();
		phoneNumberField = new TextField();

		ageSpinner = new Spinner<Integer>(15,70,16);
		maleBtn = new RadioButton("Male ");
		femaleBtn = new RadioButton("Female ");
		genderPane = new FlowPane(maleBtn, femaleBtn);

		genderToggle = new ToggleGroup();
		genderToggle.getToggles().addAll(maleBtn, femaleBtn);

		updateBtn = new Button("Update User");
		deleteBtn = new Button("Delete User");
		btnPane = new FlowPane(updateBtn, deleteBtn);
		btnPane.setHgap(10);
		
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
		
		
	updateBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {

			Alert alert = new Alert(AlertType.NONE);
			@Override
			public void handle(MouseEvent arg0) {
	
				if(userIdField.getText().equals("")) {
					
					alert.setAlertType(AlertType.ERROR);
					alert.setHeaderText("UPDATE FAILED");
					alert.setContentText("Please select the data you want to update!");
					alert.showAndWait();
				}
						
				else if(validateUpdate()) {
					String query = "UPDATE user\n"
							+ "SET  email = ?, phoneNumber = ?, age = ?, gender = ?\n"
							+ "WHERE userID =?";
					PreparedStatement ps = connect.prepareStatement(query);
					try {
						ps.setString(1, emailField.getText());
						ps.setString(2, phoneNumberField.getText());
						ps.setInt(3, ageSpinner.getValue());
						ps.setString(4, genderSelected());
						ps.setString(5, userIdField.getText());
						ps.execute();
					} catch (Exception e2) {
						e2.printStackTrace();
					}
					
					Alert updateAlert = new Alert(AlertType.INFORMATION);
					updateAlert.setHeaderText("UPDATE SUCCESS");
					updateAlert.setContentText("Click OK to continue");
					updateAlert.showAndWait();
					
				}else {
		
					Alert updateAlert = new Alert(AlertType.INFORMATION);
					updateAlert.setHeaderText("UPDATE FAILED");
					updateAlert.setContentText(updateError);
					updateAlert.showAndWait();
				}
				
				refreshTable();
				refreshAllValue();
				
			}
		});
				
		deleteBtn.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent arg0) {
	
				if(userIdField.getText().equals("")) {
					
					Alert deleteAlert = new Alert(AlertType.ERROR);
					deleteAlert.setHeaderText("DELETE ERROR");
					deleteAlert.setContentText("Make sure User is selected!");
					deleteAlert.showAndWait();
					
				}
				
				else {
				String query = "DELETE FROM user\n"
						+ "WHERE userID = ?";
				PreparedStatement ps = connect.prepareStatement(query);

				try {
					ps.setString(1, userIdField.getText());
					ps.execute();
				}catch (Exception e2) {
					// TODO: handle exception
				}
				
				Alert deleteAlert = new Alert(AlertType.INFORMATION);
				deleteAlert.setHeaderText("DELETE SUCCESS");
				deleteAlert.setContentText("Click OK to continue");
				deleteAlert.showAndWait();
				}		
				
			refreshTable();
			refreshAllValue();		
			
			}			
		});
	}
	
	public void addComponents() {
		
		content.add(userIdLbl, 0, 0);
		content.add(userIdField, 1, 0);

		content.add(unameLbl, 0, 1);
		content.add(unameField, 1, 1);

		content.add(passLbl, 0, 2);
		content.add(passField, 1, 2);

		content.add(emailLbl, 0, 3);
		content.add(emailField, 1, 3);

		content.add(phoneNumberLbl, 0, 4);
		content.add(phoneNumberField, 1, 4);

		content.add(ageLbl, 0, 5);
		content.add(ageSpinner, 1, 5);

		content.add(genderLbl, 0, 6);
		content.add(genderPane, 1, 6);

		content.add(btnPane, 1, 7);
		
		centerBox.add(userTable, 0, 0);
		centerBox.add(content, 1, 0);
	
		
		
		updateBtn.setOnAction(this);
		deleteBtn.setOnAction(this);
		
		
		
	}
	
	public void Positioning() {
		
		content.setPadding(new Insets(20));
		content.setHgap(10);
		content.setVgap(10);

		centerBox.getColumnConstraints().add(new ColumnConstraints(700));
		centerBox.getColumnConstraints().add(new ColumnConstraints(350));
		
		Mainpane.setCenter(centerBox);
		Mainpane.setTop(menubar);
	}
	
	public void createTable() {
		TableColumn<User, String> idCol = new TableColumn<>("userID");
		idCol.setCellValueFactory(new PropertyValueFactory<User, String>("userId"));
		idCol.setResizable(false);
		idCol.setMinWidth(Mainpane.getWidth()/10.5);

		TableColumn<User, String> usernameCol = new TableColumn<>("username");
		usernameCol.setCellValueFactory(new PropertyValueFactory<User, String>("username"));
		usernameCol.setResizable(false);
		usernameCol.setMinWidth(Mainpane.getWidth()/10.5);

		TableColumn<User, String> passwordCol = new TableColumn<>("password");
		passwordCol.setCellValueFactory(new PropertyValueFactory<User, String>("password"));
		passwordCol.setResizable(false);
		passwordCol.setMinWidth(Mainpane.getWidth()/10.5);
		
		TableColumn<User, String> emailCol = new TableColumn<>("email");
		emailCol.setCellValueFactory(new PropertyValueFactory<User, String>("email"));
		emailCol.setResizable(false);
		emailCol.setMinWidth(Mainpane.getWidth()/10.5);

		TableColumn<User, String> phoneNumberCol = new TableColumn<>("phone number");
		phoneNumberCol.setCellValueFactory(new PropertyValueFactory<User, String>("phoneNumber"));
		phoneNumberCol.setResizable(false);
		phoneNumberCol.setMinWidth(Mainpane.getWidth()/10.5);
		
		TableColumn<User, Integer> ageCol = new TableColumn<>("age");
		ageCol.setCellValueFactory(new PropertyValueFactory<User, Integer>("age"));
		ageCol.setResizable(false);
		ageCol.setMinWidth(Mainpane.getWidth()/10.5);

		TableColumn<User, String> genderCol = new TableColumn<>("gender");
		genderCol.setCellValueFactory(new PropertyValueFactory<User, String>("gender"));
		genderCol.setResizable(false);
		genderCol.setMinWidth(Mainpane.getWidth()/10.5);

		userTable.getColumns().addAll(idCol, usernameCol, passwordCol, emailCol, phoneNumberCol, ageCol, genderCol);
		userTable.setMaxHeight(600);
		userTable.setOnMouseClicked(tableMouseEvent());

		refreshTable();
	}
	
	private EventHandler<MouseEvent> tableMouseEvent(){
		return new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {
				// TODO Auto-generated method stub
				TableSelectionModel<User> tableSelectionModel = userTable.getSelectionModel();
				tableSelectionModel.setSelectionMode(SelectionMode.SINGLE);
				User user = tableSelectionModel.getSelectedItem();

				userIdField.setText(user.getUserId());
				unameField.setText(user.getUsername());
				passField.setText(user.getPassword());
				emailField.setText(user.getEmail());
				phoneNumberField.setText(user.getPhoneNumber());
				ageSpinner.getValueFactory().setValue(user.getAge());
				String gender = user.getGender();
				if(gender.equalsIgnoreCase("Male")) {
					genderToggle.selectToggle(maleBtn);
				}else {
					genderToggle.selectToggle(femaleBtn);
				}
				
				
			}
		};
	}
	
	private void getData() {
		users.removeAllElements();

		String query = "SELECT * FROM user";
		connect.rs = connect.execQuery(query);

		try {
			while(connect.rs.next()) {
				String id = connect.rs.getString("userID");
				String username = connect.rs.getString("username");
				String password = connect.rs.getString("password");
				String thisGender = connect.rs.getString("gender");
				String email = connect.rs.getString("email");
				String phoneNumber = connect.rs.getString("phoneNumber");
				Integer age = connect.rs.getInt("age");
				String role = connect.rs.getString("role");
//				gender = thisGender;
				users.add(new User(id,username, password, thisGender, email, phoneNumber, age, role));
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
	}

	private void refreshTable() {
		String lastId = "", nextId = "";

		getData();
		ObservableList<User> userObs = FXCollections.observableArrayList(users);
		userTable.setItems(userObs);

		if(!users.isEmpty()) {
			lastId = users.get(users.size()-1).getUserId();
			nextId = lastId.substring(2,5);

			User.setCount(Integer.valueOf(nextId)+1);
		}	
	}
	
	@Override
	public void handle(ActionEvent e) {
		if (e.getSource() == Menuitems) {
			Stage curr = (Stage)Mainpane.getScene().getWindow();
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
			Stage curr = (Stage)Mainpane.getScene().getWindow();
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
			Stage curr = (Stage)Mainpane.getScene().getWindow();
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
			Stage curr = (Stage)Mainpane.getScene().getWindow();
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
	
	public String genderSelected() {
		String gender ="";
		String meow = genderToggle.getSelectedToggle().toString();
		gender = meow.substring(meow.indexOf("'")+1, meow.length()-1);
		return gender;
	}
	
	public boolean validateUpdate() {
		boolean validation = false, filled = false, emailValidation = false, phoneNumberValidation = false, ageValidation = false, genderValidation = false;

		if(emailField.getText().equals("") && phoneNumberField.getText().equals("")) {
			filled = true;
		}else {
			updateError = "Please make sure all field are filled!";
		}

		if(emailField.getText().contains("@") && !emailField.getText().startsWith("@") && emailField.getText().endsWith(".com")) {
			emailValidation = true;
		}else {
			updateError = "Email must contain \"@\", must not start with\"@\" and must ends with \".com\"!";
		}

		if(phoneNumberField.getText().length()>=9 && phoneNumberField.getText().length()<=12) {
			phoneNumberValidation = true;
		}else {
			updateError = "Phone Number must be between 9-12 characters!";
		}

		if(ageSpinner.getValue()>=17 && ageSpinner.getValue()<=60) {
			ageValidation = true;
		}else {
			updateError = "Age must be between 17-60!";
		}
		String gender = genderSelected();
		if(!gender.equals("Male") || !gender.equals("Female")) {
			genderValidation = true;
		}else {
			updateError = "Gender must be selected, either ‘Male’ or ‘Female’";
		}

		if(emailValidation && phoneNumberValidation && ageValidation && genderValidation) {
			validation = true;
		}

		return validation;
	}
	
	public void refreshAllValue() {
		userIdField.setText("");
		unameField.setText("");
		passField.setText("");
		emailField.setText("");
		phoneNumberField.setText("");
		maleBtn.setSelected(false);
		femaleBtn.setSelected(false);
		
	}
	
	@Override
	public void start(Stage stg) throws Exception {
		Initialize();
		addComponents();
		Positioning();
		createTable();
		stg.setScene(scene);
		stg.setTitle("admin page");
		stg.setResizable(false);
		stg.show();
	}
}