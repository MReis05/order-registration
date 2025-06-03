package gui;

import java.net.URL;
import java.util.ResourceBundle;

import gui.util.Constraints;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.util.Callback;
import model.entities.Ifood;

public class IfoodFormController implements Initializable {
	
	private Ifood entity;

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
	
	@FXML
	public void onBtSaveAction() {
		System.out.println("onBtSaveAction");
	}
	
	@FXML
	public void onBtCancelAction() {
		System.out.println("onBtCancelAction");
	}
	
	public void setIfood(Ifood entity) {
		this.entity = entity;
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
		initializeComboBoxCutQuestion();
		initializeComboBoxPayment();
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
