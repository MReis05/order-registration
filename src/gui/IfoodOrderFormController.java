package gui;

import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
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
import javafx.scene.image.ImageView;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.entities.IfoodOrder;
import model.entities.enums.Category;
import model.entities.enums.PaymentMethod;
import model.entities.enums.Type;
import model.exceptions.DbException;
import model.exceptions.ValidationExceptions;
import model.services.OrderService;

public class IfoodOrderFormController implements Initializable {
	
	private IfoodOrder order;
	
	private OrderService service;
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();

	@FXML
	private DatePicker dpPurchaseDate;
	
	@FXML
	private CheckBox checkBoxServiceFee;
	
	@FXML
	private TextField txtOrderValue;
	
	@FXML
	private TextField txtDeliveryValue;
	
	@FXML
	private ComboBox<String> comboBoxCutQuestion;
	
	@FXML
	private ComboBox<PaymentMethod> comboBoxPayment;
	
	@FXML
	private TextField txtPaymentValue;
	
	@FXML
	private Label labelErrorOrderValue;
	
	@FXML
	private Label labelErrorDeliveryValue;
	
	@FXML
	private Label labelErrorPaymentValue;
	
	@FXML
	private Label labelErrorPaymentMethod;
	
	@FXML
	private Button btSave;
	
	@FXML
	private Button btCancel;
	
	private ObservableList<String> obsCut;
	
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
		catch (DbException e) {
			e.printStackTrace();
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
	
	public void setIfoodOrder(IfoodOrder order) {
		this.order = order;
	}
	
	public void setIfoodOrderService(OrderService service) {
		this.service = service;
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}
	
	private void initializeNodes() {
		Constraints.setTextFieldDouble(txtOrderValue);
		Constraints.setTextFieldDouble(txtDeliveryValue);
		Constraints.setTextFieldDouble(txtPaymentValue);
		Utils.formatDatePicker(dpPurchaseDate, "dd/MM/yyyy");
		loadAssociatedObjects();
		initializeResources();
	}
	
	private void initializeResources() {
		ImageView saveIcon = new ImageView(ImageManager.getImage("saveIcon"));
		
		saveIcon.setFitHeight(23);
		saveIcon.setFitWidth(23);
		
		btSave.setGraphic(saveIcon);
	}
	
	public IfoodOrder getFormData() {
		labelErrorDeliveryValue.setText("");
		labelErrorOrderValue.setText("");
		labelErrorPaymentMethod.setText("");
		labelErrorPaymentValue.setText("");
		
		ValidationExceptions exception = new ValidationExceptions("Validation error");
		
		order.setType(Type.VIA_IFOOD);
		
		if(dpPurchaseDate != null && dpPurchaseDate.getValue() != null) {
			order.setDate(dpPurchaseDate.getValue());
		}
		else {
			order.setDate(LocalDate.now());
		}
		if (txtOrderValue.getText() == null || txtOrderValue.getText().trim().equals("")) {
			exception.addError("orderValue", "Field can't be empty");
		}
		order.setOrderValue(new BigDecimal(txtOrderValue.getText()));
		
		if (txtDeliveryValue.getText() == null || txtDeliveryValue.getText().trim().equals("")){
			exception.addError("deliveryValue", "Field can't be empty");
		}
		order.setDeliveryValue(new BigDecimal(txtDeliveryValue.getText()));
		
		if(comboBoxPayment.getValue() == null) {
			exception.addError("paymentMethod", "You must select one Payment method");
		}
		
		order.setPaymentMethod(comboBoxPayment.getValue());
		if(comboBoxPayment.getValue() == PaymentMethod.IFOOD) {
			order.feeForIfood();
			order.setCategory(Category.VIA_IFOOD);
		}
		else {
			order.setCategory(Category.VIA_LOJA);
			if ("Sim".equals(comboBoxCutQuestion.getValue())) {
				if (txtPaymentValue.getText() == null || txtPaymentValue.getText().trim().equals("") || Utils.tryParseToDouble(txtPaymentValue.getText()) == 0.00) {
					exception.addError("paymentValue", "Field can't be empty");
				}
				order.setIfoodDirectPaymentValue(new BigDecimal(txtPaymentValue.getText()));
				order.cutPayments();
				}
			else {
				order.feeForStore();
			}
		}
		
		if(checkBoxServiceFee.isSelected()) {
			order.setServiceFee(1);
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
			txtPaymentValue.setText(order.getIfoodDirectPaymentValue().toString());
			comboBoxPayment.setValue(order.getPaymentMethod());
		}
	}
	
	public void setErrorMessages(Map<String, String> errors) {
		Set<String> field = errors.keySet();
		
		labelErrorOrderValue.setText((field.contains("orderValue") ? errors.get("orderValue") : ""));
		labelErrorDeliveryValue.setText((field.contains("deliveryValue") ? errors.get("deliveryValue") : ""));
		labelErrorPaymentValue.setText((field.contains("paymentValue") ? errors.get("paymentValue") : ""));
		labelErrorPaymentMethod.setText((field.contains("paymentMethod") ? errors.get("paymentMethod") : ""));
	}
	
	public void loadAssociatedObjects() {
		List<String> cut = new ArrayList<>();
		
		cut.addAll(Arrays.asList("Sim", "Não"));
		
		obsCut = FXCollections.observableArrayList(cut);
		obsPayemnt = FXCollections.observableArrayList(PaymentMethod.values());
		
		comboBoxCutQuestion.setItems(obsCut);
		comboBoxPayment.setItems(obsPayemnt);
	}

}
