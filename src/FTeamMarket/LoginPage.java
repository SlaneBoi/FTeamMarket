package FTeamMarket;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class LoginPage extends Application implements EventHandler<ActionEvent>{

	static User currUser;
	Scene scene;
	BorderPane Mainpane;
	GridPane content;
	Label usernamelbl, passlbl, titlelbl;
	TextField usernamefield;
	PasswordField passfield;
	FlowPane buttons;
	Button loginbtn, registerbtn;
	Connect connect = Connect.getInstance();
	
	public void Initialize() {
		Mainpane = new BorderPane();
		scene = new Scene(Mainpane, 300 , 200);
		content = new GridPane();
		
		usernamelbl = new Label("Username");
		passlbl = new Label("Password");
		titlelbl = new Label("Login");
		
		usernamefield = new TextField();
		passfield = new PasswordField();
		registerbtn = new Button("Register Account Page");
		loginbtn = new Button("Login");
		buttons = new FlowPane(registerbtn,loginbtn);
		
		loginbtn.setOnAction(this);
		registerbtn.setOnAction(this);
	}
	
	public void addComponents() {
		content.add(usernamelbl, 0, 0);
		content.add(usernamefield, 1, 0);
		
		content.add(passlbl, 0, 1);
		content.add(passfield, 1 , 1);
		
		content.add(buttons, 1, 2);
	}
	
	public void Positioning() {
		Mainpane.setTop(titlelbl);
		titlelbl.setFont(Font.font("Comic Sans", FontWeight.BOLD, 25));
		Mainpane.setAlignment(titlelbl, Pos.CENTER);
		Mainpane.setPadding(new Insets(20));
		content.setVgap(10);
		titlelbl.setPadding(new Insets(0, 0, 20, 0));
		
		Mainpane.setCenter(content);
		buttons.setPadding(new Insets(10, 0 , 0 , 0));
		buttons.setHgap(10);
			
		content.getColumnConstraints().add(new ColumnConstraints(60));	
	}

	@Override
	public void start(Stage stg) throws Exception {
		Initialize();
		addComponents();
		Positioning();
		
		stg.setTitle("Login Page");
		stg.setScene(scene);
		stg.show();
	}

	@Override
	public void handle(ActionEvent e) {
		Alert alert = new Alert(AlertType.NONE);
		 if(e.getSource() == registerbtn) {
				Stage curr = (Stage) Mainpane.getScene().getWindow();
				curr.close();
				Stage next = new Stage();
				try {
					new RegisterPage().start(next);
				}catch(Exception e1) {
					e1.printStackTrace();
				}
			}
			
		 else if(e.getSource() == loginbtn) {
			if (usernamefield.getText().equals("") || passfield.getText().equals("")) {
				
				alert.setAlertType(AlertType.INFORMATION);
				alert.setHeaderText("Incomplete Login Form");
				alert.setContentText("Username and Password cannot be empty.");
				alert.show();
			}		
		else {
			if(checkUser()) {
				alert.setAlertType(AlertType.INFORMATION);
				alert.setTitle("LOGIN SUCCESS");
				alert.setHeaderText("Click OK to continue");
				alert.showAndWait();

				if(checkUserType().equals("admin")) {
					Stage curr = (Stage) Mainpane.getScene().getWindow();
					curr.close();
					Stage next = new Stage();
					try {
						new MainPageAdmin().start(next);
					}catch(Exception e1) {
						e1.printStackTrace();
					}
				}
	
			else {
					Stage curr = (Stage) Mainpane.getScene().getWindow();
					curr.close();
					Stage next = new Stage();
					try {
						new MainPageUser().start(next);
					}catch(Exception e1) {
						e1.printStackTrace();
					}
				}
				
			}else {
				alert.setAlertType(AlertType.ERROR);
				alert.setTitle("LOGIN FAILED");
				alert.setHeaderText("Username or Password cannot be null!");
				alert.showAndWait();
			}
		}
	}	
}
	public boolean checkUser() {
		boolean isUser = false;

		String query = "SELECT * FROM user WHERE username = ? AND password = ?";

		PreparedStatement ps = connect.prepareStatement(query);

		try {
			ps.setString(1, usernamefield.getText());
			ps.setString(2, passfield.getText());
			ResultSet rs = ps.executeQuery();

			if(rs.next()) {
				String userId = rs.getString(1);
				String username = rs.getString(2);
				String password = rs.getString(3);
				String gender = rs.getString(4);
				String email = rs.getString(5);
				String phoneNumber = rs.getString(6);
				int age = rs.getInt(7);
				String role = rs.getString(8);
				
				 currUser = new User(userId, username, password, gender, email, phoneNumber, age, role);
				isUser = true;
			}else {
				isUser = false;
			}
		}catch (SQLException e1) {
			e1.printStackTrace();
		}

		return isUser;
	}
		
	public String checkUserType() {
		String userType = "";

		String query = "SELECT role, userID, username FROM user WHERE username = ? AND password = ?";

		PreparedStatement ps = connect.prepareStatement(query);

		try {
			ps.setString(1, usernamefield.getText());
			ps.setString(2, passfield.getText());
			ResultSet rs = ps.executeQuery();
			
			while(rs.next()) {
				if(rs.getString(1).equals("admin")) {
					userType = "admin";
				}else {
					userType = "user";
				}
			}
		}catch (SQLException e1) {
			e1.printStackTrace();
		}

		return userType;
	}	
}