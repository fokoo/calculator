package calculator.controller;

import javafx.fxml.FXML;
import javafx.scene.control.ListView;

import java.util.ArrayList;

public class HistoryController {

    @FXML
    private ListView listView;

    public void fillListView(ArrayList<String> historyList){
        if(historyList==null){
            System.out.println("No element in the list");
            return;
        }
        historyList.forEach((history) -> {
             //System.out.println(history);
             listView.getItems().add(history);
        });
    }
}
