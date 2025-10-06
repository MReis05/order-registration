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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.entities.IfoodOrder;
import model.entities.Order;
import model.exceptions.ValidationExceptions;
import model.services.IfoodOrderService;

public class IfoodOrderFormController implements Initializable {
	
	private IfoodOrder entity;
	
	private IfoodOrderService service;
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	@FXML
	private TextField txtId;
	
	@FXML
	private TextField txtOrderValue;
	
	@FXML
	private TextField txtDeliveryValue;
	
	@FXML
	private ComboBox<String> comboBoxCutQuestion;
	
	@FXML
	private ComboBox<String> comboBoxPayment;
	
	@FXML
	private TextField txtPaymentValue;
	
	@FXML
	private Label labelErrorOrderValue;
	
	@FXML
	private Label labelErrorDeliveryValue;
	
	@FXML
	private Label labelErrorPaymentValue;
	
	@FXML
	private Button btSave;
	
	@FXML
	private Button btCancel;
	
	private ObservableList<String> obsCut;
	
	private ObservableList<String> obsPayemnt;
	
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
			notifyDataChangeListeners();
			Utils.currentStage(event).close();
		}
		catch (ValidationExceptions e) {
			setErrorMessages(e.getErrors());
		}
		catch (DbException e) {
			Alerts.showAlert("Error in saving Ifood Order", null, e.getMessage(), AlertType.ERROR);
		}
	}
	
	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.dataChangeListeners();
		}
	}
	
	public void subscribeDataChangeListener(DataChangeListener listener) {
		dataChangeListeners.add(listener);
	}
	
	@FXML
	public void onBtCancelAction(ActionEvent event) {
		Utils.currentStage(event).close();
	}
	
	public void setIfoodOrder(IfoodOrder entity) {
		this.entity = entity;
	}
	
	public void setIfoodOrderService(IfoodOrderService service) {
		this.service = service;
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}
	
	public void initializeNodes() {
		Constraints.setTextFieldInteger(txtId);
		Constraints.setTextFieldDouble(txtOrderValue);
		Constraints.setTextFieldDouble(txtDeliveryValue);
		Constraints.setTextFieldDouble(txtPaymentValue);
		loadAssociatedObjects();
		initializeComboBoxCutQuestion();
		initializeComboBoxPayment();
	}
	
	public IfoodOrder getFormData() {
		Order order = new Order();
		IfoodOrder obj = new IfoodOrder(new Order());
		
		ValidationExceptions exception = new ValidationExceptions("Validation error");
		
		order.setId(Utils.tryParseToInt(txtId.getText()));
		
		if (txtOrderValue.getText() == null || txtOrderValue.getText().trim().equals("")) {
			exception.addError("orderValue", "Field can't be empty");
		}
		order.setOrderValue(Utils.tryParseToDouble(txtOrderValue.getText()));
		
		if (txtDeliveryValue.getText() == null || txtDeliveryValue.getText().trim().equals("")){
			exception.addError("deliveryValue", "Field can't be empty");
		}
		order.setDeliveryValue(Utils.tryParseToDouble(txtDeliveryValue.getText()));
		if (comboBoxPayment.getValue() == "Dinheiro" || comboBoxPayment.getValue() == "Cartão" || comboBoxPayment.getValue() == "Pix" ) {
			if (comboBoxCutQuestion.getValue() == "Sim") {
				if (txtPaymentValue.getText() == null || txtPaymentValue.getText().trim().equals("") || Utils.tryParseToDouble(txtPaymentValue.getText()) == 0.00) {
					exception.addError("paymentValue", "Field can't be empty");
					System.out.println("Entrou");
				}
				obj = new IfoodOrder(order, Utils.tryParseToDouble(txtPaymentValue.getText()), comboBoxPayment.getValue());
			}
			else {
				if (txtOrderValue.getText() == null || txtOrderValue.getText().trim().equals("")) {
					exception.addError("orderValue", "Field can't be empty");
					System.out.println("Entrou");
				}
				obj = new IfoodOrder(order, comboBoxPayment.getValue());
			}
		}
		else {
			obj = new IfoodOrder(order);
			obj.getPayment().setPaymentMethod(comboBoxPayment.getValue());
		}
		
		if(!exception.getErrors().isEmpty()) {
			throw exception;
		}
		return obj;
	}

	public void updateFormData() {
		if(entity == null) {
			throw new IllegalStateException("Entity was null");
		}
		txtId.setText(String.valueOf(entity.getId()));
		txtOrderValue.setText(String.format("%.2f", entity.getOrderValue()));
		txtDeliveryValue.setText(String.format("%.2f", entity.getDeliveryValue()));
		txtPaymentValue.setText(String.format("%.2f", entity.getPayment().getPaymentValue()));
		comboBoxCutQuestion.getSelectionModel().selectFirst();
		if(entity.getPayment() == null) {
			comboBoxPayment.getSelectionModel().selectFirst();
		}
		else {
			comboBoxPayment.setValue(entity.getPayment().getPaymentMethod());
		}
		
	}
	
	public void setErrorMessages(Map<String, String> errors) {
		Set<String> field = errors.keySet();
		
		labelErrorOrderValue.setText((field.contains("orderValue") ? errors.get("orderValue") : ""));
		labelErrorDeliveryValue.setText((field.contains("deliveryValue") ? errors.get("deliveryValue") : ""));
		labelErrorPaymentValue.setText((field.contains("paymentValue") ? errors.get("paymentValue") : ""));
	}
	
	public void loadAssociatedObjects() {
		List<String> cut = new ArrayList<>();
		List<String> payment = new ArrayList<>();
		
		cut.add("Sim");
		cut.add("Não");
		
		payment.add("Dinheiro");
		payment.add("Cartão");
		payment.add("Pix");
		
		obsCut = FXCollections.observableArrayList(cut);
		obsPayemnt = FXCollections.observableArrayList(payment);
		
		comboBoxCutQuestion.setItems(obsCut);
		comboBoxPayment.setItems(obsPayemnt);
	}
	
	private void initializeComboBoxCutQuestion() {
		Callback<ListView<String>, ListCell<String>> factory = lv -> new ListCell<String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item);
			}
		};
		comboBoxCutQuestion.setCellFactory(factory);
		comboBoxCutQuestion.setButtonCell(factory.call(null));
	}
	
	private void initializeComboBoxPayment() {
		Callback<ListView<String>, ListCell<String>> factory = lv -> new ListCell<String>() {
			@Override
			protected void updateItem(String item, boolean empty) {
				super.updateItem(item, empty);
				setText(empty ? "" : item);
			}
		};
		comboBoxPayment.setCellFactory(factory);
		comboBoxPayment.setButtonCell(factory.call(null));
	}

}
