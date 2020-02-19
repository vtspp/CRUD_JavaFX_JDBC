package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;
import model.entities.Department;
import model.service.DepartmentService;

public class MainViewController implements Initializable{
	@FXML
	private MenuItem menuItemSeller;
	@FXML
	private MenuItem menuItemDepartment;
	@FXML
	private MenuItem menuItemAbout;
	
	@FXML
	public void onMenuItemSellerAction() {
		System.out.println("Seller");
	}
	
	@FXML
	public void onMenuItemDepartment() {
		loadView("/gui/DepartmentList.fxml");
	}
	
	@FXML
	public void onMenuItemAbout(){
		loadView("/gui/About.fxml");
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
		
	}
	
	private synchronized void loadView (String guiFxml) {
		try{
			FXMLLoader load = new FXMLLoader(getClass().getResource(guiFxml));
			VBox vbox = load.load();
			Scene mainScene = Main.getMainScene();
			
			VBox mainVBox = (VBox)((ScrollPane) mainScene.getRoot()).getContent();
			
			Node mainMenu = mainVBox.getChildren().get(0);
			mainVBox.getChildren().clear();
			mainVBox.getChildren().add(mainMenu);
			mainVBox.getChildren().addAll(vbox.getChildren());
			
			DepartmentListController controller = load.getController();
			controller.setDepartmentService(new DepartmentService());
			controller.updateTableView();
		}
		catch(IOException e) {
			Alerts.showAlert("IOEXCEPTION", "Error loading iew.", e.getMessage(), AlertType.ERROR );
		}
		
	}

}
