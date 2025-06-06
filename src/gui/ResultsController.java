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
	private Label labelPVTotal;
	
	@FXML
	private Label labelDeliveryTotal;
	
	@FXML
	private Label labelForIfoodTotal;
	
	@FXML
	private Label labelTaxTotal;
	
	@FXML
	private Label labelTaxOfServicePTotal;
	
	@FXML
	private Label labelTaxOfServiceTTotal;
	
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
		labelPVTotal.setText((fields.contains("pvTotal") ? String.format("%.2f", results.get("pvTotal")) : ""));
		labelDeliveryTotal.setText((fields.contains("deliveryTotal") ? String.format("%.2f", results.get("deliveryTotal")) : ""));
		labelForIfoodTotal.setText((fields.contains("forIfoodTotal") ? String.format("%.2f", results.get("forIfoodTotal")) : ""));
		labelTaxTotal.setText((fields.contains("taxTotal") ? String.format("%.2f", results.get("taxTotal")) : ""));
		labelTaxOfServicePTotal.setText((fields.contains("taxOfServicePTotal") ? String.valueOf(((Double)results.get("taxOfServicePTotal")).intValue()) : ""));
		labelTaxOfServiceTTotal.setText((fields.contains("taxOfServicePTotal") ? String.valueOf(((Double)results.get("taxOfServiceTTotal")).intValue()) : ""));
		labelCashTotal.setText((fields.contains("cashTotal") ? String.format("%.2f", results.get("cashTotal")) : ""));
		labelCardTotal.setText((fields.contains("cardTotal") ? String.format("%.2f", results.get("cardTotal")) : ""));
		labelPixTotal.setText((fields.contains("pixTotal") ? String.format("%.2f", results.get("pixTotal")) : ""));
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
	}

}
