package gui.util;

import java.util.HashMap;

import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;

public class ImageManager {

	private static final HashMap<String, Image> imageCache = new HashMap<>();
	
	public static void loadImages() {
		String imagesPath = "/gui/gui.resources/images/";
		
		try {
			imageCache.put("add", new Image(ImageManager.class.getResourceAsStream(imagesPath + "plus_sign.png")));
			imageCache.put("reset", new Image(ImageManager.class.getResourceAsStream(imagesPath + "settings_gear.png")));
			imageCache.put("ifoodOrder", new Image(ImageManager.class.getResourceAsStream(imagesPath + "delivery_bike.png")));
			imageCache.put("orderChart", new Image(ImageManager.class.getResourceAsStream(imagesPath + "order_chart.png")));
			imageCache.put("directOrder", new Image(ImageManager.class.getResourceAsStream(imagesPath + "point_of_sale.png")));
			imageCache.put("Order-history", new Image(ImageManager.class.getResourceAsStream(imagesPath + "Order-history.png")));
		}
		catch (Exception e) {
			Alerts.showAlert("", "Erro em carregar arquivos", "", AlertType.ERROR);
		}
	}
	
	public static Image getImage(String key) {
		return imageCache.get(key);
	}
}
