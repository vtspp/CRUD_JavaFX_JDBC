package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Department;
import model.service.DepartmentService;

public class DepartmentListController implements Initializable{
	@FXML
	private TableView<Department> tableViewDepartment;
	@FXML
	private TableColumn<Department, Integer> tableColumnID;
	@FXML
	private TableColumn<Department, String> tableColumnName;
	@FXML
	private Button button;
	
	private DepartmentService service;
	
	private ObservableList<Department> observableList;
	
	public void onbuttonAction() {
		
	}
	
	public void setDepartmentService(DepartmentService service) {
		this.service = service;
	}
	
	public void updateTableView() {
		if(service == null) {
			throw new IllegalStateException("Service was null.");
		}
		List<Department> list = service.findAll();
		observableList = FXCollections.observableArrayList(list);
		tableViewDepartment.setItems(observableList);
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		initializeNodes();
		
	}

	private void initializeNodes() {
		tableColumnID.setCellValueFactory(new PropertyValueFactory<>("Id"));
		tableColumnName.setCellValueFactory(new PropertyValueFactory<>("Name"));
		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewDepartment.prefHeightProperty().bind(stage.heightProperty());
	}

}
