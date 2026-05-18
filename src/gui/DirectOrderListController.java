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
import model.entities.DirectOrder;
import model.entities.Order;
import model.entities.enums.Type;
import model.exceptions.DbException;
import model.services.OrderService;

public class DirectOrderListController implements Initializable, DataChangeListener {
	
	private OrderService service;
	
	@FXML
	private DatePicker dpDate;
	
	@FXML
	private Button btSearch;
	
	@FXML
	private Button btclear;
	
	@FXML
	private TableView<DirectOrder> tableViewDirectOrder;
	
	@FXML
	private TableColumn<DirectOrder, Integer> tableColumnIndex;
	
	@FXML
	private TableColumn<DirectOrder, BigDecimal> tableColumnOrderValue;
	
	@FXML
	private TableColumn<DirectOrder, BigDecimal> tableColumnDeliveryValue;
	
	@FXML
	private TableColumn<DirectOrder, String> tableColumnPaymentMethod;
	
	@FXML
	private TableColumn<DirectOrder, DirectOrder> tableColumnRemoveButtons;
	
	@FXML
	private TableColumn<DirectOrder, DirectOrder> tableColumnEditButtons;
	
	private ObservableList<DirectOrder> obsList;
	
	private List<DirectOrder> list = new ArrayList<>();
	
	@FXML
	private Button btNew;
	
	@FXML
	public void onBtNewAction(ActionEvent event) {
		Stage parentStage = Utils.currentStage(event);
		DirectOrder obj = new DirectOrder(new Order());
		dialogForm(obj, "/gui/DirectOrderDialogForm.fxml", parentStage);
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
		List<Order> orders = service.findByTypeAndDate(Type.VIA_PEDIDO_DIRETO, date);

		list = orders.stream().map(order -> (DirectOrder) order).collect(Collectors.toList());
		
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
	
	private void initializeNodes() {
		Utils.formatDatePicker(dpDate, "dd/MM/yyyy");
		initializeTable();
		initializeResources();
	}
	
	private void initializeTable() {
		Utils.formatTableColumnRowAsIndex(tableColumnIndex);
		tableColumnOrderValue.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getOrderValue()));
		Utils.formatTableColumnBigDecimal(tableColumnOrderValue, 2);
		tableColumnDeliveryValue.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue().getDeliveryValue()));
		Utils.formatTableColumnBigDecimal(tableColumnDeliveryValue, 2);
		tableColumnPaymentMethod.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getPaymentMethod().name()));
		Utils.formatTableColumnStringCamelCase(tableColumnPaymentMethod);
		
		Stage stage = (Stage)Main.getMainScene().getWindow();
		tableViewDirectOrder.prefHeightProperty().bind(stage.heightProperty());
	}
	
	private void initializeResources() {
		ImageView plusIcon = new ImageView(ImageManager.getImage("add"));
		ImageView searchIcon = new ImageView(ImageManager.getImage("searchIcon"));
		
		plusIcon.setFitWidth(23);
		plusIcon.setFitHeight(23);
		
		searchIcon.setFitWidth(23);
		searchIcon.setFitHeight(23);
		
		btNew.setGraphic(plusIcon);
		btSearch.setGraphic(searchIcon);
	}
	
	public void updateTableView() {
		obsList = FXCollections.observableArrayList(list);
		tableViewDirectOrder.setItems(obsList);
		initRemoveButtons();
		initEditButtons();
	}
	
	private void dialogForm(DirectOrder obj, String absoluteView, Stage parentStage) {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource(absoluteView));
			Pane pane = loader.load();
			
			DirectOrderFormController controller = loader.getController();
			controller.setDirectOrderService(new OrderService());
			controller.subscribeDataChangeListener(this);
			controller.setDirectOrder(obj);
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

	private void removeEntity(DirectOrder obj) {
		Optional<ButtonType> result = Alerts.showConfirmation("Removendo Pedido", "Tem certeza que deseja apagar o pedido?");
		if(result.get() == ButtonType.OK) {
			if (service == null) {
				throw new IllegalStateException("Serivce was null");
			}
			try {
				list.remove(obj);
				service.delete(obj);
				Alerts.showAlert("Sucesso", "Pedido removido com sucesso", null, AlertType.INFORMATION);
				updateTableView();
			}
			catch (DbException e) {
				Alerts.showAlert("Error removing object", null, e.getMessage(), AlertType.ERROR);
			}
		}
	}

	private void initRemoveButtons() {
		tableColumnRemoveButtons.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnRemoveButtons.setCellFactory(param -> new TableCell<DirectOrder, DirectOrder>() {
			private final Button button = new Button("Remover");

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
	
	private void initEditButtons() {
		tableColumnEditButtons.setCellValueFactory(param -> new ReadOnlyObjectWrapper<>(param.getValue()));
		tableColumnEditButtons.setCellFactory(param -> new TableCell<DirectOrder, DirectOrder>() {
			private final Button button = new Button("Editar");

			@Override
			protected void updateItem(DirectOrder obj, boolean empty) {
				super.updateItem(obj, empty);
				if (obj == null) {
					setGraphic(null);
					return;
				}
				setGraphic(button);
				button.setOnAction(event -> dialogForm(obj, "/gui/DirectOrderDialogForm.fxml", Utils.currentStage(event)));
			}
		});
	}
}
