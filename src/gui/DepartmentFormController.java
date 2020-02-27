package gui;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
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
import model.exceptions.ValidationException;
import model.service.DepartmentService;

public class DepartmentFormController implements Initializable{
	
	private Department entity;
	private DepartmentService service;
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
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
			notifyDataChangeListenrs();
			Utils.currentStage(event).close();
		}
		catch(ValidationException e) {
			setErrorMessages(e.getErrors());
		}
		catch(DbException e) {
			Alerts.showAlert("Error save object", null, e.getMessage(), AlertType.ERROR);
		}
		
	}
	
	private void notifyDataChangeListenrs() {
	
		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDateChanged();
		}
	}

	private Department getFormData() {
			
		    Department obj = new Department();
			
			ValidationException exception = new ValidationException("Validation error");
			
			obj.setId(Utils.tryParseToInt(textFieldId.getId()));
			if(textFieldName.getText() == null || textFieldName.getText().equals(" ")) {
				exception.addErros("name", "Field can´t be empty");
			}
			obj.setName(textFieldName.getText());
			
			if (exception.getErrors().size() > 0) {
				throw exception;
			}
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
	
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}
	
	public void updateFormData() {
		if(entity == null) {
			throw new IllegalStateException("Entity was null.");
		}
		textFieldId.setText(String.valueOf(entity.getId()));
		textFieldName.setText(entity.getName());
	}
	
	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();
		
		if (fields.contains("name")) {
			error.setText(errors.get("name"));
		}
	}

}
