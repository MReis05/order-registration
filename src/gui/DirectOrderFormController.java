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
import gui.util.ImageManager;
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
import javafx.scene.image.ImageView;
import model.entities.DirectOrder;
import model.entities.enums.PaymentMethod;
import model.entities.enums.Type;
import model.exceptions.ValidationExceptions;
import model.services.OrderService;

public class DirectOrderFormController implements Initializable {
	
	private DirectOrder order;
	
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
		if(order == null) {
			throw new IllegalStateException("Entity was null");
		}
		if(service == null) {
			throw new IllegalStateException("Service was null");
		}
		
		try {
			order = getFormData();
			service.saveOrUpdate(order);
			notifyDataChangeListeners();
			Utils.currentStage(event).close();
		}
		catch (ValidationExceptions e) {
			setErrorMessages(e.getErrors());
		}
		catch(Exception e) {
			Alerts.showAlert("Erro ao salvar o pedido", null, e.getMessage(), AlertType.ERROR);
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
	
	public void setDirectOrder(DirectOrder order) {
		this.order = order;
	}
	
	public void setDirectOrderService(OrderService service) {
		this.service = service;
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}
	
	private void initializeNodes() {
		Utils.formatDatePicker(dpPurchaseDate, "dd/MM/yyyy");
		Constraints.setTextFieldDouble(txtOrderValue);
		Constraints.setTextFieldDouble(txtDeliveryValue);
		loadAssociatedObjects();
		initializeResources();
	}
	
	private void initializeResources() {
		ImageView saveIcon = new ImageView(ImageManager.getImage("saveIcon"));
		
		saveIcon.setFitHeight(23);
		saveIcon.setFitWidth(23);
		
		btSave.setGraphic(saveIcon);
	}
	
	public DirectOrder getFormData() {
		labelErrorDeliveryValue.setText("");
		labelErrorOrderValue.setText("");
		labelErrorPaymentMethod.setText("");
		
		ValidationExceptions exception = new ValidationExceptions("Validation error");
		String error = "Campo não pode estar vazio";
		
		order.setType(Type.VIA_PEDIDO_DIRETO);
		
		if(dpPurchaseDate != null && dpPurchaseDate.getValue() != null) {
			order.setDate(dpPurchaseDate.getValue());
		}
		else {
			order.setDate(LocalDate.now());
		}
		
		if(txtOrderValue.getText() == null || txtOrderValue.getText().trim().equals("")) {
			exception.addError("orderValue", error);
		}
		else {
			order.setOrderValue(new BigDecimal(txtOrderValue.getText()));
		}
		if(txtDeliveryValue.getText() == null || txtDeliveryValue.getText().trim().equals("")) {
			exception.addError("deliveryValue", error);
		}
		else {
			order.setDeliveryValue(new BigDecimal(txtDeliveryValue.getText()));
		}
		if(comboBoxPayment.getValue() == PaymentMethod.IFOOD) {
			exception.addError("paymentMethod", "Não é possível selecionar Ifood");
		}
		else {
			order.setPaymentMethod(comboBoxPayment.getValue());
		}
		
		if(!exception.getErrors().isEmpty()) {
			throw exception;
		}
		
		return order;
	}

	public void updateFormData() {
		if(order == null) {
			throw new IllegalStateException("Entity was null");
		}
		if(order.getId() != null) {
			dpPurchaseDate.setValue(order.getDate());
			txtOrderValue.setText(order.getOrderValue().toString());
			txtDeliveryValue.setText(order.getDeliveryValue().toString());
			comboBoxPayment.setValue(order.getPaymentMethod());
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
		Utils.fomartComboBoxPaymentCamelCase(comboBoxPayment);
	}
}
