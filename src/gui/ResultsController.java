package gui;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

public class ResultsController implements Initializable {
	
	@FXML
	private Label labelTotal;
	
	@FXML
	private Label labelIfoodTotal;
	
	@FXML
	private Label labelDirectOrderTotal;
	
	@FXML
	private Label labelDeliveryTotal;
	
	@FXML
	private Label labelIfoodPaymentValueTotal;
	
	@FXML
	private Label labelIfoodComissionTotal;
	
	@FXML
	private Label labelServiceFeePTotal;
	
	@FXML
	private Label labelServiceFeeTTotal;
	
	@FXML
	private Label labelCashTotal;
	
	@FXML
	private Label labelCardTotal;
	
	@FXML
	private Label labelPixTotal;
	
	public void setResult(Map<String, Double> results) {
		Set<String> fields = results.keySet();
		labelTotal.setText((fields.contains("totalValue") ? String.format("%.2f", results.get("totalValue")) : ""));
		labelIfoodTotal.setText((fields.contains("ifoodTotal") ? String.format("%.2f", results.get("ifoodTotal")) : ""));
		labelDirectOrderTotal.setText((fields.contains("directOrderTotal") ? String.format("%.2f", results.get("directOrderTotal")) : ""));
		labelDeliveryTotal.setText((fields.contains("deliveryTotal") ? String.format("%.2f", results.get("deliveryTotal")) : ""));
		labelIfoodPaymentValueTotal.setText((fields.contains("ifoodPaymentValueTotal") ? String.format("%.2f", results.get("ifoodPaymentValueTotal")) : ""));
		labelIfoodComissionTotal.setText((fields.contains("ifoodComissionTotal") ? String.format("%.2f", results.get("ifoodComissionTotal")) : ""));
		labelServiceFeePTotal.setText((fields.contains("serviceFeePTotal") ? String.valueOf(((Double)results.get("serviceFeePTotal")).intValue()) : ""));
		labelServiceFeeTTotal.setText((fields.contains("serviceFeePTotal") ? String.valueOf(((Double)results.get("serviceFeeTTotal")).intValue()) : ""));
		labelCashTotal.setText((fields.contains("cashTotal") ? String.format("%.2f", results.get("cashTotal")) : ""));
		labelCardTotal.setText((fields.contains("cardTotal") ? String.format("%.2f", results.get("cardTotal")) : ""));
		labelPixTotal.setText((fields.contains("pixTotal") ? String.format("%.2f", results.get("pixTotal")) : ""));
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
	}
}
