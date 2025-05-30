package gui;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import application.Main;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import model.entities.Ifood;
import model.services.IfoodService;

public class IfoodListController implements Initializable {
	
	private IfoodService service;
	
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
	
	private ObservableList<Ifood> obsList;
	
	@FXML
	private Button btNew;
	
	@FXML
	public void onBtNewAction() {
		System.out.println("onBtNewAction");
	}

	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializaNodes();
	}
	
	public void initializaNodes() {
		tableColumnId.setCellValueFactory(new PropertyValueFactory<>("id"));
		tableColumnOrderValue.setCellValueFactory(new PropertyValueFactory<>("OrderValue"));
		Utils.formatTableColumnDouble(tableColumnOrderValue, 2);
		tableColumnDeliveryValue.setCellValueFactory(new PropertyValueFactory<>("DeliveryValue"));
		Utils.formatTableColumnDouble(tableColumnOrderValue, 2);
		tableColumnTax.setCellValueFactory(new PropertyValueFactory<>("Tax"));
		Utils.formatTableColumnDouble(tableColumnDeliveryValue, 2);
		tableColumnForIfood.setCellValueFactory(new PropertyValueFactory<>("ForIfood"));
		Utils.formatTableColumnDouble(tableColumnForIfood, 2);
		tableColumnPaymentType.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getPayment().getPaymentMethod()));
		tableColumnPaymentValue.setCellValueFactory(cellData -> new SimpleDoubleProperty(cellData.getValue().getPayment().getPaymentValue()).asObject());
		Utils.formatTableColumnDouble(tableColumnPaymentValue, 2);
		
		Stage stage = (Stage)Main.getMainScene().getWindow();
		tableViewIfood.prefHeightProperty().bind(stage.heightProperty());
	}
	
	public void updateTableView() {
		if(service == null) {
			throw new IllegalStateException();
		}
		List<Ifood> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewIfood.setItems(obsList);
	}

	public IfoodService getService() {
		return service;
	}

	public void setService(IfoodService service) {
		this.service = service;
	}

}
