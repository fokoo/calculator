package calculator;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;



/*
 Autor: Foko Fotso, Serge
 */

public class Main extends Application {

    private static Stage historyStage = null;

    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("resources/calculator.fxml"));
        primaryStage.setTitle("My First Calculator");
        primaryStage.setScene(new Scene(root, 300, 275));
        primaryStage.show();

        startHistoryStage();
    }

    public void startHistoryStage(){
        historyStage = new Stage();
        historyStage.initModality(Modality.WINDOW_MODAL);
        historyStage.setTitle("History's window");
        //historyStage.setScene(new Scene());
//        historyStage.setX(400);
//        historyStage.setY(400);
    }

    public static Stage getHistoryStage(){
        return historyStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
