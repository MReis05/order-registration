package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.ImageManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
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
			VBox newVbox = loader.load();

			contentHolder.getChildren().clear();
			screenInformation.setText("");
			
			contentHolder.getChildren().addAll(newVbox.getChildren());
			
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
		ImageView delivery_bike = new ImageView(ImageManager.getImage("ifoodOrder"));
		ImageView order_chart = new ImageView(ImageManager.getImage("orderChart"));
		ImageView point_of_sale = new ImageView(ImageManager.getImage("directOrder"));
		
		delivery_bike.setFitHeight(32);
		delivery_bike.setFitWidth(32);
		
		order_chart.setFitHeight(32);
		order_chart.setFitWidth(32);
		
		point_of_sale.setFitHeight(32);
		point_of_sale.setFitWidth(32);
		
		btDirectOrder.setGraphic(point_of_sale);
		btIfoodOrder.setGraphic(delivery_bike);
		btResult.setGraphic(order_chart);
	}
}
