package gui;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.Set;

import gui.util.ImageManager;
import gui.util.Utils;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import model.entities.DTO.OrderDTO;
import model.services.OrderService;

public class ResultsController implements Initializable {
	
	private OrderService service;
	
	@FXML
	private DatePicker dpDate;
	
	@FXML
	private DatePicker dpFinalDate;
	
	@FXML
	private Button btSearch;
	
	@FXML
	private Button btClear;
	
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
	private Label labelServiceFeeTotal;
	
	@FXML
	private Label labelCashTotal;
	
	@FXML
	private Label labelCardTotal;
	
	@FXML
	private Label labelPixTotal;
	
	@FXML
	public void onBtSearchAction() {
		LocalDate startDate = dpDate.getValue();
		LocalDate finalDate = dpFinalDate.getValue();
		
		if(dpFinalDate.getEditor().getText().trim().isEmpty()) {
			finalDate = startDate;
			dpFinalDate.setValue(finalDate);
		}
		
		setResult(service.getTotalsByDate(startDate, finalDate));
	}
	
	private void setResult(OrderDTO dto) {
		labelTotal.setText("R$ " + dto.getTotalValue().toString());
		labelIfoodTotal.setText("R$ " + dto.getIfoodOrderTotalValue().toString());
		labelDirectOrderTotal.setText("R$ " + dto.getDirectOrderTotalValue().toString());
		labelDeliveryTotal.setText("R$ " + dto.getDeliveryValue());
		labelIfoodPaymentValueTotal.setText("R$ " + dto.getIfoodPaymentValue());
		labelIfoodComissionTotal.setText("RS " + dto.getIfoodComission());
		labelServiceFeeTotal.setText(String.valueOf(((Integer)dto.getServiceFee().intValue())));
		labelCashTotal.setText("R$ " + dto.getCashTotalValue().toString());
		labelCardTotal.setText("R$ " + dto.getCardTotalValue().toString());
		labelPixTotal.setText("R$ " + dto.getPixTotalValue().toString());
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		Utils.formatDatePicker(dpDate, "dd/MM/yyyy");
		Utils.formatDatePicker(dpFinalDate, "dd/MM/yyyy");
		initializeResources();
	}
	
	private void initializeResources() {
		ImageView search_sign = new ImageView(ImageManager.getImage("searchSign"));
		
		
		search_sign.setFitWidth(23);
		search_sign.setFitHeight(23);
		
		btSearch.setGraphic(search_sign);
	}

	public OrderService getService() {
		return service;
	}

	public void setService(OrderService service) {
		this.service = service;
	}
}
