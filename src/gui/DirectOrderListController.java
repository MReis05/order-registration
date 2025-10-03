package gui;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import application.Main;
import db.DbException;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.Utils;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.Order;
import model.entities.DirectOrder;
import model.services.DirectOrderService;

public class DirectOrderListController implements Initializable, DataChangeListener {
	
	private DirectOrderService service;
	
	@FXML
	private TableView<DirectOrder> tableViewDirectOrder;
	
	@FXML
	private TableColumn<DirectOrder, Integer> tableColumnId;
	
	@FXML
	private TableColumn<DirectOrder, Double> tableColumnOrderValue;
	
	@FXML
	private TableColumn<DirectOrder, Double> tableColumnDeliveryValue;
	
	@FXML
	private TableColumn<DirectOrder, String> tableColumnPaymentMethod;
	
	@FXML
	private TableColumn<DirectOrder, DirectOrder> tableColumnRemove;
	
	private ObservableList<DirectOrder> obsList;
	
	@FXML
	private Button btNew;
	
	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		DirectOrder obj = new DirectOrder(new Order());
		dialogForm(obj, "/gui/DirectOrderDialogForm.fxml", parentStage);
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
		Utils.formatTableColumnDouble(tableColumnDeliveryValue, 2);
		tableColumnPaymentMethod.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getPayment().getPaymentMethod()));
		
		Stage stage = (Stage)Main.getMainScene().getWindow();
		tableViewDirectOrder.prefHeightProperty().bind(stage.heightProperty());
	}
	
	public void updateTableView() {
		if(service == null) {
			throw new IllegalStateException();
		}
		List<DirectOrder> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewDirectOrder.setItems(obsList);
		initRemoveButtons();
	}
	
	private void dialogForm(DirectOrder obj, String absoluteView, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteView));
			Pane pane = loader.load();
			
			DirectOrderFormController controller = loader.getController();
			controller.setDirectOrderService(new DirectOrderService());
			controller.subscribeDataChangeListener(this);
			controller.setDirectOrder(obj);
			controller.updateFormData();
			
			Stage dialogStage = new Stage();
			dialogStage.setTitle("Entre com os dados do pedido");
			dialogStage.setScene(new Scene(pane));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();
			
		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IOException", "Error in loading view", e.getMessage(), AlertType.ERROR);
		}
	}

	public DirectOrderService getService() {
		return service;
	}

	public void setService(DirectOrderService service) {
		this.service = service;
	}

	@Override
	public void dataChangeListeners() {
		updateTableView();
	}

	private void removeEntity(DirectOrder obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("Removendo Pedido", "Tem certeza que deseja apagar o pedido?");
		if(result.get() == ButtonType.OK) {
			if (service == null) {
				throw new IllegalStateException("Serivce was null");
			}
			try {
				service.delete(obj);
				updateTableView();
			}
			catch (DbException e) {
				Alerts.showAlert("Error removing object", null, e.getMessage(), AlertType.ERROR);
			}
		}
	}

	private void initRemoveButtons() {
		tableColumnRemove.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnRemove.setCellFactory(param -> new TableCell<DirectOrder, DirectOrder>() {
			private final Button button = new Button("remove");

			@Override
			protected void updateItem(DirectOrder obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> removeEntity(obj));
			}
		});
	}

}
