package gui;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import application.Main;
import gui.util.Alerts;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import model.services.IfoodService;
import model.services.PVService;

public class MainViewController implements Initializable {
	
	private IfoodService service;

	@FXML
	private MenuItem menuItemIfood;
	
	@FXML
	private MenuItem menuItemPV;
	
	@FXML
	private MenuItem menuItemResult;
	
	@FXML
	public void onMenuItemIfoodAction() {
		loadView("/gui/IfoodListView.fxml", (IfoodListController controller) -> {
			controller.setService(new IfoodService());
			controller.updateTableView();
		});
	}
	
	@FXML
	public void onMenuItemPVAction() {
		loadView("/gui/PVListView.fxml", (PVListController controller) -> {
			controller.setService(new PVService());
			controller.updateTableView();
		});	}
	
	@FXML
	public void onMenuItemResultAction() {
		totalScreen(result(), "/gui/IfoodTotalView.fxml");
	}
	
	public synchronized <T> void loadView(String absoluteView, Consumer<T> consumer) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteView));
			VBox newVBox = loader.load();

			Scene mainScene = Main.getMainScene();
			VBox mainVbox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();

			Node mainMenu = mainVbox.getChildren().get(0);
			mainVbox.getChildren().clear();
			mainVbox.getChildren().add(mainMenu);
			mainVbox.getChildren().addAll(newVBox.getChildren());
			T controller = loader.getController();
			consumer.accept(controller);
		} catch (IOException e) {
			Alerts.showAlert("IO Exception", "Error in Loading view", e.getMessage(), AlertType.ERROR);
		}
	}
	

	private void totalScreen(Map<String, Double> result, String absoluteView) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteView));
			Pane pane = loader.load();
			
			IfoodTotalController controller = loader.getController();
			controller.setResult(result);
			
			Scene mainScene = Main.getMainScene();
			VBox mainVbox = (VBox) ((ScrollPane) mainScene.getRoot()).getContent();
			
			Node mainMenu = mainVbox.getChildren().get(0);
			mainVbox.getChildren().clear();
			mainVbox.getChildren().add(mainMenu);
			mainVbox.getChildren().addAll(pane.getChildren());
			
		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IOException", "Error in loading view", e.getMessage(), AlertType.ERROR);
		}
	}
	
	public Map<String, Double> result(){
		Map<String, Double> result = service.total();
		return result;
	}
	
	public void setIfoodService(IfoodService service) {
		this.service = service;
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
	}
}
