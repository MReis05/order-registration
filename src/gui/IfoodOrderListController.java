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
import model.entities.IfoodOrder;
import model.entities.Order;
import model.services.IfoodOrderService;

public class IfoodOrderListController implements Initializable, DataChangeListener {

	private IfoodOrderService service;

	@FXML
	private TableView<IfoodOrder> tableViewIfoodOrder;

	@FXML
	private TableColumn<IfoodOrder, Integer> tableColumnId;

	@FXML
	private TableColumn<IfoodOrder, Double> tableColumnOrderValue;

	@FXML
	private TableColumn<IfoodOrder, Double> tableColumnDeliveryValue;

	@FXML
	private TableColumn<IfoodOrder, String> tableColumnCategory;

	@FXML
	private TableColumn<IfoodOrder, String> tableColumnPaymentMethod;

	@FXML
	private TableColumn<IfoodOrder, IfoodOrder> tableColumnRemove;

	private ObservableList<IfoodOrder> obsList;

	@FXML
	private Button btNew;

	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		IfoodOrder obj = new IfoodOrder(new Order());
		dialogForm(obj, "/gui/IfoodOrderDialogForm.fxml", parentStage);
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
		tableColumnPaymentMethod.setCellValueFactory(
				cellData -> new ReadOnlyStringWrapper(cellData.getValue().getPayment().getPaymentMethod()));
		tableColumnCategory.setCellValueFactory(new PropertyValueFactory<>("category"));

		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewIfoodOrder.prefHeightProperty().bind(stage.heightProperty());
	}

	public void updateTableView() {
		if (service == null) {
			throw new IllegalStateException();
		}
		List<IfoodOrder> list = service.findAll();
		obsList = FXCollections.observableArrayList(list);
		tableViewIfoodOrder.setItems(obsList);
		initRemoveButtons();
	}

	private void dialogForm(IfoodOrder obj, String absoluteView, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteView));
			Pane pane = loader.load();

			IfoodOrderFormController controller = loader.getController();
			controller.setIfoodOrderService(new IfoodOrderService());
			controller.subscribeDataChangeListener(this);
			controller.setIfoodOrder(obj);
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

	public IfoodOrderService getService() {
		return service;
	}

	public void setService(IfoodOrderService service) {
		this.service = service;
	}

	@Override
	public void dataChangeListeners() {
		updateTableView();

	}
	

	private void removeEntity(IfoodOrder obj) {
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
		tableColumnRemove.setCellFactory(param -> new TableCell<IfoodOrder, IfoodOrder>() {
			private final Button button = new Button("remove");

			@Override
			protected void updateItem(IfoodOrder obj, boolean empty) {
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
