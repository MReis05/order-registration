package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import gui.util.Alerts;
import gui.util.ImageManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import model.services.OrderService;

public class MainViewController implements Initializable {
	
	@FXML
	private VBox contentHolder;

	@FXML
	private Button btIfoodOrder;
	
	@FXML
	private Button btDirectOrder;
	
	@FXML
	private Button btResult;
	
	@FXML
	private Label screenInformation;
	
	@FXML
	public void onBtIfoodOrderAction() {
		loadView("/gui/IfoodOrderListView.fxml", (IfoodOrderListController controller) -> {
			controller.setService(new OrderService());
			controller.onBtSearchAction();
		}, "Ifood");
	}
	
	@FXML
	public void onBtDirectOrderAction() {
		loadView("/gui/DirectOrderListView.fxml", (DirectOrderListController controller) -> {
			controller.setService(new OrderService());
			controller.onBtSearchAction();
		}, "PV");	}
	
	@FXML
	public void onBtResultAction() {
		loadView("/gui/ResultsView.fxml", (ResultsController controller) ->{
			controller.setService(new OrderService());
		}, "Balanço");
	}
	
	public synchronized <T> void loadView(String absoluteView, Consumer<T> consumer, String channel) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteView));
			Node newView = loader.load(); 

			contentHolder.getChildren().clear();
			screenInformation.setText("");

			contentHolder.getChildren().add(newView);
			
			if(newView instanceof AnchorPane) {
				AnchorPane.setBottomAnchor((AnchorPane)newView, 0.00);
				AnchorPane.setLeftAnchor((AnchorPane) newView, 0.00);
				AnchorPane.setRightAnchor((AnchorPane) newView, 0.00);
				AnchorPane.setTopAnchor((AnchorPane) newView, 0.00);
			}
			
			VBox.setVgrow(newView, Priority.ALWAYS);
			
			switch(channel) {
			case "Ifood":
				screenInformation.setText("Pedidos Ifood");
				break;
			case "PV":
				screenInformation.setText("Pedidos PV");
				break;
			case "Balanço":
				screenInformation.setText("Balanço do dia");
				break;
			}
			T controller = loader.getController();
			consumer.accept(controller);
		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IO Exception", "Error in Loading view", e.getMessage(), AlertType.ERROR);
		}
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		ImageView deliveryBike = new ImageView(ImageManager.getImage("ifoodOrder"));
		ImageView orderChart = new ImageView(ImageManager.getImage("orderChart"));
		ImageView pointOfSale = new ImageView(ImageManager.getImage("directOrder"));
		
		deliveryBike.setFitHeight(32);
		deliveryBike.setFitWidth(32);
		
		orderChart.setFitHeight(32);
		orderChart.setFitWidth(32);
		
		pointOfSale.setFitHeight(32);
		pointOfSale.setFitWidth(32);
		
		btDirectOrder.setGraphic(pointOfSale);
		btIfoodOrder.setGraphic(deliveryBike);
		btResult.setGraphic(orderChart);
	}
}
