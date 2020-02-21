package gui;

import java.net.URL;
import java.util.ResourceBundle;

import db.DbException;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;
import model.service.DepartmentService;

public class DepartmentFormController implements Initializable{
	
	private Department entity;
	private DepartmentService service;
	
	@FXML
	private TextField textFieldId;
	@FXML
	private TextField textFieldName;
	@FXML
	private Label error;
	@FXML
	private Button save;
	@FXML
	private Button cancel;
	
	@FXML
	public void onSaveAction(ActionEvent event) {
		if(entity == null) {
			throw new IllegalStateException("Entity was null.");
		}
		if(service == null) {
			throw new IllegalStateException("Serice was null");
		}
		try {
			entity = getFormData();
			service.saveOrUpdate(entity);
			Utils.currentStage(event).close();
		}
		catch(DbException e) {
			Alerts.showAlert("Error save object", null, e.getMessage(), AlertType.ERROR);
		}
		
	}
	
	private Department getFormData() {
			Department obj = new Department();
			obj.setId(Utils.tryParseToInt(textFieldId.getId()));
			obj.setName(textFieldName.getText());
		return obj;
	}

	@FXML
	public void onCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}

	@Override
	public void initialize(URL url, ResourceBundle resourceBundle) {
		initializeNodes();
	}
	
	public void initializeNodes() {
		Constraints.setTextFieldInteger(textFieldId);
		Constraints.setTextFieldMaxLength(textFieldName, 30);
	}
	
	public void setDepartment(Department entity) {
		this.entity = entity;
	}
	
	public void setDepartmentService(DepartmentService service) {
		this.service = service;
	}
	
	public void updateFormData() {
		if(entity == null) {
			throw new IllegalStateException("Entity was null.");
		}
		textFieldId.setText(String.valueOf(entity.getId()));
		textFieldName.setText(entity.getName());
	}

}
