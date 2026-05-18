package gui.util;

import java.util.HashMap;

import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;

public class ImageManager {

	private static final HashMap<String, Image> imageCache = new HashMap<>();
	
	public static void loadImages() {
		String imagesPath = "/gui/gui.resources/images/";
		try {
			imageCache.put("Order-history", new Image(ImageManager.class.getResourceAsStream(imagesPath + "Order-history.png")));
			imageCache.put("add", new Image(ImageManager.class.getResourceAsStream(imagesPath + "plus_icon.png")));
			imageCache.put("reset", new Image(ImageManager.class.getResourceAsStream(imagesPath + "settings_gear.png")));
			imageCache.put("ifoodOrder", new Image(ImageManager.class.getResourceAsStream(imagesPath + "delivery_bike.png")));
			imageCache.put("orderChart", new Image(ImageManager.class.getResourceAsStream(imagesPath + "order_chart.png")));
			imageCache.put("directOrder", new Image(ImageManager.class.getResourceAsStream(imagesPath + "point_of_sale_white.png")));
			imageCache.put("searchIcon", new Image(ImageManager.class.getResourceAsStream(imagesPath + "search_icon.png")));
			imageCache.put("saveIcon", new Image(ImageManager.class.getResourceAsStream(imagesPath + "save_icon.png")));
			imageCache.put("deliveryBike", new Image(ImageManager.class.getResourceAsStream(imagesPath + "delivery_bike_black.png")));
			imageCache.put("cashIcon", new Image(ImageManager.class.getResourceAsStream(imagesPath + "cash_icon.png")));;
			imageCache.put("cardIcon", new Image(ImageManager.class.getResourceAsStream(imagesPath + "card_icon.png")));;
			imageCache.put("pixIcon", new Image(ImageManager.class.getResourceAsStream(imagesPath + "pix_icon.png")));;
			imageCache.put("ifoodIcon", new Image(ImageManager.class.getResourceAsStream(imagesPath + "ifood_icon.png")));
			imageCache.put("directOrderIcon", new Image(ImageManager.class.getResourceAsStream(imagesPath + "point_of_sale_black.png")));
			imageCache.put("ifoodPaymentIcon", new Image(ImageManager.class.getResourceAsStream(imagesPath + "shopping_bag_icon.png")));
			imageCache.put("ifoodComission", new Image(ImageManager.class.getResourceAsStream(imagesPath + "percent_icon.png")));
			imageCache.put("serviceFee", new Image(ImageManager.class.getResourceAsStream(imagesPath + "account_circle_icon.png")));
			imageCache.put("totalValue", new Image(ImageManager.class.getResourceAsStream(imagesPath + "attach_money.png")));
		}
		catch (Exception e) {
			Alerts.showAlert("", "Erro em carregar arquivos", "", AlertType.ERROR);
		}
	}
	
	public static Image getImage(String key) {
		return imageCache.get(key);
	}
}
