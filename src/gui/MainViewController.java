package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
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
import model.services.DirectOrderService;
import model.services.IfoodOrderService;

public class MainViewController implements Initializable {
	
	private List<DataChangeListener> dataChangeListeners = new ArrayList<>();
	
	@FXML
	private VBox contentHolder;
	
	private IfoodOrderService ifoodOrderService;
	
	private DirectOrderService directOrderService;

	@FXML
	private Button btIfoodOrder;
	
	@FXML
	private Button btDirectOrder;
	
	@FXML
	private Button btResult;
	
	@FXML
	private Button btReset;
	
	@FXML
	private Label screenInformation;
	
	@FXML
	public void onBtIfoodOrderAction() {
		loadView("/gui/IfoodOrderListView.fxml", (IfoodOrderListController controller) -> {
			controller.setService(new IfoodOrderService());
			controller.updateTableView();
		}, "Ifood");
	}
	
	@FXML
	public void onBtDirectOrderAction() {
		loadView("/gui/DirectOrderListView.fxml", (DirectOrderListController controller) -> {
			controller.setService(new DirectOrderService());
			controller.updateTableView();
		}, "PV");	}
	
	@FXML
	public void onBtResultAction() {
		totalScreen(result(), "/gui/ResultsView.fxml");
	}
	
	@FXML
	public void onBtResetAllAction() {
		 Optional<ButtonType> reset = Alerts.showConfirmation("Redefinir Dados", "Tem certeza que deseja apagar todos os dados de Ifood e PV?");
		    if (reset.isPresent() && reset.get() == ButtonType.OK) {
		        ifoodOrderService.resetAll();
		        directOrderService.resetAll();
		        Alerts.showAlert("Sucesso", null, "Todos os dados foram redefinidos.", AlertType.INFORMATION);
		        notifyDataChangeListeners();
		    }
	}
	
	public synchronized <T> void loadView(String absoluteView, Consumer<T> consumer, String channel) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteView));
			VBox newVbox = loader.load();

			contentHolder.getChildren().clear();
			screenInformation.setText("");
			
			contentHolder.getChildren().addAll(newVbox.getChildren());
			
			if(channel.equals("Ifood")) {
				screenInformation.setText("Pedidos Ifood");
			}
			else if (channel.equals("PV")) {
				screenInformation.setText("Pedidos PV");
			}
			
			T controller = loader.getController();
			if(controller instanceof DataChangeListener) {
				subscribeDataChangeListener((DataChangeListener) controller);
			}
			consumer.accept(controller);
		} catch (IOException e) {
			Alerts.showAlert("IO Exception", "Error in Loading view", e.getMessage(), AlertType.ERROR);
		}
	}
	

	private void totalScreen(Map<String, Double> result, String absoluteView) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteView));
			VBox pane = loader.load();
			
			ResultsController controller = loader.getController();
			controller.setResult(result);
			
			contentHolder.getChildren().clear();
			screenInformation.setText("");
			
			contentHolder.getChildren().addAll(pane.getChildren());
			screenInformation.setText("Balanço do dia");
			
		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IOException", "Error in loading view", e.getMessage(), AlertType.ERROR);
		}
	}
	
	public Map<String, Double> result(){
		Map<String, Double> result = ifoodOrderService.total();
		Map<String, Double> directOrder = directOrderService.total();
		
		for (String key : directOrder.keySet()) {
			double value = 0.00;
			if (result.containsKey(key)) {
				value += result.get(key);
				value += directOrder.get(key);
				result.put(key, value);
			}
			else {
				result.put(key, directOrder.get(key));
			}
		}
		
		return result;
	}
	
	public void setIfoodOrderService(IfoodOrderService service) {
		this.ifoodOrderService = service;
	}
	
	public void setDirectOrderService(DirectOrderService service) {
		this.directOrderService = service;
	}
	
	private void notifyDataChangeListeners() {
		for (DataChangeListener listener : dataChangeListeners) {
			listener.dataChangeListeners();
		}
	}
	
	public void subscribeDataChangeListener(DataChangeListener listener) {
		if(!dataChangeListeners.contains(listener)) {
			dataChangeListeners.add(listener);
		}
	}
	

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		ImageView delivery_bike = new ImageView(ImageManager.getImage("ifoodOrder"));
		ImageView order_chart = new ImageView(ImageManager.getImage("orderChart"));
		ImageView point_of_sale = new ImageView(ImageManager.getImage("directOrder"));
		ImageView reset = new ImageView(ImageManager.getImage("reset"));
		
		delivery_bike.setFitHeight(32);
		delivery_bike.setFitWidth(32);
		
		order_chart.setFitHeight(32);
		order_chart.setFitWidth(32);
		
		point_of_sale.setFitHeight(32);
		point_of_sale.setFitWidth(32);
		
		reset.setFitHeight(23);
		reset.setFitWidth(23);
		
		btDirectOrder.setGraphic(point_of_sale);
		btIfoodOrder.setGraphic(delivery_bike);
		btReset.setGraphic(reset);
		btResult.setGraphic(order_chart);
	}
}
