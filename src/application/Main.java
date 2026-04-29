package application;

import java.io.IOException;

import gui.util.ImageManager;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

public class Main extends Application {

	private static Scene mainScene;

	@Override
	public void start(Stage primaryStage) {
		try {
			loadResources();
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/MainView.fxml"));
			BorderPane borderPane = loader.load();
			mainScene = new Scene(borderPane);
			mainScene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.getIcons().add(ImageManager.getImage("Order-history"));
			primaryStage.setScene(mainScene);
			primaryStage.setTitle("Registro de Pedidos");
			primaryStage.setWidth(800);
	        primaryStage.setHeight(600);
	        primaryStage.setMaximized(true);
			primaryStage.show();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Scene getMainScene() {
		return mainScene;
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	public void loadResources() {
		Font.loadFont(getClass().getResourceAsStream("/gui/gui.resources/fonts/Lato-Regular.ttf"), 10);
		Font.loadFont(getClass().getResourceAsStream("/gui/gui.resources/fonts/Lato-Bold.ttf"), 10);
		Font.loadFont(getClass().getResourceAsStream("/gui/gui.resources/fonts/Montserrat-VariableFont_wght.ttf"), 10);
		
		ImageManager.loadImages();
	}

}
