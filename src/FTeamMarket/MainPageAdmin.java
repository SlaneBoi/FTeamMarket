package FTeamMarket;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;

public class MainPageAdmin extends Application implements EventHandler<ActionEvent>{
		
	Scene scene;
	BorderPane Mainpane;
	Label title;
	Menu menu;
	MenuBar menubar;
	MenuItem Menuitems, Menuitems1, Menuitems2, Menuitems3;
	
	public void Initialize() {
		Mainpane = new BorderPane();
		scene = new Scene(Mainpane,1000, 600);
		title = new Label("Welcome " + LoginPage.currUser.getUsername());
		Menuitems = new MenuItem("Manage User");
		Menuitems1 = new MenuItem("Manage Item");
		Menuitems2 = new MenuItem("Transaction");
		Menuitems3 = new MenuItem("Logout");
		
		menu = new Menu("Menu");
		menubar = new MenuBar();
	}
	
	public void addComponents() {
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
		Mainpane.setTop(menubar);
		Mainpane.setCenter(title);
		
		Mainpane.setAlignment(title, Pos.CENTER);
		title.setFont(Font.font("Comic Sans", FontWeight.BOLD, 45));
	}

	@Override
	public void start(Stage stg) throws Exception {
		Initialize();
		addComponents();
		Positioning();
		stg.setTitle("admin page");
		stg.setScene(scene);
		stg.show();
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
		if (e.getSource() == Menuitems1) {
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
		if (e.getSource() == Menuitems3) {
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
}
