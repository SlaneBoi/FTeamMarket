package FTeamMarket;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Vector;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class RegisterPage extends Application implements EventHandler<ActionEvent>{

	Connect connect = Connect.getInstance();
	String registerErrorMessage = "", genderSelected = "", generatedId;

	Scene scene;
	BorderPane mainPane;
	GridPane content;
	FlowPane genderPane, buttons;

	Text titleLbl, userIdLbl, unameLbl, passLbl, emailLbl, phoneNumberLbl, ageLbl, genderLbl;
	TextField userIdField, usernamefield, emailField, phoneNumberField;
	PasswordField passField;
	Spinner<Integer> ageSpinner;
	RadioButton maleBtn, femaleBtn;
	ToggleGroup genderToggle;
	Button loginBtn, registerBtn;

	Vector<User> users;

	public void initialize() {
		mainPane = new BorderPane();
		content = new GridPane();
		scene = new Scene(mainPane, 300, 400);
		users = new Vector<>();

		titleLbl = new Text("Register");
		userIdLbl = new Text("User ID");
		unameLbl = new Text("Username");
		passLbl = new Text("Password");
		emailLbl = new Text("Email");
		phoneNumberLbl = new Text("Phone Number");
		ageLbl = new Text("Age");
		genderLbl = new Text("Gender");

		userIdField = new TextField();
		userIdField.setEditable(false);
		userIdField.setPromptText(generateId());

		usernamefield = new TextField();
		emailField = new TextField();
		phoneNumberField = new TextField();

		passField = new PasswordField();

		ageSpinner = new Spinner<Integer>(15,70,16);
		maleBtn = new RadioButton("Male");
		femaleBtn = new RadioButton("Female");
		genderPane = new FlowPane(maleBtn, femaleBtn);

		genderToggle = new ToggleGroup();
		genderToggle.getToggles().addAll(maleBtn, femaleBtn);

		loginBtn = new Button("Login Page");
		registerBtn = new Button("Register");
		buttons = new FlowPane(loginBtn, registerBtn);

	}

	public void build() {
		content.add(userIdLbl, 0, 0);
		content.add(userIdField, 1, 0);

		content.add(unameLbl, 0, 1);
		content.add(usernamefield, 1, 1);

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

		content.add(buttons, 1, 7);

		mainPane.setTop(titleLbl);
		mainPane.setCenter(content);
	}

	public void style() {
		mainPane.setPadding(new Insets(30));
		content.setVgap(10);
		content.setHgap(10);

		mainPane.setAlignment(titleLbl, Pos.TOP_CENTER);
		mainPane.setMargin(titleLbl, new Insets(0,0,30,0));
		titleLbl.setFont(Font.font("Comic Sans", FontWeight.BOLD, 30));
		genderPane.setHgap(10);
		buttons.setHgap(10);
	}

	public void addAction() {
		registerBtn.setOnAction(this);
		loginBtn.setOnAction(this);
	}
	
	@Override
	public void start(Stage stage) throws Exception {
		initialize();
		build();
		style();
		addAction();
		stage.setTitle("Register Page");
		stage.setScene(scene);
		stage.show();
	}

	@Override
	public void handle(ActionEvent event) {
		Alert alert = new Alert(AlertType.NONE);
		if(event.getSource() == registerBtn) {
			if(validateUserInput()) {
				
				addUserData();
				
				alert.setAlertType(AlertType.INFORMATION);
				alert.setTitle("REGISTRATION SUCCESS");
				alert.setHeaderText("Click OK to continue");
				alert.showAndWait();
				
				Stage curr = (Stage) mainPane.getScene().getWindow();
				curr.close();
				Stage next = new Stage();
				try {
					new LoginPage().start(next);
				}catch(Exception e) {
					e.printStackTrace();
				}
				
			}else {
				alert.setAlertType(AlertType.ERROR);
				alert.setTitle("REGISTRATION ERROR");
				alert.setHeaderText(registerErrorMessage);
				alert.showAndWait();
			}
		}

		if(event.getSource() == loginBtn) {
			Stage curr = (Stage) mainPane.getScene().getWindow();
			curr.close();
			Stage next = new Stage();
			try {
				new LoginPage().start(next);
			}catch(Exception e) {
				e.printStackTrace();
			}
		}
	}

	public String generateId() {
		String lastId = "", nextId = "";

		String query = "SELECT * FROM user";
		connect.rs = connect.execQuery(query);

		try {
			while(connect.rs.next()) {
				String id = connect.rs.getString("userID");
				String username = connect.rs.getString("username");
				String password = connect.rs.getString("password");
				String gender = connect.rs.getString("gender");
				String email = connect.rs.getString("email");
				String phoneNumber = connect.rs.getString("phoneNumber");
				Integer age = connect.rs.getInt("age");
				String role = connect.rs.getString("role");
				users.add(new User(id, username, password, gender, email, phoneNumber, age, role));
			}
		} catch (Exception e) {
			// TODO: handle exception
		}
		
		if(!users.isEmpty()) {
			lastId = users.get(users.size()-1).getUserId();
			nextId = lastId.substring(2,5);
			System.out.println(nextId);
			User.setCount(Integer.valueOf(nextId)+1);
		}
		
		String id="US";
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
	
	public boolean validateUserInput() {
		boolean validation = false, usernameValidate = false, passwordValidate = false, emailValidate = false, phoneNumberValidate = false, ageValidate = false, genderValidate = false;
		
		if(usernamefield.getText().length() >= 5 && usernamefield.getText().length() <= 20) {
			usernameValidate = true;
		}else {
			registerErrorMessage = "Username must be between 5-20 characters!";
		}
		
		if(passField.getText().length() >= 5 && passField.getText().length() <= 20 && passwordAlphaNumeric()) {
			passwordValidate = true;
		}else {
			registerErrorMessage = "Password must be between 5-20 characters!";
		}
		
		if(emailField.getText().contains("@") && !emailField.getText().startsWith("@") && emailField.getText().endsWith(".com")) {
			emailValidate = true;
		}else {
			registerErrorMessage = "Email must consist of '@' character, '@' character must not be in front and must end with .com.!";
		}
		
		if(phoneNumberField.getText().length() >= 9 && phoneNumberField.getText().length() <= 12) {
			phoneNumberValidate = true;
		}else {
			registerErrorMessage = "Phone Number must be between 9-12 characters!";
		}
		
		if(ageSpinner.getValue() >= 17 && ageSpinner.getValue() <= 60) {
			ageValidate = true;
		}else {
			registerErrorMessage = "Age must be between 17-60!";
		}
		
		String gender = genderToggle.getSelectedToggle().toString();
		genderSelected = gender.substring(gender.indexOf("'")+1, gender.length()-1);
		if(genderSelected.equals("Male") || genderSelected.equals("Female")) {
			genderValidate = true;
		}else {
			registerErrorMessage = "Gender must be selected, either ‘Male’ or ‘Female’";
		}
		
		if(usernameValidate && passwordValidate && emailValidate && phoneNumberValidate && ageValidate && genderValidate) {
			validation = true;
		}
		return validation;
	}
	
	public boolean passwordAlphaNumeric() {
		boolean alphaNumericVerification = false;
		
		String verify = passField.getText(), alphaNumeric = "";
		
		for(int i = 0; i < verify.length(); i++) {
			if(verify.charAt(i) >= '0' && verify.charAt(i) <= '9') {
				alphaNumeric += "0";
			}else if(verify.charAt(i) >= 'a' && verify.charAt(i) <= 'z') {
				alphaNumeric += "0";
			}else if(verify.charAt(i) >= 'A' && verify.charAt(i) <= 'Z') {
				alphaNumeric += "0";
			}else {
				alphaNumeric += "1";
			}
		}
		
		if(alphaNumeric.contains("1")) {
			registerErrorMessage = "Password must be alphanumeric!";
		}else {
			alphaNumericVerification=true;
		}
		return alphaNumericVerification;
		
	}
	
	public void addUserData() {
		String query = "INSERT INTO user VALUES (?,?,?,?,?,?,?,?)";

		PreparedStatement ps = connect.prepareStatement(query);
		
		try {
			ps.setString(1, generatedId);
			ps.setString(2, usernamefield.getText());
			ps.setString(3, passField.getText());
			ps.setString(4, genderSelected);
			ps.setString(5, emailField.getText());
			ps.setString(6, phoneNumberField.getText());
			ps.setInt(7, ageSpinner.getValue());
			ps.setString(8, "user");
			ps.executeUpdate();

		}catch (SQLException e1) {
			e1.printStackTrace();
		}
	}
}