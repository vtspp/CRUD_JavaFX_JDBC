package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.Department;

public class DepartmentFormController implements Initializable{
	
	private Department entity;
	
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
	public void onSaveAction() {
		System.out.println("saved");
	}
	
	@FXML
	public void onCancelAction() {
		System.out.println("canceled");
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
	
	public void updateFormData() {
		if(entity == null) {
			throw new IllegalStateException("Entity was null.");
		}
		textFieldId.setText(String.valueOf(entity.getId()));
		textFieldName.setText(entity.getName());
	}

}
