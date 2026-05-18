package gui;

import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.TemporalAdjusters;
import java.util.ResourceBundle;

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
	private Button btToday;
	
	@FXML
	private Button btYesterday;
	
	@FXML
	private Button btThisWeek;
	
	@FXML
	private Button btLastWeek;
	
	@FXML
	private Button btThisMonth;
	
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
	private Label imageCash;
	
	@FXML
	private Label imageCard;
	
	@FXML
	private Label imagePix;
	
	@FXML
	private Label imageDelivery;
	
	@FXML
	private Label imageIfood;
	
	@FXML
	private Label imageDirectOrder;
	
	@FXML
	private Label imageIfoodPayment;
	
	@FXML
	private Label imageIfoodComission;
	
	@FXML
	private Label imageServiceFee;
	
	@FXML
	private Label imageTotalValue;
	
	@FXML
	public void onBtSearchAction() {
		LocalDate startDate = dpDate.getValue();
		LocalDate finalDate = dpFinalDate.getValue();
		
		if (startDate != null && finalDate != null) {
	        setResult(service.getTotalsByDate(startDate, finalDate));
	    }
	}
	
	@FXML
	public void onBtClearAction() {
		dpDate.getEditor().clear();
		dpFinalDate.getEditor().clear();
		
		labelTotal.setText("R$ 0,00");
		labelIfoodTotal.setText("R$ 0,00");
		labelDirectOrderTotal.setText("R$ 0,00");
		labelCashTotal.setText("R$ 0,00");
		labelCardTotal.setText("R$ 0,00");
		labelDeliveryTotal.setText("R$ 0,00");
		labelIfoodComissionTotal.setText("R$ 0,00");
		labelIfoodPaymentValueTotal.setText("R$ 0,00");
		labelServiceFeeTotal.setText("R$ 0,00");
		labelPixTotal.setText("R$ 0,00");
	}
	
	@FXML
	public void onBtTodayAction(){
		dpDate.setValue(LocalDate.now());
		dpFinalDate.setValue(LocalDate.now());
		onBtSearchAction();
	}
	
	@FXML
	public void onBtYesterdayAction() {
		dpDate.setValue(LocalDate.now().minusDays(1));
		dpFinalDate.setValue(LocalDate.now().minusDays(1));
		onBtSearchAction();
	}
	
	
	@FXML
	public void onBtLastWeekAction() {
		LocalDate lastWeek = LocalDate.now().minusWeeks(1);
		
		LocalDate monday = lastWeek.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
		LocalDate sunday = lastWeek.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
		
		dpDate.setValue(monday);
		dpFinalDate.setValue(sunday);
		
		onBtSearchAction();
	}
	
	@FXML
	public void onBtThisWeekAction() {
		LocalDate today = LocalDate.now();
		
		LocalDate monday = today.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
		LocalDate sunday = today.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
		
		dpDate.setValue(monday);
		dpFinalDate.setValue(sunday);
		
		onBtSearchAction();
	}
	
	@FXML
	public void onBtThisMonthAction() {
		LocalDate today = LocalDate.now();
		
		LocalDate firstDay = today.with(TemporalAdjusters.firstDayOfMonth());
		LocalDate LastDay = today.with(TemporalAdjusters.lastDayOfMonth());
		
		dpDate.setValue(firstDay);
		dpFinalDate.setValue(LastDay);
		
		onBtSearchAction();
	}
	
	private void setResult(OrderDTO dto) {
		labelTotal.setText("R$ " + dto.getTotalValue().toString());
		labelIfoodTotal.setText("R$ " + dto.getIfoodOrderTotalValue().toString());
		labelDirectOrderTotal.setText("R$ " + dto.getDirectOrderTotalValue().toString());
		labelDeliveryTotal.setText("R$ " + dto.getDeliveryValue());
		labelIfoodPaymentValueTotal.setText("R$ " + dto.getIfoodPaymentValue().toString());
		labelIfoodComissionTotal.setText("R$ " + dto.getIfoodComission().toString());
		labelServiceFeeTotal.setText("R$ " + dto.getServiceFee().toString());
		labelCashTotal.setText("R$ " + dto.getCashTotalValue().toString());
		labelCardTotal.setText("R$ " + dto.getCardTotalValue().toString());
		labelPixTotal.setText("R$ " + dto.getPixTotalValue().toString());
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		Utils.formatDatePicker(dpDate, "dd/MM/yyyy");
		Utils.formatDatePicker(dpFinalDate, "dd/MM/yyyy");
		initializeResources();
		
		dpDate.valueProperty().addListener((observable, oldValue, newValue) ->{
			if(newValue != null) {
				dpFinalDate.setValue(newValue);
			}
		});
	}
	
	private void initializeResources() {
		double size = 23;
		ImageView searchIcon = new ImageView(ImageManager.getImage("searchIcon"));
		ImageView bikeIcon = new ImageView(ImageManager.getImage("deliveryBike"));
		ImageView cashIcon = new ImageView(ImageManager.getImage("cashIcon"));
		ImageView cardIcon = new ImageView(ImageManager.getImage("cardIcon"));
		ImageView pixIcon = new ImageView(ImageManager.getImage("pixIcon"));
		ImageView ifoodIcon = new ImageView(ImageManager.getImage("ifoodIcon"));
		ImageView directOrderIcon = new ImageView(ImageManager.getImage("directOrderIcon"));
		ImageView ifoodPaymentIcon = new ImageView(ImageManager.getImage("ifoodPaymentIcon"));
		ImageView ifoodComissionIcon = new ImageView(ImageManager.getImage("ifoodComission"));
		ImageView serviceFeeIcon = new ImageView(ImageManager.getImage("serviceFee"));
		ImageView totalValue = new ImageView(ImageManager.getImage("totalValue"));
		
		searchIcon.setFitWidth(size);
		searchIcon.setFitHeight(size);
		
		bikeIcon.setFitHeight(size);
		bikeIcon.setFitWidth(size);
		
		cashIcon.setFitHeight(size);
		cashIcon.setFitWidth(size);
		
		cardIcon.setFitHeight(size);
		cardIcon.setFitWidth(size);
		
		pixIcon.setFitHeight(size);
		pixIcon.setFitWidth(size);
		
		ifoodIcon.setFitHeight(size);
		ifoodIcon.setFitWidth(size);
		
		directOrderIcon.setFitHeight(size);
		directOrderIcon.setFitWidth(size);
		
		ifoodPaymentIcon.setFitHeight(size);
		ifoodPaymentIcon.setFitWidth(size);
		
		ifoodComissionIcon.setFitHeight(size);
		ifoodComissionIcon.setFitWidth(size);
		
		serviceFeeIcon.setFitHeight(size);
		serviceFeeIcon.setFitWidth(size);
		
		totalValue.setFitHeight(size + 10);
		totalValue.setFitWidth(size + 10);
		
		btSearch.setGraphic(searchIcon);
		imageCash.setGraphic(cashIcon);
		imageCard.setGraphic(cardIcon);
		imagePix.setGraphic(pixIcon);
		imageDelivery.setGraphic(bikeIcon);
		imageIfood.setGraphic(ifoodIcon);
		imageDirectOrder.setGraphic(directOrderIcon);
		imageIfoodPayment.setGraphic(ifoodPaymentIcon);
		imageIfoodComission.setGraphic(ifoodComissionIcon);
		imageServiceFee.setGraphic(serviceFeeIcon);
		imageTotalValue.setGraphic(totalValue);
	}

	public OrderService getService() {
		return service;
	}

	public void setService(OrderService service) {
		this.service = service;
	}
}
