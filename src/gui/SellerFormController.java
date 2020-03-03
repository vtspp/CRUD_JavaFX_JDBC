package gui;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Constraints;
import gui.util.Utils;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.entities.Department;
import model.entities.Seller;
import model.exceptions.ValidationException;
import model.service.DepartmentService;
import model.service.SellerService;

public class SellerFormController implements Initializable {

	private Seller entity;
	private SellerService service;
	private DepartmentService departmentService;
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	@FXML
	private TextField textFieldId;
	@FXML
	private TextField textFieldName;
	@FXML
	private TextField textFieldEmail;
	@FXML
	private DatePicker datePikerBirthDate;
	@FXML
	private TextField textFieldBaseSalary;
	@FXML
	private ComboBox<Department> comboBoxDepartment;
	@FXML
	private Label error;
	@FXML
	private Label errorEmail;
	@FXML
	private Label errorBirthDate;
	@FXML
	private Label errorBaseSalary;
	@FXML
	private Button save;
	@FXML
	private Button cancel;

	private ObservableList<Department> observableList;

	@FXML
	public void onSaveAction(ActionEvent event) {
		if (entity == null) {
			throw new IllegalStateException("Entity was null.");
		}
		if (service == null) {
			throw new IllegalStateException("Serice was null");
		}
		try {
			entity = getFormData();
			service.saveOrUpdate(entity);
			notifyDataChangeListenrs();
			Utils.currentStage(event).close();
		} catch (ValidationException e) {
			setErrorMessages(e.getErrors());
		} catch (DbException e) {
			Alerts.showAlert("Error save object", null, e.getMessage(), AlertType.ERROR);
		}

	}

	private void notifyDataChangeListenrs() {

		for (DataChangeListener listener : dataChangeListeners) {
			listener.onDateChanged();
		}
	}

	private Seller getFormData() {

		Seller obj = new Seller();

		ValidationException exception = new ValidationException("Validation error");

		obj.setId(Utils.tryParseToInt(textFieldId.getId()));
		if (textFieldName.getText() == null || textFieldName.getText().equals(" ")) {
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
		Constraints.setTextFieldMaxLength(textFieldName, 70);
		Constraints.setTextFieldDouble(textFieldBaseSalary);
		Constraints.setTextFieldMaxLength(textFieldEmail, 60);
		Utils.formatDatePicker(datePikerBirthDate, "dd/MM/yyyy");
		initializeComboBoxDepartment();
	}

	public void setSeller(Seller entity) {
		this.entity = entity;
	}

	public void setServices(SellerService service, DepartmentService departmentService) {
		this.service = service;
		this.departmentService = departmentService;
	}

	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}

	public void updateFormData() {
		if (entity == null) {
			throw new IllegalStateException("Entity was null.");
		}
		textFieldId.setText(String.valueOf(entity.getId()));
		textFieldName.setText(entity.getName());
		textFieldEmail.setText(entity.getEmail());
		Locale.setDefault(Locale.US);
		textFieldBaseSalary.setText(String.format("%.2f", entity.getBaseSalary()));
		if (entity.getBirthDate() != null) {
			datePikerBirthDate.setValue(LocalDate.now(ZoneId.systemDefault()));
		}
		if(entity.getDepartment() == null) {
			comboBoxDepartment.getSelectionModel().selectFirst();
		}else {
			comboBoxDepartment.setValue(entity.getDepartment());
		}
	}

	private void setErrorMessages(Map<String, String> errors) {
		Set<String> fields = errors.keySet();

		if (fields.contains("name")) {
			error.setText(errors.get("name"));
		}
	}

	public void loadAssociatedOjects() {
		List<Department> list = departmentService.findAll();
		observableList = FXCollections.observableArrayList(list);
		comboBoxDepartment.setItems(observableList);
	}

	private void initializeComboBoxDepartment() {
		Callback<ListView<Department>, ListCell<Department>> factory = lv -> new ListCell<Department>() {
			@Override
			protected void updateItem(Department item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item.getName());
			}
		};
		comboBoxDepartment.setCellFactory(factory);
		comboBoxDepartment.setButtonCell(factory.call(null));
	}

}
