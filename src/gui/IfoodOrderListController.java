package gui;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import application.Main;
import gui.listeners.DataChangeListener;
import gui.util.Alerts;
import gui.util.ImageManager;
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
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import model.entities.IfoodOrder;
import model.entities.Order;
import model.entities.enums.Type;
import model.exceptions.DbException;
import model.services.OrderService;

public class IfoodOrderListController implements Initializable, DataChangeListener {

	private OrderService service;

	@FXML
	private DatePicker dpDate;
	
	@FXML
	private Button btSearch;
	
	@FXML
	private Button btClear;
	
	@FXML
	private TableView<IfoodOrder> tableViewIfoodOrder;

	@FXML
	private TableColumn<IfoodOrder, Integer> tableColumnIndex;

	@FXML
	private TableColumn<IfoodOrder, BigDecimal> tableColumnOrderValue;

	@FXML
	private TableColumn<IfoodOrder, BigDecimal> tableColumnDeliveryValue;

	@FXML
	private TableColumn<IfoodOrder, String> tableColumnPaymentMethod;

	@FXML
	private TableColumn<IfoodOrder, IfoodOrder> tableColumnRemoveButtons;
	
	@FXML
	private TableColumn<IfoodOrder, IfoodOrder> tableColumnEditButtons;

	private ObservableList<IfoodOrder> obsList;
	
	private List<IfoodOrder> list = new ArrayList<>();
	
	@FXML
	private Button btNew;

	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		IfoodOrder obj = new IfoodOrder(new Order());
		dialogForm(obj, "/gui/IfoodOrderDialogForm.fxml", parentStage);
	}
	
	@FXML
	public void onBtSearchAction() {
		LocalDate date;
		if(dpDate.getValue() != null) {
			date = dpDate.getValue();
		}
		else {
			date = LocalDate.now();
		}
		List<Order> orders = service.findByTypeAndDate(Type.VIA_IFOOD, date);

		list = orders.stream().map(order -> (IfoodOrder) order).collect(Collectors.toList());
		                                  
		updateTableView();
	}
	
	@FXML
	public void onBtClearAction() {
		obsList.clear();
		updateTableView();
	}
	
	@Override
	public void initialize(URL url, ResourceBundle rb) {
		initializeNodes();
	}

	public void initializeNodes() {
		initializeTable();
		initializeResources();
		Utils.formatDatePicker(dpDate, "dd/MM/yyyy");
	}
	
	private void initializeTable() {
		Utils.formatTableColumnRowAsIndex(tableColumnIndex);
		tableColumnOrderValue.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getOrderValue()));
		Utils.formatTableColumnBigDecimal(tableColumnOrderValue, 2);
		tableColumnDeliveryValue.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getDeliveryValue()));
		Utils.formatTableColumnBigDecimal(tableColumnDeliveryValue, 2);
		tableColumnPaymentMethod.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getPaymentMethod().name()));
		Utils.formatTableColumnStringCamelCase(tableColumnPaymentMethod);
		
		Stage stage = (Stage) Main.getMainScene().getWindow();
		tableViewIfoodOrder.prefHeightProperty().bind(stage.heightProperty());
	}
	
	private void initializeResources() {
		ImageView plus_sign = new ImageView(ImageManager.getImage("add"));
		ImageView search_sign = new ImageView(ImageManager.getImage("searchSign"));
		
		plus_sign.setFitHeight(23);
		plus_sign.setFitWidth(23);
		
		search_sign.setFitHeight(23);
		search_sign.setFitWidth(23);
		
		btNew.setGraphic(plus_sign);
		btSearch.setGraphic(search_sign);
	}

	public void updateTableView() {
		obsList = FXCollections.observableArrayList(list);
		tableViewIfoodOrder.setItems(obsList);
		initRemoveButtons();
		initEditButtons();
	}

	private void dialogForm(IfoodOrder obj, String absoluteView, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteView));
			Pane pane = loader.load();

			IfoodOrderFormController controller = loader.getController();
			controller.setIfoodOrderService(new OrderService());
			controller.subscribeDataChangeListener(this);
			controller.setIfoodOrder(obj);
			controller.updateFormData();

			Stage dialogStage = new Stage();
			dialogStage.setTitle("Entre com os dados do pedido");
			dialogStage.setScene(new Scene(pane));
			dialogStage.getScene().getStylesheets().add(getClass().getResource("/application/application.css").toExternalForm());
			dialogStage.getIcons().add(ImageManager.getImage("Order-history"));
			dialogStage.setResizable(false);
			dialogStage.initOwner(parentStage);
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.showAndWait();

		} catch (IOException e) {
			e.printStackTrace();
			Alerts.showAlert("IOException", "Error in loading view", e.getMessage(), AlertType.ERROR);
		}
	}

	public OrderService getService() {
		return service;
	}

	public void setService(OrderService service) {
		this.service = service;
	}

	@Override
	public void dataChangeListeners() {
		onBtSearchAction();
		updateTableView();

	}

	private void removeEntity(IfoodOrder obj) {
		Optional<ButtonType> delete = Alerts.showConfirmation("Removendo Pedido",
				"Tem certeza que deseja apagar o pedido?");
		if (delete.get() == ButtonType.OK) {
			if (service == null) {
				throw new IllegalStateException("Serivce was null");
			}
			try {
				list.remove(obj);
				service.delete(obj);
				Alerts.showAlert("Sucesso", "Pedido removido com sucesso", null, AlertType.INFORMATION);
				updateTableView();
			} catch (DbException e) {
				Alerts.showAlert("Error removing object", null, e.getMessage(), AlertType.ERROR);
			}
		}
	}
	
	private void initRemoveButtons() {
		tableColumnRemoveButtons.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnRemoveButtons.setCellFactory(param -> new TableCell<IfoodOrder, IfoodOrder>() {
			private final Button button = new Button("remover");
			

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
	
	private void initEditButtons() {
		tableColumnEditButtons.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEditButtons.setCellFactory(param -> new TableCell<IfoodOrder, IfoodOrder>() {
			private final Button button = new Button("Editar");

			@Override
			protected void updateItem(IfoodOrder obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> dialogForm(obj, "/gui/IfoodOrderDialogForm.fxml", Utils.currentStage(event)));
			}
		});
	}
}
