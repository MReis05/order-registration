package gui;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

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
import javafx.scene.control.TextField;
import model.entities.DirectOrder;
import model.entities.enums.PaymentMethod;
import model.exceptions.DbException;
import model.exceptions.ValidationExceptions;
import model.services.OrderService;

public class DirectOrderFormController implements Initializable {
	
	private DirectOrder entity;
	
	private OrderService service;
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	@FXML
	private DatePicker dpPurchaseDate;
	
	@FXML
	private TextField txtOrderValue;
	
	@FXML
	private TextField txtDeliveryValue;
	
	@FXML
	private ComboBox<PaymentMethod> comboBoxPayment;
	
	@FXML
	private Label labelErrorOrderValue;
	
	@FXML
	private Label labelErrorDeliveryValue;
	
	@FXML
	private Label labelErrorPaymentMethod;
	
	@FXML
	private Button btSave;
	
	@FXML
	private Button btCancel;
	
	private ObservableList<PaymentMethod> obsPayemnt;
	
	@FXML
	public void onBtSaveAction(ActionEvent event) {
		if(entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		if(service == null) {
			throw new IllegalStateException("Service was null");
		}
		
		try {
			entity = getFormData();
			service.saveOrUpdate(entity);
			notifyDataChangeListeners(entity);
			Utils.currentStage(event).close();
		}
		catch (ValidationExceptions e) {
			setErrorMessages(e.getErrors());
		}
		catch (DbException e) {
			Alerts.showAlert("Error in saving Direct Order", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
	private void notifyDataChangeListeners(DirectOrder entity) {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.dataChangeListeners(entity);
		}
	}
	
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}
	
	@FXML
	public void onBtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}
	
	public void setDirectOrder(DirectOrder entity) {
		this.entity = entity;
	}
	
	public void setDirectOrderService(OrderService service) {
		this.service = service;
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}
	
	public void initializeNodes() {
		Utils.formatDatePicker(dpPurchaseDate, "dd/MM/yyyy");
		Constraints.setTextFieldDouble(txtOrderValue);
		Constraints.setTextFieldDouble(txtDeliveryValue);
		loadAssociatedObjects();
	}
	
	public DirectOrder getFormData() {
		labelErrorDeliveryValue.setText("");
		labelErrorOrderValue.setText("");
		labelErrorPaymentMethod.setText("");
		DirectOrder obj = new DirectOrder();
		
		ValidationExceptions exception = new ValidationExceptions("Validation error");
		
		if(dpPurchaseDate != null && dpPurchaseDate.getValue() != null) {
			obj.setDate(dpPurchaseDate.getValue());
		}
		else {
			obj.setDate(LocalDate.now());
		}
		
		if(txtOrderValue.getText() == null || txtOrderValue.getText().trim().equals("")) {
			exception.addError("orderValue", "Field can't be empty");
		}
		obj.setOrderValue(new BigDecimal(txtOrderValue.getText()));
		if(txtDeliveryValue.getText() == null || txtDeliveryValue.getText().trim().equals("")) {
			exception.addError("deliveryValue", "Field can't be empty");
		}
		obj.setDeliveryValue(new BigDecimal(txtDeliveryValue.getText()));
		
		if(comboBoxPayment.getValue() == PaymentMethod.IFOOD || comboBoxPayment.getValue() == null) {
			exception.addError("paymentMethod", "You must select a payment method other than Ifood");
		}
		obj.setPaymentMethods(comboBoxPayment.getValue());
		
		if(!exception.getErrors().isEmpty()) {
			throw exception;
		}
		
		return obj;
	}

	public void updateFormData() {
		if(entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		if(entity.getId() != null) {
			txtOrderValue.setText(entity.getOrderValue().toString());
			txtDeliveryValue.setText(entity.getDeliveryValue().toString());
			if(entity.getPaymentMethod() != null) {
				comboBoxPayment.setValue(entity.getPaymentMethod());
			}
		}
	}
	
	public void setErrorMessages(Map<String, String> errors) {
		Set<String> field = errors.keySet();
		
		labelErrorOrderValue.setText((field.contains("orderValue") ? errors.get("orderValue") : ""));
		labelErrorDeliveryValue.setText((field.contains("deliveryValue") ? errors.get("deliveryValue") : ""));
		labelErrorPaymentMethod.setText((field.contains("paymentMethod") ? errors.get("paymentMethod") : ""));
	}
	
	public void loadAssociatedObjects() {
		obsPayemnt = FXCollections.observableArrayList(PaymentMethod.values());
		
		comboBoxPayment.setItems(obsPayemnt);
	}
}
