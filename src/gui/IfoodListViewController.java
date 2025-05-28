package gui;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import model.entities.Ifood;

public class IfoodListViewController implements Initializable {
	
	@FXML
	private TableView<Ifood> tableViewIfood;
	
	@FXML
	private TableColumn<Ifood, Integer> tableColumnId;
	
	@FXML
	private TableColumn<Ifood, Double> tableColumnOrderValue;
	
	@FXML
	private TableColumn<Ifood, Double> tableColumnDeliveryValue;
	
	@FXML
	private TableColumn<Ifood, Double> tableColumnTax;
	
	@FXML
	private TableColumn<Ifood, Double> tableColumnForIfood;
	
	@FXML
	private TableColumn<Ifood, String> tableColumnPaymentType;
	
	@FXML
	private TableColumn<Ifood, Double> tableColumnPaymentValue;
	
	@FXML
	private Button btNew;
	
	@FXML
	public void onBtNewAction() {
		System.out.println("onBtNewAction");
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
	}

}
